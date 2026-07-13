import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.imageio.ImageIO;

import biuoop.GUI;
import biuoop.Sleeper;
import biuoop.DrawSurface;

/**
 * Game.
 */
public class Game {
    final public Color MyLightOrange = new Color(255, 170, 90);
    final public Color PaddleHeadColor = new Color(173, 215, 255);
    final public Color MyOrange = new Color(255, 140, 0);
    final public Color MyRed = new Color(220, 20, 60);
    private boolean changeAltitude = false;
    private int altitude = 35350;
    private boolean changeAirSpeed = false;
    private int airSpeed = 852;
    private boolean changeTemperature = false;
    private int temperature = -53;
    private boolean changePressure = false;
    private int pressure = 4;
    private int radarAngle = 0;
    private BufferedImage image;
    // last Direction true is right false is left
    private boolean lastDirection = false;
    private int timeHeld = 20;
    private Queue<Queue<Drawable>> animations;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private Counter blocksCounter;
    private Counter ballsCounter;
    private Paddle paddle;
    private Counter score;
    private DrawSurface drawSurafce;
    private boolean movingLeft = false;
    private boolean movingRight = false;

    final int SIDEBARSEPERATOR = 60;
    final int WIDTH = 1300;
    final int HEIGHT = 900;
    final int SIDEWIDTH = 300;
    final int STARTSTATS = WIDTH + 20;
    private GUI gui = new GUI("", WIDTH + SIDEWIDTH, HEIGHT);

    /**
     * init the class.
     */
    public Game() {
        try {
            this.image = ImageIO.read(new File("src/space.png"));
        } catch (Exception e) {
            System.out.println(e);
        }
        this.animations = new LinkedList<>();
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.blocksCounter = new Counter();
        this.ballsCounter = new Counter();
        this.score = new Counter();
    }

    public DrawSurface getDrawSurface() {
        return this.drawSurafce;
    }

    public void setDrawSurface(DrawSurface d) {
        this.drawSurafce = d;
    }

    /**
     * @param c add the c collidable
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * @param s add the s sprite to game
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * create all that contected to the game.
     */
    public void initialize() {
        BlockRemover blockRemover = new BlockRemover(this, this.blocksCounter);
        HitAnimation hitAnimation = new HitAnimation(this);
        ScoreTrackingListener scoreTrackingListener = new ScoreTrackingListener(this.score);
        Point leftBlockUpperLeft = new Point(0, 0);
        Point downBlockUpperLeft = new Point(0, HEIGHT - 30);
        Point rightBlockUpperLeft = new Point(WIDTH - 30, 0);
        Point topBlockUpperLeft = new Point(0, 0);
        Rectangle leftRect = new Rectangle(leftBlockUpperLeft, 30, HEIGHT);
        Rectangle rightRect = new Rectangle(rightBlockUpperLeft, 30, HEIGHT);
        Rectangle topRect = new Rectangle(topBlockUpperLeft, WIDTH, 30);
        Rectangle downRect = new Rectangle(downBlockUpperLeft, WIDTH, 30);
        Block leftBlock = new Block(leftRect);
        Block rightBlock = new Block(rightRect);
        Block topBlock = new Block(topRect);
        Block downBlock = new Block(downRect);
        leftBlock.addHitListener(hitAnimation);
        rightBlock.addHitListener(hitAnimation);
        topBlock.addHitListener(hitAnimation);
        BallRemover ballRemover = new BallRemover(this, this.ballsCounter);
        downBlock.addHitListener(ballRemover);
        int paddleHight = 30;
        int paddleWidth = 300;
        Point paddleUpLeft = new Point(WIDTH / 2, HEIGHT - 50 - paddleHight);
        Rectangle paddleRectangle = new Rectangle(paddleUpLeft, paddleWidth, paddleHight);
        paddle = new Paddle(this.gui, paddleRectangle, HEIGHT, WIDTH, this);
        paddle.addToGame(this);
        leftBlock.addToGame(this);
        rightBlock.addToGame(this);
        topBlock.addToGame(this);
        downBlock.addToGame(this);
        Ball ball2 = new Ball(HEIGHT - paddleHight, WIDTH / 2 + 30, 15, MyLightOrange);
        ballsCounter.increase(1);
        ball2.addToGame(this);
        ball2.setGameEnvironment(this.environment);
        // Ball ball3 = new Ball(110, 210, 10, Color.BLUE);
        // ball3.addToGame(this);
        // ball3.setGameEnvironment(this.environment);
        // ballsCounter.increase(1);
        Ball ball = new Ball(HEIGHT - paddleHight, WIDTH / 2, 15, MyLightOrange);
        ballsCounter.increase(1);
        ball.addToGame(this);
        ball.setGameEnvironment(this.environment);
        Velocity volocity = Velocity.fromAngleAndSpeed(0, 8);
        ball.setVelocity(volocity);
        ball2.setVelocity(volocity);
        // ball3.setVelocity(volocity);
        int biggestRow = 12;
        int gap = 50;
        int width = 80;
        int height = 40;
        int currentY = 85;
        int marginX = 150;
        ball.setPaddle(paddle);
        // ball2.setPaddle(paddle);
        Block[][] blocks = new Block[7][biggestRow + 1];
        Color[] colors = {
                MyOrange,
                MyRed,
                MyLightOrange,
                MyOrange,
                MyRed,
                MyLightOrange,
                MyOrange,
        };
        for (int i = 0; i < blocks.length; i++) {
            // times 2 because i dont know
            int currentX = WIDTH - marginX - width * 2 - 30 - i * width;
            for (int j = i; j < blocks[0].length - i; j++) {
                Point point = new Point(currentX + width, currentY);
                Rectangle rect = new Rectangle(point, width, height);
                blocks[i][j] = new Block(rect);
                blocks[i][j].addToGame(this);
                blocks[i][j].setColor(colors[i]);
                blocks[i][j].addHitListener(blockRemover);
                blocks[i][j].addHitListener(scoreTrackingListener);

                this.blocksCounter.increase(1);

                currentX -= width;
            }
            biggestRow--;
            currentY += height;
        }
    }

