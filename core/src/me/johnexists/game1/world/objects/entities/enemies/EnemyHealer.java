package me.johnexists.game1.world.objects.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.ai.HealAI;

/*
 * --- Enemy Healer ---
 * Ability: Gets all nearby enemies within an
 * 800 blocks radius and heals them (125 health per second)
 * - 100 HP
 * - Massive
 *
 */
public class EnemyHealer extends Enemy {

    public EnemyHealer(Location location, float minScalar, float maxScalar) {
        super(location, minScalar, maxScalar);
        this.size = new Size(125,125);

        enemyType = PASSIVE;
        enemyColour = new Color(MathUtils.random(),MathUtils.random(),MathUtils.random(),1);
    }


    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        if(currentTask instanceof HealAI) {
            ((HealAI) currentTask).render(spriteBatch, shapeRenderer);
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(enemyColour);
        shapeRenderer.rect(location.getX(), location.getY(),
                size.getWidth(), size.getHeight());

        shapeRenderer.end();
        renderHealthBar(shapeRenderer);
    }

}
