

//Scalar Valued Field

//Vector Valued Field


public class Vec2D{
	public float x, y;
	public Vec2D(){}
	public Vec2D(float x, float y){
		this.x = x;
		this.y = y;
	}
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	public float length(){
		return (float) Math.sqrt(x * x + y * y); //TODO improve speed
	}
	public float radians(){
		return (float) Math.atan2(y, x);
	}
}
