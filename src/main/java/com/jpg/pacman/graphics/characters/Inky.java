package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.Coordinate;
import com.jpg.pacman.graphics.gameboard.GameBoard;

public class Inky extends Ghost {

	private int velocidadFantasma = 500;
	private DirectionEnum[] wayOut = {DirectionEnum.UP,DirectionEnum.UP,DirectionEnum.UP};
	
	/* Azul
	 * No es tan rápido como Blinky. Calcula la distancia en línea recta entre Blinky y Pac-man y
	 * lo gira 180 grados, así que Inky siempre colabora con Blinky para acorralar a Pac-man
	 * */
	
	private final static Logger logger = LogManager.getLogger (Inky.class);


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
				if(outCage) {
					//System.out.println("x " + findObjetive().getX() + " y " + findObjetive().getY());
					findObjetive();
					//chase();
					
				} else {
					goOutCage();
				}
			}
			
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
		
		logger.debug("Pinky: x " + this.x + " y " + this.y);
	}

	@Override
	protected Coordinate findObjetive() {
		
			int pacX = (int) tablero.getPacman().getX();
			int pacY = (int) tablero.getPacman().getY();
			
			int blinkyX = (int) tablero.getBlinky().getX();
			int blinkyY = (int) tablero.getBlinky().getY();
			
			int distantX = Math.abs((pacX - blinkyX));
			int distantY = Math.abs((pacY - blinkyY));
			
			if(pacX < blinkyX) distantX = distantX * -1;
			
			if(pacY < blinkyY) distantY = distantY * -1;
			
			int objetiveX = pacX + distantX;
			int objetiveY = pacY + distantY;
			
			while (!moveAllowed(objetiveX, objetiveY)) {
				
				if(distantX == 0) {
					System.out.println("distantx 0");
					if(pacY > distantY) {
						distantY = distantY + 1;
					} else {
						distantY = distantY - 1;
					}
				}
				
				if(distantY == 0) {
					System.out.println("distanty 0");
					if(pacX > distantX) {
						distantX = distantX + 1;
					} else {
						distantX = distantX - 1;
					}
				}
					
				
				
			}
			
//			System.out.println("pacX " + pacX + " pacY " + pacY);
//			System.out.println("blinkyX " + blinkyX + " blinkyY " + blinkyY);
//			System.out.println("distantX " + distantX + " distantY " + distantY);
			System.out.println("objetiveX "+ objetiveX + " objetiveY " + objetiveY);
//			System.out.println("-----------------------------------");
						
		return new Coordinate(objetiveX, objetiveY);
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
