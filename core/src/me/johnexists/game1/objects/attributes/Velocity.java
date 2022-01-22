package me.johnexists.game1.objects.attributes;

import com.badlogic.gdx.math.Vector2;

public class Velocity {

    Vector2 vector;

    public Velocity(Vector2 vector) {
        this.vector = new Vector2(vector.x,
                vector.y);
    }

    public Velocity(float x, float y) {
        this(new Vector2(x, y));
    }

    public Vector2 getVector() {
        return vector;
    }


    public static Velocity getEmptyVelocity() {
        return new Velocity(0, 0);
    }

    public Velocity multiply(float multiplyBy) {
        return new Velocity(vector.x *= multiplyBy,
                vector.y * multiplyBy);
    }

    public void add(float x, float y) {
        vector.add(new Vector2(x, y));
    }
}
