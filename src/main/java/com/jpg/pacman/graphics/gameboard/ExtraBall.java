package com.jpg.pacman.graphics.gameboard;

import javax.swing.ImageIcon;

public class ExtraBall extends Element {
	public static final int PUNTUACION  = 10;

	public ExtraBall (int x, int y) {
		this.positionX = x;
		this.positionY = y;
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/gameboard/superpill.png"));
		this.setImagen(ii.getImage());
	}
}
