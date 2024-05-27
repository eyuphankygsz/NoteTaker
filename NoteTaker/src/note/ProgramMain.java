package note;

import java.sql.SQLException;

import javax.swing.JFrame;

import database.DatabaseHelper;
import note.Entity.UserEntity;

public class ProgramMain {
	public static ProgramMain Instance;
	private DatabaseHelper dbHelper;
	private UserEntity loggedUser;
	
	public static void main(String[] args) {
		ProgramMain pm = new ProgramMain();
		pm.Start();

	}
	public void Start() {
		Instance = this;
		dbHelper = new DatabaseHelper();
		CredentialFrame frame = new CredentialFrame(this);
		frame.setVisible(true);
	}
	public void OpenLoginForm(JFrame oldFrame) {
		oldFrame.dispose();
		LoginFrame lf = new LoginFrame(this);
		lf.setVisible(true);
	}	
	public void OpenRegisterForm(JFrame oldFrame) {
		oldFrame.dispose();
		RegisterFrame rf = new RegisterFrame(this);
		rf.setVisible(true);
	}		
	public void OpenCredentialsForm(JFrame oldFrame) {
		oldFrame.dispose();
		CredentialFrame cf = new CredentialFrame(this);
		cf.setVisible(true);
	}	
	public void OpenMainFrame(JFrame oldFrame) throws SQLException {
		oldFrame.dispose();
		MainFrame rf = new MainFrame(this);
		rf.setVisible(true);
	}
	public void LogUser(UserEntity user) {
		loggedUser = user;
	}
	public UserEntity getLoggedUser() {
		return loggedUser;
	}

}
