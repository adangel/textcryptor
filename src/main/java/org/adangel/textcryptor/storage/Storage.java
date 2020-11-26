package org.adangel.textcryptor.storage;

public interface Storage {

    byte[] load();

    void save(byte[] data);
}
