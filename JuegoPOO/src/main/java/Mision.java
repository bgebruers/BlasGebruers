import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.io.IOException;

public class Mision {
    public enum ESTADO {
        AIRE, TIERRA, FIN, GANA;
    }

    public enum DIFICULTAD {
        FACIL(1), DIFICIL(2);

        private final int dif;
        private DIFICULTAD(int dif) {
            this.dif = dif;
        }
    }
    
    private ESTADO estado;
    private final DIFICULTAD dificultad;
    private final Enemigo[] repertorioEnemigos;
    private final Enemigo jefe;
    private final AvionAmigo heroe;

    private final String nombre;
    private final int tiempoTotal; // en segundos
    private int tiempoEnCurso; // en segundos
    private final int factorProb = 10; // de que se generen enemigos, bonus, etc.
    private final int apareceJefe; // faltando este tiempo aparecera el jefe
    
    private Bonus[] bonus;
    private int contadorSegundo = 0; // cuenta los frames como referencia del tiempo 

    private final ArrayList<Enemigo> enemigosCreados;
    private final ArrayList<Municion> balasEnCurso;
    private final ArrayList<Municion> balasHeroe;
    private final ArrayList<Bonus> bonusEnPantalla, bonusAsignados; 
    private final ArrayList<Impacto> impactos;

    private AvionAmigo[] refuerzos;
    private AtaqueEspecial ataqueEspecial; // VER
    private AvionRojo[] avionesRojos;
    private int avionesRojosEnProceso;

    private Rectangle bonusSecreto;
    private boolean generarBonusSecreto, bonusSecretoAgarrado;
        
    private Mision(MisionBuilder builder) {
        estado = ESTADO.AIRE;
        this.dificultad = builder.dificultad;
        this.repertorioEnemigos = builder.enemigos;
        this.jefe = builder.jefe;
        this.heroe = builder.heroe;
        
        this.nombre = builder.nombre;
        this.tiempoTotal = builder.tiempo;
        this.tiempoEnCurso = this.tiempoTotal;

        enemigosCreados = new ArrayList<Enemigo>();
        balasEnCurso = new ArrayList<Municion>();
        balasHeroe = new ArrayList<Municion>();
        bonusEnPantalla = new ArrayList<Bonus>();
        bonusAsignados = new ArrayList<Bonus>();
        impactos = new ArrayList<Impacto>();

        refuerzos = new AvionAmigo[2];
        avionesRojos = new AvionRojo[3];
        avionesRojosEnProceso = 0;
        apareceJefe = tiempoTotal/2-20;

        this.generarBonusSecreto = builder.generarBonusSecreto;
        this.bonusSecretoAgarrado = false;

        try{
            bonus = new Bonus[]{
                new Pow(new Point(0, 0)),
                new Auto(new Point(0, 0)),
                new EstrellaNinja(new Point(0, 0)),
                new SuperShell(new Point(0, 0)),
                new Refuerzos(new Point(0, 0)),
                new AmetralladoraTresCañones(new Point(0, 0)),
                new Laser(new Point(0, 0)),
                new Escopeta(new Point(0, 0)),
            };
        }catch(IOException e){
            System.out.println("Error al crear el bonus");
        }
    }

    public void update() {
        manejarTiempo();

        heroe.update();

        if(heroe.getEnergia() <= 0) {
            this.estado = ESTADO.FIN;
        }

        crearEnemigos();
        crearBonus();
        crearAvionesRojos();        

        manejarEnemigos();
        manejarBonus(); 
        manejarAvionesRojos();

        manejarImpactos();

        if(heroe.getEnergia() <= 0){
            this.estado = Mision.ESTADO.FIN;
        }

        if(ataqueEspecial != null) {
            manejarAtaqueEspecial();
        }

        if(bonusSecreto != null) {
            manejarBonusSecreto();
        }

        if(tiempoEnCurso <= apareceJefe) {
            manejarJefe();
        }
    }

