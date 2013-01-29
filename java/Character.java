//Playable character Classes
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
public class Character extends PhysicsObject{
	public static final int UP = KeyEvent.VK_UP, DOWN = KeyEvent.VK_DOWN, LEFT = KeyEvent.VK_LEFT, RIGHT = KeyEvent.VK_RIGHT, FIRE = KeyEvent.VK_A, DROP = KeyEvent.VK_O, BRIDGE = KeyEvent.VK_E, BOMB = KeyEvent.VK_U;
	
	int direction = 0;
	
	float viscosity, dragConst;
	
	boolean onGround = false;
	float groundDer;
	
	World world;
	Controller control;
	public Character(World world, Controller control, float x, float y, float mass, float radius){
		super(world, x, y, 0, 0, mass, radius, true);
		this.x = x;
		this.y = y;
		this.world = world;
		this.control = control;
		dragConst = .85f;
	}
	
	public void update(float time){
		odx = dx;
		ody = dy;
		if(control.keys[LEFT]){
			dx -= time * 10;
			direction = -1;
		}
		if(control.keys[RIGHT]){
			dx += time * 10;
			direction = 1;
		}
		if(control.keys[UP]){
			control.keys[UP] = false;
			dy -= 20;
		}
		if(control.keys[DOWN]){
			dy += 10 * time;
		}
		dy += 10 * time;
		float linearDrag = (float)Math.pow(dragConst, time);
		dx *= linearDrag;
		dy *= linearDrag;
		x += dx;
		y += dy;
		
		doCollisions();
		
		doSpecials();
	}
	
	public void doCollisions(){
		if(world.isGround((int)x, (int)y)){
			onGround = true;
			groundDer = world.gdydx((int)x, (int)y);
			//calculate the normal
			float groundNY = -1 / groundDer;
			float groundNX = 1;
			if(groundDer == 0){
				groundNY = 1;
				groundNX = 0;
			}
			if(groundNY < 0){
				groundNX = -groundNX;
				groundNY = -groundNY;
			}
			
			//multiply speed by 1 - (normalized normal . normalized velocity).
			float scalar = 1f / (length(groundNX, groundNY) * length(dx, dy));
			//System.out.printf("%f %f %f %f %f\n", groundNX, groundNY, dx, dy, scalar);
			float spdSclr = (1 - scalar * (dx * groundNX + dy * groundNY)) * .5f;
			if(spdSclr > 1){
				spdSclr = 1;
				System.out.println("Error: spdSclr " + spdSclr);
			}
			
			//TODO gravity on an inclined plane madness (normal force);
			//System.out.println("Collision results in speed scalar of " + spdSclr);
			dx *= spdSclr;
			dy *= spdSclr;
			
			//todo ground ejector code
			y--; //pull out of the ground.
		}
		else onGround = false;
	}
	
	public void doSpecials(){
		if(control.keys[FIRE]){
			world.objNew.add(new ClearingOrb(world, x, y, dx + direction * 25, dy - 20, 1, 4, true, 0xffffff));
		}
		if(control.keys[DROP]){
			world.objNew.add(new ClearingOrb(world, x, y, dx, dy + 30, 1, 8, false, 0xffffff));
		}
		if(control.keys[BRIDGE]){
			world.objNew.add(new ClearingOrb(world, x, y, dx + direction * 100, dy - 25, 1, 2, true, 0x000000));
		}
		if(control.keys[BOMB]){
			control.keys[BOMB] = false;
			world.objNew.add(new Bomb(world, x, y, dx + direction * 100, dy - 25, 1, 2, true, 0xffffff, 25));
		}
	}
	
	float length(float x, float y){
		return (float)Math.sqrt(x * x + y * y);
	}
}
