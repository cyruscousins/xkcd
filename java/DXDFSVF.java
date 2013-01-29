

//Scalar Valued Field





//partial derivative with respect to x
public class DXDFSVF extends SVF{
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
		return limScl * (f.value(x + lim, y) - f.value(x - lim, y));
	}
}
