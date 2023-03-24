package com.jpg.pacman.graphics.gameboard;

public class Coordinate {
	
	private float x;
	private float y;
	
	public Coordinate(float pX, float pY) {
		this.x = pX;
		this.y = pY;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public float getX() {
		return x;
	}	

}
