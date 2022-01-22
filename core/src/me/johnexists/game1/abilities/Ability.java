package me.johnexists.game1.abilities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Ability {

    protected float ticksLeft;

    public void update(float deltaTime) {
        ticksLeft -= deltaTime;

    }
    public abstract void render(ShapeRenderer shapeRenderer);

    public boolean isDone() {
        return !(ticksLeft > 0);
    }

}
