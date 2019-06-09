#!/usr/bin/env bash

inp="../icons"
out="../widgets/src/main/res/drawable"
for z in $inp/*.svg
do
    name=$(basename $z).xml
    svg2vd $z -o $name;
    echo ""
done
for xml in *.xml
do
    cp -n $xml $out
    echo $xml->$out
done
rename -f 's/_xml//' $out/*.xml
rename -f 's/_svg//' $out/*.xml
rm *.xml

kotlinc -script -verbose _update_drawables.kts