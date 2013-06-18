package edu.stanford.isis.dicomproxy.common;


import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.util.Locale;
import java.util.Vector;

import com.pixelmed.dicom.DisplayShutter;
import com.pixelmed.dicom.ModalityTransform;
import com.pixelmed.dicom.Overlay;
import com.pixelmed.dicom.RealWorldValueTransform;
import com.pixelmed.dicom.SUVTransform;
import com.pixelmed.dicom.VOITransform;
import com.pixelmed.display.BufferedImageUtilities;
import com.pixelmed.display.DemographicAndTechniqueAnnotations;
import com.pixelmed.display.DisplayedAreaSelection;
import com.pixelmed.display.SourceImage;
import com.pixelmed.display.SuperimposedImage;
import com.pixelmed.display.WindowCenterAndWidth;
import com.pixelmed.display.event.StatusChangeEvent;
import com.pixelmed.event.ApplicationEventDispatcher;
import com.pixelmed.event.EventContext;
import com.pixelmed.geometry.GeometryOfVolume;
import com.pixelmed.utils.FloatFormatter;


public class ImageEnhancer{

	/***/
	private static final String identString = "@(#) $Header: /userland/cvs/pixelmed/imgbook/com/pixelmed/display/SingleImagePanel.java,v 1.177 2012/09/23 17:52:41 dclunie Exp $";

	/***/
	SourceImage sImg;
	/***/
	int currentSrcImageIndex;
	/***/
	int[] currentSrcImageSortOrder;

	/***/
	BufferedImage cachedResizedImage;
	/***/
	BufferedImage cachedResizedSelectedRegionImage;
	/***/
	BufferedImage cachedPreWindowedImage;
	/***/
	int offsetDrawingOfResizedSelectedRegionImageX;
	/***/
	int offsetDrawingOfResizedSelectedRegionImageY;
	/***/
	Rectangle cachedWindowSize;

	public void dirty() {
		dirtySource();
	}

	public void dirtySource() {
		cachedResizedImage=null;
		cachedResizedSelectedRegionImage=null;
		cachedPreWindowedImage=null;
	}

	public void dirty(SourceImage sImg) {
		dirtySource(sImg);
	}

	public void dirtySource(SourceImage sImg) {
		this.sImg=sImg;
		this.realWorldValueTransform=sImg.getRealWorldValueTransform();
		this.modalityTransform=sImg.getModalityTransform();
		this.voiTransform=sImg.getVOITransform();
		this.displayShutter=sImg.getDisplayShutter();
		this.overlay=sImg.getOverlay();
		cachedResizedImage=null;
		cachedResizedSelectedRegionImage=null;
		cachedPreWindowedImage=null;
	}

	public void dirtyWindowing() {
		cachedPreWindowedImage=null;
	}

	public void dirtyPanned() {
		cachedPreWindowedImage=null;
		cachedResizedSelectedRegionImage=null;
	}

	/**
	 * whether or not to use the supplied VOI LUT, rather than a linear or sigmoid window function
	 */
	protected boolean useVOILUTNotFunction;
	/**
	 * the currently selected, default or user modified window center
	 */
	public double windowCenter;
	/**
	 * the currently selected, default or user modified window width
	 */
	public double windowWidth;
	public double getWindowCenter() {
		return windowCenter;
	}

	public void setWindowCenter(double windowCenter) {
		this.windowCenter = windowCenter;
	}

	public double getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(double windowWidth) {
		this.windowWidth = windowWidth;
	}

	/**
	 * the currently selected VOI LUT window width value that will result in the application of the VOI LUT rescaling the input (index) values
	 */
	protected double voiLUTIdentityWindowWidth;
	/**
	 * the currently selected VOI LUT window center value that will result in the application of the VOI LUT rescaling the input (index) values
	 */
	protected double voiLUTIdentityWindowCenter;
	/**
	 * the currently selected VOI LUT number of entries
	 */
	protected int voiLUTNumberOfEntries;
	/**
	 * the currently selected VOI LUT first value mapped
	 */
	protected int voiLUTFirstValueMapped;
	/**
	 * the currently selected VOI LUT bits per entry
	 */
	protected int voiLUTBitsPerEntry;
	/**
	 * the currently selected VOI LUT Data
	 */
	protected short[] voiLUTData;
	/**
	 * the currently selected VOI LUT minimum entry value
	 */
	protected int voiLUTEntryMin;
	/**
	 * the currently selected VOI LUT maximum entry value
	 */
	protected int voiLUTEntryMax;
	/**
	 * the currently selected VOI LUT top of entry range (which may be less than (2^voiLUTNumberOfEntries)-1, e.g., in buggy Agfa images)
	 */
	protected int voiLUTTopOfEntryRange;

	/**
	 * the value of rescale slope to use for current frame (set when new frame selected)
	 */
	double useSlope;
	/**
	 * the value of rescale intercept to use for current frame (set when new frame selected)
	 */
	double useIntercept;

	/***/
	double imgMin;
	/***/
	double imgMax;

	/***/
	private int largestGray;
	/***/
	private int firstvalueMapped;
	/***/
	private int numberOfEntries;
	/***/
	private int bitsPerEntry;
	/***/
	private short redTable[];
	/***/
	private short greenTable[];
	/***/
	private short blueTable[];

