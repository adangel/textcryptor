# TextCryptor

Simple text editor written in Java that
stores the text encrypted inside itself.
You only need to copy the complete jar onto
e.g. a USB thumb drive.

## Features

*   Minimal dependencies: uses Swing, standard Java
*   Encryption via PBKDF2 and AES

## Runtime Image

    jlink --module-path target/textcryptor-1.0-SNAPSHOT.jar:$JAVA_HOME/jmods \
      --add-modules org.adangel.textcryptor \
      --launcher textcryptor=org.adangel.textcryptor/org.adangel.textcryptor.App \
      --output jlink-app

Then run it with: `jlink-app/bin/textcryptor`

