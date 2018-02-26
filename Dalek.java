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
    
    // IMAGE
    static GreenfootImage image;
    
    // SFX
    static GreenfootSound[] sfx;
    
    @Override
    public void addedToWorld(World world) {
        if (image == null) {
            image = new GreenfootImage("dalek-4.png");
            image.scale((int) width, (int) height);
        }
        //System.out.println("DALEK height : " + height + ", getHeight() : " + image.getHeight());
        setImage(image);
        
        if (sfx == null) {
            initSound();
        }
    }
    @Override
    public void act() 
    {
        // Add your action code here.
    }
    
    // SFX
    public void initSound() {
        sfx = new GreenfootSound[2];
        
        sfx[0] = new GreenfootSound("dalek-stay.mp3");
        sfx[0].setVolume(45);
        
        sfx[1] = new GreenfootSound("dalek-emergency.mp3");
        sfx[1].setVolume(75);
        
        //sfx[2] = new GreenfootSound("dalek-exterminate.mp3");
        //sfx[2].setVolume(75);
        
        //sfx[1] = new GreenfootSound("dalek-destroy.mp3");
        //sfx[1].setVolume(80);
    }
    public void playSound() {
        int rand = Greenfoot.getRandomNumber(sfx.length);
        sfx[rand].play();
    }
}
