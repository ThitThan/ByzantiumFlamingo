import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Bg here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Background extends Actor
{
    public static int width = -1;
    public static int height = -1;
    
    GreenfootImage bg;
    public void addedToWorld(World w) {
        bg = new GreenfootImage("dark-bg-5.jpg");
        if (width == -1)
            width = w.getWidth();
        if (height == -1)
            height = w.getHeight();
        bg.scale(width, height);
        this.setImage(bg);
    }
    public void act() 
    {
        // Add your action code here.
    }    
}
