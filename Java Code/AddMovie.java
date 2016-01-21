package Zhao;//Xiaolang Wang, 2014/12/01, CS425_final, Add_Movie

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;


public class AddMovie extends JFrame implements ActionListener{
	
	//variables
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JButton OK,cancel;
	private JLabel title,year,length,pgRating,genre,content,notice1, notice2;
	private JTextPane textTitle, textYear,textLength,textRate,textGenre;
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	//action
	public void actionPerformed(ActionEvent ae){
		if(ae.getActionCommand().equals("OK")){
			if(textTitle.getText().equals("") || textYear.getText().equals("") || textLength.equals("")){
				JOptionPane frame=new JOptionPane();
		      	  JOptionPane.showMessageDialog(frame,
		      			    "Stared blank must be filled!",
		      			    "Empty Error",
		      			    JOptionPane.WARNING_MESSAGE);
			}
			else if(!isInt(textYear.getText()) || !isInt(textLength.getText()) ){
				JOptionPane frame=new JOptionPane();
		      	  JOptionPane.showMessageDialog(frame,
		      			    "Movie released year and movie length should be a number!",
		      			    "Not Integer Error",
		      			    JOptionPane.WARNING_MESSAGE);
			}
			else{
				if(hasDuplicate(textTitle.getText(),textYear.getText())){
					JOptionPane frame=new JOptionPane();
				  	  JOptionPane.showMessageDialog(frame,
				  			    "There already exists this movie!",
				  			    "Duplicate Movie Error",
				  			    JOptionPane.WARNING_MESSAGE);
				}

				else{
					String add1 = "insert into MOVIE values('"+textTitle.getText()+"'"
							+ ",'"+Integer.parseInt(textYear.getText())+"'"
							+ ",'"+textLength.getText()+"','"+textRate.getText()+"')";
					try {
						ps = conn.prepareStatement(add1);
						ps.executeUpdate(add1);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					String [] info  = textGenre.getText().split(",");
					
					for(int i=0;i<info.length;i++){
						String add2 = "insert into GENRE values('"+textTitle.getText()+"'"
								+ ",'"+textYear.getText()+"','"+info[i]+"')";
						try {
							ps = conn.prepareStatement(add2);
							ps.executeUpdate(add2);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					this.dispose();
					try {
						new MovieInfo();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					}
	
				}
			}
		
		if(ae.getActionCommand().equals("cancel")){
			this.dispose();
			try {
				new MovieInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
	
		}
	
	//hasDuplicate
	public static boolean hasDuplicate(String str1, String str2){
		boolean check = false;
		Connection conn1 = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		
		String select = "select TITLE,RELEASEYEAR from MOVIE";
		
		try {
			conn1 = StaffMain.getConnection();
			ps1 = conn1.prepareStatement(select);
			rs1 = ps1.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			while(rs1.next()){
				if(str1.equals(rs1.getString("TITLE"))  &&
						str2.equals(rs1.getString("RELEASEYEAR"))){
					check = true;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return check;
	}
	
	//isInt
	public static boolean isInt(String str){
		boolean check = true;
		for(int i=0;i<str.length();i++){
			if(Character.isDigit(str.charAt(i)) != true){
				check = false;
				}
			}
		return check;	
	}

	//Constructor
	public AddMovie() throws SQLException {
		
		conn = StaffMain.getConnection();

		this.setTitle("Add a Movie");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		content = new JLabel("Please enter information for a movie.");
		notice1 = new JLabel("Notice: Attributes with * must be filled!");
		notice2 = new JLabel("Notice: If there're more than one genre, use commas to seperate them!");
		
		title = new JLabel("Title *");
		year = new JLabel("Release Year *");
		length = new JLabel("Movie Length");
		pgRating = new JLabel("PG Rating");
		genre = new JLabel("Genre");
		
		
		OK = StaffMain.initialButton("OK",null,Color.black,true,this);
		cancel = StaffMain.initialButton("cancel",null,Color.black,true,this);
		
		textTitle = new JTextPane();
		textYear = new JTextPane();
		textLength = new JTextPane();
		textRate = new JTextPane();
		textGenre = new JTextPane();
		
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
					 .addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
							 .addComponent(title)
							 .addComponent(year)
							 .addComponent(length)
							 .addComponent(pgRating)
							 .addComponent(genre))
					 .addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
							 .addComponent(textTitle)
							 .addComponent(textYear)
							 .addComponent(textLength)
							 .addComponent(textRate)
							 .addComponent(textGenre)
							 .addGroup(aLayout.createSequentialGroup()
									 .addComponent(OK)
									 .addComponent(cancel)))));

		aLayout.setVerticalGroup(
			aLayout.createSequentialGroup()
				.addComponent(content)
				.addComponent(notice1)
				.addComponent(notice2)
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(title)
					 .addComponent(textTitle))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(year)
					 .addComponent(textYear))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(length)
					 .addComponent(textLength))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(pgRating)
					 .addComponent(textRate))
				.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addComponent(genre)
					 .addComponent(textGenre))
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
		
		new AddMovie();

	}

}