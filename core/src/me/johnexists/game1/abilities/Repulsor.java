package me.johnexists.game1.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.johnexists.game1.objects.attributes.Location;
import me.johnexists.game1.objects.attributes.Size;
import me.johnexists.game1.objects.entities.DamageableEntity;
import me.johnexists.game1.objects.entities.Player;

@SuppressWarnings("all")
public class Repulsor extends Ability {

    private final Player player;
    private final float STARTER_TICKS = 2;

    public Repulsor(Player player) {
        this.player = player;
        ticksLeft = STARTER_TICKS;
    }

    @Override
    public void whileActive(float deltaTime) {
        float radius = (75 + (STARTER_TICKS - ticksLeft) * 120) * Size.getXSizeMultiplier();

        player.getNearbyEntities(radius, radius)
                .stream().filter(gameObject -> !(gameObject instanceof Player))
                .forEach(gameObject -> {
                    if (gameObject instanceof DamageableEntity) {
                        ability((DamageableEntity) gameObject);
                    }
                });

    }

    private void ability(DamageableEntity damageableEntity) {
        Location locationToPlayer = damageableEntity.getLocation().distanceTo(player);
        Vector2 velocityVector = new Vector2(locationToPlayer.getX(), locationToPlayer.getY());
        velocityVector.nor();
        velocityVector.x *= -45;
        velocityVector.y *= -45;

        damageableEntity.getLocation().add(new Location(velocityVector.x, velocityVector.y));
        player.setImmortal(true);

    }

    @Override
    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch) {
        float radius = (75 + (STARTER_TICKS - ticksLeft) * 120) * Size.getXSizeMultiplier();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        {
            shapeRenderer.setColor(Color.MAROON);
            shapeRenderer.circle(player.getLocation().getX(), player.getLocation().getY(), radius);
        }
        shapeRenderer.end();
    }
}
