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

public class ResourceManager{
public static final int MAPWIDTH = 2048;
public static final int MAPHEIGHT = 2048;
public static final int MAXWORLDSIZE = 129;
public static final int HALFWORLDSIZE = 64;
	//	public static List<int[]> loadingCPs = new ArrayList<>();
	public static Map[][] world = new Map[ResourceManager.MAXWORLDSIZE][ResourceManager.MAXWORLDSIZE];
	public static boolean[][] loadFlags = new boolean[ResourceManager.MAXWORLDSIZE][ResourceManager.MAXWORLDSIZE];
	
	public static Font xkcdFont = ResourceManager.loadFont("res/xkcd.ttf");
	//kill the group of maps surroundning x, y
	public static void clearZone(int x, int y){
		x += ResourceManager.HALFWORLDSIZE;
		y += ResourceManager.HALFWORLDSIZE;
		for(int i = - 2; i <= 2; i++){
			//TODO check bounds!
			world[x + i][y - 3] = null;
			world[x + i][y + 3] = null;
			world[x + 3][y + i] = null;
			world[x - 3][y + i] = null;
		}
	}
	
	public static Map getMap(int x, int y){
		int xStore = x + ResourceManager.HALFWORLDSIZE;
		int yStore = y + ResourceManager.HALFWORLDSIZE;
		if(xStore >= 0 && yStore >= 0 && xStore < ResourceManager.MAXWORLDSIZE && yStore < ResourceManager.MAXWORLDSIZE){
			if(world[xStore][yStore] == null){
				if(!loadFlags[xStore][yStore]){
					new Thread(new MapLoader(x, y)).start();
					loadFlags[xStore][yStore] = true;
					return null;
				}
			}
			return world[xStore][yStore];
		}
		else System.err.println("Out of bounds!");
		
		//We only reach this point at the edges of the map.
		BufferedImage img = null;
		if(y < 0){
			img = testImage(ResourceManager.MAPWIDTH, ResourceManager.MAPHEIGHT, BufferedImage.TYPE_INT_RGB);
		}
		else{
			img = testImage(ResourceManager.MAPWIDTH, ResourceManager.MAPHEIGHT, BufferedImage.TYPE_INT_RGB);
		}
		int[] imgInt = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		Map map = new Map(x, y, img, imgInt);
		return map;
	}
	public static String getMapImgStr(int x, int y){
		//resolve the saved filename
		String vDir = "s";
		String hDir = "e";
		int xName = x;
		int yName = y;
		if(xName <= 0){
			hDir = "w";
			xName = -xName + 1;
		}
		if(yName <= 0){
			vDir = "n";
			yName = -yName + 1;
		}
		
		//look for the file locally
		return "" + yName + vDir + xName + hDir + ".png";
	}
	public static Map loadMap(int x, int y){
		Image map = getMapImage(x, y);
		BufferedImage b = new BufferedImage(ResourceManager.MAPWIDTH, ResourceManager.MAPHEIGHT, BufferedImage.TYPE_INT_RGB);
		if (map == null){
			System.out.println("Error loading map.  Substituting blank image.");
			b.getGraphics().setColor(Color.WHITE);
			b.getGraphics().fillRect(0, 0, ResourceManager.MAPWIDTH, ResourceManager.MAPHEIGHT);
			b.getGraphics().setColor(Color.BLACK);
			b.getGraphics().fillOval(0, 0, ResourceManager.MAPWIDTH, ResourceManager.MAPHEIGHT);//temp, for testing.
		}
		else b.getGraphics().drawImage(map, 0, 0, null);
		int[] imgInt = ((DataBufferInt)b.getRaster().getDataBuffer()).getData();
		return new Map(x, y, b, imgInt);
	}
	public static Image getMapImage(int x, int y){
		String imgStr = getMapImgStr(x, y);
		System.out.println("Loading (" + x + ", " + x + ").  \"" + imgStr + "\"");
		Image image = getImage("res/img/" + imgStr);
		if(image != null) return image;
		
		//we don't have it, grab it from xkcd.com
		try {
			//int j = 1 / 0;
			System.out.println("Reading from the web.");
			String urlStr = "http://imgs.xkcd.com/clickdrag/" + imgStr;
			URL url = new URL(urlStr);
			System.out.println(urlStr);
			long t0 = System.currentTimeMillis();
			URLConnection con = url.openConnection(); // open the url connection.
			System.out.println(System.currentTimeMillis() - t0 + "ms to open connection.");
			con.setReadTimeout(5000);
			con.setConnectTimeout(100);
			
			DataInputStream dis = new DataInputStream(con.getInputStream()); // get a data stream from the url connection.
			System.out.println(System.currentTimeMillis() - t0 + "ms to get length.");
			byte[] fileData = new byte[con.getContentLength()]; // determine how many byes the file size is and make array big enough to hold the data
			for (int i = 0; i < fileData.length; i++) { // fill byte array with bytes from the data input stream
				fileData[i] = dis.readByte();
			}
			dis.close(); // close the data input stream
			FileOutputStream fos = new FileOutputStream(new File("res/img/" + imgStr));  //create an object representing the file we want to save
			fos.write(fileData);  // write out the file we want to save.
			fos.close(); // close the output stream writer
			System.out.println("SUCCESS loading " + urlStr);
		}
		catch(MalformedURLException m) {
			System.out.println(m);
		}
		catch(java.net.SocketTimeoutException a){
			System.out.println("Cannot get resource");
			System.err.println(a);
		}
		catch(IOException io) {
			System.out.println("Cannot get resource");
			System.err.println(io);
		}
		catch(Exception e){
		}
		
		image =  getImage("res/img/" + imgStr);
		if(image == null){
			System.err.println("ERROR: Image not found locally or remotely.");
		}
		return image;
	}
	public static Image getImage(String imgStr){
		try{
			return ImageIO.read(new java.io.File(imgStr));
			} catch(Exception e){
			return null;
		}
	}
	public static BufferedImage getImage(String imgStr, int bimgtype){
		BufferedImage b = null;
		Image img = getImage(imgStr);
		if (img == null){
			b = new BufferedImage(128, 128, bimgtype);
			System.out.println("Error loading image \"" + imgStr + "\".  Substituting blank image.");
			b.getGraphics().setColor(Color.WHITE);
			b.getGraphics().fillRect(0, 0, 128, 128);
			b.getGraphics().setColor(Color.BLACK);
			b.getGraphics().fillOval(0, 0, 128, 128); //temp, for testing.
		}
		else{
			b = new BufferedImage(img.getWidth(null), img.getHeight(null), bimgtype);
			b.getGraphics().drawImage(img, 0, 0, null);
		}
		return b;
	}
	public static BufferedImage testImage(int x, int y, int bimgtype){
		BufferedImage b = new BufferedImage(x, y, bimgtype);
		Graphics g = b.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, x, y);
		g.setColor(Color.BLACK);
		g.fillOval(0, 0, x, y);//test oval
		return b;
	}
	public static Font loadFont(String name) {
		Font font = null;
		String fName = "/fonts/" + name;
		try {
			InputStream is = new FileInputStream(name); //ResourceManager.class.getResourceAsStream(fName);
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(fName + " not loaded.  Using serif font.");
			font = new Font("serif", Font.PLAIN, 24);
		}
		return font;
	}
	//This function is an example of how to not ever write code.
	public static Image[] getIntro(){
		Image image = getImage("res/img/click_and_drag.png");
		BufferedImage[] images = new BufferedImage[3];
		int[] xs = new int[]{39, 210, 392, 569}; //corner at 42
		int y1 = 210;
		for(int i = 0; i < images.length; i++){
			int width = xs[i + 1] - xs[i];
			images[i] = new BufferedImage(width, y1 + 6, BufferedImage.TYPE_INT_RGB);
			images[i].getGraphics().drawImage(image,
			0, 3, width, y1 + 3, //destination
			xs[i], 0, xs[i + 1], y1, //source
			null);
		}
		return images;
	}
}
