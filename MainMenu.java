import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MainMenu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MainMenu extends World
{
    GreenfootSound dwTheme;
    
    GreenfootImage bg;
    
    /**
     * Constructor for objects of class MainMenu.
     * 
     */
    public MainMenu()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(640, 480, 1); 
        
        // DARK BACKGROUND
        bg = new GreenfootImage(getWidth(), getHeight());
        bg.setColor(new Color(25,25,25));
        bg.fillRect(0, 0, getWidth(), getHeight());
        //bg = new GreenfootImage("starry-night.gif");
        this.setBackground(bg);
        
        
        // setup sound
        dwTheme = new GreenfootSound("dw-intro.mp3");
        //dwTheme.playLoop();
        
        // setup and add button
        Button button = new Button();
        button.setCallback(new Button.Callback() {
            public void onClick(Button button) {
                dwTheme.stop();
                Greenfoot.setWorld(new GameWorld());
            }
        });
        addObject(button, getWidth() / 2, getHeight() / 2);
    }
    
    @Override
    public void act() {
        dwTheme.playLoop();
    }
    
    @Override
    public void started() {
        //dwTheme.playLoop();
    }
    
    @Override
    public void stopped() {
        dwTheme.stop();
    }
}
