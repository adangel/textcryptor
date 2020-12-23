# TextCryptor

![logo](src/main/resources/icons/text-file-icon-64.png)

Simple text editor written in Java that
stores the text password protected and encrypted inside
itself in the jar file.
You only need to copy the complete jar onto e.g. a USB thumb drive.
This unfortunately only works on linux.

For other platforms you can save the (encrypted) text to a normal file.

## Features

*   Minimal dependencies: uses Swing, standard Java
*   Encryption via PBKDF2 and AES256

## Development

Build with `./mvnw clean package`

Run with `./mvnw exec:exec`

## Icon

The text file icon is from: https://freeicons.io/document-icon/text-file-icon-icon-25499
(Creative Commons(Attribution-NonCommercial 3.0 unported))

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

