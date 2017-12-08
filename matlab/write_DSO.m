function write_seg_status = write_DSO(seriesDir, Seg_name, Seg_mask, outputDir, fixOrientationBasedCoor, isFractional, segment_info)
% write_DSO:   Write a segmentation mask as a dicom segmentation object pointing
% to the appropiate study (DSO).
% Was created based on write_vol_to_dicom_DSO_V3 function by Luis de Sisternes (Stanford University)
%
% Inputs:
%   seriesDir: (String)The path of image series' files.
%   Seg_name: (String)Specifies the name of the segmention in the DSO.
%   Seg_mask: (3D Array) Input segmentation mask (in dimension order x,y,z).
%   outputDir: (String)The path for the output segmentations. File name
%   will be the segmentation name (whitespaces are replaced by _)
%   fixOrientationBasedCoor:    (Boolean) Try fixing orientation based coordinates (assumes the coordinates are calculated assuming the orientation is 1\0\0\0\1\0).
%   isFractional:    (Boolean) Write a fractional mask instead of binary
%   segment_info: (Struct) The semantic information about the segment
%   Default values are:
%     segment_info.Label='Segmentation';
%     segment_info.AlgorithmType='SEMIAUTOMATIC';
%     segment_info.AlgorithmName='ePAD';
%     segment_info.AnatomicRegionSequence.CodeValue='T-D0050';
%     segment_info.AnatomicRegionSequence.CodingSchemeDesignator='SRT';
%     segment_info.AnatomicRegionSequence.CodeMeaning='Tissue';
%     segment_info.SegmentedPropertyCategoryCodeSequence.CodeValue='T-D0050';
%     segment_info.SegmentedPropertyCategoryCodeSequence.CodingSchemeDesignator='SRT';
%     segment_info.SegmentedPropertyCategoryCodeSequence.CodeMeaning='Tissue';
%     segment_info.SegmentedPropertyTypeCodeSequence.CodeValue='T-D0050';
%     segment_info.SegmentedPropertyTypeCodeSequence.CodingSchemeDesignator='SRT';
%     segment_info.SegmentedPropertyTypeCodeSequence.CodeMeaning='Tissue';
%   can also have segment_info.Color which has no default. It should be in
%   CIELab format
%
% Outputs:
%    write_seg_status: Information about the metadata and the descriptions
%             used to generate the DSO file as returned by "dicomwrite".
%
% Author: Emel Alkim (Stanford University)

%check the parameters
switch nargin
    case 4
        fixOrientationBasedCoor=false;
        isFractional=false;
        segment_info=getDefaultSegmentInfo();
    case 5
        isFractional=false;
        segment_info=getDefaultSegmentInfo();
    case 6
        segment_info=getDefaultSegmentInfo();
    case 7 %there is segment info. fill in the missing params
        segment_info=fillMissingInSegmentInfo(segment_info);
end

