package me.johnexists.game1.world.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;

import java.util.Objects;


public abstract class GameObject {

    protected Location location;
    protected Size size;
    protected int viewingOrderWeight;

    public GameObject(Location location) {
        this.location = location;
        this.size = new Size(50, 50);
        viewingOrderWeight = 0;
    }

    public abstract void update(float deltaTime);

    public abstract void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);
    public void dispose() {
        if(Objects.nonNull(location.getWorld())) {
            location.getWorld().getGameState().dispose();
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getViewingOrderWeight() {
        return viewingOrderWeight;
    }

}
