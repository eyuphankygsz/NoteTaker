package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import note.ProgramMain;
import note.Entity.NoteEntity;
import note.Entity.UserEntity;

public class DatabaseHelper {
	
	public static DatabaseHelper instance;
	
	public DatabaseHelper() {
		instance = this;
	}
	public Connection GetConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/notes", "root", "admin");
	}
	public ResultSet GetAll(String table) throws SQLException {
		String query = String.format("SELECT * FROM %s",table);
		Statement st = GetConnection().createStatement();
		return st.executeQuery(query);
	}
	public ResultSet SearchUser(String nick) throws SQLException{
		String query = "SELECT * FROM users WHERE user_nick LIKE ?";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setString(1, "%" + nick + "%");
		return ps.executeQuery();
	}
	
	
	
	
	//Text
	public NoteEntity SaveNewText(String headline, String content, UserEntity[] collabs) throws SQLException {
		
		int noteIndex = -1;
		
		String query = "INSERT INTO notes (note_headLine, note_content, note_creator, note_create_date, note_edit_date) VALUES(?, ?, ?, ?, ?)";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setString(1, headline);
		ps.setString(2, content);
		ps.setInt(3, ProgramMain.Instance.getLoggedUser().getId());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date date = new java.util.Date();
		
		ps.setString(4, dateFormat.format(date));
		ps.setString(5, dateFormat.format(date));
		ps.executeUpdate();

		
		ResultSet rs = ps.executeQuery("SELECT LAST_INSERT_ID()");
		rs.next();
		noteIndex = rs.getInt(1);
		
		if(noteIndex == -1) {
			System.out.println("ERROR");
			return null;
		}
		NoteEntity noteEntity = new NoteEntity(noteIndex, headline, content, ProgramMain.Instance.getLoggedUser().getId(), collabs);
		SaveCollabs(noteEntity);

		InsertCollab(ProgramMain.Instance.getLoggedUser(), noteIndex);

		return noteEntity;
	}
	
	public void SaveText(NoteEntity noteEntity) throws SQLException {
		
		String query = "UPDATE notes SET note_headLine = ?, note_content = ?, note_edit_date = ? WHERE note_id = ?";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setString(1, noteEntity.getNoteHead());
		ps.setString(2, noteEntity.getNoteContent());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		java.util.Date date = new java.util.Date();
		System.out.println(dateFormat.format(date));
		ps.setString(3, dateFormat.format(date));
		
		ps.setInt(4, noteEntity.getNoteID());
		ps.executeUpdate();
		
		SaveCollabs(noteEntity);
				
		
	}	
	private void SaveCollabs(NoteEntity noteEntity) throws SQLException {
		
		List<UserEntity> currentCollabs = new ArrayList<UserEntity>(Arrays.asList(noteEntity.getCollabs()));
		
		UserEntity[] discardedCollabs = noteEntity.getDiscardedCollabs();
		for (UserEntity u : discardedCollabs) {
			System.out.println("DISCARD: " + u.getNick());
			currentCollabs.remove(u);
			RemoveCollab(u, noteEntity);
		}

		UserEntity[] addedCollabs = noteEntity.getAddedCollabs();		
		for (UserEntity u : addedCollabs) {
			currentCollabs.add(u);
			InsertCollab(u, noteEntity.getNoteID());
		}
		
		noteEntity.resetCollabs();
		noteEntity.setCollabs(currentCollabs.toArray(new UserEntity[0]));

	}
	
	public void DeleteText(NoteEntity noteEntity) throws SQLException {
		
		RemoveNoteCollabs(noteEntity);
		String query = "DELETE FROM notes WHERE note_id = ?";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		
		UserEntity[] collabs = noteEntity.getCollabs();
		for (int i = 0; i < collabs.length; i++)
			RemoveCollab(collabs[i], noteEntity);

		ps.setInt(1, noteEntity.getNoteID());
		ps.executeUpdate();
		
	}
	
	public NoteEntity[] GetUserNotes() throws SQLException {
		
		String query = "SELECT DISTINCT n.* "
				+ "FROM notes as n "
				+ "INNER JOIN users as u ON n.note_creator = u.user_id "
				+ "INNER JOIN collabrates as c ON n.note_id = c.note_id "
				+ "WHERE u.user_id = ? OR c.collab_id = ? "
				+ "ORDER BY n.note_edit_date DESC";
		
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setInt(1, ProgramMain.Instance.getLoggedUser().getId());
		ps.setInt(2, ProgramMain.Instance.getLoggedUser().getId());
		
		ResultSet rs = ps.executeQuery();
		ArrayList<NoteEntity> notes = new ArrayList<NoteEntity>();
		
		while(rs.next()) {
			UserEntity[] collabs = GetNoteCollabs(rs.getInt("note_id"));
			NoteEntity ne = new NoteEntity(rs.getInt("note_id"), 
					                         rs.getString("note_headLine"),
					                           rs.getString("note_content"), 
					                             rs.getInt("note_creator"),
					                               collabs);
			notes.add(ne);
		}
		
		return notes.toArray(new NoteEntity[0]);
	}	
	
	public UserEntity GetUser(String nick) throws SQLException {
		
		String query = "SELECT * FROM users WHERE user_nick = ?";

		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setString(1, nick);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			UserEntity u = new UserEntity(rs.getInt("user_id"), rs.getString("user_nick"), rs.getString("user_mail"));
			return u;					
		}
		return null;
	}
	
	public void RegisterUser(String nick, String mail, String password) throws SQLException
	{
		
		String query = "INSERT INTO users (user_nick, user_pass, user_mail) values(?, ?, ?)";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setString(1, nick);
		ps.setString(2, password);
		ps.setString(3, mail);
		ps.executeUpdate();
	
	}
	public UserEntity[] GetNoteCollabs(int noteid) throws SQLException{
		
		ArrayList<UserEntity> collabs = new ArrayList<UserEntity>();
		
		String query = "SELECT u.* "
				+ "FROM users as u "
				+ "LEFT JOIN collabrates as c ON u.user_id = c.collab_id "
				+ "WHERE c.note_id = ?";
		
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setInt(1, noteid);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			if(rs.getInt("user_id") == ProgramMain.Instance.getLoggedUser().getId())
				continue;
			UserEntity ue = new UserEntity(rs.getInt("user_id"), rs.getString("user_nick"), rs.getString("user_mail"));
			collabs.add(ue);
		}
		
		return collabs.toArray(new UserEntity[0]);
	}
	
	private void InsertCollab(UserEntity u, int noteIndex) throws SQLException {
		String query = "INSERT INTO collabrates values(?,?)";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setInt(1, u.getId());
		ps.setInt(2, noteIndex);
		ps.executeUpdate();	
	}
	private void RemoveCollab(UserEntity u, NoteEntity n) throws SQLException{
		String query = "DELETE FROM collabrates WHERE collab_id = ? AND note_id = ?";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setInt(1, u.getId());
		ps.setInt(2, n.getNoteID());
		ps.executeUpdate();
	}
	private void RemoveNoteCollabs(NoteEntity n) throws SQLException{
		String query = "DELETE FROM collabrates WHERE note_id = ?";
		PreparedStatement ps = GetConnection().prepareStatement(query);
		ps.setInt(1, n.getNoteID());
		ps.executeUpdate();
	}
	
	
	
	
	//Login
	public Boolean CheckCredentials(String nick, String pass) throws SQLException {
		Connection conn = GetConnection();
		String query = "SELECT * FROM users WHERE user_nick = ? AND user_pass = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, nick);
		ps.setString(2, pass);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {

			System.out.println("FOUND");
			UserEntity loggedUser = new UserEntity(rs.getInt("user_id"),
					                                   rs.getString("user_nick"),
					                                    rs.getString("user_mail"));
			ProgramMain.Instance.LogUser(loggedUser);
			return true;
		}

		System.out.println("NOT FOUND");
		return false;
	}
}
