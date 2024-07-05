package org.example;

import com.bridge.Game;
import com.bridge.core.exceptions.GameException;
import com.bridge.core.exceptions.renderHandlerExceptions.RenderException;
import com.bridge.renderHandler.builders.SpriteBuilder;
import com.bridge.renderHandler.render.Frame;
import com.bridge.renderHandler.repository.SoundRepository;
import com.bridge.renderHandler.repository.SpriteRepository;
import com.bridge.ipc.SocketClient;
import com.bridge.ipc.Transmitter;
import com.bridge.renderHandler.sprite.Coord;
import com.bridge.renderHandler.sprite.Sprite;

import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import static org.example.Utils.*;

public class Main {
    public static void main(String[] args) throws GameException, InterruptedException {
        String projectPath = Paths.get("").toAbsolutePath().toString();
        SocketClient socketClient = new SocketClient(SocketClient.NAMESPACE);
        Transmitter transmitter = new Transmitter(socketClient);

        SpriteRepository repository = new SpriteRepository();
        Game game = makeGame(repository, new SoundRepository());


        Coord currentCoord = new Coord(-300, -250);

        Thread gameThread =
                new Thread(
                        () -> {
                            try {
                                game.run();
                            } catch (GameException e) {

                            }
                        });



        gameThread.start();
        Thread stopperThread =
                new Thread(
                        () -> {
                            try {
                                Thread.sleep(5000);
                                SpriteBuilder builder = new SpriteBuilder(game.getSpriteIRepository());
                                Coord targetCoord = createRandomCoord(300, -350, 250, -250);
                                Sprite sprite = makeSprite(builder, projectPath + "/src/test/resources/pacman.png", 0, 10);
                                Sprite ghost = makeSprite(builder, projectPath + "/src/test/resources/ghost.png", 0, 10);
                                ghost.setPosition(targetCoord);
                                moveTo(currentCoord, targetCoord, sprite, transmitter, game);
                            } catch (InterruptedException e) {

                            } catch (RenderException e) {
                                throw new RuntimeException(e);
                            }
                        });

        stopperThread.start();
        stopperThread.join();
        gameThread.join();

        }

    public static void moveTo(Coord currentCoord, Coord targetCoord, Sprite sprite, Transmitter transmitter, Game game) throws InterruptedException, RenderException {
        while (true) {
            if (areEqualCoords(currentCoord, targetCoord, 10)) {
                break;
            }
            if (currentCoord.getX() < targetCoord.getX()) {
                currentCoord.setX(currentCoord.getX() + 1);
            } else if (currentCoord.getX() > targetCoord.getX()) {
                currentCoord.setX(currentCoord.getX() - 1);
            }

            if (currentCoord.getY() < targetCoord.getY()) {
                currentCoord.setY(currentCoord.getY() + 1);
            } else if (currentCoord.getY() > targetCoord.getY()) {
                currentCoord.setY(currentCoord.getY() - 1);
            }

            sprite.setPosition(currentCoord);
            Frame frame = new Frame(List.of(sprite), List.of());
            transmitter.send(frame);
            game.render();
            Thread.sleep(10);
        }
    }
}

