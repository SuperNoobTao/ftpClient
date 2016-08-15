package cc.superliar.ui;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
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
public class LocalFile extends JPanel implements ActionListener, MouseListener
{
    private JButton jbUp;
    private JComboBox jcbPath;
    private JComboBox serPath;
    private JTable jtFile;
    private DefaultTableModel dtmFile;
    private JLabel jlLocal;
    private File path;
    private String currentPath;
    private int currentIndex;
    private boolean init = false;

    public LocalFile() {
    	setBorder(new LineBorder(new Color(0, 0, 0)));
        
        JPanel jp = new JPanel();
        jp.setBounds(10, 10, 880, 23);
        jp.setLayout(null);
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
        setLayout(null);

        add(jp);
        
        JMenu mnNewMenu = new JMenu("menu");
        mnNewMenu.setHorizontalAlignment(SwingConstants.LEFT);
        mnNewMenu.setBounds(0, 0, 111, 22);
        jp.add(mnNewMenu);
        
        JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
        mnNewMenu.add(mntmNewMenuItem);
        
        JMenu mnNewMenu_1 = new JMenu("New menu");
        mnNewMenu_1.setBounds(121, 1, 111, 22);
        jp.add(mnNewMenu_1);
        JScrollPane scrollPane = new JScrollPane(jtFile);
        scrollPane.setBounds(10, 109, 427, 455);
        add(scrollPane);
        add(jlLocal);
        jcbPath = new JComboBox();
        jcbPath.setBounds(103, 76, 334, 23);
        add(jcbPath);
        jcbPath.addActionListener(this);
        jbUp = new JButton("Up");
        jbUp.setBounds(10, 76, 83, 23);
        add(jbUp);
        jbUp.addActionListener(this);
        
        JButton btnNewButton = new JButton("Up");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnNewButton.setBounds(464, 76, 83, 23);
        add(btnNewButton);
        
        JScrollPane scrollPane_1 = new JScrollPane((Component) null);
        scrollPane_1.setBounds(464, 109, 427, 455);
        add(scrollPane_1);
        
        JLabel label = new JLabel("xxxx", SwingConstants.CENTER);
        label.setBounds(463, 575, 427, 15);
        add(label);
        
        serPath = new JComboBox();
        serPath.setBounds(557, 77, 334, 21);
        add(serPath);
        
        JButton btnConnect = new JButton("连接");
        btnConnect.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new FrmConnect();
        	}
        });
        btnConnect.setBounds(11, 43, 93, 23);
        add(btnConnect);
        
        JButton btnNewButton_2 = new JButton("New button");
        btnNewButton_2.setBounds(114, 45, 93, 23);
        add(btnNewButton_2);
        
        JButton btnNewButton_3 = new JButton("New button");
        btnNewButton_3.setBounds(217, 43, 93, 23);
        add(btnNewButton_3);

        //显示系统分区及文件路径 并 在JTabel中显示当前路径的文件信息
        path = new File(System.getProperty("user.dir"));
        System.out.println("path="+path);
        listFiles(path);    

        init = true;
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

        //如果判断为非分区根目录，则添加 返回上级 一行
        if (strPath.split("////").length > 1)
        {
            dtmFile.addRow(new String[]{"返回上级", "", "", ""});
        }

        //列出当前目录所有目录及文件
        File[] files = path.listFiles();
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
                            sizeFormat(files[i].length()), 
                            name.substring(name.lastIndexOf(".") + 1),
                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
                }
                else
                {
                    dtmFile.addRow(new String[]{name, 
                            sizeFormat(files[i].length()), 
                            "",
                            new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(files[i].lastModified()))});
                }
            }
        }
        
        jlLocal.setText(currentPath);

        return true;
    }

    //将文件大小转换成相应字符串格式
    private String sizeFormat(long length) {
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

    //测试
    public static void main(String[] args) {
        JFrame jf = new JFrame("测试");
        jf.setSize(300, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setLocation((int)(di.getWidth() - jf.getWidth()) / 2, 
                (int)(di.getHeight() - jf.getHeight()) / 2);
        jf.getContentPane().add(new LocalFile());
        jf.setVisible(true);
    }

    //实现相应的tablemodel类
    class LocalTableModel extends DefaultTableModel
    {
        public boolean isCellEditable(int row, int column) {
            return false;
        }  
    }
}