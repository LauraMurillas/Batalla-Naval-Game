package myProject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Battleship
 * @autor Laura Murillas - 201944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 *
 *
 * SeleccionCeldas class:
 * Define la cuadrícula para almacenar Barcos con una cuadrícula de marcadores para
 * indicar detección de aciertos/fallos.
 */
public class SeleccionCeldas extends PintarBarcos {
    /**
     * Tamaño de cada celda de la cuadrícula en píxeles.
     */
    public static final int CELL_SIZE = 30;
    /**
     * Número de celdas de cuadrícula en el eje horizontal.
     */
    public static final int GRID_WIDTH = 10;
    /**
     * Número de celdas de cuadrícula en el eje Vertical.
     */
    public static final int GRID_HEIGHT = 10;
    /**
     *
     * Definiciones del número de Naves, y la cantidad de segmentos que componen cada una de esas naves.
     */
    public static final int[] BOAT_SIZES = {4,3,3,2,2,2,1,1,1,1};

    /**
     * Una cuadrícula de marcadores para indicar visualmente el acierto/fallo de los ataques.
     */
    private Marcador[][] marcadors = new Marcador[GRID_WIDTH][GRID_HEIGHT];
    /**
     * Una lista de todos los barcos en esta cuadrícula.
     */
    private List<Barcos> barcos;
    /**
     * Aleatorio
     */
    private Random rand;
    /**
     * Los barcos se dibujan cuando es true. Esto se usa principalmente para hacer que las naves del jugador siempre se muestren.
     */
    private boolean showShips;
    /**
     * True una vez que todos los elementos de los barcos han sido destruidos.
     */
    private boolean allShipsDestroyed;

    /**
     * Configura la cuadrícula para crear la configuración predeterminada de marcadores.
     *
     */
    public SeleccionCeldas(int x, int y) {
        super(x, y, CELL_SIZE*GRID_WIDTH, CELL_SIZE*GRID_HEIGHT);
        createMarkerGrid();
        barcos = new ArrayList<>();
        rand = new Random();
        showShips = false;
    }

    /**
     * Dibuja los barcos si se van a mostrar todos los barcos en esta cuadrícula,
     * o si cada barco individual está marcado como destruido. Luego dibuja todos los marcadores.
     * que debe mostrarse para los ataques realizados hasta el momento, y una cuadrícula de líneas para mostrar dónde se encuentra la cuadrícula.
     * Está superpuesta.
     *
     */
    public void paint(Graphics g) {
        for(Barcos barcos : this.barcos) {
            if(showShips || /**GamePanel.debugModeActive || */ barcos.isDestruido()) {
                barcos.paint(g);
            }
        }
        drawMarkers(g);
        drawGrid(g);
    }

    /**
     * Modifica el estado de la cuadrícula para mostrar todos los barcos si se establece en true.
     *
     */
    public void setShowShips(boolean showShips) {

        this.showShips = showShips;
    }

