package de.pi.infodisplay.client;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Handler implements KeyListener {
	
	private Console mother;
	
	public Handler(Console mother) {
		this.mother = mother;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(!mother.getCommandline().getText().isEmpty() && e.getKeyChar() == '\n') {
			mother.evaluate(mother.getCommandline().getText());
			mother.getCommandline().setText("");
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//nothing
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//nothing		
	}
}
