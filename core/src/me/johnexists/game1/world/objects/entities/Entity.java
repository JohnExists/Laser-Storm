package me.johnexists.game1.world.objects.entities;

import me.johnexists.game1.world.objects.attributes.Location;
import me.johnexists.game1.world.objects.attributes.Velocity;
import me.johnexists.game1.world.objects.entities.enemies.enemyai.AITaskHost;
import me.johnexists.game1.world.objects.GameObject;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Entity extends GameObject {
    protected Velocity velocity;

    public Entity(Location location) {
        super(location);
        velocity = Velocity.getEmptyVelocity();
    }

    @Override
    public void update(float deltaTime) {
        if (!(this instanceof AITaskHost)) {
            location.add(velocity.multiply(deltaTime)); // moves player
            velocity = Velocity.getEmptyVelocity(); // stops player
        }
    }

    public List<GameObject> getNearbyEntities(float getNearbyOnXAxis, float getNearbyOnYAxis) {
        return location.getWorld().getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof Entity)
                .filter(gameObject -> Math.abs(gameObject.getLocation().getX() - location.getX()) < getNearbyOnXAxis)
                .filter(gameObject -> Math.abs(gameObject.getLocation().getY() - location.getY()) < getNearbyOnYAxis)
                .collect(Collectors.toList());

    }

}
