#!/bin/bash

function is_gnu_sed(){
  sed --version >/dev/null 2>&1
}

GRADLE_FILENAME=build.gradle.kts
LINECOUNT_TEXT=`wc -l $GRADLE_FILENAME`
set $LINECOUNT_TEXT
LINECOUNT="$(($1+1))"
echo "Linecount $GRADLE_FILENAME: $LINECOUNT lines"
INSERTLINE="$(($LINECOUNT-2))"
echo "Inserting publishing.gradle at line: $INSERTLINE"

if is_gnu_sed; then
    sed -i "${INSERTLINE}i \    if(project.name != \"app\" && project.name != \"gradle\" && project.name != \"dependencies\") {" $GRADLE_FILENAME
else
    sed -i '' "${INSERTLINE}i\\
    if(project.name != \"app\" && project.name != \"gradle\" && project.name != \"dependencies\") \{
" $GRADLE_FILENAME
fi
INSERTLINE="$(($INSERTLINE+1))"


if is_gnu_sed; then
    sed -i "${INSERTLINE}i \        apply(from = \"\$rootDir/gradle/publishing.gradle\")" $GRADLE_FILENAME
else
    sed -i '' "${INSERTLINE}i\\
        apply(from = \"\$rootDir/gradle/publishing.gradle\")
" $GRADLE_FILENAME
fi
INSERTLINE="$(($INSERTLINE+1))"


if is_gnu_sed; then
    sed -i "${INSERTLINE}i \    }" $GRADLE_FILENAME
else
    sed -i '' "${INSERTLINE}i\\
    }
" $GRADLE_FILENAME
fi
echo "Successfully extended $GRADLE_FILENAME, now ready to let the gradle wrapper be invoked"
