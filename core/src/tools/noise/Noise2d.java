package tools.noise;

import tools.noise.utils.CosineInterpolate;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class Noise2d {

    public double SimpleNoise(double x, double y) {
        int N = 0;
        N = (int) x + (int) y * 57;
        N = (N<<13) ^ N;        

        return (double) (1.0 - ((N * (N * N * 15731 + 789221) + 1376312589)
                & Integer.MAX_VALUE) / 1073741824f);
    }

    public double SmoothedNoise(double x, double y) {
        double corners = (SimpleNoise(x - 1, y - 1) + SimpleNoise(x + 1, y - 1) 
                + SimpleNoise(x - 1, y + 1) + SimpleNoise(x + 1, y + 1)) / 16;
        double sides = (SimpleNoise(x - 1, y) + SimpleNoise(x + 1, y) 
                + SimpleNoise(x, y - 1) + SimpleNoise(x, y + 1)) / 8;
        double center = SimpleNoise(x, y) / 4;
        return corners + sides + center;
    }

    public double interpolatedNoise(double x, double y) {
        double X = Math.floor(x);
        double dX = x - X;

        double Y = Math.floor(y);
        double dY = y - Y;

        double v1 = SmoothedNoise(X, Y);
        double v2 = SmoothedNoise(X + 1, Y);
        double v3 = SmoothedNoise(X, Y + 1);
        double v4 = SmoothedNoise(X + 1, Y + 1);

        double i1 = CosineInterpolate.set(v1, v2, dX);
        double i2 = CosineInterpolate.set(v3, v4, dX);

        return CosineInterpolate.set(i1, i2, dY);
    }
    
    public double perlinNoise(double x, double y){

        double total = 0.0;
        double p = .25;
        double n = 6.0 - 1.0;

        for(int i=0; i<n; i++){

            double frequency = Math.pow(2,i);
            double amplitude = Math.pow(p,i);

            total = total + interpolatedNoise(x * frequency, y * frequency) 
                    * amplitude;

        }

        return total;
    }
    //noise.perlinNoise(6, .2, 5, point.offset.x+.01);
    public double perlinNoise(int iter, double k, double m, double x, double y){
        double total = 0.0;

        for(int i=0; i<iter-1; i++){
            double frequency = Math.pow(k,i);
            double amplitude = Math.pow(m,i);

            total = total + interpolatedNoise(x * frequency, y * frequency)
                    * amplitude;
        }
        return total;
    }
}
