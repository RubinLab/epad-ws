package edu.stanford.isis.epadws.db.mysql.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import edu.stanford.isis.epadws.server.ProxyLogger;

import org.apache.commons.codec.binary.Base64;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;
import com.pixelmed.dicom.GeometryOfVolumeFromAttributeList;
import com.pixelmed.geometry.GeometryOfVolume;


/**
 * Originally derived from DicomSegmentationObject in ePad project.
 *
 */
public class DicomSegObj
{

    static final ProxyLogger logger = ProxyLogger.getInstance();

    public DicomSegObj() {
    }

    public GeometryOfVolume getGeometry(AttributeList list) {
        GeometryOfVolume geometry = null;
        try {
            geometry = new GeometryOfVolumeFromAttributeList(list);
            System.err.print(geometry.toString());
        } catch (DicomException e) {
            logger.debug("DicomException " + e.getMessage());
        }
        return geometry;
    }


    public AttributeList getAttributes(String dicomImageUrl) {

        AttributeList list = new AttributeList();
        //String dicomImageUrl = series.getDicomImages().get(0).getUrl();
        try {
            list.read(dicomImageUrl);
        } catch (IOException e) {
            logger.debug("IOException " + e.getMessage());
        } catch (DicomException e) {
            logger.debug("DicomException " + e.getMessage());
        }
        return list;
    }




    public SourceImage convert(String name) {
        SourceImage source = null;
        try {
            source = new SourceImage(name);
        } catch (IOException e) {
            logger.warning("SourceImage failed: "+name, e);
        } catch (DicomException e) {
            logger.warning("SourceImage failed: "+name, e);
        }
        return source;
    }

    public static byte[] getFileBytes(File file) throws IOException {
        FileInputStream fileinputstream = new FileInputStream(file);
        byte abyte0[] = new byte[(int) file.length()];
        fileinputstream.read(abyte0);
        fileinputstream.close();
        return abyte0;
    }

    public static String base64EncodeBytes(byte abyte0[]) {
        String s = new String(Base64.encodeBase64(abyte0, false));
        s = s.replaceAll("\n", "");
        return s;
    }


}
