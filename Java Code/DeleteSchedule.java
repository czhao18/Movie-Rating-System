package Zhao;//Xiaolang Wang, 2014/12/01, CS425_final, Delete_Schedule

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


public class DeleteSchedule extends JFrame implements ActionListener{

	//variables
	private static final long serialVersionUID = 1L;
	private JButton delete,cancel;
	private JLabel content,notice,selected;
	private JMenuBar bar;
	private JMenu scheduleList;
	private JPanel panel;
	
	private JMenuItem [] scheduleArray = new JMenuItem [4000];
	private strList schedules = new strList(4000);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private String tempSchedule;
	
	//action
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("delete")){
			if(selected.getText().equals("(schedule selected will be shown here)")){
				 JOptionPane frame=new JOptionPane();
		      	 JOptionPane.showMessageDialog(frame,
		      			    "You haven't selected a schedule yet!",
		      			    "No Selected Schedule Error",
		      			    JOptionPane.WARNING_MESSAGE);
			}
			else{
				String [] info = selected.getText().split("\\|");
				try {
					if(hasTicket(info[0].substring(1))){
						 JOptionPane frame=new JOptionPane();
				      	 JOptionPane.showMessageDialog(frame,
				      			    "This schedule's ticket is on sale now!",
				      			    "Not Valid Schedule Error",
				      			    JOptionPane.WARNING_MESSAGE);
				      	 }
					else{
						String delete = "delete from SCHEDULE WHERE SCHEDULEID = "
								+ "'"+info[0].substring(1)+"'";
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
						this.dispose();
						new ScheduleInfo();	
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}	
		}
		if(ae.getActionCommand().equals("cancel")){
			this.dispose();
			try {
				new ScheduleInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	//hasTicket
	public static boolean hasTicket(String str) throws SQLException{
		boolean check = false;
		
		Connection conn1 = StaffMain.getConnection();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		strList tickets = new strList(4000);
		
		String select = "select * from TICKET";
		ps1 = conn1.prepareStatement(select);
		rs1 = ps1.executeQuery();
		
		while(rs1.next()){
			tickets.insert(rs1.getString("SCHEDULEID"));
		}
		
		for(int i=0;i<tickets.getIndex();i++){
			if(str.equals(tickets.getElement(i))){
				check = true;
			}
		}
		return check;	
	}
	
	//constructor
	public DeleteSchedule() throws SQLException {
		
		conn = StaffMain.getConnection();
		
		this.setTitle("Delete a Schedule");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		delete = StaffMain.initialButton("delete", null, Color.black, true, this);
		cancel = StaffMain.initialButton("cancel", null, Color.black, true, this);
		selected = new JLabel("(schedule selected will be shown here)");
		
		content = new JLabel("Please choose an existing schedule to delete.");
		notice = new JLabel("Notice: you can't delete a schedule whose ticket is available!");
		
		//Menu
		scheduleList = new JMenu("Schedule");
		bar = new JMenuBar();
		bar.add(scheduleList);
		
		//add Items to List
		String select = "select * from SCHEDULE";
		ps = conn.prepareStatement(select);
		rs = ps.executeQuery();
		while(rs.next()){
			schedules.insert(rs.getString("SCHEDULEID")+"|"+rs.getString("TITLE")+"|"+rs.getString("RELEASEYEAR")
					+"|Room "+rs.getString("RNUMBER")+"|"
					+rs.getString("MOVIETIME").substring(0,rs.getString("MOVIETIME").indexOf(".")));
		}
		
		for(int i=0;i<schedules.getIndex();i++){
			scheduleArray[i] = new JMenuItem(schedules.getElement(i));
			scheduleList.add(scheduleArray[i]);
			
			final JMenuItem tempItem = scheduleArray[i];
			tempItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					tempSchedule = tempItem.getText();
					selected.setText("("+tempSchedule+")");
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
		
		new DeleteSchedule();

	}


}