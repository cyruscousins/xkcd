//Playable character Classes
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
public class InterdimensionalOrb extends Character{
	public InterdimensionalOrb(World world, Controller control, float x, float y, float mass, float radius){
		super(world, control, x, y, mass, radius);
		
	}
	public void update(float time){
		super.update(time);
		super.doSpecials();
	}
	public void render(Graphics g, int cx, int cy){
		g.setColor(Color.RED);
		int xPos = (int)(x - cx);
		int yPos = (int)(y - cy);
		g.drawOval(xPos - 8, yPos - 8, 16, 16);
		
		
		g.drawString("dx: " + dx + ", dy " + dy + ", d2x " + (odx - dx) + ", d2y " + (ody - dy), xPos, yPos + 32);
		if(onGround) g.drawString("ground local derivative: " + groundDer, xPos, yPos + 32 + 16);
	}
}
