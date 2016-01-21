package Zhao;//Xiaolang Wang, 2014/11/30, CS425_final, Staff_main 

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//helper class, pseudo-ArrayList
class strList{ 
	String [] strArray;
	int index;

//constructors
	strList(){
		strArray = new String [30];
		index = 0;
	}
	
	strList(int size){
		strArray = new String [size];
		index = 0;
	}
	
//get
	String getElement(int spot){
		return strArray[spot];
	}

	int getIndex(){
		return index;
	}
	
//setEmpty
	void reset(){
		for(int i=0;i<index;i++){
			strArray[i] = null;
		}
		index = 0;
	}
	
//isEmpty
	boolean isEmpty(){
		return index == 0;
	}
		
//insert
	void insert(String aStr){
		strArray[index] = aStr;
		index ++;
	}
	
//delete
	void delete(int spot){
		if(spot > -1){
			for(int i=spot; i<index; i++){
				strArray[i] = strArray[i+1];
				index --;
				strArray [index] = null;
			}
		}
	}	
	
	void delete(String str){
		for (int i=0;i<index;i++){
			if(strArray[i].equals(str)){
				index--;
				strArray[i] = strArray[index];
				strArray[index] = null;
			}
		}
	}
	
//search
	int search(String str){
		int spot = -1;
		for (int i=0;i<index;i++){
			if(strArray[i].equals(str)){
				spot = i;
			}
		}
		return spot;
	}

//toString
	String toDisplay(){
		String output = "";
		for(int i =0;i<index; i++){
			output += strArray[i]+"\n";
		}
		return output;
	}

}

public class StaffMain extends JFrame implements ActionListener{
	
	//variables
	private static final long serialVersionUID = 1L;
	private JLabel content;
	private JButton member, movie, schedule, back;
	private JPanel panel;
	
	//action
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("member")){
			this.dispose();
			try {
				new MemberInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		
		if(ae.getActionCommand().equals("movie")){
			this.dispose();
			try {
				new MovieInfo();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
		if(ae.getActionCommand().equals("schedule")){
			this.dispose();
			try {
				new ScheduleInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
		if(ae.getActionCommand().equals("back")){
			this.dispose();
			new Welcome();	
			}	
		}
	
	//build button
	public static JButton initialButton(String s, Color back, Color front, boolean active,
			ActionListener listener){
		
		JButton aButton = new JButton(s);		

		aButton.setForeground(front);
		aButton.setBackground(back);
		aButton.setActionCommand(s);
		aButton.setEnabled(active);
		aButton.addActionListener(listener);
		
		return aButton;
	
	}
	
	//getConnection
	public static Connection getConnection() throws SQLException{
		Connection conn = null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception e){
			e.printStackTrace();
		}
		String url = "jdbc:oracle:thin:@fourier.cs.iit.edu:1521:orcl";
		String user = "czhao18";
		String password = "123456";
		conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	
	//constructor
	public StaffMain() {
		
		this.setTitle("Staff");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		member = initialButton("member",null,Color.black,true,this);
		movie = initialButton("movie",null,Color.black,true,this);
		schedule = initialButton("schedule",null,Color.black,true,this);
		back = initialButton("back",null,Color.black,true,this);
		
		content = new JLabel("Please choose what to do you want to do.");
		
		//layout
		GroupLayout aLayout = new GroupLayout(panel);
		panel.setLayout(aLayout);
		
		aLayout.setAutoCreateGaps(true);
		aLayout.setAutoCreateContainerGaps(true);
		
		aLayout.setHorizontalGroup(
			aLayout.createSequentialGroup()
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(member,0,100,100)
					 .addComponent(movie,0,100,100)
					 .addComponent(schedule,0,100,100)
					 .addComponent(back,0,100,100))
				.addComponent(content,0,300,600));
			
		aLayout.setVerticalGroup(
			aLayout.createSequentialGroup()
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(member,0,57,57)
						.addComponent(content))
				.addComponent(movie,0,57,57)
				.addComponent(schedule,0,57,57)
				.addComponent(back,0,57,57));
		
		
		
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

	
	public static void main(String[] args) {
		
		new StaffMain();

	}
	
}
