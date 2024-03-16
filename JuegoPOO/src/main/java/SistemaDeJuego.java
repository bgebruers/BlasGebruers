import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

class SistemaDeJuego extends JFrame {
    JLabel img1943;
    JLabel fondo = new JLabel();
   
    public static void main(String[] args) {  
        new SistemaDeJuego();
    } 

    public SistemaDeJuego() {
        this.setPreferredSize(new Dimension(450, 550));
        this.setResizable(false);
        this.pack();
        fondo.setLayout(new GridBagLayout());
        fondo.setPreferredSize(new Dimension(450, 550));
        fondo.setIcon(new ImageIcon(getClass().getResource("/imagenes/fondo.png")));
        this.add(fondo);
        this.setComponentZOrder(fondo, 0);
        mostrarCatalogo();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.repaint();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                    System.exit(0);
            };
        });
    }

    public void mostrarCatalogo() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = c.gridy = 0;
        
        c.insets = new Insets(10, 10, 10, 10);

        img1943 = new JLabel();
        img1943.setLayout(null);
    
        img1943.setIcon(new ImageIcon(getClass().getResource("/imagenes/1943.png")));
     
        img1943.setOpaque(true);

        img1943.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //se aprieta varias veces, fijarme como llamar al frame anterior del juego, con un metodo puede ser
        img1943.addMouseListener(new MouseAdapter() {
            FrameJuego fj = new FrameJuego();
            public void mousePressed(MouseEvent e) {
                if(!fj.abierto()){
                    fj.abrir();
                }
                
            }
            
        }); 
        
        
        fondo.add(img1943, c);
        String imgs[] = {"/imagenes/counter.png", "/imagenes/pes.png", "/imagenes/mario.png"};
        for(int j = 0; j <= 1; j++) {
            for(int i = j == 0 ? 2 : 1; i <= 2; i++) {
                c.gridx = (i-1);
                c.gridy = j;
                
                
                JLabel juegoGenerico = new JLabel();
                juegoGenerico.setLayout(null);

                juegoGenerico.setIcon(new ImageIcon(getClass().getResource(imgs[i+j*2-2])));

                juegoGenerico.setOpaque(true);

                juegoGenerico.setCursor(new Cursor(Cursor.HAND_CURSOR));

                fondo.add(juegoGenerico, c);
            }
        }
    }
}

class JPanelBackgroundImage extends JPanel{
	private String nombreImagen;
	Image bgImage;
	public JPanelBackgroundImage(String nombreImagen){
        this.nombreImagen=nombreImagen;
        ImageIcon tmp = new ImageIcon(getClass().getResource(this.nombreImagen));

		bgImage=tmp.getImage();
	}

	public void paintComponent (Graphics g){
  			super.paintComponent(g);
  			Graphics2D g2d = (Graphics2D) g;
  			g2d.drawImage(bgImage, 0, 0, null);
  		}
}



class FrameJuego extends JFrame implements ActionListener, Suscriber {
    JMenuBar menu;
    JMenu menu1, menu2;
    JMenuItem item1, item2, item3;
    Configuraciones conf;
    JButton jugar, ranking;
    JComboBox<String> personaje;
    String sonidoActivo;


    public FrameJuego(){
        try {
            conf = new Configuraciones();
        } catch (SQLException e) {
            System.out.println("Error al crear el objeto de Configuraciones\n" + e.getMessage());
        }       
        
        this.setLayout(new BorderLayout());          
     

        this.getContentPane().add(this.setScreen1(),BorderLayout.CENTER);
        this.validate();
        this.repaint();

        this.getContentPane().setBackground(Color.decode("#141414")); 
		this.setTitle("1943");
        this.setSize(300, 350);
       // this.setVisible(true);
        this.setLocationRelativeTo(null); //Centra la Ventana 
        
        
        
        //menu visto en pantalla
        menu = new JMenuBar();


        menu1 = new JMenu("Configuraciones");
       
        
        menu.add(menu1); 
        
        //item dentro de configuraciones
        item1 = new JMenuItem("Teclado");
        item1.addActionListener(this);
        menu1.add(item1);

        item2 = new JMenuItem("Sonido");
        item2.addActionListener(this);
        menu1.add(item2);


        this.setJMenuBar(menu);             
        this.setResizable(false);
   
        if(Configuraciones.sonidoBD.equals("CHASE")){
            FXPlayer.CHASE.play();
            sonidoActivo = "CHASE";
        }
        if(Configuraciones.sonidoBD.equals("ROBO_COP")){
            FXPlayer.ROBO_COP.play();
            sonidoActivo = "ROBO_COP";

        } 
        if(Configuraciones.sonidoBD.equals("DRAMATIC")){
            FXPlayer.DRAMATIC.play();
            sonidoActivo = "DRAMATIC";

        } 
        if(Configuraciones.sonidoBD.equals("FIGHT")){
            FXPlayer.FIGHT.play();
            sonidoActivo = "FIGHT";

        } 

       
    }

