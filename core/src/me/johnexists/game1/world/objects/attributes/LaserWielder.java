package me.johnexists.game1.world.objects.attributes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;

public interface LaserWielder {

    Laser getLaser();
    void updateLaser(float deltaTime);
    void renderLaser(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer);
}
