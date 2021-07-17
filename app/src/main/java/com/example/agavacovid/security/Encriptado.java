package com.example.agavacovid.security;



import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class Encriptado{


    private static SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] claveEncriptacion = clave.getBytes("UTF-8");

        MessageDigest sha = MessageDigest.getInstance("SHA-1");

        claveEncriptacion = sha.digest(claveEncriptacion);
        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 16);

        SecretKeySpec secretKey = new SecretKeySpec(claveEncriptacion, "AES");

        return secretKey;
    }


    @SuppressLint("NewApi")
    public static String encriptar(String datos, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        int tLen = 16;
        byte[] basicIV = new byte[tLen];
        IvParameterSpec ivSpec = new IvParameterSpec(basicIV);
        SecretKeySpec secretKey = Encriptado.crearClave(claveSecreta);

        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        basicIV = new byte[tLen];
        ivSpec = new IvParameterSpec(basicIV);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] datosEncriptar = datos.getBytes("UTF-8");
        byte[] bytesEncriptados = cipher.doFinal(datosEncriptar);
        String encriptado = Base64.getEncoder().encodeToString(bytesEncriptados);

        return encriptado;

    }


    @SuppressLint("NewApi")
    public static String desencriptar(String datosEncriptados, String claveSecreta) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SecretKeySpec secretKey = Encriptado.crearClave(claveSecreta);
        int tLen = 16;
        byte[] basicIV = new byte[tLen];
        IvParameterSpec ivSpec = new IvParameterSpec(basicIV);

        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        basicIV = new byte[tLen];
        ivSpec = new IvParameterSpec(basicIV);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] bytesEncriptados = Base64.getDecoder().decode(datosEncriptados);
        byte[] datosDesencriptados = cipher.doFinal(bytesEncriptados);
        String datos = new String(datosDesencriptados);

        return datos;
    }
}