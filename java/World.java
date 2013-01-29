//Information and methods to control the world.
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;
import java.awt.Color;
public class World implements Scene{
	Random rand = new Random();
	XFrame frame;
	
	float grav = 10;
	//camera center
	int cx, cy;
	//screen size
	int sWidth, sHeight;
	//handle to keys
	Controller controller;
	
	Character player;
	
	//Objects
	ArrayList<PhysicsObject> objects = new ArrayList<PhysicsObject>();
	ArrayList<PhysicsObject> objTrash = new ArrayList<PhysicsObject>();
	ArrayList<PhysicsObject> objNew = new ArrayList<PhysicsObject>();
	
	Vec2[] a, b;
	Wind wind;
	public static final int WINDAMT = 8;
	public static final int PSCL = 128;
	public World(XFrame frame, Controller controller){
		this.frame = frame;
		sWidth = frame.width;
		sHeight = frame.height;
		
		PressureField noise = new PressureField(this, 128, 255 * World.PSCL, 1f / 32f);
		wind = new Wind(noise, 5, .002f);
		
		a = new Vec2[World.WINDAMT];
		b = new Vec2[World.WINDAMT];
		for(int i = 0; i < World.WINDAMT; i++){
			a[i] = new Vec2();
			b[i] = new Vec2();
		}
		//player = new Vehicle(this, controller, 600, 1200, 10, 8);
		//player = new HatGuy(this, controller, 600, 1200, 10, 8);
		player = new BalloonGuy(this, controller, 600, 1200, 10, 8);
		//new InterdimensionalOrb(this, controller, x0 * 2048 + 800, y0 * 2048, 10, 8);
		
		//TODO put dropdown code.
		
		this.controller = controller;
	}
	int[] mapTrans = new int[] {0, 0,   0, 1,   1, 0,   1, 1};
	boolean[] temp = new boolean[4];
	public void update(float time){
		//update the player
		player.update(time);
		cx = (int) (player.x) - sWidth / 2;
		cy = (int) (player.y) - sHeight / 2;
		
		//update other objects
		for(PhysicsObject obj : objects){
			obj.update(time);
		}
		for(PhysicsObject obj : objTrash){
			objects.remove(obj);
		}
		objTrash.clear();
		
		for(PhysicsObject obj : objNew){
			objects.add(obj);
		}
		objNew.clear();
		
		//make new wind particles
		int count = wind.generateWind(cx, cy, sWidth, sHeight, a, b, World.WINDAMT);
		for(int i = 0; i < count; i++){
			WindParticle particle = new WindParticle(this, a[i].x, a[i].y, b[i].x, b[i].y, 0x000000, 4);
			objects.add(particle);
		}
		
		//TEST ROTOOBJ
		//if(rand.nextFloat() < .1f) objects.add(new RotoObj(this, cx + rand.nextInt(sWidth), cy + rand.nextInt(sHeight), rand.nextFloat() - .5f, rand.nextFloat() - .5f, 1, 8, true));
		
		//floater = new Floater(this, cx + rand.nextInt(sWidth), cy + rand.nextInt(sHeight), 0, 0, 1 + rand.nextInt(4), 1 + rand.nextInt(8), new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), 10);
		///objects.add(floater);
		
		//update the environment
		wind.update(time);
		
	}
	public int mapXIndex(int x){
		if(x < 0) return (x + 1) / ResourceManager.MAPWIDTH - 1; //MUST add 1 from x or we have an awful off by one error.
		else return x / ResourceManager.MAPWIDTH;
	}
	public int mapYIndex(int y){
		if(y < 0) return (y + 1) / ResourceManager.MAPHEIGHT - 1;  //MUST add 1 from x or we have an awful off by one error.
		else return y / ResourceManager.MAPHEIGHT;
	}
	
	public void render(Graphics g){
		//System.out.printf("Rendering, camera at (%d, %d).\n", cx, cy);
		int icx = (int)cx;
		int icy = (int)cy;
		
		int mx = mapXIndex(icx);
		int my = mapYIndex(icy);
		//render maps
		for(int i = 0; i < 4; i++){
			Map map = ResourceManager.getMap(mx + mapTrans[i * 2], my + mapTrans[i * 2 + 1]);
			if(map == null){
				//put map in load queue if not already there.
				//System.out.println("Render MAPNULL: (" + mx + ", " + my + ")!");
			}
			else{
				//render the map
				//System.out.println("Rendering (" + (mx + mapTrans[i * 2]) + ", " + (my + mapTrans[i * 2 + 1]) + ")!");
				map.render(this, g, icx, icy);
			}
		}
		//signal that its ok to delete maps
		ResourceManager.clearZone(mx, my);
		
		//#define PTEST
		for(PhysicsObject obj : objects){
			obj.render(g, icx, icy);
		}
		player.render(g, icx, icy);
		
	}
	public boolean isGround(int x, int y){
		return (getPixel(x, y) & 0xff) < 0x0f;
	}
	public int getPixel(int x, int y){
		int mx = mapXIndex(x);
		int my = mapYIndex(y);
		
		Map map = ResourceManager.getMap(mx, my);
		
		try{
			
			if(map == null){
				//System.out.println("getPixel MAPNULL: (" + mx + ", " + my + ")!");
				return 0;
			}
			else return map.getPixel(x - (mx * ResourceManager.MAPWIDTH), y - (my * ResourceManager.MAPHEIGHT));
			
		}
		catch(Exception e){
			System.out.println("Failure at mx = " + mx + ", my =  " + my);
			System.exit(0);
			return 0;
		}
		
	}
	//calculates the local slope at a point.
	public float gdydx(int x, int y){
		return (nearestVGround(16, x - 1, y) - nearestVGround(16, x + 1, y)) * .5f;
	}
	//returns the change in height to the nearest solid pixel.  Stops after lim pixels.
	int nearestVGround(int lim, int x, int y){
		int dy = 0;
		if(isGround(x, y)){
			//underground, go up.
			for(dy = 0; dy < lim; dy++){
				if(!isGround(x, y - dy)) break;
			}
			dy = -dy;
		}
		else{
			for(dy = 0; dy < lim; dy++){
				if(!isGround(x, y + dy)) break;
			}
		}
		return dy;
	}
	public void setPixel(int x, int y, int val){
		int mx = mapXIndex(x);
		int my = mapYIndex(y);
		
		Map map = ResourceManager.getMap(mx, my);
		if(map == null){
			//System.out.println("setPixel MAPNULL: (" + mx + ", " + my + ")!");
		}
		else map.setPixel(x - (mx * ResourceManager.MAPWIDTH), y - (my * ResourceManager.MAPHEIGHT), val);
	}
}
