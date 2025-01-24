/**
 * @author https://github.com/Wedas/
 */

package com.tank.game;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tank.level.Level;
import com.tank.level.TileType;
import com.tank.graphics.Sprite;
import com.tank.graphics.SpriteSheet;
import com.tank.graphics.TextureAtlas;
//пуля 
public class Bullet {
 //вырезаем по х 20 по у 6 точка . маштаб + ширена высота 
	public enum BulletHeading {
		B_NORTH(20 * com.tank.game.Player.SPRITE_SCALE, 6 * com.tank.game.Player.SPRITE_SCALE + 4, com.tank.game.Player.SPRITE_SCALE / 2,
				 1 * com.tank.game.Player.SPRITE_SCALE / 2), B_EAST(21 * com.tank.game.Player.SPRITE_SCALE + com.tank.game.Player.SPRITE_SCALE / 2,
						6 * com.tank.game.Player.SPRITE_SCALE + 4, com.tank.game.Player.SPRITE_SCALE / 2, 1 * com.tank.game.Player.SPRITE_SCALE / 2), B_SOUTH(
								21 * com.tank.game.Player.SPRITE_SCALE, 6 * com.tank.game.Player.SPRITE_SCALE + 4, com.tank.game.Player.SPRITE_SCALE / 2,
								1 * com.tank.game.Player.SPRITE_SCALE / 2), B_WEST(20 * com.tank.game.Player.SPRITE_SCALE + com.tank.game.Player.SPRITE_SCALE / 2,
										6 * com.tank.game.Player.SPRITE_SCALE + 4, com.tank.game.Player.SPRITE_SCALE / 2,
										1 * com.tank.game.Player.SPRITE_SCALE / 2);

		private int x, y, h, w;
 //вырезать картинки и загнать их в буфер и уже при конкретном нажатии достать нужную картинку 
		//инцилизируем 
		BulletHeading(int x, int y, int h, int w) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
//вырезаем 
		protected BufferedImage texture(TextureAtlas atlas) {
			return atlas.cut(x, y, w, h);
		}
	}
    //скорость пули определяем тип 
	private float						speed;
	//пуля и конкретный спрайт перечесление вырезаных картинок 
	private Map<BulletHeading, Sprite>	spriteMap;
	//сылка на тип 
	private BulletHeading				bulletHeading;
	//с плавующим размером  направление танка
	private float						x;
	private float						y;
	//маштаб 
	private float						scale;
	//активен 
	private boolean						isActive;
	//уровень 
	private Level						lvl;
	//Тип объекта
	private com.tank.game.EntityType type;
	//произвести взрыв 
	private boolean						explosionDone;
	//Список взрывоопасных предметов
	private List<Sprite>				explosionList;
	//количество анимаций
	private int							animationCount;
    //конструктор дает начальное состояние   создается в игрок или враг 
	//направление танка маштаб танка
	public Bullet(float x, float y, float scale, float speed, String direction, TextureAtlas atlas, Level lvl,
			com.tank.game.EntityType type) {
      //получить вырезаные картинки пули 
		spriteMap = new HashMap<BulletHeading, Sprite>();
		this.lvl = lvl;
		isActive = true;
		this.type = type;
		animationCount = 0;
		this.scale = scale;
		this.speed = speed;
		explosionDone = false;
		explosionList = new ArrayList<>();
		//вызов метода вырезаем взрыв в пустую в стенку края карты
		explosionList
				.add(new Sprite(
						new SpriteSheet(atlas.cut(16 * com.tank.game.Player.SPRITE_SCALE, 8 * com.tank.game.Player.SPRITE_SCALE,
								com.tank.game.Player.SPRITE_SCALE, com.tank.game.Player.SPRITE_SCALE), com.tank.game.Player.SPRITE_SCALE, com.tank.game.Player.SPRITE_SCALE),
						scale));
		explosionList
				.add(new Sprite(
						new SpriteSheet(atlas.cut(17 * com.tank.game.Player.SPRITE_SCALE, 8 * com.tank.game.Player.SPRITE_SCALE,
								com.tank.game.Player.SPRITE_SCALE, com.tank.game.Player.SPRITE_SCALE), com.tank.game.Player.SPRITE_SCALE, com.tank.game.Player.SPRITE_SCALE),
						scale));
		explosionList
				.add(new Sprite(
						new SpriteSheet(atlas.cut(18 * com.tank.game.Player.SPRITE_SCALE, 8 * com.tank.game.Player.SPRITE_SCALE,
								com.tank.game.Player.SPRITE_SCALE, com.tank.game.Player.SPRITE_SCALE), com.tank.game.Player.SPRITE_SCALE, com.tank.game.Player.SPRITE_SCALE),
						scale));
             //пробегаем по пулям  значениям 
		for (BulletHeading bh : BulletHeading.values()) {
			//вставляем  вырезаные картинки пуль 
			SpriteSheet sheet = new SpriteSheet(bh.texture(atlas), com.tank.game.Player.SPRITES_PER_HEADING, com.tank.game.Player.SPRITE_SCALE / 2);
			//вырезаная картинка маштаба 
			Sprite sprite = new Sprite(sheet, scale);
			//момещаем в карты картинки 
			spriteMap.put(bh, sprite);
		}
		//переключатель (направление)положение танка 
		switch (direction) {
		//на восток 
		case "EAST":
			//вызо пули на восто 
			bulletHeading = BulletHeading.B_EAST;
			//расположение пули по размерам танка 
			this.x = x + com.tank.game.Player.SPRITE_SCALE * scale/ 2;
			this.y = y + (com.tank.game.Player.SPRITE_SCALE * scale) / 4;
			break;
		case "NORT":
			bulletHeading = BulletHeading.B_NORTH;
			this.x = x + (com.tank.game.Player.SPRITE_SCALE * scale) / 4;
			this.y = y;
			break;
		case "WEST":
			bulletHeading = BulletHeading.B_WEST;
			this.x = x;
			this.y = y + (com.tank.game.Player.SPRITE_SCALE * scale) / 4;
			break;
		case "SOUT":
			bulletHeading = BulletHeading.B_SOUTH;
			this.x = x + (com.tank.game.Player.SPRITE_SCALE * scale) / 4;
			this.y = y + com.tank.game.Player.SPRITE_SCALE * scale/2;
			break;
		}
		com.tank.game.Game.registerBullet(type, this);

	}

