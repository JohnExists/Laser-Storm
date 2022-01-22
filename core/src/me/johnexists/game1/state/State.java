package me.johnexists.game1.state;

import me.johnexists.game1.logic.GameLogic;

public abstract class State {

    GameLogic gameLogic;


    public State(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public abstract void render();
    public abstract void update(float deltaTime);
    public abstract void dispose();

}
