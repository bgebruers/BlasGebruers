import java.awt.Point;
import java.io.IOException;

public class AmetralladoraTresCañones extends ArmadeFuego {
    AvionAmigo avion;

    public AmetralladoraTresCañones(Point posicion) throws IOException {
        super("Ametralladora 3 caños", "ametralladora.png", posicion);
    }

    public AmetralladoraTresCañones(AmetralladoraTresCañones bonus) throws IOException {
        super(bonus);
    }

    @Override
    public void asignarBonus(AvionAmigo avion) {
        this.avion = avion;
        avion.arma.setTipoMunicion(Arma.MUNICION.TRESCANOS);
        avion.arma.setTiros(3, new double[]{-12, 0, 12});
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
    public AmetralladoraTresCañones clone() {
        AmetralladoraTresCañones temp = null;

        try {
            temp = new AmetralladoraTresCañones(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}
