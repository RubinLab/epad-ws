<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright (c) 2015-2018, David A. Clunie DBA Pixelmed Publishing. All rights reserved. -->

<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aim="gme://caCORE.caCORE/4.4/edu.northwestern.radiology.AIM"
	xmlns:iso="uri:iso.org:21090"
	exclude-result-prefixes="aim"
	version="1.0">

<!-- the definition of the aim namespace above is required, since the
	 ImageAnnotation element in the input contains an explicit declaration
	 of that namespace, and without it being used in the templates
	 explicitly (as in match="aim:ImageAnnotation" rather than just
	 match="ImageAnnotation"), it will not match; note that the aim
	 namespace should NOT be used for attribute names, only element
	 names, else they will NOT match (i.e., use "@patientID" rather
	 than "@aim:patientID".
-->

<!-- the exclude-result-prefixes="aim" in the above is needed to prevent
     including that (unnecessary) namespace declaration in the result;
	 the omit-xml-declaration="yes" in the <output> element below is
	 not sufficient to achieve this
-->

<!-- should really convert lowercase "RadLex" CSD to "RADLEX" :(
-->

<xsl:output method="xml" indent="yes" omit-xml-declaration="no"/>

<xsl:template name="canonicalizeCodingSchemeDesignator">
	<xsl:param name="originalCSD"/>
	<xsl:choose>
		<xsl:when test="$originalCSD = 'RadLex'">RADLEX</xsl:when>
		<xsl:otherwise><xsl:value-of select="$originalCSD"/></xsl:otherwise>
	</xsl:choose>
</xsl:template>

	<xsl:template match="aim:ImageAnnotationCollection">
		<DicomStructuredReport>
			<DicomStructuredReportHeader>
				<xsl:apply-templates select="aim:person"/>		<!-- Populates the Patient Module per PS3.21 Section A.6.1.1.1 -->
				
				<!-- Populates the General Study Module per PS3.21 Section A.6.1.1.3 -->
				<xsl:choose>
					<xsl:when test="count(aim:studyInstanceUid) &gt; 0">	<!-- per CP 1779 -->
						<StudyInstanceUID group="0020" element="000d" vr="UI"><value number="1"><xsl:value-of select="aim:studyInstanceUid/@root"/></value></StudyInstanceUID>	<!-- same study as images; assume ther is always one reference -->
						<!-- have no study date or time information but these are Type 2 so send empty -->
						<StudyDate group="0008" element="0020" vr="DA"/>
						<StudyTime group="0008" element="0030" vr="TM"/>
					</xsl:when>
					<xsl:otherwise>
						<!-- when there is no study information in AIM, put it in the same study as the (first) referenced image -->
						<!-- //aim:imageStudy was used before but is lazy ... use full path per PS3.21 -->
						<xsl:choose>
							<xsl:when test="count(aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy[1]/aim:instanceUid) &gt; 0">
								<xsl:message>Warning: ImageAnnotationCollection/studyInstanceUid missing - using first image studyInstanceUid <xsl:value-of select="aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy[1]/aim:instanceUid/@root"/> instead</xsl:message>
								<StudyInstanceUID group="0020" element="000d" vr="UI"><value number="1"><xsl:value-of select="aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy[1]/aim:instanceUid/@root"/></value></StudyInstanceUID>	<!-- same study as images; assume ther is always one reference -->
								<!-- startDate="20170113" -->
								<StudyDate group="0008" element="0020" vr="DA"><value number="1"><xsl:value-of select="aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy[1]/aim:startDate/@value"/></value></StudyDate>
								<!-- startTime="07:08:44" -->
								<xsl:variable name="startTime"><xsl:value-of select="aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy[1]/aim:startTime/@value"/></xsl:variable>
								<xsl:choose>
									<xsl:when test="string-length($startTime) = 6">
										<StudyTime group="0008" element="0030" vr="TM"><value number="1"><xsl:value-of select="$startTime"/></value></StudyTime>
									</xsl:when>
									<xsl:when test="string-length($startTime) = 8">
										<StudyTime group="0008" element="0030" vr="TM"><value number="1"><xsl:value-of select="substring($startTime,1,2)"/><xsl:value-of select="substring($startTime,4,2)"/><xsl:value-of select="substring($startTime,7,2)"/></value></StudyTime>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<xsl:message>Error: ImageAnnotationCollection/studyInstanceUid missing and no imageReferenceEntity/imageStudy/instanceUid to fall back on</xsl:message>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>

				<!-- Type 2 but unknown so send empty per PS3.21 Section A.6.1.1.3 -->
				<ReferringPhysicianName group="0008" element="0090" vr="PN"/>
				<StudyID group="0020" element="0010" vr="SH"/>
				
				<xsl:choose>
					<xsl:when test="count(aim:accessionNumber) &gt; 0">	<!-- per CP 1779 -->
						<AccessionNumber group="0008" element="0050" vr="SH"><value number="1"><xsl:value-of select="aim:accessionNumber/@value"/></value></AccessionNumber>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<!-- when there is no accessionNumber for the annotation itself use the accessionNumber of the same study as the (first) referenced image, if present -->
							<xsl:when test="count(aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy/aim:accessionNumber) &gt; 0">
								<xsl:message>Warning: ImageAnnotationCollection/accessionNumber missing - using first image accessionNumber <xsl:value-of select="aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy[1]/aim:accessionNumber/@value"/> instead</xsl:message>
								<AccessionNumber group="0008" element="0050" vr="SH"><value number="1"><xsl:value-of select="aim:imageAnnotations/aim:ImageAnnotation[1]/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity[1]/aim:imageStudy[1]/aim:accessionNumber/@value"/></value></AccessionNumber>
							</xsl:when>
							<xsl:otherwise>
								<!-- Type 2 so send empty -->
								<AccessionNumber group="0008" element="0050" vr="SH"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				
				<!-- Populates the SR Document Series Module per PS3.21 Section A.6.1.1.6 -->
				<!--  fixed Type 1 values -->
				<Modality group="0008" element="0060" vr="CS"><value number="1">SR</value></Modality>
				<SeriesNumber group="0020" element="0011" vr="IS"><value number="1">7291</value></SeriesNumber>		<!--  an arbitrary, but distinct, number as defined in PS3.21 Section A.6.1.1.3 -->
				
				<xsl:choose>
					<xsl:when test="count(aim:seriesInstanceUid) &gt; 0">	<!-- per CP 1779 -->
						<SeriesInstanceUID group="0020" element="000e" vr="UI"><value number="1"><xsl:value-of select="aim:seriesInstanceUid/@root"/></value></SeriesInstanceUID>	<!-- same study as images; assume ther is always one reference -->
					</xsl:when>
					<xsl:otherwise>
						<xsl:message>Warning: ImageAnnotationCollection/seriesInstanceUid missing - using ImageAnnotationCollection/instanceUid <xsl:value-of select="aim:instanceUid/@root"/> instead</xsl:message>
						<SeriesInstanceUID group="0020" element="000e" vr="UI"><value number="1"><xsl:value-of select="aim:instanceUid/@root"/></value></SeriesInstanceUID>
						<!-- need to generate a SeriesInstanceUID; use hardcoded for test :(:(:(:( -->
						<!--<SeriesInstanceUID group="0020" element="000e" vr="UI"><value number="1">1.3.6.1.4.1.5962.1.3.0.0.1498908445.38939.0</value></SeriesInstanceUID>-->
					</xsl:otherwise>
				</xsl:choose>

				<!-- Type 2 but unknown so send empty per PS3.21 Section A.6.1.1.6 -->
				<ReferencedPerformedProcedureStepSequence group="0008" element="1111" vr="SQ"/>
				
				
				<!-- Populates the General Equipment Module per PS3.21 Section A.6.1.1.8 -->
				
				<Manufacturer group="0008" element="0070" vr="LO"><value number="1"><xsl:value-of select="aim:equipment/aim:manufacturerName/@value"/></value></Manufacturer>
				<ManufacturerModelName group="0008" element="1090" vr="LO"><value number="1"><xsl:value-of select="aim:equipment/aim:manufacturerModelName/@value"/></value></ManufacturerModelName>
				<SoftwareVersions group="0018" element="1020" vr="LO"><value number="1"><xsl:value-of select="aim:equipment/aim:softwareVersion/@value"/></value></SoftwareVersions>
				
				
				<!-- Populates the SR Document General Module per PS3.21 Section A.6.1.1.9 -->
				
				<!--  fixed Type 1 values -->
				<InstanceNumber group="0020" element="0013" vr="IS"><value number="1">1</value></InstanceNumber>
				<CompletionFlag group="0040" element="A491" vr="CS"><value number="1">COMPLETE</value></CompletionFlag>
				<VerificationFlag group="0040" element="A493" vr="CS"><value number="1">UNVERIFIED</value></VerificationFlag>	<!--  thus do not need VerifyingObserverSequence -->
				
				<!-- <dateTime value="20170201180043" /> --> <!-- V4 AIM recent -->
				<ContentDate group="0008" element="0023" vr="DA"><value number="1"><xsl:value-of select="substring(aim:dateTime/@value,1,8)"/></value></ContentDate>
				<ContentTime group="0008" element="0033" vr="TM"><value number="1"><xsl:value-of select="substring(aim:dateTime/@value,9,6)"/></value></ContentTime>
				
				<!-- ? should condition inclusion of this on presence of aim:user/aim:name/@value ? :( -->
				<AuthorObserverSequence group="0040" element="a078" vr="SQ">
					<Item>
						<ObserverType group="0040" element="a084" vr="CS"><value number="1">PSN</value></ObserverType>
						<PersonName group="0040" element="a123" vr="PN"><value number="1"><xsl:value-of select="aim:user/aim:name/@value"/></value></PersonName>
						<PersonIdentificationCodeSequence group="0040" element="1101" vr="SQ"/> <!-- Type 2C required if PSN -->
						<InstitutionName group="0008" element="0080" vr="LO"/> <!-- Type 2 -->
						<InstitutionCodeSequence group="0008" element="0082" vr="SQ"/> <!-- Type 2 -->
					</Item>
				</AuthorObserverSequence>
				
				<CurrentRequestedProcedureEvidenceSequence group="0040" element="a375" vr="SQ">
					<xsl:apply-templates select="aim:imageAnnotations/aim:ImageAnnotation/aim:imageReferenceEntityCollection" mode="CurrentRequestedProcedureEvidenceSequence"/>
					<xsl:apply-templates select="aim:imageAnnotations/aim:ImageAnnotation/aim:segmentationEntityCollection" mode="CurrentRequestedProcedureEvidenceSequence"/> <!-- per CP 1779 -->
				</CurrentRequestedProcedureEvidenceSequence>
				
				<!-- Type 2 but unknown so send empty -->
				<PerformedProcedureCodeSequence group="0040" element="A372" vr="SQ"/>

				<!-- Populates the SOP Commom Module per PS3.21 Section A.6.1.1.11 -->
				<SOPClassUID group="0008" element="0016" vr="UI"><value number="1">1.2.840.10008.5.1.4.1.1.88.22</value></SOPClassUID>	<!-- Enhanced SR is sufficient; do not need Comprehensive -->
				<SOPInstanceUID group="0008" element="0018" vr="UI"><value number="1"><xsl:value-of select="aim:uniqueIdentifier/@root"/></value></SOPInstanceUID>
				
				<SpecificCharacterSet group="0008" element="0005" vr="CS"><value number="1">ISO_IR 192</value></SpecificCharacterSet> <!-- will be overwritten bycom.pixelmed.dicom.XMLRepresentationOfStructuredReportObjectFactory based on what characters are actually used -->
				
			</DicomStructuredReportHeader>
			<DicomStructuredReportContent>
				<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1500 Mapping of Measurement Report -->
				<container continuity="SEPARATE" sopclass="1.2.840.10008.5.1.4.1.1.88.22" template="1500" templatemappingresource="DCMR">
					<concept cm="Imaging Measurement Report" csd="DCM" cv="126000" />
					
					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1204 Mapping of Language of Content Item and Descendants (with most recent language coding scheme as per CP 1567 and CP 1779) -->
					<code relationship="HAS CONCEPT MOD"><concept cm="Language of Content Item and Descendants" csd="DCM" cv="121049"/><value cm="English" csd="RFC5646" cv="eng"/>
						<code relationship="HAS CONCEPT MOD"><concept cm="Country of Language" csd="DCM" cv="121046"/><value cm="United States" csd="ISO3166_1" cv="US"/></code>
					</code>
					
					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1001 Mapping of Observation Context, which includes (only) TID 1002 Mapping of Observer Context -->
					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1002 Mapping of Observer Context -->
					<!-- Can omit the observer type, since it defaults to person -->
					<!-- <code relationship="HAS OBS CONTEXT"><concept cm="Observer Type" csd="DCM" cv="121005"/><value cm="Person" csd="DCM" cv="121006"/></code>-->
					
					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1003 Mapping of Person Observer Identifying Attributes -->
					<xsl:apply-templates select="aim:user"/>
					
					<code relationship="HAS CONCEPT MOD">
						<concept cm="Procedure reported" csd="DCM" cv="121058"/>
						<!-- should really check both modality @code="PT" and @codeSystemName="DCM" :( -->
						<xsl:variable name="modality"><xsl:value-of select="aim:imageAnnotations/aim:ImageAnnotation/aim:imageReferenceEntityCollection/aim:ImageReferenceEntity/aim:imageStudy[1]/aim:imageSeries[1]/aim:modality/@code"/></xsl:variable>
						<xsl:choose>
							<xsl:when test="$modality = 'CT'">
								<value cm="CT unspecified body region" csd="LN" cv="25045-6"/>
							</xsl:when>
							<xsl:when test="$modality = 'MR'">
								<value cm="MRI unspecified body region" csd="LN" cv="25056-3"/>
							</xsl:when>
							<xsl:when test="$modality = 'MR'">
								<value cm="NM unspecified body region" csd="LN" cv="49118-3"/>
							</xsl:when>
							<xsl:when test="$modality = 'PT'">
								<value cm="PET unspecified body region" csd="LN" cv="44136-0"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:message>Warning: unrecognized modality <xsl:value-of select="$modality"/> when deriving procedure reported - using default code</xsl:message>
								<value cm="Imaging procedure" csd="SRT" cv="P0-0099A"/>		<!-- The default per PS3.21 -->
							</xsl:otherwise>
						</xsl:choose>
					</code>
					
					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1600 Mapping of Image Library -->
					<container continuity="SEPARATE" relationship="CONTAINS">
						<concept cm="Image Library" csd="DCM" cv="111028" />
						<xsl:apply-templates select="aim:imageAnnotations/aim:ImageAnnotation/aim:imageReferenceEntityCollection" mode="ImageLibrary"/>
					</container>
								
					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1411 Mapping of Volumetric ROI Measurements -->
					<container continuity="SEPARATE" relationship="CONTAINS">
						<concept cm="Imaging Measurements" csd="DCM" cv="126010" />
						<xsl:apply-templates select="aim:imageAnnotations"/>
					</container>
				    
				    <!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1500 Mapping of Measurement Report -->
				    <xsl:if test="count(aim:imageAnnotations/aim:ImageAnnotation/aim:imagingObservationEntityCollection/aim:ImagingObservationEntity/aim:imagingObservationCharacteristicCollection/aim:ImagingObservationCharacteristic) &gt; 0">
				        <container continuity="SEPARATE" relationship="CONTAINS">
				            <concept cm="Qualitative Evaluations" csd="UMLS" cv="C0034375" />
				            <xsl:apply-templates select="aim:imageAnnotations/aim:ImageAnnotation/aim:imagingObservationEntityCollection/aim:ImagingObservationEntity/aim:imagingObservationCharacteristicCollection/aim:ImagingObservationCharacteristic"/>
				        </container>
				    </xsl:if>
				</container>
			</DicomStructuredReportContent>
		</DicomStructuredReport>
	</xsl:template>

	<!-- templates to build header -->

	<xsl:template match="aim:person">	<!-- Populates the Patient Module per PS3.21 Section A.6.1.1.1 -->
		<PatientID group="0010" element="0020" vr="LO"><value number="1"><xsl:value-of select="aim:id/@value"/></value></PatientID>		<!-- note that AIM really is misusing the id attribute, which is reserved in XML -->
		<PatientName group="0010" element="0010" vr="PN"><value number="1"><xsl:value-of select="aim:name/@value"/></value></PatientName>
		<!-- birthDate="1819-10-17T00:00:00" -->
		<PatientBirthDate group="0010" element="0030" vr="DA"><value number="1"><xsl:value-of select="substring(aim:birthDate/@value,1,4)"/><xsl:value-of select="substring(aim:birthDate/@value,6,2)"/><xsl:value-of select="substring(aim:birthDate/@value,9,2)"/></value></PatientBirthDate>
		<PatientSex group="0010" element="0040" vr="CS"><value number="1"><xsl:value-of select="aim:sex/@value"/></value></PatientSex>
		<EthnicGroup group="0010" element="2160" vr="SH"><value number="1"><xsl:value-of select="aim:ethnicGroup/@value"/></value></EthnicGroup>
		<xsl:apply-templates/>
	</xsl:template>
	
	<!-- templates to process imageReferenceEntityCollection to build header CurrentRequestedProcedureEvidenceSequence -->

	<!-- per SR Document General Module PS3.21 Section A.6.1.1.9 -->
	<xsl:template match="aim:imageReferenceEntityCollection" mode="CurrentRequestedProcedureEvidenceSequence">
		<xsl:apply-templates select="aim:ImageReferenceEntity" mode="CurrentRequestedProcedureEvidenceSequence"/>
	</xsl:template>

	<xsl:template match="aim:ImageReferenceEntity" mode="CurrentRequestedProcedureEvidenceSequence">
		<xsl:apply-templates select="aim:imageStudy" mode="CurrentRequestedProcedureEvidenceSequence"/>
	</xsl:template>

	<xsl:template match="aim:imageStudy" mode="CurrentRequestedProcedureEvidenceSequence">
		<Item>
			<ReferencedSeriesSequence group="0008" element="1115" vr="SQ">
				<xsl:apply-templates select="aim:imageSeries" mode="CurrentRequestedProcedureEvidenceSequence"/>
			</ReferencedSeriesSequence>
			<StudyInstanceUID group="0020" element="000d" vr="UI"><value number="1"><xsl:value-of select="aim:instanceUid/@root"/></value></StudyInstanceUID>
		</Item>
	</xsl:template>

	<xsl:template match="aim:imageSeries" mode="CurrentRequestedProcedureEvidenceSequence">
		<Item>
            <ReferencedSOPSequence group="0008" element="1199" vr="SQ">
            	<xsl:apply-templates select="aim:imageCollection" mode="CurrentRequestedProcedureEvidenceSequence"/>
			</ReferencedSOPSequence>
			<SeriesInstanceUID group="0020" element="000e" vr="UI"><value number="1"><xsl:value-of select="aim:instanceUid/@root"/></value></SeriesInstanceUID>
		</Item>
	</xsl:template>

	<xsl:template match="aim:imageCollection" mode="CurrentRequestedProcedureEvidenceSequence">
		<xsl:apply-templates select="aim:Image" mode="CurrentRequestedProcedureEvidenceSequence"/>
	</xsl:template>

	<xsl:template match="aim:Image" mode="CurrentRequestedProcedureEvidenceSequence">
		<Item>
			<ReferencedSOPClassUID group="0008" element="1150" vr="UI"><value number="1"><xsl:value-of select="aim:sopClassUid/@root"/></value></ReferencedSOPClassUID>
			<ReferencedSOPInstanceUID group="0008" element="1155" vr="UI"><value number="1"><xsl:value-of select="aim:sopInstanceUid/@root"/></value></ReferencedSOPInstanceUID>
		</Item>
	</xsl:template>
	
	<!-- per CP 1779 -->
	<xsl:template match="aim:segmentationEntityCollection" mode="CurrentRequestedProcedureEvidenceSequence">
		<xsl:apply-templates select="aim:SegmentationEntity" mode="CurrentRequestedProcedureEvidenceSequence"/>
	</xsl:template>

	<xsl:template match="aim:SegmentationEntity" mode="CurrentRequestedProcedureEvidenceSequence">
		<Item>
			<ReferencedSeriesSequence group="0008" element="1115" vr="SQ">
				<Item>
					<ReferencedSOPSequence group="0008" element="1199" vr="SQ">
						<Item>
							<ReferencedSOPClassUID group="0008" element="1150" vr="UI"><value number="1"><xsl:value-of select="aim:sopClassUid/@root"/></value></ReferencedSOPClassUID>
							<ReferencedSOPInstanceUID group="0008" element="1155" vr="UI"><value number="1"><xsl:value-of select="aim:sopInstanceUid/@root"/></value></ReferencedSOPInstanceUID>
						</Item>
					</ReferencedSOPSequence>
					<SeriesInstanceUID group="0020" element="000e" vr="UI"><value number="1"><xsl:value-of select="aim:seriesInstanceUid/@root"/></value></SeriesInstanceUID>
				</Item>
			</ReferencedSeriesSequence>
			<StudyInstanceUID group="0020" element="000d" vr="UI"><value number="1"><xsl:value-of select="aim:studyInstanceUid/@root"/></value></StudyInstanceUID>
		</Item>
	</xsl:template>
	
	<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1600 Mapping of Image Library -->
	
	<xsl:template match="aim:imageReferenceEntityCollection" mode="ImageLibrary">
		<container continuity="SEPARATE" relationship="CONTAINS" uid="{aim:ImageReferenceEntity/aim:uniqueIdentifier/@root}">
			<concept cm="Image Library Group" csd="DCM" cv="126200" />
			<!-- there are no characteristics to copy from the AIM into common factored out DTID 1602 "Image Library Entry Descriptors" -->
			<!-- use full path rather than lazy .//aim:Image -->
			<xsl:apply-templates select="aim:ImageReferenceEntity/aim:imageStudy/aim:imageSeries/aim:imageCollection/aim:Image" mode="ImageLibrary"/>
		</container>
	</xsl:template>
	
	<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1601 Mapping of Image Library Entry-->
	
	<xsl:template match="aim:Image" mode="ImageLibrary">
		<image relationship="CONTAINS">
			<class><xsl:value-of select="aim:sopClassUid/@root"/></class>
			<instance><xsl:value-of select="aim:sopInstanceUid/@root"/></instance>

			<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1602 Mapping of Image Library Entry Descriptors-->
			
			<!-- ../.. entries encoded encoded at the series level in AIM -->
			<code relationship="HAS ACQ CONTEXT">
				<concept cm="Modality" csd="DCM" cv="121139" />
				<value cm="{../../aim:modality/iso:displayName/@value}" csd="{../../aim:modality/@codeSystemName}" cv="{../../aim:modality/@code}" />
			</code>
			
			<!-- ../../.. entries encoded at the study level in AIM ... -->
			<!-- accessionNumber per CP 1779 -->
			<text relationship="HAS ACQ CONTEXT"><concept cm="Accession Number" csd="DCM" cv="121022"/><value><xsl:value-of select="../../../aim:accessionNumber/@value"/></value></text>
			
			<date relationship="HAS ACQ CONTEXT"><concept cm="Study Date" csd="DCM" cv="111060"/><value><xsl:value-of select="../../../aim:startDate/@value"/></value></date>
			<xsl:variable name="startTime"><xsl:value-of select="../../../aim:startTime/@value"/></xsl:variable>
			<xsl:choose>
				<xsl:when test="string-length($startTime) = 6">
					<time relationship="HAS ACQ CONTEXT"><concept cm="Study Time" csd="DCM" cv="111061"/><value><xsl:value-of select="$startTime"/></value></time>
				</xsl:when>
				<xsl:when test="string-length($startTime) = 8">
					<time relationship="HAS ACQ CONTEXT"><concept cm="Study Time" csd="DCM" cv="111061"/><value><xsl:value-of select="substring($startTime,1,2)"/><xsl:value-of select="substring($startTime,4,2)"/><xsl:value-of select="substring($startTime,7,2)"/></value></time>
				</xsl:when>
			</xsl:choose>
		</image>
	</xsl:template>	

	<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1003 Mapping of Person Observer Identifying Attributes -->
	
	<xsl:template match="aim:user">
		<pname relationship="HAS OBS CONTEXT"><concept cm="Person Observer Name" csd="DCM" cv="121008"/><value><xsl:value-of select="aim:name/@value"/></value></pname>
		<text relationship="HAS OBS CONTEXT"><concept cm="Person Observer's Login Name" csd="DCM" cv="128774"/><value><xsl:value-of select="aim:loginName/@value"/></value></text>
		<xsl:variable name="roleInTrial"><xsl:value-of select="aim:roleInTrial/@value"/></xsl:variable>
		<xsl:choose>
			<!-- have only encountered 'Performing' so far, but may need to expand this list :( -->
			<xsl:when test="$roleInTrial = 'Performing'">
				<code relationship="HAS OBS CONTEXT"><concept cm="Person Observer's Role in this Procedure" csd="DCM" cv="121011"/><value cm="Performing" csd="DCM" cv="121094"/></code>
			</xsl:when>
		</xsl:choose>
		<xsl:variable name="numberWithinRoleOfClinicalTrial"><xsl:value-of select="aim:numberWithinRoleOfClinicalTrial/@value"/></xsl:variable>
		<xsl:if test="string-length($numberWithinRoleOfClinicalTrial) &gt; 0">
			<text relationship="HAS OBS CONTEXT"><concept cm="Identifier within Person Observer's Role" csd="DCM" cv="128775"/><value><xsl:value-of select="$numberWithinRoleOfClinicalTrial"/></value></text>
		</xsl:if>
		<xsl:apply-templates/>
	</xsl:template>

	<!-- templates to match annotations and produce one Measurement Group for each -->
	<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1411 Mapping of Volumetric ROI Measurements -->
	
	<xsl:template match="aim:imageAnnotations">
		<xsl:apply-templates select="aim:ImageAnnotation"/>
	</xsl:template>
	
	<xsl:template match="aim:ImageAnnotation">
		<container continuity="SEPARATE" relationship="CONTAINS" uid="{aim:uniqueIdentifier/@root}" datetime="{aim:dateTime/@value}">
			<concept cm="Measurement Group" csd="DCM" cv="125007" />
			<text relationship="HAS OBS CONTEXT"><concept cm="Tracking Identifier" csd="DCM" cv="112039"/><value><xsl:value-of select="aim:name/@value"/></value></text>
			<xsl:choose>
				<xsl:when test="count(aim:trackingUniqueIdentifier) &gt; 0">	<!-- CP 1779 -->
					<uidref relationship="HAS OBS CONTEXT"><concept cm="Tracking Unique Identifier" csd="DCM" cv="112040"/><value><xsl:value-of select="aim:trackingUniqueIdentifier/@root"/></value></uidref>
				</xsl:when>
				<xsl:otherwise>
					<xsl:message>Warning: ImageAnnotation/trackingUniqueIdentifier missing - using ImageAnnotation/uniqueIdentifier <xsl:value-of select="aim:uniqueIdentifier/@root"/> instead</xsl:message>
					<uidref relationship="HAS OBS CONTEXT"><concept cm="Tracking Unique Identifier" csd="DCM" cv="112040"/><value><xsl:value-of select="aim:uniqueIdentifier/@root"/></value></uidref>
				</xsl:otherwise>
			</xsl:choose>
			
			<code relationship="CONTAINS">
				<concept cm="Finding" csd="DCM" cv="121071" />
				<value cm="{aim:typeCode/iso:displayName/@value}" csd="{aim:typeCode/@codeSystemName}" cv="{aim:typeCode/@code}" />
			</code>
			
			<xsl:for-each select="aim:segmentationEntityCollection/aim:SegmentationEntity">		<!-- should only be one segmentation -->
				<!--<xsl:message>Doing SegmentationEntity</xsl:message>-->
				<xsl:variable name="segSOPInstanceUID" select="aim:sopInstanceUid/@root"/>		<!-- presumably the root attribute is because it is an II data type --> <!-- or from referencedSopInstanceUid - both are present with different values in example. DAC 2017/06/17 -->
				<!--<xsl:message>Have segSOPInstanceUID <xsl:value-of select="$segSOPInstanceUID"/></xsl:message>-->
				<xsl:variable name="segSOPClassUID" select="aim:sopClassUid/@root"/>
				<!--<xsl:message>Have segSOPClassUID <xsl:value-of select="$segSOPClassUID"/></xsl:message>-->
				<xsl:variable name="sourceImageSOPInstanceUID" select="aim:referencedSopInstanceUid/@root"/>
				<!--<xsl:message>Have sourceImageSOPInstanceUID <xsl:value-of select="$sourceImageSOPInstanceUID"/></xsl:message>-->
				<xsl:variable name="sourceImageSOPClassUID" select="../../aim:imageReferenceEntityCollection/aim:ImageReferenceEntity/aim:imageStudy/aim:imageSeries/aim:imageCollection/aim:Image[aim:sopInstanceUid/@root = $sourceImageSOPInstanceUID]/aim:sopClassUid/@root"/>
				<!--<xsl:message>Have sourceImageSOPClassUID <xsl:value-of select="$sourceImageSOPClassUID"/></xsl:message>-->
				<xsl:variable name="referencedSegmentNumber" select="aim:segmentNumber/@value"/>
				<!--<xsl:message>Have referencedSegmentNumber <xsl:value-of select="$referencedSegmentNumber"/></xsl:message>-->
				<image relationship="CONTAINS" uid="{aim:uniqueIdentifier/@root}">
					<concept cm="Referenced Segment" csd="DCM" cv="121191" />
					<class><xsl:value-of select="$segSOPClassUID"/></class>
					<instance><xsl:value-of select="$segSOPInstanceUID"/></instance>
					<segment><xsl:value-of select="$referencedSegmentNumber"/></segment>
				</image>
				<image relationship="CONTAINS">
					<concept cm="Source image for segmentation" csd="DCM" cv="121233" />
					<class><xsl:value-of select="$sourceImageSOPClassUID"/></class>
					<instance><xsl:value-of select="$sourceImageSOPInstanceUID"/></instance>
				</image>
			</xsl:for-each>
			
			<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 1411 Mapping of Volumetric ROI Measurements -->
			
			<!-- stuff factored out that is common to all measurements -->
			
			<!-- not tested with ePAD or sample AIM - based on pattern seen with pre-V4 AIM samples :( -->
			<xsl:for-each select="aim:imagingPhysicalEntityCollection/aim:ImagingPhysicalEntity[
					   aim:label/@value='Location'
					or aim:label/@value='Lobar Location'
					or aim:label/@value='Segmental Location'
					or aim:label/@value='Organ Type'
				  ]/aim:typeCode">
				<xsl:variable name="locationCV"><xsl:value-of select="@code"/></xsl:variable>
				<xsl:variable name="locationCM"><xsl:value-of select="iso:displayName/@value"/></xsl:variable>
				<xsl:variable name="locationCSD"><xsl:call-template name="canonicalizeCodingSchemeDesignator"><xsl:with-param name="originalCSD" select="@codeSystemName"/></xsl:call-template></xsl:variable>
				<xsl:if test="string-length($locationCV) &gt; 0 and string-length($locationCM) &gt; 0 and string-length($locationCSD) &gt; 0">
					<code relationship="HAS CONCEPT MOD">
						<concept cm="Finding Site" csd="SRT" cv="G-C0E3"/>
						<value cm="{$locationCM}" csd="{$locationCSD}" cv="{$locationCV}"/>
					</code>
				</xsl:if>
			</xsl:for-each>
			
			<!-- individual measurements -->
			
			<xsl:for-each select="aim:calculationEntityCollection/aim:CalculationEntity/aim:calculationResultCollection/aim:CalculationResult">
				<num relationship="CONTAINS" uid="{../../aim:uniqueIdentifier/@root}">
					<xsl:choose>
						<xsl:when test="count(../../aim:typeCode) &gt; 0">	<!-- of the parent aim:CalculationEntity -->
							<concept cm="{../../aim:typeCode[1]/iso:displayName/@value}" csd="{../../aim:typeCode[1]/@codeSystemName}" cv="{../../aim:typeCode[1]/@code}"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:message>Error - CalculationEntity/typeCode to use for NUM concept missing</xsl:message>
							<concept cm="{aim:dimensionCollection/aim:Dimension/aim:label/@value}" csd="DUMMY" cv="DUMMY"/>
						</xsl:otherwise>
					</xsl:choose>
					
					<!-- DICOM limits DS VR to 16 characters - should probably round rather than truncate :( -->
					<xsl:variable name="compactResult"><xsl:value-of select="substring(normalize-space(aim:value/@value),1,16)"/></xsl:variable>
					<xsl:variable name="extendedResult"><xsl:value-of select="substring(normalize-space(aim:calculationDataCollection/aim:CalculationData/aim:value/@value),1,16)"/></xsl:variable>
					<xsl:choose>
						<xsl:when test="string-length($compactResult) &gt; 0">
							<value><xsl:value-of select="$compactResult"/></value>
						</xsl:when>
						<xsl:when test="string-length($extendedResult) &gt; 0">
							<value><xsl:value-of select="$extendedResult"/></value>
						</xsl:when>
					</xsl:choose>
					
					<!-- AIM unitOfMeasure is defined in v4 to always be UCUM -->
					<units cm="{aim:unitOfMeasure/@value}" csd="UCUM" cv="{aim:unitOfMeasure/@value}" />
					
					<xsl:for-each select="../../aim:typeCode">	<!-- all typeCode entries of the parent aim:CalculationEntity -->
						<xsl:choose>
							<xsl:when test="position() &gt; 1">	<!-- the first has already been used for the NUM concept name -->
								<xsl:variable name="modCV"><xsl:value-of select="@code"/></xsl:variable>
								<xsl:variable name="modCSD"><xsl:value-of select="@codeSystemName"/></xsl:variable>
								<xsl:variable name="modCM"><xsl:value-of select="iso:displayName/@value"/></xsl:variable>
								<!-- use predetermined modifier type of method or derivation if recognized, else generic modifier - this is beyond what PS3.21 specifies -->
								<code relationship="HAS CONCEPT MOD">
								<xsl:choose>
									<!-- CID 3488 Min/Max/Mean: Maximum, Minimum, Mean -->
									<xsl:when test="
											($modCSD = 'SRT' and $modCV ='G-A437')
										 or ($modCSD = 'SRT' and $modCV ='R-404FB')
										 or ($modCSD = 'SRT' and $modCV ='R-00317')
									">
										<concept cm="Derivation" csd="DCM" cv="121401"/>
									</xsl:when>
									<!-- CID 7464 General Region of Interest Measurement Modifiers: Standard Deviation, Total, Median, Mode, Peak Value Within ROI, Coefficient of Variance, Skewness, Kurtosis, Variance, Root Mean Square -->
									<xsl:when test="
											($modCSD = 'SRT' and $modCV ='R-10047')
										 or ($modCSD = 'SRT' and $modCV ='R-40507')
										 or ($modCSD = 'SRT' and $modCV ='R-00319')
										 or ($modCSD = 'SRT' and $modCV ='R-0032E')
										 or ($modCSD = 'DCM' and $modCV ='126031')
										 or ($modCSD = 'UMLS' and $modCV ='C0681921')
										 or ($modCSD = 'DCM' and $modCV ='126051')
										 or ($modCSD = 'DCM' and $modCV ='126052')
										 or ($modCSD = 'UMLS' and $modCV ='C1711260')
										 or ($modCSD = 'UMLS' and $modCV ='C2347976')
									">
										<concept cm="Derivation" csd="DCM" cv="121401"/>
									</xsl:when>
									<!-- CID 6147 Response Criteria: WHO, RECIST 1.0, RECIST 1.1, RANO -->
									<xsl:when test="
											($modCSD = 'DCM'  and $modCV ='112029')
										 or ($modCSD = 'DCM'  and $modCV ='126080')
										 or ($modCSD = 'DCM'  and $modCV ='126081')
										 or ($modCSD = 'NCIt' and $modCV ='C114879')
									">
										<concept cm="Measurement Method" csd="SRT" cv="G-C036"/>
									</xsl:when>
									<xsl:otherwise>
										<concept cm="Modifier" csd="UMLS" cv="C3542952"/>	<!-- otherwise use generic modifier -->
									</xsl:otherwise>
								</xsl:choose>
										<value cm="{$modCM}" csd="{$modCSD}" cv="{$modCV}"/>
								</code>
							</xsl:when>
						</xsl:choose>
					</xsl:for-each>

					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 320 Mapping of Image or Spatial Coordinates -->
					<!-- repeat for each measurement (until we can use statement-derived relationships to disambiguate) :( -->
					<xsl:apply-templates select="../../../../aim:markupEntityCollection"/>
					
					<!-- per PS3.21 Section A.6.1.2 Content Tree - TID 4019 Mapping of Algorithm Identification -->
					<!-- algorithm information is in parent CalculationEntity -->
					<!-- ignore aim:algorithm/aim:type for now, since no TID 4019 target for coded type -->
					<xsl:variable name="algorithmName"><xsl:value-of select="normalize-space(../../aim:algorithm/aim:name/@value)"/></xsl:variable>
					<xsl:choose>
						<xsl:when test="string-length($algorithmName) &gt; 0">
							<text relationship="HAS CONCEPT MOD">
								<concept cm="Algorithm Name" csd="DCM" cv="111001"/>
								<value><xsl:value-of select="$algorithmName"/></value>
							</text>
						</xsl:when>
					</xsl:choose>
					<xsl:variable name="algorithmVersion"><xsl:value-of select="normalize-space(../../aim:algorithm/aim:version/@value)"/></xsl:variable>
					<xsl:choose>
						<xsl:when test="string-length($algorithmVersion) &gt; 0">
							<text relationship="HAS CONCEPT MOD">
								<concept cm="Algorithm Version" csd="DCM" cv="111003"/>
								<value><xsl:value-of select="$algorithmVersion"/></value>
							</text>
						</xsl:when>
					</xsl:choose>
					<!-- ignore aim:algorithm/aim:parameter class attributes for now, since not yet encountered in samples -->
					
				</num>
			</xsl:for-each>

			<!-- treat Comment as if it were a TEXT form of Qualitative Evaluation per CP 1779 -->
			<xsl:if test="string-length(aim:comment/@value) &gt; 0">
				<text relationship="CONTAINS"><concept cm="Comment" csd="DCM" cv="121106"/><value><xsl:value-of select="aim:comment/@value"/></value></text>
			</xsl:if>

		</container>
	</xsl:template>
    
    <xsl:template match="aim:ImagingObservationCharacteristic">
        <code relationship="CONTAINS">
            <xsl:choose>
                <xsl:when test="string-length(aim:questionTypeCode/@code) &gt; 0">
                    <xsl:variable name="questionCSD"><xsl:call-template name="canonicalizeCodingSchemeDesignator"><xsl:with-param name="originalCSD" select="aim:questionTypeCode/@codeSystemName"/></xsl:call-template></xsl:variable>
                    <concept cm="{aim:questionTypeCode/iso:displayName/@value}" csd="{$questionCSD}" cv="{aim:questionTypeCode/@code}"/>
                </xsl:when>
                <xsl:when test="string-length(../../aim:typeCode/@code) &gt; 0">    <!-- ImagingObservationEntity/typeCode -->
                    <xsl:variable name="imagingObservationEntityTypeCodeCSD"><xsl:call-template name="canonicalizeCodingSchemeDesignator"><xsl:with-param name="originalCSD" select="../../aim:typeCode/@codeSystemName"/></xsl:call-template></xsl:variable>
                    <concept cm="{../../aim:typeCode/iso:displayName/@value}" csd="{$imagingObservationEntityTypeCodeCSD}" cv="{../../aim:typeCode/@code}"/>
                    <xsl:message>Warning: Missing questionTypeCode for ImagingObservationCharacteristic for concept name - using parent ImagingObservationEntity/typeCode (<xsl:value-of select="../../aim:typeCode/@code"/>, <xsl:value-of select="$imagingObservationEntityTypeCodeCSD"/>, <xsl:value-of select="../../aim:typeCode/iso:displayName/@value"/>) instead</xsl:message>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message>Warning: No questionTypeCode or parent ImagingObservationEntity/typeCode for ImagingObservationCharacteristic for concept name - using generic comment</xsl:message>
                    <concept cm="Comment" csd="DCM" cv="121106"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:variable name="valueCSD"><xsl:call-template name="canonicalizeCodingSchemeDesignator"><xsl:with-param name="originalCSD" select="aim:typeCode/@codeSystemName"/></xsl:call-template></xsl:variable>
            <value cm="{aim:typeCode/iso:displayName/@value}" csd="{$valueCSD}" cv="{aim:typeCode/@code}"/>
            <!--what about: :(
						<annotatorConfidence value="0.38"/>
						<label value="Imgaging Observation Char Label One"/>
						<comment value="Imaging Observation Char Comment One"/>
						<questionIndex value="2"/>
					-->
            <!-- what about nested characteristicQuantificationCollection in Vlad's example? :(-->
        </code>
    </xsl:template>
    
	<xsl:template match="aim:markupEntityCollection">
		<xsl:apply-templates select="aim:MarkupEntity"/>
	</xsl:template>
	
	<xsl:template match="aim:MarkupEntity">
		<xsl:variable name="markupEntityUID" select="aim:uniqueIdentifier/@root"/>
		<xsl:variable name="imageSOPInstanceUID" select="aim:imageReferenceUid/@root"/>
		<xsl:variable name="imageSOPClassUID" select="//aim:Image[aim:sopInstanceUid/@root=$imageSOPInstanceUID]/aim:sopClassUid/@root"/>
		<xsl:variable name="referencedFrameNumber" select="aim:referencedFrameNumber/@value"/>

		<!-- incomplete list :( -->
		<xsl:variable name="isMultiFrameSOPClassSoNeedsReferencedFrameNumber" select="
				  $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.2.1'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.2.2'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.4.1'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.4.3'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.4.4'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.128.1'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.130'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.20'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.3.1'
			   or $imageSOPClassUID = '1.2.840.10008.5.1.4.1.1.6.2'
			   "/>

		<xsl:choose>
			<xsl:when test="@xsi:type = 'TwoDimensionPoint'">
				<!--<xsl:message>Doing TwoDimensionPoint</xsl:message>-->
				<scoord relationship="INFERRED FROM" uid="{$markupEntityUID}">
					<point>
						<xsl:for-each select="aim:twoDimensionSpatialCoordinateCollection/aim:TwoDimensionSpatialCoordinate">
							<x><xsl:value-of select="aim:x/@value"/></x>
							<y><xsl:value-of select="aim:y/@value"/></y>
						</xsl:for-each>
					</point>
					<image relationship="SELECTED FROM">
						<class><xsl:value-of select="$imageSOPClassUID"/></class>
						<instance><xsl:value-of select="$imageSOPInstanceUID"/></instance>
						<xsl:if test="isMultiFrameSOPClassSoNeedsReferencedFrameNumber">
							<frame><xsl:value-of select="$referencedFrameNumber"/></frame>
						</xsl:if>
					</image>
				</scoord>
			</xsl:when>
			<xsl:when test="@xsi:type = 'TwoDimensionMultiPoint'">
				<!--<xsl:message>Doing TwoDimensionMultiPoint</xsl:message>-->
				<scoord relationship="INFERRED FROM" uid="{$markupEntityUID}">
					<multipoint>
						<xsl:for-each select="aim:twoDimensionSpatialCoordinateCollection/aim:TwoDimensionSpatialCoordinate">
							<x><xsl:value-of select="aim:x/@value"/></x>
							<y><xsl:value-of select="aim:y/@value"/></y>
						</xsl:for-each>
					</multipoint>
					<image relationship="SELECTED FROM">
						<class><xsl:value-of select="$imageSOPClassUID"/></class>
						<instance><xsl:value-of select="$imageSOPInstanceUID"/></instance>
						<xsl:if test="isMultiFrameSOPClassSoNeedsReferencedFrameNumber">
							<frame><xsl:value-of select="$referencedFrameNumber"/></frame>
						</xsl:if>
					</image>
				</scoord>
			</xsl:when>
			<xsl:when test="@xsi:type = 'TwoDimensionPolyline'">
				<!--<xsl:message>Doing TwoDimensionPolyline</xsl:message>-->
				<scoord relationship="INFERRED FROM" uid="{$markupEntityUID}">
					<polyline>
						<xsl:for-each select="aim:twoDimensionSpatialCoordinateCollection/aim:TwoDimensionSpatialCoordinate">
							<x><xsl:value-of select="aim:x/@value"/></x>
							<y><xsl:value-of select="aim:y/@value"/></y>
						</xsl:for-each>
					</polyline>
					<image relationship="SELECTED FROM">
						<class><xsl:value-of select="$imageSOPClassUID"/></class>
						<instance><xsl:value-of select="$imageSOPInstanceUID"/></instance>
						<xsl:if test="isMultiFrameSOPClassSoNeedsReferencedFrameNumber">
							<frame><xsl:value-of select="$referencedFrameNumber"/></frame>
						</xsl:if>
					</image>
				</scoord>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- remove any white space -->
	<xsl:template match="text()" />

</xsl:stylesheet>

