package graphics;
//вся картинка 
import java.awt.image.BufferedImage;

import utils.ResourceLoader;
// класс текстуры 
public class TextureAtlas {
//	используется для обработки и манипулирования данными изображения.
	//Подкласс BufferedImage описывает Image с доступным буфером данных изображения. BufferedImage состоит из ColorModel и Raster данных изображения. 
	 BufferedImage	image;
// получает адрес ресурса картинки 
	public TextureAtlas(String imageName) {
		image = ResourceLoader.loadImage(imageName);
	}
 // резать в классе стена 
	// x y это пиксели.  верхний левый угол  в право  x  верхний левый в низ y .x0 x1 x2 
	// w высота h ширена картинки 
	
	 //Для хранения изображения мы создаем объект BufferedImage,в классе игрок 
	//вырезать картинку общюю и получить по х у и высоту ширену по экрану 
	//возвращаемый тип метода типа картинка .как  типа строка или цыфра или музыка.
	
//Ctrl + Alt + H показывает вам все места, где используется или вызывается переменная или метод.

	public  BufferedImage cut(int x, int y, int w, int h) {
		// Возвращает подизображение, определяемое указанной прямоугольной областью. 	
		//Возвращает фрагмент изображения, определенный указанной прямоугольной областью.
		return image.getSubimage(x, y, w, h);
	}

}
