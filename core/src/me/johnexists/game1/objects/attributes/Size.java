package me.johnexists.game1.objects.attributes;

import com.badlogic.gdx.Gdx;

public class Size {

    private final float width, height;
    private final static float xSizeMultiplier = Gdx.graphics.getWidth() / 640f,
            ySizeMultiplier = Gdx.graphics.getHeight() / 360f;

    public Size(float width, float height) {
        this.width = width * xSizeMultiplier;
        this.height = height * ySizeMultiplier;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }


    public static float getXSizeMultiplier() {
        return xSizeMultiplier;
    }

    public static float getYSizeMultiplier() {
        return ySizeMultiplier;
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
