package me.johnexists.game1.objects.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.enemyai.AITask;
import me.johnexists.game1.enemyai.AITaskHost;
import me.johnexists.game1.enemyai.ai.StandStillAI;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.entities.Player;
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

    public Enemy(Location location) {
        super(location);
        currentTask = new StandStillAI(this);
        TARGET_DISTANCE = 1000;
        scalar = MathUtils.random(45,100);
//        scalar = MathUtils.random(1,5);
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