	/***/
	private boolean signed;
	/***/
	private boolean inverted;
	/***/
	private boolean ybr;
	/***/
	private int signMask;
	/***/
	private int signBit;

	/***/
	int pad;
	/***/
	int padRangeLimit;
	/***/
	boolean hasPad;

	/***/
	SUVTransform suvTransform;

	/***/
	RealWorldValueTransform realWorldValueTransform;

	/***/
	ModalityTransform modalityTransform;

	/***/
	VOITransform voiTransform;

	/***/
	DisplayShutter displayShutter;

	/***/
	Overlay overlay;

	/***/
	private GeometryOfVolume imageGeometry;

	/**
	 * <p>Get the geometry of the frames currently loaded in the single image panel.</p>
	 *
	 * @return	the geometry of the frames
	 */
	public GeometryOfVolume getImageGeometry() {
		return imageGeometry;
	}

	protected Vector preDefinedShapes;			// of Shape
	protected Vector preDefinedText;
	protected Vector localizerShapes;			// of Shape
	protected Vector volumeLocalizationShapes;	// of Shape
	protected Vector interactiveDrawingShapes;
	protected Vector persistentDrawingShapes;
	protected Vector selectedDrawingShapes;
	protected Vector persistentDrawingText;		// of TextAnnotation
	protected Vector<SuperimposedImage> superimposedImages;

	/**
	 * <p>Set the predefined shapes to to be displayed on the currently selected and displayed frame.</p>
	 *
	 * @param	shapes	a {@link java.util.Vector java.util.Vector} of {@link java.awt.Shape java.awt.Shape}, may be null or empty
	 */
	public final void setPreDefinedShapes(Vector shapes) {
		//System.err.println("SingleImagePanel.setPreDefinedShapes(): preDefinedShapes was:");
		//com.pixelmed.geometry.LocalizerPosterFactory.dumpShapes(preDefinedShapes);
		//System.err.println("SingleImagePanel.setPreDefinedShapes(): setting preDefinedShapes to:");
		//com.pixelmed.geometry.LocalizerPosterFactory.dumpShapes(shapes);
		this.preDefinedShapes=shapes;
	}

	/**
	 * <p>Set the shapes of any localizer postings to be displayed on the currently selected and displayed frame.</p>
	 *
	 * @param	shapes	a {@link java.util.Vector java.util.Vector} of {@link java.awt.Shape java.awt.Shape}, may be null or empty
	 */
	public final void setLocalizerShapes(Vector shapes) { this.localizerShapes=shapes; }

	/**
	 * <p>Set the shapes of any volume localization postings to be displayed on the currently selected and displayed frame.</p>
	 *
	 * @param	shapes	a {@link java.util.Vector java.util.Vector} of {@link java.awt.Shape java.awt.Shape}, may be null or empty
	 */
	public final void setVolumeLocalizationShapes(Vector shapes) { this.volumeLocalizationShapes=shapes; }

	/**
	 * <p>Set the unselected region shapes to to be displayed on the currently selected and displayed frame.</p>
	 *
	 * @param	shapes	a {@link java.util.Vector java.util.Vector} of {@link java.awt.Shape java.awt.Shape}, may be null or empty
	 */
	public final void setPersistentDrawingShapes(Vector shapes) { persistentDrawingShapes=shapes; }

	/**
	 * <p>Get the unselected region shapes to to be displayed on the currently selected and displayed frame.</p>
	 *
	 * @return	a {@link java.util.Vector java.util.Vector} of {@link java.awt.Shape java.awt.Shape}, may be null or empty
	 */
	public final Vector getPersistentDrawingShapes() { return persistentDrawingShapes; }

	/**
	 * <p>Set the selected region shapes to to be displayed on the currently selected and displayed frame.</p>
	 *
	 * @param	shapes	a {@link java.util.Vector java.util.Vector} of {@link java.awt.Shape java.awt.Shape}, may be null or empty
	 */
	public final void setSelectedDrawingShapes(Vector shapes) { selectedDrawingShapes=shapes; }

	/**
	 * <p>Get the selected region shapes to to be displayed on the currently selected and displayed frame.</p>
	 *
	 * @return	a {@link java.util.Vector java.util.Vector} of {@link java.awt.Shape java.awt.Shape}, may be null or empty
	 */
	public final Vector getSelectedDrawingShapes() { return selectedDrawingShapes; }

	/**
	 * <p>Set the superimposed images to to be displayed on the appropriate frames.</p>
	 *
	 * @param	superimposedImages	a {@link java.util.Vector java.util.Vector} of {@link com.pixelmed.display.SuperimposedImage com.pixelmed.display.SuperimposedImage}, may be null or empty
	 */
	public final void setSuperimposedImages(Vector<SuperimposedImage> superimposedImages) { this.superimposedImages = superimposedImages; }

	/**
	 * <p>Get the superimposed images to to be displayed on the appropriate frames.</p>
	 *
	 * @return	a {@link java.util.Vector java.util.Vector} of {@link com.pixelmed.display.SuperimposedImage com.pixelmed.display.SuperimposedImage}, may be null or empty
	 */
	public final Vector<SuperimposedImage> getSuperimposedImages() { return superimposedImages; }

	/***/
	private DemographicAndTechniqueAnnotations demographicAndTechniqueAnnotations = null;
	/***/

	/***/
	protected Color orientationColor;
	/***/
	protected Font orientationFont;
	/***/
	protected boolean showOrientationsLeftSide = false;

