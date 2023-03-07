package com.jpg.pacman.graphics.gameboard;

import javax.swing.ImageIcon;

public class Wall extends Element {

	public Wall (int x, int y) {
		this.positionX 	= x;
		this.positionY	= y;
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/gameboard/floor.png"));
		this.setImagen(ii.getImage());

	}

}
