package note;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.DatabaseHelper;
import note.Entity.NoteEntity;
import note.Entity.UserEntity;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtHead;
	private JTextArea txtContent;
	private JTable table;
	private JTextField findUserField;

	private ArrayList<UserEntity> foundUsers = new ArrayList<UserEntity>();
	private List<UserEntity> collabList;
	private ArrayList<NoteEntity> foundNotes = new ArrayList<NoteEntity>();
	private NoteEntity selectedNote = null;
	private MainFrame instance;
	
	public MainFrame(ProgramMain pm) throws SQLException {
		
		instance = this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 560);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(26, 26, 26));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtContent = new JTextArea();
		txtContent.setBounds(22, 78, 483, 387);
		contentPane.add(txtContent);
		
		txtHead = new JTextField();
		txtHead.addKeyListener(new java.awt.event.KeyAdapter() {
		    public void keyTyped(java.awt.event.KeyEvent evt) {
		        if(txtHead.getText().length()>=45 &&!(evt.getKeyChar()==KeyEvent.VK_DELETE||evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)) {
		            getToolkit().beep();
		            evt.consume();
		         }
		     }
		});
		txtHead.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		txtHead.setText("Headline");
		txtHead.setBounds(22, 22, 345, 46);
		contentPane.add(txtHead);
		txtHead.setColumns(10);
		
		DefaultTableModel defTable = new DefaultTableModel();
		defTable.addColumn("NoteName");
		defTable.addColumn("CollabName");
	
		
		
		table = new JTable(defTable);
		table.setEnabled(false);
		table.setBounds(560, 41, 285, 106);
		contentPane.add(table);
		
		JLabel lblNewLabel = new JLabel("Collabs:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(560, 11, 104, 24);
		contentPane.add(lblNewLabel);
		

		DefaultComboBoxModel<String> cb = new DefaultComboBoxModel<String>();

		JComboBox comboBox = new JComboBox(cb);
		comboBox.setBounds(560, 248, 285, 39);
		contentPane.add(comboBox);
		
		JLabel lblUsers = new JLabel("Users:");
		lblUsers.setForeground(Color.WHITE);
		lblUsers.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblUsers.setBounds(560, 175, 104, 24);
		contentPane.add(lblUsers);
		
		findUserField = new JTextField();
		findUserField.setBounds(560, 208, 198, 29);
		contentPane.add(findUserField);
		findUserField.setColumns(10);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cb.removeAllElements();
				foundUsers.clear();
				
				try {
					ResultSet rs = DatabaseHelper.instance.SearchUser(findUserField.getText());
					while(rs.next()) {
						UserEntity newUser = new UserEntity(rs.getInt("user_id"),
								                              rs.getString("user_nick"),
								                              rs.getString("user_mail"));
						
						if(newUser.getNick().compareTo(pm.getLoggedUser().getNick()) == 0)
							continue;
						
						
						foundUsers.add(newUser);
						cb.addElement(newUser.getNick());
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSearch.setForeground(Color.WHITE);
		btnSearch.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnSearch.setBackground(Color.LIGHT_GRAY);
		btnSearch.setBounds(768, 207, 77, 30);
		contentPane.add(btnSearch);
		
		JButton btnAddCollab = new JButton("Add Collab");
		btnAddCollab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(selectedNote == null)
				{
					JOptionPane.showMessageDialog(null, "Please Save or Select A Note first!");
					return;
				}
				if(cb.getSelectedItem() == null)
					return;
				UserEntity collab = null;
				
				String usernick = cb.getSelectedItem().toString();
				if(usernick.isEmpty() || usernick.isBlank())
					return;

				Optional<UserEntity> u = 
						foundUsers.stream().filter(user -> user.getNick().equals(usernick)).findFirst();

				for (int i = 0; i < defTable.getRowCount(); i++)
					if(defTable.getValueAt(i, 1).toString().equals(usernick)) {
						JOptionPane.showMessageDialog(null, "This collabrator is already added!");
						return;						
					}
				
				selectedNote.addCollab(u.get());
				collab = u.get();
				collabList.add(u.get());
				String[] row = new String[2];
				row[0] = (defTable.getRowCount() + 1) + "";
				row[1] = collab.getNick();
				defTable.addRow(row);
				
			}
		});
		btnAddCollab.setForeground(Color.WHITE);
		btnAddCollab.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnAddCollab.setBackground(Color.LIGHT_GRAY);
		btnAddCollab.setBounds(712, 298, 133, 39);
		contentPane.add(btnAddCollab);
		
		JButton btnRemoveCollab = new JButton("Remove Collab");
		btnRemoveCollab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(selectedNote == null)
				{
					JOptionPane.showMessageDialog(null, "Please Save or Select A Note first!");
					return;
				}

				if(cb.getSelectedItem() == null)
				{
					JOptionPane.showInternalMessageDialog(null, "You didn't choose a user!");
					 return;
				}
				
				String usernick = cb.getSelectedItem().toString();
				
				if(usernick.isEmpty() || usernick.isBlank())
					return;


				UserEntity[] discards = selectedNote.getDiscardedCollabs();				
				for (int i = 0; i < discards.length; i++)
					if(discards[i].getNick().equals(usernick)) {
						JOptionPane.showInternalMessageDialog(null, "You already discard this Collabrator!");
						return;
					}

				Optional<UserEntity> u = collabList.stream().filter(user -> user.getNick().equals(usernick)).findFirst();
				if(!u.isPresent())
				{
					JOptionPane.showMessageDialog(null, "This user is not a collabrator!");
					return;
				}
				UserEntity ue = u.get();
				System.out.println(ue.getNick());
				
				selectedNote.discardCollab(ue);
				if(collabList.contains(u.get()))
					collabList.remove(u.get());
				
				for (int i = 0; i < defTable.getRowCount(); i++) {

				System.out.println("ROW USER: "+ defTable.getValueAt(i, 1).toString());
					
					if(defTable.getValueAt(i, 1).toString().compareTo(usernick) == 0) {
						defTable.removeRow(i);						
						return;
					}
					
				}
			}
		});
		btnRemoveCollab.setForeground(Color.WHITE);
		btnRemoveCollab.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnRemoveCollab.setBackground(Color.LIGHT_GRAY);
		btnRemoveCollab.setBounds(560, 298, 133, 39);
		contentPane.add(btnRemoveCollab);
		
		DefaultComboBoxModel<NoteEntity> cbNotes = new DefaultComboBoxModel<NoteEntity>();
		
		JComboBox comboBox_1 = new JComboBox(cbNotes);
		NoteEntityRenderer renderer = new NoteEntityRenderer();
		comboBox_1.setRenderer(renderer);
		comboBox_1.setBounds(560, 471, 285, 39);
		contentPane.add(comboBox_1);

		GetNotes(cbNotes);
		
		JButton btnSelectNote = new JButton("Select Note");
		btnSelectNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					NoteEntity ne = (NoteEntity)cbNotes.getSelectedItem();					
					try {
						SelectNote(ne, defTable);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		btnSelectNote.setForeground(Color.WHITE);
		btnSelectNote.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnSelectNote.setBackground(Color.LIGHT_GRAY);
		btnSelectNote.setBounds(712, 426, 133, 39);
		contentPane.add(btnSelectNote);
		
		
		JButton btnSaveText = new JButton("Save");
		btnSaveText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
				
					String headline = txtHead.getText();
					if(headline.isBlank() || headline.isEmpty())
						headline = "Unnamed.";
					
					int collabCount = defTable.getRowCount();
					UserEntity[] collabs = new UserEntity[collabCount];
					for (int i = 0; i < collabCount; i++) {
						UserEntity u = null;
						ResultSet rs = DatabaseHelper.instance.SearchUser(defTable.getValueAt(i, 1).toString());
						if(rs.next()) {
							u = new UserEntity(rs.getInt("user_id"), rs.getString("user_nick"), rs.getString("user_mail"));
						}
						collabs[i] = u;
						System.out.println(u.getNick());
					}
					
					
					if(selectedNote != null) {
						selectedNote.setNoteHead(headline);
						selectedNote.setNoteContent(txtContent.getText());
						selectedNote.setCollabs(collabs);

						System.out.println("SelectedNote: NOT NULL");
						DatabaseHelper.instance.SaveText(selectedNote);
					}
					else {			

						System.out.println("SelectedNote: NULL");
						selectedNote = DatabaseHelper.instance.SaveNewText(headline, txtContent.getText(), collabs);	

						collabList = new ArrayList<UserEntity>();
					}
					GetNotes(cbNotes);
				
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		btnSaveText.setForeground(new Color(255, 255, 255));
		btnSaveText.setBackground(new Color(192, 192, 192));
		btnSaveText.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnSaveText.setBounds(363, 471, 142, 39);
		contentPane.add(btnSaveText);
		
		JButton btnCreateNote = new JButton("Create New Note");
		btnCreateNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtHead.setText("Headline");
				txtContent.setText("");
				selectedNote = null;
				defTable.setRowCount(0);
			}
		});
		btnCreateNote.setForeground(Color.WHITE);
		btnCreateNote.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnCreateNote.setBackground(Color.LIGHT_GRAY);
		btnCreateNote.setBounds(377, 22, 133, 45);
		contentPane.add(btnCreateNote);
		
		JLabel lblNotes = new JLabel("Notes:");
		lblNotes.setForeground(Color.WHITE);
		lblNotes.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		lblNotes.setBounds(560, 441, 104, 24);
		contentPane.add(lblNotes);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProgramMain.Instance.LogUser(null);
				ProgramMain.Instance.OpenCredentialsForm(instance);
			}
		});
		btnExit.setForeground(Color.WHITE);
		btnExit.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnExit.setBackground(Color.LIGHT_GRAY);
		btnExit.setBounds(22, 471, 142, 39);
		contentPane.add(btnExit);
		
		JButton btnRemoveNote = new JButton("Remove Note");
		btnRemoveNote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedNote == null) {
					JOptionPane.showMessageDialog(null, "Please Select A Note First!");
					return;
				}
				try {
					DatabaseHelper.instance.DeleteText(selectedNote);
					GetNotes(cbNotes);
					SelectNote(cbNotes.getElementAt(0), defTable);
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnRemoveNote.setForeground(Color.WHITE);
		btnRemoveNote.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		btnRemoveNote.setBackground(Color.LIGHT_GRAY);
		btnRemoveNote.setBounds(712, 382, 133, 39);
		contentPane.add(btnRemoveNote);
		
	}
	private void SelectNote(NoteEntity ne, DefaultTableModel collabModel) throws SQLException {
		selectedNote = ne;
		txtHead.setText(selectedNote.getNoteHead());
		txtContent.setText(selectedNote.getNoteContent());
		
		collabModel.setRowCount(0);
		
		UserEntity[] collabs = DatabaseHelper.instance.GetNoteCollabs(selectedNote.getNoteID());
		for (int i = 0; i < collabs.length; i++) {
			String[] row = {(collabModel.getRowCount() + 1) + "", collabs[i].getNick()};
			collabModel.addRow(row);
		}
		
		collabList = new ArrayList<UserEntity>(Arrays.asList(collabs));
	}
	public void GetNotes(DefaultComboBoxModel cb) throws SQLException {
		cb.removeAllElements();
		foundNotes.clear();
		NoteEntity[] notes = DatabaseHelper.instance.GetUserNotes();
		for (int i = 0; i < notes.length; i++) {
			foundNotes.add(notes[i]);
			cb.addElement(notes[i]);
		}
	}
}

class NoteEntityRenderer extends JLabel implements ListCellRenderer<NoteEntity> {
    @Override
    public Component getListCellRendererComponent(JList<? extends NoteEntity> list, NoteEntity value, int index, boolean isSelected, boolean cellHasFocus) {
    	if(value == null) return this;
        setText(value.getNoteHead());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setOpaque(true);
        return this;
    }
}