files = dir([seriesDir '/*.dcm']);
insCount=size(files',2);
refSlice=dicominfo([seriesDir '/' files(1).name]);
i=1;
for file = files'
    sliceInfo=dicominfo([seriesDir '/' file.name]);
    slices(i).ImageOrientationPatient=sliceInfo.ImageOrientationPatient;
    slices(i).ImagePositionPatient=sliceInfo.ImagePositionPatient;
    slices(i).InstanceNumber=sliceInfo.InstanceNumber;
    slices(i).SOPClassUID=sliceInfo.SOPClassUID;
    slices(i).SOPInstanceUID=sliceInfo.SOPInstanceUID;
    
    i=i+1;
end
[~, ind]=sort([slices.InstanceNumber]);
slices=slices(ind);

flip=false;
if insCount>1
    soFirst=calcSliceOrder(slices(1).ImageOrientationPatient,slices(1).ImagePositionPatient);
    soLast=calcSliceOrder(slices(insCount).ImageOrientationPatient,slices(insCount).ImagePositionPatient);
    if soFirst>soLast %order is different from instance number, flip
        disp('This mask should be flipped. Flipping');
        flip=true;
    end
end

Seg_mask_filled=zeros(size(Seg_mask,1),size(Seg_mask,2),size(Seg_mask,3));

for i=1:size(Seg_mask,3)
    
    if flip %order is different from instance number, flip
        index=size(Seg_mask,3)-i+1;
    else
        index=i;
    end
    seg=Seg_mask(:,:,i);
    
    if fixOrientationBasedCoor==true
        if slices(1).ImageOrientationPatient(1)<0
            seg=fliplr(seg);
        end
        if slices(1).ImageOrientationPatient(5)<0
            seg=flipud(seg);
        end
    end
    if isFractional==true
        Seg_mask_filled(:,:,index) = seg; %no filling for fractional
    else
        Seg_mask_filled(:,:,index) = imfill(seg,'holes');
    end
end

maxes = max(max(Seg_mask_filled(:,:,:)));
startIndex=1;
endIndex=size(maxes,3);

if isFractional==true
    disp('fractional');
    Seg_mask_dcm=uint8(rescale(permute(Seg_mask_filled,[1 2 4 3]),0,255,0,4000));
else
    
    if insCount>1 %try optimizing (removing the empty slices at the beginning and end)
        for i=1:size(maxes,3)
            if maxes(i)==1
                startIndex=i;
                break;
            end
        end
        for j=size(maxes,3):-1:1
            if maxes(j)==1
                endIndex=j;
                break;
            end
        end
        if startIndex~=1 || endIndex~=size(maxes,3)
            disp(['We can optimize. Start index is ',int2str(startIndex),', end index is ',int2str(endIndex)]);
            Seg_mask_optimized=zeros(size(Seg_mask_filled,1),size(Seg_mask_filled,2),endIndex-startIndex+1);
            for i=startIndex:endIndex
                Seg_mask_optimized(:,:,i-startIndex+1)=Seg_mask_filled(:,:,i);
                
            end
        else
            Seg_mask_optimized(:,:,:)=Seg_mask_filled(:,:,:);
        end
        
    end
    Seg_mask_dcm=255*uint8(permute(Seg_mask_optimized,[1 2 4 3]));
end

%get the first slice as a reference
info= refSlice;

info_mask.FileMetaInformationVersion = [0;1];
if isfield(info,'StudyDescription')
    info_mask.StudyDescription=info.StudyDescription;
else 
    info_mask.StudyDescription='';
end
if isfield(info,'ImageOrientationPatient')==true
    info_mask.SharedFunctionalGroupsSequence.Item_1.PlaneOrientationSequence.Item_1.ImageOrientationPatient=info.ImageOrientationPatient;
    info_mask.SharedFunctionalGroupsSequence.Item_1.PixelMeasuresSequence.Item_1.SliceThickness=info.SliceThickness;
    info_mask.SharedFunctionalGroupsSequence.Item_1.PixelMeasuresSequence.Item_1.PixelSpacing=info.PixelSpacing;
else % try getting from perframe
    if isfield(info.PerFrameFunctionalGroupsSequence.Item_1.PlaneOrientationSequence.Item_1,'ImageOrientationPatient')==true
        info_mask.SharedFunctionalGroupsSequence.Item_1.PlaneOrientationSequence.Item_1.ImageOrientationPatient=info.PerFrameFunctionalGroupsSequence.Item_1.PlaneOrientationSequence.Item_1.ImageOrientationPatient;
        info_mask.SharedFunctionalGroupsSequence.Item_1.PixelMeasuresSequence.Item_1.SliceThickness=info.PerFrameFunctionalGroupsSequence.Item_1.PixelMeasuresSequence.Item_1.SliceThickness;
        info_mask.SharedFunctionalGroupsSequence.Item_1.PixelMeasuresSequence.Item_1.PixelSpacing=info.PerFrameFunctionalGroupsSequence.Item_1.PixelMeasuresSequence.Item_1.PixelSpacing;
        if isFractional==true
            %dciodvfy throws error without this but it should be
            %meaningful!!
            info_mask.PatientOrientation='';
        end
    end
end
info_mask.SharedFunctionalGroupsSequence.Item_1.SegmentIdentificationSequence.Item_1.ReferencedSegmentNumber=1;
if insCount>1
    for i=startIndex:endIndex
        slice_info=slices(i);
        ib1=i-startIndex+1;
        item_name=['Item_' num2str(ib1)];
        info_mask.ReferencedSeriesSequence.Item_1.ReferencedInstanceSequence.(item_name).ReferencedSOPClassUID=slice_info.SOPClassUID;
        info_mask.ReferencedSeriesSequence.Item_1.ReferencedInstanceSequence.(item_name).ReferencedSOPInstanceUID=slice_info.SOPInstanceUID;
        
        
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.ReferencedSOPClassUID=slice_info.SOPClassUID;
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.ReferencedSOPInstanceUID=slice_info.SOPInstanceUID;
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.PurposeOfReferenceCodeSequence.Item_1.CodeValue='121322';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.PurposeOfReferenceCodeSequence.Item_1.CodingSchemeDesignator='DCM';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.PurposeOfReferenceCodeSequence.Item_1.CodeMeaning='Source image for image processing operation';
        
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.DerivationCodeSequence.Item_1.CodeValue='113076';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.DerivationCodeSequence.Item_1.CodingSchemeDesignator='DCM';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.DerivationCodeSequence.Item_1.CodeMeaning='Segmentation';
        
        
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).FrameContentSequence.Item_1.StackID='1';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).FrameContentSequence.Item_1.InStackPositionNumber=ib1;
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).FrameContentSequence.Item_1.DimensionIndexValues= uint32([1;ib1;1]);
        if isfield(info,'ImagePositionPatient')==true
            info_mask.PerFrameFunctionalGroupsSequence.(item_name).PlanePositionSequence.Item_1.ImagePositionPatient=slice_info.ImagePositionPatient;
        else
            if isfield(info.PerFrameFunctionalGroupsSequence.Item_1.PlanePositionSequence.Item_1,'ImagePositionPatient')==true
                disp('shouldnt come here. why the information is in frames and it is not a multiframe');
                info_mask.PerFrameFunctionalGroupsSequence.(item_name).PlanePositionSequence.Item_1.ImagePositionPatient=slice_info.PerFrameFunctionalGroupsSequence.Item_1.PlanePositionSequence.Item_1.ImagePositionPatient;
            else
                disp('shouldnt happen. why the information is not there and it is not a multiframe');
                
            end
            
        end
    end
