import java.awt.Point;
import java.io.IOException;

public class EstrellaNinja extends PowerUp {
    public EstrellaNinja(Point posicion) throws IOException {
        super("Estrella Ninja", "estrellaNinja.png", posicion);
    }

 
    public EstrellaNinja(EstrellaNinja bonus) throws IOException {
        super(bonus);
    }

    @Override
    public void asignarBonus(AvionAmigo avion) {
        avion.llenarEnergia();
    }

    public void activar() {
        if(tiempo == -1) { // el bonus nunca se uso
            this.tiempo = 5;
        }
    }

    public String toString() {
        return "Energia restablecida al maximo!";
    }

    public void destruir() {}

    @Override
    public EstrellaNinja clone() {
        EstrellaNinja temp = null;

        try {
            temp = new EstrellaNinja(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}