	/***/
	protected String sideAndViewAnnotationString;
	/***/
	protected int sideAndViewAnnotationVerticalOffset;
	/***/
	protected Color sideAndViewAnnotationColor;
	/***/
	protected Font sideAndViewAnnotationFont;
	/***/
	protected boolean showSideAndViewAnnotationLeftSide = false;


	/***/
	protected DisplayedAreaSelection originalDisplayedAreaSelection = null;
	/***/
	private DisplayedAreaSelection useDisplayedAreaSelection = null;


	/***/
	private AffineTransform preTransformImageRelativeCoordinates = null;

	/**
	 * <p>Select the AffineTransform to apply to image-relative coordinates.</p>
	 *
	 * <p>Used in cases where the supplied image has already been flipped or rotated
	 * but the coordinates relative to the original image have not.</p>
	 *
	 * @param	transform	the transform, or null to reset to no transform
	 */
	public final void setPreTransformImageRelativeCoordinates(AffineTransform transform) {
		this.preTransformImageRelativeCoordinates=transform;
	}

	/***/
	protected boolean showZoomFactor = false;
	/***/
	protected boolean showZoomFactorLeftSide = false;
	/***/
	protected double pixelSpacingInSourceImage = 0;
	/***/
	protected String typeOfPixelSpacing;

	/**
	 * <p>Select whether or not to annotate displayed image with zoom factor.</p>
	 *
	 * <p>Uses same font parameters as set for orientation annotations.</p>
	 *
	 * <p>Also implicitly effects setPixelSpacingInSourceImage().</p>
	 *
	 * @param	showZoomFactor				true or false to activate annotation of zoom factor
	 * @param	leftSide					show zoom factor on left (true) or right (false) side of view port
	 * @param	pixelSpacingInSourceImage	a single value that is the (square) row and column pixel spacing, or 0 if not known
	 */
	public final void setShowZoomFactor(boolean showZoomFactor,boolean leftSide,double pixelSpacingInSourceImage) {
		this.showZoomFactor=showZoomFactor;
		this.showZoomFactorLeftSide=leftSide;
		this.pixelSpacingInSourceImage=pixelSpacingInSourceImage;
	}

	/**
	 * <p>Select whether or not to annotate displayed image with zoom factor.</p>
	 *
	 * <p>Uses same font parameters as set for orientation annotations.</p>
	 *
	 * <p>Also implicitly effects setPixelSpacingInSourceImage().</p>
	 *
	 * @param	showZoomFactor				true or false to activate annotation of zoom factor
	 * @param	leftSide					show zoom factor on left (true) or right (false) side of view port
	 * @param	pixelSpacingInSourceImage	a single value that is the (square) row and column pixel spacing, or 0 if not known
	 * @param	typeOfPixelSpacing       	a String that describes the type of pixel spacing (e.g., detector plane, calibrated, accounting for geometric magnification, etc.), or null if not to be described when making measurements
	 */
	public final void setShowZoomFactor(boolean showZoomFactor,boolean leftSide,double pixelSpacingInSourceImage,String typeOfPixelSpacing) {
		this.showZoomFactor=showZoomFactor;
		this.showZoomFactorLeftSide=leftSide;
		this.pixelSpacingInSourceImage=pixelSpacingInSourceImage;
		this.typeOfPixelSpacing=typeOfPixelSpacing;
	}

	/**
	 * <p>Set pixel spacing in source image.</p>
	 *
	 * <p>Used for displaying zoom factor and making measurements, therefore should be appropriate choice of Pixel Spacing or Imager Pixel Spacing (appropriately corrected for radiographic magnification factor, if any), etc.</p>
	 *
	 * @param	pixelSpacingInSourceImage	a single value that is the (square) row and column pixel spacing, or 0 if not known
	 */
	public final void setPixelSpacingInSourceImage(double pixelSpacingInSourceImage) {
		this.pixelSpacingInSourceImage=pixelSpacingInSourceImage;
	}

	/**
	 * <p>Set pixel spacing in source image.</p>
	 *
	 * <p>Used for displaying zoom factor and making measurements, therefore should be appropriate choice of Pixel Spacing or Imager Pixel Spacing (appropriately corrected for radiographic magnification factor, if any), etc.</p>
	 *
	 * @param	pixelSpacingInSourceImage	a single value that is the (square) row and column pixel spacing, or 0 if not known
	 * @param	typeOfPixelSpacing       	a String that describes the type of pixel spacing (e.g., detector plane, calibrated, accounting for geometric magnification, etc.), or null if not to be described when making measurements
	 */
	public final void setPixelSpacingInSourceImage(double pixelSpacingInSourceImage,String typeOfPixelSpacing) {
		this.pixelSpacingInSourceImage=pixelSpacingInSourceImage;
		this.typeOfPixelSpacing=typeOfPixelSpacing;
	}

	/***/
	private AffineTransform windowToImageCoordinateTransform = null;

	/***/
	private int useVOIFunction = 0;		// 0 is linear, 1 is logistic

	/**
	 * <p>Set the VOI function to the (default) window center/width linear transformation.</p>
	 */
	public final void setVOIFunctionToLinear() {
		//System.err.println("SingleImagePanel.setVOIFunctionToLinear()");
		useVOIFunction = 0;
	}

