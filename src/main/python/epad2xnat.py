#!/usr/bin/python

# First rough-and-ready stab at script to take an ePAD DICOM image directory containing DICOM tage files 
# and populate an XNAT project with the subjects and studies it contains.
#
# Example usage:
# epad2xnat.py -x epad-dev1.stanford.edu:8090 -u [user] -p [password] ~/DicomProxy/resources/dicom/

import argparse, os, sys, subprocess, re, requests

xnat_auth_base='/xnat/data/JSESSION'
xnat_project_base='/xnat/data/projects/'
xnat_epad_project_name='EPAD_XNAT'
patient_id_dicom_element_name='Patient\'s Name'

def subject_name_to_xnat_id(subject_name):
  return re.sub('[\^ \-,]', '_', subject_name)

def xnat_login(xnat_base_url, user, password):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  r = requests.post(xnat_auth_url, auth=(user, password))
  jsessionid = r.text
  return jsessionid

def xnat_logout(xnat_base_url, jsessionid):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.delete(xnat_auth_url, cookies=cookies)
  print r.status_code
  
def create_xnat_subject(xnat_epad_project_subject_url, jsessionid, subject_name):
  xnat_subject_id = subject_name_to_xnat_id(subject_name)
  #payload = { 'label': subject_name } # XNAT is very sensitive to label content 
  payload = { }
  xnat_subject_url = xnat_epad_project_subject_url + xnat_subject_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_subject_url, params=payload, cookies=cookies)
  print r.status_code
  print r.text

def create_xnat_subjects(xnat_base_url, jsessionid, subject_study_pairs):
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_epad_project_name
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  
  for subject_name, _ in subject_study_pairs:
    create_xnat_subject(xnat_epad_project_subject_url, jsessionid, subject_name)

def create_xnat_study_experiment(xnat_epad_project_subject_url, jsessionid, subject_name, series_uid):
  xnat_subject_id = subject_name_to_xnat_id(subject_name)
  xnat_experiment_id = series_uid.replace('.', '_') # XNAT does not like periods in its IDs
  payload = { 'label': series_uid, 'xsiType': 'xnat:otherDicomSessionData' }
  xnat_experiment_url = xnat_epad_project_subject_url + xnat_subject_id + '/experiments/' + xnat_experiment_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_experiment_url, params=payload, cookies=cookies)
  print r.status_code

def create_xnat_study_experiments(xnat_base_url, jsessionid, subject_study_pairs):
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_epad_project_name
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  
  for subject_name, study_uid in subject_study_pairs:
    create_xnat_study_experiment(xnat_epad_project_subject_url, jsessionid, subject_name, series_uid)

# Return a list of subject, study_uid pairs.
def process_epad_image_directory(epad_image_directory):
  study_find_args = ['find', epad_image_directory, '-type', 'd', '-mindepth', '1', '-maxdepth', '1']
  subject_study_pairs = []
  
  study_dirs = subprocess.check_output(study_find_args)
  for study_dir in study_dirs.split('\n'):
    if study_dir: # Process directories directly under the base directory (which should contain DICOM study)
      study_uid = os.path.basename(study_dir).replace('_', '.') # ePAD converts . to _ in file names
      # Look for DICOM header files in each directory
      header_file_find_args = ['find', study_dir, '-type', 'f', '-name', '*.tag']
      dicom_header_files = subprocess.check_output(header_file_find_args)
      if dicom_header_files: # We found at least one DICOM header file
        dicom_header_file_path = dicom_header_files.split('\n')[0] # Pick the first (patient ID will be same in all files)
        if dicom_header_file_path: # Find the line containing the patient ID DICOM element. TODO replace with grep and no error code on no match
          dicom_header_file_name = os.path.basename(dicom_header_file_path) 
          id_grep_args = ['find', epad_image_directory, '-name', dicom_header_file_name, '-exec', 'grep', patient_id_dicom_element_name, '{}', ';']
          patient_id_dicom_elements = subprocess.check_output(id_grep_args)
          if patient_id_dicom_elements:
            patient_id_dicom_element = patient_id_dicom_elements.split('\n')[0] # Should only be one
            m = re.match('.+\[(?P<pid>.+)\].+', patient_id_dicom_element)
            if m:
              subject_name = m.group('pid')
              if subject_name:
                subject_study_pairs.append( (subject_name, study_uid) )
              else: 
                print 'Warning: patient ID missing in DICOM element ', patient_id_dicom_element, ' in header file', dicom_header_file_path 
            else:
              print 'Warning: error extracting patient ID from DICOM element ', patient_id_dicom_element, ' in header file', dicom_header_file_path
          else:
            print 'Warning: did not find a patient ID DICOM element in tag file', dicom_header_file_path
      else:
        print "Warning: no DICOM header file found for study", study_uid
  return subject_study_pairs

if __name__ == '__main__':
  parser = argparse.ArgumentParser() 
  parser.add_argument("-x", "--xnat_url", help="XNAT host", required=True)
  parser.add_argument("-u", "--user", help="XNAT user", required=True)
  parser.add_argument("-p", "--password", help="XNAT password", required=True)
  parser.add_argument("epad_image_directory", help="ePAD image directory path")
  args = parser.parse_args()

  xnat_base_url = 'http://' + args.xnat_url
  epad_image_directory = args.epad_image_directory
  user = args.user
  password=args.password

  try:  
    subject_study_pairs = process_epad_image_directory(epad_image_directory)
    
    print 'Found', len(subject_study_pairs), 'subject/study pairs' 
    for subject_name, study_uid in subject_study_pairs:
      print subject_name.ljust(30), study_uid

    jsessionid = xnat_login(xnat_base_url, user, password)
    create_xnat_subjects(xnat_base_url, jsessionid, subject_study_pairs)      
    create_xnat_study_experiments(xnat_base_url, jsessionid, subject_study_pairs)
    xnat_logout(xnat_base_url, jsessionid)
    
  except subprocess.CalledProcessError as e:
    print "Called process error: {0}".format(e.message)
  except AttributeError as e:
    print "Attribute error: {0}".format(e.message)
  except OSError as e:
    print "OS error({0}): {1}".format(e.errno, e.strerror)
  except IOError as e:
    print "IO error({0}): {1}".format(e.errno, e.strerror)
  except NameError as e:
    print "Name error: {0}".format(e.message)
  except RuntimeError as e:
    print "Runtime error({0}): {1}".format(e.errno, e.strerror)
  except UrlError as e:
    print "Runtime error({0}): {1}".format(e.errno, e.strerror)
  except:
    print "Unexpected error:", sys.exc_info()[0]

