package Zhao;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class MemberWelcome extends JFrame implements ActionListener {
	JButton existM;
	JButton newM;
	JButton back;
	JLabel message;
	static Connection con ;

	public MemberWelcome(){
		existM = new JButton("Existing Member");
		newM = new JButton("New Member");
		back = new JButton("Return to Main");
		message = new JLabel("Member Center");
		existM.addActionListener(this);
		newM.addActionListener(this);
		back.addActionListener(this);
		
		JPanel def = new JPanel();
		BorderLayout layout = new BorderLayout();
		def.setLayout(layout);
		
		def.add(message,BorderLayout.PAGE_START);
		message.setHorizontalAlignment(SwingConstants.CENTER);
		def.add(existM,BorderLayout.LINE_START);
		def.add(newM,BorderLayout.LINE_END);
		def.add(back,BorderLayout.PAGE_END);
		
		setLocationRelativeTo(null);
		add(def);
		setTitle("Cinema System");
		pack();
		setVisible(true);
		try {
			con = this.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== existM){
			this.dispose();
			new MemberExisting();
		}
		
		if(e.getSource()==newM){
			this.dispose();
			new MemberNew();
		}
		
		if(e.getSource()==back){
			this.dispose();
			new Welcome();
		}
	}
	
	static Connection getConnection() throws SQLException{
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
	
//	public static void main(String[] args){
//		new MemberWelcome();
//	}
	
	
}
