#!/usr/bin/python

# Script to take a pre-XNAT ePAD DICOM image directory containing DICOM header files 
# and to populate an XNAT project with the patients and studies it contains.
#
# This script was written before XNAT-based push functionality was available in ePAD. A a more reliable approach now is to 
# generate a new ePAD instance by pushing all DICOM images from DCM4CHEE. 
#
# Example usage:
#
# epad2xnat.py -x epad-dev1.stanford.edu:8090 -r [xnat_project-name] -u [xnat_user] -p [xnat_password] ~/DicomProxy/resources/dicom/
#
# For local testing, ZIP up ePAD DICOM  directory excluding PNG files:
#   zip -r dev2.zip ~/DicomProxy/resources/dicom -x "*.png"
# then transfer ZIP to local machine and upzip.
#
# See: https://wiki.xnat.org/display/XNAT16/Basic+DICOM+object+identification

import argparse, os, sys, subprocess, re, xnat, dicom

if __name__ == '__main__':
  parser = argparse.ArgumentParser() 
  parser.add_argument("-x", "--xnat_url", help="XNAT host", required=True)
  parser.add_argument("-r", "--xnat_project", help="XNAT project", required=True)
  parser.add_argument("-u", "--user", help="XNAT user", required=True)
  parser.add_argument("-p", "--password", help="XNAT password", required=True)
  parser.add_argument("epad_dicom_directory", help="ePAD DICOM directory path")
  args = parser.parse_args()

  xnat_base_url = 'http://' + args.xnat_url
  project_name = args.xnat_project
  epad_dicom_directory = args.epad_dicom_directory
  user = args.user
  password=args.password

  try:  
    study_uid_patient_name_id_triples = dicom.process_epad_dicom_directory(epad_dicom_directory)
    
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
