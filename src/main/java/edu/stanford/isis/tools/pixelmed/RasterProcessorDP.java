package edu.stanford.isis.tools.pixelmed;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
//import org.acr.Dicom.Logger;

public class RasterProcessorDP {
	/**
	 * Logger object.
	 */
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * Controls amount of diagnostic output.
	 */
	protected int debugLevel = 0;
	/**
	 * Getter for {@link #debugLevel} property.
	 * @return value of property
	 */
	public int getDebugLevel() {
		return debugLevel;
	}
	/**
	 * Setter for {@link #debugLevel} property.
	 * @param value value for property
	 */
	public void setDebugLevel(int value) {
		debugLevel = value;
	}

	/**
	 * Class to generate a histogram of values.
	 * @author Bradley Ross
	 *
	 */
	protected static class Distribution {
		/**
		 * Descriptive text for listings.
		 */
		protected String desc = new String();
		/**
		 * Setter for {@link #desc} property.
		 * @param value value for property
		 */
		public void setDesc(String value) {
			if (value == null) { desc = new String(); }
			desc = value;
		}
		/**
		 * Getter for {@link #desc} property.
		 * @return value of property
		 */
		public String getDesc() {
			return desc;
		}
		/**
		 * Minimum value encountered for data values.
		 */
		protected float minimum = 0.0f;
		/**
		 * Maximum value encountered for data values.
		 */
		protected float maximum = 0.0f;
		/**
		 * Low end of histogram range.
		 */
		protected float lowValue = 0.0f;
		/**
		 * High end of histogram range.
		 */
		protected float highValue = 0.0f;
		/**
		 * Size of each step in histogram.
		 */
		protected float increment = 0.0f;
		/**
		 * Counters for each range in histogram.
		 */
		int[] dist = null;
		/**
		 * Total count of data values.
		 */
		int count = 0;
		/**
		 * Constructor specifying range for values.
		 * @param bottom lowest value for histogram
		 * @param top highest value for histogram
		 * 
		 */
		public Distribution(float bottom, float top) {
			starter(bottom, top, 50);
		}
		/**
		 * Constructor specifying range for values and number of increments.
		 * @param bottom lowest value for histogram
		 * @param top highest value for histogram
		 * @param increments number of increments
		 */
		public Distribution(float bottom, float top, int increments) {
			starter(bottom, top, increments);
		}
		/**
		 * Initializes variables.
		 * @param bottom lowest value for histogram
		 * @param top highest value for histogram
		 * @param increments number of increments
		 */
		protected void starter(float bottom, float top, int increments) {
			count = 0;
			dist = new int[increments + 1];
			if (top > bottom) {
				lowValue = bottom; 
				highValue = top;
			} else {
				lowValue = top;
				highValue = bottom;
			}
			for (int i = 0; i < dist.length; i++) {
				dist[i] = 0;
			}
			minimum = highValue + 2000.0f;
			maximum = lowValue  - 2000.0f;
			increment = (highValue - lowValue) / (dist.length - 1);
		}
		/**
		 * Add a data point to the histogram.
		 * @param value data value
		 */
		public void add(float value) {
			count++;
			if (minimum > value) { minimum = value; }
			if (maximum < value) { maximum = value; }
			int loc = (int) ((value - lowValue) / increment);
			if (loc < 0) {
				loc = 0; 
			} else if (loc > dist.length - 1) {
				loc = dist.length - 1;
			}
			dist[loc]++;
		}
		/**
		 * Display the histogram.
		 */
		public void print() {
			System.out.println();
			System.out.println("Processing distribution " + getDesc());
			System.out.println("There are " + Integer.toString(count) + " items");
			System.out.println("Range is " + Float.toString(lowValue) + " to " + Float.toString(highValue));
			System.out.println("Minimum value is " + Float.toString(minimum));
			System.out.println("Maximum value is " + Float.toString(maximum));
			for (int i = 0; i < dist.length; i++) {
				System.out.println(String.format("%3d  %8.2f  %8d", i, lowValue + increment*i, dist[i] ));
			}
		}
	}
	/**
	 * Modality LUT.
	 * <p>See part 3, Annex C.11.1.1</p>
	 * <p>One way of describing the mapping is to use
	 *    {@link Tag#RescaleSlope} and {@link Tag#RescaleIntercept}.</p>
	 * <ul>
	 * </ul>
	 * <p>The other method is to use {Tag.ModalityLUTSequence}.  In this case the
	 *    following items are used within the nested Dicom object.</p>
	 * <ul>
	 * <li><p>{@link Tag#LUTDescriptor} Three integers holding the number of entries in the 
	 *        lookup table, first stored pixel value mapped, and the number of
	 *        bits in LUTData.</p></li>
	 * <li>{@link Tag#LUTExplanation}</li>
	 * <li>{@link Tag#ModalityLUTType}</li>
	 * <li>{@link Tag#LUTData}</li>
	 * <li>{@link Tag#RescaleIntercept}</li>
	 * <li>{@link Tag#RescaleSlope}</li>
	 * <li>{@link Tag#RescaleType}</li>
	 * </ul>
	 * @author Bradley Ross
	 *
	 */
	public class MLUT {
		protected DicomObject modalityLUT = null;
		protected int[] lutDescriptor;
		protected float rescaleSlope =  1.0f ;
		protected float rescaleIntercept =  0.0f ;
		public MLUT(DicomObject value) {
			if (value.containsValue(Tag.RescaleSlope)) {
				rescaleSlope = value.getFloat(Tag.RescaleSlope);
			}
			if (value.containsValue(Tag.RescaleIntercept)) {
				rescaleIntercept = value.getFloat(Tag.RescaleIntercept);
			}
			if (value.containsValue(Tag.ModalityLUTSequence)) {
				modalityLUT = value.getNestedDicomObject(Tag.ModalityLUTSequence);
				lutDescriptor = value.getInts(Tag.LUTDescriptor);
				rescaleSlope = modalityLUT.getFloat(Tag.RescaleSlope);
				rescaleIntercept = modalityLUT.getFloat(Tag.RescaleIntercept);
			}
		}
	}
	/**
	 * Value of Interest (VOI) LUT.
	 * @author Bradley Ross
	 *
	 */
	public class VLUT {
		protected DicomObject volumeLUT = null;
		protected float[] windowCenter = { -10.0f };
		protected float[] windowWidth = { -10.0f };
		public VLUT(DicomObject value) {
			if (value.containsValue(Tag.WindowCenter)) {
				windowCenter = value.getFloats(Tag.WindowCenter);
			}
			if (value.containsValue(Tag.WindowWidth)) {
				windowWidth = value.getFloats(Tag.WindowWidth);
			}
			if (value.containsValue(Tag.VOILUTSequence)) {
				volumeLUT = value.getNestedDicomObject(Tag.VOILUTSequence);
			}
		}
	}
	/**
	 * Presentation LUT.
	 * @author Bradley Ross
	 *
	 */
	public class PLUT {
		protected DicomObject presentationLUT = null;
		public PLUT(DicomObject value) {
			if (value.containsValue(Tag.PresentationLUTSequence)) {
				presentationLUT = value.getNestedDicomObject(Tag.PresentationLUTSequence);
			}
		}
	}
	protected MLUT mLUT = null; 
	protected VLUT vLUT = null;
	protected PLUT pLUT = null;
	protected int minimumGrayLevel = 1000000;
	protected int maximumGrayLevel = -1000000;
	/**
	 * Prints some information about the Raster object.
	 * @param raster object to be described
	 */
	public void print(Raster raster) {
		DataBuffer buffer = raster.getDataBuffer();
		int dataType = buffer.getDataType();
		int dataSize = DataBuffer.getDataTypeSize(dataType);
		if (pixelRepresentation == 0) {
			System.out.println("PixelData contains unsigned values");
		} else {
			System.out.println("PixelData contains signed values");
		}
		System.out.println("Bits Allocated=" + Integer.toString(bitsAllocated)
				+ ", Stored=" + Integer.toString(bitsStored)
				+ ", High=" + Integer.toString(highBit));
		System.out.println("Unused bits: high=" + Integer.toString(unusedHighBits) 
				+ ", low=" + Integer.toString(unusedLowBits));
		if (dataType == DataBuffer.TYPE_BYTE) {
			System.out.println("BYTE " + Integer.toString(dataSize));
		} else if (dataType == DataBuffer.TYPE_SHORT) {
			System.out.println("SHORT " + Integer.toString(dataSize));
		} else if (dataType == DataBuffer.TYPE_USHORT) {
			System.out.println("USHORT " + Integer.toString(dataSize));
		} else if (dataType == DataBuffer.TYPE_INT) {
			System.out.println("INT " + Integer.toString(dataSize));
		} else if (dataType == DataBuffer.TYPE_FLOAT) {
			System.out.println("FLOAT " + Integer.toString(dataSize));
		} else if (dataType == DataBuffer.TYPE_DOUBLE) {
			System.out.println("DOUBLE " + Integer.toString(dataSize));
		} else if (dataType == DataBuffer.TYPE_UNDEFINED) {
			System.out.println("UNDEFINED");
		}
		int min = 1000000;
		int max = -1000000;
		int min2 = 1000000;
		int max2 = -1000000;
		int[] dummy1 = new int[1];
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				int[] gray = raster.getPixel(x, y, dummy1);
				int signed = dataValue(gray[0]);
				if (gray[0] > max) {
					max = gray[0];
				}
				if (gray[0] < min) {
					min = gray[0];
				}

				if (signed > max2) {
					max2 = signed;
				}
				if (signed < min2) {
					min2 = signed;
				}
			}
		}
		System.out.println("Minimum value unsigned = " + Integer.toString(min));
		System.out.println("Maximum value unsigned = " + Integer.toString(max));
		System.out.println("Minimum value signed   = " + Integer.toString(min2));
		System.out.println("Maximum value signed   = " + Integer.toString(max2));
		Distribution raw = new Distribution((float) min, (float) max, 50);
		Distribution signed = new Distribution((float) min2, (float) max2, 50);
		raw.setDesc("Raster values");
		signed.setDesc("Signed values");
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				int[] gray = raster.getPixel(x,  y, dummy1);
				raw.add((float) gray[0]);
				signed.add((float) dataValue(gray[0]));
			}
		}
		raw.print();
		signed.print();
	}
	/**
	 * Combine low and high order bytes to make 16 bit signed number.
	 * @param high value of high order byte
	 * @param low value of low order byte
	 * @return 16 bit signed number
	 */
	public int combine(int high, int low) {

		return 256*high + low;
	}
	/**
	 * Obtain the high order 8 bits of a 16 bit signed number.
	 * @param value
	 * @return high order 8 bits as unsigned number
	 */
	public int high(int value) {
		
		return (value >> 8) & 255;
	}
	/**
	 * Obtain the lower 8 bits of a signed 16 bit number.
	 * @param value 16 bit signed number
	 * @return low order bits as 8 bit unsigned number
	 */
	public int low(int value) {

		return value  & 255;
	}
	protected String photometricInterpretation = null;
	/**
	 * Indicates whether PixelData values are signed or unsigned.
	 * <p>0 = unsigned, 1 = signed.</p>
	 */
	protected int pixelRepresentation = 0;
	/**
	 * Getter for {@link #pixelRepresentation} property.
	 * @return value of property
	 */
	public int getPixelRepresentation() {
		return pixelRepresentation;
	}
	protected String[] windowCenterWidthExplanation = new String[1];
	protected float[] windowCenter = new float[1];
	protected float[] windowWidth = new float[1];
	protected float rescaleIntercept = 0.0f;
	public float getRescaleIntercept() {
		return rescaleIntercept;
	}
	protected float rescaleSlope = 1.0f;

	protected String rescaleType = null;
	/**
	 * Value of {@link Tag#PixelPaddingValue}, with
	 * pixels having values below this not being actual
	 * measurements.
	 */
	protected int pixelPaddingValue = -100000;
	public int getPixelPaddingValue() {
		return pixelPaddingValue;
	}
	/**
	 * Indicates that the Dicom object has
	 * a Modality Look Up Table (MLUT).
	 */
	protected boolean hasMLUT = false;
	/**
	 * Sets values for internal tests.
	 * @param pixel value for {@link #pixelRepresentation}
	 * @param bitsA value for {@link #bitsAllocated}
	 * @param bitsS value for {@link #bitsStored}
	 * @param hBit value for {@link #highBit}
	 * @param center value for {@link #windowCenter}
	 * @param width value for {@link #windowWidth}
	 * @param intercept value for {@link #rescaleIntercept}
	 * @param slope value for {@link #rescaleSlope}
	 */
	protected void setValues(int pixel, int bitsA, int bitsS, int hBit,
			float center, float width, float intercept, float slope) {
		pixelRepresentation = pixel;
		windowCenter[0] = center;
		windowWidth[0] = width;
		rescaleIntercept = intercept;
		rescaleIntercept = intercept;
		rescaleSlope = slope;
		bitsAllocated = bitsA;
		bitsStored = bitsS;
		highBit = hBit;
		common();
	}
	/**
	 * Location of the high order bit for the pixel value.
	 * <p>Zero indicates the units position in the integer.</p>
	 */
	protected int highBit = -1;
	/**
	 * Number of bits allocated in the PixelData tag for an individual
	 * component of a pixel.
	 * 
	 * <p>For gray scale objects, this is normally 16.</p>
	 */
	protected int bitsAllocated = 16;
	/**
	 * Number of bits used to store a component of a pixel.  
	 * <p>If the value is
	 * a signed number, this includes the sign bit.</p>
	 */
	protected int bitsStored = -1;
	/**
	 * Getter for {@link #bitsStored} property.
	 * @return value of property
	 */
	public int getBitsStored() {
		return bitsStored;
	}
	/**
	 * Value to be added to pixel value for all items to be zero
	 * or positive.
	 */
	protected int adjustment = 0;
	/**
	 * Getter for value of {@link #adjustment} property.
	 * @return value of property
	 */
	public int getAdjustment() {
		return adjustment;
	}
	/**
	 * Number of unused bits on the high order side of the
	 * PixelData value.
	 */
	protected int unusedHighBits = -1;
	/**
	 * Number of unused bits on the low order side of the 
	 * PixelData value.
	 */
	protected int unusedLowBits = -1;
	/**
	 * This is a data mask that will only allow the
	 * least significant {@link #bitsStored} bits.
	 */
	protected int dataMask = (1 << 16) - 1;
	/**
	 * Default constructor used only for internal tests.
	 */
	public RasterProcessorDP() { ; }
	/**
	 * Constructor using Dicom object.
	 * @param objectValue Dicom object containing properties
	 */
	public RasterProcessorDP(DicomObject objectValue) {
		if (objectValue.containsValue(Tag.PixelRepresentation)) {
			pixelRepresentation = objectValue.getInt(Tag.PixelRepresentation);
		} else {
			System.out.println("Default used for PixelRepresentation");
			pixelRepresentation = 0;
		}
		if (objectValue.containsValue(Tag.PhotometricInterpretation)) {
			photometricInterpretation = objectValue.getString(Tag.PhotometricInterpretation);
		}
		if (objectValue.containsValue(Tag.ModalityLUTSequence)) {
			hasMLUT = true;
			DicomObject nested = objectValue.getNestedDicomObject(Tag.ModalityLUTSequence);
			mLUT = new MLUT(nested);
			if (nested.containsValue(Tag.RescaleIntercept)) {
				rescaleIntercept = nested.getFloat(Tag.RescaleIntercept);
			} else {
				System.out.println("Default value of 0.0 used for RescaleIntercept");
				rescaleIntercept = 0.0f;
			}
			if (nested.containsValue(Tag.RescaleSlope)) {
				rescaleSlope = nested.getFloat(Tag.RescaleSlope);
			} else {
				System.out.println("Default value of 1.0 used for RescaleSlope");
				rescaleSlope = 1.0f;
			}
			if (nested.containsValue(Tag.RescaleType)) {
				rescaleType = nested.getString(Tag.RescaleType);
			}
			System.out.println("Object has Modality LUT Sequence");
		} else {
			if (objectValue.containsValue(Tag.RescaleIntercept)) {
				rescaleIntercept = objectValue.getFloat(Tag.RescaleIntercept);
			} else {
				System.out.println("Default value of 0.0 used for RescaleIntercept");
				rescaleIntercept = 0.0f;
			}
			if (objectValue.containsValue(Tag.RescaleSlope)) {
				rescaleSlope = objectValue.getFloat(Tag.RescaleSlope);
			} else {
				System.out.println("Default value of 1.0 used for RescaleSlope");
				rescaleSlope = 1.0f;
			}
			if (objectValue.containsValue(Tag.RescaleType)) {
				rescaleType = objectValue.getString(Tag.RescaleType);
			}
		}

		if (objectValue.containsValue(Tag.HighBit)) {
			highBit = objectValue.getInt(Tag.HighBit);
		}
		if (objectValue.containsValue(Tag.BitsAllocated)) {
			bitsAllocated = objectValue.getInt(Tag.BitsAllocated);
		}
		if (objectValue.containsValue(Tag.BitsStored)) {
			bitsStored = objectValue.getInt(Tag.BitsStored);
		}

		if (objectValue.containsValue(Tag.WindowWidth)) {
			windowWidth = objectValue.getFloats(Tag.WindowWidth);
		} else {
			windowWidth[0] = -100.0f;
			System.out.println("DMust calculate WindowCenter" );
		}
		if (objectValue.containsValue(Tag.WindowCenter)) {
			windowCenter = objectValue.getFloats(Tag.WindowCenter);
		} else {
			windowCenter[0] = -100.0f;
			System.out.println("Must calculate WindowCenter");
		}
		if (objectValue.containsValue(Tag.WindowCenterWidthExplanation)) {
			windowCenterWidthExplanation = objectValue.getStrings(Tag.WindowCenterWidthExplanation);
		}
		if (objectValue.containsValue(Tag.PixelPaddingValue)) {
			pixelPaddingValue = objectValue.getInt(Tag.PixelPaddingValue);
			if (pixelRepresentation == 1) {
				adjustment = 1 << (bitsStored - 1);
				/*
				// This is only good for MONOCHROME2
				int temp = pixelPaddingValue & ((1 << bitsStored) - 1);
				
				if (temp >= (1 << (1 << (bitsStored - 1)))) {
					pixelPaddingValue = temp - (1 << bitsStored);
				}
				if (pixelPaddingValue < 0) {
					adjustment = -pixelPaddingValue;
				}
				*/
			}
		} else {
			if (pixelRepresentation == 1) {
				adjustment = 1 << (bitsStored - 1);
			}
		}
		common();
	}
	/**
	 * Constructor using Properties object.
	 * @param propertiesValue object containing values
	 */
	/*
	public RasterProcessor(Properties propertiesValue) {
		if (propertiesValue.containsKey("PixelRepresentation")) {
			try {
				pixelRepresentation = (int) Integer.parseInt(propertiesValue.getProperty("PropertiesValue"));
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsKey("PhotometricInterpretation")) {
			try {
				photometricInterpretation = propertiesValue.getProperty("PhotometricInterpretation");
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsKey("WindowCenter")) {
			try {
				windowCenter[0] = (float) Float.parseFloat(propertiesValue.getProperty("windowCenter"));
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsKey("WindowWidth")) {
			try {
				windowWidth[0] = (float) Float.parseFloat(propertiesValue.getProperty("windowWidth"));
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsValue("WindowCenterWidthExplanation")) {
			try {
				windowCenterWidthExplanation[0] = propertiesValue.getProperty("WindowCenterWidthExplanation");
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsValue("RescaleIntercept")) {
			try {
				rescaleIntercept = (float) Float.parseFloat(propertiesValue.getProperty("RescaleIntercept"));
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsValue("RescaleSlope")) {
			try {
				rescaleSlope = (float) Float.parseFloat(propertiesValue.getProperty("RescaleSlope"));
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsValue("RescaleType")) {
			try {
				rescaleType = (String) propertiesValue.getProperty("RescaleType");
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsValue("HighBit")) {
			try {
				highBit = (int) Integer.parseInt(propertiesValue.getProperty("HighBit"));
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsValue("BitsAllocated")) {
			try {
				bitsAllocated = (int) Integer.parseInt(propertiesValue.getProperty("BitsAllocated"));
			} catch (Exception e) { ; }
		}
		if (propertiesValue.containsValue("BitsStored")) {
			try {
				bitsStored = (int) Integer.parseInt(propertiesValue.getProperty("BitsStored"));
			} catch (Exception e) { ; }
		}
		common();
	}
	*/
	/**
	 * Common operations to constructors.
	 */
	protected void common() {

		unusedHighBits = bitsAllocated - 1 - highBit;
		unusedLowBits = highBit - bitsStored + 1;
		dataMask = (1 << bitsStored) - 1;
		System.out.println("Bits allocated=" + Integer.toString(bitsAllocated) + ", stored="
				+ Integer.toString(bitsStored) + ", high=" + Integer.toString(highBit));
		if (pixelRepresentation == 0) {
			System.out.println("Unsigned values");
		} else {
			System.out.println("Signed values");
		}
		System.out.println("Data mask is " + Integer.toString(dataMask, 16));

	}
	/**
	 * Convert the contents of PixelData to a grayscale value.
	 * <p>The values in the data buffer belonging to the raster
	 *    are actually stored as unsigned
	 *    short.  However, PixelRepresentation=1 means that the bits are
	 *    to be interpreted as if it was a signed short.  This method
	 *    calculates the actual integer value and then maps them to
	 *    a range of zero or positive values.</p>
	 * @param value content of PixelData
	 * @return grayscale
	 */
	protected int dataValue(int value) {
		int working = value;
		if (pixelRepresentation == 0) { 
			/* unsigned DataPixel values */
			return working & dataMask; 
		} else {
			if (working > (1<<(bitsStored-1) - 1)) {
				working = (working & dataMask) - (1<<(bitsStored ));
			} 
			working = working + adjustment;
			if (working < 0) {
				return 0;
			} else {
				return working;
			}
		}
	}
	/**
	 * Convert the contents of PixelData to a signed value.
	 * <p>The values in the data buffer belonging to the raster
	 *    are actually stored as unsigned
	 *    short.  However, PixelRepresentation=1 means that the bits are
	 *    to be interpreted as if it was a signed short.  This method
	 *    calculates the actual signed integer value.</p>
	 * @param value content of PixelData
	 * @return signed value
	 */
	protected int signedDataValue(int value) {
		int working = value;
		if (pixelRepresentation == 0) { 
			/* unsigned DataPixel values */
			return working & ((1<<bitsStored)-1); 
		} else {
			if (working > (1<<(bitsStored-1) - 1)) {
				return (working & dataMask) - (1<<bitsStored);
			} else {
				return (working & dataMask);
			}
		}
	}
	/**
	 * Create the image for the PNG file.
	 * <p>This routine places the high order bits in the first channel and the low order bits
	 *    in the second channel.</p>
	 * <p>If dealing with signed data, it is necessary to add a constant to
	 *    the pixel values before placing them in the PNG file.  The value
	 *    used is placed in the blue channel of the first two pixels on the
	 *    first row of the image.  The high order bits are placed in the
	 *    first pixel while the low order bits are placed in the second
	 *    pixel.</p>
	 * @param raster raster from grayscale image
	 * @return bgr image with two channels used
	 */
	public BufferedImage buildPng(Raster raster) {
		int[] dummy1 = new int[1];
		int[] gray = new int[1];
		int[] bgr = new int[3];
		BufferedImage working = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster writable = working.getRaster();
		Distribution p1 = null;
		Distribution p2 = null;
		Distribution p3 = null;
		if (debugLevel > 0) {
			if (pixelRepresentation == 0) {
				System.out.println("Running buildPng with unsigned PixelData");
			} else {
				System.out.println("Running buildPng with signed PixelData");
			}
			p1 = new Distribution(-40000.0f, 40000.0f);
			p1.setDesc("buildPng - Raw values");
			p2 = new Distribution(0.0f, 256.0f);
			p2.setDesc("buildPng - high order bits");
			p3 = new Distribution(0.0f, 256.0f);
			p3.setDesc("buildPng - low order bits");
		}
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				gray = raster.getPixel(x, y, dummy1);
				int pixelValue = dataValue(gray[0]);
				if (debugLevel > 0) {
					p1.add((float) pixelValue);
					p2.add((float) high(pixelValue));
					p3.add((float) low(pixelValue));
				}
				bgr[0] = high(pixelValue);
				bgr[1] = low(pixelValue);
				if (y == 0 && x == 0) {
					bgr[2] = high(getAdjustment());
				} else if (y == 0 && x == 1) {
					bgr[2] = low(getAdjustment());	
				} else {
					bgr[2] = 0;
				}
				writable.setPixel(x, y, bgr);
			}
		}
		if (debugLevel > 0) {
			System.out.println("Distribution of gray values");
			p1.print();
			System.out.println("Distribution of high order bits");
			p2.print();
			System.out.println("Distribution of low order bits");
			p3.print();
		}
		return working;
	}
	/**
	 * Pack 16 bit PixelData in 2 channels of image.
	 * <p>For signed PixelData -32768 ( - 1<<15) to 32767 (1<<15 - 1) mapped to 0 to 65535 (1<<16 - 1)</p>
	 * @param raster raster image from DCM4CHE2
	 * @return Image containing packed data
	 */
	public BufferedImage buildRawS(Raster raster) {
		int[] dummy1 = new int[1];
		int[] gray = new int[1];
		int[] bgr = new int[3];
		BufferedImage working = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster writable = working.getRaster();
		Distribution p1 = null;
		Distribution p2 = null;
		Distribution p3 = null;
		if (debugLevel > 0) {

			if (pixelRepresentation == 0) {
				System.out.println("Running buildRawS with unsigned PixelData");
				p1 = new Distribution(0, 10000.0f);
			} else {
				System.out.println("Running buildRawS with signed PixelData");
				p1 = new Distribution(32768.0f - 20000.0f, 32768.0f + 20000.0f);
			}
			p1.setDesc("buildRawS - Distribution of PixelData values");
			p2 = new Distribution(0.0f, 256.0f);
			p2.setDesc("buildRawS - Distribution of high order bits");
			p3 = new Distribution(0.0f, 256.0f);
			p3.setDesc("buildRawS - Distribution of low order bits" );
		}
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				gray = raster.getPixel(x, y, dummy1);
				int pixelValue = dataValue(gray[0]); 
				if (debugLevel > 0) {
					p1.add((float) pixelValue);
					p2.add((float) high(pixelValue));
					p3.add((float) low(pixelValue));
				}
				bgr[0] = high(pixelValue);
				bgr[1] = low(pixelValue);
				bgr[2] = 0;
				writable.setPixel(x, y, bgr);
			}
		}
		if (debugLevel > 0) {
			System.out.println("Distribution of gray values");
			p1.print();
			System.out.println("Distribution of high order bits");
			p2.print();
			System.out.println("Distribution of low order bits");
			p3.print();
		}
		return working;
	}	
	/**
	 * Create the image for the PNG file after rescaling.
	 * <p>This routine places the high order bits in the first channel and the low order bits
	 *    in the second channel.</p>
	 * @param raster raster from grayscale image
	 * @return bgr image with two channels used
	 */
	public BufferedImage buildScaled(Raster raster) {
		int[] dummy1 = new int[1];
		int[] gray = new int[1];
		int[] bgr = new int[3];
		BufferedImage working = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		WritableRaster writable = working.getRaster();
		Distribution p1 = null;
		Distribution p2 = null;
		Distribution p3 = null;
		if (debugLevel > 0) {
			p1 = new Distribution(-40000.0f, 40000.0f);
			p1.setDesc("buildScaled - Distribution of rescaled gray values");
			p2 = new Distribution(0.0f, 256.0f);
			p2.setDesc("buildScaled - Distribution of high order bits");
			p3 = new Distribution(0.0f, 256.0f);
			p3.setDesc("buildScaled - Distribution of low order bits");
		}
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				gray = raster.getPixel(x, y, dummy1);
				int pixelValue = dataValue(gray[0]);
				float value = rescaleSlope*pixelValue + rescaleIntercept;
				pixelValue = (int) value;
				if (debugLevel > 0) {
					p1.add((float) pixelValue);
					p2.add((float) high(pixelValue));
					p3.add((float) low(pixelValue));
				}
				bgr[0] = high(pixelValue);
				bgr[1] = low(pixelValue);
				bgr[2] = 0;
				writable.setPixel(x, y, bgr);
			}
		}
		if (debugLevel > 0) {
			System.out.println("Distribution of rescaled gray values");
			p1.print();
			System.out.println("Distribution of rescaled high order bits");
			p2.print();
			System.out.println("Distribution of rescaled low order bits");
			p3.print();
		}
		return working;
	}	
	/**
	 * Create image using windowing instructions in Dicom object.
	 * <p>In this method, the rescale and windowing operations are carried out
	 *    internally rather than by the DCM4CHE2 code.</p>
	 * @param raster Raster object from PixelData fragment
	 * @return image with windowing applied
	 */
	public BufferedImage buildWindowed(Raster raster) {
		int[] dummy1 = new int[1];
		int[] gray = new int[1];
		if (windowCenter[0] < -10 || windowWidth[0] < -10) {
			for (int x = 0; x < raster.getWidth(); x++) {
				for (int y = 0; y < raster.getHeight(); y++) {
					gray = raster.getPixel(x, y, dummy1);
					int signed = dataValue(gray[0] );

					if (signed > maximumGrayLevel) {
						maximumGrayLevel = signed;
					}
					if (signed < minimumGrayLevel) {
						minimumGrayLevel = signed;
					}
				}
			}
			windowWidth[0] = ((float) (maximumGrayLevel - minimumGrayLevel)) * rescaleSlope;
			windowCenter[0] = ((float) (maximumGrayLevel + minimumGrayLevel))*rescaleSlope/2.0f + rescaleIntercept;
			System.out.println("WindowWidth is " + Float.toString(windowWidth[0]));
			System.out.println("WindowCenter is " + Float.toString(windowCenter[0]));
		}
		BufferedImage working = new BufferedImage(raster.getWidth(), raster.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster writable = working.getRaster();
		float a01 = 255.0f*rescaleSlope/windowWidth[0];
		float a04 = 255.0f/windowWidth[0]*(rescaleIntercept -
				rescaleSlope*adjustment-windowCenter[0]+0.5f*windowWidth[0]);
		for (int x = 0; x < raster.getWidth(); x++) {
			for (int y = 0; y < raster.getHeight(); y++) {
				gray = raster.getPixel(x, y, dummy1);
				int pixelValue = dataValue(gray[0]);
				float value = a01*(float) pixelValue + a04;
				gray[0] = (int) value;
				if (gray[0] > 255) {
					gray[0] = 255;
				} else if (gray[0] < 0) {
					gray[0] = 0;
				}
				writable.setPixel(x, y, gray);
			}
		}
		return working;
	}
	/**
	 * Test conversion back and forth between two eight bit and one sixteen bit value.
	 * @param value value to be converted from 16 bit to 2 8 bit 
	 */
	public static void test(int value) {
		RasterProcessorDP instance = new RasterProcessorDP();
		System.out.println();
		System.out.print(String.format("%1$6d  ", value));
		System.out.print(String.format("%1$02x  %2$02x  %3$7d", instance.high(value), instance.low(value),
				instance.combine(instance.high(value), instance.low(value))));
	}
	protected void test2(int value) {
		System.out.println(String.format("%1$04x  %1$8d  %2$8d", value&dataMask, dataValue(value)));
	}
	/**
	 * Test driver
	 * @param args not used
	 */
	public static void main(String[] args) {
		test(2);
		test(1);
		test(0);
		test(-1);
		test(-2);
		test(100);
		test(-100);
		test(4000);
		test(-4000);
		test(32767);
		test(-32768);
		test(-32767);
		Distribution distribute = new Distribution(0.0f, 100.0f);
		distribute.add(1.0f);
		distribute.add(11.0f);
		distribute.add(5.0f);
		distribute.add(2.0f);
		distribute.add(2.1f);
		distribute.add(2.2f);
		distribute.print();
		System.out.println("Tests for bit shifting");
		long mask1 = (1l<<32) - (1l<<16);
		System.out.println(Long.toString(mask1, 16) + "  (1<<32) - (1<<16)");
		System.out.println(Long.toString(1l<<16, 16) + "  1<<16");
		System.out.println(Long.toString(1l<<15, 16) + "  1<<15");
		RasterProcessorDP instance = null;
		instance = new RasterProcessorDP();
		instance.setDebugLevel(10);
		instance.setValues(1, 16, 12, 11, 40.0f, 400.0f, -400.0f, 1.0f);

		instance.test2(0x07FF);
		instance.test2(0x07FE);
		instance.test2(0x0030);
		instance.test2(0x0020);
		instance.test2(0x0010);
		instance.test2(0x0000);
		instance.test2(0x0fff);
		instance.test2(0x0fef);
		instance.test2(0x0fdf);
		instance.test2(0x0fcf);
		instance.test2(0x0801);
		instance.test2(0x0800);
		instance = new RasterProcessorDP();
		instance.setDebugLevel(10);
		instance.setValues(1, 16, 10, 13, 40.0f, 400.0f, -400.0f, 1.0f);
		instance.test2(0x01ff);
		instance.test2(0x01fe);
		instance.test2(0x0060);
		instance.test2(0x0002);
		instance.test2(0x0001);
		instance.test2(0x0000);
		instance.test2(0xffff);
		instance.test2(0x03ff);
		instance.test2(0x03fe);
		instance.test2(0x03fd);
		instance.test2(0x0201);
		instance.test2(0x0200);
	}
}
