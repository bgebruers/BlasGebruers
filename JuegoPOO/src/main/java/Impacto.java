import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Impacto {
    public static enum tipoImpacto {
        DISPARO(new int[]{1, 2, 3, 4}), COLISION(new int[]{5, 6, 7, 8});
        
        private int[] secuencia;
        private tipoImpacto(int[] sec) {
            secuencia = sec;
        }
    }

    private Point objetivo;
    private final tipoImpacto tipo;

    private final BufferedImage[] impactos;

    private int puntero, contador;

    public Impacto(Point objetivo, tipoImpacto tipo) throws IOException {
        this.objetivo = objetivo;
        this.tipo = tipo;
        this.puntero = 0;
        this.contador = 0;

        this.impactos = new BufferedImage[8];

        impactos[0] = ImageIO.read(Impacto.class.getResource("imagenes/disparo1.png"));
        impactos[1] = ImageIO.read(Impacto.class.getResource("imagenes/disparo2.png"));
        impactos[2] = ImageIO.read(Impacto.class.getResource("imagenes/disparo3.png"));
        impactos[3] = ImageIO.read(Impacto.class.getResource("imagenes/disparo4.png"));

        impactos[4] = ImageIO.read(Impacto.class.getResource("imagenes/colision1.png"));
        impactos[5] = ImageIO.read(Impacto.class.getResource("imagenes/colision2.png"));
        impactos[6] = ImageIO.read(Impacto.class.getResource("imagenes/colision3.png"));
        impactos[7] = ImageIO.read(Impacto.class.getResource("imagenes/colision4.png"));
    }

    public void setObjetivo(Point objetivo) {
        this.objetivo = objetivo;
    }

    public boolean isEnabled() {
        return puntero < tipo.secuencia.length;
    }

    public void draw(Graphics2D g) {
        if(puntero < tipo.secuencia.length) {
            BufferedImage temp = impactos[tipo.secuencia[puntero]-1];
            g.drawImage(temp, objetivo.x - temp.getWidth()/2, objetivo.y - temp.getHeight()/2, null);
        }
        
        if(contador == 5) {
            puntero++;
            contador = 0;
        }

        contador++;
    }
}