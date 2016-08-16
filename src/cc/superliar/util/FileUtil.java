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
    
    
    //将文件大小转换成相应字符串格式
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
    
    
    //从本地上传单个文件，
    //localName：本地文件路径包含文件名，
    //ftpFile：所要放于ftp的文件夹，newName：重命名  
    public static void ftpUpload(String localName, String ftpFile, String newName) {  
    	System.out.println("进入ftp上传文件");
        File srcFile = new File(localName);  
        System.out.println("srcFile="+srcFile);
        FileInputStream fis = null;  
        try {  
            fis = new FileInputStream(srcFile);  
            //改变工作目录到所需要的路径下    
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


    
    // 从本地上传一个文件夹，
    // localPath：本地文件夹，
    // ftpPath：上传的资源所要存放的文件夹  
    public static void ftpUpload2(String localPath, String ftpPath) throws IOException {  
        File uploadFile = new File(localPath);  
        System.out.println("uploadFile="+uploadFile);
        System.out.println("uploadFile.getName()="+uploadFile.getName());
        File[] fileList = uploadFile.listFiles();  
       
        FileInputStream fis = null;  
     
        ftpClient.makeDirectory(uploadFile.getName());//上传的文件名
        
        for (int i = 0; i < fileList.length; i++) {  
        	String path_local = localPath; //本地路径在每一次循环中都先初始化
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
            	System.out.println("上传一个文件");
  
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
            // 转到指定下载目录  
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
                    // 输出
                    OutputStream is = new FileOutputStream(localFile);  
                    // 下载文件  
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
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");  
            return false;  
        }  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        //创建目录  
        if (dir.mkdirs()) {  
            System.out.println("创建目录" + destDirName + "成功！");  
            return true;  
        } else {  
            System.out.println("创建目录" + destDirName + "失败！");  
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
     *【功能描述：ftp 下载， 指定下载ftp上某一个目录或者文件到本地】 
     *【功能详细描述：功能详细描述】 
     * @see   【类、类#方法、类#成员】 
     * @param downloadPath 本地地址 
     * @param dir ftp上文件地址（相对的如：/admin/rt/） 
     * @param fileName ftp上要下载的文件名 
     * @return true, 只有所有流程都成功才返回true， 不为true就表示失败 
     */  
       
    @SuppressWarnings("finally")  
    public static boolean fileDownload(String downloadPath,String dir) {  
        
  
        try {  
        
            ftpClient.setBufferSize(1024);  
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  
              
            iterateDown(dir,downloadPath+File.separator);  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new RuntimeException("FTP下载失败  ", e);  
        } finally {  
            
            return true;  
        }  
    }   
      
      
    /** 
     *  
     *【功能描述：下载功能的迭代器，】 
     *【功能详细描述：功能详细描述】 
     * @see   【类、类#方法、类#成员】 
     * @param ftpClient ftpClient 连接器 
     * @param dir ftp上的路径 
     * @param downloadPath 本地路径 
     * @throws IOException 
     */  
    public static void iterateDown(String dir,String downloadPath) throws IOException{  
        // 列出这个地址对应到的是文件夹还是文件  
        FTPFile[] files = ftpClient.listFiles(dir);  
        for(FTPFile f:files){  
            //如果当前目录还没有创建，那么就在这里创建  
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
