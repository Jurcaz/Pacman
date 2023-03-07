package com.jpg.pacman.graphics.gameboard;

import javax.swing.ImageIcon;

public class Ball extends Element {
	public static final int PUNTUACION  = 5;

	public Ball (int x, int y) {
		this.positionX = x;
		this.positionY = y;
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/gameboard/pill.png"));
		this.setImagen(ii.getImage());
	}
}
