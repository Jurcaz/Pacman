package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.Coordinate;
import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;

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
		
			if(this.outCage) {
				mover();
			} else {
				goOutCage();
			}
			
		}
	}

	@Override
	protected void mover() {
		findShortestPathLength(this.tablero.getMap(), ((int) this.x), ((int) this.y), ((int) findObjetive().getX()), ((int) findObjetive().getY()));
		
		System.out.println("Up " + this.upBoolean + " Right " + this.rightBoolean + " Down " + this.downBoolean + " Left " + this.leftBoolean);
		
		if(this.rightBoolean) {
			this.currentDirection = DirectionEnum.RIGHT;
		}else if(this.downBoolean) {
			this.currentDirection = DirectionEnum.DOWN;
		}else if(this.leftBoolean) {
			this.currentDirection = DirectionEnum.LEFT;
		}else if(this.upBoolean) {
			this.currentDirection = DirectionEnum.UP;
		}
		
		if(currentDirectionAllowed()) go(currentDirection);
		logger.debug("Pinky: x " + this.x + " y " + this.y);
	}

	@Override
	protected Coordinate findObjetive() {
		switch (this.tablero.getPacman().currentDirection) {
			case UP:
				return new Coordinate((this.tablero.getPacman().getX()-2), (this.tablero.getPacman().getY()-4));
			case RIGHT:
				return new Coordinate((this.tablero.getPacman().getX()+4), (this.tablero.getPacman().getY()));
			case DOWN:
				return new Coordinate((this.tablero.getPacman().getX()), (this.tablero.getPacman().getY()+4));
			case LEFT:
				return new Coordinate((this.tablero.getPacman().getX()-4), (this.tablero.getPacman().getY()));
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
