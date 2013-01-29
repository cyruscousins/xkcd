

//Scalar Valued Field





//partial derivative with respect to x

//partial derivative with respect to y
public class DYDFSVF extends SVF{
	SVF f;
	float lim, limScl;
	public DerivativeSVF(SVF f, float lim){
		this.f = f;
		setLim(lim);
	}
	public void setLim(float lim){
		this.lim = lim;
		this.limScl = 1 / ( 2 * lim );
	}
	public float value(float x, float y){
		return limScl * (f.value(x, y + lim) - f.value(x, y - lim));
	}
}
