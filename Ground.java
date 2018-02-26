import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Ground here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ground extends Actor
{
    int height;
    public Ground(int height) {
        this.height = height;
    }
    
    GreenfootImage bg;
    public void addedToWorld(World world) {
        bg = new GreenfootImage(world.getWidth(), height);
        bg.setColor(new Color(0x26, 0x2F, 0x4A));
        bg.fillRect(0, 0, world.getWidth(), height);
        //bg = new GreenfootImage("starry-night.gif");
        this.setImage(bg);
        
        this.setLocation(world.getWidth()/2, world.getHeight() - (height/2));
    }
}
