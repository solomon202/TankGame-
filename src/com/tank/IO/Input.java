

package com.tank.IO;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
//клавиотура 
// и обработки событий, которая помогает реагировать на нажатия мыши, клавиши и другие события пользователя;
public class Input extends JComponent {
	//Перед началаом работы с графикой необходимо создать объект класса Graphics и передать его как аргумент методу paintComponent() класса,
	//вставляем наш дисплей на холст 
	
	//да нет 
	private boolean[]	map;

	public Input() {
    //создали от 0 до 256 
		map = new boolean[256];
   //пробегаем от начала до конца 
		for (int i = 0; i < map.length; i++) {
   //и каждой  i  присваеваем от 0 до 256.
			final int KEY_CODE = i;
     //ключь значение 
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i, 0, false), i * 2);
			getActionMap().put(i * 2, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					map[KEY_CODE] = true;
				}
			});
   
			getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(i, 0, true), i * 2 + 1);
			getActionMap().put(i * 2 + 1, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					map[KEY_CODE] = false;
				}
			});

		}

	}
   //копируем массив
	public boolean[] getMap() {
		return Arrays.copyOf(map, map.length);
	}
//обработка нажатий из класа игрок 
	public boolean getKey(int keyCode) {
		return map[keyCode];
	}

}
