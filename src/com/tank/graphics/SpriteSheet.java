/**
 * @author Initial version made by https://github.com/TheByteGuru
 * @author Further developed by https://github.com/Wedas/
 */

package com.tank.graphics;

import java.awt.image.BufferedImage;
//Таблица спрайтов
public class SpriteSheet {
// этоткласс имеет Буферизованный лист изображений.количество спрайтов.маштаб.ширина полотнища.
	private BufferedImage	sheet;
	private int				spriteCount;
	private int				scale;
	private int				spritesInWidth;
 //получить параметры 
	public SpriteSheet(BufferedImage sheet, int spriteCount, int scale) {
		this.sheet = sheet;
		this.spriteCount = spriteCount;
		this.scale = scale;
		this.spritesInWidth = sheet.getWidth() / scale;
	}
//получить  из Sprite тип BufferedImage метод класса  SpriteSheet
	public BufferedImage getSprite(int index) {

		index = index % spriteCount;

		int x = index % spritesInWidth * scale;
		int y = index / spritesInWidth * scale;

		return sheet.getSubimage(x, y, scale, scale);

	}

}
