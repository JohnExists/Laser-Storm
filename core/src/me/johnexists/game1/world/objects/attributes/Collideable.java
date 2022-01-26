package me.johnexists.game1.objects.attributes;

import com.badlogic.gdx.math.Polygon;

public interface Collideable {
    Polygon getCollisionBounds();
    void collidesWith(Collideable collideable, Runnable runnable);

}
