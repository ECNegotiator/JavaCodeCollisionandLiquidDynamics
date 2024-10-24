package Collisions;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Particle {
    private static final Random RANDOM = new Random();
    private int size; // Size of the particle (diameter)
    private int x, y; // Position of the particle
    private int xVel, yVel; // Velocity of the particle
    private int speed; // Speed of the particle
    private int radius; // Radius of the particle
    private Color color; // Colour of the particle

    // Constructor
    public Particle(int size, int speed, Color color, int startX, int startY) {
        this.size = size;
        this.speed = speed;
        this.color = color;
        this.radius = size / 2;
        reset(startX, startY);
    }

    // Resets the particle to a specified position with random velocity
    public void reset(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.xVel = getRandomVelocity();
        this.yVel = getRandomVelocity();
    }

    // Generates a random velocity
    private int getRandomVelocity() {
        return (RANDOM.nextBoolean() ? 1 : -1) * (RANDOM.nextInt(speed) + 1);
    }

    // Getters for position
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Change the direction of movement on the Y axis
    public void changeYDir() {
        yVel *= -1;
    }

    // Change the direction of movement on the X axis
    public void changeXDir() {
        xVel *= -1;
    }

    // Draw the particle on the screen
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - radius, y - radius, size, size);
    }

    // Update the particle's position and check for collisions
    public void update(int panelWidth, int panelHeight, Particle[] particles) {
        x += xVel;
        y += yVel;

        // Handle collisions with walls
        if (y + size >= panelHeight || y <= 0) {
            y = Math.max(0, Math.min(panelHeight - size, y)); // Prevent sticking
            changeYDir();
        }
        if (x + size >= panelWidth || x <= 0) {
            x = Math.max(0, Math.min(panelWidth - size, x)); // Prevent sticking
            changeXDir();
        }

        // Check for collisions with other particles
        for (Particle p : particles) {
            if (p != this && checkCollision(p)) {
                resolveCollision(p);
            }
        }
    }

    // Check for collision with another particle
    private boolean checkCollision(Particle other) {
        int dx = other.getX() - x;
        int dy = other.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= (radius + other.getRadius());
    }

    // Resolve collision with another particle
    private void resolveCollision(Particle other) {
        int dx = other.getX() - x;
        int dy = other.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Prevent division by zero
        if (distance == 0) {
            distance = 1;
        }

        double overlap = (radius + other.getRadius()) - distance;

        // Normalize the displacement vector
        dx /= distance;
        dy /= distance;

        // Adjust positions to resolve collision
        x -= dx * overlap / 2;
        y -= dy * overlap / 2;
        other.setPosition(other.getX() + (int)(dx * overlap / 2), other.getY() + (int)(dy * overlap / 2));

        // Swap velocities (simplified elastic collision response)
        int tempXVel = xVel;
        int tempYVel = yVel;
        xVel = other.xVel;
        yVel = other.yVel;
        other.xVel = tempXVel;
        other.yVel = tempYVel;
    }

    // Get the radius of the particle
    public int getRadius() {
        return radius;
    }

    // Set the position of the particle
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Stop the particle's movement
    public void stop() {
        xVel = 0;
        yVel = 0;
    }

    // Switch direction based on the provided value
    public void switchDirection(int direction) {
        xVel = speed * direction;
        yVel = speed * direction;
    }
}
