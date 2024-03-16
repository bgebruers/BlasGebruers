import java.awt.Point;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Arrays;

public class Arma extends ObjetoGrafico {
    public enum MUNICION {
        COMUN("municion1.png", false, 1), ESCOPETA("municion2.png", true, 2),
        LASER("laserM.png", false, 3), TRESCANOS("3canos.png", false, 1);

        private String municionFile;
        private boolean destructora;
        private int dano;
        private MUNICION(String municionFile, boolean destructora, int dano) {
            this.municionFile = municionFile;
            this.destructora = destructora;
            this.dano = dano;
        }
    }

    private Point objetivo;

    private MUNICION municion;
    private int frecuenciaDisparo, velocidadGiro;
    private boolean seguir, rafaga;
    private double anguloMax;
    private double angulo; // angulo de cada tiro
    private int cantTiros;
    private double[] angulos; // angulo del arma
    
    public Arma(Point posicion) throws IOException {
        super("Arma", null, posicion);

        this.municion = MUNICION.COMUN;
        this.velocidadGiro = 6;
        this.angulo = 0;

        this.frecuenciaDisparo = 25; // cada 25 frames
        this.seguir = this.rafaga = false;
        this.anguloMax = 90;

        this.cantTiros = 1;
        this.angulos = new double[]{this.angulo};
    }

    public void setObjetivo(Point objetivo) {
        this.objetivo = objetivo;
        this.posPrevia = (Point) this.objetivo.clone();
    }

    public Arma(Arma arma) throws IOException {
        super("Arma", null, arma.posicion);

        this.municion = arma.municion;
        this.objetivo = arma.objetivo;
        this.posPrevia = (Point) arma.objetivo.clone();

        this.velocidadGiro = arma.velocidadGiro;
        this.angulo = arma.angulo; 

        this.frecuenciaDisparo = arma.frecuenciaDisparo; // cada 15 frames
        this.seguir = arma.seguir;
        this.rafaga = arma.rafaga;
        this.anguloMax = arma.anguloMax;
        this.grafico = arma.grafico;
        this.cantTiros = arma.cantTiros;
        this.angulos = Arrays.copyOf(arma.angulos, arma.angulos.length);
    }

    public void setPosicion(Point posicion) {
        this.posicion = (Point) posicion.clone();
    }

    public void setTipoMunicion(MUNICION tipo) {
        this.municion = tipo;
    }

    public void setGrafico(String filename) {
        try {
            grafico = ImageIO.read(getClass().getResource("imagenes/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setModoDisparo(boolean rafaga) {
        this.rafaga = rafaga;
    }

    public void seguir(boolean seguir) {
        this.seguir = seguir;
    }

    public void setTiros(int cantidad, double[] angulos) {
        if(cantidad != angulos.length) {
            return;
        }

        this.cantTiros = cantidad > 0 ? cantidad : 1;
        this.angulos = new double[angulos.length];

        for(int i = 0; i < angulos.length; i++) {
            this.angulos[i] = this.angulo + Math.toRadians(angulos[i]);
        }
    }

    public void setFrecuenciaDisparos(int frecuenciaDisparo) {
        this.frecuenciaDisparo = frecuenciaDisparo;
    }

    public void setAnguloMaximo(double anguloMax) {
        if(anguloMax >= 0 && anguloMax <= 180)
            this.anguloMax = anguloMax;
    }

    public void setAngulo(double angulo) {
        this.angulo = Math.toRadians(angulo);
        setTiros(cantTiros, angulos);
    }

    private void rotar() {
        double y = this.posicion.getLocation().y - posPrevia.y;
        double x = this.posicion.getLocation().x - posPrevia.x - 20;

        if(Math.abs(x) > 10) {
            double tempAng = -(Math.abs(x)/x) * Math.atan(-Math.abs(x/y));

            if(x > 0 && y > 0) {
                tempAng = Math.PI - Math.abs(tempAng); 
            } else if(x < 0 && y > 0) {
                tempAng += Math.PI;
                tempAng = -tempAng; 
            }

            tempAng = Math.abs(Math.toDegrees(tempAng)) < anguloMax ? tempAng : angulo;
            
            for(int i = 0; i < this.angulos.length; i++) {
                this.angulos[i] -= angulo - tempAng;
            }

            this.angulo = tempAng;
        }
    }

    private int contadorFrecDisp = 0;
    private int contadorRafaga = 0;
    public Municion[] disparar() {
        try {
            if(this.rafaga && contadorRafaga > 6 && contadorRafaga < 20) {
                contadorRafaga++;
                return null;
            } else if(contadorRafaga > 12) {
                contadorRafaga = 0;
            }

            if(contadorFrecDisp >= this.frecuenciaDisparo) {
                contadorFrecDisp = 0;

                Point tempPos = new Point(this.posicion.x, this.posicion.y);

                if(grafico != null) {
                    tempPos.y -= this.grafico.getHeight()/2;
                }

                Municion muni[] = new Municion[this.cantTiros];
                
                int gap = 12;
                for(int i = -(int)Math.floor(cantTiros/2); i < (int)Math.ceil((double)(cantTiros)/2); i++) {
                    tempPos.x = this.posicion.x;

                    if(i >= 0 && cantTiros%2 == 0) {
                        tempPos.x += gap * (i+1);
                    } else {
                        tempPos.x += gap * i;
                    }

                    Municion tempMuni = new Municion(
                        municion.municionFile, 
                        tempPos, 
                        this.angulos[i + (int)Math.floor(cantTiros/2)]
                    );

                    tempMuni.setDestructora(municion.destructora);
                    tempMuni.setDano(municion.dano);

                    muni[i + (int)Math.floor(cantTiros/2)] = tempMuni;
                }

                contadorRafaga++;
                
                return muni;
            }
            
            contadorFrecDisp++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update() {}

    private Point posPrevia;
    private int contadorVelGiro = 0;
    public void update(Point posicion) {
        this.posicion = (Point) posicion.clone();

        if(grafico != null) {
            this.posicion.x += this.grafico.getWidth()/2;
            this.posicion.y += this.grafico.getHeight()/2;
        }

        if(seguir) {           // 60 fps
            if(contadorVelGiro == 60/this.velocidadGiro) {
                
                if(this.objetivo != null) {
                    posPrevia = (Point) this.objetivo.clone();
                }

                contadorVelGiro = 0;
            }
            
            if(posPrevia != null) {
                this.rotar();
            }
            
            contadorVelGiro++;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        Graphics2D g1 = (Graphics2D)g.create();

        g1.rotate(angulo, posicion.x, posicion.y);
        
        if(this.grafico != null) {
            g1.drawImage(grafico, posicion.x - this.grafico.getWidth()/2, posicion.y-this.grafico.getHeight()/2, null);
        } else {
            g1.drawImage(grafico, posicion.x, posicion.y, null);
        }
    }
}
