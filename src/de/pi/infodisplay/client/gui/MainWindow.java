package de.pi.infodisplay.client.gui;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import de.pi.infodisplay.client.Client;

public class MainWindow {

	private static final String filePrefix = "";
	private Client parent;
	private JDialog frmInformationdisplay;
	
	private JTabbedPane tabbackup;
	private static List<Information> infos = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Client();
	}

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
        frmInformationdisplay = new JDialog();
        frmInformationdisplay.setTitle("JPanel Beispiel");
        frmInformationdisplay.setSize(450,750);
 
        // Hier erzeugen wir unsere JPanels
        JPanel panelRot = new JPanel();
        JPanel panelBlue = new JPanel();
        JPanel panelGreen = new JPanel();
        JPanel panelYellow = new JPanel();
        JPanel panelPink = new JPanel();
        JPanel panelBlack = new JPanel();
 
      
		// Hier setzen wir die Hintergrundfarben für die JPanels
        panelRot.setBackground(Color.RED);
        panelBlue.setBackground(Color.BLUE);
        panelGreen.setBackground(Color.GREEN);
        panelYellow.setBackground(Color.YELLOW);
        panelPink.setBackground(Color.PINK);
        panelBlack.setBackground(Color.BLACK);
 
        // Erzeugung eines JTabbedPane-Objektes
        JTabbedPane tabpane = new JTabbedPane
            (JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );
 
        // Hier werden die JPanels als Registerkarten hinzugefügt
        showInfos(tabpane);
 
        tabbackup = tabpane;
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
        
      
             
        // Wir lassen unseren Dialog anzeigen
        frmInformationdisplay.setVisible(true);
        
        boolean running = true;
        
        Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				while(running) {
					try {
						Thread.sleep(2 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					tabpane.setSelectedIndex((tabpane.getSelectedIndex() +1) % tabpane.getTabCount());
					
				}
			}
        };
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	private void showInfos(JTabbedPane tabpane) {
		tabpane.removeAll();
		for(Information i : infos) {
			tabpane.addTab(i.getTitle(), new JLabel(new ImageIcon(new File(filePrefix, i.getFilePath()).getAbsolutePath())));
		}
	}
	
	
	public void addInfo(Information info) {
		infos.add(info);
		showInfos(tabbackup);
	}
	
	public void removeInfo(Information info) {
		infos.remove(info);
		showInfos(tabbackup);
	}
}