	public void update() {

		if (!isActive)
			return;

		switch (bulletHeading) {
		case B_EAST:
			x += speed;
			if (!canFly(x + com.tank.game.Player.SPRITE_SCALE * scale / 4, y, x + com.tank.game.Player.SPRITE_SCALE * scale / 4,
					y + com.tank.game.Player.SPRITE_SCALE * scale / 4))
				isActive = false;
			break;
		case B_NORTH:
			y -= speed;
			if (!canFly(x, y, x + com.tank.game.Player.SPRITE_SCALE * scale / 4, y))
				isActive = false;
			break;
		case B_SOUTH:
			y += speed;
			if (!canFly(x, y + com.tank.game.Player.SPRITE_SCALE * scale / 4, x + com.tank.game.Player.SPRITE_SCALE * scale / 4,
					y + com.tank.game.Player.SPRITE_SCALE * scale / 4))
				isActive = false;
			break;
		case B_WEST:
			x -= speed;
			if (!canFly(x, y, x, y + com.tank.game.Player.SPRITE_SCALE * scale / 4))
				isActive = false;
			break;
		}

		if (type == com.tank.game.EntityType.Player) {
			List<Bullet> enemyBullets = com.tank.game.Game.getBullets(com.tank.game.EntityType.Enemy);
			for (Bullet bullet : enemyBullets)
				if (getRectangle().intersects(bullet.getRectangle())) {
					isActive = false;
					bullet.setInactive();
					bullet.disableExplosion();
					explosionDone = true;
				}

		}

		if (x < 0 || x >= com.tank.game.Game.WIDTH || y < 0 || y > com.tank.game.Game.HEIGHT) {
			isActive = false;
		}

	}

