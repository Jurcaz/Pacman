package com.jpg.pacman.graphics.characters;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.jpg.pacman.graphics.gameboard.GameBoard;

public abstract class Ghost extends Character implements Runnable {
	protected boolean activo;
	protected boolean comestible;
	protected int velocidad;
	protected GameBoard gameboard;
	protected boolean outCage = false;
	protected DirectionEnum expectedDirection = null;

	public Ghost(GameBoard gameBoard) {
		super(gameBoard);
		this.currentDirection = DirectionEnum.UP;
		this.comestible = false;
	}

	public boolean isActivo() {
		return this.activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isComestible() {
		return this.comestible;
	}

	public void setComestible(boolean comestible) {
		this.comestible = comestible;
	}

	public int getVelocidad() {
		return this.velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	protected abstract void mover ();

	@Override
	public void muevePosicionInicial () {
		super.muevePosicionInicial();
		this.currentDirection = DirectionEnum.UP;
	}

	@Override
	public Image getImagen() {
		String imgName = this.getClass().getSimpleName();
		ImageIcon ii;
		if (comestible)
			ii = new ImageIcon(this.getClass().getResource("/img/chars/fantasmaKO.png"));
		else {
			switch (this.currentDirection) {
				case UP: 	imgName += "_U"; break;
				case DOWN: 	imgName += "_D"; break;
				case LEFT: 	imgName += "_L"; break;
				case RIGHT:	imgName += "_R"; break;
			}
			ii = new ImageIcon(this.getClass().getResource("/img/chars/"+imgName+".png"));
		}
		this.imagen = ii.getImage();

		return this.imagen;
	}

	public void setdirection(DirectionEnum pDirection) {
		setdirection(pDirection);
	}

	public boolean isOutCage() {
		return outCage;
	}

	public void setOutCage(boolean outCage) {
		this.outCage = outCage;
	}




}
