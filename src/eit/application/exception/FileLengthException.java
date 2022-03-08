package eit.application.exception;

/**
 * If file length is not right, this exception will be thrown.
 * <p>
 * It could be because of file is too big.
 * 
 * @author Muhammad Obaid Ullah
 *
 */

@SuppressWarnings("serial")

public class FileLengthException extends RuntimeException {

	/**
	 * It receives a message and adds it in FileLengthException.
	 * 
	 * @param message to be displayed
	 */

	public FileLengthException(final String message) {
		super(message);
	}
}
