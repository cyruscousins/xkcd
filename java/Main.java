//Main architecture/framework
public class Main{
	//static int Width = 800, Height = 600;
	static int Width = 1200, Height = 700;
	public static void main(String[] args){
		Main main = new Main(Width, Height);
		
		//ResourceManager.init();
		main.run();
	}
	int width, height;
	Controller controller;
	XFrame frame;
	World world;
	Scene scene;
	public Main(int width, int height){
		this.width = width;
		this.height = height;
		controller = new Controller();
		frame = new XFrame(width, height, controller);
		world = new World(frame, controller);
		frame.init();
		//must be called after frame.init()
		enterMenu();
	}
	public static final int MSPERTICK = 50; // 20 FPS
	public void run(){
		float sPerTick = MSPERTICK * .001f;
		long t0, t1;
		t0 = System.currentTimeMillis();
		int frames = 0, skips = 0;
		for(int i = 0; i < 6000; i++){
			scene.update(sPerTick);
			scene.render(frame.bufferG);
			frame.drawToScreen();
			t1 = System.currentTimeMillis();
			frames++;
			long t = t1 - t0;
			if(t < MSPERTICK){ //sleep
				try{
					Thread.sleep(MSPERTICK - t);
					} catch (Exception JEX){
					JEX.printStackTrace();
				}
			}
			else{
				skips++;
				if(frames > 100 && (((float) skips) / frames > .01f)){
					System.out.println("Warning: More than 1% of frames have been delayed.  Consider decreasing the frame time.");
				}
				try{
					Thread.sleep(1);
					} catch (Exception JEX){
					JEX.printStackTrace();
				}
				//System.out.println("Warning: speed too fast.");
			}
			t0 += MSPERTICK; //each frame, we add the desired wait time between frames.  If a frame is slow, the next frame will sleep less because of it until the machine has caught up.
		}
	}
	public void enterMenu(){
		scene = new Menu(this, 30);
	}
	public void enterIntro(){
		scene = new Intro(this);
	}
	public void enterWorld(){
		scene = world;
	}
	public void setScene(Scene scene){
		this.scene = scene;
	}
}
