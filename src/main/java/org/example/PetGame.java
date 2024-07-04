package org.example;

import CoffeeTime.InputEvents.Mouse;
import CoffeeTime.Output.Frame.Coord;
import com.bridge.Game;
import com.bridge.core.exceptions.GameException;
import com.bridge.gamesettings.AGameSettings;
import com.bridge.renderHandler.builders.SpriteBuilder;
import com.bridge.renderHandler.repository.SoundRepository;
import com.bridge.renderHandler.repository.SpriteRepository;
import com.bridge.processinputhandler.InputVerifier;
import com.bridge.processinputhandler.MouseEventManager;

import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {
    private final Sprite dog;
    private final Sprite target;
    private final MouseEventManager mouseEventManager;
    private final MouseClickSubscriber mouseClickSubscriber;
    private final Random random = new Random();
    private int score = 0;

    public Game(SpriteRepository spriteRepository, Sprite dog, Sprite target, MouseEventManager mouseEventManager) {
        this.dog = dog;
        this.target = target;
        this.mouseEventManager = mouseEventManager;
        this.mouseClickSubscriber = new MouseClickSubscriber();
        this.mouseEventManager.addSubscriber(mouseClickSubscriber);
        placeTargetRandomly();
    }

    private void placeTargetRandomly() {
        int x = random.nextInt(800); // Assuming the game width is 800
        int y = random.nextInt(600); // Assuming the game height is 600
        target.setPosition(new Coord(x, y));
    }

    private boolean checkCollision(Sprite sprite1, Sprite sprite2) {
        Coord pos1 = sprite1.getPosition();
        Coord pos2 = sprite2.getPosition();
        return pos1.x() == pos2.x() && pos1.y() == pos2.y();
    }

    public void update() {
        if (mouseClickSubscriber.isMouseClicked()) {
            Coord clickPos = mouseClickSubscriber.getClickedCoord();
            dog.setPosition(clickPos);

            if (checkCollision(dog, target)) {
                score++;
                placeTargetRandomly();
                System.out.println("Score: " + score);
            }
        }
    }

    public static void main(String[] args) throws GameException {
        String projectPath = Paths.get("").toAbsolutePath().toString();
        SpriteRepository repository = new SpriteRepository();
        SpriteBuilder builder = new SpriteBuilder(repository);

        // Create dog sprite
        Sprite dog = builder.buildSprite(projectPath + "/src/test/resources/dog.png", new Coord(0, 0), new Size(80, 80));

        // Create target sprite
        Sprite target = builder.buildSprite(projectPath + "/src/test/resources/target.png", new Coord(100, 100), new Size(50, 50));

        MouseEventManager mouseEventManager = new MouseEventManager();

        Game game = new Game(repository, dog, target, mouseEventManager);

        while (true) {
            game.update();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
