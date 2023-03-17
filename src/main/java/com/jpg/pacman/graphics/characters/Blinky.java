package com.jpg.pacman.graphics.characters;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.gameboard.GameBoard;
import com.jpg.pacman.graphics.gameboard.MapElementEnum;

public class Blinky extends Ghost {

	//tiempo en milisegundos entre movimientos del fantasma
	private int velocidadFantasma = 500;
	private DirectionEnum[] wayOut = {DirectionEnum.UP,DirectionEnum.RIGHT,DirectionEnum.UP,DirectionEnum.UP};
	private int aux = 0;
	
	ArrayList<DirectionEnum> wayToPacman = new ArrayList<>();
	
	DirectionEnum bestX = null;
	DirectionEnum bestY = null;

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
				//mover();
				int ghostX = (int) this.getX();
				int ghostY = (int) this.getY();

				int pacX = (int) this.tablero.getPacman().getX();
				int pacY = (int) this.tablero.getPacman().getY();
				
				System.out.println(findShortestPathLength(this.tablero.getMap(),ghostX,ghostY,pacX,pacY));
				
				for(DirectionEnum a :wayToPacman) System.out.println(a);
			} else {
				goOutCage();
			}

//			System.out.println("Movimientos validos: Up - " + moveUpAllowed() + " ,Down - " + moveDownAllowed() + " ,Right - " + moveRightAllowed() + " ,Left - " + moveLeftAllowed() );
//			go(DirectionEnum.UP);
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
		int ghostX = (int) this.getX();
		int ghostY = (int) this.getY();

		int pacX = (int) this.tablero.getPacman().getX();
		int pacY = (int) this.tablero.getPacman().getY();

		int x = (int) Math.abs(ghostY-pacY);
		int y = (int) Math.abs(ghostX-pacX);
		
