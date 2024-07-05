package org.example;

import com.bridge.Game;
import com.bridge.core.exceptions.GameException;
import com.bridge.core.exceptions.renderHandlerExceptions.NonExistentFilePathException;
import com.bridge.gamesettings.AGameSettings;
import com.bridge.processinputhandler.InputVerifier;
import com.bridge.processinputhandler.MouseEventManager;
import com.bridge.renderHandler.builders.SpriteBuilder;
import com.bridge.renderHandler.repository.SoundRepository;
import com.bridge.renderHandler.repository.SpriteRepository;
import com.bridge.renderHandler.sprite.Coord;
import com.bridge.renderHandler.sprite.Sprite;

import java.util.List;
import java.util.Random;

public class Utils {
    private static final Random random = new Random();


    public static Game makeGame(SpriteRepository spriteRepository, SoundRepository soundRepository){
        MouseEventManager manager = new MouseEventManager();
        InputVerifier inputVerifier = new InputVerifier(List.of(manager));
        AGameSettings gameSettings = new AGameSettings() {
            @Override
            public boolean isGameOver() {
                return false;
            }
        };
        return new Game(gameSettings);
    }

    public static void runGame(Game game) {
        try {
            game.run();
        } catch (GameException e) {

        }
    }

    public static Sprite makeSprite(SpriteBuilder builder, String path, int x, int y) {
        try {
            builder.buildCoord(80, 80)
                    .buildPath(path)
                    .buildSize(10.0, 10.0);
        } catch (NonExistentFilePathException e) {
        }
        return builder.assemble();
    }

    public static boolean areEqualCoords(Coord coord1, Coord coord2, int tolerance) {
        return Math.abs(coord1.getX() - coord2.getX()) <= tolerance && Math.abs(coord1.getY() - coord2.getY()) <= tolerance;
    }
    public static Coord createRandomCoord(int xLimitPos, int xLimitNeg, int yLimitPos, int yLimitNeg) {
        int x = xLimitNeg + random.nextInt(xLimitPos - xLimitNeg + 1);
        int y = yLimitNeg + random.nextInt(yLimitPos - yLimitNeg + 1);
        return new Coord(x, y);
    }

}