//Jarva code
import java.awt.Color;
import java.awt.Graphics;



public class Particle extends PhysicsObject {
	float d2x, d2y;
	World world;
	int frameWidth, frameHeight;
	int color;
	float life;
	//#construct 0 World world, int x, int y, int dx, int dy, int d2x, int d2y, int color
	public Particle(World world, float x, float y, float dx, float dy, float d2x, float d2y, int color, float life){
		super(world, x, y, dx, dy, 0, 0, false);
		this.world = world;
		this.color = color;
		this.life = life;
	}
	
	public Particle randParticle(World world, int x, int y, float maxRand){
		return null;
	}
	public void update(float time){
		super.update(time);
		life -= time;
		if(life <= 0)
		remove();
	}
	
	public void render(Graphics g, int cx, int cy){
		int sx = (int) (x - cx);
		int sy = (int) (y - cy);
		
		if(sx > 0 && sy > 0 && sx < world.frame.width && sy < world.frame.height){
			world.frame.imgInt[sx + sy * world.frame.width] = color;
		}
		
	}
}
