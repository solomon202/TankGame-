package display;
//есть экран в нем создаем классы рамку лист буфер для картинок и графический класс.
//далее уже используем ихнии методы 
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

import game.Game;
import javax.swing.JFrame;

import IO.Input;

// Абстрактный класс похож на обычный класс. В абстрактном классе также можно определить поля и
//методы, но в то же время нельзя создать объект или экземпляр абстрактного класса
//класс экран обстрактный потому что единственный экран 

public abstract class Display {

//экран что имееет и что умееет 
private static boolean			created	= false;
private static JFrame			window;
private static Canvas			content;

private static BufferedImage	buffer;
private static int[]			bufferData;
private static Graphics			bufferGraphics;
private static int				clearColor;

private static BufferStrategy	bufferStrategy;
//получаем параметры из класса игра  МЕТОД ФОРМИРУЕТ ЭКРАН
public static void create(int width, int height, String title, int _clearColor, int numBuffers) {

	if (created)
		return;
// получает название 
	window = new JFrame(title);
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	content = new Canvas();
//размер окна 
	Dimension size = new Dimension(width, height);
	content.setPreferredSize(size);

	window.setResizable(false);
	window.getContentPane().add(content);
	window.pack();
	window.setLocationRelativeTo(null);
	window.setVisible(true);
	
	//Суть двойной буферизации в том, что в оперативной памяти создается буфер – объект класса image или Bufferedimage, и вызывается его графический контекст, в котором формируется изображение. Там же происходит очистка буфера, которая тоже не отражается на экране. Только после выполнения всех действий готовое изображение выводится на экран.	
	
	
   //получает  размер и цвет 
	buffer = new BufferedImage(width, height,  BufferedImage.TYPE_INT_ARGB);
	bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
	bufferGraphics = buffer.getGraphics();
	((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	clearColor = _clearColor; 
	
	content.createBufferStrategy(numBuffers);
	bufferStrategy = content.getBufferStrategy();

	created = true;

}
//ОЧИСТИТЬ и заполняет переданный массив переданным значением.

//заливка цвета 
public static void clear() {
	Arrays.fill(bufferData, clearColor);
}
//Двойная буферизация является ничем иным, как техникой, которой предусматривается использование второго (внеэкранного) буфера для отрисовки фигур, спрайтов и так далее в него, с последующим копированием его содержания в экранный. Проблема в том, что при рисовании напрямую, т.е. рисование непосредственно в экранный буфер по времени не укладывается в промежуток времени перерисовки экрана (в Canvas это осуществляется функцией repaint()) и экран попросту начинает «мигать», т.е. пользователь видит перед собой промежуточный результат этого самого рисования. Использование этой самой технике позволяет разработчику избегать этих «миганий». Тем не менее, в Canvas использование этой техники является процессом велосипедостроения, т.к. разработчики стандарта и платформы J2ME не позаботились об этом.
public static void swapBuffers() {
	Graphics g = bufferStrategy.getDrawGraphics();
	//рисуем картинку
	g.drawImage(buffer, 0, 0, null);
	bufferStrategy.show();
}
//метод для работы   типа графики 
public static Graphics2D getGraphics() {
	return (Graphics2D) bufferGraphics;
}

public static void destroy() {

	if (!created)
		return;

	window.dispose();

}

public static void setTitle(String title) {

	window.setTitle(title);

}
//управление метод
public static void addInputListener(Input inputListener) {
	window.add(inputListener);
}
}