    private void manejarTiempo() {
        if(tiempoEnCurso == tiempoTotal/2) {
            this.estado = ESTADO.TIERRA;
        } if(tiempoEnCurso == 0) {
            this.estado = ESTADO.FIN;
        }

        if(contadorSegundo == 60) { // pensando siempre en 60 fps
            heroe.modificarEnergia(-0.01);
            eliminarImpactos();
            
            tiempoEnCurso--;
            contadorSegundo = 0;
        } else {
            contadorSegundo++;
        }

    }

    private void crearEnemigos() {
        int random = (int)(Math.floor(Math.random()*999+1));
                         // cuanto mas alta la dificultad, mas probabilidades de q se generen enemigos
        if(enemigosCreados.size() < 10*dificultad.dif && random <= factorProb*dificultad.dif) {

            if(random >= factorProb*dificultad.dif+1-dificultad.dif) {
                try {
                    crearFormacion(Formacion.getTipo((int)(Math.floor(Math.random()*(3)+1))));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return;
            }

            Enemigo e = crearEnemigo();

            if(e != null) {
                enemigosCreados.add(e);
            }
        }
    }

    private Enemigo crearEnemigo() {
        int nuevoEnemigo = (int)(Math.floor(Math.random()*(repertorioEnemigos.length-1-0+1)+0));
            
        if(repertorioEnemigos[nuevoEnemigo].getClass().getName().equals("Barco") 
            && this.estado != ESTADO.TIERRA) {
                return null;
            }

        Enemigo e = repertorioEnemigos[nuevoEnemigo].clone();

        int tempX = (int)(Math.floor(Math.random()*(700-100+1)+100));
        int tempY = heroe.getY()-(int)(Math.floor(Math.random()*(1300-800+1)+800));

        Rectangle nuevoEnemigoR = new Rectangle(new Point(tempX, tempY), e.getDimensions());

        // Chequear que no aparezcan enemigos superpuestos
        int contador = 0;
        for(int i = 0; i < enemigosCreados.size(); i++) {
            if(contador == 10) {
                return null;
            }
            
            Enemigo enemigo = enemigosCreados.get(i);
            Rectangle enemigoR = new Rectangle(enemigo.getPosicion(), enemigo.getDimensions());
            
            if(nuevoEnemigoR.intersects(enemigoR)) {
                tempX = (int)(Math.floor(Math.random()*(700-100+1)+100));
                tempY = heroe.getY()-(int)(Math.floor(Math.random()*(1300-800+1)+800));                        
                i--;
            }

            contador++;
        }

        if(tiempoEnCurso < apareceJefe) {
            Rectangle jefeR = new Rectangle(jefe.getPosicion(), jefe.getDimensions());
            
            int random = (int)(Math.random()*(2)+1);

            if(random <= 1) {
                tempX = (int)(Math.floor(Math.random()*(jefeR.getX()-50-100+1)+100));
            } else {
                tempX = (int)(Math.floor(Math.random()*(700-(jefeR.getX()+jefeR.getWidth()+50)+1)+(jefeR.getX()+jefeR.getWidth()+50)));
            }

            tempY = heroe.getY()-(int)(Math.floor(Math.random()*(1300-800+1)+800));
        }

        contador = 0;
        if(avionesRojosEnProceso > 0) {
            for(int i = 0; i < avionesRojos.length; i++) {
                if(avionesRojos[i] != null) {
                    if(contador == 10) {
                        return null;
                    }

                    Rectangle avionRojoR = new Rectangle(avionesRojos[i].getPosicion(), avionesRojos[i].getDimensions());

                    if(nuevoEnemigoR.intersects(avionRojoR)) {
                        tempX = (int)(Math.floor(Math.random()*(700-100+1)+100));
                        tempY = heroe.getY()-(int)(Math.floor(Math.random()*(1300-800+1)+800));                        
                    }
                }
                
                contador++;
            }         
        }

        e.setX(tempX);
        e.setY(tempY);

        return e;
    }

    private void crearFormacion(Formacion.FORMACIONES tipo) throws Exception {
        Enemigo[] integrantes = new Enemigo[tipo.tamano];

        int contador = 0;
        for(int i = 0; i < tipo.tamano; i++) {
            if(contador == 10) {
                return;
            }
            
            Enemigo temp = crearEnemigo();

            if(temp != null && !temp.getClass().getName().equals("Barco")) { // formaciones solo de aviones
                integrantes[i] = temp;
                enemigosCreados.add(temp);
            } else {
                i--;
            }

            contador++;
        }

        Formacion.iniciar(integrantes, tipo);
    }

    private void manejarEnemigos() {
        for(int i = 0; i < enemigosCreados.size(); i++) {
            Enemigo temp = enemigosCreados.get(i);

            temp.update();

            // eliminar enemigos fuera de la pantalla
            if(temp.getY() > (heroe.getY() + (Juego1943.getInstance().getHeight())) + 25) {
                enemigosCreados.remove(i);
                i--;
                continue;
            }

            disparar(temp);
        }

        for(int i = 0; i < balasEnCurso.size(); i++) {
            Municion temp = balasEnCurso.get(i);
            
            temp.update();

            // eliminar balas fuera de la pantalla
            if(temp.getY() > (heroe.getY() + (Juego1943.getInstance().getHeight())) + 25) { 
                balasEnCurso.remove(i);
                i--;
                continue;
            }
        }

        for(int i = 0; i < balasHeroe.size(); i++) {
            Municion temp = balasHeroe.get(i);
            
            temp.update();

            if(!((Juego1943)(Juego1943.getInstance())).getViewPort().contains(
                    new Rectangle(temp.getPosicion(), temp.getDimensions()))) { 
                balasHeroe.remove(i);
                i--;
                continue;
            }
        }
    }
   
    private void crearBonus(){
        int random = (int)(Math.floor(Math.random()*999+1));
        
        if(bonusEnPantalla.size() < 1 && random < 10/dificultad.dif ) {
            Bonus b = generarBonus();
            
            if(b != null) {
                bonusEnPantalla.add(b);
            }
        }
        
        int bs = (int)(Math.floor(Math.random()*5000+1));
        if(bs == 3121 && bonusSecreto == null && generarBonusSecreto) {
            crearBonusSecreto();
        }
    }

    private void crearBonusSecreto() {
        int x = (int)(Math.floor(Math.random()*(700-100+1)+100));
        
        int yMax = (int) ((Juego1943)Juego1943.getInstance()).getViewPort().getY();
        int yMin = (int) (((Juego1943)Juego1943.getInstance()).getViewPort().getY() + ((Juego1943)Juego1943.getInstance()).getViewPort().getHeight());
        int y = (int)(Math.floor(Math.random()*(yMax-yMin+1)+yMin));

        bonusSecreto = new Rectangle(x, y, 10, 10);
    }
        
    private Bonus generarBonus() {
        int randomBonus = (int)(Math.floor(Math.random()*((bonus.length-1)+1)));
        Bonus b = bonus[randomBonus].clone();

        b.setX((int)(Math.floor(Math.random()*(700-100+1)+100)));
        b.setY(heroe.getY()-500);    

        return b;
    }

    private void reemplazarBonus(Bonus bn) {
        Bonus b = generarBonus();

        if(b == null) {
            bn.modificarVida(100); // Si hubiera un error, se le rellena la vida al bonus q ya hay y queda ese.
            return;
        }

        int i = 0; // solo para garantizar q no se quede en bucle mas de lo debido
        while(b.getClass().equals(bn.getClass()) && i < 10) {
            b = generarBonus();
            i++;
        }

        b.setX(bn.getX());
        b.setY(bn.getY());

        int index = bonusEnPantalla.indexOf(bn);
        if(index != -1) {
            bonusEnPantalla.remove(index);
            bonusEnPantalla.add(index, b);
        }
    }
    
    private void manejarBonus() {
        for(int i = 0; i < bonusEnPantalla.size(); i++) {
            Bonus temp = bonusEnPantalla.get(i);
            Rectangle bonusRectangle = new Rectangle(temp.getPosicion(), temp.getDimensions());

            if(new Rectangle(heroe.getPosicion(), heroe.getDimensions())
            .intersects(bonusRectangle)) {
                bonusEnPantalla.remove(i);
                otorgarBonus(temp);
            }

            // eliminar bonuses fuera de la pantalla
            if(temp.getY() > (heroe.getY() + (Juego1943.getInstance().getHeight())) + 25) { 
                bonusEnPantalla.remove(i);
                i--;
                continue;
            }
        }

        for(int i = 0; i < bonusAsignados.size(); i++) {
            Bonus b = bonusAsignados.get(i);
            
            if(!b.activo()) {
                bonusAsignados.remove(i);

                if(b.getClass().getName().equals("Refuerzos")) {
                    refuerzos = new AvionAmigo[2];
                }
            }

            b.update();
        }

        manejarRefuerzos();
    }

    private void otorgarBonus(Bonus bonus) {
        String incompatibles = "Escopeta, Laser, AmetralladoraTresCañones";

        for(int i = 0; i < bonusAsignados.size(); i++) {
            Bonus b = bonusAsignados.get(i);

            boolean incompatibilidad = incompatibles.toString().contains(b.getClass().getName()) && incompatibles.toString().contains(bonus.getClass().getName());

            if(b.getClass().getName().equals(bonus.getClass().getName()) 
                || incompatibilidad) { 
                b.destruir();             // si se agarra un tipo de bonus cuando ya se tiene,
                bonusAsignados.remove(i); // se reinicia el tiempo del bonus
            }                             
        }

        bonusAsignados.add(bonus);
        bonus.asignarBonus(heroe);
        bonus.activar();

        if(bonus.getClass().getName().equals("Refuerzos")) {
            this.refuerzos = ((Refuerzos)bonus).getRefuerzos();
        }
    }

    private void manejarBonusSecreto() {
        if(bonusSecreto.getY() > (heroe.getY() + (Juego1943.getInstance().getHeight())) + 25) {
            bonusSecreto = null;
        }
    }

    private void crearAvionesRojos() {
        int random = (int)(Math.random()*500+1);
        
        if(avionesRojosEnProceso == 0 && random == 1){
            try {
                int tempX = (int)(Math.floor(Math.random()*(700-100+1)+100));
                Point pos = new Point(tempX, heroe.getY()-650);

                avionesRojos = new AvionRojo[]{
                    new AvionRojo(pos),
                    new AvionRojo(pos),
                    new AvionRojo(pos)
                };

                avionesRojosEnProceso = 3;
    
                Formacion.iniciar(avionesRojos, Formacion.FORMACIONES.SIMPLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void manejarAvionesRojos(){
        for (int i = 0; i < avionesRojos.length; i++) {
            if(avionesRojos[i] != null){
                avionesRojos[i].update();

                if(avionesRojos[i].getY() > (heroe.getY() + (Juego1943.getInstance().getHeight())) + 25) { 
                    eliminarAvionRojo(i, false);
                    continue;
                }
            }
        }
    }

    private void eliminarAvionRojo(int j, boolean darBonus) {
        avionesRojosEnProceso--;

        if(avionesRojosEnProceso == 0 && darBonus) {
            otorgarBonusAvionesRojos(avionesRojos[j].getPosicion());
        }

        avionesRojos[j] = null;
    }

    private void otorgarBonusAvionesRojos(Point posicion){
        Bonus b = generarBonus();

        b.setX((int)posicion.getX());
        b.setY((int)posicion.getY()-50);

        bonusEnPantalla.add(b);
    }

    public void ataqueEspecial() {
        try {
            if(this.estado == ESTADO.AIRE) {
                this.ataqueEspecial = new AtaqueEspecial(AtaqueEspecial.ATAQUE.RAYO);
                enemigosCreados.removeIf(enemigo -> enemigo.getClass().getName().equals("AvionEnemigo"));
            } else if(this.estado == ESTADO.TIERRA) {
                this.ataqueEspecial = new AtaqueEspecial(AtaqueEspecial.ATAQUE.TSUNAMI);
                enemigosCreados.removeIf(enemigo -> enemigo.getClass().getName().equals("Barco"));
            }

            if(tiempoEnCurso <= apareceJefe) {
                jefe.modificarEnergia(-jefe.getResistencia()*2);
            }
            
            heroe.modificarEnergia(-100/5);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void manejarAtaqueEspecial() {
        if(ataqueEspecial != null && !ataqueEspecial.isEnabled()) {
            this.ataqueEspecial = null;
        }
    }

    private void manejarRefuerzos() {
        for(int i = 0; i < refuerzos.length; i++) {
            AvionAmigo temp = refuerzos[i];

            if(temp == null) {
                for(int j = 0; j < bonusAsignados.size(); j++) {
                    Bonus b = bonusAsignados.get(j);

                    if(b.getClass().getName().equals("Refuerzos")) {
                        ((Refuerzos)b).destruirRefuerzo(i+1);
                    }
                }

                continue;
            }

            enemigosCreados.forEach(enemigo -> {
                int dy = Math.abs(enemigo.getY()+enemigo.grafico.getHeight()/2) - Math.abs(temp.getY());
                int dx = Math.abs(temp.getX() - enemigo.getX()+enemigo.grafico.getWidth()/2);

                if(dy < 300 && dx < 60) {
                    Municion[][] balas = temp.disparar();

                    for(Municion[] bala : balas) {
                        if(bala != null) {
                            for(Municion b : bala) {
                                if(b != null) {
                                    disparoHeroe(b);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void eliminarImpactos() {
        for(int i = 0; i < impactos.size(); i++) {
            if(!impactos.get(i).isEnabled()) {
                impactos.remove(i);
            }
        }
    }

    private void disparar(Enemigo e) {
        Municion municiones[][] = e.disparar();

        if(municiones != null) {
            for(Municion[] ms : municiones) {
                if(ms != null) {
                    for(Municion m : ms) {
                        if(m != null) {
                            balasEnCurso.add(m);
                        }
                    }
                }
            }
        }
    }

    private void manejarJefe() {
        if(tiempoEnCurso == apareceJefe) {
            jefe.setX(Juego1943.getInstance().getWidth()/2 - jefe.grafico.getWidth()/2);
            jefe.setY(heroe.getY()-1000);
        }

        Rectangle j = new Rectangle(jefe.getPosicion(), jefe.getDimensions());
        
        if(((Juego1943)Juego1943.getInstance()).getViewPort().contains(j)) {
            jefe.setY((int)(((Juego1943)Juego1943.getInstance()).getViewPort().getY()+35));
        }

        jefe.update();

        Municion municiones[][] = jefe.disparar();

        if(municiones != null) {
            for(Municion[] ms : municiones) {
                if(ms != null) {
                    for(Municion m : ms) {
                        if(m != null) {
                            balasEnCurso.add(m);
                        }
                    }
                }
            }
        }
    }

    private void manejarImpactos() {
        Rectangle avionAmigo = new Rectangle(heroe.getPosicion(), heroe.getDimensions());
    
        Rectangle jefeR = new Rectangle(this.jefe.getPosicion(), this.jefe.getDimensions());

        Rectangle refuerzo1 = null, refuerzo2 = null;

        if(refuerzos[0] != null) {
            refuerzo1 = new Rectangle(refuerzos[0].getPosicion(), refuerzos[0].getDimensions());
        }
        
        if(refuerzos[1] != null) {
            refuerzo2 = new Rectangle(refuerzos[1].getPosicion(), refuerzos[1].getDimensions());
        }

        for(int i = 0; i < balasEnCurso.size(); i++) {
            Municion bala = balasEnCurso.get(i);
            Rectangle balaR = new Rectangle(bala.getPosicion(), bala.getDimensions());

            if(balaR.intersects(avionAmigo)) {
                if(!heroe.isEsquivando()) {
                    anadirImpacto(avionAmigo, Impacto.tipoImpacto.DISPARO);
                }
                
                heroe.modificarEnergia(-heroe.getResistencia());
                balasEnCurso.remove(i);
            }
            
            for(int j = 0; j < refuerzos.length; j++) {
                if(refuerzos[j] != null) {
                    Rectangle r = j == 0 ? refuerzo1 : refuerzo2;
                    
                    if(balaR.intersects(r)) {
                        anadirImpacto(r, Impacto.tipoImpacto.DISPARO);
                        refuerzos[j].modificarEnergia(-refuerzos[j].getResistencia());
                        balasEnCurso.remove(i);
                        
                        if(refuerzos[j].getEnergia() <= 0) {
                            refuerzos[j] = null;
                        }
                    }
                }
            }
        }           
    
        
        for(int i = 0; i < balasHeroe.size(); i++) {
            Municion bala = balasHeroe.get(i);
            Rectangle balaR = new Rectangle(bala.getPosicion(), bala.getDimensions());

            for(int j = 0; j < enemigosCreados.size(); j++) {
                Enemigo enemigo = enemigosCreados.get(j);
                Rectangle enemigoR = new Rectangle(enemigo.getPosicion(), enemigo.getDimensions());

                if(balaR.intersects(enemigoR)) {
                    anadirImpacto(enemigoR, Impacto.tipoImpacto.DISPARO);
                    
                    enemigo.modificarEnergia(-enemigo.getResistencia() * bala.getDano());
                    
                    balasHeroe.remove(i);

                    //en este metodo se haria el modificar puntaje
                    if(enemigo.getEnergia() <= 0) {
                        heroe.pasarPuntaje(enemigo.puntajeDado());
                        enemigosCreados.remove(j);        
                    }
                }
            }

            if(bala.esDestructora()) {
                for(int j = 0; j < balasEnCurso.size(); j++) {
                    Municion ebala = balasEnCurso.get(j);
                    Rectangle ebalaR = new Rectangle(ebala.getPosicion(), ebala.getDimensions());
        
                    if(balaR.intersects(ebalaR)) {
                        anadirImpacto(ebalaR, Impacto.tipoImpacto.DISPARO);
                        balasEnCurso.remove(j);
                    }
                }
            }

            if(balaR.intersects(jefeR)) {
                anadirImpacto(jefeR, Impacto.tipoImpacto.DISPARO);
                
                this.jefe.modificarEnergia(-this.jefe.getResistencia() * bala.getDano());
                
                balasHeroe.remove(i);

                if(this.jefe.getEnergia() <= 0) {
                    this.estado = ESTADO.GANA;
                }
            }

            for (int j = 0; j < avionesRojos.length; j++) {
                if(avionesRojos[j] == null) {
                    continue;
                }

                Rectangle avionRojo = new Rectangle(this.avionesRojos[j].getPosicion(), avionesRojos[j].getDimensions());
        
                if(balaR.intersects(avionRojo)) {
                    anadirImpacto(avionRojo, Impacto.tipoImpacto.DISPARO);
                    avionesRojos[j].modificarEnergia(-avionesRojos[j].getResistencia() * bala.getDano()); 
    
                    if(avionesRojos[j].getEnergia() <= 0 ){
                        eliminarAvionRojo(j, true);
                    }
                }
            }
        }

        for(int i = 0; i < refuerzos.length; i++) {
            if(refuerzos[i] != null) {
                Rectangle refuerzo = i == 0 ? refuerzo1 : refuerzo2;
                
                for(int j = 0; j < avionesRojos.length; j++) {
                    if(avionesRojos[j] == null) {
                        continue;
                    }

                    Rectangle avionRojo = new Rectangle(this.avionesRojos[j].getPosicion(), avionesRojos[j].getDimensions());

                    if(avionRojo.intersects(refuerzo)) {
                        anadirImpacto(avionRojo, Impacto.tipoImpacto.COLISION);
                        anadirImpacto(refuerzo, Impacto.tipoImpacto.COLISION);
    
                        refuerzos[i] = null;
    
                        eliminarAvionRojo(j, true);
                    }
                }
            }
        }

        for(int i = 0; i < avionesRojos.length; i++) {
            if(avionesRojos[i] == null) {
                continue;
            }

            Rectangle avionRojo = new Rectangle(this.avionesRojos[i].getPosicion(), avionesRojos[i].getDimensions());
            
            if(avionRojo.intersects(avionAmigo)) {
                anadirImpacto(avionRojo, Impacto.tipoImpacto.COLISION);
                anadirImpacto(avionAmigo, Impacto.tipoImpacto.COLISION);

                eliminarAvionRojo(i, true);
                heroe.modificarEnergia(-heroe.getResistencia()*2);
            }
        }
        
        for (int i = 0; i < bonusEnPantalla.size(); i++) {
            Bonus bonus = bonusEnPantalla.get(i);
            Rectangle bonusR = new Rectangle(bonus.getPosicion(), bonus.getDimensions());

            for (int j = 0; j < balasHeroe.size(); j++) {
                Municion bala = balasHeroe.get(j);
                Rectangle balaR = new Rectangle(bala.getPosicion(), bala.getDimensions());

                if(balaR.intersects(bonusR)) {
                    anadirImpacto(bonusR, Impacto.tipoImpacto.DISPARO);

                    balasHeroe.remove(j);
    
                    bonus.modificarVida(-bonus.getResistencia() * bala.getDano());
                    
                    if(bonus.getVida() <= 0) {  
                        reemplazarBonus(bonus);
                    }
                }
            }
        }
        
        for(int i = 0; i < enemigosCreados.size(); i++) {
            Enemigo enemigo = enemigosCreados.get(i);
            Rectangle enemigoR = new Rectangle(enemigo.getPosicion(), enemigo.getDimensions());
            
            if(enemigo.getClass().getName().equals("AvionEnemigo") && enemigoR.intersects(avionAmigo)) {
                anadirImpacto(avionAmigo, Impacto.tipoImpacto.COLISION);
                anadirImpacto(enemigoR, Impacto.tipoImpacto.COLISION);
                
                enemigosCreados.remove(i);
                
                enemigo.modificarEnergia(-100);
                heroe.modificarEnergia(-heroe.getResistencia()*2); 
            }
            
            for(int j = 0; j < refuerzos.length; j++) {
                if(refuerzos[j] != null) {
                    Rectangle r = j == 0 ? refuerzo1 : refuerzo2;
                    
                    if(enemigoR.intersects(r)) {
                        anadirImpacto(enemigoR, Impacto.tipoImpacto.COLISION);
                        anadirImpacto(r, Impacto.tipoImpacto.COLISION);

                        refuerzos[j] = null;

                        enemigosCreados.remove(i);
                        enemigo.modificarEnergia(-100);
                    }
                }
            }
        }

        if(bonusSecreto != null) {
            if(avionAmigo.intersects(bonusSecreto)) {
                this.bonusSecretoAgarrado = true;
            }
        }
    }

    private void anadirImpacto(Rectangle objetivo, Impacto.tipoImpacto impacto) {
        try {
            impactos.add(new Impacto(
                new Point((int)(objetivo.getX()+objetivo.getWidth()/2), (int)(objetivo.getY()+objetivo.getHeight()/2-5)), 
                impacto)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disparoHeroe(Municion bala) {
        balasHeroe.add(bala);
    }

    public ESTADO getEstado() {
        return estado;
    }

    public void draw(Graphics2D g, int pos) {
        bonusEnPantalla.forEach(bonus -> {
            bonus.draw(g);
        });

        for (AvionRojo avionRojo : avionesRojos) {
            if(avionRojo != null) {
                avionRojo.draw(g);
            }
        }

        enemigosCreados.forEach(enemigo -> {
            enemigo.draw(g);
        });

        if(tiempoEnCurso <= apareceJefe) {
            jefe.draw(g);
            g.setColor(Color.red);
            g.drawRect(((Juego1943)Juego1943.getInstance()).getWidth()-225, pos+1, 200, 15);
            g.fillRect(((Juego1943)Juego1943.getInstance()).getWidth()-225, pos+1, (int)jefe.getEnergia()*2, 15);

        }

        
        if(!bonusAsignados.isEmpty()) {
            g.setColor(Color.black);
            g.drawString("Bonus Activos:", 11, pos+73);

            g.setColor(Color.white);
            g.drawString("Bonus Activos:", 10, pos+72);
        }
        
        for(int i = 0; i < bonusAsignados.size(); i++) {
            Bonus bonus = bonusAsignados.get(i);
            
            bonus.draw(g);
            
            g.setColor(Color.black);
            g.drawString("    " + bonus.toString(), 11, pos+73+17*(i+1));
            
            g.setColor(Color.white);
            g.drawString("    " + bonus.toString(), 10, pos+72+17*(i+1));
        }
        
        heroe.draw(g);
        
        balasEnCurso.forEach(bala -> {
            bala.draw(g);
        });

        balasHeroe.forEach(bala -> {
            bala.draw(g);
        });

        impactos.forEach(impacto -> {
            impacto.draw(g);
        });

        if(this.ataqueEspecial != null) {
            this.ataqueEspecial.draw(g);
        }

        if(this.bonusSecretoAgarrado) {
            g.setColor(Color.black);
            g.drawString("Has agarrado el bonus secreto. Felicitaciones!!", heroe.getX()-heroe.grafico.getWidth()/2-50+1, heroe.getY()-heroe.grafico.getHeight()-10+1);
            
            g.setColor(Color.white);
            g.drawString("Has agarrado el bonus secreto. Felicitaciones!!", heroe.getX()-heroe.grafico.getWidth()/2-50, heroe.getY()-heroe.grafico.getHeight()-10);

            TimerTask task = new TimerTask() {
                public void run() {
                    Mision.this.estado = ESTADO.GANA;
                }
            };

            Timer timer = new Timer();            
            timer.schedule(task, 3000);
        }

        String t = tiempoEnCurso/60 + ":" + tiempoEnCurso%60;

        g.setColor(Color.black);
        g.drawString(this.nombre, 11, pos+1);

        g.drawString("Tiempo restante: " + t, 11, pos+18);

        g.drawString("Energia: " + String.format("%.2f", Math.floor(heroe.getEnergia())), 11, pos+35);

        g.drawRect(11, pos+40, 200, 15);
        g.fillRect(11, pos+40, (int)heroe.getEnergia()*2, 15);
        
        g.drawString("Score: " + String.valueOf(heroe.PuntajeJugador()), (Juego1943.getInstance().getWidth()/2)-4, pos+1);
        
        g.setColor(Color.white);
        g.drawString(this.nombre, 10, pos);

        g.drawString("Tiempo restante: " + t, 10, pos+17);

        g.drawString("Energia: " + String.format("%.2f", Math.floor(heroe.getEnergia())), 10, pos+34);
    
        g.drawString("Score: " + String.valueOf(heroe.PuntajeJugador()), (Juego1943.getInstance().getWidth()/2)-5, pos);
    } 

    public static class MisionBuilder {
        private String nombre;
        private final AvionAmigo heroe;
        private final Enemigo[] enemigos;
        private final Enemigo jefe;
        
        private int tiempo;
        private DIFICULTAD dificultad;
        private boolean generarBonusSecreto;

        public MisionBuilder(AvionAmigo heroe, Enemigo[] enemigos, Enemigo jefe) {
            this.heroe = heroe;
            this.enemigos = enemigos;
            this.jefe = jefe;
            
            this.nombre = "Mision";
            this.tiempo = 60*2;
            this.dificultad = DIFICULTAD.FACIL;
            this.generarBonusSecreto = false;
        }

        public MisionBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public MisionBuilder setTiempo(int tiempo) {
            this.tiempo = tiempo;
            return this;
        }

        public MisionBuilder setDificultad(DIFICULTAD dificultad) {
            this.dificultad = dificultad;
            return this;
        }

        public MisionBuilder generarBonusSecreto(boolean generarBonusSecreto) {
            this.generarBonusSecreto = generarBonusSecreto;
            return this;
        }

        public Mision build(){
			return new Mision(this);
		}
    }
}