package com.jpg.pacman.graphics.characters;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.jpg.pacman.graphics.gameboard.GameBoard;

public abstract class Character {

	protected float x;
	protected float y;
	protected Image imagen;
	protected int width;
	protected int height;
	protected DirectionEnum currentDirection = DirectionEnum.UP;
	protected DirectionEnum lastDirection = DirectionEnum.LEFT;
	protected GameBoard tablero;
	protected int POSXINI;
	protected int POSYINI;

	public GameBoard getMapa() {
		return this.tablero;
	}

	public void setMapa(GameBoard gameBoard) {
		this.tablero = gameBoard;
	}

	public Character(GameBoard gameBoard) {
		this.tablero = gameBoard;
	}

	public float getX() {
		return this.x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return this.y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Image getImagen() {
		String imgName = this.getClass().getSimpleName();
		switch (this.currentDirection) {
			case UP: 	imgName += "_U"; break;
			case DOWN: 	imgName += "_D"; break;
			case LEFT: 	imgName += "_L"; break;
			case RIGHT:	imgName += "_R"; break;
		}
		ImageIcon ii = new ImageIcon(this.getClass().getResource("/img/chars/"+imgName+".png"));
		this.imagen = ii.getImage();

		return this.imagen;
	}

	public void setImagen(Image imagen) {
		this.imagen = imagen;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void muevePosicionInicial () {
		this.x = this.POSXINI;
		this.y = this.POSYINI;
	}

}