package me.johnexists.game1.world.objects.attributes;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.johnexists.game1.world.World;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.entities.Entity;

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

    private void add(float x, float y) {
        Location newLocation = preventSurpassingBounds(new Location(this.x + x * getXSizeMultiplier(),
                this.y + y * getXSizeMultiplier(), world));
        this.x = newLocation.getX();
        this.y = newLocation.getY();
    }

    public void moveTowards(GameObject gameObject, float amount) {
        Location distanceToTarget = distanceTo(gameObject);
        Vector2 vectorToTarget = new Vector2(distanceToTarget.getX(), distanceToTarget.getY());
        vectorToTarget.nor();
        vectorToTarget.x *= amount;
        vectorToTarget.y *= amount;
        add(vectorToTarget.x, vectorToTarget.y);
    }

    public void add(Velocity velocity) {
        Location newLocation = preventSurpassingBounds(new Location(this.x + velocity.getVector().x * getXSizeMultiplier(),
                this.y + velocity.getVector().y * getXSizeMultiplier(), world));
        this.x = newLocation.getX();
        this.y = newLocation.getY();
    }

    public Location distanceTo(GameObject gameObject) {
        assert gameObject != null;
        return new Location(gameObject.getLocation().getX() - x,
                        gameObject.getLocation().getY() - y, getWorld());

    }

    public boolean isXLesserThan(float x) {
        return !(Math.abs(this.x) > x * Size.getXSizeMultiplier());
    }
    public boolean isYLesserThan(float y) {
        return !(Math.abs(this.y) > y * Size.getYSizeMultiplier());
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

    private static Location preventSurpassingBounds(Location location) {
        return new Location(MathUtils.clamp(location.getX(), 50 * getXSizeMultiplier(),  World.MAP_X - 50 * getXSizeMultiplier()),
                MathUtils.clamp(location.getY(), 50 * getYSizeMultiplier(), World.MAP_Y - 50 * getYSizeMultiplier()));
    }
}
