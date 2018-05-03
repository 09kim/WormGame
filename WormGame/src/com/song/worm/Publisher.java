package com.song.worm;

public interface Publisher {
	public void add(Observer ob);
	public void remove(Observer ob);
	public void notifyOb();
}
