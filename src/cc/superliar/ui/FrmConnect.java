package cc.superliar.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cc.superliar.util.FileUtil;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrmConnect extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField edtAddress;
	private JTextField edtUser;
	private JTextField edtPwd;
	private JTextField edtPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FrmConnect dialog = new FrmConnect();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FrmConnect() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("adress£º");
			lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNewLabel.setBounds(42, 70, 64, 15);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("username£º");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNewLabel_1.setBounds(30, 105, 76, 15);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblPsd = new JLabel("psd£º");
			lblPsd.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPsd.setBounds(42, 142, 64, 15);
			contentPanel.add(lblPsd);
		}
		{
			JLabel lblPort = new JLabel("port£º");
			lblPort.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPort.setBounds(251, 70, 54, 15);
			contentPanel.add(lblPort);
		}
		
		edtAddress = new JTextField();
		edtAddress.setBounds(116, 67, 111, 21);
		contentPanel.add(edtAddress);
		edtAddress.setColumns(10);
		
		edtUser = new JTextField();
		edtUser.setColumns(10);
		edtUser.setBounds(116, 102, 111, 21);
		contentPanel.add(edtUser);
		
		edtPwd = new JTextField();
		edtPwd.setColumns(10);
		edtPwd.setBounds(116, 139, 111, 21);
		contentPanel.add(edtPwd);
		
		edtPort = new JTextField();
		edtPort.setColumns(10);
		edtPort.setBounds(310, 67, 66, 21);
		contentPanel.add(edtPort);
		{
			JLabel lblNewLabel_2 = new JLabel("Á¬½Ó");
			lblNewLabel_2.setFont(new Font("ËÎÌå", Font.PLAIN, 15));
			lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_2.setBounds(180, 10, 66, 21);
			contentPanel.add(lblNewLabel_2);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				        String address = edtAddress.getText();
				        int port = Integer.parseInt(edtPort.getText());
				        String username = edtUser.getText();
				        String password = edtPwd.getText();
				        System.out.println(address+port+username+password);
				        FileUtil.login(address,port,username,password);
				        dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	        this.setSize(500,300);
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        this.setLocation( (int) (screenSize.width - this.getWidth()) / 2,  (int) (screenSize.height - this.getHeight()) / 2);
	        this.setVisible(true);
	}
	
}
