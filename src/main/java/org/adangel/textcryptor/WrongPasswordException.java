package org.adangel.textcryptor;

public class WrongPasswordException extends Exception {
    private static final long serialVersionUID = 1598562574127218472L;

    public WrongPasswordException(Throwable cause) {
        super(cause);
    }
}
