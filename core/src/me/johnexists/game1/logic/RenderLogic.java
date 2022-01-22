package me.johnexists.game1.logic;

import me.johnexists.game1.state.State;

public class RenderLogic {

    public void render(State state) {
        state.render();
    }

    public void dispose(State state) {
        state.dispose();
    }

}
