package com.jpg.pacman.graphics.characters;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.math.RoundingMode;
import java.text.NumberFormat;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.Ball;
import com.jpg.pacman.graphics.gameboard.EmptyObject;
import com.jpg.pacman.graphics.gameboard.ExtraBall;
import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;
import com.jpg.pacman.graphics.gameboard.utils.SoundDaemon;

public class Pacman extends Character{

	private int numVidas;
	private int puntuacion;
	private boolean imgBocaCerrada=false;
	private boolean pausado;
	private Image imagenL, imagenR, imagenU, imagenD, imagenC;
	private final float inc = .5f;
	private final int NUM_VIDAS = 3;

	private final static Logger logger = LogManager.getLogger (Pacman.class);

	public Pacman (GameBoard mapa) {
		super (mapa);

		ImageIcon ii = new ImageIcon(this.getClass().getResource(
			"/img/chars/Pacman_L.png"));
		imagenL = ii.getImage();
		imagen = imagenL;
		ii = new ImageIcon(this.getClass().getResource(
				"/img/chars/Pacman_D.png"));
		imagenD = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"/img/chars/Pacman_U.png"));
		imagenU = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"/img/chars/Pacman_R.png"));
		imagenR = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(
				"/img/chars/Pacman_C.png"));
		imagenC = ii.getImage();


		this.POSXINI = 13;
		this.POSYINI = 16;
		this.muevePosicionInicial();
		width = imagen.getWidth(null);
		height = imagen.getHeight(null);
		numVidas = NUM_VIDAS;
		puntuacion = 0;
	}


	public void keyPressed(KeyEvent e) {
		logger.debug("keyPressed direccion actual "+currentDirection.toString());
		int key = e.getKeyCode();
		NumberFormat numberFormatDOWN = NumberFormat.getInstance();
		numberFormatDOWN.setMaximumFractionDigits(0);
		numberFormatDOWN.setRoundingMode(RoundingMode.DOWN);
		NumberFormat numberFormatUP = NumberFormat.getInstance();
		numberFormatUP.setMaximumFractionDigits(0);
		numberFormatUP.setRoundingMode(RoundingMode.UP);

		//cambio de eje
		if ((this.currentDirection== DirectionEnum.LEFT || this.currentDirection== DirectionEnum.RIGTH) && (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)) {
			//la nueva posicion de x debe ser entero
			float nuevaPos;
			if (key == KeyEvent.VK_UP) nuevaPos= x-inc;
			else nuevaPos = x+inc;
			if (Float.parseFloat(numberFormatDOWN.format(nuevaPos))!=(int)nuevaPos) return;
		}

		if (key == KeyEvent.VK_LEFT && puedeDesplazarse (x-inc,y)) {
			this.currentDirection = DirectionEnum.LEFT;
			setImg(this.currentDirection);
		}
		if (key == KeyEvent.VK_RIGHT && puedeDesplazarse (x+inc,y)) {
			this.currentDirection = DirectionEnum.RIGTH;
			setImg(this.currentDirection);
		}
		if (key == KeyEvent.VK_UP && puedeDesplazarse (x,y-inc)) {
				this.currentDirection = DirectionEnum.UP;
				setImg(this.currentDirection);
		}
		if (key == KeyEvent.VK_DOWN && puedeDesplazarse (x,y+inc)) {
			this.currentDirection = DirectionEnum.DOWN;
			setImg(this.currentDirection);
		}
		logger.debug("keyPressed direccion nueva "+currentDirection.toString());
	}


	public int getNumVidas() {
		return numVidas;
	}

	public void setNumVidas(int numVidas) {
		this.numVidas = numVidas;
	}

	public void reiniciarVidas() {
		this.numVidas = NUM_VIDAS;
	}
	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}

	public void pausarPacman() {
		pausado = true;
	}
	public void activarPacman() {
		pausado = false;
	}


	@Override
	public Image getImagen() {
		return this.imagen;
	}

	//Método para establecer el icono del personaje
	private void setImg(DirectionEnum direccion) {
		logger.debug("segImg - direcion"+ direccion.toString()+" boca cerrada "+imgBocaCerrada);
		switch (direccion)		{
			case LEFT: {
				if (!imgBocaCerrada) {
					imagen = this.imagenL;
					this.imgBocaCerrada = true;
				}
				else {
					imagen = this.imagenC;
					this.imgBocaCerrada = false;
				}
				break;
			}

			case UP : {
				if (!imgBocaCerrada) {
					imagen = this.imagenU;
					this.imgBocaCerrada = true;
				}
				else {
					imagen = this.imagenC;
					this.imgBocaCerrada = false;
				}
				break;
			}
			case RIGTH: {
				if (!imgBocaCerrada) {
					imagen = this.imagenR;
					this.imgBocaCerrada = true;
				}
				else {
					imagen = this.imagenC;
					this.imgBocaCerrada = false;
				}
				break;
			}

			case DOWN: {
				if (!imgBocaCerrada) {
					imagen = this.imagenD;
					this.imgBocaCerrada = true;
				}
				else {
					imagen = this.imagenC;
					this.imgBocaCerrada = false;
				}
				break;
			}
			default :{
				imagen = this.imagenC;
				this.imgBocaCerrada = false;
			}
		}
		logger.debug("segImg - direcion"+ direccion.toString()+" boca cerrada "+imgBocaCerrada);
	}

	public void muevePacman () {
		logger.debug("muevePacman : "+this.currentDirection.toString()+"  ("+x+","+y+")");
		if (!pausado) {
			switch (this.currentDirection) {
				case UP:
					if (puedeDesplazarse (x, y-inc)) {
						setImg(this.currentDirection);
						this.y = y -inc;
					}
					break;
				case DOWN:
					if (puedeDesplazarse (x, y+inc)) {
						setImg(this.currentDirection);
						this.y = y + inc;
					}
					break;
				case LEFT:
					if (puedeDesplazarse (x-inc, y)) {
						setImg(this.currentDirection);
						this.x = x -inc;
					}
					break;
				case RIGTH:
					if (puedeDesplazarse (x+inc, y)) {
						setImg(this.currentDirection);
						this.x = x + inc;
					}
					break;
			}

			if (this.getMapa().getMapa()[(int)y][(int)x].getClass().getSimpleName().equals("Ball")) {
				this.getMapa().getMapa()[(int)y][(int)x]= new EmptyObject ();
				SoundDaemon.setAudio(SoundDaemon.BALL);
				this.puntuacion += Ball.PUNTUACION;
			}
			if (this.getMapa().getMapa()[(int)y][(int)x].getClass().getSimpleName().equals("ExtraBall")) {
				this.getMapa().getMapa()[(int)y][(int)x]= new EmptyObject ();
				this.puntuacion += ExtraBall.PUNTUACION;
				this.getMapa().setHuirFantasmas(true);
			}
			if (this.getMapa().getMapa()[(int)y][(int)x].getClass().getSimpleName().equals("Gate")) {
				if (this.x == 0) this.x=this.getMapa().getMap()[0].length-1;
				else if (this.x == this.getMapa().getMap()[0].length-1) this.x = 0;
			}
		}
	}

	/**
	 * Comprueba si puede moverse a la posicion x,y
	 * */
	private boolean puedeDesplazarse (float x, float y) {
		logger.debug("Puede desplazarse");
		if (x>=0 && y>=0 && y<this.getMapa().getMap().length && x<this.getMapa().getMap()[0].length &&
				this.getMapa().getMap()[(int)y][(int)x] != MapElementEnum.WALL &&
				this.getMapa().getMap()[(int)y][(int)x] != MapElementEnum.GHOSTGATE) {
			logger.debug ("Contenido del mapa en (x ="+(int)x+",y="+(int)y+") "+this.getMapa().getMap()[(int)x][(int)y].toString());
			logger.debug("Puede desplazarse a "+x+" "+y);
			return true;
		}
		logger.debug("No puede desplazarse a "+x+" "+y);
		return false;
	}
	@Override
	public void muevePosicionInicial () {
		super.muevePosicionInicial();
		this.currentDirection = DirectionEnum.LEFT;
	}

}
