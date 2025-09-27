package net.felinamods.hg.utils;

public class ClientAnimUtils {
    public static String currentState = "";
    public static double animElapsed = 0;
    public static int ammo = 0;
    public static double animStartTick = -1;

    public static void update(String anim, double start, double elapsed) {
        currentState = anim;
        animStartTick = start;
        animElapsed = elapsed;
    }

    public static void setState(String anim) {
        currentState = anim;
       // System.out.println("state changed to " + anim);
    } 

    public static void setAmmo(int value) {
        ammo = value;
    }   

    public static void setStartTick(double value) {
        animStartTick = value;
    }     
}

