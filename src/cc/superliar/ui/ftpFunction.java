package cc.superliar.ui;




import java.net.Socket;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ftpFunction {

    private Socket connectSocket;//鎺у埗杩炴帴锛�?敤浜庝紶閫佸拰鍝嶅簲鍛戒�?
    private Socket dataSocket;//鏁版嵁杩炴帴锛岀敤浜庢暟鎹紶杈�?
    private BufferedReader inData;//鎺у埗杩炴帴涓敤浜庤鍙栬繑鍥炰俊鎭殑鏁版嵁娴�?
    private BufferedWriter outData;//鎺у埗杩炴帴涓敤浜庝紶閫佺敤鎴峰懡浠ょ殑鏁版嵁娴�?
    private String response = null;//灏嗚繑鍥炰俊鎭皝瑁呮垚瀛楃涓�?
    private String remoteHost;//杩滅▼涓绘満鍚�
    private int remotePort;//閫氫俊绔彛鍙�
    private String remotePath;//杩滅▼璺緞
    private String user;//鐢ㄦ埛鍚�?
    private String passWord;//鐢ㄦ埛鍙ｄ护
    File rootPath = new File("/");//鏍硅矾寰�?
    File currentPath = rootPath;//褰撳墠璺緞
    private boolean logined;//鍒ゆ柇鏄惁鐧诲綍鏈嶅姟鍣ㄧ殑鏍囧織
    private boolean debug;

    public ftpFunction() {
        remoteHost = "localhost";
        remotePort = 21;
        remotePath = "/";
        user = "user";
        passWord = "123";
        logined = false;
        debug = false;
    }

    //璁剧疆鏈嶅姟鍣ㄥ煙鍚嶏紙IP鍦板潃锛�?
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }
    //杩斿洖鏈嶅姟鍣ㄥ煙鍚嶏紙IP鍦板潃锛�?
    public String getRemoteHost() {
        return remoteHost;
    }
    //璁剧疆绔彛
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
    //杩斿洖绔彛
    public int getRemotePort() {
        return remotePort;
    }
    //The remote directory path
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }
    /// The current remote directory path.
    public String getRemotePath() {
        return remotePath;
    }
    //鐢ㄦ埛鍚�?
    public void setUser(String user) {
        this.user = user;
    }
    //瀵嗙�?
    public void setPW(String password) {
        this.passWord = password;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Socket connect() {
        try {
            if (connectSocket == null) {

                connectSocket = new Socket(remoteHost, remotePort);
                inData = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));//杈撳叆淇℃伅(瀛楃杈撳叆娴�)

                outData = new BufferedWriter(new OutputStreamWriter(connectSocket.getOutputStream()));//杈撳嚭淇℃伅(瀛楃杈撳嚭娴�)
            }
            response = readLine();
          JOptionPane.showConfirmDialog(null,
                    "鏈嶅姟鍣ㄥ凡缁忔垚鍔熻繛鎺�",
                    "杩炴帴淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {           
             JOptionPane.showConfirmDialog(null,
                    " 杩炴帴澶辫触",
                    " 杩炴帴淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return connectSocket;
    }

    public void login() {
        try {
            if (connectSocket == null) {
                JOptionPane.showConfirmDialog(null,
                    " 鏈嶅姟鍣ㄥ皻鏈繛鎺ワ紝璇峰厛杩炴帴锛�",
                    " 杩炴帴淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);               
                return;
            }
            sendCommand("USER " + user);
            response = readLine();
            if (!response.startsWith("331")) {
                cleanup();
                 JOptionPane.showConfirmDialog(null,
                    " 鐢ㄦ埛鍚嶆垨瀵嗙爜閿欒锛�",
                    " 杩炴帴淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Error:鐢ㄦ埛鍚嶆垨瀵嗙爜閿欒锛�" + response);
                System.out.println(response);
                return;
            }
            sendCommand("PASS " + passWord);
            response = readLine();
            if (!response.startsWith("230")) {
                cleanup();
                 JOptionPane.showConfirmDialog(null,
                    " 鐢ㄦ埛鍚嶆垨瀵嗙爜閿欒锛�",
                    " 杩炴帴淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Error:鐢ㄦ埛鍚嶆垨瀵嗙爜閿欒锛�" + response);
                System.out.println(response);
                return;
            }
            logined = true;
             JOptionPane.showConfirmDialog(null,
                    " 鐧婚檰鎴愬姛锛�",
                    " 杩炴帴淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
            cwd(remotePath);
        } catch (Exception e) {
          JOptionPane.showConfirmDialog(null,
                    " 鐧婚檰澶辫触锛�",
                    " 鐧婚檰淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //鑾峰彇杩滅▼鏈嶅姟鍣ㄧ殑鐩綍鍒楄�??
    public ArrayList<String> list(String mask) throws IOException {
        if (!logined) {
            System.out.println("鏈嶅姟鍣ㄥ皻鏈繛鎺ャ��?");
        //login();
        }
        ArrayList<String> fileList = new ArrayList<String>();
        try {
            dataSocket = createDataSocket();
            if (mask == null || mask.equals("") || mask.equals(" ")) {
                sendCommand("LIST");
            } else {
                sendCommand("LIST " + mask);
            }
            response = readLine();
            if (!response.startsWith("1")) {
                System.out.println(response);
            }
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            String line;
            while ((line = dataIn.readLine()) != null) {
                fileList.add(line);
            }

            dataIn.close();//鍏抽棴鏁版嵁娴�
            dataSocket.close();//鍏抽棴鏁版嵁杩炴�? 
            response = readLine();

            System.out.println("List Complete.");
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fileList;
    }
    ///
    /// Close the FTP connection.
    /// 閫�鍑虹櫥褰曞苟缁堟杩炴帴QUIT
    public synchronized void close() throws IOException {
        try {
            sendCommand("QUIT ");
        } finally {
            cleanup();
            System.out.println("姝ｅ湪鍏抽棴......");
        }
    }

    private void cleanup() {
        try {
            inData.close();
            outData.close();
            connectSocket.close();
            //connectSocket = null;
            logined = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    ///
    /// If the value of mode is true, set binary mode for downloads.
    /// Else, set Ascii mode.
    ///
    ///
    public void setBinaryMode(Boolean mode) throws IOException {

        if (mode) {
            sendCommand("TYPE I ");
        } else {
            sendCommand("TYPE A ");
        }
        response = readLine();
        if (!response.startsWith("200")) {
            throw new IOException("Caught Error " + response);
        }
    }
    //鏄剧ず褰撳墠杩滅▼宸ヤ綔鐩綍PWD
    public synchronized String pwd() throws IOException {
        sendCommand("XPWD ");
        String dir = null;
        response = readLine();
        if (response.startsWith("257")) {         //鏈嶅姟鍣ㄥ搷搴斾俊鎭锛�257 "/C:/TEMP/" is current directory.鎴彇涓ゅ紩鍙蜂箣闂寸殑鍐呭�?
            int fristQuote = response.indexOf('\"');
            int secondQuote = response.indexOf('\"', fristQuote + 1);
            if (secondQuote > 0) {
                dir = response.substring(fristQuote + 1, secondQuote);
            }
        }
        System.out.println(""+dir);
        return dir;
    }
    //CWD �?瑰彉杩滅▼绯荤粺鐨勫伐浣滅洰褰�
    public synchronized boolean cwd(String dir) throws IOException {
        if (dir.equals("/")) {//鏍硅矾寰�?
            System.out.println("褰撳墠璺緞鏄牴鐩綍锛�");
        }
        if (!logined) {
            login();
        }
        sendCommand("CWD " + dir);
        response = readLine();
        if (response.startsWith("250 ")) {
            return true;
        } else {
            return false;
        }
    }
    //涓婁紶鏂囦欢
    public synchronized boolean upload(String localFileName) throws IOException {
        dataSocket = createDataSocket();
        int i = localFileName.lastIndexOf("/");
        if (i == -1) {
            i = localFileName.lastIndexOf("\\");
        }
        String element_1 = "";
        if (i != -1) {
            element_1 = localFileName.substring(i + 1);
        }
        sendCommand("STOR " + element_1);
        response = readLine();
        if (!response.startsWith("1")) {
            System.out.println(response);
        }
        FileInputStream dataIn = new FileInputStream(localFileName);
        BufferedOutputStream dataOut = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        do {
            bytesRead = dataIn.read(buffer);
            if (bytesRead != -1) {
                dataOut.write(buffer, 0, bytesRead);
            }
        } while (bytesRead != -1);
        dataOut.flush();
        dataOut.close();
        dataIn.close();
        dataSocket.close();//鍏抽棴姝ゆ暟鎹繛鎺�?
        response = readLine();

        if (response.startsWith("226")) {
            JOptionPane.showConfirmDialog(null,
                    " 鏂囦欢涓婁紶鎴愬姛锛�?",
                    " 涓婁紶淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);            
        }
        return (response.startsWith("226"));
    }
    //涓嬭浇鏂囦欢  RETR
    public synchronized boolean download(String remoteFile, String localFile) throws IOException {

        dataSocket = createDataSocket();
        sendCommand("RETR " + remoteFile);
        response = readLine();
        if (!response.startsWith("1")) {
            System.out.println(response);
        }
        System.out.println(localFile);
        BufferedInputStream dataIn = new BufferedInputStream(dataSocket.getInputStream());
        new File(localFile).createNewFile();
        FileOutputStream fileOut = new FileOutputStream(localFile);
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        do {
            bytesRead = dataIn.read(buffer);
            if (bytesRead != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }
        } while (bytesRead != -1);
        fileOut.flush();
        fileOut.close();
        dataSocket.close();
        response = readLine();

        if (response.startsWith("226")) {
             JOptionPane.showConfirmDialog(null,
                    "涓嬭浇鎴愬姛",
                    " 涓嬭浇淇℃伅", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);         
        }
        return (response.startsWith("226"));
    }
    


    
    //鍦ㄨ繙绋嬫湇鍔�?�櫒涓婂垱寤轰竴涓洰褰�?
    public void mkdir(String dirName) throws IOException {

        if (!logined) {
            login();
        }

        sendCommand("XMKD " + dirName); // 鍒涘缓鐩綍
        response = readLine();
        if (!response.startsWith("257")) {      //FTP鍛戒护鍙戦�佽繃绋嬪彂鐢熷紓甯�
            System.out.println( response);
        } else {
             JOptionPane.showConfirmDialog(null,
                    "鍒涘缓鐩綍"+dirName+"  鎴愬姛锛侊紒",
                    " 鍒涘缓鐩綍", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);             //鎴愬姛鍒涘缓鐩�?
        }

    }
    //鍒犻櫎杩滅▼涓湇鍔″櫒涓婄殑涓�涓洰褰�
    public void rmdir(String dirName) throws IOException {
        if (!logined) {                 //濡傛灉灏氭湭涓庢湇鍔″櫒杩炴帴锛屽垯杩炴帴鏈嶅姟鍣�
            login();
        }

        sendCommand("XRMD " + dirName);
        response = readLine();
        if (!response.startsWith("250")) {     //FTP鍛戒护鍙戦�佽繃绋嬪彂鐢熷紓甯�
            System.out.println(response);
        } else {
             JOptionPane.showConfirmDialog(null,
                    "鍒犻櫎鐩綍"+dirName+"  鎴愬姛锛侊紒",
                    " 鍒犻櫎鐩綍", JOptionPane.CLOSED_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);          //鎴愬姛鍒犻櫎鐩�?
        }

    }
    //寤虹珛鏁版嵁杩炴�?
    private Socket createDataSocket() throws IOException {

        sendCommand("PASV ");               //閲囩敤Pasv妯�?�紡锛堣鍔ㄦā寮忥級锛岀敱鏈嶅姟鍣ㄨ繑鍥炴暟鎹紶杈撶殑涓存椂绔彛鍙凤紝浣跨敤璇ョ鍙ｈ繘琛屾暟鎹紶杈�
        response = readLine();
        if (!response.startsWith("227")) {      //FTP鍛戒护浼犺緭杩囩▼鍙戠敓寮傚�?
            System.out.println(response);
        }
        String clientIp = "";
        int port = -1;
        int opening = response.indexOf('(');               //閲囩敤Pasv妯�?�紡鏈嶅姟鍣ㄨ繑鍥炵殑淇℃伅濡傗��227 Entering Passive Mode (127,0,0,1,64,2)鈥�
        int closing = response.indexOf(')', opening + 1);  //鍙�"()"涔嬮棿鐨勫唴瀹癸�?127,0,0,1,64,2 锛屽�?4涓暟�?�椾负鏈満IP鍦板潃锛岃浆鎹㈡�?127.0.0.1鏍煎�?
        if (closing > 0) {                                 //绔彛鍙风敱鍚�2涓暟�?�楄绠�?緱鍑猴細64*256+2=16386
            String dataLink = response.substring(opening + 1, closing);

            StringTokenizer arg = new StringTokenizer(dataLink, ",", false);
            clientIp = arg.nextToken();

            for (int i = 0; i < 3; i++) {
                String hIp = arg.nextToken();
                clientIp = clientIp + "." + hIp;
            }
            port = Integer.parseInt(arg.nextToken()) * 256 + Integer.parseInt(arg.nextToken());
        }

        return new Socket(clientIp, port);
    }
//鐢ㄤ簬璇诲彇鏈嶅姟鍣ㄨ繑鍥炵殑鍝嶅簲淇℃�?
    private String readLine() throws IOException {
        String line = inData.readLine();
        if (debug) {
            System.out.println("< " + line);
        }
        return line;
    }
//鐢ㄤ簬鍙戦�佸懡浠�
    private void sendCommand(String line) {
        if (connectSocket == null) {
            System.out.println("FTP灏氭湭杩炴帴");         //鏈缓绔嬮�氫俊閾炬帴锛屾姏鍑哄紓甯歌鍛�
        }
        try {
            outData.write(line + "\r\n");               //鍙戦�佸懡浠�?
            outData.flush();                            //鍒锋柊杈撳嚭娴�
            if (debug) {
                System.out.println("> " + line);        //鍚屾椂鎺у埗鍙拌緭鍑虹浉搴斿懡浠や俊鎭紝浠ヤ究鍒嗘�??
            }
        } catch (Exception e) {
            connectSocket = null;
            System.out.println(e);
            return;
        }
    }
}
