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
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;

import de.pi.infodisplay.Main;
import de.pi.infodisplay.client.Client;
import de.pi.infodisplay.client.Information;
import de.pi.infodisplay.shared.packets.PacketClientOutInfoUpdate;

public class MainWindow {
	
	private final List<Information> informations = new ArrayList<>();
	
	private ProgressWindow downloadProgress;

	private Client parent;
	private JFrame frmInformationdisplay;
	private JTabbedPane tabpane;
		
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
 
 
        // Erzeugung eines JTabbedPane-Objektes
        tabpane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
        
        // TODO: Panels ins TabbedPane hinzufügen.
        
        
        // JTabbedPane wird unserem Dialog hinzugefügt
        frmInformationdisplay.getContentPane().add(tabpane);
        
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
						
					if(tabpane.getTabCount() > 0)
						tabpane.setSelectedIndex((tabpane.getSelectedIndex() +1) % tabpane.getTabCount());
						
				}
        };
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public void showInfos(JTabbedPane tabpane) {
		tabpane.removeAll();
		for(Information i : parent.getNettyClient().getInformationManager().getInformations()) {
			tabpane.addTab(i.getTitle(), new JLabel(new ImageIcon(i.getInfoFile().getAbsolutePath())));
		}
	}
	
	public void addInformations(Collection<Information> informations) {
		informations.clear();
		this.informations.addAll(informations);
	}
	
	public void addPanelsToList() {
		informations.forEach(info -> {
			JPanel panel = new JPanel();
			panel.add(new JLabel(new ImageIcon(info.getInfoFile().getAbsolutePath())));
			tabpane.addTab(info.getTitle(), panel);
		});
	}
	
	public void addProgress() {
		JProgressBar bar = downloadProgress.getProgressBar();
		bar.setValue(100 * downloadProgress.getActualDataCount() / downloadProgress.getAbsoluteDataCount());
	}
	
	public void createProgressWindow(int maxdata) {
		closeProgressWindow();
		downloadProgress = new ProgressWindow("Lade Infos herunter", maxdata);
		downloadProgress.setVisible(true);
	}
	
	public void closeProgressWindow() {
		if(downloadProgress != null) {
			downloadProgress.exit();
		}
	}
}
