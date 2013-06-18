package edu.stanford.isis.tools.pixelmed;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.pixelmed.anatproc.CodedConcept;
import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.dicom.TagFromName;


/**
 * <p>A class for converting segmentation results in TIFF files to DICOM segmentation objects.</p>
 *
 * @author	Wei Lu (luwei@tju.edu.cn)
 * @date 2013-5
 */

public class ConvertTIFFMasksToSegmentationObjects {

	private AttributeList list = new AttributeList();
	private byte [] pixels = null; 
	private short image_width = 0, image_height = 0, image_frames = 0;
	private short [] orientation = new short [] {1, 0, 0, 0, 0, 1};
	private double [] spacing = new double [] {0.65, 0.8};
	private double thickness = 0.5;
	private double [][] positions = null;
	
	/**
	 * @param mask_files: Array of the TIFF files which save the masks.
	 * @param dicom_files: Array of the original DICOM files. 
	 * @param output_file: Name of the output segmentation objects file.
	 * @throws DicomException
	 * @throws IOException 
	 */
	public ConvertTIFFMasksToSegmentationObjects(ArrayList <String> mask_files, ArrayList <String> dicom_files, String output_file) throws DicomException, IOException {
		try {
			get_attributes_from_dicom_files(dicom_files);
			get_pixels_from_mask_files(mask_files);
		} catch(Exception e) {
			System.err.println(e);
			e.printStackTrace(System.err);
			throw(new DicomException("Error in reading dicom files!"));
		}
		
		SegmentationObjectsFileWriter obj = new SegmentationObjectsFileWriter(list, orientation, spacing, thickness); 

		CodedConcept category = new CodedConcept(
		        "260787004"                /*conceptUniqueIdentifier*/,
		        "SRT"                        /*codingSchemeDesignator*/,
		        "SNM3"                        /*legacyCodingSchemeDesignator*/,
		        null                        /*codingSchemeVersion*/,
		        "A-00004"                /*codeValue*/,
		        "Physical Object"        /*codeMeaning*/,
		        null                        /*codeStringEquivalent*/,
		        null                        /*synonynms*/);
		CodedConcept type = new CodedConcept(
		        null                /*conceptUniqueIdentifier*/,
		        "SRT"                        /*codingSchemeDesignator*/,
		        null                        /*legacyCodingSchemeDesignator*/,
		        null                        /*codingSchemeVersion*/,
		        "T-32000"                /*codeValue*/,
		        "Heart"        /*codeMeaning*/,
		        null                        /*codeStringEquivalent*/,
		        null                        /*synonynms*/);
		
		obj.AddOneSegment("Segment No.1 is for ...", category, type);	
		
		obj.AddAllFrames(pixels, image_frames, image_width, image_height, "binary", (short)0, positions);
		
		obj.SaveDicomFile(output_file);

	}
	
	/**
	 * List the files in the assigned path and sort the filenames in alphabetic order.
	 * @param path
	 * @return Names of the files.
	 */
	public static ArrayList <String> list_files_in_folder_in_alphabetic_order(String path) {
		final File folder = new File(path);
	    File [] listOfFiles = folder.listFiles();
	    
	    ArrayList <String> file_list = new ArrayList <String>();

	    for(int i = 0; i < listOfFiles.length; i++) {
	      if(listOfFiles[i].isFile()) {
	        file_list.add(listOfFiles[i].toString());
	      }
	    }		
	    Collections.sort(file_list);
	    
		return file_list;
	}
		
	private void get_attributes_from_dicom_files(ArrayList <String> dicom_files) throws FileNotFoundException, IOException, DicomException {
		AttributeList list = new AttributeList();
		String input_file = null;
		DicomInputStream i_stream = null;
		
		// Get common attributes from the first input file.
		input_file = dicom_files.get(0);
		i_stream = new DicomInputStream(new FileInputStream(input_file));
		list.read(i_stream);
		this.list = (AttributeList)list.clone();
		
		image_width = (short)Attribute.getSingleIntegerValueOrDefault(list, TagFromName.Columns, 1);
		image_height = (short)Attribute.getSingleIntegerValueOrDefault(list, TagFromName.Rows, 1);
		image_frames =  (short)dicom_files.size();
		// Get geometric info.
		{
			Attribute a = list.get(TagFromName.SliceThickness);
			thickness = a.getSingleDoubleValueOrDefault(0.1);
			a = list.get(TagFromName.PixelSpacing);
			spacing = a.getDoubleValues();
			a = list.get(TagFromName.ImageOrientationPatient);
			String [] s = a.getStringValues();
			for(int i = 0; i < s.length; i++)
				orientation[i] = (short)Float.parseFloat(s[i]);
		}
		
		// Get position of each frame.
		{	// Get sequence format.
			positions = new double[image_frames][3];
			for(int i = 0; i < dicom_files.size(); i++) {
				input_file = dicom_files.get(i);
				i_stream = new DicomInputStream(new FileInputStream(input_file));
				list.clear();
				list.read(i_stream);
				Attribute attr = list.get(TagFromName.ImagePositionPatient);
				positions[i] = attr.getDoubleValues();
			}
		}		
		
	}
	
	private void get_pixels_from_mask_files(ArrayList <String> mask_files) throws FileNotFoundException, IOException, DicomException {
		BufferedImage image = null;
		for(int i = 0; i < mask_files.size(); i++) {
			image = ImageIO.read(new File(mask_files.get(i)));
			//BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
			byte [] new_frame = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
			if(pixels == null) {
				pixels = (byte [])new_frame.clone();
			}
			else {	
				byte [] temp = new byte[pixels.length + new_frame.length];
				System.arraycopy(pixels, 0, temp, 0, pixels.length);
				System.arraycopy(new_frame, 0, temp, pixels.length, new_frame.length);
				pixels = (byte [])temp.clone();
			}
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path_to_mask_files = "./DicomFiles/Images/TIFF/"; 
		String path_to_dicom_files =  "./DicomFiles/Images/PET/";
		String output_file = "./TEMP/tiff.sobin";
		
		ArrayList <String> dicom_files = list_files_in_folder_in_alphabetic_order(path_to_dicom_files);
		ArrayList <String> mask_files = list_files_in_folder_in_alphabetic_order(path_to_mask_files);

		try {
			ConvertTIFFMasksToSegmentationObjects obj = new ConvertTIFFMasksToSegmentationObjects(mask_files, dicom_files, output_file);
		}  catch(Exception e) {
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(0);
		}
		
		System.out.println("DICOM segmentation objects file " + output_file + " is generated.");
	}

}