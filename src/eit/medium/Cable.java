package eit.medium;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Cable class implements the functions of a cable.
 * <p>
 * Data can be put in the cable in a queue by using transmit() method and
 * <p>
 * can be retrieved from the queue by using receive() method. 
 * @author Muhammad Obaid Ullah
 *
 * @param <E> is element of class Cable.
 */

public class Cable<E> implements Line<E> {

	public static final int DATA_STREAMS = 3;
	public String[] eingabe;
	public File input2;
	public static ArrayList<String[]> stringArray = new ArrayList<String[]>(); // Queue in the cable

	/**
	 * This function puts the input value into a queue in the Cable.
	 * <p>
	 * The data is already encoded in string array before putting in cable.
	 * @param element input of the user.
	 * @return boolean returns true if the data is transmitted .
	 */
	public boolean transmit(E element) {
		try {
			stringArray.add((String[]) element); // adds the input into queue
		} catch (Exception e) {
			throw new NoSuchElementException();
		}
		return true;
	}

	/**
	 * This function retrieves the data from the cable and returns it as a String array.
	 * 
	 * @return decoded value in the form of string array will be returned.
	 * @throws NoSuchElementException exception
	 */
	@SuppressWarnings("unchecked")
	@Override

	public E receive() throws NoSuchElementException {

		Iterator<String[]> it = stringArray.iterator();

		String[] code = new String[DATA_STREAMS];
		try {  //searches for the next available data in the queue and stores it in code
			code = it.next();
			it.remove();
			return (E) code; // data is returned
		} catch (Exception e) {
			throw new NoSuchElementException(); // exception is thrown if data is not found.
		}

	}

	/**
	 * This function checks if there is data in the cable or not.
	 * 
	 * @return boolean returns false if the cable is empty , otherwise true.
	 */
	@Override
	public boolean hasData() {

		boolean hasData = true;
		try {
			if (stringArray.isEmpty()) { // if the queue is empty , false will be returned

				hasData = false;

			}
		} catch (Exception e) {
			throw new NoSuchElementException();
		}
		return hasData;

	}

}