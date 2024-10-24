package Fluid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Circle {
    private static final int SIZE = 20; // Diameter of the circle
    private static final double MAX_VELOCITY = 10.0; // Max velocity to cap
    private static final double DRAG = 0.995; // Drag to slow down particles

    public int x;  // X position of the circle
    public int y;  // Y position of the circle
    private double xVel;  // X velocity of the circle
    private double yVel;  // Y velocity of the circle
    private final double gravity = 0; // Gravity constant
    private final double damping = 0.9; // Damping factor for collision
    @SuppressWarnings("unused")
    private final double viscosity = 0.99; // Viscosity for fluid simulation
    private final double repulsion = 0.95; // Repulsion constant for particle interaction

    // Constructor to initialise the circle's position and velocity
    public Circle(int x, int y, double xVel, double yVel) {
        this.x = x;
        this.y = y;
        this.xVel = xVel;
        this.yVel = yVel;
    }

    // Method to draw the circle
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, SIZE, SIZE);  // Drawing the circle at (x, y) with SIZE diameter
    }

    // Method to update the circle's position and apply gravity and viscosity
    public void update(double delta, int width, int height, List<Circle> circles) {
        // Apply gravity to y velocity and update the y position based on delta time
        yVel += gravity;
        xVel *= DRAG; // Apply drag to x velocity
        yVel *= DRAG; // Apply drag to y velocity
        x += xVel * delta;
        y += yVel * delta;

        // Cap the velocity to avoid extremely high speeds
        xVel = Math.min(Math.max(xVel, -MAX_VELOCITY), MAX_VELOCITY);
        yVel = Math.min(Math.max(yVel, -MAX_VELOCITY), MAX_VELOCITY);

        // Handle collisions with the screen boundaries
        if (y + SIZE > height || y < 0) {
            yVel *= -damping;
            y = y + SIZE > height ? height - SIZE : y;
            y = y < 0 ? 0 : y;
        }
        if (x + SIZE > width || x < 0) {
            xVel *= -damping;
            x = x + SIZE > width ? width - SIZE : x;
            x = x < 0 ? 0 : x;
        }

        // Handle particle-particle interactions
        for (Circle other : circles) {
            if (other != this) {
                double dx = other.x - this.x;
                double dy = other.y - this.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                double minDistance = SIZE;

                if (distance < minDistance) {
                    double angle = Math.atan2(dy, dx);
                    double overlap = minDistance - distance;
                    double fx = overlap * Math.cos(angle) * repulsion;
                    double fy = overlap * Math.sin(angle) * repulsion;

                    // Apply force to the particles
                    xVel -= fx;
                    yVel -= fy;
                    other.xVel += fx;
                    other.yVel += fy;
                }
            }
        }
    }
}
