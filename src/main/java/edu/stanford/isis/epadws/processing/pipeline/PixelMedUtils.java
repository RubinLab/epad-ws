package edu.stanford.isis.epadws.processing.pipeline;

import com.pixelmed.dicom.*;

import java.io.FileInputStream;
import java.io.IOException;


/**
 * @author alansnyder
 */
public class PixelMedUtils {

    private PixelMedUtils(){}

    private static DicomInputStream getDicomInputStream(String dicomFilePath)
        throws IOException
    {
        return new DicomInputStream(new FileInputStream(dicomFilePath));
    }

    public static AttributeList readAttributeListFromDicomFile(String dicomFilePath)
        throws IOException, DicomException
    {
        DicomInputStream dis = getDicomInputStream(dicomFilePath);
        AttributeList attributeList = new AttributeList();
        attributeList.read(dis);
        return attributeList;
    }

    public static boolean isDicomSegmentationObject(String filePath){
        try{
            AttributeList list = readAttributeListFromDicomFile(filePath);
            String sopClassUID = Attribute.getSingleStringValueOrEmptyString(list, TagFromName.SOPClassUID);

            return sopClassUID.equals(SOPClass.SegmentationStorage);
        }catch (Exception e){
            return false;
        }
    }

    public static boolean isMultiframedDicom(String filePath){
        try{
            AttributeList list = readAttributeListFromDicomFile(filePath);
            int numberOfFrames = Attribute.getSingleIntegerValueOrDefault(list,TagFromName.NumberOfFrames,1);

            return numberOfFrames>1;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean isEnhancedMultiframeImage(String filePath){
        try{
            AttributeList list = readAttributeListFromDicomFile(filePath);
            String sopClassUID = Attribute.getSingleStringValueOrEmptyString(list, TagFromName.SOPClassUID);

            return SOPClass.isEnhancedMultiframeImageStorage(sopClassUID);
        }catch (Exception e){
            return false;
        }
    }

}
