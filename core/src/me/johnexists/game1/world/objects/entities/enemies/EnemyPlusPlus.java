package me.johnexists.game1.world.objects.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import me.johnexists.game1.world.objects.attributes.LaserWielder;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.generators.GeneratorConstants;
import me.johnexists.game1.world.objects.weapons.lasers.BasicLaser;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;

import java.util.Optional;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;

/*
 * --- Enemy Plus Plus ---
 * Ability: A Step Above Default Enemies,
 * launches a laser and a bullet if in
 * range of a player
 * - 100 HP
 * - 2 Rectangles
 * - Average Size
 *
 */

@SuppressWarnings("all")
public class EnemyPlusPlus extends Enemy implements LaserWielder {

    private final float GAP = 12 * Size.getXSizeMultiplier();
    protected Optional<BulletPlusPlus> currentBullet;
    private Laser laser;

    public EnemyPlusPlus(Location location) {
        super(location);
        this.size = new Size(65, 65);
        this.enemyColour = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
        this.laser = new BasicLaser(this, getLocalPlayer(getLocation().getWorld()),
                GeneratorConstants.values()[MathUtils.random(1, 5)]);
        laser.disable();
        currentBullet = empty();
        enemyType = AGGRESSIVE_LAUNCH;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentBullet.ifPresent(bulletPlusPlus ->
                bulletPlusPlus.update(deltaTime));
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(Line);
        {
            shapeRenderer.setColor(enemyColour);
            shapeRenderer.rect(location.getX(), location.getY(), size.getWidth(), size.getHeight());
        }
        shapeRenderer.end();

        shapeRenderer.begin(Filled);
        {
            shapeRenderer.setColor(enemyColour);
            shapeRenderer.rect(location.getX() + GAP, location.getY() + GAP,
                    size.getWidth() - (GAP * 2), size.getHeight() - (GAP * 2));
        }
        shapeRenderer.end();

        renderHealthBar(shapeRenderer);
        currentBullet.ifPresent(bulletPlusPlus ->
                bulletPlusPlus.render(spriteBatch, shapeRenderer));
    }

    public void launch() {
        if (currentBullet.isEmpty()) {
            Location bulletLoc = new Location(location.getX(), location.getY(), location.getWorld());
            bulletLoc.add(size.getWidth() / 2, size.getHeight() / 2);

            currentBullet = Optional.of(new BulletPlusPlus(bulletLoc, getLocalPlayer(location.getWorld())));
        }
    }

    public void wipeBullet() {
        currentBullet.ifPresent(bulletPlusPlus ->
                location.getWorld().despawn(bulletPlusPlus));
        currentBullet = empty();
    }

    @Override
    public Laser getLaser() {
        return laser;
    }

    @Override
    public void clearLaser() {
        laser = null;
    }

    private class BulletPlusPlus extends DamageableEntity {

        Player localPlayer;

        public BulletPlusPlus(Location location, Player localPlayer) {
            super(location);
            this.localPlayer = localPlayer;
            this.size = new Size(15, 15);
            setImmortal(true);
        }

        @Override
        public void update(float deltaTime) {
            if (nonNull(localPlayer)) {
                Location distanceToPlayer = location.distanceTo(localPlayer);
                Vector2 vectorToPlayer = new Vector2(distanceToPlayer.getX(), distanceToPlayer.getY());
                vectorToPlayer.nor();
                vectorToPlayer.x *= deltaTime * 200;
                vectorToPlayer.y *= deltaTime * 200;

                location.add(vectorToPlayer.x, vectorToPlayer.y);

                collidesWith(localPlayer, () -> {
                    localPlayer.damage(75);
                    if (!localPlayer.isAlive()) {
                        location.getWorld().despawn(localPlayer);
                    }
                    wipeBullet();
                });
            }
        }

        @Override
        public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
            shapeRenderer.begin(Filled);
            {
                shapeRenderer.setColor(Color.GRAY);
                shapeRenderer.rect(location.getX(), location.getY(),
                        size.getWidth(), size.getHeight());
            }
            shapeRenderer.end();
        }
    }
}
