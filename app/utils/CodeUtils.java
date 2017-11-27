
package utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.mchange.lang.ByteUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CodeUtils {
	private static String message = "guochenfan";
	private static String encrypt;
	private static String decrypt;
	private static String secret = "123456";
	private static KeyGenerator keygen;

	public static String md5(String message) {
		encrypt = DigestUtils.md5Hex(message);
		try {
			encrypt = ByteUtils.toHexAscii(MessageDigest.getInstance("md5").digest(message.getBytes())).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
		}
		return encrypt;
	}

	public static String sha(String message) {
		encrypt = DigestUtils.sha1Hex(message);
		System.err.println("sha encode:" + encrypt);
		try {
			encrypt = ByteUtils.toHexAscii(MessageDigest.getInstance("sha").digest(message.getBytes()));
			System.err.println("sha encode:" + encrypt);
		} catch (NoSuchAlgorithmException e) {
			System.err.println("sha encode error:" + e.getMessage());
		}
		return encrypt;
	}

	public static String base64Encode(String message) {
		encrypt = new BASE64Encoder().encode(message.getBytes());
		System.err.println("base64 encode:" + encrypt);
		return encrypt;
	}

	public static String base64Decode(String encrypt) {
		try {
			decrypt = new String(new BASE64Decoder().decodeBuffer(encrypt));
			System.err.println("base64 decode:" + decrypt);
		} catch (IOException e) {
			System.err.println("base64 decode error:" + e.getMessage());
		}
		return decrypt;
	}
	/*
	 * SecureRandom 实现完全隨操作系统本身的内部状态，除非调用方在调用 getInstance 方法之后又调用了 setSeed 方法；该实现在
	 * windows 上每次生成的 key 都相同，但是在 solaris 或部分 linux 系统上则不同。
	 * 
	 * 
	 * 加密完byte[] 后，需要将加密了的byte[] 转换成base64保存，如： BASE64Encoder base64encoder = new
	 * BASE64Encoder(); String encode=base64encoder.encode(bytes)；
	 * 解密前，需要将加密后的字符串从base64转回来再解密，如： BASE64Decoder base64decoder = new
	 * BASE64Decoder(); byte[] encodeByte = base64decoder.decodeBuffer(str);
	 */

	public static String aesEncode(String secret, String message) {
		try {
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			// 2.根据secret规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			// keygen.init(128, new SecureRandom(secret.getBytes()));
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secret.getBytes());
			keygen.init(128, secureRandom);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			byte[] byte_encode = message.getBytes("utf-8");
			// 9.根据密码器的初始化方式--加密：将数据加密
			byte[] byte_AES = cipher.doFinal(byte_encode);
			// 10.将加密后的数据转换为字符串
			// 这里用Base64Encoder中会找不到包
			// 解决办法：
			// 在项目的Build path中先移除JRE System Library，再添加库JRE System
			// Library，重新编译后就一切正常了。
			encrypt = new String(new BASE64Encoder().encode(byte_AES));
			// 11.将字符串返回
			System.err.println("aes encode:" + encrypt);
		} catch (Exception e) {
			System.err.println("aes encode error:" + e.getMessage());
		}
		return encrypt;
	}

	public static String aesDecode(String secret, String encrypt) {
		try {
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			// 2.根据secret规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secret.getBytes());
			keygen.init(128, secureRandom);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.将加密并编码后的内容解码成字节数组
			byte[] byte_content = new BASE64Decoder().decodeBuffer(encrypt);
			/*
			 * 解密
			 */
			byte[] byte_decode = cipher.doFinal(byte_content);
			decrypt = new String(byte_decode, "utf-8");
			System.err.println("aes decode:" + decrypt);
		} catch (Exception e) {
			System.err.println("aes decode error:" + e.getMessage());
		}

		return decrypt;
	}

	// http://stackoverflow.com/questions/29151211/how-to-decrypt-an-encrypted-aes-256-string-from-cryptojs-using-java
	// openssl enc -aes-128-ecb -in plain.txt -a -out encrypt.txt -pass
	// pass:123456
	// enc -aes-128-cbc -in encrypt.txt -d -a -out plain1.txt -pass pass:123456
	public static void main(String[] args) throws NoSuchAlgorithmException {
		md5(message);
		sha(message);
		encrypt = base64Encode(message);
		decrypt = base64Decode(encrypt);
		base64Encode("Salted__");
		keygen = KeyGenerator.getInstance("AES");
		encrypt = aesEncode(secret, message);
		decrypt = aesDecode(secret, encrypt);
		encrypt = aesEncode(secret, message);
		decrypt = aesDecode(secret, encrypt);
		message = "yechenfan";
		encrypt = aesEncode(secret, message);
		decrypt = aesDecode(secret, encrypt);

	}

}
