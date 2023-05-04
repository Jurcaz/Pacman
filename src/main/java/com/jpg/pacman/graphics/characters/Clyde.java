package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.Coordinate;
import com.jpg.pacman.graphics.gameboard.GameBoard;

public class Clyde extends Ghost {
	
	private int velocidadFantasma = 500;
	private DirectionEnum[] wayOut = {DirectionEnum.UP,DirectionEnum.LEFT,DirectionEnum.UP,DirectionEnum.UP};

	/**Naranja
	 * Persigue a Pac-man directamente igual que Blinky, sin embargo considerando al propio Pac-man
	 * en un círculo de ocho espacios en cualquier dirección, Clyde huye cuando se acerca demasiado
	 * a él moviéndose a la esquina inferior izquierda del laberinto.
	 * Dado que los fantasmas no pueden girar a la dirección opuesta de su movimiento actual Clyde
	 * se verá forzado a chocar con Pac-man en caso de encontrarse en frente de él.
	 * */

	private final static Logger logger = LogManager.getLogger (Clyde.class);

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
//				if(outCage) {findObjetive();
//					if(toClose()) {
//						frightened();
//					} else {
//						chase();
//					}
//				
//				} else {
//					goOutCage();
//				}
//			}

		}

	}

	@Override
	protected void chase() {
		if(toClose()) {
			frightened();
		} else if(isIntersection()) {
			findShortestPathLength(this.tablero.getMap(), ((int) this.getX()), ((int) this.getY()), ((int) findObjetive().getX()), ((int) findObjetive().getY()));

			if(rightBoolean) {
				this.currentDirection = DirectionEnum.RIGHT;
			}else if(downBoolean) {
				this.currentDirection = DirectionEnum.DOWN;
			}else if(leftBoolean) {
				this.currentDirection = DirectionEnum.LEFT;
			}else if(upBoolean) {
				this.currentDirection = DirectionEnum.UP;
			}

			go(this.currentDirection);
		} else if(currentDirectionAllowed()){
			go(this.currentDirection);
		} else {
			findShortestPathLength(this.tablero.getMap(), ((int) this.getX()), ((int) this.getY()), ((int) findObjetive().getX()), ((int) findObjetive().getY()));

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
		return new Coordinate(this.tablero.getPacman().getX(), this.tablero.getPacman().getY());
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
	
	private boolean toClose() {
		if( (findObjetive().getX() - this.x) <= 8 || (findObjetive().getY() - this.y) <= 8 ) return true;
		
		return false;
		
	}

}
