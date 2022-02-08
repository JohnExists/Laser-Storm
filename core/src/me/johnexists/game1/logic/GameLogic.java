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

    private static final float CYCLE_SPEED = 0.01f;

    private static float r = 0f, g = 0, b = 0;
    private static boolean reversed = false;
    static char currSel = 'r';

    private UpdateLogic updateLogic;
    private RenderLogic renderLogic;
    private KeyInput keyInput;
    private Optional<State> selectedState;

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
        ScreenUtils.clear(Color.DARK_GRAY);
        selectedState.ifPresent(state -> updateLogic.update(state));
        renderLogic.render(selectedState.get());

    }

    public static Color cycle() {
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

    @SuppressWarnings("all")
    public static String formatNumber(double n, int iteration) {
        if(n < 1000) {
            return String.valueOf(Math.round(n));
        }
        char[] c = new char[]{'k', 'm', 'b', 't'};
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) % 10 == 0;
        return (d < 1000 ?
                ((d > 99.9 || isRound || (!isRound && d > 9.99) ?
                        (int) d * 10 / 10 : d + ""
                ) + "" + c[iteration])
                : formatNumber(d, iteration + 1));

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
