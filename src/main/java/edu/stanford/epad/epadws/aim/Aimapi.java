package edu.stanford.epad.epadws.aim;

import java.util.List;

import edu.stanford.epad.epadws.aim.aimapi.LoggedInUser;
import edu.stanford.epad.epadws.aim.aimapi.Patient;
import edu.stanford.hakan.aim3api.base.GeometricShape;
import edu.stanford.hakan.aim4api.projects.epad.GeometricShape.ROIShape;
import edu.stanford.hakan.aim4api.projects.epad.TwoDCoordinate;
import edu.stanford.hakan.aim4api.wrappers.epad.old.AE;
import edu.stanford.hakan.aim4api.wrappers.epad.old.GeoShape;
import edu.stanford.hakan.aim4api.wrappers.epad.old.I;
import edu.stanford.hakan.aim4api.wrappers.epad.old.IO;

/**
 * Interface for aimapi which will be implemented in edu.stanford.hakan.aim4api
 * eventually.
 * 
 * @author debra willrett
 * 
 */
public interface Aimapi {

	//
	// patients
	//

	Patient getPatient();

	void setPatient(Patient patient);

	//
	// users
	//

	LoggedInUser getLoggedInUser();

	void setLoggedInUser(LoggedInUser loggedInUser);

	//
	// aim
	//

	void clearValues();

	//
	// recist
	//

	boolean isBaseline();

	boolean isFollowUp();

	boolean isTarget();

	boolean isNonTarget();

	boolean isResolved();

	boolean isNew();

	String getLesionStatus();

	String getLesionLocation();

}
