package me.johnexists.game1.world.objects.weapons.lasers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.entities.DamageableEntity;

public class HealerLaser extends Laser {


    public HealerLaser(DamageableEntity wielder) {
        super(wielder, null);
        damagePerSecond = 10000;
    }

    @Override
    protected void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
//        basicLaserBody(shapeRenderer, Color.PINK);
    }

    @Override
    protected void onCollision(float deltaTime, DamageableEntity damageableEntity) {
        damageableEntity.damage(damagePerSecond * deltaTime);
        wielder.heal(damagePerSecond * deltaTime);
    }
}
