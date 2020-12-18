package org.adangel.textcryptor.storage;

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

    public void load(Data data) {
        Path dataPath = determineDataPath();
        if (Files.exists(dataPath)) {
            try (InputStream in = Files.newInputStream(dataPath)) {
                Properties props = new Properties();
                props.load(in);
                data.fromProperties(props);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path determineDataPath() {
        URL url = FileStorage.class.getClassLoader().getResource(FileStorage.class.getName().replaceAll("\\.", "/") + ".class");
        String resourceUrl = url.toExternalForm();
        if (!resourceUrl.startsWith("file:")) {
            throw new UnsupportedOperationException("Don't know how to deal with " + resourceUrl);
        }
        
        Path path = Paths.get(URI.create(resourceUrl)).getParent();
        if (!Files.isDirectory(path)) {
            throw new RuntimeException("Directoy " + path + " does not exist");
        }
        Path data = path.resolve("data.txt");
        System.out.println("data path: " + data);
        return data;
    }

    public void save(Data data) {
        Path filePath = determineDataPath();
        try (OutputStream out = Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            data.asProperties().store(out, "TextCryptor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
