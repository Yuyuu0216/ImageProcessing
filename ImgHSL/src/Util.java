
public class Util {
	
	final static int checkPixelBounds(int value){
		if(value > 255) return 255;
		if(value < 0) return 0;
		return value;
 	} 
	
	//get red channel from colorspace (4 bytes)
	final static int getR(int rgb){
		  return checkPixelBounds((rgb & 0x00ff0000)>>>16); 	
   }

	//get green channel from colorspace (4 bytes)
	final static int getG(int rgb){
	  return checkPixelBounds((rgb & 0x0000ff00)>>>8);
	}
	
	//get blue channel from colorspace (4 bytes)
	final static int getB(int rgb){
		  return checkPixelBounds(rgb & 0x000000ff);
	}
	
	//make ARGB color format from R, G, and B channels 
	final static int makeColor(int r, int g, int b){
		return (255 << 24 | r << 16 | g << 8 | b); 
	}
	
	final static int covertToGray(int r, int g, int b){
		return checkPixelBounds((int)(0.2126 * r + 0.7152 * g + 0.0722 * b));		
	}
	
	final static double [] affine(double[][] a, double[] b){
		int aRow = a.length;
		int bRow = b.length;
		double[] result = new double[aRow];
		for(int i = 0 ; i < aRow ; i++) {
			for(int j = 0 ; j < bRow ; j++) {
				result[i] += a[i][j]*b[j];
			}
		}
		return  result;
	}
	
	static HSL RGB2HSL(double r, double g, double b) {
		double h = 0;
		double s;
		double l;
		r = r / 255.0;
		g = g / 255.0;
		b = b / 255.0;
		
		double min = Math.min(Math.min(r, g), b);
		double max = Math.max(Math.max(r, g), b);
		l = 0.5 * (min + max);
		s = getS(min, max, l);
		
		if( s == 0)
			h = 0.0;
		else if(max == r)
			h = ((g - b) / (max - min)) % 6;
		else if(max == g)
			h = 2.0 + (b - r) / (max - min);
		else if(max == b)
			h = 4.0 + (r - g) / (max - min);
		h *= 60;
		if(h < 0.0)
			h += 360;
		if(h > 360)
			h -= 360;
		if(h < 0 || h > 360)
			System.out.println("RGB2HSL: hue =" + h);
		return new HSL(h, s, l);
	}
	
	static double getS(double min, double max, double l) {
		if(min == max) {
			return 0;
		}
		if(l <= 0.5) {
			return (max - min) / (max + min);
		}
		else {
			return (max - min) / (2.0 - max - min);
		}
	}
	
	static double checkValue(double v) {
		if(v < 0) {
			return v + 1.0;
		}
		else if(v > 1.0) {
			return v - 1.0;
		}
		else {
			return v;
		}
	}
	
	static RGB HSL2RGB(double hue, double sat, double lum) {
		if(hue > 360)
			hue -= 360;
		if(hue < 0)
			hue += 360;
		if(hue > 360 || hue < 0)
			System.out.println("hue = "+ hue);
		
		if(sat < 0.0)
			sat = 0.0;
		if(sat > 1.0)
			sat = 1.0;
		if(lum < 0.0)
			lum = 0.0;
		if(lum > 1.0)
			lum = 1.0;
		
		if(sat < 0 || sat > 1)
			System.out.println("Care sat");
		if(lum < 0 || lum > 1)
			System.out.println("Care lum");
		double r,g,b;
		double c = (1.0 - Math.abs(2 * lum - 1.0)) * sat;
		double x = c * (1- Math.abs(((hue/60) % 2) - 1));
		double m = lum - c /2.0 ;
		if(hue >= 0 && hue < 60) {
			r = c;
			g = x;
			b = 0;
		}else if(hue >= 60 && hue < 120) {
			r = x;
			g = c;
			b = 0;
		}else if(hue >= 120 && hue < 180) {
			r = 0;
			g = c;
			b = x;
		}else if(hue >= 180 && hue < 240) {
			r = 0;
			g = x;
			b = c;
		}else if(hue >= 240 && hue < 300) {
			r = x;
			g = 0;
			b = c;
		}else {
			r = c;
			g = 0;
			b = x;
		}
		r += m;
		g += m;
		b += m;
		return new RGB(checkPixelBounds((int)(255 * r)),
					   checkPixelBounds((int)(255 * g)),
					   checkPixelBounds((int)(255 * b)));
	}
}
