//Jarva code
import java.awt.Color;
import java.awt.Graphics;




public class WindParticle extends Particle{
	public WindParticle(World world, float x, float y, float dx, float dy, int color, float life){
		super(world, x, y, dx, dy, 0, 0, color, life);
		windCoeff = 1f;
	}
	public float windCoeff;
	public void update(float time){
		float wrx = world.wind.wind.xvalue(x, y) - dx;
		float wry = world.wind.wind.yvalue(x, y) - dy;
		dx += wrx * windCoeff * time;
		dy += wry * windCoeff * time;
		
		x += dx * time;
		y += dy * time;
		
		life -= time;
		if(life <= 0) remove();
	}
}
