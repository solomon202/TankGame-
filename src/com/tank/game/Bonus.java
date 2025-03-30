/**
 * @author https://github.com/Wedas/
 */
package com.tank.game;

import java.awt.image.BufferedImage;
import com.tank.graphics.TextureAtlas;

public enum Bonus {
	//определяем что конкретно вырезаем 
	PROTECTION(16 * com.tank.game.Entity.SPRITE_SCALE, 7 * com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE),
	FREEZE(17 * com.tank.game.Entity.SPRITE_SCALE, 7 * com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE),
	SHIELD(18 * com.tank.game.Entity.SPRITE_SCALE, 7 * com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE),
	STAR(19 * com.tank.game.Entity.SPRITE_SCALE, 7 * com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE),
	DETONATION(20 * com.tank.game.Entity.SPRITE_SCALE, 7 * com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE),
	LIFE(21 * com.tank.game.Entity.SPRITE_SCALE, 7 * com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE, com.tank.game.Entity.SPRITE_SCALE);
	

	private int x, y, h, w;

	Bonus(int x, int y, int h, int w) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	//вырезаем с помощью метода 
    //Тип метода в Java обозначает конкретный тип данных, которые метод возвращает после выполнения. 
	//Если метод не возвращает никакого значения, его возвращаемый тип  данных указывается как void
	//возвращяется обьектный тип 
	public BufferedImage texture(TextureAtlas atlas) {
		return atlas.cut(x, y, w, h);
	}
	//возвращяемый обьектный тип 
	public static  Bonus fromNumeric(int n){
		switch(n){
		case 0:
			return PROTECTION;
		case 1:
			return FREEZE;
		case 2:
			return SHIELD;
		case 3:
			return STAR;
		case 4:
			return DETONATION;			
		case 5:
			return LIFE;		
		default:
			return LIFE;
		}
	}
}
