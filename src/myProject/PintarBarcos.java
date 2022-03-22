package myProject;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas andrade - 1944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Clase PintarBarcos - Version 1.0.0  Date 08/03/2022
 */

/**
 * Clase PintarBarcos:
 * Define un Rectángulo simple con una posición para la esquina superior izquierda
 * y un ancho/alto para representar el tamaño del Rectángulo.
 */
public class PintarBarcos {
    /**
     * Atributos
     */

    //Esta es la esquina superior izquierda del Rectángulo.
    protected Posicion posicion;

    //Ancho del Rectángulo.
    protected int width;

    //Alto del rectangulo
    protected int height;



    /**
     * Crea el nuevo Rectángulo con las propiedades proporcionadas.
     */
    public PintarBarcos(Posicion posicion, int width, int height) {
        this.posicion = posicion;
        this.width = width;
        this.height = height;
    }

    /**
     * Pinta el barco con las coordenadas X y Y en la esquina superior izquierda
     */
    public PintarBarcos(int x, int y, int width, int height) {
        this(new Posicion(x,y),width,height);
    }


    /**
     * Este metodo obtiene el alto del barco
     */
    public int getHeight() {
        return height;
    }


    /**
     * Este metodo obtiene el ancho del barco
     */
    public int getWidth() {
        return width;
    }

    /**
     * Este metodo obtiene la posicion de la esquina superior izquierda del rectangulo
     */
    public Posicion getPosition() {
        return posicion;
    }

    /**
     * Este metodo retorna TRUE si targetPosicion esta dentro del rectangulo
     */
    public boolean isPositionInside(Posicion targetPosicion) {
        return targetPosicion.x >= posicion.x && targetPosicion.y >= posicion.y
                && targetPosicion.x < posicion.x + width && targetPosicion.y < posicion.y + height;
    }
}