	public void render(Graphics2D g) {
		if (!isActive && explosionDone) {
			com.tank.game.Game.unregisterBullet(type, this);
			return;
		}
		if (!isActive)
			drawExplosion(g);

		if (isActive) {
			spriteMap.get(bulletHeading).render(g, x, y);
		}
	}

	private boolean canFly(float startX, float startY, float endX, float endY) {
		int tileStartX = (int) (startX / Level.SCALED_TILE_SIZE);
		int tileStartY = (int) (startY / Level.SCALED_TILE_SIZE);
		int tileEndX = (int) (endX / Level.SCALED_TILE_SIZE);
		int tileEndY = (int) (endY / Level.SCALED_TILE_SIZE);

		Integer[][] tileArray = lvl.getTileMap();

		if (Integer.max(tileStartY, tileEndY) >= tileArray.length
				|| Integer.max(tileStartX, tileEndX) >= tileArray[0].length || Integer.min(tileStartY, tileEndY) < 0
				|| Integer.min(tileStartX, tileEndX) < 0)
			return false;
		else if (isImpassableTile(tileArray[tileStartY][tileStartX], tileArray[tileEndY][tileEndX])) {

			if (isDestroyableTile(tileArray[tileStartY][tileStartX]))
				lvl.update(tileStartX, tileStartY);

			if (isDestroyableTile(tileArray[tileEndY][tileEndX]))
				lvl.update(tileEndX, tileEndY);

			return false;
		} else
			return true;
	}

	private boolean isDestroyableTile(int tileNum) {
		if (tileNum == TileType.BRICK.numeric() || tileNum == TileType.DOWN_LEFT_EAGLE.numeric()
				|| tileNum == TileType.DOWN_RIGHT_EAGLE.numeric() || tileNum == TileType.UP_LEFT_EAGLE.numeric()
				|| tileNum == TileType.UP_RIGHT_EAGLE.numeric()
				|| tileNum == TileType.METAL.numeric() && type == com.tank.game.EntityType.Player && com.tank.game.Player.getPlayerStrength() == 3) {
			return true;
		}

		return false;
	}

	private boolean isImpassableTile(Integer... tileNum) {
		for (int i = 0; i < tileNum.length; i++) {
			if (tileNum[i] == TileType.BRICK.numeric() || tileNum[i] == TileType.METAL.numeric()
					|| tileNum[i] == TileType.DOWN_LEFT_EAGLE.numeric()
					|| tileNum[i] == TileType.DOWN_RIGHT_EAGLE.numeric()
					|| tileNum[i] == TileType.UP_LEFT_EAGLE.numeric() || tileNum[i] == TileType.UP_RIGHT_EAGLE.numeric()
					|| tileNum[i] == TileType.DOWN_LEFT_DEAD_EAGLE.numeric()
					|| tileNum[i] == TileType.DOWN_RIGHT_DEAD_EAGLE.numeric()
					|| tileNum[i] == TileType.UP_LEFT_DEAD_EAGLE.numeric()
					|| tileNum[i] == TileType.UP_RIGHT_DEAD_EAGLE.numeric()) {
				return true;
			}
		}
		return false;
	}

	public boolean isActive() {
		return isActive;
	}

	public Rectangle2D.Float getRectangle() {
		return new Rectangle2D.Float(x, y, com.tank.game.Player.SPRITE_SCALE * scale / 2, com.tank.game.Player.SPRITE_SCALE * scale / 2);
	}

	public void setInactive() {
		isActive = false;
	}

	public void drawExplosion(Graphics2D g) {
		if (explosionDone)
			return;

		float adjustedX = x - com.tank.game.Player.SPRITE_SCALE * scale / 4;
		float adjustedY = y - com.tank.game.Player.SPRITE_SCALE * scale / 4;

		if (animationCount % 9 < 3)
			explosionList.get(0).render(g, adjustedX, adjustedY);
		else if (animationCount % 9 >= 3 && animationCount % 9 < 6)
			explosionList.get(1).render(g, adjustedX, adjustedY);
		else if (animationCount % 9 > 6)
			explosionList.get(2).render(g, adjustedX, adjustedY);
		animationCount++;

		if (animationCount > 12)
			explosionDone = true;

	}

	public void disableExplosion() {
		explosionDone = true;
	}

}
