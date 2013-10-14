#!/usr/bin/python

# First stab at script to take a pre-XNAT ePAD DICOM image directory containing DICOM header files 
# and to populate an XNAT project with the subjects and studies it contains.
#
# Example usage:
# epad2xnat.py -x epad-dev1.stanford.edu:8090 -u [user] -p [password] ~/DicomProxy/resources/dicom/

import argparse, os, sys, subprocess, re, requests, uuid

xnat_auth_base='/xnat/data/JSESSION'
xnat_project_base='/xnat/data/projects/'
patient_id_dicom_element_name='Patient\'s Name'

def project_name_to_xnat_project_id(project_name):
  return re.sub('[ ,]', '_', project_name) # Replace spaces and commas with underscores

def patient_name_to_xnat_subject_id(patient_name):
  return re.sub('[\^ ,]', '_', patient_name) # Replace ^, spaces, and commas with underscores

def study_uid_to_xnat_experiment_id(study_uid):
  return study_uid.replace('.', '_') # XNAT does not like periods in its IDs

def xnat_login(xnat_base_url, user, password):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  r = requests.post(xnat_auth_url, auth=(user, password))
  if r.status_code == requests.codes.ok:
    jsessionid = r.text
    return jsessionid
  else:
    print 'Error: log in to XNAT failed - status code =', r.status_code
    r.raise_for_status()

def xnat_logout(xnat_base_url, jsessionid):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.delete(xnat_auth_url, cookies=cookies)
  if r.status_code != requests.codes.ok:
    print 'Warning: XNAT logout request failed - status code =', r.status_code
  
def create_xnat_project(xnat_base_url, jsessionid, project_name):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_project_url = xnat_base_url + xnat_project_base  
  payload = { 'ID': xnat_project_id, 'name': project_name } 
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.post(xnat_project_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok: # XNAT returns 200 rather than 201 even if project does not already exist
    print 'Project', project_name, 'created'
  else:
    print 'Warning: failed to create project', project_name, '- status code =', r.status_code
    r.raise_for_status()

def create_xnat_subject(xnat_base_url, jsessionid, project_name, patient_name):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_project_id
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  xnat_subject_id = patient_name_to_xnat_subject_id(patient_name)
  #payload = { 'ID': xnat_subject_id, 'label': xnat_subject_id } # Subject labels are sensitive in XNAT  
  payload = {}
  xnat_subject_url = xnat_epad_project_subject_url + xnat_subject_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_subject_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok:
    print 'Subject', patient_name, 'already exists in XNAT'
  elif r.status_code == requests.codes.created:
    print 'Subject', patient_name, 'created'
  else:
    print 'Warning: failed to create subject', patient_name, 'with id', xnat_subject_id, '- status code =', r.status_code

def create_xnat_subjects(xnat_base_url, jsessionid, project_name, subject_study_pairs):  
  for patient_name, _ in subject_study_pairs:
    create_xnat_subject(xnat_base_url, jsessionid, project_name, patient_name)

def create_xnat_experiment(xnat_base_url, jsessionid, project_name, patient_name, study_uid):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_project_id
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  xnat_subject_id = patient_name_to_xnat_subject_id(patient_name)
  xnat_experiment_id = study_uid_to_xnat_experiment_id(study_uid)
  payload = { 'label': study_uid, 'xsiType': 'xnat:otherDicomSessionData' }
  xnat_experiment_url = xnat_epad_project_subject_url + xnat_subject_id + '/experiments/' + xnat_experiment_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_experiment_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok:
    print 'Experiment for study', study_uid, 'already existed in XNAT'
  elif r.status_code == requests.codes.created:
    print 'Experiment for study', study_uid, 'created'
  else:
    print 'Warning: failed to create experiment for tudy', study_uid, '- status code =', r.status_code

def create_xnat_experiments(xnat_base_url, jsessionid, project_name, subject_study_pairs):
  for patient_name, study_uid in subject_study_pairs:
    create_xnat_experiment(xnat_base_url, jsessionid, project_name, patient_name, study_uid)

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
              patient_name = m.group('pid')
              if patient_name:
                subject_study_pairs.append( (patient_name, study_uid) )
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
  parser.add_argument("-r", "--xnat_project", help="XNAT project", required=True)
  parser.add_argument("-u", "--user", help="XNAT user", required=True)
  parser.add_argument("-p", "--password", help="XNAT password", required=True)
  parser.add_argument("epad_image_directory", help="ePAD image directory path")
  args = parser.parse_args()

  xnat_base_url = 'http://' + args.xnat_url
  xnat_project_name = args.xnat_project
  epad_image_directory = args.epad_image_directory
  user = args.user
  password=args.password

  try:  
    subject_study_pairs = process_epad_image_directory(epad_image_directory)
    
    print 'Found', len(subject_study_pairs), 'subject/study pairs' 
    for patient_name, study_uid in subject_study_pairs:
      print patient_name.ljust(30), study_uid

    patient_name_to_xnat_subject_id_map = {}

    jsessionid = xnat_login(xnat_base_url, user, password)
    create_xnat_project(xnat_base_url, jsessionid, xnat_project_name)
    create_xnat_subjects(xnat_base_url, jsessionid, xnat_project_name, subject_study_pairs)      
    create_xnat_experiments(xnat_base_url, jsessionid, xnat_project_name, subject_study_pairs)
    xnat_logout(xnat_base_url, jsessionid)
    print 'Done.'
    
  except subprocess.CalledProcessError as e:
    print "Called process error:", e
  except AttributeError as e:
    print "Attribute error:;", e
  except OSError as e:
    print "OS error:", e
  except IOError as e:
    print "IO error:", e
  except NameError as e:
    print "Name error:", e
  except RuntimeError as e:
    print "Runtime error:", e
  except:
    print "Unexpected error:", sys.exc_info()[0]