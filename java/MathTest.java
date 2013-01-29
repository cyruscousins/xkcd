//Test the functionality of various mathematical functions
public class MathTest {
public static final int TEST = 2;
public static final int DER3 = 2;
public static final int FDER = 1;
public static final int PLASMA = 3;
	public static void main(String[] args){
		MathTest test = new MathTest(512);
		test.run();
	}
	XFrame frame;
	int size;
	FunctionAnalyzer f;
	MappedSVF3D f3;
	SVF func;
	VVF fder;
	
	PlasmaTest p;
	public MathTest(int size){
		this.size = size;
		switch (MathTest.TEST){
			case MathTest.FDER:
			func = Noise.noiseSVF(64, .5f, 0, 255);//FunctionAnalyzer.testMSVF();
			fder = new DifferentiatingVVF(func, 2);
			f = new FunctionAnalyzer(func, fder);
			break;
			case MathTest.DER3:
			f3 = Noise.noiseSVF3D(64, .25f, 0, 255);//FunctionAnalyzer.testMSVF();
			fder = new DifferentiatingVVF(f3, 2);
			f = new FunctionAnalyzer(f3, fder);
			break;
		}
		frame = new XFrame(size * 2, size * 2, null);
		
		float scale = .5f; float min = 0; float max = 255;
		//p = new PlasmaTest(new SVF[]{Noise.noiseSVF(size, scale, min, max), Noise.noiseSVF(size, scale, min, max), Noise.noiseSVF(size, scale, min, max)}), size, frame.width);
		p = new PlasmaTest(new SVF[]{Noise.noiseSVF(size, scale, min, max), Noise.noiseSVF(size, scale, min, max), Noise.noiseSVF(size, scale, min, max)}, size, frame.width);
	}
	public void run(){
		frame.init();
		switch (MathTest.TEST){
			case MathTest.FDER:
			f.render(frame.imgInt, size, frame.width);
			break;
			case MathTest.DER3:
			int z = 0;
			while(true){
				f3.setZ(f3.z + 1f);
				f.render(frame.imgInt, size, frame.width);
				try{
					Thread.sleep(50);
					} catch (Exception JEX){
					JEX.printStackTrace();
				}
				frame.drawToScreen();
			}
			//break;
			case MathTest.PLASMA:
			p.render(frame.imgInt);
			break;
		}
		frame.drawToScreen();
		frame.takeScreenShot("SS01");
		System.out.println("Draw complete");
	}
}
