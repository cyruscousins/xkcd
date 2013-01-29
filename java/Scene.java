import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;

public interface Scene{
	public void update(float time);
	public void render(Graphics g);
}
