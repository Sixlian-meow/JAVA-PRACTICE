package com.itheima.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class HelloWorld extends JPanel implements ActionListener, KeyListener {

    private static final int TILE_SIZE = 25;
    private static final int BOARD_WIDTH = 25;
    private static final int BOARD_HEIGHT = 25;
    private static final int SCREEN_WIDTH = TILE_SIZE * BOARD_WIDTH;
    private static final int SCREEN_HEIGHT = TILE_SIZE * BOARD_HEIGHT;
    private static final int INITIAL_DELAY = 120;

    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private int directionX = 1;
    private int directionY = 0;
    private boolean running = false;
    private boolean gameOver = false;
    private int score = 0;
    private Timer timer;
    private final Random random = new Random();

    public HelloWorld() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
        directionX = 1;
        directionY = 0;
        score = 0;
        gameOver = false;
        running = true;
        spawnFood();
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(INITIAL_DELAY, this);
        timer.start();
    }

    private void spawnFood() {
        int x, y;
        do {
            x = random.nextInt(BOARD_WIDTH);
            y = random.nextInt(BOARD_HEIGHT);
        } while (isSnakeAt(x, y));
        food = new Point(x, y);
    }

    private boolean isSnakeAt(int x, int y) {
        for (Point p : snake) {
            if (p.x == x && p.y == y) return true;
        }
        return false;
    }

    private void move() {
        Point head = new Point(snake.get(0).x + directionX, snake.get(0).y + directionY);

        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT) {
            running = false;
            gameOver = true;
            timer.stop();
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(i).x == head.x && snake.get(i).y == head.y) {
                running = false;
                gameOver = true;
                timer.stop();
                return;
            }
        }

        snake.add(0, head);

        if (head.x == food.x && head.y == food.y) {
            score += 10;
            spawnFood();
            if (timer.getDelay() > 50) {
                timer.setDelay(timer.getDelay() - 2);
            }
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (gameOver) {
            drawGameOver(g);
            return;
        }

        g.setColor(new Color(50, 50, 50));
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * TILE_SIZE, SCREEN_WIDTH, i * TILE_SIZE);
        }

        g.setColor(Color.RED);
        g.fillOval(food.x * TILE_SIZE + 2, food.y * TILE_SIZE + 2, TILE_SIZE - 4, TILE_SIZE - 4);

        for (int i = 0; i < snake.size(); i++) {
            if (i == 0) {
                g.setColor(new Color(0, 200, 0));
            } else {
                g.setColor(new Color(45, 180, 0));
            }
            g.fillRect(snake.get(i).x * TILE_SIZE + 1, snake.get(i).y * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
        FontMetrics fm = getFontMetrics(g.getFont());
        String scoreText = "得分: " + score;
        g.drawString(scoreText, (SCREEN_WIDTH - fm.stringWidth(scoreText)) / 2, 30);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.setColor(Color.RED);
        g.setFont(new Font("Microsoft YaHei", Font.BOLD, 48));
        String title = "游戏结束";
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString(title, (SCREEN_WIDTH - fm.stringWidth(title)) / 2, SCREEN_HEIGHT / 2 - 40);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        String scoreText = "最终得分: " + score;
        fm = getFontMetrics(g.getFont());
        g.drawString(scoreText, (SCREEN_WIDTH - fm.stringWidth(scoreText)) / 2, SCREEN_HEIGHT / 2 + 10);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));
        String hint = "按 空格键 重新开始";
        fm = getFontMetrics(g.getFont());
        g.drawString(hint, (SCREEN_WIDTH - fm.stringWidth(hint)) / 2, SCREEN_HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameOver && key == KeyEvent.VK_SPACE) {
            startGame();
            return;
        }

        switch (key) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (directionX != 1) { directionX = -1; directionY = 0; }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (directionX != -1) { directionX = 1; directionY = 0; }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (directionY != 1) { directionX = 0; directionY = -1; }
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (directionY != -1) { directionX = 0; directionY = 1; }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("贪吃蛇");
            HelloWorld game = new HelloWorld();
            frame.add(game);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
