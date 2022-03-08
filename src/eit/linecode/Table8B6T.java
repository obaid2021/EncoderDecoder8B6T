package eit.linecode;

/**
 * This class contains 8B6T Table/Unit. The table is used to convert Hexadecimal
 * numbers to 6T code and vice versa.
 * <p>
 * This class also converts convert bytes to 6T code and 6T code to bytes using
 * the 8B6T Table
 * 
 * @author Muhammad Obaid Ullah
 */
public class Table8B6T {
	private static final int SIXT_CODE_LENGTH = 6;
	private static final int POSITION0 = 0;
	private static final int POSITION1 = 1;
	private static final int POSITION2 = 2;
	private static final int POSITION3 = 3;
	private static final int POSITION4 = 4;
	private static final int POSITION5 = 5;
	private static final int POSITION6 = 6;
	private static final int POSITION7 = 7;
	private static final int POSITION8 = 8;
	// String of 8B6T table
	public static String codingTable = "00 +-==+- 20 ==-++- 40 +=+==- 60 =-=++=\n"
			+ "01 =+-+-= 21 --+==+ 41 ++==-= 61 ==-+=+\n" + "02 +-=+-= 22 ++-=+- 42 +=+=-= 62 =-=+=+\n"
			+ "03 -=++-= 23 ++-=-+ 43 =++=-= 63 -==+=+\n" + "04 -=+=+- 24 ==+=-+ 44 =++==- 64 -==++=\n"
			+ "05 =+--=+ 25 ==+=+- 45 ++=-== 65 ==-=++\n" + "06 +-=-=+ 26 ==-==+ 46 +=+-== 66 =-==++\n"
			+ "07 -=+-=+ 27 --+++- 47 =++-== 67 -===++\n" + "08 -+==+- 28 -=-++= 48 ===+== 68 -+-++=\n"
			+ "09 =-++-= 29 --=+=+ 49 ===-++ 69 --++=+\n" + "0a -+=+-= 2a -=-+=+ 4a ===+-+ 6a -+-+=+\n"
			+ "0b +=-+-= 2b =--+=+ 4b ===++- 6b +--+=+\n" + "0c +=-=+- 2c =--++= 4c ===-+= 6c +--++=\n"
			+ "0d =-+-=+ 2d --==++ 4d ===-=+ 6d --+=++\n" + "0e -+=-=+ 2e -=-=++ 4e ===+-= 6e -+-=++\n"
			+ "0f +=--=+ 2f =--=++ 4f ===+=- 6f +--=++\n" + "10 +=+--= 30 +-==-+ 50 +=+--+ 70 -++===\n"
			+ "11 ++=-=- 31 =+--+= 51 ++=-+- 71 +-+===\n" + "12 +=+-=- 32 +-=-+= 52 +=+-+- 72 ++-===\n"
			+ "13 =++-=- 33 -=+-+= 53 =++-+- 73 ==+===\n" + "14 =++--= 34 -=+=-+ 54 =++--+ 74 -=+===\n"
			+ "15 ++==-- 35 =+-+=- 55 ++=+-- 75 =-+===\n" + "16 +=+=-- 36 +-=+=- 56 +=++-- 76 +=-===\n"
			+ "17 =++=-- 37 -=++=- 57 =+++-- 77 =+-===\n" + "18 =+-=+- 38 -+==-+ 58 +++=-- 78 =--+++\n"
			+ "19 =+-=-+ 39 =-+-+= 59 +++-=- 79 -=-+++\n" + "1a =+-++- 3a -+=-+= 5a +++--= 7a --=+++\n"
			+ "1b =+-==+ 3b +=--+= 5b ++=--= 7b --=++=\n" + "1c =-+==+ 3c +=-=-+ 5c ++=--+ 7c ++-==-\n"
			+ "1d =-+++- 3d =-++=- 5d ++===- 7d ==+==-\n" + "1e =-+=-+ 3e -+=+=- 5e --+++= 7e ++---+\n"
			+ "1f =-+=+- 3f +=-+=- 5f ==-++= 7f ==+--+\n" + "80 +-+==- a0 =-=++- c0 +-+=+- e0 +-=++-\n"
			+ "81 ++-=-= a1 ==-+-+ c1 ++-+-= e1 =+-+-+\n" + "82 +-+=-= a2 =-=+-+ c2 +-++-= e2 +-=+-+\n"
			+ "83 -++=-= a3 -==+-+ c3 -+++-= e3 -=++-+\n" + "84 -++==- a4 -==++- c4 -++=+- e4 -=+++-\n"
			+ "85 ++--== a5 ==--++ c5 ++--=+ e5 =+--++\n" + "86 +-+-== a6 =-=-++ c6 +-+-=+ e6 +-=-++\n"
			+ "87 -++-== a7 -==-++ c7 -++-=+ e7 -=+-++\n" + "88 =+===- a8 -+-++- c8 =+==+- e8 -+=++-\n"
			+ "89 ==+=-= a9 --++-+ c9 ==++-= e9 =-++-+\n" + "8a =+==-= aa -+-+-+ ca =+=+-= ea -+=+-+\n"
			+ "8b +===-= ab +--+-+ cb +==+-= eb +=-+-+\n" + "8c +====- ac +--++- cc +===+- ec +=-++-\n"
			+ "8d ==+-== ad --+-++ cd ==+-=+ ed =-+-++\n" + "8e =+=-== ae -+--++ ce =+=-=+ ee -+=-++\n"
			+ "8f +==-== af +---++ cf +==-=+ ef +=--++\n" + "90 +-+--+ b0 =-===+ d0 +-+=-+ f0 +-===+\n"
			+ "91 ++--+- b1 ==-=+= d1 ++--+= f1 =+-=+=\n" + "92 +-+-+- b2 =-==+= d2 +-+-+= f2 +-==+=\n"
			+ "93 -++-+- b3 -===+= d3 -++-+= f3 -=+=+=\n" + "94 -++--+ b4 -====+ d4 -++=-+ f4 -=+==+\n"
			+ "95 ++-+-- b5 ==-+== d5 ++-+=- f5 =+-+==\n" + "96 +-++-- b6 =-=+== d6 +-++=- f6 +-=+==\n"
			+ "97 -+++-- b7 -==+== d7 -+++=- f7 -=++==\n" + "98 =+=--+ b8 -+-==+ d8 =+==-+ f8 -+===+\n"
			+ "99 ==+-+- b9 --+=+= d9 ==+-+= f9 =-+=+=\n" + "9a =+=-+- ba -+-=+= da =+=-+= fa -+==+=\n"
			+ "9b +==-+- bb +--=+= db +==-+= fb +=-=+=\n" + "9c +==--+ bc +--==+ dc +===-+ fc +=-==+\n"
			+ "9d ==++-- bd --++== dd ==++=- fd =-++==\n" + "9f +==+-- bf +--+== df +==+=- ff +=-+==\n"
			+ "9e =+=+-- be -+-+== de =+=+=- fe -+=+==       ";

