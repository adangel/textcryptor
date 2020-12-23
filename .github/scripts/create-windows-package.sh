#!/usr/bin/env bash

WIX_URL=https://github.com/wixtoolset/wix3/releases/download/wix3112rtm/wix311-binaries.zip
WIX_FILE=$(basename ${WIX_URL})
WIX_DIR=wixtools
curl --location --output ${WIX_FILE} ${WIX_URL}
mkdir ${WIX_DIR}
pushd ${WIX_DIR}
7z x ../${WIX_FILE}
popd

export PATH=$(pwd)/${WIX_DIR}:${PATH}

JAR_FILE=target/textcryptor-*.jar
VERSION=$(echo $JAR_FILE)
VERSION=${VERSION##target/textcryptor-}
VERSION=${VERSION%%-SNAPSHOT.jar}

# default type, e.g. a exe installer under Windows
echo "Creating installable package..."
jpackage --name textcryptor \
    --module-path target/textcryptor-*.jar \
    --module org.adangel.textcryptor \
    --app-version ${VERSION} \
    --win-menu \
    --dest target