	/**
	 * <p>Set the VOI function to a non-linear transformation using a logistic (sigmoid) curve with window center and width as parameters.</p>
	 */
	public final void setVOIFunctionToLogistic() {
		//System.err.println("SingleImagePanel.setVOIFunctionToLogistic()");
		useVOIFunction = 1;
	}

	/***/
	private boolean useWindowLinearExactCalculationInsteadOfDICOMStandardMethod = false;

	/**
	 * <p>Set the VOI linear function to use the exact window center/width linear transformation when applying to rescaled pixels.</p>
	 */
	public final void setWindowLinearCalculationToExact() {
		//System.err.println("SingleImagePanel.setWindowLinearCalculationToExact()");
		useWindowLinearExactCalculationInsteadOfDICOMStandardMethod = true;
	}

	/**
	 * <p>Set the VOI linear function to use the DICOM offset window center/width linear transformation when applying to rescaled pixels.</p>
	 *
	 * <p>The DICOM offset subtracts 0.5 from the window center and subtracts 1.0 from the window width before applying to rescaled pixels.</p>
	 */
	public final void setWindowLinearCalculationToDicom() {
		//System.err.println("SingleImagePanel.setWindowLinearCalculationToDicom()");
		useWindowLinearExactCalculationInsteadOfDICOMStandardMethod = false;
	}

	private double windowingAccelerationValue = 1;

	/**
	 * <p>Set the windowing acceleration value to use.</p>
	 */
	public final void setWindowingAccelerationValue(double value) {
		//System.err.println("SingleImagePanel.setWindowingAccelerationValue(): to "+value);
		windowingAccelerationValue = value;
	}


	/***/
	EventContext typeOfPanelEventContext;

	/***/
	int lastx;
	/***/
	int lasty;

	/***/
	int lastmiddley;

	/***/
	double windowingMultiplier = 1;
	/***/
	double panningMultiplier = 1;

	/**
	 * if -1, then use statistical value first time then user adjusted values subsequently
	 * if >= 0, then use selected VOI transform (if there is one for that frame)
	 */
	int currentVOITransformInUse;

	/**
	 * @param	e
	 */


	protected int getSourceImageHeight() {
		return sImg.getHeight();
	}

	protected int getSourceImageWidth() {
		return sImg.getWidth();
	}

	/**
	 *
	 * Get location on source image from window relative location.
	 *
	 * @param	xw	x coordinate in AWT window as returned by MouseEvent.getX()
	 * @param	yw	y coordinate in AWT window as returned by MouseEvent.getY()
	 * @return		source image-relative coordinates with sub-pixel resolution clamped to image size but including BLHC of BLHC pixel (per DICOM PS 3.3 Figure C.10.5-1) 
	 */
	protected Point2D getImageCoordinateFromWindowCoordinate(double xw,double yw) {
		double xi = 0;
		double yi = 0;
		if (windowToImageCoordinateTransform != null) {
			Point2D pointd = new Point2D.Double(xw,yw);
			pointd = windowToImageCoordinateTransform.transform(pointd,pointd);	// overwrites self rather than allocating a new point
			xi = pointd.getX();
			yi = pointd.getY();
			//System.err.println("SingeImagePanel.getImageCoordinateFromWindowCoordinate(): window ("+xw+","+yw+") -> ("+xi+","+yi+")");
			if (xi < 0) {
				xi=0;
			}
			else {
				int width = getSourceImageWidth();
				if (xi > width) {
					xi=width;
				}
			}
			if (yi < 0) {
				yi=0;
			}
			else {
				int height = getSourceImageHeight();
				if (yi > height) {
					yi=height;
				}
			}
			//System.err.println("SingeImagePanel.getImageCoordinateFromWindowCoordinate(): clamped to image size ("+xi+","+yi+")");
		}
		return new Point2D.Double(xi,yi);
	}

	/**
	 * This value is outside mouseMoved() only so that it doesn't need to be constantly reallocated - it is not used by any other method
	 */
	protected double[] currentLocationIn3DSpace = new double[3];

