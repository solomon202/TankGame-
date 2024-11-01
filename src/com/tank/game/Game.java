/**
 * @author Initial version made by https://github.com/TheByteGuru
 * @author Further developed by https://github.com/Wedas/
 */

package com.tank.game;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.tank.IO.Input;
import com.tank.display.Display;
import com.tank.level.Level;
import com.tank.graphics.TextureAtlas;
import com.tank.utils.Time;
import com.tank.utils.Utils;
//реализации интерфейса содержит основной метод run() — в нём и находится точка входа и логика исполняемого потока.
public class Game implements Runnable {
	//static вызываются напрямую через класс, а не через объект.
	//final защищает переменные, методы и классы от изменений.
	public static final int							WIDTH			= 624;
	public static final int							HEIGHT			= 624;
	public static final String						TITLE			= "Tanks";
	public static final int							CLEAR_COLOR		= 0xff000000;
	public static final int							NUM_BUFFERS		= 3;
	public static final float						UPDATE_RATE		= 60.0f;
	public static final float						UPDATE_INTERVAL	= Time.SECOND / UPDATE_RATE;
	public static final float						SCALE			= 3f;
	public static final long						IDLE_TIME		= 1;
	public static final String						ATLAS_FILE_NAME	= "texture_atlas.png";
	private static final float						PLAYER_SPEED	= 3f;

	private static final int						FREEZE_TIME		= 8000;
	//Реализует методы получения, удаления и вставки в начало, середину и конец списка.
	//ВРАГ.
	private static List<Enemy>						enemyList		= new LinkedList<>();
	//сцена
	private static int								stage			= 1;

	private static Map<EntityType, List<Bullet>>	bullets;
	//Объект graphics обычно представляет собой окно или холст, на котором можно рисовать. 
	private static Graphics2D						graphics;
	private static boolean							enemiesFrozen;
	private static long								freezeImposedTime;

	private boolean									running;
	private Thread									gameThread;
	private Input									input;
	private static TextureAtlas						atlas;
	private static Player							player;
	private static Level							lvl;
	private static int								enemyCount;
	private boolean									canCreateEnemy;
	private static boolean							gameOver;
	private BufferedImage							gameOverImage;
	private long									timeWin;

