import  java.awt.*;
import  java.awt.event.*;
import java.sql.Array;
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

    // Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe
    {
     int x = pipeX;
     int y = pipeY;
     int width = pipeWidth;
     int height = pipeHeight;
     Image img;

     boolean passed = false;

     Pipe(Image img)
     {
         this.img = img;
     }
    }

    // Game Logic
    Bird bird;
    int velocityY = 0;
    int velocityX = -4; // moves pipes to the left
    int gravity = 1;
    ArrayList<Pipe>  pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;

    boolean isGameOver = false;
    double score = 0.0;

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
        pipes = new ArrayList<Pipe>();

        // Place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                placePipes();;
            }
        });

        placePipeTimer.start();

        // Game Timer
        gameLoop = new Timer(1000/60, this ); // 1000 / 60 fps = 16.6 mili seconds
        gameLoop.start();
    }

    public  void placePipes()
    {
        int randomPipeY = (int)(pipeY - pipeHeight / 4 - Math.random() * (pipeHeight/2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(botPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);

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


        // Pipes
        for (int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));

        if(isGameOver)
        {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else
        {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public  void move()
    {
        velocityY += gravity;

        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width)
            {
                pipe.passed = true;
                score += 0.5; // 0.5 points for each pipes
            }

            if(collision(bird, pipe))
            {
                isGameOver = true;
            }
        }

        if(bird.y > boardHeight) // If the bird falls below screen
        {
            isGameOver = true;
        }
    }

    public  boolean collision(Bird a, Pipe b)
    {
        return  a.x < b.x + b.width &&  // a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&  // a's top right corner doesn't passes b's top left corner
                a.y < b.y + b.height && // a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;   // a's bottom left corner doesn't passes b's top left corner
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();

        // Every 16 mili second repaint
        repaint();

        if(isGameOver)
        {
            placePipeTimer.stop();
            gameLoop.stop();
        }
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

            if(isGameOver)
            {
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                isGameOver = false;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
