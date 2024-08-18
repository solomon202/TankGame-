package main;

import game.Game;
//класс маин точька входа 


public class Main {
	
	public static void main(String[] args) {
//создаем игру 
		
		  //создании экземпляра объекта
		   //Конструктор — это специальный метод, который имеет имя, совпадающее с именем класса,
		   //и вызывается при создании экземпляра объекта 
		Game tanks = new Game();
		tanks.start();

}
}