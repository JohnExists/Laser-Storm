package me.johnexists.game1.world.objects.abilities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Ability {

    protected float ticksLeft;

    public void update(float deltaTime) {
        ticksLeft -= deltaTime;
        whileActive(deltaTime);
    }

    protected abstract void whileActive(float deltaTime);

    public abstract void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch);

    public boolean isDone() {
        return !(ticksLeft > 0);
    }

}
