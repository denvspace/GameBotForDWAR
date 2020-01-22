import java.awt.*;
import java.awt.event.InputEvent;

public class Coordinates {
    private int x;
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void leftClickMouse() throws AWTException {
        Robot robot = new Robot();
        robot.mouseMove(getX(),getY());
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(520);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
}