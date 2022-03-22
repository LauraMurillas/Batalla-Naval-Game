package myProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas - 201944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Class GamePanel - Version 1.0.0  Date: 08/03/2022
 */

/**
 * GamePanel class:
 * Maneja toda la información de estado y la interacción entre los elementos del juego.
 * Controla dos cuadrículas, una para el jugador y otra para la computadora, con un
 * panel de estado entre ellos. Dependiendo del estado del juego, el jugador puede
 * coloque barcos en su cuadrícula o ataque la cuadrícula de la computadora. El estado
 * El panel muestra el estado actual entre las dos cuadrículas.
 */
public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
    /**
     * GameStates que cambian la forma en que se puede llevar a cabo la interacción.
     * Colocación de barcos: en este estado, el jugador puede colocar barcos en su tablero.
     *               El estado termina cuando se han colocado todos los barcos.
     * FiringShots: el jugador puede atacar la cuadrícula de la computadora y recibir respuestas.
     *               El estado termina cuando todos los barcos en cualquiera de las cuadrículas han sido destruidos.
     * GameOver: cuando el jugador o la computadora han sido destruidos para evitar la entrada.
     *            Finaliza cuando el jugador sale o elige reiniciar.
     */
    public enum GameState { PlacingShips, FiringShots, GameOver }

    //Referencia al panel de estado para pasar mensajes de texto para mostrar lo que está sucediendo.
    private EstadoPanel estadoPanel;

    //La cuadrícula de la computadora para que el jugador ataque.
    private SeleccionCeldas computer;

    //La cuadrícula del jugador para que la computadora ataque.
    private SeleccionCeldas player;

    //Administra lo que hará la computadora en cada turno.
    private Battleship aiController;

    //Referencia al barco temporal que se está colocando durante el estado ColocandoBarcos.
    private Barcos placingBarcos;

    //Posición de cuadrícula donde se encuentra el barco de colocación.
    private Posicion tempPlacingPosicion;

    //Referencia a qué barco debe colocarse a continuación durante el estado Colocación de barcos.
    private int placingShipIndex;

    // El estado del juego para representar si el jugador puede colocar barcos, atacar la computadora,
    // o si el juego ya ha terminado.
    private GameState gameState;

    //Un estado que se puede alternar con D para mostrar los barcos de la computadora.
    public static boolean debugModeActive;



    /**
     * Inicializa todo lo necesario para comenzar a jugar. Las cuadrículas para cada jugador se inicializan y
     * luego se usa para determinar cuánto espacio se requiere. Los escucha están conectados, AI configurados y
     * tod listo para comenzar el juego colocando un barco para el jugador.
     */
    public GamePanel(int aiChoice) {
        computer = new SeleccionCeldas(350,50);
        player = new SeleccionCeldas(0,50);

        setBackground(new Color(0, 240, 255));

        setPreferredSize(new Dimension(660, 360));
        addMouseListener(this);
        addMouseMotionListener(this);
        if(aiChoice == 0) aiController = new RandomSimple(player);

        estadoPanel = new EstadoPanel(new Posicion(0,0),660,49);
        restart();
    }

    /**
     * Dibuja las cuadrículas para ambos jugadores, cualquier barco que se coloque y el panel de estado.
     */
    public void paint(Graphics g) {
        super.paint(g);
        computer.paint(g);
        player.paint(g);
        if(gameState == GameState.PlacingShips) {
            placingBarcos.paint(g);
        }
        estadoPanel.paint(g);
    }

    /**
     * Este metodo controla las entradas del teclado
     * Presionar Escape sale de la aplicación.
     * Presionar R se reinicia el juego.
     * Presionar Z gira la nave mientras está en estado ColocandoBarcos.
     * Presioanr D activa el modo de depuración para mostrar las naves de la computadora.
     * Aqui KeyCode es el que tiene la tecla presionada, viene de la clase Game
     */
    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        } else if(keyCode == KeyEvent.VK_R) {
            restart();
        } else if(gameState == GameState.PlacingShips && keyCode == KeyEvent.VK_G) {
            placingBarcos.cambiarOrientacion();
            updateShipPlacement(tempPlacingPosicion);
        } else if(keyCode == KeyEvent.VK_D) {
            debugModeActive = !debugModeActive;
        }
        repaint();
    }

    /**
     * Restablece todas las propiedades de la clase a sus valores predeterminados listos para que comience un nuevo juego.
     */
    public void restart() {
        computer.reset();
        player.reset();
        // Player can see their own ships by default
        player.setShowShips(true);
        aiController.reset();
        tempPlacingPosicion = new Posicion(0,0);
        placingBarcos = new Barcos(new Posicion(0,0),
                new Posicion(player.getPosition().x,player.getPosition().y),
                SeleccionCeldas.BOAT_SIZES[0], true);
        placingShipIndex = 0;
        updateShipPlacement(tempPlacingPosicion);
        computer.populateShips();
  //      debugModeActive = false;
        estadoPanel.reset();
        gameState = GameState.PlacingShips;
    }

    /**
     * Utiliza la posición del ratón para probar la actualización del barco que se está colocando durante el estado ColocandoBarco.
     * Luego, si el lugar en el que se ha colocado es válido, el barco se bloqueará llamando a placeShip().
     * Usa las coordenadas del mouse
     */
    private void tryPlaceShip(Posicion mousePosicion) {
        Posicion targetPosicion = player.getPositionInGrid(mousePosicion.x, mousePosicion.y);
        updateShipPlacement(targetPosicion);
        if(player.canPlaceShipAt(targetPosicion.x, targetPosicion.y,
                SeleccionCeldas.BOAT_SIZES[placingShipIndex], placingBarcos.isCualPosicion())) {
            placeShip(targetPosicion);
        }
    }

    /**
     * Finaliza la inserción del barco que se está colocando almacenándola en la cuadrícula del jugador.
     * Luego, prepara el siguiente barco para colocarla o pasa al siguiente estado.
     */
    private void placeShip(Posicion targetPosicion) {
        placingBarcos.definirPosicionColor(Barcos.ColorPosicion.Placed);
        player.placeShip(placingBarcos, tempPlacingPosicion.x, tempPlacingPosicion.y);
        placingShipIndex++;
        // Si aun quedan barcos por colocar
        if(placingShipIndex < SeleccionCeldas.BOAT_SIZES.length) {
            placingBarcos = new Barcos(new Posicion(targetPosicion.x, targetPosicion.y),
                    new Posicion(player.getPosition().x + targetPosicion.x * SeleccionCeldas.CELL_SIZE,
                            player.getPosition().y + targetPosicion.y * SeleccionCeldas.CELL_SIZE),
                    SeleccionCeldas.BOAT_SIZES[placingShipIndex], true);
            updateShipPlacement(tempPlacingPosicion);
        } else {
            gameState = GameState.FiringShots;
            estadoPanel.setLinea1("Oprime las celdas en el tablero derecho para atacar a tu enemigo!");
            estadoPanel.setLinea2("Destruye todos los barcos para ganar!");
        }
    }

    /**
     * Intentos de disparar a una posición en el tablero de la computadora.
     * Se notifica al jugador si acertó/falló, si volvió a hacer clic en el mismo lugar (celda) no hace nada.
     * Después del turno del jugador, la computadora recibe un turno si el juego aún no ha terminado.
     */
    private void tryFireAtComputer(Posicion mousePosicion) {
        Posicion targetPosicion = computer.getPositionInGrid(mousePosicion.x, mousePosicion.y);
        // Ignorar si ya se hizo clic en la posición
        if(!computer.isPositionMarked(targetPosicion)) {
            doPlayerTurn(targetPosicion);
            // Solo le da el turno a la computadora si el juego no terminó en el turno del jugador (o sea, el jugador gano).
            if(!computer.areAllShipsDestroyed()) {
                doAITurn();
            }
        }
    }

    /**
     * Procesa el turno del JUGADOR en función de dónde seleccionó atacar.
     * Según el resultado del ataque, se muestra un mensaje al jugador,
     * y si destruyó el último barco, el juego se actualiza a un estado ganado.
     */
    private void doPlayerTurn(Posicion targetPosicion) {
        boolean hit = computer.markPosition(targetPosicion);
        String hitMiss = hit ? "¡Le diste!" : "Fallo";
        String destroyed = "";
        if(hit && computer.getMarkerAtPosition(targetPosicion).getAssociatedShip().isDestruido()) {
            destroyed = "(Destruido)";
        }
        estadoPanel.setLinea1("Jugador " + hitMiss + " " + targetPosicion + destroyed);
        if(computer.areAllShipsDestroyed()) {
            // Jugador GANA
            gameState = GameState.GameOver;
            estadoPanel.showGameOver(true);
        }
    }

    /**
     * Procesa el turno de la COMPUTADORA usando el controlador de la computadorA para seleccionar un movimiento.
     * Luego procesa el resultado para mostrárselo al jugador.
     * Si la computadora destruyó el último barco, el juego terminará con la victoria de la computadora.
     */
    private void doAITurn() {
        Posicion aiMove = aiController.selectMove();
        boolean hit = player.markPosition(aiMove);
        String hitMiss = hit ? "¡Te dieron!" : " Computadora Falló";
        String destroyed = "";
        if(hit && player.getMarkerAtPosition(aiMove).getAssociatedShip().isDestruido()) {
            destroyed = "(Destruido)";
        }
        estadoPanel.setLinea2("Computer " + hitMiss + " " + aiMove + destroyed);
        if(player.areAllShipsDestroyed()) {
            // Computadora GANA
            gameState = GameState.GameOver;
            estadoPanel.showGameOver(false);
        }
    }

    /**
     * Actualiza la ubicación del barco que se está colocando si el mouse está dentro de la cuadrícula.
     */
    private void tryMovePlacingShip(Posicion mousePosicion) {
        if(player.isPositionInside(mousePosicion)) {
            Posicion targetPos = player.getPositionInGrid(mousePosicion.x, mousePosicion.y);
            updateShipPlacement(targetPos);
        }
    }

    /**
     * Restringe el barco para que quepa dentro de la cuadrícula.
     * Actualiza la posición dibujada del barco y cambia el color del barco en función de si es una ubicación válida o no válida.
     */
    private void updateShipPlacement(Posicion targetPos) {
        // Hace que el barco quepa dentro de la cuadricula
        if(placingBarcos.isCualPosicion()) {
            targetPos.x = Math.min(targetPos.x, SeleccionCeldas.GRID_WIDTH - SeleccionCeldas.BOAT_SIZES[placingShipIndex]);
        } else {
            targetPos.y = Math.min(targetPos.y, SeleccionCeldas.GRID_HEIGHT - SeleccionCeldas.BOAT_SIZES[placingShipIndex]);
        }
        // Actualiza la posición del dibujo para usar en la nueva posición
        placingBarcos.setDrawPosicion(new Posicion(targetPos),
                new Posicion(player.getPosition().x + targetPos.x * SeleccionCeldas.CELL_SIZE,
                        player.getPosition().y + targetPos.y * SeleccionCeldas.CELL_SIZE));
        // Almacena la posición de la cuadricula para otros casos de prueba
        tempPlacingPosicion = targetPos;
        // Cambia el color del barco dependiendo de si se puede colocar o no en la ubicación actual.
        if(player.canPlaceShipAt(tempPlacingPosicion.x, tempPlacingPosicion.y,
                SeleccionCeldas.BOAT_SIZES[placingShipIndex], placingBarcos.isCualPosicion())) {
            placingBarcos.definirPosicionColor(Barcos.ColorPosicion.Valid);
        } else {
            placingBarcos.definirPosicionColor(Barcos.ColorPosicion.Invalid);
        }
    }

    /**
     * Se activa cuando se suelta el botón del ratón.
     * Si está en el estado ColocandoBarcos y el cursor está dentro de la cuadrícula del jugador, intentará colocar el barco.
     * De lo contrario, si está en el estado FiringShots y el cursor está en la cuadrícula de la computadora,
     * intentará disparar a la computadora.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Posicion mousePosicion = new Posicion(e.getX(), e.getY());
        if(gameState == GameState.PlacingShips && player.isPositionInside(mousePosicion)) {
            tryPlaceShip(mousePosicion);
        } else if(gameState == GameState.FiringShots && computer.isPositionInside(mousePosicion)) {
            tryFireAtComputer(mousePosicion);
        }
        repaint();
    }

    /**
     * Se activa cuando el mouse se mueve dentro del panel. No hace nada si no está en el estado ColocandoBarcos.
     * Intentará mover el barco que se está colocando en las coordenadas del mouse.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameState != GameState.PlacingShips) return;
        tryMovePlacingShip(new Posicion(e.getX(), e.getY()));
        repaint();
    }


    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}
