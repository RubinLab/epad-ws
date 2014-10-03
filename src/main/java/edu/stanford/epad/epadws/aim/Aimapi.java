package edu.stanford.epad.epadws.aim;

import java.util.Date;

import edu.stanford.epad.epadws.aim.aimapi.LoggedInUser;
import edu.stanford.epad.epadws.aim.aimapi.Patient;

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
	public boolean hasSeries(String seriesID);
	public boolean hasImage(String imageID);
	public String getFirstSeriesID();
	public String getSeriesID(String imageID);
	public String getFirstStudyID();
	public String getStudyID(String seriesID);
	public Date getFirstStudyDate();
	public String getPatientID();
	public String getPatientName();
	public String getFirstImageID();
	public boolean hasImage();
	public boolean hasSegmentation();
	public boolean hasSegmentationImage(String imageID);
	public String getFirstSegmentationImageID();
;
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
