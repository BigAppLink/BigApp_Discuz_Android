/**
 * 
 */
package com.youzu.android.framework.crypt;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

import javax.crypto.Cipher;

import android.util.Log;


/**
 * @author jiaozhu V1.0
 * @date 2014-8-12
 */
public class Rsa {
	
	private static final String LOGTAG ="Rsa";
	private static final String PKCS12 = "PKCS12";
	
	/**
     * 取得证书序列号（16进制String）
     * 
     * @param cert
     * @return
     */
    public static String getSerialNoFromCertification(X509Certificate cert) {
        if (cert != null) {
            return cert.getSerialNumber().toString(16);
        }
        return null;
    }

    public static String getSerialNoFromBase64Cert(String base64Cert) {
        X509Certificate cert = getX509CertFromBase64Cert(base64Cert);
        if (cert != null) {
            return cert.getSerialNumber().toString(16);
        } else {
            return null;
        }
    }

    /**
     * 转换Base64证书为X509证书
     * 
     * @param cert
     * @return
     */
    public static X509Certificate getX509CertFromBase64Cert(String base64Cert) {
        if (base64Cert == null) {
            return null;
        }
        return getX509CertFromBytes(Base64.decode(base64Cert));
    }

    /**
     * 转换byte[]为证书
     * 
     * @param cert
     * @return
     */
    private static X509Certificate getX509CertFromBytes(byte[] byteArrayCert) {
        if (byteArrayCert == null) {
            return null;
        }
        return getX509CertFromInputStream(new ByteArrayInputStream(byteArrayCert));
    }

    /**
     * 从cer流获取证书
     * 
     * @param streamCert
     * @return
     */
    private static X509Certificate getX509CertFromInputStream(InputStream streamCert) {
        X509Certificate cert = null;
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) factory.generateCertificate(streamCert);
            // System.out.println(cert.getBasicConstraints());
        } catch (CertificateException ex) {
            Log.e(ex.getMessage(), ex.toString());
        }
        return cert;
    }

    /**
     * 从p12证书文件获取用户证书
     * 
     * @param pfxFileName
     * @param password
     * @return
     */
    public static X509Certificate getX509CertFromPfxFile(String pfxFileName, String password) {
        if (pfxFileName == null || password == null) {
            return null;
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(pfxFileName);
            return getX509CertFromPfxInputStream(in, password);
        } catch (FileNotFoundException ex) {
            Log.e(LOGTAG,ex.getMessage());
        }
        return null;
    }

    /**
     * 从p12证书流获取用户证书
     * 
     * @param in
     * @param password
     * @return
     */
    private static X509Certificate getX509CertFromPfxInputStream(InputStream in, String password) {
        try {
            KeyStore ks = KeyStore.getInstance(PKCS12);
            char[] passwd = password.toCharArray();
            ks.load(in, passwd);
            in.close();
            String alias = null;
            Enumeration<String> e = ks.aliases();
            if (e.hasMoreElements()) {
                alias = e.nextElement();
            }
            X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
            return cert;
        } catch (Exception ex) {
            Log.e(LOGTAG,ex.getMessage());
        }
        return null;
    }

    /**
     * Generates Public Key from BASE64 encoded string
     * 
     * @param key
     *            BASE64 encoded string which represents the key
     * @return The PublicKey
     * @throws java.lang.Exception
     */
    public static PublicKey getPublicKeyFromBase64String(String base64PublicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("Rsa");

            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decode(base64PublicKey));
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception ex) {
            Log.e(LOGTAG,ex.getMessage());
            return null;
        }
    }

    /**
     * 从pfx文件得到公钥
     * 
     * @param certPath
     * @param password
     * @return
     */
    public static PublicKey getPublicKeyByPfxFile(String certPath, String password) {
        X509Certificate cert = getX509CertFromPfxFile(certPath, password);
        if (cert != null) {
            return cert.getPublicKey();
        } else {
            return null;
        }
    }

    /**
     * 从p12证书文件获得私钥
     * 
     * @param certPath
     * @param password
     * @return
     */
    public static PrivateKey getPrivateKeyByPfxFile(String certPath, String password) {
        if (certPath == null || password == null) {
            return null;
        }
        try {
            FileInputStream in = null;
            in = new FileInputStream(certPath);
            return getPrivateKeyByInputStream(in, password);
        } catch (IOException ex) {
            Log.e(LOGTAG,ex.getMessage());
        }
        return null;
    }

    private static PrivateKey getPrivateKeyByInputStream(FileInputStream in, String password) {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(PKCS12);
            ks.load(in, password.toCharArray());
            in.close();
            String alias = null;
            Enumeration<String> e = ks.aliases();
            while (e.hasMoreElements()) {
                alias = e.nextElement();
            }
            return (PrivateKey) ks.getKey(alias, password.toCharArray());
        } catch (Exception ex) {
            Log.e(LOGTAG,ex.getMessage());
        }
        return null;
    }

    /**
     * 转换Base64字符串为私钥
     * 
     * @param base64PrivateKey
     * @return
     */
    public static PrivateKey getPrivateKeyFromBase64String(String base64PrivateKey) {
        try {
            byte[] privateKeyBytes = Base64.decode(base64PrivateKey);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFact = KeyFactory.getInstance("Rsa");
            PrivateKey privateKey = keyFact.generatePrivate(keySpec);

            return privateKey;
        } catch (Exception ex) {
            Log.e(LOGTAG,ex.getMessage());
        }
        return null;
    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * RSA加密 ,用证书加密，直接加密data,非数字信封,data长度有限制。
     * 
     * @param data
     * @param cert
     * @return
     */
    public static String encryptRSA(byte[] data, X509Certificate cert) {
        try {
            Cipher cipher = Cipher.getInstance("Rsa/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
            byte[] encrypted = cipher.doFinal(data);
            return Base64.encode(encrypted);
        } catch (Exception ex) {
            Log.e(LOGTAG,ex.getMessage());
            return null;
        }
    }

    /**
     * RSA加密 ,用 公钥加密，直接加密data,非数字信封,data长度有限制。
     * 
     * @param data
     * @param pubKey
     * @return
     */
    public static String encryptRSA(byte[] data, PublicKey pubKey) {
        try {
            Cipher cipher = Cipher.getInstance("Rsa/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encrypted = cipher.doFinal(data);
            return Base64.encode(encrypted);
        } catch (Exception ex) {
            Log.e(LOGTAG,ex.getMessage());
            return null;
        }
    }

    /**
     * RSA非对称解密，字节解密 base64EnvelopedData的 数据，非数字信封解密
     * 
     * @param base64EnvelopedData
     * @param key
     * @return
     */
    public static byte[] decryptRSA(String base64EnvelopedData, PrivateKey key) {
        try {
            Cipher cipher = Cipher.getInstance("Rsa/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] content = Base64.decode(base64EnvelopedData);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            Log.e(LOGTAG,ex.getMessage());
            return null;
        }
    }

}
