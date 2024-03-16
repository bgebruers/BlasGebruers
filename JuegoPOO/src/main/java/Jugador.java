public abstract class Jugador {
    protected String nombre;
    
    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
}
