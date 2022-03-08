package eit.application;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import eit.application.exception.FileLengthException;
import eit.application.exception.FileNameException;
import eit.cli.Main;
import eit.linecode.DataDecoder8B6T;
import eit.linecode.DataEncoder8B6T;
import eit.medium.Cable;

/**
 * This class creates exactly one control frame and at least one data frame
 * based on the file to be transferred,
 * <p>
 * triggered by the transmitFile() method.
 * <p>
 * If the receiveFile() is triggered , this class collects the control frame and
 * <p>
 * dataframe from the cable and compiles it into file as an output.
 * 
 * @author Muhammad Obaid Ullah
 *
 */

public class EasyFileTransferProtocol {
	private static final int MAXIMUM_SIZE = 2048;
	private static final int DATA_STREAMS = 3;
	private static final long MAX_SIZE = 16777215;
	private static final int MIN_SIZE = 4;
	private static final int MAX_NAME_SIZE = 256;
	DataEncoder8B6T enc = new DataEncoder8B6T();
	Cable<String[]> transmitter = new Cable<String[]>();
	static DataDecoder8B6T decoder = new DataDecoder8B6T();

	/**
	 * This is a constructor that constructs the objects of DataEncoder8B6T,
	 * <p>
	 * DataDecoder8B6T and Cable class.
	 * 
	 * @param enc   Encoder class.
	 * @param dec   Decoder class.
	 * @param cable Cable class.
	 */

	public EasyFileTransferProtocol(DataEncoder8B6T enc, DataDecoder8B6T dec, Cable<String[]> cable) {

	}

	/**
	 * This function divides the file in data frame and control frame.
	 * <p>
	 * The control frame has the information about file name, file size and file
	 * name length.
	 * <p>
	 * The data frame has the information about the content of the file.
	 * <p>
	 * This function also throws IOException
	 * 
	 * @param file file that is to be transmitted.
	 */

	public void transmitFile(File file) {

		controlFrame(file); // file is sent to this method to get control frame
		try {
			dataFrame(file); // file is sent to this method to get data frame
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is called by Main class to receive the file from Cable class.
	 * <p>
	 * receiveFile() is the method to retrieve any existing data from the queue,
	 * <p>
	 * decode it and return it as a file again. First Control frame and them data
	 * frame
	 * 
	 * will be decoded by 8B6T and put in a byte array. At the end a file will be
	 * <p>
	 * created from those bytes and returned.
	 * 
	 * @return received file.
	 */

	public File receiveFile() {

		Cable<String[]> receiver = new Cable<String[]>();

		String[] datastreams = new String[DATA_STREAMS];
		byte[] asciiCode, bufferBytes = new byte[0];
		// Control Frame

		String read = new String();
		datastreams = receiver.receive(); // receiving control frame from cable
		asciiCode = decoder.decode(datastreams); // decoding frame into bytes
		read = Main.streamReader(asciiCode); // reading bytes as string

		datastreams = receiver.receive();
		asciiCode = decoder.decode(datastreams);
		read = Main.streamReader(asciiCode);
		int lenBytes = Integer.parseInt(read);

		datastreams = receiver.receive();
		asciiCode = decoder.decode(datastreams);
		read = Main.streamReader(asciiCode);

		// DataFrame
		File file = new File("rcvd-" + read); // new file with new name

		int fileParts = (lenBytes / MAXIMUM_SIZE); // estimation of file parts with sizes not more than 2048 bytes
		if (lenBytes % MAXIMUM_SIZE != 0) {
			fileParts = fileParts + 1;
		}

		for (int i = 0; i < fileParts; i++) {
			datastreams = receiver.receive(); // receiving data frame from cable with size 2048
			asciiCode = decoder.decode(datastreams); // decoding data frame into Bytes
			bufferBytes=ByteBuffer.allocate(bufferBytes.length+asciiCode.length).put(bufferBytes).put(asciiCode).array()
					;
			// joing bytes into single byte array
		}

		try {
			OutputStream os = new FileOutputStream(file); // creation of output file
			os.write(bufferBytes); // data insertion into file
			os.close(); // file closed
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file; // returns the complete received File
	}

	/**
	 * This function creates the controlFrame for the file which is made up of
	 * <p>
	 * Length of File name , length of file in bytes and name of file.
	 * <p>
	 * Control frame will be put in the queue after 8B6T encoding first.
	 * 
	 * @param file the file which should be transmit and received.
	 */

	public void controlFrame(File file) {

		String name = file.getName(); // name of file

		if (name.length() < MIN_SIZE) {// exception if file name is less than 4 bytes
			throw new FileNameException("File name is too short!!!");
		}
		if (file.length() > MAX_SIZE) {// exception if file size is bigger than 16777215 bytes
			throw new FileLengthException("File is too big!!!");
		}
		if (name.length() > MAX_NAME_SIZE) {// exception if file name is greater than 256 bytes
			throw new FileNameException("File name is too long!!!");
		}
		String[] input = new String[DATA_STREAMS];

		input[0] = Integer.toString(name.length()); // length of name in string
		input[1] = Long.toString(file.length()); // length of file
		input[2] = file.getName(); // name of file
		byte[] fnl = new byte[0]; /*
									 * space allocation for file name , file size and file name length
									 */
		byte[] fnlBytes = new byte[DATA_STREAMS];
		byte[] nameFile = new byte[MAX_NAME_SIZE];

		// bytes of strings will be determined here
		fnl = input[0].getBytes();
		fnlBytes = input[1].getBytes();
		nameFile = input[2].getBytes();

		// encoding of control frame
		transmitter.transmit(enc.encode(fnl));
		transmitter.transmit(enc.encode(fnlBytes));
		transmitter.transmit(enc.encode(nameFile));

	}

	/**
	 * This function creates the data frames of maximum 2048 bytes for the received
	 * file.
	 * <p>
	 * Each data frame will be put in queue of Cable one by one after 8B6T encoding.
	 * 
	 * @param file the file that will be transmitted.
	 * @throws IOException exception.
	 */

	public void dataFrame(File file) throws IOException {
		// data of the file will be stored in byte array
		byte[] aByteArray = Files.readAllBytes(file.toPath());

		int len = aByteArray.length;
		int fileParts = (len / MAXIMUM_SIZE);
		// number of parts based on maximum size 2048 rule will be determined
		if (len % MAXIMUM_SIZE != 0) {
			fileParts = fileParts + 1;
		}
		int count = 0;
		int i = 0;
		// file is split into 2048 bytes parts here
		for (i = 0; i < fileParts - 1; i++) {
			byte[] byte2DArray = new byte[MAXIMUM_SIZE];
			String[] datastream = new String[DATA_STREAMS];
			for (int j = 0; j < MAXIMUM_SIZE; j++) {
				byte2DArray[j] = aByteArray[count];
				count++;
			}
			// each part will be encoded
			datastream = enc.encode(byte2DArray);
			// encoded data streams will be transmitted in queue of cable
			transmitter.transmit(datastream);

		}
		int lastValues = len - ((fileParts - 1) * MAXIMUM_SIZE);
		// Last part which could be less 2048 will be transmitted here
		byte[] byte2DArray = new byte[lastValues];
		if (count != 0) {
			for (int j = 0; j < lastValues; j++) {

				byte2DArray[j] = aByteArray[count];
				count++;
			}
		}
		String[] datastream = new String[DATA_STREAMS];
		// encoding of last part of file
		datastream = enc.encode(byte2DArray);
		// transmitting last part
		transmitter.transmit(datastream);

	}

}