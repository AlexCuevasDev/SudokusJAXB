package com.acuevas.sudokus.persistance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.acuevas.sudokus.exceptions.CriticalException;
import com.acuevas.sudokus.exceptions.CriticalException.StructErrors;
import com.acuevas.sudokus.model.sudokus.Sudokus;

public class SudokusDAO {

	private static SudokusDAO reader;
	private Sudokus sudokus = new Sudokus();

	private SudokusDAO() {

	}

	/**
	 * Gets the instance of this class, if none exists then it creates one.
	 * 
	 * @return SudokusDAO
	 */
	public static SudokusDAO getInstance() {
		if (reader == null)
			reader = new SudokusDAO();
		return reader;
	}

	/**
	 * Reads and implements the Sudoku from a plain text file with the format 
	 * // @formatter:off
	 * % (level) (description) 
	 * (uncompleted sudoku)
	 * (completed sudoku)
	 * // @formatter:on
	 * 
	 * @param file the File from where to read the sudokus
	 * @return Sudokus, an object with a List of Sudoku
	 * @throws CriticalException Exceptions that cannot permit the program to continue
	 * @see CriticalException
	 */
	public Sudokus readSudokusTXT(File file) throws CriticalException {
		// TODO MAYBE ERASE THE REPLACE(" ")?
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			String line;
			Integer level;
			String description;
			String uncompletedSudoku;
			String completedSudoku;

			while ((line = bufferedReader.readLine()) != null) {
				if (line.substring(0, 1).equals("%")) {
					line = line.replace(" ", "");
					try {
						level = Integer.parseInt(line.substring(1, 2));
						description = line.substring(2, line.length());
						uncompletedSudoku = bufferedReader.readLine();
						completedSudoku = bufferedReader.readLine();
						if (uncompletedSudoku.length() != 80 + 1 | completedSudoku.length() != 80 + 1) {
							// 80+1 because length starts counting on 1 instead of 0.
							throw new CriticalException(StructErrors.SUDOKUES_NOT_CORRECT);
						}
					} catch (CriticalException ex) {
						throw ex;
					} catch (Exception e) {
						// catch with generic Exception because no matter why if something goes wrong
						// here we cannot continue and the struct is bad.
						throw new CriticalException(StructErrors.PERSISTANCE_STRUCTURE);
					}
					Sudokus.Sudoku sudoku = new Sudokus.Sudoku(level, description, uncompletedSudoku, completedSudoku);
					sudokus.getSudokus().add(sudoku);
				}
			}
		} catch (IOException e) {
			// TODO CHANGE THIS INTO A VIEW CLASS, IF I CHANGE THE CONSOLE TO A GUI IT MUST
			// STILL WORK CHANGING ONLY VIEW CLASS
			System.err.println(e.getMessage());
		} catch (CriticalException e) {
			throw e;
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return sudokus;
	}

	public void writeIntoXML(Object JAXBElement, File file) throws CriticalException {
		Marshaller marshaller;
		try {
			if ((marshaller = getMarshallerFromObj(JAXBElement)) != null) {
				try {
					marshaller.marshal(JAXBElement, file);
				} catch (JAXBException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (CriticalException e) {
			file.delete();
			e.printStackTrace();
			throw new CriticalException(StructErrors.CRITICAL_FAILURE);
		}
	}

	/**
	 * Reads an XML and transforms it into classes using JAXB tech.
	 * 
	 * @param JAXBElement An object of the class you want to insert the data into.
	 * @param file        The file to read data from
	 * @return T Instance of the Object<T> with all the data.
	 * @throws CriticalException
	 */
	@SuppressWarnings("unchecked")
	public <T> T readFromXML(Object JAXBElement, File file) throws CriticalException {
		Unmarshaller unmarshaller;
		try {
			if (!file.exists())
				throw new CriticalException(StructErrors.FILE_NOT_FOUND); // Had to throw manually, couldn't implement
																			// the
			// IOException catch clause
			if ((unmarshaller = getUnmarshallerFromObj(JAXBElement)) != null) {
				return (T) unmarshaller.unmarshal(file);
			}
		} catch (CriticalException | JAXBException e) {
			throw new CriticalException(StructErrors.CRITICAL_FAILURE);
		}
		return null;
		// TODO THROWS 2 FILE NOT FOUND EXCEPTIONS, ASK MAR
	}

	private Marshaller getMarshallerFromObj(Object object) throws CriticalException {
		try {
			return getMarshaller(getContext(object));
		} catch (JAXBException e) {
			throw new CriticalException(StructErrors.MARSHALLER_ERROR); // TODO GET N� OF THE LINE FROM CODE TO SHOW ON
			// EXCEPTION
		}
	}

	private Unmarshaller getUnmarshallerFromObj(Object object) throws CriticalException {
		try {
			return getUnmarshaller(getContext(object));
		} catch (JAXBException e) {
			throw new CriticalException(StructErrors.UNMARSHALLER_ERROR);
		}
	}

	private JAXBContext getContext(Object object) throws CriticalException {
		try {
			return JAXBContext.newInstance(object.getClass());
		} catch (JAXBException e) {
			throw new CriticalException(StructErrors.GETTING_CONTEXT_ERROR);
		}
	}

	private Unmarshaller getUnmarshaller(JAXBContext context) throws JAXBException {
		return context.createUnmarshaller();
	}

	private Marshaller getMarshaller(JAXBContext context) throws JAXBException {
		return context.createMarshaller();
	}
}
