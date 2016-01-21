package Zhao;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Welcome extends JFrame implements ActionListener{
	private JLabel label1 ;
	
	private static final int FRAME_WIDTH=700;
	private static final int FRAME_HEIGHT=400;
	private static final int FRAME_X_ORIGIN=150;
	private static final int FRAME_Y_ORIGIN=600;
	private static final int BUTTON_WIDTH=250;
	private static final int BUTTON_HEIGHT=100;
	JButton stuff,member;
//	static Connection conn;
	//JButton("OK");
	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		JButton clickButton = (JButton) event.getSource();
		if (clickButton == stuff) {
			this.dispose();
			new StaffMain();
		} else {
			this.dispose();
			new MemberWelcome();
		}
		
	}
	public Welcome(){
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setResizable(false);
		setTitle("MyFirstFrame");
		setLocation(FRAME_X_ORIGIN,FRAME_Y_ORIGIN);
		setLayout(null);
		/*GroupLayout gl = new GroupLayout(this);
		this.setLayout(gl);
		gl.setHorizontalGroup(
				gl.createSequentialGroup()
				.addComponent(okButton)
				.addComponent(label1)
				.addComponent(cancelButton)
				
				
				);
		gl.setVerticalGroup(
				gl.createSequentialGroup()
				.addComponent(label1)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(okButton)
						.addComponent(cancelButton))
				);*/
		
		label1=new JLabel("Welcome!");
		label1.setBounds(280, 100, 200, 60);
		
		add(label1);
		//add stuff button
		stuff = new JButton("Stuff");
		stuff.setBounds(100,200,BUTTON_WIDTH,BUTTON_HEIGHT);
		add(stuff);
		//add member button
		member = new JButton("Member");
		member.setBounds(380,200,BUTTON_WIDTH,BUTTON_HEIGHT);
		add(member);
		
		stuff.addActionListener(this);
		member.addActionListener(this);
		
		Color a = new Color(255,250,194);
		Color Tif = new Color(175,223,228);
		member.setBackground(a);
		
		stuff.setBackground(a);
		
		this.getContentPane().setBackground(Tif);
	
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
//	public static Connection getConnection() throws SQLException{
//		Connection conn = null;
//		try{
//			
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		String url = "jdbc:oracle:thin:@fourier.cs.iit.edu:1521:orcl";
//		String user = "czhao18";
//		String password = "123456";
//		conn = DriverManager.getConnection(url, user, password);
//		return conn;
//	}
	public static void main(String[] args) throws SQLException{
		new Welcome();
//		conn=getConnection();
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		String sql = "INSERT INTO MOVIE VALUES('Intersteller',2014,'200','PG-13')";
//		ps = conn.prepareStatement(sql); 
//		ps.executeUpdate(sql);
//		ps.close();
		
	}
	 
}

