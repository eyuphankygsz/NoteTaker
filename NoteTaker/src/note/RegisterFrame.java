package note;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import database.DatabaseHelper;
import note.Entity.UserEntity;

public class RegisterFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTextField usernameField;
	private JTextField passwordField;
	private RegisterFrame instance;
	private JTextField mailField;
	private JTextField passwordAgain;
	private JTextField mailAgain;
	
	
	/**
	 * Create the frame.
	 */
	public RegisterFrame(ProgramMain pm) {
		instance = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 623, 493);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(26, 26, 26));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
		passwordField.setBounds(63, 135, 216, 49);
		contentPane.add(passwordField);
		
		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setForeground(Color.WHITE);
		lblNickname.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNickname.setBounds(63, 21, 104, 24);
		contentPane.add(lblNickname);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblPassword.setBounds(63, 112, 104, 24);
		contentPane.add(lblPassword);
		
		JLabel lblEmail = new JLabel("E-mail:");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblEmail.setBounds(63, 195, 104, 24);
		contentPane.add(lblEmail);
		
		mailField = new JTextField();
		mailField.setFont(new Font("Tahoma", Font.PLAIN, 22));
		mailField.setColumns(10);
		mailField.setBounds(63, 218, 216, 49);
		contentPane.add(mailField);
		
		JLabel lblPassword2 = new JLabel("Password: Again:");
		lblPassword2.setForeground(Color.WHITE);
		lblPassword2.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblPassword2.setBounds(336, 112, 145, 24);
		contentPane.add(lblPassword2);
		
		passwordAgain = new JTextField();
		passwordAgain.setFont(new Font("Tahoma", Font.PLAIN, 22));
		passwordAgain.setColumns(10);
		passwordAgain.setBounds(336, 135, 216, 49);
		contentPane.add(passwordAgain);
		
		JLabel lblEmailAgain = new JLabel("E-mail Again:");
		lblEmailAgain.setForeground(Color.WHITE);
		lblEmailAgain.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblEmailAgain.setBounds(336, 195, 145, 24);
		contentPane.add(lblEmailAgain);
		
		mailAgain = new JTextField();
		mailAgain.setFont(new Font("Tahoma", Font.PLAIN, 22));
		mailAgain.setColumns(10);
		mailAgain.setBounds(336, 218, 216, 49);
		contentPane.add(mailAgain);

		JLabel warningText = new JLabel("");
		warningText.setHorizontalAlignment(SwingConstants.CENTER);
		warningText.setForeground(Color.WHITE);
		warningText.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		warningText.setBounds(40, 278, 534, 50);
		contentPane.add(warningText);
	
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {					
						warningText.setText("");
						String nick = usernameField.getText();
						String mail = mailField.getText();
						String password = passwordField.getText();

						
						 if(nick.isEmpty() || nick.isBlank()) {
							warningText.setText("Nick can't be empty!");
							return;
						}
						else if(password.isEmpty() || password.isBlank()) {
							warningText.setText("Password can't be empty!");
							return;
						}
						else if(mail.isEmpty() || mail.isBlank()) {
							warningText.setText("Mail can't be empty!");
							return;
						}
						else if(!mail.matches("[^@]+@[^@]+\\.[^@]+"))
						{
							warningText.setText("Mail is not valid.");
							return;
						}
						else if(mail.compareTo(mailAgain.getText()) != 0) {
							warningText.setText("Mails not matched.");
							return;
						}
						else if(password.compareTo(passwordAgain.getText()) != 0) {
							warningText.setText("Passwords not matched.");
							return;
						}
						else {
							ResultSet rs = DatabaseHelper.instance.SearchUser(nick);
							if(rs.next()) {
								warningText.setText("This username is already taken.");
								return;
							}
						}

						DatabaseHelper.instance.RegisterUser(nick, mail, password);
						ProgramMain.Instance.OpenLoginForm(instance);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 22));
		btnRegister.setBackground(Color.LIGHT_GRAY);
		btnRegister.setBounds(190, 339, 216, 58);
		contentPane.add(btnRegister);
		
		JButton btnLogin = new JButton("Have An Account?");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgramMain.Instance.OpenLoginForm(instance);
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogin.setBackground(Color.LIGHT_GRAY);
		btnLogin.setBounds(190, 408, 216, 34);
		contentPane.add(btnLogin);
		
		
	}
}
