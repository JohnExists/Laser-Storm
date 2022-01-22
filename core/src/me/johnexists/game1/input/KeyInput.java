package me.johnexists.game1.input;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyInput implements InputProcessor {

    private final List<Integer> keyInputs;
    private final List<MouseClickRegister> onMouseReleasedEvents;
    private final Map<KeyUpRegister, Integer> onKeyReleasedEvents;

    public KeyInput() {
        keyInputs = new ArrayList<>();
        onMouseReleasedEvents =  new ArrayList<>();
        onKeyReleasedEvents = new HashMap<>();
    }

    @Override
    public boolean keyDown(int keycode) {
        keyInputs.add(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyInputs.remove(Integer.valueOf(keycode));

        onKeyReleasedEvents.forEach((keyUpRegister, integer) -> {
            if(integer == keycode) {
                keyUpRegister.onRelease();
            }
        });

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    @SuppressWarnings("all")
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (int i = 0; i < onMouseReleasedEvents.size(); i++) {
            onMouseReleasedEvents.get(i).onClick();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void isPressed(int keycode, Runnable onPressed) {
        if (keyInputs.contains(keycode)) {
            onPressed.run();
        }
    }

    public void registerOnMouseReleased(MouseClickRegister mouseClickRegister) {
        onMouseReleasedEvents.add(mouseClickRegister);
    }

    public void cleanInputs() {
        onMouseReleasedEvents.clear();
    }

    public void registerOnKeyReleased(int keycode, KeyUpRegister keyUpRegister) {
        onKeyReleasedEvents.put(keyUpRegister, keycode);
    }
}
