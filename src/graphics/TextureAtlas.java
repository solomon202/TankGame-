package graphics;

import java.awt.image.BufferedImage;

import utils.ResourceLoader;
//идем в ресурс где лежит картинка 
public class TextureAtlas {

	BufferedImage	image;

	public TextureAtlas(String imageName) {
		image = ResourceLoader.loadImage(imageName);
	}

	public BufferedImage cut(int x, int y, int w, int h) {
		return image.getSubimage(x, y, w, h);
	}

}
