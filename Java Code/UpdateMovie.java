package Zhao;//Xiaolang Wang, 2014/12/05, CS402_final, Update_Movie

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


public class UpdateMovie extends JFrame implements ActionListener{
	
	//variables
	private static final long serialVersionUID = 1L;
	private JLabel updating,content,notice1,notice2,selected,length,rate,genre;
	private JMenuBar bar;
	private JMenu movieList;
	private JPanel panel;
	private String temp;
	private JTextPane textLength,textRate,textGenre;
	private JButton update,cancel;
			
	private JMenuItem [] movieArray = new JMenuItem [4000];
	private strList movies = new strList(4000);
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;	
	
	//action
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("update")){
			if(selected.getText().equals("(movie selected will be shown here)")){
				 JOptionPane frame=new JOptionPane();
		      	 JOptionPane.showMessageDialog(frame,
		      			    "You haven't selected a movie yet!",
		      			    "No Selected Movie Error",
		      			    JOptionPane.WARNING_MESSAGE);
			}
			else{
				
				String [] info = selected.getText().split("\\|");
				if(!textLength.getText().equals("")){
					if(!isInt(textLength.getText())){
						JOptionPane frame=new JOptionPane();
				      	 JOptionPane.showMessageDialog(frame,
				      			    "Length must be an integer!",
				      			    "Not Valid Length Error",
				      			    JOptionPane.WARNING_MESSAGE);	
					}
					else{
						String update = "update MOVIE set LASTING ='"+textLength.getText()+"' "
								+ "where TITLE = '"+info[0].substring(1)+"' and "
								+ "RELEASEYEAR = '"+info[1]+"'";
							try {
								ps = conn.prepareStatement(update);
								ps.executeUpdate(update);
							} catch (SQLException e) {
								e.printStackTrace();
							}
					}
				}
				
				if(!textRate.getText().equals("")){
					String update = "update MOVIE set PGRATE ='"+textRate.getText()+"' "
							+ "where TITLE = '"+info[0].substring(1)+"' and "
							+ "RELEASEYEAR = '"+info[1]+"'";
						try {
							ps = conn.prepareStatement(update);
							ps.executeUpdate(update);
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}
				
				if(!textGenre.getText().equals("")){
					String [] genre = textGenre.getText().split(",");
					
					String delete = "delete from GENRE where TITLE = '"+info[0].substring(1)+"' and "
							+ "RELEASEYEAR = '"+info[1]+"'";
					try {
						ps = conn.prepareStatement(delete);
						ps.executeUpdate(delete);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					for(int i=0;i<genre.length;i++){
						String add = "insert into GENRE values('"+info[0].substring(1)+"'"
								+ ",'"+info[1]+"','"+genre[i]+"')";
						try {
							ps = conn.prepareStatement(add);
							ps.executeUpdate(add);
						} catch (SQLException e) {
							e.printStackTrace();
						}
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
		if(ae.getActionCommand().equals("cancel")){
			this.dispose();
			try {
				new MovieInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
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
	
	//constructor
	public UpdateMovie() throws SQLException{
		
		conn = StaffMain.getConnection();
		
		this.setTitle("Update Movie Information");
		
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		update = StaffMain.initialButton("update", null, Color.black, true, this);
		cancel = StaffMain.initialButton("cancel", null, Color.black, true, this);
		selected = new JLabel("(movie selected will be shown here)");
		
		content = new JLabel("Please choose an existing movie to update.");
		notice1 = new JLabel("Notice: you can update selected movie's length, PG rating and genre!");
		notice2 = new JLabel("Notice: If there're more than one genre, use commas to seperate them!");
		updating = new JLabel("Update>>>>>>>>>");
		
		
		length = new JLabel("Movie Length");
		rate = new JLabel("PG Rating");
		genre = new JLabel("Genre");
		textLength = new JTextPane();
		textRate = new JTextPane();
		textGenre = new JTextPane();
		
		//Menu
		movieList = new JMenu("Movie");
		bar = new JMenuBar();
		bar.add(movieList);
		
		//add Items to List
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
							.addComponent(notice1)
							.addComponent(notice2)
							.addGroup(aLayout.createSequentialGroup()
								.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(updating)		
									.addComponent(bar)	
									.addComponent(length)
									.addComponent(rate)
									.addComponent(genre))
								.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
									.addComponent(selected,0,300,500)
									.addComponent(textLength)
									.addComponent(textRate)
									.addComponent(textGenre)
									.addGroup(aLayout.createSequentialGroup()
										.addComponent(update)
										.addComponent(cancel)))));		
				
				
				aLayout.setVerticalGroup(
						aLayout.createSequentialGroup()
							.addComponent(content)
							.addComponent(notice1)
							.addComponent(notice2)
							.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(bar)
								.addComponent(selected))
							.addComponent(updating)	
							.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(length)
								.addComponent(textLength))
							.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(rate)
								.addComponent(textRate))
							.addGroup(aLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(genre)
								.addComponent(textGenre))
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
		
		new UpdateMovie();

	}


}
