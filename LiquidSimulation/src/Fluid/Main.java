package Fluid;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Canvas implements Runnable {

    private static final long serialVersionUID = -668240625892092763L;

    public static final int WIDTH = 1500;
    public static final int HEIGHT = WIDTH * 9 / 16;

    private boolean running = false;
    private Thread gameThread;

    private List<Circle> circles;

    public Main() {
        canvasSetup();
        initialize(); 
        new Window("Fluid", this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Add a new circle where the user clicked
                circles.add(new Circle(e.getX(), e.getY(), Math.random() * 4 - 2, Math.random() * 4 - 2));
            }
        });
    }

    private void initialize() {
    	int NumOfParts;
    	@SuppressWarnings("resource")
        Scanner keyboard = new Scanner(System.in);
    	
    	System.out.println("Please enter the number of Cirlces you want: ");
    	NumOfParts = keyboard.nextInt();
        circles = new ArrayList<>();
        // Create multiple circles with different positions and velocities
        for (int i = 0; i < NumOfParts; i++ ) {
        circles.add(new Circle(WIDTH / 4, HEIGHT / 4, 2, 2));
        }
        // Add more circles as needed
    }

    private void canvasSetup() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update(delta);  // Pass delta and the list of circles to the update method
                delta--;
            }
            if (running) {
                draw();
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
            System.out.println("Frames: " + frames);
        }
        stop();
    }

    private void draw() {
        BufferStrategy buffer = this.getBufferStrategy();

        if (buffer == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = buffer.getDrawGraphics();
        drawBackground(g);
        // Draw each circle in the list
        for (Circle circle : circles) {
            circle.draw(g);
        }
        g.dispose();
        buffer.show();
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void update(double delta) {
        // Update each circle in the list
        for (Circle circle : circles) {
            circle.update(delta, WIDTH, HEIGHT, circles); // Pass delta, width, height, and the list of circles
        }
    }

    public synchronized void start() {
        if (!running) {
            gameThread = new Thread(this);
            gameThread.start();
            running = true;
        }
    }

    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Main(); 
    }

    public static int ensureRange(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }
}
