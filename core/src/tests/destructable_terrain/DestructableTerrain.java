package tests.destructable_terrain;

import Engine.game.Game;
import Engine.game.StateBasedGame;
import Engine.management.GameSettings;
import Engine.statesystem.GameStateManager;
import Engine.statesystem.States;

public class DestructableTerrain extends StateBasedGame {

	@Override
	public void initGameStates() {
            //getStateManager().addState(new LoadingState(States.LOADING));
            
            getStateManager().addState(new MainState(States.TEST));
            getStateManager().enterState(States.TEST);
            
            //getStateManager().addState(new Noise2dTest(69));
            //getStateManager().enterState(69);		
	}

	@Override
	public void settings() {
		Game.settings.title = "This is an example game!";
		Game.settings.width = 800;
		Game.settings.height = 600;
	}
	
}
