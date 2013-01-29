//Jarva code
import java.awt.Color;
import java.awt.Graphics;





public class Floater extends PhysicsObject{
	Color color;
	float life;
	public Floater(World world, float x, float y, float dx, float dy, float mass, float radius, Color color, float life){
		super(world, x, y, dx, dy, mass, radius, false);
		this.color = color;
		this.life = life;
		windSA = radius;
	}
	float windSA;
	public void update(float time){
		float wrx = world.wind.wind.xvalue(x, y) - dx;
		float wry = world.wind.wind.yvalue(x, y) - dy;
		dx += wrx * windSA * time / mass;
		dy += wry * windSA * time / mass;
		super.update(time);
		life -= time;
		if(life <= 0)
		remove();
	}
}