	/**
	 * @param	e
	 */
	public void mouseMoved(MouseEvent e) {
		//System.err.println(e.getX()+" "+e.getY());
		{
			double x = e.getX();
			double y = e.getY();
			Point2D point = getImageCoordinateFromWindowCoordinate(x,y);
			double subPixelX = point.getX();
			double subPixelY = point.getY();		
			//System.err.println("X: "+subPixelX+" ("+x+") Y: "+subPixelX+" ("+y+")");
			int useSrcImageIndex = currentSrcImageSortOrder == null ? currentSrcImageIndex : currentSrcImageSortOrder[currentSrcImageIndex];

			StringBuffer sbuf = new StringBuffer();
			sbuf.append("(");
			sbuf.append(FloatFormatter.toString(subPixelX,Locale.US));
			sbuf.append(",");
			sbuf.append(FloatFormatter.toString(subPixelY,Locale.US));
			if (imageGeometry != null) {
				imageGeometry.lookupImageCoordinate(currentLocationIn3DSpace,subPixelX,subPixelY,useSrcImageIndex);
				{
					sbuf.append(": ");
					sbuf.append(FloatFormatter.toString(currentLocationIn3DSpace[0],Locale.US));
					sbuf.append(",");
					sbuf.append(FloatFormatter.toString(currentLocationIn3DSpace[1],Locale.US));
					sbuf.append(",");
					sbuf.append(FloatFormatter.toString(currentLocationIn3DSpace[2],Locale.US));
				}
			}
			sbuf.append(")");

			BufferedImage src = sImg.getBufferedImage(useSrcImageIndex);
			int wholePixelXFromZero = (int)subPixelX;	// just truncate (not round) ... 0 becomes 0, 0.5 becomes 0, 1.0 is next pixel and becomes 1, only extreme BLHC of BLHC pixel goes out of bounds
			int wholePixelYFromZero = (int)subPixelY;		
			if (wholePixelXFromZero >= 0 && wholePixelXFromZero < src.getWidth() && wholePixelYFromZero >= 0 && wholePixelYFromZero < src.getHeight()) {	// check avoids occasional ArrayIndexOutOfBoundsException exception
				double storedPixelValue;
				if (src.getRaster().getDataBuffer() instanceof DataBufferFloat) {
					float[] storedPixelValues  = src.getSampleModel().getPixel(wholePixelXFromZero,wholePixelYFromZero,(float[])null,src.getRaster().getDataBuffer());
					storedPixelValue=storedPixelValues[0];
				}
				else if (src.getRaster().getDataBuffer() instanceof DataBufferDouble) {
					double[] storedPixelValues  = src.getSampleModel().getPixel(wholePixelXFromZero,wholePixelYFromZero,(double[])null,src.getRaster().getDataBuffer());
					storedPixelValue=storedPixelValues[0];
				}
				else {
					int[] storedPixelValues  = src.getSampleModel().getPixel(wholePixelXFromZero,wholePixelYFromZero,(int[])null,src.getRaster().getDataBuffer());
					int storedPixelValueInt=storedPixelValues[0];
					//System.err.println("storedPixelValue as stored = 0x"+Integer.toHexString(storedPixelValueInt)+" "+storedPixelValueInt+" dec");
					if (signed && (storedPixelValueInt&signBit) != 0) {
						storedPixelValueInt|=signMask;	// sign extend
						//System.err.println("storedPixelValue extended  = 0x"+Integer.toHexString(storedPixelValueInt)+" "+storedPixelValueInt+" dec");
					}
					storedPixelValue=storedPixelValueInt;
				}
				if (realWorldValueTransform != null) {
					sbuf.append(" = ");
					sbuf.append(realWorldValueTransform.toString(useSrcImageIndex,storedPixelValue));
					sbuf.append(" [");
					sbuf.append(FloatFormatter.toString(storedPixelValue,Locale.US));	// this will not append spurious trailing "0." as default toString() method would
					sbuf.append("]");
				}

				if (suvTransform != null) {
					sbuf.append(" ");
					sbuf.append(suvTransform.toString(useSrcImageIndex,storedPixelValue));
				}
			}

			ApplicationEventDispatcher.getApplicationEventDispatcher().processEvent(new StatusChangeEvent(sbuf.toString()));
		}
	}


	// called by paintComponent() first time or after change create image with window values applied ...

	/**
	 * @param	src
	 * @param	center
	 * @param	width
	 * @param	identityCenter
	 * @param	identityWidth
	 * @param	signed
	 * @param	inverted
	 * @param	useSlope
	 * @param	useIntercept
	 * @param	hasPad
	 * @param	pad
	 * @param	padRangeLimit
	 * @param	numberOfEntries
	 * @param	bitsPerEntry
	 * @param	grayTable
	 * @param	entryMin
	 * @param	entryMax
	 * @param	topOfEntryRange
	 */
	public static final BufferedImage applyVOILUT(BufferedImage src,double center,double width,double identityCenter,double identityWidth,
			boolean signed,boolean inverted,double useSlope,double useIntercept,
			boolean hasPad,int pad,int padRangeLimit,
			int numberOfEntries,int firstValueMapped,int bitsPerEntry,short[] grayTable,int entryMin,int entryMax,int topOfEntryRange) {
		return WindowCenterAndWidth.applyVOILUT(src,center,width,identityCenter,identityWidth,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit,numberOfEntries,firstValueMapped,bitsPerEntry,grayTable,entryMin,entryMax,topOfEntryRange);
	}

	/**
	 * @param	src
	 * @param	center
	 * @param	width
	 * @param	signed
	 * @param	inverted
	 * @param	useSlope
	 * @param	useIntercept
	 * @param	hasPad
	 * @param	pad
	 * @param	padRangeLimit
	 */
	public static final BufferedImage applyWindowCenterAndWidthLogistic(BufferedImage src,double center,double width,
			boolean signed,boolean inverted,double useSlope,double useIntercept,boolean hasPad,int pad,int padRangeLimit) {
		return WindowCenterAndWidth.applyWindowCenterAndWidthLogistic(src,center,width,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit);
	}

	/**
	 * @param	src
	 * @param	center
	 * @param	width
	 * @param	signed
	 * @param	inverted
	 * @param	useSlope
	 * @param	useIntercept
	 * @param	hasPad
	 * @param	pad
	 * @param	padRangeLimit
	 */
	public static final BufferedImage applyWindowCenterAndWidthLinear(BufferedImage src,double center,double width,
			boolean signed,boolean inverted,double useSlope,double useIntercept,boolean hasPad,int pad,int padRangeLimit) {
		return WindowCenterAndWidth.applyWindowCenterAndWidthLinear(src,center,width,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit);
	}

