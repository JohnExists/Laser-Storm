package me.johnexists.game1.objects.weapons.lasers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.entities.Player;

public class LaserDEMO extends Laser {

    public LaserDEMO(DamageableEntity wielder) {
        super(wielder, null);
        this.laserConst = Player.getLaserSkin();
//        damagePerSecond = 150;
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