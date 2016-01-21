package Zhao;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class MemberRateH extends JFrame implements ActionListener{
	private String id;
	private String name;
	
	private JTable table1;
	private MyTableModel myModel1;
	private JTable table2;
	private MyTableModel myModel2;

	private JButton ok = new JButton("1");
	private JButton block = new JButton("2");
	private JButton block1 = new JButton("3");
	private JButton block2 = new JButton("Return");
	private JButton block3 = new JButton("5");
	private JButton block4 = new JButton("6");
	private JButton block5 = new JButton("7");
	
	
	
	public MemberRateH(String aId, String aName){
		id = aId;
		name = aName;
		
		JScrollPane panel1 = new JScrollPane();
		JScrollPane panel2 = new JScrollPane();
		
		table1 = new JTable();
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table1.setDefaultRenderer(Object.class, r);
		panel1.setViewportView(table1);
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table1.getTableHeader().setReorderingAllowed(false);
		table1.getTableHeader().setResizingAllowed(false);
		table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myModel1 = this.showMovieRate();
		table1.setModel(myModel1);
		
		table2 = new JTable();
		DefaultTableCellRenderer s = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table2.setDefaultRenderer(Object.class, s);
		panel2.setViewportView(table2);
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table2.getTableHeader().setReorderingAllowed(false);
		table2.getTableHeader().setResizingAllowed(false);
		table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myModel2 = this.showActorRate();
		table2.setModel(myModel2);
		
		JPanel tpanel = new JPanel();
		GridLayout layout = new GridLayout(0, 2, 7, 7);
		tpanel.setLayout(layout);
		tpanel.add(panel1);
		tpanel.add(panel2);
		
		
		JPanel bpanel = new JPanel();
		GridLayout layout1 = new GridLayout(0, 7, 7, 7);
		bpanel.setLayout(layout1);
		bpanel.add(ok);
		ok.setVisible(false);
		bpanel.add(block);
		block.setVisible(false);
		bpanel.add(block1);
		block1.setVisible(false);
		bpanel.add(block2);
		block2.addActionListener(this);
		bpanel.add(block3);
		block3.setVisible(false);
		bpanel.add(block4);
		block4.setVisible(false);
		bpanel.add(block5);
		block5.setVisible(false);
		
		
		GridLayout layout2 = new GridLayout(0, 1, 7, 7);
		this.setLayout(layout2);
		this.add(tpanel);
		this.add(bpanel);

		this.setSize(700,250);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		this.setTitle("My Rate");
	}
	
	public ResultSet getMovieRate() throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT TITLE,RELEASEYEAR,RATE From RATEMOVIE WHERE USERID = '"
				+ id + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}
	
	public ResultSet getActorRate() throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT PNAME,BIRTHDATE,GENDER,RATE From PEOPLE NATURAL JOIN ACT NATURAL JOIN RATEPEOPLE WHERE USERID = '"
				+ id + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}
	
	public MyTableModel showMovieRate() {
		ResultSet myRate;
		MyTableModel model = null;
		int rowN = 0;
		try {
			ResultSet test = this.getMovieRate();
			while (test.next()) {
				rowN++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSetMetaData data1;

		try {
			myRate = this.getMovieRate();
			data1 = myRate.getMetaData();
			String[] columnNames = new String[data1.getColumnCount()];
			;
			Object[][] data = new Object[rowN][data1.getColumnCount()];
			for (int i = 1; i <= data1.getColumnCount(); i++) {
				columnNames[i - 1] = data1.getColumnName(i);
			}
			for (int i = 0; myRate.next(); i++) {
				data[i][0] = myRate.getString(1);
				data[i][1] = myRate.getString(2);
				data[i][2] = myRate.getString(3);
			}
			model = new MyTableModel(data, columnNames);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return model;
	}

	public MyTableModel showActorRate() {
		ResultSet myRate;
		MyTableModel model = null;
		int rowN = 0;
		try {
			ResultSet test = this.getActorRate();
			while (test.next()) {
				rowN++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSetMetaData data1;

		try {
			myRate = this.getActorRate();
			data1 = myRate.getMetaData();
			String[] columnNames = new String[data1.getColumnCount()];
			;
			Object[][] data = new Object[rowN][data1.getColumnCount()];
			for (int i = 1; i <= data1.getColumnCount(); i++) {
				columnNames[i - 1] = data1.getColumnName(i);
			}
			for (int i = 0; myRate.next(); i++) {
				data[i][0] = myRate.getString(1);
				data[i][1] = myRate.getString(2);
				data[i][2] = myRate.getString(3);
				data[i][3] = myRate.getString(4);
			}
			model = new MyTableModel(data, columnNames);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return model;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==block2){
			this.dispose();
			new MemberMain(id,name);
		}
		
	}

}

