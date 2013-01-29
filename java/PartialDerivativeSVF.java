

//Scalar Valued Field




public class PartialDerivativeSVF extends SVF{
	SVF f;
	float dx, dy; //the direction we take the derivative in
	float lim, limScl;
	//dx * dx + dy * dy = 1
	public DerivativeSVF(SVF f, float dx, float dy, float lim){
		this.f = f;
		
		this.dx = dx;
		this.dy = dy;
		setLim(lim);
	}
	public void setLim(float lim){
		this.lim = lim;
		dx *= lim;
		dy *= lim;
		this.limScl = 1 / ( 2 * lim );
	}
	public float value(float x, float y){
		return limScl * (f.value(x + dx, x + dy) - f.value(x - dx, y - dy));
	}
}
