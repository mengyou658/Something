package com.moons.xst.track.common;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * 加密解密工具包
 * @author sx
 * @date 2014-9-26
 */
public class CyptoUtils { 
	
    /**
     * MD5算法，加密
     *
     * @param s 待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException 
     * @throws Exception 
     */
	 public final static String MD5(String s) {  
	        char hexDigits[] = { '0', '1', '2', '3', '4',  
	                             '5', '6', '7', '8', '9',  
	                             'A', 'B', 'C', 'D', 'E', 'F' };  
	        try {  
	            byte[] btInput = s.getBytes();  
	     //获得MD5摘要算法的 MessageDigest 对象  
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
	     //使用指定的字节更新摘要  
	            mdInst.update(btInput);  
	     //获得密文  
	            byte[] md = mdInst.digest();  
	     //把密文转换成十六进制的字符串形式  
	            int j = md.length;  
	            char str[] = new char[j * 2];  
	            int k = 0;  
	            for (int i = 0; i < j; i++) {  
	                byte byte0 = md[i];  
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
	                str[k++] = hexDigits[byte0 & 0xf];  
	            }  
	            String mdstr = null ;
	            for(int i = 0;i<str.length;i++)
	            {
	            	if(i>0&&i<str.length && i%2==0)
	            		mdstr +="-";
	            	if(i==0)
	            		mdstr = String.valueOf(str[i]);
	            	else {
	            		mdstr += str[i];
					}
	            	
	            }
	            String string = mdstr;
	            return mdstr;  
	        }  
	        catch (Exception e) {  
	            e.printStackTrace();  
	            return null;  
	        }  
	    } 
	
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	
    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException 
     * @throws Exception 
     */
    public static String encode(String key,String data) {
    	if(data == null)
    		return null;
    	try{
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());	    	
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        //key的长度不能够小于8位字节
	        Key secretKey = keyFactory.generateSecret(dks);
	        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
	        IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
	        AlgorithmParameterSpec paramSpec = iv;
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);           
	        byte[] bytes = cipher.doFinal(data.getBytes());            
	        return byte2hex(bytes);
    	}catch(Exception e){
    		e.printStackTrace();
    		return data;
    	}
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static String decode(String key,String data) {
    	if(data == null)
    		return null;
        try {
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e){
    		e.printStackTrace();
    		return data;
        }
    }

	/**
	 * 二行制转字符串
	 * @param b
	 * @return
	 */
    private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b!=null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
    
    private static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length/2];
		for (int n = 0; n < b.length; n+=2) {
		    String item = new String(b,n,2);
		    b2[n/2] = (byte)Integer.parseInt(item,16);
		}
        return b2;
    }
    
}
