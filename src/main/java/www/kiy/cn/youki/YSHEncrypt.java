package www.kiy.cn.youki;
 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec; 
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec; 

import www.kiy.cn.HotKey.eRSAType; 
/**
 * java与.net之间数据进行交互时，只能通过使用  公钥进行加密，私钥进行解密
 * 
 * 而使用私钥进行加密可用于防篡改和防止假冒的 
 * 
 * 兼容.net
 * 非对称加解密
 * @author Administrator
 *
 */ 

public class YSHEncrypt {
	private static YSHEncrypt myencrypt =null;
	
	public static YSHEncrypt getEncryptInstance(){
		if(myencrypt==null){
			myencrypt = new YSHEncrypt();
		}
		return myencrypt;
	}
	
	private  final int KEYSIZE = 512;
	private KeyPair keyPair;  
	public  String signData(String data, String strPrivateKey) {

		return signData(data, strPrivateKey, eRSAType.privateKey);
	}

	public String signData(String data, String strPrivateKey, eRSAType t) {

		
//		if (Cipher.DECRYPT_MODE == mode) { 
//				return doFinalData(cipher, Convert.ToBase64Byte(data), 128);
//		}
		return Convert.ToBase64String(Data(Convert.ToBase64Byte(data), strPrivateKey, Cipher.ENCRYPT_MODE, t));
	}

	public boolean verifyData(String data, String sign, String strPublicKey) {
		String ss = verifyData(sign, strPublicKey);

		if (ss.equals(data))
			return true;
		return false;
	}

	public String verifyData(String sign, String strPublicKey) {
		return verifyData(sign, strPublicKey, eRSAType.publicKey);
	}

