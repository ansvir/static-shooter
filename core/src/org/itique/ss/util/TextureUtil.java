package org.itique.ss.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class TextureUtil {

    public static Texture resizeTexture(Texture texture, int width, int height) {
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        Pixmap newPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        newPixmap.drawPixmap(pixmap, 0, 0);
        Texture newTexture = new Texture(newPixmap);
        newPixmap.dispose();
        texture.dispose();
        return newTexture;
    }

    public static Texture mergeTextures(Texture srcTexture, Texture destTexture, int srcX, int srcY, int destX, int destY) {
        if (!srcTexture.getTextureData().isPrepared()) {
            srcTexture.getTextureData().prepare();
        }
        if (!destTexture.getTextureData().isPrepared()) {
            destTexture.getTextureData().prepare();
        }
        Pixmap srcPixmap = srcTexture.getTextureData().consumePixmap();
        Pixmap destPixmap = destTexture.getTextureData().consumePixmap();
        Pixmap newPixmap = new Pixmap(Math.max(srcPixmap.getWidth(), destPixmap.getWidth()),
                Math.max(srcPixmap.getHeight(), destPixmap.getHeight()), Pixmap.Format.RGBA8888);
        newPixmap.drawPixmap(srcPixmap, srcX, srcY);
        newPixmap.drawPixmap(destPixmap, destX, destY);
        Texture newTexture = new Texture(newPixmap);
        newPixmap.dispose();
        srcTexture.dispose();
        destTexture.dispose();
        return newTexture;
    }

}
