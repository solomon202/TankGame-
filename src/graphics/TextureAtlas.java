package graphics;

import java.awt.image.BufferedImage;

import utils.ResourceLoader;
//идем в ресурс где лежит картинка 
public class TextureAtlas {

	BufferedImage	image;
//ресурсы 
	public TextureAtlas(String imageName) {
		image = ResourceLoader.loadImage(imageName);
	}
 // резать в классе стена 
	public BufferedImage cut(int x, int y, int w, int h) {
		// и  возвращяет 
		return image.getSubimage(x, y, w, h);
	}

}
