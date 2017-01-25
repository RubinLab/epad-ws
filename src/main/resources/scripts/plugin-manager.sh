params=""
while [ $# -gt 0 ]
do
    params="$params $1"
    if [ $1 = "-j" ]
    then 
	jar="$2"	
    fi
    shift
done
java -Djava.library.path=${LD_LIBRARY_PATH} -cp ~/DicomProxy/lib/epad-ws-1.1-jar-with-dependencies.jar:$jar edu.stanford.epad.epadws.service.PluginOperations $params
