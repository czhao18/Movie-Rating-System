package Zhao;

//Xiaolang Wang, 2014/11/30, CS425_final, Add_Member

import java.awt.Color;
import java.awt.HeadlessException;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;


public class AddMember extends JFrame implements ActionListener{
	
	//variables
	
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JButton OK,cancel;
	private JLabel ID,name,content,notice;
	private JTextPane textID, textName;
	
	private Connection conn = null;
	private PreparedStatement ps = null;

	
	//action
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("OK")){
			
			if(textID.getText().equals("") || textName.getText().equals("")){
				 JOptionPane frame=new JOptionPane();
		      	  JOptionPane.showMessageDialog(frame,
		      			    "Each blank must be filled!",
		      			    "Empty Error",
		      			    JOptionPane.WARNING_MESSAGE);
			} 
			else{
				try {
					if(!isAvailable(textID.getText())){
						JOptionPane frame=new JOptionPane();
					  	  JOptionPane.showMessageDialog(frame,
					  			    "The entered ID is not available!",
					  			    "Invalid ID Error",
					  			    JOptionPane.WARNING_MESSAGE);
					  	  
					}
					else{
						String add = "insert into USERS values('"+textID.getText()+"'"
								+ ",'"+textName.getText()+"','member')";
						try {
							ps = conn.prepareStatement(add);
							ps.executeUpdate(add);
						} catch (SQLException e) {
							e.printStackTrace();
							} 
						
						this.dispose();
						new MemberInfo();
					}
					
				} catch (HeadlessException | SQLException e) {
					e.printStackTrace();
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
	
	
	//checkID
	public static boolean isAvailable(String str) throws SQLException{
		
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		Connection conn1 = StaffMain.getConnection();
		
		String select = "select userID from USERS where userClass = 'member'";
		ps1 = conn1.prepareStatement(select);
		rs1 = ps1.executeQuery();
		
		boolean check = true;
		if(str.length() != 6){
			check = false;
			}
		for(int i=0;i<str.length();i++){
			if(Character.isDigit(str.charAt(i)) != true){
				check = false;
				}
			}
		while(rs1.next()){
			if(str.equals(rs1.getString("USERID"))){
				check = false;
				}
			}
		return check;
        }

	//Constructor
	public AddMember() throws SQLException {
		
		conn = StaffMain.getConnection();

		this.setTitle("Add a Member");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		content = new JLabel("Please enter information for a member");
		notice = new JLabel("Notice: ID must 6-digit long, both blanks must be filled!");
		ID = new JLabel("ID");
		name = new JLabel("name");
		
		OK = StaffMain.initialButton("OK",null,Color.black,true,this);
		cancel = StaffMain.initialButton("cancel",null,Color.black,true,this);
		
		textID = new JTextPane();
		textName = new JTextPane();
		
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
					 .addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
							 .addComponent(ID)
							 .addComponent(name))
					 .addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
							 .addComponent(textID)
							 .addComponent(textName)
							 .addGroup(aLayout.createSequentialGroup()
									 .addComponent(OK)
									 .addComponent(cancel)))));

		aLayout.setVerticalGroup(
			aLayout.createSequentialGroup()
				.addComponent(content)
				.addComponent(notice)
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(ID)
					 .addComponent(textID))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(name)
					 .addComponent(textName))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(OK)
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
		
		new AddMember();

	}

}
