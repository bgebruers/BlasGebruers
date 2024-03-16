import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class Mapa extends ObjetoGrafico {
    private BufferedImage aire = null, tierra = null, transicion = null;
	private String tierraImg, transicionImg;
	private int contador;

	private TimerTask task;
	private Timer timer;      

    public Mapa(String filenameAire, String filenameTierra) throws IOException {
		super("mapa", filenameAire, new Point(0, 0));
		timer = new Timer();  
        aire = ImageIO.read(getClass().getResource("imagenes/" + filenameAire));
		grafico = aire;
		contador = 1;

		this.tierraImg = filenameTierra;
        cargarImagen(false);
    }      

	private void cargarImagen(boolean transicionB) {
		task = new TimerTask() {
			public void run() {
				try {
					if(transicionB) {
						transicion =  ImageIO.read(getClass().getResource("imagenes/" + transicionImg));
					} else {
						tierra = ImageIO.read(getClass().getResource("imagenes/" + tierraImg));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		timer.schedule(task, 5000);
	}

	public int getWidth(){
		return grafico.getWidth();
	}

	public int getHeight(){
		return grafico.getHeight();
	}

	public void setPosition(int x, int y){
		this.posicion.x = x;
		this.posicion.y = y;
	}

	public void setTransicion(String filename) {
		this.transicionImg = filename;
		cargarImagen(true);
	}

	public void cambiarMapa() {
		if(grafico == aire) {
			if(transicion != null)
				grafico = transicion;
			else
				grafico = tierra;
		}
		else if(grafico == transicion)
			grafico = tierra;
	}

    @Override
    public void update() {
		Rectangle mp = new Rectangle(this.posicion.x-100, this.posicion.y+(-grafico.getHeight()+Juego1943.getInstance().getHeight()+75)*contador, 
			grafico.getWidth()+200, grafico.getHeight());
		
		Rectangle temp = ((Juego1943)Juego1943.getInstance()).getViewPort();
		Rectangle vp = new Rectangle((int)temp.getX(), (int)temp.getY()-50, (int)temp.getWidth(), (int)temp.getHeight());
		
		if(!mp.contains(vp)) {
			contador++;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(grafico, this.posicion.x, 
			this.posicion.y+(-grafico.getHeight()+Juego1943.getInstance().getHeight()+75)*contador, null);
	}

	public int getX(){
		return posicion.x;
	}

	public int getY(){
		return posicion.y;
	}
}
