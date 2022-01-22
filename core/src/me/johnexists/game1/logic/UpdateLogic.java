package me.johnexists.game1.logic;

import com.badlogic.gdx.Gdx;
import me.johnexists.game1.state.State;

public class UpdateLogic {

    public static final float UPDATES_PER_SECOND = 1/60.0f;


    public void update(State state) {
        float deltaTime = Math.max(Gdx.graphics.getDeltaTime(), UPDATES_PER_SECOND);
        state.update(deltaTime);

    }

}
