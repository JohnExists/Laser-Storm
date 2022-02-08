package me.johnexists.game1.world.objects.weapons.lasers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;

@Deprecated
public class LaserDEMO extends Laser {

    public LaserDEMO(DamageableEntity wielder) {
        super(wielder, null);
        this.laserConst = Player.getLaserSkin();
        damagePerSecond = Float.MAX_VALUE;

    }

    @Override
    protected void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        spriteBatch.begin();
        {
            spriteBatch.draw(laserConst.displaySprite, loc1.getX() - (THICKNESS / 2), loc1.getY() - (THICKNESS / 2),
                    (THICKNESS / 2), (THICKNESS / 2), lineLength, THICKNESS, 1.0f, 1.0f,
                    degrees);
        }
        spriteBatch.end();

    }

}
