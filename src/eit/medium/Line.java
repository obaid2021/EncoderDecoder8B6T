package eit.medium;

import java.util.NoSuchElementException;

/**
 * This interface defines the methods transmit(), receive(), and hasData().
 * 
 * @author Muhammad Obaid Ullah
 *
 * @param <E> data.
 */

public interface Line<E> {

	/**
	 * This function defines the transmit method that will be implemented in
	 * <p>
	 * cable class.
	 * @param element input of the user.
	 * @return boolean
	 */
	public boolean transmit(E element);

	/**
	 * This function defines the receive method of cable class.
	 * 
	 * @return decoded value will be returened.
	 * @throws NoSuchElementException exception
	 */
	public E receive() throws NoSuchElementException;

	/**
	 * This function checks if there are values in the cable or not.
	 * 
	 * @return boolean. returns false if the cable is empty.
	 */
	public boolean hasData();

}
