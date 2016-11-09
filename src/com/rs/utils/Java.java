package com.rs.utils;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Java extends JFrame {
	
	/**
	 * Farming Progress Bar, Percentage until harvest.
	 */

	private static final long serialVersionUID = 1L;
	JProgressBar current;
	JTextArea out;
	JButton find;
	Thread runner;
	int num = 0;
	
	public Java() {
		super("Farming Progress Bar");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLookAndFeel();
		setSize(300, 68);
		setLayout(new FlowLayout());
		current = new JProgressBar(0, 2000);
		current.setValue(0);
		current.setStringPainted(true);
		add(current);
	}
	
	public void iterate() {
		while (num < 2000) {
			current.setValue(num);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { 
			}
			num += 1;
			}
		}
	
	public void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			System.err.println("Failed to use the system look and feel" +e);
		}
	}
	
	public static void main() {
		Java frame = new Java();
		frame.setVisible(true);
		frame.iterate();
	}
}