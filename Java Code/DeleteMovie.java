package Zhao;//Xiaolang Wang, 2014/12/01, CS425_final, Delete_Movie

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


public class DeleteMovie extends JFrame implements ActionListener{

	//variables
	private static final long serialVersionUID = 1L;
	private JButton delete,cancel;
	private JLabel content,notice1,notice2,selected;
	private JMenuBar bar;
	private JMenu movieList;
	private JPanel panel;
	
	private JMenuItem [] movieArray = new JMenuItem [4000];
	private strList movies = new strList(4000);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private String tempMovie;
	//action
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("delete")){
			if(selected.getText().equals("(movie selected will be shown here)")){
				JOptionPane frame=new JOptionPane();
		      	 JOptionPane.showMessageDialog(frame,
		      			    "You haven't selected a movie yet!",
		      			    "No Selected Movie Error",
		      			    JOptionPane.WARNING_MESSAGE);
		      	 }
			else{
				String [] info = selected.getText().split("\\|");
				
				try {
					if(hasSchedule(info[0].substring(1),info[1])){
						JOptionPane frame=new JOptionPane();
				      	 JOptionPane.showMessageDialog(frame,
				      			    "This movie is with at least one schedule now!",
				      			    "Not Valid Movie Error",
				      			    JOptionPane.WARNING_MESSAGE);
				      	 }
					else{
						//delete from ACT
						String delete = "delete from ACT where TITLE ='"+info[0].substring(1)+"'"
								+ "and RELEASEYEAR = '"+info[1]+"'";
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
						
						//delete from CREATOR
						delete = "delete from CREATOR where TITLE ='"+info[0].substring(1)+"'"
								+ "and RELEASEYEAR = '"+info[1]+"'";
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
						
						//delete from GENRE
						delete = "delete from GENRE where TITLE ='"+info[0].substring(1)+"'"
								+ "and RELEASEYEAR = '"+info[1]+"'";
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
						
						//delete from RATEMOVIE
						delete = "delete from RATEMOVIE where TITLE ='"+info[0].substring(1)+"'"
								+ "and RELEASEYEAR = '"+info[1]+"'";
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
						
						//delete from MOVIE
						delete = "delete from MOVIE where TITLE ='"+info[0].substring(1)+"'"
								+ "and RELEASEYEAR = '"+info[1]+"'";
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
						
						this.dispose();
						new MovieInfo();
					}
				} catch (SQLException e) {
					e.printStackTrace();
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
	
	//hasSchedule
	public static boolean hasSchedule(String title,String year) throws SQLException{
		boolean check = false;
		Connection conn1 = StaffMain.getConnection();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		
		String select = "select TITLE,RELEASEYEAR from SCHEDULE";
		ps1 = conn1.prepareStatement(select);
		rs1 = ps1.executeQuery();
		
		while(rs1.next()){
			if(title.equals(rs1.getString("TITLE")) && year.equals(rs1.getString("RELEASEYEAR"))){
				check = true;
			}
		}
		return check;	
	}
	
	
	
	//constructor
	public DeleteMovie() throws SQLException {
		
		conn = StaffMain.getConnection();
		
		this.setTitle("Delete a Movie");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		delete = StaffMain.initialButton("delete", null, Color.black, true, this);
		cancel = StaffMain.initialButton("cancel", null, Color.black, true, this);
		selected = new JLabel("(movie selected will be shown here)");
		
		content = new JLabel("Please choose an existing movie to delete.");
		notice1 = new JLabel("Notice: you can't delete a movie with schedule!");
		notice2 = new JLabel("Notice: deleting a movie will cause deleting its other information!");
		
		//Menu
		movieList = new JMenu("Movie");
		bar = new JMenuBar();
		bar.add(movieList);
		
		//add Items to movieList
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
					selected.setText("("+tempMovie+")");
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
					.addComponent(bar)
					.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(selected,0,300,500)
						.addGroup(aLayout.createSequentialGroup()
							.addComponent(delete)
							.addComponent(cancel)))));
		
		aLayout.setVerticalGroup(
			aLayout.createSequentialGroup()
				.addComponent(content)
				.addComponent(notice1)
				.addComponent(notice2)
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
		
		new DeleteMovie();

	}


}