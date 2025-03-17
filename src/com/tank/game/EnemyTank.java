/**
 * @author https://github.com/Wedas/
 */

package com.tank.game;

import com.tank.level.Level;
import com.tank.graphics.TextureAtlas;

public class EnemyTank extends Enemy {
	private static final int	NORTH_X	= 8;
	private static final int	NORTH_Y	= 0;
	private static final float	SPEED	= 1.8f;
	private static final int	LIVES	= 0;
    //Вражеский танк 
	public EnemyTank(float x, float y, float scale, TextureAtlas atlas, Level lvl) {
		//передаем в конструктор  выше 
		super(x, y, scale, SPEED, atlas, lvl, NORTH_X, NORTH_Y, LIVES);

	}
 
}
