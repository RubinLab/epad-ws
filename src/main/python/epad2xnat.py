#!/usr/bin/python

# epad2xnat.py -x epad-dev1.stanford.edu:8090 -u xxx -p xxx dicom/

import argparse, os, sys, subprocess, re, requests

xnat_auth_base_url='/xnat/data/JSESSION'
xnat_project_base_url='/xnat/data/projects/'
xnat_epad_project_name='EPAD_XNAT'

patient_id_element_name='Patient\'s Name'

parser = argparse.ArgumentParser() 

parser.add_argument("-x", "--xnat", help="XNAT host", required=True)
parser.add_argument("-u", "--user", help="XNAT user", required=True)
parser.add_argument("-p", "--password", help="XNAT password", required=True)
parser.add_argument("basepath", help="base directory path")

args = parser.parse_args()

xnat_auth_url = 'http://'+args.xnat+xnat_auth_base_url
xnat_epad_project_url = 'http://'+args.xnat+xnat_project_base_url + xnat_epad_project_name
xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
basepath = args.basepath
user = args.user
password=args.password

series_find_args = ['find', basepath, '-type', 'd', '-mindepth', '1', '-maxdepth', '1']

def user_name_to_xnat_id(username):
  return re.sub('[\^ \-,]', '_', username)

def create_xnat_subject(xnat_epad_project_subject_url, username, jsessionid):
  xnat_user_id = user_name_to_xnat_id(username)
  #payload = { 'label': username } # XNAT is very sensitive to labels
  payload = { }
  xnat_subject_url = xnat_epad_project_subject_url + xnat_user_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_subject_url, params=payload, cookies=cookies)
  print r.status_code

def create_xnat_series_experiment(xnat_epad_project_subject_url, username, series_uid, jsessionid):
  xnat_user_id = user_name_to_xnat_id(username)
  xnat_experiment_id = series_uid.replace('.', '_')
  payload = { 'label': series_uid, 'xsiType': 'xnat:otherDicomSessionData' }
  xnat_experiment_url = xnat_epad_project_subject_url + xnat_user_id + '/experiments/' + xnat_experiment_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_experiment_url, params=payload, cookies=cookies)
  print r.status_code

try:
  r = requests.post(xnat_auth_url, auth=(user, password))
  jsessionid = r.text
  print 'Got XNAT session token', jsessionid

  series_dirs = subprocess.check_output(series_find_args)

  for series_dir in series_dirs.split('\n'):
    if series_dir: # Process directories directly under the base directory (which should contain DICOM series)
      series_uid = os.path.basename(series_dir).replace('_', '.') # ePAD converts . to _ in file names

      # Look for DICOM header files in each directory
      header_file_find_args = ['find', series_dir, '-type', 'f', '-name', '*.tag']
      header_files = subprocess.check_output(header_file_find_args)

      if header_files:  # We found at least one DICOM header file
        header_file_path = header_files.split('\n')[0] # Pick the first (patient ID will be same in all files)
        if header_file_path:
          header_file_name = os.path.basename(header_file_path)
          # Find the line containing the patient ID DICOM element. TODO replace with grep and no error code on no match
          id_grep_args = ['find', '.', '-name', header_file_name, '-exec', 'grep', patient_id_element_name, '{}', ';']
          patient_id_elements = subprocess.check_output(id_grep_args)
      
          if patient_id_elements:
            patient_id_element = patient_id_elements.split('\n')[0] # Should only be one
            m = re.match('.+\[(?P<pid>.+)\].+', patient_id_element)
            if m:
              subject_name = m.group('pid')
              if subject_name:
                print subject_name.ljust(30), series_uid
                create_xnat_subject(xnat_epad_project_subject_url, subject_name, jsessionid)
                create_xnat_series_experiment(xnat_epad_project_subject_url, subject_name, series_uid, jsessionid)

              else:
                print 'Warning: patient ID value missing in DICOM element ', patient_id_element, ' in header file', header_file_path
            else:
              print 'Warning: error extracting patient ID from DICOM element ', patient_id_element, ' in header file', header_file_path
          else:
            print 'Warning: did not find a patient ID DICOM element in tag file', header_file_path
      else:
        print "Warning: no DICOM header file found for series", series


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

