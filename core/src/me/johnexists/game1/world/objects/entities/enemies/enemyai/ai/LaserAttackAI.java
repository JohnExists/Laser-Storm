package me.johnexists.game1.world.objects.entities.enemies.enemyai.ai;

import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.attributes.LaserWielder;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.AITask;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.AITaskHost;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;

import java.util.Objects;

import static java.lang.Math.abs;
import static java.util.Objects.*;
import static me.johnexists.game1.world.objects.entities.enemies.Enemy.*;

public class LaserAttackAI implements AITask {

    public final DamageableEntity host, target;
    public Laser laser;

    public LaserAttackAI(DamageableEntity host, DamageableEntity target, LaserWielder laserWielder) {
        this.host = host;
        this.target = target;
        this.laser = laserWielder.getLaser();
        if (Objects.nonNull(laser)) {
            laser.enable();
        }
    }

    @Override
    public void update(float deltaTime) {
        Player localPlayer = getLocalPlayer(host.getLocation().getWorld());
        AITaskHost taskHost = (AITaskHost) host;
        Location distanceToPlayer = nonNull(localPlayer) ?
                host.getLocation().distanceTo(requireNonNull(localPlayer)) :
                null;

        if (!target.isAlive()) {
            laser.disable();
            taskHost.endTask(new StandStillAI(host));
        }

        if (nonNull(distanceToPlayer)) {
            if (abs(distanceToPlayer.getX()) > TARGET_DISTANCE ||
                    abs(distanceToPlayer.getY()) > TARGET_DISTANCE) {
                laser.disable();
                taskHost.endTask(new AgroTargetAI(host, target,
                        new Size(MathUtils.random(100, 175), MathUtils.random(100, 175))));
            }
        }
    }

}