else %just one instance should be multiframe
    info=refSlice;%first image
    numOfFrames=size(Seg_mask,3);
    if numOfFrames<info.NumberOfFrames
        disp('The input mask is smaller than the frames! Assuming they start from the beginning');
    end
    %there is just one image for multiframe
    info_mask.ReferencedSeriesSequence.Item_1.ReferencedInstanceSequence.Item_1.ReferencedSOPClassUID=info.SOPClassUID;
    info_mask.ReferencedSeriesSequence.Item_1.ReferencedInstanceSequence.Item_1.ReferencedSOPInstanceUID=info.SOPInstanceUID;
    
    
    for i=1:numOfFrames
        item_name=['Item_' num2str(i)];
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.ReferencedSOPClassUID=info.SOPClassUID;
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.ReferencedSOPInstanceUID=info.SOPInstanceUID;
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.PurposeOfReferenceCodeSequence.Item_1.CodeValue='121322';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.PurposeOfReferenceCodeSequence.Item_1.CodingSchemeDesignator='DCM';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.SourceImageSequence.Item_1.PurposeOfReferenceCodeSequence.Item_1.CodeMeaning='Source image for image processing operation';
        
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.DerivationCodeSequence.Item_1.CodeValue='113076';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.DerivationCodeSequence.Item_1.CodingSchemeDesignator='DCM';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).DerivationImageSequence.Item_1.DerivationCodeSequence.Item_1.CodeMeaning='Segmentation';
        
        
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).FrameContentSequence.Item_1.StackID='1';
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).FrameContentSequence.Item_1.InStackPositionNumber=i;
        info_mask.PerFrameFunctionalGroupsSequence.(item_name).FrameContentSequence.Item_1.DimensionIndexValues= uint32([1;i;1]);
        if isfield(info.PerFrameFunctionalGroupsSequence.Item_1.PlanePositionSequence.Item_1,'ImagePositionPatient')==true
            info_mask.PerFrameFunctionalGroupsSequence.(item_name).PlanePositionSequence.Item_1.ImagePositionPatient=info.PerFrameFunctionalGroupsSequence.Item_1.PlanePositionSequence.Item_1.ImagePositionPatient;
        else
            disp('shouldnt happen. why the information is not there ');
            
        end
        
    end
