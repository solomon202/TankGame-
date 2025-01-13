

package com.tank.game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tank.IO.Input;
import com.tank.level.Level;
import com.tank.graphics.Sprite;
import com.tank.graphics.SpriteSheet;
import com.tank.graphics.TextureAtlas;
// Игрок
public class Player extends Entity {
    //время защиты 
	private static final int	PROTECTION_TIME	= 4000;
	//ВНЕШНИЙ ВИД  .  маштаб спрайта .192 по x
	private static final float	APPEARANCE_X	= Entity.SPRITE_SCALE * Game.SCALE * 4;
	private static final float	APPEARANCE_Y	= Entity.SPRITE_SCALE * Game.SCALE * 12;
   //движение колекция констант 
	public enum Heading {
		//движение по восток запад ключь NORTH_STRONG значение это отдельный спрайт 
		NORTH_SIMPLE(0 * SPRITE_SCALE, 0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), EAST_SIMPLE(
				6 * SPRITE_SCALE, 0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), SOUTH_SIMPLE(4 * SPRITE_SCALE,
						0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), WEST_SIMPLE(2 * SPRITE_SCALE,
								0 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),

		NORTH_MEDIUM(0 * SPRITE_SCALE, 2 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), EAST_MEDIUM(
				6 * SPRITE_SCALE, 2 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), SOUTH_MEDIUM(4 * SPRITE_SCALE,
						2 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), WEST_MEDIUM(2 * SPRITE_SCALE,
								2 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),

		NORTH_STRONG(0 * SPRITE_SCALE, 7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), EAST_STRONG(
				6 * SPRITE_SCALE, 7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), SOUTH_STRONG(4 * SPRITE_SCALE,
						7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE), WEST_STRONG(2 * SPRITE_SCALE,
								7 * SPRITE_SCALE, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),;
//изночальное положение  heading = Heading.NORTH_SIMPLE;
// при нажатии кнопки	heading = Heading.SOUTH_SIMPLE;
		// и далие эти координаты передаютсяя в конструктор для вырезания спрайта
		
		
		private int x, y, h, w;
      //передаем ключи в них координаты по которым вырезать спрайт 
		Heading(int x, int y, int h, int w) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
     //тип метод вызезания картинки 
		protected BufferedImage texture(TextureAtlas atlas) {
			return atlas.cut(x, y, w, h);
		}
	}

	private static int				lives;
	private static int				strength;
    
