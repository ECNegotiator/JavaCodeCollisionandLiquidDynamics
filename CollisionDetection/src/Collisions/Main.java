package Collisions;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Main extends Canvas implements Runnable {

    private static final long serialVersionUID = -668240625892092763L;

    public static final int WIDTH = 1000;
    public static final int HEIGHT = WIDTH * 9 / 16;

    private boolean running = false;
    private Thread gameThread;

    // Array to store and manage particles
    private Particle[] particles;

    public Main() {
        canvasSetup();
        initialize();
        new Window("Collisions", this);
    }

    private void initialize() {
        particles = new Particle[4];
        particles[0] = new Particle(120, 5, Color.BLUE, 45, 45);
        particles[1] = new Particle(100, 4, Color.RED, 200, 150);
        particles[2] = new Particle(80, 6, Color.GREEN, 400, 300);
        particles[3] = new Particle(110, 2, Color.WHITE, 300, 200);

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
                update();
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

        for (Particle particle : particles) {
            particle.draw(g);
        }

        g.dispose();
        buffer.show();
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void update() {
        for (Particle particle : particles) {
            particle.update(WIDTH, HEIGHT, particles);
        }
    }

    public synchronized void start() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            running = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int sign(double d) {
        return (d < 0) ? -1 : 1;
    }

    public static void main(String[] args) {
        new Main().start(); // Start the game loop
    }

    public static int ensureRange(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }
}
