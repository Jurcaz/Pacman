package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.Coordinate;
import com.jpg.pacman.graphics.gameboard.GameBoard;

public class Pinky extends Ghost {

	//tiempo en milisegundos entre movimientos del fantasma
	private int velocidadFantasma = 500;
	private DirectionEnum[] wayOut = {DirectionEnum.UP,DirectionEnum.UP,DirectionEnum.UP};

	private final static Logger logger = LogManager.getLogger (Pinky.class);

	/*	Rosa
	 *  Su blanco son cuatro espacios ubicados a la derecha, izquierda y abajo de pacman
	 *  cuando este mira en tales direcciones, cuando Pac-man mira hacia arriba,
	 *  el blanco es cuatro espacios arriba y dos a la izquierda.
	 *  Esto hace que Pinky trate de atrapar a Pac-man por enfrente mientras Blinky lo persigue por detrás.*/

	public Pinky (GameBoard mapa) {
		super (mapa);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/chars/Pinky_D.png"));
		imagen = ii.getImage();
		POSXINI = 13;
		POSYINI = 13;
		this.muevePosicionInicial();
		width = imagen.getWidth(null);
		height = imagen.getHeight(null);
		comestible = false;
		activo = true;
		velocidad =1;
		frightened = false;
	}

	@Override
	public void run() {

		while (!this.getMapa().isPartidaAcabada()) {
			//vuelve a su posición de inicio
			if (this.getMapa().isReiniciarPosiciones()) {
				x = POSXINI;
				y = POSYINI;
			}
			try {
				Thread.sleep(velocidadFantasma);
			} catch (InterruptedException e) {
				logger.error("Excepción en el sleep "+e.getMessage());
			}

//			if(isComestible()) {
//				frightened();
//			} else {
//				if(outCage) {
//					System.out.println("x " + findObjetive().getX() + " y " + findObjetive().getY());
//					chase();
//
//				} else {
//					goOutCage();
//				}
//			}

		}
	}

	@Override
	protected void chase() {
		if(isIntersection()) {
			findShortestPathLength(this.tablero.getMap(), ((int) this.x), ((int) this.y), ((int) findObjetive().getX()), ((int) findObjetive().getY()));

			if(rightBoolean) {
				this.currentDirection = DirectionEnum.RIGHT;
			}else if(downBoolean) {
				this.currentDirection = DirectionEnum.DOWN;
			}else if(leftBoolean) {
				this.currentDirection = DirectionEnum.LEFT;
			}else if(upBoolean) {
				this.currentDirection = DirectionEnum.UP;
			}

			if(currentDirectionAllowed()) go(this.currentDirection);
		} else if(currentDirectionAllowed()){
			go(this.currentDirection);
		} else {
			findShortestPathLength(this.tablero.getMap(), ((int) this.x), ((int) this.y), ((int) findObjetive().getX()), ((int) findObjetive().getY()));

			if(rightBoolean) {
				this.currentDirection = DirectionEnum.RIGHT;
			}else if(downBoolean) {
				this.currentDirection = DirectionEnum.DOWN;
			}else if(leftBoolean) {
				this.currentDirection = DirectionEnum.LEFT;
			}else if(upBoolean) {
				this.currentDirection = DirectionEnum.UP;
			}
		}
		this.frightened = true;
	}

	@Override
	protected Coordinate findObjetive() {
		int auxX = (int) this.tablero.getPacman().getX();
		int auxY = (int) this.tablero.getPacman().getY();

		switch (this.tablero.getPacman().currentDirection) {
			case UP:
				auxY = auxY - 4;
				auxX = auxX - 2;

				while (!(moveAllowed(auxX, auxY))) {
					auxX = auxX + 1;
				}

				if(moveAllowed(auxX,auxY)) return new Coordinate(auxX, auxY);

				while(!(moveAllowed(auxX, auxY))) {
					auxY = auxY + 1;
				}

				if(moveAllowed(auxX,auxY)) return new Coordinate(auxX, auxY);

			case RIGHT:
				auxX = auxX + 4;
				while(!(moveAllowed(auxX, auxY))) {
					auxX = auxX - 1;
				}

				if(moveAllowed(auxX,auxY)) return new Coordinate(auxX, auxY);

			case DOWN:
				auxY = auxY + 4;
				while (!(moveAllowed(auxX,auxY))) {
					auxY = auxY - 1;
				}

				if(moveAllowed(auxX,auxY)) return new Coordinate(auxX, auxY);

			case LEFT:
				auxX = auxX -4;
				while(!(moveAllowed(auxX, auxY))) {
					auxX = auxX + 1;
				}

				if(moveAllowed(auxX,auxY)) return new Coordinate(auxX, auxY);
		}
		return null;
	}

	@Override
	protected void goOutCage() {
			try {
				this.currentDirection = this.wayOut[out];
				go(this.currentDirection);
				out++;
			} catch (IndexOutOfBoundsException e) {
				this.outCage = true;
			}

	}
}
