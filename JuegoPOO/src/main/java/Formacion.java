public class Formacion {
    public enum FORMACIONES {
        SIMPLE(1, 3), ROMBO(2, 4), PROTEGER(3, 5);
                                                            // el protegido sera el ultimo de los integrantes
        private int tipo;
        public final int tamano;
        private FORMACIONES(int tipo, int tamano) {
            this.tipo = tipo;
            this.tamano = tamano;
        }
    }

    public static void iniciar(Enemigo[] integrantes, FORMACIONES tipo) {
        switch(tipo.tipo) {
            case 1:
                integrantes[0].update();

                integrantes[1].setX(integrantes[0].getX()-integrantes[0].grafico.getWidth()-50);
                integrantes[1].setY((int)(integrantes[0].getY()-50));

                integrantes[2].setX(integrantes[0].getX()+integrantes[0].grafico.getWidth()+50);
                integrantes[2].setY((int)(integrantes[0].getY()-50));
                break;
            case 2:
                integrantes[0].update();

                integrantes[1].setX(integrantes[0].getX()-integrantes[0].grafico.getWidth()-50);
                integrantes[1].setY((int)(integrantes[0].getY()-50));

                integrantes[2].setX(integrantes[0].getX()+integrantes[0].grafico.getWidth()+50);
                integrantes[2].setY((int)(integrantes[0].getY()-50));
                
                integrantes[3].setX(integrantes[0].getX());
                integrantes[3].setY((int)(integrantes[0].getY()-integrantes[0].grafico.getHeight()-50));
                break;
            case 3:
                integrantes[0].update();

                integrantes[1].setX(integrantes[0].getX()-integrantes[0].grafico.getWidth()-50);
                integrantes[1].setY((int)(integrantes[0].getY()-50));

                integrantes[2].setX(integrantes[0].getX()+integrantes[0].grafico.getWidth()+50);
                integrantes[2].setY((int)(integrantes[0].getY()-50));
                
                integrantes[3].setX(integrantes[0].getX());
                integrantes[3].setY((int)(integrantes[0].getY()-integrantes[0].grafico.getHeight()-integrantes[4].grafico.getHeight()-50));
                
                integrantes[4].setX(integrantes[0].getX());
                integrantes[4].setY((int)(integrantes[0].getY()-integrantes[0].grafico.getHeight()-20));
                break;
        }
    }

    public static FORMACIONES getTipo(int tipo) {
        if(tipo == 1) {
            return FORMACIONES.SIMPLE;
        } else if(tipo == 2) {
            return FORMACIONES.ROMBO;
        } else if(tipo == 3) {
            return FORMACIONES.PROTEGER;
        }
        
        return FORMACIONES.SIMPLE;
    }
}
