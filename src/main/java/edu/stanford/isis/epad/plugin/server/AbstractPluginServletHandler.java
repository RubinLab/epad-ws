package edu.stanford.isis.epad.plugin.server;

import edu.stanford.hakan.aim3api.base.GeometricShape;
import edu.stanford.hakan.aim3api.base.GeometricShapeCollection;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;
import edu.stanford.hakan.aim3api.base.SpatialCoordinate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author alansnyder
 * Date: 9/7/12
 */
public abstract class AbstractPluginServletHandler implements PluginServletHandler
{
    protected static final Logger logger = PluginLogger.getLogger();

    /**
     * The J2EE equivalent of init
     */
    @Override
    public abstract void init();

    /**
     * The J2EE equivalent of destroy.
     */
    @Override
    public abstract void destroy();

    /**
     * Standard J2EE doGet handler that is forwarded from the DicomProxy
     *
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    @Override
    public abstract void doGet(HttpServletRequest req, HttpServletResponse res);

    /**
     * Standard J2EE doPost handler that is forwarded from the DicomProxy
     *
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     */
    @Override
    public abstract void doPost(HttpServletRequest req, HttpServletResponse res);

    /**
     * Very short string to identify this version of the release.
     *
     * @return String example   "1.0.0 - Aug. 15, 2012"
     */
    @Override
    public abstract String getVersion();

    /**
     * Define a globally unique name to call this plugin. It should
     * be only lower-case alpha characters [a-z]
     *
     * @return String
     */
    @Override
    public abstract String getName();

    @Override
    public abstract AimShape[] getValidShapes();

    /**
     * Contact info for the author(s).
     *
     * @return String
     */
    @Override
    public abstract String getAuthorsContactInfo();

    /**
     * Short description (less than 100 words) of what the plugin does.
     *
     * @return String
     */
    @Override
    public abstract String getDescription();


    protected void outputException(Throwable t,PrintWriter out){

        //make for web-page response
        out.println("  message: "+t.getMessage());
        out.println(" ");
        out.println( printStackTrace(t) );

        //log
        logger.log(Level.WARNING,t.getMessage(),t);
    }


    protected String printStackTrace(Throwable throwable){
        StringBuilder sb = new StringBuilder();

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        for(StackTraceElement currElement : stackTraceElements){
            sb.append(currElement.getClassName());
            sb.append(" : ");
            sb.append(currElement.getMethodName());
            sb.append(" line: ");
            sb.append(currElement.getLineNumber());
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean hasValidShape(ImageAnnotation imageAnnotation)
    {
        //get the valid shapes.
        Set<AimShape> validShapes = new HashSet<AimShape>(Arrays.asList(getValidShapes()));

        GeometricShapeCollection gsc = imageAnnotation.getGeometricShapeCollection();
        List<GeometricShape> shapeList = gsc.getGeometricShapeList();
        AimShape currShape=null;
        for(GeometricShape shape : shapeList){
            List<SpatialCoordinate> coordList = shape.getSpatialCoordinateList();
            if(coordList.size()==1){
                currShape = AimShape.POINT;
            }
            else if(coordList.size()==2){
                if(AimUtils.isCircle(imageAnnotation)){
                    currShape=AimShape.CIRCLE;
                }else{
                    currShape=AimShape.LINE;
                }
            }
            else if(coordList.size()==3){
                //This is either a polygon(closed), rectangle, or polyline(open)
                if(AimUtils.isMultiPoint(imageAnnotation)){
                    currShape = AimShape.OPEN_POLYGON;
                }else{
                    currShape = AimShape.CLOSED_POLYGON;
                }
            }
            //do we have a match?
            if(validShapes.contains(currShape)){
                return true;
            }
        }//for
        //no valid shape found
        return false;
    }
}
