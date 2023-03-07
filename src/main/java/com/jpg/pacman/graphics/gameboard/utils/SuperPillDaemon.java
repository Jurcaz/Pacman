package com.jpg.pacman.graphics.gameboard.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.GameBoard;

public class SuperPillDaemon extends Thread{

	private final static Logger logger = LogManager.getLogger (SuperPillDaemon.class);
	private GameBoard juego;

	public SuperPillDaemon (GameBoard juego) {
		this.juego = juego;
	}
	@Override
	public void run () {
		logger.debug("Inicio del demonio");
		int seg = 0, acum = 0;
		while (acum<GameBoard.getSegundosRestantesSuperPill()) {
			seg = GameBoard.getSegundosRestantesSuperPill() - acum;
			try {
				Thread.sleep(seg*1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
			acum += seg;
		}
		logger.debug("Fin del demonio");
		juego.setHuirFantasmas(false);
	}

}
