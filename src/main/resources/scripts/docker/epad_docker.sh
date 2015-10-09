#!/bin/bash
#
# Script for installing/running ePAD, a Quantitative Imaging Platform from the Rubin Lab at Stanford, using Docker
#
#Copyright (c) 2015 The Board of Trustees of the Leland Stanford Junior University
#All rights reserved.
#
# Please do not Redistribute. The latest copy of this script is available from http://epad.stanford.edu/
#
#THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
#INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
#DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
#SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
#SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
#WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
#USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#
echo "STARTING ePad using docker" 
start() {
	path=$(pwd)
	if [ ! -d "$path/DicomProxy/etc" ]; then
		echo "ePad installation not detected"
		read -r -p "Are you sure you want to install ePad (including mysql/dcm4chee/eXist) in this directory ($path)? [y/N] " response
		if [[ $response =~ ^([yY][eE][sS]|[yY])$ ]]
		then
		    echo ""
		    echo "mysql databases, dcm4chee files, eXist db and ePad images/configuration will be installed in $path/DicomProxy"
		    echo "Note: ports 8080, 9080, 8899 should not be in use"
		    echo ""
		else
		    echo ""
		    echo "Please cd to the root directory of epad docker installation and then start this script"
		    echo ""
		    exit
		fi
	fi
	if hash docker 2>/dev/null; then
		echo "RUNNING mysql container ..."
		echo ""
		mkdir -p DicomProxy/mysql
	 	docker run -it -d --name mysql -e MYSQL_ROOT_PASSWORD=epad -v $path/DicomProxy/mysql/:/var/lib/mysql/ rubinlab/mysql
	 	sleep 1
		RUNNING=$(docker inspect -f {{.State.Running}} mysql 2> /dev/null)
	 	if [ $? -ne 0 ]; then
    		echo "mysql did not start up"
    		exit 1
		fi
		if [ "$RUNNING" == "false" ]; then
  			echo "mysql has stopped running"
 			docker rm mysql
 			exit 2
		fi
  		echo "Waiting for mysql to start up ..."
		sleep 10
		epaddbexists=`docker exec -i -t mysql  mysql -uroot -pepad -e "show databases like 'epaddb'" | grep -v Warning` 
		if [[ $string == *"ERROR"* ]]
		then
			echo "Error checking Database Existence"
			docker stop mysql
			docker rm mysql
			exit
		fi
		if [[ ! -z "$epaddbexists" ]];
		then
			echo "DATABASES ALREADY EXISTS: $epaddbexists"
		else
			echo "DATABASE epaddb DOES NOT EXIST"
			read -r -p "Are you sure you want to install new mysql databases in($path/DicomProxy/mysql)? [y/N] " response
			if [[ $response =~ ^([yY][eE][sS]|[yY])$ ]]
			then
				echo "INSTALLING databases on mysql container..."
			else
			    docker stop mysql
			    docker rm mysql
			    exit
			fi
			echo ""
			docker exec mysql sh /home/install.sh
			mkdir -p DicomProxy/etc
		fi
		mkdir -p DicomProxy/etc/scripts
		mkdir -p DicomProxy/dcm4chee
		mkdir -p DicomProxy/bin
		mkdir -p DicomProxy/tmp
		mkdir -p DicomProxy/jetty
		mkdir -p DicomProxy/log
		mkdir -p DicomProxy/lib
		mkdir -p DicomProxy/lib/plugins
		mkdir -p DicomProxy/webapps
		mkdir -p DicomProxy/resources
		mkdir -p DicomProxy/resources/annotations
		mkdir -p DicomProxy/resources/dicom
		mkdir -p DicomProxy/resources/download
		mkdir -p DicomProxy/resources/fileupload
		mkdir -p DicomProxy/resources/files
		mkdir -p DicomProxy/resources/download
		mkdir -p DicomProxy/resources/schema
		mkdir -p DicomProxy/resources/templates
		mkdir -p DicomProxy/resources/upload
		if [ ! -e "DicomProxy/lib/epad-ws-1.1-jar-with-dependencies.jar" ]; then
			if hash wget 2>/dev/null; then
				wget --directory-prefix=DicomProxy/lib/ ftp://epad-distribution.stanford.edu/epad-ws-1.1-jar-with-dependencies.jar
			else
				echo ">>>>> Please install wget to download the latest version of ePad <<<<<"
			fi
		fi
		if [ ! -e "DicomProxy/webapps/ePad.war" ]; then
			if hash wget 2>/dev/null; then
				wget --directory-prefix=DicomProxy/webapps/ ftp://epad-distribution.stanford.edu/ePad.war
			else
				echo ">>>>> Please install wget to download the latest version of ePad <<<<<"
			fi
		fi
		echo "RUNNING dcm4chee container..."
        	echo ""
 		docker run --name dcm4chee -v $path/DicomProxy/dcm4chee/:/app/dcm4chee-2.17.1-mysql/server/default/archive/ -d --link mysql:mysql -p 9080:9080 -p 11112:11112 rubinlab/dcm4chee
 		echo "RUNNING eXist container ..."
        	echo ""
		docker run -it -d --name exist -p 8899:8899 rubinlab/exist
		echo "RUNNING ePAD container ..."
                echo ""
		docker run -it -d --name epad_web -v $path/DicomProxy/:/root/DicomProxy/ -v /etc/localtime:/etc/localtime:ro -v /etc/timezone:/etc/timezone:ro -p 8080:8080 --link dcm4chee:dcm4chee --link mysql:mysql_host --link exist:exist -e DOCKER_HOST=`hostname` rubinlab/epad
		echo ""
		echo ""
		echo "Please navigate to http://`hostname`:8080/epad/ in your browser and login as admin/admin"
		echo ""
		echo ""
	else
		echo 'you need to install docker'
	fi
}

start
