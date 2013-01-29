//Playable character Classes
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.image.BufferedImage;
public class BalloonGuy extends Character{
	Image image;
	
	public BalloonGuy(World world, Controller control, float x, float y, float mass, float radius){
		super(world, control, x, y, mass, radius);
		image = ResourceManager.getImage("res/img/balloonguyalpha.png", BufferedImage.TYPE_INT_ARGB);
	}
	public void setMass(float mass){
		this.mass = mass;
	}
	float windCoeff = .1f;
	
	public void update(float time){
		//manual movement
		if(control.keys[LEFT]){
			dx -= time * 10;
			direction = -1;
		}
		if(control.keys[RIGHT]){
			dx += time * 10;
			direction = 1;
		}
		if(control.keys[UP]){
			dy -= 10 * time;
		}
		if(control.keys[DOWN]){
			dy += 10 * time;
		}
		
		//wind calculations.  Wind force is linear, and based on the relative velocity of wind
		float wrx = world.wind.wind.xvalue(x, y) - dx;
		float wry = world.wind.wind.yvalue(x, y) - dy;
		dx += wrx * windCoeff * time;
		dy += wry * windCoeff * time;
		
		//gravity is mostly balanced by bouyant force
		dy += 2.5f * time;
		
		//collisions
		super.doCollisions();
		
		//and apply the movement;
		x += dx;
		y += dy;
		
		//special moves
		super.doSpecials();
	}
	
	public void render(Graphics g, int cx, int cy){
		//g.setColor(Color.RED);
		int xPos = (int)(x - cx);
		int yPos = (int)(y - cy);
		//TODO image flipping
		g.drawImage(image, xPos - iradius, yPos - iradius, null);
		
		//g.drawOval(xPos - 8, yPos - 8, 16, 16);
		
		g.drawString("dx: " + dx + ", dy " + dy + ", d2x " + (odx - dx) + ", d2y " + (ody - dy), xPos + iradius, yPos + 32);
		if(onGround) g.drawString("ground local derivative: " + groundDer, xPos, yPos + 32 + 16);
	}
}
