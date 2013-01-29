//Rotating image
import java.awt.Graphics;
import java.awt.image.*;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

//An exploding factoring number.
public class PFNum extends RotoObj{
	int num;
	float fontSize;
	Color color;
	boolean defPF; //definitiely a prime factor
	boolean rad;
	float explodeTime;
	float maxExplodeTime;
	public PFNum(World world, float x, float y, int num, boolean defPF, float fontSize, Color color, int[] imgInt, int width, int height, float explodeTime){
		super(world, x, y);
		this.num = num;
		this.defPF = defPF;
		this.fontSize = fontSize;
		this.color = color;
		this.imgInt = imgInt;
		this.width = width;
		this.height = height;
		this.explodeTime = maxExplodeTime = explodeTime;
		calcProperties();
		//this.life = 30;
	}
	public static PFNum genPFNum(World world, float x, float y, int num, boolean rad, float fontSize, Color color){
		if(color.equals(Color.WHITE)) color = Color.BLACK;
		
		//size?
		int width = (int)(fontSize * 1.0f * (numsInInt(num) + .1f));
		int height = (int)(1.2f * fontSize);
		int bottomGap = (int)(.1f * fontSize);
		int numStartX = (int)(1 + .05f * x);
		if(rad){
			numStartX += (int)(.2f * fontSize);
			width += (int)(.2f * fontSize);
			height += (int)(.1f * fontSize);
		}
		
		Font font = ResourceManager.xkcdFont.deriveFont(Font.BOLD, fontSize);
		int fWidth = (int)(fontSize / 10 + 1); //how wide to draw lines
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] imgInt = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setFont(font);
		//hint to use text antialiasing
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		for(int i = 0; i < imgInt.length; i++){
			imgInt[i] = 0x00FFFFFF;
		}
		//draw number
		g.setColor(color);
		g.drawString("" + num, 3 + numStartX, height - bottomGap);
		//draw radical
		if(rad){
			g.drawLine(numStartX / 6 + fWidth, height / 2, numStartX / 3, height - fWidth);
			g.drawLine(numStartX / 3, height - fWidth, numStartX, fWidth);
			g.drawLine(numStartX, fWidth, width - fWidth, fWidth);
		}
		
		g.dispose();
		PFNum pfNum = new PFNum(world, x, y, num, false, fontSize, color, imgInt, width, height, rad ? 30 : 2);
		pfNum.d2y = world.grav;
		
		return pfNum;
	}
	//need one of these arrays per thread.
	public static final int storeSize = 6;
	static int[] primeFactors = new int[storeSize];
	public void update(float time){
		super.update(time);
		explodeTime -= time;
		if(defPF){}
		if(explodeTime < 0){
			int pfCount = calcPrimeFactors(primeFactors, storeSize, num);
			if(pfCount == 1){
				PFNum r = genPFNum(world, x, y, num, true, fontSize * .8f, color);
				//TODO add circular rand velocity
				r.dx = dx + world.rand.nextFloat() * 1000 / (1 + mass);
				r.dy = dy + world.rand.nextFloat() * 1000 / (1 + mass);
				
				r.dheading = dheading + world.rand.nextFloat() * .5f - .25f;
				r.heading = heading;
				r.heading = r.dheading = 0; //test radicals
				world.objNew.add(r);
			}
			else for(int i = 0; i < pfCount; i++){
				PFNum r = genPFNum(world, x, y, primeFactors[i], false, fontSize * .8f, color);
				r.dx = dx + world.rand.nextFloat() * 1000 / (1 + mass);
				r.dy = dy + world.rand.nextFloat() * 1000 / (1 + mass);
				
				r.dheading = dheading + world.rand.nextFloat() * .5f - .25f;
				r.heading = heading;
				
				world.objNew.add(r);
			}
			remove();
		}
	}
	
	//TODO this belongs in math?
	public static int calcPrimeFactors(int[] store, int storeSize, int val) {
		int storeI = 0;
		for (int i = 2; i <= val / i; i++) {
			while (val % i == 0) {
				store[storeI++] = i;
				if(storeI == storeSize) return storeI;
				val /= i;
			}
		}
		if (val > 1) {
			store[storeI++] = val;
		}
		return storeI;
	}
	public static int numsInInt(int i){
		if(i >= 100) return 3;
		if(i >= 10) return 2;
		return 1;
	}
	public String toString(){
		return "PFNum " + num + ", Color: " + color;
	}
}
