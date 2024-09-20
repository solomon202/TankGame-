package graphics;

import java.awt.image.BufferedImage;
//держит в себе более разрезаем  маленький кусок квадратик  анимации 4 маленькие танки 
public class SpriteSheet {
// сылка на обьект Буферизованный лист изображений
	private BufferedImage	sheet;
	//количество спрайтов;
	private int				spriteCount;
	//маштаб 
	private int				scale;
	//спрайты по ширине;
	private int				spritesInWidth;
//создаётся  в классе стена  и получае параметры  картинку.количество спрайтов .маштаб
	public SpriteSheet(BufferedImage sheet, int spriteCount, int scale) {
		this.sheet = sheet;
		this.spriteCount = spriteCount;
		this.scale = scale;
 //взять ширину обьщей картинки и разделить на размер одного танка 
		this.spritesInWidth = sheet.getWidth() / scale;

	}
 //математика вырезаем кусок по координатам 0 в обьекте картинка
	public BufferedImage getSprite(int index) {
  //остатка от деления 
		index = index % spriteCount;

		int x = index % spritesInWidth * scale;
		int y = index / spritesInWidth * scale;

		return sheet.getSubimage(x, y, scale, scale);

	}

}
