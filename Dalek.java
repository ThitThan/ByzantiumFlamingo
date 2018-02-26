import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Dalek here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Dalek extends Actor
{
    
    // SCALING
    public static double scale = 0.7;
    public static double width = 224 * scale;
    public static double height = 240 * scale;
    static GreenfootImage image;
    
    @Override
    public void addedToWorld(World world) {
        if (image == null) {
            image = new GreenfootImage("dalek-4.png");
            image.scale((int) width, (int) height);
        }
        //System.out.println("DALEK height : " + height + ", getHeight() : " + image.getHeight());
        setImage(image);
    }
    
    public void act() 
    {
        // Add your action code here.
    }
}
