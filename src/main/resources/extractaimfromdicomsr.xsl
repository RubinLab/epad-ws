<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright (c) 2015-2018, David A. Clunie DBA Pixelmed Publishing. All rights reserved. -->

<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
version="1.0">

<xsl:output method="xml" indent="yes" omit-xml-declaration="no"/>

<xsl:template match="DicomStructuredReport">

	<xsl:variable name="imageLibraryNode" select="/DicomStructuredReport/DicomStructuredReportContent/container[concept/@csd = 'DCM' and concept/@cv = '126000']/container[concept/@csd = 'DCM' and concept/@cv = '111028']"/>	<!-- cm="Imaging Measurement Report" cm="Image Library" -->

	<ImageAnnotationCollection
		xmlns="gme://caCORE.caCORE/4.4/edu.northwestern.radiology.AIM"
		xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		aimVersion="AIMv4_2"
		xsi:schemaLocation="gme://caCORE.caCORE/4.4/edu.northwestern.radiology.AIM AIM_v4.2_rv2_XML.xsd">



		<uniqueIdentifier  root="{normalize-space(DicomStructuredReportHeader/SOPInstanceUID/value)}"/>
		<studyInstanceUid  root="{normalize-space(DicomStructuredReportHeader/StudyInstanceUID/value)}"/>
		<seriesInstanceUid root="{normalize-space(DicomStructuredReportHeader/SeriesInstanceUID/value)}"/>
		<accessionNumber  value="{normalize-space(DicomStructuredReportHeader/AccessionNumber/value)}"/>
		
		<xsl:variable name="dateTimeRawValues">
			<xsl:value-of select="DicomStructuredReportHeader/ContentDate/value"/>
			<xsl:value-of select="DicomStructuredReportHeader/ContentTime/value"/>
		</xsl:variable>
		<dateTime value="{normalize-space($dateTimeRawValues)}"/>
		
		<xsl:variable name="username" select="normalize-space(DicomStructuredReportContent/container[concept/@csd = 'DCM' and concept/@cv = '126000']/pname[concept/@csd = 'DCM' and concept/@cv = '121008']/value)"/>	<!-- cm="Imaging Measurement Report" cm="Person Observer Name" -->
		<xsl:variable name="loginName" select="normalize-space(DicomStructuredReportContent/container[concept/@csd = 'DCM' and concept/@cv = '126000']/text[concept/@csd = 'DCM' and concept/@cv = '128774']/value)"/>	<!-- cm="Imaging Measurement Report" cm="Person Observer's Login Name" -->
		<xsl:variable name="roleInClinicalTrial" select="normalize-space(DicomStructuredReportContent/container[concept/@csd = 'DCM' and concept/@cv = '126000']/code[concept/@csd = 'DCM' and concept/@cv = '121011']/value/@cm)"/>	<!-- cm="Imaging Measurement Report" cm="Person Observer's Role in this Procedure" -->
		<xsl:variable name="numberWithinRoleInClinicalTrial" select="normalize-space(DicomStructuredReportContent/container[concept/@csd = 'DCM' and concept/@cv = '126000']/code[concept/@csd = 'DCM' and concept/@cv = '121011']/text[concept/@csd = 'DCM' and concept/@cv = '128775']/value)"/>	<!-- cm="Imaging Measurement Report" cm="Person Observer's Role in this Procedure" cm="Identifier within Person Observer's Role" -->
		<xsl:if test="string-length($username) &gt; 0
			or string-length($loginName) &gt; 0
			or string-length($roleInClinicalTrial) &gt; 0
			or string-length($numberWithinRoleInClinicalTrial) &gt; 0">
				<user>
					<name value="{$username}"/>
					<loginName value="{$loginName}"/>
					<roleInTrial value="{$roleInClinicalTrial}"/>
					<xsl:if test="string-length($numberWithinRoleInClinicalTrial) &gt; 0"> <!-- don't send it if we don't have a number since empty value is not a valid number -->
						<numberWithinRoleOfClinicalTrial value="{$numberWithinRoleInClinicalTrial}"/>
					</xsl:if>
			</user>
		</xsl:if>
		
		<xsl:variable name="manufacturerName" select="normalize-space(DicomStructuredReportHeader/Manufacturer/value)"/>
		<xsl:variable name="manufacturerModelName" select="normalize-space(DicomStructuredReportHeader/ManufacturerModelName/value)"/>
		<xsl:variable name="softwareVersion" select="normalize-space(DicomStructuredReportHeader/SoftwareVersions/value)"/>
		<xsl:if test="string-length($manufacturerName) &gt; 0
			       or string-length($manufacturerModelName) &gt; 0
				   or string-length($softwareVersion) &gt; 0">
			<equipment>
					<manufacturerName value="{$manufacturerName}"/>
					<manufacturerModelName value="{$manufacturerModelName}"/>
					<softwareVersion value="{$softwareVersion}"/>
			</equipment>
		</xsl:if>
		
		<xsl:variable name="personname" select="normalize-space(DicomStructuredReportHeader/PatientName/value)"/>
		<xsl:variable name="personid" select="normalize-space(DicomStructuredReportHeader/PatientID/value)"/>
		<xsl:variable name="birthDate" select="normalize-space(DicomStructuredReportHeader/PatientBirthDate/value)"/>
		<xsl:variable name="sex" select="normalize-space(DicomStructuredReportHeader/PatientSex/value)"/>
		<xsl:variable name="ethnicGroup" select="normalize-space(DicomStructuredReportHeader/EthnicGroup/value)"/>
		<person>
			<xsl:if test="string-length($personname) &gt; 0">
				<name value="{$personname}"/>
			</xsl:if>
			<xsl:if test="string-length($personid) &gt; 0">
				<id value="{$personid}"/>
			</xsl:if>
			<xsl:if test="string-length($birthDate) &gt; 0">
				<birthDate value="{$birthDate}"/>
			</xsl:if>
			<xsl:if test="string-length($sex) &gt; 0">
				<sex value="{$sex}"/>
			</xsl:if>
			<xsl:if test="string-length($ethnicGroup) &gt; 0">
				<ethnicGroup value="{$ethnicGroup}"/>
			</xsl:if>
		</person>
		
		<imageAnnotations>
			
			<xsl:for-each select="DicomStructuredReportContent/container[concept/@csd = 'DCM' and concept/@cv = '126000']/container[concept/@csd = 'DCM' and concept/@cv = '126010']/container[concept/@csd = 'DCM' and concept/@cv = '125007']">	<!-- cm="Imaging Measurement Report" cm="Imaging Measurements" "Measurement Group" -->
				<ImageAnnotation>
					<uniqueIdentifier root="{@uid}"/>
					<!-- per CP 1779: uniqueIdentifier is not the same as trackingUniqueIdentifier - do not populate it :( -->
					
					<xsl:variable name="findingValueNode" select="code[concept/@csd = 'DCM' and concept/@cv = '121071']/value"/>	<!-- "Finding" -->
					
					<typeCode code="{normalize-space($findingValueNode/@cv)}" codeSystemName="{normalize-space($findingValueNode/@csd)}">
						<iso:displayName xmlns:iso="uri:iso.org:21090" value="{normalize-space($findingValueNode/@cm)}"/>
					</typeCode>
					
					<dateTime value="{@datetime}"/>
					
					<name                    value=  "{normalize-space(text[concept/@csd = 'DCM' and concept/@cv = '112039']/value)}"/>	<!-- "Tracking Identifier" -->
					<comment                 value=  "{normalize-space(text[concept/@csd = 'DCM' and concept/@cv = '121106']/value)}"/>	<!-- "Comment" -->
					<trackingUniqueIdentifier root="{normalize-space(uidref[concept/@csd = 'DCM' and concept/@cv = '112040']/value)}"/>	<!-- per CP 1779: "Tracking Unique Identifier" -->

					<calculationEntityCollection>
						<xsl:for-each select="num">
							<CalculationEntity>
								<uniqueIdentifier root="{@uid}"/>
								<typeCode code="{normalize-space(concept/@cv)}" codeSystemName="{normalize-space(concept/@csd)}">
									<iso:displayName xmlns:iso="uri:iso.org:21090" value="{normalize-space(concept/@cm)}"/>
								</typeCode>
								<xsl:for-each select="code[concept/@csd = 'DCM' and concept/@cv = '121401']/value">	<!-- "Derivation" -->
									<typeCode code="{normalize-space(@cv)}" codeSystemName="{normalize-space(@csd)}">
										<iso:displayName xmlns:iso="uri:iso.org:21090" value="{normalize-space(@cm)}"/>
									</typeCode>
								</xsl:for-each>
								<xsl:for-each select="code[concept/@csd = 'SRT' and concept/@cv = 'G-C036']/value">	<!-- "Measurement Method" -->
									<typeCode code="{normalize-space(@cv)}" codeSystemName="{normalize-space(@csd)}">
										<iso:displayName xmlns:iso="uri:iso.org:21090" value="{normalize-space(@cm)}"/>
									</typeCode>
								</xsl:for-each>
								<xsl:variable name="descriptionRawValues">
									<xsl:value-of select="concept/@cm"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="code[concept/@csd = 'DCM' and concept/@cv = '121401']/value/@cm"/>
									<xsl:text> </xsl:text>
									<xsl:value-of select="code[concept/@csd = 'SRT' and concept/@cv = 'G-C036']/value/@cm"/>
								</xsl:variable>
								<description value="{normalize-space($descriptionRawValues)}"/>

								<calculationResultCollection>
									<CalculationResult type="Scalar" xsi:type="CompactCalculationResult">
										<unitOfMeasure value="{normalize-space(units/@cv)}"/>
										<dataType code="C48870" codeSystemName="NCI">
											<iso:displayName xmlns:iso="uri:iso.org:21090" value="Double"/>
										</dataType>
										<dimensionCollection>
											<Dimension>
												<index value="0"/>
												<size value="1"/>
												<label value="{normalize-space(code[concept/@csd = 'DCM' and concept/@cv = '121401']/value/@cm)}"/>	<!-- "Derivation" -->
											</Dimension>
										</dimensionCollection>
										<value value="{value}"/>
									</CalculationResult>
								</calculationResultCollection>
								
								<xsl:variable name="algorithmName" select="normalize-space(text[concept/@csd = 'DCM' and concept/@cv = '111001']/value)"/>	<!-- "Algorithm Name" -->
								<xsl:variable name="algorithmVersion" select="normalize-space(text[concept/@csd = 'DCM' and concept/@cv = '111003']/value)"/>	<!-- "Algorithm Version" -->
								<xsl:if test="string-length($algorithmName) &gt; 0
									or string-length($algorithmVersion) &gt; 0">
									<algorithm>
										<name value="{$algorithmName}"/>
										<!-- nothing for <type/> -  nothing like this is stored in DICOM SR - use fixed value :( -->
										<type code="RID12780" codeSystemName="RadLex" codeSystemVersion="3.2">
											<iso:displayName xmlns:iso="uri:iso.org:21090" value="Calculation"/>
										</type>
										<version value="{$algorithmVersion}"/>
									</algorithm>
								</xsl:if>

							</CalculationEntity>
						</xsl:for-each>
					</calculationEntityCollection>
					
					<!-- since the AIM allows the same markup (scoord) for multiple measurements (num), this will create multiple copies of the same markup with the same uid :( -->
					<xsl:if test="count(num/scoord) &gt; 0">
						<markupEntityCollection>
							<xsl:for-each select="num/scoord">
								<xsl:variable name="markupEntityType">
									<xsl:choose>
										<xsl:when test="count(polyline) &gt; 0">TwoDimensionPolyline</xsl:when>
										<xsl:otherwise>TwoDimensionGeometricShapeEntity</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<MarkupEntity xsi:type="{$markupEntityType}">
									<uniqueIdentifier root="{@uid}"/>
									<shapeIdentifier value="{position()}"/>	<!-- making this up - not persisted in SR -->
									<includeFlag value="true"/>
									<imageReferenceUid root="{image/instance}"/>
									<xsl:variable name="referencedImageFrame"><xsl:value-of select="normalize-space(image/frame)"/></xsl:variable>
									<xsl:if test="string-length($referencedImageFrame) &gt; 0">
										<referencedFrameNumber value="{$referencedImageFrame}"/>
									</xsl:if>
									<twoDimensionSpatialCoordinateCollection>
										<xsl:if test="count(polyline) &gt; 0">
											<xsl:for-each select="polyline/x">
												<TwoDimensionSpatialCoordinate>
													<coordinateIndex value="{position()-1}"/>	<!-- should be position in what for-each selects, and starts from 1 and we want 0 -->
													<x value="{normalize-space(.)}"/>
													<y value="{normalize-space(following-sibling::y[1])}"/>
												</TwoDimensionSpatialCoordinate>
											</xsl:for-each>
										</xsl:if>
									</twoDimensionSpatialCoordinateCollection>
								</MarkupEntity>
							</xsl:for-each>
						</markupEntityCollection>
						<!-- crude first attempt - assume every markup and every NUM are related - and only do it if uids are present for both :( -->
						<xsl:if test="count(num/@uid) &gt; 0
							  and count(num/scoord/@uid) &gt; 0">
							<imageAnnotationStatementCollection>
								<xsl:for-each select="num">
									<xsl:variable name="numuid" select="@uid"/>
									<xsl:for-each select="scoord">
										<ImageAnnotationStatement xsi:type="CalculationEntityReferencesMarkupEntityStatement">
											<subjectUniqueIdentifier root="{$numuid}"/>
											<objectUniqueIdentifier root="{@uid}"/>
										</ImageAnnotationStatement>
									</xsl:for-each>
								</xsl:for-each>
							</imageAnnotationStatementCollection>
						</xsl:if>
					</xsl:if>

					<xsl:if test="count(image[concept/@csd = 'DCM' and concept/@cv = '121191']) &gt; 0">
						<segmentationEntityCollection>
							<xsl:for-each select="image[concept/@csd = 'DCM' and concept/@cv = '121191']">	<!-- "Referenced Segment" -->
								<SegmentationEntity xsi:type="DicomSegmentationEntity">
									<uniqueIdentifier root="{@uid}"/>
									<xsl:variable name="segmentationSOPInstanceUID"><xsl:value-of select="normalize-space(instance)"/></xsl:variable>
									<sopInstanceUid root="{$segmentationSOPInstanceUID}"/>
									<studyInstanceUid  root="{normalize-space(/DicomStructuredReport/DicomStructuredReportHeader/CurrentRequestedProcedureEvidenceSequence/Item[normalize-space(ReferencedSeriesSequence/Item/ReferencedSOPSequence/Item/ReferencedSOPInstanceUID/value) = $segmentationSOPInstanceUID]/StudyInstanceUID/value)}"/>
									<seriesInstanceUid root="{normalize-space(/DicomStructuredReport/DicomStructuredReportHeader/CurrentRequestedProcedureEvidenceSequence/Item/ReferencedSeriesSequence/Item[normalize-space(ReferencedSOPSequence/Item/ReferencedSOPInstanceUID/value) = $segmentationSOPInstanceUID]/SeriesInstanceUID/value)}"/>
									<sopClassUid root="{normalize-space(class)}"/>
									<referencedSopInstanceUid root="{normalize-space(../image[concept/@csd = 'DCM' and concept/@cv = '121233']/instance)}"/>	<!-- "Source image for segmentation" -->
									<segmentNumber value="{normalize-space(segment)}"/>
								</SegmentationEntity>
							</xsl:for-each>
						</segmentationEntityCollection>
						<!-- crude first attempt - assume every SEG and every NUM are related - only do it fi uids are present for both :( -->
						<xsl:if test="count(image[concept/@csd = 'DCM' and concept/@cv = '121191']/@uid) &gt; 0
							      and count(num/@uid) &gt; 0">
							<imageAnnotationStatementCollection>
								<xsl:for-each select="image[concept/@csd = 'DCM' and concept/@cv = '121191']">	<!-- "Referenced Segment" -->
									<xsl:variable name="seguid" select="@uid"/>
									<xsl:for-each select="../num">
										<ImageAnnotationStatement xsi:type="CalculationEntityReferencesSegmentationEntityStatement">
											<subjectUniqueIdentifier root="{@uid}"/>
											<objectUniqueIdentifier root="{$seguid}"/>
										</ImageAnnotationStatement>
									</xsl:for-each>
								</xsl:for-each>
							</imageAnnotationStatementCollection>
						</xsl:if>
					</xsl:if>
					
					<imageReferenceEntityCollection>
						<xsl:for-each select="/DicomStructuredReport/DicomStructuredReportHeader/CurrentRequestedProcedureEvidenceSequence/Item">
							<xsl:variable name="sopInstanceUIDOfFirstInstanceInStudy" select="normalize-space(ReferencedSeriesSequence/Item/ReferencedSOPSequence/Item/ReferencedSOPInstanceUID/value)"/>
							<!--<xsl:message>Have sopInstanceUIDOfFirstInstanceInStudy <xsl:value-of select="$sopInstanceUIDOfFirstInstanceInStudy"/></xsl:message>-->
							<xsl:variable name="imageLibraryNodeOfFirstInstanceInStudy" select="$imageLibraryNode/container[concept/@csd = 'DCM' and concept/@cv = '126200']/image[instance = $sopInstanceUIDOfFirstInstanceInStudy]"/>	<!-- "Image Library Group" -->
							<!--<xsl:message>Have imageLibraryNodeOfFirstInstanceInStudy for instance <xsl:value-of select="$imageLibraryNodeOfFirstInstanceInStudy/instance"/></xsl:message>-->
							
							<xsl:if test="$imageLibraryNodeOfFirstInstanceInStudy">		<!-- if not in Image Library then may be segmentation, which we don't want to include in imageReferenceEntityCollection -->
								<ImageReferenceEntity xsi:type="DicomImageReferenceEntity">
									<uniqueIdentifier root="{$imageLibraryNodeOfFirstInstanceInStudy/../@uid}"/>	<!-- parent will be "Image Library Group" -->
									<imageStudy>
										<instanceUid root="{normalize-space(StudyInstanceUID/value)}"/>
										<startDate       value="{normalize-space($imageLibraryNodeOfFirstInstanceInStudy/date[concept/@csd = 'DCM' and concept/@cv = '111060'])}"/>	<!-- "Study Date" -->
										<startTime       value="{normalize-space($imageLibraryNodeOfFirstInstanceInStudy/time[concept/@csd = 'DCM' and concept/@cv = '111061'])}"/>	<!-- "Study Time" -->
										<accessionNumber value="{normalize-space($imageLibraryNodeOfFirstInstanceInStudy/text[concept/@csd = 'DCM' and concept/@cv = '121022'])}"/>	<!-- "Accession Number" -->
										<xsl:for-each select="ReferencedSeriesSequence/Item">
											<xsl:variable name="sopInstanceUIDOfFirstInstanceInSeries" select="normalize-space(ReferencedSOPSequence/Item/ReferencedSOPInstanceUID/value)"/>
											<!--<xsl:message>Have sopInstanceUIDOfFirstInstanceInSeries <xsl:value-of select="$sopInstanceUIDOfFirstInstanceInSeries"/></xsl:message>-->
											<xsl:variable name="imageLibraryNodeOfFirstInstanceInSeries" select="$imageLibraryNode/container[concept/@csd = 'DCM' and concept/@cv = '126200']/image[instance = $sopInstanceUIDOfFirstInstanceInSeries]"/>	<!-- "Image Library Group" -->
											<!--<xsl:message>Have imageLibraryNodeOfFirstInstanceInSeries for instance <xsl:value-of select="$imageLibraryNodeOfFirstInstanceInSeries/instance"/></xsl:message>-->
											
											<imageSeries>
												<instanceUid root="{normalize-space(SeriesInstanceUID/value)}"/>
												<xsl:variable name="modalityValueNode" select="$imageLibraryNodeOfFirstInstanceInSeries/code[concept/@csd = 'DCM' and concept/@cv = '121139']/value"/>	<!-- "Modality" -->
												<modality code="{normalize-space($modalityValueNode/@cv)}" codeSystemName="{normalize-space($modalityValueNode/@csd)}">
													<iso:displayName xmlns:iso="uri:iso.org:21090" value="{normalize-space($modalityValueNode/@cm)}"/>
												</modality>
												<imageCollection>
													<xsl:for-each select="ReferencedSOPSequence/Item">
														<Image>
															<sopClassUid root="{normalize-space(ReferencedSOPClassUID/value)}"/>
															<sopInstanceUid root="{normalize-space(ReferencedSOPInstanceUID/value)}"/>
														</Image>
													</xsl:for-each>
												</imageCollection>
											</imageSeries>
										</xsl:for-each>
									</imageStudy>
								</ImageReferenceEntity>
							</xsl:if>
						</xsl:for-each>
					</imageReferenceEntityCollection>

				</ImageAnnotation>
			</xsl:for-each>
			
			
			
		</imageAnnotations>
		
	</ImageAnnotationCollection>

</xsl:template>

</xsl:stylesheet>
