#!/usr/bin/python

import argparse, os, sys, subprocess, re, epad

if __name__ == '__main__':
  parser = argparse.ArgumentParser() 
  parser.add_argument("-x", "--epad_url", help="ePAD host", required=True)
  parser.add_argument("-u", "--user", help="ePAD user", required=True)
  parser.add_argument("-p", "--password", help="ePAD password", required=True)
  args = parser.parse_args()

  epad_base_url = args.epad_url
  user = args.user
  password=args.password

  try:  

    jsessionid = epad.login(epad_base_url, user, password)
    epad.logout(epad_base_url, jsessionid)
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
