package graficos;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDHT = 240;
	private final int HEIGHT = 160;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	private Spriteheet sheet;
	private BufferedImage[] player;
	private int frames = 0;
	private int maxFrames = 20; // velocidade
	private int curAnimation = 0, maxAnimation = 2;
	
	
	public Game() {
		sheet = new Spriteheet("/spriteheet.png");
		player = new BufferedImage[2];
		player [0] = sheet.getSprite(0,0, 16, 16);
		player [1] = sheet.getSprite(16, 0, 16, 16);
		this.setPreferredSize(new Dimension(WIDHT*SCALE, HEIGHT*SCALE));
		initFrame();
		image  = new BufferedImage(WIDHT,HEIGHT,BufferedImage.TYPE_INT_RGB);
	}
	
	public void initFrame() {
		frame = new JFrame("NevesGames");
		frame.add(this); // o this � o canvas, o frame consegue pegar todas as propriedades 
		frame.setResizable(false); // eu n�o quero que o usu�rio consiga redimencionar a janela 
		frame.pack();//� um metodo que calcula certas dimens�es e mostrar
		frame.setLocationRelativeTo(null); // eu quero que a janela fique no centro 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // quando clicar para fechar, quero que de fato feche sem que o programa continue rodando
		frame.setVisible(true); // quando inicializar j� estar vis�vel
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String [] args) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		frames ++;
		
		if(frames >= maxFrames) {
			frames = 0;
			curAnimation ++;
			if(curAnimation >= maxAnimation) {
				curAnimation = 0;
			}
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy(); // � uma sequ�ncia de buffers que colocams na tela para otimizar a rendereiza��o
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(19,29,19));
		g.fillRect(0, 0, WIDHT, HEIGHT); // � um retangulo esse metodo
		g.setColor(Color.BLUE);
		g.fillRect(WIDHT/2, HEIGHT/2, 20, 20); //x , y , altura, largura
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.setColor(Color.white);
		g.drawString("Neves Game", 20, 20);
		
		/*Renderizar o jogo*/
		Graphics2D g2 = (Graphics2D) g; //casting eu to transformando uma vari�vel do tipo apenas Graphics g para graphics 2d na variavel g2
		// com o casting eu consigo ativar alguns m�todos para os graficos 2d
		//g2.rotate(Math.toRadians(0), 90+8, 90+8);// eu coloco o +8 para que o ponto central seja o meio do desenho, e 8 pq � a metade de 16 que � o tamanho da imagem, 16x16, assim ele pega o ponto central do desenho
		g2.drawImage(player[curAnimation],10, 50, 32, 32, null);
		//g.drawImage(player, 10, 30, 40, 40, null);
		
		
		g.dispose(); // metodo de otimiza��o que limpa dados que tem na imagem que n�o precisamos que foram utilizados antes
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDHT*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}

	@Override
	public void run() {
		
		long lastTime = System.nanoTime(); // ele pega o tempo atual do computador em nano segundo
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		while(isRunning) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick(); // atualize
				render(); // renderize
				frames ++;
				delta --;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		stop();
	}

}