end
info_mask.ReferencedSeriesSequence.Item_1.SeriesInstanceUID=info.SeriesInstanceUID;


info_mask.ReferringPhysicianName='';
info_mask.PatientName=info.PatientName;
info_mask.PatientID=info.PatientID;
info_mask.PatientBirthDate= info.PatientBirthDate;
info_mask.PatientSex= info.PatientSex;
if isfield(info,'PatientAge')==true
    info_mask.PatientAge= info.PatientAge;
end
if isfield(info,'PatientWeight')==true
    info_mask.PatientWeight= info.PatientWeight;
end
info_mask.StudyID=info.StudyID;

info_mask.MediaStorageSOPClassUID='1.2.840.10008.5.1.4.1.1.66.4'; % SEG image storage
instanceuid=dicomuid;
info_mask.MediaStorageSOPInstanceUID=instanceuid;
info_mask.TransferSyntaxUID='1.2.840.10008.1.2.1'; % Explicit VR Little Endian
info_mask.ImplementationClassUID='1.2.840.10008.5.1.4.1.1.66.4'; % SEG image storage
info_mask.ImplementationVersionName='ePAD_matlab_1.0';
info_mask.ImageType='DERIVED\PRIMARY';
%info_mask.InstanceCreatorUID='1.2.276.0.7230010.3'; %ml should be smt for
%epad
info_mask.SOPClassUID='1.2.840.10008.5.1.4.1.1.66.4';  % SEG Image Storage
info_mask.SOPInstanceUID= instanceuid;

info_mask.AccessionNumber=info.AccessionNumber;
info_mask.Modality='SEG';
info_mask.Manufacturer='Stanford University';

info_mask.ManufacturerModelName= 'ePAD Matlab';
info_mask.DeviceSerialNumber='SN123456';
info_mask.SoftwareVersion='1.0';
info_mask.StudyInstanceUID=info.StudyInstanceUID;
info_mask.SeriesInstanceUID= dicomuid;
info_mask.SeriesNumber= 1000;
info_mask.ContentDate=datestr(now,'yyyymmdd');
info_mask.StudyDate=info.StudyDate;
info_mask.SeriesDate=datestr(now,'yyyymmdd');
info_mask.AcquisitionDate=datestr(now,'yyyymmdd');
currentTime=datestr(now,'HHMMSS.FFF');
info_mask.ContentTime=currentTime;
info_mask.StudyTime=info.StudyTime;
info_mask.SeriesTime=currentTime;
info_mask.AcquisitionTime=currentTime;
info_mask.InstanceNumber= 1;
info_mask.FrameOfReferenceUID= info.FrameOfReferenceUID;
info_mask.PositionReferenceIndicator= '';

