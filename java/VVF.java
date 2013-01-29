

//Scalar Valued Field





//partial derivative with respect to x

//partial derivative with respect to y

//Vector Valued Field
public abstract class VVF{
	public abstract float xvalue(float x, float y);
	public abstract float yvalue(float x, float y);
	public Vec2 value(Vec2 in, Vec2 out){
		out.x = xvalue(in.x, in.y);
		out.y = yvalue(in.x, in.y);
		return out;
	}
	
	public static final float lim = .1f, limScl = (1 / (lim * 2));
	
	
	public static VVF differenatite(SVF field, int samples, float oScale, float nScale, float lim){
		float limScl = (1 / (lim * 2));
		float inv = 1f / oScale;
		int s1 = samples + 1;
		float[] xdat = new float[s1 * s1];
		float[] ydat = new float[s1 * s1];
		for(int x = 0; x < samples; x++)
		for(int y = 0; y < samples; y++){
			//System.out.printf("x: %d, y: %d, i: %d/%d\n", x, y, x + y * s1, xdat.length);
			xdat[x + y * s1] = (float)((field.value(inv * x + lim, inv * y) - field.value(inv * x - lim, inv * y)) * limScl);
			ydat[x + y * s1] = (float)((field.value(inv * x, inv * y + lim) - field.value(inv * x, inv * y - lim)) * limScl);
		}
		return new MappedVVF(new MappedSVF(xdat, samples, nScale), new MappedSVF(ydat, samples, nScale));
	}
	
}
