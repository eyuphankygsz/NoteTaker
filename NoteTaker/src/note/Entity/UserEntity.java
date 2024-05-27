package note.Entity;

public class UserEntity {
	private int id;
	private String nick, mail;


	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getId() {
		return id;
	}
	public UserEntity(int id, String nick, String mail) {
		this.id = id;
		this.nick = nick;
		this.mail = mail;
	}
}
