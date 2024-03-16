import java.awt.Point;
import java.io.IOException;

public class Auto extends PowerUp {
    AvionAmigo avion;

    public Auto(Point posicion) throws IOException {
        super("Auto", "auto.png", posicion);
    }

    public Auto(Auto bonus) throws IOException {
        super(bonus);
    }

    public void asignarBonus(AvionAmigo avion) {
        this.avion = avion;
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
            avion.arma.setModoDisparo(false);
        }
    }

    @Override
    public Auto clone() {
        Auto temp = null;

        try {
            temp = new Auto(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}
