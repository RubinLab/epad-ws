package edu.stanford.isis.epad.plugin.server;

import edu.stanford.hakan.aim3api.base.GeometricShape;
import edu.stanford.hakan.aim3api.base.ImageAnnotation;

/**
 *
 * Original author d.willrett.
 *
 *
 * This code is from the epad1909 project
 * package br.usp.icmc.biomac.client.view.utils.AimUtils;
 *
 *
 * We are going to ask Hakan to
 * add these to the AIMv3API since they are usef
 *
 * Date: 9/28/12
 */
public class AimUtils {

    private AimUtils(){}

    /**
     *  is this a circle shape?
     * @param ia
     * @return
     */
    public static boolean isCircle(ImageAnnotation ia) {
        if (ia != null) {
        for (GeometricShape gs : ia.getGeometricShapeCollection()
                .getGeometricShapeList()) {
            if (gs.getXsiType().equalsIgnoreCase("Circle")) {
                return true;
            }
        }
        }
        return false;
    }

    /**
     *  is this a polyline shape?
     * @param ia
     * @return
     */
    public static boolean isPolyline(ImageAnnotation ia) {
        if (ia != null) {
        for (GeometricShape gs : ia.getGeometricShapeCollection()
                .getGeometricShapeList()) {
            if (gs.getXsiType().equalsIgnoreCase("Polyline")) {
                if (gs.getLineStyle().contains("normal")) {
                    return true;
                }
            }
        }
        }
        return false;
    }

    /**
     *  is this a multipoint shape?
     * @param ia
     * @return
     */
    public static boolean isMultiPoint(ImageAnnotation ia) {
        if (ia != null) {
        for (GeometricShape gs : ia.getGeometricShapeCollection()
                .getGeometricShapeList()) {
            if (gs.getXsiType().equalsIgnoreCase("MultiPoint")) {
                return true;
            }
        }
        }
        return false;
    }

    /**
     *  is this a spline shape? default to spline for a polyline for now
     *  because the linestyle is not saving 'curve' and 'normal'
     * @param ia
     * @return
     */
    public static boolean isSpline(ImageAnnotation ia) {
        if (ia != null) {
        for (GeometricShape gs : ia.getGeometricShapeCollection().getGeometricShapeList()) {
            if (gs.getXsiType().equalsIgnoreCase("Polyline")) {
                //if (gs.getLineStyle().contains("curve")) {
                    return true;
                //}
            }
        }
        }
        return false;
    }




}