	private Heading					heading;
	private Map<Heading, Sprite>	spriteMap;
	private float					speed;
	private float					bulletSpeed;
	private Bullet					bullet;
	private boolean					isProtected;
	private List<Sprite>			protectionList;
        //получает маштаб скорость картинка уровень 
	public Player(float scale, float speed, TextureAtlas atlas, Level lvl) {
		//конкретную картинку расположение передаем  выше к конструктор класса 
		super(EntityType.Player, APPEARANCE_X, APPEARANCE_Y, scale, atlas, lvl);
        //начальное положение 
		heading = Heading.NORTH_SIMPLE;
		//ключь спрайт 
		spriteMap = new HashMap<Heading, Sprite>();
		
		this.speed = speed;
		bulletSpeed = 6;
		lives = 2;
		//прочность 
		strength = 1;
        
		isProtected = true;
		//добовляем обьект в лист 
		protectionList = new ArrayList<>();
		//ресуем картинку защиты нимб вокруг танка 
		//лист со спрайтами 
		//и конкретный спрайт 
    	protectionList.add(
	     		new Sprite(new SpriteSheet(atlas.cut(16 * SPRITE_SCALE, 9 * SPRITE_SCALE, SPRITE_SCALE, SPRITE_SCALE),
						SPRITES_PER_HEADING, SPRITE_SCALE), scale));
		protectionList.add(
				new Sprite(new SpriteSheet(atlas.cut(17 * SPRITE_SCALE, 9 * SPRITE_SCALE, SPRITE_SCALE, SPRITE_SCALE),
						SPRITES_PER_HEADING, SPRITE_SCALE), scale));
         
		
		//пробегаем по списку карты 
		//переменная pet типа Pet, в которой содержится объект класса Pet конструктор 
		for (Heading h: Heading.values()) {
			//и создаем страницу спрайта  передавая ссылку
		    SpriteSheet sheet = new SpriteSheet(
		    		h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
		    Sprite sprite = new Sprite(sheet, scale);
	    	spriteMap.put(h, sprite);
		}

	}
   
	@Override
	public void update(Input input) {
		
    //if  это  позволяет задать условие, в соответствии с которым дальнейшая часть программы может быть выполнена.		
		//Жив ли Орел
		if (!lvl.isEagleAlive())
			return;
         //сформировался ли сам шаблон танка 
		if (evolving)
			return;
      //   текущие время  и созданиеп и  время защиты 
		if (System.currentTimeMillis() > createdTime + EVOLVING_TIME + PROTECTION_TIME)
			//находится под защитой
			isProtected = false;
// класс сущьность это 32-битное число с плавающей точкой
		float newX = x;
		float newY = y;
//сначала определяем какая кнопка нажата 
		if (input.getKey(KeyEvent.VK_UP)) {
			//при нажатии отняли скорость реакции  и движение по и игрику происходит отнимание двигаясь к нулю 
			newY -= speed;
			//маштаб окна игры маштаб жизнейи табло цыфр
			x = newX = (Math.round(newX / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
			//по каким координатам вырезается спрайт прочность 
			heading = strength > 1 ? (strength > 2 ? Heading.NORTH_STRONG : Heading.NORTH_MEDIUM)
					: Heading.NORTH_SIMPLE;
		} else if (input.getKey(KeyEvent.VK_RIGHT)) {
			newX += speed;
			y = newY = (Math.round(newY / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
			heading = strength > 1 ? (strength > 2 ? Heading.EAST_STRONG : Heading.EAST_MEDIUM) : Heading.EAST_SIMPLE;
		} else if (input.getKey(KeyEvent.VK_DOWN)) {
			newY += speed;
			x = newX = (Math.round(newX / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
			heading = strength > 1 ? (strength > 2 ? Heading.SOUTH_STRONG : Heading.SOUTH_MEDIUM)
					: Heading.SOUTH_SIMPLE;
		} else if (input.getKey(KeyEvent.VK_LEFT)) {
			newX -= speed;
			y = newY = (Math.round(newY / Level.SCALED_TILE_SIZE)) * Level.SCALED_TILE_SIZE;
			heading = strength > 1 ? (strength > 2 ? Heading.WEST_STRONG : Heading.WEST_MEDIUM) : Heading.WEST_SIMPLE;

		}
       //если не равно нулю стоим на месте 
		if (newX < 0) {
			newX = 0;
			//новое положение двигаемся по  х
		} else if (newX >= Game.WIDTH - SPRITE_SCALE * scale) {
			newX = Game.WIDTH - SPRITE_SCALE * scale;
		}

		if (newY < 0) {
			newY = 0;
		} else if (newY >= Game.HEIGHT - SPRITE_SCALE * scale) {
			newY = Game.HEIGHT - SPRITE_SCALE * scale;
		}
       //переключатель (направление)ключь
		switch (heading) {
		case NORTH_SIMPLE:
		case NORTH_MEDIUM:
		case NORTH_STRONG:
			//Могу двигаться если нет пересечение с врагом проверка 
			if (canMove(newX, newY, newX + (SPRITE_SCALE * scale / 2), newY, newX + (SPRITE_SCALE * scale), newY)
					//пересекается с врагом
					&& !intersectsEnemy(newX, newY)) {
				x = newX;
				y = newY;
			}
			break;
		case SOUTH_SIMPLE:
		case SOUTH_MEDIUM:
		case SOUTH_STRONG:
			if (canMove(newX, newY + (SPRITE_SCALE * scale), newX + (SPRITE_SCALE * scale / 2),
					newY + (SPRITE_SCALE * scale), newX + (SPRITE_SCALE * scale), newY + (SPRITE_SCALE * scale))
					&& !intersectsEnemy(newX, newY)) {
				x = newX;
				y = newY;
			}
			break;
		case EAST_SIMPLE:
		case EAST_MEDIUM:
		case EAST_STRONG:
			if (canMove(newX + (SPRITE_SCALE * scale), newY, newX + (SPRITE_SCALE * scale),
					newY + (SPRITE_SCALE * scale / 2), newX + (SPRITE_SCALE * scale), newY + (SPRITE_SCALE * scale))
					&& !intersectsEnemy(newX, newY)) {
				x = newX;
				y = newY;
			}
			break;
		case WEST_SIMPLE:
		case WEST_MEDIUM:
		case WEST_STRONG:
			if (canMove(newX, newY, newX, newY + (SPRITE_SCALE * scale / 2), newX, newY + (SPRITE_SCALE * scale))
					&& !intersectsEnemy(newX, newY)) {
				x = newX;
				y = newY;
			}
			break;
		}
     //список пуль интерфейс list тип Bullet сылка bullets передать метод тип обьекта 
		//получить пулю конкретную тоесть врага 
		List<Bullet> bullets = Game.getBullets(EntityType.Enemy);
		//если она есть 
		if (bullets != null) {
			//то пробегаем по списку 
			for (Bullet enemyBullet : bullets) {
				//возвращает false без проверки второго операнда.
				//получаем прямоугольник .пересекает ли пуля его 
				if (getRectangle().intersects(enemyBullet.getRectangle()) && enemyBullet.isActive()) {
					if (!isProtected)
						isAlive = false;
					enemyBullet.setInactive();
				}

			}

		}
        //уравень да нет  прямоугольник танка и прямоугольник бонуса 
		
		if (lvl.hasBonus() && getRectangle().intersects(lvl.getBonusRectangle())) {
			//получение конкретного бонуса 
			Bonus bonus = lvl.getBonus();
			switch (bonus) {
			case PROTECTION:
				createdTime = System.currentTimeMillis();
				isProtected = true;
				break;
			case FREEZE:
				Game.freezeEnemies();
				break;
			case SHIELD:
				lvl.protectEagle();
				break;
			case STAR:
				upgrade();
				break;
			case DETONATION:
				Game.detonateEnemies();
				break;
			case LIFE:
				if (++lives > 9)
					lives = 9;
				break;
			}
			lvl.removeBonus();

		}
		//пуля 
		//если нажата кнопка 
		//сравниваем пуля вообще существует 
      //boolean выражение, результатом которого является true или false
		if (input.getKey(KeyEvent.VK_SPACE)) {
			//необходимо исполнить, в случае, если условие истинно
			if (bullet == null || !bullet.isActive()) {
				
				if (Game.getBullets(EntityType.Player).size() == 0) {
					//создаем пулю
					bullet = new Bullet(x, y, scale, bulletSpeed, heading.toString().substring(0, 4), atlas, lvl,
							EntityType.Player);
				}
			}
		}

	}
     //пересекается с врагом
	private boolean intersectsEnemy(float newX, float newY) {
		//список врагов 
		List<Enemy> enemyList = Game.getEnemies();
		//вызов квадрата по координатам  
		Rectangle2D.Float rect = getRectangle(newX, newY);
		for (Enemy enemy : enemyList) {
			//если пересекаются то да 
			if (rect.intersects(enemy.getRectangle()))
				return true;
		}
		return false;
	}

	@Override
	//отображает получаем сылку обьекта который ресует 
	public void render(Graphics2D g) {
		//да нет 
		if (evolving) {
			//формируем ресунок передавая параметры 
			drawEvolving(g);
			return;
		}
		//положения. картинка. размер картинки 
		spriteMap.get(heading).render(g, x, y);

		if (isProtected)
			drawProtection(g);

	}
//ресует защиту 
	private void drawProtection(Graphics2D g) {
		if (animationCount % 16 < 8)
			protectionList.get(0).render(g, x, y);
		else
			protectionList.get(1).render(g, x, y);
		animationCount++;

	}
 //ресуем взрыв 
	@Override
	public void drawExplosion(Graphics2D g) {
		super.drawExplosion(g);
		if (--lives >= 0)
			reset();
		else
			Game.setGameOver();
	}
//сброс
	public void reset() {
		this.x = APPEARANCE_X;
		this.y = APPEARANCE_Y;
		isAlive = true;
		evolving = true;
		isProtected = true;
		createdTime = System.currentTimeMillis();
		strength = 1;
		heading = Heading.NORTH_SIMPLE;

	}
   //жизни 
	public boolean hasMoreLives() {
		return lives >= 0;
	}
//жив 
	@Override
	public boolean isAlive() {
		return isAlive;
	}
//сила танка мощь
	private void upgrade() {
		if (++strength > 3)
			strength = 3;

		switch (heading) {
		case NORTH_SIMPLE:
			heading = Heading.NORTH_MEDIUM;
			break;
		case EAST_SIMPLE:
			heading = Heading.EAST_MEDIUM;
			break;
		case SOUTH_SIMPLE:
			heading = Heading.SOUTH_MEDIUM;
			break;
		case WEST_SIMPLE:
			heading = Heading.WEST_MEDIUM;
			break;

		case NORTH_MEDIUM:
			heading = Heading.NORTH_STRONG;
			break;
		case EAST_MEDIUM:
			heading = Heading.EAST_STRONG;
			break;
		case SOUTH_MEDIUM:
			heading = Heading.SOUTH_STRONG;
			break;
		case WEST_MEDIUM:
			heading = Heading.WEST_STRONG;
			break;

		case NORTH_STRONG:
		case EAST_STRONG:
		case SOUTH_STRONG:
		case WEST_STRONG:
		}

	}
//получить жизни игрока 
	public static int getPlayerLives() {
		return lives;
	}
	//получить силу игрока 
	public static int getPlayerStrength() {
		return strength;
	}
//переходите на Следующий Уровень
	public void moveOnNextLevel() {
		this.x = APPEARANCE_X;
		this.y = APPEARANCE_Y;
		evolving = true;
		isProtected = true;
		bullet = null;
		createdTime = System.currentTimeMillis();

		switch (heading) {
		case EAST_SIMPLE:
		case SOUTH_SIMPLE:
		case WEST_SIMPLE:
		case NORTH_SIMPLE:
			heading = Heading.NORTH_SIMPLE;
			break;

		case EAST_MEDIUM:
		case SOUTH_MEDIUM:
		case WEST_MEDIUM:
		case NORTH_MEDIUM:
			heading = Heading.NORTH_MEDIUM;
			break;

		case EAST_STRONG:
		case SOUTH_STRONG:
		case WEST_STRONG:
		case NORTH_STRONG:
			heading = Heading.NORTH_STRONG;
			break;
		}

	}
	

}
