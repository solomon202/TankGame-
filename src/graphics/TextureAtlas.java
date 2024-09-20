package graphics;
//вся картинка 
import java.awt.image.BufferedImage;

import utils.ResourceLoader;
// класс текстуры 
public class TextureAtlas {
//	используется для обработки и манипулирования данными изображения.
	BufferedImage	image;
// получает адрес ресурса картинки 
	public TextureAtlas(String imageName) {
		image = ResourceLoader.loadImage(imageName);
	}
 // резать в классе стена 
	// x y это пиксели.  верхний левый угол  в право  x  верхний левый в низ y .x0 x1 x2 
	// w высота h ширена картинки 
	
	 //Для хранения изображения мы создаем объект BufferedImage,в классе экран
	//вырезать картинку общюю и получить по х у и высоту ширену по экрану 
	public BufferedImage cut(int x, int y, int w, int h) {
		// и 
		return image.getSubimage(x, y, w, h);
	}

}
