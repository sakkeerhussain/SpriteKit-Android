package com.explorerz.carroms.ui;
/*
 * Created by sakkeerhussain on 25/03/17.
 */

import android.graphics.PointF;
import android.graphics.RectF;

import com.explorerz.carroms.game.Game;

import static com.explorerz.carroms.game.Game.RADIUS_COIN;

public class RedCaromSprite extends Sprite {

    public RedCaromSprite() {
        super();
        float centerX = Game.TOTAL_HEIGHT / 2;
        base = new RectF(-RADIUS_COIN, RADIUS_COIN, RADIUS_COIN, -RADIUS_COIN);
        translation = new PointF(centerX, centerX);
    }
}
