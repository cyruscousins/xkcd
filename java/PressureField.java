//Classes that control the wind and such things.
import java.util.Random;


public class PressureField extends MappedSVF3D{
	World world;
	public PressureField(World world, int fieldSize, float intensity, float xyscale){
		super(Noise.genNoise3f(fieldSize, .85f, 15, 63, 1, 1, 0, intensity), fieldSize, xyscale);
		this.world = world;
	}
	public static final int MAX = 200;
	public static final int VSTEP = 50;
	public static final int HSTEP = 10;
	public static final int GSCALAR = 10000;
	public float value(float x, float y){
		int ix = (int) x;
		int iy = (int) y;
		int gHeight;
		int gdx = 0;
		for(gHeight = PressureField.VSTEP; gHeight < PressureField.MAX; gHeight += PressureField.VSTEP, gdx += PressureField.HSTEP){
			if((world.getPixel(ix + gdx, iy + gHeight) | world.getPixel(ix - gdx, iy + gHeight)) < 0x0f){ //black(ish)
				break;
			}
		}
		return super.value(x, y) + 1f * x + PressureField.GSCALAR / gHeight;
	}
}