	/**
	 * This method converts hexadecimal number to 6T code string.
	 * <p>
	 * This method uses the table 8B6T for the conversion.
	 * 
	 * @param hexadecimal is the number that is to be converted to 6T code.
	 * @return 6T code will be returned.
	 */
	public String convert(String hexadecimal) {

		char hexArray[], codeTable[];
		hexArray = hexadecimal.toCharArray(); /*
												 * received Hexadecimal String will be converted to Hex char variable
												 */

		codeTable = codingTable.toCharArray(); /* String table will be converted to char table */
		char arrayOf6T[] = new char[SIXT_CODE_LENGTH];
		String converted6T;
		int letter1, letter2;
		if (hexArray.length < 2) /*
									 * if the number of letters in received Hexadecimal code are less two , for
									 * example 6 , 3 , a zero will be added to match the table as 06 , 03
									 */
		{
			letter1 = hexArray[0]; /*
									 * letter1 gets the value of received hexadecimal code's first letter
									 */
			letter2 = 0; // letter 2 is assigned value 0
		} else {
			letter1 = hexArray[0]; // letter1 is assigned first value of letter from hexadecimal code
			letter2 = hexArray[1]; // letter2 is assigned second value of letter from hexadecimal code
		}
		for (int i = 0; i < codeTable.length; i++) {
			if (codeTable[i] == letter1) /*
											 * for example Received hexadecimal = 6A, 6 will be match with letter1 and A
											 * will be matched with letter2
											 */
			{
				if (codeTable[i + 1] == letter2) { /*
													 * if both of them match , the code will be taken from the codeTable
													 * by skipping one space and taking the next 6 values as 6T code
													 */
					arrayOf6T[POSITION0] = codeTable[i + POSITION3];
					arrayOf6T[POSITION1] = codeTable[i + POSITION4];
					arrayOf6T[POSITION2] = codeTable[i + POSITION5];
					arrayOf6T[POSITION3] = codeTable[i + POSITION6];
					arrayOf6T[POSITION4] = codeTable[i + POSITION7];
					arrayOf6T[POSITION5] = codeTable[i + POSITION8];
				}
			}
		}
		converted6T = String.copyValueOf(arrayOf6T, POSITION0, POSITION6);
		// array of 6T will be stored as a string and returned
		return converted6T;
	}

	/**
	 * This method converts the 6T code to Hexadecimal number.
	 * <p>
	 * This method also uses 8B6T table given in the class Table8B6T.
	 * 
	 * @param code is the 6T code which will be converted to hexadecimal
	 * @return It will return Hexadecimal code that corresponds to 6T code in Table
	 */
	public String lookInTable(String code) {
		char[] codeTable = codingTable.toCharArray();
		char[] match = code.toCharArray();
		char[] answer = new char[2];
		String hexdecimalCode = "not found";
		for (int i = 0; i < codeTable.length - SIXT_CODE_LENGTH; i++) {
			if (match[POSITION0] == codeTable[i] && match[POSITION1] == codeTable[i + POSITION1]
					&& match[POSITION2] == codeTable[i + POSITION2] && match[POSITION3] == codeTable[i + POSITION3]
					&& match[POSITION4] == codeTable[i + POSITION4] && match[POSITION5] == codeTable[i + POSITION5]) {
				answer[POSITION0] = codeTable[i - POSITION3];
				answer[POSITION1] = codeTable[i - POSITION2];
				hexdecimalCode = new String(answer);
				return hexdecimalCode;
			}
		}
		return "not found";
	}
}
