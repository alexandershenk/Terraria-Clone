package worldspace;

import Engine.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import static com.badlogic.gdx.graphics.GL20.GL_NEAREST;
import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE_2D;
import static com.badlogic.gdx.graphics.GL20.GL_TEXTURE_MAG_FILTER;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class WorldSpaceRender {

    public static final String VERT_SHADER
            = "attribute vec2 a_position;\n"
            + "attribute float inBackground;\n"
            + "attribute vec2 a_texCoord0;\n"
            + "uniform mat4 u_projTrans;\n"
            + "varying vec2 v_texCoords;"
            + "varying vec4 vColor;\n"
            + "void main() {\n"
            + "v_texCoords = a_texCoord0; \n"
            + "if(inBackground == 1.0f){\n"
            + "vColor = vec4(.5,.5,.5,1);//mask for bg tiles\n"
            + "}else{\n"
            + "vColor = vec4(1,1,1,1);\n"
            + "}\n"
            + "	gl_Position =  u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n"
            + "}";

    public static final String FRAG_SHADER
            = "#ifdef GL_ES\n"
            + "precision mediump float;\n"
            + "#endif\n"
            + "varying vec2 v_texCoords;\n"
            + "uniform sampler2D u_texture;\n"
            + "varying vec4 vColor;\n"
            + "void main() {\n"
            + "	gl_FragColor = vColor * texture2D(u_texture, v_texCoords);\n"
            + "}";

    private Mesh mesh;
    private OrthographicCamera cam;
    private ShaderProgram shader;

    private HashMap<String, Integer> vertCom;

    private final int MAX_TRIS = 10000;
    private final int MAX_VERTS = MAX_TRIS * 3;
    private int NUM_COMPONENTS = 0;
    private float[] verts;
    private int idx = 0;
    private WorldWindow window;

    private ArrayList<ArrayList<EvalPointsSet>> renderSet
            = new ArrayList<ArrayList<EvalPointsSet>>();

    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("textures/UnifiedElems/ElemsTex.atlas"));
    AtlasRegion region;

    private Texture texture = atlas.findRegion("dirt").getTexture();

    private static ShaderProgram createMeshShader() {
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
        String log = shader.getLog();
        if (!shader.isCompiled()) {
            throw new GdxRuntimeException(log);
        }
        if (log != null && log.length() != 0) {
            System.out.println("Shader Log: " + log);
        }
        return shader;
    }

    public WorldSpaceRender() {
        this.vertCom = new HashMap<String, Integer>();
        vertCom.put("Pos", 2);
        vertCom.put("Bg", 1);
        vertCom.put("Tex", 2);

        for (Integer value : vertCom.values()) {
            NUM_COMPONENTS += value;
        }

        verts = new float[MAX_VERTS * NUM_COMPONENTS];

        mesh = new Mesh(true, MAX_VERTS, 0,
                new VertexAttribute(Usage.Position, vertCom.get("Pos"), "a_position"),
                new VertexAttribute(Usage.Generic, vertCom.get("Bg"), "inBackground"),
                new VertexAttribute(Usage.TextureCoordinates, vertCom.get("Tex"), "a_texCoord0"));

        shader = createMeshShader();
    }

    public class Vertice extends WorldSpaceObject implements Elemental {

        private final int startIdx;
        private int EUID;
        private final EvalPointsSet set;

        public Vertice(Coords coords, EvalPointsSet set) {
            this(set);
        }

        public Vertice(EvalPointsSet set) {
            startIdx = idx;
            this.set = set;
        }

        @Override
        public void setOffset(Coords coords) {
            verts[startIdx] = coords.x;
            verts[startIdx + 1] = coords.y;
        }

        @Override
        public void isBackground(Boolean isBackground) {
            if (isBackground) {
                verts[startIdx + 2] = 1;
            } else {
                verts[startIdx + 2] = 0;
            }
        }

        @Override
        public boolean isBackground() {
            return (verts[startIdx + 2] == 1);
        }

        @Override
        public void setEUID(int EUID) {
            this.EUID = EUID;
        }

        @Override
        public int getEUID() {
            return this.EUID;
        }
    }

    public WorldSpaceRender(OrthographicCamera cam) {
        this();
        this.cam = cam;
    }

    public void addPolySet(ArrayList<EvalPointsSet> set) {
        renderSet.add(set);
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public void setWindow(WorldWindow window) {
        this.window = window;
    }

    public void render() {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //System.out.println(window.offset.x);
        for (int y = 0; y < window.height; y++) {
            for (int x = 0; x < window.width; x++) {
                for (int i = 0; i < WorldSpace.CHUNK_HEIGHT; i++) {
                    for (int j = 0; j < WorldSpace.CHUNK_WIDTH; j++) {

                        ArrayList<EvalPointsSet> sets = window.window.get(y).get(x).getGrid()[i][j].getData();
                        if (sets.get(0).getOffset().x
                                > Game.mainCamera.position.x - Game.mainCamera.viewportWidth * Game.mainCamera.zoom
                                && sets.get(0).getOffset().x
                                < Game.mainCamera.position.x + Game.mainCamera.viewportWidth * Game.mainCamera.zoom
                                && sets.get(0).getOffset().y
                                > Game.mainCamera.position.y - Game.mainCamera.viewportHeight * Game.mainCamera.zoom
                                && sets.get(0).getOffset().y
                                < Game.mainCamera.position.y + Game.mainCamera.viewportHeight * Game.mainCamera.zoom) {
                            for (EvalPointsSet set : sets) {

                                drawGridData(set.getPoly());
                            }
                        }
               
                }
            }
        }

    }

    flush();
}

private void flush() {
        //if we've already flushed
        if (idx == 0) {
            return;
        }

        mesh.setVertices(verts);
        Gdx.gl.glDepthMask(false);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
                GL_NEAREST);
        Gdx.graphics.getGL20().glEnable(GL20.GL_TEXTURE_2D);
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        int vertexCount = (idx / NUM_COMPONENTS);

        texture.bind();
        shader.begin();
        shader.setUniformi("u_texture", 0);
        shader.setUniformMatrix("u_projTrans", cam.combined);

        mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);
        shader.end();

        Gdx.gl.glDepthMask(true);

        idx = 0;
    }

    private void colorAttrib(PolyPointsSet set) {
        if (set.isBackground()) {
            verts[idx++] = 1;
        } else {
            verts[idx++] = 0;
        }

        if (set.offset.x + WorldSpace.GRID_SIZE == verts[idx - 3]) {
            verts[idx++] = region.getU2();
        } else {
            verts[idx++] = region.getU()
                    + (((((verts[idx - 4]) / WorldSpace.GRID_SIZE))
                    - (float) Math.floor((verts[idx - 4]) / WorldSpace.GRID_SIZE))
                    * (region.getU2() - region.getU()));
        }
        if (set.offset.y - WorldSpace.GRID_SIZE == verts[idx - 3]) {
            verts[idx++] = region.getV2();

        } else {
            verts[idx++] = region.getV()
                    + (((((verts[idx - 4]) / WorldSpace.GRID_SIZE))
                    - (float) Math.floor((verts[idx - 4]) / WorldSpace.GRID_SIZE))
                    * (region.getV2() - region.getV()));
        }
    }

    private void positionAttrib(int x, int y) {
        verts[idx++] = x;
        verts[idx++] = y;
    }

    private void drawGridData(PolyPointsSet pointsSet) {
        region = atlas.findRegion(ElementNames.data.get(pointsSet.getEUID()));       

        if (idx >= verts.length - (NUM_COMPONENTS * 4 * 3))
            flush();

        switch (pointsSet.getGridCase()) {
            case 1:

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);
                break;
            case 2:
                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);
                break;
            case 3:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);
                break;
            case 4:
                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);
                break;
            case 5:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 6:
                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 7:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 8:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 9:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 10://*
                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 11://*
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideC();
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);
                break;
            case 12:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 13:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.getSideB();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 14:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.getSideD();
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.getSideA();
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
            case 15:
                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x + WorldSpace.GRID_SIZE;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);

                verts[idx++] = pointsSet.offset.x;
                verts[idx++] = pointsSet.offset.y - WorldSpace.GRID_SIZE;
                colorAttrib(pointsSet);
                break;
        }
    }

    public void dispose() {
        mesh.dispose();
        shader.dispose();
    }

}
