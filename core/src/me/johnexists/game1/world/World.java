package me.johnexists.game1.world;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.BloomEffect;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.state.GameState;
import me.johnexists.game1.world.effects.Particle;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.DamageableEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;

public class World {

    public static final float MAP_X = 6500 * Size.getXSizeMultiplier(),
            MAP_Y = 6500 * Size.getYSizeMultiplier();

    private final List<GameObject> gameObjects;
    private final List<Particle> activeParticles;
    private final GameState gameState;

    private DamageableEntity mainCharacter;
    private VfxManager vfxManager;

    public World(GameState gameState) {
        this.gameState = gameState;
        gameObjects = new ArrayList<>();
        activeParticles = new ArrayList<>();
        loadGraphics();
        sortList();
    }

    private void loadGraphics() {
        vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
        BloomEffect vfxEffect = new BloomEffect();
        vfxManager.addEffect(vfxEffect);
        vfxEffect.setBloomIntensity(2.5f);
    }


    public void loadEntities(int level) {
        SpawnConfigurations.loadPreConfigurations(this);
        SpawnConfigurations spawnConfig = SpawnConfigurations.generateConfigurationByLevel(this, level);
        spawnConfig.spawn();
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void update(float deltaTime) {
        vfxManager.update(deltaTime);
        synchronizeCameraWith(mainCharacter);
        sortList();

        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).update(deltaTime);
        }

    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void render() {
        SpriteBatch spriteBatch = getGameState().getSpriteBatch();
        ShapeRenderer shapeRenderer = getGameState().getShapeRenderer();

        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();
        {
            renderGrid();
            for (int i = 0; i < gameObjects.size(); i++) {
                Location distanceToMainCharacter = gameObjects.get(i).getLocation().distanceTo(mainCharacter);
                boolean isWithinBounds = distanceToMainCharacter.isXLesserThan(graphics.getWidth()) &&
                        distanceToMainCharacter.isYLesserThan(graphics.getHeight());

                if (isWithinBounds) {
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
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
    }

    public void renderGrid() {
        final float GAP = 50 * getXSizeMultiplier();
        ShapeRenderer shapeRenderer = getGameState().getShapeRenderer();

        shapeRenderer.begin();
        {
            shapeRenderer.setColor(GameLogic.cycle());
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


    public void setMainCharacter(DamageableEntity mainCharacter) {
        this.mainCharacter = mainCharacter;
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

    public void dispose() {

    }
}
