package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//картинка самая маленькая одного танка .
public class Sprite {
  //Лист со спрайтами обьект 
	private SpriteSheet	sheet;
	//маштаб переменная 
	private float		scale;
//получаем все это 
	public Sprite(SpriteSheet sheet, float scale) {
		this.scale = scale;
		this.sheet = sheet;
	}
//метод отображать  который вытаскивает и вырезает картинку 
	public void render(Graphics2D g, float x, float y) {
//Буферизованное изображение листа изображений. получаем спрайт /с помощью сылки получаем метод класса sprite sheet
		BufferedImage image = sheet.getSprite(0);
		//метод рисует изображение в определенном месте
		//изобрабражене . координаты .получение ширины изображения().высоты.умноженое на маштаб.
		g.drawImage(image, (int) (x), (int) (y), (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);

	}
}
