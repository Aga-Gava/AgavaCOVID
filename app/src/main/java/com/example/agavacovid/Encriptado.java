package com.example.agavacovid;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encriptado {
    private static String initVector="0123456789abcdef0123456789abcdef";

    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    @SuppressLint("NewApi")
    public static String encrypt(String mensaje) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {

        IvParameterSpec iv = new IvParameterSpec (initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec (getKeyFromPassword("cacnea", "HKJ273384").toString().getBytes("UTF-8"), "AES/CFB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encryp = cipher.doFinal(mensaje.getBytes());
        return new String(Base64.getEncoder().encode(encryp));
    }

    @SuppressLint("NewApi")
    public static String decrypt(String mensaje) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
        IvParameterSpec iv = new IvParameterSpec (initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec (getKeyFromPassword("cacnea", "HKJ273384").toString().getBytes("UTF-8"), "AES/CFB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(mensaje));
        return new String(original);
    }
}
