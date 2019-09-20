 package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tests.destructable_terrain.DestructableTerrain;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        //DestructableTerrain game = new DestructableTerrain();
        DestructableTerrain game = new DestructableTerrain();
        /*
        config.title = Game.settings.title;
        config.width = Game.settings.xxxxxxwidth;
        config.height = Game.settings.height;
        */
        config.fullscreen = false;
        config.resizable = false;
        config.vSyncEnabled = true; // Setting to false disables vertical sync
        config.foregroundFPS = 60; // Setting to 0 disables foreground fps throttling
        config.backgroundFPS = 60; // Setting to 0 disables background fps throttling	
        
        Settings settings = new Settings();
        settings.maxWidth = 512;
        settings.maxHeight = 512;
        TexturePacker.process(settings, "textures/IndividualElems", "textures/UnifiedElems", "ElemsTex");
               
        
        new LwjglApplication(game, config);
    }
}
