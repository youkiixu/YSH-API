package www.kiy.cn.youki;
 
import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
 
//import org.junit.Test; 
/**
 * 
 * 兼容.net
 * 非对称加解密
 * @author Administrator
 *
 */
public class RSA {
	private final int KEYSIZE = 512;
	private KeyPair keyPair;  
    private RSAPublicKey  publicKey;  
    private RSAPrivateKey  privateKey;  
    
//    @Test
//	public void test() {
//
//		String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKRZyzdQQ/Z8OxILFsJcDIvlkwG4ftRRwasW1ODRjcYz8AC987kyZF0Vqh/CxdbfnYVg2kwwA/8Styoxu3ZtZ+dyuw4PQl/liP/tSWONAD9lvdQ+yU87Tciwm/TLUHIAERbdVgCJr6PTaHAG17FrwhCZ3vtU+yJrT5QYU7ku7utzAgEDAoGAG2RMiTgLU79fLayDyw9XbKZDKvQVI2L1nIPOJXhCS7NSqspTSYhmD4OcWqB2TnqaQOV5t11V/9hz3F2fPmeRUPlqWFKhHFlyGsiMMqWcWgbLwsU+royhTiPJ7jHtwVMALKE/Zyl5bmauX4fuGaRGyz57mpcrr/1PujlAtvcxYNMCQQDNWwVUM+mgKjTLMVgi9bwAUyO5opXMd9RtscclSAVAVSVBKDuVoEwDahIrwUXNNqvLo2mdwPF/V+4epmHEBfmZAkEAzOH2y0fMLLIqbs7biGBnPKQf5a6iIw4fbC6YenXkzbrl1LFZ+zbBZ+ggq4HRxOKhV1XyLDQQ/BlIoikNn8Cs6wJBAIjnWOLNRmrGzdzLkBdOfVWMwnvBuTL6jZ52hMOFWNWOGNYa0mPAMqzxYXKA2TN5x90Xm76AoP+P9BRu69gD+7sCQQCIlqSHhTLIdsb0ieewQETTGBVDycFstBTyybr8TpiJJ0PjIOanedZFRWsdATaDQcDk4/bIIrX9ZjBsG15qgHNHAkAjk8Y1kCNRjixxKwPJzh8xaWMsAQQvmGzEKGrw7m8xLkxDLMlWs1dkG3NIhFW1cdJHaZMURkbwdtkVPsY6hnh8";
//		String publicKey = "MIGdMA0GCSqGSIb3DQEBAQUAA4GLADCBhwKBgQCkWcs3UEP2fDsSCxbCXAyL5ZMBuH7UUcGrFtTg0Y3GM/AAvfO5MmRdFaofwsXW352FYNpMMAP/ErcqMbt2bWfncrsOD0Jf5Yj/7UljjQA/Zb3UPslPO03IsJv0y1ByABEW3VYAia+j02hwBtexa8IQmd77VPsia0+UGFO5Lu7rcwIBAw==";
//		RSA rsa = new RSA();
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
//		map.put("out_trade_no7", "a201809291713391661009564198"); 
//		String str =SetLog.GetJSONString(map);
//		JMap mm=this.encode(str, privateKey);
//		String encode= mm.get("data").toString();
//		
//	//	HttpRequestHelp.doPost("http://localhost:9898/", mm); 
//		
//		System.out.println(encode.length());
//		System.out.println(mm);
//		
//		String decode= this.decode(encode, publicKey, mm);
//		
//		 System.out.println(decode); 
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
			pairgen.initialize(KEYSIZE, random);
			this.keyPair = pairgen.generateKeyPair(); // 生成密钥对
			this.privateKey = (RSAPrivateKey) this.keyPair.getPrivate();// 甲方私钥
			this.publicKey = (RSAPublicKey) this.keyPair.getPublic(); // 甲方公钥

		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return this.keyPair;
	}
   
	private PrivateKey CreatePrivateKey(String privateKey) {
		//System.out.println(privateKey);
		byte[] s = Convert.ToBase64Byte(privateKey);

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
		ByteArrayOutputStream out =null;
		byte [] result=null;
		try {
			byte[] b = data.getBytes("utf-8");
			//超出128则报异常
			int max = 116;//最大字节
			int len = b.length;

			PrivateKey privatekey = this.CreatePrivateKey(strPrivateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privatekey); 
		 
			if(len<=max){ 
				result=cipher.doFinal(b);
			}else{
				out = new ByteArrayOutputStream();
				int index = 0;
				while (len - index > 0) {
					byte []bb;
					if(len-index>max)
						bb=cipher.doFinal(b, index, max);
					else {
 
						bb=cipher.doFinal(b, index, len-index);
					}
					
					out.write(bb,0,bb.length);
					
					index +=max; 
				}
				
				out.flush(); 
				result=out.toByteArray(); 
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out =null;
			}
		}

		return   Convert.ToBase64String(result);
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
		String ss=verifyData(  sign,  strPublicKey);
		
		if (ss.equals(data))
			return true;
		return false;
	}
	public String verifyData( String sign, String strPublicKey) {
 
//		try {
//			PublicKey pubKey = this.CreatePublicKey(strPublicKey);
//			// 实例化密钥工厂
//			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//			// 数据解密
//			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
//			cipher.init(Cipher.DECRYPT_MODE, pubKey);
//			int max =116;
//			
//					
//			byte[] bb = cipher.doFinal(Base64.decodeBase64(sign));
//			String ss = new String(bb, "utf-8"); 
//			System.out.println(ss);
//			if (ss.equals(data))
//				return true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		ByteArrayOutputStream out =null;
		try {
			PublicKey pubKey = this.CreatePublicKey(strPublicKey);
			// 实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			// 数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			
			byte [] b = Convert.ToBase64Byte(sign);
			int len = b.length;
			int max =256;  
			
			String ss=null;
			if(len<=max){
				byte[] bb = cipher.doFinal(b);
				ss = new String(bb, "utf-8");	
			}else{ 
				out = new ByteArrayOutputStream();
				int index = 0;
				while (len - index > 0) {
					byte []bb;
					if(len-index>max)
						bb=cipher.doFinal(b, index, max);
					else {
 
						bb=cipher.doFinal(b, index, len-index);
					}
						
					out.write(bb,0,bb.length); 
					index +=max; 
				}
			}
			ss= new String (out.toByteArray(),"utf-8");
			System.out.println(ss);
			return ss;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) { 
					e.printStackTrace();
				}
				out=null;
			}
		}
		
		return null;

	}
	
	
	
	
	
	/**
	 * add by xyj 2016-08-16
	 * 解密运算
	 * @param str
	 * @return
	 */
	public  JMap encode(String str,String strPrivateKey){
		return SetLog.ObjectToMap(  DesCode(str,Cipher.ENCRYPT_MODE,strPrivateKey,null));
	}
	/**
	 * add by xyj 2016-08-16
	 * 加密运算
	 * @param str
	 * @return
	 */
	public  String decode(String str,String strPublicKey,JMap k){
		return Convert.ToString(  DesCode(str,Cipher.DECRYPT_MODE,strPublicKey,k));
	}
	private  Object DesCode(String str,int MODE,String strKey,JMap k){ 
		byte [] bkey = null;
		byte [] biv= null;
		
		try {
		//	JMap ik = null;
			//JMap iv = null;
			
			if (MODE ==Cipher.ENCRYPT_MODE) { 
				//随机生成key
				bkey = new byte[8];
				biv = new byte[8];
			//	ik = new JMap();
			//	iv = new JMap();
				
				for(int i=0;i<8;i++){
					//根据glen 随机生成key 与iv
					bkey[i]= (byte) ((int) 100*Math.random());
					biv[i]= (byte) ((int) 100*Math.random());
				//	ik.put(Convert.ToString(i), bkey[i]);
				//	iv.put( Convert.ToString(i), biv[i]); 
				}
				
			} else {
				String sik=  new String( Convert.ToBase64Byte(Convert.ToString(k.get("ik"))),"utf-8") ;
				String siv= new String ( Convert.ToBase64Byte(Convert.ToString(k.get("iv"))),"utf-8");
				
				bkey= this.verifyData(sik,strKey).getBytes();
				biv=this.verifyData(siv,strKey).getBytes();
				// ik = SetLog.ObjectToMap(this.signData(Convert.ToString(k.get("ik")), strKey) );
				 //iv = SetLog.ObjectToMap(this.signData(Convert.ToString(k.get("iv")), strKey));
				
//				bkey = new byte[ik.size()];
//				biv = new byte[iv.size()];
//				for (int i = 0; i < bkey.length; i++) {
//					bkey[i] = Integer.valueOf(ik.get(Convert.ToString(i)).toString()).byteValue();
//					biv[i] = Integer.valueOf(iv.get(Convert.ToString(i)).toString()).byteValue();
//				}
				 
			}
			DESKeySpec  kspec = new DESKeySpec(bkey);// 设置密钥参数
			IvParameterSpec iv1 = new IvParameterSpec(biv);// 设置向量
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
			SecretKey key = keyFactory.generateSecret(kspec);// 得到密钥对象
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher .init(MODE, key, iv1);
			JMap result = new JMap();
		    if(MODE ==Cipher.ENCRYPT_MODE){ 
		    	byte[] pasByte = cipher.doFinal(str.getBytes("utf-8"));
		    	
		    	if(!Convert.isNullOrEmpty( strKey )){
		    	  String  strK= this.signData(new String( bkey,"utf-8"), strKey);
		    	  String  strV= this.signData(new String( biv,"utf-8"), strKey);
		    	  result.put("ik",Convert.ToBase64String(strK.getBytes("utf-8")) );
		    	  result.put("iv",Convert.ToBase64String(strV.getBytes("utf-8")) ); 
		    	}
		    	result.put("data",Convert.ToBase64String(pasByte));
		    	return result;
		    	//this.signData( , strPrivateKey);
		    	
		    	
//		        byte[] pasByte = cipher.doFinal(str.getBytes("utf-8")); 
//		        String base64=Base64.encodeBase64String(pasByte);
//		        
//		        StringBuilder sb= new StringBuilder();
//		        sb.append(base64).append("[====]");
//		        String ss= "";
//		        for(byte b :bkey)
//		        	ss +=b+",";
//		         
//		        sb.append(ss.substring(0,ss.length()-1)).append("____");
//		          
//		        ss="";
//		        for(byte b :biv) 
//		        	ss +=b+",";  
//		        sb.append(ss.substring(0,ss.length()-1));
//		        return Base64.encodeBase64String(sb.toString().getBytes("utf-8")); //Base64.encode(sb.toString().getBytes("utf-8"));
		          
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
    
	

	private PublicKey CreatePublicKey(String publicKey) { 
		byte[] s =Convert.ToBase64Byte(publicKey); 
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
		return Convert.ToBase64String(publicKey.getEncoded()) ;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = (RSAPublicKey) this.CreatePublicKey(publicKey);
	}

	public String getPrivateKey() {
		return Convert.ToBase64String(privateKey.getEncoded());
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = (RSAPrivateKey) this.CreatePrivateKey(privateKey);

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
			// 鑾峰彇MD5鎽樿绠楁硶鐨凪essageDigest瀵硅薄
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// 浣跨敤鎸囧畾瀛楄妭鏇存柊鎽樿
			md5.update(b);
			// 鑾峰彇瀵嗘枃
			byte[] md = md5.digest();
			int j = md.length;
			// 鎶婂瘑鏂囪浆鎹㈢О鍗佸叚杩涘埗鐨勫瓧绗︿覆褰㈠紡
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
