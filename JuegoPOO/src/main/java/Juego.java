import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.entropyinteractive.*;

abstract public class Juego extends JGame {
    protected String nombre;
    protected String descripcion;
    protected int ancho, alto;

    public Juego(String nombre, String descripcion) {
        super(nombre, 800, 600);
        ancho = 800;
        alto = 600;
        
    
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    protected void setIcon(File icon) {
        try {
            this.getFrame().setIconImage(ImageIO.read(icon));
        } catch (IOException e) {
            System.out.println("Error estableciendo icono del juego");
        }
    }
   
}
