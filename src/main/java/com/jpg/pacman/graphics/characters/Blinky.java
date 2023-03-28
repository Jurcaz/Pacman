package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.Coordinate;
import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;

public class Blinky extends Ghost {

	//tiempo en milisegundos entre movimientos del fantasma
	private int velocidadFantasma = 500;
	private DirectionEnum[] wayOut = {DirectionEnum.UP,DirectionEnum.RIGHT,DirectionEnum.UP,DirectionEnum.UP,DirectionEnum.LEFT};
	
	private final static Logger logger = LogManager.getLogger (Blinky.class);

	/*
	 *  Después de que Pac-Man coma cierta cantidad de puntos, su velocidad incrementa considerablemente
	 *  (este número disminuye en niveles más altos). Blinky persigue a Pac-man directamente.
	 * **/

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
			
			if(isComestible()) {
				frightened();
			} else {
				if(this.isOutCage()) {
					chase();
				} else {
					goOutCage();
				}
			}
			
		}
	}

	public Blinky (GameBoard mapa) {
		super (mapa);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/chars/Blinky_U.png"));
		imagen = ii.getImage();
		POSXINI = 12;
		POSYINI = 13;
		this.muevePosicionInicial();
		width = imagen.getWidth(null);
		height = imagen.getHeight(null);
		comestible = false;
		activo = true;
		velocidad = 1;
	}

	@Override
	protected void chase() {
		if(isIntersection()) {
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
		this.frightened = 0;
		
	}
	
	@Override
	protected Coordinate findObjetive() {
		return new Coordinate(this.tablero.getPacman().getX(), this.tablero.getPacman().getY());
	}

	protected void goOutCage() {
		try {
			this.currentDirection = wayOut[out];
			go(this.currentDirection);
			out++;
		} catch (IndexOutOfBoundsException e) {
			this.outCage = true;
		}
	}

}