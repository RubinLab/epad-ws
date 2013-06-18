package edu.stanford.isis.test;

import java.io.*; 
import com.pixelmed.dicom.*;

public class SegmentationObjectsFileWriter {

	public static final String Manufacturer = "Stanford University";
	public static final String ManufacturerModelName = "ePAD";
	public static final String DeviceSerialNumber = "SN123456";
	public static final String SoftwareVersion = "2.0.1";
	public static final String SeriesDescription = "Segmentation result";
	
	private AttributeList list;
	private SequenceAttribute segment_sequence;
	private SequenceAttribute shared_functional_groups_sequence;
	private SequenceAttribute per_frame_functional_groups_sequence;
	private Attribute pixel_data;
	private int current_segment;
	private int frame_counter;
	
	/**
	 * @param type is the segmentation type, either "BINARY" or "FRACTIONAL"; 
	 * fractional_type is either "PROBABIITY" or "OCCUPANCY" for fractional type, or null for binary type.
	 * reference is the whole attributes list.  
	 * @throws DicomException 
	 */	
	public SegmentationObjectsFileWriter(String seg_type, String fractional_type, AttributeList original_attrs) throws DicomException {
		/*********************************************************************
		 * Initialize all the global objects. 
		 *********************************************************************/
		current_segment = 0;
		frame_counter = 0;
		pixel_data = new OtherByteAttribute(TagFromName.PixelData);
		list = new AttributeList();
		segment_sequence = new SequenceAttribute(TagFromName.SegmentSequence);
		shared_functional_groups_sequence = new SequenceAttribute(TagFromName.SharedFunctionalGroupsSequence);
		per_frame_functional_groups_sequence = new SequenceAttribute(TagFromName.PerFrameFunctionalGroupsSequence);

		/*********************************************************************
		 * Add constant attributes of segmentation objects. 
		 *********************************************************************/
		{ Attribute a = new UniqueIdentifierAttribute(TagFromName.MediaStorageSOPClassUID); a.addValue(SOPClass.SegmentationStorage); list.put(a); }
		{ Attribute a = new UniqueIdentifierAttribute(TagFromName.TransferSyntaxUID); a.addValue(TransferSyntax.ExplicitVRLittleEndian); list.put(a); }
		{ Attribute a = new UniqueIdentifierAttribute(TagFromName.ImplementationClassUID); a.addValue(SOPClass.SegmentationStorage); list.put(a); }
		{ Attribute a = new UniqueIdentifierAttribute(TagFromName.SOPClassUID); a.addValue(SOPClass.SegmentationStorage); list.put(a); }
		{ Attribute a = new CodeStringAttribute(TagFromName.Modality); a.addValue("SEG"); list.put(a); }
		{ Attribute a = new CodeStringAttribute(TagFromName.ContentLabel); a.addValue("ROI"); list.put(a); }
		{ Attribute a = new UnsignedShortAttribute(TagFromName.SamplesPerPixel); a.addValue(1); list.put(a); }
		{ Attribute a = new CodeStringAttribute(TagFromName.PhotometricInterpretation); a.addValue("MONOCHROME2"); list.put(a); }
		{ Attribute a = new CodeStringAttribute(TagFromName.ImageType); a.addValue("DERIVED"); a.addValue("PRIMARY"); list.put(a); }
		{ Attribute a = new UnsignedShortAttribute(TagFromName.PixelRepresentation); a.addValue(0); list.put(a); }
		{ Attribute a = new ShortStringAttribute(TagFromName.LossyImageCompression); a.addValue("00"); list.put(a); }

		if(seg_type.equalsIgnoreCase("BINARY")) { // 1-bit/pixel
			{ Attribute a = new CodeStringAttribute(TagFromName.SegmentationType); a.addValue("BINARY"); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsAllocated); a.addValue(1); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsStored); a.addValue(1); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.HighBit); a.addValue(0); list.put(a); }
		}
		else { // 8-bit/pixel
			{ Attribute a = new CodeStringAttribute(TagFromName.SegmentationType); a.addValue("FRACTIONAL"); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsAllocated); a.addValue(8); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.MaximumFractionalValue); a.addValue(0xFF); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsStored); a.addValue(8); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.HighBit); a.addValue(7); list.put(a); }
			if(fractional_type.equalsIgnoreCase("OCCUPANCY")) 
				{ Attribute a = new CodeStringAttribute(TagFromName.SegmentationFractionalType); a.addValue("OCCUPANCY"); list.put(a); }
			else
				{ Attribute a = new CodeStringAttribute(TagFromName.SegmentationFractionalType); a.addValue("PROBABILITY"); list.put(a); }
		}
		
		/*********************************************************************
		 * Other attributes. 
		 *********************************************************************/
		{	// Date and time.
			java.util.Date currentDateTime = new java.util.Date();
			String date = new java.text.SimpleDateFormat("yyyyMMdd").format(currentDateTime);
			String time = new java.text.SimpleDateFormat("HHmmss.SSS").format(currentDateTime);
			{ Attribute a = new DateAttribute(TagFromName.StudyDate); a.addValue(date); list.put(a); }
			{ Attribute a = new DateAttribute(TagFromName.SeriesDate); a.addValue(date); list.put(a); }
			{ Attribute a = new DateAttribute(TagFromName.AcquisitionDate); a.addValue(date); list.put(a); }
			{ Attribute a = new DateAttribute(TagFromName.ContentDate); a.addValue(date); list.put(a); }
			{ Attribute a = new TimeAttribute(TagFromName.StudyTime); a.addValue(time); list.put(a); }
			{ Attribute a = new TimeAttribute(TagFromName.SeriesTime); a.addValue(time); list.put(a); }
			{ Attribute a = new TimeAttribute(TagFromName.AcquisitionTime); a.addValue(time); list.put(a); }
			{ Attribute a = new TimeAttribute(TagFromName.ContentTime); a.addValue(time); list.put(a); }
		}
		// Device specific information.
		{ Attribute a = new UniqueIdentifierAttribute(TagFromName.InstanceCreatorUID); a.addValue(VersionAndConstants.instanceCreatorUID); list.put(a); }
		{ Attribute a = new ShortStringAttribute(TagFromName.AccessionNumber); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.SeriesDescription); a.addValue(SeriesDescription); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.Manufacturer); a.addValue(Manufacturer); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.ManufacturerModelName); a.addValue(ManufacturerModelName); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.DeviceSerialNumber); a.addValue(DeviceSerialNumber); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.SoftwareVersion); a.addValue(SoftwareVersion); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.ContentDescription); a.addValue(SeriesDescription); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.ContentCreatorName); a.addValue(ManufacturerModelName); list.put(a); }
		// Default values and generated UIDs.
		String uid = null;
		String study_id = "1"; //Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.StudyID);
		String series_number = "1000"; //Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.SeriesNumber);
		String instance_number = "1"; //Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.InstanceNumber);
		{ Attribute a = new ShortStringAttribute(TagFromName.StudyID); a.addValue(study_id); list.put(a); }
		{ Attribute a = new IntegerStringAttribute(TagFromName.SeriesNumber); a.addValue(series_number); list.put(a); }
		{ Attribute a = new IntegerStringAttribute(TagFromName.InstanceNumber); a.addValue(instance_number); list.put(a); }
		UIDGenerator u = new UIDGenerator();	
		{ Attribute a = new UniqueIdentifierAttribute(TagFromName.SOPInstanceUID); a.addValue(u.getNewSOPInstanceUID(study_id, series_number,instance_number)); list.put(a); }
		{ Attribute a = new UniqueIdentifierAttribute(TagFromName.StudyInstanceUID); a.addValue(u.getNewStudyInstanceUID(study_id)); list.put(a); }
		{
			uid = u.getNewSeriesInstanceUID(study_id, series_number);
			Attribute a = new UniqueIdentifierAttribute(TagFromName.SeriesInstanceUID); 
			a.addValue(uid); 
			list.put(a); 
			a = new UniqueIdentifierAttribute(TagFromName.FrameOfReferenceUID); 
			a.addValue(uid); 
			list.put(a); 
		}
		
		/*********************************************************************
		 * Extract attributes from the original attributes list. 
		 *********************************************************************/
		// Following attributes are copied directly.
		{ 
			Attribute a = new UnsignedShortAttribute(TagFromName.Rows); 
			short temp = (short)Attribute.getSingleIntegerValueOrDefault(original_attrs, TagFromName.Rows, 1);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			Attribute a = new UnsignedShortAttribute(TagFromName.Columns); 
			short temp = (short)Attribute.getSingleIntegerValueOrDefault(original_attrs, TagFromName.Columns, 1);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			Attribute a = new PersonNameAttribute(TagFromName.ReferringPhysicianName); 
			String temp = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.ReferringPhysicianName);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			Attribute a = new PersonNameAttribute(TagFromName.PatientName); 
			String temp = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.PatientName);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			Attribute a = new LongStringAttribute(TagFromName.PatientID); 
			String temp = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.PatientID);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			Attribute a = new DateAttribute(TagFromName.PatientBirthDate); 
			String temp = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.PatientBirthDate);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			Attribute a = new CodeStringAttribute(TagFromName.PatientSex); 
			String temp = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.PatientSex);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			Attribute a = new UniqueIdentifierAttribute(TagFromName.PositionReferenceIndicator);
			String temp = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.PositionReferenceIndicator);
			a.addValue(temp); 
			list.put(a); 
		}
		{ 
			AttributeList a = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(original_attrs, TagFromName.DimensionOrganizationSequence);			
			SequenceAttribute seq = new SequenceAttribute(TagFromName.DimensionOrganizationSequence);
			seq.addItem(a);
			list.put(seq); 
		}
		{ 
			AttributeList a = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(original_attrs, TagFromName.DimensionIndexSequence);			
			SequenceAttribute seq = new SequenceAttribute(TagFromName.DimensionIndexSequence);
			seq.addItem(a);
			list.put(seq); 
		}
		
		// Following attributes are inherited.
		String orig_class_uid = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.SOPClassUID);
		String orig_inst_uid = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.SOPInstanceUID);
		SequenceAttribute pixel_measures_sequence = (SequenceAttribute)SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(
					original_attrs, TagFromName.SharedFunctionalGroupsSequence, TagFromName.PixelMeasuresSequence);
		SequenceAttribute plane_orientation_sequence = (SequenceAttribute)SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(
					original_attrs, TagFromName.SharedFunctionalGroupsSequence, TagFromName.PlaneOrientationSequence);

		// Generate Shared Functional Groups Sequence
		AttributeList reference = new AttributeList(); // Item of DerivationImageSequence.
		{
			AttributeList image = new AttributeList(); // Item of SourceImageSequence.
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue("121322"); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue("DCM"); item.put(a); } 
			{ Attribute a = new LongStringAttribute(TagFromName.CodeMeaning); a.addValue("Source image for image processing operation"); item.put(a); }
			SequenceAttribute seq= new SequenceAttribute(TagFromName.PurposeOfReferenceCodeSequence); 
			seq.addItem(item); 
			image.put(seq); 
			{ Attribute a = new LongStringAttribute(TagFromName.ReferencedSOPClassUID); a.addValue(orig_class_uid); image.put(a); }
			{ Attribute a = new LongStringAttribute(TagFromName.ReferencedInstanceSequence); a.addValue(orig_inst_uid); image.put(a); }
			seq= new SequenceAttribute(TagFromName.SourceImageSequence); 
			seq.addItem(image); 
			reference.put(seq); 
		}
		{
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue("113076"); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue("DCM"); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeMeaning); a.addValue("Segmentation"); item.put(a); }
			SequenceAttribute seq= new SequenceAttribute(TagFromName.DerivationCodeSequence); 
			seq.addItem(item); 
			reference.put(seq); 
		}
		if(pixel_measures_sequence != null) {
			Attribute spacing = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(pixel_measures_sequence, TagFromName.PixelSpacing); 
			list.put(spacing);
			Attribute thickness = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(pixel_measures_sequence, TagFromName.SliceThickness); 
			list.put(thickness);
			
			SequenceAttribute seq= new SequenceAttribute(TagFromName.PixelMeasuresSequence); 
			AttributeList item = new AttributeList();
			item.put(spacing);
			item.put(thickness);
			seq.addItem(item); 
			reference.put(seq);
		}
		if(plane_orientation_sequence != null) {
			Attribute a = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(plane_orientation_sequence, TagFromName.ImageOrientationPatient); 
			SequenceAttribute seq= new SequenceAttribute(TagFromName.PlaneOrientationSequence);
			AttributeList item = new AttributeList();
			item.put(a);
			seq.addItem(item); 
			reference.put(seq);
		}
		{
			SequenceAttribute seq = new SequenceAttribute(TagFromName.DerivationImageSequence);
			seq.addItem(reference);
			AttributeList item = new AttributeList();
			item.put(seq);
			shared_functional_groups_sequence.addItem(item);
		}
		
	}
	
	/**
	 * @param Attributes of the new segment. 
	 * @throws DicomException 
	 */
	public void AddOneSegment(int number, String description, String category, String type) throws DicomException {
		current_segment = number;
		// SegmentSequence attribute
		AttributeList list = new AttributeList();
		{ Attribute a = new UnsignedShortAttribute(TagFromName.SegmentNumber); a.addValue(current_segment); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.SegmentDescription); a.addValue(description); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.SegmentLabel); a.addValue(type); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.SegmentAlgorithmType); a.addValue("SEMIAUTOMATIC"); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.SegmentAlgorithmName); a.addValue(ManufacturerModelName + SoftwareVersion); list.put(a); }
		{ 	
			String [] context = parse_context(category);
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeName); a.addValue(context[0]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue(context[1]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeMeaning); a.addValue(context[2]); item.put(a); }
			SequenceAttribute seq = new SequenceAttribute(TagFromName.AnatomicRegionSequence); 
			seq.addItem(item); 
			list.put(seq); 
		}
		{  
			String [] context = parse_context(category);
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeName); a.addValue(context[0]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue(context[1]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeMeaning); a.addValue(context[2]); item.put(a); }
			SequenceAttribute seq= new SequenceAttribute(TagFromName.SegmentedPropertyCategoryCodeSequence); 
			seq.addItem(item); 
			list.put(seq); 
		}
		{ 
			String [] context = parse_context(type);
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue(context[0]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue(context[1]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeMeaning); a.addValue(context[2]); item.put(a); }
			SequenceAttribute seq= new SequenceAttribute(TagFromName.SegmentedPropertyTypeCodeSequence); 
			seq.addItem(item); 
			list.put(seq); 
		}
		
		SequenceItem item = new SequenceItem(list); 
		segment_sequence.addItem(item);
	}

	/**
	 * @param frames contains a width*height*frame_num sequence; 
	 * @throws DicomException 
	 */
	public void AddAllFrames(byte [] frames, int frame_num) throws DicomException {
		// Add new data to the end of the existing data.
		byte [] current = pixel_data.getByteValues();
		if(current != null) {
			byte [] temp = new byte[current.length + frames.length];
			System.arraycopy(current, 0, temp, 0, current.length);
			System.arraycopy(frames, 0, temp, current.length, frames.length);
			pixel_data.setValues(temp);
		}
		else {	
			byte [] temp = (byte [])frames.clone();
			pixel_data.setValues(temp);
		}
		// Update the frame counter (NumberOfFrames).
		frame_counter += frame_num;
		
		// Generate Per-frame attributes. Refer to Table A.51-2 SEGMENTATION FUNCTIONAL GROUP MACROS
		for(int k = 1; k <= frame_num; k++) { 
			AttributeList item = new AttributeList();
			AttributeList item2 = new AttributeList();
			// Assign SegmentIdentificationSequence.
			SequenceAttribute seq = new SequenceAttribute(TagFromName.SegmentIdentificationSequence);
			Attribute a = new UnsignedShortAttribute(TagFromName.ReferencedSegmentNumber); 
			a.addValue(current_segment); 
			item.put(a);
			seq.addItem(item); 
			item2.put(seq); 
			// Assign FrameContentSequence.
			seq = new SequenceAttribute(TagFromName.FrameContentSequence);
			a = new UnsignedShortAttribute(TagFromName.StackID); 
			a.addValue(current_segment); 
			item = new AttributeList();
			item.put(a);
			a = new UnsignedShortAttribute(TagFromName.InStackPositionNumber); 
			a.addValue(k); 
			item.put(a);
			a = new UnsignedShortAttribute(TagFromName.DimensionIndexValues); 
			a.addValue(1); 
			a.addValue(k); 
			item.put(a);
			seq.addItem(item); 
			item2.put(seq); 
			
			per_frame_functional_groups_sequence.addItem(item2);
		}
	}

	/**
	 * @param Name of the DICOM file.
	 * @throws DicomException 
	 * @throws IOException 
	 */
	public void SaveDicomFile(String file_name) throws IOException, DicomException {
		// Add object Segment Sequence.
		list.put(TagFromName.SegmentSequence, segment_sequence);
		
		// Add object Shared Functional Groups Sequence.
		list.put(TagFromName.SharedFunctionalGroupsSequence, shared_functional_groups_sequence);
		
		// Add object Per-frame Functional Groups Sequence.
		list.put(TagFromName.PerFrameFunctionalGroupsSequence, per_frame_functional_groups_sequence);
		
		// Add object Pixel Data.
		list.put(pixel_data);
		{ Attribute a = new IntegerStringAttribute(TagFromName.NumberOfFrames); a.addValue(frame_counter); list.put(a);	}
		
		// Save the whole list.
		list.write(file_name, TransferSyntax.ExplicitVRLittleEndian, true, true);
	}

	
	/**
	 * @param Input is in "value\\scheme\\meaning" format. Output strings in scheme, value, meaning order.
	 */
	private String [] parse_context(String prop) {
		String [] val = {"Unknown Scheme", "Unknown Value", "Unknown Meaning"};
		try {
			String [] input = prop.split("\\\\");
			val[0] = input[1];
			val[1] = input[0];
			val[2] = input[2];
		} catch(Exception e) {
			System.err.println("Input " + prop + " is not supported!");
			System.err.println("It should be like value\\scheme\\meaning.");
		}
		
		return val;
	}

	/**
	 * @param arg
	 */
	public static void main(String[] args) {

		String input_file = "CT0011";
		String output_file = "segmentation_test_out.dcm";
		
		byte [] pixels = null; 
		SegmentationObjectsFileWriter obj = null;
		short image_width = 0, image_height = 0, image_frames = 0;
		boolean one_bit_per_pixel = true; // Choose 1 bit/pixel or 1 byte/pixel.
						
		try {
			System.out.println("Input DICOM file is " + input_file + ".");
			DicomInputStream i_stream = new DicomInputStream(new FileInputStream(input_file));
			AttributeList list = new AttributeList();
			list.read(i_stream);
			
			{	// Get sequence format.
				image_width = (short)Attribute.getSingleIntegerValueOrDefault(list, TagFromName.Columns, 1);
				image_height = (short)Attribute.getSingleIntegerValueOrDefault(list, TagFromName.Rows, 1);
				image_frames =  (short)Attribute.getSingleIntegerValueOrDefault(list, TagFromName.NumberOfFrames, 1);
			}				
			if(one_bit_per_pixel) {
				pixels = new byte[image_width * image_height * image_frames / 8];
				obj = new SegmentationObjectsFileWriter("BINARY", null, list);
			}
			else {
				pixels = new byte[image_width * image_height * image_frames];
				obj = new SegmentationObjectsFileWriter("FRACTIONAL", "OCCUPANCY", list);
			}

			// Segment 1
			obj.AddOneSegment(1, "Segment No.1 is for ...", SegmentationPropertyCategories.Tissue, SegmentationPropertyTypes.Lung);		
			obj.AddAllFrames(pixels, image_frames);
			
			// Segment 2
			obj.AddOneSegment(2, "Segment No.2 is for ...", SegmentationPropertyCategories.AnatomicalStructure, SegmentationPropertyTypes.AnkleJoint);		
			obj.AddAllFrames(pixels, image_frames);
			
			obj.SaveDicomFile(output_file);
			
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(0);
		}

		System.out.println("DICOM file is generated.");
	}

}