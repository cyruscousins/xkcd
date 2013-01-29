//Test the functionality of various mathematical functions

public class FunctionAnalyzer{
	SVF f;
	VVF fprime;
	public FunctionAnalyzer(SVF f, VVF fprime){
		this.f = f;
		this.fprime = fprime;
	}
	public void render(int[] frame, int size, int sSize){
		float dScale = 4;
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				int fval = (int)(f.value(x, y) / 2);
				if(fval > 255 || fval < 0) System.out.println("f out of bounds: " + fval);
				int fx = (int)(fprime.xvalue(x, y) * dScale + 128);
				if(fx > 255 || fx < 0) System.out.println("df/dx out of bounds: " + fx);
				int fy = (int)(fprime.yvalue(x, y) * dScale + 128);
				if(fy > 255 || fy < 0) System.out.println("df/dy out of bounds: " + fy);
				
				frame[x + y * sSize] = fval << 16 | fx << 8 | fy;
				frame[x + y * sSize + size] = fval << 16;
				frame[x + y * sSize + size * sSize] = fx << 8;
				frame[x + y * sSize + size + size * sSize] = fy;
			}
		}
	}
	
	
	public static final float[] SVFfloats = new float[]{
		0, 100,  60,  80, 120,  40,   0,
		20,  80,  50, 100,  40,  95,  20,
		45,  60, 120, 200,  50, 120,  45,
		100, 200, 250, 300, 150,  80, 100,
		60, 120,  80, 150,  90,  40,  60,
		80, 140,  70, 175,  70,  50,  80,
		0, 100,  60,  80, 120,  40,   0
	};
	public static SVF testMSVF(){
		MappedSVF m = new MappedSVF(SVFfloats, 6, 1f / 8f);
		//m.scale();
		return m;
	}
}
