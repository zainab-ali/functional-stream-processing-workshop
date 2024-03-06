#!/usr/bin/env bash

# This script is used to regenerate all images.
FILES=$(rg -l "AquascapeApp.Simple.File" scala)

mkdir -p scapes
pushd scapes || exit
for FILE in $FILES
do
    scala-cli run ../project.scala ../"$FILE"
done
popd scapes || exit
	    
