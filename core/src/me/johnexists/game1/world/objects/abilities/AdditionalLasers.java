package me.johnexists.game1.world.objects.abilities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.weapons.lasers.BasicLaser;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;

import java.util.ArrayList;
import java.util.List;

public class AdditionalLasers extends Ability {

    private final MultipleLasers multipleLasers;
    private final Player player;

    public AdditionalLasers(Player player, boolean isPenta) {
        this.player = player;
        multipleLasers = new MultipleLasers(isPenta);
        ticksLeft = 29f;
    }

    @Override
    protected void whileActive(float deltaTime) {
        multipleLasers.update(deltaTime);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        multipleLasers.render(spriteBatch, shapeRenderer);
    }

    public class MultipleLasers extends Laser {

        private final List<BasicLaser> basicLasers;

        public MultipleLasers(boolean isPenta) {
            super(player, Player.laserGenerator);
            basicLasers = new ArrayList<>();

            if (isPenta) {
                for (int i = -2; i < 3; i++) {
                    if (i != 0) {
                        basicLasers.add(new BasicLaser(wielder, i * 9, Player.laserGenerator));
                    }
                }
            } else {
                basicLasers.add(new BasicLaser(wielder, -9f, Player.laserGenerator));
                basicLasers.add(new BasicLaser(wielder, 9f, Player.laserGenerator));
            }
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            basicLasers.forEach(b -> b.update(deltaTime));
        }

        @Override
        protected void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
            basicLasers.forEach(b -> b.renderLaserBody(spriteBatch, shapeRenderer));
        }
    }

}
