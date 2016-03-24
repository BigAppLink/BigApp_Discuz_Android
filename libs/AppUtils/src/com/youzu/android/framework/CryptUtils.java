package com.youzu.android.framework;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.youzu.android.framework.crypt.Aes;
import com.youzu.android.framework.crypt.Base64;
import com.youzu.android.framework.crypt.Des3;
import com.youzu.android.framework.crypt.Md5;
import com.youzu.android.framework.crypt.Rsa;

public class CryptUtils {
	
	public static String getMD5(String str){
		return Md5.getMD5(str);
	}
	
	
	//Base64
	public static String base64Encode(byte[] binaryData){
		return Base64.encode(binaryData);
	}
	
	public static byte[] base64Decode(String encoded){
		return Base64.decode(encoded);
	}
	
	
	//Des3
	public static String encryptDESede(String originalTxt, String keyTxt){
		return Des3.encryptDESede(originalTxt, keyTxt);
	}
	
	public static String decryptDESede(String encryptedHexTxt, String keyTxt){
		return Des3.decryptDESede(encryptedHexTxt, keyTxt);		
	}
	
	//Aes
	public static String encryptAES(String originalTxt, String keyTxt){
		return Aes.encryptAES(originalTxt, keyTxt);
	}
	
	public static String decryptAES(String encryptedHexTxt, String keyTxt){
		return Aes.decryptAES(encryptedHexTxt, keyTxt);
	}
	
	//Rsa
	public static PublicKey getPublicKeyFromBase64String(String base64PublicKey){
		return Rsa.getPublicKeyFromBase64String(base64PublicKey);
	}
	
	public static PrivateKey getPrivateKeyFromBase64String(String base64PrivateKey){
		return Rsa.getPrivateKeyFromBase64String(base64PrivateKey);
	}
	
	public static String encryptRSA(byte[] data, PublicKey pubKey){
		return Rsa.encryptRSA(data, pubKey);
	}
	
	public static byte[] decryptRSA(String base64EnvelopedData, PrivateKey key){
		return Rsa.decryptRSA(base64EnvelopedData, key);
	}
	

}
