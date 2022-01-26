package me.johnexists.game1.world.objects.entities.enemyai.ai;

import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.enemyai.AITask;
import me.johnexists.game1.world.objects.entities.enemyai.AITaskHost;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static me.johnexists.game1.world.objects.entities.enemies.Enemy.TARGET_DISTANCE;
import static me.johnexists.game1.world.objects.entities.enemies.Enemy.getLocalPlayer;

public class StandStillAI implements AITask {

    private final DamageableEntity host;

    public StandStillAI(DamageableEntity host) {
        this.host = host;

    }

    @Override
    public void update(float deltaTime) {
        Player localPlayer = getLocalPlayer(host.getLocation().getWorld());
        AITaskHost taskHost = (AITaskHost) host;
        Location distanceToPlayer = nonNull(localPlayer) ?
                host.getLocation().distanceTo(requireNonNull(localPlayer)) :
                null;

        if (nonNull(distanceToPlayer)) {
            if (!distanceToPlayer.isXGreaterThan(TARGET_DISTANCE) &&
                    !distanceToPlayer.isYGreaterThan(TARGET_DISTANCE)) {
                Size enemyRange = switch (taskHost.getEnemyType()) {
                    case PASSIVE, AGGRESSIVE_FAR, AGGRESSIVE_LAUNCH -> new Size(MathUtils.random(100, 175), MathUtils.random(100, 175));
                    case AGGRESSIVE_NEAR -> new Size(45, 45);
                    default -> throw new IllegalStateException("Unexpected value: " + (taskHost).getEnemyType());
                };

                taskHost.endTask(new AgroTargetAI(host, localPlayer, enemyRange));
            }

        }
    }

}
