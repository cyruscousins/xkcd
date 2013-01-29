

//Scalar Valued Field





//partial derivative with respect to x

//partial derivative with respect to y

//Vector Valued Field


//Perform differentiation live

public class Vec2{
	public float x, y;
	public Vec2(){}
	public Vec2(float x, float y){
		this.x = x;
		this.y = y;
	}
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	public float lSquared(){
		return x * x + y * y;
	}
	public float length(){
		return (float) Math.sqrt(x * x + y * y); //TODO improve speed
	}
	public float radians(){
		return (float) Math.atan2(y, x);
	}
}
