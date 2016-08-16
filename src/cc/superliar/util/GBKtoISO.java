package cc.superliar.util;

import java.io.UnsupportedEncodingException;

public class GBKtoISO {

	public static String GtoI(String gbk) throws UnsupportedEncodingException{
		 byte[] bgbk = gbk.getBytes("GBK");
		
		 String str2 = new String(bgbk,"ISO-8859-1");
		
		 return str2;
		 
	}
	

	public static String ItoG(String iso) throws UnsupportedEncodingException{
		byte[] bios = iso.getBytes("ISO-8859-1");
		String b = new String(bios,"GBK");
		return b;
	}
	
	
	 public static void main(String[] args) throws UnsupportedEncodingException{
		String a = "ab我大大大叔的阿萨德按时的爱中国";
		String str2 =GtoI(a);
		System.out.println(str2);
		
		String b =ItoG(str2);
		System.out.println(b);
		
	}
	
}
