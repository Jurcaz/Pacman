package com.jpg.pacman.graphics.gameboard.utils;

import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javazoom.jl.player.Player;

public class SoundDaemon extends Thread {

	public static final String INTRO = "/sound/intro.wav";
	public static final String BALL = "/sound/atepellot.wav";
	public static final String DEATH = "/sound/died.wav";
	public static final String PASCUA1 = "/sound/P1.mp3";
	public static final String PASCUA2 = "/sound/P2.mp3";
	private static String pista = "";

	private final static Logger logger = LogManager.getLogger (SoundDaemon.class);

	public SoundDaemon() {
		/* TODO document why this constructor is empty */ }

	public static synchronized String getAudio() {
		return pista;
	}

	public static synchronized void setAudio(String audio) {
		pista = audio;
	}

	@Override
	public void run() {
		while (true) {
			if (!pista.equals("")) {
				if (pista.endsWith("mp3")) this.reproducirMP3();
				else this.reproducirWAV();
			} else
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	private void reproducirMP3() {
		try {
			InputStream inputStream = getClass().getResourceAsStream(pista);
			Player player = new Player(inputStream);
			player.play();
			while (!player.isComplete()) { Thread.sleep(1000);}
			pista="";
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reproducirWAV() {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(pista));
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
			logger.debug("Reproducir WAV: antes del slep");
			Thread.sleep(clip.getMicrosecondLength()/900);
			logger.debug("Reproducir WAV: despues del slep");
			SoundDaemon.pista="";
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
