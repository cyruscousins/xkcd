//Test the functionality of various mathematical functions


public class PlasmaTest{
	SVF[] f;
	int size;
	int sSize;
	public PlasmaTest(SVF[] f, int size, int sSize){
		this.f = f;
		this.size = size;
		this.sSize = sSize;
	}
	public void render(int[] frame){
		System.out.println(" ");
		float dScale = 4;
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				int f1 = (int)(f[0].value(x, y));
				if(f1 > 255 || f1 < 0) System.out.println("r out of bounds: " + f1);
				int f2 = (int)(f[1].value(x, y));
				if(f2 > 255 || f2 < 0) System.out.println("g out of bounds: " + f2);
				int f3 = (int)(f[2].value(x, y));
				if(f3 > 255 || f3 < 0) System.out.println("b out of bounds: " + f3);
				
				frame[x + y * sSize] = f1 << 16 | f2 << 8 | f3;
				frame[x + y * sSize + size] = f1 << 16;
				frame[x + y * sSize + size * sSize] = f2 << 8;
				frame[x + y * sSize + size + size * sSize] = f3;
			}
		}
	}
}
