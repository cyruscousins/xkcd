//Information and methods to control the world.
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;
import java.awt.Color;

//import java.awt.Runnable;
public class MapLoader implements Runnable{
	int x, y;
	public MapLoader(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void run(){
		int xStore = x + ResourceManager.HALFWORLDSIZE;
		int yStore = y + ResourceManager.HALFWORLDSIZE;
		Map map = ResourceManager.loadMap(x, y);
		//TODO Is this thread safe?
		ResourceManager.world[xStore][yStore] = map;
		ResourceManager.loadFlags[xStore][yStore] = false;
	}
}
