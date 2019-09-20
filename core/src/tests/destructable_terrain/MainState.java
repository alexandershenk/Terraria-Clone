package tests.destructable_terrain;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import Engine.game.Game;
import Engine.statesystem.GameStateManager;
import Engine.statesystem.BasicGameState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import worldspace.Chunk;
import worldspace.Coords;
import worldspace.ModifierEvaluator;
import worldspace.WorldSpace;
import worldspace.WorldWindow;
import worldspace.modifyType;

public class MainState extends BasicGameState {

    private FPSLogger fps = new FPSLogger();
    private Chunk[][] chunks;
    private int speed = 4;
    private ModifierEvaluator modifier = new ModifierEvaluator();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private WorldWindow window;

    public MainState(int stateID) {
        super(stateID);
    }

    @Override
    public void init(GameStateManager gsm) {
        super.init(gsm);
        Game.mainCamera.position.x=0;
        Game.mainCamera.position.y=0;

        WorldSpace.init();

        try {
            window = new WorldWindow(
                    new Coords((int) (Game.mainCamera.position.x)
                            - WorldSpace.CHUNK_WIDTH * WorldSpace.GRID_SIZE,
                            (int) Game.mainCamera.position.y
                                    + WorldSpace.CHUNK_HEIGHT * WorldSpace.GRID_SIZE*2));
            System.out.println(Game.mainCamera.position.x+", "+window.offset.y);
        } catch (IOException ex) {
            Logger.getLogger(MainState.class.getName()).log(Level.SEVERE, null, ex);
        }
            WorldSpace.render.setWindow(window);

        modifier.setEUID(1);
        modifier.setModifier(modifyType.SQUARE);

        WorldSpace.render.setCam(Game.mainCamera);

            /*for(int y=0; y < 6; y++){
            for(int x=0; x< 6; x++){
            try {
            WorldSpace.loadChunkAt(
            new Coords(
            WorldSpace.CHUNK_WIDTH*
            WorldSpace.GRID_SIZE*x,
            (WorldSpace.CHUNK_HEIGHT*
            WorldSpace.GRID_SIZE)-
            WorldSpace.CHUNK_HEIGHT*
            WorldSpace.GRID_SIZE*y));
            } catch (IOException ex) {
            Logger.getLogger(MainState.class.getName()).log(
            Level.SEVERE, null, ex);
            }
            }
            }*/
            
        //Game.mainCamera.translate(0, -WorldSpace.GRID_SIZE*WorldSpace.CHUNK_HEIGHT/2, 0);



        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                int mX = Gdx.input.getX();
                int mY = Gdx.input.getY();
                
                Vector3 worldCoord = Game.mainCamera.unproject(new Vector3(mX, mY, 0));

                Coords coords = new Coords((int) worldCoord.x,
                        (int) worldCoord.y);
                
                if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                    modifier.isBackground(true);
                
                else
                    modifier.isBackground(false);
                
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    modifier.setModifier(modifyType.SQUARE);
                } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    modifier.setModifier(modifyType.ORGANIC);

                }

                modifier.eval(coords);
                return true;
            }
        });
    }

    @Override
    public void render(SpriteBatch batch) {
        fps.log();
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Game.mainCamera.update();
        try {
            window.update(new Coords((int) Game.mainCamera.position.x
                    - WorldSpace.CHUNK_WIDTH * WorldSpace.GRID_SIZE,
                    (int) Game.mainCamera.position.y
                    + WorldSpace.CHUNK_HEIGHT * WorldSpace.GRID_SIZE*2));
        } catch (IOException ex) {
            Logger.getLogger(MainState.class.getName()).log(Level.SEVERE, null, ex);
        }

        WorldSpace.render.render();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            Game.mainCamera.zoom += 0.02;
            speed += .99;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Game.mainCamera.zoom -= 0.02;
            if (speed > 3) {
                speed -= .99;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            Game.mainCamera.translate(-speed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Game.mainCamera.translate(speed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            Game.mainCamera.translate(0, -speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            Game.mainCamera.translate(0, speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            modifier.setEUID(-1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            modifier.setEUID(0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            modifier.setEUID(1);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            modifier.setEUID(2);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)) {
            modifier.setEUID(3);
            System.out.println(modifier.getEUID());
        }

    }

    @Override
    public void dispose() {
        Iterator it = WorldSpace.ExistingChunks.entrySet().iterator();
        for (Chunk value : WorldSpace.ExistingChunks.values()) {
            try {
                value.dispose();
            } catch (IOException ex) {
                Logger.getLogger(MainState.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

}
