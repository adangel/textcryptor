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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import org.adangel.textcryptor.Data;

public class FileStorage implements Storage {
    private final Path filePath;

    public FileStorage() {
        filePath = determineDataPath();
    }

    public FileStorage(File file) {
        filePath = file.toPath();
    }

    @Override
    public String toString() {
        return "FileStorage: " + filePath;
    }

    public void load(Data data) {
        if (Files.exists(filePath)) {
            try (InputStream in = Files.newInputStream(filePath)) {
                Properties props = new Properties();
                props.load(in);
                data.fromProperties(props);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path determineDataPath() {
        URL url = FileStorage.class.getClassLoader()
                .getResource(FileStorage.class.getName().replaceAll("\\.", "/") + ".class");
        String resourceUrl = url.toExternalForm();
        if (!resourceUrl.startsWith("file:")) {
            throw new UnsupportedOperationException("Don't know how to deal with " + resourceUrl);
        }

        Path path = Paths.get(URI.create(resourceUrl)).getParent();
        if (!Files.isDirectory(path)) {
            throw new RuntimeException("Directoy " + path + " does not exist");
        }
        Path data = path.resolve("data.txt");
        return data;
    }

    public void save(Data data) {
        try (OutputStream out = Files.newOutputStream(filePath, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            data.asProperties().store(out, "TextCryptor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
