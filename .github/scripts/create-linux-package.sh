#!/usr/bin/env bash

# default type, e.g. a deb package under Debian
echo "Creating installable package..."
jpackage --name textcryptor \
    --module-path target/textcryptor-*.jar \
    --module org.adangel.textcryptor \
    --icon src/main/resources/icons/text-file-icon-64.png \
    --dest target


#
# See https://github.com/AppImage/AppImageKit/wiki/Bundling-Java-apps
#

echo "Creating AppDir..."
jlink --no-header-files --no-man-pages --compress=2 --strip-debug \
    --module-path target/textcryptor-*.jar \
    --add-modules org.adangel.textcryptor \
    --launcher textcryptor=org.adangel.textcryptor/org.adangel.textcryptor.App \
    --output target/AppDir

cp src/main/resources/icons/text-file-icon-64.png target/AppDir

cat > target/AppDir/textcryptor.desktop <<EOF
[Desktop Entry]
Name=textcryptor
Exec=bin/textcryptor
Icon=text-file-icon-64
Type=Application
Categories=Utility;
Terminal=false
StartupNotify=true
EOF

cat > target/AppDir/AppRun <<'EOF'
#!/bin/sh
JLINK_VM_OPTIONS=
DIR=`dirname $0`
$DIR/bin/java $JLINK_VM_OPTIONS -m org.adangel.textcryptor/org.adangel.textcryptor.App $@
EOF
chmod a+x target/AppDir/AppRun

echo "Downloading appimagetool..."
wget -c https://github.com/AppImage/AppImageKit/releases/download/12/appimagetool-x86_64.AppImage \
    -O target/appimagetool-x86_64.AppImage
chmod +x target/appimagetool-x86_64.AppImage

echo "Creating appimage..." 
target/appimagetool-x86_64.AppImage target/AppDir/
