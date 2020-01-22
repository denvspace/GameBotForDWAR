import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Алгоритм работы программы:
 * 1. Рисуем форму с 2 кнопками "Старт" и "Стоп"
 * -Кнопка "Старт" запускает работу программы
 * -Кнопка "Стоп" останавливает работу программы
 * 2. Создаем скриншот рабоочей зоны игры
 * 3. Анализ полученного изображения:
 * 2а. Сбор ресурсов:
 * -по цвету пикселей находим ресурсы
 * -заносим все полученные пиксели в массив
 * -случайной функцией выбираем из массива индекс
 * -получаем координаты пикселя
 * -выделяем ресурс по выбранному пикселю кликом мышью
 * -добываем выделенный ресурс
 * -если сбор окончился неудачно - нажатие клавиши ОК и продолжение сбора
 * -если ресурса на экране нет, скроллим вниз(вверх)
 * <p>
 * 2б. Бой с мобами:
 */
public class Action {

    public static final Color BIRUZA = new Color(122, 255, 255);
    public static final Color AGAT = new Color(40, 253, 255);
    public static final Color AQUAMARIN = new Color(11, 124, 109);
    public static final Color SAPPHIRE = new Color(0, 1, 152);
    public static final Color KLEVER = new Color(255, 25, 255);

    private static final Coordinates action = new Coordinates(640, 195);
    private static final Coordinates strikeUp = new Coordinates(560, 345);
    private static final Coordinates strikeFront = new Coordinates(590, 390);
    private static final Coordinates strikeDown = new Coordinates(565, 440);
    private static final Coordinates block = new Coordinates(480, 390);
    private static final Coordinates done = new Coordinates(980, 500);
    private static final Coordinates inventory = new Coordinates(850, 100);
    private static final Coordinates location = new Coordinates(910, 100);
    private static final Coordinates hunt = new Coordinates(960, 100);

    private static Color c;
    private static boolean pause;
    private static boolean isDown = true;
    private static int scroll = 0;

    public static void setPause(boolean pause) {
        Action.pause = pause;
    }

    private static BufferedImage getSearchZone() {
        BufferedImage searchZone = null;
        try {
            searchZone = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return searchZone;
    }

    private static BufferedImage getArea(Coordinates begin, Coordinates end) {
        BufferedImage searchArea = null;
        try {
            int x = begin.getX();
            int y = begin.getY();
            int width = end.getX() - x;
            int height = end.getY() - y;
            searchArea = new Robot().createScreenCapture(new Rectangle(x, y, width, height));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return searchArea;
    }

    private static boolean enterColor(BufferedImage bufferedImage, Color color) {
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 1; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                int rColor = (rgb >> 16) & 0xFF;
                int gColor = (rgb >> 8) & 0xFF;
                int bColor = (rgb & 0xFF);
                if (new Color(rColor, gColor, bColor).equals(color)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isFight() {
        if (enterColor(getArea(new Coordinates(355, 196), new Coordinates(478, 215)), new Color(156, 0, 0))) {
            return true;
        } else return false;
    }

    private static void getCoordinatesResourses(ArrayList arrayList, BufferedImage bufferedImage) {
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 1; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                int rColor = (rgb >> 16) & 0xFF;
                int gColor = (rgb >> 8) & 0xFF;
                int bColor = (rgb & 0xFF);
                c = new Color(rColor, gColor, bColor);
                if (c.equals(AGAT) || c.equals(BIRUZA) || c.equals(AQUAMARIN)) {
//                if (c.equals(SAPPHIRE)) {
//                if (c.equals(KLEVER)) {
                    arrayList.add(new Coordinates(x, y));
                }
            }
        }
    }

    public static synchronized void searchResourses() throws AWTException, InterruptedException {
        while (pause) {
            if (!isFight()) {
                BufferedImage image = getSearchZone();
                ArrayList<Coordinates> coordinatesList = new ArrayList<>();
                getCoordinatesResourses(coordinatesList, image);
                int randomRes = coordinatesList.size();
                if (randomRes > 1) {
                    Coordinates coordinates = coordinatesList.get(new Random().nextInt(randomRes - 1));

                    coordinates.leftClickMouse();
                    baseRandomTime(4, 5);
                    action.leftClickMouse();
                    baseRandomTime(10, 15);

                    //Моя ли добыча первая
                    if (enterColor(getArea(new Coordinates(860, 520), new Coordinates(1120, 540)), new Color(20, 139, 11))) {
                        baseRandomTime(50, 200);
                    } else {
                        done.leftClickMouse();
                    }
                } else if (randomRes == 0 || randomRes == 1) {
                    if (isDown) {
                        scrollDown();
                        scroll++;
                        baseRandomTime(4, 5);
                        if (scroll == 9) {
                            isDown = false;
                            done.leftClickMouse();
                            baseRandomTime(4, 5);
                        }
                    } else {
                        scrollUp();
                        scroll--;
                        baseRandomTime(4, 5);
                        if (scroll == 0) {
                            isDown = true;
                            done.leftClickMouse();
                            baseRandomTime(4, 5);
                        }
                    }
                }
            } else {
                fight();
            }
        }
    }

    private static synchronized int baseRandomTime(int base, int correction) throws InterruptedException {
        int tempRandom = new Random().nextInt(base) + correction;
        Thread.sleep(tempRandom * 100);
        return tempRandom * 100;
    }

    private static void fight() throws AWTException, InterruptedException {

        while (isFight()) {
            Coordinates temp[] = {strikeFront, strikeUp, strikeDown};
            int strike = new Random().nextInt(3);
            temp[strike].leftClickMouse();
            baseRandomTime(20, 30);
            if((enterColor(getArea(new Coordinates(522, 327), new Coordinates(674, 350)), new Color(70, 105, 0)))){
                break;
            }
            baseRandomTime(10, 10);
        }
        hunt.leftClickMouse();
        baseRandomTime(10,50);
    }

    public static void getMotherZika() {
        /**
         * Theese mast be method for farming Mother Zic
         */
    }

    private static void scrollUp() throws AWTException {
        new Robot().mouseWheel(-1);
    }

    private static void scrollDown() throws AWTException {
        new Robot().mouseWheel(1);
    }
}