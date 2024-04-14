import  java.awt.*;
import  java.awt.event.*;
import  java.util.ArrayList;
import  java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements  ActionListener, KeyListener
{
    int boardWidth = 360;
    int boardHeight= 640;

    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image botPipeImg;

    // Bird
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;




    class Bird
    {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;

        Image img;

        Bird(Image img)
        {
            this.img = img;
        }
    }
    // Game Logic
    Bird bird;
    Timer gameLoop;
    int velocityY = 0;
    int gravity = 1;
    FlappyBird()
    {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
   //     setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this); // Needed for the key listeners to work

        backgroundImg  = new ImageIcon(getClass().getResource("./Arts/flappybirdbg.png")).getImage();
        birdImg  = new ImageIcon(getClass().getResource("./Arts/flappybird.png")).getImage();
        topPipeImg  = new ImageIcon(getClass().getResource("./Arts/toppipe.png")).getImage();
        botPipeImg  = new ImageIcon(getClass().getResource("./Arts/bottompipe.png")).getImage();

        bird = new Bird(birdImg);

        // Game Timer
        gameLoop = new Timer(1000/60, this ); // 1000 / 60 fps = 16.6 mili seconds
        gameLoop.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public  void draw(Graphics g)
    {   // Background
        g.drawImage(backgroundImg, 0,0, boardWidth,boardHeight, null);

        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
    }

    public  void move()
    {
        velocityY += gravity;

        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();

        // Every 16 mili second repaint
        repaint();
    }
    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            velocityY = -9;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
