package tests.destructable_terrain;

import Engine.statesystem.BasicGameState;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import tools.noise.Noise2d;

/**
 *
 * @author roasstbeef (alex.s)
 */
public class Noise2dTest extends BasicGameState{

    public Noise2dTest(int stateID) {
        super(stateID);
    
        try {
            int width = 800, height = 800;

            // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
            // into integer pixels
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);   
            WritableRaster raster = bi.getRaster();
            
            Noise2d noise = new Noise2d();
            for(int x = 0; x < width; x++) {
                for(int y = 0; y < height; y++) {
                    int res = (int) 
                            Math.abs(noise.perlinNoise(6, .4, 5, x+.01, y+.01));
                    res = (res > 20) ? res : 0;
                    //System.out.println(res);
                    raster.setPixel(x, y, new int[]{res,res,res,255});                    
    
                }
            }
            ImageIO.write(bi, "PNG", new File("plane.png"));

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
        @Override
    public void render(SpriteBatch batch) {
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void dispose() {
    }

}
