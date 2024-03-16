import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class ObjetoGrafico {
    protected String nombre;
    protected BufferedImage grafico;
    protected Point posicion;

    public ObjetoGrafico(String nombre, String grafico, Point posicion) throws IOException {
        this.nombre = nombre;
        
        if(grafico != null)
            this.grafico = ImageIO.read(this.getClass().getResource("imagenes/" + grafico));

        if(posicion != null)
            this.posicion = (Point) posicion.clone();
    }

    protected void setGrafico(BufferedImage grafico) {
        this.grafico = grafico;
    }

    public Point getPosicion() {
        return this.posicion;
    }

    public void setX(int x) {
        this.posicion.x = x;
    }

    public void setY(int y) {
        this.posicion.y = y;
    }

    public int getX() {
        return this.posicion.x;
    }

    public int getY() {
        return this.posicion.y;
    }

    public Dimension getDimensions() {
        if(grafico != null) {
            return new Dimension(grafico.getWidth(), grafico.getHeight());
        }

        return null;
    }

    public String getNombre(){
        return nombre;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g);
}
