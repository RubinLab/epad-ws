package edu.stanford.isis.tools.pixelmed;

/**
 /**
 * <p>A class for saving segmentation results.</p>
 *
 * @author	Wei Lu (luwei@tju.edu.cn)
 * @date 2012-12
 */


import java.io.*; 
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Set;

import com.pixelmed.dicom.*;
import com.pixelmed.anatproc.CodedConcept;
import com.pixelmed.utils.CopyStream;


public class SegmentationObjectsFileWriter {

	public static final String Manufacturer = "Stanford University";
	public static final String ManufacturerModelName = "ePAD";
	public static final String DeviceSerialNumber = "SN123456";
	public static final String SoftwareVersion = "2.0.1";
	public static final String SeriesDescription = "Segmentation result";
	public static final String SourceApplicationEntityTitle = "Default title";
	
	private AttributeList list = new AttributeList();
	private SequenceAttribute segment_sequence = new SequenceAttribute(TagFromName.SegmentSequence);
	private AttributeList shared_functional_groups_item = new AttributeList();
	private SequenceAttribute per_frame_functional_groups_sequence = new SequenceAttribute(TagFromName.PerFrameFunctionalGroupsSequence);
	private Attribute pixel_data = new OtherByteAttribute(TagFromName.PixelData);;
	private int current_segment = 0;
	private int frame_counter = 0;
	private boolean use_temp_file = false;
	private String temp_file = "temp_pixel_data_file.tmp";	// Will be renamed to a unique value per instance.
	
	/**
	 * Initialize the list with constant attributes and inherited attributes.
	 * @param original_attrs is the whole attributes list.  
	 * @param patient_orientation
	 * @param pixel_spacing
	 * @param slice_thickness
	 * @throws DicomException
	 */
	
	public SegmentationObjectsFileWriter(AttributeList original_attrs, short [] patient_orientation, double [] pixel_spacing, double slice_thickness) throws DicomException {
		/*********************************************************************
		 * Generate a unique name for the temporary pixel data file.
		 *********************************************************************/
		{  
			int rand = (int)(Math.random() * Integer.MAX_VALUE);
			temp_file = Integer.toString(rand) + ".tmp"; // temp_file has a unique name.
		}

		if(original_attrs == null)
			throw(new DicomException("The original attributes must not be null!"));
		
		if(patient_orientation == null || patient_orientation.length != 6)
			throw(new DicomException("Invalid patient orientation!"));
		
		if(pixel_spacing == null || pixel_spacing.length != 2)
			throw(new DicomException("Invalid pixel spacing!"));
		
		if(slice_thickness <= 0)
			throw(new DicomException("Invalid slice thickness!"));

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
		{ Attribute a = new CodeStringAttribute(TagFromName.LossyImageCompression); a.addValue("00"); list.put(a); }
		
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
		{ Attribute a = new PersonNameAttribute(TagFromName.ContentCreatorName); a.addValue(ManufacturerModelName + "^" + SoftwareVersion); list.put(a); }
		
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
		// Add meta information header.
		FileMetaInformation.addFileMetaInformation(list, TransferSyntax.ExplicitVRLittleEndian, SourceApplicationEntityTitle);

		
		/*********************************************************************
		 * Extract attributes from the original attributes list. 
		 *********************************************************************/
		CompositeInstanceContext cic = new CompositeInstanceContext(original_attrs);
		cic.removeInstance();
		cic.removeSeries();
		cic.removeEquipment();
		list.putAll(cic.getAttributeList());

		{ 	// Define dimensions as (stack id, in-stack position, segment number).
			SequenceAttribute seq = new SequenceAttribute(TagFromName.DimensionOrganizationSequence);
			AttributeList a = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(original_attrs, TagFromName.DimensionOrganizationSequence);			
			Attribute org_uid = new UniqueIdentifierAttribute(TagFromName.DimensionOrganizationUID); 
			if(a == null) {
				a = new AttributeList();
				org_uid.addValue(u.getNewUID()); 
				a.put(org_uid);
			} else {
				org_uid.addValue(Attribute.getSingleStringValueOrDefault(a, TagFromName.DimensionOrganizationUID, u.getNewUID()));
			}
			seq.addItem(a);
			list.put(seq); 

			seq = new SequenceAttribute(TagFromName.DimensionIndexSequence);
			a = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(original_attrs, TagFromName.DimensionIndexSequence);			
			if(a == null) {
				a = new AttributeList();
				a.put(org_uid);
				AttributeTagAttribute t = new AttributeTagAttribute(TagFromName.DimensionIndexPointer);
				t.addValue(0x0020, 0x9056); // Stack ID as a dimension
				a.put(t);
				t= new AttributeTagAttribute(TagFromName.FunctionalGroupPointer);
				t.addValue(0x0020, 0x9111); // Frame Content Sequence 
				a.put(t);
				LongStringAttribute lo = new LongStringAttribute(TagFromName.DimensionDescriptionLabel);
				lo.addValue("Stack ID");
				a.put(t);
				seq.addItem(a);

				a = new AttributeList();
				a.put(org_uid);
				t = new AttributeTagAttribute(TagFromName.DimensionIndexPointer);
				t.addValue(0x0020, 0x9057); // In-Stack Position Number as a dimension
				a.put(t);
				t= new AttributeTagAttribute(TagFromName.FunctionalGroupPointer);
				t.addValue(0x0020, 0x9111); // Frame Content Sequence 
				a.put(t);
				lo = new LongStringAttribute(TagFromName.DimensionDescriptionLabel);
				lo.addValue("In-Stack Position Number");
				a.put(t);
				seq.addItem(a);

				/* Temporal dimension is rarely used, so it is not supported here.
				a = new AttributeList();
				a.put(org_uid);
				t = new AttributeTagAttribute(TagFromName.DimensionIndexPointer);
				t.addValue(0x0020, 0x9128); // Temporal Position Index as a dimension
				a.put(t);
				t= new AttributeTagAttribute(TagFromName.FunctionalGroupPointer);
				t.addValue(0x0020, 0x9111); // Frame Content Sequence 
				a.put(t);
				lo = new LongStringAttribute(TagFromName.DimensionDescriptionLabel);
				lo.addValue("Temporal Position Index");
				a.put(t);
				seq.addItem(a);
				*/

				a = new AttributeList();
				a.put(org_uid);
				t = new AttributeTagAttribute(TagFromName.DimensionIndexPointer);
				t.addValue(0x0062, 0x000b); // Referenced Segment Number as a dimension 
				a.put(t);
				t= new AttributeTagAttribute(TagFromName.FunctionalGroupPointer);
				t.addValue(0x0062, 0x000a); // Segment Identification Sequence 
				a.put(t);
				lo = new LongStringAttribute(TagFromName.DimensionDescriptionLabel);
				lo.addValue("Referenced Segment Number");
				a.put(t);
				seq.addItem(a);
			} else {
				seq.addItem(a);
			}
			list.put(seq); 
		}
		
		// Attributes below are not mandatory, so special check needs to be done.
		{ 
			String temp = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.PositionReferenceIndicator);
			Attribute a = new LongStringAttribute(TagFromName.PositionReferenceIndicator);
			a.addValue(temp); 
			list.put(a); 
		}

