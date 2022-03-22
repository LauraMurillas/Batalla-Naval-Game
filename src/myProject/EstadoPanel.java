package myProject;

import java.awt.*;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas andrade - 1944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Class EstadoPanel - Version 1.0.0  Date 08/03/2022
 */


/**
 * Clase EstadoPanel:
 * Define un panel de texto simple para mostrar una línea superior e inferior de texto.
 * Algunos de estos ya están definidos en la clase, y proporciona
 * métodos adicionales para establecer los mensajes en valores personalizados.
 */
public class EstadoPanel extends PintarBarcos {
    /**
     * Atributos
     */

    //La fuente que se usará para dibujar ambos mensajes.
    private final Font font = new Font("Arial", Font.BOLD, 20);

    //Mensaje para mostrar en la línea superior mientras se coloca el barco.
    private final String linea1 = "¡Ubica los barcos a la izquierda, este será tu tablero!";

    //Mensaje para mostrar en la línea inferior mientras se coloca el barco.
    private final String linea2 = "Oprime G para cambiar la orientación.";

    //Mensaje para mostrar en la línea superior cuando se pierde el juego.
    private final String perdio = "Perdiste :(";

    //Mensaje para mostrar en la línea superior cuando se gana el juego.
    private final String gano = "Ganaste :D";

    //Mensaje para mostrar en la línea inferior cuando se gana o se pierde el juego.
    //Le indica al jugador como reiniciar el juego
    private final String reinicio = "Presiona R para reiniciar.";

    //El mensaje actual que se mostrará en la línea superior.
    private String Linea1;

    //El mensaje actual que se mostrará en la línea inferior.
    private String Linea2;

    /**
     * Configura el panel de estado para que esté listo para dibujar un fondo,
     * y texto predeterminado inicial.
     */
    public EstadoPanel(Posicion posicion, int width, int height) {
        super(posicion, width, height);
        reset();
    }

    /**
     * Este metodo restablece el mensaje a los valores predeterminados para la ubicación del barco.
     */
    public void reset() {
        Linea1 = linea1;
        Linea2 = linea2;
    }

    /**
     * Establece el mensaje que se mostrará en función de si el jugador ha ganado o perdido.
     * el objeto playerWon es Verdadero si el jugador ganó, o falso si el jugador perdió
     */
    public void showGameOver(boolean playerWon) {
        Linea1 = (playerWon) ? gano : perdio;
        Linea2 = reinicio;
    }

    /**
     * Establece el mensaje para que se muestre en la línea superior de salida en cualquier cadena especificada.
     *
     * @param message Mensaje para mostrar en la línea superior.
     */
    public void setLinea1(String message) {
        Linea1 = message;
    }

    /**
     * Establece el mensaje para que se muestre en la línea inferior de la salida en cualquier cadena especificada.
     *
     * @param message Mensaje para mostrar en la línea inferior.
     */
    public void setLinea2(String message) {
        Linea2 = message;
    }

    /**
     * Dibuja un fondo gris claro con texto negro centrado sobre dos líneas usando
     * los mensajes de la línea superior e inferior
     */
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(posicion.x, posicion.y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(Linea1);
        g.drawString(Linea1, posicion.x+width/2-strWidth/2, posicion.y+20);
        strWidth = g.getFontMetrics().stringWidth(Linea2);
        g.drawString(Linea2, posicion.x+width/2-strWidth/2, posicion.y+40);
    }
}
