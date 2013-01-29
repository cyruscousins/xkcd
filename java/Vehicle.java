//Playable character Classes
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Vehicle extends Character{
	Image image;
	float heading;
	float dheading;
	float wheading;
	float wdheading;
	public Vehicle(World world, Controller control, float x, float y, float mass, float radius){
		super(world, control, x, y, mass, radius);
	}
	public void setMass(float mass){
		this.mass = mass;
	}
	float windCoeff = .1f;
	float wheelRadius = 1;
	public static final float PI = (float)Math.PI;
	public void update(float time){
		float rdrag = .9f; //rotational drag
		float wdrag = .75f; //wheel drag
		heading += dheading;
		wheading += wdheading;
		//manual movement
		if(onGround){
			wdrag = .5f;
			System.out.println(wdheading);
			dx += -wdheading * wheelRadius * time;
			//we have the slope of the ground.  Use it to get a normal... multiply by gravity to scale it in the y direction ... and we have the new dx;  oh wait that formula is totally wrong.  TODO!
			if(groundDer != 0) dx += -world.grav * groundDer * time;
			
		}
		if(control.keys[LEFT]){
			wdheading += 2.5f * time;
		}
		if(control.keys[RIGHT]){
			wdheading -= 2.5f * time;
		}
		if(onGround && control.keys[UP]){
			dy -= 15;
		}
		
		//wind calculations.  Wind force is linear, and based on the relative velocity of wind
		float wrx = world.wind.wind.xvalue(x, y) - dx;
		float wry = world.wind.wind.yvalue(x, y) - dy;
		dx += wrx * windCoeff * time;
		dy += wry * windCoeff * time;
		
		dy += world.grav * time;
		
		//collisions
		super.doCollisions();
		
		//and apply the movement;
		x += dx;
		y += dy;
		
		//special moves
		super.doSpecials();
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
		
		public void render(Graphics g, int cx, int cy){
			g.setColor(Color.RED);
			int xPos = (int)(x - cx);
			int yPos = (int)(y - cy);
			//TODO image flipping
			g.setColor(Color.BLACK);
			g.fillOval(xPos - iradius, yPos - iradius * 2,  iradius * 2, iradius * 2);
			
			//g.drawOval(xPos - 8, yPos - 8, 16, 16);
			
			g.drawString("dx: " + dx + ", dy " + dy + ", d2x " + (odx - dx) + ", d2y " + (ody - dy), xPos, yPos + 32);
			if(onGround) g.drawString("ground local derivative: " + groundDer, xPos, yPos + 32 + 16);
		}
	}
