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
import com.znv.linkup.base.IGameService;
import com.znv.linkup.base.card.Piece;
import com.znv.linkup.base.card.PiecePair;
import com.znv.linkup.base.card.path.LinkInfo;

public class GameView extends View {

    private IGameService gameService;
    private Piece selectedPiece;
    private LinkInfo linkInfo;
    private Paint paint;
    private Paint textPaint;
    private Bitmap selectedImage;
    private PiecePair promptPieces;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // setBackgroundResource(R.drawable.sky);

        paint = new Paint();
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        paint.setStrokeWidth(9);

        textPaint = new Paint();
        textPaint.setStrokeWidth(3);
        textPaint.setColor(0xffff0000);
        textPaint.setTextSize(30);
    }

    public void setGameService(IGameService gameService) {
        this.gameService = gameService;
    }

    public void setLinkInfo(LinkInfo linkInfo) {
        this.linkInfo = linkInfo;
    }

    public void setSelectedImage(Bitmap bm) {
        selectedImage = bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (gameService == null) {
            return;
        }
        Piece[][] pieces = gameService.getPieces();

        if (promptPieces != null) {
            drawSelectPiece(canvas, promptPieces.getPieceOne());
            drawSelectPiece(canvas, promptPieces.getPieceTwo());
            promptPieces = null;
        } else if (Piece.hasImage(selectedPiece)) {
            drawSelectPiece(canvas, selectedPiece);
        }

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
            invalidate();
        }
    }

    private void drawSelectPiece(Canvas canvas, Piece piece) {
        canvas.drawBitmap(selectedImage, piece.getBeginX(), piece.getBeginY(), null);
    }

    private void drawPiece(Canvas canvas, Piece piece) {
        canvas.drawBitmap(piece.getImage(), piece.getBeginX(), piece.getBeginY(), null);
    }

    private void drawLine(Canvas canvas, LinkInfo linkInfo) {
        List<Piece> linkPieces = linkInfo.getLinkPieces();
        for (int i = 0; i < linkPieces.size() - 1; i++) {
            Point currentPoint = linkPieces.get(i).getCenter();
            Point nextPoint = linkPieces.get(i + 1).getCenter();
            canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y, paint);
        }
        // drawScores(canvas, linkPieces);
    }

    // private void drawScores(Canvas canvas, List<Piece> linkPieces) {
    // drawText(canvas, linkPieces.get(0), "+" + GameSettings.CardScore);
    // drawText(canvas, linkPieces.get(linkPieces.size() - 1), "+" + GameSettings.CardScore);
    // for (int i = 1; i < linkPieces.size() - 1; i++) {
    // drawText(canvas, linkPieces.get(i), "+" + GameSettings.CornerScore);
    // }
    // }
    //
    // private void drawText(Canvas canvas, Piece piece, String text) {
    // canvas.drawText(text, piece.getCenter().x - 10, piece.getCenter().y - 10, textPaint);
    // }

    public void setSelectedPiece(Piece piece) {
        this.selectedPiece = piece;
    }

    public void setPromptPieces(PiecePair promptPieces) {
        this.promptPieces = promptPieces;
    }
}
