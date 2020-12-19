package org.adangel.textcryptor;

import java.util.Base64;
import java.util.Properties;

public class Data {

    private String text = "";
    private int cursorPosition;
    private byte[] encryptedText = new byte[0];
    private char[] password = new char[0];
    private byte[] salt = new byte[0];
    private byte[] iv = new byte[0];
    private boolean dirty;
    
    public boolean isDirty() {
        return dirty;
    }
    
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
    
    public void setText(String text) {
        this.text = text;
        this.encryptedText = new byte[0];
    }
    
    public String getText() {
        return text;
    }
    
    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }
    
    public int getCursorPosition() {
        return cursorPosition;
    }
    
    public void setEncryptedText(byte[] encryptedText) {
        this.encryptedText = encryptedText;
        this.text = "";
    }
    
    public byte[] getEncryptedText() {
        return encryptedText;
    }
    
    public void setPassword(char[] password) {
        this.password = password;
    }
    
    public char[] getPassword() {
        return password;
    }
    
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
    
    public byte[] getSalt() {
        return salt;
    }
    
    public void setIv(byte[] iv) {
        this.iv = iv;
    }
    
    public byte[] getIv() {
        return iv;
    }
    
    public Properties asProperties() {
        Properties properties = new Properties();
        properties.setProperty("cursorPosition", String.valueOf(cursorPosition));
        properties.setProperty("encryptedText", Base64.getEncoder().encodeToString(encryptedText));
        properties.setProperty("salt", Base64.getEncoder().encodeToString(salt));
        properties.setProperty("iv", Base64.getEncoder().encodeToString(iv));
        return properties;
    }
    
    public void fromProperties(Properties properties) {
        this.cursorPosition = Integer.parseInt(properties.getProperty("cursorPosition", "0"));
        this.encryptedText = Base64.getDecoder().decode(properties.getProperty("encryptedText", ""));
        this.salt = Base64.getDecoder().decode(properties.getProperty("salt", ""));
        this.iv = Base64.getDecoder().decode(properties.getProperty("iv", ""));
    }
}
