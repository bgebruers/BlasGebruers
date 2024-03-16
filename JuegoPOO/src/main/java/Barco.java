import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

public class Barco extends Enemigo {
    protected Arma arma1, arma2;

    public Barco(String grafico, Point posicion, Point objetivo) throws IOException {
        super(grafico, posicion, objetivo);
        this.rangoDeteccion = 800;
    
        arma.setPosicion(new Point(this.getX()+this.grafico.getWidth()/2-10, this.getY()+this.grafico.getHeight()/2-20));
        arma.setGrafico("armaBarco1.png");
        arma.seguir(true);
        arma.setFrecuenciaDisparos(60);

        arma1 = new Arma(new Point(this.getX()+this.grafico.getWidth()/2-10, this.getY()+this.grafico.getHeight()/2-70));
        arma1.setObjetivo(this.objetivo);
        arma1.setGrafico("armaBarco1.png");
        arma1.seguir(true);
        arma1.setFrecuenciaDisparos(60);
        
        arma2 = new Arma(new Point(this.getX()+this.grafico.getWidth()/2-10, this.getY()+this.grafico.getHeight()/2+30));
        arma2.setObjetivo(this.objetivo);
        arma2.setGrafico("armaBarco1.png");
        arma2.seguir(true);
        arma2.setFrecuenciaDisparos(60);

        this.resistencia = this.energia/8;
    }
    
    public Barco(Barco enemigo) throws IOException {
        super(enemigo);
        
        arma = new Arma(enemigo.arma);
        arma1 = new Arma(enemigo.arma1);
        arma2 = new Arma(enemigo.arma2);

        this.resistencia = enemigo.resistencia;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad < 4 ? velocidad : 4;
    }

    public Arma[] getArmas() {
        return new Arma[]{arma, arma1, arma2};
    }

    @Override
    public Municion[][] disparar() {
        if(this.objetivoEnRadar())
            return new Municion[][]{arma.disparar(), arma1.disparar(), arma2.disparar()};
         
        return null;
    }

    @Override
    public void update() {
        this.avanzar();
        arma.update(new Point(this.getX()+this.grafico.getWidth()/2-10, this.getY()+this.grafico.getHeight()/2-20));
        arma1.update(new Point(this.getX()+this.grafico.getWidth()/2-10, this.getY()+this.grafico.getHeight()/2-70));
        arma2.update(new Point(this.getX()+this.grafico.getWidth()/2-10, this.getY()+this.grafico.getHeight()/2+30));

        if(this.objetivoEnRadar()) {
            this.setVelocidad(2);
        }
    }

//puntaje que otorga si se destruye
    public int puntajeDado(){
        return 150;
    }


    @Override
    public void draw(Graphics2D g) {
        g.drawImage(grafico, posicion.x, posicion.y, null);
        arma.draw(g);
        arma1.draw(g);
        arma2.draw(g);
    }

    @Override
    public Enemigo clone() {
        Barco temp = null;

        try {
            temp = new Barco(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

}
