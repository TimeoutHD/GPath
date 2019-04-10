package de.pi.infodisplay.client;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import de.pi.infodisplay.shared.packets.Packet;
import de.pi.infodisplay.shared.packets.PacketClientOutDisconnect;
import de.pi.infodisplay.shared.packets.PacketClientOutInfo;

@Deprecated
public class Console extends JFrame {

	private static final long serialVersionUID = -7727966330078490710L;
	
	private JTextField commandline = new JTextField();
	private JTextArea output = new JTextArea("");
	private JScrollPane outputscrollpane = new JScrollPane(output);
	
	private Handler handler;
	
	private Client parent;
   
	public Console(String title, Client parent) { 
		super(title);
		handler = new Handler(this);
		this.parent = parent;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int frameWidth = 600; 
		int frameHeight = d.height - 45;
		setSize(frameWidth, frameHeight);
		setSize(frameWidth, frameHeight);
		setResizable(false);
		
		int x = 0;
		int y = 0;
		setLocation(x, y);
    	setResizable(false);
    	Container cp = getContentPane();
    	cp.setLayout(null);
    
    	output.setEditable(false);
    	outputscrollpane.setBounds(0, 0, 595, frameHeight-52);
    	cp.add(outputscrollpane);
    	commandline.setBounds(0, frameHeight-45, 595, 20);
    	cp.add(commandline);
    
    	commandline.requestFocusInWindow();
    	
    	commandline.addKeyListener(handler);
    	
    	commandline.requestFocusInWindow();
    	
    	setVisible(true);
    	
	}
	
	public JTextField getCommandline() {
		return commandline;
	}

	public JTextArea getOutput() {
		return output;
	}

	public JScrollPane getOutputscrollpane() {
		return outputscrollpane;
	}

	public Handler getHandler() {
		return handler;
	}

	public void print(String str) {
		String existing = output.getText();
		output.setText(existing + str);
	}
	
	public void printLine(String str) {
		String existing = output.getText();
		output.setText(existing + str + "\n");
	}
	
	public void evaluate(String cmd) {
		String[] split = cmd.split(" ");
		Command c = Command.getCommandByName(split[0]);
		if(c != null) {
			if(split.length > 1) {
				String[] args = IntStream.range(1, split.length).mapToObj(i -> split[i]).toArray(String[]::new);
				System.out.println(Arrays.toString(args));
				execute(c, args);
			}
			else  {
				execute(c, new String[0]);
			}
			return;
		}
		printLine("No valid Command!");
	}
	
	public void execute(Command cmd, String[] args) {
		Packet packet;
		switch(cmd) {
		case KILL:
			packet = new PacketClientOutDisconnect();
			this.parent.getNettyClient().sendPacket(packet);
			System.exit(0);
		case SEND:
			packet = new PacketClientOutInfo(String.join(" ", args));
			System.out.println(this.parent.getNettyClient());
			this.parent.getNettyClient().sendPacket(packet);
			printLine("Sent.");
			break;
		case CONNECT:
			printLine("Connected.");
			break;
		case DISCONNECT:
			packet = new PacketClientOutDisconnect();
			this.parent.getNettyClient().sendPacket(packet);
			printLine("Disconnected. -_-");
			break;
		case HELP:
			printHelp();
			break;
		case CLEAR:
			clear();
			break;
		default:
			break;
		}
	}
	
	private void printHelp() {
		printLine("HELP:");
		for(Command c : Command.values()) {
			print(c.toString());
			print(" [");
			print(c.getFullCommand());
			print(", ");
			print(c.getShortCommand());
			printLine("]");
		}
	}

	public void clear() {
		output.setText("");
	}

	public Client getNettyParent() {
		return parent;
	}
}