	/**
	 * @param	src
	 * @param	center
	 * @param	width
	 * @param	signed
	 * @param	inverted
	 * @param	useSlope
	 * @param	useIntercept
	 * @param	hasPad
	 * @param	pad
	 * @param	padRangeLimit
	 * @param	useExactCalculationInsteadOfDICOMStandardMethod
	 */
	public static final BufferedImage applyWindowCenterAndWidthLinear(BufferedImage src,double center,double width,
			boolean signed,boolean inverted,double useSlope,double useIntercept,boolean hasPad,int pad,int padRangeLimit,boolean useExactCalculationInsteadOfDICOMStandardMethod) {
		return WindowCenterAndWidth.applyWindowCenterAndWidthLinear(src,center,width,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit,useExactCalculationInsteadOfDICOMStandardMethod);
	}

	/**
	 * @param	src
	 * @param	center
	 * @param	width
	 * @param	signed
	 * @param	inverted
	 * @param	useSlope
	 * @param	useIntercept
	 * @param	hasPad
	 * @param	pad
	 * @param	padRangeLimit
	 * @param	largestGray
	 * @param	bitsPerEntry
	 * @param	numberOfEntries
	 * @param	redTable
	 * @param	greenTable
	 * @param	blueTable
	 */
	public static final BufferedImage applyWindowCenterAndWidthWithPaletteColor(BufferedImage src,double center,double width,
			boolean signed,boolean inverted,double useSlope,double useIntercept,boolean hasPad,int pad,int padRangeLimit,
			int largestGray,int bitsPerEntry,int numberOfEntries,
			short[] redTable,short[] greenTable,short[] blueTable) {
		//System.err.println("SingleImagePanel.applyWindowCenterAndWidthWithPaletteColor():");
		return WindowCenterAndWidth.applyWindowCenterAndWidthWithPaletteColor(src,center,width,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit,
				largestGray,bitsPerEntry,numberOfEntries,redTable,greenTable,blueTable);
	}

	// Common constructor support ...

	protected void establishInitialWindowOrVOILUT() {
		// choose the initial window center and width or VOI LUT ...

		useVOILUTNotFunction=false;
		windowWidth=0;
		windowCenter=0;
		voiLUTIdentityWindowCenter=0;
		voiLUTIdentityWindowWidth=0;
		voiLUTNumberOfEntries=0;
		voiLUTFirstValueMapped=0;
		voiLUTBitsPerEntry=0;
		voiLUTData=null;
		voiLUTEntryMin=0;
		voiLUTEntryMax=0;
		voiLUTTopOfEntryRange=0;

		//System.err.println("SingleImagePanel.establishInitialWindowOrVOILUT(): Looking at voiTransform "+voiTransform);
		if (voiTransform != null) {
			final int nTransforms = voiTransform.getNumberOfTransforms(currentSrcImageIndex);

			// first look for actual LUT, and prefer LUT over window values

			currentVOITransformInUse=0;
			while (currentVOITransformInUse < nTransforms) {
				if (voiTransform.isLUTTransform(currentSrcImageIndex,currentVOITransformInUse)) {
					//System.err.println("SingleImagePanel.doCommonConstructorStuff(): found possible LUT "+currentVOITransformInUse);
					voiLUTNumberOfEntries=voiTransform.getNumberOfEntries (currentSrcImageIndex,currentVOITransformInUse);
					voiLUTFirstValueMapped=voiTransform.getFirstValueMapped(currentSrcImageIndex,currentVOITransformInUse);
					voiLUTBitsPerEntry=voiTransform.getBitsPerEntry    (currentSrcImageIndex,currentVOITransformInUse);
					voiLUTData=voiTransform.getLUTData         (currentSrcImageIndex,currentVOITransformInUse);
					voiLUTEntryMin=voiTransform.getEntryMinimum    (currentSrcImageIndex,currentVOITransformInUse);
					voiLUTEntryMax=voiTransform.getEntryMaximum    (currentSrcImageIndex,currentVOITransformInUse);
					voiLUTTopOfEntryRange=voiTransform.getTopOfEntryRange (currentSrcImageIndex,currentVOITransformInUse);
					if (voiLUTData != null && voiLUTData.length == voiLUTNumberOfEntries) {
						useVOILUTNotFunction=true;	// only if LUT is "good"
						// initialize "pseudo-window" to scale input values used to index LUT to identity transformation (i.e., apply LUT exactly as supplied)
						// note that the choice of identity values is arbitrary, but is chosen this way so that the numerical values "make sense" to the user
						// must be consistent with identity values specified in applyVOILUT() invocation
						voiLUTIdentityWindowWidth  = voiLUTNumberOfEntries;
						voiLUTIdentityWindowCenter = voiLUTFirstValueMapped + voiLUTNumberOfEntries/2;
						windowWidth  = voiLUTIdentityWindowWidth;
						windowCenter = voiLUTIdentityWindowCenter;
					}
					break;
				}
				++currentVOITransformInUse;
			}

			if (!useVOILUTNotFunction) {		// no LUT, so search transforms again for window values
				currentVOITransformInUse=0;
				while (currentVOITransformInUse < nTransforms) {
					if (voiTransform.isWindowTransform(currentSrcImageIndex,currentVOITransformInUse)) {
						useVOILUTNotFunction=false;
						windowWidth=voiTransform.getWidth(currentSrcImageIndex,currentVOITransformInUse);
						windowCenter=voiTransform.getCenter(currentSrcImageIndex,currentVOITransformInUse);
						break;
					}
					++currentVOITransformInUse;
				}
			}
		}
		if (!useVOILUTNotFunction && windowWidth <= 0) {			// if no LUT, use supplied window only if there was one, and if its width was not zero or negative (center may legitimately be zero)
			//if (iMean != 0.0 && iSD != 0.0) {
			//	windowWidth=iSD*2.0;
			//	windowCenter=iMean;
			//}
			//else {
			double ourMin = imgMin*useSlope+useIntercept;
			double ourMax = imgMax*useSlope+useIntercept;
			//windowWidth=(ourMax-ourMin)/2.0;
			windowWidth=(ourMax-ourMin);
			windowCenter=(ourMax+ourMin)/2.0;
			currentVOITransformInUse=-1;		// flag not to mess with values when scrolling through frames
			//}
		}
	}	

