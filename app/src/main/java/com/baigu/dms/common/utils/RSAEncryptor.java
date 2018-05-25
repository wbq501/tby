package com.baigu.dms.common.utils;

import org.apaches.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class RSAEncryptor {

//	private static final String PUBLIC_KEY= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDez4AiqpY8n+OEqsxCp2NltFaakkQjfvpGIEPwplpYKW4WLyAS9jN9/guix1rMHgnFERI1S2MNvQisS7ZbrN9HD6ZdJVocoB5qjfRe8hNF/4xTzHNOzIxVPBO+BkqQDtiwdhVv6qzEbKI+CxAAZo/Cb7w+Za3CRLJSvMUjkGRwtwIDAQAB";
//	private static final String PRIVATE_KEY= "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAN7PgCKqljyf44SqzEKnY2W0VpqSRCN++kYgQ/CmWlgpbhYvIBL2M33+C6LHWsweCcUREjVLYw29CKxLtlus30cPpl0lWhygHmqN9F7yE0X/jFPMc07MjFU8E74GSpAO2LB2FW/qrMRsoj4LEABmj8JvvD5lrcJEslK8xSOQZHC3AgMBAAECgYEAk7B1XgU3Gq5dEIZqaNAtia/VLBZCBklXuf4PKsgJ4KBtsVTBbPA3R4+KxPZh0CUlErRzHlJ/MQ8ZXaO+F9xqDqKXCIeuvgwF4g4f4FqYUv81KdpH9Kgu61/wGvO58uYJmqDdfKCs1RRQNVW5AIh2ar07eFtSxz5gWmHgPW5sNaECQQD3ZimqB2sE4P+qrCWlU8g9Y3pUapCiA3HIzWDZ2j4wzzdYcIUNK9kkwFQ5hfylymTuH/0nm/kMdEzKW29LROonAkEA5o6AWLGgY2dXGVJhcMMovHvOO+6bhqbH15npDtOmPPudV/AFfreRF7qJvpjMfz2TjzXWCAqLxyCnWC0jtacu8QJBALc7gGJBCj9b0Z/7zgoL6oOlGOZK4wJLoMuDaVcQr/0AjFJBTBn8PuBcXkUYeo3kQCqpoJrkh2l0/abmX6CU0wMCQC/09D1+tR34IOXjfOE3twF9vyg4OaGIj4kJOrEa1TlaHSBtjLcO9983re83WVIePNNNH3gMuYZyBF0TBpqCbAECQQCqq2Yha7SU+UHYQRazrQpgkt3JPSyP1BI3yEAEyUBepo3R3rgjCGquIJwDsPdeOTE4cDHGM/PA9qgpNdOci83u";

	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;

	private String privateKeyStr;
	private String publicKeyStr;

	public RSAEncryptor() {
		// load the PublicKey and PrivateKey manually
	}


	public String decryptWithBase64(String base64String) throws Exception {
		byte[] binaryData = decrypt(getPrivateKey(), new Base64().decode(base64String) /*org.apache.commons.codec.binary.Base64.decodeBase64(base46String.getBytes())*/);
		String string = new String(binaryData);
		return string;
	}

	public String encryptWithBase64(String string) throws Exception {
		byte[] binaryData = encrypt(getPublicKey(), string.getBytes());
		String base64String = new Base64().encodeAsString(binaryData) /* org.apache.commons.codec.binary.Base64.encodeBase64(binaryData) */;
		return base64String;
	}



	// convenient properties
	public static RSAEncryptor sharedInstance = null;
	public static void setSharedInstance (RSAEncryptor rsaEncryptor) {
		sharedInstance = rsaEncryptor;
	}

	/**
	 * 获取私钥
	 * @return 当前的私钥对象
	 */
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取公钥
	 * @return 当前的公钥对象
	 */
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	public String getPrivateKeyStr() {
		return privateKeyStr;
	}

	public String getPublicKeyStr() {
		return publicKeyStr;
	}

	/**
	 * 随机生成密钥对
	 */
	public void genKeyPair(){
		KeyPairGenerator keyPairGen= null;
		try {
			keyPairGen= KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair= keyPairGen.generateKeyPair();
		this.privateKey= (RSAPrivateKey) keyPair.getPrivate();
		privateKeyStr = String.valueOf(Base64.encodeBase64(this.privateKey.getEncoded()));
		this.publicKey= (RSAPublicKey) keyPair.getPublic();
		publicKeyStr = String.valueOf(Base64.encodeBase64(this.publicKey.getEncoded()));
	}

	/**
	 * 从文件中输入流中加载公钥
	 * @param in 公钥输入流
	 * @throws Exception 加载公钥时产生的异常
	 */
	public void loadPublicKey(InputStream in) throws Exception{
		try {
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			while((readLine= br.readLine())!=null){
				if(readLine.charAt(0)=='-'){
					continue;
				}else{
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载公钥
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	public void loadPublicKey(String publicKeyStr) throws Exception{
		try {
			byte[] buffer= Base64.decodeBase64(publicKeyStr.getBytes());
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
			this.publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * @return 是否成功
	 * @throws Exception
	 */
	public void loadPrivateKey(InputStream in) throws Exception{
		try {
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			String readLine= null;
			StringBuilder sb= new StringBuilder();
			while((readLine= br.readLine())!=null){
				if(readLine.charAt(0)=='-'){
					continue;
				}else{
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	public void loadPrivateKey(String privateKeyStr) throws Exception{
		try {
			byte[] buffer= Base64.decodeBase64(privateKeyStr.getBytes());
			PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			this.privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 加密过程
	 * @param publicKey 公钥
	 * @param plainTextData 明文数据
	 * @return
	 * @throws Exception 加密过程中的异常信息
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{
		if(publicKey== null){
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher= null;
		try {
			cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");//, new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output= cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 解密过程
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception{
		if (privateKey== null){
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher= null;
		try {
			cipher= Cipher.getInstance("RSA/ECB/PKCS1Padding");//, new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output= cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}



	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 字节数据转十六进制字符串
	 * @param data 输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data){
		StringBuilder stringBuilder= new StringBuilder();
		for (int i=0; i<data.length; i++){
			//取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
			//取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i<data.length-1){
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}


	public static void main(String[] args) throws Exception {
		test();
	}

	public static void test() {
		RSAEncryptor rsaEncryptor = new RSAEncryptor();
		try {
//			rsaEncryptor.genKeyPair();
//			System.out.println(rsaEncryptor.getPrivateKeyStr());
//			System.out.println(rsaEncryptor.getPublicKeyStr());
//	        	rsaEncryptor.loadPrivateKey(PRIVATE_KEY);
//	        	rsaEncryptor.loadPublicKey(PUBLIC_KEY);
			String test = "DFSDF#*SS!qwer1223";
			String testRSAEnWith64 = rsaEncryptor.encryptWithBase64(test);
			String testRSADeWith64 = rsaEncryptor.decryptWithBase64(testRSAEnWith64);
//			System.out.println("\nEncrypt: \n" + testRSAEnWith64);
//			System.out.println("\nDecrypt: \n" + testRSADeWith64);

			// NSLog the encrypt string from Xcode , and paste it here.
			// 请粘贴来自IOS端加密后的字符串
//	            String rsaBase46StringFromIOS =
//	                    "nIIV7fVsHe8QquUbciMYbbumoMtbBuLsCr2yMB/WAhm+S/kGRPlf+k2GH8imZIYQ" + "\r" +
//	                    "QBDssVLQmS392QlxS87hnwMRJIzWw6vdRv/k79TgTfu6tI/9QTqIOvNlQIqtIcVm" + "\r" +
//	                    "R/suvydoymKgdlB+ce5/tHSxfqEOLLrL1Zl2PqJSP4A=";
//
//	            String decryptStringFromIOS = rsaEncryptor.decryptWithBase64(rsaBase46StringFromIOS);
//	            System.out.println("Decrypt result from ios client: \n" + decryptStringFromIOS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}