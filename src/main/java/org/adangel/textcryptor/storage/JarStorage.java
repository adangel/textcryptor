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

    public JarStorage() {
        if (isJar()) {
            System.out.println("Valid JAR storage");
        } else {
            System.out.println("Not a Jar file... Invalid ...");
        }
    }

    private String getOwnUrl() {
        Class<JarStorage> clazz = JarStorage.class;
        // e.g.
        // jar:file:/usr/local/lib/textcryptor-1.0-SNAPSHOT-jar-with-dependencies.jar!/org/adangel/textcryptor/storage/JarStorage.class
        URL url = clazz.getClassLoader().getResource(clazz.getName().replaceAll("\\.", "/") + ".class");
        return url.toExternalForm();
    }

    public boolean isJar() {
        return getOwnUrl().startsWith("jar:");
    }

    private Path getJarPath() {
        String url = getOwnUrl();
        url = url.substring("jar:".length(), url.indexOf('!'));
        URI fileUri = URI.create(url);
        Path path = Path.of(fileUri);
        System.out.println("Determined file: " + path);
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
                 JarOutputStream out = new JarOutputStream(Files.newOutputStream(tempJarFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE), in.getManifest());) {

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
