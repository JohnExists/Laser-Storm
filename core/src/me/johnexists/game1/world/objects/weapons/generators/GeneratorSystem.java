package me.johnexists.game1.world.objects.weapons.generators;

import me.johnexists.game1.world.objects.attributes.LaserWielder;
import me.johnexists.game1.world.objects.entities.DamageableEntity;

public class GeneratorSystem {

    public LaserWielder component;

    public GeneratorSystem(LaserWielder component) {
        this.component = component;
    }

    public static float returnModifiedDamage(GeneratorConstants generatorConstants, DamageableEntity damageableEntity, float scalar) {
        float nearbyEntities = damageableEntity.getNearbyEntities(12, 12)
                .size();

        if(generatorConstants.equals(GeneratorConstants.HEALTH_UPSCALAR)) {
            damageableEntity.heal(scalar * 1.5f);
        }

        return switch (generatorConstants) {
            case NONE -> 0.0F;
            case MINI -> 85;
            case MAX -> 165F;
            case QUANTITY_SCALAR -> nearbyEntities * 5f * scalar;
            case QUANTITY_MAX_SCALAR -> nearbyEntities * 7.5f * scalar;
            case HEALTH_UPSCALAR -> 15f * scalar;
            case HYPER_CHARGED -> 800f;
        };

    }
}
