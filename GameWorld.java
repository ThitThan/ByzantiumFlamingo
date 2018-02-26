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
    // GROUND
    static final int GROUND_HEIGHT = 32;
    
    // DALEK
    static final int DALEK_DEFAULT_POS = -36;
    static final int DALEK_THREAT_POS = 48;
    
    // ACTORS
    Flamingo fla;
    Dalek dalek;
    Ground ground;
    
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
        
        // SOUND
        theme = new GreenfootSound[] { 
            new GreenfootSound("iAmTheDoctor01.mp3"), 
            new GreenfootSound("iAmTheDoctor02.mp3") 
        };
        //theme[0].playLoop();
        
        // Add a Flamingo
        fla = new Flamingo();
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
        
        // ENEMY (DALEK)
        dalek = new Dalek();
        setPaintOrder(Dalek.class, Grass.class);
        addGroundObject(dalek, DALEK_DEFAULT_POS, (int) (getHeight() * 0.25));
        
        // GROUND
        ground = new Ground(GROUND_HEIGHT);
        addObject(ground, 0, 0);
    }
    
    //
    // DALEK
    //
    boolean dalekCatchedUp = false;
    
    //
    // MAIN GAME EVENTS
    //
    //int bgScrollAmount = 2;
    static final int OBSTACLE_DISTANCE = 960;
    static final int SPEED_UP_EVERY = 50000;
    double lastObsX = 0;
    double lastSpeedup = 0;
    
    boolean dalekKeyPressed = false;
    boolean dalekSoundPlayed = false;

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
        if (xPos - lastObsX >= (OBSTACLE_DISTANCE / 4 * sceneVelo)) {
            Grass grass = new Grass();
            addObstacle(grass);
            
            //System.out.println("lastObsX = " + lastObsX);
            lastObsX = xPos;
        }
        
        // SPPED UP
        if (xPos - lastSpeedup >= SPEED_UP_EVERY) {
            sceneVelo += 1;
            lastSpeedup = xPos;
            
            System.out.println("lastSpeedup = " + lastSpeedup + ", velo = " + sceneVelo);
        }
        
        // ANIMATE DALEK IF NEEDED
        if (dalekCatchedUp) {
            if (!dalekSoundPlayed) {
                dalek.playSound();
                dalekSoundPlayed = true;
            }
            if (dalek.getX() < DALEK_THREAT_POS)
                dalek.setLocation(dalek.getX() + 2, dalek.getY());
        }
        else {
            dalekSoundPlayed = false;
            if (dalek.getX() > DALEK_DEFAULT_POS)
                dalek.setLocation(dalek.getX() - 2, dalek.getY());
        }
        
        // PRESS 'D' TO TEST DALEK
        if (Greenfoot.isKeyDown("d")) {
            if (!dalekKeyPressed) {
                dalekCatchedUp = !dalekCatchedUp;
                dalekKeyPressed = true;
                //System.out.println("d pressed");
            }
        }
        else {
            dalekKeyPressed = false;
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
        // add to world first
        addGroundObject(actor, 0, 0);
        
        // set location (put on the ground)
        int x = getWidth() + (actor.getImage().getWidth() / 2);
        //int x = getWidth() / 2;
        int y = getHeight() - GROUND_HEIGHT - (actor.getImage().getHeight()/2);
        System.out.println(getHeight() + " - " + GROUND_HEIGHT + " - (" + actor.getImage().getHeight() + " / 2) = " + y);
        //int y = getHeight() / 2;
        actor.setLocation(x, y);
        
        // add to ArrayList
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
    static final double DEFAULT_SCENE_VELO = 4;
    static final double BG_VELO = 0.5;
    
    double sceneVelo = 4;
    //double bgVelo = 0.5;
    
    double maxBgX = getWidth() * 2.5;
    double minBgX = - (getWidth()/2);
    int centerY = getHeight()/2;
    
    double[] bgX;
    
    public void setupBackground() {
        bg = new Background[3];
        
        bg[0] = new Background();
        addObject(bg[0], getWidth()/2, centerY);
        
        bg[1] = new Background();
        addObject(bg[1], (int)(getWidth() * 1.5), centerY);
        
        bg[2] = new Background();
        addObject(bg[2], (int) maxBgX, centerY);
        
        // x position in double
        bgX = new double[bg.length];
        for (int i = 0; i < bg.length; i++) {
            bgX[i] = bg[i].getX();
        }
        
        //System.out.println("minBgX = " + minBgX);
        //System.out.println("maxBgX = " + maxBgX);
    }
    public void scrollBackground(int n) {
        for (int i = 0; i < Math.abs(n); i++)
            scrollBackground(n > 0);
    }
    public void scrollBackground(boolean forward) {
        for (int i = 0; i < bg.length; i++) {
            double newX = bgX[i] - ((BG_VELO / DEFAULT_SCENE_VELO * sceneVelo) * (forward ? 1.0:-1.0));
            //double newX = bgX[i] - (1 * (forward ? 1:-1));
            
            // DISTANCE
            //System.out.print(newX + " ");
            xPos += (sceneVelo * (forward ? 1:-1));
            
            //System.out.print(" <= " + minBgX + " : " + (newX <= minBgX) + " ");
            
            if (newX <= minBgX) {
                //System.out.println("nope it's " + newX + " ");
                newX = newX + minBgX + maxBgX;
            }
            bgX[i] = newX;
            bg[i].setLocation((int) newX, bg[i].getY());
        }
        //System.out.println();
        scrollObstacles((int)sceneVelo);
    }
}
