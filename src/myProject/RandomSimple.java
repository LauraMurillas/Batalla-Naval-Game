package myProject;

import java.util.Collections;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas andrade - 1944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Class Random simple - Version 1.0.0  Date 08/03/2022
 */

/**
 * SimpleRandom class:
 * Simplemente barajará una lista de movimientos válidos en un
 * orden aleatorio, y luego selecciona los movimientos basado en los que aparecen en la lista
 */
   public class RandomSimple extends Battleship {
    /**
     * Inicializa la computadora al aleatorizar el orden de los movimientos.
     *
     * @param playerGrid Referencia el grid del jugador para atacar.
     */
    public RandomSimple(SeleccionCeldas playerGrid) {
        super(playerGrid);
        Collections.shuffle(validMoves);
    }

    /**
     * Restablece la computadora restableciendo la clase principal y luego
     * reorganizar la lista actualizada de movimientos válidos.
     */
    @Override
    public void reset() {
        super.reset();
        Collections.shuffle(validMoves);
    }

    /**
     * Toma el movimiento de la parte superior de la lista y lo devuelve.
     *
     * @return Una posición de la lista de movimientos válidos.
     */
    @Override
    public Posicion selectMove() {
        Posicion nextMove = validMoves.get(0);
        validMoves.remove(0);
        return nextMove;
    }
}
