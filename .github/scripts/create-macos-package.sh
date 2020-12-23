#!/usr/bin/env bash

# default type, e.g. a disk image (dmg) on macos
echo "Creating installable package..."
jpackage --name textcryptor \
    --module-path target/textcryptor-*.jar \
    --module org.adangel.textcryptor \
    --dest target

