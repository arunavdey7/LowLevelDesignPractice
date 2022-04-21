package com.company;



// Requirements:

// 1. Player can choose their Symbol.
// 2. Player should be able to see when their turn is.
// 3. Player can see the available cells he can put his/her Symbol into.
// 4. Player can put their symbol in the desired location on the board.
// 5. Player can see who is the winner.
// 6. System/Driver can check for rows, columns and two diagonals.

// Actors:
// 1. Player
// 2. System/Driver


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Symbol
{
    CROSS, CIRCLE;
}
enum GameState
{
    PLAYER_1_WON, PLAYER_2_WON, DRAW, ONGOING;
}
class Player
{
    private int playerId;
    private boolean isCurrentPlayer;
    private Symbol chosenSymbol;

    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }
    public void setCurrentPlayer(boolean currentPlayer) {
        isCurrentPlayer = currentPlayer;
    }
    public Symbol getChosenSymbol() {
        return chosenSymbol;
    }
    public void setChosenSymbol(Symbol chosenSymbol) {
        this.chosenSymbol = chosenSymbol;
    }
    public Player(int playerId, boolean isCurrentPlayer, Symbol chosenSymbol)
    {
        this.playerId = playerId;
        this.isCurrentPlayer = isCurrentPlayer;
        this.chosenSymbol = chosenSymbol;
    }
    public String putSymbolAt(Position position, Board board)
    {
        Position[][] positions = board.getCurrentBoardStatus();
        if(positions[position.getRow()][position.getCol()].isOccupied() == false)
        {
            positions[position.getRow()][position.getCol()].setOccupied(true);
            positions[position.getRow()][position.getCol()].setSymbol(this.getChosenSymbol());
            return "Move successfully done";
        }
        return "Move not successful as the entered position is already taken";
    }
}
class Position
{
    private int row;
    private int col;
    private boolean isOccupied;
    private Symbol symbol;

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public boolean isOccupied() {
        return isOccupied;
    }
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public Position(int row, int col, boolean isOccupied, Symbol symbol) {
        this.row = row;
        this.col = col;
        this.isOccupied = isOccupied;
        this.symbol = symbol;
    }

}
class Board
{
    private Position arr[][] = new Position[3][3];

