package org.adangel.textcryptor.storage;

import org.adangel.textcryptor.Data;

public interface Storage {

    void load(Data data);

    void save(Data data);
}
