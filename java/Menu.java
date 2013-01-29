import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;

public class Menu implements Scene{
	Main main;
	Image bg;
	float time;
	
	Font font;
	Font fontBold;
	int g1;
	int topSpacing;
	int g2;
	int bottomSpacing;
	
	Controller controller;
	
	String[] titleStrings = new String[]{"The World of XKCD", "All images drawn by Randall Monroe", "All programming by Cyrus Cousins"};
	String[] menuStrings = new String[]{"/xkcd/Play", "/xkcd/Options", "/xkcd/Source", "/xkcd/Terminal"};
	int selectIndex;
	
	public Menu(Main main, int textSize){
		this.main = main;
		controller = main.controller;
		
		font = ResourceManager.xkcdFont.deriveFont(Font.PLAIN, textSize);
		
		g1 = 20 + textSize * 2 / 3;
		fontBold = ResourceManager.xkcdFont.deriveFont(Font.BOLD, textSize * 3 / 2);
		topSpacing = textSize * 2;
		g2 = g1 + topSpacing * titleStrings.length;
		bottomSpacing = textSize * 4 / 3;
		
		//hint to use text antialiasing on the menu
		((Graphics2D)main.frame.bufferG).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
	public void update(float time){
		this.time += time;
		if(controller.keys[controller.keyMap[controller.DOWN]]){
			if(++selectIndex > menuStrings.length) selectIndex = 0;
			controller.keys[controller.keyMap[controller.DOWN]] = false;
		}
		else if(controller.keys[controller.keyMap[controller.UP]]){
			if(--selectIndex < 0)  selectIndex = menuStrings.length - 1;
			controller.keys[controller.keyMap[controller.UP]] = false;
		}
		if(controller.keys[controller.keyMap[Controller.B1]]){
			switch(selectIndex){
				case 0:
				main.frame.bufferG.setFont(ResourceManager.xkcdFont.deriveFont(Font.BOLD, 12));
				main.enterIntro();
				break;
				case 1:
				break;
				case 3:
				main.setScene(new Terminal(main, this));
				break;
			}
		}
	}
	public void render(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, main.frame.width, main.frame.height);
		g.setColor(Color.BLACK);
		
		//g.drawImage(introImg, 0, 0, null);
		
		g.setFont(fontBold);
		for(int i = 0; i < titleStrings.length; i++){
			g.drawString(titleStrings[i], 20, g1 + topSpacing * i);
		}
		
		g.setFont(font);
		for(int i = 0; i < menuStrings.length; i++){
			if(i == selectIndex){
				g.setColor(Color.GRAY);
			}
			else{
				g.setColor(Color.BLACK);
			}
			g.drawString(menuStrings[i], 30, g2 + bottomSpacing * i);
		}
	}
}