info_mask.DimensionOrganizationSequence.Item_1.DimensionOrganizationUID= dicomuid;
info_mask.DimensionIndexSequence.Item_1.DimensionIndexPointer=uint16([32 36950]);%Not writing pointers for now
info_mask.DimensionIndexSequence.Item_1.FunctionalGroupPointer=uint16([32 37137]);%Not writing pointers for now
info_mask.DimensionIndexSequence.Item_1.DimensionDescriptionLabel='Stack ID';
info_mask.DimensionIndexSequence.Item_2.DimensionIndexPointer=uint16([32 36951]);%Not writing pointers for now
info_mask.DimensionIndexSequence.Item_2.FunctionalGroupPointer=uint16([32 37137]);%Not writing pointers for now
info_mask.DimensionIndexSequence.Item_2.DimensionDescriptionLabel='In-Stack Position Number';
info_mask.DimensionIndexSequence.Item_3.DimensionIndexPointer=uint16([98 11]);%Not writing pointers for now
info_mask.DimensionIndexSequence.Item_3.FunctionalGroupPointer=uint16([98 10]);%Not writing pointers for now
info_mask.DimensionIndexSequence.Item_3.DimensionDescriptionLabel='Referenced Segment Number';
info_mask.SamplesPerPixel= 1;
info_mask.PhotometricInterpretation= 'MONOCHROME2';
info_mask.NumberOfFrames= size(Seg_mask,1);
info_mask.Rows= size(Seg_mask,2);
info_mask.Columns= size(Seg_mask,3);
if isFractional==false
    info_mask.BitsAllocated= 1;
    info_mask.BitsStored= 1;
    info_mask.HighBit= 0;
else
    info_mask.BitsAllocated= 8;
    info_mask.BitsStored= 8;
    info_mask.HighBit= 7;
end
info_mask.PixelRepresentation= 0;
info_mask.LossyImageCompression='00';
if isFractional==true
    info_mask.SegmentationType='FRACTIONAL';
    info_mask.SegmentationFractionalType='PROBABILITY';
else
    info_mask.SegmentationType='BINARY';
end

info_mask.SegmentSequence.Item_1.AnatomicRegionSequence.Item_1.CodeValue=segment_info.AnatomicRegionSequence.CodeValue;
info_mask.SegmentSequence.Item_1.AnatomicRegionSequence.Item_1.CodingSchemeDesignator=segment_info.AnatomicRegionSequence.CodingSchemeDesignator;
info_mask.SegmentSequence.Item_1.AnatomicRegionSequence.Item_1.CodeMeaning=segment_info.AnatomicRegionSequence.CodeMeaning;



info_mask.SegmentSequence.Item_1.SegmentedPropertyCategoryCodeSequence.Item_1.CodeValue=segment_info.SegmentedPropertyCategoryCodeSequence.CodeValue;
info_mask.SegmentSequence.Item_1.SegmentedPropertyCategoryCodeSequence.Item_1.CodingSchemeDesignator=segment_info.SegmentedPropertyCategoryCodeSequence.CodingSchemeDesignator;
info_mask.SegmentSequence.Item_1.SegmentedPropertyCategoryCodeSequence.Item_1.CodeMeaning=segment_info.SegmentedPropertyCategoryCodeSequence.CodeMeaning;
info_mask.SegmentSequence.Item_1.SegmentNumber=1;
info_mask.SegmentSequence.Item_1.SegmentLabel=segment_info.Label;
info_mask.SegmentSequence.Item_1.SegmentAlgorithmType=segment_info.AlgorithmType;
info_mask.SegmentSequence.Item_1.SegmentAlgorithmName=segment_info.AlgorithmName;

if isfield(segment_info,'Color')==true %if color is sent put it in the header
    info_mask.SegmentSequence.Item_1.RecommendedDisplayCIELabValue=segment_info.Color;
