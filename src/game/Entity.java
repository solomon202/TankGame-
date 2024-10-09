package game;

import java.awt.Graphics2D;

import IO.Input;
//получить игрока и его новое положение 
//сущьность получает имя и положения игрока
public abstract class Entity {

	public final EntityType	type;

	protected float			x;
	protected float			y;
  //модификатор доступа, который разрешает наследование элементов, но закрывает внешний доступ к полям и методам, если класс определён в другом пакете.
	protected Entity(EntityType type, float x, float y) {
		//Внутри класса для вызова своего конструктора  используется this()
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public abstract void update(Input input);

	public abstract void render(Graphics2D g);

}
