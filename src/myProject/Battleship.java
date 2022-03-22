package myProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas andrade - 1944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Class Battleship - Version 1.0.0  Date 08/03/2022
 */

/**
 * Clase BattleShip:
 * Clase modelo que controla la logica del juego (del computador)
 * El metodo selectMove() es el que determina cual movimiento se debe aplica
 * Debe ser anulado por clases para implementar una funcionalidad real.
 */

public class Battleship {
    /**
     * Atributos.
     */

    //Esta es la cuadrícula controlada por el jugador para probar ataques.
    protected SeleccionCeldas playerGrid;

    //Una lista de todos los movimientos válidos.
    // Se puede actualizar después de los movimientos.
    protected List<Posicion> validMoves;


    /**
     * Crea la configuracion basica, como las celdas del jugador
     * y crea la lista de los movimientos validos
     */
    public Battleship(SeleccionCeldas playerGrid) {
        this.playerGrid = playerGrid;
        createValidMoveList();
    }

    /**
     * Este metodo se usa en la logica (Clase GamePanel) para escoger la posicion para atacar
     * Devuelve por defecto la posicion ZERO (X=0 y Y=0)
     */
    public Posicion selectMove() {

        return Posicion.ZERO;
    }

    /**
     * Vuelve a crear una lista valida de movimientos
     */
    public void reset() {

        createValidMoveList();
    }

    /**
     * Crea una lista de movimientos validos
     * Se llena una lista con coordenadas (X y Y) de una cuadricula.
     */
  private void createValidMoveList() {
      validMoves = new ArrayList<>();
       for(int x = 0; x < SeleccionCeldas.GRID_WIDTH; x++) {
          for(int y = 0; y < SeleccionCeldas.GRID_HEIGHT; y++) {
            validMoves.add(new Posicion(x,y));
          }
       }
    }
}

