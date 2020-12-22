/*
 * Copyright 2020 Andreas Dangel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.adangel.textcryptor.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.adangel.textcryptor.Data;

public class JarStorage implements Storage {

    private String getOwnUrl() {
        Class<JarStorage> clazz = JarStorage.class;
        // e.g.
        // jar:file:/usr/local/lib/textcryptor-1.0-SNAPSHOT-jar-with-dependencies.jar!/org/adangel/textcryptor/storage/JarStorage.class
        URL url = clazz.getClassLoader().getResource(clazz.getName().replaceAll("\\.", "/") + ".class");
        return url.toExternalForm();
    }

    @Override
    public String toString() {
        return "JarStorage: " + getJarPath() + "!/data.txt";
    }

    public boolean isJar() {
        if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
            return getOwnUrl().startsWith("jar:");
        }
        return false;
    }

    public Path getJarPath() {
        String url = getOwnUrl();
        url = url.substring("jar:".length(), url.indexOf('!'));
        URI fileUri = URI.create(url);
        Path path = Path.of(fileUri);
        return path;
    }

    @Override
    public void load(Data data) {
        try (JarFile jarFile = new JarFile(getJarPath().toFile());) {
            ZipEntry entry = jarFile.getEntry("data.txt");
            if (entry != null) {
                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    Properties props = new Properties();
                    props.load(inputStream);
                    data.fromProperties(props);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Data data) {
        try {
            Path tempJarFile = Files.createTempFile("TextCryptor-temp", ".jar");
            Path originalJarFile = getJarPath();
            try (JarInputStream in = new JarInputStream(Files.newInputStream(originalJarFile, StandardOpenOption.READ));
                    JarOutputStream out = new JarOutputStream(
                            Files.newOutputStream(tempJarFile, StandardOpenOption.CREATE,
                                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE),
                            in.getManifest());) {

                JarEntry entry = in.getNextJarEntry();
                boolean written = false;
                while (entry != null) {
                    out.putNextEntry(entry);
                    if ("data.txt".equals(entry.getName())) {
                        data.asProperties().store(out, "TextCryptor");
                        written = true;
                    } else {
                        in.transferTo(out);
                    }
                    in.closeEntry();
                    out.closeEntry();
                    entry = in.getNextJarEntry();
                }

                if (!written) {
                    entry = new JarEntry("data.txt");
                    out.putNextEntry(entry);
                    data.asProperties().store(out, "TextCryptor");
                    out.close();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            Files.move(tempJarFile, originalJarFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
