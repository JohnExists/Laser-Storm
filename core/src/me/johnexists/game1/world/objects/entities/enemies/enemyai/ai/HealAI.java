package me.johnexists.game1.world.objects.entities.enemies.enemyai.ai;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.AITask;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.AITaskHost;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.enemies.Enemy;
import me.johnexists.game1.world.objects.entities.enemies.EnemyHealer;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;
import me.johnexists.game1.world.objects.weapons.lasers.LaserConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

public class HealAI implements AITask {

    private final DamageableEntity host;
    private final List<HealerLaser> activeLasers;

    public HealAI(DamageableEntity host) {
        this.host = host;
        activeLasers = new ArrayList<>();
    }

    @Override
    public void update(float deltaTime) {
        activeLasers.clear();
        getNearbyAvailableEntities().forEach(gameObject ->
                activeLasers.add(new HealerLaser(host, (DamageableEntity) gameObject)));
        activeLasers.forEach(healerLaser -> healerLaser.update(deltaTime));

        if (!host.isAlive()) {
            ((AITaskHost) host).endTask(new StandStillAI(host));
        }
    }

    public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.PURPLE);
        activeLasers.forEach(healerLaser -> healerLaser.render(spriteBatch, shapeRenderer));

    }

    public List<GameObject> getNearbyAvailableEntities() {
        return host.getNearbyEntities(800 * getYSizeMultiplier(), 800 * getYSizeMultiplier())
                .stream().filter(gameObject -> gameObject instanceof Enemy)
                .filter(gameObject -> !(gameObject instanceof EnemyHealer))
                .limit(5)
                .collect(Collectors.toList());
    }

    private static class HealerLaser extends Laser {

        public HealerLaser(DamageableEntity wielder, DamageableEntity target) {
            super(wielder, target, null);
        }

        @Override
        public void update(float deltaTime) {
            if (isActive) {
                loc1 = wielder.getLocation();
                loc2 = target.getLocation();
                degrees = calculateDegrees();
                lineLength = calculateLineLength();
                target.heal(8000 * deltaTime);
            }
        }

        @Override
        protected void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
            basicLaserBody(spriteBatch, LaserConstants.PURPLE, 0);
        }


    }

}
