package Zhao;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MemberMain extends JFrame implements ActionListener{
	private JButton search = new JButton("Search");
	private JButton rate = new JButton("Rate");
	private JButton order = new JButton("My Order");
	private JButton mrate = new JButton("My Rate");
	private JButton back = new JButton("Back");
	
	private String id;
	private String name;
	
	public MemberMain(String aID, String bName){
		id = aID;
		name = bName;
		String str = "Welcome";
		JLabel label = new JLabel(str);
		JLabel label1 = new JLabel(name);
		label1.setForeground(Color.RED);
		
		
		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(0,2,7,7);
		panel.setLayout(layout);
		
		JPanel panel1 = new JPanel();
		GridLayout layout1 = new GridLayout(0,1,7,7);
		panel1.setLayout(layout1);
		
		JPanel panel2 = new JPanel();
		GridLayout layout2 = new GridLayout(0,1,7,7);
		panel2.setLayout(layout2);
		
		panel2.add(search);
		search.addActionListener(this);
		panel2.add(rate);
		rate.addActionListener(this);
		panel2.add(order);
		order.addActionListener(this);
		panel2.add(mrate);
		mrate.addActionListener(this);
		panel2.add(back);
		back.addActionListener(this);
		
		panel1.add(label);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel1.add(label1);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		
		panel.add(panel1);
		panel.add(panel2);
		
		add(panel);
		setSize(300,200);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==search){
			this.dispose();
			new MemberSearch(id,name);
		}
		
		if(e.getSource()==rate){
			this.dispose();
			new MemberRate(id,name);
		}
		
		if(e.getSource()==order){
			this.dispose();
			new MemberOrder(id,name);
		}
		
		if(e.getSource()==mrate){
			this.dispose();
			new MemberRateH(id,name);
		}
		
		if(e.getSource()==back){
			this.dispose();
			new MemberWelcome();
		}
	}
//	public static void main(String[] args) {
//	new MemberMain();
//}
}
