import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;

public abstract class Bonus extends ObjetoGrafico {
    protected int tiempo; // tiempo en segundos que dura en en el avion
    protected double vidaBonus, resistenciaBonus;
    
    public Bonus(String nombre, String grafico, Point posicion) throws IOException {
        super(nombre, grafico, posicion);
        this.tiempo = -1;
        this.vidaBonus = 100;
        this.resistenciaBonus = vidaBonus/5;
    }
 
    public abstract void asignarBonus(AvionAmigo avion);
    
    public void modificarVida(double deltaE){
        if((vidaBonus + deltaE) > 0){
            vidaBonus +=  deltaE;
        }else { 
            vidaBonus = 0;
           destruir();
        }
    }

    public void activar() {
        if(tiempo == -1) { // el bonus nunca se uso
            this.tiempo = 20;
        }
    }

    public double getVida(){
        return this.vidaBonus;
    }

    public double getResistencia(){
        return this.resistenciaBonus;
    }

    public boolean activo() {
        return tiempo > 0;
    }

    public String toString() {
        return nombre + ": " + tiempo;
    }
    
    private int contador = 0;
    @Override
    public void update() {
        if(tiempo == 0)
            return;

        if(contador == 60) {
            tiempo--;
            contador = 0;
        }

        contador++;
    }

    public abstract void destruir();

    public void draw(Graphics2D g) {
        if(!this.activo()) {
            g.drawImage(grafico, posicion.x, posicion.y, null);
        }
    }   
   
    public abstract Bonus clone();
}
