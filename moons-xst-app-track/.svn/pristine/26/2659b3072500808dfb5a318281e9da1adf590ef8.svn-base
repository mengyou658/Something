package com.moons.xst.track.common;

public class HexUtils { 
	/**      
	 * 用于建立十六进制字符的输出的小写字符数组      
	 **/   
	private static final char[] 
			DIGITS_LOWER = {'0', '1', '2', '3', '4', '5',         
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};  
	
	/**      
	 * 用于建立十六进制字符的输出的大写字符数组      
	 */  
	private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5',     
		'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};     
	/**      
	 *  将字节数组转换为十六进制字符数组      *    
	 * @param data byte[]    
	 * @return 十六进制char[]     
	 */  
	public static char[] encodeHex(byte[] data) 
	{         
		return encodeHex(data, true);     
		
	} 
	
	private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',  
        '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',  
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',  
        'Z' };
	
	/**      
	 * 将字节数组转换为十六进制字符数组    
	 *  @param data        byte[]     
	 *  @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式     
	 *  @return 十六进制char[]      
	 */   
	public static char[] encodeHex(byte[] data, boolean toLowerCase)
	{         
		return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);    
		
	}       
	/** 将字节数组转换为十六进制字符数组      *     
	 *  @param data     byte[]      
	 *  @param toDigits 用于控制输出的char[]     
	 *  @return 十六进制char[]     
	 */   
	protected static char[] encodeHex(byte[] data, char[] toDigits)
	{         
		int l = data.length;       
		char[] out = new char[l << 1];        
		// two characters form the hex value.        
		for (int i = 0, j = 0; i < l; i++) {         
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];       
			out[j++] = toDigits[0x0F & data[i]];       
			}         return out;     
	}   
	
	/**  将字节数组转换为十六进制字符串      *    
	 *  @param data byte[]     
	 *  @return 十六进制String     
	 */    
	public static String encodeHexStr(byte[] data) 
	{         
		return encodeHexStr(data, true);    
		
	}    
	
	/**      
	 * 将字节数组转换为十六进制字符串      *      
	 * @param data        byte[]     
	 * @param toLowerCase <code>true</code> 传换成小写格式 ， <code>false</code> 传换成大写格式    
	 * @return 十六进制String      
	 */    
	public static String encodeHexStr(byte[] data, boolean toLowerCase)
	{         
		return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);    
		
	}    
	
	/** 将字节数组转换为十六进制字符串      *      
	 * @param data     byte[]     
	 *  @param toDigits 用于控制输出的char[]      
	 *  @return 十六进制String      
	 */    
	protected static String encodeHexStr(byte[] data, char[] toDigits) 
	{         
		return new String(encodeHex(data, toDigits));     
		
	}  
	
	/**      
	 * 将十六进制字符数组转换为字节数组      *     
	 * @param data 十六进制char[]      
	 * @return byte[]      
	 * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常      
	 */   
	public static byte[] decodeHex(char[] data) 
	{           
		int len = data.length;          
		if ((len & 0x01) != 0) 
		{           
			throw new RuntimeException("Odd number of characters.");      
 
		}          
		byte[] out = new byte[len >> 1];           
		// two characters form the hex value.         
		for (int i = 0, j = 0; j < len; i++) {         
			int f = toDigit(data[j], j) << 4;           
			j++;            
			f = f | toDigit(data[j], j);           
			j++;           
			out[i] = (byte) (f & 0xFF);       
			
		}           
		return out;     
			
	}  
	/**
	 * 十六进制字符串转换成bytes
	 */

	public static byte[] hexStr2Bytes(String data) 
	{           
		int m = 0, n = 0;
		int l = data.length() / 2;
		
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(data.substring(i * 2, m), data.substring(m, n));
		}
		return ret;

			
	} 
	private static byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}


	/** 将十六进制字符转换成一个整数      *     
	 *  @param ch    十六进制char      
	 *  @param index 十六进制字符在字符数组中的位置      
	 *  @return 一个整数     
	 *  @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常      
	 *   
	 */   
	protected static int toDigit(char ch, int index)
	{   
		int digit = Character.digit(ch, 16);        
		if (digit == -1) {             
		throw new RuntimeException("Illegal hexadecimal character " + ch            
				+ " at index " + index);        
		
		}        
		return digit;     
	}     
	public static void main(String[] args) 
	{         
		String srcStr = "qwer";        
		String encodeStr = encodeHexStr(srcStr.getBytes());     
		String decodeStr = new String(decodeHex(encodeStr.toCharArray()));     
		System.out.println("before encode:" + srcStr);       
		System.out.println("after encode:" + encodeStr);        
		System.out.println("convert:" + decodeStr);    
	}   	
	public static byte[] CRC16_Check(byte Pushdata[],int length)  
    {  
    	int Reg_CRC=0xffff; 
    	int temp;
    	int i,j;    
      
    	for( i = 0; i<length; i ++)  
    	{  
    		temp = Pushdata[i];
    		if(temp < 0) temp += 256; 
    		temp &= 0xff;
    		Reg_CRC^= temp;  
    		 
    	   for (j = 0; j<8; j++)  
    	   {  
    		   if ((Reg_CRC & 0x0001) == 0x0001)  
    			   Reg_CRC=(Reg_CRC>>1)^0xA001; 
    		   else  
    			   Reg_CRC >>=1; 
    	   }    
    	}  
    	byte[] crcByte = new byte[]{(byte) ((Reg_CRC&0xffff) % 256),(byte) ((Reg_CRC&0xffff) / 256)};
    	return crcByte;  
    }
	/**
	 * 2字节转换有符号数
	 * @param b1
	 * @param b2
	 * @return
	 */
	 public static int convertTwoBytesToInt1 (byte b1, byte b2)      // signed
	  {
	    return (b2 << 8) | (b1 & 0xFF);
	  }
      /**
       * 4字节转换有符号数
       * @param b1
       * @param b2
       * @param b3
       * @param b4
       * @return
       */
	  public static int convertFourBytesToInt1 (byte b1, byte b2, byte b3, byte b4)
	  {
	    return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
	  }
      /**
       * 2字节转无有符号数
       * @param b1
       * @param b2
       * @return
       */
	  public static int convertTwoBytesToInt2 (byte b1, byte b2)      // unsigned
	  {
	    return (b2 & 0xFF) << 8 | (b1 & 0xFF);
	  }
       /**
        * 4字节转无有符号数
        * @param b1
        * @param b2
        * @param b3
        * @param b4
        * @return
        */
	  public static long convertFourBytesToInt2 (byte b1, byte b2, byte b3, byte b4)
	  {
	    return (long) (b4 & 0xFF) << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
	  }
	  
	  /** 
	   * 将十进制的数字转换为指定进制的字符串 
	   *  
	   * @param n 十进制的数字 
	   * @param base 指定的进制 
	   * @return 
	   */  
	  public static String toOtherBaseString(long n, int base) {  
	      long num = 0;  
	      if (n < 0) {  
	          num = ((long) 2 * 0x7fffffff) + n + 2;  
	      } else {  
	          num = n;  
	      }  
	      char[] buf = new char[32];  
	      int charPos = 32;  
	      while ((num / base) > 0) {  
	          buf[--charPos] = digits[(int) (num % base)];  
	          num /= base;  
	      }  
	      buf[--charPos] = digits[(int) (num % base)];  
	      return new String(buf, charPos, (32 - charPos));  
	  }
	  
	  /** 
     * 将其它进制的数字（字符串形式）转换为十进制的数字 
     *  
     * @param str 其它进制的数字（字符串形式） 
     * @param base 指定的进制 
     * @return 
     */  
    public static long toDecimalism(String str, int base) {  
        char[] buf = new char[str.length()];  
        str.getChars(0, str.length(), buf, 0);  
        long num = 0;  
        for (int i = 0; i < buf.length; i++) {  
            for (int j = 0; j < digits.length; j++) {  
                if (digits[j] == buf[i]) {  
                    num += j * Math.pow(base, buf.length - i - 1);  
                    break;  
                }  
            }  
        }  
        return num;  
    }

    public static StringBuilder aBString(int i){
        StringBuilder stringBuilder=new StringBuilder();
        switch (i) {
        case 10:
            return stringBuilder.append("A");
        case 11:
            return stringBuilder.append("B");
        }
        return stringBuilder.append(i);
    }
}

