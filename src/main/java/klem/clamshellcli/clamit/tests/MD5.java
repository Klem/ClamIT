package klem.clamshellcli.clamit.tests;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

public class MD5 {
	public static void main(String[] args)throws Exception
    {
    	String password = "123456";
    	byte[] passBytes = password.getBytes("UTF-8");
    	
        MessageDigest md = DigestUtils.getMd5Digest();
        
        byte byteData[] = md.digest(passBytes);
 
        String hash = Hex.encodeHexString(byteData);
 
        System.out.println("Hash:: " + hash);
        System.out.println("Algorithm:: " + md.getAlgorithm());
        System.out.println("Length:: " + md.getDigestLength());
    }
}
