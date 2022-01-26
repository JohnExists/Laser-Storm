package me.johnexists.game1.world.objects.attributes;

import com.badlogic.gdx.math.Polygon;

public interface Collideable {
    Polygon getCollisionBounds();
    void collidesWith(Collideable collideable, Runnable runnable);

}
