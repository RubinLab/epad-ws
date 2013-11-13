#!/usr/bin/python
#
# Script to list DICOM patient ID, patient name, study ID headers in the DICOM files in an ePAD DICOM directory.
# This is typically in a directory called ~/DicomProxy/resources/dicom on an ePAD machine.
#
# Example usage:
#
# epad-dicom-headers.py ~/DicomProxy/resources/dicom/
#

import argparse, dicom, subprocess

if __name__ == '__main__':
  parser = argparse.ArgumentParser() 
  parser.add_argument("epad_dicom_directory", help="ePAD DICOM directory path")
  args = parser.parse_args()

  epad_dicom_directory = args.epad_dicom_directory

  try:  
    study_uid_patient_name_id_triples = dicom.process_epad_dicom_directory(epad_dicom_directory)
    
    print 'Found', len(study_uid_patient_name_id_triples), 'study UID/patient ID/patient name triples' 
    for study_uid, patient_id, patient_name in study_uid_patient_name_id_triples:
      print patient_id.ljust(40), patient_name.ljust(30), study_uid
    
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
