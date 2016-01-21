package Zhao;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class MemberBuy extends JFrame  implements ActionListener{
	private String id;
	private String name;
	private String title;
	private String releaseYear;
	
	private JTable table;
	private MyTableModel myModel;
	
	private JButton ok = new JButton("Ok");
	private JButton cancel = new JButton("Cancel");
	
	private JLabel number = new JLabel("Quantity");
	private JLabel method = new JLabel("Payment Method");
	
	private JComboBox box1 = new JComboBox();
	private JComboBox box2 = new JComboBox();

	
	public MemberBuy(String aId, String aName, String aTitle, String aYear){
		id = aId;
		name = aName;
		title = aTitle;
		releaseYear = aYear;
		
		JScrollPane panel = new JScrollPane();

		table = new JTable();
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
		panel.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myModel = this.showAll();
		table.setModel(myModel);
		
		JPanel content = new JPanel();
		GridLayout layout1 = new GridLayout(0,2,7,7);
		content.setLayout(layout1);
		box1.addItem("1");
		box1.addItem("2");
		box1.addItem("3");
		box1.addItem("4");
		content.add(number);
		number.setHorizontalAlignment(SwingConstants.CENTER);
		content.add(box1);
		box2.addItem("Debit");
		box2.addItem("Credit");
		content.add(method);
		method.setHorizontalAlignment(SwingConstants.CENTER);
		content.add(box2);
		content.add(ok);
		ok.addActionListener(this);
		content.add(cancel);
		cancel.addActionListener(this);
		
		GridLayout layout2 = new GridLayout(0,1,7,7);
		this.setLayout(layout2);
		this.add(panel);
		this.add(content);
		
		this.setSize(600,300);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		this.setTitle("Buy");
		
	}
	
	public ResultSet getSchedule(String aTitle, String aYear) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From SCHEDULE WHERE TITLE = '"
				+ aTitle + "' AND RELEASEYEAR = '" + aYear + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}
	
	public MyTableModel showAll() {
		ResultSet scheduleSet;
		MyTableModel model = null;
		int rowN = 0;
		try {
			ResultSet test = this.getSchedule(title,releaseYear);
			while (test.next()) {
				rowN++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSetMetaData data1;

		try {
			scheduleSet = this.getSchedule(title,releaseYear);
			data1 = scheduleSet.getMetaData();
			String[] columnNames = new String[data1.getColumnCount() ];
			;
			Object[][] data = new Object[rowN][data1.getColumnCount()];
			for (int i = 1; i <= data1.getColumnCount() ; i++) {
				columnNames[i - 1] = data1.getColumnName(i);
			}
			for (int i = 0; scheduleSet.next(); i++) {
				data[i][0] = scheduleSet.getString(1);
				data[i][1] = scheduleSet.getString(2);
				data[i][2] = scheduleSet.getString(3);
				data[i][3] = scheduleSet.getString(4);
				data[i][4] = scheduleSet.getString(5);
			}
			model = new MyTableModel(data, columnNames);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return model;
	}
	

	public void addTB(String aId, String aClass,String aTicket, String aPayment)
			throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "INSERT INTO BOOK VALUES ('" + aId + "','"+aClass+"','" + aTicket
				+ "','" + aPayment + "')";
		ps = con.prepareStatement(sql);
		ps.executeUpdate();
	}
	
	public void addTS(String aTid, String aSchedule)
			throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "INSERT INTO TICKET VALUES ('" + aTid + "','" + aSchedule
				+ "')";
		ps = con.prepareStatement(sql);
		ps.executeUpdate();
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ok) {
			int rN = -1;
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.isRowSelected(i)) {
					rN = i;
					break;
				}
			}

			if (rN < 0) {
				JOptionPane.showMessageDialog(this, "Please Select A Movie",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				String newTid ="";
				String aclass = "member";
				while(newTid.length()<6){
				int random=(int)(Math.random()*10);
				newTid += random;
				}
				String pay =(String) box2.getSelectedItem();
				pay = pay.toLowerCase();
				String scheduleID = (String)table.getValueAt(rN, 4);
				try {
					this.addTS(newTid, scheduleID);
					this.addTB(id,aclass,newTid, pay);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(this, "Purchase Successed!",
						"Congratulation", JOptionPane.PLAIN_MESSAGE);
				this.dispose();
				new MemberSearch(id,name);
			}
		}
		
		if(e.getSource()==cancel){
			this.dispose();
			new MemberSearch(id,name);
		}
		
	}
	
}