    Position[][] getCurrentBoardStatus() {
        return this.arr;
    }
    List<Position> getAvailablePositions() {
        List<Position> availablePosns = new ArrayList<>();
        for(int i = 0 ; i < 3; i++)
        {
            for(int j = 0 ; j < 3; j++)
            {
                if(arr[i][j].isOccupied() == false)
                {
                    availablePosns.add(arr[i][j]);
                }
            }
        }
        return availablePosns;
    }
}
public class Driver
{
    static Player  player1, player2;
    static Board board = new Board();
    public static void main(String args[])
    {
        Scanner ob = new Scanner(System.in);
        System.out.println("Choose symbol for player 1 [Type X for CROSS and O for CIRCLE]");
        String symbol = ob.nextLine();
        if(symbol.equalsIgnoreCase("X"))
        {
            player1 = new Player(1,true,Symbol.CROSS);
            player2 = new Player(2,false,Symbol.CIRCLE);
        }
        else if(symbol.equalsIgnoreCase("O"))
        {
            player1 = new Player(1,true,Symbol.CIRCLE);
            player2 = new Player(2,false,Symbol.CROSS);
        }
        Driver driver = new Driver();
        board = driver.initializeGame();
        Position positions[][] = board.getCurrentBoardStatus();
        while(driver.getGameState(board) == GameState.ONGOING)
        {
            Player selectedPlayer = player1.isCurrentPlayer() ? player1:player2;
            System.out.println("Player "+selectedPlayer.getPlayerId()+" chance: ("+selectedPlayer.getChosenSymbol()+")");
            System.out.println("Choose positions from the available positions");
            List<Position> availablePositions= board.getAvailablePositions();
            for(Position iter:availablePositions)
            {
                if(iter.isOccupied() == false)
                    System.out.println(iter.getRow()+","+iter.getCol());
            }
            String input[] = ob.nextLine().split(",");
            int row,col;
            row = Integer.parseInt(input[0]);
            col = Integer.parseInt(input[1]);
            Position positionObj = positions[row][col];
            positionObj.setRow(row);
            positionObj.setCol(col);
            positionObj.setOccupied(true);
            positionObj.setSymbol(selectedPlayer.getChosenSymbol());
            selectedPlayer.putSymbolAt(positionObj,board);
            System.out.println("Move successfully done.");
            if(selectedPlayer.equals(player1))
            {
                player1.setCurrentPlayer(false);
                player2.setCurrentPlayer(true);
            }
            else if(selectedPlayer.equals(player2))
            {
                player2.setCurrentPlayer(false);
                player1.setCurrentPlayer(true);
            }
            driver.displayBoard();
            positions = board.getCurrentBoardStatus();
            System.out.println("GAME STATUS : "+driver.getGameState(board));
        }
    }
    public void displayBoard()
    {
        Position position[][] = board.getCurrentBoardStatus();
        for(int i = 0 ; i < 3; i++)
        {
            for(int j = 0 ; j < 3; j++)
            {
                if(position[i][j].getSymbol() == Symbol.CROSS)
                    System.out.print("X ");
                else if(position[i][j].getSymbol() == Symbol.CIRCLE)
                    System.out.print("O ");
                else
                    System.out.print("__ ");
            }
            System.out.println();
        }
    }
    public Board initializeGame()
    {
        Position position[][] = Driver.board.getCurrentBoardStatus();
        for(int i = 0 ; i < 3; i++)
        {
            for(int j = 0 ; j < 3; j++)
            {
                position[i][j] = new Position(i,j,false,null);
            }
        }
        return board;
    }
    public GameState getGameState(Board board)
    {
        Symbol winnerSymbol = null;
        Position positions[][] = board.getCurrentBoardStatus();
        // Seriously?, use a f'in loop, duh
        // Checking rows:
        if((positions[0][0].getSymbol() == positions[0][1].getSymbol()) && (positions[0][1].getSymbol() == positions[0][2].getSymbol()))
        {
            winnerSymbol = positions[0][0].getSymbol();
        }
        else if((positions[1][0].getSymbol() == positions[1][1].getSymbol()) && (positions[1][1].getSymbol() == positions[1][2].getSymbol()))
        {
            winnerSymbol = positions[1][0].getSymbol();
        }
        else if((positions[2][0].getSymbol() == positions[2][1].getSymbol()) && (positions[2][1].getSymbol() == positions[2][2].getSymbol()))
        {
            winnerSymbol = positions[2][0].getSymbol();
        }

        //Checking columns:
        else if((positions[0][0].getSymbol() == positions[1][0].getSymbol()) && (positions[1][0].getSymbol() == positions[2][0].getSymbol()))
        {
            winnerSymbol = positions[0][0].getSymbol();
        }
        else if((positions[0][1].getSymbol() == positions[1][1].getSymbol()) && (positions[1][1].getSymbol() == positions[2][1].getSymbol()))
        {
            winnerSymbol = positions[0][1].getSymbol();
        }
        else if((positions[0][2].getSymbol() == positions[1][2].getSymbol()) && (positions[1][2].getSymbol() == positions[2][2].getSymbol()))
        {
            winnerSymbol = positions[0][2].getSymbol();
        }

        //Checking Positive Diagonal:
        else if((positions[0][0].getSymbol() == positions[1][1].getSymbol()) && (positions[1][1].getSymbol() == positions[2][2].getSymbol()))
        {
            winnerSymbol = positions[0][0].getSymbol();
        }
        //Checking Negative Diagonal:
        else if((positions[0][2].getSymbol() == positions[1][1].getSymbol()) && (positions[1][1].getSymbol() == positions[2][0].getSymbol()))
        {
            winnerSymbol = positions[0][2].getSymbol();
        }
        if(winnerSymbol != null)
        {
            if(winnerSymbol == player1.getChosenSymbol())
                return GameState.PLAYER_1_WON;
            if(winnerSymbol == player2.getChosenSymbol())
                return GameState.PLAYER_2_WON;
        }
        // Check for empty positions:
        for(int i = 0 ; i < 3; i++)
        {
            for(int j = 0 ; j < 3; j++)
            {
                if(positions[i][j].isOccupied() == false)
                    return GameState.ONGOING;
            }
        }
        return GameState.DRAW;

    }
}



// 00 01 02
// 10 11 12
// 20 21 22