    public void abrir(){
        this.setVisible(true);
    }

    public boolean abierto(){
        return this.isVisible();
    }
    
    @Override
    public void update() {
        Jugador1943 jugador = ((Juego1943)Juego1943.getInstance()).getJugador();

        String nombre = jugador.getNombre();     
        int puntos = jugador.getPuntaje(); 
        conf.Ranking(nombre, puntos);

    }

    private JPanel setScreen1(){
        JPanelBackgroundImage  fondoPanel=new JPanelBackgroundImage("imagenes/pantallaJuego.jpg");
        JPanelBackgroundSemiOpaco botonesPanel=new JPanelBackgroundSemiOpaco();

        botonesPanel.setLayout(new GridLayout(3,1,10,10));
        botonesPanel.setBorder(new EmptyBorder(160, 60, 60, 60));

        jugar = new JButton("Jugar");
        ranking = new JButton("Ver Ranking");
        personaje = new JComboBox<>(new String[]{"P-38", "P-39(?"});
        personaje.setToolTipText("Elije tu personaje");

        botonesPanel.add(jugar);
        botonesPanel.add(ranking);
        botonesPanel.add(personaje);

        jugar.addActionListener(this);
        ranking.addActionListener(this);

        fondoPanel.add(botonesPanel);

        return fondoPanel;
    }

    
    public void actionPerformed(ActionEvent evento) {

        if (evento.getActionCommand() == jugar.getActionCommand()){
            Juego juego = Juego1943.getInstance();

            ((Juego1943)juego).addSuscriber(this);
            ((Juego1943)juego).setSpriteAvionAmigo(personaje.getSelectedIndex());

            Thread t = new Thread() {
                public void run(){
                    juego.run(1.0 / 60.0);
                }
            };

            t.start();
        }


        if(evento.getSource()== ranking){
            ArrayList<String[]> arr = new ArrayList<>();
           arr = conf.verRanking();

           JFrame frameRank = new JFrame();
           frameRank.setLayout(new GridLayout(0, 2));
           frameRank.setTitle("Ranking");
           frameRank.setPreferredSize(new Dimension(100, (arr.size()+1)*40));
           
           JLabel nombre = new JLabel("Nombre");
           JLabel score = new JLabel("Score");


           frameRank.add(nombre);
           frameRank.add(score);
           
           

            for (int i = 0; i < arr.size(); i++) {
                frameRank.add(new JLabel(arr.get(i)[0]));
                frameRank.add(new JLabel(arr.get(i)[1]));
            }

            frameRank.setLocationRelativeTo(null);
            frameRank.setVisible(true);
            frameRank.pack();

        }

        if (evento.getSource()==item1) {
            JFrame framConfTecla;
            JLabel lb1, lb2, lb3, lb4, lb5, lb6, lb7;
            Choice ch1, ch2, ch3, ch4, ch5, ch6, ch7;
            JButton defecto = new JButton("Defecto");

            String opciones[] = {"a","b", "c", "d" ,"e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        
            framConfTecla = new JFrame("Configuracion de teclas");

            framConfTecla.setLayout(new GridLayout(8,8));
            framConfTecla.setPreferredSize(new Dimension(300,400));

            lb1 = new JLabel("Hacia adelante: ");

            ch1 = new Choice();
            ch1.add("↑"); //muestra un cuadrado, cambiarlo
            for (int i = 0; i < opciones.length; i++) {
                ch1.add(opciones[i]);
            }

            framConfTecla.add(lb1);
            framConfTecla.add(ch1);
            
            ch1.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.selecTeclas(ch1.getSelectedItem(), 1, KeyEvent.getExtendedKeyCodeForChar(ch1.getSelectedItem().charAt(0)));                   
                }
            });



            lb2 = new JLabel("Hacia abajo: ");
            ch2 = new Choice();
            ch2.add("↓");                                   //muestra un cuadrado, cambiarlo
            for (int i = 0; i < opciones.length; i++) {
                ch2.add(opciones[i]);
            }

