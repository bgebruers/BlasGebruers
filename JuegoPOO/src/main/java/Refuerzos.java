import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

public class Refuerzos extends Bonus {
    private AvionAmigo avionAmigo, a1, a2;

    public Refuerzos(Point posicion) throws IOException {
        super("Refuerzos", "refuerzo.png", posicion);
    }

    public Refuerzos(Refuerzos bonus) throws IOException {
        super("Refuerzos", "refuerzo.png", new Point(0,0));
    }

    @Override
    public void asignarBonus(AvionAmigo avionAmigo) {
        try {
            this.avionAmigo = avionAmigo;

            a1 = new AvionAmigo(
                avionAmigo.getSpritesEnUso(),
                new Point(
                    (int)avionAmigo.getPosicion().getX() - avionAmigo.grafico.getWidth() - 20, 
                    (int)avionAmigo.getPosicion().getY() + avionAmigo.grafico.getHeight() + 20
                )
            );

            a2 = new AvionAmigo(
                avionAmigo.getSpritesEnUso(),
                new Point(
                    (int)avionAmigo.getPosicion().getX() + avionAmigo.grafico.getWidth() + 20, 
                    (int)avionAmigo.getPosicion().getY() + avionAmigo.grafico.getHeight() + 20
                )
            );

            a1.arma.setModoDisparo(true);
            a2.arma.setModoDisparo(true);

            a1.arma.setFrecuenciaDisparos(10);
            a2.arma.setFrecuenciaDisparos(10);
        } catch (IOException e) {}
    }

    public void destruirRefuerzo(int i) {
        if(i == 1) {
            a1 = null;
        }
        
        if (i == 2) {
            a2 = null;
        }
    }

    public AvionAmigo[] getRefuerzos() {
        if(this.activo()) {
            return new AvionAmigo[]{a1, a2};
        }

        return null;
    }

    @Override
    public void update() {
        super.update();

        if(avionAmigo != null) {
            if(a1 != null) {
                a1.setX((int)avionAmigo.getPosicion().getX() - avionAmigo.grafico.getWidth() - 20);
                a1.setY((int)avionAmigo.getPosicion().getY() + avionAmigo.grafico.getHeight() + 20);
                a1.setResistencia(100/10);
                a1.update();
                a1.setIcon(avionAmigo.getIcon());
            }
            
            if(a2 != null) {
                a2.setX((int)avionAmigo.getPosicion().getX() + avionAmigo.grafico.getWidth() + 20);
                a2.setY((int)avionAmigo.getPosicion().getY() + avionAmigo.grafico.getHeight() + 20);
                a2.setResistencia(100/10);
                a2.update();
                a2.setIcon(avionAmigo.getIcon());
            }
        }
    }

    public void destruir() {
        destruirRefuerzo(0);
        destruirRefuerzo(1);
    }

    @Override
    public void draw(Graphics2D g) {
        if(this.activo()) {
            if(a1 != null) {
                a1.draw(g);
            }
            
            if(a2 != null) {
                a2.draw(g);
            }
        } else {
            super.draw(g);
        }
    }

    @Override
    public Refuerzos clone() {
        Refuerzos temp = null;

        try {
            temp = new Refuerzos(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }
}


