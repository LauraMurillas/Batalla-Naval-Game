package myProject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas andrade - 1944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Class Barcos - Version 1.0.0  Date 08/03/2022
 */

/**
 * Clase Barcos:
 * Define un objeto de barco simple que se puede dibujar en la pantalla..
 * información sobre cuántos segmentos incluye, la dirección de la
 * barcos, cuántos de los segmentos han sido destruidos y propiedades para
 * establecer el color.
 */
public class Barcos {
    /**
     * Atributos
     */

    //Se utiliza para el cambio de color cuando se esta colocando un barco.
    //Válido: indica que el barco podría colocarse en la ubicación actual que se muestra como un barco verde.
    //No válido: indica que el barco no se puede colocar en la ubicación actual que se muestra como un barco rojo.
    //Colocado: se utiliza cuando el barco se ha colocado y utilizará la configuración de color predeterminada.
    public enum ColorPosicion {Valid, Invalid, Placed}

    //Estas son las coordenadas de la posicion donde se encuentra el barco en la cuadricula.
    private Posicion celdaPosicion;

    //La posicion en pixeles para dibujar el barco
    private Posicion drawPosicion;

    //El numero de segmentos en el barco para mostrar cuantas celdas atraviesa
    private int segmentos;

    //Indica la posicion del barco. Si es TRUE el barco esta en HORIZONTAL, si es FALSE el barco esta en VERTICAL
    private boolean cualPosicion;


    //Es el numero de secciones destruidas
    // Ayuda a determinar si todo el barco ha sido destruido en comparacion con los segmentos.
    private int seccionesDestruidas;

    //Se utiliza para cambiar el color de los barcos durante la colocación manual por parte del jugador
    //Si es VERDE la posicion es valida, si es ROJA no es valida.
    private ColorPosicion colorPosicion;



    /**
     * Crea el barco con propiedades predeterminadas listo para usar. Supone que ya se ha colocado cuando se creó.
     */
    public Barcos(Posicion celdaPosicion, Posicion drawPosicion, int segments, boolean cualPosicion) {
        this.celdaPosicion = celdaPosicion;
        this.drawPosicion = drawPosicion;
        this.segmentos = segments;
        this.cualPosicion = cualPosicion;
        seccionesDestruidas = 0;
        colorPosicion = ColorPosicion.Placed;
    }

    /**
     * Dibuja el barco seleccionando primero el color y luego dibujando el barco en la dirección correcta.
     * El color se selecciona para que sea: verde si se está colocando actualmente y es válido, rojo si se está colocando y no es válido.
     * Si ya se ha colocado, se mostrará en rojo si está destruido o en gris oscuro en cualquier otro caso.
     * Aqui g es el objeto Graphics
     */
    public void paint(Graphics g) {
        if(colorPosicion == ColorPosicion.Placed) {
            g.setColor(seccionesDestruidas >= segmentos ? Color.RED : Color.DARK_GRAY);
        } else {
            g.setColor(colorPosicion == ColorPosicion.Valid ? Color.GREEN : Color.RED);
        }
        if(cualPosicion) paintHorizontal(g);
        else paintVertical(g);
    }

    /**
     * Define el color de la ubicacion para indicar el estado del barco.
     * las válidas se muestran verde, las no válidas se muestran rojo.
     */
    public void definirPosicionColor(ColorPosicion colorPosicion) {
        this.colorPosicion = colorPosicion;
    }

    /**
     * Este metodo alterna el estado actual del barco entre vertical y horizontal.
     */
    public void cambiarOrientacion() {
        cualPosicion = !cualPosicion;
    }

    /**
     * Este metodo se invoca cuando se haya destruido una sección
     * para que el barco lleve un registro de cuántas secciones se han destruido.
     */
    public void seccionDestruida() {
        seccionesDestruidas++;
    }

    /**
     * Este metodo Comprueba si el barco ha sido destruido
     * Si el numero de secciones destruidas es Mayor o igual al numero de segmentos retorna un TRUE, es decir, SI se destruyó
     */
    public boolean isDestruido() { return seccionesDestruidas >= segmentos; }

    /**
     * Actualiza la posicion para dibujar el barco en una nueva posicion.
     */
    public void setDrawPosicion(Posicion gridPosicion, Posicion drawPosicion) {
        this.drawPosicion = drawPosicion;
        this.celdaPosicion = gridPosicion;
    }

    /**
     * Este metodo retorna la direccion del barco
     * retorna TRUE si el barco esta en horizontal y FALSE si esta en vertical
     */
    public boolean isCualPosicion() {
        return cualPosicion;
    }

    /**
     * Retorna el numero de segmentos que tiene el barco
     */
    public int getSegmentos() {
        return segmentos;
    }

    /**
     * Retorna una lista de todas las celdas que ocupa esta nave.
     */
    public List<Posicion> getOccupiedCoordinates() {
        List<Posicion> result = new ArrayList<>();
        if(cualPosicion) { // handle the case when horizontal
            for(int x = 0; x < segmentos; x++) {
                result.add(new Posicion(celdaPosicion.x+x, celdaPosicion.y));
            }
        } else { // handle the case when vertical
            for(int y = 0; y < segmentos; y++) {
                result.add(new Posicion(celdaPosicion.x, celdaPosicion.y+y));
            }
        }
        return result;
    }

    /**
     * Dibuja el barco vertical dibujando primero un triángulo para la primera celda y luego
     * un rectángulo en las celdas restantes (el número de segmentos restantes del barco).
     */
    public void paintVertical(Graphics g) {
        int barcoWidth = (int)(SeleccionCeldas.CELL_SIZE * 0.8);
        int barcoLeftX = drawPosicion.x + SeleccionCeldas.CELL_SIZE / 2 - barcoWidth / 2;
        g.fillPolygon(new int[]{drawPosicion.x+ SeleccionCeldas.CELL_SIZE/2,barcoLeftX,barcoLeftX+barcoWidth},
                new int[]{drawPosicion.y+ SeleccionCeldas.CELL_SIZE/4, drawPosicion.y+ SeleccionCeldas.CELL_SIZE, drawPosicion.y+ SeleccionCeldas.CELL_SIZE},3);
        g.fillRect(barcoLeftX, drawPosicion.y+ SeleccionCeldas.CELL_SIZE, barcoWidth,
                (int)(SeleccionCeldas.CELL_SIZE * (segmentos-1.2)));
    }

    /**
     * Dibuja el barco Horizontal dibujando primero un triángulo para la primera celda y luego
     * un rectángulo en las celdas restantes (el número de segmentos restantes del barco).
     */
    public void paintHorizontal(Graphics g) {
        int barcoWidth = (int)(SeleccionCeldas.CELL_SIZE * 0.8);
        int barcoTopY = drawPosicion.y + SeleccionCeldas.CELL_SIZE / 2 - barcoWidth / 2;
        g.fillPolygon(new int[]{drawPosicion.x+ SeleccionCeldas.CELL_SIZE/4, drawPosicion.x+ SeleccionCeldas.CELL_SIZE, drawPosicion.x+ SeleccionCeldas.CELL_SIZE},
                new int[]{drawPosicion.y+ SeleccionCeldas.CELL_SIZE/2,barcoTopY,barcoTopY+barcoWidth},3);
        g.fillRect(drawPosicion.x+ SeleccionCeldas.CELL_SIZE,barcoTopY,
                (int)(SeleccionCeldas.CELL_SIZE * (segmentos-1.2)), barcoWidth);
    }
}
