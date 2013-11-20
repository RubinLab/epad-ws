#!/usr/bin/env python

import os, sys, re, requests

xnat_auth_base='/xnat/data/JSESSION'
xnat_project_base='/xnat/data/projects/'

# XNAT names allow alphanumeric, underscore (_), dash(-), period (.) and space characters only.
# Replace other characters with underscores. We also replace spaces with underscores. 
def project_name_to_xnat_project_id(project_name):
  return re.sub('[^a-zA-Z0-9\.\-_]', '_', project_name)

# Xnat labels allow alphanumeric, underscore (_), dash(-), and space characters only.
# Replace other characters with underscores. Also first replace DICOM ^ with underscore. 
# We also replace spaces with underscores. 
def patient_name_to_xnat_subject_label(patient_name):
  return re.sub('[^a-zA-Z0-9\-_]', '_', patient_name.replace('^', '_')) 

def study_uid_to_xnat_experiment_id(study_uid):
  return study_uid.replace('.', '_') # XNAT does not like periods in its IDs

def login(xnat_base_url, user, password):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  r = requests.post(xnat_auth_url, auth=(user, password))
  if r.status_code == requests.codes.ok:
    jsessionid = r.text
    return jsessionid
  else:
    print 'Error: XNAT login request failed - status code =', r.status_code
    r.raise_for_status()

def logout(xnat_base_url, jsessionid):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.delete(xnat_auth_url, cookies=cookies)
  if r.status_code != requests.codes.ok:
    print 'Warning: XNAT logout request failed - status code =', r.status_code
  
def create_project(xnat_base_url, jsessionid, project_name):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_project_url = xnat_base_url + xnat_project_base  
  payload = { 'ID': xnat_project_id, 'name': project_name } 
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.post(xnat_project_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok: # XNAT returns 200 rather than 201 even if project does not already exist
    print 'Project', project_name, 'created with XNAT project ID', xnat_project_id
  else:
    print 'Warning: failed to create project', project_name, '- status code =', r.status_code
    r.raise_for_status()

# We want to use auto-generated XNAT subject IDs so do not use the patient_id as the ID of the XNAT subject we create. 
# (In any case, XNAT IDs allow only alphanumeric and the dash (-) character so we could not reliably map patient IDs to that field because of the 
# wide range of typical characters.)
# 
# XNAT labels are also very restricted (alphanumeric, underscore (_), dash(-), period (.) and space characters only) and 
# can not thus he used to reliably store the patient_name field. 
# So we use the unrestricted XNAT "src" field to store the patient_name field and put the patient_name field in the label field
# with non allowed characters replaced with underscores. All ePAD searches should be performed using the "src" field, e.g.,
#
# curl -b JSESSIONID=... -X GET "http://[host]:[port]/subjects/?project=*&src=*hhh*&columns=label,src"
#
# Note that subject labels do not have to be globally unique in XNAT so more than one patient can share the same label. 
# They are, however, tied go a particular project through their XNAT ID. 
#
def create_subject(xnat_base_url, jsessionid, project_name, patient_id, patient_name):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_project_id
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  xnat_subject_label = patient_name_to_xnat_subject_label(patient_name)
  cookies = dict(JSESSIONID=jsessionid)
  payload = { 'label' : project_id+'--'+xnat_subject_label, 'src': patient_name } 
  xnat_subject_url = xnat_epad_project_subject_url
  r = requests.post(xnat_subject_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok:
    print 'Subject', patient_name, 'already exists in XNAT'
  elif r.status_code == requests.codes.created:
    print 'Subject', patient_name, 'created with XNAT subject label', xnat_subject_label 
  else:
    print 'Warning: failed to create subject', patient_name, 'with label', xnat_subject_label, '- status code =', r.status_code

def create_subjects(xnat_base_url, jsessionid, project_name, study_uid_patient_name_id_triples):  
  for _, patient_id, patient_name in study_uid_patient_name_id_triples:
    create_subject(xnat_base_url, jsessionid, project_name, patient_id, patient_name)

def create_experiment(xnat_base_url, jsessionid, project_name, patient_id, patient_name, study_uid):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_project_id
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  xnat_subject_label = patient_name_to_xnat_subject_label(patient_name)
  xnat_experiment_id = study_uid_to_xnat_experiment_id(study_uid)
  payload = { 'name': study_uid, 'xsiType': 'xnat:otherDicomSessionData' }
  xnat_experiment_url = xnat_epad_project_subject_url + xnat_subject_label + '/experiments/' + xnat_experiment_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_experiment_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok:
    print 'Experiment for study', study_uid, 'already existed in XNAT'
  elif r.status_code == requests.codes.created:
    print 'Experiment for study', study_uid, 'created with XNAT experiment ID', xnat_experiment_id
  else:
    print 'Warning: failed to create experiment for study', study_uid, '- status code =', r.status_code
    print 'Warning: xnat_experiment_url', xnat_experiment_url

def create_experiments(xnat_base_url, jsessionid, project_name, study_uid_patient_name_id_triples):
  for study_uid, patient_id, patient_name in study_uid_patient_name_id_triples:
    create_experiment(xnat_base_url, jsessionid, project_name, patient_id, patient_name, study_uid)
