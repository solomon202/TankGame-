package game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import IO.Input;

import graphics.SpriteSheet;
import graphics.TextureAtlas;



//описывает обьект стену 
public class BrickWall  {
	//private BufferedImage image;
	int x;
	int y;
	int h;
	int w;
//	TextureAtlas atlas;
	//private Graphics2D			graphics;
	
	

	
//получить через конструктор координаты на экране размер и адрес картинки 
//	public BrickWall(int x, int y) {
		//получает статические данные конкретное имя и координаты на экране 
//		super(EntityType.BrickWall,x, y);
			
//	}
	
   //обработка движени 
	
	
	public void update(Input input) {
	
		
	}
	 
	
   //сылается на экземпляр класса Graphics2D 
	public void render(Graphics2D g,TextureAtlas atlas) {
		//g.drawImage(atlas.cut(x, y, w, h),300,300,null);
		g.drawImage(atlas.cut(256,0,16,16),30,30,80,80,null);
	

	}


}

