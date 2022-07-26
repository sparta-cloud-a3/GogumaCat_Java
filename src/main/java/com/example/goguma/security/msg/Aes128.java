package com.example.goguma.security.msg;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;

public class Aes128 {

    private static final String aes128SecretKey = "";
    public static byte[] aes128ivBytes = aes128SecretKey.getBytes();
    public static byte[] aes128ivBytesUtf8 = aes128SecretKey.getBytes(StandardCharsets.UTF_8);

    public static String getAES128encode(String encodeData) {
        String result = "";
        try {
            byte[] textBytes = encodeData.getBytes("UTF-8");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(aes128ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(aes128ivBytesUtf8, "AES");
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

            return Base64Utils.encodeToString(cipher.doFinal(textBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getAES128decode(String decodeData) {
        String result = "";
        try {
            byte[] textBytes = Base64Utils.decode(decodeData.getBytes());
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(aes128ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(aes128ivBytesUtf8, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

            return new String(cipher.doFinal(textBytes), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}