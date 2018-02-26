import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class GameOver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameOver extends World
{
    GreenfootSound dwTheme;
    
    GreenfootImage[] bg;
    
    boolean skipLightning = false;
    
    public GameOver()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(640, 480, 1); 
        
        bg = new GreenfootImage[16];
        for (int i = 0; i < bg.length; i++) {
            bg[i] = new GreenfootImage("timevortex" + i + ".jpg");
            bg[i].scale(640, 480);
        }
        
        animateBg(1);
        
        // setup sound
        dwTheme = new GreenfootSound("dw-intro.mp3");
        //dwTheme.playLoop();
    }
    
    public void act() {
        animateBg(1);
        dwTheme.playLoop();
    }
    
    // WALKING ANIMATION
    static final double START_FRAME = 4;
    
    double frame = START_FRAME;
    double bgSpeed = 0.15;
    public void animateBg(int frames) {
        this.frame += (frames * bgSpeed);
        
        //Skip Lightning
        /*if (this.frame >= bg.length - 3) {
            if (skipLightning)
                this.frame++;
            else
                skipLightning = true;
        }*/
        
        if (this.frame >= bg.length)
            this.frame -= (bg.length - START_FRAME);
        else if (frame < START_FRAME)
            this.frame += bg.length;
        
        //if (frame % 3 == 0)
            setBackground(bg[(int) this.frame]);
    }
    
    @Override
    public void started() {
        //dwTheme.playLoop();
    }
    
    @Override
    public void stopped() {
        frame = 0;
        dwTheme.stop();
    }
}
