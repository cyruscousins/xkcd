//Information and methods to control the world.
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;
import java.awt.Color;

//import java.awt.Runnable;

import java.awt.Image;
import java.awt.image.*;
import javax.imageio.ImageIO;

import java.awt.Font;

import java.net.*;
import java.io.*;

public class Map{
	//We can probably do better than storing as an image.
	//RLE on a per line per map basis?
	Image image;
	int[] imgInt;
	int x, y;
	public Map(int x, int y, Image image, int[] imgInt){
		if(image.getWidth(null) != ResourceManager.MAPWIDTH || image.getHeight(null) != ResourceManager.MAPHEIGHT){
			int i = 1 / 0;
		}
		this.x = x;
		this.y = y;
		this.image = image;
		this.imgInt = imgInt;
	}
	public void render(World world, Graphics g, int cx, int cy){
		//System.out.printf("Drawing map (%d, %d) at (%d, %d).\n", x, y, x * ResourceManager.MAPWIDTH - cx, y * ResourceManager.MAPHEIGHT - cy, null);
		//g.drawImage(image, 0, 0, null);
		int x0 = x * ResourceManager.MAPWIDTH - cx;
		int y0 = y * ResourceManager.MAPHEIGHT - cy;
		//see if the image is at all in bounds.
		if(x0 > world.sWidth || x0 + ResourceManager.MAPWIDTH < 0 || y0 > world.sHeight || y0 + ResourceManager.MAPWIDTH < 0) return;
		g.drawImage(image, x * ResourceManager.MAPWIDTH - cx, y * ResourceManager.MAPHEIGHT - cy, null);
	}
	public int getPixel(int x, int y){
		if(x + y * ResourceManager.MAPWIDTH > imgInt.length)
		System.out.println("WIDTH: " + image.getWidth(null) + "\nHEIGHT: " + image.getHeight(null) + "\nMAX: " + imgInt.length + "\nREQQ: " + x + "\nREQY: " + y + "\nREQI: " + (x + y * ResourceManager.MAPWIDTH));
		return imgInt[x + y * ResourceManager.MAPWIDTH];
	}
	public void setPixel(int x, int y, int val){
		//System.out.println("setting a pixel");
		imgInt[x + y * ResourceManager.MAPWIDTH] = val;
	}
}
