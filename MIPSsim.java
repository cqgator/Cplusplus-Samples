// This is a project I wrote for my Computer Organization class. It converts custom 32-bit string of binary integer instructions (given as an input file) to
// corresponding MIPS instructions and executes the instructions and writes out to two files, one containing the contents of the execution,
// and one containing the disassembled instructions from the 32-bit string of binary integers. This is written in java.


import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MIPSsim {

	static Map<String, String> cat1 = new HashMap<String, String>();
	static Map<String, String> cat2 = new HashMap<String, String>();
	static Map<String, String> cat3 = new HashMap<String, String>();
	static ArrayList<String> insts = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		//System.out.println(args[0]);
		Scanner br = new Scanner(new FileReader(args[0]));
		PrintWriter disWrite = new PrintWriter("disassembly.txt", "UTF-8");
		PrintWriter simWrite = new PrintWriter("simulation.txt", "UTF-8");
		ArrayList<Integer> data = new ArrayList<Integer>();

		cat1.put("000", "NOP");
		cat1.put("001", "J");
		cat1.put("010", "BEQ");
		cat1.put("011", "BNE");
		cat1.put("100", "BGTZ");
		cat1.put("101", "SW");
		cat1.put("110", "LW");
		cat1.put("111", "BREAK");
		cat3.put("000", "ORI");
		cat3.put("001", "XORI");
		cat3.put("010", "ADDI");
		cat3.put("011", "SUBI");
		cat3.put("100", "ANDI");
		cat3.put("101", "SRL");
		cat3.put("110", "SRA");
		cat3.put("111", "SLL");
		cat2.put("000", "XOR");
		cat2.put("001", "MUL");
		cat2.put("010", "ADD");
		cat2.put("011", "SUB");
		cat2.put("100", "AND");
		cat2.put("101", "OR");
		cat2.put("110", "ADDU");
		cat2.put("111", "SUBU");
		int[] regs = new int[32];
		int offset = 64;
		boolean check = false;
		// puts all instructions in thing
		while (br.hasNextLine() && check == false) {
			String in = br.nextLine();
			if (in.equals("00111100000000000000000000001101"))
				check = true;

			insts.add(in);
			disWrite.print(in + "\t" + offset + "\t");
			String oS = in.substring(0, 3);

			String oC = in.substring(3, 6);
			// System.out.println(oS+" "+oC);
			if (oS.equals("001")) {
				disWrite.println(cat1Form(oC, in));
			}
			if (oS.equals("010"))
				disWrite.println(cat2Form(oC, in));
			if (oS.equals("100"))
				disWrite.println(cat3Form(oC, in));
			offset += 4;
		}
		int finOffset = offset;
		while (br.hasNextLine()) {
			String twos = br.nextLine();
			int toAdd = Integer.parseInt(toBintoDec(twos));
			disWrite.println(twos + "\t" + offset + "\t" + toAdd);
			// twos = toBin(twos);
			data.add(toAdd);
			offset += 4;
		}
		// array index * 4 + offset = memory address
		// this is the main loop to exec instructions
		// i should just incorporate this into other stuff :/
		int pc = 64;
		// instead of going through each inst, go until the break
		boolean fin = false;
		int cyc = 1;
		int yiu = 0;
		while (fin == false) {
			String instR = "";
			int currentPc = pc;
			String str = "";
			str = insts.get((pc - 64) / 4);
			// this is the executable code for Cat. 3 instruction
		//	System.out.println("Here's da cat:" + getCat(str));
			if (getCat(str) == 3) {
				String iString = "";
				String opCode = str.substring(3, 6);
				// instR = cat3.get(opCode);
				int dest = Integer.parseInt(str.substring(6, 11), 2);
				int s1 = Integer.parseInt(str.substring(11, 16), 2);
				String imm = "";
				iString = str.substring(11, 16);
				if (opCode.equals("101") || opCode.equals("110") || opCode.equals("111")) {
					imm = toBintoDec(str.substring(27));
					//next line makes it unsigned for xori, addi, subi
				} else if (opCode.equals("000") || opCode.equals("001") || opCode.equals("100")) {
					imm = Integer.toString(Integer.parseInt(str.substring(16), 2));
				} else
					imm = toBintoDec(str.substring(16));
				int im = Integer.parseInt(imm);
				if (opCode.equals("010")) {
					regs[dest] = regs[s1] + im;
				}
				if (opCode.equals("011")) {
					regs[dest] = regs[s1] - im;
				}
				if (opCode.equals("101")) {
					String newStr = "";
					iString = to16Bin(regs[s1]);
					for (int y = 0; y < im; y++) {
						newStr += "0";
					}
					iString = newStr + iString.substring(0, iString.length() - im);
					regs[dest] = Integer.parseInt(toBintoDec(iString));
				}
				if (opCode.equals("110")) {
					String newStr = "";
					iString = to16Bin(regs[s1]);
				//	System.out.println(iString);
					boolean addNeg = false;
					if (iString.substring(0, 1).equals("1"))
						addNeg = true;
					for (int y = 0; y < im; y++) {
						if (iString.substring(0, 1).equals("1"))
							newStr += 1;
						else
							newStr += "0";
					}
					iString = newStr + iString.substring(0, iString.length() - im);
					regs[dest] = Integer.parseInt(toBintoDec(iString));
					// if(addNeg)
					// regs[dest]*=(-1);
				}
				if (opCode.equals("111")) {
					String newStr = "";
					iString = to16Bin(regs[s1]);
					// System.out.println(iString+"Da string");
					for (int y = 0; y < im; y++) {
						newStr += "0";
					}
					// System.out.println("Heres the zros"+newStr);
					iString = iString.substring(im, iString.length()) + newStr;
					// System.out.println(iString);
					regs[dest] = Integer.parseInt(toBintoDec(iString));
				}
				if (opCode.equals("001")){
					regs[dest] = Integer.parseInt(toBintoDec(sickEm(to16Bin(regs[s1]), to16Bin(im), 2)));
					//System.out.println(to16Bin(regs[s1]) +"   space  "+to16Bin(im));
				}
				if (opCode.equals("100"))
					regs[dest] = Integer.parseInt(toBintoDec(sickEm(to16Bin(regs[s1]), to16Bin(im), 0)));
				if (opCode.equals("000"))
					regs[dest] = Integer.parseInt(toBintoDec(sickEm(to16Bin(regs[s1]), to16Bin(im), 1)));

				pc += 4;
			}
			// this is done
			else if (getCat(str) == 1) {
				String opCode = str.substring(3, 6);
				// instR = cat1.get(opCode);
				if (opCode.equals("001"))
					pc = Integer.parseInt(str.substring(6), 2) * 4;
				if (opCode.equals("010")) {
					int c1 = regs[Integer.parseInt(str.substring(6, 11), 2)];
					int c2 = regs[Integer.parseInt(str.substring(11, 16), 2)];
					int go = Integer.parseInt(toBintoDec(str.substring(16)));
					if (c1 == c2)
						pc = pc + (go * 4) + 4;
					else
						pc += 4;
				}
				if (opCode.equals("011")) {
					int c1 = regs[Integer.parseInt(str.substring(6, 11), 2)];
					int c2 = regs[Integer.parseInt(str.substring(11, 16), 2)];
					int go = Integer.parseInt(toBintoDec(str.substring(16)));
					if (c1 != c2)
						pc = pc + (go * 4) + 4;
					else
						pc += 4;
				}
				if (opCode.equals("100")) {
					int c1 = regs[Integer.parseInt(str.substring(6, 11), 2)];
					// int c2 = Integer.parseInt(str.substring(11, 16), 2);
					int go = Integer.parseInt(toBintoDec(str.substring(16)));
					//System.out.println(c1);
					if (c1 > 0)
						pc = pc + (go * 4) + 4;
					else
						pc += 4;
				}
				if (opCode.equals("101")) {
					int regSrc = Integer.parseInt(str.substring(11, 16), 2);
					int go = Integer.parseInt(toBintoDec(str.substring(16)));
					int store = Integer.parseInt(str.substring(6, 11), 2);
					data.remove(((regs[store] - finOffset) / 4) + (go / 4));
					data.add(((regs[store] - finOffset) / 4) + (go / 4), regs[regSrc]);
				}
				if (opCode.equals("110")) {
					// System.out.println(finOffset);
					int regSrc = Integer.parseInt(str.substring(11, 16), 2);
					int go = Integer.parseInt(toBintoDec(str.substring(16)));
					int store = Integer.parseInt(str.substring(6, 11), 2);
					regs[regSrc] = data.get(((regs[store] - finOffset) / 4) + (go / 4));
				}
			} else if (getCat(str) == 2) {
				int dest = Integer.parseInt(str.substring(6, 11), 2);
				int s1 = Integer.parseInt(str.substring(11, 16), 2);
				int s2 = Integer.parseInt(str.substring(16, 21), 2);
				String opCode = str.substring(3, 6);
				// instR = cat1.get(opCode);
				if (opCode.equals("010"))
					regs[dest] = regs[s1] + regs[s2];
				if (opCode.equals("011"))
					regs[dest] = regs[s1] - regs[s2];
				if (opCode.equals("001"))
					regs[dest] = regs[s1] * regs[s2];
				if (opCode.equals("000"))
					regs[dest] = Integer.parseInt(toBintoDec(sickEm(to16Bin(regs[s1]), to16Bin(regs[s2]), 2)));
				if (opCode.equals("100"))
					regs[dest] = Integer.parseInt(toBintoDec(sickEm(to16Bin(regs[s1]), to16Bin(regs[s2]), 0)));
				if (opCode.equals("101"))
					regs[dest] = Integer.parseInt(toBintoDec(sickEm(to16Bin(regs[s1]), to16Bin(regs[s2]), 1)));

			}
			if (pc == currentPc)
				pc += 4;
			// need to put the print out
			// here at the end after altering data
			// instR = str;
			// System.out.println(instR);
			printOut(cyc, str, currentPc, simWrite, regs, finOffset, data);
			// System.out.println("yay it worked");
			cyc++;
			if (str.equals("00111100000000000000000000001101") || yiu > 40)
				fin = true;

			yiu++;
		}

		// printOut(1, String inst, int adr, simWrite, regs, data);
		disWrite.close();
		simWrite.close();
		br.close();
	}

	// lw sets the the register it gets to data at address(offset+valueOf(reg))
	static String cat1Form(String opCode, String in) {
		String str = "";
		str += cat1.get(opCode);
		if (opCode.equals("001"))
			str += " #" + Integer.toString(Integer.parseInt(in.substring(6), 2) * 4);
		if (opCode.equals("010") || opCode.equals("011"))
			str += " R" + Integer.parseInt(in.substring(6, 11), 2) + ", R" + Integer.parseInt(in.substring(11, 16), 2)
					+ ", #" + toBintoDec(in.substring(16));
		if (opCode.equals("100"))
			str += " R" + Integer.parseInt(in.substring(6, 11), 2) + ", #" + toBintoDec(in.substring(16));
		if (opCode.equals("101") || opCode.equals("110"))
			str += " R" + Integer.parseInt(in.substring(11, 16), 2) + ", " + toBintoDec(in.substring(16)) + "(R"
					+ Integer.parseInt(in.substring(6, 11), 2) + ")";
		if (str.length() == 0)
			str = "hgurwgiurwiugorkkormfgo";
		return str;
	}

	static String cat2Form(String opCode, String in) {
		String str = "";
		String dest = Integer.toString(Integer.parseInt(in.substring(6, 11), 2));
		String s1 = Integer.toString(Integer.parseInt(in.substring(11, 16), 2));
		String s2 = Integer.toString(Integer.parseInt(in.substring(16, 21), 2));
		str += cat2.get(opCode);
		str += " R" + dest + ", R" + s1 + ", R" + s2;
		return str;
	}

	static String cat3Form(String opCode, String in) {
		String str = "";
		String dest = Integer.toString(Integer.parseInt(in.substring(6, 11), 2));
		String s1 = Integer.toString(Integer.parseInt(in.substring(11, 16), 2));
		String imm = "";
		if (opCode.equals("101") || opCode.equals("110") || opCode.equals("111")) {
			imm = toBintoDec(in.substring(27));
		} else if (opCode.equals("000") || opCode.equals("001") || opCode.equals("100")) {
			imm = Integer.toString(Integer.parseInt(in.substring(16), 2));
		} else
			imm = toBintoDec(in.substring(16));
		str += cat3.get(opCode);
		str += " R" + dest + ", R" + s1 + ", #" + imm;
		return str;
	}

	static void printOut(int cycle, String inst, int adr, PrintWriter simWrite, int[] regs, int finOffset,
			ArrayList<Integer> data) {
		simWrite.println("--------------------");
		String line = "";
		String opCode = inst.substring(3, 6);
		if (getCat(inst) == 1)
			line = cat1Form(opCode, inst);
		if (getCat(inst) == 2)
			line = cat2Form(opCode, inst);
		if (getCat(inst) == 3)
			line = cat3Form(opCode, inst);
		simWrite.println("Cycle " + cycle + ":" + "\t" + adr + "\t" + line);
		
	//	if (!line.equals("BREAK")) {
			simWrite.println();
			simWrite.println("Registers");
			// simWrite.println();
			// next thing to do is print out the registers
			for (int x = 0; x < 32; x++) {
				if (x == 0) {
					simWrite.print("R00:\t" + regs[x]);
				} else if (x == 8) {
					simWrite.print("R08:\t" + regs[x]);
				} else if (x == 16) {
					simWrite.print("R16:\t" + regs[x]);
				} else if (x == 24) {
					simWrite.print("R24:\t" + regs[x]);
				} else
					simWrite.print("\t" + regs[x]);
				if ((x + 1) % 8 == 0)
					simWrite.println();
			}
			simWrite.println();
			simWrite.println("Data");
			for (int x = 0; x < data.size(); x++) {
				if (x % 8 == 0) {
					if (x != 0)
						simWrite.println();
					simWrite.print(Integer.toString(finOffset + (x * 4)) + ":\t");
				}
				simWrite.print(data.get(x) + "\t");
			}
			simWrite.println();
			simWrite.println();
		//}
		
		// print the data or some shit idk

	}

	static int getCat(String in) {
		String oS = in.substring(0, 3);
		if (oS.equals("001")) {
			return 1;
		}
		if (oS.equals("010"))
			return 2;
		if (oS.equals("100"))
			return 3;
		else
			return -1;
	}

	static String toBintoDec(String twos) {
		String newTwo = twos;
		if (twos.substring(0, 1).equals("1")) {
			String newStr = "";
			for (int x = 0; x < twos.length(); x++) {
				if (twos.substring(x, x + 1).equals("1"))
					newStr += "0";
				else
					newStr += "1";
			}
			int temp = Integer.parseInt(newStr, 2);
			newStr = Integer.toBinaryString(temp + 1);
			return "-" + Integer.toString(Integer.parseInt(newStr, 2));
		} else
			return Integer.toString(Integer.parseInt(newTwo, 2));
	}

	static String toBin(String twos) {
		String newTwo = twos;
		if (twos.substring(0, 1).equals("1")) {
			String newStr = "";
			for (int x = 0; x < twos.length(); x++) {
				if (x != twos.length() - 1) {
					if (twos.substring(x, x + 1).equals("1"))
						newStr += "0";
					else
						newStr += "1";
				} else
					newStr += twos.substring(x, x + 1);
			}
			newTwo = newStr;
		}
		return newTwo;
	}

	static String to16Bin(int i) {
		String ert = "00000000000000000000000000000000";
		if (i < 0) {
			ert = Integer.toBinaryString(i);
		} else if (i > 0) {
			String temp = Integer.toBinaryString(i);
			int diff = 32 - temp.length();
			String newStr = "";
			for (int x = 0; x < diff; x++)
				newStr += "0";
			ert = newStr + temp;
		}
		return ert;
	}

	// produce and of two 16 bit strings
	// 0 = and, 1 = or, 2 = xor
	static String sickEm(String s1, String s2, int type) {
		String str = "";
		for (int x = 0; x < s1.length(); x++) {
			if (type == 0) {
				if (s1.substring(x, x + 1).equals("1") && s2.substring(x, x + 1).equals("1"))
					str += "1";
				else
					str += "0";
			}
			if (type == 1) {
				if (s1.substring(x, x + 1).equals("1") || s2.substring(x, x + 1).equals("1"))
					str += "1";
				else
					str += "0";
			}

			if (type == 2) {
				if ((s1.substring(x, x + 1).equals("1") || s2.substring(x, x + 1).equals("1"))
						&& !(s1.substring(x, x + 1).equals("1") && s2.substring(x, x + 1).equals("1")))
					str += "1";
				else
					str += "0";
			}
		//	System.out.println(str);
		}
		return str;
	}
}