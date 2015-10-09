#!/bin/bash
#
# Script for uninstalling Docker ePAD, a Quantitative Imaging Platform from the Rubin Lab at Stanford, using Docker
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
echo "Uninstalling ePad using docker"
read -r -p "Are you sure you want to uninstall ePad (You may lose any saved images/annotations/other data if you have not backed them up)? [y/N] " response
if [[ $response =~ ^([yY][eE][sS]|[yY])$ ]]
then
	docker stop epad_web
	docker rm epad_web
	docker stop exist
	docker rm exist
	docker stop dcm4chee
	docker rm dcm4chee
	docker stop mysql
	docker rm mysql
else
    echo "Nothing uninstalled"
    exit
fi
