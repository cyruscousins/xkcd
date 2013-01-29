

//Scalar Valued Field





//partial derivative with respect to x

//partial derivative with respect to y

//Vector Valued Field


//Perform differentiation live
public class DifferentiatingVVF extends VVF{
	SVF f;
	float lim, limScl;
	public DifferentiatingVVF(SVF f, float lim){
		this.f = f;
		setLim(lim);
	}
	
	public void setLim(float lim){
		this.lim = lim;
		this.limScl = 1 / ( 2 * lim );
	}
	
	public float xvalue(float x, float y){
		return limScl * (f.value(x + lim, y) - f.value(x - lim, y));
	}
	
	public float yvalue(float x, float y){
		return limScl * (f.value(x, y + lim) - f.value(x, y - lim));
	}
	
}
