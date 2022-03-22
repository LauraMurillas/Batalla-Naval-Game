package myProject;

/**
 * Battleship
 * @autor Laura Murillas - 201944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 *
 * Posicion class:
 * Se utiliza para representar las posiciones x,y.
 */
public class Posicion {
    /**
     * Vector de movimiento hacia abajo.
     */
    public static final Posicion DOWN = new Posicion(0,1);
    /**
     * Vector de movimiento hacia arriba
     */
    public static final Posicion UP = new Posicion(0,-1);
    /**
     * Vector de movimiento hacia la izquierda
     */
    public static final Posicion LEFT = new Posicion(-1,0);
    /**
     * Vector de movimiento hacia la derecha
     */
    public static final Posicion RIGHT = new Posicion(1,0);
    /**
     * Venctor 0
     */
    public static final Posicion ZERO = new Posicion(0,0);

    /**
     * coordenada X.
     */
    public int x;
    /**
     * coordenada Y.
     */
    public int y;

    /**
     * Establece el valor de Posición.
     *
     */
    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copie el constructor para crear una nueva Posición usando los valores en otra.
     *
     */
    public Posicion(Posicion posicionToCopy) {
        this.x = posicionToCopy.x;
        this.y = posicionToCopy.y;
    }

    /**
     * Establece la Posición en las coordenadas x e y especificadas.
     *
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Actualiza esta posición agregando los valores de otherPosition.
     *
     */
    public void add(Posicion otherPosicion) {
        this.x += otherPosicion.x;
        this.y += otherPosicion.y;
    }

    /**
     * Calcular la distancia desde esta posición a la otra posición.
     *
     */
    public double distanceTo(Posicion otherPosicion) {
        return Math.sqrt(Math.pow(x- otherPosicion.x,2)+Math.pow(y- otherPosicion.y,2));
    }

    /**
     * Multiplica ambos componentes de la posición por una cantidad.
     *
     */
    public void multiply(int amount) {
        x *= amount;
        y *= amount;
    }

    /**
     * Actualiza esta posición restando los valores de la otra posición.
     *
     */
    public void subtract(Posicion otherPosicion) {
        this.x -= otherPosicion.x;
        this.y -= otherPosicion.y;
    }

    /**
     * Compara el objeto Posición con otro objeto.
     * Cualquier objeto que no sea Posición devolverá falso. De lo contrario, compara x e y para la igualdad.
     *
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion posicion = (Posicion) o;
        return x == posicion.x && y == posicion.y;
    }

    /**
     * Obtiene una versión de cadena de Position.
     *
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
