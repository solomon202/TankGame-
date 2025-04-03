/**
 * @author https://github.com/Wedas/
 */

package com.tank.game;

import com.tank.level.Level;
import com.tank.graphics.TextureAtlas;
    //Вражеский зеленый танк
public class EnemyGreenTank extends com.tank.game.Enemy {
	// расположение на текстуре первое вырезание  
	private static final int	NORTH_X	= 0;
	private static final int	NORTH_Y	= 11;
	private static final float	SPEED	= 1.5f;
	private static final int	LIVES	= 2;
   //создаем зеленый танк уинего есть данные виде параметров в классе  в обьекте игра
	// х у рандомная позиция на экране 
	public EnemyGreenTank(float x, float y, float scale, TextureAtlas atlas, Level lvl) {
       //		статические данные 
		super(x, y, scale, SPEED, atlas, lvl, NORTH_X, NORTH_Y, LIVES);

	}

}
