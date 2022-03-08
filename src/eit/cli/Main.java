package eit.cli;

import eit.linecode.DataEncoder8B6T;
import eit.medium.Cable;

import eit.application.EasyFileTransferProtocol;
import eit.linecode.DataDecoder8B6T;

import java.io.File;

import java.io.IOException;

import edu.fra.uas.oop.Terminal;

/**
 * Main method takes and implements input commands from the user. It does the
 * operations according to the commands given to it.
 * 
 * @author Muhammad Obaid Ullah
 */

public class Main {

	private static final int QUIT = 4;
	private static final int ENCODE = 7;
	private static final int ARR_SIZE = 10000;
	private static final int STREAMS = 3;
	private static final int COMMAND_LEGNTH = 6;
	private static final int HEXADECIMAL = 16;
	private static final int HEXA_END = 8;
	private static final int REST = 9;
	private static final int TF_NEXT = 13;

	static String fileName = null;
	static DataEncoder8B6T encoder = new DataEncoder8B6T();
	static DataDecoder8B6T decoder = new DataDecoder8B6T();
	static Cable<String[]> receiver = new Cable<String[]>();

	/**
	 * Main method has basically various tasks.
	 * <p>
	 * It takes input from user. If the command starts with encode and a space after
	 * it, it will send it to encoder class.The data streams will be displayed after
	 * encoding.
	 * <p>
	 * If decode is written it will ask for three data streams to be given as input
	 * and will send it to Decoder class.
	 * <p>
	 * Decoded bytes will be changed to string and displayed on console. If quit is
	 * typed , the program will end.
	 * <p>
	 * if anything else is entered , it will show an error message of Unknown
	 * command.
	 * <p>
	 * Input after the transmit command will be transmitted using Cable. Typing
	 * receive will fetch the data from the queue in the cable.
	 * <p>
	 * Similarly transmitFile and receiveFile commands will transmit a complete file
	 * and receive a complete file.
	 * <p>
	 * If there is no data on the cable, a warning of no data on line!!! will be
	 * issued.
	 * 
	 * @param args main method
	 * @throws IOException if the entered input is wrong
	 */