end
info_mask.SegmentSequence.Item_1.SegmentedPropertyTypeCodeSequence.Item_1.CodeValue=segment_info.SegmentedPropertyTypeCodeSequence.CodeValue;
info_mask.SegmentSequence.Item_1.SegmentedPropertyTypeCodeSequence.Item_1.CodingSchemeDesignator=segment_info.SegmentedPropertyTypeCodeSequence.CodingSchemeDesignator;
info_mask.SegmentSequence.Item_1.SegmentedPropertyTypeCodeSequence.Item_1.CodeMeaning=segment_info.SegmentedPropertyTypeCodeSequence.CodeMeaning;

if isFractional==true
    info_mask.MaximumFractionalValue=255;
end
info_mask.ContentCreatorsName='ePAD^matlab';
info_mask.ContentLabel= 'ROI';
%just for info display
if fixOrientationBasedCoor==true
    disp('Check if corrdinates need fixing for orientation');
    if slices(1).ImageOrientationPatient(1)<0
        disp('is going to flipping lr');
    end
    if slices(1).ImageOrientationPatient(5)<0
        disp('is going to flipping ud');
    end
end
% Save the DSO
info_mask.ContentDescription=[Seg_name 'segmentation'];
info_mask.SeriesDescription=[Seg_name ' segmentation'];

file_name=[outputDir regexprep(Seg_name,'\W','_') '.dcm'];
if exist(file_name,'file')==2
    disp('There is file with the same name already. Appending timestamp');
    t = datetime('now','Format','yyyyMMddHHmmss');
    file_name=[outputDir regexprep(Seg_name,'\W','_') '_' char(t) '.dcm'];
    
end
write_seg_status=dicomwrite(Seg_mask_dcm, file_name, info_mask,'CreateMode', 'Copy', 'TransferSyntax', '1.2.840.10008.1.2.1', 'UseMetadataBitDepths', 'true');
end

function C = rescale(A,new_min,new_max, lower_limit, upper_limit)
%modified vs of Nasser M. Abbasi 011212
current_max = upper_limit;
A(A>upper_limit)=upper_limit;
current_min = lower_limit;
A(A<lower_limit)=lower_limit;
C =((double(A)-current_min)*(new_max-new_min))/(current_max-current_min) + new_min;
end



function sliceOrder = calcSliceOrder(imageOrientation,imagePosition)
% calcSliceOrder:   Calculate the slice order based on image orientation and position
% Written based on Ted's IDL code
%
% Inputs:
%   imageOrientation: (String)The image orientation in the form it is in the header 1\0\0\0\1\0.
%   imagePosition: (String)The image position in the form it is in the
%   header 657\-356.5\103
%
% Outputs:
%    sliceOrder: A double value that can be used to compare with other
%    slice's value to identify order
%
% Author: Emel Alkim (Stanford University)
dc = zeros(2,3);
for row=1:2
    for col=1:3
        dc(row,col) = imageOrientation((row-1)*3+col);
    end
end

% z(1) = dc(1,2) * dc(2,3) - dc(2,2) * dc(1,3);
% z(2) = dc(1,3) * dc(2,1) - dc(2,3)* dc(1,1);
% z(3) = dc(1,1) * dc(2,2) - dc(2,1) * dc(1,2);
z=cross(dc(1,:), dc(2,:));
[~,maxZ]=max(abs(z));
sliceOrder= imagePosition(maxZ)/z(maxZ);
end


