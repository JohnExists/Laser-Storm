package me.johnexists.game1.objects.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import me.johnexists.game1.objects.attributes.Collideable;
import me.johnexists.game1.objects.attributes.Damageable;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static com.badlogic.gdx.math.Intersector.overlapConvexPolygons;

public abstract class DamageableEntity extends Entity implements Collideable, Damageable {

    protected float scalar;
    private float health;
    private boolean immortal;

    public DamageableEntity(Location location) {
        super(location);
        health = MAX_HEALTH;
        immortal = false;
        scalar = 1;
    }

    public abstract void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);

    @Override
    public Polygon getCollisionBounds() {
        Polygon polygon = new Polygon(new float[]{0, 0, size.getWidth(), 0,
                size.getWidth(), size.getHeight(), 0, size.getHeight()});
        polygon.setPosition(location.getX(), location.getY());

        return polygon;
    }

    @Override
    public void collidesWith(Collideable collideable, Runnable runnable) {
        if (overlapConvexPolygons(getCollisionBounds(),
                collideable.getCollisionBounds())) {
            runnable.run();
        }
    }

    @Override
    public void damage(float deltaDamage) {
        if (!immortal) {
            health -= deltaDamage;
        }
    }

    @Override
    public void heal(float heal) {
        health = Math.min(health + heal, MAX_HEALTH);
    }

    public float getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

    public float getScalar() {
        return scalar;
    }

    protected void renderHealthBar(ShapeRenderer shapeRenderer) {
        float healthPercentage = Math.min((getHealth() / MAX_HEALTH), 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            graphics.getGL20().glEnable(GL20.GL_BLEND);
            gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setColor(new Color(65 / 255f, 65 / 255f, 65 / 255f, 0.45f));
            shapeRenderer.rect(location.getX(),
                    location.getY()  + size.getHeight() * 1.25f, size.getWidth(), 10 * Size.getYSizeMultiplier());

            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(location.getX(),
                    location.getY() + size.getHeight() * 1.25f,
                    healthPercentage * size.getWidth() , 10 * Size.getYSizeMultiplier());
        }
        shapeRenderer.end();
        gl.glDisable(GL20.GL_BLEND);

    }


}
