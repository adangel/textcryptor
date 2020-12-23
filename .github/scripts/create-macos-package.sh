#!/usr/bin/env bash

JAR_FILE=target/textcryptor-*.jar
VERSION=$(echo $JAR_FILE)
VERSION=${VERSION##target/textcryptor-}
VERSION=${VERSION%%-SNAPSHOT.jar}

# default type, e.g. a disk image (dmg) on macos
echo "Creating installable package..."
jpackage --name textcryptor \
    --module-path target/textcryptor-*.jar \
    --module org.adangel.textcryptor \
    --app-version ${VERSION} \
    --icon src/main/resources/icons/text-file-icon-64.png \
    --mac-package-name "TextCryptor" \
    --dest target

