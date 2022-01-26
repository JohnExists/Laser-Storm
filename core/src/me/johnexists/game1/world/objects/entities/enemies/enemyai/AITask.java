package me.johnexists.game1.world.objects.entities.enemyai;

public interface AITask {

    int PASSIVE = 0, AGGRESSIVE_NEAR = 1, AGGRESSIVE_FAR = 2, AGGRESSIVE_LAUNCH = 3;

    void update(float deltaTime);

}