package www.kiy.cn.youki;
 
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec; 
import javax.crypto.Cipher;  
import org.apache.tomcat.util.codec.binary.Base64;
/**
 * 
 * 兼容.net
 * 非对称加解密
 * @author Administrator
 *
 */
public class RSA {
	private static final int KEYSIZE = 512;
	private KeyPair keyPair;  
    private RSAPublicKey  publicKey;  
    private RSAPrivateKey  privateKey;  
    
    
//	private void test() {
//
//		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKRZyzdQQ/Z8OxILFsJcDIvlkwG4ftRRwasW1ODRjcYz8AC987kyZF0Vqh/CxdbfnYVg2kwwA/8Styoxu3ZtZ+dyuw4PQl/liP/tSWONAD9lvdQ+yU87Tciwm/TLUHIAERbdVgCJr6PTaHAG17FrwhCZ3vtU+yJrT5QYU7ku7utzAgEDAoGAG2RMiTgLU79fLayDyw9XbKZDKvQVI2L1nIPOJXhCS7NSqspTSYhmD4OcWqB2TnqaQOV5t11V/9hz3F2fPmeRUPlqWFKhHFlyGsiMMqWcWgbLwsU+royhTiPJ7jHtwVMALKE/Zyl5bmauX4fuGaRGyz57mpcrr/1PujlAtvcxYNMCQQDNWwVUM+mgKjTLMVgi9bwAUyO5opXMd9RtscclSAVAVSVBKDuVoEwDahIrwUXNNqvLo2mdwPF/V+4epmHEBfmZAkEAzOH2y0fMLLIqbs7biGBnPKQf5a6iIw4fbC6YenXkzbrl1LFZ+zbBZ+ggq4HRxOKhV1XyLDQQ/BlIoikNn8Cs6wJBAIjnWOLNRmrGzdzLkBdOfVWMwnvBuTL6jZ52hMOFWNWOGNYa0mPAMqzxYXKA2TN5x90Xm76AoP+P9BRu69gD+7sCQQCIlqSHhTLIdsb0ieewQETTGBVDycFstBTyybr8TpiJJ0PjIOanedZFRWsdATaDQcDk4/bIIrX9ZjBsG15qgHNHAkAjk8Y1kCNRjixxKwPJzh8xaWMsAQQvmGzEKGrw7m8xLkxDLMlWs1dkG3NIhFW1cdJHaZMURkbwdtkVPsY6hnh8";
//		String publicKey = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCkWcs3UEP2fDsSCxbCXAyL5ZMBuH7UUcGrFtTg0Y3GM/AAvfO5MmRdFaofwsXW352FYNpMMAP/ErcqMbt2bWfncrsOD0Jf5Yj/7UljjQA/Zb3UPslPO03IsJv0y1ByABEW3VYAia+j02hwBtexa8IQmd77VPsia0+UGFO5Lu7rcwIBAw==";
//		RSA rsa = new RSA();
//		// rsa.CreateKey();
//		//
//		//  privateKey = rsa.getPrivateKey();
//		//  publicKey = rsa.getPublicKey();
//		rsa.setPublicKey(publicKey);
//		rsa.setPrivateKey(privateKey);
//
//		String sign = rsa.signData("123", privateKey);
//		System.out.println(sign);
//		rsa.verifyData("123", sign, publicKey);
//
//	}
	
	/**
	 * 生成秘钥对
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public KeyPair CreateKey() {
		try {

			KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = new SecureRandom();
			pairgen.initialize(RSA.KEYSIZE, random);
			this.keyPair = pairgen.generateKeyPair(); // 生成密钥对
			this.privateKey = (RSAPrivateKey) this.keyPair.getPrivate();// 甲方私钥
			this.publicKey = (RSAPublicKey) this.keyPair.getPublic(); // 甲方公钥

		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return this.keyPair;
	}
   
	private PrivateKey CreatePrivateKey(String privateKey) {
		System.out.println(privateKey);
		byte[] s = Base64.decodeBase64(privateKey);

		try {
			// 取得私钥
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(s);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// 生成私钥
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
			// this.privateKey =(RSAPrivateKey) priKey ;
			return priKey;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String signData(String data, String strPrivateKey) { 
		try {
			byte[] b = data.getBytes("utf-8");
			PrivateKey privatekey = this.CreatePrivateKey(strPrivateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privatekey);
			return Base64.encodeBase64String(cipher.doFinal(b));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 公钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密数据
	 */
	public boolean verifyData(String data, String sign, String strPublicKey) {
		try {
			PublicKey pubKey = this.CreatePublicKey(strPublicKey);
			// 实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			// 数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			byte[] bb = cipher.doFinal(Base64.decodeBase64(sign));
			String ss = new String(bb, "utf-8"); 
			if (ss.equals(data))
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
    
	

	private PublicKey CreatePublicKey(String publicKey) { 
		byte[] s = Base64.decodeBase64(publicKey);
		try {
			// 实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// 初始化公钥
			// 密钥材料转换
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(s);
			// 产生公钥
			PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
			return pubKey;

		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getPublicKey() {
		return Base64.encodeBase64String(publicKey.getEncoded());
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = (RSAPublicKey) this.CreatePublicKey(publicKey);
	}

	public String getPrivateKey() {
		return Base64.encodeBase64String(privateKey.getEncoded());
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = (RSAPrivateKey) this.CreatePrivateKey(privateKey);

	}
}
