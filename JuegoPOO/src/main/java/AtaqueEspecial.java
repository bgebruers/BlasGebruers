import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AtaqueEspecial {
    public static enum ATAQUE {
        RAYO(new int[]{1, 2, 3, 4}, 2), TSUNAMI(new int[]{5, 6, 7, 8, 9, 10}, 4);

        private int[] secuencia;
        private int frames;
        private ATAQUE(int[] sec, int frames) {
            secuencia = sec;
            this.frames = frames;
        }
    }

    public final ATAQUE tipo;

    private final BufferedImage[] animaciones;

    private int puntero, contador;

    public AtaqueEspecial(ATAQUE tipo) throws IOException {
        this.tipo = tipo;
        this.puntero = 0;
        this.contador = 0;

        this.animaciones = new BufferedImage[11];

        animaciones[0] = ImageIO.read(Impacto.class.getResource("imagenes/rayo1.png"));
        animaciones[1] = ImageIO.read(Impacto.class.getResource("imagenes/rayo2.png"));
        animaciones[2] = ImageIO.read(Impacto.class.getResource("imagenes/rayo3.png"));
        animaciones[3] = ImageIO.read(Impacto.class.getResource("imagenes/rayo4.png"));

        animaciones[4] = ImageIO.read(Impacto.class.getResource("imagenes/tsunami1.png"));
        animaciones[5] = ImageIO.read(Impacto.class.getResource("imagenes/tsunami2.png"));
        animaciones[6] = ImageIO.read(Impacto.class.getResource("imagenes/tsunami3.png"));
        animaciones[7] = ImageIO.read(Impacto.class.getResource("imagenes/tsunami4.png"));
        animaciones[8] = ImageIO.read(Impacto.class.getResource("imagenes/tsunami4.png"));
        animaciones[9] = ImageIO.read(Impacto.class.getResource("imagenes/tsunami4.png"));
    }

    public boolean isEnabled() {
        return puntero < tipo.secuencia.length;
    }

    public void draw(Graphics2D g) {
        if(puntero < tipo.secuencia.length) {
            BufferedImage temp = animaciones[tipo.secuencia[puntero]-1];
            g.drawImage(temp, 0, 
                (int)(((Juego1943)Juego1943.getInstance()).getViewPort().getY()), 
                null);
        }
        
        if(contador == tipo.frames) {
            puntero++;
            contador = 0;
        }

        contador++;
    }
}
