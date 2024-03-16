import java.awt.Point;
import java.io.IOException;

public class Escopeta extends ArmadeFuego {
    AvionAmigo avion;

    public Escopeta(Point posicion) throws IOException {
        super("Escopeta", "escopeta.png", posicion);         
    }
     
    public Escopeta(Escopeta bonus) throws IOException {
        super(bonus);
    }

    @Override
    public void asignarBonus(AvionAmigo avion) {
        this.avion = avion;
        avion.arma.setTipoMunicion(Arma.MUNICION.ESCOPETA);
        avion.arma.setFrecuenciaDisparos(20);
        avion.arma.setModoDisparo(true);
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
            avion.arma.setFrecuenciaDisparos(6);
            avion.arma.setModoDisparo(false);
        }
    }

    @Override
    public Escopeta clone() {
        Escopeta temp = null;

        try {
            temp = new Escopeta(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}
