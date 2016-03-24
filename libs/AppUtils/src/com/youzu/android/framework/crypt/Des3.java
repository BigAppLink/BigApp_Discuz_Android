package com.youzu.android.framework.crypt;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.annotation.SuppressLint;
import android.util.Log;

public class Des3
{
	
	/**
     * 3DES加密
     * 
     * @param encryptedHexTxt
     * @param keyTxt
     * @return
     */
    public static String encryptDESede(String originalTxt, String keyTxt) {

        try {
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

            SecretKeySpec skeySpec = new SecretKeySpec(keyTxt.getBytes("utf-8"), "DESede");
            IvParameterSpec iv = new IvParameterSpec(new byte[] { 50, 51, 52, 53, 54, 55, 56, 57 });
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] originalBytes = originalTxt.getBytes("utf-8");

            byte[] encryptedBytes = cipher.doFinal(originalBytes);
            return bytesToHexString(encryptedBytes);
        } catch (Exception ex) {
            Log.e("encryptDESede",ex.getMessage());
            return null;
        }
    }

    /**
     * 3DES解密
     * 
     * @param encryptedHexTxt
     * @param keyTxt
     * @return
     */
    public static String decryptDESede(String encryptedHexTxt, String keyTxt) {

        try {
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

            SecretKeySpec skeySpec = new SecretKeySpec(keyTxt.getBytes("utf-8"), "DESede");
            IvParameterSpec iv = new IvParameterSpec(new byte[] { 50, 51, 52, 53, 54, 55, 56, 57 });
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] encryptedBytes = hexStringToBytes(encryptedHexTxt);

            byte[] original = cipher.doFinal(encryptedBytes);
            return new String(original);
        } catch (Exception ex) {
            Log.e("decryptDESede",ex.getMessage());
            return null;
        }
    }
    
    @SuppressLint("DefaultLocale")
    public static String bytesToHexString(byte[] src) {
        StringBuffer sb = new StringBuffer();
        if (src == null || src.length == 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.length() < 1)
            return null;
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            int high = Integer.parseInt(hexString.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexString.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
	
}