    // Run the game -- start the animation loop.
    /**
     * run the game.
     */
    public void run() {
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;
        int noMoreBlocks = 0;
        int noMoreBalls = 0;
        while (true) {
            long startTime = System.currentTimeMillis(); // timing
            if (this.ballsCounter.getValue() == noMoreBalls) {
                System.out.println("Game Over. \nYour score is:" + this.score.getValue());
                this.gui.close();
                return;
            }
            if (this.blocksCounter.getValue() == noMoreBlocks) {
                this.score.increase(100);
                System.out.println("You win!\nYour score is:" + this.score.getValue());
                this.gui.close();
                return;
            }

            DrawSurface d = this.gui.getDrawSurface();
            try {
                d.drawImage(0, 0, this.image);

            } catch (Exception e) {
                System.out.println(e);
            }
            drawHead(d);
            if (!this.animations.isEmpty()) {
                for (Queue<Drawable> animation : this.animations) {
                    if (!animation.isEmpty()) {
                        Drawable object = animation.remove();
                        object.drawOn(d);
                    }
                }
            }
            if (this.movingLeft) {
                drawRightAnimation(d, this.paddle.getRightX(), this.paddle.getMiddleHeight());
            } else if (this.movingRight) {
                drawLeftAnimation(d, this.paddle.getLeftX(), this.paddle.getMiddleHeight());
            } else {
                if (this.timeHeld > 20)
                    timeHeld--;
            }
            double maxDx = 5;
            double fracture = maxDx / 255;
            double currentDX = fracture * this.timeHeld;
            currentDX = currentDX*currentDX;
            if (currentDX > 0 && currentDX <= 9)
                this.paddle.setDX(3 + currentDX);
            this.movingLeft = false;
            this.movingRight = false;
            this.sprites.drawAllOn(d);
            drawSideBar(d);
            drawTail(d);
            this.gui.show(d);
            this.sprites.notifyAllTimePassed();

            // timing
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }

    /**
     * @param s the sprite to remove
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * game
     * 
     * @param c the Collidable to remove
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    public void hitAnimation(Point hitPoint, Color color, int radius, int duration) {
        LinkedList<Drawable> animation = new LinkedList<Drawable>();
        for (int i = 0; i < duration; i++) {
            animation.add(new Circle((int) hitPoint.getX(), (int) hitPoint.getY(), i * 2, color));
        }
        this.animations.add(animation);
    }

    public void rightMoveAnimation() {
        this.movingRight = true;
        this.lastDirection = true;
        if (timeHeld < 255)
            this.timeHeld++;
    }

    public void leftMoveAnimation() {
        this.movingLeft = true;
        this.lastDirection = false;
        if (timeHeld < 255)
            this.timeHeld++;
    }

    private void drawLeftAnimation(DrawSurface d, int x, int y) {
        int sideMargin = 20;
        int buttomMargin = 4;
        double maxLength = 70;
        double fracture = maxLength / 255;
        double currentLength = fracture * this.timeHeld;
        int radiusOval = (int) currentLength;
        d.setColor(new Color(255, 85, 0));
        d.fillCircle(x - sideMargin - 53, y - 6, 3);
        d.fillCircle(x - sideMargin - 60, y + 2, 2);
        d.fillOval(x - sideMargin - radiusOval, y - 8 - buttomMargin, radiusOval, 8);
        d.fillOval(x - sideMargin - radiusOval - 15, y - buttomMargin, radiusOval + 15, 8);
        d.fillOval(x - sideMargin - radiusOval, y + 8 - buttomMargin, radiusOval, 8);
        d.setColor(new Color(255, 50, 0));
        d.fillCircle(x - sideMargin, y, 17);
    }

    private void drawRightAnimation(DrawSurface d, int x, int y) {
        int sideMargin = 20;
        int buttomMargin = 4;
        double maxLength = 70;
        double fracture = maxLength / 255;
        double currentLength = fracture * this.timeHeld;
        int radiusOval = (int) currentLength;

        d.setColor(new Color(255, 85, 0));
        d.fillCircle(x + sideMargin + 53, y - 6, 3);
        d.fillCircle(x + sideMargin + 60, y + 2, 2);
        d.fillOval(x + sideMargin, y - 8 - buttomMargin, radiusOval, 8);
        d.fillOval(x + sideMargin, y - buttomMargin, radiusOval + 15, 8);
        d.fillOval(x + sideMargin, y + 8 - buttomMargin, radiusOval, 8);
        d.setColor(new Color(255, 50, 0));
        d.fillCircle(x + sideMargin, y, 17);
    }

    private void drawTail(DrawSurface d) {
        int marginBottom = 20 + 30;
        int in = 15;
        int width = 30;
        d.setColor(Color.blue);
        if (this.lastDirection) {
            d.fillRectangle(this.paddle.getLeftX() - width / 2 + in, HEIGHT - this.paddle.getHeight() - marginBottom,
                    width, this.paddle.getHeight());
        } else {
            d.fillRectangle(this.paddle.getRightX() - width / 2 - in, HEIGHT - this.paddle.getHeight() - marginBottom,
                    width, this.paddle.getHeight());
        }
    }

    public void blockRemove(Block beingRemoved) {
        blockRemoveAnimation(beingRemoved);
    }

    private void blockRemoveAnimation(Block block) {
        int leftY = (int) block.getTopLeft().getY();
        int topX = (int) block.getTopLeft().getX();
        int rightY = (int) block.getBottomRight().getY();
        int bottomX = (int) block.getBottomRight().getX();

        Ball[] smallBalls = new Ball[20];
        Random random = new Random();

        for (int i = 0; i < smallBalls.length; i++) {
            int randomX = random.nextInt(bottomX - topX);
            randomX = randomX + topX;
            int randomY = random.nextInt(rightY - leftY);
            randomY = randomY + leftY;
            int randomRadius = random.nextInt(4) + 8;
            int randomSpeed = random.nextInt(2) + 1;
            int randomAngle = random.nextInt(360);
            Velocity velocity = Velocity.fromAngleAndSpeed(randomAngle, randomSpeed);
            smallBalls[i] = new Ball(randomX, randomY, randomRadius, block.getColor());
            smallBalls[i].setVelocity(velocity);
        }
        for (int i = 0; i < smallBalls.length; i++) {
            LinkedList<Drawable> animation = new LinkedList<Drawable>();
            for (int j = 0; j < 20; j++) {
                Ball currentBall = smallBalls[i];
                Velocity velocity = currentBall.getVelocity();
                int dx = (int) velocity.getChangeX();
                int dy = (int) velocity.getChangeY();
                Ball newBall = new Ball((int) currentBall.getX() + dx * j, (int) currentBall.getY() + dy * j,
                        currentBall.getRadius() - j / 2, block.getColor());
                newBall.setVelocity(velocity);
                animation.add(newBall);
            }
            this.animations.add(animation);
        }
    }

    private void drawHead(DrawSurface d) {
        int radius = 29;
        int margin = 23 + 28;
        int width = 60;
        d.setColor(PaddleHeadColor);
        if (this.lastDirection) {
            d.fillOval(this.paddle.getRightX() - width / 2, HEIGHT - radius - margin, width, radius);
        } else {
            d.fillOval(this.paddle.getLeftX() - width / 2, HEIGHT - radius - margin, width, radius);
        }
    }

    private void drawSideBar(DrawSurface drawSurface) {
        drawSurface.setColor(Color.green);
        drawSurface.setColor(Color.darkGray);
        drawSurface.fillRectangle(WIDTH, 0, SIDEWIDTH, 20 + HEIGHT);
        drawSurface.setColor(Color.green);
        // the seperator line
        drawSurface.drawLine(WIDTH, 130, WIDTH + SIDEWIDTH - 30, 130);
        int y = 30;
        int marginFromRight = 30;
        drawSurface.drawRectangle(WIDTH, y, SIDEWIDTH - marginFromRight, HEIGHT - 60);
        int middleSideBar = WIDTH + SIDEWIDTH / 2;
        ScoreIndicator scoreIndicator = new ScoreIndicator(this.score);
        scoreIndicator.betterDrawOn(drawSurface, WIDTH + 15, 100, 28);
        drawTimeHeld(drawSurface);
        drawRadar(drawSurface);
        drawTime(drawSurface);
        drawAltitude(drawSurface);
        drawAirSpeed(drawSurface);
        drawWeight(drawSurface);
        drawTemperature(drawSurface);
        drawPressure(drawSurface);
        drawSurface.setColor(Color.green);
        drawSurface.drawRectangle(0, 0, WIDTH + SIDEWIDTH - 1, HEIGHT - 1);

    }

    private void drawWeight(DrawSurface d) {
        int x = STARTSTATS;
        int y = 420 + 60 + 60 + 60 + 60;
        int size = 23;
        d.setColor(Color.green);
        d.drawText(x, y, "重量 : 21,485 (lb)", size);
    }

    private void drawPressure(DrawSurface d) {
        Random random = new Random();
        int add = random.nextInt(2);
        boolean plus = random.nextBoolean();
        if (plus)
            add = -add;
        if (LocalTime.now().getSecond() % 2 == 0) {
            if (!this.changePressure) {
                this.pressure = this.pressure + add;
                this.changePressure = true;
            }
        } else {
            this.changePressure = false;
        }

        int x = STARTSTATS;
        int y = 420 + SIDEBARSEPERATOR * 2;
        int size = 23;
        d.setColor(Color.green);
        d.drawText(x, y, "圧力 : 11." + this.pressure + " psi", size);
    }

    private void drawTemperature(DrawSurface d) {
        Random random = new Random();
        int add = random.nextInt(2);
        boolean plus = random.nextBoolean();
        if (plus)
            add = -add;
        if (LocalTime.now().getSecond() % 2 == 0) {
            if (!this.changeTemperature) {
                this.temperature = this.temperature + add;
                this.changeTemperature = true;
            }
        } else {
            this.changeTemperature = false;
        }

        int x = STARTSTATS;
        int y = 420 + SIDEBARSEPERATOR * 3;
        int size = 23;
        d.setColor(Color.green);
        d.drawText(x, y, "気温 : " + this.temperature + " °C", size);
    }

    private void drawAirSpeed(DrawSurface d) {
        Random random = new Random();
        int add = random.nextInt(5);
        boolean plus = random.nextBoolean();
        if (plus)
            add = -add;
        if (LocalTime.now().getSecond() % 2 == 0) {
            if (!this.changeAirSpeed) {
                this.airSpeed = this.airSpeed + add;
                this.changeAirSpeed = true;
            }
        } else {
            this.changeAirSpeed = false;
        }

        int x = STARTSTATS;
        int y = 420 + SIDEBARSEPERATOR;
        int size = 23;
        d.setColor(Color.green);
        d.drawText(x, y, "空速 : " + this.airSpeed + " km/h", size);
    }

    private void drawAltitude(DrawSurface d) {
        Random random = new Random();
        int add = random.nextInt(100);
        boolean plus = random.nextBoolean();
        if (plus)
            add = -add;
        if (LocalTime.now().getSecond() % 2 == 0) {
            if (!this.changeAltitude) {
                this.altitude = altitude + add;
                this.changeAltitude = true;
            }
        } else {
            this.changeAltitude = false;
        }

        int x = STARTSTATS;
        int y = 420;
        int size = 23;
        int altitude2digits = this.altitude / 1000;
        int altitude3digits = this.altitude % 1000;
        d.setColor(Color.green);
        if (altitude3digits < 10)
            d.drawText(x, y, "高度 : " + altitude2digits + ",00" + altitude3digits, size);
        else if (altitude3digits < 100)
            d.drawText(x, y, "高度 : " + altitude2digits + ",0" + altitude3digits, size);
        else
            d.drawText(x, y, "高度 : " + altitude2digits + "," + altitude3digits, size);
    }

    private void drawTime(DrawSurface d) {
        int hour = LocalTime.now().getHour();
        int minute = LocalTime.now().getMinute();
        int seconds = LocalTime.now().getSecond();
        int startX = STARTSTATS;
        int startY = 200;
        int textSize = 23;
        int marginBottom = 2;
        int gapBetweenDots = 10;
        int gap = 13;
        int dotRadius = 2;
        int betweenText = 40;
        int gapFromText = 70;
        String spaceSaver = "0";
        d.setColor(Color.green);
        d.drawText(startX, startY, "時間 :", textSize);
        d.drawText(startX + gapFromText, startY, "" + hour, textSize);
        d.fillCircle(startX + gapFromText + textSize + gap, startY - marginBottom, dotRadius);
        d.fillCircle(startX + gapFromText + textSize + gap, startY - marginBottom - gapBetweenDots, dotRadius);
        if (minute < 10)
            d.drawText(startX + betweenText + gapFromText, startY, spaceSaver + minute, textSize);
        else
            d.drawText(startX + gapFromText + betweenText, startY, "" + minute, textSize);
        d.fillCircle(startX + gapFromText + textSize + betweenText + gap, startY - marginBottom, dotRadius);
        d.fillCircle(startX + gapFromText + textSize + betweenText + gap, startY - marginBottom - gapBetweenDots,
                dotRadius);
        if (seconds < 10)
            d.drawText(startX + gapFromText + betweenText * 2, startY, spaceSaver + seconds, textSize);
        else
            d.drawText(startX + gapFromText + betweenText * 2, startY, "" + seconds, textSize);

    }

    private void drawRadar(DrawSurface d) {
        this.radarAngle = (this.radarAngle + 1) % 360;
        double howMuchBehind = 65;
        double centerX = WIDTH + 130;
        double centerY = 300;
        double numberOfLines = 200;
        double radius = 50;
        double thirdRadius = radius / 3;
        double radians = Math.toRadians(this.radarAngle);
        double cosAngle = Math.cos(radians);
        double sinAngle = Math.sin(radians);
        double lineEndX = centerX + cosAngle * radius;
        double lineEndY = centerY + sinAngle * radius;
        double fractureOfBehind = howMuchBehind / numberOfLines;
        d.setColor(Color.green);
        d.drawCircle((int) centerX, (int) centerY, (int) (radius - thirdRadius * 0));
        d.drawCircle((int) centerX, (int) centerY, (int) (radius - thirdRadius * 1));
        d.drawCircle((int) centerX, (int) centerY, (int) (radius - thirdRadius * 2));
        d.drawLine((int) centerX, (int) centerY, (int) lineEndX, (int) lineEndY);
        d.drawLine((int) centerX, (int) centerY, (int) (centerX + radius), (int) (centerY));
        d.drawLine((int) centerX, (int) centerY, (int) (centerX - radius), (int) (centerY));
        d.drawLine((int) centerX, (int) centerY, (int) (centerX), (int) (centerY + radius));
        d.drawLine((int) centerX, (int) centerY, (int) (centerX), (int) (centerY - radius));
        double Red = 0;
        double Green = 255;
        double Blue = 0;
        double DarkGray = 105;
        double fromRedToGray = DarkGray / numberOfLines;
        double fromGreenToGray = (255 - DarkGray) / numberOfLines;
        double fromBlueToGray = DarkGray / numberOfLines;
        for (int i = 0; i < numberOfLines; i++) {
            d.setColor(new Color((int) Red, (int) Green, (int) Blue));
            Red = i * fromRedToGray;
            Green = 255 - i * fromGreenToGray;
            Blue = i * fromBlueToGray;
            double behindAngle = (this.radarAngle - fractureOfBehind * i) % 360;
            double behindRadians = Math.toRadians(behindAngle);
            double behindCosAngle = Math.cos(behindRadians);
            double behindSinAngle = Math.sin(behindRadians);
            double behindLineEndX = centerX + behindCosAngle * radius;
            double behindLineEndY = centerY + behindSinAngle * radius;
            d.drawLine((int) centerX, (int) centerY, (int) behindLineEndX, (int) behindLineEndY);
        }
        if (230 <= this.radarAngle && this.radarAngle <= 340) {
            d.setColor(Color.green);
            d.fillCircle((int) (centerX - radius / 2), (int) (centerY - radius / 2), 5);
        }
        if (320 <= this.radarAngle || this.radarAngle <= 70) {
            d.setColor(Color.green);
            d.fillCircle((int) (centerX + radius / 2), (int) (centerY - radius / 2), 5);
        }
    }

    private void drawTimeHeld(DrawSurface d) {
        int length = 200;
        double fracture = (double) length / (double) 255;
        double currentLength = fracture * (double) this.timeHeld;
        int timeHeldWidth = (int) (20 + currentLength);
        int timeHeldHeight = 20;
        int timeHeldX = WIDTH + 20;
        int timeHeldy = HEIGHT - 120;
        int r = 0 + this.timeHeld;
        int g = 255 - this.timeHeld;
        int b = 0;
        d.setColor(new Color(r, g, b));
        d.fillRectangle(timeHeldX, timeHeldy, timeHeldWidth, timeHeldHeight);
        d.setColor(Color.green);
        d.drawRectangle(timeHeldX, timeHeldy, length + 20, timeHeldHeight);
    }

    public void ballDestroyAnimation(Ball ball) {
        int x = (int) ball.getX();
        int y = (int) ball.getY();

        Ball[] smallBalls = new Ball[10];
        Random random = new Random();

        for (int i = 0; i < smallBalls.length; i++) {
            int randomRadius = random.nextInt(6) + 8;
            int randomSpeed = random.nextInt(10) + 1;
            int randomAngle = random.nextInt(180) - 90;
            Velocity velocity = Velocity.fromAngleAndSpeed(randomAngle, randomSpeed);
            smallBalls[i] = new Ball(x, y, randomRadius, ball.getColor());
            smallBalls[i].setVelocity(velocity);
        }
        for (int i = 0; i < smallBalls.length; i++) {
            LinkedList<Drawable> animation = new LinkedList<Drawable>();
            for (int j = 0; j < 20; j++) {
                Ball currentBall = smallBalls[i];
                Velocity velocity = currentBall.getVelocity();
                int dx = (int) velocity.getChangeX();
                int dy = (int) velocity.getChangeY();
                Ball newBall = new Ball((int) currentBall.getX() + dx * j, (int) currentBall.getY() + dy * j,
                        currentBall.getRadius() - j / 2, ball.getColor());
                newBall.setVelocity(velocity);
                animation.add(newBall);
            }
            this.animations.add(animation);
        }
    }
}
