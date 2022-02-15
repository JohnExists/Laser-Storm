package me.johnexists.game1.state;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.hud.HUDEndScreen;
import me.johnexists.game1.ui.hud.HUDHealth;
import me.johnexists.game1.ui.hud.HUDMinimap;
import me.johnexists.game1.ui.hud.HUDPause;
import me.johnexists.game1.world.World;
import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Size;
import me.johnexists.game1.world.objects.entities.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.graphics;

public class GameState extends State {

    private final OrthographicCamera gameCamera;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final List<UIElement> hudElements;
    private final HUDPause hudPause;
    private final int level;

    private final World world;
    private boolean paused, gameEnd;

    public GameState(GameLogic gameLogic, int level) {
        super(gameLogic);
        this.level = level;
        this.world = new World(this);
        createNewWorld();
        hudElements = new ArrayList<>();
        hudPause = new HUDPause(this);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        shapeRenderer.setAutoShapeType(true);
        gameCamera = new OrthographicCamera(graphics.getWidth(), graphics.getHeight());
        gameCamera.update();

        initHUD();

        gameLogic.getKeyInput().registerOnKeyReleased(Input.Keys.ESCAPE,
                () -> {
                    if (!gameEnd) {
                        togglePause();
                    }
                });
    }

    public void createNewWorld() {
        world.loadEntities(level);
        paused = false;
        gameEnd = false;

    }

    public void initHUD() {
        hudElements.clear();
        hudElements.add(new HUDHealth(new Location(50 * Size.getXSizeMultiplier(),
                graphics.getHeight() - 50 * Size.getXSizeMultiplier(), world),
                new Size(100, 100), world.getMainCharacter()));
        hudElements.add(new HUDMinimap(new Location(graphics.getWidth() - 100 * Size.getXSizeMultiplier(),
                graphics.getHeight() - 100 * Size.getYSizeMultiplier()), world));
    }

    @Override
    public void render() {

        gl.glLineWidth(Size.getXSizeMultiplier());
        spriteBatch.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        world.render();
        hudElements.forEach(UIElement::render);

        if (paused) {
            hudPause.render();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!paused) {
            if (world.getMainCharacter().isAlive()) {
                world.update(deltaTime);
            }
            if (!gameEnd) {
                hudElements.forEach(hud -> hud.update(deltaTime));
                if (!world.getMainCharacter().isAlive()) {
                    hudElements.clear();
                    gameEnd = true;
                    hudElements.add(new HUDEndScreen(false));
                }
                if (world.getGameObjects().stream().noneMatch(obj -> obj instanceof Enemy)) {
                    hudElements.clear();
                    gameEnd = true;
                    hudElements.add(new HUDEndScreen(true));
                }

            } else {
                gameLogic.getKeyInput().isPressed(Input.Keys.R, () -> {
                    System.gc();
                    createNewWorld();
                    initHUD();
                });
                gameLogic.getKeyInput().isPressed(Input.Keys.M, () ->
                        gameLogic.setSelectedState(Optional.of(new MainMenuState(getGameLogic()))));
            }

        } else {
            hudPause.update(deltaTime);
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        hudElements.forEach(UIElement::dispose);
        world.dispose();

    }

    public OrthographicCamera getGameCamera() {
        return gameCamera;
    }


    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public void togglePause() {
        paused = !paused;
    }

    public int getLevel() {
        return level;
    }

    public boolean didGameEnd() {
        return gameEnd;
    }
}
