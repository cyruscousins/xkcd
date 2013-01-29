//Jarva code
import java.awt.Color;
import java.awt.Graphics;

public class ClearingOrb extends PhysicsObject{
	boolean hasGravity = true;
	float life = 10;
	int color;
	public ClearingOrb(World world, float x, float y, float dx, float dy, float mass, float radius, boolean hasGrav, int color){
		super(world, x, y, dx, dy, mass, radius, hasGrav);
		this.color = color;
	}
	
	public void update(float time){
		super.update(time);
		int xi = (int) x, yi = (int) y;
		int r2 = iradius * iradius;
		for(int x0 = - iradius; x0 <= iradius; x0++)
		for(int y0 = - iradius; y0 <= iradius; y0++){
			if(x0 * x0 + y0 * y0 < r2) world.setPixel(xi + x0, yi + y0, color);
		}
		life -= time;
		if(life <= 0)
		remove();
	}
}
