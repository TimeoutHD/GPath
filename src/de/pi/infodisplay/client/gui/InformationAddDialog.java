package de.pi.infodisplay.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.shared.packets.PacketClientOutAddInformation;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class InformationAddDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2853952738758845183L;
	
	private Client parent;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField titleField;
	private JTextField pathField;

	/**
	 * Create the dialog.
	 */
	public InformationAddDialog(Client parent) {
		this.parent = parent;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNeueInformationHinzufgen = new JLabel("Neue Information hinzufügen");
		lblNeueInformationHinzufgen.setBounds(12, 12, 414, 35);
		lblNeueInformationHinzufgen.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblNeueInformationHinzufgen);
		
		JLabel lblTitel = new JLabel("Titel");
		lblTitel.setBounds(41, 59, 49, 22);
		contentPanel.add(lblTitel);
		
		titleField = new JTextField();
		titleField.setBounds(97, 61, 329, 19);
		contentPanel.add(titleField);
		titleField.setColumns(10);
		
		JLabel lblDatei = new JLabel("Datei:");
		lblDatei.setHorizontalAlignment(SwingConstants.CENTER);
		lblDatei.setBounds(20, 116, 70, 22);
		contentPanel.add(lblDatei);
		
		pathField = new JTextField();
		pathField.setEditable(false);
		pathField.setBounds(97, 118, 278, 19);
		contentPanel.add(pathField);
		pathField.setColumns(10);
		
		JButton fileChooserButton = new JButton("");
		fileChooserButton.addActionListener(action -> {
			JFrame frame = new JFrame();
			
			PNGFileChooser chooser = new PNGFileChooser(this);
			chooser.showOpenDialog(frame);
			chooser.setVisible(true);
		});
		fileChooserButton.setBounds(387, 115, 39, 25);
		contentPanel.add(fileChooserButton);
			
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(action -> {
			try {
				PacketClientOutAddInformation packet = new PacketClientOutAddInformation(titleField.getText(), new File(pathField.getText()));
				parent.getNettyClient().sendPacket(packet);
				dispose();
			} catch (IOException e) {
				Main.LOG.log(Level.SEVERE, "Packet kann nicht erstellt werden", e);
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}
	
	public void setPathField(String path) {
		this.pathField.setText(path);
	}
	
	public Client getClientParent() {
		return parent;
	}
 }
