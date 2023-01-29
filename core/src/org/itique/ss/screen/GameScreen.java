package org.itique.ss.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.itique.ss.actor.EnemyActor;
import org.itique.ss.actor.EnemyStatus;
import org.itique.ss.actor.HeroActor;
import org.itique.ss.map.Cell;
import org.itique.ss.map.DefaultMap;

import java.util.Random;

public class GameScreen implements Screen {

    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture background;
    private int xRange;
    private int yRange;
    private Game game;
    private long previousSpawn;
    private Music backgroundMusic;
    private HeroActor hero;
    private Cell[][] tiledMap;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("war_background.mp3"));
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.play();
        previousSpawn = System.currentTimeMillis();
        camera = new OrthographicCamera();
        viewport = new FillViewport(640, 480, camera);
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        xRange = stage.getViewport().getScreenWidth();
        yRange = stage.getViewport().getScreenHeight() / 3;
        background = new Texture("field.png");
        tiledMap = DefaultMap.getMap();
        hero = new HeroActor(tiledMap.length / 2, tiledMap[0].length / 2);
        this.stage.addActor(hero);
        this.stage.addActor(spawnEnemy());
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
        updateMotion(delta);
        int cursorY = Gdx.app.getInput().getY();
        if (!(cursorY <= stage.getViewport().getScreenHeight() - hero.getHeight())) {
            hero.setY(hero.getHeight() / 4f - cursorY);
        }
        hero.setX(Gdx.app.getInput().getX() + 60);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.update();
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.getActors().forEach(a -> { if (a instanceof EnemyActor) { ((EnemyActor) a).dispose(); }});
        stage.dispose();
        backgroundMusic.dispose();
        hero.dispose();
    }

    private EnemyActor spawnEnemy() {
        Random random = new Random();
        return new EnemyActor(random.nextInt(xRange) + 10,
                yRange, 200.0f);
    }

    private void updateMotion(float delta) {
        if (System.currentTimeMillis() - previousSpawn >= 6000L) {
            previousSpawn = System.currentTimeMillis();
            EnemyActor enemyActor = spawnEnemy();
            enemyActor.walk(delta);
            enemyActor.setTimeline(System.currentTimeMillis());
            stage.addActor(enemyActor);
        }
        Array<Actor> actorsToRemove = new Array<>();
        stage.getActors().forEach(a -> {
            if (a instanceof EnemyActor) {
                long time = System.currentTimeMillis();
                if (time - ((EnemyActor) a).getTimeline() >= 1000L
                        && time - ((EnemyActor) a).getTimeline() <= 4000L) {
                    ((EnemyActor) a).stop(delta);
                    ((EnemyActor) a).shoot(delta);
                    Random random = new Random();
                    int probability = random.nextInt(100);
                    if (probability >= 50) {
                        this.hero.setHealth(this.hero.getHealth() - 1.0f);
                    }
                } else {
                    ((EnemyActor) a).setTimeline(time);
                    if (((EnemyActor) a).getStatus() != EnemyStatus.DEAD) {
                        ((EnemyActor) a).walk(delta);
                    }
                }
                a.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ((EnemyActor) a).heart(delta);
                    }
                });
            }
        });
        stage.getActors().forEach(a -> {
            if (a instanceof EnemyActor && ((EnemyActor) a).getStatus() == EnemyStatus.DEAD) {
                ((EnemyActor) a).down(delta);
                if (((EnemyActor) a).invalidate(delta)) {
                    actorsToRemove.add(a);
                }
            }
        });
        stage.getActors().removeAll(actorsToRemove, true);
    }
}
