
package com.tank.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.JFrame;
import com.tank.IO.Input;
import com.tank.game.Game;
//обстрактный класс 
public abstract class Display {
   //создался нет 
   //дисплей имеет окно рамку буфер данные цвет 
	private static boolean			created	= false;
	private static JFrame			window;
	private static Canvas			content;

	private static BufferedImage	buffer;
	private static int[]			bufferData;
	private static Graphics			bufferGraphics;
	private static int				clearColor;

	private static BufferStrategy	bufferStrategy;
    //создать экран заданным параметрам 
	public static void create(int width, int height, String title, int _clearColor, int numBuffers) {
    
		if (created)
			return;
        //представляет собой окно с рамкой и строкой заголовка название танк 
		window = new JFrame(title);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //панель меню, которая связана с окном
		MenuBar menuBar = new MenuBar();
		Menu gameMenu = new Menu("Game");
		MenuItem newGameMenu = new MenuItem("New");
		//добавить событие прослушивателя действий обнуление
		newGameMenu.addActionListener((event) -> {
			Game.reset();
		});
		window.setMenuBar(menuBar);
		menuBar.add(gameMenu);
		gameMenu.add(newGameMenu);
        //Содержимое холста
		content = new Canvas();
        //размер 
		Dimension size = new Dimension(width, height);
		content.setPreferredSize(size);
        //окно и вызываем  его методы 
		window.setResizable(false);
		window.getContentPane().add(content);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
        //используется для обработки и манипулирования данными изображения
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
		bufferGraphics = buffer.getGraphics();
		((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		clearColor = _clearColor;
        
		content.createBufferStrategy(numBuffers);

		bufferStrategy = content.getBufferStrategy();

		created = true;

	}
      //создать метод для отчистки 
	public static void clear() {
		Arrays.fill(bufferData, clearColor);
	}
	
       //Двойная буферизация в Java нужна для создания «не мигающей» графики
	public static void swapBuffers() {
		Graphics g = bufferStrategy.getDrawGraphics();
		g.drawImage(buffer, 0, 0, null);
		bufferStrategy.show();
	}
//управлением цветами и размещением текста.  
	public static Graphics2D getGraphics() {
		return (Graphics2D) bufferGraphics;
	}
  //освобождает все ресурсы экрана
	public static void destroy() {

		if (!created)
			return;

		window.dispose();

	}
   //Установить заголовок
	public static void setTitle(String title) {

		window.setTitle(title);

	}
 //добавить компонент по которому кликают 
	public static void addInputListener(Input inputListener) {
		window.add(inputListener);
	}

}
