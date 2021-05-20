package com.example.agavacovid;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encriptado {
/*    private static SecretKey key = AESUtil.generateKey(256);
    private static String initVector="";

    @SuppressLint("NewApi")
    public static String encrypt(String mensaje) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        IvParameterSpec iv = new IvParameterSpec (initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec (key.getBytes("UTF-8"), "AES/CFB/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encryp = cipher.doFinal(mensaje.getBytes());
        return new String(Base64.getEncoder().encode(encryp));
    }

    @SuppressLint("NewApi")
    public static String decrypt(String mensaje) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        IvParameterSpec iv = new IvParameterSpec (initVector.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec (key.getBytes("UTF-8"), "AES/CBC/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(Base64.getDecoder().decode(mensaje));
        return new String(original);
    }
*/
}
