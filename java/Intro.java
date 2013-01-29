import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.LinkedList;
//Interact with underlying shell
public class Intro implements Scene{
	Main main;
	Image[] introImg;
	float time;
	float tPP = .5f;
	public Intro(Main main){
		this.main = main;
		introImg = ResourceManager.getIntro();
	}
	public void update(float time){
		this.time += time;
		if(this.time > tPP * 3) main.enterWorld();
	}
	public void render(Graphics g){
		int imgInd = 0;
		if(time > tPP){
			imgInd = 1;
		}
		if(time > tPP * 2){
			imgInd = 2;
		}
		g.drawImage(introImg[imgInd], 0, 0, null);
	}
}
