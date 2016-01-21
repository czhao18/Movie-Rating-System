package Zhao;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class MemberNew extends JFrame implements ActionListener {

	JTextField field1 = new JTextField("");
	JTextField field2 = new JTextField("");
	JTextField field3 = new JTextField("");
	JTextField field4 = new JTextField("");

	JButton ok = new JButton("OK");
	JButton cancel = new JButton("Cancel");
	JButton ins = new JButton("Instruction");
	JButton back = new JButton("Return To Main");

	public MemberNew() {
		JLabel label0 = new JLabel("Register New Member");
		JLabel label1 = new JLabel("First Name");
		JLabel label2 = new JLabel("Last Name");
		JLabel label3 = new JLabel("ID Number");
		JLabel label4 = new JLabel("ReEnter ID");

		JPanel panel = new JPanel();

		GridLayout layout = new GridLayout(0, 2, 7, 7);
		setSize(270, 200);
		panel.setLayout(layout);

		// panel.add(label0);
		// label0.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(label1);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(field1);

		panel.add(label2);
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(field2);

		panel.add(label3);
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(field3);

		panel.add(label4);
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(field4);

		panel.add(ok);
		ok.addActionListener(this);
		panel.add(cancel);
		cancel.addActionListener(this);
		panel.add(ins);
		ins.addActionListener(this);
		panel.add(back);
		back.addActionListener(this);

		setTitle("New Member");
		add(panel);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancel) {
			this.dispose();
			new MemberWelcome();
		}

		if (e.getSource() == ins) {
			String msg = "1. First and Last name must be letters!\n"
					+ "2. ID number can only contain numbers!\n"
					+ "3. Two ID inputs must be the same, or there would be\n"
					+ "    an error message!";
			JOptionPane.showMessageDialog(this, msg, "Instruction", 1);
		}

		if (e.getSource() == ok) {

			boolean first = true;
			for (int i = 0; i < field1.getText().length(); i++) {
				char temp = field1.getText().charAt(i);
				if (!Character.isLetter(temp)) {
					first = false;
				}
			}

			boolean last = true;
			for (int i = 0; i < field2.getText().length(); i++) {
				char temp = field2.getText().charAt(i);
				if (!Character.isLetter(temp)) {
					last = false;
				}
			}

			boolean number = true;
			for (int i = 0; i < field3.getText().length(); i++) {
				char temp = field3.getText().charAt(i);
				if (!Character.isDigit(temp)) {
					number = false;
				}
			}

			boolean length = true;
			if (field3.getText().length() != 6)
				length = false;

			if (!first) {
				JOptionPane.showMessageDialog(this,
						"Firstname must be letters!", "Error", 0);
				field1.setText("");

			} else if (!last) {
				JOptionPane.showMessageDialog(this,
						"Lastname must be letters!", "Error", 0);
				field2.setText("");
			} else if (!number) {
				JOptionPane.showMessageDialog(this, "ID must be numbers!",
						"Error", 0);
				field3.setText("");
				field4.setText("");
			} else if (!length) {
				JOptionPane.showMessageDialog(this, "ID is 6 digit long!",
						"Error", 0);
				field3.setText("");
				field4.setText("");
			} else if (!field3.getText().equals(field4.getText())) {
				JOptionPane.showMessageDialog(this,
						"Two ID Inputs are not the same!", "Error", 0);
				field3.setText("");
				field4.setText("");
			} else if (field1.getText().equals("")
					|| field2.getText().equals("")
					|| field3.getText().equals("")
					|| field4.getText().equals("")) {
				JOptionPane.showMessageDialog(this, "Input can not be empty",
						"Error", 0);
			} else
				try {
					if (this.exist(field3.getText())) {
						JOptionPane.showMessageDialog(this,
								"ID Already Exist!", "Error", 0);
						field3.setText("");
						field4.setText("");
					} else {
						/*
						 * Create new member tuple
						 */
						String fName = field1.getText() + " "
								+ field2.getText();
						String fId = field3.getText();

						this.add(fId, fName, "member");
						this.dispose();
						new MemberMain(fId, fName);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		}

		if (e.getSource() == back) {
			this.dispose();
			new Welcome();
		}
	}

	public void add(String aId, String aName, String aClass)
			throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "INSERT INTO USERS VALUES ('" + aId + "','" + aName
				+ "','" + aClass + "')";
		ps = con.prepareStatement(sql);
		ps.executeUpdate();
	}

	public ResultSet getResult() throws SQLException {
		Connection con = MemberWelcome.con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * From USERS";
		ps = con.prepareStatement(sql);
		rs = ps.executeQuery();
	
		return rs;
	}

	public boolean exist(String target) throws SQLException {
		boolean temp = false;
		ResultSet rs = getResult();
		while (rs.next()) {
			if (rs.getString(1).equals(target)) {
				temp = true;
			}

		}
		return temp;
	}

	// public static void main(String[] args) {
	// new MemberNew();
	// }

}
