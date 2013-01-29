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
public class Terminal implements Scene{
	Main main;
	Scene parentScene;
	
	Scene parent;
	Controller controller;
	
	boolean shift;
	
	LinkedList<String> strings;
	String inputString = "";
	public static final int BUFFERSIZE = 512;
	int lineSize = 80;
	byte[] inBuffer = new byte[BUFFERSIZE];
	
	int fontSize = 12;
	int listSize = 55; //TODO use an array.
	Font font = ResourceManager.xkcdFont.deriveFont(Font.PLAIN, fontSize);
	
	ProcessBuilder p;
	Process process;
	InputStream inStream;
	InputStream errorStream;
	OutputStream outStream;
	
	public Terminal(Main main, Scene parent){
		this.main = main;
		this.parentScene = parent;
		this.controller = main.controller;
		strings = new LinkedList<String>();
		
		p = new ProcessBuilder();
		p.redirectErrorStream(true);
	}
	public void update(float time){
		if(process != null){
			//take in process output
			try{
				int bytes = inStream.available();
				while(bytes > 0){
					int bytesToRead = Math.min(bytes, BUFFERSIZE);
					inStream.read(inBuffer, 0, bytesToRead);
					bytes -= BUFFERSIZE;
					String nString = new String(inBuffer, 0, BUFFERSIZE, "ASCII");
					String[] nStrings = nString.split("\n");
					for(int i = 0; i < nStrings.length; i++){
						while(nStrings[i].length() > lineSize){
							String oString = nStrings[i].substring(0, lineSize);
							nStrings[i] = nStrings[i].substring(lineSize, nStrings[i].length());
							lineOut(oString);
						}
						if(nStrings[i].length() > 0) lineOut(nStrings[i]);
					}
				}
			} catch(Exception e){ e.printStackTrace(); }
		}
		
		//Shift
		if(controller.keys[KeyEvent.VK_SHIFT]) shift = true;
		else shift = false;
		
		//alphabetic
		for(int i = 0; i < 'z' - 'a'; i++){
			if(controller.keys[KeyEvent.VK_A + i]){
				controller.keys[KeyEvent.VK_A + i] = false;
				inputString += "" + (char)((shift ? 'A' : 'a') + i);
			}
		}
		
		//numerical
		for(int i = 0; i < '9' - '0'; i++){
			if(controller.keys[KeyEvent.VK_0 + i]){
				controller.keys[KeyEvent.VK_0 + i] = false;
				inputString += "" + (char)('0' + i);
			}
		}
		
		//dot
		if(controller.keys[KeyEvent.VK_PERIOD]){
			controller.keys[KeyEvent.VK_PERIOD] = false;
			inputString += "" + '.';
		}
		
		//slash
		if(controller.keys[KeyEvent.VK_SLASH]){
			controller.keys[KeyEvent.VK_SLASH] = false;
			inputString += "" + '/';
		}
		
		//Backslash
		if(controller.keys[KeyEvent.VK_BACK_SLASH]){
			controller.keys[KeyEvent.VK_BACK_SLASH] = false;
			inputString += "" + '\\';
		}
		
		//Backspace
		if(controller.keys[KeyEvent.VK_BACK_SPACE]){
			controller.keys[KeyEvent.VK_BACK_SPACE] = false;
			if(inputString.length() > 0){
				inputString = inputString.substring(0, inputString.length() - 1);
			}
		}
		if(controller.keys[KeyEvent.VK_ENTER]){
			controller.keys[KeyEvent.VK_ENTER] = false;
			lineOut(inputString);
			//start process
			startProcess(inputString);
			inputString = "";
		}
		else if(controller.keys[KeyEvent.VK_ESCAPE]){
			killProcess();
			main.setScene(parent);
		}
	}
	void startProcess(String pString){
		try{
			p.command(pString);
			process = p.start();
			
			inStream = process.getInputStream();
			outStream = process.getOutputStream();
			inStream = new BufferedInputStream(inStream);
		}
		catch(Exception e){
			
		}
	}
	void killProcess(){
		if(process != null){
			process.destroy();
			process = null;
		}
		if (inStream != null){
			try{
				inStream.close();
				} catch (Exception JEX){
				JEX.printStackTrace();
			}
		}
		if (outStream != null){
			try{
				outStream.flush();
				outStream.close();
				} catch (Exception JEX){
				JEX.printStackTrace();
			}
		}
	}
	void lineOut(String string){
		strings.add(string);
		if(strings.size() > listSize) strings.remove(0);
	}
	public void render(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, main.frame.width, main.frame.height);
		g.setColor(Color.BLACK);
		g.setFont(font);
		int i = 1;
		for(String s: strings){
			g.drawString(s, 10, fontSize * i);
			i++;
		}
		//g.drawString("XKCD:~", 10, main.frame.height - 10);
		g.drawString("XKCD:~" + inputString, 40, main.frame.height - 10);
	}
}
