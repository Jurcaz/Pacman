package com.jpg.pacman.graphics.gameboard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jpg.pacman.graphics.characters.Blinky;
import com.jpg.pacman.graphics.characters.Clyde;
import com.jpg.pacman.graphics.characters.Ghost;
import com.jpg.pacman.graphics.characters.Inky;
import com.jpg.pacman.graphics.characters.Pacman;
import com.jpg.pacman.graphics.characters.Pinky;
import com.jpg.pacman.graphics.gameboard.utils.SoundDaemon;
import com.jpg.pacman.graphics.gameboard.utils.SuperPillDaemon;


public class GameBoard extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public final int TAM_MAPA = 29;
	public final int ELEMENT_SIZE = 35;
	private static final int SEGUNDOS_SUPERPILL  = 8;
	private static final int PTOS_FANTASMA  = 200;

	private Element[][] mapa = new Element[TAM_MAPA][TAM_MAPA];
	private Ghost blinky;
	private Ghost pinky;
	private Ghost inky;
	private Ghost clyde;

	private Thread threadBlinky;
	private Thread threadPinky;
	private Thread threadInky;
	private Thread threadClyde;

	private Pacman pacman;
	private Timer timer;
	private static int segundosRestantesSuperPill=0;
	//variable para indicar si los fantasmas deben huir
	private static boolean huirFantasma;
	//variable para indicar si se ha acabado la partida
	private boolean partidaAcabada;
	//variable para indicar si pacman ha muerto y se debe reiniciar la posición de los fantasmas y pacman
	private boolean reiniciarPosiciones;
	private boolean juegoPausado;
	//nivel del juego
	private int nivel;
	private final static Logger logger = LogManager.getLogger (GameBoard.class);

	// Definición del mapa.
	private final MapElementEnum[][] map = {
/* 0 */		{ MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL },
/* 1 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL },
/* 2 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL },
/* 3 */		{ MapElementEnum.WALL, MapElementEnum.SUPERPILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.SUPERPILL, MapElementEnum.WALL },
/* 4 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL },
/* 5 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
			  MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL },
/* 6 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL },
/* 7 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL },
/* 8 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL },
/* 9 */		{ MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL },
/*10 */		{ MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY },
/*11 */		{ MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY,
			  MapElementEnum.WALL,  MapElementEnum.PILL,  MapElementEnum.WALL,  MapElementEnum.WALL,  MapElementEnum.EMPTY,
			  MapElementEnum.WALL,  MapElementEnum.WALL,  MapElementEnum.WALL,  MapElementEnum.GHOSTGATE,  MapElementEnum.GHOSTGATE,
			  MapElementEnum.WALL,  MapElementEnum.WALL,  MapElementEnum.WALL,  MapElementEnum.EMPTY, MapElementEnum.WALL,
			  MapElementEnum.WALL,	MapElementEnum.PILL,  MapElementEnum.WALL,  MapElementEnum.EMPTY, MapElementEnum.EMPTY,
			  MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY },
/*12 */		{ MapElementEnum.WALL,  MapElementEnum.WALL,  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL,  MapElementEnum.PILL,  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY,
			  MapElementEnum.WALL,  MapElementEnum.EMPTY, MapElementEnum.EMPTY,MapElementEnum.EMPTY,MapElementEnum.EMPTY,
			  MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.EMPTY,MapElementEnum.WALL,
			  MapElementEnum.WALL,  MapElementEnum.PILL,  MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
			  MapElementEnum.WALL,  MapElementEnum.WALL,  MapElementEnum.WALL },
/*13 */		{ MapElementEnum.GATE, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY,
			  MapElementEnum.EMPTY, MapElementEnum.PILL,  MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY,
			  MapElementEnum.WALL,  MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY,
			  MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL,  MapElementEnum.EMPTY, MapElementEnum.EMPTY,
			  MapElementEnum.EMPTY, MapElementEnum.PILL,  MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY,
			  MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.GATE },// PORTAL
/*14 */		{ MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL },
/*15 */		{ MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY },
/*16 */		{ MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY, MapElementEnum.EMPTY },
/*17 */		{ MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.EMPTY, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL },
/*18 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
					MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL },
/*19 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL },
/*20 */		{ MapElementEnum.WALL, MapElementEnum.SUPERPILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.SUPERPILL, MapElementEnum.WALL },
/*21 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL },
/*22 */		{ MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL },
/*23 */		{ MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL },
/*24 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL,
					MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL },
/*25 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL },
/*26 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.WALL },
/*27 */		{ MapElementEnum.WALL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL,
					MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.PILL, MapElementEnum.WALL },
/*28 */		{ MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL,
					MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL, MapElementEnum.WALL },
	};

	// CONSTRUCTOR DE LA CLASE.
	public GameBoard() {
		this.nivel = 1;
		addKeyListener(new KAdapter());

		setFocusable(true);
		setBackground(Color.black);
		setDoubleBuffered(true);
		blinky 	= new Blinky(this);
		pinky 	= new Pinky(this);
		inky 	= new Inky(this);
		clyde 	= new Clyde(this);
		pacman 	= new Pacman(this);

		cargaMapa();

		timer = new Timer(50, this);
		timer.start();
		huirFantasma = false;
		partidaAcabada = false;
		reiniciarPosiciones = false;

		// arrancar los fantasmas
		threadBlinky = new Thread (blinky);
		threadBlinky.start();

		threadPinky = new Thread (pinky);
		threadPinky.start();

		threadInky = new Thread (inky);
		threadInky.start();

		threadClyde = new Thread (clyde);
		threadClyde.start();

	}

	// MÉTODO PARA LA CARGA DEL MAPA.
	private void cargaMapa() {

		try {
			for (int columna = 0; columna < map.length; columna++) {
				for (int fila = 0; fila < map.length -1; fila++) {

					if (map[columna][fila] == MapElementEnum.EMPTY) {
						mapa[columna][fila] = new EmptyObject ();
					} else if (map[columna][fila] == MapElementEnum.WALL) {
						mapa[columna][fila] = new Wall (fila,columna );
					} else if (map[columna][fila] == MapElementEnum.PILL) {
						mapa[columna][fila] = new Ball(fila, columna);
					} else if (map[columna][fila] == MapElementEnum.SUPERPILL) {
						mapa[columna][fila] = new ExtraBall (fila, columna);
					} else if (map[columna][fila] == MapElementEnum.GATE) {
						mapa[columna][fila] = new Gate (fila, columna);
					}  else if (map[columna][fila] == MapElementEnum.GHOSTGATE) {
						mapa[columna][fila] = new GhostGate (fila, columna);
					}
				}
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//logger.info("En el método paint");
		Graphics2D g2d = (Graphics2D) g; // LO CONVERTIMOS EN TIPO 2D.
		Toolkit.getDefaultToolkit().sync(); // MÉTODO PARA SINCRONIZAR.

		try {
			for (int columna = 0; columna < mapa.length; columna++) {
				for (int fila = 0; fila < mapa.length - 1; fila++) {
					if (mapa[columna][fila]!=null && mapa[columna][fila].isActivo()) {
						g2d.drawImage(mapa[columna][fila].getImagen(),
								fila * ELEMENT_SIZE, columna * ELEMENT_SIZE, this);
					}
				}
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

		//logger.debug ("paint - posicion pacman (x,y) ("+pacman.getX()+","+pacman.getY()+")");

		//comprobar si pacman ha muerto o blinky mueren
		if ((int)pacman.getX()==(int)blinky.getX() && (int)pacman.getY()==(int)blinky.getY()) {
			if (blinky.isComestible()) {
				blinky.setComestible(false);
				blinky.muevePosicionInicial();
				int ptos = PTOS_FANTASMA;
				if (!clyde.isComestible()) ptos=2*ptos;
				if (!inky.isComestible()) ptos=2*ptos;
				if (!pinky.isComestible()) ptos=2*ptos;
				pacman.setPuntuacion(pacman.getPuntuacion()+ptos);
			}
			else {
				SoundDaemon.setAudio(SoundDaemon.DEATH);
				pacman.setNumVidas(pacman.getNumVidas()-1);
				pacman.muevePosicionInicial();
			}
		}

		//comprobar si pacman ha muerto o clyde mueren
		if ((int)pacman.getX()==(int)clyde.getX() && (int)pacman.getY()==(int)clyde.getY()) {
			if (clyde.isComestible()) {
				clyde.setComestible(false);
				clyde.muevePosicionInicial();
				int ptos = PTOS_FANTASMA;
				if (!blinky.isComestible()) ptos=2*ptos;
				if (!inky.isComestible()) ptos=2*ptos;
				if (!pinky.isComestible()) ptos=2*ptos;
				pacman.setPuntuacion(pacman.getPuntuacion()+ptos);

			}
			else {
				SoundDaemon.setAudio(SoundDaemon.DEATH);
				pacman.setNumVidas(pacman.getNumVidas()-1);
				pacman.muevePosicionInicial();
			}
		}

		//comprobar si pacman ha muerto o inky mueren
		if ((int)pacman.getX()==(int)inky.getX() && (int)pacman.getY()==(int)inky.getY()) {
			if (inky.isComestible()) {
				inky.setComestible(false);
				inky.muevePosicionInicial();
				int ptos = PTOS_FANTASMA;
				if (!blinky.isComestible()) ptos=2*ptos;
				if (!clyde.isComestible()) ptos=2*ptos;
				if (!pinky.isComestible()) ptos=2*ptos;
				pacman.setPuntuacion(pacman.getPuntuacion()+ptos);
			}
			else {
				SoundDaemon.setAudio(SoundDaemon.DEATH);
				pacman.setNumVidas(pacman.getNumVidas()-1);
				pacman.muevePosicionInicial();
			}
		}

		//comprobar si pacman ha muerto o inky mueren
		if ((int)pacman.getX()==(int)pinky.getX() && (int)pacman.getY()==(int)pinky.getY()) {
			if (pinky.isComestible()) {
				pinky.setComestible(false);
				pinky.muevePosicionInicial();
				int ptos = PTOS_FANTASMA;
				if (!blinky.isComestible()) ptos=2*ptos;
				if (!clyde.isComestible()) ptos=2*ptos;
				if (!inky.isComestible()) ptos=2*ptos;
				pacman.setPuntuacion(pacman.getPuntuacion()+ptos);
			}
			else {
				SoundDaemon.setAudio(SoundDaemon.DEATH);
				pacman.setNumVidas(pacman.getNumVidas()-1);
				pacman.muevePosicionInicial();
			}
		}

		if (pacman.getNumVidas()==0) {
			//partida acabada
			this.reiniciarNivel();
		}

		if (nivelAcabado()) {
			nivel ++;
			this.reiniciarNivel();
		}

		g2d.drawImage(blinky.getImagen(),
				((int) (blinky.getX())*ELEMENT_SIZE), (int)(blinky.getY()*ELEMENT_SIZE), this);
		g2d.drawImage(clyde.getImagen(),
				((int) clyde.getX())*ELEMENT_SIZE, ((int) clyde.getY())*ELEMENT_SIZE, this);
		g2d.drawImage(inky.getImagen(),
				((int) inky.getX())*ELEMENT_SIZE, ((int) inky.getY())*ELEMENT_SIZE, this);
		g2d.drawImage(pinky.getImagen(),
				((int) pinky.getX())*ELEMENT_SIZE, ((int) pinky.getY())*ELEMENT_SIZE, this);
		g2d.drawImage(pacman.getImagen(),
				((int) pacman.getX())*ELEMENT_SIZE, ((int) pacman.getY())*ELEMENT_SIZE, this);


		// SECCIÓN PARA DIBUJAR LA PUNTUACIÓN DE PACMAN EN PANTALLA.
		g2d.setColor(Color.BLUE);
		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		g2d.drawString("PUNTUACIÓN", 1000, 50);

		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Verdana", Font.BOLD, 30));
		g2d.drawString(String.valueOf(pacman.getPuntuacion()), 1065, 90);

		// Sección para mostrar el nivel
		g2d.setColor(Color.BLUE);
		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		g2d.drawString("Nivel "+nivel, 1000, 130);


		// SECCIÓN PARA DIBUJAR LAS VIDAS DE PACMAN.
		g2d.setColor(Color.BLUE);
		g2d.setFont(new Font("Verdana", Font.BOLD, 20));
		g2d.drawString("VIDAS", 1000, 160);

		for (int i = 0; i < pacman.getNumVidas(); i++) {
			ImageIcon ii = new ImageIcon(this.getClass().getResource(
					"/img/chars/Pacman_L.png"));
			g2d.drawImage(ii.getImage(), 1000 + i * 40, 170, this);

			//System.out.println("pacman cords " + pacman.getX() +" "+ pacman.getY());
		}
	}

		// movimiento del pacman
		@Override
		public void actionPerformed(ActionEvent e) {
			requestFocus();
			pacman.muevePacman();
			repaint();
		}

		//reiniciar nivel
		public void reiniciarNivel () {
			pacman.reiniciarVidas();
			blinky.muevePosicionInicial();
			blinky.setComestible(false);
			pinky.muevePosicionInicial();
			pinky.setComestible(false);
			inky.muevePosicionInicial();
			inky.setComestible(false);
			clyde.muevePosicionInicial();
			clyde.setComestible(false);
			pacman.muevePosicionInicial();

			this.cargaMapa();

		}


		private class KAdapter extends KeyAdapter {

			@Override
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if ((!juegoPausado)&&(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)) {
					pacman.keyPressed(e);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if ((!juegoPausado)&&(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)) {
					pacman.keyPressed(e);
				}
				if (key == KeyEvent.VK_P) {
					if (!juegoPausado){
						pacman.pausarPacman();
						juegoPausado = true;
						threadBlinky.suspend();
						threadInky.suspend();
						threadPinky.suspend();
						threadClyde.suspend();

					}
					else {
						pacman.activarPacman();
						juegoPausado = false;
						threadBlinky.resume();
						threadInky.resume();
						threadPinky.resume();
						threadClyde.resume();

					}
				}

			}
		}


		/**
		 * Devuelve el tablero de juego
		 * */
		public MapElementEnum[][] getMap() {
			return map;
		}


		public static int getSegundosRestantesSuperPill() {
			return segundosRestantesSuperPill;
		}

		public static void setSegundosRestantesSuperPill(int segundosRestantesSuperPill) {
			GameBoard.segundosRestantesSuperPill = segundosRestantesSuperPill;
		}

		public static boolean isHuirFantasma() {
			return huirFantasma;
		}

		public void setHuirFantasmas (boolean huir) {
			if (huir) {
				blinky.setComestible(true);
				pinky.setComestible(true);
				inky.setComestible(true);
				clyde.setComestible(true);

				huirFantasma = true;
				if (segundosRestantesSuperPill>0)
					segundosRestantesSuperPill += SEGUNDOS_SUPERPILL;
				else {
					segundosRestantesSuperPill = SEGUNDOS_SUPERPILL;
					Thread demonio = new SuperPillDaemon (this);
					demonio.start();
				}
			}
			else {
				blinky.setComestible(false);
				pinky.setComestible(false);
				inky.setComestible(false);
				clyde.setComestible(false);

				huirFantasma = false;
				segundosRestantesSuperPill =0;
			}
		}

		public boolean isPartidaAcabada() {
			return partidaAcabada;
		}

		public void setPartidaAcabada(boolean partidaAcabada) {
			this.partidaAcabada = partidaAcabada;
		}

		public boolean isReiniciarPosiciones() {
			return reiniciarPosiciones;
		}

		public void setReiniciarPosiciones(boolean reiniciarPosiciones) {
			this.reiniciarPosiciones = reiniciarPosiciones;
		}
		public boolean nivelAcabado () {
			for (Element[] element : mapa) {
				for (int fila = 0; fila < mapa.length - 1; fila++) {
					if (element[fila] instanceof Ball || element[fila] instanceof ExtraBall) {
						//logger.debug("NIVELACABADO: Bola encontrada en "+fila+" "+columna);
						return false;
					}
				}
			}
			return true;
		}


		public Element[][] getMapa() {
			return mapa;
		}

		public void setMapa(Element[][] mapa) {
			this.mapa = mapa;
		}

		public Pacman getPacman() {
			return pacman;
		}
}