	public String verifyData(String sign, String strKey, eRSAType t) {
		try {
			return new String(Data(sign.getBytes("utf-8"), strKey, Cipher.DECRYPT_MODE, t), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * add by xyj 2016-08-16
	 * 解密运算
	 * @param str
	 * @return
	 */
	public JMap encode(String str, String strPrivateKey) {
		return encode(str, strPrivateKey, eRSAType.privateKey);
	}

	public JMap encode(String str, String strKey, eRSAType t) {
		return SetLog.ObjectToMap(DesCode(str, Cipher.ENCRYPT_MODE, strKey, null, t));

	}
	/**
	 * add by xyj 2016-08-16
	 * 加密运算
	 * @param str
	 * @return
	 */
	public String decode(String str, String strPublicKey, JMap k) {
		return decode(str, strPublicKey, k, eRSAType.publicKey);
	}

	public String decode(JMap data, String strPublicKey) {
		return decode( data,  strPublicKey,  eRSAType.publicKey);
	}
	public String decode( JMap data, String strKey, eRSAType t) {
		return decode( data.getWithRemoveKey("d").toString(),  strKey,  data,  t);
	}
	public String decode(String str, String strKey, JMap k, eRSAType t) {
		return Convert.ToString(DesCode(str, Cipher.DECRYPT_MODE, strKey, k, t));
	}
    //private RSAPublicKey  publicKey;  
   // private RSAPrivateKey  privateKey;  
    
//    @Test
//	public void test() {
//
//		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKRZyzdQQ/Z8OxILFsJcDIvlkwG4ftRRwasW1ODRjcYz8AC987kyZF0Vqh/CxdbfnYVg2kwwA/8Styoxu3ZtZ+dyuw4PQl/liP/tSWONAD9lvdQ+yU87Tciwm/TLUHIAERbdVgCJr6PTaHAG17FrwhCZ3vtU+yJrT5QYU7ku7utzAgEDAoGAG2RMiTgLU79fLayDyw9XbKZDKvQVI2L1nIPOJXhCS7NSqspTSYhmD4OcWqB2TnqaQOV5t11V/9hz3F2fPmeRUPlqWFKhHFlyGsiMMqWcWgbLwsU+royhTiPJ7jHtwVMALKE/Zyl5bmauX4fuGaRGyz57mpcrr/1PujlAtvcxYNMCQQDNWwVUM+mgKjTLMVgi9bwAUyO5opXMd9RtscclSAVAVSVBKDuVoEwDahIrwUXNNqvLo2mdwPF/V+4epmHEBfmZAkEAzOH2y0fMLLIqbs7biGBnPKQf5a6iIw4fbC6YenXkzbrl1LFZ+zbBZ+ggq4HRxOKhV1XyLDQQ/BlIoikNn8Cs6wJBAIjnWOLNRmrGzdzLkBdOfVWMwnvBuTL6jZ52hMOFWNWOGNYa0mPAMqzxYXKA2TN5x90Xm76AoP+P9BRu69gD+7sCQQCIlqSHhTLIdsb0ieewQETTGBVDycFstBTyybr8TpiJJ0PjIOanedZFRWsdATaDQcDk4/bIIrX9ZjBsG15qgHNHAkAjk8Y1kCNRjixxKwPJzh8xaWMsAQQvmGzEKGrw7m8xLkxDLMlWs1dkG3NIhFW1cdJHaZMURkbwdtkVPsY6hnh8";
//		String publicKey = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCkWcs3UEP2fDsSCxbCXAyL5ZMBuH7UUcGrFtTg0Y3GM/AAvfO5MmRdFaofwsXW352FYNpMMAP/ErcqMbt2bWfncrsOD0Jf5Yj/7UljjQA/Zb3UPslPO03IsJv0y1ByABEW3VYAia+j02hwBtexa8IQmd77VPsia0+UGFO5Lu7rcwIBAw==";
//		 
//		// rsa.CreateKey();
//		//
//		//  privateKey = rsa.getPrivateKey();
//		//  publicKey = rsa.getPublicKey();
//		//rsa.setPublicKey(publicKey);
//		//rsa.setPrivateKey(privateKey);
//		//{"key":"111SaaSQRQuery","domain":"www.kiy.cn:443","strSysMac":"30-5A-3A-E4-4F-14","out_trade_no":"a201809291713391661009564198","sign":""}
//		JMap map = new JMap();
//		map.put("key", "SaaSQRQuery");
//		map.put("domain", "www.kiy.cn:443");
//		map.put("strSysMac", "30-5A-3A-E4-4F-14");
//		map.put("out_trade_no6", "a201809291713391661009564198"); 
//		map.put("out_trade_no7", "a201809291713391661009564198"); 
//		map.put("out_trade_no6", "a201809291713391661009564198"); 
//		map.put("out_trade_no1", "a201809291713391661009564198"); 
//		map.put("out_trade_no2", "a201809291713391661009564198"); 
//		map.put("out_trade_no3", "a201809291713391661009564198"); 
//		map.put("out_trade_no4", "a201809291713391661009564198"); 
//		map.put("out_trade_no5", "a201809291713391661009564198"); 
//		map.put("out_trade_no6", "a201809291713391661009564198"); 
//		map.put("out_trade_no7", "a201809291713391661009564198");
//		map.put("out_trade_no8", "a201809291713391661009564198");
//		
//		String str =SetLog.GetJSONString(map);
////		String sign=this.signData(str, privateKey);
////		
////		System.out.println(sign);
////		JMap mm = new JMap();
////		mm.put("privateKey" , sign);
////		mm.put("publicKey" , this.signData(str, publicKey,eRSAType.publicKey) );
////		JMap m =  HttpRequestHelp.doPost("http://localhost:9898/", mm); 
////		String sign1= Convert.ToString(  m.get("sign"));
////		System.out.println(this.verifyData(sign1, privateKey,eRSAType.PrivateKey));
//		
//		//String dd= this.verifyData(sign, privateKey,eRSAType.PrivateKey);
//		  
//		
////		
//		JMap mm=this.encode(str, publicKey,eRSAType.publicKey);
//		String d= this.decode(mm.get("d").toString(), privateKey, mm, eRSAType.privateKey);
//		System.out.println(d); 
//		
//		JMap m =  HttpRequestHelp.doPost("http://localhost:9898/", mm);  
//		System.out.println(  this.decode(m, privateKey, eRSAType.privateKey)); 
//	}
	
	 
	
	public byte[] Data(byte[] data, String strKey, int mode, eRSAType rsa) {

		try {
			KeyFactory keyFactory = getKeyFactory();
			Key k = null; 
			if (rsa == eRSAType.privateKey)
				k = keyFactory.generatePrivate(getPKCS8EncodedKeySpec(strKey));
			else
				k = keyFactory.generatePublic(getX509EncodedKeySpec(strKey));
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(mode, k);
			if (Cipher.DECRYPT_MODE == mode) { 
 				return doFinalData(cipher, data, 128);
			}
			return doFinalData(cipher, data, 116);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private byte[] doFinalData(Cipher cipher, byte[] b, int max) {

		ByteArrayOutputStream out = null;
		byte[] result = null;
		try {
			// byte[] b = Convert.ToBase64Byte(data);
			int len = b.length;
			if (len <= max) {
				result = cipher.doFinal(b);
			} else {
				out = new ByteArrayOutputStream();
				int index = 0;
				while (len - index > 0) {
					byte[] bb;
					if (len - index > max)
						bb = cipher.doFinal(b, index, max);
					else {

						bb = cipher.doFinal(b, index, len - index);
					}

					out.write(bb, 0, bb.length);

					index += max;
				}

				out.flush();
				result = out.toByteArray();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out = null;
			}
		}
		return result;
	}
	 
	
	
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
			pairgen.initialize(KEYSIZE, random);
			this.keyPair = pairgen.generateKeyPair(); // 生成密钥对
			// this.privateKey = (RSAPrivateKey) this.keyPair.getPrivate();// 甲方私钥 
			// this.publicKey = (RSAPublicKey) this.keyPair.getPublic(); // 甲方公钥

		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return this.keyPair;
	}

	private PKCS8EncodedKeySpec getPKCS8EncodedKeySpec(String strKey) {
		byte[] s = Convert.ToBase64Byte(strKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(s);
		return pkcs8KeySpec;
	}

	private X509EncodedKeySpec getX509EncodedKeySpec(String strKey) {
		byte[] s = Convert.ToBase64Byte(strKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(s);
		return x509KeySpec;
	}
	
	

	private KeyFactory getKeyFactory() throws NoSuchAlgorithmException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory;
	}

	

	private Object DesCode(String str, int MODE, String strKey, JMap k, eRSAType t) {
		byte[] bkey = null;
		byte[] biv = null;

		try {
			if (MODE == Cipher.ENCRYPT_MODE) {
				// 随机生成key
				bkey = new byte[8];
				biv = new byte[8];

				for (int i = 0; i < 8; i++) {
					// 根据glen 随机生成key 与iv
					bkey[i] = (byte) ((int) 100 * Math.random());
					biv[i] = (byte) ((int) 100 * Math.random());
				}

			} else {
				byte[] sik = Convert.ToBase64Byte(Convert.ToString(k.get("k")));
				byte[] siv = Convert.ToBase64Byte(Convert.ToString(k.get("v")));
				// bkey = this.verifyData(sik, strKey, t);
				// biv = this.verifyData(siv, strKey, t);
				// Data(Convert.ToString( k.get("k")),;
				
				///Convert.ToBase64String(bkey).getBytes("utf-8") 
				bkey = Convert.ToBase64Byte(   new String (Data(sik, strKey, MODE, t),"utf-8"));
				biv =  Convert.ToBase64Byte(new String ( Data(siv, strKey, MODE, t),"utf-8"));
			}
			DESKeySpec kspec = new DESKeySpec(bkey);// 设置密钥参数
			IvParameterSpec iv1 = new IvParameterSpec(biv);// 设置向量
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
			SecretKey key = keyFactory.generateSecret(kspec);// 得到密钥对象
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(MODE, key, iv1);
			JMap result = new JMap();
			if (MODE == Cipher.ENCRYPT_MODE) {
				byte[] pasByte = cipher.doFinal(str.getBytes("utf-8"));

				if (!Convert.isNullOrEmpty(strKey)) { 
					//由于.net中不是所有的byte[]转string 后还能再转回同样的byte[],故加多64加密
					result.put("k",Convert.ToBase64String( Data(Convert.ToBase64String(bkey).getBytes("utf-8")  , strKey, MODE, t)));
					result.put("v",Convert.ToBase64String(Data( Convert.ToBase64String(biv).getBytes("utf-8"),strKey , MODE, t)));
					//result.put("k",Convert.ToBase64String( Data(   bkey, strKey, MODE, t)));
					//result.put("v",Convert.ToBase64String(Data(biv, strKey, MODE, t))); 
				}
				result.put("d", Convert.ToBase64String(pasByte));
				return result;
			} else {
				byte[] bb = Convert.ToBase64Byte(str);
				byte[] pasByte = cipher.doFinal(bb);
				return new String(pasByte, "UTF-8");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
     
	/**
	 * MD5 加密
	 * @param str
	 * @return
	 */
	public String EncoderByMd5(String str) {
		try {
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			byte[] b = str.getBytes("utf-8");// ("utf-8"); 
			MessageDigest md5 = MessageDigest.getInstance("MD5"); 
			md5.update(b); 
			byte[] md = md5.digest();
			int j = md.length; 
			char[] c = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				c[k++] = hexDigits[byte0 >>> 4 & 0xf];
				c[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(c);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
}
