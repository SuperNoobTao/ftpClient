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
 * ʵ�ֱ����ļ������Ϊ�̳�JPanel��һ�����
 *
 * @author Lonsy
 * @version 1.0
 */
public class LocalFile extends JFrame implements ActionListener, MouseListener
{
    private JButton jbUp;
    
    private JComboBox jcbPath;
    
    private JTable jtFile;//������ļ�

    private DefaultTableModel dtmFile;

    private JLabel jlLocal;//�ײ���ַ��
    
    private File path;

    private String currentPath;
  
    private int currentIndex;
   
    private boolean init = false;
    
    File[] files ;
   
    String ftpPathop;
    
    private JPopupMenu popup = new JPopupMenu();//����һ������ʽ�˵�  
    private JMenuItem refresh = new JMenuItem("ˢ��");  //�Ҽ������˵� 
    private JMenuItem delete  = new JMenuItem("ɾ��");    
    private JMenuItem download  = new JMenuItem("����");   
    private JMenuItem upload  = new JMenuItem("�ϴ�");   
    private JMenuItem rename =  new JMenuItem("������");  
    private JMenuItem upDir = new JMenuItem("�ϼ�Ŀ¼");   
    private JMenuItem showLog = new JMenuItem("��ʾ��־��Ϣ");
    private JButton btnNewButton_1;

    public LocalFile() {
    	
    	this.setSize(503,650);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation( (int) (screenSize.width - this.getWidth()) / 2,  (int) (screenSize.height - this.getHeight()) / 2);
        this.setVisible(true);
        
        createPopup();
        
        dtmFile = new LocalTableModel();
        dtmFile.addColumn("����");
        dtmFile.addColumn("��С");
        dtmFile.addColumn("����");
        dtmFile.addColumn("�޸�����");
        jtFile = new JTable(dtmFile);
        jtFile.setShowGrid(false);
        jtFile.addMouseListener(this);
        
       
        jlLocal = new JLabel("����״̬", JLabel.CENTER);
        jlLocal.setBounds(10, 575, 426, 15);
        
        JScrollPane scrollPane = new JScrollPane(jtFile);
        scrollPane.setBounds(10, 109, 427, 455);
        
        jcbPath = new JComboBox();
        jcbPath.setBounds(103, 76, 334, 23);
        
        jbUp = new JButton("Up");
        jbUp.setBounds(10, 76, 83, 23);

        JButton btnConnect = new JButton("ע��");      
        btnConnect.setBounds(11, 43, 93, 23);
        
        
        getContentPane().setLayout(null); 
        getContentPane().add(scrollPane);
        getContentPane().add(jlLocal);
        getContentPane().add(jcbPath);
        getContentPane(  ).add(jbUp);
        getContentPane().add(btnConnect);
        
        JButton btnNewButton = new JButton("�ϴ�");
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
            		System.out.println("���ļ�"+f.toString()+","+f.getName()+"��Ŀ¼="+path+",ftpPath="+ftpPath);       		
            		FileUtil.ftpUpload(f.toString(),ftpPath,f.getName());
            		listFiles(path);    
            		}else{  	
            			System.out.println("���ļ���"+f.toString()+","+f.getName()+"��Ŀ¼="+path);
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
        
        JButton button = new JButton("����");
        button.setBounds(216, 43, 93, 23);
        getContentPane().add(button);
        
        btnNewButton_1 = new JButton("ȫ������");
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
					JOptionPane.showMessageDialog(null, "��ѡ���ļ�", "��ʾ",
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

        //��ʾϵͳ�������ļ�·�� �� ��JTabel����ʾ��ǰ·�����ļ���Ϣ
        path = new File("E:/ftpTest");
        System.out.println("path="+path);
        listFiles(path);    
        init = true;
        
     
    }

    
    private void createPopup() {
		// TODO Auto-generated method stub

	}

	//����·����ѡ���¼�
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==jbUp && jtFile.getValueAt(0, 0).toString().equals("�����ϼ�")
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

    //JTable���ļ���˫���¼�
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount()==2) {
            int row = ((JTable)e.getSource()).getSelectedRow();
            if (((JTable)e.getSource()).getValueAt(row, 2).toString().equals("�ļ���"))
            {
                File file;
                
                //�ж��Ƿ�Ϊ��Ŀ¼������ͬ����һ�� / �Ĳ��
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
            else if (((JTable)e.getSource()).getValueAt(row, 0).toString().equals("�����ϼ�")
                    && ((JTable)e.getSource()).getValueAt(row, 2).toString().equals(""))
            {
                listFiles(new File(currentPath).getParentFile());
            }
        }
    }
    //����һ�����õ��¼�
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    //��ʾϵͳ�������ļ�·�� �� ��JTabel����ʾ��ǰ·�����ļ���Ϣ
    private boolean listFiles(File path) {
        String strPath = path.getAbsolutePath();
        System.out.println(strPath);
        if (path.isDirectory() == false)
        {	System.out.println(strPath);
            JOptionPane.showMessageDialog(this, "��·�������ڣ����޴��ļ�");
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

        //�����������
        dtmFile.setRowCount(0);

      

        //�г���ǰĿ¼����Ŀ¼���ļ�
        files = path.listFiles();
        for (int i=0; i<files.length; i++)
        {
            String name = files[i].getName();
            if (files[i].isDirectory())
            {
                dtmFile.addRow(new String[]{name, "", "�ļ���", ""});
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


    //ʵ����Ӧ��tablemodel��
    class LocalTableModel extends DefaultTableModel
    {
        public boolean isCellEditable(int row, int column) {
            return false;
        }  
    }
}