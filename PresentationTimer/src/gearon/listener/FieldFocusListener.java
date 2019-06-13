package gearon.listener;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
//import javax.swing.UIManager;

public class FieldFocusListener implements FocusListener {

	@Override
	public void focusGained(FocusEvent e) {
		JTextField jTextField = (JTextField) e.getSource();
		jTextField.setBackground(Color.white);
	}

	@Override
	public void focusLost(FocusEvent e) {
		JTextField jTextField = (JTextField)e.getSource();
		//jTextField.setBackground(UIManager.getColor("TextField.background"));
		jTextField.setBackground(Color.black);
	}
}