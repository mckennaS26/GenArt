import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow extends JPanel {
    //1 initial setup of window
    //1 Panel is the Canvas where we will draw the Art
    // extend makes the MainWindow object a drawable object
    private final int WINDOW_WIDTH = 1920;
    private final int WINDOW_HEIGHT = 1080;

    private final int NUM_PARTICLES = 1000;
    //Particle p;

    ArrayList<Particle> particles;

    public MainWindow() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)); // starting size of window


        particles = new ArrayList<>();

        for(int i = 0; i < NUM_PARTICLES; i++){
            // min + Maht.random() * (max - min)
            int x = randInt(0, WINDOW_WIDTH);
            int y = randInt(0, WINDOW_HEIGHT);
            int radius = randInt(5, 15);
            int red = randInt(0, 255);
            int green = randInt(0, 255);
            int blue = randInt(0, 255);

            // create a local radius variable, and assign a value btwn 5-15
            //create RBG values btwn 0-255 to assing a random Color to new Color
            particles.add(new Particle(x, y, radius, new Color(red, green, blue), WINDOW_WIDTH, WINDOW_HEIGHT));

        }
        //runs every 16 milliseconds (1000/16 ~ 60fps)
        Timer timer = new Timer(50, new ActionListener() {
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
    public static void main(String[] args){
        // frame is the window (bar, close button, resizing)
        JFrame frame = new JFrame ("Title Here");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // actually quit the program when the window closes

        // initial setup of the window
        MainWindow mainWindow = new MainWindow();
        frame.setContentPane(mainWindow); // puts our drawable object int the frame
        frame.pack(); // sizes the frame to match our preferred size
        frame.setLocationRelativeTo(null); // opens the window in the middle of the screen
        frame.setVisible(true); // false would hide the window

    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for(Particle pTemp : particles) {
            pTemp.draw(g2);
        }
    }

    public void update() {
       for( Particle pTemp : particles){
           pTemp.updateParticleAngular();
           pTemp.updateParticleLinear();
           pTemp.updateParticleSize();
       }
    }
}
