//Jarva code
import java.awt.Color;
import java.awt.Graphics;


public class Bomb extends PhysicsObject{
	boolean hasGravity = true;
	float life = 10;
	int color;
	int blastRadius;
	public Bomb(World world, float x, float y, float dx, float dy, float mass, float radius, boolean hasGrav, int color, int blastRadius){
		super(world, x, y, dx, dy, mass, radius, hasGrav);
		this.color = color;
		this.blastRadius = blastRadius;
	}
	
	public void update(float time){
		super.update(time);
		life -= time;
		int xi = (int) x, yi = (int) y;
		int r2 = iradius * iradius;
		for(int x0 = - iradius; x0 <= iradius; x0++)
		for(int y0 = - iradius; y0 <= iradius; y0++){
			if(x0 * x0 + y0 * y0 < r2 && world.isGround(xi + x0, yi + y0)){
				explode(xi, yi);
				life = 0; x0 = iradius + 1; break;
			}
		}
		if(life <= 0){
			remove();
		}
	}
	public void innerExplode(int xi, int yi){
		System.out.println("inner explosion at (" + xi + ", " + yi + ")");
		RotoObj r = RotoObj.genRotoObj(world, xi, yi, blastRadius);
		if(r != null){
			r.d2y = 10f; //TODO gravity
			r.dx = dx;
			r.dy = dy;
			r.dheading = (world.rand.nextFloat() - .5f) * 2f;
			world.objNew.add(r);
		}
		blastRadius *= 2;
	}
	public void explode(int xi, int yi){
		System.out.println("explosion at (" + xi + ", " + yi + ")");
		
		
		int fragments = 4;
		for(int i = 0; i < fragments; i++){ //TODO write this fast!
			float theta = (float)(Math.PI * 2 * i / fragments);
			float ctheta = (float)(Math.cos(theta));
			float stheta = (float)(Math.sin(theta));
			int nx = (int)(xi + blastRadius * ctheta);
			int ny = (int)(yi + blastRadius * stheta);
			
			RotoObj r = RotoObj.genRotoObj(world, nx, ny, blastRadius);
			if(r == null) return;
			r.dx = ctheta * blastRadius;
			r.dy = ctheta * blastRadius;
			r.dheading = world.rand.nextFloat() * .5f - .25f;
			world.objNew.add(r);
		}
		
		
		int r2 = blastRadius * blastRadius;
		for(int x0 = - blastRadius; x0 <= blastRadius; x0++){
			for(int y0 = - blastRadius; y0 <= blastRadius; y0++){
				if(x0 * x0 + y0 * y0 < r2){
					if(world.getPixel(xi + x0, yi + y0) != 0x00ffffff){
						//TODO add particle
						world.setPixel(xi + x0, yi + y0, color);
					}
				}
			}
		}
		
		
	}
}
