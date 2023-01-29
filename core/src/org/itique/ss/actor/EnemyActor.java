package org.itique.ss.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class EnemyActor extends Actor implements Disposable {

    private static final int COLS_ANIM = 3;
    private static final int ROWS_ANIM = 2;
    private float health;
    private TextureRegion texture;
    private long timeline;
    private EnemyStatus status;
    private int bullets;
    private Animation<TextureRegion> animationWalk;
    private Animation<TextureRegion> animationShoot;
    private Animation<TextureRegion> animationDown;
    private Direction direction;
    private float stateTime;
    private Texture animSheet;
    private long dieTime;

    public EnemyActor(int x, int y, float health) {
        super.setX(x);
        super.setY(y);
        this.direction = Direction.DOWN;
        this.animSheet = new Texture(Gdx.files.internal("enemy.png"));
        super.setSize((float) animSheet.getWidth() / COLS_ANIM / 4,
                (float) animSheet.getHeight() / ROWS_ANIM / 4);
        this.health = health;
        this.status = EnemyStatus.ALIVE;
        this.bullets = 30 * 3;
        TextureRegion[][] tmp = TextureRegion.split(animSheet,
                animSheet.getWidth() / COLS_ANIM,
                animSheet.getHeight() / ROWS_ANIM);
        TextureRegion[] walkRegions = new TextureRegion[1];
        TextureRegion[] shootRegions = new TextureRegion[1];
        TextureRegion[] downRegions = new TextureRegion[3];
        walkRegions[0] = tmp[0][0];
        shootRegions[0] = tmp[0][1];
        downRegions[0] = tmp[1][0];
        downRegions[1] = tmp[1][1];
        downRegions[2] = tmp[1][2];
        this.animationWalk = new Animation<>(3f, walkRegions);
        this.animationShoot = new Animation<>(3f, shootRegions);
        this.animationDown = new Animation<>(3f, downRegions);
        this.stateTime = 0f;
        setZIndex(1);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void heart(float delta) {
        this.health -= 1.0f;
        if (this.health <= 0.0f) {
            this.status = EnemyStatus.DEAD;
            this.dieTime = System.currentTimeMillis();
            this.direction = Direction.STOP;
        }
    }

    public void shoot(float delta) {
        this.stateTime += delta;
        this.bullets -= 3;
        this.texture = animationShoot.getKeyFrame(this.stateTime, false);
    }

    public void walk(float delta) {
        this.stateTime += delta;
        this.texture = animationWalk.getKeyFrame(this.stateTime, true);
        if (this.direction == Direction.DOWN) {
            setY(getY() - 1);
            setSize(getWidth() + 0.5f, getHeight() + 1f);
        } else if (this.direction == Direction.UP){
            setY(getY() + 1);
            setSize(getWidth() - 0.5f, getHeight() - 1f);
        }
    }

    public void stop(float delta) {
        this.stateTime += delta;
        this.texture = animationWalk.getKeyFrame(this.stateTime, true);
        this.direction = Direction.STOP;
    }

    public void down(float delta) {
        this.stateTime += delta;
        this.texture = animationDown.getKeyFrame(this.stateTime, false);
        this.direction = Direction.STOP;
    }

    public boolean invalidate(float delta) {
        this.stateTime += delta;
        return System.currentTimeMillis() - this.dieTime >= 10000L;
    }

    public boolean hasBullets() {
        return this.bullets > 0;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public long getTimeline() {
        return timeline;
    }

    public void setTimeline(long timeline) {
        this.timeline = timeline;
    }

    public EnemyStatus getStatus() {
        return status;
    }

    public void setStatus(EnemyStatus status) {
        this.status = status;
    }

    public int getBullets() {
        return bullets;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.texture, getX(), getY(), getWidth(), getHeight());
    }


    @Override
    public void dispose() {
        this.animSheet.dispose();
    }
}
