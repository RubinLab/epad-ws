package edu.stanford.isis.tools;

import java.io.File;

/**
 *
 *
 * @author mabidi
 */
public class DeleteSpaces
{

    /**
     * @param args
     */
    public static void main(String[] args) {
         File[] files = new File("/home/seleniumtests/cure_data/").listFiles();
         renameFiles(files);

    }

    private static void renameFiles(File[] originalFiles) {
         for (File originalFile : originalFiles) {
         //File originalFile = new File(file);

           if(!originalFile.getName().startsWith(".")){
              if (originalFile.isDirectory()) {
                 File renamedFile = new File(originalFile.getAbsolutePath().replaceAll(" ", "_").replaceAll("_-_", "_").replaceAll("_+_", "_").replaceAll("-", "_").replaceAll("#", "_").replaceAll("\\+", "_").replaceAll("&", "_").replaceAll("\\(", "_").replaceAll("\\)", "_"));
                 if(!renamedFile.getName().equals(originalFile.getName())){
                      boolean success = originalFile.renameTo(renamedFile);
                      System.out.println("Original Directory name: "+originalFile.getAbsolutePath());
                      System.out.println("Rename was successful?"+success+"\n File has been renamed to: " + renamedFile.getAbsolutePath());
                      renameFiles(renamedFile.listFiles());
                 } else {
                      renameFiles(originalFile.listFiles());
                 }

                // Calls same method again.

           } else if (originalFile.isFile()){

        File renamedFile = new File(originalFile.getAbsolutePath().replaceAll(" ", "_").replaceAll("_-_", "_").replaceAll("_+_", "_").replaceAll("-", "_").replaceAll("#", "_").replaceAll("\\+", "_").replaceAll("&", "_").replaceAll("\\(", "_").replaceAll("\\)", "_"));

        boolean success = originalFile.renameTo(renamedFile);
                 System.out.println("Original Directory name"+originalFile.getAbsolutePath());

               System.out.println("Rename was successful?"+success+"\n File has been renamed to: " + renamedFile.getAbsolutePath());

           }}
         }
    }


}
