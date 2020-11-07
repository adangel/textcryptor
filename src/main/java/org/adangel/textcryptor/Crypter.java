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
    private final char[] password = "Foo123".toCharArray();
    private static final int iterationCount = 20;
    private static final int keyLength = 256; //AES_256
    private final byte[] iv = new byte[] {1,2,3,4,5,6,7,8,9,10,11,12}; // 12 bytes
    private final byte[] salt = new byte[] {1,2,3,4,5,6,7,8};

    private Cipher getCipher(int mode, char[] pw) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            //byte[] salt = new byte[8];
            //SecureRandom random = new SecureRandom();
            //random.nextBytes(salt);
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
    
    public byte[] encrypt(String text, char[] pw) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, pw);
            
            return cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String decrypt(byte[] data, char[] pw) {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, pw);
            
            byte[] clear = cipher.doFinal(data);
            return new String(clear, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            //throw new RuntimeException(e);
            return "(decrypting failed: " + e.toString() + ")";
        }
    }
}
