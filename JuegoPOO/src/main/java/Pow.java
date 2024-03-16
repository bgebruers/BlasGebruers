import java.awt.Point;
import java.io.IOException;


public class Pow extends PowerUp {
    public Pow(Point posicion) throws IOException {
        super("Pow", "pow.png", posicion);
    }

    public Pow(Pow bonus) throws IOException {
        super(bonus);
    }

    @Override
    public void asignarBonus(AvionAmigo avion) {
        avion.modificarEnergia(30);
    }

    public void activar() {
        if(tiempo == -1) { // el bonus nunca se uso
            this.tiempo = 5;
        }
    }

    public void destruir() {}

    public String toString() {
        return "30 puntos de energia restablecida!";
    }

    @Override
    public Pow clone() {
        Pow temp = null;

        try {
            temp = new Pow(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}
