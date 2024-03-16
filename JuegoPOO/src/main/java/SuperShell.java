import java.awt.Point;
import java.io.IOException;

public class SuperShell extends PowerUp {
    AvionAmigo avion;

    public SuperShell(Point posicion) throws IOException {
        super("Super Shell", "superShell.png", posicion);   
    }

    public SuperShell(SuperShell bonus) throws IOException {
        super(bonus);
    }

    public void asignarBonus(AvionAmigo avion) {
        this.avion = avion;
        avion.arma.setModoDisparo(true);
        avion.arma.setFrecuenciaDisparos(3);
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
            avion.arma.setFrecuenciaDisparos(6);
        }
    }

    @Override
    public SuperShell clone() {
        SuperShell temp = null;

        try {
            temp = new SuperShell(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}
