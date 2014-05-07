package com.znv.linkup.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.znv.linkup.R;
import com.znv.linkup.ViewSettings;
import com.znv.linkup.core.Game;
import com.znv.linkup.core.GameSettings;
import com.znv.linkup.core.card.Piece;
import com.znv.linkup.core.card.PiecePair;
import com.znv.linkup.core.card.path.LinkInfo;
import com.znv.linkup.core.util.ImageUtil;

/**
 * 主游戏显示区，负责卡片图片显示，消除路径显示，选中显示
 * 
 * @author yzb
 * 
 */
public class GameView extends View {

    private Game game;
    private Piece selectedPiece;
    private LinkInfo linkInfo;
    private Paint paint;
    private Bitmap selectedImage;
    private PiecePair promptPieces;
    private int centerOffset = 0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        // paint.setStrokeWidth(ViewSettings.PathImageWidth);
    }

    /**
     * 设置游戏逻辑处理时设置图片，可以在此实现换肤
     * 
     * @param game
     *            游戏逻辑处理的接口实现类，game对象
     */
    public void setGameService(Game game) {
        this.game = game;

        Piece[][] pieces = game.getPieces();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                Piece piece = pieces[i][j];
                if (piece != null) {
                    // 设置游戏卡片和障碍卡片
                    if (piece.getImageId() == GameSettings.ObstacleCardValue) {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.obstacle);
                        piece.setImage(ImageUtil.scaleBitmap(bm, piece.getWidth(), piece.getHeight()));
                    } else if (piece.getImageId() != GameSettings.GroundCardValue) {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), ViewSettings.CardImageIds[piece.getImageId() - 1]);
                        piece.setImage(ImageUtil.scaleBitmap(bm, piece.getWidth(), piece.getHeight()));
                    }
                }
            }
        }
    }

    /**
     * 设置连接路径
     * 
     * @param linkInfo
     *            连接路径信息
     */
    public void setLinkInfo(LinkInfo linkInfo) {
        if (linkInfo == null || linkInfo.getLinkPieces().size() < 2) {
            return;
        }

        // 设置连接路径图片
        Piece p = linkInfo.getLinkPieces().get(0);
        centerOffset = p.getWidth() / 6;
        Bitmap pathImage = ImageUtil.scaleBitmap(p.getImage(), p.getWidth() / 3, p.getHeight() / 3);
        paint.setShader(new BitmapShader(pathImage, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        this.linkInfo = linkInfo;
    }

    /**
     * 设置选择卡片时显示的图片
     * 
     * @param bm
     *            选择的背景图片
     */
    public void setSelectedImage(Bitmap bm) {
        selectedImage = bm;
    }

    /**
     * 设置选择的卡片
     * 
     * @param piece
     *            选择的卡片信息
     */
    public void setSelectedPiece(Piece piece) {
        this.selectedPiece = piece;
    }

    /**
     * 设置提示的卡片对
     * 
     * @param promptPieces
     *            提示的卡片对
     */
    public void setPromptPieces(PiecePair promptPieces) {
        this.promptPieces = promptPieces;
    }

    /**
     * 绘制游戏区
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (game == null) {
            return;
        }
        Piece[][] pieces = game.getPieces();

        if (promptPieces != null) {
            drawSelectPiece(canvas, promptPieces.getPieceOne());
            drawSelectPiece(canvas, promptPieces.getPieceTwo());
            promptPieces = null;
        } else if (Piece.hasImage(selectedPiece)) {
            drawSelectPiece(canvas, selectedPiece);
        }

        /**
         * 绘制所有的游戏图片和障碍图片
         */
        if (pieces != null) {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (Piece.hasImage(pieces[i][j])) {
                        drawPiece(canvas, pieces[i][j]);
                    }
                }
            }
        }

        if (promptPieces == null && linkInfo != null) {
            drawLine(canvas, linkInfo);
            linkInfo = null;
        }
    }

    /**
     * 绘制选中图片
     * 
     * @param canvas
     *            画布
     * @param piece
     *            选择的卡片信息
     */
    private void drawSelectPiece(Canvas canvas, Piece piece) {
        canvas.drawBitmap(selectedImage, piece.getBeginX(), piece.getBeginY(), null);
    }

    /**
     * 绘制游戏卡片
     * 
     * @param canvas
     *            画布
     * @param piece
     *            游戏卡片信息
     */
    private void drawPiece(Canvas canvas, Piece piece) {
        canvas.drawBitmap(piece.getImage(), piece.getBeginX(), piece.getBeginY(), null);
    }

    /**
     * 绘制连接路径
     * 
     * @param canvas
     *            画布
     * @param linkInfo
     *            连接路径信息
     */
    private void drawLine(Canvas canvas, LinkInfo linkInfo) {
        List<Piece> linkPieces = linkInfo.getLinkPieces();
        for (int i = 0; i < linkPieces.size() - 1; i++) {
            Point currentPoint = linkPieces.get(i).getCenter();
            Point nextPoint = linkPieces.get(i + 1).getCenter();
            // 调整路径位置，完整显示路径图片
            adjustCenter(currentPoint, nextPoint, i);
            canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y, paint);
        }
    }

    /**
     * 调整路径位置，完整显示路径图片
     * 
     * @param currentPoint
     *            当前点
     * @param nextPoint
     *            下一个点
     * @param i
     *            路径点索引
     */
    private void adjustCenter(Point currentPoint, Point nextPoint, int i) {
        if (i == 0) {
            if (currentPoint.x == nextPoint.x) {
                currentPoint.y = (nextPoint.y > currentPoint.y) ? (currentPoint.y + centerOffset) : (currentPoint.y - centerOffset);
            } else {
                currentPoint.x = (nextPoint.x > currentPoint.x) ? (currentPoint.x + centerOffset) : (currentPoint.x - centerOffset);
            }
        } else if (i == linkInfo.getLinkPieces().size() - 2) {
            if (currentPoint.x == nextPoint.x) {
                nextPoint.y = (nextPoint.y > currentPoint.y) ? (nextPoint.y - centerOffset) : (nextPoint.y + centerOffset);
            } else {
                nextPoint.x = (nextPoint.x > currentPoint.x) ? (nextPoint.x - centerOffset) : (nextPoint.x + centerOffset);
            }
        }
    }
}
