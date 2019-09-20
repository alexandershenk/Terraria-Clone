package tests.destructable_terrain;

import Engine.game.Game;
import Engine.statesystem.BasicGameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import static com.badlogic.gdx.graphics.GL20.GL_NEAREST;
import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE_2D;
import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE_MAG_FILTER;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class WaterTest extends BasicGameState{


	public static final String VERT_SHADER =  
            Gdx.files.internal("WaterTest.vert").readString();
	
	public static final String FRAG_SHADER = 
            Gdx.files.internal("WaterTest.frag").readString();

	
	protected static ShaderProgram createMeshShader() {
		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
		String log = shader.getLog();
		if (!shader.isCompiled())
			throw new GdxRuntimeException(log);		
		if (log!=null && log.length()!=0)
			System.out.println("Shader Log: "+log);
		return shader;
	}
	private Texture texture;
	private Mesh mesh;
	private OrthographicCamera cam;
	private ShaderProgram shader;
		
	//Position attribute - (x, y) 
	public static final int POSITION_COMPONENTS = 2;
        
	//Color attribute - (r, g, b, a)
	public static final int COLOR_COMPONENTS = 4;
	
	//Total number of components for all attributes
	public static final int NUM_COMPONENTS = POSITION_COMPONENTS + 
                COLOR_COMPONENTS+2;

	//The maximum number of triangles our mesh will hold
	public static final int MAX_TRIS = 2;
	
	//The maximum number of vertices our mesh will hold
	public static final int MAX_VERTS = MAX_TRIS * 3;
	
	//The array which holds all the data, interleaved like so:
	//    x, y, r, g, b, a
	//    x, y, r, g, b, a, 
	//    x, y, r, g, b, a, 
	//    ... etc ...
	private float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];
	
	//The index position
	private int idx = 0;
        
        long currTime = System.nanoTime();
        long prevTime = System.nanoTime()-1;
        float dTime;

    public WaterTest(int stateID) {
        super(stateID);
        create();
    }
	
	public void create() {
                FileHandle imageFileHandle = Gdx.files.internal("textures/boundary.png"); 
                texture = new Texture(imageFileHandle);
		mesh = new Mesh(true, MAX_VERTS, 0, 
                    new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
                    new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"),
                    new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
		
		shader = createMeshShader();
		cam = new OrthographicCamera();
                
        }



	@Override
	public void render(SpriteBatch batch) {
            currTime = System.nanoTime();
            if(dTime>100)
                dTime=0;
            else
                dTime +=( ((currTime - prevTime) / 1e6f));
            prevTime=currTime;
            //this will push the triangles into the batch
            draw(Color.RED);

            //this will render the triangles to GL
            flush();
	}
	
	void flush() {
		//if we've already flushed
		if (idx==0)
			return;
                
                Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, 
                        GL_NEAREST);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
		Gdx.gl.glDepthMask(false);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		mesh.setVertices(verts);
		int vertexCount = (idx/NUM_COMPONENTS);
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                
                texture.bind();
                shader.begin();
		
		shader.setUniformMatrix("u_projTrans", cam.combined);
                shader.setUniformf("d_time", dTime);
		mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);

		shader.end();
		
		Gdx.gl.glDepthMask(true);
		
		idx = 0;
	}
	
	void draw(Color color) {
		if (idx==verts.length)
			flush();
		
		verts[idx++] = 0;
		verts[idx++] = 0;
		verts[idx++] = color.r;
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;
		verts[idx++] = 0;
		verts[idx++] = 1;
		
		verts[idx++] = 640;
		verts[idx++] = 0;
		verts[idx++] = color.r;
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;
		verts[idx++] = 1;
		verts[idx++] = 1;

		verts[idx++] = 0;
		verts[idx++] = 480;
		verts[idx++] = color.r;
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;
                verts[idx++] = 0;
		verts[idx++] = 0;

                
		verts[idx++] = 640;
		verts[idx++] = 0;
		verts[idx++] = color.r;
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;
                verts[idx++] = 1;
		verts[idx++] = 1;

		
		verts[idx++] = 640;
		verts[idx++] = 480;
		verts[idx++] = color.r;
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;
                verts[idx++] = 1;
		verts[idx++] = 0;


		verts[idx++] = 0;
		verts[idx++] = 480;
		verts[idx++] = color.r;
		verts[idx++] = color.g;
		verts[idx++] = color.b;
		verts[idx++] = color.a;
                verts[idx++] = 0;
		verts[idx++] = 0;


	}

	@Override
	public void dispose() {
		mesh.dispose();
		shader.dispose();
	}


    @Override
    public void update(float delta) {
    }


}