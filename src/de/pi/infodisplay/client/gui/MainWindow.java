package de.pi.infodisplay.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.client.Information;
import de.pi.infodisplay.shared.packets.PacketClientOutInfoUpdate;
import java.awt.BorderLayout;
import java.awt.Color;

public class MainWindow {
		
	private ProgressWindow downloadProgress;

	private Client parent;
	private JFrame frmInformationdisplay;
	private JTabbedPane tabbedPane;
		
	/**
	 * Create the application.
	 */
	public MainWindow(Client parent) {
		this.parent = parent;
		initialize();
		this.frmInformationdisplay.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 *
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
        // Erzeugung eines neuen Dialoges
        frmInformationdisplay = new JFrame();
        frmInformationdisplay.setTitle("InformationDisplay");
        frmInformationdisplay.setSize(450,750);
        
        JMenuBar menuBar = new JMenuBar();
        frmInformationdisplay.setJMenuBar(menuBar);
        
        JMenu mnNewMenu = new JMenu("Optionen");
        menuBar.add(mnNewMenu);
        
        JMenuItem mntmMitServerVerbinden = new JMenuItem("Mit Server verbinden...");
        mntmMitServerVerbinden.addActionListener(action -> {
        	ConnectDialog dialog = new ConnectDialog(parent);
        	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        	dialog.setVisible(true);
        });
        mnNewMenu.add(mntmMitServerVerbinden);
        
        JMenuItem mntmDatenstandAktualisieren = new JMenuItem("Datenstand aktualisieren");
        mnNewMenu.add(mntmDatenstandAktualisieren);
		mntmDatenstandAktualisieren.addActionListener(action -> {
			PacketClientOutInfoUpdate update = new PacketClientOutInfoUpdate();
			parent.getNettyClient().sendPacket(update);
		});
		
		JMenuItem mntmBeenden = new JMenuItem("Beenden");
		mntmBeenden.addActionListener(action -> {
			parent.getNettyClient().disconnect();
			System.exit(0);
		});
		
		JMenuItem mntmNeueInformationHinzufgen = new JMenuItem("Neue Information hinzufügen");
		mntmNeueInformationHinzufgen.addActionListener(action -> {
			InformationAddDialog dialog = new InformationAddDialog(parent);
        	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		});
		mnNewMenu.add(mntmNeueInformationHinzufgen);
		mnNewMenu.add(mntmBeenden);
		
		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);
		
		JMenuItem mntmEinstellungen = new JMenuItem("Einstellungen");
		mnBearbeiten.add(mntmEinstellungen);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		frmInformationdisplay.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
		JPanel test = new JPanel();
		test.setBackground(Color.CYAN);
		tabbedPane.add("Test", test);
      
             
        // Wir lassen unseren Dialog anzeigen
        frmInformationdisplay.setVisible(true);
        
        boolean running = true;
        
        Runnable runnable = () -> {

				while(running) {
					try {
						Thread.sleep(2L * 1000L);
					} catch (InterruptedException e) {
						Main.LOG.log(Level.WARNING, "Cannot sleep thread for 2 seconds", e);
						Thread.currentThread().interrupt();
					}
						
					if(tabbedPane.getTabCount() > 0)
						tabbedPane.setSelectedIndex((tabbedPane.getSelectedIndex() +1) % tabbedPane.getTabCount());
						
				}
        };
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public void addPanelsToList() {
		List<Information> infos = parent.getNettyClient().getInformationManager().getInformations();
		System.out.println(infos.size());
		infos.forEach(info -> {
			System.out.println("Erstelle information");
			JPanel panel = new JPanel();
			panel.add(new JLabel(new ImageIcon(info.getInfoFile().getAbsolutePath())));
			tabbedPane.addTab(info.getTitle(), panel);
		});
	}
	
	public synchronized void addProgress() {
		downloadProgress.increateDataCount();
		downloadProgress.setProgress(100 * downloadProgress.getActualDataCount() / downloadProgress.getAbsoluteDataCount());
		if(downloadProgress.getActualDataCount() == downloadProgress.getAbsoluteDataCount()) {
			System.out.println("Ist fertig");
			closeProgressWindow();
			addPanelsToList();
		}
	}
	
	public synchronized void createProgressWindow(int maxdata) {
		closeProgressWindow();
		downloadProgress = new ProgressWindow("Lade Infos herunter", maxdata);
		downloadProgress.setVisible(true);
	}
	
	public synchronized void closeProgressWindow() {
		if(downloadProgress != null) {
			downloadProgress.close();
		}
	}
}
