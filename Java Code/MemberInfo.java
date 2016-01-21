package Zhao;//Xiaolang Wang, 2014/11/30, CS425_FINAL, Member_info

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



public class MemberInfo extends JFrame implements ActionListener {
	
	//variables
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JButton add,delete,update,back;
	private JLabel content;
	private JScrollPane list;
	
	private strList members = new strList(4000);
	private JTextArea text;
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	//action
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("add")){
			this.dispose();
			try {
				new AddMember();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		
		if(ae.getActionCommand().equals("delete")){
			this.dispose();
			try {
				new DeleteMember();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		if(ae.getActionCommand().equals("update")){
			this.dispose();
			try {
				new UpdateMember();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		

		if(ae.getActionCommand().equals("back")){
			this.dispose();
			new StaffMain();
			}	
		}

	public MemberInfo() throws SQLException {
		
		conn = StaffMain.getConnection();

		this.setTitle("Manage Memeber Information");
		
		panel = new JPanel();
		
		add = StaffMain.initialButton("add",null,Color.black,true,this);
		delete = StaffMain.initialButton("delete",null,Color.black,true,this);
		update = StaffMain.initialButton("update",null,Color.black,true,this);
		back = StaffMain.initialButton("back",null,Color.black,true,this);
		
		content = new JLabel("Please choose what to do you want to do.");
		
		//fulfill the JScrollPane
		String select = "select USERID,UNAME from USERS where userClass = 'member'";
		ps = conn.prepareStatement(select);
		rs = ps.executeQuery();
		while(rs.next()){
			String info = rs.getString("USERID") + "|" + rs.getString("UNAME");
			members.insert(info);
		}
		
		
		text = new JTextArea();
		text.setText(members.toDisplay());
		text.setEditable(false);
		
		list = new JScrollPane(text);
		list.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	
		//layout
		GroupLayout aLayout = new GroupLayout(panel);
		panel.setLayout(aLayout);
		
		aLayout.setAutoCreateGaps(true);
		aLayout.setAutoCreateContainerGaps(true);
		
		aLayout.setHorizontalGroup(
			aLayout.createSequentialGroup()
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(add,0,100,100)
					 .addComponent(delete,0,100,100)
					 .addComponent(update,0,100,100)
					 .addComponent(back,0,100,100))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(content,0,300,600)
					 .addComponent(list,0,350,600)));
			
		aLayout.setVerticalGroup(
			aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(aLayout.createSequentialGroup()
					 .addComponent(add,0,57,57)
					 .addComponent(delete,0,57,57)
					 .addComponent(update,0,57,57)
					 .addComponent(back,0,57,57))
				.addGroup(aLayout.createSequentialGroup()
					 .addComponent(content)
					 .addComponent(list,0,220,220)));
		
		
		
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

		new MemberInfo();
		
	}
	
	
}
