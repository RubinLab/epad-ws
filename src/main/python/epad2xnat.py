#!/usr/bin/python

# First stab at script to take a pre-XNAT ePAD DICOM image directory containing DICOM header files 
# and to populate an XNAT project with the patients and studies it contains.
#
# Example usage:
# epad2xnat.py -x epad-dev1.stanford.edu:8090 -r [project-name] -u [user] -p [password] ~/DicomProxy/resources/dicom/

import argparse, os, sys, subprocess, re, xnat

patient_id_dicom_element_name='Patient\'s Name'

# Return a list of patient, study_uid pairs.
def process_epad_image_directory(epad_image_directory):
  study_find_args = ['find', epad_image_directory, '-type', 'd', '-mindepth', '1', '-maxdepth', '1']
  patient_study_pairs = []
  
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
                patient_study_pairs.append( (patient_name, study_uid) )
              else: 
                print 'Warning: patient ID missing in DICOM element ', patient_id_dicom_element, ' in header file', dicom_header_file_path 
            else:
              print 'Warning: error extracting patient ID from DICOM element ', patient_id_dicom_element, ' in header file', dicom_header_file_path
          else:
            print 'Warning: did not find a patient ID DICOM element in tag file', dicom_header_file_path
      else:
        print "Warning: no DICOM header file found for study", study_uid
  return patient_study_pairs

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
    patient_study_pairs = process_epad_image_directory(epad_image_directory)
    
    print 'Found', len(patient_study_pairs), 'subject/study pairs' 
    for patient_name, study_uid in patient_study_pairs:
      print patient_name.ljust(30), study_uid

    jsessionid = xnat.login(xnat_base_url, user, password)
    xnat.create_project(xnat_base_url, jsessionid, project_name)
    xnat.create_subjects(xnat_base_url, jsessionid, project_name, patient_study_pairs)      
    xnat.create_experiments(xnat_base_url, jsessionid, project_name, patient_study_pairs)
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
