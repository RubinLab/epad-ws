package edu.stanford.isis.test;

/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), available at http://sourceforge.net/projects/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa HealthCare.
 * Portions created by the Initial Developer are Copyright (C) 2010
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See listed authors below.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

/**
 * CID 7150 Segmentation Property Categories.
 *
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Rev: 13502 $ $Date:: 2010-06-09#$
 * @since Jun 2, 2010
 */

/**
 * <p>Add code meaning to the string definition.</p>
 *
 * @author	Wei Lu  (lvwei@stanford.edu; luwei@tju.edu.cn)
 * @date 2012-12
 */
public class SegmentationPropertyCategories {
	  /** (T-D000A, SRT, "Anatomical Structure") */
	  public static final String AnatomicalStructure = "T-D000A\\SRT\\AnatomicalStructure";

	  /** (R-42019, SRT, "Function") */
	  public static final String Function = "R-42019\\SRT\\Function";

	  /** (M-01000, SRT, "Morphologically Altered Structure") */
	  public static final String MorphologicallyAlteredStructure = "M-01000\\SRT\\MorphologicallyAlteredStructure";

	  /** (A-00004, SRT, "Physical object") */
	  public static final String PhysicalObject = "A-00004\\SRT\\PhysicalObject";

	  /** (T-D0050, SRT, "Tissue") */
	  public static final String Tissue = "T-D0050\\SRT\\Tissue";

}