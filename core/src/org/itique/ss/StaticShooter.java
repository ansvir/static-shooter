package org.itique.ss;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import org.itique.ss.screen.GameScreen;

public class StaticShooter extends Game {


	private Cursor cursor;
	private Pixmap cursorPixmap;
	
	@Override
	public void create () {
		cursorPixmap = new Pixmap(Gdx.files.internal("target.png"));
		cursor = Gdx.graphics.newCursor(cursorPixmap, cursorPixmap.getWidth() / 2,
				cursorPixmap.getHeight() / 2);
		Gdx.graphics.setCursor(cursor);
		setScreen(new GameScreen(this));
	}
	
	@Override
	public void dispose () {
		cursor.dispose();
		cursorPixmap.dispose();
	}
}
