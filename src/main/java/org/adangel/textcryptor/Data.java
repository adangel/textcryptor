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

package org.adangel.textcryptor;

import java.util.Base64;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

import javax.swing.UIManager;

public class Data implements Publisher<Data> {
    private static final String PROP_IV = "iv";
    private static final String PROP_SALT = "salt";
    private static final String PROP_CURSOR_POSITION = "cursorPosition";
    private static final String PROP_ENCRYPTED_TEXT = "encryptedText";
    private static final String PROP_FONT_SIZE = "fontSize";
    private static final String PROP_LINE_WRAP = "lineWrap";
    private static final String PROP_LINE_NUMBERS = "lineNumbers";
    private static final String PROP_LOOK_AND_FEEL = "lookAndFeel";

    private String text = "";
    private boolean dirty;
    private char[] password = new char[0];

    private Set<Subscriber<? super Data>> subscribers = new CopyOnWriteArraySet<>();

    @Override
    public void subscribe(Subscriber<? super Data> subscriber) {
        subscribers.add(subscriber);
    }

    private void publish() {
        subscribers.forEach(s -> s.onNext(this));
    }

    private final Properties properties = new Properties();

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
        publish();
    }

    public void setText(String text) {
        this.text = text;
        properties.remove(PROP_ENCRYPTED_TEXT);
        publish();
    }

    public String getText() {
        return text;
    }

    public void setCursorPosition(int cursorPosition) {
        properties.setProperty(PROP_CURSOR_POSITION, String.valueOf(cursorPosition));
        publish();
    }

    public int getCursorPosition() {
        return Integer.parseInt(properties.getProperty(PROP_CURSOR_POSITION, "0"));
    }

    public void setEncryptedText(byte[] encryptedText) {
        properties.setProperty(PROP_ENCRYPTED_TEXT, Base64.getEncoder().encodeToString(encryptedText));
        this.text = "";
    }

    public byte[] getEncryptedText() {
        return Base64.getDecoder().decode(properties.getProperty(PROP_ENCRYPTED_TEXT, ""));
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public char[] getPassword() {
        return password;
    }

    public void setSalt(byte[] salt) {
        properties.setProperty(PROP_SALT, Base64.getEncoder().encodeToString(salt));
    }

    public byte[] getSalt() {
        return Base64.getDecoder().decode(properties.getProperty(PROP_SALT, ""));
    }

    public void setIv(byte[] iv) {
        properties.setProperty(PROP_IV, Base64.getEncoder().encodeToString(iv));
    }

    public byte[] getIv() {
        return Base64.getDecoder().decode(properties.getProperty(PROP_IV, ""));
    }

    public int getFontSize() {
        return Integer.parseInt(properties.getProperty(PROP_FONT_SIZE, "12"));
    }

    public void setFontSize(int fontSize) {
        properties.setProperty(PROP_FONT_SIZE, String.valueOf(fontSize));
        publish();
    }

    public boolean isLineWrap() {
        return Boolean.parseBoolean(properties.getProperty(PROP_LINE_WRAP, Boolean.TRUE.toString()));
    }

    public void setLineWrap(boolean lineWrap) {
        properties.setProperty(PROP_LINE_WRAP, Boolean.toString(lineWrap));
        publish();
    }

    public boolean isLineNumbers() {
        return Boolean.parseBoolean(properties.getProperty(PROP_LINE_NUMBERS, Boolean.TRUE.toString()));
    }

    public void setLineNumbers(boolean lineNumbers) {
        properties.setProperty(PROP_LINE_NUMBERS, Boolean.toString(lineNumbers));
        publish();
    }

    public String getLookAndFeel() {
        return properties.getProperty(PROP_LOOK_AND_FEEL, UIManager.getCrossPlatformLookAndFeelClassName());
    }

    public void setLookAndFeel(String lookAndFeel) {
        properties.setProperty(PROP_LOOK_AND_FEEL, lookAndFeel);
        publish();
    }

    public Properties asProperties() {
        Properties copy = new Properties();
        copy.putAll(properties);
        return copy;
    }

    public void fromProperties(Properties properties) {
        this.properties.putAll(properties);
        publish();
    }
}
