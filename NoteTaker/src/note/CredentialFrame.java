package note;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;

public class CredentialFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CredentialFrame instance;
	/**
	 * Launch the application.
	 */
	/**
	 * Create the frame.
	 */
	public CredentialFrame(ProgramMain pm) {
		
		instance = this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 391, 369);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(26, 26, 26));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnLogin.setBackground(new Color(192, 192, 192));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pm.OpenLoginForm(instance);
			}
		});
		btnLogin.setBounds(79, 135, 216, 67);
		contentPane.add(btnLogin);
		
		JLabel lvlLOGO = new JLabel("Note Taker");
		lvlLOGO.setForeground(new Color(238, 238, 238));
		lvlLOGO.setBackground(new Color(26, 26, 26));
		lvlLOGO.setHorizontalAlignment(SwingConstants.CENTER);
		lvlLOGO.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lvlLOGO.setBounds(73, 38, 232, 72);
		contentPane.add(lvlLOGO);
		
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pm.OpenRegisterForm(instance);
			}
		});
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnRegister.setBackground(Color.LIGHT_GRAY);
		btnRegister.setBounds(79, 213, 216, 67);
		contentPane.add(btnRegister);
	}
}
