//Main architecture/framework
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;

import java.awt.event.*;
public class Controller implements KeyListener{
	public static final int[][] KEYMAPS = new int[][]{
		{KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_A, KeyEvent.VK_O, KeyEvent.VK_E}, //Dvorak defualts
		{KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_A, KeyEvent.VK_O, KeyEvent.VK_D} //QWERTY defaults
	};
	public static final int LEFT = 0, UP = 1, RIGHT = 2, DOWN = 3, B1 = 4, B2 = 5, B3 = 6, KCount = 7;
	
	public int[] keyMap;
	
	/*
	public static final int[][] DEFAULT_KEY_MAPPINGS = new int[][]{
		new int[]{KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN},
		new int[]{KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S},
		new int[]{KeyEvent.VK_K, KeyEvent.VK_U, KeyEvent.VK_H, KeyEvent.VK_J},
		
	};
	//keyMappings follows [localplayer][key] indexing.
	public static int[][] keyMappings;
	*/
	public static final int KEYCOUNT = 128;
	public boolean[] keys;
	public Controller(){
		keys = new boolean[Controller.KEYCOUNT];
		keyMap = new int[KCount];
		System.arraycopy(KEYMAPS[0], 0, keyMap, 0, KCount);
	}
	//key events
	public void keyReset() {
		for(int i = 0; i < Controller.KEYCOUNT; i++){
			keys[i] = false;
		}
	}
	public void keyTyped(KeyEvent e)
	{
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key < 128){
			keys[key] = true;
		}
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode();
		if(key < 128){
			keys[key] = false;
		}
	}
}
