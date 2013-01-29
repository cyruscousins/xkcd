//Jarva code
import java.awt.Color;
import java.awt.Graphics;
public class PhysicsObject{
	boolean hasGravity;
	World world;
	float x, y;
	float dx, dy;
	float odx, ody;
	float mass, invMass, radius;
	int iradius;
	
	public PhysicsObject(World world, float x, float y, float dx, float dy, float mass, float radius, boolean hasGrav){
		this.world = world;
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.mass = mass;
		this.hasGravity = hasGrav;
		setRadius(radius);
	}
	//minimal constructor.  mass and radius should be set with setRadius and setMass.
	public PhysicsObject(World world, float x, float y){
		this.world = world;
		this.x = x;
		this.y = y;
	}
	public void setRadius(float radius){
		this.radius = radius;
		this.iradius = (int) radius + 1;
	}
	public void setMass(float mass){
		this.mass = mass;
		this.invMass = 1f / mass;
	}
	public void remove(){
		world.objTrash.add(this);
	}
	public void update(float time){
		if(hasGravity) dy += world.grav;
		x += dx * time;
		y += dy * time;
	}
	
	float getd2x(){
		return odx - dx;
	}
	float getd2y(){
		return ody - dy;
	}
	
	public void render(Graphics g, int cx, int cy){
		g.setColor(Color.BLACK);
		int sx = (int)(x - cx);
		int sy = (int)(y - cy);
		//System.out.println("Rendering at " + sx + ", " + sy);
		g.drawOval(sx - iradius, sy - iradius, iradius * 2 + 1, iradius * 2 + 1);
		//renderPhysInfo(g, sx, sy, true);
	}
	public void renderPhysInfo(Graphics g, int x, int y, boolean cartesian){
		for(int i = 0; i < PHYSSTRTYPES_LEN; i++){
			g.drawString(physStr(i, cartesian), x, y + (i + 1) * 12);
		}
	}
	
	Vec2D temp = new Vec2D();
	public static final int LOC = 0, VEL = 1, ACC = 2, PHYSSTRTYPES_LEN = 3;
	 public static final String[] physStrTypes = new String[]{new String("Location"), new String("Velocity"), new String("Acceleration")};
	//public static final String[] physStrTypes = new String[]{"","",""};
	//public static final int LOC = 0, VEL = 1, ACC = 2, PHYSSTRTYPES_LEN = 4;
	//returns a string with physics info.
	public String physStr(int stringType, boolean cartesian){
		String ret = physStrTypes[stringType];
		float x = 0, y = 0;
		switch (stringType){
			case LOC:
			x = this.x;
			y = this.y;
			break;
			case VEL:
			x = dx;
			y = dy;
			break;
			case ACC:
			x = getd2y();
			y = getd2x();
			break;
		}
		if(cartesian) return ret + ": (" + x + ", " + y + ")";
		//else radial
		temp.x = x;
		temp.y = y;
		return ret + ": Magnitude " + temp.length() + ", heading: " + temp.radians();
	}
}
