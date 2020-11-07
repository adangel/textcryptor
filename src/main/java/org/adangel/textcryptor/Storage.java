package org.adangel.textcryptor;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Storage {

    public String load() {
        Path data = determineDataPath();
        if (Files.exists(data)) {
            try {
                byte[] raw = Files.readAllBytes(data);
                Crypter crypter = new Crypter();
                return crypter.decrypt(raw);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "(no data exists yet)";
    }

    private Path determineDataPath() {
        URL url = Storage.class.getClassLoader().getResource(Storage.class.getName().replaceAll("\\.", "/") + ".class");
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
    
    public void save(String text) {
        Path data = determineDataPath();
        try {
            Crypter crypter = new Crypter();
            byte[] raw = crypter.encrypt(text);
            Files.write(data, raw,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
