import java.awt.Point;
import java.io.IOException;

public abstract class ArmadeFuego extends Bonus {

    public ArmadeFuego(Bonus bonus) throws IOException {
        super(bonus.nombre, "", (Point)bonus.getPosicion().clone());
        setGrafico(bonus.grafico);
    }

    
    public ArmadeFuego(String nombre, String grafico, Point posicion) throws IOException {
        super(nombre, grafico, posicion);
    }
   
    @Override
    public void update() {
        super.update();
    }
}
