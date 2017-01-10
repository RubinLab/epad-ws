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
	UNAME=`uname`
	if [ "$UNAME" == "Darwin" ]; then
		if [ "x$DOCKER_HOST" == "x" ]; then
			echo "DOCKER_HOST env variable not defined. Please use Docker Terminal."
			exit 1
		fi
	fi
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
		echo "Pulling all docker images, this will take some time!"
		sleep 2
		echo ""
		docker pull rubinlab/mysql
		echo ""
		echo "RUNNING mysql container ..."
		echo ""
		mkdir -p DicomProxy/mysql
		RUNNING=$(docker inspect -f {{.State.Running}} mysql 2> /dev/null)
		if [ $? -ne 0 ] || [ "$RUNNING" == "<no value>" ] ; then
			if [ "$UNAME" == "Darwin" ]; then
				docker run -it -d --name mysql -e MYSQL_ROOT_PASSWORD=epad rubinlab/mysql
			else
	 	docker run -it -d --name mysql -e MYSQL_ROOT_PASSWORD=epad -v $path/DicomProxy/mysql/:/var/lib/mysql/ rubinlab/mysql
			fi
			echo "Waiting for mysql to stabilize ..."
			sleep 10 
		elif [ "$RUNNING" == "false" ]; then
  			echo "mysql container already installed, starting container"
 			docker start mysql
			sleep 2
		fi
		docker pull rubinlab/dcm4chee
		docker pull rubinlab/exist
		if [ "$UNAME" == "Darwin" ]; then
		    docker pull rubinlab/epad:mac
		else
		    docker pull rubinlab/epad
		fi
		RUNNING=$(docker inspect -f {{.State.Running}} mysql 2> /dev/null)
	 	if [ $? -ne 0 ] || [ "$RUNNING" == "<no value>" ] ; then
		    echo "mysql did not start up"
		    exit 1
		fi
		if [ "$RUNNING" == "false" ]; then
  			echo "mysql has stopped running"
 			docker rm mysql
 			exit 2
		fi
		epaddbexists=`docker exec -i -t mysql  mysql -uroot -pepad -e "show databases like 'epaddb'" | grep -v Warning` 
		if [[ $epaddbexists == *"ERROR"* ]]
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
		RUNNING=$(docker inspect -f {{.State.Running}} dcm4chee 2> /dev/null)
		if [ $? -ne 0 ]; then
			if [ "$UNAME" == "Darwin" ]; then
				docker run --name dcm4chee  -d --link mysql:mysql -p 1099:1099 -p 9080:9080 -p 11112:11112 rubinlab/dcm4chee
			else
 		docker run --name dcm4chee -v $path/DicomProxy/dcm4chee/:/app/dcm4chee-2.17.1-mysql/server/default/archive/ -d --link mysql:mysql -p 1099:1099 -p 9080:9080 -p 11112:11112 rubinlab/dcm4chee
			fi
		elif [ "$RUNNING" == "false" ]; then
  			echo "dcm4chee container already installed, starting container"
 			docker start dcm4chee
		fi
 		echo "RUNNING eXist container ..."
        	echo ""
		RUNNING=$(docker inspect -f {{.State.Running}} exist 2> /dev/null)
		if [ $? -ne 0 ] || [ "$RUNNING" == "<no value>" ] ; then
			docker run -it -d --name exist -p 8899:8899 rubinlab/exist
		elif [ "$RUNNING" == "false" ]; then
  			echo "exist container already installed, starting container"
 			docker start exist
		fi
		RUNNING=$(docker inspect -f {{.State.Running}} epad_web 2> /dev/null)
		if [ "$RUNNING" == "true" ]; then
  			echo "epad container already running"
 			echo "To stop and restart container, enter: 'docker stop epad_web; docker start epad_web'"
  			echo "To reinstall container, enter: 'docker stop epad_web; docker rm epad_web' and retry this script"
			exit 0
		elif [ "$RUNNING" == "false" ]; then
  			echo "epad_web container already installed"
  			echo "To reinstall container, enter: 'docker stop epad_web; docker rm epad_web' and retry this script"
  			echo "Starting container ..."
			docker start epad_web
		fi
		RUNNING=$(docker inspect -f {{.State.Running}} epad_web 2> /dev/null)
		if [ $? -ne 0 ] || [ "$RUNNING" == "<no value>" ] ; then
			echo "RUNNING ePAD container ..."
			echo ""
			if [ "$UNAME" == "Darwin" ]; then
				
				docker run -it -d --name epad_web -v $path/DicomProxy/:/root/mac/ -p 8080:8080 --link dcm4chee:dcm4chee --link mysql:mysql_host --link exist:exist -e MACDOCKER_HOST=$DOCKER_HOST rubinlab/epad:mac
			else
				docker run -it -d --name epad_web -v $path/DicomProxy/:/root/DicomProxy/ -v /etc/localtime:/etc/localtime:ro -v /etc/timezone:/etc/timezone:ro -p 8080:8080 --link dcm4chee:dcm4chee --link mysql:mysql_host --link exist:exist -e DOCKER_HOST=`hostname` rubinlab/epad
			fi
		fi
		echo ""
		echo ""
		if [ "$UNAME" == "Darwin" ]; then
			echo "Please WAIT for at least TWO minutes and then navigate to http://`docker-machine ip default`:8080/epad/ in your browser and login as admin/admin"
		else
			echo "Please WAIT for at least TWO minutes and then navigate to http://`hostname`:8080/epad/ in your browser and login as admin/admin"
		fi
		echo ""
		echo ""
	else
		echo 'you need to install docker'
	fi
}

start
