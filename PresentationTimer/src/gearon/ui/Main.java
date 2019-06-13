package gearon.ui;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String args[]){
		try{
			
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					MainFrame mainFrame = new MainFrame();
					mainFrame.init();
					mainFrame.render();
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
