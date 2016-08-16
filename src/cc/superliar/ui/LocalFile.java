package cc.superliar.ui;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

import cc.superliar.util.FileUtil;


import java.lang.Runtime;
import java.lang.Process;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.border.LineBorder; 

/**
 * 实现本地文件浏览，为继承JPanel的一个面板
 *
 * @author Lonsy
 * @version 1.0
 */
public class LocalFile extends JFrame implements ActionListener, MouseListener
{
    private JButton jbUp;
    
    private JComboBox jcbPath;
    
    private JTable jtFile;//表格中文件

    private DefaultTableModel dtmFile;

    private JLabel jlLocal;//底部地址栏
    
    private File path;

    private String currentPath;
  
    private int currentIndex;
   
    private boolean init = false;
    
    File[] files ;
   
    String ftpPathop;
    
    private JPopupMenu popup = new JPopupMenu();//建立一个弹出式菜单  
    private JMenuItem refresh = new JMenuItem("刷新");  //右键弹出菜单 
    private JMenuItem delete  = new JMenuItem("删除");    
    private JMenuItem download  = new JMenuItem("下载");   
    private JMenuItem upload  = new JMenuItem("上传");   
    private JMenuItem rename =  new JMenuItem("重命名");  
    private JMenuItem upDir = new JMenuItem("上级目录");   
    private JMenuItem showLog = new JMenuItem("显示日志信息");
    private JButton btnNewButton_1;

