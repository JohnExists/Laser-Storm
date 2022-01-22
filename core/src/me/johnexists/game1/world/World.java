package me.johnexists.game1.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import me.johnexists.game1.effects.Particle;
import me.johnexists.game1.objects.GameObject;
import me.johnexists.game1.objects.attributes.LaserWielder;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.entities.Player;
import me.johnexists.game1.objects.entities.enemies.DefaultEnemy;
import me.johnexists.game1.objects.entities.enemies.EnemyHealer;
import me.johnexists.game1.objects.entities.enemies.EnemyPlusPlus;
import me.johnexists.game1.objects.entities.enemies.XEnemy;
import me.johnexists.game1.state.GameState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static com.badlogic.gdx.math.MathUtils.clamp;
import static me.johnexists.game1.objects.attributes.Size.getXSizeMultiplier;

@SuppressWarnings("all")
public class World {

    public static final float MAP_X = 5000 * Size.getYSizeMultiplier(),
            MAP_Y = 5000 * Size.getYSizeMultiplier();
    private final List<GameObject> gameObjects;
    private final List<Particle> activeParticles;
    private final GameState gameState;

    private Player player;
    private DamageableEntity mainCharacter;
    private boolean isCameraSynchronized = false;

    public World(GameState gameState) {
        this.gameState = gameState;
        gameObjects = new ArrayList<>();
        activeParticles = new ArrayList<>();
        loadEntities();
//        mainCharacter.setImmortal(true);
//        spawn(new DefaultEnemy(new Location(player.getLocation().getX() + 2000 * Size.getXSizeMultiplier(),
//                player.getLocation().getY() + 2000 * Size.getYSizeMultiplier(), this)));

        sortList();
    }

    public void loadEntities() {
        activeParticles.clear();
        gameObjects.clear();
        mainCharacter = null;
        player = new Player(gameState, new Location(1000, 1000, this));
//        player.setLocation(new Location(1500 * getXSizeMultiplier(), 1500 * getYSizeMultiplier(), this));
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

    public void update(float deltaTime) {
        Location mainCharacterLoc = mainCharacter.getLocation();
        boolean isWithinBounds = mainCharacterLoc.getX() > graphics.getWidth() / 2 &&
                mainCharacterLoc.getY() > graphics.getHeight() / 2;
//        if (isWithinBounds) {
        synchronizeCameraWith(mainCharacter);
//        }
        sortList();

        if (!gameObjects.isEmpty()) {
            for (int i = 0; i < gameObjects.size(); i++) {
                gameObjects.get(i).update(deltaTime);
                if (gameObjects.get(i) instanceof LaserWielder) {
                    ((LaserWielder) gameObjects.get(i)).getLaser().update(deltaTime);
                }
            }
        }

    }

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

    public void spawnParticle(Particle particles) {
        if (particles.isAvailable()) {
            activeParticles.add(particles);
        }
    }

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
        if (isCameraSynchronized) {
            Location obLoc = gameObject.getLocation();
            float width = graphics.getWidth() / 2, height = graphics.getHeight() / 2;
            float x = clamp(obLoc.getX(), width, MAP_X - width),
                    y = clamp(obLoc.getY(), height, MAP_Y - height);

            gameState.getGameCamera().position.set(x, y, 0);
            gameState.getGameCamera().update();
        } else {
            gameObject.getLocation().setX(gameState.getGameCamera().position.x);
            gameObject.getLocation().setY(gameState.getGameCamera().position.y);
            isCameraSynchronized = true;
        }
    }
}
