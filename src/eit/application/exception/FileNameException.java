package eit.application.exception;

/**
 * If file name is not right, this exception will be thrown.
 * <p>
 * It could be because of file name is too long or too short.
 * 
 * @author Muhammad Obaid Ullah
 *
 */

@SuppressWarnings("serial")

public class FileNameException extends RuntimeException {

	/**
	 * It receives a message and adds it in FileNameException.
	 * 
	 * @param message to be displayed
	 */
	public FileNameException(final String message) {
		super(message);
	}
}