	/***/
	private boolean useConvertToMostFavorableImageType;	// used in paintComponent()

	/**
	 * @param	sImg
	 * @param	typeOfPanelEventContext
	 * @param	sortOrder
	 * @param	preDefinedShapes
	 * @param	preDefinedText
	 * @param	imageGeometry
	 */
	private void doCommonConstructorStuff(SourceImage sImg,
			EventContext typeOfPanelEventContext,
			int[] sortOrder,
			Vector preDefinedShapes,Vector preDefinedText,
			GeometryOfVolume imageGeometry) {
		this.sImg = sImg;
		boolean convertNonGrayscale = false;
		if (sImg != null && sImg.getNumberOfBufferedImages() > 0) {
			BufferedImage img=sImg.getBufferedImage(0);
			//	SampleModel sampleModel = img.getSampleModel();
			if (img != null && img.getRaster().getNumBands() > 1) {
				//		if (sampleModel instanceof ComponentSampleModel && ((ComponentSampleModel)sampleModel).getPixelStride() != 1
				//		 || sampleModel instanceof PixelInterleavedSampleModel) {
				convertNonGrayscale = true;
				//		}
			}
		}
		try {
			useConvertToMostFavorableImageType =
				//	   (System.getProperty("mrj.version") != null && Double.parseDouble(System.getProperty("mrj.version")) < 4)	|| // because slow otherwise
				convertNonGrayscale == true;
			//useConvertToMostFavorableImageType = true;
			//useConvertToMostFavorableImageType = false;
		}
		catch (NumberFormatException e) {
		}


		currentSrcImageIndex=0;

		currentSrcImageSortOrder=sortOrder;	// may be null ... that is OK ... paintComponent() handles null as implicit frame order

		dirtySource();

		this.typeOfPanelEventContext=typeOfPanelEventContext;
		this.largestGray=sImg.getPaletteColorLargestGray();
		this.firstvalueMapped=sImg.getPaletteColorFirstValueMapped();
		this.numberOfEntries=sImg.getPaletteColorNumberOfEntries();
		this.bitsPerEntry=sImg.getPaletteColorBitsPerEntry();
		this.redTable=sImg.getPaletteColorRedTable();
		this.greenTable=sImg.getPaletteColorGreenTable();
		this.blueTable=sImg.getPaletteColorBlueTable();

		this.signed=sImg.isSigned();
		this.inverted=sImg.isInverted();
		this.ybr=sImg.isYBR();

		this.hasPad=sImg.isPadded();
		this.pad=sImg.getPadValue();
		this.padRangeLimit=sImg.getPadRangeLimit();

		if (sImg != null && sImg.getNumberOfBufferedImages() > 0) {
			BufferedImage img=sImg.getBufferedImage(0);
			imgMin=sImg.getMinimum();					// defer until have fetched BufferedImage, else may be invalid if in memoery mapped file
			imgMax=sImg.getMaximum();
			signBit=0;
			signMask=0;
			if (signed) {
				// the source image will already have been sign extended to the data type size
				// so we don't need to worry about other than exactly 8 and 16 bits
				if (img.getSampleModel().getDataType() == DataBuffer.TYPE_BYTE) {
					signBit=0x0080;
					signMask=0xffffff80;
				}
				else {	// assume short or ushort
					signBit=0x8000;
					signMask=0xffff8000;
				}
			}
		}

		this.suvTransform=sImg.getSUVTransform();
		this.realWorldValueTransform=sImg.getRealWorldValueTransform();
		this.modalityTransform=sImg.getModalityTransform();
		this.voiTransform=sImg.getVOITransform();
		this.displayShutter=sImg.getDisplayShutter();
		this.overlay=sImg.getOverlay();

		// slope & intercept get set on 1st paintComponent() anyway, but
		// we need to get them now to statistically derive the default
		// window center and width ...

		if (modalityTransform != null) {
			useSlope = modalityTransform.getRescaleSlope    (currentSrcImageIndex);
			useIntercept = modalityTransform.getRescaleIntercept(currentSrcImageIndex);
		}
		else {
			useSlope=1.0;
			useIntercept=0.0;
		}

		establishInitialWindowOrVOILUT();

		this.preDefinedShapes=preDefinedShapes;
		this.preDefinedText=preDefinedText;

		this.imageGeometry=imageGeometry;
	}


