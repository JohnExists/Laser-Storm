package me.johnexists.game1.world.objects.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.AITask;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.AITaskHost;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.ai.StandStillAI;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.World;

import java.util.stream.Collectors;
/**
 * Template For All ENEMY classes
 *
 */
public abstract class Enemy extends DamageableEntity implements AITaskHost {

    public AITask currentTask;
    protected int enemyType;
    protected Color enemyColour;
    public static float TARGET_DISTANCE;

    public Enemy(Location location, float minScalar, float maxScalar) {
        super(location);
        currentTask = new StandStillAI(this);
        TARGET_DISTANCE = 450 * Size.getXSizeMultiplier();
        scalar = MathUtils.random(minScalar, maxScalar);
        bitsValue = Math.round(scalar * 17);
        health = Math.min(MAX_HEALTH + ((location.getWorld().getGameState().getLevel() - 1) * 60), MAX_OVERFLOW_HEALTH);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentTask.update(deltaTime);
    }

    @Override
    public void endTask(AITask newTask) {
        this.currentTask = newTask;
    }

    public int getEnemyType() {
        return enemyType;
    }

    public static Player getLocalPlayer(World world) {
        try {
            return (Player) world.getGameObjects().stream()
                    .filter(gameObject -> gameObject instanceof Player)
                    .collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Color getEnemyColour() {
        return enemyColour;
    }




}
