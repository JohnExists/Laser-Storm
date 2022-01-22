package me.johnexists.game1.logic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import me.johnexists.game1.input.KeyInput;
import me.johnexists.game1.state.MainMenuState;
import me.johnexists.game1.state.State;

import java.util.Optional;

@SuppressWarnings("all")
public class GameLogic extends Game {

    private final float CYCLE_SPEED = 0.01f;

    private UpdateLogic updateLogic;
    private RenderLogic renderLogic;
    private KeyInput keyInput;
    private Optional<State> selectedState;
    private float r = 0f, g = 0, b = 0;
    private boolean reversed = false;
    char currSel = 'r';

    @Override
    public void create() {
        updateLogic = new UpdateLogic();
        keyInput = new KeyInput();
        renderLogic = new RenderLogic();
        selectedState = Optional.of(new MainMenuState(this));

        Gdx.input.setInputProcessor(keyInput);

        keyInput.registerOnKeyReleased(Input.Keys.F11,
                () -> {
                    boolean fullScreen = Gdx.graphics.isFullscreen();
                    Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                    if (fullScreen) {
                        Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
                    } else {
                        Gdx.graphics.setFullscreenMode(currentMode);
                    }
                });
    }

    @Override
    public void render() {
//        ScreenUtils.clear(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
        ScreenUtils.clear(Color.DARK_GRAY);
//        ScreenUtils.clear(0, 0.75f, 0.75f, 1);
        selectedState.ifPresent(state -> updateLogic.update(state));
        renderLogic.render(selectedState.get());

    }

    public Color cycle() {
        if (!reversed) {
            switch (currSel) {
                case 'r' -> {
                    r += CYCLE_SPEED;
                    if (r > 1) {
                        currSel = 'g';
                    }
                }
                case 'g' -> {
                    g += CYCLE_SPEED;
                    if (g > 1) {
                        currSel = 'b';
                        r = 0;
                    }
                }
                case 'b' -> {
                    b += CYCLE_SPEED;
                    if (b > 1) {
                        currSel = 'r';
                        r = 0f;
                        g = 0;
                        b = 0;
                    }
                }
            }
        }
        return new Color(r, g, b, 1f);
    }

    public Color shiftColour(float r, float g, float b) {
        return Color.PURPLE;
    }

    public void setSelectedState(Optional<State> selectedState) {
        this.selectedState = selectedState;
    }

    @Override
    public void dispose() {
        selectedState.ifPresent(state -> renderLogic.dispose(state));
    }

    public KeyInput getKeyInput() {
        return keyInput;
    }

    public State getSelectedState() {
        return selectedState.orElse(null);
    }
}
