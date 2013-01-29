//Classes that control the wind and such things.
import java.util.Random;

public class Wind{
	PressureField pressure;
	VVF wind;
	Random random = new Random();
	public Wind(PressureField pressure, int samples, float scale){
		this.pressure = pressure;
		wind = new DifferentiatingVVF(pressure, 1);//VVF.differentiate(pressure, samples, scale, scale);
	}
	//exclusive minimum velocity to render the wind.
	float vSqrMin = 4;
	float curTime;
	public void update(float time){
		pressure.setZ(pressure.z + time);
	}
	public int generateWind(int x0, int y0, int width, int height, Vec2 locs[], Vec2 vels[], int max){
		int count = 0;
		for(int i = 0; i < max; i++){
			float x = x0 + random.nextInt(width);
			float y = y0 + random.nextInt(height);
			
			locs[count].set(x, y);
			wind.value(locs[count], vels[count]);
			if(vels[count].lSquared() * random.nextFloat() > vSqrMin){
				count++; //use this value
			}
		}
		return count;
	}
}
