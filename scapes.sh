#!/usr/bin/env bash

# This script is used to regenerate all images.
FILES=$(rg -l "AquascapeApp" scala)

mkdir -p scapes
pushd scapes || exit
for FILE in $FILES
do
    scala-cli run ../project.scala ../"$FILE"
done
popd || exit
	    
