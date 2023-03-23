package com.jpg.pacman.graphics.characters;

import java.awt.Image;

import javax.swing.ImageIcon;

import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;

public abstract class Ghost extends Character implements Runnable {
	protected boolean activo;
	protected boolean comestible;
	protected int velocidad;
	protected GameBoard gameboard;
	protected boolean outCage = false;
	protected boolean upBoolean, rightBoolean, downBoolean, leftBoolean;
	protected int out;

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
		this.out = 0;
		outCage = false;
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

	protected void go(DirectionEnum pDirection) {
		switch (pDirection) {
		case UP:
			this.setY(this.y-1);
			break;
		case DOWN:
			this.setY(this.y+1);
			break;
		case LEFT:
			this.setX(this.x-1);
			break;
		case RIGHT:
			this.setX(this.x+1);
			break;
		}
	}
	
	protected boolean currentDirectionAllowed() {
		switch (this.currentDirection) {
		case UP:
			return moveAllowed(this.x, this.y-1);
		case RIGHT:
			return moveAllowed(this.x+1, this.y);
		case DOWN:
			return moveAllowed(this.x, this.y+1);
		case LEFT:
			return moveAllowed(this.x-1, this.y);
		default:
			return false;
		}
	}
	
	protected boolean moveAllowed(float pXact, float pYact) {
		if((pXact>=0 && pXact<this.getMapa().TAM_MAPA) && (pYact>=0 && pYact<this.getMapa().TAM_MAPA) &&
				((this.getMapa().getMap()[(int)pYact][(int)pXact]==MapElementEnum.PILL) ||
				 (this.getMapa().getMap()[(int)pYact][(int)pXact]==MapElementEnum.SUPERPILL) ||
				 (this.getMapa().getMap()[(int)pYact][(int)pXact]==MapElementEnum.EMPTY)))
			return true;
		else {
			return false;
		}
	}
	
	protected boolean isSafe(boolean[][] visited, int x, int y) {
        return moveAllowed(x, y) && !visited[x][y];
    }

    protected int findShortestPath(MapElementEnum[][] mat, boolean[][] visited, int i, int j, int x, int y, int min_dist, int dist) {
    	if (i == x && j == y) {
    		if(dist<min_dist) {

    			min_dist = dist;

    			try {
    				downBoolean = visited[((int) this.getX())][((int) this.getY())+1];
				} catch (IndexOutOfBoundsException e) {
					downBoolean = false;
				}
    			
    			try {
    				upBoolean = visited[((int) this.getX())][((int) this.getY())-1];
				} catch (IndexOutOfBoundsException e) {
					upBoolean = false;
				}
    			
    			try {
    				rightBoolean = visited[((int) this.getX())+1][((int) this.getY())];
				} catch (IndexOutOfBoundsException e) {
					rightBoolean = false;
				}

    			try {
    				leftBoolean = visited[((int) this.getX())-1][((int) this.getY())];
				} catch (IndexOutOfBoundsException e) {
					leftBoolean = false;
				}
    			
    		}

    		return min_dist;
    	}

    	visited[i][j] = true;

    	if (isSafe(visited, i + 1, j)) {min_dist = findShortestPath(mat, visited, i + 1, j, x, y, min_dist, dist + 1);}
    	if (isSafe(visited, i, j + 1)) {min_dist = findShortestPath(mat, visited, i, j + 1, x, y, min_dist, dist + 1);}
    	if (isSafe(visited, i - 1, j)) {min_dist = findShortestPath(mat, visited, i - 1, j, x, y, min_dist, dist + 1);}
    	if (isSafe(visited, i, j - 1)) {min_dist = findShortestPath(mat, visited, i, j - 1, x, y, min_dist, dist + 1);}

    	visited[i][j] = false;

    	return min_dist;
    }

    protected int findShortestPathLength(MapElementEnum[][] mat, int i, int j, int x, int y) {

        int M = mat.length;
        int N = mat[0].length;

        int min_dist;

        boolean[][] visited = new boolean[M][N];

        min_dist = findShortestPath(mat, visited, i, j, x, y, Integer.MAX_VALUE, 0);

       if (min_dist != Integer.MAX_VALUE) {
    	   return min_dist;
       }
       return -1;
    }


}
