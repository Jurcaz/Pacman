package com.jpg.pacman.graphics.characters;

import java.util.ArrayList;
import java.util.Arrays;

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
	private boolean[][] solution = new boolean[this.tablero.TAM_MAPA][this.tablero.TAM_MAPA];
	
	ArrayList<DirectionEnum> wayToPacman = new ArrayList<>();
	
	DirectionEnum bestX = null;
	DirectionEnum bestY = null;
	
	boolean up, down, right, left;

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
				checkBetterDirection();
				mover();
//				int ghostX = (int) this.getX();
//				int ghostY = (int) this.getY();

//				int pacX = (int) this.tablero.getPacman().getX();
//				int pacY = (int) this.tablero.getPacman().getY();
				
//				System.out.println(findShortestPathLength(this.tablero.getMap(),ghostX,ghostY,pacX,pacY));
				
//				for(boolean[] path : solution){
//		            for(boolean a: path) {
//		                if(a) { System.out.print("a "); } else { System.out.print("o "); }
//		            }
//		            System.out.println();
//		        }
//				System.out.println("---------------------------------------------------------");
//				
//				if(up) {
//					go(DirectionEnum.UP);
//				} else if (down) {
//					go(DirectionEnum.DOWN);
//				} else if (right) {
//					go(DirectionEnum.RIGHT);
//				} else if (left) {
//					go(DirectionEnum.LEFT);
//				}
				
//				System.out.println("Up " + up + " Right " + right + " Down " + down + " Left " + left);
//				
//				up=false;
//				down=false;
//				right=false;
//				left=false;
							
//				System.out.println(solution[ghostX-1][ghostY-1] +""+ solution[ghostX][ghostY-1] +""+ solution[ghostX+1][ghostY-1]);
//				System.out.println(solution[ghostX-1][ghostY] +""+ solution[ghostX][ghostY] +""+ solution[ghostX+1][ghostY]);
//				System.out.println(solution[ghostX-1][ghostY+1] +""+ solution[ghostX][ghostY+1] +""+ solution[ghostX+1][ghostY+1]);
				
			} else {
				goOutCage();
			}

			System.out.println(" -------------------------Fin--------------------------- ");
			
			
