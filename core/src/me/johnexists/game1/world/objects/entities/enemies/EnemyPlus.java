package me.johnexists.game1.world.objects.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.attributes.LaserWielder;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.generators.GeneratorConstants;
import me.johnexists.game1.world.objects.weapons.lasers.BasicLaser;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;

import java.util.Objects;
import java.util.Optional;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;
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
public class EnemyPlus extends Enemy implements LaserWielder {

    private final float GAP = 12 * Size.getXSizeMultiplier();
    protected Optional<BulletPlus> currentBullet;
    private Laser laser;

    public EnemyPlus(Location location, float minScalar, float maxScalar) {
        super(location, minScalar, maxScalar);
        this.size = new Size(65, 65);
        this.enemyColour = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
        this.laser = new BasicLaser(this, getLocalPlayer(getLocation().getWorld()),
                GeneratorConstants.values()[MathUtils.random(3, 5)]);
        laser.disable();
        currentBullet = empty();
        enemyType = AGGRESSIVE_LAUNCH;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentBullet.ifPresent(bulletPlus ->
                bulletPlus.update(deltaTime));
        updateLaser(deltaTime);
    }

    @Override
    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        renderLaser(spriteBatch, shapeRenderer);
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
        currentBullet.ifPresent(bulletPlus ->
                bulletPlus.render(spriteBatch, shapeRenderer));
    }

    public void launch() {
        if (currentBullet.isEmpty()) {
            Location bulletLoc = new Location(location.getX() + size.getWidth() / 2,
                    location.getY() + size.getHeight() / 2,
                    location.getWorld());

            currentBullet = Optional.of(new BulletPlus(bulletLoc, getLocalPlayer(location.getWorld())));
        }
    }

    public void wipeBullet() {
        currentBullet.ifPresent(bulletPlus ->
                location.getWorld().despawn(bulletPlus));
        currentBullet = empty();
    }

    @Override
    public Laser getLaser() {
        return laser;
    }

    @Override
    public void updateLaser(float deltaTime) {
        if (Objects.nonNull(laser)) {
            laser.update(deltaTime);
        }
    }

    @Override
    public void renderLaser(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        if (Objects.nonNull(laser)) {
            laser.render(spriteBatch, shapeRenderer);
        }
    }

    private class BulletPlus extends DamageableEntity {

        Player localPlayer;

        public BulletPlus(Location location, Player localPlayer) {
            super(location);
            this.localPlayer = localPlayer;
            this.size = new Size(15, 15);
            setImmortal(true);
        }

        @Override
        public void update(float deltaTime) {
            if (Objects.nonNull(localPlayer)) {
                location.moveTowards(localPlayer, deltaTime * 200);
                collidesWith(localPlayer, () -> {
                    localPlayer.damage(75 * scalar);
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
