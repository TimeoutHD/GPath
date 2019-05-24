package de.pi.infodisplay.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.shared.packets.PacketClientOutAuthorizeUser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6075276325929184556L;
	
	private final JPanel contentPanel = new JPanel();
	
	private Client parent;
	private JTextField hostField;
	private JTextField portField;

	/**
	 * Create the dialog.
	 */
	public ConnectDialog(Client parent) {
		this.parent = parent;
		setTitle("Mit Server verbinden");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
			JLabel title = new JLabel("Mit Server verbinden");
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setBounds(152, 0, 118, 43);
			title.setLabelFor(this);
			title.setFont(new Font("Arial", Font.PLAIN, 12));
			contentPanel.add(title);
		
		
			JTextField usernameField = new JTextField();
			usernameField.setBounds(134, 131, 276, 22);
			contentPanel.add(usernameField);
		
		JLabel usernameTitle = new JLabel("Benutzername:");
		usernameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		usernameTitle.setBounds(10, 131, 118, 22);
		contentPanel.add(usernameTitle);
		
		JLabel passwordTitle = new JLabel("Passwort:");
		passwordTitle.setHorizontalAlignment(SwingConstants.CENTER);
		passwordTitle.setBounds(10, 163, 118, 22);
		contentPanel.add(passwordTitle);
		
		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(134, 163, 276, 22);
		contentPanel.add(passwordField);
		
		hostField = new JTextField();
		hostField.setBounds(134, 54, 276, 20);
		contentPanel.add(hostField);
		hostField.setColumns(10);
		
		portField = new JTextField();
		portField.setBounds(134, 85, 276, 20);
		contentPanel.add(portField);
		portField.setColumns(10);
		
		JLabel hostTitle = new JLabel("Server-IP:");
		hostTitle.setHorizontalAlignment(SwingConstants.CENTER);
		hostTitle.setBounds(10, 54, 118, 20);
		contentPanel.add(hostTitle);
		
		JLabel portTitle = new JLabel("Server-Port:");
		portTitle.setHorizontalAlignment(SwingConstants.CENTER);
		portTitle.setBounds(10, 85, 118, 20);
		contentPanel.add(portTitle);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						String host = hostField.getText();
						String portString = portField.getText();
						if(portString.matches("\\d+")) {
							int port = Integer.valueOf(portString);
							
							if(parent.connectToServer(host, port)) {
								String username = usernameField.getText();
								String password = String.valueOf(passwordField.getPassword());
								if(username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
									PacketClientOutAuthorizeUser authorize = new PacketClientOutAuthorizeUser(usernameField.getText(), String.valueOf(passwordField.getPassword()));
									parent.getNettyClient().sendPacket(authorize);
								}
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public Client getClient() {
		return parent;
	}
	
	public void closeDialog() {
		this.setVisible(false);
		this.dispose();
	}
}