		// Following attributes are inherited.
		String orig_class_uid = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.SOPClassUID);
		String orig_inst_uid = Attribute.getSingleStringValueOrEmptyString(original_attrs, TagFromName.SOPInstanceUID);

		// Generate Shared Functional Groups Sequence
		{	// DerivationImageSequence
			SequenceAttribute derivation_image_seq = new SequenceAttribute(TagFromName.DerivationImageSequence);
			AttributeList reference = new AttributeList(); 
			{
				AttributeList item = new AttributeList();
				{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue("113076"); item.put(a); } 
				{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue("DCM"); item.put(a); } 
				{ Attribute a = new LongStringAttribute(TagFromName.CodeMeaning); a.addValue("Segmentation"); item.put(a); }
				SequenceAttribute seq= new SequenceAttribute(TagFromName.DerivationCodeSequence); 
				seq.addItem(item); 
				reference.put(seq); 
			}
			{	// SourceImageSequence
				SequenceAttribute source_image_seq = new SequenceAttribute(TagFromName.SourceImageSequence);
				AttributeList image = new AttributeList(); 
				AttributeList item = new AttributeList();
				{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue("121322"); item.put(a); } 
				{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue("DCM"); item.put(a); } 
				{ Attribute a = new LongStringAttribute(TagFromName.CodeMeaning); a.addValue("Source image for image processing operation"); item.put(a); }
				SequenceAttribute seq= new SequenceAttribute(TagFromName.PurposeOfReferenceCodeSequence); 
				seq.addItem(item); 
				image.put(seq); 
				{ Attribute a = new UniqueIdentifierAttribute(TagFromName.ReferencedSOPClassUID); a.addValue(orig_class_uid); image.put(a); }
				{ Attribute a = new UniqueIdentifierAttribute(TagFromName.ReferencedSOPInstanceUID); a.addValue(orig_inst_uid); image.put(a); }
				source_image_seq.addItem(image); 
				reference.put(source_image_seq);
			}
			derivation_image_seq.addItem(reference);
			shared_functional_groups_item.put(derivation_image_seq);
		}
		{	// PlaneOrientationSequence
			SequenceAttribute plane_orientation_seq= new SequenceAttribute(TagFromName.PlaneOrientationSequence);
			AttributeList item = new AttributeList();
			Attribute a = new DecimalStringAttribute(TagFromName.ImageOrientationPatient); 
			for(int i = 0; i < patient_orientation.length; i++)
				a.addValue(patient_orientation[i]);
			item.put(a);
			plane_orientation_seq.addItem(item); 
			shared_functional_groups_item.put(plane_orientation_seq);
		}
		{	// PixelMeasuresSequence
			SequenceAttribute pixel_measures_seq= new SequenceAttribute(TagFromName.PixelMeasuresSequence); 
			AttributeList item = new AttributeList();
			Attribute spacing = new DecimalStringAttribute(TagFromName.PixelSpacing); spacing.addValue(pixel_spacing[0]); spacing.addValue(pixel_spacing[1]);
			Attribute thickness = new DecimalStringAttribute(TagFromName.SliceThickness); thickness.addValue(slice_thickness);
			item.put(spacing);
			item.put(thickness);
			pixel_measures_seq.addItem(item); 
			shared_functional_groups_item.put(pixel_measures_seq);
		}
	}
	
	/**
	 * Add a segment.
	 * @param description is the user defined string which may be the purpose of segmenting.
	 * @param category should be a value defined in future SegmentationPropertyCategories. 
	 * @param type should be a value defined in future SegmentationPropertyTypes.
	 * @throws DicomException
	 */
	public void AddOneSegment(String description, CodedConcept category, CodedConcept type) throws DicomException {
		// Validate the parameters.
		current_segment++;
		
		if(description == null) {
			description = "Segment No." + Integer.toString(current_segment);
		}
		
		// SegmentSequence attribute
		AttributeList list = new AttributeList();
		{ Attribute a = new UnsignedShortAttribute(TagFromName.SegmentNumber); a.addValue(current_segment); list.put(a); }
		{ Attribute a = new ShortTextAttribute(TagFromName.SegmentDescription); a.addValue(description); list.put(a); }
		{  
			String [] context = parse_context(type);
			Attribute a = new LongStringAttribute(TagFromName.SegmentLabel); 
			a.addValue(context[2]); 
			list.put(a); 
		}
		{ Attribute a = new CodeStringAttribute(TagFromName.SegmentAlgorithmType); a.addValue("SEMIAUTOMATIC"); list.put(a); }
		{ Attribute a = new LongStringAttribute(TagFromName.SegmentAlgorithmName); a.addValue(ManufacturerModelName + SoftwareVersion); list.put(a); }
		{ 	
			String [] context = parse_context(category);
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue(context[0]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue(context[1]); item.put(a); } 
			{ Attribute a = new LongStringAttribute(TagFromName.CodeMeaning); a.addValue(context[2]); item.put(a); }
			SequenceAttribute seq = new SequenceAttribute(TagFromName.AnatomicRegionSequence); 
			seq.addItem(item); 
			list.put(seq); 
		}
		{  
			String [] context = parse_context(category);
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue(context[0]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue(context[1]); item.put(a); } 
			{ Attribute a = new LongStringAttribute(TagFromName.CodeMeaning); a.addValue(context[2]); item.put(a); }
			SequenceAttribute seq= new SequenceAttribute(TagFromName.SegmentedPropertyCategoryCodeSequence); 
			seq.addItem(item); 
			list.put(seq); 
		}
		{ 
			String [] context = parse_context(type);
			AttributeList item = new AttributeList();
			{ Attribute a = new ShortStringAttribute(TagFromName.CodingSchemeDesignator); a.addValue(context[0]); item.put(a); } 
			{ Attribute a = new ShortStringAttribute(TagFromName.CodeValue); a.addValue(context[1]); item.put(a); } 
			{ Attribute a = new LongStringAttribute(TagFromName.CodeMeaning); a.addValue(context[2]); item.put(a); }
			SequenceAttribute seq= new SequenceAttribute(TagFromName.SegmentedPropertyTypeCodeSequence); 
			seq.addItem(item); 
			list.put(seq); 
		}
		
		SequenceItem item = new SequenceItem(list); 
		segment_sequence.addItem(item);
	}

	/**
	 * Add a sequence of frames to current segment.
	 * @param frames contains a width*height*frame_num sequence; 
	 * @param type is either "PROBABILITY" or "OCCUPANCY" for fractional type, or "BINARY"/null for binary type.
	 * @param stack_id is the index of current stack.
	 * @param positions is the position of each frame.
	 * @throws DicomException
	 * @throws IOException
	 */
	public void AddAllFrames(byte [] frames, int frame_num, int image_width, int image_height, String type, short stack_id, double [][] positions) 
			throws DicomException, IOException {
		// Validate the input data and parameter.
		if(frames == null) {
			throw(new DicomException("There is no pixel data!"));
		}
		
		if(frame_num <= 0 || image_width <= 0 || image_height <= 0) {
			throw(new DicomException("Image size is not correct!"));
		}
		
		if(type == null)
			type = "BINARY";
		else if(!type.equalsIgnoreCase("PROBABILITY") && !type.equalsIgnoreCase("OCCUPANCY"))
			type = "BINARY";
		
		if(!type.equalsIgnoreCase("BINARY")) { // 8-bit/pixel
			if(frames.length != image_width * image_height * frame_num) {
				throw(new DicomException("Image size or type is not correct!"));
			}
		}
		else if(frames.length != (image_width * image_height * frame_num - 1)/ 8 + 1) {
			throw(new DicomException("Image size or type is not correct!"));
		}
		
		if(frame_counter == 0) { // Record the segmentation type at the first time.
			if(!type.equalsIgnoreCase("BINARY")) { // 8-bit/pixel
				{ Attribute a = new CodeStringAttribute(TagFromName.SegmentationType); a.addValue("FRACTIONAL"); list.put(a); }
				{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsAllocated); a.addValue(8); list.put(a); }
				{ Attribute a = new UnsignedShortAttribute(TagFromName.MaximumFractionalValue); a.addValue(0xFF); list.put(a); }
				{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsStored); a.addValue(8); list.put(a); }
				{ Attribute a = new UnsignedShortAttribute(TagFromName.HighBit); a.addValue(7); list.put(a); }
				{ Attribute a = new CodeStringAttribute(TagFromName.SegmentationFractionalType); a.addValue(type.toUpperCase()); list.put(a); }
			}
			else { // 1-bit/pixel
				{ Attribute a = new CodeStringAttribute(TagFromName.SegmentationType); a.addValue("BINARY"); list.put(a); }
				{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsAllocated); a.addValue(1); list.put(a); }
				{ Attribute a = new UnsignedShortAttribute(TagFromName.BitsStored); a.addValue(1); list.put(a); }
				{ Attribute a = new UnsignedShortAttribute(TagFromName.HighBit); a.addValue(0); list.put(a); }
			}
		} else { // All the segmentation objects MUST have the same data type.
			String s_type = Attribute.getSingleStringValueOrDefault(list, TagFromName.SegmentationFractionalType, "BINARY");
			if(!s_type.equalsIgnoreCase(type))
				throw(new DicomException("All the segmentation objects MUST have same data type!"));
		}
		
		if(frame_counter == 0) { // Record the rows and columns at the first time.
			Attribute a = new UnsignedShortAttribute(TagFromName.Rows); 
			a.addValue(image_height); 
			list.put(a); 
			a = new UnsignedShortAttribute(TagFromName.Columns); 
			a.addValue(image_width); 
			list.put(a); 
		}
		else { // Check whether the new frames have the same resolution.
			short height = (short)Attribute.getSingleIntegerValueOrDefault(list, TagFromName.Rows, 1);
			short width = (short)Attribute.getSingleIntegerValueOrDefault(list, TagFromName.Columns, 1);
			if(height != image_height || width != image_width)
				throw(new DicomException("Image size MUST be same!"));
		}

		// Add new data to the end of the existing data.
		if(use_temp_file == false) { // Save pixel data to memory.
			try {
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
			} catch(java.lang.OutOfMemoryError e) {
				use_temp_file = true;
				byte [] data = pixel_data.getByteValues();
				if(data != null)
					save_pixeldata_to_temp_file(data, temp_file, false);
				if(frames != null)
					save_pixeldata_to_temp_file(frames, temp_file, true);
			}
		}
		else { // Save pixel data to a temporary file.
			if(frames != null)
				save_pixeldata_to_temp_file(frames, temp_file, true);
		}
		
		// Sort the frames according to their positions.
		int [] in_stack_position = sort_frames_by_position(positions, frame_num);

		// Generate Per-frame attributes. Refer to Table A.51-2 SEGMENTATION FUNCTIONAL GROUP MACROS
		for(int k = 1; k <= frame_num; k++) { 
			AttributeList item2 = new AttributeList();
			
			{	
				AttributeList item = new AttributeList();
				SequenceAttribute seq = new SequenceAttribute(TagFromName.SegmentIdentificationSequence);
				Attribute a = new UnsignedShortAttribute(TagFromName.ReferencedSegmentNumber); 
				a.addValue(current_segment); 
				item.put(a);
				seq.addItem(item); 
				item2.put(seq); 
			}
			
			{	// Assign FrameContentSequence.
				SequenceAttribute seq = new SequenceAttribute(TagFromName.FrameContentSequence);
				AttributeList item = new AttributeList();
				Attribute a = new ShortStringAttribute(TagFromName.StackID); 
				stack_id = stack_id > 0 ? stack_id : 1;
				a.addValue(Integer.toString(stack_id)); 
				item.put(a);
				a = new UnsignedLongAttribute(TagFromName.InStackPositionNumber); 
				a.addValue(in_stack_position[k - 1]); 
				item.put(a);
				a = new UnsignedLongAttribute(TagFromName.DimensionIndexValues); 
				a.addValue(stack_id); a.addValue(in_stack_position[k - 1]); a.addValue(current_segment); 
				item.put(a);
				seq.addItem(item); 
				item2.put(seq); 
			}
			
			{	// Assign PlanePositionSequence
				SequenceAttribute seq = new SequenceAttribute(TagFromName.PlanePositionSequence);
				AttributeList item = new AttributeList();
				Attribute a = new DecimalStringAttribute(TagFromName.ImagePositionPatient);
				if(positions != null) {
					a.addValue(positions[k - 1][0]); 
					a.addValue(positions[k - 1][1]); 
					a.addValue(positions[k - 1][2]);
				} else {
					a.addValue(0); a.addValue(0); a.addValue(0);
				}
				item.put(a);
				seq.addItem(item); 
				item2.put(seq); 
			}	
			
			per_frame_functional_groups_sequence.addItem(item2);
		}
		
		// Update the frame counter (NumberOfFrames).
		frame_counter += frame_num;
		{ Attribute a = new IntegerStringAttribute(TagFromName.NumberOfFrames); a.addValue(frame_counter); list.put(a);	}
	}

	/**
	 * Finally save the information to a DICOM file.
	 * @param Name of the DICOM file.
	 * @throws DicomException 
	 * @throws IOException 
	 */
	public void SaveDicomFile(String file_name) throws IOException, DicomException {
		// Add object Segment Sequence.
		list.put(TagFromName.SegmentSequence, segment_sequence);
		
		// Combine attributes that can be shared.
		combine_shared_attributes(shared_functional_groups_item, per_frame_functional_groups_sequence);
		// Add object Shared Functional Groups Sequence.
		SequenceAttribute shared_functional_groups_sequence = new SequenceAttribute(TagFromName.SharedFunctionalGroupsSequence);
		shared_functional_groups_sequence.addItem(shared_functional_groups_item);
		list.put(TagFromName.SharedFunctionalGroupsSequence, shared_functional_groups_sequence);
		// Add object Per-frame Functional Groups Sequence.
		list.put(TagFromName.PerFrameFunctionalGroupsSequence, per_frame_functional_groups_sequence);
		
		if(use_temp_file) {
			list.write(file_name, TransferSyntax.ExplicitVRLittleEndian, true, true);
			append_pixeldata_attribute(temp_file, file_name);
		}
		else {
			// Add object Pixel Data.
			list.put(pixel_data);
			// Save the whole list.
			list.write(file_name, TransferSyntax.ExplicitVRLittleEndian, true, true);
		}
	}

	
	/**
	 * @param Output strings in scheme, value, meaning order.
	 */
	private String [] parse_context(CodedConcept prop) {
		String [] val = {"Unknown Scheme", "Unknown Value", "Unknown Meaning"};

		try {
			val[0] = prop.getCodingSchemeDesignator();
			val[1] = prop.getCodeValue();
			val[2] = prop.getCodeMeaning();
		} catch(Exception e) {
			System.err.println("The property is not a valid CodedConcept object!");
			val[0] = "Unknown Scheme";
			val[1] = "Unknown Value";
			val[2] = "Unknown Meaning";
		}
		
		return val;
	}
	
	/**
	 * @param pixels contain the pixel data.
	 * @param pixel_file is the name of the temporary file.
	 * @param append is false when the new file is created and append is true when new data is appended to the existing file.
	 * @throws IOException
	 */
	private void save_pixeldata_to_temp_file(byte [] pixels, String pixel_file, boolean append) throws IOException {
		OutputStream temp = new FileOutputStream(pixel_file, append);
		temp.write(pixels);
		temp.close();
	}
	
	/**
	 * This function calls PixelMed methods to generate PixelData attribute and append it to the output.
	 * @param pixel_file: This file stores raw pixel data.
	 * @param output_file: This file is the final DICOM segmentation objects output.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DicomException
	 */
	private void append_pixeldata_attribute(String pixel_file, String output_file) throws FileNotFoundException, IOException, DicomException {
		String tmp_tag_file = "TAG_" + pixel_file; // File with tag.
		
		{	// Copy raw pixel data to a tagged file.
			File i = new File(pixel_file);
			DicomOutputStream o = new DicomOutputStream(new FileOutputStream(tmp_tag_file), 
					TransferSyntax.ExplicitVRLittleEndian, TransferSyntax.ExplicitVRLittleEndian);
			OtherByteAttributeOnDisk data = new OtherByteAttributeOnDisk(TagFromName.PixelData, i.length(), 
													new DicomInputStream(i, TransferSyntax.ExplicitVRLittleEndian, false), 0);
			data.write(o);
			o.close();
			i.delete();	// Delete the temporary raw data file.	
		}
		
		{	// Append the tagged file to the output.
			File i = new File(tmp_tag_file);
			DataInputStream pixels = new DataInputStream((new FileInputStream(i)));
			FileOutputStream o = new FileOutputStream(output_file, true);
			// Skip the 132-byte DICOM header.
			CopyStream.skipInsistently(pixels, 132);
			CopyStream.copy(pixels, o);
			
			o.close();
			pixels.close();
			i.delete();	// Delete the temporary file.
		}
	}

	/**
	 * Moved common attributes from per-frame functional group to shared functional group.
	 * @param shared_group contains the attributes in SharedFunctionalGroup.
	 * @param per_frame_sequence is the attribute sequence of PerFrameFunctionalGroup.
	 * @throws DicomException
	 */
	private void combine_shared_attributes(AttributeList shared_group, SequenceAttribute per_frame_sequence) throws DicomException {
		// Check all the items in per_frame_sequence and move the common attributes to shared_group.
		AttributeTag [] checked_attrs =  new AttributeTag[] {TagFromName.SegmentIdentificationSequence, TagFromName.PlanePositionSequence 
				/*, TagFromName.StackID, TagFromName.PlaneOrientationSequence, TagFromName.SliceThickness, TagFromName.PixelSpacing*/};
		Hashtable <AttributeTag, SequenceAttribute> common_attrs = get_common_attributes(checked_attrs, per_frame_sequence);
		add_attributes_to_shared_group(common_attrs, shared_group);
		remove_attributes_from_sequence(common_attrs.keySet(), per_frame_sequence);
	}
	
	/**
	 * Test if the attribute is common to all frames.
	 * @param tags provides the TagName of the attributes to be checked. 
	 * @param sequence is the attribute sequence of all frames.
	 * @return Return (key, value) of common attributes.
	 * @throws DicomException 
	 */
	private Hashtable <AttributeTag, SequenceAttribute> get_common_attributes(AttributeTag [] tags, SequenceAttribute sequence) throws DicomException  {
		Hashtable <AttributeTag, SequenceAttribute> attrs = new Hashtable <AttributeTag, SequenceAttribute>();
		for(AttributeTag tag : tags) {
			attrs.put(tag, new SequenceAttribute(tag));
		}

		Iterator <SequenceItem> it = sequence.iterator();
		boolean initial = true;
		while(it.hasNext()) {
			AttributeList l = it.next().getAttributeList();
			if(initial) {
				// Initialize the attributes.
				initial = false;
				Enumeration <AttributeTag> i = attrs.keys();
				while(i.hasMoreElements()) {
					AttributeTag key = i.nextElement();
					SequenceAttribute seq = null;
					// Since some keys are not the top-level attributes, get their parent sequence attributes instead. 
					if(key.equals(TagFromName.StackID)) {
						seq = (SequenceAttribute)l.get(TagFromName.FrameContentSequence);
					}
					else if(key.equals(TagFromName.SliceThickness) || key.equals(TagFromName.PixelSpacing)) {
						seq = (SequenceAttribute)l.get(TagFromName.PixelMeasuresSequence);
					}
					else {
						seq = (SequenceAttribute)l.get(key);
					}
					
					if(seq == null)
						seq = new SequenceAttribute(key);
					
					attrs.put(key, seq);
				}
			} else {
				// Check if the values are equal.
				Enumeration <AttributeTag> i = attrs.keys();
				while(i.hasMoreElements()) {
					AttributeTag key = i.nextElement(); 
					SequenceAttribute seq = null;
					// Since some keys are not the top-level attributes, get their parent sequence attributes instead. 
					if(key.equals(TagFromName.StackID))
						seq = (SequenceAttribute)l.get(TagFromName.FrameContentSequence);
					else if(key.equals(TagFromName.SliceThickness) || key.equals(TagFromName.PixelSpacing))
						seq = (SequenceAttribute)l.get(TagFromName.PixelMeasuresSequence);
					else
						seq = (SequenceAttribute)l.get(key);
					if(compare_sequence_attribute(key, attrs.get(key), seq) != true)
						attrs.remove(key);
				}
			}
		}
		
		return attrs;
	}
	
	private boolean compare_sequence_attribute(AttributeTag tag, SequenceAttribute s1, SequenceAttribute s2) throws DicomException {
		if(s1 == null || s2 == null) 
			return false;
		
		boolean equal = false;
		
		if(tag.equals(TagFromName.SegmentIdentificationSequence)) {
			Attribute a1 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s1, TagFromName.ReferencedSegmentNumber);
			Attribute a2 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s2, TagFromName.ReferencedSegmentNumber);
			short [] val1 = a1.getShortValues();
			short [] val2 = a2.getShortValues();
			for(int i = 0; i < val1.length; i++) {
				equal = val1[i] == val2[i];
				if(!equal) break;
			}
		}
		else if(tag.equals(TagFromName.StackID)) {
			Attribute a1 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s1, TagFromName.StackID);
			Attribute a2 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s2, TagFromName.StackID);
			short [] val1 = a1.getShortValues();
			short [] val2 = a2.getShortValues();
			for(int i = 0; i < val1.length; i++) {
				equal = val1[i] == val2[i];
				if(!equal) break;
			}
		}
		else if(tag.equals(TagFromName.PlanePositionSequence)) {
			Attribute a1 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s1, TagFromName.ImagePositionPatient);
			Attribute a2 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s2, TagFromName.ImagePositionPatient);
			double [] val1 = a1.getDoubleValues();
			double [] val2 = a2.getDoubleValues();
			for(int i = 0; i < val1.length; i++) {
				equal = val1[i] == val2[i];
				if(!equal) break;
			}
		}
		else if(tag.equals(TagFromName.PlaneOrientationSequence)) {
			Attribute a1 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s1, TagFromName.ImageOrientationPatient);
			Attribute a2 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s2, TagFromName.ImageOrientationPatient);
			short [] val1 = a1.getShortValues();
			short [] val2 = a2.getShortValues();
			for(int i = 0; i < val1.length; i++) {
				equal = val1[i] == val2[i];
				if(!equal) break;
			}
		}
		else if(tag.equals(TagFromName.SliceThickness)) {
			Attribute a1 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s1, TagFromName.SliceThickness);
			Attribute a2 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s2, TagFromName.SliceThickness);
			double [] val1 = a1.getDoubleValues();
			double [] val2 = a2.getDoubleValues();
			for(int i = 0; i < val1.length; i++) {
				equal = val1[i] == val2[i];
				if(!equal) break;
			}
		}
		else if(tag.equals(TagFromName.PixelSpacing)) {
			Attribute a1 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s1, TagFromName.PixelSpacing);
			Attribute a2 = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(s2, TagFromName.PixelSpacing);
			double [] val1 = a1.getDoubleValues();
			double [] val2 = a2.getDoubleValues();
			for(int i = 0; i < val1.length; i++) {
				equal = val1[i] == val2[i];
				if(!equal) break;
			}
		}
		
		return equal;
	}

	private void remove_attributes_from_sequence(Set <AttributeTag> tags, SequenceAttribute sequence)  {
		Iterator <SequenceItem> it = sequence.iterator();
		while(it.hasNext()) {
			AttributeList l = it.next().getAttributeList();
			Iterator <AttributeTag> i = tags.iterator();
			while(i.hasNext()) {
				AttributeTag tag = i.next(); 
				if(tag.equals(TagFromName.StackID)) {
					AttributeList seq = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(l, TagFromName.FrameContentSequence);
					seq.remove(tag);
				}
				else if(tag.equals(TagFromName.SliceThickness) || tag.equals(TagFromName.PixelSpacing)) {
					AttributeList seq = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(l, TagFromName.PixelMeasuresSequence);
					seq.remove(tag);
					if(seq.isEmpty())
						l.remove(TagFromName.PixelMeasuresSequence);	// Delete this attribute if it is empty.
				}
				else
					l.remove(tag);
			}
		}
	}

	private void add_attributes_to_shared_group(Hashtable <AttributeTag, SequenceAttribute> attrs, AttributeList shared)  {
		/*{TagFromName.SegmentIdentificationSequence, TagFromName.StackID, TagFromName.PlanePositionSequence, 
		TagFromName.PlaneOrientationSequence, TagFromName.SliceThickness, TagFromName.PixelSpacing}*/
		Enumeration <AttributeTag> i = attrs.keys();
		while(i.hasMoreElements()) {
			AttributeTag tag = i.nextElement(); 
			if(tag.equals(TagFromName.StackID)) {
				Attribute a = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(attrs.get(tag), tag);
				if(a == null) continue;
				SequenceAttribute seq = new SequenceAttribute(TagFromName.FrameContentSequence);
				AttributeList item = new AttributeList();
				item.put(a);
				seq.addItem(item); 
				shared.put(seq); 
			}
			else if(tag.equals(TagFromName.SliceThickness) || tag.equals(TagFromName.PixelSpacing)) {
				Attribute a = SequenceAttribute.getNamedAttributeFromWithinSequenceWithSingleItem(attrs.get(tag), tag);
				if(a == null) continue;
				SequenceAttribute pixel_measures_seq = (SequenceAttribute)shared.get(TagFromName.PixelMeasuresSequence); 
				if(pixel_measures_seq != null) { // If PixelMeasuresSequence is already there, add one attribute to it instead of rewriting it.
					AttributeList l = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(pixel_measures_seq);
					l.put(a);
				} else {
					SequenceAttribute seq = new SequenceAttribute(TagFromName.PixelMeasuresSequence);
					AttributeList item = new AttributeList();
					item.put(a);
					seq.addItem(item); 
					shared.put(seq);
				}
			}
			else 
				shared.put(attrs.get(tag));
		}
	}
	

	/**
	 * Sort the frames by their position.
	 * @param positions are the geometry attribute of the input frame.
	 * @param frame_num is the number of frames.
	 * @return index ordered by frame's position.
	 * @throws DicomException
	 */
	private int [] sort_frames_by_position(double [][] positions, int frame_num) throws DicomException {
		int [] index = new int[frame_num];
		
		for(int i = 0; i < frame_num; i++) {
			index[i] = i;
		}
		
		return index;
	}
	

	/**
	 * This demo gets segmentation maps from map_file, then inserts the maps twice as two segments.
	 * @param args: java SegmentationObjectsFileWriter dicom_file map_file output_file mode
	 * mode is "BINARY" or "FRACTIONAL". 
	 */
	public static void main(String[] args) {
		String input_file = args[0];//"./DicomFiles/CT0010";//
		String map_file = args[1];//"./TEMP/CT0010.mapbin";//
		String output_file = args[2];//"./TEMP/CT0010.sobin";//"segmentation_test_out.dcm";
		String mode = args[3]; //"BINARY";
		
		byte [] pixels = null; 
		SegmentationObjectsFileWriter obj = null;
		short image_width = 0, image_height = 0, image_frames = 0;
		
		// Read pixel array from the map_file.
		File file = new File(map_file);
		pixels = new byte[(int)file.length()];
		try {
			DataInputStream dis;
			dis = new DataInputStream((new FileInputStream(file)));
			dis.readFully(pixels);
			dis.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
				
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
			
			short [] orientation = new short [] {1, 0, 0, 0, 0, 1};
			double [] spacing = new double [] {0.65, 0.8};
			double thickness = 0.5;
			
			
			obj = new SegmentationObjectsFileWriter(list, orientation, spacing, thickness); 

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
			double [][] positions = new double[image_frames][3];
			/*void AddAllFrames(byte [] frames, int frame_num, int image_width, int image_height, String type, 
					double [][] positions, short [] patient_orientation, double slice_thickness, double [] pixel_spacing, short stack_id)*/ 			
			// Segment 1
			obj.AddOneSegment("Segment No.1 is for ...", category, type);		
			obj.AddAllFrames(pixels, image_frames, image_width, image_height, "binary", (short)0, positions);
			short stack_id = 2;
			positions[0][0] = 0.2;
			obj.AddAllFrames(pixels, image_frames, image_width, image_height, "binary", stack_id, positions);
			// Segment 2
			//obj.AddOneSegment(null, null, null);
			obj.AddOneSegment("Segment No.2 is for ...", category, type);		
			obj.AddAllFrames(pixels, image_frames, image_width, image_height, "binary", (short)1, positions);
			obj.AddAllFrames(pixels, image_frames, image_width, image_height, "binary", stack_id, positions);
			
			obj.SaveDicomFile(output_file);
			
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace(System.err);
			System.exit(0);
		}

		System.out.println("DICOM file " + output_file + " is generated.");
	}

}