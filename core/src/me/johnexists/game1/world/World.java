package me.johnexists.game1.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.world.effects.Particle;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.attributes.LaserWielder;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.entities.enemies.DefaultEnemy;
import me.johnexists.game1.world.objects.entities.enemies.EnemyHealer;
import me.johnexists.game1.world.objects.entities.enemies.EnemyPlusPlus;
import me.johnexists.game1.world.objects.entities.enemies.XEnemy;
import me.johnexists.game1.state.GameState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;

public class World {

    public static final float MAP_X = 8500 * Size.getYSizeMultiplier(),
            MAP_Y = 8500 * Size.getYSizeMultiplier();
    private final List<GameObject> gameObjects;
    private final List<Particle> activeParticles;
    private final GameState gameState;

    @SuppressWarnings("FieldCanBeLocal")
    private Player player;
    private DamageableEntity mainCharacter;

    public World(GameState gameState) {
        this.gameState = gameState;
        gameObjects = new ArrayList<>();
        activeParticles = new ArrayList<>();
        loadEntities();
//        mainCharacter.setImmortal(true);

        sortList();
    }

    public void loadEntities() {
        activeParticles.clear();
        gameObjects.clear();
        mainCharacter = null;
        player = new Player(gameState, new Location(1000, 1000, this));
        mainCharacter = player;
        spawn(player);
        for (int i = 0; i < 25; i++) {
            spawn(new DefaultEnemy(getRandomLocation()));
            spawn(new XEnemy(getRandomLocation()));
            spawn(new EnemyPlusPlus(getRandomLocation()));
        }
        for (int i = 0; i < 6; i++) {
            spawn(new EnemyHealer(getRandomLocation()));
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void update(float deltaTime) {
        synchronizeCameraWith(mainCharacter);
        sortList();

        try {
            if (!gameObjects.isEmpty()) {
                for (int i = 0; i < gameObjects.size(); i++) {
                    gameObjects.get(i).update(deltaTime);
                    if (gameObjects.get(i) instanceof LaserWielder) {
                        ((LaserWielder) gameObjects.get(i)).getLaser().update(deltaTime);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void render() {
        SpriteBatch spriteBatch = getGameState().getSpriteBatch();
        ShapeRenderer shapeRenderer = getGameState().getShapeRenderer();
        renderGrid();

        for (int i = 0; i < gameObjects.size(); i++) {
            Location distanceToMainCharacter = gameObjects.get(i).getLocation().distanceTo(mainCharacter);
            boolean isWithinBounds = !distanceToMainCharacter.isXGreaterThan(graphics.getWidth()) &&
                    !distanceToMainCharacter.isYGreaterThan(graphics.getHeight());

            if (isWithinBounds) {
                if (gameObjects.get(i) instanceof LaserWielder) {
                    ((LaserWielder) gameObjects.get(i)).getLaser().render(spriteBatch, shapeRenderer);
                }
                gameObjects.get(i).render(spriteBatch, shapeRenderer);
            }
        }

        for (int i = 0; i < activeParticles.size(); i++) {
            activeParticles.get(i).continueParticle();
            if (!activeParticles.get(i).isRunning()) {
                activeParticles.remove(activeParticles.get(i));
            }
        }

    }

    public void renderGrid() {
        final float GAP = 50 * getXSizeMultiplier();
        final ShapeRenderer shapeRenderer = getGameState().getShapeRenderer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        {
            shapeRenderer.setColor(getGameState().getGameLogic().cycle());
            gl.glLineWidth(getXSizeMultiplier());
            for (int i = 0; i < MAP_X; i += GAP) {
                shapeRenderer.line(0, i, MAP_X, i);
            }
            for (int i = 0; i < MAP_Y; i += GAP) {
                shapeRenderer.line(i, 0, i, MAP_Y);
            }
        }
        shapeRenderer.end();
    }

    public void spawn(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public void spawnParticle(Particle particle) {
        if (particle.isAvailable()) {
            activeParticles.add(particle);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void despawn(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    private void sortList() {
        gameObjects.sort(Comparator.comparingInt(GameObject::getViewingOrderWeight));
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Particle> getActiveParticles() {
        return activeParticles;
    }

    public GameState getGameState() {
        return gameState;
    }

    public DamageableEntity getMainCharacter() {
        return mainCharacter;
    }

    public Location getRandomLocation() {
        return new Location(MathUtils.random(MAP_X),
                MathUtils.random(MAP_Y), this);
    }

    public void synchronizeCameraWith(GameObject gameObject) {
        Location obLoc = gameObject.getLocation();
        float width = graphics.getWidth() / 2f, height = graphics.getHeight() / 2f;
        float x = MathUtils.clamp(obLoc.getX(), width, MAP_X - width),
                y = MathUtils.clamp(obLoc.getY(), height, MAP_Y - height);

        gameState.getGameCamera().position.set(x, y, 0);
        gameState.getGameCamera().update();
    }
}
