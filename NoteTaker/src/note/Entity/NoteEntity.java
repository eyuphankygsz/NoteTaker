package note.Entity;

import java.util.ArrayList;
import java.util.Iterator;

public class NoteEntity {

	private int noteID,noteCreatorID;
	private String noteHead, noteContent;
	private UserEntity[] collabs;
	private ArrayList<UserEntity> discartedCollabs = new ArrayList<UserEntity>();
	private ArrayList<UserEntity> addedCollabs= new ArrayList<UserEntity>();
	
	
	public int getNoteID() {
		return noteID;
	}
	public String getNoteHead() {
		return noteHead;
	}
	public void setNoteHead(String noteHead) {
		this.noteHead = noteHead;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	public int getNoteCreator() {
		return noteCreatorID;
	}
	public void setNoteCreator(int noteCreator) {
		this.noteCreatorID = noteCreator;
	}
	public UserEntity[] getCollabs() {
		return collabs;
	}
	public void setCollabs(UserEntity[] collabs) {
		this.collabs = collabs;
	}
	
	public void discardCollab(UserEntity collab) {
		discartedCollabs.add(collab);
		System.out.println("DISCARDED: " + collab.getNick());
		if(addedCollabs.contains(collab)) {			
			System.out.println(collab.getNick() + " Moved to discardeds from addeds.");
			addedCollabs.remove(collab);
		}
	}
	public UserEntity[] getDiscardedCollabs() {
		return discartedCollabs.toArray(new UserEntity[0]);
	}
	public void addCollab(UserEntity collab) {
		addedCollabs.add(collab);
		System.out.println("ADDED: " + collab.getNick());

		if(discartedCollabs.contains(collab)) {
			System.out.println(collab.getNick() + " Moved to addeds from discardeds.");
			discartedCollabs.remove(collab);
		}
	}
	public UserEntity[] getAddedCollabs() {
		return addedCollabs.toArray(new UserEntity[0]);
	}
	public void resetCollabs() {
		discartedCollabs.clear();
		addedCollabs.clear();
	}
	public NoteEntity(int id, String head, String content, int creator, UserEntity[] collabs) {
		noteID = id;
		noteHead = head;
		noteContent = content;
		noteCreatorID = creator;
		this.collabs = collabs;
	}
}
