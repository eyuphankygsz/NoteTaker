package note;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.DatabaseHelper;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.DropMode;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField passwordField;
	private LoginFrame instance;
	
	public LoginFrame(ProgramMain pm) {
		instance = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 356, 387);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(26, 26, 26));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnRegister = new JButton("Don't Have An Account?");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgramMain.Instance.OpenRegisterForm(instance);
			}
		});
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegister.setBackground(Color.LIGHT_GRAY);
		btnRegister.setBounds(63, 290, 216, 34);
		contentPane.add(btnRegister);
		
		usernameField = new JTextField();
		usernameField.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyTyped(java.awt.event.KeyEvent evt) {
		        if(usernameField.getText().length()>=45 &&!(evt.getKeyChar()==KeyEvent.VK_DELETE||evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)) {
		            getToolkit().beep();
		            evt.consume();
		         }
		     }
		});
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		usernameField.setBounds(63, 47, 216, 49);
		contentPane.add(usernameField);
		usernameField.setColumns(10);
		
		passwordField = new JTextField();
		passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyTyped(java.awt.event.KeyEvent evt) {
		        if(usernameField.getText().length()>=45 &&!(evt.getKeyChar()==KeyEvent.VK_DELETE||evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)) {
		            getToolkit().beep();
		            evt.consume();
		         }
		     }
		});
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		passwordField.setColumns(10);
		passwordField.setBounds(63, 130, 216, 49);
		contentPane.add(passwordField);

		JLabel lblError = new JLabel("");
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setForeground(new Color(255, 255, 255));
		lblError.setBounds(63, 173, 216, 37);
		contentPane.add(lblError);

		JButton btnLogin = new JButton("LOGIN");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(DatabaseHelper.instance.CheckCredentials(usernameField.getText(), passwordField.getText())) {						
						lblError.setText("");
						pm.OpenMainFrame(instance);
					}
					else
						lblError.setText("Wrong Credentials!");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 22));
		btnLogin.setBackground(Color.LIGHT_GRAY);
		btnLogin.setBounds(63, 221, 216, 58);
		contentPane.add(btnLogin);
		
		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setForeground(Color.WHITE);
		lblNickname.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNickname.setBounds(63, 23, 104, 24);
		contentPane.add(lblNickname);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblPassword.setBounds(63, 107, 104, 24);
		contentPane.add(lblPassword);
		
	}
}

