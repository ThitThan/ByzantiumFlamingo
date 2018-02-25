import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Flamingo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Flamingo extends Actor
{
    // SCALING
    public static double scale = 0.15;
    public static double width = 449 * scale;
    public static double height = 729 * scale;
    
    // STATIC IMAGES
    static GreenfootImage[] images;
    
    // SPEED
    int walkingSpeed = 2;
    public int getWalkingSpeed() {
        return this.walkingSpeed;
    }
    public void setWalkingSpeed(int speed) {
        this.walkingSpeed = speed;
    }
    
    // CALLBACK
    public interface Callback {
        void onJumpStarted(Flamingo fla);
        void onJumpFinished(Flamingo fla);
        void onCrashed(Flamingo fla, Actor act);
        boolean isFloating(Flamingo fla);
    }
    Callback mCallback;
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
    
    @Override
    public void addedToWorld(World world) {
        images = new GreenfootImage[36];
        for (int i = 0; i < 36; i++) {
            images[i] = new GreenfootImage("Flamingo" + (i+1) + ".png");
             
            images[i].scale((int) width, (int) height);
        }
        setImage( images[0] );
    }
    
    boolean jumpPressed = false;
    boolean isJumping = false;
    boolean wasOnGround = false;
    int jumpCount = 0;
    public void act() {
        animateWalk(1);
        
        // CONTROLS
        /*if (Greenfoot.isKeyDown("right")) {
            animateWalk(1);
        }*/
        /*if (Greenfoot.isKeyDown("left")) {
            animateWalk(-2);
        }*/
        if (Greenfoot.isKeyDown("space") || Greenfoot.isKeyDown("up")) {
            if (!jumpPressed && v < START_SPEED * 2/3 && jumpCount < MAX_JUMP) {
                jump();
                jumpCount++;
                //System.out.println("JumpCount : " + jumpCount);
                jumpPressed = true;
            }
        }
        else {
            jumpPressed = false;
        }
        
        // reset jump counting
        if (mCallback != null && !mCallback.isFloating(this) && !isJumping && !wasOnGround) {
            jumpCount = 0;
            //System.out.println("[ONGROUND]");
            
            wasOnGround = true;
        }
        
        // JUMPING
        if (isJumping) {
            applyJumping();
        }
        
        // CHECK CRASHING
        
    }   
    
    // JUMPING
    //static final int JUMP_HEIGHT = 120;
    static final int MAX_JUMP = 2;
    static final double GRAVITY = 9.8;
    static final double START_SPEED = 12.34;
    double v = 0, t = 0;
    public void jump() {
        //System.out.println("Jumped!!");
        if (mCallback != null)
            mCallback.onJumpStarted(this);
          
        v = START_SPEED;
        t = 0;
        isJumping = true;
        //this.setLocation(getX(), getY() - JUMP_HEIGHT);
    }
    public void applyJumping() {
        v = v - (GRAVITY * t * 0.05);
        t += 0.1;
        
        if (v < START_SPEED * 2/3)
            animateWalk(-1);
        //frame = 6;
        
        this.setLocation(getX(), (int) (getY() - v));
        //System.out.println("v : " + v + "\t t : " + t);
        
        // SHOULD STOP?
        if (v <= 0) {
            v = 0;
            t = 0;
            isJumping = false;
            wasOnGround = false;
            
            if (mCallback != null)
                mCallback.onJumpFinished(this);
        }
    }
    
    // WALKING ANIMATION
    int frame = 0;
    public void animateWalk(int frames) {
        this.frame += (frames*walkingSpeed);
        
        if (this.frame >= 36)
            this.frame -= 36;
        else if (frame < 0)
            this.frame += 36;
        
        if (frame % 3 == 0)
            setImage(images[this.frame]);
    }
}
