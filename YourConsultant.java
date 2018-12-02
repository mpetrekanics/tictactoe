package com.topdesk.cases.tictactoe.yoursolution;

import com.topdesk.cases.tictactoe.CellLocation;
import com.topdesk.cases.tictactoe.CellState;
import com.topdesk.cases.tictactoe.Consultant;
import com.topdesk.cases.tictactoe.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YourConsultant implements Consultant {

	// Set up the directions and their cell locations
	// It is useful to handle those cells together that
	// form a 3-unit line, for example if we want to check
	// if a player has won the game or is about to win the game
	
	// the lines 
	final static int[][] threeCellsInALine = { { 0, 1, 2 },{ 3, 4, 5 },{ 6, 7, 8 },
		{ 0, 3, 6 },{ 1, 4, 7 },{ 2, 5, 8 },{ 0, 4, 8 },{ 2, 4, 6 }};
	
	// an integer cannot be null just 0. I need null therefore I use this as a 
	// substitute:
	final static int nullvalue = 10;
	
	// it's easier to use an array to keep a gameboard
	// this gameboard (e:empty):
	// eOX
	// eeX
	// Oee
	// will be represented by this array:
	// {0, 2, 1, 0, 0, 1, 2, 0, 0}
		
	public int[] convertGameBoardToArray(GameBoard gameBoard){
		int[] game = new int[9];
		for (int i = 0; i < 9; i++){
			if (gameBoard.getCellState(id(i)).toString().equals("EMPTY")){
				game[i] = 0;
			} else if 
			(gameBoard.getCellState(id(i)).toString().equals("OCCUPIED_BY_X")){
				game[i] = 1;
			} else {game[i] = 2;}		 
		}
		return game;
	}
	
	// I use ids instead of CellLocations
	
	public CellLocation id(int nmbr){
				if (nmbr == 0){return CellLocation.TOP_LEFT;}
				else if (nmbr == 1){return CellLocation.TOP_CENTRE;}
				else if (nmbr == 2){return CellLocation.TOP_RIGHT;}
				else if (nmbr == 3){return CellLocation.CENTRE_LEFT;}
				else if (nmbr == 4){return CellLocation.CENTRE_CENTRE;}
				else if (nmbr == 5){return CellLocation.CENTRE_RIGHT;}
				else if (nmbr == 6){return CellLocation.BOTTOM_LEFT;}
				else if (nmbr == 7){return CellLocation.BOTTOM_CENTRE;}
				else {return CellLocation.BOTTOM_RIGHT;}
			}
			
	// get all the ids of the empty fields, for example if there's a gameboard 
	// where the centre row is occupied then the result is:
	// {0, 1, 2, 6, 7, 8, nullvalue, nullvalue, nullvalue}
	// nullvalue is 10 actually but it doesn't matter
	public int[] getAllEmpties(int[] game){
				int[] anArr; 
				anArr = new int[] {nullvalue, nullvalue, nullvalue, nullvalue, 
						nullvalue, nullvalue, nullvalue, nullvalue, nullvalue};
				int actual = 0;
				for (int i = 0; i < 9; i++){
					if (game[i] == 0) {
						anArr[actual] = i;
						actual++;
					}
				}
				return anArr;
			}
			
	public int howManyEmpties(int[] anArr){
				int n = 0;
				for (int j = 0; j < 9; j++){
					if (anArr[j] != nullvalue){
						n++;
						}
		    	}
				return n;
			}	

	//who am I? Returns true when I am with X. Returns false when I control O. 
	public boolean amIX(int[] game){
		int howManyX = 0;
		int howManyO = 0;
		for (int i = 0; i < 9; i++){
			if (game[i] == 1) {
				howManyX++;
			}else if (game[i] == 2)
			{howManyO++;}
		}
		if (howManyX == howManyO) {
			return true;
		}else if (howManyX - 1 == howManyO) {
			return false;
		} else {
			throw new IllegalStateException();
		}
	}

	// decide if a situation is ALREADY a winner
	// forMe == true : from my point of view
	// forMe == false : from my opponent's point of view
	public boolean isWinnerForPlayer(int[] game, boolean amix, boolean forMe){
		// go through the 8 possible line (3 columns, 3 rows and 2 diagonals)
		for (int line = 0; line < 8; line++) {
			if ((amix == forMe) && 
				// and there are 3 Xs in a line:	
				(game[threeCellsInALine[line][0]] == 1)
				&& (game[threeCellsInALine[line][1]] == 1)
				&& (game[threeCellsInALine[line][2]] == 1)) {
			return true;
		} else if ((amix != forMe) && 
				// and there are 3 Os in a line:
				(game[threeCellsInALine[line][0]] == 2)
				&& (game[threeCellsInALine[line][1]] == 2)
				&& (game[threeCellsInALine[line][2]] == 2)) {
			return true;
		}} 
		return false;
	}

	// create a new game table
	// from an existing game (game)
	// put a new O or X to a location (where)
	// forMe is true if it's my move, false if it's my opponent's
	public int[] makeAMove (int[] game, int where, boolean amix, boolean forMe){
		if (amix == forMe) {
			game[where] = 1;
		} else {game[where] = 2;}
		return game;
	}
	
	// revert a Move. Convert a specific cell into an empty cell. 
    public int[] revertThisMove (int[] game, int whichMove){
        game[whichMove] = 0;
        return game;
    }
	
	// a situation is winning if:
	// - it is already a winner one (e.g. you have three Xs in a line)
	// - all the possible moves for your opponent is a loser move for him
	// to decide the latter, the thatMoveWon method calls another method:
    // thatMoveLose
    // which has the following logic behind:
    // a situation is losing if:
    // - it's NOT losing if it is already a winner situation
    // - let's consider all the possible move for your opponent; if there is a winner
    // move among them (one is enough) then the situation is losing. To decide  
    // whether a move is winner or not, the thatMoveLose calls the thatMoveWon. 
    
    // So the two methods call each other. 
        
    public boolean thatMoveWon (int[] game, boolean amix, boolean forMe){
		if (isWinnerForPlayer(game, amix, forMe)){
			return true;
		}
		int[] empties = getAllEmpties(game);
		int currentOptionsCount = howManyEmpties(empties);
		if (currentOptionsCount > 0){
			for (int i = 0; i < currentOptionsCount; i++){
				if (!thatMoveLose(makeAMove(game, empties[i], 
						amix, !forMe), amix, !forMe)) {
					game = revertThisMove(game, empties[i]);
					return false;}
				game = revertThisMove(game, empties[i]);
			}
			return true;
		}
		return false;
	}	

	public boolean thatMoveLose (int[] game, boolean amix, boolean forMe){
		if (isWinnerForPlayer(game, amix, forMe)){
			return false;
		}
		int[] empties = getAllEmpties(game);
		int currentOptionsCount = howManyEmpties(empties);
		if (currentOptionsCount > 0){
			for (int i = 0; i < currentOptionsCount; i++){
				if (thatMoveWon(makeAMove(game, empties[i], 
						amix, !forMe), amix, !forMe)){
					game = revertThisMove(game, empties[i]);
					return true;}
				game = revertThisMove(game, empties[i]);
			}
		}
		return false;
	}
	
	public CellLocation suggest(GameBoard gameBoard) {
		
		CellLocation mySuggestedMoveLocation = null;
		
		int[] game = convertGameBoardToArray(gameBoard);
		
		boolean amix = amIX(game);
		
		int[] empties = getAllEmpties(game);
		
		int currentOptionsCount = howManyEmpties(empties);
		
		// if the board is already full, then illegalState		
		if (currentOptionsCount == 0){
			throw new IllegalStateException();
		}
		
		// if someone has already won: illegalStateException
		
		// I have won already
		if (isWinnerForPlayer(game, amix, true)){
			throw new IllegalStateException();
		}
		
		// My opponent has won already
		if (isWinnerForPlayer(game, amix, false)){
			throw new IllegalStateException();
		}
		
		///////
		
		// if there's a winner move, recommend the first winner move
		for (int i = 0; i < currentOptionsCount; i++) {
			if (thatMoveWon(makeAMove(game, empties[i], amix, true), amix, true)){
				mySuggestedMoveLocation = id(empties[i]);
				return mySuggestedMoveLocation;
			}
			game = revertThisMove(game, empties[i]);
		}

		// There is no winner move. We have to go for the draw. The first 
		// move which doesn't lead to losing will be good for us. 
		for (int i = 0; i < currentOptionsCount; i++) {
			if (!thatMoveLose(makeAMove(game, empties[i], amix, true), amix, true)){
				mySuggestedMoveLocation = id(empties[i]);
				return mySuggestedMoveLocation;
			}
			game = revertThisMove(game, empties[i]);
		}
		
		// There is no chance for us, not even for a draw. Recommend the first 
		// possible move then. 
		mySuggestedMoveLocation = id(empties[0]);
		return mySuggestedMoveLocation;
		}
	}
		
