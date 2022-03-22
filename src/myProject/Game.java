package myProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Battleship - Batalla Naval
 * @autor Laura Murillas andrade - 1944153 - laura.murillas@correounivalle.edu.co
 * @autor Manuel Cuellar - 2041041 - manuel.cuellar@correounivalle.edu.co
 * Class Game - Version 1.0.0  Date 08/03/2022
 */

/**
 * Game class: Clase Escucha
 * Define el punto de entrada para el juego al crear el marco y le añade un GamePanel.
 * Aqui adentro se encuentran la clase main
 */
public class Game extends JFrame {

    //Atributos
    private JPanel Inicio;
    private JButton comenzar,instrucciones;
    private JTextArea Texto;
    private JLabel Imagen;
    private ImageIcon ImagenBarcos;
    private Escucha escucha;



    //GUI class
    public Game(){
        initGUI();

        //Default JFrame configuration
        this.setTitle("BATALLA NAVAL");
        //this.setSize(1200,700);
        this.pack();
        this.setResizable(true);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * This method is used to set up the default JComponent Configuration,
     * create Listener and control Objects used for the GUI class
     */
    private void initGUI() {
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        escucha = new Escucha();

        Inicio= new JPanel();
        Inicio.setPreferredSize(new Dimension(400,430));
        Inicio.setBorder(BorderFactory.createTitledBorder("Battleship"));
        add(Inicio,constraints);

        comenzar = new JButton("Nuevo Juego");
        comenzar.addActionListener(escucha);
        constraints.gridx=0;
        constraints.gridy=2;
        constraints.gridwidth=1;
        constraints.fill=GridBagConstraints.NONE;
        constraints.anchor=GridBagConstraints.CENTER;
        add(comenzar,constraints);


        ImagenBarcos = new ImageIcon(getClass().getResource("/resources/battle.png"));
        Imagen = new JLabel(ImagenBarcos);
        Inicio.add(Imagen);


        instrucciones = new JButton("Reglas del Juego");
        instrucciones.addActionListener(escucha);
        constraints.gridx=0;
        constraints.gridy=2;
        constraints.gridwidth=1;
        constraints.fill=GridBagConstraints.NONE;
        constraints.anchor=GridBagConstraints.LINE_START;
        add(instrucciones,constraints);
    }



    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Game game = new Game();
        });
    }



    private GamePanel gamePanel;


    private class Escucha implements ActionListener,KeyListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==instrucciones){
                JOptionPane.showMessageDialog(null,"En este juego de estrategia, tu misión es hundir todas las naves de tu adversario\n -> El tablero izquierdo es donde debes colocar tus barcos,"
                        + "\n-> Recuerda presionar G para girar tus barcos al momento de ubicarlos en tu tablero"
                        + "\n-> El tablero de la derecha es donde debes apuntar tus lanzamientos"
                        +"\n ¡intentar derribar las naves enemigas, antes de que derriben las tuyas!");


            }
            else if(e.getSource()==comenzar) {

                String[] options = new String[]{"Comenzar"};


                String message = "Bienvenido a Battleships" +
                        "\n Haz clic en comenzar para iniciar el juego";

                int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                        "Battleship",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);

                instrucciones.setVisible(false);
                comenzar.setVisible(false);


                
                JFrame FrameGame = new JFrame("Battleship");


                FrameGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                FrameGame.setResizable(false);


                gamePanel = new GamePanel(difficultyChoice);


                FrameGame.getContentPane().add(gamePanel);
                FrameGame.addKeyListener(escucha);
                FrameGame.pack();
                FrameGame.setVisible(true);


            }

        }

        //No se utiliza
        @Override
        public void keyTyped(KeyEvent e) {
        }

        // Lee la tecla que presionó el jugador y la envia a GamePanel
        @Override
        public void keyPressed(KeyEvent e) {
            gamePanel.handleInput(e.getKeyCode());
        }

        //No se utiliza
        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
