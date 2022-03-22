package myProject;

import java.awt.*;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas andrade - 1944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Class Game - Version 1.0.0  Date 08/03/2022
 */

/**
 * Clase Marcador:
 * Crea los rectangulos que se usan para dibujar los barcos un rectángulo de color simple que puede ser
 * Puede estar en estado Visible u oculto, y cambiará de color en función de si
 * representa el lugar donde se encuentra un barco.
 */
public class Marcador extends PintarBarcos {
    /**
     * Atributos
     */

    //El color para mostrar cuando un barco se encuentra en esta clase Marcador.
    private final Color HIT_COLOR = new Color(219, 23, 23, 180);

    //El color que se muestra cuando no hay ningún barco en este marcador.
    private final Color MISS_COLOR = new Color(26, 26, 97, 180);

    //Relleno alrededor de los bordes del rectángulo coloreado para hacerlo un poco más pequeño.
    private final int PADDING = 2;

    //Cuando sea VERDADERO, el Marcador se pintará.
    private boolean showMarker;

    //Cambia el color utilizado para dibujar el barco.
    // Cuando se asigna un barco, se usará HIT_COLOUR, cuando sea nulo, se usará MISS_COLOUR.
    private Barcos barcosAtMarker;

    /**
     * Prepara el marcador con un estado predeterminado donde está listo para dibujarse en el tablero
     * en una posición específica sin barco asociado por defecto.
     * Utiliza:
     * Coordenada en X para dibujar este marcador.
     * Coordenada en Y para dibujar este marcador.
     * width (ancho de la celda del marcador).
     * height (Altura de la celda del marcador).
     */
    public Marcador(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    /**
     * Se restablece a un barco sin referencia y con el marcador no visible.
     */
    public void reset() {
        barcosAtMarker = null;
        showMarker = false;
    }

    /**
     *Si no se ha marcado previamente, le indicará al barco asociado que se ha destruido otra sección.
     * Luego marque el marcador para hacerlo visible para dibujar.
     */
    public void mark() {
        if(!showMarker && isShip()) {
            barcosAtMarker.seccionDestruida();
        }
        showMarker = true;
    }

    /**
     * Este metodo sirve para saber si ya se ha interactuado con el marcador.
     * Retorna True si el marcador está visible.
     */
    public boolean isMarked() {
        return showMarker;
    }

    /**
     * Establece el barco en la referencia especificada (cuadricula). Cambia el color utilizado si el marcador es visible y
     * permite la notificación en el barco si se esta usando el Marcador
     */
    public void setAsShip(Barcos barcos) {

        this.barcosAtMarker = barcos;
    }

    /**
     *Este metodo retorna True si se ha configurado un barco (esta asociado a un barco).
     */
    public boolean isShip() {

        return barcosAtMarker != null;
    }

    /**
     * Este metodo devuelve el barco asociado si lo hay, de lo contrario será nulo.
     */
    public Barcos getAssociatedShip() {

        return barcosAtMarker;
    }

    /**
     * No retorna nada si no está marcado.
     * Dibuja un rectángulo para que coincida con el tamaño de relleno correcto del marcador.
     * Utiliza el color en función de si este objeto está sobre un barco.
     */
    public void paint(Graphics g) {
        if(!showMarker) return;

        g.setColor(isShip() ? HIT_COLOR : MISS_COLOR);
        g.fillRect(posicion.x+PADDING+1, posicion.y+PADDING+1, width-PADDING*2, height-PADDING*2);
    }
}
