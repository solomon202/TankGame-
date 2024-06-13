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
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import game.Game;
import javax.swing.JFrame;

// Абстрактный класс похож на обычный класс. В абстрактном классе также можно определить поля и
//методы, но в то же время нельзя создать объект или экземпляр абстрактного класса
//класс экран обстрактный потому что единственный экран 
public  abstract class Display {
	//проверка окно создано или нет 
	private static boolean			created	= false;
	// рамка
	private static JFrame			window;
	//чистый лист 
	private static Canvas			content;
	//класс который содержит изображение чтобы ресовалась вся отдельная картинка а не по очереди компоненты 
	private static BufferedImage	buffer;
	// хранит лист байтов изображения 
	private static int[]			bufferData;
	private static Graphics			bufferGraphics;
	//цвет для отчистки листа 
	private static int				clearColor;
 
	//создать рамку  статический метод .размер  окна имя 
	public static void create(int width, int height, String title) {
      //если окно есть то все норм 
		if (created)
			return;
		
		//создания рамки и чистого листа 
		window = new JFrame(title);
		//закрыть программу при нажатии на крестик 
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content = new Canvas();
		//размер листа  Это что то типа константы, которую ты можешь назначить своему приложению и использовать для всех окон, 
		//и если тебе захочется все размеры всех форм в приложении изменить, просто меняешь в одном месте. 
		Dimension size = new Dimension(width, height);
		//метод листа размер 
		content.setPreferredSize(size);
		
		//методы рамки 
		
		//постоянный размер окна чтобы нельзя менять размер окна 
		window.setResizable(false);
		//добовляем нашь лист на рамку 
		window.getContentPane().add(content);
		//изменет размер окна точьно под нашь лист что бы небыло наслоений
		window.pack();
		//позицыя она на экране 
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		//наша картина в буфере всей графики выводится на лист
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);//статическое поле BufferedImage.TYPE_INT_ARGB
		//информация о нашей графике вытащить  и в ставить в нашу перемкнную лист байтов изображения 
		//вызвали  класс DataBufferInt расширения его датабуфер 
		bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
		//буфер графики туда передали наши картинки 
		bufferGraphics = buffer.getGraphics();
		//функция сглаживания 
		((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		clearColor = clearColor;
		//создалась дошла до этого метода 
		created = true;
}
	//очищяет лист 
	public static void clear() {
		Arrays.fill(bufferData, clearColor);

	}
  //возвращает графический обьект  для изменения с наружи нашей графики 
	public static Graphics2D getGraphics() {
		return (Graphics2D) bufferGraphics;
	}
//уничтожить окно 
	public static void destroy() {

		if (!created)
			return;

		window.dispose();

	}

	public static void setTitle(String title) {

		window.setTitle(title);

	}

	public static void addInputListener(Input inputListener) {
		window.add(inputListener);
	}
}

