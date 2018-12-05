package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.acuevas.sudokus.controller.Manager;
import com.acuevas.sudokus.exceptions.CriticalException;
import com.acuevas.sudokus.model.Ranking;
import com.acuevas.sudokus.model.records.Records;
import com.acuevas.sudokus.model.records.Records.Record;
import com.acuevas.sudokus.model.sudokus.Sudokus;
import com.acuevas.sudokus.model.sudokus.Sudokus.Sudoku;
import com.acuevas.sudokus.model.users.Users;
import com.acuevas.sudokus.model.users.Users.User;
import com.acuevas.sudokus.utils.SudokuGenerator;

class Tests {

//Model
	private static Sudokus sudokus = new Sudokus();
	private static Users users = new Users();
	private static Records records = new Records();
//Test objects
	private static Sudokus.Sudoku sudoku1;
	private static Sudokus.Sudoku sudoku2;
	private static Sudokus.Sudoku sudoku3;
	private static Users.User user;

	/**
	 * Inits the fields before the tests
	 */
	@BeforeAll
	static void init() {
		sudoku1 = new Sudoku(7, "Medium (Level 7)",
				"5......4129.7.4..83...1.....4...2...6.2...5.9...8...3.....8...34..6.9.5283......7",
				"578263941291754368364918725743592186682341579159876234925487613417639852836125497");
		sudoku2 = new Sudoku(6, "Medium (Level 6)",
				".3.....6..518.723.2.7...8.4...754.......3.......692...3.6...5.2.852.371..4.....9.",
				"839425167451867239267319854623754981794138625518692473376981542985243716142576398");
		sudoku3 = new Sudoku(6, "Medium (Level 6)",
				"..94.83..1.4...85.....23.......719.58.......42.158.......69.....56...4.8..78.52..",
				"729458361134769852568123749643271985875936124291584637482697513956312478317845296");

		sudokus.getSudokus().add(sudoku1);
		sudokus.getSudokus().add(sudoku2);
		sudokus.getSudokus().add(sudoku3);

		user = new User("testname", "test", "1234");
		users.getUsers().add(user);
	}

	/**
	 * Before each test, resets the records
	 */
	@BeforeEach
	void reset() {
		records.getRecords().clear();
	}

	/**
	 * Checks if the sudoku given by getSudokusNotUsed from Manager works properly.
	 * assertFalse if the sudoku given is not the same as sudoku1, the one played by
	 * the user in any of the iterations.
	 */
	@Test
	void randomSudokuNotUsedByUser() {
		final int iterations = 15;

		records.getRecords().add(new Record(user.getUsername(), 60, sudoku1));

		for (int i = 0; i < iterations; i++) {
			Sudoku randSudokuNotUsed = Manager.getSudokusNotUsed(user, sudokus, records);
			assertFalse((randSudokuNotUsed.equals(sudoku1)));
		}
	}

	/**
	 * Checks if the getSudokusNotUsed method returns null when all the sudokus are
	 * completed.
	 */
	@Test
	void randomSudokuWhenAllDone() {
		final int iterations = 15;

		records.getRecords().add(new Record(user.getUsername(), 60, sudoku1));
		records.getRecords().add(new Record(user.getUsername(), 60, sudoku2));
		records.getRecords().add(new Record(user.getUsername(), 60, sudoku3));

		for (int i = 0; i < iterations; i++) {
			Sudoku randSudokuNotUsed = Manager.getSudokusNotUsed(user, sudokus, records);
			assertNull(randSudokuNotUsed);
		}
	}

	/**
	 * Checks if the username is already registered
	 */
	@Test
	void usernameRegistered() {
		String testUsername = "test";
		assertTrue(Manager.usernameInUse(testUsername, users));
	}

	/**
	 * Checks if the mean time is correct by randomly giving times and expecting a
	 * double for a number of X iterations
	 */
	@Test
	void getMeanTimeFromUser() {
		Random random = new Random();
		SudokuGenerator sudokuGenerator = SudokuGenerator.getInstance();
		final float time1 = random.nextInt(200); // made time variables float because if they were int the result of
													// dividing them is rounded to X.0
		final float time2 = random.nextInt(200);
		final float time3 = random.nextInt(200);
		final Double expectedMean = (double) ((time1 + time2 + time3) / 3);
		final int iterations = 15;

		records.getRecords().add(new Record(user.getUsername(), (int) time1, sudokuGenerator.getRandomSudoku()));
		records.getRecords().add(new Record(user.getUsername(), (int) time2, sudokuGenerator.getRandomSudoku()));
		records.getRecords().add(new Record(user.getUsername(), (int) time3, sudokuGenerator.getRandomSudoku()));

		for (int i = 0; i < iterations; i++) {
			int index = 2;
			Users.User user2 = new User("testname" + index, "test" + index++, "1234");
			records.getRecords()
					.add(new Record(user2.getUsername(), random.nextInt(200), sudokuGenerator.getRandomSudoku()));
			records.getRecords()
					.add(new Record(user2.getUsername(), random.nextInt(200), sudokuGenerator.getRandomSudoku()));
		}
		double meanTime = Ranking.getMeanTime(user, records);
		assertEquals(expectedMean, meanTime, 0.01);
	}

	/**
	 * Checks if the user can finish the sudoku properly.
	 */
	@Test
	void finishSudoku() {
		user.setPlayedSudoku(sudoku1);
		try {
			Manager.registerRecord(user, records);
		} catch (CriticalException e) {
			e.printStackTrace();
		}
		assertTrue(records.getRecords().stream().anyMatch(record -> record.hasUser(user)));
	}

	/**
	 * Checks if the user has played at least one Sudoku
	 */
	@Test
	void userHasPlayed() {
		records.getRecords().add(new Record(user.getUsername(), 20, SudokuGenerator.getInstance().getRandomSudoku()));
	}

}
