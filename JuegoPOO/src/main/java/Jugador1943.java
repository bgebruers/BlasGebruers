public class Jugador1943 extends Jugador {
    private int puntaje;

    public Jugador1943(){
        super("");
        this.puntaje = 0;
    }

    public void setPuntaje(int puntos){
        this.puntaje = this.puntaje + puntos;
    }

    public int getPuntaje(){
        return this.puntaje;
    }

}
