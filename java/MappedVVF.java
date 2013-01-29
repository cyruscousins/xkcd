

//Scalar Valued Field





//partial derivative with respect to x

//partial derivative with respect to y

//Vector Valued Field

public class MappedVVF extends VVF{
	SVF x, y;
	//#construct SVF x SVF y
	public MappedVVF(SVF x, SVF y){
		this.x = x;
		this.y = y;
	}
	public float xvalue(float x, float y){
		return this.x.value(x, y);
	}
	public float yvalue(float x, float y){
		return this.y.value(x, y);
	}
	public Vec2 value(Vec2 in, Vec2 out){
		out.x = x.value(in);
		out.y = y.value(out);
		return out;
	}
}
