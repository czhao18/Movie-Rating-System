package Zhao;//Xiaolang Wang, 2014/12/05, CS425_final, Update_Member

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
import javax.swing.JTextPane;


public class UpdateMember extends JFrame implements ActionListener  {
	
	//variables
	private static final long serialVersionUID = 1L;
	private JLabel updating,content,notice,selected,name;
	private JMenuBar bar;
	private JMenu memberList;
	private JPanel panel;
	private String temp;
	private JTextPane textName;
	private JButton update,cancel;
	
	private JMenuItem [] memberArray = new JMenuItem [4000];
	private strList members = new strList(4000);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	//action
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("update")){
			if(selected.getText().equals("(member selected will be shown here)")){
				 JOptionPane frame=new JOptionPane();
		      	 JOptionPane.showMessageDialog(frame,
		      			    "You haven't selected a member yet!",
		      			    "No Selected Member Error",
		      			    JOptionPane.WARNING_MESSAGE);
			}
			else {
				if(!textName.getText().equals("")){
					String [] info = selected.getText().split("\\|");
				
					String update ="update USERS set UNAME ='"+textName.getText()+"'"
						+ " where USERID = '"+info[0].substring(1)+"'";
				
					try {
						ps = conn.prepareStatement(update);
						ps.executeUpdate(update);
					} catch (SQLException e) {
						e.printStackTrace();
					}				
				}
			
				this.dispose();
				try {
					new MemberInfo();
				} catch (SQLException e) {
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
	
	
	//constructor
	public UpdateMember() throws SQLException {
		
		conn = StaffMain.getConnection();
		
		this.setTitle("Update Member Information");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		update = StaffMain.initialButton("update", null, Color.black, true, this);
		cancel = StaffMain.initialButton("cancel", null, Color.black, true, this);
		selected = new JLabel("(member selected will be shown here)");
		
		content = new JLabel("Please choose an existing member to update.");
		notice = new JLabel("Notice: you can update selected member's name only!");
		updating = new JLabel("Update>>>>>>>>>");
		
		name = new JLabel("name");
		textName = new JTextPane();
		
		//Menu
		memberList = new JMenu("Member");
		bar = new JMenuBar();
		bar.add(memberList);
		
		//add Items to memberList
		
		String select = "select USERID,UNAME from USERS where userClass = 'member'";
		ps = conn.prepareStatement(select);
		rs = ps.executeQuery();
		while(rs.next()){
			members.insert(rs.getString("userID")+"|"+rs.getString("uName"));
		}
		
		for(int i=0;i<members.getIndex();i++){
			memberArray[i] = new JMenuItem(members.getElement(i));
			memberList.add(memberArray[i]);
			
			final JMenuItem tempItem = memberArray[i];
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
						.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(updating)	
							.addComponent(bar)	
							.addComponent(name))
						.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
							.addComponent(selected,0,300,500)
							.addComponent(textName)
							.addGroup(aLayout.createSequentialGroup()
								.addComponent(update)
								.addComponent(cancel)))));		
		
		
		aLayout.setVerticalGroup(
				aLayout.createSequentialGroup()
					.addComponent(content)
					.addComponent(notice)
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(bar)
						.addComponent(selected))
					.addComponent(updating)	
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(name)
						.addComponent(textName))
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(update)
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
		
		new UpdateMember();

	}

	


}
