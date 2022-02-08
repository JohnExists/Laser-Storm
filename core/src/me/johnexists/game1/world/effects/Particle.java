package me.johnexists.game1.world.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.attributes.Location;

public abstract class Particle {

    protected float life;
    protected final Location location;
    protected final ShapeRenderer shapeRenderer;
    protected final SpriteBatch spriteBatch;
    protected float deltaTime;

    public Particle(Location location, float deltaTime) {
        this.location = location;
        this.deltaTime = deltaTime;
        this.shapeRenderer = location.getWorld().getGameState().getShapeRenderer();
        this.spriteBatch = location.getWorld().getGameState().getSpriteBatch();
        this.life = 0;
    }

    public abstract void continueParticle();
    public abstract boolean isAvailable();
    public abstract void dispose();

    public boolean isRunning() {
        return life > 0;
    }


}
