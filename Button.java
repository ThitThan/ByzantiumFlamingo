import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class Button extends Actor
{
    interface Callback {
        void onClick(Button button);
    }
    
    Callback mCallback;
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
    
    public void addedToWorld(World world) {
        
    }
    
    public void act() 
    {
        if (Greenfoot.mouseClicked(this)) {
            //Greenfoot.setWorld(new MyWorld());
            if (this.mCallback != null)
                this.mCallback.onClick(this);
        }
    }
}
