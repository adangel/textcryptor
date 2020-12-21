# TextCryptor

Simple text editor written in Java that
stores the text encrypted inside itself.
You only need to copy the complete jar onto
e.g. a USB thumb drive.

## Features

*   Minimal dependencies: uses Swing, standard Java
*   Encryption via PBKDF2 and AES

## Development

Build with `./mvnw clean package`

Run with `./mvnw exec:exec`

## Runtime Image

    jlink --module-path target/textcryptor-1.0-SNAPSHOT.jar:$JAVA_HOME/jmods \
      --add-modules org.adangel.textcryptor \
      --launcher textcryptor=org.adangel.textcryptor/org.adangel.textcryptor.App \
      --output jlink-app

Then run it with: `jlink-app/bin/textcryptor`

## License

```
Copyright 2020 Andreas Dangel

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

