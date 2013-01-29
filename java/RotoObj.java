//Rotating image
import java.awt.Graphics;
public class RotoObj extends PhysicsObject{
	public static final int WHITE = 0x00ffffff;
	int bwidth, bheight;
	
	int[] imgInt;
	int width;
	int height;
	
	int xCen, yCen;
	float momentOfInertia;
	
	float d2x, d2y;
	float heading, dheading;
	
	float health; //decreases with collisions, until a fix.
	
	public RotoObj(World world, float x, float y, float dx, float dy, float mass, float radius, boolean hasGrav){
		super(world, x, y, dx, dy, mass, radius, hasGrav);
		imgInt = new int[16 * 16];
		for(int i = 0; i < imgInt.length; i++){
			imgInt[i] = WHITE;
		}
		for(int i = 0; i < 16; i++){ //make a vertical and diagonal line.
			imgInt[i] = 0;
			imgInt[i + i * 16] = 0;
		}
		this.width = this.height = 16;
		
		prepVars();
		dheading = .25f;
		System.out.println("New rotoimg at (" + x + ", " + y + ")");
		d2y = -.1f;
		calcProperties();
	}
	
	public RotoObj(World world, float x, float y, int[] img, int width, int height){
		super(world, x, y);
		setRadius(iSqrt((width * width + height * height) / 4)); //radius of a circle that contains everything
		this.imgInt = img;
		this.width = width;
		this.height = height;
		calcProperties();
		x -= xCen;
		y -= yCen;
		
		
		prepVars();
	}
	
	//For use by subclasses
	public RotoObj(World world, float x, float y){
		super(world, x, y);
		prepVars();
	}
	
	//TODO!!!
	public static int iSqrt(int x){
		return(int)(Math.sqrt(x));
	}
	
	//Generates a RotoObj centered around the given location.
	public static RotoObj genRotoObj(World world, int x, int y, int rad){
		System.out.println("genRotoObj at (" + x + ", " + y + ")");
		int diam = rad * 2 + 1;
		int dSqr = diam * diam;
		int[] img = new int[dSqr];
		for(int i = 0; i < dSqr; i++) img[i] = WHITE;
		int rSqr = rad * rad;
		int count = 0;
		
		//square code
		for(int dy = -rad; dy <= rad; dy++){
			int yPart = (dy + rad) * diam;
			for(int dx = -rad; dx <= rad; dx++){
				int iVal = world.getPixel(x + dx, y + dy);
				if(iVal != WHITE){
					count++;
					img[dx + rad + yPart] = iVal;
					world.setPixel(x + dx, y + dy, WHITE); //TODO clear pixel function.
				}
			}
		}
		
		if(count == 0) return null;
		RotoObj obj = new RotoObj(world, x, y, img, diam, diam);
		obj.d2y = 10;
		return obj;
	}
	/*
	//TODO new thread for this?
	public static final int fallMax = 200;
	public static int[] coordStoreX = new int[fallMax];
	public static int[] coordStoreY = new int[fallMax];
	public static int[] coordStackX = new int[fallMax];
	public static int[] coordStackY = new int[fallMax];
	public static RotoObj fallDown(World world, int x, int y){
		System.out.println("fall down at (" + x + ", " + y + ")");
		
		//manage a stacklike data structure to load coordinates into
		coordStoreX[0] = coordStackX[0] = x;
		coordStoreY[1] = coordStackY[1] = y;
		
		int storei = 0, stacki = 0;
		while(stacki >= 0){
			if(world.getPixelCoordStackX[stacki], coordStackY[stacki]) != WHITE){
				
			}
		}
		coords
		
	}
	*/
	
	//Prep some variables
	public void prepVars(){
		bwidth = world.frame.width;
		bheight = world.frame.height;
	}
	
	//calculate mass, center, and moment.
	public void calcProperties(){
		int count = 0;
		int xSum = 0;
		int ySum = 0;
		for(int yi = 0; yi < height; yi++){
			int yP = yi * width;
			for(int xi = 0; xi < width; xi++){
				int i = xi + yP;
				if(imgInt[i] != WHITE){ //if not white
					count++;
					xSum += xi;
					ySum += yi;
				}
			}
		}
		this.mass = count;
		this.health = mass / 4;
		if(mass == 0) System.out.println(this);
		/*if(mass > 0)*/ this.invMass = 1f / mass;
		this.xCen = xSum / count;
		this.yCen = ySum / count;
		//System.out.println("calcProps: xc " + xCen + ", yc " + yCen + ", mass" + mass);
		//TODO must iterate again for moment.
		float moment = 0;
	}
	public void fix(){
		System.out.println("Fixing at " + x + ", " + y);
		float cosHead = (float)Math.cos(heading);
		float sinHead = (float)Math.sin(heading);
		for(int yi = 0; yi < height; yi++){
			int yP = yi * width;
			for(int xi = 0; xi < width; xi++){
				int i = xi + yP;
				if(imgInt[i] != WHITE){ //if not white
					int xtrans = xi - xCen;
					int ytrans = yi - yCen;
					int nx = (int)(cosHead * xtrans - sinHead * ytrans);
					int ny = (int)(cosHead * ytrans + sinHead * xtrans);
					//world positions
					int wx = (int)x + nx;
					int wy = (int)y + ny;
					world.setPixel(wx, wy, imgInt[i]);
					//world.setPixel(wx, wy, 0xff0000);
				}
			}
		}
		remove();
	}
	
	public void update(float time){
		//System.out.println("Robj " + mass + " at (" + x + ", " + y + ")");
		//update velocity
		dx += d2x * time;
		dy += d2y * time;
		
		//update position
		x += dx * time;
		y += dy * time;
		
		super.update(time);
		heading += dheading;
		
		//check for collisions.  Fix upon a collision.
		
		float cosHead = (float)Math.cos(heading);
		float sinHead = (float)Math.sin(heading);
		for(int yi = 0; yi < height; yi++){
			int yP = yi * width;
			for(int xi = 0; xi < width; xi++){
				int i = xi + yP;
				if(imgInt[i] != WHITE){ //if not white
					int xtrans = xi - xCen;
					int ytrans = yi - yCen;
					int nx = (int)(cosHead * xtrans - sinHead * ytrans);
					int ny = (int)(cosHead * ytrans + sinHead * xtrans);
					//world positions
					int wx = (int)x + nx;
					int wy = (int)y + ny;
					if(world.getPixel(wx, wy) != WHITE){
						health-=1;
						if(health <=0){
							fix();
							//double break
							yi = height;
							break;
						}
						else{
							world.setPixel(wx, wy, WHITE);
						}
					}
				}
			}
		}
	}
	
	public void render(Graphics g, int cx, int cy){
		//rotate/render
		float cosHead = (float)Math.cos(heading);
		float sinHead = (float)Math.sin(heading);
		for(int yi = 0; yi < height; yi++){
			int yP = yi * width;
			for(int xi = 0; xi < width; xi++){
				int i = xi + yP;
				if(imgInt[i] < WHITE){ //if not white
					int xtrans = xi - xCen;
					int ytrans = yi - yCen;
					int nx = (int)(cosHead * xtrans - sinHead * ytrans);
					int ny = (int)(cosHead * ytrans + sinHead * xtrans);
					//buffer positions
					int bx = (int)x + nx - cx;
					int by = (int)y + ny - cy;
					if(bx > 0 && by > 0 && bx < bwidth && by < bheight){ //todo slight optimization is possible.
						world.frame.imgInt[bx + by * bwidth] = imgInt[i];
					}
				}
			}
		}
	}
}
