package Zhao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class MemberExisting extends JFrame implements ActionListener{
	JButton ok;
	JButton cancel;
	ResultSet rset;
	private ResultSet rs;
	private String name ;
	private String id;
	JTextField field = new JTextField("");
	
	public MemberExisting(){
		JLabel label = new JLabel("Please Enter Your ID Number");
		
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		
		this.setSize(350,80);
//		JPanel panel = new JPanel();
//		FlowLayout panelLayout = new FlowLayout();
//		panel.setLayout(panelLayout);
//		panel.setSize(400, 50);
		
		
		JPanel buttonPanel = new JPanel();
		GridLayout buttonLayout = new GridLayout(0,2,7,7);
		buttonPanel.setLayout(buttonLayout);
		buttonPanel.add(ok);
		ok.addActionListener(this);
		buttonPanel.add(cancel);
		cancel.addActionListener(this);
		
		GridLayout frameLayout = new GridLayout(0,2,5,5);
		this.setLayout(frameLayout);
		this.add(label);
		this.add(field);
//		this.add(panel);
		this.add(buttonPanel);
		
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
		this.setTitle("Log In");
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ok){
			try {
				 rs= this.getResult();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			try {
				if(field.getText().equals("")){
					JOptionPane.showMessageDialog(this, "Input Can't Be Empty!", "Error", 0);
					field.setText("");
				}
				else if(!valid(field.getText())){
					JOptionPane.showMessageDialog(this, "Invalid ID, Please Enter Again!\n"
							+ "ID Must Be Number And Six Digit Long", "Error", 0);
					field.setText("");
				}
				else if(exist(field.getText())){
					this.dispose();
					new MemberMain(id,name);
				}
				else{
					JOptionPane.showMessageDialog(this, "Users Doesn't Exist!", "Error", 0);
					field.setText("");
				}
				
				
					
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	
		}
		
		if(e.getSource()==cancel){
			this.dispose();
			new MemberWelcome();
		}
	}
	
	public ResultSet getResult() throws SQLException{
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From USERS";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}
	
	public boolean exist(String target) throws SQLException{
		boolean temp = false;
		ResultSet rs = getResult();
		while(rs.next()){
			if(rs.getString(1).equals(target)){
				temp = true;
				id = rs.getString(1);
				name = rs.getString(2);
			}
				
		}
		return temp;		
	}
	
	public boolean valid(String target){
		boolean digitTemp = true;
		boolean lengthTemp = true;
		for(int i =0; i<target.length();i++){
			if(!Character.isDigit(target.charAt(i)))
				digitTemp = false;
		}
		if(target.length()!=6)
			lengthTemp = false;
		return digitTemp && lengthTemp;
	}
//	
//	public static void main(String[] args) {
//		new MemberExisting();
//	}
}
