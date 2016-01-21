package Zhao;//Xiaolang Wang, 2014/12/05, CS402_final, Update_Schedule

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

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


public class UpdateSchedule extends JFrame implements ActionListener{
	
	//variables
	private static final long serialVersionUID = 1L;
	private JLabel content,notice1,notice2,updating,
				   time,year,month,day,hour,minute,selectedSchedule,selectedRoom,selectedMovie;
	private JMenuBar barSchedule,barRoom,barMovie;
	private JMenu scheduleList,movieList,roomList;
	private JPanel panel;
	private String tempSchedule,tempMovie,tempRoom;
	private JTextPane textYear,textMonth,textDay,textHour,textMinute;
	private JButton update,cancel;
			
	private JMenuItem [] movieArray = new JMenuItem [4000];
	private JMenuItem [] scheduleArray = new JMenuItem [4000];
	private JMenuItem [] roomArray = new JMenuItem [4000];
	private strList movies = new strList(4000);
	private strList schedules = new strList(4000);
	private strList rooms = new strList(4000);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;	

	//action
		public void actionPerformed(ActionEvent ae) {
			if(ae.getActionCommand().equals("update")){
				if(selectedSchedule.getText().equals("(Schedule selected will be shown here)")){
					 JOptionPane frame=new JOptionPane();
			      	 JOptionPane.showMessageDialog(frame,
			      			    "You haven't selected a schedule yet!",
			      			    "No Selected Schedule Error",
			      			    JOptionPane.WARNING_MESSAGE);
				}
				else if(isEmpty() && !(textYear.getText().equals("") && 
						textMonth.getText().equals("") && textDay.getText().equals("") &&
						textHour.getText().equals("") && textMinute.getText().equals(""))){
					 JOptionPane frame=new JOptionPane();
			      	 JOptionPane.showMessageDialog(frame,
			      			    "You have to fill all time blanks if you want to update time!",
			      			    "Empty Error",
			      			    JOptionPane.WARNING_MESSAGE);
				}
				else{
					String [] info = selectedSchedule.getText().split("\\|");
					
					if(!selectedRoom.getText().equals("(new room for this schedule)")){
						String [] roomInfo = selectedRoom.getText().split("\\|");
						
						String update = "update SCHEDULE set RNUMBER = "
								+ "'"+roomInfo[0].substring(6)+"' where "
								+ "SCHEDULEID = '"+info[0].substring(1)+"'";
						try {
							ps = conn.prepareStatement(update);
							ps.executeUpdate(update);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					if(!selectedMovie.getText().equals("(new movie for this schedule)")){
						String [] movieInfo = selectedMovie.getText().split("\\|");
						//update title and release year
						String update = "update SCHEDULE set TITLE = "
								+ "'"+movieInfo[0].substring(1)+"', RELEASEYEAR = '"+movieInfo[1]+"'"
								+ " where SCHEDULEID = '"+info[0].substring(1)+"'";
						try {
							ps = conn.prepareStatement(update);
							ps.executeUpdate(update);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					//update time
					if(!isEmpty()){
						if(!isYear(textYear.getText()) || !isMonth(textMonth.getText()) ||
						   !isDay(textDay.getText()) || !isHour(textHour.getText()) ||
						   !isMinute(textMinute.getText())){
							JOptionPane frame=new JOptionPane();
						  	  JOptionPane.showMessageDialog(frame,
						  			    "At least one time blank is not correctly filled!",
						  			    "Invalid Time Error",
						  			    JOptionPane.WARNING_MESSAGE);
								}
						else{
							String update = "update SCHEDULE set MOVIETIME ="
									+ "to_timestamp('"+createTime()+"','yyyy-mm-dd hh24:mi:ss')";
							try {
								ps = conn.prepareStatement(update);
								ps.executeUpdate(update);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						}	
					}
					this.dispose();
					try {
						new ScheduleInfo();
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
		
		
		
		//helping methods
		// isEmpty
		public boolean isEmpty(){
			boolean check = true;
			if(!textYear.getText().equals("")){
				if(!textMonth.getText().equals("")){
					if(!textDay.getText().equals("")){
						if(!textHour.getText().equals("")){
							if(!textMinute.getText().equals("")){
								check = false;
							}
						}
					}
				}
			}
			return check;
		}
		
		//check time numbers
		public static boolean isYear(String str){
			boolean check = true;
			if(str.length() != 4){
				check = false;
			}
			
			for(int i=0;i<str.length();i++){
				if(Character.isDigit(str.charAt(i)) != true){
					check = false;
					}
				}
		
			return check;	
		}
		
		public static boolean isMonth(String str){
			boolean check = true;
			if (str.length() >2){
				check = false;
			}
			for(int i=0;i<str.length();i++){
				if(Character.isDigit(str.charAt(i)) != true){
					check = false;
					}
				}
			if (check = true){
				if(Integer.parseInt(str) > 12 || Integer.parseInt(str) < 1){
					check = false;
				}
			}
			return check;		
		}
		
		public static boolean isDay(String str){
			boolean check = true;
			if (str.length() >2){
				check = false;
			}
			for(int i=0;i<str.length();i++){
				if(Character.isDigit(str.charAt(i)) != true){
					check = false;
					}
				}
			if (check = true){
				if(Integer.parseInt(str) > 31 || Integer.parseInt(str) < 1){
					check = false;
				}
			}
			return check;		
		}
		
		public static boolean isHour(String str){
			boolean check = true;
			if (str.length() >2){
				check = false;
			}
			for(int i=0;i<str.length();i++){
				if(Character.isDigit(str.charAt(i)) != true){
					check = false;
					}
				}
			if (check = true){
				if(Integer.parseInt(str) > 24 || Integer.parseInt(str) < 1){
					check = false;
				}
			}
			return check;		
		}
		
		public static boolean isMinute(String str){
			boolean check = true;
			if (str.length() >2){
				check = false;
			}
			for(int i=0;i<str.length();i++){
				if(Character.isDigit(str.charAt(i)) != true){
					check = false;
					}
				}
			if (check = true){
				if(Integer.parseInt(str) > 60 || Integer.parseInt(str) < 0){
					check = false;
				}
			}
			return check;		
		}
		
		//create timeStamp
		public String createTime(){
			DecimalFormat df = new DecimalFormat("00");
			
			String timeStamp = "";
			timeStamp += textYear.getText() + "-" 
						+ df.format(Integer.parseInt(textMonth.getText()))+ "-" 
						+ df.format(Integer.parseInt(textDay.getText()))+ " "
						+ df.format(Integer.parseInt(textHour.getText()))+ ":" 
						+ df.format(Integer.parseInt(textMinute.getText()))+ ":00" ;
			
			return timeStamp;
		}
	
	//constructor
	public UpdateSchedule() throws SQLException {
		conn = StaffMain.getConnection();
		
		this.setTitle("Update Schedule Information");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		update = StaffMain.initialButton("update", null, Color.black, true, this);
		cancel = StaffMain.initialButton("cancel", null, Color.black, true, this);
		selectedSchedule = new JLabel("(Schedule selected will be shown here)");
		
		content = new JLabel("Please choose an existing schedule to update.");
		notice1 = new JLabel("Notice: you can update selected schedule's movie, room and time!");
		notice2 = new JLabel("Notice: When updating time you need to fulfill all time blanks!");
		
		selectedMovie = new JLabel("(new movie for this schedule)");
		selectedRoom = new JLabel("(new room for this schedule)");
		
		updating = new JLabel("Update>>>>>>>>>");
		time = new JLabel("Time");
		year = new JLabel("Year");
		month = new JLabel("Month");
		day = new JLabel("Day");
		hour = new JLabel("Hour");
		minute = new JLabel("Minute");
		
		textYear = new JTextPane();
		textMonth = new JTextPane();
		textDay = new JTextPane();
		textHour = new JTextPane();
		textMinute = new JTextPane();
		
		//Menu
		scheduleList = new JMenu("Schedule");
		movieList = new JMenu("Movie");
		roomList = new JMenu("Room");
		barSchedule = new JMenuBar();
		barMovie = new JMenuBar();
		barRoom = new JMenuBar();
		barSchedule.add(scheduleList);
		barMovie.add(movieList);
		barRoom.add(roomList);
		
		//add Items to ScheduleList
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
					selectedSchedule.setText("("+tempSchedule+")");
				}
			});
		}
		//add Items to RoomList
		select = "select * from ROOM";
		ps = conn.prepareStatement(select);
		rs = ps.executeQuery();
		
		while(rs.next()){
			rooms.insert("Room "+rs.getString("RNUMBER")+"|"+rs.getString("CAPACITY"));
		}
		
		for(int i=0;i<rooms.getIndex();i++){
			roomArray[i] = new JMenuItem(rooms.getElement(i));
			roomList.add(roomArray[i]);
			
			final JMenuItem tempItem = roomArray[i];
			tempItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					tempRoom = tempItem.getText();
					selectedRoom.setText("("+tempRoom+")");
				}
			});
		}

		//add Items to MovieList
		select = "select * from MOVIE";
		ps = conn.prepareStatement(select);
		rs = ps.executeQuery();
		
		while(rs.next()){
			movies.insert(rs.getString("TITLE")+"|"+rs.getString("RELEASEYEAR")
					+"|"+rs.getString("LASTING")+"mins|"+rs.getString("PGRATE"));
		}
		
		for(int i=0;i<movies.getIndex();i++){
			movieArray[i] = new JMenuItem(movies.getElement(i));
			movieList.add(movieArray[i]);
			
			final JMenuItem tempItem = movieArray[i];
			tempItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					tempMovie = tempItem.getText();
					selectedMovie.setText("("+tempMovie+")");
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
				.addComponent(notice1)
				.addComponent(notice2)
				.addGroup(aLayout.createSequentialGroup()
					.addComponent(barSchedule)
					.addComponent(selectedSchedule))
				.addComponent(updating)
				.addGroup(aLayout.createSequentialGroup()
					.addComponent(barRoom)
					.addComponent(selectedRoom))
				.addGroup(aLayout.createSequentialGroup()
					.addComponent(barMovie)
					.addComponent(selectedMovie))
				.addComponent(time)
				.addGroup(aLayout.createSequentialGroup()
					.addComponent(year)	
					.addComponent(textYear)
					.addComponent(month)
					.addComponent(textMonth)
					.addComponent(day)
					.addComponent(textDay))
				.addGroup(aLayout.createSequentialGroup()
					.addComponent(hour)	
					.addComponent(textHour)
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addGroup(aLayout.createSequentialGroup()
							.addComponent(minute)
							.addComponent(textMinute))
						.addGroup(aLayout.createSequentialGroup()
							.addComponent(update)
							.addComponent(cancel)))));
		
		aLayout.setVerticalGroup(
				aLayout.createSequentialGroup()
					.addComponent(content)
					.addComponent(notice1)
					.addComponent(notice2)
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(barSchedule)
						.addComponent(selectedSchedule))
					.addComponent(updating)
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(barRoom)
						.addComponent(selectedRoom))
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(barMovie)
						.addComponent(selectedMovie))
					.addComponent(time)
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(year)
						.addComponent(textYear)
						.addComponent(month)
						.addComponent(textMonth)
						.addComponent(day)
						.addComponent(textDay))
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(hour)	
						.addComponent(textHour)
						.addComponent(minute)
						.addComponent(textMinute))
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
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
		
		new UpdateSchedule();

	}

}
