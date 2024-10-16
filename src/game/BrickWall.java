package game;

import java.awt.Graphics2D;

import IO.Input;
import graphics.TextureAtlas;
//описывает обьект стену 
public class BrickWall extends Entity {
//получить через конструктор координаты на экране размер и адрес картинки 
	public BrickWall(int x, int y, int scale, TextureAtlas atlas) {
		//получает статические данные конкретное имя и координаты на экране 
		super(EntityType.BrickWall,x, y);
	}
//обработка движени 
	@Override
	public void update(Input input) {
		// TODO Auto-generated method stub
		
	}
//обрабработка картинки вырезаем 
	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
