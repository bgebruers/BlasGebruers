import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class VehiculoMilitar extends ObjetoGrafico implements Disparable {
    protected Arma arma;
    protected double energia, resistencia;

    public VehiculoMilitar(String nombre, String grafico, Point posicion) throws IOException {
        super(nombre, grafico, posicion);

        this.arma = new Arma(new Point(this.getX(), this.getY()));

        this.energia = 100;
        this.resistencia = this.energia/10; // energia / cuantos disparos resiste
    }

    public void setX(int x) {
        if(x > 0 && x < Juego1943.getInstance().getWidth()-grafico.getWidth())
            this.posicion.x = x;
    }

    protected void setGrafico(BufferedImage grafico) {
        this.grafico = grafico;
    }

    public void setResistencia(double resistencia) {
        this.resistencia = resistencia;
    }

    public double getResistencia() {
        return this.resistencia;
    }

    public void llenarEnergia() {
        energia = 100;
    }

    public Arma[] getArmas() {
        return new Arma[]{arma};
    }

    public void modificarEnergia(double deltaE) {
        if((energia + deltaE) <= 100)
            energia += deltaE;
        else
            energia = 100;
    }

    public double getEnergia() {
        return energia;
    }
    
    @Override
    public void update() {
        if(this.grafico != null)
            arma.update(new Point(posicion.x+this.grafico.getWidth()/2, posicion.y+this.grafico.getHeight()/2));
        else
            arma.update(new Point(this.getX(), this.getY()));
    }

    public void draw(Graphics2D g) {
        g.drawImage(grafico, posicion.x, posicion.y, null);
        arma.draw(g);
    }
}