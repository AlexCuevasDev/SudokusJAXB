package com.acuevas.sudokus.userInteraction;

import java.util.List;

import com.acuevas.sudokus.model.Ranking;
import com.acuevas.sudokus.model.sudokus.Sudokus.Sudoku;

/**
 * This class manages all the output the program shows the user
 * 
 * @author Alex
 *
 */
public abstract class UserInteraction {
	/**
	 * Different messages used along the program
	 * 
	 * @author Alex
	 *
	 */
	public enum Messages {
		ASK_TIME("How long did it take to complete the sudoku?"), ASK_USERNAME("Please, insert your username"),
		ASK_PASSWORD("Please, insert your password"), ASK_NAME("Please, insert your name"), AGAIN("again"),
		NEW_PASWORD("Insert your new password"), PSWRD_CHANGED("Password changed succesfully"),
		INCORRECT_PASSWORD("Incorrect password"), FINISH_SUDOKU("Did you finish the sudoku?"),
		CHOOSE_OPTION("Please, choose an option"), GOODBYE("Goodbye!"), WRONG_KEY("You pressed a wrong key"),
		MEAN_TIME("Your mean time is: ");

		private String message;

		private Messages(String message) {
			this.message = message;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return message;
		}

	};

//the two menus of the program
	public final static String menu1 = " 1. Log in \n 2. Register \n 3. Exit";
	public final static String menu2 = "1. Play sudoku \n 2. Register a record(finish a sudoku) \n 3. Change password \n 4. See my mean time \n 5. Rankings \n 6. Log out";

	/**
	 * Prints an error on console in colour red
	 * 
	 * @param error String with the error
	 */
	public static void printError(String error) {
		System.err.println(error);
	}

	/**
	 * Prints the specified message on console, if nextLine uses println to jump
	 * line else stays on the same line
	 *
	 * @param message  a Message enum
	 * @param nextLine whether you want to jump to the next line or not
	 */
	public static void printMessage(Messages message, boolean nextLine) {
		if (nextLine)
			System.out.println(message + ".");
		else
			System.out.print(message + " ");
	}

	/**
	 * Prints the menu
	 * 
	 * @param menu String
	 */
	public static void printMenu(String menu) {
		System.out.println(menu);
	}

	/**
	 * Prints a String
	 * 
	 * @param string String
	 */
	public static void printString(String string) {
		System.out.println(string);
	}

	/**
	 * Prints all the rankings
	 * 
	 * @param sortedRankings List<Ranking> with the rankings sorted
	 */
	public static void printRankings(List<Ranking> sortedRankings) {
		sortedRankings.forEach(System.out::println);

	}

	/**
	 * Prints a Sudoku
	 * 
	 * @param playedSudoku a Sudoku
	 */
	public static void printSudoku(Sudoku playedSudoku) {
		System.out.println(playedSudoku);
	}

}
