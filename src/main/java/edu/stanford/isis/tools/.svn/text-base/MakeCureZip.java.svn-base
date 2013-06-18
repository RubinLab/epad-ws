package edu.stanford.isis.tools;

import java.io.File;

/**
 * Make the zip file script for the cure import process.
 */
public class MakeCureZip {

    public static void main(String[] args){



        File[] files = new File("/home/seleniumtests/cure_done/").listFiles();
        makeZipFileScript(files);

        System.out.println("-- zip file done --");

        String ending = ".dcm";
        int totalFiles = countFiles(files,".dcm");
        System.out.println("Total# "+ending+" files: "+totalFiles);
    }


    private static int makeZips(File patientDir,String patientName,int counter){
        File[] subDirs = patientDir.listFiles();
        for(File currSubDir : subDirs){
            //System.out.println("cd ./"+currSubDir.getName());

            String zipFileName = patientName+"_"+currSubDir.getName()+"_"+counter;
            System.out.println("zip -r "+zipFileName+" "+currSubDir.getName());
            counter++;
            System.out.println("mv "+zipFileName+".zip /home/seleniumtests/cure_export/");
            //System.out.println("cd ../");
        }
        return counter;
    }

    /**
     * Go one level down to make the zip file utility.
     * @param files
     */
    private static void makeZipFileScript(File[] files){

        int counter = 1;
        for(File currFile : files){
            if( isIntDir(currFile) ){
                System.out.println("cd "+currFile.getAbsolutePath());
                File[] subDirs = currFile.listFiles();
                for(File currSubDir : subDirs){
                    if(currSubDir.getName().indexOf('.')>-1){
                        continue;
                    }
                    System.out.println("cd ./"+currSubDir.getName());
                    counter = makeZips(currSubDir,currFile.getName(),counter);
                    System.out.println("cd ../");
                }
            }else{
                System.out.println(currFile.getName()+" is not a patient dir.");
            }
        }//for


    }

    private static boolean isIntDir(File file){
        try{
            if( !file.isDirectory() ){
                return false;
            }
            String name = file.getName();
            Integer.parseInt(name);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    private static int countFiles(File[] files, String ending){
        int totalFiles=0;
        for(File currFile : files){
            String currFileName = currFile.getName().toLowerCase();
            if(currFileName.endsWith(ending)){
                totalFiles++;
            }
            if(currFile.isDirectory()){
                totalFiles += countFiles(currFile.listFiles(),ending);
                System.out.println(" "+totalFiles+" of type: "+ending+" in " +currFile.getAbsolutePath());
            }
        }

        return totalFiles;

    }




}
