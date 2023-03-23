package com.jpg.pacman.graphics.characters;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;

public class Blinky extends Ghost {

	//tiempo en milisegundos entre movimientos del fantasma
	private int velocidadFantasma = 50;
	private DirectionEnum[] wayOut = {DirectionEnum.UP,DirectionEnum.RIGHT,DirectionEnum.UP,DirectionEnum.UP,DirectionEnum.LEFT};
	private int out;
	private boolean upBoolean, rightBoolean, downBoolean, leftBoolean;

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

			if(this.isOutCage()) {
				int ghostX = (int) this.getX();
				int ghostY = (int) this.getY();

				int pacX = (int) this.tablero.getPacman().getX();
				int pacY = (int) this.tablero.getPacman().getY();

				System.out.println(findShortestPathLength(this.tablero.getMap(), ghostX, ghostY, pacX, pacY));

				if(rightBoolean) {
					this.currentDirection = DirectionEnum.RIGHT;
				}else if(downBoolean) {
					this.currentDirection = DirectionEnum.DOWN;
				}else if(leftBoolean) {
					this.currentDirection = DirectionEnum.LEFT;
				}else if(upBoolean) {
					this.currentDirection = DirectionEnum.UP;
				}

				mover();
			} else {
				goOutCage();
			}
			
			System.out.println("  --------------------Fin-de-turno--------------------  ");
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
	protected void mover() {
		if(currentDirectionAllowed()) go(this.currentDirection);
	}

	//----------------------------------------------------------------------------------------------------//

    private boolean isSafe(boolean[][] visited, int x, int y) {
        return moveAllowed(x, y) && !visited[x][y];
    }

    private int findShortestPath(MapElementEnum[][] mat, boolean[][] visited, int i, int j, int x, int y, int min_dist, int dist) {
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

    private int findShortestPathLength(MapElementEnum[][] mat, int i, int j, int x, int y) {

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

	//----------------------------------------------------------------------------------------------------//

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

	private void goOutCage() {
		try {
			this.currentDirection = wayOut[out];
			go(this.currentDirection);
			out++;
		} catch (IndexOutOfBoundsException e) {
			this.outCage = true;
		}
	}

	private boolean currentDirectionAllowed() {
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

	private void go(DirectionEnum pDirection) {
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

}