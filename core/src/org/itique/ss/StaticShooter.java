package org.itique.ss;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StaticShooter extends ApplicationAdapter {

	Stage stage;
	Viewport viewport;
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture background;
	Texture enemy;
	Cursor cursor;
	Pixmap cursorPixmap;
	
	@Override
	public void create () {
		cursorPixmap = new Pixmap(Gdx.files.internal("target.png"));
		cursor = Gdx.graphics.newCursor(cursorPixmap, cursorPixmap.getWidth() / 2,
				cursorPixmap.getHeight() / 2);
		Gdx.graphics.setCursor(cursor);
		enemy = new Texture("enemy.png");
		camera = new OrthographicCamera();
		viewport = new FitViewport(640, 480, camera);
		batch = new SpriteBatch();
		stage = new Stage(viewport, batch);
		background = new Texture("field.png");
	}

	@Override
	public void render () {
		ScreenUtils.clear(Color.WHITE);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		cursor.dispose();
		enemy.dispose();
		cursor.dispose();
		stage.dispose();
	}
}