	public static void main(String[] args) {
		String[] input_stream = new String[STREAMS];

		while (true) // while will run until quit is written
		{
			String command, first_command, next_command;
			String[] datastream = new String[ARR_SIZE];

			boolean error = true;
			command = Terminal.readLine();
			byte byteArray[];
			if (command.length() >= QUIT)
			/*
			 * if the length of the given code is exactly four then it will be matched with
			 * quit command to exit program
			 */
			{
				if (command.equals("quit") || command.equals("QUIT")) {
					error = false; /*
									 * default value of error(true) is changed to false will not through an error
									 */
					break;
				}
				/*
				 * if the possible length of string is more than seven "encode " command will be
				 * checked
				 */
				if (command.length() == COMMAND_LEGNTH) {
					first_command = command.substring(0, COMMAND_LEGNTH);
					if (first_command.equals("decode")) {
						error = false;
						/*
						 * Three data streams of 6T code will be input here as input_stream[0] ,
						 * input_stream[1] , input_stream[2]
						 */
						Terminal.printLine("Please enter first encoded data stream:");
						input_stream[0] = Terminal.readLine();

						Terminal.printLine("Please enter second encoded data stream:");
						input_stream[1] = Terminal.readLine();

						Terminal.printLine("Please enter third encoded data stream:");
						input_stream[2] = Terminal.readLine();
						/*
						 * object 'decoder' of class 'DataDecoder8B6T' is created to use the functions
						 * of that class
						 */
						/*
						 * Array of three input streams will be given as an input in the decode function
						 * of class 'DataDecoder8B6T' and output of that function which will be in bytes
						 * will be stored in byte array named as 'asciiCode'
						 */
						byte[] asciiCode = decoder.decode(input_stream);
						String decodedWord = streamReader(asciiCode);
						Terminal.printLine(decodedWord);
						// The output of function asciiCode must not be null

					}
				}

			}

			if (command.length() > ENCODE) {
				first_command = command.substring(0, ENCODE);
				/*
				 * if the first seven letters in the command saved as first_command are matched
				 * with input command it enters here
				 */
				if (first_command.equals("encode ") || first_command.equals("ENCODE ")) {
					error = false;
					next_command = command.substring(ENCODE);
					/*
					 * first seven after confirmation of having encode command are ignored and next
					 * letters are taken as valid input
					 */

					byteArray = next_command.getBytes(); // string is converted into byte array
					DataEncoder8B6T obj = new DataEncoder8B6T(); // object of class DataEncoder8B6T created
					datastream = obj.encode(byteArray); // array of bytes is given as input to encode method

					// output is displayed using terminal class print line method
					Terminal.printLine("DataStream 1: " + datastream[0]);
					Terminal.printLine("DataStream 2: " + datastream[1]);
					Terminal.printLine("DataStream 3: " + datastream[2]);
				}

			}

			if (command.startsWith("transmit ")) {
				error = false;
				String[] input = new String[STREAMS]; // string array to receive datastreams
				Cable<String[]> transmitter = new Cable<String[]>();

				byteArray = command.substring(REST).getBytes(); // input after transmit command

				DataEncoder8B6T obj = new DataEncoder8B6T();
				input = obj.encode(byteArray); // input will be sent to get encoded in 8b6T

				transmitter.transmit(input); // encoded data stream will be put in cable
			}

			if (command.equals("receive")) {

				boolean dataIsThere = receiver.hasData(); // check if there is data on cable
				String[] datastreams = new String[STREAMS];
				if (dataIsThere == true) {
					datastreams = receiver.receive(); // retrieve data from cable
					byte[] asciiCode = decoder.decode(datastreams); // decode the data streams in bytes
					String decodedWord = streamReader(asciiCode); // convert bytes into string
					Terminal.printLine(decodedWord);
				} else {
					Terminal.printLine("No data on line!!!"); // if no data is on cable
				}

				error = false;
			}

			if (command.startsWith("transmitFile ")) {
				fileName = command.substring(TF_NEXT);
				File file = new File(fileName); // filename will be read from console

				EasyFileTransferProtocol eftp = new EasyFileTransferProtocol(null, null, null);
				eftp.transmitFile(file); // file will be sent to transmitfile() method

				Terminal.printLine("send file: " + fileName); // ouput on console after success
				error = false;

			}
			if (command.equals("receiveFile")) {

				EasyFileTransferProtocol eftp = new EasyFileTransferProtocol(null, null, null);
				File file;
				if (receiver.hasData()) // if the cable has data in it
				{
					file = eftp.receiveFile(); // receiveFile function will be called
					String fileName = file.getName(); // name of the file that is received
					Terminal.printLine("received file: " + fileName + " with size: " + file.length());
					// output on console
				} else {
					Terminal.printLine("No data on line!!!"); // if there is no data on cable
				}
				error = false;

			}
			// if the error remains true , it shows the error message

			if (error == true) {
				Terminal.printError("Unknown Command");
			}

		}
	}

	/**
	 * This method converts ascii bytes into readable string.
	 * 
	 * @param asciiCode is the hexadecimal bytes array
	 * @return returns the string after conversion from byte
	 */
	public static String streamReader(byte[] asciiCode) {
		String final_output = new String();
		if (asciiCode != null) { /*
									 * A string array of the same length as that of asciiCode byte array is taken
									 * here
									 */
			String[] output = new String[asciiCode.length];

			StringBuffer sb = new StringBuffer();
			int decimal = 0;

			for (int i = 0; i < asciiCode.length; i++) { // hexadecimal will be converted to string
				if (asciiCode[i] < 0) {
					output[i] = Integer.toHexString(asciiCode[i]);
					output[i] = output[i].substring(COMMAND_LEGNTH, HEXA_END);
					decimal = Integer.parseInt(output[i], HEXADECIMAL);
					output[i] = Character.toString(decimal);
				} else {
					output[i] = Character.toString(asciiCode[i]);
				}
				sb.append(output[i]);
			}

			final_output = sb.toString();

		}
		return final_output;
	}
}