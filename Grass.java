import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Grass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Grass extends Actor
{
    // SCALING
    public static double scale = 0.18;
    public static double width = 640 * scale;
    public static double height = 256 * scale;
    static GreenfootImage image;
    
    @Override
    public void addedToWorld(World world) {
        if (image == null) {
            image = new GreenfootImage("grass-blue-2.png");
            image.scale((int) width, (int) height);
            //System.out.println("GRASS height : " + height + ", getHeight() : " + image.getHeight());
        }
        System.out.println("GRASS height : " + height + ", getHeight() : " + image.getHeight());
        setImage(image);
    }
    
    public void act() 
    {
        // Add your action code here.
    }
}
