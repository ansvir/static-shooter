package org.itique.ss.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import org.itique.ss.util.TextureUtil;

public class HeroActor extends Actor implements Disposable {

    private float health;
    private int bullets;
    private Texture heroTexture;
    private Texture heroHands;
    private Texture currentGun;
    private int xPos;
    private int yPos;

    public HeroActor(int xPos, int yPos) {
        this.health = 100.0f;
        this.bullets = 30 * 6;
        heroHands = new Texture("hands1.png");
        currentGun = new Texture("gun1.png");
        heroTexture = TextureUtil.mergeTextures(heroHands, currentGun, 0, 0, 37, 2);
        setOrigin(Align.bottom);
        setX(640 / 2f);
        setY(-20);
        setSize(350, 350);
        setZIndex(2);
        this.xPos = xPos;
        this.yPos = yPos;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.heroTexture, getX(), getY(), getWidth(), getHeight());
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getBullets() {
        return bullets;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public Texture getHeroTexture() {
        return heroTexture;
    }

    public void setHeroTexture(Texture heroTexture) {
        this.heroTexture = heroTexture;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int x) {
        this.xPos = x;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int y) {
        this.yPos = y;
    }

    @Override
    public void dispose() {
        heroHands.dispose();
        currentGun.dispose();
    }
}
