package de.pi.infodisplay.client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.TextField;

import javax.swing.SwingConstants;

import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.shared.packets.PacketClientOutAuthorizeUser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConnectDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6075276325929184556L;
	
	private final JPanel contentPanel = new JPanel();
	
	private Client parent;

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
		
		
			TextField usernameField = new TextField();
			usernameField.setBounds(134, 66, 276, 22);
			contentPanel.add(usernameField);
		
		JLabel usernameTitle = new JLabel("Benutzername:");
		usernameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		usernameTitle.setBounds(10, 66, 118, 22);
		contentPanel.add(usernameTitle);
		
		JLabel passwordTitle = new JLabel("Passwort:");
		passwordTitle.setHorizontalAlignment(SwingConstants.CENTER);
		passwordTitle.setBounds(10, 99, 118, 22);
		contentPanel.add(passwordTitle);
		
		TextField passwordField = new TextField();
		passwordField.setBounds(134, 99, 276, 22);
		contentPanel.add(passwordField);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						PacketClientOutAuthorizeUser authorize = new PacketClientOutAuthorizeUser(usernameField.getText(), passwordField.getText());
						parent.getNettyClient().sendPacket(authorize);
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
