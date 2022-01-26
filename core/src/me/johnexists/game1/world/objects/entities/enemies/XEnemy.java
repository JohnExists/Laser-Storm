package me.johnexists.game1.world.objects.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import me.johnexists.game1.world.objects.attributes.Collideable;
import me.johnexists.game1.world.objects.attributes.LaserWielder;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.effects.BloodParticle;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;

import java.util.ArrayList;
import java.util.List;

/*
 * --- XEnemy ---
 * Ability: Spins in a circle
 * towards the players
 * Deals massive damage to
 * nearby players
 * - 100 Health
 * - Small
 * - Massive X-Shape
 * - Constantly Rotates
 */
@SuppressWarnings("FieldCanBeLocal")
public class XEnemy extends Enemy {

    private static final float ENTITY_DAMAGE = 25;
    protected static final float DEGREES_ROTATED_PER_SECOND = 90;

    private final List<XEnemyExtension> extensions;
    private final Player player;

    private float currentRotation = 0;

    public XEnemy(Location location) {
        super(location);
        this.size = new Size(45, 45);
        this.extensions = new ArrayList<>();
        enemyType = AGGRESSIVE_NEAR;
        player = getLocalPlayer(location.getWorld());
        enemyColour = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);

        for (int i = 0; i < 4; i++) {
            extensions.add(new XEnemyExtension(90 * i));
        }
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.currentTask.update(deltaTime);
        extensions.forEach(xEnemyExtension -> {
            xEnemyExtension.update(deltaTime);
            xEnemyExtension.collidesWith(player, () -> {
                player.damage(ENTITY_DAMAGE * deltaTime * getScalar());
                System.out.println(ENTITY_DAMAGE * deltaTime * getScalar());
                checkIfAlive(player, deltaTime);
            });
        });
        currentRotation += deltaTime * DEGREES_ROTATED_PER_SECOND;
    }


    private void checkIfAlive(DamageableEntity damageableEntity, float deltaTime) {
        if (!damageableEntity.isAlive()) {
            getLocation().getWorld().despawn(damageableEntity);
            getLocation().getWorld().spawnParticle(new BloodParticle(damageableEntity.getLocation(), deltaTime));
            if (damageableEntity instanceof LaserWielder) {
                ((LaserWielder) damageableEntity).clearLaser();
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        renderBody(spriteBatch, shapeRenderer);
        renderHealthBar(shapeRenderer);
    }

    private void renderBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            shapeRenderer.setColor(enemyColour);
            extensions.forEach(ext -> ext.render(spriteBatch, shapeRenderer));
            shapeRenderer.rect(location.getX(), location.getY(),
                    size.getWidth() / 2, size.getHeight() / 2, size.getWidth(), size.getHeight(),
                    1.0f, 1.0f, currentRotation);

        }
        shapeRenderer.end();

    }

    private class XEnemyExtension extends GameObject implements Collideable {

        private final XEnemy currentEnemy;
        private float currentRotation;
        private final int THICKNESS = 10, LENGTH = 192;

        public XEnemyExtension(float startingRotation) {
            super(new Location(0, 0));
            this.size = new Size(LENGTH, THICKNESS);
            this.currentEnemy = XEnemy.this;
            currentRotation = startingRotation;
        }

        @Override
        public Polygon getCollisionBounds() {
            Polygon polygon = new Polygon(new float[]{0, 0, size.getWidth(), 0,
                    size.getWidth(), size.getHeight(), 0, size.getHeight()});
            polygon.setPosition(location.getX(), location.getY());
            polygon.setRotation(currentRotation);

            return polygon;
        }

        @Override
        public void collidesWith(Collideable collideable, Runnable runnable) {
            if (Intersector.overlapConvexPolygons(getCollisionBounds(),
                    collideable.getCollisionBounds())) {
                runnable.run();
            }
        }

        @Override
        public void update(float deltaTime) {
            currentRotation += deltaTime * DEGREES_ROTATED_PER_SECOND;
            location = new Location(currentEnemy.getLocation().getX() + currentEnemy.getSize().getWidth() / 2,
                    currentEnemy.getLocation().getY() + currentEnemy.getSize().getHeight() / 2 - size.getHeight() / 2f,
                    currentEnemy.getLocation().getWorld());
        }

        @Override
        public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
            shapeRenderer.setColor(enemyColour);
            shapeRenderer.rect(location.getX(), location.getY(),
                    (size.getHeight() / 2f), (size.getHeight() / 2f), size.getWidth(), size.getHeight(), 1.0f, 1.0f,
                    currentRotation);
        }
    }
}
