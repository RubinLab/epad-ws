function result = extractDSOsFromRT(seriesDir,rtPath, outputDir)

tic
%% load the files we need
% disp('Loading CT and RTSTRUCT')
struct = RtStruct(DicomObj(rtPath));
% [m,~]=lastwarn();
% if contains(m,'Not enough data imported')      
%     disp('Could not read with vreuristics. Trying without ');
%     struct = RtStruct(rtPath);
%     lastwarn('');
% end 
% ctScan = CtScan([seriesDir '/*.dcm']);
ctScan = CtScan(seriesDir);
ctScan = ctScan.readDicomData();
%refImage = createImageFromCt(ctScan, false);
 refImage = createImageFromCt(ctScan);

numOfSegments=0;
for i = 1:length(struct.contourNames)
cn = struct.contourNames{i};
%    cn='Tumor 125';
disp(['Processing ' cn]);

try
    tic
    contour = createContour(struct, cn);

    voi = createVolumeOfInterest(contour, refImage);
    mask= createContourMaskFromVoi(voi);
    
%     disp('writing dso');
    rotatedMask=imrotate(permute(mask.uncompressedPixelData,[1 3 2]),90);
%    voi=voi.addPixelData(rotatedMask);

    rgb=uint8(contour.colorRgb.*255);
%     disp(rgb);
    scaled=rgb2scaledcielab(rgb);
%     disp(scaled);
    segment_info.Color=scaled;
    write_DSO(seriesDir,cn,rotatedMask, outputDir, true, false,segment_info);
    toc
    numOfSegments=numOfSegments+1;
    
 catch MExc
     warning(MExc.message);
 end

end
result=numOfSegments;
toc
