package game;

import java.awt.Graphics2D;

import IO.Input;
//получить игрока и его новое положение 
//сущьность получает имя и положения игрока
public abstract class Entity {
//Вы можете сослаться на константы в перечислении выше, как это:
//	Level level = Level.HIGH
		
	public final EntityType	type;
   //название и место нахождения 
	protected float			x;
	protected float			y;
  //модификатор доступа, который разрешает наследование элементов, но закрывает внешний доступ к полям и методам, если класс определён в другом пакете.
	protected Entity(EntityType type, float x, float y) {
		//Внутри класса для вызова своего конструктора  используется this()
		this.type = type;
		this.x = x;
		this.y = y;
	}
//методы которые мы имплиминтируем в наследуемый класс
	public abstract void update(Input input);

	public abstract void render(Graphics2D g);

}
