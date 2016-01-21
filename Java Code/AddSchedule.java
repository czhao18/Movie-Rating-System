package Zhao;//Xiaolang Wang, 2014/12/01, CS425_final, Add_Schedule

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;

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

public class AddSchedule extends JFrame implements ActionListener{
	
	//variables
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JButton OK,cancel;
	private JLabel time,year,month,day,hour,minute,content,notice1, notice2,selectedMovie,selectedRoom;
	private JTextPane textYear,textMonth,textDay,textHour,textMinute;
	private JMenu movieList,roomList;
	private JMenuBar barRoom,barMovie;

	private JMenuItem [] movieArray = new JMenuItem [4000];
	private JMenuItem [] roomArray = new JMenuItem [4000];
	private strList movies = new strList(4000);
	private strList rooms = new strList(4000);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private String tempMovie, tempRoom;
	
	//action
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("OK")){
			if(isEmpty()){
				JOptionPane frame=new JOptionPane();
			  	  JOptionPane.showMessageDialog(frame,
			  			    "At least one blank is not filled!",
			  			    "Empty Error",
			  			    JOptionPane.WARNING_MESSAGE);
			}
			else if(selectedRoom.getText().equals(("(room selected)"))){
				JOptionPane frame=new JOptionPane();
			  	  JOptionPane.showMessageDialog(frame,
			  			    "You have to choose a room for this schedule!",
			  			    "No Selected Room Error",
			  			    JOptionPane.WARNING_MESSAGE);
				}
			else if(selectedMovie.getText().equals(("(movie selected)"))){
				JOptionPane frame=new JOptionPane();
			  	  JOptionPane.showMessageDialog(frame,
			  			    "You have to choose a movie for this schedule!",
			  			    "No Selected Movie Error",
			  			    JOptionPane.WARNING_MESSAGE);
				}
			else if(!isYear(textYear.getText()) || !isMonth(textMonth.getText()) ||
					!isDay(textDay.getText()) || !isHour(textHour.getText()) ||
					!isMinute(textMinute.getText())){
				JOptionPane frame=new JOptionPane();
			  	  JOptionPane.showMessageDialog(frame,
			  			    "At least one time blank is not correctly filled!",
			  			    "Invalid Time Error",
			  			    JOptionPane.WARNING_MESSAGE);
					}
			
			else{
				String ID = "";
				try {
					ID = createID();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				String [] info1 = selectedMovie.getText().split("\\|");
				String [] info2 = selectedRoom.getText().split("\\|");

				String insert = "insert into SCHEDULE values("
						+ "'"+info1[0].substring(1)+"','"+info1[1]+"'"
								+ ",'"+info2[0].substring(5)+"',"
								+ "to_timestamp('"+createTime()+"','yyyy-mm-dd hh24:mi:ss'),'"+ID+"')";
				try {
					ps = conn.prepareStatement(insert);
					ps.executeUpdate(insert);
				} catch (SQLException e) {
					e.printStackTrace();
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
	
	//create a random schedule ID
	public static String createID() throws SQLException{
		Connection conn1 = StaffMain.getConnection();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		strList idList = new strList(4000);
		String ID = "";
		Random rnd = new Random();
		
		String select = "select scheduleID from SCHEDULE";
		ps1 = conn1.prepareStatement(select);
		rs1 = ps1.executeQuery();
		
		while(rs1.next()){
			idList.insert(rs1.getString("scheduleID"));
		}
		
		for(int i=0;i<6;i++){
			ID += rnd.nextInt(10)+"";
		}
		
		while(idList.search(ID)>0){
			ID = "";
			for(int i=0;i<6;i++){
				ID += rnd.nextInt(10)+"";
			}
		}
		
		return ID;	
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
	public AddSchedule() throws SQLException {
		
		conn = StaffMain.getConnection();
		
		this.setTitle("Add a Schedule");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		OK = StaffMain.initialButton("OK", null, Color.black, true, this);
		cancel = StaffMain.initialButton("cancel", null, Color.black, true, this);
		
		selectedMovie = new JLabel("(movie selected)");
		selectedRoom = new JLabel("(room selected)");
		content = new JLabel("Please enter information for a schedule.");
		notice1 = new JLabel("Notice: Please choose an existing movie and room for this sechdule!");
		notice2 = new JLabel("Notice: Each blank must be filled!");
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
		movieList = new JMenu("Movie");
		roomList = new JMenu("Room");
		barMovie = new JMenuBar();
		barRoom = new JMenuBar();
		barMovie.add(movieList);
		barRoom.add(roomList);
		
		//add Items to movie choosing bar
		String select = "select * from MOVIE";
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
		
		//add items to room choosing bar
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
							.addComponent(OK)
							.addComponent(cancel)))));
		
		aLayout.setVerticalGroup(
			aLayout.createSequentialGroup()
				.addComponent(content)
				.addComponent(notice1)
				.addComponent(notice2)
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
		
		new AddSchedule();

	}

	
}
