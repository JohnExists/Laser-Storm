package me.johnexists.game1.enemyai.ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import me.johnexists.game1.objects.attributes.Collideable;
import me.johnexists.game1.objects.attributes.LaserWielder;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.enemyai.AITask;
import me.johnexists.game1.enemyai.AITaskHost;
import me.johnexists.game1.objects.GameObject;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.entities.Player;
import me.johnexists.game1.objects.entities.enemies.EnemyPlusPlus;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static me.johnexists.game1.objects.entities.enemies.Enemy.TARGET_DISTANCE;
import static me.johnexists.game1.objects.entities.enemies.Enemy.getLocalPlayer;

public class AgroTargetAI implements AITask {

    private final DamageableEntity host, target;
    private final EnemyTargetBounds enemyTargetBounds;
    private final Size targetBounds;

    public AgroTargetAI(DamageableEntity host, DamageableEntity target, Size playerTargetDestination) {
        this.host = host;
        this.target = target;
        this.targetBounds = playerTargetDestination;
        this.enemyTargetBounds = new EnemyTargetBounds(playerTargetDestination);

        if (host instanceof LaserWielder) {
            ((LaserWielder) host).getLaser().disable();
        }
    }

    @Override
    public void update(float deltaTime) {
        moveEnemy(deltaTime);
        updateTask();

        if (!target.isAlive()) {
            ((AITaskHost) host).endTask(new StandStillAI(host));
        }

        enemyTargetBounds.update(deltaTime);
    }

    private void moveEnemy(float deltaTime) {
        AITaskHost taskHost = (AITaskHost) host;
        Player localPlayer = getLocalPlayer(host.getLocation().getWorld());
        Location distanceToPlayer = nonNull(localPlayer) ?
                host.getLocation().distanceTo(requireNonNull(localPlayer)) :
                null;

        if (nonNull(distanceToPlayer)) {
            if (!distanceToPlayer.isXGreaterThan(TARGET_DISTANCE) &&
                    !distanceToPlayer.isYGreaterThan(TARGET_DISTANCE)) {
                Vector2 hostToTargetVector = new Vector2(distanceToPlayer.getX(), distanceToPlayer.getY());

                hostToTargetVector.nor();
                hostToTargetVector.x *= deltaTime * 125; // 125
                hostToTargetVector.y *= deltaTime * 125; // 125
                host.getLocation().add(new Location(hostToTargetVector.x, hostToTargetVector.y));
            } else {
                taskHost.endTask(new StandStillAI(host));
            }
        }
    }

    private void updateTask() {
        AITaskHost taskHost = (AITaskHost) host;
        if (Intersector.overlapConvexPolygons(enemyTargetBounds.getCollisionBounds(),
                host.getCollisionBounds())) {
            switch (taskHost.getEnemyType()) {
                case AGGRESSIVE_FAR:
                    if (taskHost instanceof LaserWielder) {
                        taskHost.endTask(new LaserAttackAI(host, target, (LaserWielder) host
                        ));
                    }
                    break;
                case PASSIVE:
                    taskHost.endTask(new HealAI(host));
                    break;
                case AGGRESSIVE_NEAR:
                    taskHost.endTask(new StandStillAI(host));
                    break;
                case AGGRESSIVE_LAUNCH:
                    if (taskHost instanceof EnemyPlusPlus) {
                        ((EnemyPlusPlus) taskHost).launch();
                        taskHost.endTask(new LaserAttackAI(host, target, (LaserWielder) host
                        ));
                    }
                    break;
            }
        }
    }

    private class EnemyTargetBounds extends GameObject implements Collideable {

        private final Polygon polygonBounds;

        public EnemyTargetBounds(Size playerTargetDestination) {
            super(new Location(target.getLocation().getX() - (targetBounds.getWidth() / 2),
                    target.getLocation().getY() - (targetBounds.getHeight() / 2)));
            this.polygonBounds = new Polygon(new float[]{0, 0, playerTargetDestination.getWidth(), 0,
                    playerTargetDestination.getWidth(), playerTargetDestination.getHeight(), 0, playerTargetDestination.getHeight()});
        }

        @Override
        public Polygon getCollisionBounds() {
            return this.polygonBounds;
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
            location = target.getLocation();
            polygonBounds.setPosition(location.getX(), location.getY());
        }

        @Override
        public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            {
                shapeRenderer.polygon(getCollisionBounds().getVertices());
            }
            shapeRenderer.end();
        }

    }
}