            //Captura el elemento del choice
            ch2.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.selecTeclas(ch2.getSelectedItem(), 2, KeyEvent.getExtendedKeyCodeForChar(ch2.getSelectedItem().charAt(0)));
                }
            });            

            framConfTecla.add(lb2);
            framConfTecla.add(ch2);
            
            lb3 = new JLabel("Derecha: ");
            ch3 = new Choice();
            ch3.add("→");                                   //muestra un cuadrado, cambiarlo
            for (int i = 0; i < opciones.length; i++) {
                ch3.add(opciones[i]);
            }
            framConfTecla.add(lb3);
            framConfTecla.add(ch3);

            ch3.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.selecTeclas(ch3.getSelectedItem(), 3, KeyEvent.getExtendedKeyCodeForChar(ch3.getSelectedItem().charAt(0)));
                }
            });


            lb4 = new JLabel("Izquierda: ");
            ch4 = new Choice();
            ch4.add("←");                                   //muestra un cuadrado, cambiarlo
            for (int i = 0; i < opciones.length; i++) {
                ch4.add(opciones[i]);
            }
            framConfTecla.add(lb4);
            framConfTecla.add(ch4);
           
            ch4.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.selecTeclas(ch4.getSelectedItem(), 4, KeyEvent.getExtendedKeyCodeForChar(ch4.getSelectedItem().charAt(0)));
                }
            });
           
            lb5 = new JLabel("Disparar: ");
            ch5 = new Choice();
            ch5.add("x");                                   
            for (int i = 0; i < opciones.length; i++) {
                ch5.add(opciones[i]);
            }
            framConfTecla.add(lb5);
            framConfTecla.add(ch5);

            ch5.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.selecTeclas(ch5.getSelectedItem(), 5, KeyEvent.getExtendedKeyCodeForChar(ch5.getSelectedItem().charAt(0)));
                }
            });


            lb6 = new JLabel("Ataques especiales: ");
            ch6 = new Choice();
            ch6.add("z");                                   
            for (int i = 0; i < opciones.length; i++) {
                ch6.add(opciones[i]);
            }
            framConfTecla.add(lb6);
            framConfTecla.add(ch6);

            ch6.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.selecTeclas(ch6.getSelectedItem(), 6, KeyEvent.getExtendedKeyCodeForChar(ch6.getSelectedItem().charAt(0)));
                }
            });

            lb7 = new JLabel("Pausa: ");
            ch7 = new Choice();
            ch7.addItem("Barra espacio");                                   
            for (int i = 0; i < opciones.length; i++) {
                ch7.add(opciones[i]);
            }
            framConfTecla.add(lb7);
            framConfTecla.add(ch7);
            
            ch7.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.selecTeclas(ch7.getSelectedItem(), 7, KeyEvent.getExtendedKeyCodeForChar(ch7.getSelectedItem().charAt(0)));
                }
            });

            framConfTecla.add(defecto);
            defecto.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if(evt.getSource() == defecto){
                        conf.setDefecto("Defecto");
                    }
                }
            });

            framConfTecla.setVisible(true);
            framConfTecla.pack();

        }

        if(evento.getSource()== item2){
            FXPlayer.init();
            FXPlayer.volume = FXPlayer.Volume.LOW;
  
            JFrame framConfSonido = new JFrame();
            framConfSonido.setLayout(new FlowLayout());
            framConfSonido.setPreferredSize(new Dimension(200,100));
           
            Choice sonidos = new Choice();
            sonidos.addItem("CHASE");
            sonidos.addItem("ROBO_COP");
            sonidos.addItem("DRAMATIC");
            sonidos.addItem("FIGHT");

            sonidos.select(Configuraciones.sonidoBD);

            //para cambiar la musica seleccionada
            sonidos.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent ie){
                    conf.setNuevoSonido(sonidos.getSelectedItem());
                }
            });

            Checkbox activo = new Checkbox("Sonido");
            activo.setState(true);
            activo.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    activo.setState(false);
                    if(!activo.getState()){
                       // FXPlayer.volume = FXPlayer.Volume.MUTE; 
                        if(sonidoActivo.equals("CHASE")){
                            FXPlayer.CHASE.stop();
                        }
                        if(sonidoActivo.equals("ROBO_COP")){
                            FXPlayer.ROBO_COP.stop();
                        }
                        if(sonidoActivo.equals("DRAMATIC")){
                            FXPlayer.DRAMATIC.stop();
                        }
                        if(sonidoActivo.equals("FIGHT")){
                            FXPlayer.FIGHT.stop();
                        }
                    }
                }
            });

           
            JButton defecto = new JButton("Defecto");    
            
            defecto.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    conf.resetearSonido();
                    sonidos.select(Configuraciones.sonidoBD);
                }
            });

            framConfSonido.add(sonidos);
            framConfSonido.add(defecto);
            framConfSonido.add(activo);

            framConfSonido.setVisible(true);
            framConfSonido.pack();

        }
    }   
}
class JPanelBackgroundSemiOpaco extends JPanel{
	 
	public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);     
	}
}