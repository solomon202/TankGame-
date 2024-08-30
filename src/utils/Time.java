package utils;

public class Time {
	//Но вот проблема. Метод paint вызывается только один раз. И как же его обновлять постоянно? Для этого существует очень полезная вещь — таймер. Давайте создадим его.

	public static final long	SECOND	= 1000000000l;

	public static long get() {
		return System.nanoTime();
	}

}
