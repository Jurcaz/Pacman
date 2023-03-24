package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import com.jpg.pacman.graphics.gameboard.Coordinate;
import com.jpg.pacman.graphics.gameboard.GameBoard;

public class Inky extends Ghost {

	/* Azul
	 * No es tan rápido como Blinky. Calcula la distancia en línea recta entre Blinky y Pac-man y
	 * lo gira 180 grados, así que Inky siempre colabora con Blinky para acorralar a Pac-man
	 * */

	public Inky (GameBoard mapa) {
		super (mapa);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/chars/Inky_D.png"));
		imagen = ii.getImage();
		POSXINI = 14;
		POSYINI = 13;
		this.muevePosicionInicial();
		width = imagen.getWidth(null);
		height = imagen.getHeight(null);
		comestible = false;
		activo = true;
		velocidad =1;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void mover() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Coordinate findObjetive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void goOutCage() {
		// TODO Auto-generated method stub
		
	}

}
