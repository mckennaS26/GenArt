import java.awt.*;

public class Particle {
    // initialize x, y, color, radius
    private int x, y, radius, xDirection, xSpeed, yDirection, ySpeed;
    private Color color;

    private int width, height;

    // variables for circular motion
    private double angle;
    private double angularspeed ;
    private int orbitRadius;
    private int centerX;
    private int centerY;

    //life cycle variables
    private double timeSinceTouchedMilli; // last time a particle touched another
    private double nextSpawnAllowedMilli; // cooldown to prevent infinite spawning

    //initial setup of the particle
    public Particle(int x, int y, int radius, Color color, int windowWidth, int windowHeight, double spawTimer){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;

        xDirection = 1;
        yDirection = 1;

        xSpeed = (int)(4 + Math.random() * (4 - 3));
        ySpeed = (int)(4 + Math.random() * (4 - 3));

        width = windowWidth;
        height = windowHeight;

        centerX = randInt(0, windowWidth);
        centerY = randInt(0, windowHeight);
        angle = randInt(0, 50);
        angularspeed = randDouble(0.01, 0.5);
        orbitRadius = randInt(10, 40);

        timeSinceTouchedMilli = System.currentTimeMillis();
        nextSpawnAllowedMilli = System.currentTimeMillis() * spawTimer;
    }
    public int randInt(int min, int max) {
        return (int) (min + Math.random() * (max -min));
    }

    public double randDouble(double min, double max) {
        return min + Math.random() * (max - min);
    }

    public void draw(Graphics2D g){
        g.setColor(color);
        g.fillOval(x, y, radius*2, radius*2);
    }

    public void updateParticleAngular() {
       angle += angularspeed;
       x = (int) (centerX + orbitRadius * Math.cos(angle));
       y = (int) (centerY + orbitRadius * Math.sin(angle));
    }

   public void updateParticleLinear(){
       if(x <= 0 || x + (radius*2) > width){
            xDirection *= -1;
            if( x < 0) {
                x  = 0;
            }else if(x+(radius*2) > width) {
                x = width - (radius *2);
            }
        }
        if(y <= 0 ||y + (radius*2) > height){
            yDirection *= -1;
            if(y < 0) {
                y = 0;
            }else if (y+(radius*2) < height) {
                y = height - (radius*2);
            }
        }

        x += xSpeed * xDirection;
        y += ySpeed * yDirection;
    }

    public void updateParticleSize(){
        radius = randInt(5,25);
    }

    //helpers to spawn particles
    public int getRadius() {
        return radius;
    }

    public int getCenterX() {
        return radius + x;
    }

    public int getCenterY() {
        return radius + y;
    }

    public void touched(double nowMilli) {
        timeSinceTouchedMilli = nowMilli;
    }

    public boolean canSpawn(double nowMilli) {
        return nowMilli >= nextSpawnAllowedMilli;
    }

    public void setSpawnCooldown(double nowMilli, double cooldownMilli) {
        nextSpawnAllowedMilli = nowMilli + cooldownMilli;
    }

    public boolean shouldDie(double nowMilli, double idleDeathMilli) {
        return nowMilli - timeSinceTouchedMilli > idleDeathMilli;
    }
}
