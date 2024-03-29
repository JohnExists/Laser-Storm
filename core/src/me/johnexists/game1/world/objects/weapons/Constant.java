package me.johnexists.game1.world.objects.weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface Constant {

    Constant[] returnList();
    void dispose();
    Sprite getSprite();
    boolean isUnlocked();
    void unlock();
    int getCost();
}
