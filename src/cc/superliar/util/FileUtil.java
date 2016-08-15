package cc.superliar.util;

import org.apache.commons.net.ftp.*;

public class FileUtil {
	
	static private FTPClient ftpClient = new FTPClient();
	 
    public static boolean login(String address,int port,String username,String password){
        try {
            ftpClient.connect(address, port);
            ftpClient.setControlEncoding("GBK");
            int reply = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                if (ftpClient.login(username, password)) {
                    System.out.println("Successful login!");
                    return true;
                } else {
                    System.out.println("fail to login!");
                }
            }
        } catch (Exception e) {
            System.out.println("Failure connection!");
            e.printStackTrace();
        }
        return false;
    }
}
