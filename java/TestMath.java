
public class TestMath{
	public static void main(String[] args){
		/*
		float[][] rawData = new float[16][16];
		for(int x = 0; x < 16; x++)
		for(int y = 0; y < 16; y++){
			int dx = x - 8, dy = y - 8;
			rawData[x][y] = (float)(128 / (1 + Math.sqrt(dx * dx + dy * dy)));
		}
		MappedSVF svf = MappedSVF.translateToSVF(rawData, 16, 1);
		System.out.println("Map");
		svf.printMap();
		VVF der = VVF.differentiate(svf, 16, 1, 1);
		System.out.println("df/dx");
		((MappedSVF)der.x).printMap();
		System.out.println("df/dy");
		((MappedSVF)der.y).printMap();
		*/
		/*
		MappedSVF svf = MappedSVF.translateToSVF(new float[]{1, 2, 4, 2, 4, 9, 4, 9, 16}, 3, 1 / 3f);
		
		for(int i = 0; i < 36; i++){
			float x = i % 6 * .5f;
			float y = i / 6 * .5f;
			System.out.println("f(" + x + ", " + y + ") = " + svf.value(x, y));
		}
		
		System.out.println("\n");
		svf.printMap();
		*/
		
	}
}
