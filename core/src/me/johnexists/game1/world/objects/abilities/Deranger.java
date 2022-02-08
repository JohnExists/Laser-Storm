package me.johnexists.game1.world.objects.abilities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.johnexists.game1.world.objects.GameObject;
import me.johnexists.game1.world.objects.entities.DamageableEntity;
import me.johnexists.game1.world.objects.entities.Player;
import me.johnexists.game1.world.objects.entities.enemies.Enemy;
import me.johnexists.game1.world.objects.weapons.generators.GeneratorConstants;
import me.johnexists.game1.world.objects.weapons.lasers.Laser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static me.johnexists.game1.world.objects.attributes.Size.getXSizeMultiplier;
import static me.johnexists.game1.world.objects.attributes.Size.getYSizeMultiplier;

@SuppressWarnings("SpellCheckingInspection")
public class Deranger extends Ability {

    private final Player player;
    private List<AbilityLaser> lasers;

    public Deranger(Player player) {
        this.player = player;
        lasers = new ArrayList<>();
        initList();
        ticksLeft = 5;
    }

    @Override
    protected void whileActive(float deltaTime) {
        for (int i = 0; i < lasers.size(); i++) {
            lasers.get(i).update(deltaTime);
            if (!lasers.get(i).getTarget().isAlive()) {
                if (getNearbyEntities().size() > 0) {
                    DamageableEntity substitute = (DamageableEntity) getNearbyEntities().get(0);
                    if (nonNull(substitute)) {
                        lasers.set(i, new AbilityLaser(substitute));
                    }
                }
            }
        }

        lasers = lasers.stream()
                .filter(laser -> laser.getTarget().isAlive())
                .collect(Collectors.toList());

        if(lasers.size() == 0) {
            initList();
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        lasers.forEach(a -> a.renderLaserBody(spriteBatch, shapeRenderer));
    }

    public void initList() {
        getNearbyEntities().forEach(obj -> lasers.add(new AbilityLaser((DamageableEntity) obj)));
    }

    public List<GameObject> getNearbyEntities() {
        return player.getNearbyEntities(350 * getXSizeMultiplier(),
                        350 * getYSizeMultiplier())
                .stream().filter(g -> g instanceof Enemy)
                .limit(5)
                .collect(Collectors.toList());
    }

    private class AbilityLaser extends Laser {

        private final DamageableEntity target;

        public AbilityLaser(DamageableEntity target) {
            super(player, target, Player.getLaserGenerator());
            this.target = target;
            enable();
        }

        @Override
        protected void renderLaserBody(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
            basicLaserBody(spriteBatch, Player.getLaserSkin(), 0f);
        }

        public DamageableEntity getTarget() {
            return target;
        }
    }
}
