package gearon.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.text.*;

import gearon.controller.TimeUnitController;
import gearon.listener.*;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JButton startButton = null;
	private JButton cancelButton = null;

	private JTextField minuteText = null;
	private JTextField secondText = null;

	private LimitDocumentFilter limitDocumentFilter = new LimitDocumentFilter(2);
	
	private TimeUnitController controller = new TimeUnitController();
	
	ControlPanel controlPanel;
	PresentPanel presentPanel;

	public void init() {
		setTitle("Timer");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		controlPanel = new ControlPanel();
		presentPanel = new PresentPanel();
		controlPanel.setBackground(Color.gray);
		presentPanel.setBackground(Color.gray);
		this.add(controlPanel, BorderLayout.WEST);
		this.add(presentPanel, BorderLayout.EAST);
		this.setAlwaysOnTop(true);
		this.pack();

	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int screenWidth = (int)dimension.getWidth();
	    int frameWidth = this.getWidth();
	    int x = screenWidth - frameWidth - 10;
	    int y = 10;
	    
	    setLocation(x, y);
	    initializeData();
	}

	public void render() {
		this.setVisible(true);
	}

	public void hide() {
		this.setVisible(false);
	}

	class ControlPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public ControlPanel() {
			this.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			int row = 2;
			int collum = 1;
			GridLayout gridLayout = new GridLayout(row, collum);
			gridLayout.setVgap(5);
			this.setLayout(gridLayout);
			startButton = new JButton();
			startButton.setText("Start");
			int buttonWidth = 80;
			int buttonHeight = 20;
			startButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
			
			{
				startButton.addActionListener(new StartButtonListener());
				cancelButton.addActionListener(new CancelButtonListener());
			}
			
			this.add(startButton);
			this.add(cancelButton);
		}
	}

	class PresentPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public PresentPanel() {
			minuteText = new CustomTextField();
			JLabel label = new JLabel(":");
			secondText = new CustomTextField();
			FieldFocusListener fieldFocusListener = new FieldFocusListener();
			minuteText.addFocusListener(fieldFocusListener);
			secondText.addFocusListener(fieldFocusListener);
			
			this.add(minuteText);
			this.add(label);
			this.add(secondText);
		}
	}

	class LimitDocumentFilter extends DocumentFilter {

		private int limit;

		public LimitDocumentFilter(int limit) {
			if (limit <= 0) {
				throw new IllegalArgumentException("Limit can not be <= 0");
			}
			this.limit = limit;
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
			int currentLength = fb.getDocument().getLength();
			int overLimit = (currentLength + text.length()) - limit - length;
			if (overLimit > 0) {
				text = text.substring(0, text.length() - overLimit);
			}
			if (text.length() > 0) {
				super.replace(fb, offset, length, text, attrs);
			}
		}
	}

	class CustomTextField extends JTextField {

		private static final long serialVersionUID = 1L;
		
		public CustomTextField() {
			((AbstractDocument) this.getDocument()).setDocumentFilter(limitDocumentFilter);
			this.setColumns(2);
			this.setPreferredSize(new Dimension(20, 50));
			this.setForeground(Color.green);
			this.setBackground(Color.black);
			this.setFont(new Font(Font.SERIF, Font.BOLD, 30));
			
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e){
					char c = e.getKeyChar();
					if(c < '0' || c > '9'){
						e.consume();
					}
				}
			});
			
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					/**
					 * Do nothing now
					 */
				}
			});
		}
	}
	
	private boolean started = false;
	private boolean canceled = true;
	
	class StartButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(started == false){
				started = true;
				if(canceled == true){
					canceled = false;
					int minute = Integer.parseInt(minuteText.getText());
					controller.setMinute(minute);
					initializeData();
					minuteText.setEditable(false);
					secondText.setEditable(false);
					new CustomTimer().start();
				}
				startButton.setText("Pause");
			}else{
				started = false;
				startButton.setText("Start");
			}
			
			minuteText.setFocusable(false);
			secondText.setFocusable(false);
		}
	}
	
	public void initializeData(){
		int minute = controller.getMinute();
		String minuteStr = null;
		if(minute < 10){
			minuteStr = "0" + minute; 
		}else{
			minuteStr = String.valueOf(minute);
		}
		minuteText.setText(minuteStr);
		secondText.setText("00");
	}
	
	class CancelButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			canceled = true;
			started = false;
			overtime = false;
			startButton.setText("Start");
			minuteText.setEditable(true);
			secondText.setEditable(true);
			minuteText.setForeground(Color.green);
			secondText.setForeground(Color.green);
			
			minuteText.setFocusable(true);
			secondText.setFocusable(true);
			
			initializeData();
		}	
	}
	
	private boolean overtime = false;
	
	class CustomTimer extends Thread{
		@Override
		public void run(){
			int minute = 0;
			int second = 0;
			while (true){
				if(started == true){
					String minuteStr = minuteText.getText();
					String secondStr = secondText.getText();
					try{
						minute = Integer.parseInt(minuteStr);
					}catch(Exception e){
						e.printStackTrace();
						minute = 0;
					}
					try{
						second = Integer.parseInt(secondStr);
					}catch(Exception e){
						e.printStackTrace();
						second = 0;
					}
					if(overtime == false){
						if(minute > 0){
							if(second > 0){
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								second--;
								if(second < 10){
									secondStr = "0" + second;
								}else{
									secondStr = String.valueOf(second);
								}
							}else if(second == 0){
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								second = 59;
								minute--;
								secondStr = String.valueOf(second);
								if(minute < 10){
									minuteStr = "0" + String.valueOf(minute);
								}else{
									minuteStr = String.valueOf(minute);
								}
							}
						}else if(minute == 0){
							if(second > 0){
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								second--;
								if(second < 10){
									secondStr = "0" + second;
								}else{
									secondStr = String.valueOf(second);
								}
							}else if(second == 0){
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								second = 1;
								overtime = true;
								new Alert().start();
								secondStr = "0" + String.valueOf(second);
							}
						}
					}else{
						/**
						 * overtime == true
						 */
						minuteText.setForeground(Color.red);
						secondText.setForeground(Color.red);
						
						if(second < 59){
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							second++;
							if(second < 10){
								secondStr = "0" + second;
							}else{
								secondStr = String.valueOf(second);
							}
						}else{
							/**
							 * second == 59
							 */
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							second = 0;
							minute++;
							/**
							 * Don't support minutes bigger than 100, loop to 0 instead.
							 */
							if(minute == 100){
								minute = 0;
							}
							secondStr = "00";
							if(minute < 10){
								minuteStr = "0" + minute;
							}else{
								minuteStr = String.valueOf(minute);
							}
						}
					}
					if (canceled == false) {
						secondText.setText(secondStr);
						minuteText.setText(minuteStr);
					}
					
				}else{
					/**
					 * Do nothing when timer is paused
					 */
				}
			}
		}
	}
	
	class Alert extends Thread{
		@Override
		public void run(){
			for(int i = 0; i < 5; i++){
				controlPanel.setBackground(Color.red);
				presentPanel.setBackground(Color.red);
				controlPanel.repaint();
				presentPanel.repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				controlPanel.setBackground(Color.gray);
				presentPanel.setBackground(Color.gray);
				controlPanel.repaint();
				presentPanel.repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
