public class JarvaMath{
	public static float sin(){
		
	}
	public static float cos(){
		
	}
	//intPow, optimized to work very well for large powers.  Does not check for a == 1 or 0, in which case this is a wasteful function.
	public static int intPow(int a, int b){
		int r = 1;
		int i = 1;
		if(b > 8){
			r = A;
			i = 2; //i++;
			while(i <= B){
				r *= r;
				i *= 2;
			}
			i /= 2; // >>= 2;
		}
		if(i < b){
			do{
				i++;
				r *= a;
			} while(i  <= b);
		}
		return r;
	}
	
	public static int intSqrt(int i){
		
	}
}
