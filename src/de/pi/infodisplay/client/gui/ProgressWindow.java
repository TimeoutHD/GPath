package de.pi.infodisplay.client.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ProgressWindow extends JFrame implements Runnable {

	private static final long serialVersionUID = 5760632333934168050L;
	private volatile boolean exit;
	
	private JPanel contentPane;
	
	private JProgressBar progressBar;
	private JLabel titleLabel;
	
	private String title;
	private int absoluteDataCount;

	/**
	 * Create the frame.
	 */
	public ProgressWindow(String title, int absoluteDataCount) {
		this.absoluteDataCount = absoluteDataCount;
		this.title = title;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		titleLabel.setText(title);
	}
	
	public int getAbsoluteDataCount() {
		return absoluteDataCount;
	}

	@Override
	public void run() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(12, 142, 414, 25);
		contentPane.add(progressBar);
		
		titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(41, 53, 364, 15);
		contentPane.add(titleLabel);
	}

	public void exit() {
		this.setVisible(false);
		this.dispose();
		exit = true;
	}
	
	public boolean isRunning() {
		return !exit;
	}
}