//			System.out.println("Movimientos validos: Up - " + moveUpAllowed() + " ,Down - " + moveDownAllowed() + " ,Right - " + moveRightAllowed() + " ,Left - " + moveLeftAllowed() );
//			go(DirectionEnum.UP);
//			lastDirection = currentDirection;
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
		if(currentDirectionAllowed()) go(currentDirection);
		
		if(this.currentDirection == DirectionEnum.UP) {
			this.lastDirection = DirectionEnum.DOWN;
		} else if(this.currentDirection == DirectionEnum.DOWN) {
			this.lastDirection = DirectionEnum.UP;
		} else if(this.currentDirection == DirectionEnum.RIGHT) {
			this.lastDirection = DirectionEnum.LEFT;
		} else if(this.currentDirection == DirectionEnum.LEFT) {
			this.lastDirection = DirectionEnum.RIGHT;
		}
		
		logger.debug("Blinky: x " + this.x + " y " + this.y +" | "+"Pacman: x " + this.tablero.getPacman().getX() + " y " + this.tablero.getPacman().getY());
	}
	
	private void checkBetterDirection() {
		
		int up, down, right, left;
		
		int pacX = (int) this.tablero.getPacman().getX();
		int pacY = (int) this.tablero.getPacman().getY();
		
		if(moveUpAllowed() && (this.lastDirection != DirectionEnum.UP)) {
			int ghostX = (int) this.x;
			int ghostY = (int) this.y-1;
			
			up = Math.abs((pacX-ghostX))+Math.abs((pacY-ghostY));
		} else {
			up = Integer.MAX_VALUE;
		}
		
		if(moveDownAllowed() && (this.lastDirection != DirectionEnum.DOWN)) {
			int ghostX = (int) this.x;
			int ghostY = (int) this.y+1;
			
			down = Math.abs((pacX-ghostX))+Math.abs((pacY-ghostY));
		} else {
			down = Integer.MAX_VALUE;
		}
		
		if(moveRightAllowed() && (this.lastDirection != DirectionEnum.RIGHT)) {
			int ghostX = (int) this.x+1;
			int ghostY = (int) this.y;
			
			right = Math.abs((pacX-ghostX))+Math.abs((pacY-ghostY));
		} else {
			right = Integer.MAX_VALUE;
		}
		
		if(moveLeftAllowed() && (this.lastDirection != DirectionEnum.LEFT)) {
			int ghostX = (int) this.x-1;
			int ghostY = (int) this.y;
			
			left = Math.abs((pacX-ghostX))+Math.abs((pacY-ghostY));
		} else {
			left = Integer.MAX_VALUE;
		}
		
		int result = (Math.min((Math.min(up, down)), (Math.min(right, left))));
		
		if( (result == up    && result == (down|right|left))	|| 
			(result == down  && result == (up|right|left))		|| 
			(result == right && result == (up|down|left))		|| 
			(result == left  && result == (up|down|right)) ) {
			if(moveUpAllowed()) {
				this.currentDirection = DirectionEnum.UP;
			} else if(moveLeftAllowed()) {
				this.currentDirection = DirectionEnum.LEFT;
			} else if(moveDownAllowed()) {
				this.currentDirection = DirectionEnum.DOWN;
			} else if(moveRightAllowed()) {
				this.currentDirection = DirectionEnum.RIGHT;
			}
		}
		
		System.out.println("Up " + up + " Right " + right + " Down " + down + " Left " + left);
		
		if(result == up) {
//			go(DirectionEnum.UP);
			this.currentDirection = DirectionEnum.UP;
		} else if(result == down) {
//			go(DirectionEnum.DOWN);
			this.currentDirection = DirectionEnum.DOWN;
		} else if(result == right) {
//			go(DirectionEnum.RIGHT);
			this.currentDirection = DirectionEnum.RIGHT;
		} else if(result == left) {
//			go(DirectionEnum.LEFT);
			this.currentDirection = DirectionEnum.LEFT;
		}
		
	}
	
	
	private void checkBetterDirectionOld(int pGhostY, int pGhostX, int pPacY, int pPacX, int pX, int pY) {
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
	
	class Tool{
		int distancia;
		boolean[][] mapa;
		
		Tool(int dis, boolean[][] map){
			this.distancia = dis;
			this.mapa = map;
		}
	}
	
	private boolean isSafe(boolean[][] visited, int x, int y) {
        return (moveAllowed(x, y) && !visited[x][y]);
    }
	
	public int findShortestPath(boolean[][] visited, int i, int j, int x, int y, int min_dist, int dist){
		
		// si se encuentra el destino, actualice `min_dist`
		if (i == x && j == y) {
			
			int aux = Integer.min(dist, min_dist);
			
			left = visited[i-1][j];
			right = visited[i+1][j];
			down = visited[i][j+1];
			up = visited[i][j-1];
						
//			System.out.println(solution[i-2][j-2] +" "+ solution[i-1][j-2] +" "+ solution[i][j-2] +" "+ solution[i+1][j-2] +" "+ solution[i+2][j+2]);
//			System.out.println(solution[i-2][j-1] +" "+ solution[i-1][j-1] +" "+ solution[i][j-1] +" "+ solution[i+1][j-1] +" "+ solution[i+2][j-1]);
//			System.out.println(solution[i-2][j] +" "+ solution[i-1][j] +" "+ solution[i][j] +" "+ solution[i+1][j] +" "+ solution[i+2][j]);
//			System.out.println(solution[i-2][j+1] +" "+ solution[i-1][j+1] +" "+ solution[i][j+1] +" "+ solution[i+1][j+1] +" "+ solution[i+2][j+1]);
//			System.out.println(solution[i-2][j+2] +" "+ solution[i-1][j+2] +" "+ solution[i][j+2] +" "+ solution[i+1][j+2] +" "+ solution[i+2][j+2]);
//			System.out.println("---------------" + min_dist);
				
//			for(int auxI = 0; auxI < visited.length; auxI++) {
//				for(int auxJ = 0; auxJ < visited[auxJ].length; auxJ++) {
//					if(visited[auxI][auxJ]) { System.out.print("a "); } else { System.out.print("o "); }
//				}
//				System.out.println();
//			}
//			System.out.println("---------------------------------------------------------"+min_dist);
			
//			for(boolean[] path : visited){
//	            for(boolean a: path) {
//	                
//	            }
//	            System.out.println();
//	        }
//			System.out.println("---------------------------------------------------------"+min_dist);
			
//			if(visited[i+1][j]) {	down = true;	} else {	down = false; }
//			if(visited[i][j+1])	{	right = true;	} else {	right = false; }
//			if(visited[i-1][j]) {	up = true;		} else {	up = false; }
//			if(visited[i][j-1]) {	left = true;	} else {	left = false; }
			
//			System.arraycopy(visited, 0, solution, 0, visited.length);
			
//			solution = Arrays.copyOf(visited, visited.length);
			
			return aux; 
		}
			
		// establece (i, j) la celda como visitada
		visited[i][j] = true;
		
		
		// ir a la celda superior
		if (isSafe(visited, i - 1, j))	{
			min_dist = findShortestPath(visited, i - 1, j, x, y, min_dist, dist + 1);
//			wayToPacman.add(DirectionEnum.UP);
				
			//System.out.println("up ");
//			threadUp.add(DirectionEnum.UP);
//			threadDown.add(DirectionEnum.UP);
//			threadRight.add(DirectionEnum.UP);
//			threadDown.add(DirectionEnum.UP);
		}
				
		// ir a la celda inferior
		if (isSafe(visited, i + 1, j))	{
			min_dist = findShortestPath(visited ,i + 1, j, x, y, min_dist, dist + 1);
//			wayToPacman.add(DirectionEnum.DOWN);
			
			//System.out.println("down ");
//			threadUp.add(DirectionEnum.DOWN);
//			threadDown.add(DirectionEnum.DOWN);
//			threadRight.add(DirectionEnum.DOWN);
//			threadDown.add(DirectionEnum.DOWN);
		}
		
		// ir a la celda de la derecha
		if (isSafe(visited, i, j + 1))	{
			min_dist = findShortestPath(visited, i, j + 1, x, y, min_dist, dist + 1);
//			wayToPacman.add(DirectionEnum.RIGHT);
					
			//System.out.println("right ");
//			threadUp.add(DirectionEnum.RIGHT);
//			threadDown.add(DirectionEnum.RIGHT);
//			threadRight.add(DirectionEnum.RIGHT);
//			threadDown.add(DirectionEnum.RIGHT);
		}
					
		// ir a la celda de la izquierda
		if (isSafe(visited, i, j - 1))	{
			min_dist = findShortestPath(visited, i, j - 1, x, y, min_dist, dist + 1);
//			wayToPacman.add(DirectionEnum.LEFT);
					
			//System.out.println("left ");
//			threadUp.add(DirectionEnum.LEFT);
//			threadDown.add(DirectionEnum.LEFT);
//			threadRight.add(DirectionEnum.LEFT);
//			threadDown.add(DirectionEnum.LEFT);
		}		
		
//		if(wayToPacman.size()>0) wayToPacman.remove(wayToPacman.size()-1);
		
//		if(threadUp.size()>0) threadUp.remove(threadUp.size()-1);
//		if(threadDown.size()>0) threadDown.remove(threadDown.size()-1);
//		if(threadRight.size()>0) threadRight.remove(threadRight.size()-1);
//		if(threadLeft.size()>0) threadLeft.remove(threadLeft.size()-1);
		
		// retroceder: eliminar (i, j) de la matriz visitada
		visited[i][j] = false;
//		solution[i][j] = false;
//		up=false;
//		down=false;
//		right=false;
//		left=false;
		
		return min_dist;
	}

	// Envoltura sobre la función findShortestPath()
	public int findShortestPathLength(MapElementEnum[][] mat, int i, int j, int x, int y) {
	// caso base: entrada inválida
//	if (mat == null || mat.length == 0 || mat[i][j] == 0 || mat[x][y] == 0) {
//		return -1;
//	}
	
	int M = mat.length;
	int N = mat[0].length;
	
	int min_dist;
	
	// construye una matriz `M × N` para realizar un seguimiento de las celdas visitadas
	boolean[][] visited = new boolean[M][N];
	
	min_dist = findShortestPath(visited, i, j, x, y, Integer.MAX_VALUE, 0);
	
//	for(boolean[] path : visited){
//        for(boolean a: path) {
//            if(a) { System.out.print("a "); } else { System.out.print("o "); }
//        }
//        System.out.println();
//    }
//	System.out.println("--------------------");
	
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
