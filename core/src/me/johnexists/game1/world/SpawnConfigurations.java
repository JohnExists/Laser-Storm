package me.johnexists.game1.world;

import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.entities.enemies.DefaultEnemy;
import me.johnexists.game1.world.objects.entities.enemies.EnemyHealer;
import me.johnexists.game1.world.objects.entities.enemies.EnemyPlus;
import me.johnexists.game1.world.objects.entities.enemies.XEnemy;

import java.util.ArrayList;
import java.util.List;

public class SpawnConfigurations {

    private final List<GameObject> entitiesToSpawn;
    private final World world;

    private SpawnConfigurations(World world, List<GameObject> entitiesToSpawn) {
        this.world = world;
        this.entitiesToSpawn = entitiesToSpawn;
    }

    public static SpawnConfigurations returnRandomConfig(World world, int amount, float minScalar, float maxScalar) {
        if (world.getGameObjects().stream().anyMatch(gameObject -> gameObject instanceof Player)) {
            List<GameObject> rand, availableEnemies;
            availableEnemies = new ArrayList<>();
            rand = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                availableEnemies.clear();
                if (world.getGameState().getLevel() > 4) {
                    availableEnemies.add(new EnemyHealer(world.getRandomLocation(), minScalar, maxScalar));
                    availableEnemies.add(new EnemyPlus(world.getRandomLocation(), minScalar, maxScalar));
                }
                if (world.getGameState().getLevel() > 7) {
                    availableEnemies.add(new XEnemy(world.getRandomLocation(), minScalar, maxScalar));
                }
                availableEnemies.add(new DefaultEnemy(world.getRandomLocation(), minScalar, maxScalar));

                rand.add(availableEnemies.get(MathUtils.random.nextInt(availableEnemies.size())));
            }
            return new SpawnConfigurations(world, rand);
        }
        throw new IllegalStateException("load pre-config first! SpawnConfigurations.loadPreConfigurations(<world>);");
    }

    public static SpawnConfigurations generateConfigurationByLevel(World world, int level) {
        return returnRandomConfig(world, 45 + (level * 5), 2.5f + (level * 5), 10 + (level * 5));
    }

    public void spawn() {
        entitiesToSpawn.forEach(world::spawn);
    }

    public static void loadPreConfigurations(World world) {
        Player.kills = 0;
        world.getGameObjects().clear();
        world.getActiveParticles().clear();
        Player player;
        player = new Player(world.getGameState(), new Location(1000,
                1000, world));
        world.setMainCharacter(player);
        world.spawn(player);
    }
}
