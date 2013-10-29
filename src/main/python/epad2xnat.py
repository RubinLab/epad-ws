#!/usr/bin/python

# First stab at script to take a pre-XNAT ePAD DICOM image directory containing DICOM header files 
# and to populate an XNAT project with the patients and studies it contains.
#
# Example usage:
# epad2xnat.py -x epad-dev1.stanford.edu:8090 -r [xnat_project-name] -u [xnat_user] -p [xnat_password] ~/DicomProxy/resources/dicom/
#
# See: https://wiki.xnat.org/display/XNAT16/Basic+DICOM+object+identification

import argparse, os, sys, subprocess, re, xnat

patient_name_dicom_element_name='Patient\'s Name'
patient_id_dicom_element_name='Patient ID'

def get_dicom_element(dicom_header_file_name, dicom_element_name):
  with file(dicom_header_file_name) as dicom_header_file:
    for dicom_element in dicom_header_file:
      if dicom_element_name in dicom_element:
        return dicom_element
  return None

def get_dicom_element_value(dicom_element):
  m = re.match('.+\[(?P<value>.+)\].+', dicom_element)
  if m:
    return m.group('value')
  else:
    print 'Warning: could not extract value from DICOM element', dicom_eleament
    return None

def get_dicom_header_file_path(study_dir):
  header_file_find_args = ['find', study_dir, '-type', 'f', '-name', '*.tag']
  dicom_header_files = subprocess.check_output(header_file_find_args)
  if dicom_header_files: # We found at least one DICOM header file
    dicom_header_file_path = dicom_header_files.split('\n')[0] # Pick the first file (should be same patient elements in all)
    if dicom_header_file_path:
      return dicom_header_file_path
  return None
  
# Return a list of patient, study_uid pairs.
def process_epad_image_directory(epad_image_directory):
  study_find_args = ['find', epad_image_directory, '-type', 'd', '-mindepth', '1', '-maxdepth', '1']
  study_uid_patient_name_id_triple = []
  
  study_dirs = subprocess.check_output(study_find_args)
  for study_dir in study_dirs.split('\n'):
    if study_dir: # Process directories directly under the base directory (which should contain DICOM study)
      study_uid = os.path.basename(study_dir).replace('_', '.') # ePAD converts . to _ in file names
      dicom_header_file_path = get_dicom_header_file_path(study_dir)      
      if dicom_header_file_path: 
        patient_id_dicom_element = get_dicom_element(dicom_header_file_path, patient_id_dicom_element_name) 
        if patient_id_dicom_element: 
          patient_name_dicom_element = get_dicom_element(dicom_header_file_path, patient_name_dicom_element_name)
          if patient_name_dicom_element:  
            patient_id = get_dicom_element_value(patient_id_dicom_element)
            patient_name = get_dicom_element_value(patient_name_dicom_element)
            if patient_id and patient_name:
              study_uid_patient_name_id_triple.append( (study_uid, patient_id, patient_name) )
          else:
            print 'Warning: no patient name found in DICOM header file', dicom_header_file_path
        else:
          print 'Warning: no patient ID found in DICOM header file', dicom_header_file_path
      else:
        print 'Warning: no DICOM header file found for study', study_uid
  return study_uid_patient_name_id_triple

if __name__ == '__main__':
  parser = argparse.ArgumentParser() 
  parser.add_argument("-x", "--xnat_url", help="XNAT host", required=True)
  parser.add_argument("-r", "--xnat_project", help="XNAT project", required=True)
  parser.add_argument("-u", "--user", help="XNAT user", required=True)
  parser.add_argument("-p", "--password", help="XNAT password", required=True)
  parser.add_argument("epad_image_directory", help="ePAD image directory path")
  args = parser.parse_args()

  xnat_base_url = 'http://' + args.xnat_url
  project_name = args.xnat_project
  epad_image_directory = args.epad_image_directory
  user = args.user
  password=args.password

  try:  
    study_uid_patient_name_id_triples = process_epad_image_directory(epad_image_directory)
    
    print 'Found', len(study_uid_patient_name_id_triples), 'study UID/patient ID/patient name triples' 
    for study_uid, patient_id, patient_name in study_uid_patient_name_id_triples:
      print patient_id.ljust(40), patient_name.ljust(30), study_uid

    jsessionid = xnat.login(xnat_base_url, user, password)
    xnat.create_project(xnat_base_url, jsessionid, project_name)
    xnat.create_subjects(xnat_base_url, jsessionid, project_name, study_uid_patient_name_id_triples)      
    xnat.create_experiments(xnat_base_url, jsessionid, project_name, study_uid_patient_name_id_triples)
    xnat.logout(xnat_base_url, jsessionid)
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
