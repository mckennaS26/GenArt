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

    //initial setup of the particle
    public Particle(int x, int y, int radius, Color color, int windowWidth, int windowHeight){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;

        xDirection = 1;
        yDirection = 1;

        xSpeed = (int)(2 + Math.random() * (5 - 2));
        ySpeed = (int)(2 + Math.random() * (5 - 2));

        width = windowWidth;
        height = windowHeight;

        centerX = randInt(0, windowWidth);
        centerY = randInt(0, windowHeight);
        angle = randInt(0, 50);
        angularspeed = randDouble(0.01, 0.5);
        orbitRadius = randInt(10, 40);
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
       if(Math.random() >= .97 || x + (radius*2) > width){
            xDirection *= -1;
        }
        if(Math.random() >= .98 || y + (radius*2) > height){
            yDirection *= -1;
        }

        x += xSpeed * xDirection;
        y += ySpeed * yDirection;

    }

    public void updateParticleSize(){
        radius = randInt(5,25);
    }
}
