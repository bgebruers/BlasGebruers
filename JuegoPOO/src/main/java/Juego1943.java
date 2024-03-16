import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.processing.SupportedOptions;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.entropyinteractive.Keyboard;

public class Juego1943 extends Juego implements ActionListener {
    private static final Juego instance = new Juego1943();
    private final double desplazamiento = 180.0;
    
    private ArrayList<Suscriber> suscribers;

    private BufferedImage fotoFin;
    
    private Mapa mapa;
    private Camara cam;
    
    private boolean terminado= false;
    
    Jugador1943 jugador; //esta instancia es para llevar el puntaje a la sist de juego

    private VehiculoMilitar avionAmigo;
    private int spriteAvionAmigoElegido;

    private int misionAsignada;
    private Mision mision;
    private int animacionEnCurso;
    private boolean pausa;

    private Keyboard keyboard;

    private Juego1943(){
        super("1943", "Juego 1943");
        
        suscribers = new ArrayList<Suscriber>();
        spriteAvionAmigoElegido = 0;

        setIcon(new File(getClass().getResource("/imagenes/1943ico.png").getPath()));
        
        try {
            
            pausa = false;
            mapa = new Mapa("fondo1943Aire.jpg", "fondo1943Tierra.jpg");
            mapa.setTransicion("fondo1943Transicion.jpg");

            jugador = new Jugador1943();

            cam = new Camara(120, 0, 0);
            animacionEnCurso = 0;
        } catch (IOException e) {
            System.out.println("No se pudo crear avion amigo");
        }
    }

    public void addSuscriber(Suscriber nuevo) {
        suscribers.add(nuevo);
    }

    public Jugador1943 getJugador() {
        return jugador;
    }

    public void setSpriteAvionAmigo(int i) {
        if(i == 0 || i == 1)
            this.spriteAvionAmigoElegido = i;
    }

