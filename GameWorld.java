import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameWorld extends World
{   
    static final int GROUND_HEIGHT = 24;
    
    double xPos;
    int groundY;
    
    // Sound
    GreenfootSound[] theme;
    
    public GameWorld()
    {    
        // Create a new world with 640x480 cells with a cell size of 1x1 pixels.
        super(640, 480, 1, false);
        this.groundY = getHeight() - GROUND_HEIGHT;
        
        // BACKGROUND
        this.setupBackground();
        this.setupObstacles();
        
        // GRAVITY
        this.setupGravity();
        
        // Add a Flamingo
        Flamingo fla = new Flamingo();
        fla.setCallback(new Flamingo.Callback() {
            public void onJumpStarted(Flamingo f) {
                if (ignoreGravity.indexOf(f) == -1)
                    ignoreGravity.add(f);
            }
            public void onJumpFinished(Flamingo f) {
                ignoreGravity.remove(f);
            }
            public void onCrashed(Flamingo f, Actor a) {
                System.out.println("Crashed with a " + a.getClass().getSimpleName());
            }
            public boolean isFloating(Flamingo f) {
                return GameWorld.this.isFloating(f);
            }
        });
        addGroundObject(fla, (getWidth()/2) - 24, getHeight() / 2);
        
        // SOUND
        theme = new GreenfootSound[] { 
            new GreenfootSound("iAmTheDoctor01.mp3"), 
            new GreenfootSound("iAmTheDoctor02.mp3") 
        };
        //theme[0].playLoop();
    }
    
    //
    // MAIN GAME EVENTS
    //
    //int bgScrollAmount = 2;
    static final int OBSTACLE_DISTANCE = 960;
    static final int SPEED_UP_EVERY = 100000;
    double lastObsX = 0;
    double lastSpeedup = 0;
    @Override
    public void act() {
        applyGravity();
        scrollBackground(1);
        
        // CONTROL
        /*if (Greenfoot.isKeyDown("right")) {
            scrollBackground(1);
        }*/
        /*if (Greenfoot.isKeyDown("left")) {
            scrollBackground(-2);
        }*/
        
        // SOUND
        playTheme();
        
        // ADD OBSTACLES
        
        if (xPos - lastObsX >= (OBSTACLE_DISTANCE / 4 * bgVelo)) {
            Grass grass = new Grass();
            addObstacle(grass);
            
            //System.out.println("lastObsX = " + lastObsX);
            lastObsX = xPos;
        }
        if (xPos - lastSpeedup >= SPEED_UP_EVERY) {
            bgVelo += 1;
            lastSpeedup = xPos;
            
            System.out.println("lastSpeedup = " + lastSpeedup + ", velo = " + bgVelo);
        }
    }
    
    @Override
    public void started() {
        //playTheme();
    }
    
    @Override
    public void stopped() {
        theme[0].stop();
        theme[1].stop();
    }
    
    // SOUND
    int track = 1;
    public void playTheme() {
        if (!theme[track].isPlaying()) {
            track++;
            track %= theme.length;
            theme[track].play();
            //System.out.println("Playing Track " + track);
        }
    }
    public void stopTheme() {
        for (GreenfootSound track : theme) {
            track.stop();
        }
    }
    
    //
    // GRAVITY SYSTEM
    //
    static final double GRAVITY = 9.8;
    HashMap<Actor, Double> velos;
    HashMap<Actor, Double> times;
    ArrayList<Actor> ignoreGravity;
    
    ArrayList<Actor> groundObjects;
    public void addGroundObject(Actor actor, int x, int y) {
        this.addObject(actor, x, y);
        groundObjects.add(actor);
        //System.out.println("groundObjects : " + groundObjects.size());
    }
    public void removeGroundObject(Actor actor) {
        this.removeObject(actor);
        groundObjects.remove(actor);
        //System.out.println("groundObjects : " + groundObjects.size());
    }
    
    public void setupGravity() {
        this.groundObjects = new ArrayList<>();
        
        this.ignoreGravity = new ArrayList<>();
        this.velos = new HashMap<>();
        this.times = new HashMap<>();
    }
    public int getBottomY(Actor obj) {
        return obj.getY() + (obj.getImage().getHeight() / 2);
    }
    public int distanceAboveGround(Actor obj) {
        int result = groundY - getBottomY(obj);
        //System.out.println(result + " px above ground");
        return result;
    }
    public boolean isFloating(Actor obj) {
        return distanceAboveGround(obj) > 0;
    }
    public void applyGravity() {
        for (int i = 0; i < groundObjects.size(); i++) {
            Actor obj = groundObjects.get(i);
            
            // ignore gravity for that object
            if (ignoreGravity.indexOf(obj) != -1) {
                //System.out.println("Ignore gravity");
                velos.put(obj, null);
                times.put(obj, null);
                continue;
            }
            
            //int realY = obj.getY() + (obj.getImage().getHeight() / 2);
            
            if (isFloating(obj)) {
                if (velos.get(obj) == null)
                    velos.put(obj, 0.0);
                if (times.get(obj) == null)
                    times.put(obj, 0.0);
                
                double v = velos.get(obj);
                double t = times.get(obj);
                
                v = v + (GRAVITY * t * 0.05);
            
                // if too far below ground -> GET BACK UP
                int newY = (int) (obj.getY() + v);
                obj.setLocation(obj.getX(), newY);
                if (getBottomY(obj) > groundY) {
                    newY = groundY - (obj.getImage().getHeight() / 2);
                    obj.setLocation(obj.getX(), newY);
                }
                
                t += 0.1;
            
                //System.out.println("v : " + v + "\t t : " + t);
                velos.put(obj, v);
                times.put(obj, t);
                
                // Animate Flamingo feet
                if (obj instanceof Flamingo)
                    ((Flamingo) obj).animateWalk(-1);
                    
                //System.out.println("NOT ON GROUND");
            }
            else if (velos.get(obj) != null) {
                velos.put(obj, null);
                times.put(obj, null);
            }
        }
    }
    
    //
    // OBSTACLES
    //
    ArrayList<Actor> obstacles;
    public void setupObstacles() {
        this.obstacles = new ArrayList<>();
    }
    public void addObstacle(Actor actor) {
        int x = getWidth() + (actor.getImage().getWidth() / 2);
        int y = getHeight() - GROUND_HEIGHT - (actor.getImage().getHeight() / 2);
        //int y = getHeight() / 2;
        addGroundObject(actor, x, y);
        obstacles.add(actor);
    }
    public void removeObstacle(Actor actor) {
        removeGroundObject(actor);
        obstacles.remove(actor);
    }
    public void scrollObstacles(int frames) {
        for (int i = 0; i < obstacles.size(); i++) {
            Actor obj = obstacles.get(i);
            obj.setLocation(obj.getX() - frames, obj.getY());
            
            // remove when offscreen
            if (obj.getX() < -(obj.getImage().getWidth() / 2)) {
                removeObstacle(obj);
                //System.out.println("grass removed");
            }
        }
    }
    
    //
    // SCROLLING BACKGROUND
    //
    Background[] bg;
    double bgVelo = 4;
    int maxBgX = (int)(getWidth() * 2.5);
    int minBgX = - (getWidth()/2);
    int centerY = getHeight()/2;
    public void setupBackground() {
        bg = new Background[3];
        
        bg[0] = new Background();
        addObject(bg[0], getWidth()/2, centerY);
        
        bg[1] = new Background();
        addObject(bg[1], (int)(getWidth() * 1.5), centerY);
        
        bg[2] = new Background();
        addObject(bg[2], maxBgX, centerY);
    }
    public void scrollBackground(int n) {
        for (int i = 0; i < Math.abs(n); i++)
            scrollBackground(n > 0);
    }
    public void scrollBackground(boolean forward) {
        for (int i = 0; i < bg.length; i++) {
            int newX = (int)(bg[i].getX() - (bgVelo * (forward ? 1:-1)));
            // DISTANCE
            //System.out.println("xPos = " + xPos);
            xPos += (bgVelo * (forward ? 1:-1));
            
            //int previous = i-1;
            //if (previous == -1)
            //    previous = bg.length - 1;
                
            if (newX <= minBgX) {
                newX = newX + minBgX + maxBgX;
            }
                //newX = bg[previous].getX() + getWidth();
            //System.out.println("newX : " + newX);
            bg[i].setLocation(newX, bg[i].getY());
        }
        scrollObstacles((int)bgVelo);
    }
}
