package me.johnexists.game1.world.objects.attributes;

import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.entities.Entity;
import me.johnexists.game1.world.World;

import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

public class Location {

    private float x, y;
    private final World world;

    public Location(float x, float y) {
        this.world = null;
        this.x = x;
        this.y = y;
    }

    public Location(float x, float y, World world) {
        this.world = world;
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public World getWorld() {
        return world;
    }

    public void add(float x, float y) {
        this.x += x * getXSizeMultiplier();
        this.y += y * getYSizeMultiplier();
    }

    public void add(Location location) {
        this.x += location.getX() * getXSizeMultiplier();
        this.y += location.getY() * getYSizeMultiplier();
    }

    public void add(Velocity velocity) {
        this.x += velocity.getVector().x * getXSizeMultiplier();
        this.y += velocity.getVector().y * getYSizeMultiplier();
    }

    public Location distanceTo(GameObject gameObject) {
        assert gameObject != null;
        return new Location(gameObject.getLocation().getX() - x,
                        gameObject.getLocation().getY() - y, getWorld());

    }

    public boolean isXGreaterThan(float x) {
        return Math.abs(this.x) > x * Size.getXSizeMultiplier();
    }
    public boolean isYGreaterThan(float y) {
        return Math.abs(this.y) > y * Size.getYSizeMultiplier();
    }


    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", world=" + world +
                '}';
    }

    public static Location getCircleLoc(CircleShape circleShape) {
        Entity circleEntity = (Entity) circleShape;
        return new Location(circleEntity.getLocation().getX() - circleEntity.getSize().getWidth() / 2,
                circleEntity.getLocation().getY() - circleEntity.getSize().getHeight() / 2);
    }
}