    /**
     * Restablece SeleccionCeldas diciéndoles a todos los marcadores que se restablezcan,
     * eliminando todos los barcos de la cuadrícula y volviendo por defecto a no
     * mostrando cualquier barco, y un estado donde no se ha destruido ningún barco.
     */
    public void reset() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                marcadors[x][y].reset();
            }
        }
        barcos.clear();
        showShips = false;
        allShipsDestroyed = false;
    }

    /**
     * Marca la posición especificada y luego verifica todos los barcos para determinar si tienen
     * tod ha sido destruido.
     *
     */
    public boolean markPosition(Posicion posToMark) {
        marcadors[posToMark.x][posToMark.y].mark();

        allShipsDestroyed = true;
        for(Barcos barcos : this.barcos) {
            if(!barcos.isDestruido()) {
                allShipsDestroyed = false;
                break;
            }
        }
        return marcadors[posToMark.x][posToMark.y].isShip();
    }

    /**
     * Comprueba si todos los barcos han sido destruidos.
     */
    public boolean areAllShipsDestroyed() {
        return allShipsDestroyed;
    }

    /**
     * Este metodo retorna TRUE si la posicion esta marcada
     */
    public boolean isPositionMarked(Posicion posToTest) {
        return marcadors[posToTest.x][posToTest.y].isMarked();
    }


    /**
     * Obtiene el marcador en la posición especificada.
     * Sirve tambien para permitir que la computadora tenga más acceso a los datos en la cuadrícula del jugador.
     * Retorna una referencia al marcador en la posición especificada.
     */
    public Marcador getMarkerAtPosition(Posicion posToSelect) {

        return marcadors[posToSelect.x][posToSelect.y];
    }


    /**
     * Este metodo hace que la posicion del mouse se vuelva una coordenada en la cuadricula si es posible.
     * Devuelve (-1,-1) para una posición no válida (como fuera de la cuadricula)
     * o la posición de cuadrícula correspondiente
     */
    public Posicion getPositionInGrid(int mouseX, int mouseY) {
        if(!isPositionInside(new Posicion(mouseX,mouseY))) return new Posicion(-1,-1);

        return new Posicion((mouseX - posicion.x)/CELL_SIZE, (mouseY - posicion.y)/CELL_SIZE);
    }

    /**
     * Este metodo prueba si un barco con las propiedades especificadas (alto y ancho) sería válido para su colocación.
     * Prueba esto comprobando si el barco encaja dentro de los límites de la cuadrícula
     * y luego comprueba si todos los segmentos caerían en los lugares donde.
     * Esto se maneja por separado dependiendo de si es un barco horizontal (de lado) o vertical
     * si sideway es True indica que es horizontal, si es falso es vertical.
     * Retorna True si el barco se puede colocar con las propiedades especificadas en la cuadricula.
     */
    public boolean canPlaceShipAt(int gridX, int gridY, int segments, boolean sideways) {
        if(gridX < 0 || gridY < 0) return false;

        if(sideways) { // En el caso que sea horizontal
            if(gridY > GRID_HEIGHT || gridX + segments > GRID_WIDTH) return false;
            for(int x = 0; x < segments; x++) {
                if(marcadors[gridX+x][gridY].isShip()) return false;
            }
        } else { // En caso de que sea vertical
            if(gridY + segments > GRID_HEIGHT || gridX > GRID_WIDTH) return false;
            for(int y = 0; y < segments; y++) {
                if(marcadors[gridX][gridY+y].isShip()) return false;
            }
        }
        return true;
    }

    /**
     * Dibuja una cuadrícula formada por líneas negras de un solo píxel.
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        // Dibuja lineas verticales
        int y2 = posicion.y;
        int y1 = posicion.y+height;
        for(int x = 0; x <= GRID_WIDTH; x++)
            g.drawLine(posicion.x+x * CELL_SIZE, y1, posicion.x+x * CELL_SIZE, y2);

        // Dibuja lineas horizontales
        int x2 = posicion.x;
        int x1 = posicion.x+width;
        for(int y = 0; y <= GRID_HEIGHT; y++)
            g.drawLine(x1, posicion.y+y * CELL_SIZE, x2, posicion.y+y * CELL_SIZE);
    }

    /**
     * Dibuja todos los marcadores. Los marcadores determinarán individualmente si es necesario dibujar.
     */
    private void drawMarkers(Graphics g) {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                marcadors[x][y].paint(g);
            }
        }
    }

    /**
     * Crea todos los objetos de marcador estableciendo sus posiciones de dibujo en la cuadrícula para inicializarlos.
     */
    private void createMarkerGrid() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                marcadors[x][y] = new Marcador(posicion.x+x*CELL_SIZE, posicion.y + y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Borra todos los barcos actuales y luego coloca aleatoriamente todos los barcos.
     * Los barcos no se colocarán encima de otros barcos.
     * Este método asume que hay mucho espacio para colocar todas las naves sin importar la configuración.
     */
    public void populateShips() {
        barcos.clear();
        for(int i = 0; i < BOAT_SIZES.length; i++) {
            boolean sideways = rand.nextBoolean();
            int gridX,gridY;
            do {
                gridX = rand.nextInt(sideways?GRID_WIDTH-BOAT_SIZES[i]:GRID_WIDTH);
                gridY = rand.nextInt(sideways?GRID_HEIGHT:GRID_HEIGHT-BOAT_SIZES[i]);
            } while(!canPlaceShipAt(gridX,gridY,BOAT_SIZES[i],sideways));
            placeShip(gridX, gridY, BOAT_SIZES[i], sideways);
        }
    }

    /**
     * Coloca un barco en la cuadrícula con las propiedades especificadas.
     * Supone que ya se han realizado comprobaciones para verificar que el barco se puede colocar allí.
     * Indica a las celdas de marcador que un barco está encima de ellas para usar para la colocación de otros barcos y
     * la detección de impactos.
     */
    public void placeShip(int gridX, int gridY, int segments, boolean sideways) {
        placeShip(new Barcos(new Posicion(gridX, gridY),
                new Posicion(posicion.x+gridX*CELL_SIZE, posicion.y+gridY*CELL_SIZE),
                segments, sideways), gridX, gridY);
    }

    /**
     * Coloca un barco en la cuadrícula con las propiedades especificadas.
     * Supone que ya se han realizado comprobaciones para verificar que el barco se puede colocar allí.
     * Indica a las celdas de marcador que un barco está encima de ellas para usar para la colocación de otros barcos
     * y la detección de impactos.
     */
    public void placeShip(Barcos barcos, int gridX, int gridY) {
        this.barcos.add(barcos);
        if(barcos.isCualPosicion()) { // Si el barco esta horizontal
            for(int x = 0; x < barcos.getSegmentos(); x++) {
                marcadors[gridX+x][gridY].setAsShip(this.barcos.get(this.barcos.size()-1));
            }
        } else { // Si el barco esta vertical
            for(int y = 0; y < barcos.getSegmentos(); y++) {
                marcadors[gridX][gridY+y].setAsShip(this.barcos.get(this.barcos.size()-1));
            }
        }
    }
}
