/**
 * @author Initial version made by https://github.com/TheByteGuru
 * @author Further developed by https://github.com/Wedas/
 */

package com.tank.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
//графический объект
public class Sprite {

	//картинка со спрайта 
	private BufferedImage	image;
	 //она имеет  Лист со спрайтами.размер.
	public Sprite(SpriteSheet sheet, float scale) {
		this(sheet, scale, 0, true);
	}

	public Sprite(SpriteSheet sheet, float scale, int spriteNumber, boolean alpha) {
		
		image = sheet.getSprite(spriteNumber);
		
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		GraphicsConfiguration config = device.getDefaultConfiguration();
		BufferedImage buffy = config.createCompatibleImage((int) (image.getWidth() * scale), (int) (image.getHeight() * scale),
				Transparency.TRANSLUCENT);
		Graphics g = buffy.getGraphics();
		g.drawImage(image, 0, 0, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), null);
		this.image = buffy;
		g.dispose();
		
	
	 
	 if(alpha)
		for (int i = 0; i < image.getHeight(); i++)
			for (int j = 0; j < image.getWidth(); j++) {
				int pixel = image.getRGB(j, i);
				if ((pixel & 0x00FFFFFF) < 10)
					image.setRGB(j, i, (pixel & 0x00FFFFFF));
			}
			
	}

	public void render(Graphics2D g, float x, float y) {

		g.drawImage(image, (int) (x), (int) (y), null);

	}

	
}
