package org.adangel.textcryptor.storage;

public class StorageProvider {

    public static Storage getSupported() {
        JarStorage jarStorage = new JarStorage();
        if (jarStorage.isJar()) {
            return jarStorage;
        }
        
        FileStorage fileStorage = new FileStorage();
        return fileStorage;
    }
}
