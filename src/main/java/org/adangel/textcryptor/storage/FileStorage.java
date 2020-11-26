package org.adangel.textcryptor.storage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.adangel.textcryptor.Crypter;

public class FileStorage implements Storage {

    public byte[] load() {
        Path data = determineDataPath();
        if (Files.exists(data)) {
            try {
                byte[] raw = Files.readAllBytes(data);
                return raw;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new byte[0];
    }

    public String load(char[] pw) {
        byte[] raw = load();

        if (raw.length == 0) {
            return "(no data exists yet)";
        }

        Crypter crypter = new Crypter();
        return crypter.decrypt(raw, pw);
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

    public void save(byte[] data) {
        Path filePath = determineDataPath();
        try {
            Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(String text, char[] pw) {
        Crypter crypter = new Crypter();
        byte[] raw = crypter.encrypt(text, pw);
        save(raw);
    }
}
