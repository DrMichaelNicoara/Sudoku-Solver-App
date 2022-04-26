package com.example.sudokusolver;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    private boolean solved;

    public void setSolved(boolean solved) {
        solver.setSelected_row(-1);
        solver.setSelected_col(-1);
        this.solved = solved;
    }

    private final int boardColor;
    private final int cellFillColor;
    private final int cellsHighlightColor;
    private final int letterColor;
    private final int letterColorSolve;

    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellsHighlightColorPaint = new Paint();

    private final Paint letterColorPaint = new Paint();
    private final Rect letterColorPaintBounds = new Rect();


    private final Solver solver = new Solver();
    public Solver getSolver() { return solver; }

    private int cellSize;

    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        solved = false;

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SudokuBoard, 0, 0);

        try{
            boardColor = typedArray.getInteger(R.styleable.SudokuBoard_boardColor, 0);
            cellFillColor = typedArray.getInteger(R.styleable.SudokuBoard_cellFillColor, 0);
            cellsHighlightColor = typedArray.getInteger(R.styleable.SudokuBoard_cellsHighlightColor, 0);
            letterColor = typedArray.getInteger(R.styleable.SudokuBoard_letterColor, 0);
            letterColorSolve = typedArray.getInteger(R.styleable.SudokuBoard_letterColorSolve, 0);
        }finally {
            typedArray.recycle();
        }
    }

    private void setPaintConfig(Paint paint, Paint.Style style, int strokeWidth, int color)
    {
        paint.setStyle(style);
        paint.setColor(color);

        if(style == Paint.Style.STROKE)
            paint.setStrokeWidth(strokeWidth);
        else if(style == Paint.Style.FILL)
            paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        cellSize = dimension/9;

        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        setPaintConfig(boardColorPaint, Paint.Style.STROKE, 16, boardColor);
        setPaintConfig(cellFillColorPaint, Paint.Style.FILL, 0, cellFillColor);
        setPaintConfig(cellsHighlightColorPaint, Paint.Style.FILL, 0, cellsHighlightColor);
        setPaintConfig(letterColorPaint, Paint.Style.FILL, 0, letterColor);


        canvas.drawRect(0, 0, getWidth(), getHeight(), boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
        colorCell(canvas, solver.getSelected_row(), solver.getSelected_col());
    }

    private void drawBoard(Canvas canvas)
    {
        int width;
        for(int i=0; i<10; i++)
        {
            // Draw thin lines, but every 3 lines draw a thick line
            if(i%3 == 0)
                width = 10;
            else width = 4;

            setPaintConfig(boardColorPaint, Paint.Style.STROKE, width, boardColor);
            canvas.drawLine(cellSize*i, 0, cellSize*i, getWidth(), boardColorPaint);
            canvas.drawLine(0, cellSize*i, getWidth(), cellSize*i, boardColorPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(solved) return false;

        float x = event.getX();
        float y = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            solver.setSelected_row((int) Math.ceil(y/cellSize));
            solver.setSelected_col((int) Math.ceil(x/cellSize));
            return true;
        }
        else return false;
    }

    private void colorCell(Canvas canvas, int row, int col)
    {
        if(solver.getSelected_col() != -1 && solver.getSelected_row() != -1)
        {
            canvas.drawRect((col-1)*cellSize, 0, col*cellSize, cellSize*9, cellsHighlightColorPaint);
            canvas.drawRect(0, (row-1)*cellSize, cellSize*9, row*cellSize, cellsHighlightColorPaint);
            canvas.drawRect((col-1)*cellSize, (row-1)*cellSize, col*cellSize, row*cellSize, cellsHighlightColorPaint);
        }

        invalidate();
    }

    private void drawNumbers(Canvas canvas)
    {
        letterColorPaint.setTextSize(cellSize);
        for(int row=0; row<9; row++)
        {
            for(int col=0; col<9; col++)
            {
                if(solver.getBoard()[row][col] != 0)
                {
                    drawLetter(canvas, row, col);
                }
            }
        }

        letterColorPaint.setColor(letterColorSolve);
        for(ArrayList<Object> letter : solver.getEmptyBoxIndex())
        {
            int row = (int) letter.get(0);
            int col = (int) letter.get(1);

            drawLetter(canvas, row, col);
        }
    }

    private void drawLetter(Canvas canvas, int row, int col)
    {
        String text = Integer.toString(solver.getBoard()[row][col]);

        float width, height;
        width = letterColorPaint.measureText(text);
        letterColorPaint.getTextBounds(text, 0, text.length(), letterColorPaintBounds);
        height = letterColorPaintBounds.height();

        canvas.drawText(text, col*cellSize + (cellSize - width)/2, row*cellSize+cellSize - (cellSize - height)/2, letterColorPaint);
    }
}
