package graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spriteheet {
	
	private BufferedImage spriteheet; //usar para carregar a imagem

	public Spriteheet(String path) {
		try {
			spriteheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSprite(int x, int y, int width, int height) { // metodo proprio que retorna uma nova imagem nas posições x y e tamanho
		return spriteheet.getSubimage(x, y, width, height); // eu pego o spriteheet que já é um bufferedimage e chamo o metodo que ele tem que retorna uma bufferedimagem tambem mas com as coordenadas que eu quero
	}// depois vou pro game e crio o objeto
}
