#!/usr/bin/env python

import os, sys, re, subprocess

patient_name_dicom_element_name='Patient\'s Name'
patient_id_dicom_element_name='Patient ID'

def get_dicom_element(dicom_header_file_name, dicom_element_name):
  with file(dicom_header_file_name) as dicom_header_file:
    for dicom_element in dicom_header_file:
      if dicom_element_name in dicom_element: # TODO Need better test here
        return dicom_element
  return None

def get_dicom_element_value(dicom_element):
  m = re.match('.+\[(?P<value>.+)\].+', dicom_element)
  if m:
    return m.group('value')
  else:
    print 'Warning: could not extract value from DICOM element', dicom_element
    return None

def get_dicom_header_file_path(study_dir):
  header_file_find_args = ['find', study_dir, '-type', 'f', '-name', '*.tag']
  dicom_header_files = subprocess.check_output(header_file_find_args)
  if dicom_header_files: # We found at least one DICOM header file
    dicom_header_file_path = dicom_header_files.split('\n')[0] # Pick the first file (should be same patient elements in all)
    if dicom_header_file_path:
      return dicom_header_file_path
  return None
  
# Return a list of study_uid, patient name, patient id triples
def process_epad_dicom_directory(epad_dicom_directory):
  study_find_args = ['find', epad_dicom_directory, '-type', 'd', '-mindepth', '1', '-maxdepth', '1']
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
