package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;

public class Pinky extends Ghost {

	//tiempo en milisegundos entre movimientos del fantasma
	private int velocidadFantasma = 500;

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
			//mover();
			//System.out.println("\nWEBOSSSSSSSSSSSSS\n");
		}


	}

	@Override
	protected void mover() {
		//se mueve a la siguiente posición en la misma dirección si está libre y si no gira
		float xact = this.getX();
		float yact = this.getY();

		switch (this.currentDirection) {
			case UP:
				yact -=1;
				break;
			case DOWN:
				yact +=1;
				break;
			case LEFT:
				xact -=1;
				break;
			case RIGHT:
				xact += 1;
				break;
		}
		//comprueba si la nueva posición está dentro de los límites y es un hueco
		if ((xact>=0 && xact<this.getMapa().TAM_MAPA) && (yact>=0 && yact<this.getMapa().TAM_MAPA) &&
		((this.getMapa().getMap()[(int)yact][(int)xact]==MapElementEnum.PILL) ||
		 (this.getMapa().getMap()[(int)yact][(int)xact]==MapElementEnum.SUPERPILL) ||
		 (this.getMapa().getMap()[(int)yact][(int)xact]==MapElementEnum.GHOSTGATE) ||
		 (this.getMapa().getMap()[(int)yact][(int)xact]==MapElementEnum.EMPTY))) {
			this.setX(xact);
			this.setY(yact);

		 }
		else {
			int aleatorio = (int)(Math.random()*4);
			logger.debug("Valor aleatorio "+aleatorio);
			switch (aleatorio) {
			case 0:
				this.currentDirection = DirectionEnum.LEFT;
				break;
			case 1:
				this.currentDirection = DirectionEnum.RIGHT;
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
}
