package com.example.sudokusolver;

import java.util.ArrayList;

public class Solver {
    private int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;

    private int selected_row;
    private int selected_col;

    public int getSelected_row() { return selected_row; }

    public void setSelected_row(int selected_row) {
        this.selected_row = selected_row;
    }

    public int getSelected_col() {
        return selected_col;
    }

    public void setSelected_col(int selected_col) {
        this.selected_col = selected_col;
    }

    Solver()
    {
        selected_row = -1;
        selected_col = -1;

        board = new int[9][9];

        for(int row=0; row<9; row++) {
            for (int col = 0; col < 9; col++) {
                board[row][col] = 0;
            }
        }

        emptyBoxIndex = new ArrayList<>();
    }

    public void getEmptyBoxIndexes()
    {
        for(int row=0; row<9; row++) {
            for (int col = 0; col < 9; col++) {
                if(board[row][col] == 0)
                {
                    this.emptyBoxIndex.add(new ArrayList<>());
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(row);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(col);
                }
            }
        }
    }

    public void setNumberPos(int num)
    {
        if(this.selected_row != -1 && this.selected_col != -1)
        {
            if(this.board[this.selected_row-1][this.selected_col-1] == num)
            {
                this.board[this.selected_row-1][this.selected_col-1] = 0;
            }
            else this.board[this.selected_row-1][this.selected_col-1] = num;

            if(!check(this.selected_row-1, this.selected_col-1))
            {
                this.board[this.selected_row-1][this.selected_col-1] = 0;
            }
        }
    }

    public int[][] getBoard() { return this.board; }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndex() { return this.emptyBoxIndex; }

    private boolean check(int row, int col)
    {
        if(this.board[row][col] > 0)
        {
            for(int i=0; i<9; i++)
            {
                if(this.board[i][col] == this.board[row][col] && row != i)
                    return false;
                if(this.board[row][i] == this.board[row][col] && col != i)
                    return false;
            }

            int boxRow = row/3;
            int boxCol = col/3;

            for(int i=boxRow*3; i<boxRow*3 + 3; i++)
                for(int j=boxCol*3; j<boxCol*3 + 3; j++)
                    if(this.board[i][j] == this.board[row][col] && row != i && col != j) {
                        return false;
                    }

            return true;
        } else return false;
    }

    public boolean solve(SudokuBoard display)
    {
        int row = -1;
        int col = -1;
        boolean found = false;
        for(int i=0; i<9 && !found; i++)
        {
            for(int j = 0; j<9; j++)
            {
                if(this.board[i][j] == 0)
                {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
        }

        if(!found) return true;

        for(int i=0; i<=9; i++)
        {
            this.board[row][col] = i;
            display.invalidate();

            if(check(row, col))
            {
                if(solve(display))
                {
                    return true;
                }
            }

            this.board[row][col] = 0;
        }

        return false;
    }

    public void resetBoard()
    {
        for(int i=0; i<9; i++)
        {
            for(int j=0; j<9; j++)
            {
                this.board[i][j] = 0;
            }
        }

        this.emptyBoxIndex = new ArrayList<>();
    }
}
