package me.johnexists.game1.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.logic.GameLogic;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.hud.HUDDeath;
import me.johnexists.game1.ui.hud.HUDHealth;
import me.johnexists.game1.ui.hud.HUDMinimap;
import me.johnexists.game1.ui.hud.HUDPause;
import me.johnexists.game1.world.World;

import java.util.ArrayList;
import java.util.List;

public class GameState extends State {

    private final OrthographicCamera gameCamera;
    private final SpriteBatch spriteBatch;
    private final ShapeRenderer shapeRenderer;
    private final List<UIElement> hudElements;
    private final HUDPause hudPause;

    private World world;
    private boolean paused;

    public GameState(GameLogic gameLogic) {
        super(gameLogic);
        this.world = new World(this);
        createNewWorld();
        hudElements = new ArrayList<>();
        hudPause = new HUDPause(this);

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        gameCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameCamera.update();

        initHUD();

        gameLogic.getKeyInput().registerOnKeyReleased(Input.Keys.ESCAPE,
                this::togglePause);
    }

    public void createNewWorld() {
//        this.world = null;
//        this.world = new World(this);
//        world.spawnEntities();
        world.loadEntities();
        paused = false;
    }

    public void initHUD() {
        hudElements.clear();
        hudElements.add(new HUDHealth(new Location(50 * Size.getXSizeMultiplier(),
                Gdx.graphics.getHeight() - 50 * Size.getXSizeMultiplier(), world),
                new Size(100, 100), world.getMainCharacter()));
        hudElements.add(new HUDMinimap(new Location(Gdx.graphics.getWidth() - 100 * Size.getXSizeMultiplier(),
                Gdx.graphics.getHeight() - 100 * Size.getYSizeMultiplier()), world));
    }

    @Override
    public void render() {
        Gdx.gl.glLineWidth(Size.getXSizeMultiplier());
        spriteBatch.setProjectionMatrix(gameCamera.combined);
        shapeRenderer.setProjectionMatrix(gameCamera.combined);
        world.render();
        hudElements.forEach(UIElement::render);

        if (!(world.getMainCharacter().isAlive()) && hudElements.stream()
                .noneMatch(hudElement -> hudElement instanceof HUDDeath)) {
            hudElements.clear();
            hudElements.add(new HUDDeath());
        }

        if (paused) {
            hudPause.render();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (!paused) {
            if (world.getMainCharacter().isAlive()) {
                world.update(deltaTime);
                hudElements.forEach(hudElement -> hudElement.update(deltaTime));
            } else {
                gameLogic.getKeyInput().isPressed(Input.Keys.R, () -> {
                    hudElements.clear();
                    world = new World(this);
                    initHUD();
                });
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
}