function segment_info = getDefaultSegmentInfo()
% getDefaultSegmentInfo:   Get default segment info
%
% Inputs:
%   none
%
% Outputs:
%   segment_info: struct with values needed in segment sequence
%
% Author: Emel Alkim (Stanford University)
segment_info.Label='Segmentation';
segment_info.AlgorithmType='SEMIAUTOMATIC';
segment_info.AlgorithmName='ePAD';
segment_info.AnatomicRegionSequence.CodeValue='T-D0050';
segment_info.AnatomicRegionSequence.CodingSchemeDesignator='SRT';
segment_info.AnatomicRegionSequence.CodeMeaning='Tissue';
segment_info.SegmentedPropertyCategoryCodeSequence.CodeValue='T-D0050';
segment_info.SegmentedPropertyCategoryCodeSequence.CodingSchemeDesignator='SRT';
segment_info.SegmentedPropertyCategoryCodeSequence.CodeMeaning='Tissue';
segment_info.SegmentedPropertyTypeCodeSequence.CodeValue='T-D0050';
segment_info.SegmentedPropertyTypeCodeSequence.CodingSchemeDesignator='SRT';
segment_info.SegmentedPropertyTypeCodeSequence.CodeMeaning='Tissue';
end

function segment_info = fillMissingInSegmentInfo(segment_info)
% fillMissingInSegmentInfo:   Fill the missing fields in segment info with
% default values
%
% Inputs:
%   segment_info
%
% Outputs:
%   segment_info: filled in struct with values needed in segment sequence
%
% Author: Emel Alkim (Stanford University)
if isfield(segment_info,'Label')==false
    segment_info.Label='Segmentation';
end
if isfield(segment_info,'AlgorithmType')==false
    segment_info.AlgorithmType='SEMIAUTOMATIC';
end
if isfield(segment_info,'AlgorithmName')==false
    segment_info.AlgorithmName='ePAD';
end

if isfield(segment_info,'AnatomicRegionSequence')==false || isfield(segment_info.AnatomicRegionSequence,'CodeValue')==false
    segment_info.AnatomicRegionSequence.CodeValue='T-D0050';
end
if isfield(segment_info,'AnatomicRegionSequence')==false || isfield(segment_info.AnatomicRegionSequence,'CodingSchemeDesignator')==false
    segment_info.AnatomicRegionSequence.CodingSchemeDesignator='SRT';
end
if isfield(segment_info,'AnatomicRegionSequence')==false || isfield(segment_info.AnatomicRegionSequence,'CodeMeaning')==false
    segment_info.AnatomicRegionSequence.CodeMeaning='Tissue';
end
if isfield(segment_info,'SegmentedPropertyCategoryCodeSequence')==false || isfield(segment_info.SegmentedPropertyCategoryCodeSequence,'CodeValue')==false
    segment_info.SegmentedPropertyCategoryCodeSequence.CodeValue='T-D0050';
end
if isfield(segment_info,'SegmentedPropertyCategoryCodeSequence')==false || isfield(segment_info.SegmentedPropertyCategoryCodeSequence,'CodingSchemeDesignator')==false
    segment_info.SegmentedPropertyCategoryCodeSequence.CodingSchemeDesignator='SRT';
end
if isfield(segment_info,'SegmentedPropertyCategoryCodeSequence')==false || isfield(segment_info.SegmentedPropertyCategoryCodeSequence,'CodeMeaning')==false
    segment_info.SegmentedPropertyCategoryCodeSequence.CodeMeaning='Tissue';
end
if isfield(segment_info,'SegmentedPropertyTypeCodeSequence')==false || isfield(segment_info.SegmentedPropertyTypeCodeSequence,'CodeValue')==false
    segment_info.SegmentedPropertyTypeCodeSequence.CodeValue='T-D0050';
end
if isfield(segment_info,'SegmentedPropertyTypeCodeSequence')==false || isfield(segment_info.SegmentedPropertyTypeCodeSequence,'CodingSchemeDesignator')==false
    segment_info.SegmentedPropertyTypeCodeSequence.CodingSchemeDesignator='SRT';
end
if isfield(segment_info,'SegmentedPropertyTypeCodeSequence')==false || isfield(segment_info.SegmentedPropertyTypeCodeSequence,'CodeMeaning')==false
    segment_info.SegmentedPropertyTypeCodeSequence.CodeMeaning='Tissue';
end
end