	/**
	 * @param	sImg
	 * @param	typeOfPanelEventContext
	 * @param	sortOrder
	 * @param	preDefinedShapes
	 * @param	preDefinedText
	 * @param	imageGeometry
	 */
	public ImageEnhancer(SourceImage sImg,
			EventContext typeOfPanelEventContext,
			int[] sortOrder,
			Vector preDefinedShapes,Vector preDefinedText,
			GeometryOfVolume imageGeometry) {
		doCommonConstructorStuff(sImg,
				typeOfPanelEventContext,
				sortOrder,
				preDefinedShapes,preDefinedText,
				imageGeometry);
	}

	/**
	 * @param	sImg
	 * @param	typeOfPanelEventContext
	 * @param	imageGeometry
	 */
	public ImageEnhancer(SourceImage sImg,
			EventContext typeOfPanelEventContext,
			GeometryOfVolume imageGeometry) {
		doCommonConstructorStuff(sImg,
				typeOfPanelEventContext,
				null,
				null,null,
				imageGeometry);
	}

	/**
	 * @param	sImg
	 * @param	typeOfPanelEventContext
	 */
	public ImageEnhancer(SourceImage sImg,
			EventContext typeOfPanelEventContext) {
		doCommonConstructorStuff(sImg,
				typeOfPanelEventContext,
				null,
				null,null,
				null);
	}

	/**
	 * @param	sImg
	 */
	public ImageEnhancer(SourceImage sImg) {
		doCommonConstructorStuff(sImg,
				null,
				null,
				null,null,
				null);
	}



	/**
	 * @param	g
	 */
	public BufferedImage enhanceImage() {

		int useSrcImageIndex = 0;
		BufferedImage useSrcImage = sImg.getBufferedImage(useSrcImageIndex);

		if (ybr) {
			// do this BEFORE any interpolation, else get artifacts at edges
			BufferedImage convertedImage = BufferedImageUtilities.convertYBRToRGB(useSrcImage);
			if (convertedImage != null) {
				useSrcImage = convertedImage;
				dirtySource();
			}
		}

		if (currentVOITransformInUse != -1
				&& !useVOILUTNotFunction
				&& voiTransform != null
				&& voiTransform.getNumberOfTransforms(useSrcImageIndex) > currentVOITransformInUse) {
			
			windowWidth=voiTransform.getWidth(useSrcImageIndex,currentVOITransformInUse);
			windowCenter=voiTransform.getCenter(useSrcImageIndex,currentVOITransformInUse);
		}

		System.out.println("windowWidth = "+windowWidth);
		System.out.println("windowCenter = "+windowCenter);
		

		// Second, find rescale attributes for this frame
		if (modalityTransform != null) {
			useSlope = modalityTransform.getRescaleSlope(useSrcImageIndex);
			useIntercept = modalityTransform.getRescaleIntercept(useSrcImageIndex);
		}
		else {
			useSlope=1.0;
			useIntercept=0.0;
		}

		// Finally, actually build the destination image
		if (useVOILUTNotFunction) {
			cachedPreWindowedImage = applyVOILUT(useSrcImage,windowCenter,windowWidth,voiLUTIdentityWindowCenter,voiLUTIdentityWindowWidth,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit,
					voiLUTNumberOfEntries,voiLUTFirstValueMapped,voiLUTBitsPerEntry,voiLUTData,voiLUTEntryMin,voiLUTEntryMax,voiLUTTopOfEntryRange);
		}
		else if (numberOfEntries != 0 && redTable != null) {
			cachedPreWindowedImage = applyWindowCenterAndWidthWithPaletteColor(useSrcImage,windowCenter,windowWidth,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit,
					largestGray,bitsPerEntry,numberOfEntries,redTable,greenTable,blueTable);
		}
		else if (useVOIFunction == 1) {
			cachedPreWindowedImage = applyWindowCenterAndWidthLogistic(useSrcImage,windowCenter,windowWidth,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit);
		}
		else {
			cachedPreWindowedImage = applyWindowCenterAndWidthLinear(useSrcImage,windowCenter,windowWidth,signed,inverted,useSlope,useIntercept,hasPad,pad,padRangeLimit,useWindowLinearExactCalculationInsteadOfDICOMStandardMethod);
		}

		if (cachedPreWindowedImage != null && useConvertToMostFavorableImageType) {
			cachedPreWindowedImage=BufferedImageUtilities.convertToMostFavorableImageType(cachedPreWindowedImage);
		}

		return cachedPreWindowedImage;
	}

	
	public void findVisuParametersImage() {

		int useSrcImageIndex = 0;
		BufferedImage useSrcImage = sImg.getBufferedImage(useSrcImageIndex);

		if (ybr) {
			// do this BEFORE any interpolation, else get artifacts at edges
			BufferedImage convertedImage = BufferedImageUtilities.convertYBRToRGB(useSrcImage);
			if (convertedImage != null) {
				useSrcImage = convertedImage;
				dirtySource();
			}
		}

		if (currentVOITransformInUse != -1
				&& !useVOILUTNotFunction
				&& voiTransform != null
				&& voiTransform.getNumberOfTransforms(useSrcImageIndex) > currentVOITransformInUse) {
			
			windowWidth=voiTransform.getWidth(useSrcImageIndex,currentVOITransformInUse);
			windowCenter=voiTransform.getCenter(useSrcImageIndex,currentVOITransformInUse);
		}

		System.out.println("windowWidth = "+windowWidth);
		System.out.println("windowCenter = "+windowCenter);
	}
	
}
