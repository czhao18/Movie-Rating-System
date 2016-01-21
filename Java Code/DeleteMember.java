package Zhao;//Xiaolang Wang, 2014/12/01, CS425_final, Delete_Member

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class DeleteMember extends JFrame implements ActionListener{

	//variables
	private static final long serialVersionUID = 1L;
	private JButton delete,cancel;
	private JLabel content,notice,selected;
	private JMenuBar bar;
	private JMenu menu;
	private JPanel panel;
	private String temp;
	
	private JMenuItem [] itemArray = new JMenuItem [4000];
	private strList members = new strList(4000);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	
	//action
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("delete")){			
			if(selected.getText().equals("(member selected will be shown here)")){
				 JOptionPane frame=new JOptionPane();
		      	 JOptionPane.showMessageDialog(frame,
		      			    "You haven't selected a member yet!",
		      			    "No Selected Member Error",
		      			    JOptionPane.WARNING_MESSAGE);
			}
			
			else{
				boolean hasBook = false;
				
				String ID = selected.getText().substring(1,7);
				String select = "select userID from BOOK";
				
				try {
					ps = conn.prepareStatement(select);
					rs = ps.executeQuery();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				try {
					while(rs.next()){
						if(rs.getString("userID").equals(ID)){
							hasBook = true;
							JOptionPane frame=new JOptionPane();
						  	  JOptionPane.showMessageDialog(frame,
						  			    "This member has booked a ticket!",
						  			    "Booked User Error",
						  			    JOptionPane.WARNING_MESSAGE);
						  	  this.dispose();
						  	  new DeleteMember();
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
				if(hasBook == false){
					String delete = "delete from USERS where USERID = '"+ID+"'";
					try {
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					this.dispose();
					try {
						new MemberInfo();
					} catch (SQLException e) {
						e.printStackTrace();
					}	
				}
				
			}	
		}
		
		
		if(ae.getActionCommand().equals("cancel")){
			this.dispose();
			try {
				new MemberInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	//constructor
	public DeleteMember() throws SQLException {
		
		conn = StaffMain.getConnection();
		
		this.setTitle("Delete a Member");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		delete = StaffMain.initialButton("delete", null, Color.black, true, this);
		cancel = StaffMain.initialButton("cancel", null, Color.black, true, this);
		selected = new JLabel("(member selected will be shown here)");
		
		content = new JLabel("Please choose an existing member to delete.");
		notice = new JLabel("Notice: you can't delete a member if he bought a ticket!");
		
		//Menu
		menu = new JMenu("Member");
		bar = new JMenuBar();
		bar.add(menu);
		
		//add Items to List
		String select = "select USERID,UNAME from USERS where userClass = 'member'";
		ps = conn.prepareStatement(select);
		rs = ps.executeQuery();
		while(rs.next()){
			members.insert(rs.getString("userID")+"|"+rs.getString("uName"));
		}
		
		for(int i=0;i<members.getIndex();i++){
			itemArray[i] = new JMenuItem(members.getElement(i));
			menu.add(itemArray[i]);
			
			final JMenuItem tempItem = itemArray[i];
			tempItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					temp = tempItem.getText();
					selected.setText("("+temp+")");
				}
			});
		}
		
		

		
		//layout
		GroupLayout aLayout = new GroupLayout(panel);
		panel.setLayout(aLayout);
		
		aLayout.setAutoCreateGaps(true);
		aLayout.setAutoCreateContainerGaps(true);
		
		aLayout.setHorizontalGroup(
			aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(content)
				.addComponent(notice)
				.addGroup(aLayout.createSequentialGroup()
					.addComponent(bar)
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(selected,0,300,500)
						.addGroup(aLayout.createSequentialGroup()
							.addComponent(delete)
							.addComponent(cancel)))));
		
		aLayout.setVerticalGroup(
			aLayout.createSequentialGroup()
				.addComponent(content)
				.addComponent(notice)
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(bar)
					.addComponent(selected))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(delete)
					.addComponent(cancel)));
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		layout.setHorizontalGroup(
	            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	            .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	        );

		layout.setVerticalGroup(
	            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	            .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	        );	
	        
		pack();
		
		setLocationRelativeTo(null);
		this.setVisible(true);		
	
	}
	
	public static void main(String[] args) throws SQLException {
		
		new DeleteMember();

	}


}