    private void asignarMision(int misionAsignada){
        this.misionAsignada = misionAsignada;
        avionAmigo.setX(400);
        avionAmigo.setY(300);

        cam.setX(0);
        cam.setY(0);

        try {
            Enemigo e1 = new AvionEnemigo("avionEnemigo1.png", new Point(0, 0), avionAmigo.getPosicion());
            Enemigo e2 = new AvionEnemigo("avionEnemigo2.png", new Point(0, 0), avionAmigo.getPosicion());
            
            Enemigo e3 = new AvionEnemigo("avionEnemigo3.png", new Point(0, 0), avionAmigo.getPosicion());
            e3.arma.seguir(true);
            e3.arma.setAnguloMaximo(75);
            
            Enemigo e4 = new AvionEnemigo("avionEnemigo4.png", new Point(0, 0), avionAmigo.getPosicion());
            e4.setResistencia(100/3);
            e4.arma.setFrecuenciaDisparos(15);
            e4.arma.setTiros(2, new double[]{0, 0});
            
            Enemigo e5 = new AvionEnemigo("avionEnemigo5.png", new Point(0, 0), avionAmigo.getPosicion());
            e4.setResistencia(100/8);
            e5.arma.setFrecuenciaDisparos(20);
            e5.arma.setTipoMunicion(Arma.MUNICION.ESCOPETA);

            Enemigo e6 = new Barco("barco1.png", new Point(0, 0), avionAmigo.getPosicion());
            Enemigo e7 = new Barco("barco2.png", new Point(0, 0), avionAmigo.getPosicion());

            Enemigo jefe1 = new Barco("jefe1.png", new Point(0, 0), avionAmigo.getPosicion());
            
            jefe1.setResistencia(100/45);
            
            for(Arma a : jefe1.getArmas()) {
                a.setFrecuenciaDisparos(12);
                a.setModoDisparo(true);
                a.setAnguloMaximo(170);
            }
            
            Enemigo jefe2 = new Barco("jefe2.png", new Point(0, 0), avionAmigo.getPosicion());
            
            jefe2.setResistencia(100/70);
            
            for(Arma a : jefe2.getArmas()) {
                a.setTiros(2, new double[]{0, 0});
                a.setAnguloMaximo(170);
            }

            Enemigo enemigos[];
            switch(misionAsignada) {
                case 1:
                    enemigos = new Enemigo[]{e1, e2, e4, e6};

                    mision = new Mision.MisionBuilder((AvionAmigo)avionAmigo, enemigos, jefe1)
                    .setNombre("Mision 1")
                    .setTiempo(60*2)
                    .setDificultad(Mision.DIFICULTAD.FACIL)
                    .generarBonusSecreto(true)
                    .build();
                    break;
                case 2:
                    enemigos = new Enemigo[]{e1, e3, e5, e6, e7};

                    mision = new Mision.MisionBuilder((AvionAmigo)avionAmigo, enemigos, jefe2)
                    .setNombre("Mision 2")
                    .setTiempo(60*4)
                    .setDificultad(Mision.DIFICULTAD.DIFICIL)
                    .build();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameStartup() {
        keyboard = this.getKeyboard();
        
        try {
            avionAmigo = new AvionAmigo(AvionAmigo.SPRITES.getSprite(spriteAvionAmigoElegido), new Point(400, 300));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cam.setRegionVisible(800, 600);

        try {
            avionAmigo = new AvionAmigo(AvionAmigo.SPRITES.getSprite(spriteAvionAmigoElegido), new Point(400, 300));
        } catch (IOException e) {
            e.printStackTrace();
        }

        asignarMision(1);
    }

    @Override
    public void gameShutdown() {
        
    }
    
    @Override 
    public void gameUpdate(double delta) {
        if(keyboard.isKeyPressed(Configuraciones.pausa)) {
            pausa = !pausa;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(pausa) {
            return;
        }

        try {

            if(mision.getEstado() == Mision.ESTADO.GANA && misionAsignada == 1){
                fotoFin =  ImageIO.read(getClass().getResource("imagenes/win.png"));
                if(keyboard.isKeyPressed(80)){      //80 es el codigo de VK_P
                    gameStartup();
                    asignarMision(2);
                }
            }

            if(mision.getEstado() == Mision.ESTADO.FIN && misionAsignada == 1){
                fotoFin = ImageIO.read(getClass().getResource("imagenes/gameover.jpg"));
                if(keyboard.isKeyPressed(80)){
                    gameStartup();
                    asignarMision(1);
                }
            }

            if(mision.getEstado() == Mision.ESTADO.GANA && misionAsignada == 2){
                fotoFin = ImageIO.read(getClass().getResource("imagenes/win2.png"));
                if(terminado == false){                
                    terminarJuego();
                    terminado = true;
                }
                return;
            }
            
            if(mision.getEstado() == Mision.ESTADO.FIN && misionAsignada == 2){
                fotoFin = ImageIO.read(getClass().getResource("imagenes/gameover.jpg"));
                if(keyboard.isKeyPressed(80)){
                    gameStartup();
                    asignarMision(2);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }  

        if (keyboard.isKeyPressed(Configuraciones.arriba)) {
            if(this.getViewPort().contains(avionAmigo.getX(), avionAmigo.getY()  - avionAmigo.grafico.getHeight(), 
                avionAmigo.grafico.getWidth(), avionAmigo.grafico.getHeight())) {
                avionAmigo.setY(avionAmigo.getY() - (int)(desplazamiento * delta));
            } else {
                avionAmigo.setY(avionAmigo.getY() - (int)(120 * delta * 0.5));
            }
        }
        
        if (keyboard.isKeyPressed(Configuraciones.abajo)) {
            if(this.getViewPort().contains(avionAmigo.getX(), avionAmigo.getY() + (int)(desplazamiento * delta), 
                avionAmigo.grafico.getWidth(), avionAmigo.grafico.getHeight())) {
                avionAmigo.setY(avionAmigo.getY() + (int)(desplazamiento * delta));
            } else {
                avionAmigo.setY(avionAmigo.getY() - (int)(cam.getDesplazamiento() * delta * 0.5));
            }
        }

        if(!keyboard.isKeyPressed(Configuraciones.arriba) && !keyboard.isKeyPressed(Configuraciones.abajo)) {
            avionAmigo.setY(avionAmigo.getY() - (int)(cam.getDesplazamiento() * delta * 0.5));
        }

        if (keyboard.isKeyPressed(Configuraciones.izq)) {
            avionAmigo.setX(avionAmigo.getX() - (int)(desplazamiento * delta));

            if(!(animacionEnCurso > 0 && animacionEnCurso < 180))
                ((AvionAmigo)avionAmigo).setIcon(AvionAmigo.Iconos.IZQ);
        }

        if (keyboard.isKeyPressed(Configuraciones.der)) {
            avionAmigo.setX(avionAmigo.getX() + (int)(desplazamiento * delta));
            
            if(!(animacionEnCurso > 0 && animacionEnCurso < 180))
                ((AvionAmigo)avionAmigo).setIcon(AvionAmigo.Iconos.DER);
        }
        
        if(!keyboard.isKeyPressed(Configuraciones.izq) && !keyboard.isKeyPressed(Configuraciones.der)) {
            if(!(animacionEnCurso > 0 && animacionEnCurso < 180))
                ((AvionAmigo)avionAmigo).setIcon(AvionAmigo.Iconos.COMUN);
        }

        if (keyboard.isKeyPressed(Configuraciones.disparo) && !((AvionAmigo)avionAmigo).isEsquivando()) {
            Municion[][] balas = avionAmigo.disparar();

            for(Municion[] bala : balas) {
                if(bala != null) {
                    for(Municion b : bala) {
                        if(b != null) {
                            mision.disparoHeroe(b);
                        }
                    }
                }
            }
        }

        if(keyboard.isKeyPressed(Configuraciones.disparo) && keyboard.isKeyPressed(Configuraciones.dispEspecial)) {
            ((AvionAmigo)avionAmigo).esquivar();
        } else if(keyboard.isKeyPressed(Configuraciones.dispEspecial) && !((AvionAmigo)avionAmigo).isEsquivando()) {
            mision.ataqueEspecial();
        }
        
        if(mision.getEstado() == Mision.ESTADO.TIERRA && animacionEnCurso < 180) {
            animacion();
        }

        cam.avanzar((AvionAmigo)avionAmigo, delta);
        mapa.update();
        mision.update();
    }

    private void animacion() {
        if(animacionEnCurso == 0 || animacionEnCurso == 120)
            ((AvionAmigo)avionAmigo).setIcon(AvionAmigo.Iconos.BAJANDO1);
        else if(animacionEnCurso == 60)
            ((AvionAmigo)avionAmigo).setIcon(AvionAmigo.Iconos.BAJANDO2);
        else if(animacionEnCurso == 179)
            ((AvionAmigo)avionAmigo).setIcon(AvionAmigo.Iconos.COMUN);

        if(animacionEnCurso == 60 || animacionEnCurso == 175)
            mapa.cambiarMapa();

        animacionEnCurso++;
    }
    
    public void terminarJuego(){
        String text = "Ingrese su nombre";

        this.animacionEnCurso = 0;

        JFrame frameTerminado = new JFrame();
        frameTerminado.setPreferredSize(new Dimension(200, 100));
        frameTerminado.setLayout(new FlowLayout());

        JLabel score = new JLabel("Score: "+ String.valueOf(jugador.getPuntaje()));
        JTextField ingresarNom = new JTextField(text);

        ingresarNom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                    
                if(ingresarNom.getText() != null){
                    jugador.setNombre(ingresarNom.getText());

                } else{
                    jugador.setNombre(null); 
                }
                
                suscribers.forEach(suscriber -> suscriber.update());
            }
        });

        ingresarNom.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(ingresarNom.getText().equals(text)){
                    ingresarNom.setText("");
                }
            }
        });


        frameTerminado.setLocationRelativeTo(null);
        frameTerminado.add(ingresarNom);
        frameTerminado.add(score);
        frameTerminado.setVisible(true);
        frameTerminado.setResizable(false);
        frameTerminado.pack();

        frameTerminado.requestFocus();
    }
    
   
    @Override
    public void gameDraw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(mision.getEstado() == Mision.ESTADO.FIN || mision.getEstado() == Mision.ESTADO.GANA 
                || avionAmigo.getEnergia() <=0){
                    g.drawImage(fotoFin, 0, 0, null);    
                return;
              } 
       
        
        g.translate(cam.getX(),cam.getY());
    
        mapa.setPosition((int)cam.getX(), (int)cam.getY());
        mapa.draw(g);
        
        mision.draw(g, (int)(mapa.getY()-(cam.getY()*2)+60));

        g.translate(-cam.getX(),-cam.getY());
    }
    public Rectangle getViewPort() {
        return new Rectangle(new Point((int)cam.getX(), -(int)cam.getY()),
            new Dimension(this.getWidth(), this.getHeight()));
    }

    public static Juego getInstance() {
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) { }  
    
}

class Camara {
    private double desplazamiento;
    private double x,y;

	private double resX, resY;

    public Camara(double desplazamiento, double x,double y) {
    	this.desplazamiento=desplazamiento;
    	this.x=x;
    	this.y=y;
    }

	public void avanzar(AvionAmigo avion, double delta){
		this.y += desplazamiento * delta * 0.5;
    }

	public void setViewPort(double x,double y){
		setRegionVisible(x,y);
	}

	public void setRegionVisible(double x,double y){
		resX=x;
		resY=y;
	}

    public void setX(double x){
    	this.x=x;
    }

     public void setY(double y){
    	this.y=y;
    }

    public double getDesplazamiento(){
    	return this.desplazamiento;
    }

    public double getX(){
    	return this.x;
    }

     public double getY(){
    	return this.y;
    }

    public double getTamanoX(){
    	return this.resX;
    }

     public double getTamanoY(){
    	return this.resY;
    }
}

