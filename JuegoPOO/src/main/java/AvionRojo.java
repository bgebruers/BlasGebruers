import java.awt.Point;
import java.io.IOException;

public class AvionRojo extends AvionEnemigo {
    public AvionRojo(Point posicion) throws IOException{
       super("avionEspecial.png", posicion, new Point(0, 0));
       this.resistencia = this.energia/2;
       this.objetivo = null;
       this.velocidad = 2;
    }

    public Municion[][] disparar(){
        return null;
    }

    @Override
    public void update() {
        this.avanzar();
    }
}