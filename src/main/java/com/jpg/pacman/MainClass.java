package com.jpg.pacman;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.utils.SoundDaemon;

public class MainClass extends JFrame{

	public MainClass() {
		add (new GameBoard());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(1200,1200);
		setLocationRelativeTo(null);
		setTitle ("----- PACMAN VERSIÓN: 0.1------");
		setVisible(true);
	}

	public static void main(String[] args) {
		SoundDaemon sd = new SoundDaemon ();
		sd.start();
		//SoundDaemon.setAudio(SoundDaemon.INTRO);
		//SoundDaemon.setAudio(SoundDaemon.PASCUA2);
		new MainClass();

	}

}
