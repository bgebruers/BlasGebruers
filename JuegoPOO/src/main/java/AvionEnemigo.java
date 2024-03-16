import java.awt.Point;
import java.io.IOException;

public class AvionEnemigo extends Enemigo {
    public AvionEnemigo(String filename, Point posicion, Point objetivo) throws IOException {
        super(filename, posicion, objetivo);
        this.resistencia = this.energia/5;
        this.arma.setFrecuenciaDisparos(30);
    }
    
    public AvionEnemigo(AvionEnemigo enemigo) throws IOException {
        super(enemigo);
        this.resistencia = enemigo.resistencia;
        this.arma = new Arma(enemigo.arma);
    }
    
   
    public Municion[][] disparar() {
        if(this.objetivoEnRadar() && 
            Math.abs((this.posicion.x + (grafico.getWidth()/2)) - (objetivo.x + (grafico.getWidth()/2))) < 70)
            return new Municion[][]{arma.disparar()};
         
        return null;
    }

    @Override
    public Enemigo clone() {
        AvionEnemigo temp = null;

        try {
            temp = new AvionEnemigo(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

    public int puntajeDado(){
        return 100;
    }
}
