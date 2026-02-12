import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JPanel {
    //1 initial setup of window
    //1 Panel is the Canvas where we will draw the Art
    // extend makes the MainWindow object a drawable object
    private final int WINDOW_WIDTH = 2560;
    private final int WINDOW_HEIGHT = 1440;

    private final int NUM_PARTICLES = 10;

    private final int SPAWN_DISTANCE = 40;
    private final int SPAWN_TIMER = 1000;
    private final int SPAWN_COUNT = 3;
    private final int MAX_PARTICLES = 5000;

    private final int DEATH_TIMER = 2000;
    //Particle p;

    ArrayList<Particle> particles;

    public MainWindow() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // starting size of window


        particles = new ArrayList<>();

        for(int i = 0; i < NUM_PARTICLES; i++) {
            // min + Maht.random() * (max - min)
            int x = randInt(0, WINDOW_WIDTH);
            int y = randInt(0, WINDOW_HEIGHT);
            int radius = randInt(5, 15);
            int red = randInt(0, 255);
            int green = randInt(0, 255);
            int blue = randInt(0, 255);

            // create a local radius variable, and assign a value btwn 5-15
            //create RBG values btwn 0-255 to assing a random Color to new Color
            particles.add(new Particle(x, y, radius, new Color(red, green, blue), WINDOW_WIDTH, WINDOW_HEIGHT, SPAWN_TIMER));

        }
        //runs every 16 milliseconds (1000/16 ~ 60fps)
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();

            }
        });
        timer.start();
    }

    public int  randInt(int min, int max) {
        return (int)(min + Math.random() * (max - min));
    }

    public static void main(String[] args) {
        // frame is the window (bar, close button, resizing)
        JFrame frame = new JFrame ("Mckenna Straup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // actually quit the program when the window closes

        // initial setup of the window
        MainWindow mainWindow = new MainWindow();
        frame.setContentPane(mainWindow); // puts our drawable object int the frame
        frame.pack(); // sizes the frame to match our preferred size
        frame.setLocationRelativeTo(null); // opens the window in the middle of the screen
        frame.setVisible(true); // false would hide the window

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for(Particle pTemp : particles) {
            pTemp.draw(g2);
        }
    }

    public void update() {
       for( Particle pTemp : particles){
           pTemp.linearMotion();
          // pTemp.updateParticleAngular();
           //pTemp.updateParticleSize();
       }

       double now = System.currentTimeMillis();
       ArrayList<Particle> particlesToAdd = new ArrayList<>();

       for(int i = 0; i < particles.size(); i++) {
           Particle a = particles.get(i);
           for(int j = i+1; j < particles.size(); j++) {
               Particle b = particles.get(j);

               if (isTouching(a, b)) {
                   //System.out.println("Touching (" + a.getCenterX() + ", " + a.getCenterY() + ") (" + b.getCenterX() + ", " + b.getCenterY() + ")");
                   //reset spawn timers
                   a.touched(now);
                   b.touched(now);

                   if(a.canSpawn(now) && b.canSpawn(now)) {
                       a.setSpawnCooldown(now, SPAWN_TIMER);
                       b.setSpawnCooldown(now, SPAWN_TIMER);
                       for(int k = 0; k < SPAWN_COUNT; k++) {
                           if(particles.size() < MAX_PARTICLES) {
                               Particle toAdd = spawnParticleNear(a, b);
                               particlesToAdd. add(toAdd);
                           }
                       }
                   }
               }
           }
       }
       particles.addAll(particlesToAdd);

       for(int i = particles.size() - 1; i >= 0; i--) {
           Particle p = particles.get(i);
           if(p.shouldDie(now, DEATH_TIMER)) {
               particles.remove(p);
           }
       }

       if(particles.size() > MAX_PARTICLES || particles.isEmpty()) {
           particles.clear();
           for(int i = 0; i < NUM_PARTICLES; i++) {
               // min + Maht.random() * (max - min)
               int x = randInt(0, WINDOW_WIDTH);
               int y = randInt(0, WINDOW_HEIGHT);
               int radius = randInt(5, 15);
               int red = randInt(0, 255);
               int green = randInt(0, 255);
               int blue = randInt(0, 255);

               // create a local radius variable, and assign a value btwn 5-15
               //create RBG values btwn 0-255 to assing a random Color to new Color
               particles.add(new Particle(x, y, radius, new Color(red, green, blue), WINDOW_WIDTH, WINDOW_HEIGHT, SPAWN_TIMER));

           }
       }
    }

    private boolean isTouching(Particle a, Particle b) {
        // difference btwn the 2 centers
        double deltaX = a.getCenterX() - b.getCenterX();
        double deltaY = a.getCenterY() - b.getCenterY();

        //distance btwn centers squared

        double distanceBetweenCentersSquared = deltaX * deltaX + deltaY * deltaY;

        // sum of both particle radii
        double combinedRadius = a.getRadius() + b.getRadius();

        //if distance btwn centers is less than or equal to combeined radius, the particles are touching or overlapping
        return distanceBetweenCentersSquared <+ combinedRadius * combinedRadius;
    }

    private Particle spawnParticleNear (Particle a, Particle b) {
        // midpoint btwn the centers of the particles
        double collisionMidpointX = (a.getCenterX() * b.getCenterX()) / 2.0;
        double collisionMidpointY = (a.getCenterY() * b.getCenterY()) / 2.0;

        //random offset to spread out particles
        int randomOffsetX = randInt(-SPAWN_DISTANCE, SPAWN_DISTANCE);
        int randomOffsetY = randInt(-SPAWN_DISTANCE, SPAWN_DISTANCE);

        int spawnPositionX = (int)(collisionMidpointX + randomOffsetX);
        int spawnPositionY = (int)(collisionMidpointY + randomOffsetY);

        Color randomColor = new Color(randInt(0, 255), randInt(0, 255), randInt(0, 255));

        return new Particle(spawnPositionX, spawnPositionY, (a.getRadius() + b.getRadius())/2, randomColor, WINDOW_WIDTH, WINDOW_HEIGHT, SPAWN_TIMER);



    }
}