	public Game() {
		//пока поток стоит 
		running = false; 
		//настройки дисплея и создаёт изменяемое по размеру окно с указанными шириной и высотой
		//и плюс полоска с подсчетом.
		Display.create(WIDTH + 8 * Level.SCALED_TILE_SIZE, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
		//Создать объект типа Graphics:метод paintComponent() этого класса можно рисовать на объекте graphics.
		//Перед началом работы с графикой необходимо создать объект класса Graphics и передать его как аргумент методу paintComponent() класса,
		//вставляем наш дисплей на холст 
		graphics = Display.getGraphics();
		//ввод клава
		input = new Input();
		//добавить прослушиватель входных данных
		Display.addInputListener(input);
		//адрес текстуры  и работа непосредственно с картинками 
		atlas = new TextureAtlas(ATLAS_FILE_NAME);
		//пуля структура данных ключь значение 
		bullets = new HashMap<>();
		
		//вносим ключь имявраг и игрок  и пулю враг игрок
		bullets.put(EntityType.Player, new LinkedList<Bullet>());
		bullets.put(EntityType.Enemy, new LinkedList<Bullet>());
		//Уровень в него передаем картинку и сцена 
		lvl = new Level(atlas, stage);
		//игрок маштаб скорость картинка уровень 
		player = new Player(SCALE, PLAYER_SPEED, atlas, lvl);
		//враги замерли
		enemiesFrozen = false;
		//количество врагов
		enemyCount = 20;
		//время выйгрыша
		timeWin = 0;
		//проиграл нет 
		gameOver = false;
		//картинка пройгрыша 
		// так выглядит код int x = 2 * ((5 + 3) * 4 – 8) = 2 * (8 * 4 – 8) = 2 * (32 – 8) = 2 * 24 = 48;
		//получить цыфру по которой будут получены с помощью метода  пиксили 
		gameOverImage = Utils.resize(
				atlas.cut(36 * Level.TILE_SCALE, 23 * Level.TILE_SCALE, 4 * Level.TILE_SCALE, 2 * Level.TILE_SCALE),
				4 * Level.SCALED_TILE_SIZE, 2 * Level.SCALED_TILE_SIZE);
		//получить высоту изображения < чем высота изображения выйти из цыкла 
	    	for (int i = 0; i < gameOverImage.getHeight(); i++)
			for (int j = 0; j < gameOverImage.getWidth(); j++) {
				//получить все цвета из матрицы по ширене и длине 
			int pixel = gameOverImage.getRGB(j, i);
			//из комбинации R Е.Д., G Reen и B LUE цвета.Это дает 256 * 256 * 256 = 16777216 возможных цветов.
			if ((pixel & 0x00FFFFFF ) < 10)
					gameOverImage.setRGB(j, i, (pixel & 0x00FFFFFF ));
			}

	}
   //вызовы методов этого класса в блок synchronized. которые хотим синхранизировать 
	public synchronized void start() {
    //если поток да то выполняктся поток 
		//код для исполнения, если истина токод выполняется 
		if (running)
			return;

		running = true;
		gameThread = new Thread(this);
		gameThread.start();

	}

	public synchronized void stop() {
//если поток нет  то поток не выполняется 
		if (!running)
			return;

		running = false;

		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//переход в метод
		cleanUp();

	}
//обновление 
	private void update() {
		// задать условие, в соответствии с которым дальнейшая часть программы может быть выполнена
		//если размер списка 5 врагов не равен  = 0 и количество врагов не равно 0 выигрыш во времени не 0.
		if (enemyList.size() == 0 && enemyCount == 0 && timeWin == 0)
			//текущее время в миллисекундах определения времени выполнения операций,
			timeWin = System.currentTimeMillis();
       // размер врагов количество враго количество жизней не пройгрышь 
		if (enemyList.size() == 0 && enemyCount == 0 && player.hasMoreLives() && !gameOver)
			//то следующий уровень
			nextLevel();

		canCreateEnemy = true;
     //создать врага  
		//если размер листов врага не больше  4 и количество врагов больше нуля 
		if (enemyList.size() < 4 && enemyCount > 0) {
			//то создаем рандомную генератора случайных значений
			Random rand = new Random();
		// позицыя по х кразмер экрана  размер спрайта и количесто раз это сгенерировать 
			//случайное значение  умноженое на размер игры окна и размер спрайта игрока умноженую на маштаб и деленная на два это количество генерации
			//получаем вероятное число 
			float possibleX = rand.nextInt(3) * ((Game.WIDTH - Player.SPRITE_SCALE * Game.SCALE) / 2);
			
			Rectangle2D.Float recForX = new Rectangle2D.Float(possibleX, 0, Player.SPRITE_SCALE * Game.SCALE,
					Player.SPRITE_SCALE * Game.SCALE);
			for (Enemy enemy : enemyList) {
				if (enemy.isEvolving()) {
					canCreateEnemy = false;
					break;
				}

				if (canCreateEnemy)
					if (recForX.intersects(enemy.getRectangle())) {
						canCreateEnemy = false;
					}
			}
			if (canCreateEnemy) {
				if (player != null)
					if (recForX.intersects(player.getRectangle())) {
						canCreateEnemy = false;
					}
				if (canCreateEnemy) {
					Enemy enemy = null;
					enemyCount--;
					if (stage == 1) {
						if (enemyCount < 3)
							enemy = new EnemyInfantryVehicle(possibleX, 0, SCALE, atlas, lvl);
						else
							enemy = new EnemyTank(possibleX, 0, SCALE, atlas, lvl);
					} else {
						Random random = new Random();
						switch (random.nextInt(4)) {
						case 0:
							enemy = new EnemyInfantryVehicle(possibleX, 0, SCALE, atlas, lvl);
							break;
						case 1:
							enemy = new EnemyGreenTank(possibleX, 0, SCALE, atlas, lvl);
							break;
						default:
							enemy = new EnemyTank(possibleX, 0, SCALE, atlas, lvl);
						}
					}
					enemy.setPlayer(player);
					enemyList.add(enemy);
					;
				}
			}
		}

		List<Bullet> playerBulletList = getBullets(EntityType.Player);
		if (playerBulletList.size() > 0) {
			for (Enemy enemy : enemyList) {
				if (enemy.isEvolving())
					continue;
				if (enemy.getRectangle().intersects(playerBulletList.get(0).getRectangle())
						&& playerBulletList.get(0).isActive()) {
					enemy.fixHitting(Player.getPlayerStrength());
					playerBulletList.get(0).setInactive();
					if (!enemy.hasMoreLives())
						enemy.setDead();
				}
			}
		}

		if (enemiesFrozen) {
			if (System.currentTimeMillis() > freezeImposedTime + FREEZE_TIME)
				enemiesFrozen = false;
		} else {
			for (Enemy enemy : enemyList)
				enemy.update(input);
		}

		for (int i = 0; i < bullets.get(EntityType.Enemy).size(); i++)
			bullets.get(EntityType.Enemy).get(i).update();

		for (int i = 0; i < bullets.get(EntityType.Player).size(); i++)
			bullets.get(EntityType.Player).get(i).update();

		if (player != null && !player.hasMoreLives())
			player = null;

		if (player != null)
			player.update(input);

	}

	private void nextLevel() {

		if (timeWin == 0 || System.currentTimeMillis() < timeWin + 5000)
			return;

		bullets = new HashMap<>();
		bullets.put(EntityType.Player, new LinkedList<Bullet>());
		bullets.put(EntityType.Enemy, new LinkedList<Bullet>());
		if (++stage > 3)
			stage = 1;
		lvl = new Level(atlas, stage);
		enemiesFrozen = false;
		enemyCount = 20;
		enemyList = new LinkedList<>();
		player.moveOnNextLevel();
		timeWin = 0;

	}

	private void render() {

		Display.clear();

		
		lvl.render(graphics);

		
		if (player != null) {
			if (!player.isAlive()) {
				player.drawExplosion(graphics);
			} else
				player.render(graphics);
		}
		
		

		for (int i = 0; i < enemyList.size(); i++) {
			if (!enemyList.get(i).isAlive()) {
				enemyList.get(i).drawExplosion(graphics);
				enemyList.remove(i);
			}
		}
		
		

		for (Enemy enemy : enemyList)
			enemy.render(graphics);
		
		

		for (int i = 0; i < bullets.get(EntityType.Enemy).size(); i++)
			bullets.get(EntityType.Enemy).get(i).render(graphics);
		
		
		for (Bullet bullet : getBullets(EntityType.Player))
			bullet.render(graphics);
		
		

		lvl.renderGrass(graphics);
		
		

		if (gameOver) {
			graphics.drawImage(gameOverImage, Game.WIDTH / 2 - 2 * Level.SCALED_TILE_SIZE, Game.HEIGHT / 2, null);

		}
		Display.swapBuffers();

	}

	public void run() {

		int fps = 0;
		int upd = 0;
		int updl = 0;

		long count = 0;

		float delta = 0;

		long lastTime = Time.get();
		while (running) {
			long now = Time.get();
			long elapsedTime = now - lastTime;
			lastTime = now;

			count += elapsedTime;

			boolean render = false;
			delta += (elapsedTime / UPDATE_INTERVAL);

			while (delta > 1) {
				update();
				upd++;
				delta--;
				if (render) {
					updl++;
				} else {
					render = true;
				}
			}

			if (render) {
				render();
				fps++;
			} else {
				try {
					Thread.sleep(IDLE_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (count >= Time.SECOND) {
				Display.setTitle(TITLE + " || Fps: " + fps + " | Upd: " + upd + " | Updl: " + updl);
				upd = 0;
				fps = 0;
				updl = 0;
				count = 0;
			}

		}

	}

	private void cleanUp() {
		Display.destroy();
	}

	public static List<Enemy> getEnemies() {
		return enemyList;
	}

	public static void registerBullet(EntityType type, Bullet bullet) {
		bullets.get(type).add(bullet);
	}

	public static void unregisterBullet(EntityType type, Bullet bullet) {
		if (bullets.get(type).size() > 0) {
			bullets.get(type).remove(bullet);
		}
	}

	public static List<Bullet> getBullets(EntityType type) {
		return bullets.get(type);
	}

	public static void freezeEnemies() {
		enemiesFrozen = true;
		freezeImposedTime = System.currentTimeMillis();

	}

	public static void detonateEnemies() {
		for (Enemy enemy : enemyList)
			enemy.setDead();

	}

	public static int getEnemyCount() {
		return enemyCount;
	}

	public static void setGameOver() {
		gameOver = true;

	}

	public static void reset() {

		bullets = new HashMap<>();
		bullets.put(EntityType.Player, new LinkedList<Bullet>());
		bullets.put(EntityType.Enemy, new LinkedList<Bullet>());
		stage = 1;
		lvl = new Level(atlas, stage);
		enemiesFrozen = false;
		enemyCount = 20;
		enemyList = new LinkedList<>();
		player = new Player(SCALE, PLAYER_SPEED, atlas, lvl);
		gameOver = false;

	}

}
