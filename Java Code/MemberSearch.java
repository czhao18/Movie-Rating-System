package Zhao;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class MemberSearch extends JFrame implements ActionListener {

	private JLabel title = new JLabel("Title");
	private JLabel year = new JLabel("Release Year");
	private JLabel genre = new JLabel("Genre");
	private JLabel director = new JLabel("Director");
	private JLabel actor = new JLabel("actor");

	private JButton search = new JButton("Search");
	private JButton restore = new JButton("Clear");
	private JButton detail = new JButton("Detail");
	private JButton ins = new JButton("Instruction");
	protected JButton buy = new JButton("Buy");
	protected JButton rateM = new JButton("Rate Movie");
	protected JButton rateA = new JButton("Rate Actor");
	private JButton back = new JButton("Back");

	protected String id;
	protected String name;

	private JTextField field1 = new JTextField();
	private JTextField field2 = new JTextField();
	private JTextField field3 = new JTextField();
	private JTextField field4 = new JTextField();

	ArrayList<String> type = new ArrayList<String>();
	private JCheckBox[] checkArray = new JCheckBox[100];

	protected JTable table;
	private MyTableModel myModel;

	public MemberSearch(String aID, String aName) {
		id = aID;
		name = aName;

		JPanel panel1 = new JPanel();
		GridLayout layout1 = new GridLayout(0, 2, 7, 7);
		panel1.setLayout(layout1);
		panel1.add(title);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(field1);
		panel1.add(year);
		year.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(field2);
		panel1.add(director);
		director.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(field3);
		panel1.add(actor);
		actor.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(field4);

		panel1.add(genre);
		genre.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel gPanel = new JPanel();
		gPanel.setLayout(new GridLayout(0, 3, 3, 1));

		try {
			ResultSet rs = this.getGenre();
			while (rs.next()) {
				if (!type.contains(rs.getString(3)))
					type.add(rs.getString(3));
			}

			for (int i = 0; i < type.size(); i++) {
				String temp = type.get(i);
				checkArray[i] = new JCheckBox(temp);
				gPanel.add(checkArray[i], false);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		panel1.add(gPanel);

		JPanel panel2 = new JPanel();
		GridLayout layout2 = new GridLayout(0, 3, 10, 10);
		panel2.setLayout(layout2);
		panel2.add(search);
		search.addActionListener(this);
		panel2.add(detail);
		detail.addActionListener(this);
		panel2.add(restore);
		restore.addActionListener(this);
		panel2.add(buy);
		buy.addActionListener(this);
		panel2.add(rateM);
		rateM.addActionListener(this);
		panel2.add(rateA);
		rateA.addActionListener(this);
		panel2.add(ins);
		ins.addActionListener(this);
		panel2.add(back);
		back.addActionListener(this);
		rateM.setVisible(false);
		rateA.setVisible(false);

		JPanel panel3 = new JPanel();
		GridLayout layout3 = new GridLayout(0, 1, 7, 80);
		panel3.setLayout(layout3);
		panel3.add(panel1);
		panel3.add(panel2);
		panel3.setBounds(0, 0, 200, 300);

		JScrollPane panel4 = new JScrollPane();

		table = new JTable();
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
		panel4.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myModel = this.showAll();
		table.setModel(myModel);

		// JTextArea textfield = new JTextArea(300,500);

		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(0, 2, 7, 7);
		panel.setLayout(layout);
		panel.add(panel3);
		panel.add(panel4);

		setSize(1200, 500);
		add(panel);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		this.setTitle("Search & Buy");
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == search) {

			if (this.search()) {

			} else {
				ArrayList<String> aList = new ArrayList<String>();
				for (int i = 0; i < type.toArray().length; i++) {

					boolean sl = checkArray[i].isSelected();
					if (sl) {
						String aGenre = checkArray[i].getText();
						try {
							ResultSet temp = this.getMovieG(aGenre);
							while (temp.next()) {
								String str = temp.getString(1) + ","
										+ temp.getString(2);
								aList.add(str);
							}
						} catch (SQLException a) {
							// TODO Auto-generated catch block
							a.printStackTrace();
						}
					}

				}

				for (int i = table.getRowCount() - 1; i >= 0; i--) {
					boolean checkName = false;
					boolean checkYear = false;
					for (int j = 0; j < aList.size(); j++) {
						String temp = (String) aList.toArray()[j];
						String[] data = temp.split(",");
						// System.out.println(data[0]+data[1]);
						checkName = data[0].equals(table.getValueAt(i, 0));
						checkYear = data[1].equals(table.getValueAt(i, 1));
						if (checkName && checkYear)
							break;
					}
					if (!(checkName && checkYear))
						myModel.removeRow(i);
				}
			}
		}

		if (e.getSource() == ins) {

		}

		if (e.getSource() == detail) {
			String title = "", year = "";
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.isRowSelected(i)) {
					title = (String) table.getValueAt(i, 0);
					year = (String) table.getValueAt(i, 1);
					break;
				}
			}
			if (title.equals("")) {
				JOptionPane.showMessageDialog(this, "Please Select A Movie",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					JOptionPane.showMessageDialog(this,
							this.showDetail(title, year), "Movie Details",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

		if (e.getSource() == restore) {
			field1.setText("");
			field2.setText("");
			field3.setText("");
			field4.setText("");
			for (int i = 0; i < type.toArray().length; i++) {
				checkArray[i].setSelected(false);
			}
			myModel = this.showAll();
			table.setModel(myModel);
		}

		if (e.getSource() == buy) {
			String title = "", year = "";
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.isRowSelected(i)) {
					title = (String) table.getValueAt(i, 0);
					year = (String) table.getValueAt(i, 1);
					break;
				}
			}
			if (title.equals("")) {
				JOptionPane.showMessageDialog(this, "Please Select A Movie",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				this.dispose();
				new MemberBuy(id, name, title, year);
			}
		}

		if (e.getSource() == back) {
			this.dispose();
			new MemberMain(id, name);
		}
	}

	public boolean search() {
		boolean check = false;
		if (this.nameIsValid(field1.getText())) {
			int rnum = table.getRowCount();
			String text = field1.getText().toLowerCase();
			String[] str = new String[rnum];
			for (int i = 0; i < rnum; i++) {
				str[i] = (String) table.getValueAt(i, 0);
			}
			for (int i = rnum - 1; i >= 0; i--) {
				boolean temp = true;
				str[i] = str[i].toLowerCase();
				if (str[i].contains(text)) {

				} else
					temp = false;
				if (!temp) {
					myModel.removeRow(i);
				}
			}
			myModel.fireTableDataChanged();
			check = true;
		}

		if (this.yearIsValid(field2.getText())) {
			for (int i = table.getRowCount() - 1; i >= 0; i--) {
				int year = Integer.parseInt((String) table.getValueAt(i, 1));
				if (Integer.parseInt(field2.getText()) != year) {
					myModel.removeRow(i);
				}
			}
			myModel.fireTableDataChanged();
			check = true;
		}

		if (this.nameIsValid(field3.getText())) {
			try {
				ResultSet writer = this.getWriter(field3.getText());
				ArrayList<String> idList = new ArrayList<String>();
				while (writer.next()) {
					idList.add(writer.getString(1));
				}

				ArrayList<String> movieList = new ArrayList<String>();
				for (int i = 0; i < idList.size(); i++) {
					ResultSet temp = this
							.getMovie((String) idList.toArray()[i]);
					while (temp.next()) {
						String aMovie = temp.getString(3) + ","
								+ temp.getString(4);
						movieList.add(aMovie);
					}
				}

				for (int i = table.getRowCount() - 1; i >= 0; i--) {
					for (int j = 0; j < movieList.size(); j++) {
						String temp = (String) movieList.toArray()[j];
						String[] data = temp.split(",");
						boolean checkName = data[0].equals(table.getValueAt(i,
								0));
						boolean checkYear = data[1].equals(table.getValueAt(i,
								1));
						if (!(checkName && checkYear))
							myModel.removeRow(i);
					}
				}
				myModel.fireTableDataChanged();

			} catch (SQLException e) {
				e.printStackTrace();
			}
			check = true;
		}

		if (this.nameIsValid(field4.getText())) {
			try {
				ResultSet actor = this.getActor(field4.getText());
				ArrayList<String> idList = new ArrayList<String>();
				while (actor.next()) {
					idList.add(actor.getString(1));
				}
				actor.close();
				ArrayList<String> movieList = new ArrayList<String>();
				for (int i = 0; i < idList.size(); i++) {
					ResultSet temp = this
							.getMovieA((String) idList.toArray()[i]);
					while (temp.next()) {
						String aMovie = temp.getString(3) + ","
								+ temp.getString(4);
						movieList.add(aMovie);
					}
				}

				for (int i = table.getRowCount() - 1; i >= 0; i--) {
					for (int j = 0; j < movieList.size(); j++) {
						String temp = (String) movieList.toArray()[j];
						String[] data = temp.split(",");
						// System.out.println(data[0]+data[1]);
						boolean checkName = data[0].equals(table.getValueAt(i,
								0));
						boolean checkYear = data[1].equals(table.getValueAt(i,
								1));
						if (!(checkName && checkYear))
							myModel.removeRow(i);
					}
				}
				myModel.fireTableDataChanged();

			} catch (SQLException e) {
				e.printStackTrace();
			}

			check = true;
		}
		// ArrayList<String> aList = new ArrayList<String>();
		// for (int i = 0; i < type.toArray().length; i++) {
		//
		// boolean sl = checkArray[i].isSelected();
		// if (sl) {
		// String aGenre = checkArray[i].getText();
		// try {
		// ResultSet temp = this.getMovieG(aGenre);
		// while (temp.next()) {
		// String str = temp.getString(1) + ","
		// + temp.getString(2);
		// aList.add(str);
		// }
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		//
		// }
		//
		// for (int i = table.getRowCount() - 1; i >= 0; i--) {
		// boolean checkName = false;
		// boolean checkYear = false;
		// for (int j = 0; j < aList.size(); j++) {
		// String temp = (String) aList.toArray()[j];
		// String[] data = temp.split(",");
		// // System.out.println(data[0]+data[1]);
		// checkName = data[0].equals(table.getValueAt(i, 0));
		// checkYear = data[1].equals(table.getValueAt(i, 1));
		// if (checkName && checkYear)
		// break;
		// }
		// if (!(checkName && checkYear))
		// myModel.removeRow(i);
		// }
		return check;
	}

	public boolean yearIsValid(String aString) {
		if (aString.equals(""))
			return false;
		if (aString.length() > 4)
			return false;
		for (int i = 0; i < aString.length(); i++) {
			if (!Character.isDigit(aString.charAt(i)))
				return false;
		}

		return true;
	}

	public boolean nameIsValid(String aString) {
		for (int i = 0; i < aString.length(); i++) {
			if (!(Character.isLetter(aString.charAt(i))
					|| aString.charAt(i) == ' ' || aString.charAt(i) == '.'))
				return false;
		}
		if (aString.equals(""))
			return false;
		return true;
	}

	public ResultSet getSchedule() throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From SCHEDULE";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getGenre() throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From GENRE";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getWriter(String aName) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		aName = aName.toLowerCase();
		String name = "%" + aName + "%";
		String sql = "SELECT * From PEOPLE WHERE PJOB = 'director' AND LOWER(PNAME) LIKE '"
				+ name + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getActor(String aName) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		aName = aName.toLowerCase();
		String name = "%" + aName + "%";
		String sql = "SELECT DISTINCT * From PEOPLE WHERE PJOB = 'actor' AND LOWER(PNAME) LIKE '"
				+ name + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getMovie(String aId) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From CREATOR WHERE PJOB = 'director' AND PID = '"
				+ aId + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getMovieA(String aId) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From ACT WHERE PJOB = 'actor' AND PID = '" + aId
				+ "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getMovieG(String aGenre) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From SCHEDULE NATURAL JOIN GENRE WHERE GENRE = '"
				+ aGenre + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getMovieInfo(String aTitle, String aYear)
			throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From MOVIE NATURAL JOIN GENRE WHERE TITLE = '"
				+ aTitle + "' AND RELEASEYEAR = '" + aYear + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet getActor(String aTitle, String aYear) throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From ACT NATURAL JOIN PEOPLE WHERE TITLE = '"
				+ aTitle + "' AND RELEASEYEAR = '" + aYear + "'";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();

		return rs;
	}

	public ResultSet getCreator(String aTitle, String aYear)
			throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From CREATOR NATURAL JOIN PEOPLE WHERE TITLE = '"
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
			ResultSet test = this.getSchedule();
			while (test.next()) {
				rowN++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSetMetaData data1;

		try {
			scheduleSet = this.getSchedule();
			data1 = scheduleSet.getMetaData();
			String[] columnNames = new String[data1.getColumnCount() - 1];
			;
			Object[][] data = new Object[rowN][data1.getColumnCount()];
			for (int i = 1; i <= data1.getColumnCount() - 1; i++) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return model;
	}

	public String showDetail(String aTitle, String aYear) throws SQLException {
		ResultSet actor = this.getActor(aTitle, aYear);
		ResultSet creator = this.getCreator(aTitle, aYear);
		ResultSet movieInfo = this.getMovieInfo(aTitle, aYear);
		String showAll;
		showAll = "Title:  " + aTitle + "\nReleaseYear:  " + aYear
				+ "\nDirector & Writer:  ";
		while (creator.next()) {
			if (creator.getString(2).equals("director"))
				showAll += creator.getString(5) + "(director),";
			else
				showAll += creator.getString(5) + "(writer),";
		}

		showAll += "\nActor:  ";
		while (actor.next()) {
			showAll += actor.getString(6) + " As " + actor.getString(5) + ",";
		}
		movieInfo.next();
		showAll += "\nLasting Time:  " + movieInfo.getString(3);
		showAll += "\nPG-Rating:  " + movieInfo.getString(4);
		showAll += "\nGenre:  " + movieInfo.getString(5) + ",";
		while (movieInfo.next()) {
			showAll += movieInfo.getString(5) + ",";
		}

		return showAll;
	}

	// public static void main(String[] args) {
	// new MemberSearch(id ,name);
	// }

}
