import CoffeeTime.InputEvents.Mouse;
import CoffeeTime.InputEvents.Position;
import com.bridge.processinputhandler.IEventSubscriber;
import com.bridge.renderHandler.sprite.Coord;

import java.util.concurrent.atomic.AtomicBoolean;

public class MouseClickSubscriber implements IEventSubscriber<Mouse> {
    private final AtomicBoolean mouseClicked = new AtomicBoolean(false);
    private Coord clickedCoord;

    @Override
    public void doNotify(Mouse mouseEvent) {
        if ("MousePressed".equals(mouseEvent.type())) {
            mouseClicked.set(true);
            Position pos = mouseEvent.position();
            clickedCoord = new Coord((int) pos.x(), (int) pos.y());
        }
    }

    public boolean isMouseClicked() {
        return mouseClicked.get();
    }

    public Coord getClickedCoord() {
        return clickedCoord;
    }
}