//		if(!expectedDirectionAllowed()) {
//			if(currentDirectionAllowed()) go(this.currentDirection);
//		} else {
//			checkBetterDirection(ghostY, ghostX, pacY, pacX, x, y);
//		}
		
		checkBetterDirection(ghostY, ghostX, pacY, pacX, x, y);
		if(currentDirectionAllowed()) go(currentDirection);
		
		logger.debug("Blinky: x " + ghostX + " y " + ghostY +" | "+"Pacman: x " + pacX + " y " + pacY + " | " + "Diferencia: x " + x + " y " + y);
	}
	
	private void checkBetterDirection(int pGhostY, int pGhostX, int pPacY, int pPacX, int pX, int pY) {
		if(pY == 0 || pX == 0) {
			if(pY == 0) {
				if(pGhostY > pPacY){
					if(moveUpAllowed()) {this.currentDirection = DirectionEnum.UP;}
				} else {
					if(moveDownAllowed()) {this.currentDirection = DirectionEnum.DOWN;}
				}
			}
			
			if(pX == 0) {
				if(pGhostX > pPacX){
					if(moveLeftAllowed()) {this.currentDirection = DirectionEnum.LEFT;}
				} else {
					if(moveRightAllowed()) {this.currentDirection = DirectionEnum.RIGHT;}
				}
			}
			
		} else {
			//movemos en y
			if(pX < pY) {
				if(pGhostY > pPacY && moveUpAllowed()) {
					if(moveUpAllowed()) {this.currentDirection = DirectionEnum.UP;}
				} else if (pGhostY < pPacY && moveDownAllowed()) {
					if(moveDownAllowed()) {this.currentDirection = DirectionEnum.DOWN;}
				}
					
			//movemos en x
			} else if (pX > pY) {
				if(pGhostX > pPacX && moveLeftAllowed()) {
					if(moveLeftAllowed()) {this.currentDirection = DirectionEnum.LEFT;}
				} else if(pGhostX < pPacX && moveRightAllowed()) {
					if(moveRightAllowed()) {this.currentDirection = DirectionEnum.RIGHT;}
				}
			}
		}
		System.out.println("blinky better direction " + this.currentDirection);
	}
	
	private void go(DirectionEnum pDirection) {
		float Yaux = this.y;
		float Xaux = this.x;

		switch (pDirection) {
		case UP:
			Yaux -=1;
			//this.currentDirection=DirectionEnum.UP;
			break;
		case DOWN:
			Yaux +=1;
			//this.currentDirection=DirectionEnum.DOWN;
			break;
		case LEFT:
			Xaux -=1;
			//this.currentDirection=DirectionEnum.LEFT;
			break;
		case RIGHT:
			Xaux += 1;
			//this.currentDirection=DirectionEnum.RIGHT;
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
			go(DirectionEnum.RIGHT);
			break;
		case RIGHT:
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
	
	//Diferentes metodos con y sin necesiidad de parametros para saber si el movimiento es valido
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
	
	private boolean currentDirectionAllowed() {
		switch (this.currentDirection) {
		case UP:
			return moveUpAllowed();
		case RIGHT:
			return moveRightAllowed();
		case DOWN:
			return moveDownAllowed();
		case LEFT:
			return moveLeftAllowed();
		default:
			return false;
		}
	}
	
	private boolean expectedDirectionAllowed() {
		switch (this.expectedDirection) {
		case UP:
			return moveUpAllowed();
		case RIGHT:
			return moveRightAllowed();
		case DOWN:
			return moveDownAllowed();
		case LEFT:
			return moveLeftAllowed();
		default:
			return false;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean isSafe(MapElementEnum[][] mat, boolean[][] visited, int x, int y) {
        return (moveAllowed(x, y) && !visited[x][y]);
    }
	
	public int findShortestPath(MapElementEnum[][] mat, boolean[][] visited, int i, int j, int x, int y, int min_dist, int dist){
		
		// si se encuentra el destino, actualice `min_dist`
		if (i == x && j == y) {
		
//			for(DirectionEnum a : threadUp) System.out.println(a);
			
//			for(boolean[] path : visited){
//			for(boolean a: path) {
//			if(a) { System.out.print("J "); } else { System.out.print("o "); }
//			}
//			System.out.println();
//			}
//			System.out.println("--------------------");
			
			return Integer.min(dist, min_dist);       
		}
			
		// establece (i, j) la celda como visitada
		visited[i][j] = true;
		
		// ir a la celda inferior
		if (isSafe(mat, visited, i + 1, j))	{
			min_dist = findShortestPath(mat, visited, i + 1, j, x, y, min_dist, dist + 1);
			wayToPacman.add(DirectionEnum.DOWN);
			
			//System.out.println("down ");
//			threadUp.add(DirectionEnum.DOWN);
//			threadDown.add(DirectionEnum.DOWN);
//			threadRight.add(DirectionEnum.DOWN);
//			threadDown.add(DirectionEnum.DOWN);
		}
		
		// ir a la celda de la derecha
			if (isSafe(mat, visited, i, j + 1))	{
			min_dist = findShortestPath(mat, visited, i, j + 1, x, y, min_dist, dist + 1);
			wayToPacman.add(DirectionEnum.RIGHT);
			
			//System.out.println("right ");
//			threadUp.add(DirectionEnum.RIGHT);
//			threadDown.add(DirectionEnum.RIGHT);
//			threadRight.add(DirectionEnum.RIGHT);
//			threadDown.add(DirectionEnum.RIGHT);
		}
		
		// ir a la celda superior
		if (isSafe(mat, visited, i - 1, j))	{
			min_dist = findShortestPath(mat, visited, i - 1, j, x, y, min_dist, dist + 1);
			wayToPacman.add(DirectionEnum.UP);
			
			//System.out.println("up ");
//			threadUp.add(DirectionEnum.UP);
//			threadDown.add(DirectionEnum.UP);
//			threadRight.add(DirectionEnum.UP);
//			threadDown.add(DirectionEnum.UP);
		}
		
		// ir a la celda de la izquierda
		if (isSafe(mat, visited, i, j - 1))	{
			min_dist = findShortestPath(mat, visited, i, j - 1, x, y, min_dist, dist + 1);
			wayToPacman.add(DirectionEnum.LEFT);
			
			//System.out.println("left ");
//			threadUp.add(DirectionEnum.LEFT);
//			threadDown.add(DirectionEnum.LEFT);
//			threadRight.add(DirectionEnum.LEFT);
//			threadDown.add(DirectionEnum.LEFT);
		}
		
		if(wayToPacman.size()>0) wayToPacman.remove(wayToPacman.size()-1);
		
//		if(threadUp.size()>0) threadUp.remove(threadUp.size()-1);
//		if(threadDown.size()>0) threadDown.remove(threadDown.size()-1);
//		if(threadRight.size()>0) threadRight.remove(threadRight.size()-1);
//		if(threadLeft.size()>0) threadLeft.remove(threadLeft.size()-1);
		
		// retroceder: eliminar (i, j) de la matriz visitada
		visited[i][j] = false;
		
		return min_dist;
	}

	// Envoltura sobre la función findShortestPath()
	public int findShortestPathLength(MapElementEnum[][] mat, int i, int j, int x, int y) {
	// caso base: entrada inválida
//	if (mat == null || mat.length == 0 || mat[i][j] == 0 || mat[x][y] == 0) {
//		return -1;
//	}
	
	// matriz `M × N`
	int M = mat.length;
	int N = mat[0].length;
	
	int min_dist;
	
	// construye una matriz `M × N` para realizar un seguimiento de las celdas visitadas
	boolean[][] visited = new boolean[M][N];
	
	min_dist = findShortestPath(mat, visited, i, j, x, y, Integer.MAX_VALUE, 0);
	
	if (min_dist != Integer.MAX_VALUE) {
		return min_dist;
	}
		return -1;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
