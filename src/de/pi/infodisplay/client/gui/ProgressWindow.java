package de.pi.infodisplay.client.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

public class ProgressWindow extends JFrame {

	private static final long serialVersionUID = -9076607663002763621L;
	private JPanel contentPane;
	
	private int absoluteDatacount;
	private int actualDataCount;
	
	private JLabel titleField;
	private JProgressBar progressBar;

	/**
	 * Create the frame.
	 */
	public ProgressWindow(String title, int absoluteDataCount) {
		this.absoluteDatacount = absoluteDataCount;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		titleField = new JLabel(title);
		titleField.setHorizontalAlignment(SwingConstants.CENTER);
		titleField.setBounds(12, 36, 414, 21);
		contentPane.add(titleField);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(12, 157, 414, 21);
		contentPane.add(progressBar);
	}
	
	public void close() {
		this.setVisible(false);
		this.dispose();
	}
	
	public void setTitle(String title) {
		this.titleField.setText(title);
	}
	
	public int getAbsoluteDataCount() {
		return absoluteDatacount;
	}
	
	public int getActualDataCount() {
		return actualDataCount;
	}
	
	public void increateDataCount() {
		if(actualDataCount < absoluteDatacount) actualDataCount++;
	}
	
	public void setProgress(int progress) {
		this.progressBar.setValue(progress);
	}
}
