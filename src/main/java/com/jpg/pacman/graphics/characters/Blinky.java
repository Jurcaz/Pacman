package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;

public class Blinky extends Ghost {

	//tiempo en milisegundos entre movimientos del fantasma
	private int velocidadFantasma = 500;
	private DirectionEnum[] wayOut = {DirectionEnum.UP,DirectionEnum.RIGTH,DirectionEnum.UP,DirectionEnum.UP};
	private int aux = 0;

	private final static Logger logger = LogManager.getLogger (Blinky.class);

	/*	Rojo
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

			if(this.outCage) {
				mover();
			} else {
				goOutCage();
			}

			//go(DirectionEnum.UP);
			lastDirection = currentDirection;
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
		velocidad =1;
	}

	@Override
	protected void mover() {
		System.out.println("\nWEBOSSSSSSSSSSSSS\n");
		//se mueve a la siguiente posición en la misma dirección si está libre y si no gira
		DirectionEnum expectedDirection = null;
		
		float ghostX = this.getX();
		float ghostY = this.getY();

		float pacX = this.tablero.getPacman().getX();
		float pacY = this.tablero.getPacman().getY();

		float x = Math.abs(ghostY-pacY);
		float y = Math.abs(ghostX-pacX);

		switch (this.currentDirection) {
			case UP:
				ghostY -=1;
				break;
			case DOWN:
				ghostY +=1;
				break;
			case LEFT:
				ghostX -=1;
				break;
			case RIGTH:
				ghostX += 1;
				break;
		}
		
		//comprueba si la nueva posición está dentro de los límites y es un hueco
		if (moveAllowed(ghostX, ghostY)) {
			this.setX(ghostX);
			this.setY(ghostY);
		 }
		else {

			int aleatorio = (int)(Math.random()*4);
			logger.debug("Valor aleatorio "+aleatorio);
			switch (aleatorio) {
			case 0:
				this.currentDirection = DirectionEnum.LEFT;
				break;
			case 1:
				this.currentDirection = DirectionEnum.RIGTH;
				break;
			case 2:
				this.currentDirection = DirectionEnum.DOWN;
				break;
			case 3:
				this.currentDirection = DirectionEnum.UP;
				break;
			}

		}
		logger.debug("Pinky: x " + this.x + " y " + this.y);
	}
	
	private void go(DirectionEnum pDirection) {
		float Yaux = this.y;
		float Xaux = this.x;

		switch (pDirection) {
		case UP:
			Yaux -=1;
			break;
		case DOWN:
			Yaux +=1;
			break;
		case LEFT:
			Xaux -=1;
			break;
		case RIGTH:
			Xaux += 1;
			break;
	}
		this.setX(Xaux);
		this.setY(Yaux);

	}

	private void goBack() {
		switch (this.lastDirection) {
		case UP:
			go(DirectionEnum.DOWN);
			break;
		case DOWN:
			go(DirectionEnum.UP);
			break;
		case LEFT:
			go(DirectionEnum.RIGTH);
			break;
		case RIGTH:
			go(DirectionEnum.LEFT);
			break;
		}
	}

	private void goOutCage() {
		try {
			go(wayOut[aux]);
			aux++;
		} catch (Exception e) {
			this.outCage = true;
		}
	}

	@Override
	public void muevePosicionInicial () {
		super.muevePosicionInicial();
		this.outCage = false;
		this.aux = 0;
	}
	
	private boolean moveAllowed(float pXact, float pYact) {
		if((pXact>=0 && pXact<this.getMapa().TAM_MAPA) && (pYact>=0 && pYact<this.getMapa().TAM_MAPA) &&
				((this.getMapa().getMap()[(int)pYact][(int)pXact]==MapElementEnum.PILL) ||
				 (this.getMapa().getMap()[(int)pYact][(int)pXact]==MapElementEnum.SUPERPILL) ||
				 (this.getMapa().getMap()[(int)pYact][(int)pXact]==MapElementEnum.EMPTY)))
			return true;
		else {
			return false;
		}

	}
	
	private boolean moveUpAllowed() {
		if(((this.x)>=0 && (this.x)<this.getMapa().TAM_MAPA) && ((this.y-1)>=0 && (this.y-1)<this.getMapa().TAM_MAPA) &&
				((this.getMapa().getMap()[(int)(this.y-1)][(int)(this.x)]==MapElementEnum.PILL) ||
				 (this.getMapa().getMap()[(int)(this.y-1)][(int)(this.x)]==MapElementEnum.SUPERPILL) ||
				 (this.getMapa().getMap()[(int)(this.y-1)][(int)(this.x)]==MapElementEnum.EMPTY)))
			return true;
		else {
			return false;
		}
	}
	
	private boolean moveDownAllowed() {
		if(((this.x)>=0 && (this.x)<this.getMapa().TAM_MAPA) && ((this.y+1)>=0 && (this.y+1)<this.getMapa().TAM_MAPA) &&
				((this.getMapa().getMap()[(int)(this.y+1)][(int)(this.x)]==MapElementEnum.PILL) ||
				 (this.getMapa().getMap()[(int)(this.y+1)][(int)(this.x)]==MapElementEnum.SUPERPILL) ||
				 (this.getMapa().getMap()[(int)(this.y+1)][(int)(this.x)]==MapElementEnum.EMPTY)))
			return true;
		else {
			return false;
		}
	}
	
	private boolean moveLeftAllowed() {
		if(((this.x-1)>=0 && (this.x-1)<this.getMapa().TAM_MAPA) && ((this.y)>=0 && (this.y)<this.getMapa().TAM_MAPA) &&
				((this.getMapa().getMap()[(int)(this.y)][(int)(this.x-1)]==MapElementEnum.PILL) ||
				 (this.getMapa().getMap()[(int)(this.y)][(int)(this.x-1)]==MapElementEnum.SUPERPILL) ||
				 (this.getMapa().getMap()[(int)(this.y)][(int)(this.x-1)]==MapElementEnum.EMPTY)))
			return true;
		else {
			return false;
		}
	}
	
	private boolean moveRightAllowed() {
		if(((this.x+1)>=0 && (this.x+1)<this.getMapa().TAM_MAPA) && ((this.y)>=0 && (this.y)<this.getMapa().TAM_MAPA) &&
				((this.getMapa().getMap()[(int)(this.y)][(int)(this.x+1)]==MapElementEnum.PILL) ||
				 (this.getMapa().getMap()[(int)(this.y)][(int)(this.x+1)]==MapElementEnum.SUPERPILL) ||
				 (this.getMapa().getMap()[(int)(this.y)][(int)(this.x+1)]==MapElementEnum.EMPTY)))
			return true;
		else {
			return false;
		}
	}
	
	

}

//	@Override
//	protected void mover() {
//		//se mueve a la siguiente posición en la misma dirección si está libre y si no gira
//		float pacX = this.gameboard.getPacman().getX();
//		float pacY = this.gameboard.getPacman().getY();
//
//		float xact = this.getX();
//		float yact = this.getY();
//
//		float x = Math.abs(yact-pacY);
//		float y = Math.abs(xact-pacX);
//
//		if(xact > pacX) {
//			DirectionEnum bestX = DirectionEnum.LEFT;
//		} else {
//			DirectionEnum bestX = DirectionEnum.RIGTH;
//		}
//
//		if(yact > pacY) {
//			DirectionEnum bestY = DirectionEnum.UP;
//		} else {
//			DirectionEnum bestY = DirectionEnum.DOWN;
//		}
//
//		go(DirectionEnum.UP);
//
//
//		//comprueba si la nueva posición está dentro de los límites y es un hueco
//		if (moveAllowed(xact, yact)) {
//			if(y>x) {
//				if (lastDirection == DirectionEnum.LEFT || lastDirection == DirectionEnum.RIGTH) {
//					currentDirection = lastDirection;
//					if (!this.moveAllowed(xact, yact))
//						go(currentDirection == DirectionEnum.LEFT ? DirectionEnum.RIGTH : DirectionEnum.LEFT);
//			} else {
//				currentDirection = bestX;
//				if (!this.moveAllowed(xact, yact)) {
//					curDirection = preferredHorizontal == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
//					if (!this.moveIsAllowed(curDirection))
//						curDirection = preferredVertical == Direction.UP ? Direction.DOWN : Direction.UP;
//				}
//			}
//		 }
//		else {
//
//
//		}
//		}
//	}
