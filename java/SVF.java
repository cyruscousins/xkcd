

//Scalar Valued Field
public abstract class SVF{
	public abstract float value(float x, float y);
	public float value(Vec2 vec){
		return value(vec.x, vec.y);
	}
}
