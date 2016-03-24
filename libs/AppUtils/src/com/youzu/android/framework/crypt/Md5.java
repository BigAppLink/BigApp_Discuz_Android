/**
 * 
 */
package com.youzu.android.framework.crypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author jiaozhu V1.0
 * @date 2014-8-12
 */
public class Md5 {
	public static String getMD5(String str) {  
	    MessageDigest messageDigest = null;  

	    try {  
	        messageDigest = MessageDigest.getInstance("Md5");  

	        messageDigest.reset();  

	        messageDigest.update(str.getBytes("UTF-8"));  
	    } catch (NoSuchAlgorithmException e) {  
	        System.out.println("NoSuchAlgorithmException caught!");  
	        System.exit(-1);  
	    } catch (UnsupportedEncodingException e) {  
	        e.printStackTrace();  
	    }  

	    byte[] byteArray = messageDigest.digest();  

	    StringBuffer md5StrBuff = new StringBuffer();  

	    for (int i = 0; i < byteArray.length; i++) {              
	        if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
	            md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
	        else  
	            md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));  
	    }  

	    return md5StrBuff.toString();  
	}
	
	
	public static void main (String[] args) {
		System.out.println(getMD5("admin"));
	}
}
