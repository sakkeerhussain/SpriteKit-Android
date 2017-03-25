package com.explorerz.carroms.ui;
/*
 * Created by sakkeerhussain on 25/03/17.
 */

import android.graphics.PointF;
import android.graphics.RectF;

import com.explorerz.carroms.game.Game;

public class CaromBoardSprite extends Sprite {

    public CaromBoardSprite() {
        super();
        float centerX = Game.TOTAL_HEIGHT / 2;
        base = new RectF(-centerX, centerX, centerX, -centerX);
        translation = new PointF(centerX, centerX);
    }
}
