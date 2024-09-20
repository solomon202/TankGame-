package game;
//класс игра 
import java.awt.Dimension;
import display.Display;
import java.awt.Graphics2D;

import IO.Input;
import display.Display;
import graphics.TextureAtlas;
import utils.Time;

public class Game implements Runnable{
	
	//создаем установить  данные переменных 
	public static final int		WIDTH			= 800;
	public static final int		HEIGHT			= 600;
	public static final String	TITLE			= "Tanks";
	public static final int		CLEAR_COLOR		= 0xff000000;
	public static final int		NUM_BUFFERS		= 3;

	public static final float	UPDATE_RATE		= 60.0f;
	public static final float	UPDATE_INTERVAL	= Time.SECOND / UPDATE_RATE;
	public static final long	IDLE_TIME		= 1;

	public static final String	ATLAS_FILE_NAME	= "texture_atlas.png";
	
   //создаем сылочные переменные 
	private boolean				running;
	private Thread				gameThread;
	private Graphics2D			graphics;
	private Input				input;
	private TextureAtlas		atlas;
	private Player				player;
	private Wall                wall;
	//и графику с помощью ссылки вставляем врамку 
   //формируется сама рамка в ней картинка 
	public Game() {
		
		running = false;
		//передаем в рамку параметры в обьект экран 
		Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
		//метод вызова для работы с графикой
		graphics = Display.getGraphics();
		// обьект управление 
		input = new Input();
		//добовляем средство вода в экран 
		Display.addInputListener(input);
		//адрес картинки в с работой картинок для вырезания 
		atlas = new TextureAtlas(ATLAS_FILE_NAME);
		//сам танк уже вырезаный 
	    //игрок разположение размер вырезаная картинка 
		player = new Player(300, 300, 2, 3, atlas);
		//координаты по x и y/  размер /скорость 
		wall = new Wall(350, 350, 2, 3, atlas);
		
	}
	//Этот метод запускает выполнение потока,

	public synchronized void start() {

		if (running)
			return;

		running = true;
		//создем поток 
		//Объекты, реализующие этот интерфейс, могут выполняться потоком
		gameThread = new Thread(this);
		
		//Здесь поток начнет выполняться.
		gameThread.start();

	}

	public synchronized void stop() {

		if (!running)
			return;

		running = false;

		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		cleanUp();

	}

	private void update() {
		player.update(input);
		wall.update(input);
	}
//выводит кадр 
	private void render() {
		Display.clear();
		player.render(graphics);
		wall.render(graphics);
		Display.swapBuffers();
	}
	//Объекты, реализующие этот интерфейс, могут выполняться потоком
//реализуем  метод интерфейса 
	//При этом класс Thread уже реализует интерфейс Runnable, но с пустой реализацией метода run().Так что при создании экземпляра Thread создается поток, который ничего не делает. Поэтому в потомке надо переопределить метод run(). 
	//В нем следует написать реализацию алгоритмов, которые должны выполняться в данном потоке. 
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
     // функция проверки 
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
}
