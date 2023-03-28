package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import com.jpg.pacman.graphics.gameboard.Coordinate;
import com.jpg.pacman.graphics.gameboard.GameBoard;

public class Clyde extends Ghost {

	/**Naranja
	 * Persigue a Pac-man directamente igual que Blinky, sin embargo considerando al propio Pac-man
	 * en un círculo de ocho espacios en cualquier dirección, Clyde huye cuando se acerca demasiado
	 * a él moviéndose a la esquina inferior izquierda del laberinto.
	 * Dado que los fantasmas no pueden girar a la dirección opuesta de su movimiento actual Clyde
	 * se verá forzado a chocar con Pac-man en caso de encontrarse en frente de él.
	 * */

	public Clyde (GameBoard mapa) {
		super (mapa);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/chars/Clyde_D.png"));
		imagen = ii.getImage();
		POSXINI = 15;
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
	protected void chase() {
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
