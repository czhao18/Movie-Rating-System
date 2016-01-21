package Zhao;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class MemberRateMovie extends JFrame implements ActionListener {

	private String id;
	private String name;
	private String title;
	private String year;

	private JTable table1;
	private MyTableModel myModel;

	private JButton ok = new JButton("Ok");
	private JButton cancel = new JButton("Cancel");

	private JLabel rateM = new JLabel("Rate Movie");
	private JComboBox box1 = new JComboBox();
	private JScrollPane panel = new JScrollPane();

	public MemberRateMovie(String aId, String aName) {
		id = aId;
		name = aName;

		table1 = new JTable();
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table1.setDefaultRenderer(Object.class, r);
		panel.setViewportView(table1);
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table1.getTableHeader().setReorderingAllowed(false);
		table1.getTableHeader().setResizingAllowed(false);
		table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myModel = this.showAll();
		table1.setModel(myModel);
		this.setStyle();

	}

	public MemberRateMovie(String aId, String aName, String aTitle, String aYear) {
		id = aId;
		name = aName;
		title = aTitle;
		year = aYear;

		table1 = new JTable();
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table1.setDefaultRenderer(Object.class, r);
		panel.setViewportView(table1);
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table1.getTableHeader().setReorderingAllowed(false);
		table1.getTableHeader().setResizingAllowed(false);
		table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myModel = this.showMovie();
		table1.setModel(myModel);
		this.setStyle();
	}

	public void setStyle() {
		JPanel bpanel = new JPanel();
		GridLayout layout1 = new GridLayout(0, 2, 7, 7);
		bpanel.setLayout(layout1);
		bpanel.add(rateM);
		rateM.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 10; i > 0; i--) {
			box1.addItem(i);
		}
		bpanel.add(box1);
		bpanel.add(ok);
		ok.addActionListener(this);
		bpanel.add(cancel);
		cancel.addActionListener(this);

		GridLayout layout2 = new GridLayout(0, 1, 7, 7);
		this.setLayout(layout2);
		this.add(panel);
		this.add(bpanel);

		this.setSize(1000, 300);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		this.setTitle("Rate");
	}

	public ResultSet getMovie(String aTitle, String aYear) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From MOVIE WHERE TITLE = '" + aTitle
				+ "' AND RELEASEYEAR = '" + aYear + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getAll() throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From MOVIE";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public MyTableModel showMovie() {
		ResultSet scheduleSet;
		MyTableModel model = null;
		int rowN = 0;
		try {
			ResultSet test = this.getMovie(title, year);
			while (test.next()) {
				rowN++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSetMetaData data1;

		try {
			scheduleSet = this.getMovie(title, year);
			data1 = scheduleSet.getMetaData();
			String[] columnNames = new String[data1.getColumnCount()];
			;
			Object[][] data = new Object[rowN][data1.getColumnCount()];
			for (int i = 1; i <= data1.getColumnCount(); i++) {
				columnNames[i - 1] = data1.getColumnName(i);
			}
			for (int i = 0; scheduleSet.next(); i++) {
				data[i][0] = scheduleSet.getString(1);
				data[i][1] = scheduleSet.getString(2);
				data[i][2] = scheduleSet.getString(3);
				data[i][3] = scheduleSet.getString(4);
			}
			model = new MyTableModel(data, columnNames);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return model;
	}

	public MyTableModel showAll() {
		ResultSet scheduleSet;
		MyTableModel model = null;
		int rowN = 0;
		try {
			ResultSet test = this.getAll();
			while (test.next()) {
				rowN++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSetMetaData data1;

		try {
			scheduleSet = this.getAll();
			data1 = scheduleSet.getMetaData();
			String[] columnNames = new String[data1.getColumnCount()];
			;
			Object[][] data = new Object[rowN][data1.getColumnCount()];
			for (int i = 1; i <= data1.getColumnCount(); i++) {
				columnNames[i - 1] = data1.getColumnName(i);
			}
			for (int i = 0; scheduleSet.next(); i++) {
				data[i][0] = scheduleSet.getString(1);
				data[i][1] = scheduleSet.getString(2);
				data[i][2] = scheduleSet.getString(3);
				data[i][3] = scheduleSet.getString(4);
			}
			model = new MyTableModel(data, columnNames);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			String title = "", year = "";
			String aClass = "member";
			for (int i = 0; i < table1.getRowCount(); i++) {
				if (table1.isRowSelected(i)) {
					title = (String) table1.getValueAt(i, 0);
					year = (String) table1.getValueAt(i, 1);
					break;
				}
			}
			if (title.equals("")) {
				JOptionPane.showMessageDialog(this, "Please Select A Movie",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else
				try {
					if(exist(title,year)){
						JOptionPane.showMessageDialog(this, "You Already Rated This Movie!",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						String aRate = String.valueOf(box1.getSelectedItem());
						try {
							this.addRate(title, year, id, aClass, aRate);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
						JOptionPane.showMessageDialog(this, "Rate Successed!",
								"Congratulation", JOptionPane.PLAIN_MESSAGE);
						this.dispose();
						new MemberRate(id,name);
					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}

		if (e.getSource() == cancel) {
			this.dispose();
			new MemberRate(id, name);
		}

	}
	
	public void addRate(String aTitle, String aYear,String aId, String aClass,String aRate)
			throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "INSERT INTO RATEMOVIE VALUES ('" + aTitle + "','"+aYear+"','" + aId
				+ "','" + aClass + "','" + aRate + "')";
		ps = con.prepareStatement(sql);
		ps.executeUpdate();
	}
	
	public boolean exist(String aTitle, String aYear) throws SQLException{
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From RATEMOVIE WHERE USERID = '"
				+ id + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		
		while(rs.next()){
			if(rs.getString(1).equals(aTitle)&&rs.getString(2).equals(aYear))
				return true;
		}
		
		return false;
	}

}
