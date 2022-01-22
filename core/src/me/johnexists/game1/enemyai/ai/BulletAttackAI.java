package me.johnexists.game1.enemyai.ai;

import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.enemyai.AITask;
import me.johnexists.game1.enemyai.AITaskHost;
import me.johnexists.game1.objects.entities.DamageableEntity;

public class BulletAttackAI implements AITask {

    public DamageableEntity host, target;

    public BulletAttackAI(DamageableEntity host, DamageableEntity target) {
        this.host = host;
        this.target = target;
    }

    @Override
    public void update(float deltaTime) {
        if (!target.isAlive()) {
            ((AITaskHost) host).endTask(new StandStillAI(host));
        }
        if (!host.getNearbyEntities(300, 300).contains(target)) {
            AITaskHost aiHost = (AITaskHost) host;
            aiHost.endTask(new AgroTargetAI(host, target,
                    new Size(MathUtils.random(100, 250), MathUtils.random(100, 250))));
        }
    }

}
