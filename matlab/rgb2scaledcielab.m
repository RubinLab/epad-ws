function scaledcielab = rgb2scaledcielab(rgb)
%keep the same as epad
scaledcielab=scaleLab4DSO(rgb2lab(double(rgb)));

end

function result= ade(r)
if r>0.04045
    result=((double(((r+0.055)/1.055))^(2.4)));
else
    result=(r/12.92);
end
end
function xyz= rgb2xyz(rgb)

ad=ade(rgb(1)/255.0);
ae=ade(rgb(2)/255.0);
af=ade(rgb(3)/255.0);

x=ad*41.24+ae*35.76+af*18.05;
y=ad*21.26+ae*71.52+af*7.22;
z=ad*1.93+ae*11.92+af*95.05;
xyz=[x y z];
% disp('xyz');
% disp(xyz);

end

function result= ijk(r)
if r>0.008856
    result=(power(r,(1.0/3.0)));
else
    result=((7.787*r)+(16.0/116.0));
end
end
function lab= xyz2lab(xyz)
%D65 Daylight reference
xr=95.047;
yr=100;
zr=108.883;

ag=xyz(1)/xr;
ah=xyz(2)/yr;
ai=xyz(3)/zr;

aj=ijk(ag);
ak=ijk(ah);
al=ijk(ai);

l=(116*ak)-16;
a=500*(aj-ak);
b=200*(ak-al);

lab=[ l a b];
% disp('lab');
% disp(lab);
end

function lab= rgb2lab(rgb)
lab= xyz2lab(rgb2xyz(rgb));
end
function scaledcielab= scaleLab4DSO(lab)
scaledcielab(1)=uint16(floor(lab(1)/100.0*65535.0)); %0:100
scaledcielab(2)=uint16(floor((lab(2)+128.0)/255.0 *65535.0)); %-128:127
scaledcielab(3)=uint16(floor((lab(3)+128.0)/255.0 *65535.0)); %-128:127
% disp(scaledcielab);
end