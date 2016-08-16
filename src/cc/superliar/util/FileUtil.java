package cc.superliar.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.*;
import org.apache.commons.io.IOUtils;  
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
    
    
    //���ļ���Сת������Ӧ�ַ�����ʽ
    public  static String sizeFormat(long length) {
        long kb;
        if (length < 1024)
        {
            return String.valueOf(length);
        }
        else if ((kb = length / 1024) < 1024)
        {
            return (String.valueOf(kb) + "kb");
        }
        else
        {
            return (String.valueOf(length / 1024 / 1024) + "kb");
        }
    }
    
    
    //�ӱ����ϴ������ļ���
    //localName�������ļ�·�������ļ�����
    //ftpFile����Ҫ����ftp���ļ��У�newName��������  
    public static void ftpUpload(String localName, String ftpFile, String newName) {  
    	System.out.println("����ftp�ϴ��ļ�");
        File srcFile = new File(localName);  
        System.out.println("srcFile="+srcFile);
        FileInputStream fis = null;  
        try {  
            fis = new FileInputStream(srcFile);  
            //�ı乤��Ŀ¼������Ҫ��·����    
            ftpClient.changeWorkingDirectory(ftpFile);    
            System.out.println("ftpFile="+ftpFile);
            ftpClient.setBufferSize(1024);  
            ftpClient.setControlEncoding("GBK");  
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
            ftpClient.storeFile(newName, fis);  
        } catch (Exception e) {  
            System.out.println("Failed to upload!");  
            e.printStackTrace();  
        } finally {  
            try {  
                fis.close();  
               
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }


    
    // �ӱ����ϴ�һ���ļ��У�
    // localPath�������ļ��У�
    // ftpPath���ϴ�����Դ��Ҫ��ŵ��ļ���  
    public static void ftpUpload2(String localPath, String ftpPath) throws IOException {  
        File uploadFile = new File(localPath);  
        System.out.println("uploadFile="+uploadFile);
        System.out.println("uploadFile.getName()="+uploadFile.getName());
        File[] fileList = uploadFile.listFiles();  
       
        FileInputStream fis = null;  
     
        ftpClient.makeDirectory(uploadFile.getName());//�ϴ����ļ���
        
        for (int i = 0; i < fileList.length; i++) {  
        	String path_local = localPath; //����·����ÿһ��ѭ���ж��ȳ�ʼ��
            try {  
            	if(fileList[i].isDirectory() == true){
            		path_local=path_local+"\\"+fileList[i].getName();
            		
            		ftpClient.changeWorkingDirectory(ftpPath+"/"+uploadFile.getName());  
       
            		ftpUpload2(path_local,ftpPath+"/"+uploadFile.getName());
            	} 
                fis = new FileInputStream(fileList[i]);  
                String ftpFileName = fileList[i].getName();  
                ftpClient.changeWorkingDirectory(ftpPath+"/"+uploadFile.getName());  
                ftpClient.setBufferSize(1024);  
                ftpClient.setControlEncoding("GBK");  
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
                ftpClient.storeFile(ftpFileName, fis); 
            	System.out.println("�ϴ�һ���ļ�");
  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        try {  
            fis.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
  
    }  
    
    
  
    public static   void downLoadFile(String remote, String local)  
    {  
    	
    	System.out.println("remote==="+remote);
     	System.out.println("local===="+local);
    	
        try  
        {  
   ftpClient.enterLocalPassiveMode();
            // ת��ָ������Ŀ¼  
        	ftpClient.changeWorkingDirectory(new String(remote.getBytes("GBK"),"ISO-8859-1"));  
        	
            FTPFile[] files = ftpClient.listFiles();  
   
            for (FTPFile file : files)  
            {  
                if (file.isDirectory())  
                {  
                    downLoadFile( remote + "\\" + file.getName() + "\\", local + "\\" + file.getName() );  
                }  
                else  
                {      
                    File localFile = new File(local + "\\" + GBKtoISO.GtoI(file.getName()));  
                	System.out.println(localFile);
                    if (!localFile.getParentFile().exists())  
                    {  
                        localFile.getParentFile().mkdirs();  

                    }  
                    // ���
                    OutputStream is = new FileOutputStream(localFile);  
                    // �����ļ�  
                    ftpClient.setBufferSize(1024);  
                    ftpClient.setAutodetectUTF8(true);
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
                    ftpClient.enterLocalPassiveMode();
                    boolean i  =  ftpClient.retrieveFile(GBKtoISO.GtoI(file.getName()), is);  
                  	System.out.println(i);
                  
                 
   
                }  
            }  
   
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    
    
    }
    
    public static void retrieve(String remote, String local)  
    {  
     
   
        try  
        {  
            String serverpath = remote;  
   
            String localpath = local;  
          
   
      
   
            downLoadFile(serverpath, localpath);  
            // ftp  
            ftpClient.logout();  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
      
    }  
    
    
    
    
    
  
    public static boolean createDir(String destDirName) {  
        File dir = new File(destDirName);  
        if (dir.exists()) {  
            System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�Ŀ��Ŀ¼�Ѿ�����");  
            return false;  
        }  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        //����Ŀ¼  
        if (dir.mkdirs()) {  
            System.out.println("����Ŀ¼" + destDirName + "�ɹ���");  
            return true;  
        } else {  
            System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�");  
            return false;  
        }  
    }  
    
    
    
    
    

    
    public static void ftpDisConnect() {  
        try {  
            if (ftpClient.isConnected()) {  
                ftpClient.disconnect();  
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }


	
    
    /** 
     *  
     *������������ftp ���أ� ָ������ftp��ĳһ��Ŀ¼�����ļ������ء� 
     *��������ϸ������������ϸ������ 
     * @see   ���ࡢ��#��������#��Ա�� 
     * @param downloadPath ���ص�ַ 
     * @param dir ftp���ļ���ַ����Ե��磺/admin/rt/�� 
     * @param fileName ftp��Ҫ���ص��ļ��� 
     * @return true, ֻ���������̶��ɹ��ŷ���true�� ��Ϊtrue�ͱ�ʾʧ�� 
     */  
       
    @SuppressWarnings("finally")  
    public static boolean fileDownload(String downloadPath,String dir) {  
        
  
        try {  
        
            ftpClient.setBufferSize(1024);  
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
              
            iterateDown(dir,downloadPath+File.separator);  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new RuntimeException("FTP����ʧ��  ", e);  
        } finally {  
            
            return true;  
        }  
    }   
      
      
    /** 
     *  
     *���������������ع��ܵĵ��������� 
     *��������ϸ������������ϸ������ 
     * @see   ���ࡢ��#��������#��Ա�� 
     * @param ftpClient ftpClient ������ 
     * @param dir ftp�ϵ�·�� 
     * @param downloadPath ����·�� 
     * @throws IOException 
     */  
    public static void iterateDown(String dir,String downloadPath) throws IOException{  
        // �г������ַ��Ӧ�������ļ��л����ļ�  
        FTPFile[] files = ftpClient.listFiles(dir);  
        for(FTPFile f:files){  
            //�����ǰĿ¼��û�д�������ô�������ﴴ��  
            File filedown = new File(downloadPath);  
            if(!filedown.exists()){  
                filedown.mkdirs();  
            }  
            String localPath = downloadPath+File.separator+f.getName();  
            File file = new File(localPath);  
            if(f.isFile()){  
                FileOutputStream fos = null;  
                fos = new FileOutputStream(localPath);  
                String path = dir+File.separator+f.getName();  
                ftpClient.retrieveFile(path, fos);  
                IOUtils.closeQuietly(fos);  
            }else if(f.isDirectory()){  
                file.mkdirs();  
                iterateDown(dir+File.separator+f.getName(),localPath);  
            }  
        }  
    }  
      
    
    
    
    
    
    
    
    
}
