package com.example.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SudokuBoard gameBoard;
    private Solver gameBoardSolver;

    private Button solveBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        solveBTN = findViewById(R.id.solveBTN);

        gameBoard = findViewById(R.id.SudokuBoard);
        gameBoardSolver = gameBoard.getSolver();
    }

    public void BTNPress(View view)
    {
        Button btn = findViewById(view.getId());
        gameBoardSolver.setNumberPos(Integer.parseInt(btn.getText().toString()));
        gameBoard.invalidate();
    }

    public void solve(View view)
    {
        if(solveBTN.getText().toString().equals(getString(R.string.solve)))
        {
            solveBTN.setText(R.string.clear);
            gameBoard.setSolved(true);

            gameBoardSolver.getEmptyBoxIndexes();
            SolveBoardThread solveBoardThread = new SolveBoardThread();
            new Thread(solveBoardThread).start();
            gameBoard.invalidate();
        }
        else {
            solveBTN.setText(R.string.solve);

            gameBoardSolver.resetBoard();
            gameBoard.setSolved(false);
            gameBoard.invalidate();
        }
    }

    class SolveBoardThread implements Runnable{
        @Override
        public void run()
        {
            gameBoardSolver.solve(gameBoard);
        }
    }
}