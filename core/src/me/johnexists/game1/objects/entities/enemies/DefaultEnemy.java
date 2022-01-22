package me.johnexists.game1.objects.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.objects.attributes.LaserWielder;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.weapons.generators.GeneratorConstants;
import me.johnexists.game1.objects.weapons.lasers.BasicLaser;
import me.johnexists.game1.objects.weapons.lasers.Laser;

/*
 * --- Default Enemy ---
 * Ability: Follows the players
 * and casts a laser at them once in range
 * - 100 HP
 * - Average
 *
 */
public class DefaultEnemy extends Enemy implements LaserWielder {

    private Laser laser;

    public DefaultEnemy(Location location) {
        super(location);
        this.laser = new BasicLaser(this, getLocalPlayer(getLocation().getWorld()),
                GeneratorConstants.values()[MathUtils.random(1, 5)]);
        laser.disable();
        viewingOrderWeight = 3;
        enemyType = AGGRESSIVE_FAR;
        enemyColour = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);

    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            shapeRenderer.setColor(enemyColour);
            renderBody(shapeRenderer);
        }
        shapeRenderer.end();
        renderHealthBar(shapeRenderer);
    }

    private void renderBody(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(location.getX(), location.getY(), size.getWidth(), size.getHeight());
    }

    @Override
    public Laser getLaser() {
        return laser;
    }

    @Override
    public void clearLaser() {
        laser = null;
    }
}
