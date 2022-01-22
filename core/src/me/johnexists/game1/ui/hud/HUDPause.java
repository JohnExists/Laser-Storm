package me.johnexists.game1.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.state.GameState;
import me.johnexists.game1.state.MainMenuState;
import me.johnexists.game1.ui.UIElement;
import me.johnexists.game1.ui.uimenu.UIButton;

import static java.util.Optional.of;

public class HUDPause extends UIElement {

    private final ShapeRenderer renderBackground;
    private final GlyphLayout glyphLayout;
    private UIButton resumeButton, restartButton, mainMenuButton;

    private GameState gameState;
    private final float WIDTH = Gdx.graphics.getWidth(),
            HEIGHT = Gdx.graphics.getHeight();

    public HUDPause(GameState gameState) {
        super(new Location(0, 0),
                new Size(Gdx.graphics.getWidth() / Size.getXSizeMultiplier(),
                        Gdx.graphics.getHeight() / Size.getYSizeMultiplier()));

        this.gameState = gameState;
        renderBackground = new ShapeRenderer();
        renderBackground.setAutoShapeType(true);
        glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, "Paused",
                Color.WHITE, Gdx.graphics.getWidth(), Align.center, true);

        initButtons(gameState);
    }

    private void initButtons(GameState gameState) {

        resumeButton = new UIButton(gameState);
        resumeButton.setLocation(new Location(WIDTH / 2, HEIGHT / 2 - 150));
        resumeButton.setDisplayText("Resume");
        resumeButton.setOnClick(gameState::togglePause);
        resumeButton.setScale(0.85f);
        resumeButton.setColor(Color.WHITE);

        restartButton = new UIButton(gameState);
        restartButton.setLocation(new Location(WIDTH / 2, HEIGHT / 2 - 225));
        restartButton.setDisplayText("Restart");
        restartButton.setOnClick(() -> {
            gameState.createNewWorld();
            gameState.initHUD();
        });
        restartButton.setScale(0.85f);
        restartButton.setColor(Color.WHITE);

        mainMenuButton = new UIButton(gameState);
        mainMenuButton.setLocation(new Location(WIDTH / 2, HEIGHT / 2 - 300));
        mainMenuButton.setDisplayText("Main Menu");
        mainMenuButton.setOnClick(() -> {
            gameState.getGameLogic().getKeyInput().cleanInputs();
            gameState.getGameLogic().setSelectedState(of(new MainMenuState(gameState.getGameLogic())));
        });
        mainMenuButton.setScale(0.85f);
        mainMenuButton.setColor(Color.WHITE);

    }

    @Override
    public void update(float deltaTime) {
        if (gameState.getGameLogic().getSelectedState() instanceof GameState) {
            resumeButton.update(deltaTime);
            restartButton.update(deltaTime);
            mainMenuButton.update(deltaTime);
        }
    }

    @Override
    public void render() {
        renderPauseScreen();
        spriteBatch.begin();
        {
            font.draw(spriteBatch, glyphLayout, 0,
                    HEIGHT / 2 + glyphLayout.height / 2);
        }
        spriteBatch.end();

        resumeButton.render();
        restartButton.render();
        mainMenuButton.render();
    }

    public void renderPauseScreen() {
        renderBackground.begin(ShapeRenderer.ShapeType.Filled);
        {
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderBackground.setColor(new Color(0, 0, 0, 0.25f));
            renderBackground.rect(0, 0, WIDTH, HEIGHT);
        }
        renderBackground.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        super.dispose();
        renderBackground.dispose();
    }
}