    public LocalFile() {
    	
    	this.setSize(503,650);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( (int) (screenSize.width - this.getWidth()) / 2,  (int) (screenSize.height - this.getHeight()) / 2);
        this.setVisible(true);
        
        createPopup();
        
        dtmFile = new LocalTableModel();
        dtmFile.addColumn("名称");
        dtmFile.addColumn("大小");
        dtmFile.addColumn("类型");
        dtmFile.addColumn("修改日期");
        jtFile = new JTable(dtmFile);
        jtFile.setShowGrid(false);
        jtFile.addMouseListener(this);
        
       
        jlLocal = new JLabel("本地状态", JLabel.CENTER);
        jlLocal.setBounds(10, 575, 426, 15);
        
        JScrollPane scrollPane = new JScrollPane(jtFile);
        scrollPane.setBounds(10, 109, 427, 455);
        
        jcbPath = new JComboBox();
        jcbPath.setBounds(103, 76, 334, 23);
        
        jbUp = new JButton("Up");
        jbUp.setBounds(10, 76, 83, 23);

        JButton btnConnect = new JButton("注销");      
        btnConnect.setBounds(11, 43, 93, 23);
        
        
        getContentPane().setLayout(null); 
        getContentPane().add(scrollPane);
        getContentPane().add(jlLocal);
        getContentPane().add(jcbPath);
        getContentPane(  ).add(jbUp);
        getContentPane().add(btnConnect);
        
        JButton btnNewButton = new JButton("上传");
        btnNewButton.setBounds(113, 43, 93, 23);
        getContentPane().add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fd = new JFileChooser();  
            	fd.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);  
            	fd.showOpenDialog(null);  
            	File f = fd.getSelectedFile();  
            	String ftpPath = jlLocal.getText().substring(10);
            	ftpPathop =  jlLocal.getText().replace("\\", "/");
            	path = new File(ftpPathop);
            	if(f != null){
            		if(f.isDirectory() == false){
            		System.out.println("是文件"+f.toString()+","+f.getName()+"根目录="+path+",ftpPath="+ftpPath);       		
            		FileUtil.ftpUpload(f.toString(),ftpPath,f.getName());
            		listFiles(path);    
            		}else{  	
            			System.out.println("是文件夹"+f.toString()+","+f.getName()+"根目录="+path);
            			try {
							FileUtil.ftpUpload2(f.toString(),ftpPath);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
            			listFiles(path);    
            	}
            	}
           }
        });
        
        JButton button = new JButton("下载");
        button.setBounds(216, 43, 93, 23);
        getContentPane().add(button);
        
        btnNewButton_1 = new JButton("全部下载");
        btnNewButton_1.setBounds(322, 43, 93, 23);
        getContentPane().add(btnNewButton_1);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	String path = jlLocal.getText();
              	
            	System.out.println(path);
            }
        });
        
        
        
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
        		int i = jtFile.getSelectedRow();
                System.out.println(i);
            	if (i < 0) {
					JOptionPane.showMessageDialog(null, "请选择文件", "提示",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
            	String path = jlLocal.getText();
            	
            	String fileName = files[i].getName();
            	
            	String ftpPath = path+"\\"+fileName;
            	
                FileUtil.createDir("D:\\shentao\\dwonload\\"+files[i].getName());  
                
            	FileUtil.fileDownload("D:\\shentao\\dwonload\\"+files[i].getName(),ftpPath.substring(10));
            	
            	System.out.println(ftpPath);

            }
        });
        
        
        jcbPath.addActionListener(this);
        jbUp.addActionListener(this);

        //显示系统分区及文件路径 并 在JTabel中显示当前路径的文件信息
        path = new File("E:/ftpTest");
        System.out.println("path="+path);
        listFiles(path);    
        init = true;
        
     
    }

    
    private void createPopup() {
		// TODO Auto-generated method stub

	}

	//处理路径的选择事件
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==jbUp && jtFile.getValueAt(0, 0).toString().equals("返回上级")
                && jtFile.getValueAt(0, 2).toString().equals(""))
        {
        	System.out.println("currentPath="+currentPath);
            listFiles(new File(currentPath).getParentFile());
            return;
        }
        if (init == false)
        {
            return;
        }
        int index = jcbPath.getSelectedIndex();
        String item = (String)jcbPath.getSelectedItem();
        if (item.startsWith("  "))
        {
            int root = index - 1;
            while (((String)jcbPath.getItemAt(root)).startsWith("  "))
            {
                root--;
            }
            String path = (String)jcbPath.getItemAt(root);
            while (root < index)
            {
                path += ((String)jcbPath.getItemAt(++root)).trim();;
                path += "//";
            }
            if (listFiles(new File(path)) == false)
            {
                jcbPath.setSelectedIndex(currentIndex);
            }
            else
            {
                currentIndex = index;
            }
        }
        else
        {
            if (listFiles(new File(item)) == false)
            {
                jcbPath.setSelectedIndex(currentIndex);
            }
            else
            {
                currentIndex = index;
            }
        }
    }

    //JTable里文件夹双击事件
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==2) {
            int row = ((JTable)e.getSource()).getSelectedRow();
            if (((JTable)e.getSource()).getValueAt(row, 2).toString().equals("文件夹"))
            {
                File file;
                
                //判断是否为根目录，作不同处理。一个 / 的差别
                if (currentPath.split("///").length > 1)
                {
                	System.out.println("currentPath="+currentPath);
                    file = new File(currentPath + "///" + ((JTable)e.getSource()).getValueAt(row, 0).toString());
                    System.out.println(file);
                }
                else
                	
                {                    
                    file = new File(currentPath +"/"+ ((JTable)e.getSource()).getValueAt(row, 0).toString());
                    System.out.println(file);
                }
                listFiles(file);
            }
            else if (((JTable)e.getSource()).getValueAt(row, 0).toString().equals("返回上级")
                    && ((JTable)e.getSource()).getValueAt(row, 2).toString().equals(""))
            {
                listFiles(new File(currentPath).getParentFile());
            }
        }
    }
    //其他一堆无用的事件
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    //显示系统分区及文件路径 并 在JTabel中显示当前路径的文件信息
    private boolean listFiles(File path) {
        String strPath = path.getAbsolutePath();
        System.out.println(strPath);
        if (path.isDirectory() == false)
        {	System.out.println(strPath);
            JOptionPane.showMessageDialog(this, "此路径不存在，或无此文件");
            return false;
        }
        
        currentPath = path.getAbsolutePath();
        init = false;
        jcbPath.removeAllItems();
        File[] roots = File.listRoots();
        int index = 0;
        for (int i=0; i<roots.length; i++)
        {
            String rootPath = roots[i].getAbsolutePath();
            jcbPath.addItem(rootPath);
            if (currentPath.indexOf(rootPath) != -1)
            {
                String[] bufPath = currentPath.split("////");
                for (int j=1; j<bufPath.length; j++)
                {
                    String buf = "  ";
                    for (int k=1; k<j; k++)
                    {
                        buf += "  ";
                    }
                    jcbPath.addItem(buf + bufPath[j]);
                    index = i + j;
                }
                if (bufPath.length == 1)
                {
                    index = i;
                }
            }
        }
        jcbPath.setSelectedIndex(index);
        init = true;
        currentIndex = index;

        //清空现有数据
        dtmFile.setRowCount(0);

      

        //列出当前目录所有目录及文件
        files = path.listFiles();
        for (int i=0; i<files.length; i++)
        {
            String name = files[i].getName();
            if (files[i].isDirectory())
            {
                dtmFile.addRow(new String[]{name, "", "文件夹", ""});
            }
            else
            {
                if (name.lastIndexOf(".") != -1)
                {
                    dtmFile.addRow(new String[]{name.substring(0, name.lastIndexOf(".")), 
                            FileUtil.sizeFormat(files[i].length()), 
                            name.substring(name.lastIndexOf(".") + 1),
                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
                }
                else
                {
                    dtmFile.addRow(new String[]{name, 
                    		FileUtil.sizeFormat(files[i].length()), 
                            "",
                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
                }
            }
        }
        
        jlLocal.setText(currentPath);

        return true;
    }
    



   

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LocalFile dialog = new LocalFile();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    //实现相应的tablemodel类
    class LocalTableModel extends DefaultTableModel
    {
        public boolean isCellEditable(int row, int column) {
            return false;
        }  
    }
}