package org.adangel.textcryptor;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

// https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html
// https://crypto.stackexchange.com/questions/26783/ciphertext-and-tag-size-and-iv-transmission-with-aes-in-gcm-mode
// https://security.stackexchange.com/questions/179204/using-pbkdf2-for-hash-and-aes-key-generation-implementation
public class Crypter {
    private static final int iterationCount = 20;
    private static final int keyLength = 256; //AES_256
    private static final int ivLength = 12; // 12 bytes
    private static final int saltLength = 8; // 8 bytes
    private SecureRandom random = new SecureRandom();

    private Cipher getCipher(int mode, char[] pw, byte[] salt, byte[] iv) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(pw, salt, iterationCount, keyLength);
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            SecretKey secretKeyAes = new SecretKeySpec(secretKey.getEncoded(), "AES");

            int myTLen = 128;
            GCMParameterSpec myParams = new GCMParameterSpec(myTLen, iv);
            Cipher cipher = Cipher.getInstance("AES_256/GCM/NoPadding");
            cipher.init(mode, secretKeyAes, myParams);
            return cipher;
        } catch (NoSuchAlgorithmException e) {
            for (Provider p : Security.getProviders()) {
                System.out.println(p);
                System.out.println(" " + p.getInfo());
                for (Service s : p.getServices()) {
                    String algo = s.getAlgorithm();
                    if (algo.startsWith("AES") || algo.startsWith("PBK")) {
                        System.out.println("  " + s.getAlgorithm() + " (" + s.getType() + ")");
                        
                    }
                }
            }
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] createRandomSalt() {
        byte[] bytes = new byte[saltLength];
        random.nextBytes(bytes);
        return bytes;
    }

    private byte[] createRandomIV() {
        byte[] bytes = new byte[ivLength];
        random.nextBytes(bytes);
        return bytes;
    }

    public void encrypt(Data data) {
        try {
            byte[] salt = createRandomSalt();
            byte[] iv = createRandomIV();
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, data.getPassword(), salt, iv);
            data.setEncryptedText(cipher.doFinal(data.getText().getBytes(StandardCharsets.UTF_8)));
            data.setSalt(salt);
            data.setIv(iv);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public void decrypt(Data data) throws WrongPasswordException {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, data.getPassword(), data.getSalt(), data.getIv());
            byte[] clear = cipher.doFinal(data.getEncryptedText());
            data.setText(new String(clear, StandardCharsets.UTF_8));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new WrongPasswordException(e);
        }
    }
}
