

//Scalar Valued Field



public class MappedSVF3D extends SVF{
	float[] data; //contains border repeats.
	int length;
	int l1;
	float scale;
	
	float z, fz;
	int iz;
	
	public MappedSVF3D(float[] data, int length, float scale){
		this.data = data;
		this.length = length;
		l1 = length + 1;
		this.scale = scale;
	}
	public void setZ(float z){
		if(z < 0) z -= length * ((int)z / length - 1);
		this.z = z;
		iz = (int)z;
		fz = z - iz;
		if (iz >= length) iz %= length;
		iz *= l1 * l1;
	}
	public float value(float x, float y){
		return(xyzvalue(x, y, z));
	}
	private float xyzvalue(float x, float y, float z){
		x *= scale;
		y *= scale;
		
		if(x < 0) x -= length * ((int)x / length - 1);
		if(y < 0) y -= length * ((int)y / length - 1);
		
		int ix = (int) x;
		int iy = (int) y;
		
		
		float fx = x - ix;
		float fy = y - iy;
		
		if(ix >= length) ix %= length;
		if(iy >= length) iy %= length;
		
		//		System.out.println("x " + x + ", y " + y + ", ix " + ix + ", iy " + iy + ", fx " + fx + ", fy " + fy);
		//		System.out.println("Alen: " + data.length);
		
		iy *= l1; //sneaky.
		/*
		System.out.println(ix + iy);
		System.out.println(ix + 1 + iy);
		System.out.println(ix + iy + l1);
		System.out.println(ix + 1 + iy + l1);
		*/
		int ix2 = ix + 1;
		int iy2 = iy + l1;
		int iz2 = iz + l1 * l1;
		return 	(
		(data[ix + iy  + iz] * (1 - fx) + data[ix2 + iy  + iz] * fx) * (1 - fy) +
		(data[ix + iy2 + iz] * (1 - fx) + data[ix2 + iy2 + iz] * fx) * fy
		) * (1 - fz) + (
		(data[ix + iy  + iz2] * (1 - fx) + data[ix2 + iy  + iz2] * fx) * (1 - fy) +
		(data[ix + iy2 + iz2] * (1 - fx) + data[ix2 + iy2 + iz2] * fx) * fy
		) * (fz);
		
	}
	public static final java.text.DecimalFormat df = new java.text.DecimalFormat("000.00");
	public void printMap(){
		for(int y = 0; y < length; y++){
			String s = "";
			for(int x = 0; x < length; x++){
				s += df.format(value(x, y)) + "\t";
			}
			System.out.println(s);
		}
	}
	public void testPrintMap(){
		/*		for(int x = 0; x <= length; x++)
		for(int y = 0; y <= length; y++){
			System.out.println("f(" + x + ", " + y + ")" + data[x + l1 * y]);
		}
		*/
	}
	
	public void scale(float scale){
		for(int i = 0; i < data.length; i++){
			data[i] *= scale;
		}
	}
}
