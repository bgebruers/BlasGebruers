import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

public class Municion extends ObjetoGrafico {
    private int velocidad, dano;
    private double angulo;
    private boolean destructora;

    public Municion(String filename, Point posicion, double angulo) throws IOException {
        super("Municion", filename, posicion);
    
        this.dano = 1;
        this.angulo = angulo;
        this.velocidad = 6;
        this.destructora = false;
    }

    public void setDestructora(boolean destructora) {
        this.destructora = destructora;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public int getDano() {
        return this.dano;
    }

    public boolean esDestructora() {
        return this.destructora;
    }

    private void avanzar() {
        if(angulo%Math.PI != 0) {    
            if(Math.sin(angulo) < 0)
                this.posicion.x += 1 + Math.abs(Math.sin(angulo)) * velocidad;
            else 
                this.posicion.x -= 1 + Math.abs(Math.sin(angulo)) * velocidad;
        }

        this.posicion.y += 1 + Math.cos(angulo) * velocidad;
    }

    public void setVelocidad(int velocidad) {
        if(velocidad > 1)
            this.velocidad = velocidad;
    }

    @Override
    public void update() {
        avanzar();
    }

    @Override
    public void draw(Graphics2D g) {
        if(this.posicion != null) {
            Graphics2D g1 = (Graphics2D)g.create();

            int x = posicion.x;
            int y = posicion.y;
            
            g1.rotate(angulo, x, y);

            y += (int)(this.grafico.getHeight()/2);
            x -= this.grafico.getWidth()/2;

            g1.drawImage(grafico, x, y, null);
        }
    }
}
