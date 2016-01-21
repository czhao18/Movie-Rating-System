package Zhao;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public class MemberRate extends MemberSearch{
	
	public MemberRate(String aId, String aName){
		super(aId,aName);
		rateM.setVisible(true);
		rateA.setVisible(true);
		buy.setVisible(false);
		this.setTitle("Search & Rate");
	}

	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);
		
		if(e.getSource()==rateM){
			String title = "", year = "";
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.isRowSelected(i)) {
					title = (String) table.getValueAt(i, 0);
					year = (String) table.getValueAt(i, 1);
					break;
				}
			}
			if (title.equals("")) {
					this.dispose();
					new MemberRateMovie(id,name);
			} else {
					this.dispose();
					new MemberRateMovie(id, name, title, year);				
			}
		}
		
		if(e.getSource()==rateA){
			String title = "", year = "";
			for (int i = 0; i < table.getRowCount(); i++) {
				if (table.isRowSelected(i)) {
					title = (String) table.getValueAt(i, 0);
					year = (String) table.getValueAt(i, 1);
					break;
				}
			}
			if (title.equals("")) {
					this.dispose();
					new MemberRateActor(id,name);
			} else {
					this.dispose();
					new MemberRateActor(id, name, title, year);				
			}
		}
	}
//	 public static void main(String[] args) {
//	 new MemberRate();
//	 }

}
