package me.johnexists.game1.world.objects.attributes;

public interface Damageable {

    float MAX_HEALTH = 100;
    float MAX_OVERFLOW_HEALTH = 450;

    void damage(float damage);
    void heal(float heal);
    void overflowHeal(float heal);

}
