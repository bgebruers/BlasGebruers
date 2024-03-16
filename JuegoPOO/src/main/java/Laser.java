import java.awt.Point;
import java.io.IOException;

public class Laser extends ArmadeFuego {
    AvionAmigo avion;

    public Laser(Point posicion) throws IOException {
        super("Laser", "laser.png", posicion);
    }

    public Laser(Laser bonus) throws IOException {
        super(bonus);        
    }

    @Override
    public void asignarBonus(AvionAmigo avion) {
        this.avion = avion;
        avion.arma.setTipoMunicion(Arma.MUNICION.LASER);
        avion.arma.setTiros(1, new double[]{0});
    }
    
    @Override
    public void update() {
        super.update();
        if(!this.activo()) {
            destruir();
        }
    }
    
    public void destruir() {
        if(avion != null) {
            avion.arma.setTipoMunicion(Arma.MUNICION.COMUN);
            avion.arma.setTiros(2, new double[]{0, 0});
        }
    }

    @Override
    public Laser clone() {
        Laser temp = null;

        try {
            temp = new Laser(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}
