/* Ideas to make bomb better:
 * Make it accept either first 3 letters or full word
 * Give better descriptions when asking for something
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;

public class Bomb {
	private static Scanner input = new Scanner(System.in);
	private int batteries;
	private int holders;
	private String serial;
	private ArrayList<String> indicators;
	private ArrayList<String> ports;
	private int lastDigit;
	private boolean vowel;
	private static boolean accel = false;
	private static boolean debug = false;
	private static boolean superaccel=false;
	private static boolean edgeworkEZ = false;
    private int emptyPortPlate;
    private int portPlates;
    //STL
    public static ArrayList<ArrayList<ArrayList<Integer>>> lists;
    //Maze
    public static ArrayList<ArrayList<String>> moves;
	//FEVars
	private ArrayList<Boolean> validity;
	private ArrayList<Integer> stagenums;
	private ArrayList<String> stagecolors;
	private ArrayList<Integer> stages;
	private String sequence;
	//Methods
	public static void main(String[] args) {
		if (accel) {
			int bat = 3;
			String snum = "asdfj9";
			if (!superaccel) {
				System.out.println("give serial");
				snum=input.next();
			}
			ArrayList<String> inds = new ArrayList<String>();
			int holders=3;
			ArrayList<String> daports = new ArrayList<String>();
			Bomb IAmDaBomb = new Bomb(bat,holders,snum,inds,daports, -1);
			IAmDaBomb.loop();
		}
		
		else if (!edgeworkEZ){
			System.out.print("Batteries: ");
			int bat = input.nextInt();
			int holds=0;
			if (bat<2) {
				if (bat==1) {
					holds=1;
				}
			}
			else {
				System.out.print("Holders: ");
				holds=input.nextInt();
			}
			
			ArrayList<String> inds = new ArrayList<String>();
			while (true) {
				System.out.println("Current Indicators: "+inds+"\nEnter any indicators with a * afterwards to indicate lit or no star for unlit, or enter STOP to continue: ");
				String ind = input.next();
				ind=ind.toUpperCase();
				if (ind.equals("STOP"))break;
				inds.add(ind);
			}
			ArrayList<String> daports = new ArrayList<String>();
			while (true) {
				System.out.println("Port options: par, ser, rca, rj, ps2, dvi");
				System.out.print("Current Ports: "+daports+"\nEnter any ports (enter doubles), or enter STOP to continue: ");
				String port = input.next();
				port=port.toLowerCase();
				if (port.startsWith("stop"))break;
				if (port.startsWith("par"))daports.add("parallel");
				if (port.startsWith("ser"))daports.add("serial");
				if (port.startsWith("rca"))daports.add("rca");
				if (port.startsWith("rj"))daports.add("rj45");
				if (port.startsWith("ps2"))daports.add("ps2");
				if (port.startsWith("dvi"))daports.add("dvi");
			}
			System.out.print("Serial Number: ");
			String snum = input.next();
			Bomb IAmDaBomb = new Bomb(bat,holds,snum,inds,daports, -1);
			IAmDaBomb.loop();
		}
		else {
			System.out.println("Edgework.");
			String unfilteredInput = input.nextLine().replaceAll("\\s", "").toLowerCase();
			//System.out.println(unfilteredInput);
			if (unfilteredInput.startsWith("edge")) {
				unfilteredInput=unfilteredInput.substring(unfilteredInput.indexOf("//")+2);
			}
			if (unfilteredInput.startsWith("rule")) {
				unfilteredInput=unfilteredInput.substring(unfilteredInput.indexOf("//")+2);
			}
			
			int bats = Integer.parseInt(unfilteredInput.substring(0,unfilteredInput.indexOf("b")));
			unfilteredInput=unfilteredInput.substring(unfilteredInput.indexOf("b")+1);
			int holds = Integer.parseInt(unfilteredInput.substring(0,unfilteredInput.indexOf("h")));
			unfilteredInput=unfilteredInput.substring(unfilteredInput.indexOf("h")+3);
			
			boolean indicators = false;
			int index = 0;
			ArrayList<String> inds = new ArrayList<String>();
			if (unfilteredInput.indexOf("*")>=0)indicators=true;
			int endOfIndicators = unfilteredInput.indexOf("//");
			if (endOfIndicators%3==0)indicators=true;
			if (indicators) {
				while (index<endOfIndicators) {
					if (unfilteredInput.substring(index,index+1).equals("*")) {
						inds.add(unfilteredInput.substring(index+1,index+4).toUpperCase()+"*");
						index+=4;
					}
					else {
						inds.add(unfilteredInput.substring(index,index+3).toUpperCase());
						index+=3;
					}
				}
				index+=2;
			}
			unfilteredInput = unfilteredInput.substring(index);
			index = 0;
			
			ArrayList<String> unfilteredPorts = new ArrayList<String>();
			ArrayList<String> daports = new ArrayList<String>();
			int numOfPlates = -1;
			boolean Dohaveports = true;
			if (unfilteredInput.indexOf("[")==-1)Dohaveports=false;
			if (Dohaveports) {
				//System.out.println("beginning of ports "+unfilteredInput);
				numOfPlates = countOf(unfilteredInput, "[");
				while (!unfilteredInput.substring(0,1).equals("/")) {
					int endOfPlate = unfilteredInput.indexOf("]");
					int comma = unfilteredInput.indexOf(",");

					while (comma<endOfPlate&&comma!=-1&&comma!=0) {
						
						//System.out.println("adding "+unfilteredInput.substring(1,comma));
						unfilteredPorts.add(unfilteredInput.substring(1,comma));

						unfilteredInput=unfilteredInput.substring(comma);
						endOfPlate = unfilteredInput.indexOf("]");
						comma = unfilteredInput.substring(1).indexOf(",")+1;
						//System.out.println(comma);
						//System.out.println(endOfPlate);

					}
					unfilteredPorts.add(unfilteredInput.substring(1,endOfPlate));
					unfilteredInput=unfilteredInput.substring(endOfPlate+1);
				}
				
				for (String port : unfilteredPorts) {
					if (port.startsWith("par"))daports.add("parallel");
					if (port.startsWith("ser"))daports.add("serial");
					if (port.startsWith("rca"))daports.add("rca");
					if (port.startsWith("rj"))daports.add("rj45");
					if (port.startsWith("ps2"))daports.add("ps2");
					if (port.startsWith("dvi"))daports.add("dvi");
				}
			}
			else numOfPlates = 0;
			String serial = unfilteredInput.substring(unfilteredInput.length()-6,unfilteredInput.length());
			
			//System.out.println(bats);
			//System.out.println(holds);
			//System.out.println(serial);
			//System.out.println(inds);
			//System.out.println(daports);
			//System.out.println(numOfPlates);
			
			Bomb IAmDaBomb = new Bomb (bats, holds, serial, inds, daports, numOfPlates);
			IAmDaBomb.loop();
		}
		
	}
	//Bomb Structure
	public Bomb (int bats, int holds, String ser, ArrayList<String> inds, ArrayList<String> daports, int plates) {
		batteries = bats;
		holders=holds;
		serial = ser.toLowerCase();
		indicators = inds;
		ports = daports;
		lastDigit = Integer.parseInt(serial.substring(5,6));
		vowel = false;
		lists = new ArrayList<ArrayList<ArrayList<Integer>>>();
		moves = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 5; i++) {
			if (serial.substring(i,i+1).equalsIgnoreCase("A")||serial.substring(i,i+1).equalsIgnoreCase("E")||serial.substring(i,i+1).equalsIgnoreCase("I")||serial.substring(i,i+1).equalsIgnoreCase("O")||serial.substring(i,i+1).equalsIgnoreCase("U"))vowel=true;
		}
		portPlates = plates;
		//FEVars
		validity = new ArrayList<Boolean>();
		stagenums = new ArrayList<Integer>();
		stagecolors = new ArrayList<String>();
		stages = new ArrayList<Integer>();
		sequence = "";
	}
	public void loop() {
		int page = 1;
		int choice = 0;
		while (true) {
			if (choice!=9&&choice!=10&&choice!=39&&choice!=40&&choice!=69&&choice!=70) {
				System.out.print("enter anything to continue");
				@SuppressWarnings("unused")
				String asdfdsa = input.next();
			}
			if (page<1)page=1;
			if (page>3)page=3;
			if (page==1) {
			System.out.println("Pick a module to defuse."
					+ "\n1.Simple Wires      11.Astrology       21.Colored Squares"
					+ "\n2.Button            12.Murder          22.The Bulb"
					+ "\n3.Simon Says        13.Two Bits        23.Painting"
					+ "\n4.Who's On First    14.RPSLS           24.Ice Cream"
					+ "\n5.Memory            15.Color Flash     25.Word Search"
					+ "\n6.Complicated Wires 16.Semaphore       26.Cooking"
					+ "\n7.Wire Sequences    17.Shape Shift     27.Laundry"
					+ "\n8.Passwords         18.Burglar Alarm   28.Big Circle"
					+ "\n9.Previous Page     19.Horrible Memory 29.The Sun"
					+ "\n10.Next Page        20.Simon Screams   30.Skewed Slots"
					+ "\n\n1234.Bomb Defused");
			}
			if (page==2) {
			System.out.println("Pick a module to defuse."
					+ "\n1.Adventure Game    11.Cheap Checkout  21.Lightspeed"
					+ "\n2.Backgrounds       12.The Clock       22.Da Dime Keeper"
					+ "\n3.Bitmaps           13.Combination Lock23.Color Generator"
					+ "\n4.Binary LEDs       14.Comp. Buttons   24.Waste Management"
					+ "\n5.Battleships       15.Identity Parade 25.The Moon"
					+ "\n6.Bitwise Operations16.Logic Gates     26.Bases"
					+ "\n7.Blind Alley       17.Mashgebra       27.Playfair Cipher"
					+ "\n8.Caesar Cipher     18.Forget Me Not   28.Unfair Cipher"
					+ "\n9.Previous Page     19.Forget Every    29.TBCC"
					+ "\n10.Next Page        20.Chord Qualities 30.Black Hole"
					+ "\n\n1234.Bomb Defused");
			}
			if (page==3) {
				System.out.println("Pick a module to defuse."
						+ "\n1.Light Cycle    11.Simon Stores  21.WIP"
						+ "\n2.Safety Safe       12.WIP       22.WIP"
						+ "\n3.Morsematics       13.WIP   23.WIP"
						+ "\n4.BizzFuzz       14.WIP   24.WIP"
						+ "\n5.Logical Buttons       15.WIP 25.WIP"
						+ "\n6.SplitLoot   16.WIP     26.WIP"
						+ "\n7.TheCube      17.WIP       27.WIP"
						+ "\n8.Simon Sends     18.WIP   28.WIP"
						+ "\n9.Previous Page     19.WIP   29.WIP"
						+ "\n10.Next Page        20.WIP 30.WIP"
						+ "\n\n1234.Bomb Defused");
				}
			try {
			choice = input.nextInt();
			}
			catch (InputMismatchException e) {
				choice = 99;
			}
			if (choice==1234)break;
			if (choice==-1) {
				System.out.println(batteries);
				System.out.println(holders);
				System.out.println(indicators);
				System.out.println(ports);
				System.out.println(serial);
			}
			choice+=30*(page-1);
			switch (choice) {
			case 0:
				this.Maze();
				break;
			case 1:
				this.SimpleWires();
				break;
			case 2:
				this.Button();
				break;
			case 3:
				this.SimonSays();
				break;
			case 4:
				this.WhosOnFirst();
				break;
			case 5:
				this.Memory();
				break;
			case 6:
				this.ComplicatedWires();
				break;
			case 7:
				this.WireSequences();
				break;
			case 8:
				this.Passwords();
				break;
			case 9:
				page-=1;
				break;
			case 10:
				page+=1;
				break;
			case 11:
				this.Astrology();
				break;
			case 12:
				this.Murder();
				break;
			case 13:
				this.TwoBits();
				break;
			case 14:
				this.RPSLS();
				break;
			case 15:
				this.ColorFlash();
				break;
			case 16:
				this.Semaphore();
				break;
			case 17:
				this.ShapeShift();
				break;
			case 18:
				this.BurglarAlarm();
				break;
			case 19:
				this.HorribleMemory();
				break;
			case 20:
				this.SimonScreams();
				break;
			case 21:
				this.ColoredSquares();
				break;
			case 22:
				this.TheBulb();
				break;
			case 23:
				this.Painting();
				break;
			case 24:
				this.IceCream();
				break;
			case 25:
				this.WordSearch();
				break;
			case 26:
				this.Cooking();
				break;
			case 27:
				this.Laundry();
				break;
			case 28:
				this.BigCircle();
				break;
			case 29:
				this.TheSun();
				break;
			case 30:
				this.SkewedSlots();
				break;
			case 31:
				this.AdventureGame();
				break;
			case 32:
				this.Backgrounds();
				break;
			case 33:
				this.Bitmaps();
				break;
			case 34:
				this.BinaryLEDs();
				break;
			case 35:
				this.Battleships();
				break;
			case 36:
				this.BitwiseOperations();
				break;
			case 37:
				this.BlindAlley();
				break;
			case 38:
				this.CaesarCipher();
				break;
			case 39:
				page-=1;
				break;
			case 40:
				page+=1;
				break;
			case 41:
				this.CheapCheckout();
				break;
			case 42:
				this.TheClock();
				break;
			case 43:
				this.CombinationLock();
				break;
			case 44:
				this.ComplicatedButtons();
				break;
			case 45:
				this.IdentityParade();
				break;
			case 46:
				this.LogicGates();
				break;
			case 47:
				this.Mashgebra();
				break;
			case 48:
				this.FMN();
				break;
			case 49:
				this.ForgetEverything();
				break;
			case 50:
				this.ChordQualities();
				break;
			case 51:
				this.Lightspeed();
				break;
			case 52:
				this.TheTimeKeeper();
				break;
			case 53:
				this.ColorGenerator();
				break;
			case 54:
				this.WasteManagement();
				break;
			case 55:
				this.TheMoon();
				break;
			case 56:
				this.Bases();
				break;
			case 57:
				this.PlayfairCipher();
				break;
			case 58:
				this.UnfairCipher();
				break;
			case 59:
				this.TBCC();
				break;
			case 60:
				this.BlackHole();
				break;
			case 61:
				this.LightCycle();
				break;
			case 62:
				this.SafetySafe();
				break;
			case 63:
				this.Morsematics();
				break;
			case 64:
				this.FizzBuzz();
				break;
			case 65:
				this.LogicalButtons();
				break;
			case 66:
				this.SplittingTheLoot();
				break;
			case 67:
				this.TheCube();
				break;
			case 68:
				this.SimonSends();
				break;
			case 69:
				page--;
				break;
			case 70:
				page++;
				break;
			case 71:
				this.SimonStores();
				break;
			default:
				break;
			}
		
		}
		System.out.println("Program over naturally");
	}
	//Vanilla Modules
	
	public void SimpleWires() {
		System.out.println("Enter wire colors with the first letter of the color, except for blue, enter u instead of b.");
		ArrayList<String> swires = new ArrayList<String>();
		while (true) {
			if (swires.size() == 6)break;
			System.out.print("Current Wires: "+swires+"\nEnter next wire, or enter STOP to continue: ");
			String wire = input.next();
			wire=wire.toLowerCase();
			if (wire.equals("stop"))break;
			swires.add(wire);
			
		}
		boolean bad = false;
		for (int i = 0; i <swires.size(); i++) {
			if (swires.get(i).length()>1)bad=true;
			swires.set(i, swires.get(i).toLowerCase());
		}
		if (bad)System.out.println("Bad entering of wires.");
		else {
			int wiretocut = 0;
			if (swires.size()==3) {
				if (countOf(swires,"r")==0)wiretocut=2;
				else if (swires.get(2).equals("w"))wiretocut=3;
				else if (countOf(swires,"u")>1)wiretocut=swires.lastIndexOf("u")+1;
				else wiretocut=3;
			}
			if (swires.size()==4) {
				if (countOf(swires,"r")>1&&lastDigit%2==1)wiretocut=swires.lastIndexOf("r")+1;
				else if (swires.get(3).equals("y")&&countOf(swires,"r")==0)wiretocut=1;
				else if (countOf(swires,"u")==1)wiretocut=1;
				else if (countOf(swires,"y")>1)wiretocut=4;
				else wiretocut=2;
			}
			if (swires.size()==5) {
				if (swires.get(4).equals("b")&&lastDigit%2==1)wiretocut=4;
				else if (countOf(swires,"r")==1&&countOf(swires,"y")>1)wiretocut=1;
				else if (countOf(swires,"b")==0)wiretocut=2;
				else wiretocut=1;
			}
			if (swires.size()==6) {
				if (countOf(swires,"y")==0&&lastDigit%2==1)wiretocut=3;
				else if (countOf(swires,"y")==1&&countOf(swires,"w")>1)wiretocut=4;
				else if (countOf(swires,"r")==0)wiretocut=6;
				else wiretocut=4;
			}
			System.out.println("Cut wire "+wiretocut);
		}
		
	}
	public void Button() {
		String color;
		String word;
		String choice = "Hold button";
		System.out.println("Enter the first letter of the color of your button");
		System.out.println("Then enter the first letter of the word on your button");
		color=input.next();
		word=input.next();
		color = color.toLowerCase();
		if (color.equals("b"))color="u";
		word=word.toLowerCase();
		if (color.equals("u")&&word.equals("a"));
		else if (batteries>1&&word.equals("d"))choice = "Click";
		else if (color.equals("w")&&indicators.contains("CAR*"));
		else if (batteries>2&&indicators.contains("FRK*"))choice="Click";
		else if (color.equals("r")&&word.equals("h"))choice="Click";
		
		if (choice.equals("Hold button"))System.out.println("Hold button, y is 5, u is 4, all else is 1");
		else System.out.println(choice);
		System.out.print("enter anything to continue");
		@SuppressWarnings("unused")
		String asdfdsa = input.next();
	}
	public void SimonSays() {
		System.out.println("Enter your number of strikes");
		int strikes = input.nextInt();
		if (strikes>2)strikes=2;
		ArrayList<String> flashes = new ArrayList<String>();
		while (true) {
			System.out.println("Enter the letter of the newest flashing color, KEEP PUTTING 'U' FOR BLUE or put STOP if you're done");
			String newadd = input.next();
			newadd=newadd.toLowerCase();
			if (newadd.equals("stop"))break;
			if (newadd.equals("b"))newadd="u";
			if (!(newadd.equals("r")||newadd.equals("u")||newadd.equals("g")||newadd.equals("y")))System.out.println("Color not accepted");
			else {
				flashes.add(newadd);
				String press = "";
				for (int i = 0; i < flashes.size(); i++) {
					if (strikes==0) {
						if (vowel) {
							if (flashes.get(i).equals("r")) {
								press+="Blue, ";
							}
							if (flashes.get(i).equals("u")) {
								press+="Red, ";
							}
							if (flashes.get(i).equals("g")) {
								press+="Yellow, ";
							}
							if (flashes.get(i).equals("y")) {
								press+="Green, ";
							}
						}
						else {
							if (flashes.get(i).equals("r")) {
								press+="Blue, ";
							}
							if (flashes.get(i).equals("u")) {
								press+="Yellow, ";
							}
							if (flashes.get(i).equals("g")) {
								press+="Green, ";
							}
							if (flashes.get(i).equals("y")) {
								press+="Red, ";
							}
						}
					}
					if (strikes==1) {
						if (vowel) {
							if (flashes.get(i).equals("r")) {
								press+="Yellow, ";
							}
							if (flashes.get(i).equals("u")) {
								press+="Green, ";
							}
							if (flashes.get(i).equals("g")) {
								press+="Blue, ";
							}
							if (flashes.get(i).equals("y")) {
								press+="Red, ";
							}
						}
						else {
							if (flashes.get(i).equals("r")) {
								press+="Red, ";
							}
							if (flashes.get(i).equals("u")) {
								press+="Blue, ";
							}
							if (flashes.get(i).equals("g")) {
								press+="Yellow, ";
							}
							if (flashes.get(i).equals("y")) {
								press+="Green, ";
							}
						}
					}
					if (strikes==2) {
						if (vowel) {
							if (flashes.get(i).equals("r")) {
								press+="Green, ";
							}
							if (flashes.get(i).equals("u")) {
								press+="Red, ";
							}
							if (flashes.get(i).equals("g")) {
								press+="Yellow, ";
							}
							if (flashes.get(i).equals("y")) {
								press+="Blue, ";
							}
						}
						else {
							if (flashes.get(i).equals("r")) {
								press+="Yellow, ";
							}
							if (flashes.get(i).equals("u")) {
								press+="Green, ";
							}
							if (flashes.get(i).equals("g")) {
								press+="Blue, ";
							}
							if (flashes.get(i).equals("y")) {
								press+="Red, ";
							}
						}
					}
					if (strikes>2) {
						System.out.println("You have too many strikes");
						break;
					}
				
				}
				System.out.println(press);
			}
			
		}
	}
	public void WhosOnFirst() {
		@SuppressWarnings("unused")
		String x = input.nextLine();
		for (int i = 0; i < 3; i++) {
			String labelneeded;
			System.out.println("Enter the phrase on the display. For a literal blank, enter 'lb'");
			String display = input.nextLine().toLowerCase();
			if (display.equals("ur"))labelneeded="Top left";
			else if (display.equals("first")||display.equals("okay")||display.equals("c"))labelneeded="Top right";
			else if (display.equals("yes")||display.equals("nothing")||display.equals("led")||display.equals("theyare"))labelneeded="middle left";
			else if (display.equals("blank")||display.equals("read")||display.equals("red")||display.equals("you")||display.equals("your")||display.equals("you're")||display.equals("their"))labelneeded="middle right";
			else if (display.equals("lb")||display.equals("reed")||display.equals("leed")||display.equals("they're"))labelneeded="bottom left";
			else labelneeded="bottom right";
			System.out.println("Enter the label on the "+labelneeded+" button.");
			String therealone=input.nextLine().toLowerCase();;
			//System.out.println(therealone);
			String answer="";
			if (therealone.equals("ready"))answer="yes, okay, what, middle, left, press, right, blank, ready";
			if (therealone.equals("first"))answer="left, okay, yes, middle, no, right, nothing, uhhh, wait, ready, blank, what, press, first";
			if (therealone.equals("no"))answer="blank, uhhh, wait, first, what, ready, right, yes, nothing, left, press, okay, no";
			if (therealone.equals("blank"))answer="WAIT, RIGHT, OKAY, MIDDLE, BLANK";
			if (therealone.equals("nothing"))answer="UHHH, RIGHT, OKAY, MIDDLE, YES, BLANK, NO, PRESS, LEFT, WHAT, WAIT, FIRST, NOTHING";
			if (therealone.equals("yes"))answer="OKAY, RIGHT, UHHH, MIDDLE, FIRST, WHAT, PRESS, READY, NOTHING, YES";
			if (therealone.equals("what"))answer="UHHH, WHAT";
			if (therealone.equals("uhhh"))answer="READY, NOTHING, LEFT, WHAT, OKAY, YES, RIGHT, NO, PRESS, BLANK, UHHH";
			if (therealone.equals("left"))answer="RIGHT, LEFT";
			if (therealone.equals("right"))answer="YES, NOTHING, READY, PRESS, NO, WAIT, WHAT, RIGHT";
			if (therealone.equals("middle"))answer="BLANK, READY, OKAY, WHAT, NOTHING, PRESS, NO, WAIT, LEFT, MIDDLE";
			if (therealone.equals("okay"))answer="MIDDLE, NO, FIRST, YES, UHHH, NOTHING, WAIT, OKAY";
			if (therealone.equals("wait"))answer="UHHH, NO, BLANK, OKAY, YES, LEFT, FIRST, PRESS, WHAT, WAIT";
			if (therealone.equals("press"))answer="RIGHT, MIDDLE, YES, READY, PRESS";
			if (therealone.equals("you"))answer="SURE, YOU ARE, YOUR, YOU'RE, NEXT, UH HUH, UR, HOLD, WHAT?, YOU";
			if (therealone.equals("you are"))answer="YOUR, NEXT, LIKE, UH HUH, WHAT?, DONE, UH UH, HOLD, YOU, U, YOU'RE, SURE, UR, YOU ARE";
			if (therealone.equals("your"))answer="UH UH, YOU ARE, UH HUH, YOUR";
			if (therealone.equals("you're"))answer="YOU, YOU'RE";
			if (therealone.equals("ur"))answer="DONE, U, UR";
			if (therealone.equals("u"))answer="UH HUH, SURE, NEXT, WHAT?, YOU'RE, UR, UH UH, DONE, U";
			if (therealone.equals("uh huh"))answer="uh huh";
			if (therealone.equals("uh uh"))answer="UR, U, YOU ARE, YOU'RE, NEXT, UH UH";
			if (therealone.equals("what?"))answer="YOU, HOLD, YOU'RE, YOUR, U, DONE, UH UH, LIKE, YOU ARE, UH HUH, UR, NEXT, WHAT?";
			if (therealone.equals("done"))answer="SURE, UH HUH, NEXT, WHAT?, YOUR, UR, YOU'RE, HOLD, LIKE, YOU, U, YOU ARE, UH UH, DONE";
			if (therealone.equals("next"))answer="WHAT?, UH HUH, UH UH, YOUR, HOLD, SURE, NEXT";
			if (therealone.equals("hold"))answer=" YOU ARE, U, DONE, UH UH, YOU, UR, SURE, WHAT?, YOU'RE, NEXT, HOLD";
			if (therealone.equals("sure"))answer="YOU ARE, DONE, LIKE, YOU'RE, YOU, HOLD, UH HUH, UR, SURE";
			if (therealone.equals("like"))answer="YOU'RE, NEXT, U, UR, HOLD, DONE, UH UH, WHAT?, UH HUH, YOU, LIKE";
			System.out.println("Click the first of these on your bomb.");
			System.out.println(answer);
		}
	}
	public void Memory() {
		System.out.println("For each round, enter the number on the display followed by the numbers you see, in order. E.G. 12314");
		//STAGE 1
		//4 0's: display
		//3 0's: pos 1
		//2 0's: pos 2
		//1 0:   pos 3
		//0 0's: pos 4
		System.out.print("Stage 1: ");
		int stage1=input.nextInt();
		int displayS1=stage1/10000;
		int pos1=-1;
		int label1=5;
		if (displayS1==1||displayS1==2) {
			pos1=100;
			label1=((stage1/pos1)%10);
			System.out.println("Press label "+label1);
		}
		if (displayS1==3) {
			pos1=10;
			label1=((stage1/pos1)%10);
			System.out.println("Press label "+label1);
		}
		if(displayS1==4) {
			
			pos1=1;
			label1=((stage1/pos1)%10);
			System.out.println("Press label "+label1);
		}
		//STAGE 2
		System.out.print("Stage 2: ");
		int stage2=input.nextInt();
		int displayS2=stage2/10000;
		int pos2=-1;
		int label2=5;
		
		if (displayS2==1) {
			label2=4;
			for (int i = 1; i < 10000; i*=10) {
				int throwaway=stage2/i;
				if (throwaway%10==4) {
					pos2=i;
				}
			}
			System.out.println("Press label 4");
		}
		if (displayS2==2||displayS2==4) {
			pos2=pos1;
			label2=((stage2/pos2)%10);
			System.out.println("Press label "+label2);
		}
		if (displayS2==3) {
			pos2=1000;
			label2=((stage2/pos2)%10);
			System.out.println("Press label "+label2);
		}
		//Stage 3
		System.out.print("Stage 3: ");
		int stage3=input.nextInt();
		int displayS3=stage3/10000;
		int pos3=-1;
		int label3=5;
		
		if (displayS3==1) {
			label3=label2;
			for (int i = 1; i < 10000; i*=10) {
				int throwaway=stage3/i;
				if (throwaway%10==label3) {
					pos3=i;
				}
			}
			System.out.println("Press label "+label3);
		}
		if (displayS3==2) {
			label3=label1;
			for (int i = 1; i < 10000; i*=10) {
				int throwaway=stage3/i;
				if (throwaway%10==label3) {
					pos3=i;
				}
			}
			System.out.println("Press label "+label3);
		}
		if (displayS3==3) {
			pos3=10;
			label3=((stage3/pos3)%10);
			System.out.println("Press label "+label3);
		}
		if (displayS3==4) {
			label3=4;
			for (int i = 1; i < 10000; i*=10) {
				int throwaway=stage2/i;
				if (throwaway%10==4) {
					pos3=i;
				}
			}
			System.out.println("Press label 4");
		}
		//Stage 4
		System.out.print("Stage 4: ");
		int stage4=input.nextInt();
		int displayS4=stage4/10000;
		int pos4=-1;
		int label4=5;
		if (displayS4 == 1) {
			pos4=pos1;
			label4=((stage4/pos4)%10);
			System.out.println("Press label "+label4);
		}
		if (displayS4 == 2) {
			pos4=1000;
			label4=((stage4/pos4)%10);
			System.out.println("Press label "+label4);
		}
		if (displayS4 == 3||displayS4==4) {
			pos4=pos2;
			label4=((stage4/pos4)%10);
			System.out.println("Press label "+label4);
		}
		//Stage 5
		System.out.print("Stage 5: ");
		int stage5=input.nextInt();
		int displayS5=stage5/10000;
		if (displayS5==1) {
			System.out.println("Press label "+label1);
		}
		if (displayS5==2) {
			System.out.println("Press label "+label2);
		}
		if (displayS5==3) {
			System.out.println("Press label "+label4);
		}
		if (displayS5==4) {
			System.out.println("Press label "+label3);
		}
	}
	public void ComplicatedWires() {
		System.out.println("Enter your wires, hitting enter after each one. When done type STOP");
		ArrayList<String> wires = new ArrayList<String>();
		ArrayList<String> cuts = new ArrayList<String>();
		boolean serialcut = false;
		if (lastDigit%2==0)serialcut=true;
		boolean parallelcut = false;
		if (ports.contains("parallel"))parallelcut=true;
		boolean batterycut = false;
		if (batteries>1)batterycut=true;
		while (true) {
			
			System.out.print("Current Wires: "+wires+"\nEnter next wire, or enter STOP to continue: ");
			String wire = input.next();
			wire=wire.toUpperCase();
			if (wire.equals("STOP"))break;
			wires.add(wire);
			if (wires.size()>=6)break;
		}
		for (String j:wires) {
			boolean cutwire = false;
			boolean r=false;
			boolean b=false;
			boolean s=false;
			boolean l=false;
			for (int i = 0; i <j.length(); i++) {
				if (j.substring(i, i+1).equals("R"))r=true;
				if (j.substring(i, i+1).equals("B"))b=true;
				if (j.substring(i, i+1).equals("S"))s=true;
				if (j.substring(i, i+1).equals("L"))l=true;
			}
			
			if (r) {
				if (b) {
					if (s) {
						if (l) {
							cutwire=false;
						}
						else {
							cutwire=parallelcut;
						}
					}
					else if (l) {
						cutwire=serialcut;
					}
					else {
						cutwire=serialcut;
					}
				}
				else if (s) {
					if (l) {
						cutwire=batterycut;
					}
					else {
						cutwire=true;
					}
				}
				else if (l) {
					cutwire=batterycut;
				}
				else {
					cutwire=serialcut;
				}
			}
			else if (b) {
				if (s) {
					if (l) {
						cutwire=parallelcut;
					}
					else {
						cutwire=false;
					}
				}
				else if (l) {
					cutwire=parallelcut;
				}
				else {
					cutwire=serialcut;
				}
				
			}
			else if (s) {
				if (l) {
					cutwire=batterycut;
				}
				else {
					cutwire=true;
				}
			}
			else if (l) {
				cutwire=false;
			}
			else {
				cutwire=true;
			}
		if (cutwire) {
			cuts.add("cut");
		}
		else {
			cuts.add("don't");
		}
		}
		System.out.println(cuts);
	}
	public void WireSequences() {
		ArrayList<String> reds = new ArrayList<String>();
		reds.add("c");
		reds.add("b");
		reds.add("a");
		reds.add("ac");
		reds.add("b");
		reds.add("ac");
		reds.add("abc");
		reds.add("ab");
		reds.add("b");
		ArrayList<String> blues = new ArrayList<String>();
		blues.add("b");
		blues.add("ac");
		blues.add("b");
		blues.add("a");
		blues.add("b");
		blues.add("bc");
		blues.add("c");
		blues.add("ac");
		blues.add("a");
		ArrayList<String> blacks = new ArrayList<String>();
		blacks.add("abc");
		blacks.add("ac");
		blacks.add("b");
		blacks.add("ac");
		blacks.add("b");
		blacks.add("bc");
		blacks.add("ab");
		blacks.add("c");
		blacks.add("c");
		int countred = 0;
		int countblue = 0;
		int countblack = 0;
		System.out.println("Enter your wires, and the letters they go to. e.g. bc for black to c REMEMBER BLUE IS U");
		for (int round = 0; round <4; round++) {
			ArrayList<String> wires = new ArrayList<String>();
			ArrayList<String> cuts = new ArrayList<String>();
			while (true) {
				if (wires.size() == 3)break;
				System.out.print("Current Wires: "+wires+"\nEnter next wire, or enter STOP to continue: ");
				String addwire = input.next();
				addwire=addwire.toLowerCase();
				if (addwire.equals("stop"))break;
				wires.add(addwire);
			}
			for (String wire: wires) {
				if (wire.substring(0,1).equals("r")) {
					String cutornot = "don't";
					for (int i = 0; i <reds.get(countred).length(); i++) {
						if (wire.substring(1,2).equals(reds.get(countred).substring(i, i+1)))cutornot="cut";
					}
					countred++;
					cuts.add(cutornot);
				}
				if (wire.substring(0,1).equals("u")) {
					String cutornot = "don't";
					for (int i = 0; i <blues.get(countblue).length(); i++) {
						if (wire.substring(1,2).equals(blues.get(countblue).substring(i, i+1)))cutornot="cut";
					}
					countblue++;
					cuts.add(cutornot);
				}
				if (wire.substring(0,1).equals("b")) {
					String cutornot = "don't";
					for (int i = 0; i <blacks.get(countblack).length(); i++) {
						if (wire.substring(1,2).equals(blacks.get(countblack).substring(i, i+1)))cutornot="cut";
					}
					countblack++;
					cuts.add(cutornot);
				}
			}
			System.out.println(cuts);
		}
	}
	public void Passwords() {
		System.out.println("Enter the first and fourth columns");
		ArrayList<String> col1 = new ArrayList<String>();
		ArrayList<String> col4 = new ArrayList<String>();
		ArrayList<String> possibilities = new ArrayList<String>();
		while (col1.size()<6) {
			System.out.print("Enter next letter: ");
			col1.add(input.next().toLowerCase());
		}
		System.out.println("Col 1 received, enter Col 4");
		while (col4.size()<6) {
			System.out.print("Enter next letter: ");
			col4.add(input.next().toLowerCase());
		}
		if (col1.contains("a")) {
			if (col4.contains("u"))possibilities.add("about");
			if (col4.contains("e"))possibilities.add("after");
			if (col4.contains("i"))possibilities.add("again");
		}
		if (col1.contains("b")&&col4.contains("o"))possibilities.add("below");
		if (col1.contains("c")&&col4.contains("l"))possibilities.add("could");
		if (col1.contains("e")&&col4.contains("r"))possibilities.add("every");
		if (col1.contains("f")) {
			if (col4.contains("s"))possibilities.add("first");
			if (col4.contains("n"))possibilities.add("found");
		}
		if (col1.contains("g")&&col4.contains("a"))possibilities.add("great");
		if (col1.contains("h")&&col4.contains("s"))possibilities.add("house");
		if (col1.contains("l")) {
			if (col4.contains("g"))possibilities.add("large");
			if (col4.contains("r"))possibilities.add("learn");
		}
		if (col1.contains("n")&&col4.contains("e"))possibilities.add("never");
		if (col1.contains("o")&&col4.contains("e"))possibilities.add("other");
		if (col1.contains("p")) {
			if (col4.contains("c"))possibilities.add("place");
			if (col4.contains("n"))possibilities.add("plant");
			if (col4.contains("n"))possibilities.add("point");
		}
		if (col1.contains("r")&&col4.contains("h"))possibilities.add("right");
		if (col1.contains("s")) {
			if (col4.contains("l"))possibilities.add("small");
			if (col4.contains("n"))possibilities.add("sound");
			if (col4.contains("l"))possibilities.add("spell");
			if (col4.contains("l"))possibilities.add("still");
			if (col4.contains("d"))possibilities.add("study");
		}
		if (col1.contains("t")) {
			if (col4.contains("i"))possibilities.add("their");
			if (col4.contains("r"))possibilities.add("there");
			if (col4.contains("s"))possibilities.add("these");
			if (col4.contains("n"))possibilities.add("thing");
			if (col4.contains("n"))possibilities.add("think");
			if (col4.contains("e"))possibilities.add("three");
		}
		if (col1.contains("w")) {
			if (col4.contains("e"))possibilities.add("water");
			if (col4.contains("r"))possibilities.add("where");
			if (col4.contains("c"))possibilities.add("which");
			if (col4.contains("l"))possibilities.add("world");
			if (col4.contains("l"))possibilities.add("would");
			if (col4.contains("t"))possibilities.add("write");
		}
		System.out.println(possibilities);
	}
	public void Maze() {
		String[][] table = {{"DR", "LR", "LD", "RD", "RL", "L"},
							{"UD", "RD", "UL", "UR", "RL", "DL"},
							{"UD", "RU", "LD", "RD", "RL", "ULD"},
							{"UD", "R", "ULR", "UL", "R", "ULD"},
							{"UDR", "LR", "LD", "DR", "L", "UD"},
							{"UR", "L", "UR", "LU", "R", "LU"}};
		for (int i = 0; i < (table.length*table[0].length); i++){
			if (mazeHelper(2,2,table,new ArrayList<String>(), 5,5, i)){
				i = 100;
				System.out.println(moves);
			}
		}
	}
	//Modded Modules
	
	public void Semaphore() {
		boolean letters = false;
		String answer="";
		System.out.println("Enter both flag directions with a space in between. Cardinal directions have dupe letters");
		ArrayList<String> sequence = new ArrayList<String>();
		while (true) {
			System.out.print("Enter flag directions or stop to stop: ");
			String flags = input.nextLine().toUpperCase();
			if (flags.equals("STOP"))break;
			sequence.add(flags);
		}
		for (String flag : sequence) {
			String reverse = flag.substring(2,4)+flag.substring(0,2);
			if (reverse.equals("NN EE")||flag.equals("NN EE")&&letters)if(countOf(serial,"j")<1)answer="NNWW";
			if (reverse.equals("NN NE")||flag.equals("NN NE"))letters=false;
			if (reverse.equals("NN EE")||flag.equals("NN EE")&&!letters)letters=true;
			if (reverse.equals("SW SS")||flag.equals("SW SS")) {
				if (letters)if(countOf(serial, "a")<1)answer="SW SS";
				else if(countOf(serial,"1")<1)answer="SW SS";
			}
			if (reverse.equals("WW SS")||flag.equals("WW SS")) {
				if (letters) {
					if(countOf(serial, "b")<1)answer="WW SS";
				}
				else if(countOf(serial,"2")<1)answer="WW SS";
			}
			if (reverse.equals("NW SS")||flag.equals("NW SS")) {
				if (letters) {
					if(countOf(serial, "c")<1)answer="NW SS";
				}
				else if(countOf(serial,"3")<1)answer="NW SS";
			}
			if (reverse.equals("NN SS")||flag.equals("NN SS")) {
				if (letters) {
					if(countOf(serial, "d")<1)answer="NN SS";
				}
				else if(countOf(serial,"4")<1)answer="NN SS";
			}
			if (reverse.equals("SS NE")||flag.equals("SS NE")) {
				if (letters) {
					if(countOf(serial, "e")<1)answer="SS NE";
				}
				else if(countOf(serial,"5")<1)answer="SS NE";
			}
			if (reverse.equals("SS EE")||flag.equals("SS EE")) {
				if (letters) {
					if(countOf(serial, "f")<1)answer="SS EE";
				}
				else if(countOf(serial,"6")<1)answer="SS EE";
			}
			if (reverse.equals("SS SE")||flag.equals("SS SE")) {
				if (letters) {
					if(countOf(serial, "g")<1)answer="SS SE";
				}
				else if(countOf(serial,"7")<1)answer="SS SW";
			}
			if (reverse.equals("WW SW")||flag.equals("WW SW")) {
				if (letters) {
					if(countOf(serial, "h")<1)answer="WW SW";
				}
				else if(countOf(serial,"8")<1)answer="WW SW";
			}
			if (reverse.equals("NW SW")||flag.equals("NW SW")) {
				if (letters) {
					if(countOf(serial, "i")<1)answer="NW SW";
				}
				else if(countOf(serial,"9")<1)answer="NW SW";
			}
			if (reverse.equals("SW NN")||flag.equals("SW NN")) {
				if (letters) {
					if(countOf(serial, "k")<1)answer="SW NN";
				}
				else if(countOf(serial,"0")<1)answer="SW NN";
			}
			
			if (reverse.equals("SW NW")||flag.equals("SW NW"))if(countOf(serial,"l")<1)answer="SW NW";
			if (reverse.equals("SW EE")||flag.equals("SW EE"))if(countOf(serial,"m")<1)answer="SW EE";
			if (reverse.equals("SW SE")||flag.equals("SW SE"))if(countOf(serial,"n")<1)answer="SW SE";
			if (reverse.equals("NW WW")||flag.equals("NW WW"))if(countOf(serial,"o")<1)answer="NW WW";
			if (reverse.equals("WW NN")||flag.equals("WW NN"))if(countOf(serial,"p")<1)answer="WW NN";
			if (reverse.equals("WW NE")||flag.equals("WW NE"))if(countOf(serial,"q")<1)answer="WW NE";
			if (reverse.equals("WW EE")||flag.equals("WW EE"))if(countOf(serial,"r")<1)answer="WW EE";
			if (reverse.equals("WW SW")||flag.equals("WW SW"))if(countOf(serial,"s")<1)answer="WW SW";
			if (reverse.equals("NW NN")||flag.equals("NW NN"))if(countOf(serial,"t")<1)answer="NW NN";
			if (reverse.equals("NW NE")||flag.equals("NW NE"))if(countOf(serial,"u")<1)answer="NW NE";
			if (reverse.equals("NN SW")||flag.equals("NN SW"))if(countOf(serial,"v")<1)answer="NN SW";
			if (reverse.equals("NE EE")||flag.equals("NE EE"))if(countOf(serial,"w")<1)answer="NE EE";
			if (reverse.equals("NE SE")||flag.equals("NE SE"))if(countOf(serial,"x")<1)answer="NE SE";
			if (reverse.equals("NW EE")||flag.equals("NW EE"))if(countOf(serial,"y")<1)answer="NW EE";
			if (reverse.equals("EE SE")||flag.equals("EE SE"))if(countOf(serial,"z")<1)answer="EE SE";	
			if(!answer.equals(""))System.out.println(answer);
			
		}
	}
	public void Astrology() {
		/*
		 * Cheat Sheet for Positions
		 * Fire:0
		 * Water:1
		 * Earth:2
		 * Air:3
		 * 
		 * Sun:0	Mars:4		Neptune:8
		 * Moon:1	Jupiter:5	Pluto:9
		 * Mercury:2Saturn:6		
		 * Venus:3	Uranus:7
		 * 
		 * Aries:0	Leo:4	 Sagittarius:8
		 * Taurus:1	Virgo:5	 Capricorn:9
		 * Gemini:2	Libra:6	 Aquarius:10
		 * Cancer:3	Scorpio:7Pisces:11
		 * 
		 */
		
		int[][] AlchemyAstrology = {{ 0, 0, 1,-1, 0, 1,-2, 2, 0,-1}, 
									{-2, 0,-1, 0, 2, 0,-2, 2, 0, 1},
									{-1,-1, 0,-1, 1, 2, 0, 2, 1,-2},
									{-1, 2,-1, 0,-2,-1, 0, 2,-2, 2}};
		
		int[][] AlchemyZodiac =    {{ 1, 0,-1, 0, 0, 2, 2, 0, 1, 0, 1, 0},
									{ 2, 2,-1, 2,-1,-1,-2, 1, 2, 0, 0, 2},
									{-2,-1, 0, 0, 1, 0, 1, 2,-1,-2, 1, 1},
									{ 1, 1,-2,-2, 2, 0,-1, 1, 0, 0,-1,-1}};
		
		int[][] AstrologyZodiac =  {{-1,-1, 2, 0,-1, 0,-1, 1, 0, 0,-2,-2},
									{-2, 0, 1, 0, 2, 0,-1, 1, 2, 0, 1, 0},
									{-2,-2,-1,-1, 1,-1, 0,-2, 0, 0,-1, 1},
									{-2, 2,-2, 0, 0, 1,-1, 0, 2,-2,-1, 1},
									{-2, 0,-1,-2,-2,-2,-1, 1, 1, 1, 0,-1},
									{-1,-2, 1,-1, 0, 0, 0, 1, 0,-1, 2, 0},
									{-1,-1, 0, 0, 1, 1, 0, 0, 0, 0,-1,-1},
									{-1, 2, 0, 0, 1,-2, 1, 0, 2,-1, 1, 0},
									{1, 0, 2, 1,-1, 1, 1, 1, 0,-2, 2, 0},
									{-1, 0, 0,-1,-2, 1, 2, 1, 1, 0, 0,-1}};
		String AlchemySignString;
		int AlchemySign;
		
		String AstrologySignString;
		int AstrologySign;
		
		String ZodiacSignString;
		int ZodiacSign;
		int Tweak=0;
		int sum=0;
		
		System.out.println("Please enter your Alchemy Sign \n(fire, water, earth, air)");
		AlchemySignString=input.next().toLowerCase();
		
		System.out.println("Please enter your Astrology Sign \n(sun, moon, mercury, venus, mars, jupiter, saturn, uranus, neptune, pluto)");
		AstrologySignString=input.next().toLowerCase();
		
		System.out.println("Please enter your Zodiac Sign \n(aries, taurus, gemini, cancer, leo, virgo, libra, scorpio, sagittarius, capricorn, aquarius, pisces)");
		ZodiacSignString=input.next().toLowerCase();
		
		boolean done = false;
		for (int i = 0; i < AlchemySignString.length(); i++) {
			for (int j = 0; j < serial.length(); j++) {
				if (AlchemySignString.substring(i,i+1).equals(serial.substring(j, j+1))&&!done){
					done=true;
					Tweak+=1;
				}
			}
		}
		if (!done)Tweak-=1;
		
		done = false;
		for (int i = 0; i < AstrologySignString.length(); i++) {
			for (int j = 0; j < serial.length(); j++) {
				if (AstrologySignString.substring(i,i+1).equals(serial.substring(j, j+1))&&!done){
					done=true;
					Tweak+=1;
				}
			}
		}
		if (!done)Tweak-=1;
		
		done = false;
		for (int i = 0; i < ZodiacSignString.length(); i++) {
			for (int j = 0; j < serial.length(); j++) {
				if (ZodiacSignString.substring(i,i+1).equals(serial.substring(j, j+1))&&!done){
					done=true;
					Tweak+=1;
				}
			}
		}
		if (!done)Tweak-=1;
		if (debug) System.out.println("After serial "+Tweak);
		switch (AlchemySignString) {
		case "fire":
			AlchemySign=0;
			break;
		case "water":
			AlchemySign=1;
			break;
		case "earth":
			AlchemySign=2;
			break;
		case "air":
			AlchemySign=3;
			break;
		default:
			System.out.println("Error in entering alchemy");
			AlchemySign=0;
			break;
		}
		
		switch (AstrologySignString) {
		case "sun":
			AstrologySign=0;
			break;
		case "moon":
			AstrologySign=1;
			break;
		case "mercury":
			AstrologySign=2;
			break;
		case "venus":
			AstrologySign=3;
			break;
		case "mars":
			AstrologySign=4;
			break;
		case "jupiter":
			AstrologySign=5;
			break;
		case "saturn":
			AstrologySign=6;
			break;
		case "uranus":
			AstrologySign=7;
			break;
		case "neptune":
			AstrologySign=8;
			break;
		case "pluto":
			AstrologySign=9;
			break;
		default:
			System.out.println("Error entering astrology");
			AstrologySign=0;
			break;
		}
		
		switch (ZodiacSignString) {
		case "aries":
			ZodiacSign=0;
			break;
		case "taurus":
			ZodiacSign=1;
			break;
		case "gemini":
			ZodiacSign=2;
			break;
		case "cancer":
			ZodiacSign=3;
			break;
		case "leo":
			ZodiacSign=4;
			break;
		case "virgo":
			ZodiacSign=5;
			break;
		case "libra":
			ZodiacSign=6;
			break;
		case "scorpio":
			ZodiacSign=7;
			break;
		case "sagittarius":
			ZodiacSign=8;
			break;
		case "capricorn":
			ZodiacSign=9;
			break;
		case "aquarius":
			ZodiacSign=10;
			break;
		case "pisces":
			ZodiacSign=11;
			break;
		default:
			System.out.println("Error entering Zodiac");
			ZodiacSign=0;
			break;
		}
		sum += AlchemyAstrology[AlchemySign][AstrologySign];
		if (debug)System.out.println(AlchemySignString +", "+AstrologySignString+"="+AlchemyAstrology[AlchemySign][AstrologySign]);
		sum += AlchemyZodiac[AlchemySign][ZodiacSign];
		if (debug)System.out.println(AlchemySignString +", "+ZodiacSignString+"="+AlchemyZodiac[AlchemySign][ZodiacSign]);
		sum += AstrologyZodiac[AstrologySign][ZodiacSign];
		if (debug)System.out.println(AstrologySignString +", "+ZodiacSignString+"="+AstrologyZodiac[AstrologySign][ZodiacSign]);
		sum += Tweak;
		System.out.println("Omen is "+sum);
		
	}
	public void Murder() {
		//edge case
		int tempholders=holders;
		if (holders==0) {
			tempholders=1;
		}
		
		System.out.println("Enter all suspects first three letters of color");
		ArrayList<String> suspects = new ArrayList<String>();
		while (suspects.size()<4) {
			System.out.print("Current suspects "+suspects+"\nEnter a new one: ");
			String addsuspect = input.next().toLowerCase().substring(0,3);
			if (addsuspect.length()>3) {
				System.out.println("too long, only first 3 letters");
			}
			else {
				suspects.add(addsuspect);
			}
		}
		System.out.println("Enter all weapons first three letters of weapon, pip for pipe");
		ArrayList<String> weapons = new ArrayList<String>();
		while (weapons.size()<4) {
			System.out.print("Current weapons "+weapons+"\nEnter a new one: ");
			String addweapon = input.next().toLowerCase().substring(0,3);
			if (addweapon.length()>3) {
				System.out.println("too long, only first 3 letters, pip for pipe");
			}
			else {
				weapons.add(addweapon);
			}
		}
		System.out.println("Enter red room.");
		String room = input.next().toLowerCase().substring(0,3);
		int suspectrow = 0;
		//suspect row
		if (indicators.contains("TRN*"))suspectrow=5;
		else if (room.equals("din"))suspectrow=7;
		else if (countOf(ports,"rca")>1)suspectrow=8;
		else if (batteries/tempholders==2||batteries==0)suspectrow=2;
		else if (room.equals("stu"))suspectrow=4;
		else if (batteries>4)suspectrow=9;
		else if (indicators.contains("FRQ"))suspectrow=1;
		else if (room.equals("con"))suspectrow=3;
		else suspectrow=6;
		
		int weaponrow = 0;
		//weapon row
		if (room.equals("lou"))weaponrow=3;
		else if (batteries>4)weaponrow=1;
		else if (ports.contains("serial"))weaponrow=9;
		else if (room.equals("bil"))weaponrow=4;
		else if (batteries==0)weaponrow=6;
		else if (noLitIndicators())weaponrow=5;
		else if (room.equals("hal"))weaponrow=7;
		else if (countOf(ports,"rca")>1)weaponrow=2;
		else weaponrow=8;
		
		suspectrow-=1;
		weaponrow-=1;
		String[] suspecttable = {"sca", "plu", "pea", "gre", "mus", "whi"};
		String[] weapontable =  {"can", "dag", "pip", "rev", "rop", "spa"};
		ArrayList<Integer> suspectNumber = new ArrayList<Integer>();
		ArrayList<Integer> weaponNumber =  new ArrayList<Integer>();
		
		if (suspects.contains("sca"))suspectNumber.add(0);
		if (suspects.contains("plu"))suspectNumber.add(1);
		if (suspects.contains("pea"))suspectNumber.add(2);
		if (suspects.contains("gre"))suspectNumber.add(3);
		if (suspects.contains("mus"))suspectNumber.add(4);
		if (suspects.contains("whi"))suspectNumber.add(5);
		
		if (weapons.contains("can"))weaponNumber.add(0);
		if (weapons.contains("dag"))weaponNumber.add(1);
		if (weapons.contains("pip"))weaponNumber.add(2);
		if (weapons.contains("rev"))weaponNumber.add(3);
		if (weapons.contains("rop"))weaponNumber.add(4);
		if (weapons.contains("spa"))weaponNumber.add(5);
		
		String[][] table = {{"din", "lib", "lou", "kit", "stu", "con"},
							{"stu", "hal", "bil", "lou", "kit", "lib"},
							{"kit", "bil", "bal", "lib", "con", "din"},
							{"lou", "bal", "din", "con", "hal", "kit"},
							{"bil", "kit", "stu", "bal", "din", "hal"},
							{"con", "lou", "lib", "stu", "bil", "bal"},
							{"bal", "con", "kit", "hal", "lib", "stu"},
							{"hal", "stu", "con", "din", "lou", "bil"},
							{"lib", "din", "hal", "bil", "bal", "lou"}};
		
		ArrayList<String> answers = new ArrayList<String>();
		
		for (int j : suspectNumber) {
			for (int k : weaponNumber) {
				if (table[suspectrow][j].equals(table[weaponrow][k])) {
					answers.add("Suspect: "+suspecttable[j]+"\nWeapon: "+weapontable[k]+"\nRoom: "+table[weaponrow][k]);
				}
			}
		}
		for (String answer : answers) {
			System.out.println(answer);
		}
	}
	public void TwoBits() {
		char[] k = serial.toCharArray();
		int start = 0;
		int firstdigit;
		int seconddigit;
		for (char l : k) {
			if ((int)l>96&&(int)l<123) {
				start = l-96;
				break;
			}
		}
		start+=(lastDigit*batteries);
		if (ports.contains("rca")&&!ports.contains("rj45"))start*=2;
		if (start<10) {
			firstdigit=0;
			seconddigit=start;
		}
		else {
			firstdigit=start/10%10;
			seconddigit=start%10;
		}
		
		String[][] twoBitTable= {{"kb", "dk", "gv", "tk", "pv", "kp", "bv", "vt", "pz", "dt"},
								 {"ee", "zk", "ke", "ck", "zp", "pp", "tp", "tg", "pd", "pt"},
								 {"tz", "eb", "ec", "cc", "cz", "zv", "cv", "gc", "bt", "gt"},
								 {"bz", "pk", "kz", "kg", "vd", "ce", "vb", "kd", "gg", "dg"},
								 {"pb", "vv", "ge", "kv", "dz", "pe", "db", "cd", "td", "cb"},
								 {"gb", "tv", "kk", "bg", "bp", "vp", "ep", "tt", "ed", "zg"},
								 {"de", "dd", "ev", "te", "zd", "bb", "pc", "bd", "kc", "zb"},
								 {"eg", "bc", "tc", "ze", "zc", "gp", "et", "vc", "tb", "vz"},
								 {"ez", "ek", "dv", "cg", "ve", "dp", "bk", "pg", "gk", "gz"},
								 {"kt", "ct", "zz", "vg", "gd", "cp", "be", "zt", "vk", "dc"}};
		String letters = twoBitTable[firstdigit][seconddigit];
		for (int i = 0; i<3; i++) {
			System.out.println("Query: "+letters);
			System.out.print("Response: ");
			int response = input.nextInt();
			if (response<10) {
				firstdigit=0;
				seconddigit=response;
			}
			else {
				firstdigit=response/10%10;
				seconddigit=response%10;
			}
			letters = twoBitTable[firstdigit][seconddigit];
		}
		System.out.println("Submit: "+letters);
	}
	public void RPSLS() {
		System.out.println("What is your decoy?\n1.Rock\n2.Paper\n3.Scissors\n4.Lizard\n5.Spock\n6.No Decoy");
		int decoy = input.nextInt();
		decoy--;
		String answer = "";
		boolean skipletters = false;
		int rock = 0,paper=0,scissors=0,lizard=0,spock = 0;
		for (int i = 0; i < 5; i++) {
			if (serial.substring(i,i+1).equals("x")||serial.substring(i,i+1).equals("y"))skipletters=true;
		}
		if (!skipletters) {
			for (int i = 0; i < 5; i++) {
				if (serial.substring(i,i+1).equals("r")||serial.substring(i,i+1).equals("o"))rock++;
				if (serial.substring(i,i+1).equals("p")||serial.substring(i,i+1).equals("a"))paper++;
				if (serial.substring(i,i+1).equals("s")||serial.substring(i,i+1).equals("i"))scissors++;
				if (serial.substring(i,i+1).equals("l")||serial.substring(i,i+1).equals("z"))lizard++;
				if (serial.substring(i,i+1).equals("c")||serial.substring(i,i+1).equals("k"))spock++;
			}
			if (rock>paper&&rock>scissors&&rock>lizard&&rock>spock&&decoy!=0)answer="rock";
			if (paper>rock&&paper>scissors&&paper>lizard&&paper>spock&&decoy!=1)answer="paper";
			if (scissors>paper&&scissors>rock&&scissors>lizard&&scissors>spock&&decoy!=2)answer="scissors";
			if (lizard>paper&&lizard>scissors&&lizard>rock&&lizard>spock&&decoy!=3)answer="lizard";
			if (spock>paper&&spock>scissors&&spock>lizard&&spock>rock&&decoy!=4)answer="spock";
		}
		
		rock = 0;
		paper=0;
		scissors=0;
		lizard=0;
		spock = 0;
		
		if (!ports.contains("ps2")&&answer.equals("")) {
			for (String port : ports) {
				if (port.equals("rj45"))rock++;
				if (port.equals("parallel"))paper++;
				if (port.equals("serial"))scissors++;
				if (port.equals("dvi"))lizard++;
				if (port.equals("rca"))spock++;
			}
			if (rock>paper&&rock>scissors&&rock>lizard&&rock>spock&&decoy!=0)answer="rock";
			if (paper>rock&&paper>scissors&&paper>lizard&&paper>spock&&decoy!=1)answer="paper";
			if (scissors>paper&&scissors>rock&&scissors>lizard&&scissors>spock&&decoy!=2)answer="scissors";
			if (lizard>paper&&lizard>scissors&&lizard>rock&&lizard>spock&&decoy!=3)answer="lizard";
			if (spock>paper&&spock>scissors&&spock>lizard&&spock>rock&&decoy!=4)answer="spock";
		}
		
		rock = 0;
		paper=0;
		scissors=0;
		lizard=0;
		spock = 0;
		
		if (!indicators.contains("TRN*")&&answer.equals("")) {
			for (String ind : indicators) {
				if (ind.equals("FRK*")||ind.equals("FRQ*"))rock++;
				if (ind.equals("BOB*")||ind.equals("IND*"))paper++;
				if (ind.equals("CAR*")||ind.equals("SIG*"))scissors++;
				if (ind.equals("CLR*")||ind.equals("NSA*"))lizard++;
				if (ind.equals("SND*")||ind.equals("MSA*"))spock++;
			}
			if (rock>paper&&rock>scissors&&rock>lizard&&rock>spock&&decoy!=0)answer="rock";
			if (paper>rock&&paper>scissors&&paper>lizard&&paper>spock&&decoy!=1)answer="paper";
			if (scissors>paper&&scissors>rock&&scissors>lizard&&scissors>spock&&decoy!=2)answer="scissors";
			if (lizard>paper&&lizard>scissors&&lizard>rock&&lizard>spock&&decoy!=3)answer="lizard";
			if (spock>paper&&spock>scissors&&spock>lizard&&spock>rock&&decoy!=4)answer="spock";
		}
		
		rock = 0;
		paper=0;
		scissors=0;
		lizard=0;
		spock = 0;
		
		if (!indicators.contains("TRN")&&answer.equals("")) {
			for (String ind : indicators) {
				if (ind.equals("FRK")||ind.equals("FRQ"))rock++;
				if (ind.equals("BOB")||ind.equals("IND"))paper++;
				if (ind.equals("CAR")||ind.equals("SIG"))scissors++;
				if (ind.equals("CLR")||ind.equals("NSA"))lizard++;
				if (ind.equals("SND")||ind.equals("MSA"))spock++;
			}
			if (rock>paper&&rock>scissors&&rock>lizard&&rock>spock&&decoy!=0)answer="rock";
			if (paper>rock&&paper>scissors&&paper>lizard&&paper>spock&&decoy!=1)answer="paper";
			if (scissors>paper&&scissors>rock&&scissors>lizard&&scissors>spock&&decoy!=2)answer="scissors";
			if (lizard>paper&&lizard>scissors&&lizard>rock&&lizard>spock&&decoy!=3)answer="lizard";
			if (spock>paper&&spock>scissors&&spock>lizard&&spock>rock&&decoy!=4)answer="spock";
		}
		
		rock = 0;
		paper=0;
		scissors=0;
		lizard=0;
		spock = 0;
		
		if (answer.equals("")) {
			for (int i = 0; i < 5; i++) {
				if (serial.substring(i,i+1).equals("0")||serial.substring(i,i+1).equals("5"))rock++;
				if (serial.substring(i,i+1).equals("3")||serial.substring(i,i+1).equals("6"))paper++;
				if (serial.substring(i,i+1).equals("1")||serial.substring(i,i+1).equals("9"))scissors++;
				if (serial.substring(i,i+1).equals("2")||serial.substring(i,i+1).equals("8"))lizard++;
				if (serial.substring(i,i+1).equals("4")||serial.substring(i,i+1).equals("7"))spock++;
			}
			if (rock>paper&&rock>scissors&&rock>lizard&&rock>spock&&decoy!=0)answer="rock";
			if (paper>rock&&paper>scissors&&paper>lizard&&paper>spock&&decoy!=1)answer="paper";
			if (scissors>paper&&scissors>rock&&scissors>lizard&&scissors>spock&&decoy!=2)answer="scissors";
			if (lizard>paper&&lizard>scissors&&lizard>rock&&lizard>spock&&decoy!=3)answer="lizard";
			if (spock>paper&&spock>scissors&&spock>lizard&&spock>rock&&decoy!=4)answer="spock";
		}
		
		if (answer.equals("rock"))System.out.println("Press paper and spock.");
		if (answer.equals("paper"))System.out.println("Press scissors and lizard.");
		if (answer.equals("scissors"))System.out.println("Press spock and rock.");
		if (answer.equals("lizard"))System.out.println("Press rock and scissors.");
		if (answer.equals("spock"))System.out.println("Press lizard and paper.");
		if (answer.equals(""))System.out.println("Press all except "+decoy);
	}
	public void ColorFlash() {
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> colors = new ArrayList<String>();
		System.out.println("Enter the WORDS first. First three letters only.");
		while (words.size()<8) {
			System.out.print("Enter next word: ");
			String word = input.next().toLowerCase();
			if (word.length() != 3)System.out.println("Incorrect word length.");
			else words.add(word);
		}
		System.out.println("Words received, repeat for colors.");
		
		while (colors.size()<8) {
			System.out.print("Enter next color: ");
			String color = input.next().toLowerCase();
			if (color.length() != 3)System.out.println("Incorrect color length.");
			else colors.add(color);
		}
		
		
		boolean blueInGreen = false;
		boolean whiteInWhiteOrRed = false;
		int countOfMagenta = 0;
		boolean consecWordDiffColor = false;
		int wordnomatchcolor = 0;
		boolean redInYellowOrYellowInWhite = false;
		boolean consecColorDiffWord = false;
		int countOfWordYellow = 0;
		int countOfColorBlue = 0;
		boolean yellowInRed = false;
		
		for (int i = 0 ; i <8; i++) {
			if (words.get(i).equals("blu")&&colors.get(i).equals("gre"))blueInGreen=true;
			if (words.get(i).equals("red")&&colors.get(i).equals("yel"))redInYellowOrYellowInWhite=true;
			if (words.get(i).equals("yel")&&colors.get(i).equals("whi"))redInYellowOrYellowInWhite=true;
			if (words.get(i).equals("whi")&&(colors.get(i).equals("whi")||colors.get(i).equals("red")))whiteInWhiteOrRed=true;
			if (words.get(i).equals("mag")||colors.get(i).equals("mag"))countOfMagenta++;
			if (words.get(i).equals("yel")&&colors.get(i).equals("red"))yellowInRed=true;
			if (i < 7) {
				if (words.get(i).equals(words.get(i+1))&&!colors.get(i).equals(colors.get(i+1)))consecWordDiffColor=true;
				if (colors.get(i).equals(colors.get(i+1))&&!words.get(i).equals(words.get(i+1)))consecColorDiffWord=true;
			}
			if (!words.get(i).equals(colors.get(i)))wordnomatchcolor++;
			if (words.get(i).equals("yel"))countOfWordYellow++;
			if (colors.get(i).equals("blu"))countOfColorBlue++;
		}
		
		
		if (colors.get(7).equals("red")) {
			if (countOf(words, "gre")>2)System.out.println("Yes on the third time Green is used as either the word or the color of the word in the sequence.");
			else if (countOf(colors, "blu")==1)System.out.println("No on the word Magenta");
			else System.out.println("Yes on last white (either word or color)");
		}
		if (colors.get(7).equals("yel")) {
			if (blueInGreen)System.out.println("Yes on first time green is color of word");
			else if (whiteInWhiteOrRed)System.out.println("Yes on second time where color != word");
			else System.out.println("No on word "+countOfMagenta);
		}
		if (colors.get(7).equals("gre")) {
			if (consecWordDiffColor)System.out.println("No on word 5");
			else if (countOfMagenta>2)System.out.println("No on first yellow (either word or color)");
			else System.out.println("Yes on any entry where color matches word.");
		}
		if (colors.get(7).equals("blu")) {
			if (wordnomatchcolor>2)System.out.println("Yes on first time where color != word");
			else if (redInYellowOrYellowInWhite)System.out.println("No on white in red");
			else System.out.println("Yes on last green (word or color)");
		}
		if (colors.get(7).equals("mag")) {
			if (consecColorDiffWord)System.out.println("Yes on entry 3");
			else if (countOfWordYellow>countOfColorBlue)System.out.println("No on last word yellow");
			else System.out.println("No first time color is "+words.get(6));
		}
		if (colors.get(7).equals("whi")) {
			if (colors.get(2).equals(words.get(3))||colors.get(2).equals(words.get(4)))System.out.println("No on first blue (color or word)");
			else if (yellowInRed)System.out.println("Yes on last color blue");
			else System.out.println("NO");
		}
		
	}
	public void ShapeShift() {
		System.out.println("Enter your starting shape no spaces. round,ticket,flat,point");
		String current = input.next();
		String answer = "";
		ArrayList<String> visited = new ArrayList<String>();
		while (answer.equals("")) {
			visited.add(current);
			if (countOf(visited,current)>1)answer=current;
			if (current.equals("flatround"))if(ports.contains("dvi"))current="roundpoint";else current="ticketpoint";
			else if (current.equals("roundpoint"))if(indicators.contains("SIG*"))current="ticketticket";else current="flatflat";
			else if (current.equals("ticketpoint"))if(ports.contains("ps2"))current="pointticket";else current="pointround";
			else if (current.equals("ticketticket"))if(batteries>2)current="ticketflat";else current="flatticket";
			else if (current.equals("flatflat"))if(lastDigit%2==1)current="pointflat";else current="pointround";
			else if (current.equals("pointticket"))if(ports.contains("rj45"))current="roundpoint";else current="roundround";
			else if (current.equals("pointround"))if(ports.contains("parallel"))current="flatpoint";else current="roundflat";
			else if (current.equals("ticketflat"))if(indicators.contains("FRQ"))current="flatround";else current="pointpoint";
			else if (current.equals("flatticket"))if(indicators.contains("BOB"))current="ticketround";else current="pointpoint";
			else if (current.equals("pointflat"))if(indicators.contains("CAR"))current="ticketround";else current="roundticket";
			else if (current.equals("roundround"))if(vowel)current="ticketticket";else current="flatround";
			else if (current.equals("flatpoint"))if(indicators.contains("MSA*"))current="ticketpoint";else current="pointflat";
			else if (current.equals("roundflat"))if(indicators.contains("SND*"))current="roundround";else current="roundticket";
			else if (current.equals("pointpoint"))if(indicators.contains("IND*"))current="pointticket";else current="flatflat";
			else if (current.equals("roundticket"))if(batteries-holders>0)current="ticketflat";else current="flatticket";
			else if (current.equals("ticketround"))if(ports.contains("rca"))current="flatpoint";else current="roundflat";
		}
		System.out.println(answer);
	}
	public void BurglarAlarm() {
		System.out.print("Enter your burgle number: ");
		ArrayList<Integer> moduleNumber = new ArrayList<Integer>();
		int moduleUnfiltered = input.nextInt();
		int sum = 0;
		while (moduleNumber.size()<8) {
			sum+=moduleUnfiltered%10;
			moduleNumber.add(0,moduleUnfiltered%10);
			moduleUnfiltered/=10;
		}
		System.out.print("Number of solved modules: ");
		int solvedModules = input.nextInt();
		if (portPlates==-1) {
			System.out.print("Port plates: ");
			portPlates = input.nextInt();
		}
		boolean serialBurgle = false;
		boolean serialAlarm = false;
		
		int letters = 0;
		for (int i = 0; i<6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				if (x==1||x==4)serialBurgle=true;
				if (x==5||x==3)serialAlarm=true;
			}
			catch(NumberFormatException e) {
				letters++;
				String j =serial.substring(i,i+1);
				if (j.equals("b")||j.equals("u")||j.equals("r")||j.equals("g"))serialBurgle=true;
				if (j.equals("a")||j.equals("l")||j.equals("m"))serialAlarm=true;
			}
		}
		
		ArrayList<Integer> burgleNumber = new ArrayList<Integer>();
		//Number 1
		if (batteries>ports.size()) {
			if (holders%2==0)burgleNumber.add(9);
			else burgleNumber.add(1);
		}
		else {
			if (moduleNumber.get(7)%2==0)burgleNumber.add(3);
			else burgleNumber.add(4);
		}
		//Number 2
		if (ports.contains("ps2")) {
			if (letters>3)burgleNumber.add(0);
			else burgleNumber.add(6);
		}
		else {
			if (indicators.contains("BOB*"))burgleNumber.add(5);
			else burgleNumber.add(2);
		}
		//Number 3
		if (solvedModules%2==0) {
			if (moduleNumber.get(2)%2==0)burgleNumber.add(8);
			else burgleNumber.add(4);
		}
		else {
			if (ports.contains("rj45"))burgleNumber.add(9);
			else burgleNumber.add(3);
		}
		//Number 4
		if (sum%2==1) {
			if (portPlates>indicators.size())burgleNumber.add(7);
			else burgleNumber.add(3);
		}
		else {
			if ((holders-(batteries-holders))>(batteries-holders)*2)burgleNumber.add(7);
			else burgleNumber.add(2);
		}
		//Number 5
		if (solvedModules>(batteries*portPlates)) {
			if (ports.size()%2==0)burgleNumber.add(9);
			else burgleNumber.add(3);
		}
		else {
			if (ports.size()>indicators.size())burgleNumber.add(7);
			else burgleNumber.add(8);
		}
		//Number 6
		if (ports.contains("parallel")) {
			if (ports.contains("serial"))burgleNumber.add(1);
			else burgleNumber.add(5);
		}
		else {
			if (indicators.contains("FRQ*"))burgleNumber.add(0);
			else burgleNumber.add(4);
		}
		//Number 7
		boolean noUnlit = true;
		for (String ind : indicators) {
			if (ind.length()<4)noUnlit=false;
		}
		if (batteries>4) {
			if (noUnlit)burgleNumber.add(2);
			else burgleNumber.add(6);
		}
		else {
			if (noLitIndicators())burgleNumber.add(4);
			else burgleNumber.add(9);
		}
		//Number 8
		if (batteries == indicators.size()) {
			if (serialBurgle)burgleNumber.add(1);
			else burgleNumber.add(0);
		}
		else {
			if (serialAlarm)burgleNumber.add(0);
			else burgleNumber.add(8);
		}
		for (int i = 0; i < 8; i++) {
			burgleNumber.add(i,burgleNumber.get(i)+moduleNumber.get(i));
			burgleNumber.remove(i+1);
		}
		
		String enter = "";
		for (int i : burgleNumber) {
			if (debug)System.out.println(i%10);
			enter+=i%10;
		}
		System.out.println(enter);
	}
	public void HorribleMemory() {
		System.out.println("For each stage, enter display number. Then enter all of the numbered buttons. Then enter all of the colors.");
		System.out.println("Colors are m, p, r, b, o, g");
		System.out.println("Stage 1 display: ");
		int s1display = input.nextInt();
		System.out.println("Received. Stage 1 numbers: ");
		int s1numbersint = input.nextInt();
		System.out.println("Received. Stage 1 colors: ");
		String s1colorsstring = input.next().toLowerCase();
		ArrayList<Integer> s1numbers = new ArrayList<Integer>();
		ArrayList<String> s1colors = new ArrayList<String>();
		for (int i = 1; i<7; i++) {
			int j = (int) (s1numbersint/Math.pow(10, 6-i))%10;
			s1numbers.add(j);
			s1colors.add(s1colorsstring.substring(i-1,i));
		}
		
		int label1=-1;
		int pos1=-1;;
		String color1 = "Error";
		
		if (s1display==1) {
			label1=6;
			pos1=s1numbers.indexOf(label1)+1;
			color1=s1colors.get(pos1-1);
			
		}
		if (s1display==2) {
			pos1=1;
			label1=s1numbers.get(pos1-1);
			color1=s1colors.get(pos1-1);

		}
		if (s1display==3) {
			color1="g";
			pos1=s1colors.indexOf(color1)+1;
			label1=s1numbers.get(pos1-1);
		}
		if (s1display==4) {
			pos1=3;
			label1=s1numbers.get(pos1-1);
			color1=s1colors.get(pos1-1);
		}
		if (s1display==5) {
			label1=2;
			pos1=s1numbers.indexOf(label1)+1;
			color1=s1colors.get(pos1-1);
		}
		if (s1display==6) {
			color1="o";
			pos1=s1colors.indexOf(color1)+1;
			label1=s1numbers.get(pos1-1);
		}
		System.out.println("Press "+label1);
		
		System.out.println("Stage 2 display: ");
		int s2display = input.nextInt();
		System.out.println("Received. Stage 2 numbers: ");
		int s2numbersint = input.nextInt();
		System.out.println("Received. Stage 2 colors: ");
		String s2colorsstring = input.next().toLowerCase();
		ArrayList<Integer> s2numbers = new ArrayList<Integer>();
		ArrayList<String> s2colors = new ArrayList<String>();
		for (int i = 1; i<7; i++) {
			int j = (int) (s2numbersint/Math.pow(10, 6-i))%10;
			s2numbers.add(j);
			s2colors.add(s2colorsstring.substring(i-1,i));
		}
		int label2=-1;
		int pos2=-1;;
		String color2 = "Error";
		
		if (s2display==1) {
			pos2=pos1;
			label2=s2numbers.get(pos2-1);
			color2=s2colors.get(pos2-1);
		}
		if (s2display==2) {
			color2="p";
			pos2=s2colors.indexOf(color2)+1;
			label2=s2numbers.get(pos2-1);
		}
		if (s2display==3) {
			label2=1;
			pos2=s2numbers.indexOf(label2)+1;
			color2=s2colors.get(pos2-1);
		}
		if (s2display==4) {
			label2=label1;
			pos2=s2numbers.indexOf(label2)+1;
			color2=s2colors.get(pos2-1);
		}
		if (s2display==5) {
			pos2=6;
			label2=s2numbers.get(pos2-1);
			color2=s2colors.get(pos2-1);
		}
		if (s2display==6) {
			color2=color1;
			pos2=s2colors.indexOf(color2)+1;
			label2=s2numbers.get(pos2-1);
		}
		System.out.println("Press "+label2);
		
		System.out.println("Stage 3 display: ");
		int s3display = input.nextInt();
		System.out.println("Received. Stage 3 numbers: ");
		int s3numbersint = input.nextInt();
		System.out.println("Received. Stage 3 colors: ");
		String s3colorsstring = input.next().toLowerCase();
		ArrayList<Integer> s3numbers = new ArrayList<Integer>();
		ArrayList<String> s3colors = new ArrayList<String>();
		for (int i = 1; i<7; i++) {
			int j = (int) (s3numbersint/Math.pow(10, 6-i))%10;
			s3numbers.add(j);
			s3colors.add(s3colorsstring.substring(i-1,i));
		}
		int label3=-1;
		int pos3=-1;;
		String color3 = "Error";
		
		if (s3display==1) {
			label3=s1numbers.get(3);
			pos3=s3numbers.indexOf(label3)+1;
			color3=s3colors.get(pos3-1);
		}
		if (s3display==2) {
			pos3=s2colors.indexOf("g")+1;
			label3=s3numbers.get(pos3-1);
			color3=s3colors.get(pos3-1);
		}
		if (s3display==3) {
			color3=s2colors.get(s2numbers.indexOf(5));
			pos3=s3colors.indexOf(color3)+1;
			label3=s3numbers.get(pos3-1);
		}
		if (s3display==4) {
			label3=s1numbers.get(0);
			pos3=s3numbers.indexOf(label3)+1;
			color3=s3colors.get(pos3-1);
		}
		if (s3display==5) {
			pos3=pos2;
			label3=s3numbers.get(pos3-1);
			color3=s3colors.get(pos3-1);
		}
		if (s3display==6) {
			color3=s1colors.get(2);
			pos3=s3colors.indexOf(color3)+1;
			label3=s3numbers.get(pos3-1);
		}
		System.out.println("Press "+label3);
		
		System.out.println("Stage 4 display: ");
		int s4display = input.nextInt();
		System.out.println("Received. Stage 4 numbers: ");
		int s4numbersint = input.nextInt();
		System.out.println("Received. Stage 4 colors: ");
		String s4colorsstring = input.next().toLowerCase();
		ArrayList<Integer> s4numbers = new ArrayList<Integer>();
		ArrayList<String> s4colors = new ArrayList<String>();
		for (int i = 1; i<7; i++) {
			int j = (int) (s4numbersint/Math.pow(10, 6-i))%10;
			s4numbers.add(j);
			s4colors.add(s4colorsstring.substring(i-1,i));
		}
		int label4=-1;
		int pos4=-1;;
		String color4 = "Error";
		
		if (s4display==1) {
			pos4=s1numbers.indexOf(2)+1;
			label4=s4numbers.get(pos4-1);
			color4=s4colors.get(pos4-1);
		}
		if (s4display==2) {
			label4=s3numbers.get(1);
			pos4=s4numbers.indexOf(label4)+1;
			color4=s4colors.get(pos4-1);
		}
		if (s4display==3) {
			color4=color2;
			pos4=s4colors.indexOf(color4)+1;
			label4=s4numbers.get(pos4-1);
		}
		if (s4display==4) {
			pos4=pos3;
			label4=s4numbers.get(pos4-1);
			color4=s4colors.get(pos4-1);
		}
		if (s4display==5) {
			color4=s1colors.get(s1numbers.indexOf(4));
			pos4=s4colors.indexOf(color4)+1;
			label4=s4numbers.get(pos4-1);
		}
		if (s4display==6) {
			label4=s3numbers.get(5);
			pos4=s4numbers.indexOf(label4)+1;
			color4=s4colors.get(pos4-1);
		}
		System.out.println("Press "+label4);
		
		System.out.println("Stage 5 display: ");
		int s5display = input.nextInt();
		System.out.println("Received. Stage 5 numbers: ");
		int s5numbersint = input.nextInt();
		System.out.println("Received. Stage 5 colors: ");
		String s5colorsstring = input.next().toLowerCase();
		ArrayList<Integer> s5numbers = new ArrayList<Integer>();
		ArrayList<String> s5colors = new ArrayList<String>();
		for (int i = 1; i<7; i++) {
			int j = (int) (s5numbersint/Math.pow(10, 6-i))%10;
			s5numbers.add(j);
			s5colors.add(s5colorsstring.substring(i-1,i));
		}
		int label5=-1;
		int pos5=-1;;
		String color5 = "Error";
		
		if (s5display==1) {
			color5=s4colors.get(2);
			pos5=s5colors.indexOf(color5)+1;
			label5=s5numbers.get(pos5-1);
		}
		if (s5display==2) {
			pos5=s3numbers.indexOf(6)+1;
			label5=s5numbers.get(pos5-1);
			color5=s5colors.get(pos5-1);
		}
		if (s5display==3) {
			label5=label4;
			pos5=s5numbers.indexOf(label5)+1;
			color5=s5colors.get(pos5-1);
		}
		if (s5display==4) {
			label5=s1numbers.indexOf(s1colors.indexOf("r"));
			pos5=s5numbers.indexOf(label5)+1;
			color5=s5colors.get(pos5-1);
		}
		if (s5display==5) {
			color5=color3;
			pos5=s5colors.indexOf(color5)+1;
			label5=s5numbers.get(pos5-1);
		}
		if (s5display==6) {
			pos5=s2colors.indexOf("b")+1;
			label5=s5numbers.get(pos5-1);
			color5=s5colors.get(pos5-1);
		}
		System.out.println("Press "+label5);
	}
	public void SimonScreams() {
		ArrayList<String> order = new ArrayList<String>();
		ArrayList<String> sequence = new ArrayList<String>();
		ArrayList<Integer> sequenceint = new ArrayList<Integer>();
		String[][] rules = {{"FFC", "CEH", "HAF", "ECD", "DDE", "AHA"},
							{"AHF", "DFC", "ECH", "CDE", "FEA", "HAD"},
							{"DED", "ECF", "FHE", "HAA", "AFH", "CDC"},
							{"HCE", "ADA", "CFD", "DHH", "EAC", "FEF"},
							{"CAH", "FHD", "DDA", "AEC", "HCF", "EFE"},
							{"EDA", "HAE", "AEC", "FFF", "CHD", "DCH"}};
		String[][] colorPress = {{"Y", "P", "O", "G", "R", "B"},
								 {"O", "Y", "G", "B", "P", "R"},
								 {"G", "R", "B", "O", "Y", "P"},
								 {"R", "B", "P", "Y", "O", "G"},
								 {"B", "O", "R", "P", "G", "Y"},
								 {"P", "G", "Y", "R", "B", "O"}};		
		System.out.println("Please enter the order of colors (first letter) around the star. CLOCKWISE please");
		System.out.println("ENTER 1 STRING WITH ALL COLORS e.g. roygbp");
		String orderString = input.next().toLowerCase();
		System.out.println("Recieved, please enter sequence");
		String sequenceString = input.next().toLowerCase();
		for (int i = 0; i < 6; i++) {
			order.add(orderString.substring(i, i+1));
			if (i<sequenceString.length())sequence.add(sequenceString.substring(i, i+1));
		}
		
		ArrayList<Integer> appliedRules = new ArrayList<Integer>();
		if (indicators.size()>=3)appliedRules.add(0);
		if (ports.size()>=3)appliedRules.add(1);
		int letters = 0;for (int i = 0; i<6; i++) {try {@SuppressWarnings("unused")
		int x = Integer.parseInt(serial.substring(i,i+1));}catch(NumberFormatException e) {letters++;}}
		if (letters<4)appliedRules.add(2);
		if (letters>2)appliedRules.add(3);
		if (batteries>2)appliedRules.add(4);
		if (holders>2)appliedRules.add(5);
		boolean red = false;
		boolean blue = false;
		boolean yellow = false;
		for (int stage = 1; stage < 4; stage++) {
			for (String j : sequence) {
				if (j.equals("y"))yellow = true;
				if (j.equals("b"))blue = true;
				if (j.equals("r"))red = true;
				sequenceint.add(order.indexOf(j));
			}
			
			boolean threeclockwise = false;
			boolean twoclockwise = false;
			boolean zerothree = false;
			boolean onefour = false;
			boolean twofive = false;
			boolean repeatadjacent=false;
			for (int i=0; i<sequenceint.size(); i++) {
				int j = i+1;
				int k = i+2;
				if (!(j>=sequenceint.size()))if (sequenceint.get(j)-sequenceint.get(i)==1||sequenceint.get(j)-sequenceint.get(i)==-5)twoclockwise=true;
				if (!(k>=sequenceint.size())) {
					if ((sequenceint.get(j)-sequenceint.get(i)==1||sequenceint.get(j)-sequenceint.get(i)==-5)&&(sequenceint.get(k)-sequenceint.get(j)==1||sequenceint.get(k)-sequenceint.get(j)==-5))threeclockwise=true;
					if ((Math.abs(sequenceint.get(j)-sequenceint.get(i))==1||Math.abs(sequenceint.get(j)-sequenceint.get(i))==5)&&(sequenceint.get(k)==sequenceint.get(i))) {
						
						repeatadjacent=true;
					}
				}
				if (sequenceint.get(i)==0||sequenceint.get(i)==3)zerothree=true;
				if (sequenceint.get(i)==1||sequenceint.get(i)==4)onefour=true;
				if (sequenceint.get(i)==2||sequenceint.get(i)==5)twofive=true;
				
			}
			int ruletouse = 5;
			if (threeclockwise) {
				ruletouse=0;
				System.out.println("3clockwise");
			}
			else if (repeatadjacent) {
				ruletouse=1;
				System.out.println("repeat adj");
			}
			else if (!(blue&&yellow)&&!(red&&yellow)&&!(red&&blue)) {
				ruletouse=2;
				System.out.println("primaries");
			}
			else if (!(zerothree&&onefour&&twofive)) {
				ruletouse=3;
				System.out.println("opposites");
			}
			else if (twoclockwise) {
				ruletouse=4;
				System.out.println("2clockwise");
			}
			
			int stagecolor = -1;
			switch (sequence.get(stage-1)) {
			case "r":
				stagecolor=0;
				break;
			case "o":
				stagecolor=1;
				break;
			case "y":
				stagecolor=2;
				break;
			case "g":
				stagecolor=3;
				break;
			case "b":
				stagecolor=4;
				break;
			case "p":
				stagecolor=5;
				break;
			}
			String rulesToApply = rules[ruletouse][stagecolor].substring(stage-1, stage);
			int goshdarn = -1;
			switch (rulesToApply) {
			case "A":
				goshdarn=0;
				break;
			case "C":
				goshdarn=1;
				break;
			case "D":
				goshdarn=2;
				break;
			case "E":
				goshdarn=3;
				break;
			case "F":
				goshdarn=4;
				break;
			case "H":
				goshdarn=5;
				break;
			}
			for (int i: appliedRules) {
				System.out.print(colorPress[goshdarn][i]+" ");
			}
			if (stage!=3) {
			System.out.println();
			System.out.println("Enter entire sequence or stop");
			sequenceString = input.next();
			sequence.clear();
			sequenceint.clear();
			for (int i = 0; i<sequenceString.length(); i++)sequence.add(sequenceString.substring(i, i+1));
			}
			}
		
	}
	public void ColoredSquares() {
		int squaresColored = 0;
		String prevPressed = "";
		String[][] table = {{"Blue", "Column", "Red", "Yellow", "Row", "Green", "Magenta"},
							{"Row", "Green", "Blue", "Magenta", "Red", "Column", "Yellow"},
							{"Yellow", "Magenta", "Green", "Row", "Blue", "Red", "Column"},
							{"Blue", "Green", "Yellow", "Column", "Red", "Row", "Magenta"},
							{"Yellow", "Row", "Blue", "Magenta", "Column", "Red", "Green"},
							{"Magenta", "Red", "Yellow", "Green", "Column", "Blue", "Row"},
							{"Green", "Row", "Column", "Blue", "Magenta", "Yellow", "Red"},
							{"Magenta", "Red", "Green", "Blue", "Yellow", "Column", "Row"},
							{"Column", "Yellow", "Red", "Green", "Row", "Magenta", "Blue"},
							{"Green", "Column", "Row", "Red", "Magenta", "Blue", "Yellow"},
							{"Red", "Yellow", "Row", "Column", "Green", "Magenta", "Blue"},
							{"Column", "Blue", "Magenta", "Red", "Yellow", "Row", "Green"},
							{"Row", "Magenta", "Column", "Yellow", "Blue", "Green", "Red"},
							{"Red", "Blue", "Magenta", "Row", "Green", "Yellow", "Column"},
							{"Column", "Row", "Column", "Row", "Column", "Row", "Column"}};
			
		System.out.println("Press the color group containing fewest squares.");
		System.out.println("Which color did you press? (whole word)");
		prevPressed=input.next().toLowerCase();
		prevPressed=prevPressed.substring(0,1).toUpperCase()+prevPressed.substring(1);
		System.out.print("How many squares are lit?");
		squaresColored = input.nextInt();
		while (squaresColored < 16) {
			int col = -1;
			switch (prevPressed) {
			case "Red":
				col=0;
				break;
			case "Blue":
				col=1;
				break;
			case "Green":
				col=2;
				break;
			case "Yellow":
				col=3;
				break;
			case "Magenta":
				col=4;
				break;
			case "Row":
				col=5;
				break;
			case "Column":
				col=6;
				break;
			}
			prevPressed = table[squaresColored-1][col];
			System.out.println("Press "+prevPressed);
			System.out.println("How many squares are lit?");
			squaresColored=input.nextInt();
		}
	}
	public void TheBulb() {

		System.out.println("What is your bulb's color? (first letter)");
		String bulbcolor  = input.next().toLowerCase().substring(0,1);
		System.out.println("Is your bulb translucent? y/n?");
		String translucent = input.next().toLowerCase().substring(0,1);
		System.out.println("0 if bulb off, 1 if bulb on");
		int state = input.nextInt();
		ArrayList<Boolean> steps = new ArrayList<Boolean>();
		for (int i = 0; i < 15; i++)steps.add(false);
		String remember = "";
		steps.set(0, true);
		if (steps.get(0)) {
			if (state==1) {
				if (translucent.equals("y")) {
					System.out.println("Press I");
					steps.set(1, true);
				}
				else {
					System.out.println("Press O");
					steps.set(2, true);
				}
			}
			else {
				System.out.println("Unscrew bulb.");
				steps.set(3, true);
			}
		}
		if (steps.get(1)) {
			if (bulbcolor.equals("r")) {
				System.out.println("Press I, then unscrew.");
				steps.set(4, true);
			}
			else if (bulbcolor.equals("w")) {
				System.out.println("Press O, then unscrew.");
				steps.set(5, true);
			}
			else {
				System.out.println("Unscrew bulb.");
				steps.set(6, true);
			}
		}
		if (steps.get(2)) {
			if (bulbcolor.equals("g")) {
				System.out.println("Press I, then unscrew.");
				steps.set(5, true);
			}
			else if (bulbcolor.equals("p")) {
				System.out.println("Press O, then unscrew.");
				steps.set(4, true);
			}
			else {
				System.out.println("Unscrew bulb.");
				steps.set(7, true);
			}
		}
		if (steps.get(3)) {
			if (indicators.contains("CAR")||indicators.contains("CAR*")||indicators.contains("IND")||indicators.contains("IND*")||indicators.contains("MSA")||indicators.contains("MSA*")||indicators.contains("SND")||indicators.contains("SND*")) {
				System.out.println("Press I");
				steps.set(8, true);
			}
			else {
				System.out.println("Press O");
				steps.set(9, true);
			}
		}
		if (steps.get(4)) {
			System.out.println("If bulb went off during first instruction, press same button. Then screw in.");
			System.out.println("Else, press diff button, then screw in.");
		}
		if (steps.get(5)) {
			System.out.println("If bulb went off when I pressed, press first button pressed, then screw in.");
			System.out.println("Else, press second or third button, then screw in.");
		}
		if (steps.get(6)) {
			if (bulbcolor.equals("g")) {
				System.out.println("Press I");
				steps.set(10, true);
				remember = "SIG";
			}
			else if (bulbcolor.equals("p")) {
				System.out.println("Press I, then screw in.");
				steps.set(11, true);
			}
			else if (bulbcolor.equals("b")) {
				System.out.println("Press O");
				steps.set(10, true);
				remember = "CLR";
			}
			else {
				System.out.println("Press O, then screw in.");
				steps.set(12, true);
			}
		}
		if (steps.get(7)) {
			if (bulbcolor.equals("w")) {
				System.out.println("Press I");
				steps.set(10, true);
				remember = "FRQ";
			}
			else if (bulbcolor.equals("r")) {
				System.out.println("Press I, then screw in.");
				steps.set(12, true);
			}
			else if (bulbcolor.equals("y")) {
				System.out.println("Press O");
				steps.set(10, true);
				remember = "FRK";
			}
			else {
				System.out.println("Press O, then screw in.");
				steps.set(11, true);
			}
		}
		if (steps.get(8)) {
			if (bulbcolor.equals("b")) {
				System.out.println("Press I");
				steps.set(13, true);
			}
			else if (bulbcolor.equals("g")) {
				System.out.println("Press I, screw in.");
				steps.set(11, true);
			}
			else if (bulbcolor.equals("y")) {
				System.out.println("Press O");
				steps.set(14, true);
			}
			else if (bulbcolor.equals("w")) {
				System.out.println("Press O, screw in");
				steps.set(12, true);
			}
			else if (bulbcolor.equals("p")) {
				System.out.println("Screw in, press I");
				steps.set(11, true);
			}
			else {
				System.out.println("Screw in, press O");
				steps.set(12, true);
			}
		}
		if (steps.get(9)) {
			if (bulbcolor.equals("p")) {
				System.out.println("Press I");
				steps.set(13, true);
			}
			else if (bulbcolor.equals("r")) {
				System.out.println("Press I, screw in.");
				steps.set(12, true);
			}
			else if (bulbcolor.equals("b")) {
				System.out.println("Press O");
				steps.set(14, true);
			}
			else if (bulbcolor.equals("y")) {
				System.out.println("Press O, screw in");
				steps.set(11, true);
			}
			else if (bulbcolor.equals("g")) {
				System.out.println("Screw in, press I");
				steps.set(12, true);
			}
			else {
				System.out.println("Screw in, press O");
				steps.set(11, true);
			}
		}
		if (steps.get(10)) {
			if (indicators.contains(remember)||indicators.contains(remember+"*")) {
				System.out.println("Press I, screw in");
			}
			else {
				System.out.println("Press O, screw in");
			}
		}
		if (steps.get(11)) {
			System.out.println("If light on, press I. Else press O");
		}
		if (steps.get(12)) {
			System.out.println("If light on, press O. Else press I");
		}
		if (steps.get(13)) {
			if (translucent.equals("n")) {
				System.out.println("Press I, screw in");
			}
			else {
				System.out.println("Press O, screw in");
			}
		}
		if (steps.get(14)) {
			if (translucent.equals("y")) {
				System.out.println("Press I, screw in");
			}
			else {
				System.out.println("Press O, screw in");
			}
		}
	}
	public void Painting() {
		String blindness = "";
		int j = batteries+indicators.size()+ports.size()+2;
		ArrayList<String> uniqueLettersIndicators = new ArrayList<String>();
		if (j==11) {
			blindness="prota";
		}
		if (j==13) {
			blindness="deuter";
		}
		if (j==10) {
			blindness="trita";
		}
		if (blindness.equals("")) {
			for (String ind : indicators) {
				for (int i = 0; i <3; i++) {
					String character = ind.substring(i,i+1);
					if (!uniqueLettersIndicators.contains(character))uniqueLettersIndicators.add(character);
				}
			}
			int deuterCount = 0;
			int tritaCount = 0;
			int protaCount = 0;
			for (String l : uniqueLettersIndicators) {
				deuterCount+=countOf("DEUTERANOMALY",l);
				protaCount+=countOf("PROTANOMALY",l);
				tritaCount+=countOf("TRITANOPIA",l);
			}
			if (deuterCount>protaCount&&deuterCount>tritaCount)blindness="deuter";
			if (protaCount>deuterCount&&protaCount>tritaCount)blindness="prota";
			if (tritaCount>protaCount&&tritaCount>deuterCount)blindness="trita";
		}
		if (blindness.equals(""))blindness="prota";
		if (countOf(ports, "dvi")==2&&countOf(ports,"rj45")==1&&indicators.contains("CLR*"))blindness="BOB";
		if (blindness.equals("prota")) {
			System.out.println("Black to red, brown to green, orange to red\nblue to red, green to orange, swap purple and pink.");
		}
		if (blindness.equals("deuter")) {
			System.out.println("Red to green, blue to pink, swap green yellow\npink to gray, swap purple brown");
		}
		if (blindness.equals("trita")) {
			System.out.println("Swap blue gray, swap purple black\ngreen to blue, swap orange red");
		}
		if (blindness.equals("BOB"))System.out.println("SWAP EVERYTHING to a diff color");
	}
	public void IceCream() {
		/*Allergy conversion
		 * Chocolate: 0
			Strawberry: 1
			Raspberry: 2
			Nuts: 3
			Cookies: 4
			Mint: 5
			Fruit: 6
			Cherry: 7
			Marshmallows: 8
		 * 
		 * Ice Cream Conversion
		 * Cookies and Cream: 0
		 * Neapolitan: 1
		 * Tutti Frutti: 2
		 * The Classic: 3
		 * Rocky Road: 4
		 * Double Chocolate: 5
		 * Mint Chocolate Chip: 6
		 * Double Strawberry: 7
		 * Raspberry Ripple: 8
		 * Vanilla: 9
		 */
		if (emptyPortPlate==2) {
			System.out.println("Empty port plate? y/n");
			String emptyPortPlate = input.next().toLowerCase().substring(0,1);
			if (emptyPortPlate.equals("y"))this.emptyPortPlate=1;
			else this.emptyPortPlate=0;
		}
		int[][] customerAllergens =	   {{150, 683, 071, 432, 361},
										{830, 214, 435, 267, 143},
										{845, 167, 256, 375, 361},
										{267, 014, 823, 781, 573},
										{341, 362, 021, 247, 856},
										{163, 752, 145, 420, 375},
										{461, 236, 157, 682, 274},
										{625, 417, 820, 126, 367},
										{426, 123, 034, 650, 478},
										{635, 512, 426, 710, 372},
										{035, 164, 548, 207, 736},
										{463, 102, 674, 258, 031},
										{371, 820, 713, 678, 451},
										{241, 780, 346, 103, 652},
										{125, 680, 321, 745, 184},
										{031, 257, 346, 671, 530},
										{812, 648, 043, 164, 325},
										{732, 156, 547, 340, 621},
										{562, 136, 347, 205, 813},
										{568, 210, 482, 425, 051}};
		
		int iceCreamPriorities[][] = {{0,1,2,3,4,5,6,7,8,9},{5,6,1,4,2,7,0,8,3,9},{1,2,0,8,7,6,5,3,4,9},{7,0,4,3,1,5,2,8,6,9}};
		int iceCreamList = -1;
		if (numOfLitIndicators()-(indicators.size()-numOfLitIndicators())>0) iceCreamList = 0;
		else if (this.emptyPortPlate==1) iceCreamList=1;
		else if (batteries>2) iceCreamList=2;
		else iceCreamList=3;
		
		for (int round = 0; round <3; round++) {
		System.out.println("Customer name? (full name)");
		String customer = input.next().toLowerCase();
		customer = customer.substring(0,1).toUpperCase()+customer.substring(1);
		int customerint = 999;
		ArrayList<Integer> allergies = new ArrayList<Integer>();
		switch (customer) {
		case "Mike":
			customerint=0;
			break;
		case "Tim":
			customerint=1;
			break;
		case "Tom":
			customerint=2;
			break;
		case "Dave":
			customerint=3;
			break;
		case "Adam":
			customerint=4;
			break;
		case "Cheryl":
			customerint=5;
			break;
		case "Sean":
			customerint=6;
			break;
		case "Ashley":
			customerint=7;
			break;
		case "Jessica":
			customerint=8;
			break;
		case "Taylor":
			customerint=9;
			break;
		case "Simon":
			customerint=10;
			break;
		case "Sally":
			customerint=11;
			break;
		case "Jade":
			customerint=12;
			break;
		case "Sam":
			customerint=13;
			break;
		case "Gary":
			customerint=14;
			break;
		case "Victor":
			customerint=15;
			break;
		case "George":
			customerint=16;
			break;
		case "Jacob":
			customerint=17;
			break;
		case "Pat":
			customerint=18;
			break;
		case "Bob":
			customerint=19;
			break;
		}
		
		for (int i = 0; i <3; i++) allergies.add((customerAllergens[customerint][lastDigit/2])/(int) (Math.pow(10, i))%10);
		ArrayList<Integer> desiredIceCream = new ArrayList<Integer>();
		while (desiredIceCream.size()<4) {
			System.out.println("Current flavors "+desiredIceCream);
			System.out.println("Enter new flavor your customer desires. Ignore vanilla.");
			System.out.println("0.Cookies and Cream.\n1.Neapolitan\n2.Tutti Frutti\n3.The Classic\n4.Rocky Road\n5.Double Chocolate\n6.Mint Chocolate Chip\n7.Double Strawberry\n8.Raspberry Ripple");
			desiredIceCream.add(input.nextInt());
		}
		ArrayList<Boolean> canHave = new ArrayList<Boolean>();
		while (canHave.size()<9)canHave.add(true);
		for (int j : allergies) {
			if (j==0) {
				canHave.set(5, false);
				canHave.set(4, false);
				canHave.set(1, false);
				canHave.set(3, false);
				canHave.set(6, false);
			}
			if (j==1) {
				canHave.set(1, false);
				canHave.set(2, false);
				canHave.set(7, false);
			}
			if (j==2) {
				canHave.set(2, false);
				canHave.set(8, false);
			}
			if (j==3) canHave.set(4, false);
			if (j==4) canHave.set(0, false);
			if (j==5) canHave.set(6, false);
			if (j==6) {
				canHave.set(2, false);
				canHave.set(8, false);
				canHave.set(7, false);
				canHave.set(3, false);
			}
			if (j==7)canHave.set(3, false);
			if (j==8)canHave.set(4, false);
		}
		
		int iceCreamToSell = -1;
		for (int i = 0; i<10; i++) {
			if (iceCreamToSell==-1&&canHave.get(iceCreamPriorities[iceCreamList][i])&&desiredIceCream.contains(iceCreamPriorities[iceCreamList][i])) {
				iceCreamToSell = iceCreamPriorities[iceCreamList][i];
			}
		}
		String answer = "";
		switch (iceCreamToSell) {
		case 0:
			answer = "Cookies and Cream";
			break;
		case 1:
			answer = "Neapolitan";
			break;
		case 2:
			answer = "Tutti Frutti";
			break;
		case 3:
			answer = "The Classic";
			break;
		case 4:
			answer = "Rocky Road";
			break;
		case 5:
			answer = "Double Chocolate";
			break;
		case 6:
			answer = "Mint Chocolate Chip";
			break;
		case 7:
			answer = "Double Strawberry";
			break;
		case 8:
			answer = "Raspberry Ripple";
			break;
		case 9:
			answer = "Vanilla";
			break;
		}
		System.out.println("Give them "+answer);
		}
		}
	public void WordSearch() {
		String[][] wordsToFind = {{"Jinx", "Unique", "Locate", "Yes", "Test", "Work", "Help", "Delta"},
				{"Serial", "Six", "Edge", "Expert", "Bravo", "Talk", "Mike", "True"},
				{"Four", "Listen", "Spell", "Timer", "Lima", "Read", "Module", "Seven"},
				{"Unique", "Word", "Yes", "Xray", "Work", "Bombs", "Delta", "Victor"},
				{"Word", "Red", "Xray", "Green", "Bombs", "Found", "Victor", "Zulu"},
				{"Yes", "Xray", "Alarm", "False", "Delta", "Victor", "Manual", "Panic"},
				{"Office", "Four", "Tango", "Spell", "Count", "Lima", "List", "Module"},
				{"Spell", "Timer", "Word", "Red", "Module", "Seven", "Bombs", "Found"},
				{"Edge", "Expert", "Look", "North", "Mike", "True", "Number", "India"},
				{"Tango", "Spell", "Unique", "Word", "List", "Module", "Work", "Bombs"},
				{"Six", "Letter", "Expert", "Beep", "Talk", "Golf", "True", "Romeo"},
				{"Listen", "Next", "Timer", "Serial", "Read", "Math", "Seven", "Bravo"},
				{"Red", "Edge", "Green", "Look", "Found", "Mike", "Zulu", "Number"},
				{"Green", "Look", "Echo", "Blue", "Zulu", "Number", "Kaboom", "Line"},
				{"Solve", "Tango", "Jinx", "Unique", "Yankee", "List", "Test", "Work"},
				{"North", "Twenty", "Submit", "Color", "India", "See", "Boom", "Port"},
				{"Look", "North", "Blue", "Submit", "Number", "India", "Line", "Boom"},
				{"Next", "Oscar", "Serial", "Six", "Math", "Chart", "Bravo", "Talk"},
				{"False", "Echo", "Find", "Check", "Panic", "Kaboom", "Sierra", "Add"},
				{"Expert", "Beep", "North", "Twenty", "True", "Romeo", "India", "See"},
				{"Echo", "Blue", "Check", "Quebec", "Kaboom", "Line", "Add", "Search"},
				{"Blue", "Submit", "Quebec", "Done", "Line", "Boom", "Search", "Hotel"},
				{"Timer", "Serial", "Red", "Edge", "Seven", "Bravo", "Found", "Mike"},
				{"Xray", "Green", "False", "Echo", "Victor", "Zulu", "Panic", "Kaboom"},
				{"Locate", "Yes", "Call", "Alarm", "Help", "Delta", "Decoy", "Manual"},
				{"Alarm", "False", "East", "Find", "Manual", "Panic", "Finish", "Sierra"}};
	
	System.out.println("Reading order what are your letters, no spaces.");
	String userLetters = input.next().toLowerCase();
	
	String userWords = "";
	int offset = 0;
	if (lastDigit%2==0)offset=4;
	for (int i = 0; i <4; i++) {
		userWords+=wordsToFind[userLetters.charAt(i)-97][i+offset]+" ";
	}
	System.out.println(userWords);
	}
	public void Cooking() {
		if (portPlates==-1) {
			System.out.println("How many port plates?");
			portPlates = input.nextInt();
		}
		int serialLetters = 0;
		for (int i = 0; i<6; i++)if ((int)serial.toLowerCase().charAt(i)>96)serialLetters++;
		String light = "light off";
		
		int letters = 0;
		for (int i = 0; i<6; i++) {
			try {
				@SuppressWarnings("unused")
				int x = Integer.parseInt(serial.substring(i,i+1));
			}
			catch(NumberFormatException e) {
				letters++;
			}
		}
		if (vowel)light = "light on";
		if (ports.contains("ps2"))light = "light on";
		int meal = (holders-indicators.size()+(batteries*ports.size())-portPlates)%5;
		while (meal < 1)meal += 5;
		int temp = 0;
		switch (meal) {
		case 1:
			temp = 250;
			break;
		case 2:
			temp = 160;
			break;
		case 3:
			temp = 200;
			break;
		case 4:
		case 5:
			temp = 180;
			break;
		}
		int ovenNum = (numOfLitIndicators()-(indicators.size()-numOfLitIndicators())+serialLetters)%6;
		while (ovenNum < 1)ovenNum+=6;
		String oven = "";
		switch (ovenNum) {
		case 1:
			oven = "Bottom";
			break;
		case 2:
			oven = "Bottom+grill";
			break;
		case 3:
			oven = "Bottom+top";
			break;
		case 4:
			oven = "Fan";
			break;
		case 5:
			oven = "Grill";
			break;
		case 6:
			oven = "Fan+grill";
			break;
		}
		
		
		
		if (emptyPortPlate==2) {
			System.out.println("Empty port plate? y/n");
			String emptyPortPlate = input.next().toLowerCase().substring(0,1);
			if (emptyPortPlate.equals("y"))this.emptyPortPlate=1;
			else this.emptyPortPlate=0;
		}
		int cookFor = -1;
		if (indicators.contains("FRK*")||ports.contains("serial")) {
			cookFor = 4;
		}
		else if (indicators.contains("FRQ*")||this.emptyPortPlate==1) {
			cookFor = 0;
		}
		else if (indicators.contains("SND")||letters<3) {
			cookFor = 5;
		}
		else if (ports.contains("hdmi")||ports.contains("cvideo")||ports.contains("usb")) {
			cookFor = 3;
		}
		else if (indicators.contains("BOB")||indicators.contains("BOB*")) {
			cookFor = 1;
		}
		else {
			cookFor = 2;
		}
		
		int[][] cookTimes = {{10,15,20,05,30,50},{75,70,80,75,65,10},{55,70,65,50,45,60},{95,90,75,85,70,35},{25,30,35,20,40,10}};
		int time = cookTimes[meal-1][cookFor];
		System.out.println(oven+", "+temp+" degrees, "+time+" minutes, "+light);
	}
	public void Laundry() {
		System.out.print("Num of solved modules: ");
		int solved = input.nextInt();
		System.out.print("Num of unsolved modules: ");
		int unsolved = input.nextInt();
		
		int itemNum = (unsolved+indicators.size())%6;
		int matNum = (ports.size()+solved-holders)%6;
		int colorNum = (lastDigit+batteries)%6;
		while (matNum<0)matNum+=6;
		System.out.println("item mat color"+itemNum+" "+matNum+" "+colorNum);
		String special = "";
		String wash = "";
		String dry = "";
		String iron = "";
		
		
		
		String[][] mats = {{"50", "95", "Hand", "30", "40", "30"},{"Petroleum only", "Don't dryclean", "reduced moist", "low heat", "wet cleaning", "no tetrachlore"}};
		String[][] colors = {{"3 dot", "flat", "no dot", "big X", "1 dot", "2 dot"}, {"any solvent", "low heat", "short cycle", "no steam finish", "no chlorine", "no chlorine"}};
		String [][] items = {{"300", "no steam", "iron", "200", "300", "110"},{"bleach", "no tetrachlore", "reduced moist", "reduced mosit", "don't bleach", "don't dryclean"}};
		String[] matNames = {"polyester", "cotton", "wool", "nylon", "corduroy", "leather"};
		boolean match = false;
		for (int i = 0; i < matNames[matNum].length(); i++) for (int j = 0; j<serial.length(); j++)if (matNames[matNum].substring(i, i+1).equals(serial.substring(j, j+1)))match=true;
		
		wash = mats[0][matNum];
		dry = colors[0][colorNum];
		iron = items[0][itemNum];
		
		
		if (colorNum==4)special = "No chlorine";
		else if (itemNum==0||matNum==4)special = mats[1][matNum];
		else if (match) special = colors[1][colorNum];
		else special = items[1][itemNum];
		if (matNum==5||colorNum==3)wash = "30";
		if (matNum==2||colorNum==1)dry = "3 dot";
		if (batteries==4&&holders==2&&indicators.contains("BOB*"))System.out.println("BOBOBOBOBOBOBOBOBOBOBOBOBOBOBOB");
		System.out.println(("Wash "+wash+" dry "+dry+" iron "+iron+" special "+special));
	}
	public void BigCircle() {
		System.out.print("Num of solved modules: ");
		int solved = input.nextInt();
		int sum = 0;
		for (String ind : indicators) {
			
			if (ind.equals("BOB")||ind.equals("CAR")||ind.equals("CLR")) {
				sum-=1;
			}
			if (ind.equals("BOB*")||ind.equals("CAR*")||ind.equals("CLR*")) {
				sum+=1;
			}
			if (ind.equals("FRK")||ind.equals("FRQ")||ind.equals("MSA")||ind.equals("NSA")) {
				sum-=2;
			}
			if (ind.equals("FRK*")||ind.equals("FRQ*")||ind.equals("MSA*")||ind.equals("NSA*")) {
				sum+=2;
			}
			if (ind.equals("SIG")||ind.equals("SND")||ind.equals("TRN")) {
				sum-=3;
			}
			if (ind.equals("SIG*")||ind.equals("SND*")||ind.equals("TRN*")) {
				sum+=3;
			}
		}
		
		sum+=(solved*3);
		if (batteries%2==1)sum+=4;
		else sum-=4;
		int pairs = 0;
		if (ports.contains("parallel")&&ports.contains("serial")) {
			System.out.println("How many port plates do you have with both a parallel and a serial?");
			pairs = input.nextInt();
		}
		sum+=countOf(ports,"parallel")*5;
		sum-=9*pairs;
		pairs = 0;
		if (ports.contains("dvi")&&ports.contains("rca")) {
			System.out.println("How many port plates do you have with both an rca and a dvi?");
			pairs = input.nextInt();
		}
		sum-=countOf(ports,"dvi")*5;
		if (sum<1)sum*=-1;
		sum+=9*pairs;
		sum%=10;
		String serialChar = "";
		if (sum<6)serialChar=serial.substring(sum,sum+1).toUpperCase();
		else serialChar = serial.substring(10-sum,11-sum).toUpperCase();
		String solution = "";
		if (serialChar.equals("0")||serialChar.equals("1")||serialChar.equals("2")) {
			solution = "Red Yellow Blue";
		}
		if (serialChar.equals("3")||serialChar.equals("4")||serialChar.equals("5")) {
			solution = "Orange Green Magenta";
		}
		if (serialChar.equals("6")||serialChar.equals("7")||serialChar.equals("8")) {
			solution = "Blue Black Red";
		}
		if (serialChar.equals("9")||serialChar.equals("A")||serialChar.equals("B")) {
			solution = "Magenta White Orange";
		}
		if (serialChar.equals("C")||serialChar.equals("D")||serialChar.equals("E")) {
			solution = "Orange Blue Black";
		}
		if (serialChar.equals("F")||serialChar.equals("G")||serialChar.equals("H")) {
			solution = "Green Red White";
		}
		if (serialChar.equals("I")||serialChar.equals("J")||serialChar.equals("K")) {
			solution = "Magenta Yellow Black";
		}
		if (serialChar.equals("L")||serialChar.equals("M")||serialChar.equals("N")) {
			solution = "Red Orange Yellow";
		}
		if (serialChar.equals("O")||serialChar.equals("P")||serialChar.equals("Q")) {
			solution = "Yellow Green Blue";
		}
		if (serialChar.equals("R")||serialChar.equals("S")||serialChar.equals("T")) {
			solution = "Blue Magenta Red";
		}
		if (serialChar.equals("U")||serialChar.equals("V")||serialChar.equals("W")) {
			solution = "Black White Green";
		}
		if (serialChar.equals("X")||serialChar.equals("Y")||serialChar.equals("Z")) {
			solution = "White Yellow Blue";
		}
		System.out.println("If your circle is spinning counter clockwise, press in reverse.");
		System.out.println(solution);
	}
	public void TheSun() {
		System.out.println("Where is your LED?");
		String LEDString = input.next().toUpperCase();
		int led = -1;
		switch(LEDString) {
		case "N":
			led = 0;
			break;
		case "NE":
			led = 1;
			break;
		case "E":
			led = 2;
			break;
		case "SE":
			led = 3;
			break;
		case "S":
			led = 4;
			break;
		case "SW":
			led = 5;
			break;
		case "W":
			led = 6;
			break;
		case "NW":
			led = 7;
			break;
		}
		
		int consonantsInSerial = 0;
		int digitsInSerial = 0;
		for (int i = 0; i<6; i++) {
			try {
				@SuppressWarnings("unused")
				int x = Integer.parseInt(serial.substring(i,i+1));
				digitsInSerial++;
			}
			catch(NumberFormatException e) {
				if (!(serial.substring(i,i+1).equalsIgnoreCase("A")||
						serial.substring(i,i+1).equalsIgnoreCase("E")||
						serial.substring(i,i+1).equalsIgnoreCase("I")||
						serial.substring(i,i+1).equalsIgnoreCase("O")||
						serial.substring(i,i+1).equalsIgnoreCase("U")))consonantsInSerial+=1;;
			}
		}
		System.out.print("Total number of modules: ");
		int modules = input.nextInt();
		if (portPlates == -1) {
			System.out.print("Num of port plates: ");
			portPlates = input.nextInt();
		}
		ArrayList<Integer> variables = new ArrayList<Integer>();
		variables.add(indicators.size()%7);
		variables.add(consonantsInSerial);
		variables.add(batteries%7);
		variables.add(digitsInSerial%7);
		variables.add(ports.size()%7);
		variables.add(modules%7);
		variables.add(holders%7);
		variables.add(portPlates%7);
		
		ArrayList<String> press = new ArrayList<String>();
		for (int i = 0; i<8; i++)press.add("WRONG");
		for (int i : variables) {
			if (i<2)press.set(led%8, "Outer ");
			if (i>1&&i<5)press.set(led%8,"Inner ");
			if (i>4)press.set(led%8,"Center ");
			led++;
		}
		//0 is North, 1 is NE, and so on
		int position = modules%8;
		int[] serialNums = serialConvertToNums();
		for(int i =0; i<6; i++)if (serialNums[i]%10>4)serialNums[i]=1;else serialNums[i]=-1;
		if (debug) printArray(serialNums);
		String answer = "";
		for (int i = 0; i<8; i++) {
			
			if (position<0)position+=8;
			while(press.get(position%8).equals("done")) {
				if (i<7)position+=serialNums[i-1];
				else position+=1;
				if (position<0)position+=8;
			}
			
			String helper = "";
			switch (position%8) {
			case 0:
				helper = "North";
				break;
			case 1:
				helper = "Northeast";
				break;
			case 2:
				helper = "East";
				break;
			case 3:
				helper = "Southeast";
				break;
			case 4:
				helper = "South";
				break;
			case 5:
				helper = "Southwest";
				break;
			case 6:
				helper = "West";
				break;
			case 7:
				helper = "Northwest";
				break;
			}
			
			
			answer+=press.get(position%8)+helper+", ";
			if (i==3)answer+="\n";
			press.set(position%8, "done");
			if (i<6)position+=serialNums[i];
			
		}
		System.out.println(answer);
	}
	public void SkewedSlots() {
		System.out.print("Skewed slots number: ");
		int orig = input.nextInt();
		int dig3orig = orig%10;
		orig/=10;
		int dig2orig = orig%10;
		orig/=10;
		int dig1orig = orig;
		int[] digits = {dig1orig, dig2orig, dig3orig};
		int[] origdigits = {dig1orig, dig2orig, dig3orig};
		
		for (int i=0; i<3; i++) {
			if (digits[i]==2)digits[i]=5;
			if (digits[i]==7)digits[i]=0;
			digits[i]+= (numOfLitIndicators() - (indicators.size()-numOfLitIndicators()));
			if (digits[i]%3==0)digits[i]+=4;
			else if (digits[i]>7)digits[i]*=2;
			else if (digits[i]<3&&digits[i]%2==0)digits[i]/=2;
			else if (!(ports.contains("rca")||ports.contains("ps2")))digits[i]=origdigits[i]+batteries;
		}
		System.out.println("all slots mods "+digits[0]+digits[1]+digits[2]);
		if (digits[0]>5&&digits[0]%2==0)digits[0]/=2;
		else if (isPrime(digits[0]))digits[0]+=lastDigit;
		else if (ports.contains("parallel"))digits[0]*=-1;
		else if (!(origdigits[1]%2==1))digits[0]-=2;
		
		if (ports.contains("BOB")||ports.contains("BOB*"));
		else if (digits[1]==0) {System.out.println("ran"+digits[1]+origdigits[0]); digits[1]+=origdigits[0];}
		else {
			int prev = 0;
			boolean flag = false;
			int save = 0;
			for (int i = 1; i<145; i+=0) {
				if (prev==digits[1]) {
					flag=true;
					save = i;
					System.out.println("saved a "+save);
					break;
				}
				int holder = prev;
				prev=i;
				i+=holder;
			}
			if (flag)digits[1]+=save;
			else if (digits[1]>6)digits[1]+=4;
			else digits[1]*=3;
		}
		
		if (ports.contains("serial")) {
			int big = 0;
			for (int i = 0; i<6; i++) {
				try {
					int x = Integer.parseInt(serial.substring(i,i+1));
					if (x>big)big=x;
				}
				catch(NumberFormatException e) {}
			}
			digits[2]+=big;
		}
		else if (origdigits[2]==origdigits[1]||origdigits[2]==origdigits[0]);
		else if (digits[2]>4) {
			int sum = 0;
			String k = Integer.toBinaryString(origdigits[2]);
			for (int i =0; i < k.length(); i++)if (k.substring(i, i+1).equals("1"))sum++;
			digits[2]=sum;
		}
		else digits[2]+=1;
		
		for (int i = 0 ; i<3; i++) {
			while (digits[i]<0)digits[i]+=10;
			while (digits[i]>9)digits[i]-=10;
			System.out.print(digits[i]);
		}
		System.out.println();
		
	}
	public void AdventureGame() {
		System.out.print("Enemy: ");
		String enemy = input.next().toLowerCase();
		System.out.print("Strength: ");
		int str = input.nextInt();
		System.out.print("Dexterity: ");
		int dex = input.nextInt();
		System.out.print("Intelligence: ");
		int intel = input.nextInt();
		System.out.print("Height (enter as a decimal): ");
		double hgt = input.nextDouble();
		System.out.print("Temperature: ");
		int tmp = input.nextInt();
		System.out.print("Gravity: ");
		double grv = input.nextDouble();
		System.out.print("Pressure: ");
		int psi = input.nextInt();
		ArrayList<String> items = new ArrayList<String>();
		while (true) {
			System.out.println("Current Items: "+items+"\nEnter any items or enter STOP to continue: ");
			String item = input.nextLine().toLowerCase();
			if (item.equals("stop"))break;
			items.add(item);
		}
		
		for (String j : items) {
			switch(j) {
			case "balloon":
				if (!enemy.equals("eagle")&&(grv<9.3||psi>110))System.out.println("Use "+j);
				break;
			case "battery":
				if (batteries<=1&&!(enemy.equals("golem")||enemy.equals("wizard")))System.out.println("Use "+j);
				break;
			case "bellows":
				if ((psi>105&&(enemy.equals("dragon")||enemy.equals("eagle"))||psi<95))System.out.println("Use "+j);
				break;
			case "crystal ball":
				if (!(enemy.equals("wizard"))&&intel>lastDigit)System.out.println("Use "+j);
				break;
			case "feather":
				if (dex>str||dex>intel)System.out.println("Use "+j);
				break;
			case "hard drive":
				if(countOf(ports, "rca")>1||countOf(ports, "serial")>1||countOf(ports, "rj45")>1||countOf(ports, "dvi")>1||countOf(ports, "ps2")>1||countOf(ports, "parallel")>1)System.out.println("Use "+j);
				break;
			case "lamp":
				if (tmp<12 && !(enemy.equals("lizard")))System.out.println("Use "+j);
				break;
			case "moonstone":
				if (indicators.size()-numOfLitIndicators()>1)System.out.println("Use "+j);
				break;
			case "potion":
				System.out.println("use then restart module");
				break;
			case "small dog":
				if (!(enemy.equals("dragon")||enemy.equals("troll")||enemy.equals("demon")))System.out.println("Use "+j);
				break;
			case "stepladder":
				if (hgt<4&&!(enemy.equals("goblin")||enemy.equals("lizard")))System.out.println("Use "+j);
				break;
			case "sunstone":
				if (numOfLitIndicators()>1)System.out.println("Use "+j);
				break;
			case "symbol":
				if (enemy.equals("demon")||enemy.equals("golem")||tmp>31)System.out.println("Use "+j);
				break;
			case "ticket":
				if (hgt>4.5&&grv>9.1&&grv<10.5)System.out.println("Use "+j);
				break;
			case "trophy":
				if (enemy.equals("troll")||str>firstDigit())System.out.println("Use "+j);
				break;
			}
		}
		int enemystr = 0;
		int enemydex = 0;
		int enemyint = 0;
		switch(enemy) {
		case "demon":
			enemystr = 50;
			enemydex = 50;
			enemyint = 50;
			break;
		case "dragon":
			enemystr = 10;
			enemydex = 11;
			enemyint = 13;
			System.out.println("dragons are cool");
			break;
		case "eagle":
			enemystr = 4;
			enemydex = 7;
			enemyint = 3;
			break;
		case "goblin":
			enemystr = 3;
			enemydex = 6;
			enemyint = 5;
			break;
		case "golem":
			enemystr = 9;
			enemydex = 4;
			enemyint = 7;
			break;
		case "troll":
			enemystr = 8;
			enemydex = 5;
			enemyint = 4;
			break;
		case "lizard":
			enemystr = 4;
			enemydex = 6;
			enemyint = 3;
			break;
		case "wizard":
			enemystr = 4;
			enemydex = 3;
			enemyint = 8;
			break;
		}
		int stradv = str-enemystr;
		int dexadv = dex-enemydex;
		int intadv = intel-enemyint;
		boolean flag = false;
		if (items.contains("caber")&&stradv>=dexadv&&stradv>=intadv) {
			flag = true;
			System.out.println("Use caber");
		}
		if (items.contains("longbow")&&dexadv>=stradv&&dexadv>=intadv) {
			flag = true;
			System.out.println("Use longbow");
		}
		if (items.contains("grimoire")&&intadv>=stradv&&intadv>=dexadv) {
			flag = true;
			System.out.println("Use grimoire");
		}
		if (!flag) {
			if (stradv-dexadv>1&&stradv-intadv>1&&items.contains("broadsword"))System.out.println("Use broadsword");
			else if (dexadv-stradv>1&&dexadv-intadv>1&&items.contains("nasty knife"))System.out.println("Use nasty knife knife");
			else if (intadv-dexadv>1&&intadv-stradv>1&&items.contains("magic orb"))System.out.println("Use magic orb");
			else System.out.println("Str/dex/int"+stradv+"/"+dexadv+"/"+intadv);
		}
		
	}
	public void Backgrounds() {
		System.out.println("FULL NAMES OF COLORS");
		System.out.print("Button color: ");
		String buttoncolor = input.next().toLowerCase();
		System.out.print("Backing color: ");
		String backingcolor = input.next().toLowerCase();
		int[][] presses = {{3,	2,	9,	1,	7,	4},
						   {7,	9,	8,	8,	2,	3},
						   {5,	1,	7,	4,	4,	6},
						   {6,	4,	2,	6,	8,	5},
						   {5,	1,	5,	3,	9,	9},
						   {1,	2,	3,	6,	7,	8}};
		ArrayList<String> rules = new ArrayList<String>();
		if (backingcolor.equals(buttoncolor))rules.add("colormatch");
		if ((rules.size()<2)&&buttoncolor.equals("white")||backingcolor.equals("white")||buttoncolor.equals("black")||backingcolor.equals("black"))rules.add("blackwhite");
		if (holders==0)if (rules.size()<2)rules.add("nodbats");
		if (holders>0) {
			if ((rules.size()<2)&&batteries/holders==2)rules.add("nodbats");
		}
		if ((rules.size()<2)&&batteries==holders)rules.add("noaabats");
		if ((rules.size()<2)&&(buttoncolor.equals("red")||buttoncolor.equals("blue")||buttoncolor.equals("yellow"))&&(backingcolor.equals("red")||backingcolor.equals("blue")||backingcolor.equals("yellow")))rules.add("primaries");
		if ((rules.size()<2)&&buttoncolor.equals("green")||buttoncolor.equals("orange")||buttoncolor.equals("purple"))rules.add("secondary");
		if ((rules.size()<2)&&indicators.contains("SND"))rules.add("SND");
		if ((rules.size()<2)&&ports.contains("serial"))rules.add("serial");
		if ((rules.size()<2)&&((backingcolor.equals("yellow")&&buttoncolor.equals("green"))||(backingcolor.equals("red")&&buttoncolor.equals("purple"))))rules.add("mixblue");
		while (rules.size()<2)rules.add("otherwise");
		int [] nums = new int[2];
		for (int i = 0; i <2; i++) {
			switch (rules.get(i)) {
			case "colormatch":
				if (i==0)nums[i]=0;
				else nums[i]=2;
				break;
			case "blackwhite":
				if (i==0)nums[i]=3;
				else nums[i]=1;
				break;
			case "nodbats":
				if (i==0)nums[i]=2;
				else nums[i]=4;
				break;
			case "noaabats":
				if (i==0)nums[i]=3;
				else nums[i]=3;
				break;
			case "primaries":
				if (i==0)nums[i]=1;
				else nums[i]=5;
				break;
			case "secondary":
				if (i==0)nums[i]=5;
				else nums[i]=4;
				break;
			case "SND":
				if (i==0)nums[i]=4;
				else nums[i]=1;
				break;
			case "serial":
				if (i==0)nums[i]=1;
				else nums[i]=2;
				break;
			case "mixblue":
				if (i==0)nums[i]=2;
				else nums[i]=3;
				break;
			case "otherwise":
				if (i==0)nums[i]=4;
				else nums[i]=0;
				break;
			}
		}
		System.out.println(presses[nums[0]][nums[1]]);
	}
	public void Bitmaps() {
		System.out.print("Black squares quad 1: ");
		int q1 = input.nextInt();
		System.out.print("Black squares quad 2: ");
		int q2 = input.nextInt();
		System.out.print("Black squares quad 3: ");
		int q3 = input.nextInt();
		System.out.print("Black squares quad 4: ");
		int q4 = input.nextInt();
		System.out.println("any 3x3 squares? y/n");
		String threebythree = input.next().toLowerCase().substring(0,1);
		System.out.println("any full lines? y/n");
		String fullline = input.next().toLowerCase().substring(0,1);
		ArrayList<Integer> quadrants = new ArrayList<Integer>();
		quadrants.add(q1);
		quadrants.add(q2);
		quadrants.add(q3);
		quadrants.add(q4);
		
		int smallWPixels = 0;
		int numQuads5WPixels = 0;
		int mostlyWhiteQuads = 0;
		int mostlyBlackQuads = 0;
		int totalWhitePixels = 0;
		int smallBPixels = 0;
		int numQuads5BPixels = 0;
		int smallestBlackPixels = 16;
		
		for (int i = 0; i < 4; i++) {
			if (quadrants.get(i)<smallestBlackPixels)smallestBlackPixels=quadrants.get(i);
			if (quadrants.get(i)>10) {
				smallWPixels = 16-quadrants.get(i);
				numQuads5WPixels++;
			}
			if (quadrants.get(i)<6) {
				smallBPixels = quadrants.get(i);
				numQuads5BPixels++;
			}
			if (quadrants.get(i)>8)mostlyBlackQuads++;
			if (quadrants.get(i)<8)mostlyWhiteQuads++;
			totalWhitePixels+=16-quadrants.get(i);
		}
		
		int answer = 0;
		int pos = lastDigit;
		while (answer==0) {
		switch (pos%10) {
		case 0:
			if (numQuads5WPixels==1)answer = totalWhitePixels-smallWPixels;
			break;
		case 1:
			if (mostlyWhiteQuads==numOfLitIndicators()) {
				answer = batteries;
				System.out.println(1);
			}
			break;
		case 2:
			if (fullline.equals("y")) {
				System.out.println("Coordinate of full line");
				answer = -1;
			}
			break;
		case 3:
			if (mostlyWhiteQuads<mostlyBlackQuads) {
				System.out.println(3);
				answer = mostlyBlackQuads;
			}
			break;
		case 4:
			if (totalWhitePixels>35) {
				System.out.println(4);
				answer = totalWhitePixels;
			}
		case 5:
			if (mostlyWhiteQuads>mostlyBlackQuads) {
				System.out.println(5);
				answer = smallestBlackPixels;
			}
			break;
		case 6:
			if (numQuads5BPixels==1) {
				System.out.println(6);
				answer = 64-totalWhitePixels-smallBPixels;
			}
			break;
		case 7:
			if (mostlyBlackQuads==indicators.size()-numOfLitIndicators()) {
				System.out.println(7);
				answer = ports.size();
			}
			break;
		case 8:
			if (threebythree.equals("y")) {
				System.out.println("Answer is x pos of three by three center");
				answer = -1;
			}
			break;
		case 9:
			if (mostlyWhiteQuads==mostlyBlackQuads) {
				System.out.println(9);
				answer = firstDigit();
			}
			break;
			
		
		}
		pos++;
		}
		answer = answer%4;
		if (answer==0)answer=4;
		System.out.println(answer);
	}
	public void BinaryLEDs() {
		int[][] sequences = {{17, 15, 6, 2, 24, 8, 26, 25, 21, 24, 1, 15, 18, 8},
							 {18, 15, 19, 31, 12, 6, 19, 21, 11, 16, 19, 2, 1, 29},
							 {8, 25, 1, 15, 20, 15, 9, 3, 6, 24, 1, 24, 5, 26},
							 {21, 27, 6, 12, 27, 20, 7, 1, 19, 15, 3, 13, 9, 28},
							 {3, 21, 14, 22, 7, 28, 16, 27, 22, 17, 26, 2, 31, 15},
							 {8, 22, 30, 19, 1, 25, 31, 16, 9, 7, 6, 13, 9, 7},
							 {5, 18, 12, 7, 5, 12, 31, 16, 10, 15, 17, 9, 12, 25},
							 {4, 20, 18, 25, 20, 4, 24, 29, 17, 16, 12, 16, 29, 19}};
		ArrayList<ArrayList<Integer>> arraysequences = new ArrayList<ArrayList<Integer>>();
		for (int[] sequence : sequences) {
			ArrayList<Integer> asdf = new ArrayList<Integer>();
			for (int l : sequence) {
				asdf.add(l);
			}
			arraysequences.add(asdf);
		}
		//System.out.println(arraysequences);
		System.out.println("Enter 5 sequences of numbers.");
		ArrayList<String> sequencestring = new ArrayList<String>();
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for (int i = 0; i <5; i++)sequencestring.add(input.next());
		for (String j : sequencestring) {
			int temp = 0;
			if (j.contains("1"))temp+=16;
			if (j.contains("2"))temp+=8;
			if (j.contains("3"))temp+=4;
			if (j.contains("4"))temp+=2;
			if (j.contains("5"))temp+=1;
			nums.add(temp);
		}
		System.out.println(nums);
		int thesequence = -1;
		for (int i = 0; i<arraysequences.size(); i++) {
			if (Math.abs(arraysequences.get(i).indexOf(nums.get(0))-arraysequences.get(i).indexOf(nums.get(1)))==1&&
					Math.abs(arraysequences.get(i).indexOf(nums.get(1))-arraysequences.get(i).indexOf(nums.get(2)))==1&&
					Math.abs(arraysequences.get(i).indexOf(nums.get(2))-arraysequences.get(i).indexOf(nums.get(3)))==1&&
					Math.abs(arraysequences.get(i).indexOf(nums.get(3))-arraysequences.get(i).indexOf(nums.get(4)))==1)thesequence=i;
		}
		switch (thesequence) {
		case -1:
			System.out.println("bad nums try again");
			break;
		case 0:
			System.out.println("Cut green on 4 only (2)");
			break;
		case 1:
			System.out.println("Cut blue on 23 (12)");
			break;
		case 2:
			System.out.println("Cut green on 125 (25)");
			break;
		case 3:
			System.out.println("Cut blue on 5 only (1)");
			break;
		case 4:
			System.out.println("Cut green on 123 (28)");
			break;
		case 5:
			System.out.println("Cut green on 34 (6)");
			break;
		case 6:
			System.out.println("Cut blue on 2345 (15)");
			break;
		case 7:
			System.out.println("Cut blue on 23 (12)");
			break;
		}
		
	}
	public void Battleships() {
		ArrayList<Integer> ints = new ArrayList<Integer>();
		ArrayList<String> chars = new ArrayList<String>();
		for (int i = 0; i<6; i++) {
			try {
				ints.add(Integer.parseInt(serial.substring(i,i+1)));
			}
			catch (NumberFormatException e) {
				chars.add(serial.substring(i,i+1));
			}
		}
		int length = 0;
		if (ints.size()==chars.size())length=3;
		else length = 2;
		for (int i = 0; i<length; i++) {
			System.out.println(antiModulo(((int)chars.get(i).charAt(0)-6),5) +" across, "+antiModulo(ints.get(i),5) +"down");
		}
		System.out.println(antiModulo(ports.size(),5)+" across, "+antiModulo((indicators.size()+batteries),5)+" down");
	}
	public void CheapCheckout() {
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<Double> prices = new ArrayList<Double>();
		ArrayList<String> categories = new ArrayList<String>();
		while (items.size()<4) {
			System.out.println("Current items "+items+" Enter a new item (fixed price only).");
			String useritem = input.nextLine().toLowerCase();
			System.out.println(useritem);
			items.add(useritem);
			if (items.get(0).equals(""))items.remove(0);
		}
		while (prices.size()<6)prices.add(1.0);
		while (categories.size()<6)categories.add("asdf");
		System.out.print("Enter your non fixed priced items like this (0.5 potatoes) or (1.0 steak).\nVariable priced item 1: ");
		String useritem = input.nextLine().toLowerCase();
		String[] thing = useritem.split(" ");
		prices.set(4, Double.parseDouble(thing[0]));
		items.add(thing[1]);
		System.out.print("Variable priced item 2: ");
		useritem = input.nextLine().toLowerCase();
		String[] thing2 = useritem.split(" ");
		prices.set(5, Double.parseDouble(thing2[0]));
		items.add(thing2[1]);
		
		System.out.println("What is the customer paying?");
		double customer = input.nextDouble();
		
		
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		int day = now.get(Calendar.DAY_OF_WEEK);
		if (debug)System.out.println("day of week "+day);
		for (int i =0; i <6; i++) {
			String item = items.get(i);
			double price = 0;
			switch (item) {
			case "bananas":
				price = 0.87;
				categories.set(i,"fruit");
				break;
			case "broccoli":
				price = 1.39;
				break;
			case "candy canes":
				price = 3.51;
				if (day==1)price+=2.15;
				categories.set(i,"sweet");
				break;
			case "canola oil":
				price = 2.28;
				break;
			case "cereal":
				price = 4.19;
				break;
			case "cheese":
				price = 4.49;
				if (day==1)price+=2.15;
				break;
			case "chicken":
				price = 1.99;
				break;
			case "chocolate bar":
				price = 2.10;
				categories.set(i,"sweet");
				break;
			case "chocolate milk":
				price = 5.68;
				break;
			case "coffee beans":
				price = 7.85;
				if (day==1)price+=2.15;
				break;
			case "cookies":
				price =2.00;
				if (day==1)price+=2.15;
				categories.set(i,"sweet");
				break;
			case "deodorant":
				price = 3.97;
				break;
			case "fruit punch":
				price = 2.08;
				categories.set(i,"sweet");
				break;
			case "grape jelly":
				price = 2.98;
				categories.set(i,"sweet");
				break;
			case "grapefruit":
				price = 1.08;
				categories.set(i,"fruit");
				break;
			case "gum":
				price = 1.12;
				categories.set(i,"sweet");
				break;
			case "honey":
				price = 8.25;
				categories.set(i,"sweet");
				break;
			case "ketchup":
				price = 3.59;
				break;
			case "lemons":
				price = 1.74;
				categories.set(i,"fruit");
				break;
			case "lettuce":
				price = 1.10;
				break;
			case "lollipops":
				price = 2.61;
				if (day==1)price+=2.15;
				categories.set(i,"sweet");
				break;
			case "lotion":
				price = 7.97;
				break;
			case "mayonnaise":
				price = 3.99;
				if (day==1)price+=2.15;
				break;
			case "mints":
				price = 6.39;
				if (day==1)price+=2.15;
				categories.set(i,"sweet");
				break;
			case "mustard":
				price = 2.36;
				if (day==1)price+=2.15;
				break;
			case "oranges":
				price = .8;
				categories.set(i,"fruit");
				break;
			case "paper towels":
				price = 9.46;
				if (day==1)price+=2.15;
				break;
			case "pasta sauce":
				price = 2.30;
				if (day==1)price+=2.15;
				break;
			case "peanut butter":
				price = 5.00;
				break;
			case "pork":
				price = 4.14;
				break;
			case "potato chips":
				price = 3.25;
				if (day==1)price+=2.15;
				break;
			case "potatoes":
				price = .68;
				break;
			case "shampoo":
				price = 4.98;
				if (day==1)price+=2.15;
				break;
			case "socks":
				price = 6.97;
				if (day==1)price+=2.15;
				break;
			case "soda":
				price = 2.05;
				if (day==1)price+=2.15;
				categories.set(i,"sweet");
				break;
			case "spaghetti":
				price = 2.92;
				if (day==1)price+=2.15;
				break;
			case "steak":
				price = 4.97;
				break;
			case "sugar":
				price = 2.08;
				if (day==1)price+=2.15;
				categories.set(i,"sweet");
				break;
			case "tea":
				price = 2.35;
				break;
			case "tissues":
				price = 3.94;
				if (day==1)price+=2.15;
				break;
			case "tomatoes":
				price = 1.80;
				categories.set(i,"fruit");
				break;
			case "toothpaste":
				price = 2.50;
				if (day==1)price+=2.15;
				break;
			case "turkey":
				price = 2.98;
				break;
			case "water bottles":
				price = 9.37;
				if (day==1)price+=2.15;
				break;
			case "white bread":
				price = 2.43;
				break;
			case "white milk":
				price = 3.62;
				break;	
			
			}
			if (day==3&&i<4)price+=digitalRoot(price);
			if (day==6&&categories.get(i).equals("fruit"))price*=1.25;
			if (day==7&&categories.get(i).equals("sweet"))price*=.65;
			if (debug)System.out.println("added "+(Math.round(prices.get(i)*price * 100.0) / 100.0));
			prices.set(i, (Math.round(prices.get(i)*price * 100.0) / 100.0));
		}
		if (day==2) {
			prices.set(0, (Math.round(prices.get(0)*.85 * 100.0) / 100.0));
			prices.set(2, (Math.round(prices.get(2)*.85 * 100.0) / 100.0));
			prices.set(5, (Math.round(prices.get(5)*.85 * 100.0) / 100.0));
		}
		if (day==5) {
			prices.set(0, (Math.round(prices.get(0)*.5 * 100.0) / 100.0));
			prices.set(2, (Math.round(prices.get(2)*.5 * 100.0) / 100.0));
			prices.set(4, (Math.round(prices.get(4)*.5 * 100.0) / 100.0));
		}
		if (day==4) {
			for (int i = 0; i <6; i++) {
				double num = prices.get(i)*100.0;

				int big = -1;
				int small = 10;
				ArrayList<Integer> order = new ArrayList<Integer>();
				while (num>1) {
					order.add((int)(num%10+.01));
					if (num%10>big)big=(int) (num%10+.01);
					if (num%10<small)small=(int) (num%10+.01);
					num/=10;
				}
				

				while (order.indexOf(big)!=-1)order.set(order.indexOf(big), small+100);
				while (order.indexOf(small)!=-1)order.set(order.indexOf(small), big);
				while (order.indexOf(small+100)!=-1)order.set(order.indexOf(small+100), small);
			
				String dude = "";
				for (int j= order.size()-1; j>-1; j--) {
					dude+=order.get(j);
				}
				
				prices.set(i, Double.parseDouble(dude)/100.0);
			}
			
		}
		double sum = 0;
		for (double k: prices) {
			sum+=k;
		}
		if (debug)System.out.println(sum+" total");
		if (sum>customer) {
			System.out.println("Customer needs to pay more, hit submit. Enter new price.");
			customer=input.nextDouble();
		}
		System.out.println(Math.round((customer-sum) * 100.0) / 100.0);
	}
	public void BitwiseOperations() {
		ArrayList<Boolean> one = new ArrayList<Boolean>();
		ArrayList<Boolean> two = new ArrayList<Boolean>();
		boolean meme = false;
		System.out.print("Gate: ");
		String gate = input.next();
		gate=gate.toLowerCase();
		System.out.println("Num of modules: ");
		int modules = input.nextInt();
		System.out.println("Time mode y/n?");
		if (input.next().toLowerCase().substring(0,1).equals("y")) {
			meme=true;
		}
		//byte 1
		if (batteries==holders)one.add(true);else one.add(false);
		if (ports.contains("parallel"))one.add(true);else one.add(false);
		if (indicators.contains("NSA*"))one.add(true);else one.add(false);
		one.add(meme);
		if (numOfLitIndicators()>1)one.add(true);else one.add(false);
		if (modules%3==0)one.add(true);else one.add(false);
		if (batteries-((batteries-holders)*2)<2)one.add(true);else one.add(false);
		if (ports.size()<4)one.add(true);else one.add(false);
		//byte 2
		if (holders!=0) {
			if (batteries/holders!=2)two.add(true);else two.add(false);
		}
		else {
			two.add(false);
		}
		if (ports.size()>2)two.add(true);else two.add(false);
		if (holders>1)two.add(true);else two.add(false);
		if (indicators.contains("BOB*"))two.add(true);else two.add(false);
		if (indicators.size()-numOfLitIndicators()>1)two.add(true);else two.add(false);
		if (lastDigit%2==1)two.add(true);else two.add(false);
		if (modules%2==0)two.add(true);else two.add(false);
		if (batteries>1)two.add(true);else two.add(false);
		
		if (gate.equals("and"))for (int i=0; i<8; i++)System.out.print((one.get(i)&&two.get(i))+" ");
		if (gate.equals("or"))for (int i=0; i<8; i++)System.out.print((one.get(i)||two.get(i))+" ");
		if (gate.equals("xor"))for (int i=0; i<8; i++)System.out.print(((one.get(i)&&!two.get(i))||(!one.get(i)&&two.get(i)))+" ");
		if (gate.equals("not"))for (int i=0; i<8; i++)System.out.print(!one.get(i)+" ");
	}
	public void BlindAlley() {
		ArrayList<Integer> points = new ArrayList<Integer>();
		while (points.size()<8)points.add(0);
		boolean even =false;
		for (int i = 0; i <6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				if (x%2==0)even=true;
			}
			catch(NumberFormatException e) {}
		}
		if (indicators.contains("BOB"))points.set(0, points.get(0)+1);
		if (indicators.contains("CAR*"))points.set(0, points.get(0)+1);
		if (indicators.contains("IND*"))points.set(0, points.get(0)+1);
		if (holders%2==0)points.set(0, points.get(0)+1);
		
		if (indicators.contains("CAR"))points.set(1, points.get(1)+1);
		if (indicators.contains("NSA"))points.set(1, points.get(1)+1);
		if (indicators.contains("FRK*"))points.set(1, points.get(1)+1);
		if (ports.contains("rj45"))points.set(1, points.get(1)+1);
		
		if (indicators.contains("FRQ"))points.set(2, points.get(2)+1);
		if (indicators.contains("IND"))points.set(2, points.get(2)+1);
		if (indicators.contains("TRN"))points.set(2, points.get(2)+1);
		if (ports.contains("dvi"))points.set(2, points.get(2)+1);
		
		if (indicators.contains("SIG"))points.set(3, points.get(3)+1);
		if (indicators.contains("SND"))points.set(3, points.get(3)+1);
		if (indicators.contains("NSA*"))points.set(3, points.get(3)+1);
		if (batteries%2==0)points.set(3, points.get(3)+1);
		
		if (indicators.contains("BOB*"))points.set(4, points.get(4)+1);
		if (indicators.contains("CLR*"))points.set(4, points.get(4)+1);
		if (ports.contains("serial"))points.set(4, points.get(4)+1);
		if (ports.contains("ps2"))points.set(4, points.get(4)+1);
		
		if (indicators.contains("FRQ*"))points.set(5, points.get(5)+1);
		if (indicators.contains("SIG*"))points.set(5, points.get(5)+1);
		if (indicators.contains("TRN*"))points.set(5, points.get(5)+1);
		if (even)points.set(5, points.get(5)+1);
		
		if (indicators.contains("FRK"))points.set(6, points.get(6)+1);
		if (indicators.contains("MSA*"))points.set(6, points.get(6)+1);
		if (ports.contains("parallel"))points.set(6, points.get(6)+1);
		if (vowel)points.set(6, points.get(6)+1);
		
		if (indicators.contains("CLR"))points.set(7, points.get(7)+1);
		if (indicators.contains("MSA"))points.set(7, points.get(7)+1);
		if (indicators.contains("SND*"))points.set(7, points.get(7)+1);
		if (ports.contains("rca"))points.set(7, points.get(7)+1);
		
		int big = 0;
		ArrayList<Integer> bigs = new ArrayList<Integer>();
		for (int i = 0; i<points.size(); i++) {
			if (points.get(i)>big) {
				big=points.get(i);
				bigs.clear();
			}
			if (points.get(i)==big) {
				if (i>1)bigs.add(i+2);
				else bigs.add(i+1);
			}
		}
		System.out.println(bigs);
	}
	public void CaesarCipher() {
		int offset = 0;
		if (vowel)offset--;
		offset+=batteries;
		if (lastDigit%2==0)offset++;
		if (indicators.contains("CAR")||indicators.contains("CAR*"))offset++;
		if (ports.contains("parallel")&&indicators.contains("NSA*"))offset=0;
		System.out.println("Offset is "+offset);
	}
	public void TheClock() {
		System.out.println("More than half time remaining? y/n");
		String remain = input.next().toLowerCase().substring(0,1);
		int hours = 11;
		int minutes = 51;
		System.out.println("0.Roman Numerals\n1.No Numerals\n2.Arabic Numerals");
		hours+=input.nextInt()*4;
		System.out.println("0.Silver Casing\n1.Gold Casing");
		hours+=2*input.nextInt();
		System.out.println("0.Numerals and tickmark colors match hands.\n1.Numerals and tickmark colors do NOT match hands.");
		hours+=1*input.nextInt();
		
		System.out.println("0.Hands are lines\n1.Hands are spades\n2.Hands are arrows");
		minutes+=20*input.nextInt();
		System.out.println("0.Red numerals/tickmarks\n1.Green numerals/tickmarks\n2.Blue numerals/tickmarks\n3.Gold numerals/tickmarks\n4.Black numerals/tickmarks");
		minutes+=4*input.nextInt();
		System.out.println("0.Black AM/PM text\n1.White AM/PM text");
		minutes+=2*input.nextInt();
		System.out.println("0.Seconds hand present\n1.No seconds hand");
		minutes+=input.nextInt();
		
		if (remain.equals("y"))System.out.println("Add "+antiModulo(hours, 12)+" hours, and "+antiModulo(minutes,60)+" minutes.");
		else System.out.println("Subtract "+antiModulo(hours, 12)+" hours, and "+antiModulo(minutes,60)+" minutes.");
	}
	public void CombinationLock() {
		System.out.print("Num of modules: ");
		int modules = input.nextInt();
		System.out.println("Num of solved modules: ");
		int solved = input.nextInt();
		int dig1 = (lastDigit+solved+batteries)%20;
		int dig2 = (modules+solved)%20;
		System.out.println(dig1+" "+dig2);
	}
	public void ComplicatedButtons() {
		String[] labels = new String[3];
		String[] colors = new String[3];
		for (int i = 0; i <3; i++) {
			System.out.println("Color/Label 1 char each");
			String j = input.next().toLowerCase();
			labels[i]=j.substring(1);
			colors[i]=j.substring(0,1);
		}
		ArrayList<String> answers = new ArrayList<String>();
		for (int i = 0; i <3; i++) {
			if (colors[i].equals("w")) {
				if (i==1&&labels[i].equals("p")&&holders>1)answers.add("press ");
				else if (i==1)answers.add("don't ");
				else answers.add("press ");
			}
			if (colors[i].equals("r")) {
				if (i==1)if(holders>2)answers.add("press ");else answers.add("don't ");
				else if (labels[i].equals("p"))answers.add("press ");
				else answers.add("repeat ");
			}
			if (colors[i].equals("b")) {
				if (i==1)if(ports.contains("serial"))answers.add("press");else answers.add("don't ");
				else if (labels[i].equals("p"))answers.add("don't ");
				else answers.add("repeat ");
			}
			if (colors[i].equals("p")) {
				if (i==1&&labels[i].equals("p"))answers.add("don't ");
				else if (labels[i].equals("p"))if (ports.contains("serial"))answers.add("press ");else answers.add("don't ");
				else answers.add("repeat ");
			}
		}
		if (labels[0].equals("p")) {
			if (batteries==0||batteries==1)System.out.println(answers.get(0)+answers.get(1)+answers.get(2)+"1, 2, 3 button 2 is correct if no buttons pressed");
			if (batteries==2||batteries==3)System.out.println(answers.get(1)+answers.get(2)+answers.get(0)+"2, 3, 1 button 3 is correct if no buttons pressed");
			if (batteries==4||batteries==5)System.out.println(answers.get(2)+answers.get(0)+answers.get(1)+"3, 1, 2 button 1 is correct if no buttons pressed");
			if (batteries>5)System.out.println(answers.get(0)+answers.get(1)+answers.get(2)+"1, 2, 3 button 2 is correct if no buttons pressed");
		}
		if (labels[0].equals("h")) {
			if (batteries==0||batteries==1)System.out.println(answers.get(1)+answers.get(0)+answers.get(2)+"2, 1, 3 button 1 is correct if no buttons pressed");
			if (batteries==2||batteries==3)System.out.println(answers.get(2)+answers.get(1)+answers.get(0)+"3, 2, 1 button 2 is correct if no buttons pressed");
			if (batteries==4||batteries==5)System.out.println(answers.get(0)+answers.get(2)+answers.get(1)+"1, 3, 2 button 3 is correct if no buttons pressed");
			if (batteries>5)System.out.println(answers.get(1)+answers.get(2)+answers.get(0)+"2, 3, 1button 3 is correct if no buttons pressed");
		}
		if (labels[0].equals("d")) {
			if (batteries==0||batteries==1)System.out.println(answers.get(2)+answers.get(0)+answers.get(1)+"3, 1, 2 button 1 is correct if no buttons pressed");
			if (batteries==2||batteries==3)System.out.println(answers.get(0)+answers.get(1)+answers.get(2)+"1, 2, 3 button 2 is correct if no buttons pressed");
			if (batteries==4||batteries==5)System.out.println(answers.get(1)+answers.get(0)+answers.get(2)+"2, 1, 3 button 1 is correct if no buttons pressed");
			if (batteries>5)System.out.println(answers.get(2)+answers.get(0)+answers.get(1)+"3, 1, 2 button 1 is correct if no buttons pressed");
		}
	}
	public void IdentityParade() {
		ArrayList<String> hair = new ArrayList<String>();
		ArrayList<String> build = new ArrayList<String>();
		ArrayList<String> attire = new ArrayList<String>();
		while (hair.size()<3) {
			System.out.println("Current hair colors "+hair+". Enter new hair.");
			String j = input.next().toLowerCase();
			if (j.equals("blond"))j="blonde";
			if (j.equals("gray"))j="grey";
			hair.add(j);
		}
		System.out.println("Received.");
		while (build.size()<3) {
			System.out.println("Current builds "+build+". Enter new build.");
			build.add(input.next().toLowerCase());
		}
		System.out.println("Received.");
		while (attire.size()<3) {
			System.out.println("Current attires "+attire+". Enter new attire. tanktop is one word.");
			String a = input.next().toLowerCase();
			if (a.equals("t-shirt"))a="tshirt";
			attire.add(a);
		}
		String suspect = "";
		if (build.contains("fat")) {
			if (hair.contains("red")&&attire.contains("jumper"))suspect="Nate Red Jumper Fat";
			if (hair.contains("blonde")&&attire.contains("suit"))suspect="Louise Blonde Suit Fat";
			if (hair.contains("black")&&attire.contains("tshirt"))suspect="Harriet Black Tshirt Fat";
		}
		if (build.contains("hunched")) {
			if (hair.contains("brown")&&attire.contains("suit"))suspect="Andy Brown Suit Hunched";
			if (hair.contains("white")&&attire.contains("blazer"))suspect="Quentin White Blazer Hunched";
			if (hair.contains("red")&&attire.contains("hoodie"))suspect="Chrissie Red Hoodie Hunched";
		}
		if (build.contains("muscular")) {
			if (hair.contains("red")&&attire.contains("tanktop"))suspect="James Red Tanktop Muscular";
			if (hair.contains("black")&&attire.contains("jumper"))suspect="Rhiannon Black Jumper Muscular";
			if (hair.contains("blonde")&&attire.contains("tshirt"))suspect="Penny Blonde Tshirt Muscular";
		}
		if (build.contains("short")) {
			if (hair.contains("white")&&attire.contains("tanktop"))suspect="Kayleigh White Tanktop Short";
			if (hair.contains("grey")&&attire.contains("blazer"))suspect="Gemma Grey Blazer Short";
			if (hair.contains("blonde")&&attire.contains("tanktop"))suspect="Dylan Blonde Tanktop Short";
		}
		if (build.contains("slim")) {
			if (hair.contains("brown")&&attire.contains("blazer"))suspect="Megan Brown Blazer Slim";
			if (hair.contains("grey")&&attire.contains("suit"))suspect="Eddie Grey Suit Slim";
			if (hair.contains("black")&&attire.contains("hoodie"))suspect="Oscar Black Hoodie Slim";
		}
		if (build.contains("tall")) {
			if (hair.contains("brown")&&attire.contains("hoodie"))suspect="Fiona Brown Hoodie Tall";
			if (hair.contains("white")&&attire.contains("jumper"))suspect="Ian White Jumper Tall";
			if (hair.contains("grey")&&attire.contains("tshirt"))suspect="Ben Grey Tshirt Tall";
		}
		System.out.println(suspect);		
	}
	public void LogicGates() {
		ArrayList<String> gates = new ArrayList<String>();
		while (gates.size()<4) {
			System.out.println("Current gates "+gates+" enter new gates.");
			gates.add(input.next().toLowerCase());
		}
		ArrayList<String> gatesMap = new ArrayList<String>();
		String [] j = {"and","or","xor","nand","nor","xnor"};
		for (String i:j)gatesMap.add(i);
		boolean dupeFound = false;
		for (String i : gates)if (countOf(gates, i)>1)dupeFound=true;
		String gateE = gatesMap.get((gatesMap.indexOf(gates.get(0))+gatesMap.indexOf(gates.get(1))+1)%6);
		while (dupeFound&&gates.indexOf(gateE)>-1)gateE=gatesMap.get((gatesMap.indexOf(gateE)+1)%6);
		gates.add(gateE);
		for (String i : gates)if (countOf(gates, i)>1)dupeFound=true;
		String gateF = gatesMap.get((gatesMap.indexOf(gates.get(4))+gatesMap.indexOf(gates.get(2))+1)%6);
		while (dupeFound&&gates.indexOf(gateF)>-1)gateF=gatesMap.get((gatesMap.indexOf(gateF)+1)%6);
		gates.add(gateF);
		for (String i : gates)if (countOf(gates, i)>1)dupeFound=true;
		String gateG = gatesMap.get((gatesMap.indexOf(gates.get(5))+gatesMap.indexOf(gates.get(3))+1)%6);
		while (dupeFound&&gates.indexOf(gateG)>-1)gateG=gatesMap.get((gatesMap.indexOf(gateG)+1)%6);
		gates.add(gateG);
		System.out.println(gates);
	}
	public void Mashgebra() {
		
		System.out.print("Want to do mashematics? y/n: ");
		if (input.next().substring(0, 1).toLowerCase().equals("y")) {
		
			System.out.print("1st number: ");
			int num1 = input.nextInt();
			System.out.print("First operand: ");
			String operand1 = input.next();
			System.out.print("2nd number: ");
			int num2 = input.nextInt();
			System.out.print("2nd operand: ");
			String operand2 = input.next();
			System.out.print("3rd number: ");
			int num3 = input.nextInt();
			double asdf = 0;
			if ((operand1.equals("+")||operand1.equals("-"))&&(operand2.equals("*")||operand2.equals("/"))) {
				asdf =doMath(num1, doMath(num2, num3, operand2), operand1);
			}
			else asdf = doMath(doMath(num1,num2,operand1),num3,operand2);
			while (asdf<0)asdf+=50;
			while (asdf>99)asdf-=50;
			System.out.println(asdf);
		}
		else {
			int x = sumOfSerialDigits();
			int y = indicators.size()-ports.size();
			System.out.print("Modules: ");
			int modules = input.nextInt();
			int z = modules+(2*(batteries-holders)*(batteries-(2*(batteries-holders))));
			if (holders>2)x+=2;
			if (ports.contains("rj45"))x-=1;
			if (indicators.contains("BOB*"))x+=4;
			if (vowel)x-=3;
			if (holders<3)y-=2;
			if (ports.contains("serial"))y+=3;
			if (indicators.contains("FRQ"))y-=5;
			if (isPrime(sumOfSerialDigits()))y+=4;
			if (holders==0)z+=3;
			if (ports.contains("parallel"))z-=6;
			if (indicators.contains("MSA*"))z+=2;
			if (sumOfSerialDigits()%3==0)z+=1;
			System.out.println("X: "+x);
			System.out.println("Y: "+y);
			System.out.println("Z: "+z);
		}
	}
	public void FMN() {
		System.out.println("Enter your whole set of FMN numbers.");
		String stringnums = input.next();
		int big = 0;
		int numsInSerial = 0;
		int smalloddserial = 9;
		for (int i = 0; i<6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				if (x>big)big=x;
				if (x%2==1&&x<smalloddserial)smalloddserial=x;
				numsInSerial++;
			}
			catch(NumberFormatException e) {}
		}
		ArrayList<Integer> moduleNums = new ArrayList<Integer>();
		ArrayList<Integer> answer = new ArrayList<Integer>();
		while (stringnums.length()>1) {
			moduleNums.add(Integer.parseInt(stringnums.substring(0, 1)));
			stringnums=stringnums.substring(1);
		}
		moduleNums.add(Integer.parseInt(stringnums.substring(0, 1)));
		for (int i = 0; i < moduleNums.size(); i++) {
			int numtoadd = -9999;
			if (i<2) {
				if (i==0) {
					if (indicators.contains("CAR"))numtoadd=2;
					else if (numOfLitIndicators()-(indicators.size()-numOfLitIndicators())<0)numtoadd=7;
					else if (numOfLitIndicators()==indicators.size())numtoadd=numOfLitIndicators();
					else numtoadd=lastDigit;
				}
				else {
					if (ports.contains("serial")&&numsInSerial>2)numtoadd=3;
					else if (answer.get(0)%2==0)numtoadd=answer.get(0)+1;
					else numtoadd = answer.get(0)-1;
				}
			}
			else {
				if (answer.get(i-1)==0||answer.get(i-2)==0)numtoadd=big;
				else if (answer.get(i-1)%2==0&&answer.get(i-2)%2==0)numtoadd=smalloddserial;
				else {
					int j = answer.get(i-1)+answer.get(i-2);
					if (j>=10)numtoadd=1;
					else numtoadd=j%10;
				}
			}
			if (debug) {
				System.out.println("Adding "+numtoadd+" to "+moduleNums.get(i)+", position "+i);
			}
			answer.add((numtoadd+moduleNums.get(i))%10);
		}
		for (int i = 0; i < answer.size(); i ++)System.out.print(answer.get(i));
	}
	public void ForgetEverything() {
		System.out.print("Stage number (0 if done), -1 to check stages: ");
		String color = "";
		boolean nixietubes = true;
		boolean skiperrors = false;
		int asdf = -1;
		try {
			asdf = input.nextInt();
		}
		catch (InputMismatchException e) {
			skiperrors = true;
		}
		int stage = asdf;
		if (stage==-1) {
			for (int i = 0; i < stages.size(); i++) {
				if (i==0)System.out.println("1: "+sequence);
				else if (!stagecolors.get(i).equals("E"))System.out.println(stages.get(i)+": "+stagecolors.get(i).toUpperCase()+" "+validity.get(i).toString().substring(0, 1)+" "+stagenums.get(i));
				else System.out.println("MISSING STAGE"+(i+1));
			}
		}
		else if (stage!=0) {
			
			if (stage!=1) {
				System.out.print("Color: ");
				color = input.next().toLowerCase().substring(0,1);
				System.out.print("Present? y/n: ");
				String uservalidity = input.next().toLowerCase().substring(0,1);
				if (uservalidity.equals("t")||uservalidity.equals("y"))nixietubes=true;
				else nixietubes=false;
				System.out.print("Relevant Digit: ");
				int digit = 0;
				try {
					digit = input.nextInt();
				}
				catch (InputMismatchException j) {
					skiperrors = true;
				}
				if (!skiperrors) {
					while (stagenums.size()<stage) {
						validity.add(false);
						stagenums.add(0);
						stagecolors.add("E");
						stages.add(0);
					}
					stagenums.set(stage-1, digit);
				}
			}
			else {
				System.out.print("Enter full sequence: ");
				color = "STAGE1";
				if (stagenums.size()<1) {
					stagenums.add(0);
					stagecolors.add("E");
					validity.add(false);
					stages.add(0);
				}
				stagenums.set(0, 9999999);
				sequence = input.next();
			}
			
			if (!skiperrors) {
				stages.set(stage-1,stage);
				stagecolors.set(stage-1, color);
				validity.set(stage-1, nixietubes);
			}
		}
		else {
			//VALIDITY DETERMINATION
			for (int i = 2; i < validity.size(); i++) {
				if (validity.get(i-1)&&validity.get(i-2)) {
					validity.set(i, false);
				}
				if (!validity.get(i-1)&&!validity.get(i-2)) {
					validity.set(i, true);
				}
			}
			//REPLACING
			for (int i=1; i < stagenums.size(); i++) {
				if (validity.get(i)) {
					sequence = FEHelper(i%10, sequence, stagenums.get(i), stagecolors.get(i));
				}
			}			
			System.out.println(sequence);
		}
	//	System.out.println(stagenums);
	}
	public void ChordQualities() {
		String startingNote = "";
		ArrayList<Integer> distances = new ArrayList<Integer>();
		int[][] distanceLookupTable = {{4,3,3,2}, {3,4,3,2}, {4,3,4,1}, {3,4,4,1}, {3,1,6,2}, {3,3,4,2}, {2,2,3,5}, {2,1,4,5}, {4,4,2,2}, {4,4,3,1}, {5,2,3,2}, {3,5,3,1}};
		System.out.print("USE SHARPS INSTEAD OF FLATS\nFirst note: ");
		startingNote = input.next();
		int total = 0;
		for (int i = 0; i <3; i++) {
			System.out.print("Distance "+(i+1)+": ");
			int j = input.nextInt();
			total+=j;
			distances.add(j);
		}
		distances.add(12-total);
		int foundChord = -1;
		int posFromStartingNote = 0;
		ArrayList<Integer> copydistances = new ArrayList<Integer>(distances);
		for (int note = 0; note<4; note++) {
			int chordToTry = 0;
			while (chordToTry < 12&&foundChord==-1) {
				boolean fail = false;
				if (debug)System.out.print("Trying "+copydistances+" against ");
				if (debug)printArray(distanceLookupTable[chordToTry]);
				for (int pos = 0; pos<4; pos++) {
					if (distanceLookupTable[chordToTry][pos]!=copydistances.get(pos))fail=true;
				}
				
				if (!fail)foundChord = chordToTry;
				else chordToTry++;
			}
			if (foundChord==-1) {
				posFromStartingNote+=1;
				copydistances = shiftLeft(copydistances);
			}
		}
		
		
		ArrayList<String> notes = new ArrayList<String>();
		notes.add(startingNote);
		int startingPos = -1;
		switch (startingNote) {
		case "C":
			startingPos = 0;
			break;
		case "C#":
			startingPos = 1;
			break;
		case "D":
			startingPos = 2;
			break;
		case "D#":
			startingPos = 3;
			break;
		case "E":
			startingPos = 4;
			break;
		case "F":
			startingPos = 5;
			break;
		case "F#":
			startingPos = 6;
			break;
		case "G":
			startingPos = 7;
			break;
		case "G#":
			startingPos = 8;
			break;
		case "A":
			startingPos = 9;
			break;
		case "A#":
			startingPos = 10;
			break;
		case "B":
			startingPos = 11;
			break;
		}
		if (debug)System.out.println("chord "+foundChord + ", dist from starting "+posFromStartingNote+", startingPos of orig note was "+startingPos);
		int fix = startingPos;
		for (int i = 0; i < 3; i++) {
			switch ((distances.get(i)+fix)%12) {
			case 0:
				notes.add("C");
				break;
			case 1:
				notes.add("C#");
				break;
			case 2:
				notes.add("D");
				break;
			case 3:
				notes.add("D#");
				break;
			case 4:
				notes.add("E");
				break;
			case 5:
				notes.add("F");
				break;
			case 6:
				notes.add("F#");
				break;
			case 7:
				notes.add("G");
				break;
			case 8:
				notes.add("G#");
				break;
			case 9:
				notes.add("A");
				break;
			case 10:
				notes.add("A#");
				break;
			case 11:
				notes.add("B");
				break;
			}
			fix += distances.get(i);
		}
		if (debug)System.out.println(notes);
		String root = notes.get(posFromStartingNote);
		if (debug)System.out.println("Original root is "+root);
		
		//Root 2 Quality
		int newQuality = -1;
		switch (root) {
		case "A":
			newQuality = 11;
			break;
		case "A#":
			newQuality = 9;
			break;
		case "B":
			newQuality = 1;
			break;
		case "C":
			newQuality = 5;
			break;
		case "C#":
			newQuality = 7;
			break;
		case "D":
			newQuality = 2;
			break;
		case "D#":
			newQuality = 4;
			break;
		case "E":
			newQuality = 10;
			break;
		case "F":
			newQuality = 6;
			break;
		case "F#":
			newQuality = 0;
			break;
		case "G":
			newQuality = 3;
			break;
		case "G#":
			newQuality = 8;
			break;
		}
		//Quality 2 Root
		switch (foundChord) {
		case 0:
			System.out.println("G");
			break;
		case 1:
			System.out.println("G#");
			break;
		case 2:
			System.out.println("A#");
			break;
		case 3:
			System.out.println("F");
			break;
		case 4:
			System.out.println("A");
			break;
		case 5:
			System.out.println("C#");
			break;
		case 6:
			System.out.println("D#");
			break;
		case 7:
			System.out.println("E");
			break;
		case 8:
			System.out.println("F#");
			break;
		case 9:
			System.out.println("C");
			break;
		case 10:
			System.out.println("D");
			break;
		case 11:
			System.out.println("B");
			break;
		}
		printArray(distanceLookupTable[newQuality]);
	}
	public void Lightspeed() {
		//OH BOY HERE WE GO
		String[][] quadrants = {{"GDBA", "DGBA", "BDAG"}, {"ADBG", "BADG", "DBAG"},{"ABGD", "BGDA", "DAGB"}};
		
		String[][] planetnames= {{"Risa", "Bajor XI", "Talos IV", "Galor IV", "Bolarus IX", "Malcor III", "Ferasa Prime", "Cardassia Prime", "Sol III"},
								 {"Ba'ku", "Ceti Alpha V",  "Romulus", "Andor", "Rigel VIII", "Qo'noS", "Eridon Prime", "Iconia", "Vulcan"},
								 {"Rakosa V", "Dryan II", "Ledos", "Ilidaria", "Hemikek IV", "Talax", "Avery III", "Ocampa", "Kyana Prime"},
								 {"T-Rogoran Prime", "Merakord II", "Skovar VI", "Brax", "Batrus", "Vandros IV", "Gaia IV", "Dosa II", "Callinon VII"}};
		int[][] planetdilithium = {{88, 74, 66, 58, 47, 39, 31, 26, 15},
								   {83, 75, 67, 58, 50, 42, 34, 21, 13},
								   {93, 84, 70, 62, 53, 46, 34, 27, 19},
								   {92, 81, 73, 64, 56, 40, 31, 24, 13}};
		String[][] planetclass = {{"M", "Y", "H", "L", "H", "L", "K", "K", "M"},
								  {"H", "L", "K", "Y", "K", "M", "H", "L", "M"},
								  {"M", "L", "K", "L", "Y", "M", "H", "K", "H"},
								  {"M", "Y", "M", "H", "L", "K", "L", "K", "H"}};
		String[][] officernames = {{"Darwin F", "Lang T", "McKenzie W", "Suder L", "Telfer W"},
								   {"Jetal A", "Kaplan M", "Kim H", "Wildman S", "Young C"},
								   {"Barclay R", "Nesterowicz J", "Paris T", "Torres B", "Tuvok"},
								   {"Brownfield, D", "Cavit", "Data", "Howard M", "La Forge G"},
								   {"Chakotay", "Riker W", "Sisko B"}};
			
		String[][] officeravailibility= {{"9", "4", "7", "2", "1"}, 
										 {"46", "58", "13", "29", "70"},
										 {"2356", "2468", "1790", "3569", "1457"},
										 {"12569", "24680", "12345", "13579", "67890"},
										 {"246890", "123670", "135789"}};
		
		System.out.println("Enter a number, 1. pluto, 2.vortex, 3.coffee");
		int symbol = input.nextInt();
		System.out.println("Color, 1. yellow, 2. orange, 3. purple.");
		int color = input.nextInt();
		System.out.println("Green triangle, 1.top left, 2. top right, 3. bottom right, 4. bottom left");
		int greentriangle = input.nextInt();
		System.out.print("Antimatter: ");
		int antimatter = input.nextInt();
		System.out.print("Dilithium: ");
		int dilithium = input.nextInt();
		System.out.print("Shields: ");
		int shields = input.nextInt();
		System.out.print("Gimme stardate: ");
		double fakestardate = input.nextDouble();
		int stardate = ((int)(fakestardate * 10))/10;
		int substardate = ((int) (fakestardate*10))%10;
		String quadrant = quadrants[color-1][symbol-1].substring(greentriangle-1, greentriangle);
		//WARP SPEED
		int warp = -1;
		if (quadrant.equals("D")) {
			warp = antimatter/10 - (6-(shields/15));
		}
		else { //INCORRECT FOR SHIELDS AT 100
			warp = antimatter/10 - (3-(shields/25));
		}
		if (quadrant.equals("A"))warp-=2;
		if (warp<1)warp=1;
		System.out.println("Warp Speed: "+warp);
		//PLANET CALCULATION
		int quadrantint= -1;
		if (quadrant.equals("A"))quadrantint=0;
		if (quadrant.equals("B"))quadrantint=1;
		if (quadrant.equals("D"))quadrantint=2;
		if (quadrant.equals("G"))quadrantint=3;
		int startingpositioninlist = -1;
		for (int i = 0; i < 9; i++) {
			if (planetdilithium[quadrantint][i]<=dilithium) {
				startingpositioninlist=i;
				break;
			}
		}
		int counter = 1;
		System.out.println("Pick the first planet you see that appears on this list. Then enter the number associated.");
		for (int i = startingpositioninlist; i<9; i++) {
			System.out.println(counter+". "+planetnames[quadrantint][i]);
			counter++;
		}
		int planetavailable = input.nextInt()+startingpositioninlist-1;
		
		//OFFICER CHOICE
		int startingpositioninofficers = -1;
		String classofplanet = planetclass[quadrantint][planetavailable];
		switch (classofplanet) {
		case "Y":
			startingpositioninofficers = 3;
			break;
		case "H":
			startingpositioninofficers = 2;
			break;
		case "L":
			startingpositioninofficers = 1;
			break;
		default:
			startingpositioninofficers = 0;
			break;
		}
		ArrayList<String> possibleOfficers = new ArrayList<String>();
		for (int i = startingpositioninofficers; i<5; i++) {
			for (int j = 0; j<officernames[i].length; j++) {
				if (officeravailibility[i][j].contains(""+substardate))possibleOfficers.add(officernames[i][j]);
			}
		}
		System.out.println("Send da first man u cee");
		System.out.println(possibleOfficers);
		System.out.println("What class of man did you send?");
		System.out.println("1. Cr, 2. En, 3. Lt, 4. Lt Cm., 5. Cm., 6. Cpt");
		int classofdudeman = input.nextInt();
		//ENCRYPTION KODE
		
		int sum = 0;
		for (int i = 0; i<4; i++) {
			sum+=stardate%10;
			stardate/=10;
		}
		String dig2 = (sum%10)+"";
		String dig1 = stardate+"";
		if (portPlates==-1) {
			System.out.println("Num of port plates: ");
			portPlates = input.nextInt();
		}
		String dig3 = (substardate+numOfLitIndicators()+portPlates)%10 +"";
		int digit4 = -1;
		if (batteries<4) {
			if (classofdudeman<5) {
				if (quadrant.equals("A")||quadrant.equals("B")) {
					digit4=7;
				}
				else digit4=1;
			}
			else {
				if (quadrant.equals("G")||quadrant.equals("B")) {
					digit4=5;
				}
				else digit4=3;
			}
		}
		if (batteries<8&&batteries>3) {
			if (classofdudeman<3) {
				if (quadrant.equals("A")||quadrant.equals("G")) {
					digit4=0;
				}
				else digit4=Integer.parseInt(dig1);
			}
			else {
				if (quadrant.equals("B")||quadrant.equals("D")) {
					digit4=8;
				}
				else digit4=6;
			}
		}
		if (batteries>7) {
			if (classofdudeman<4) {
				if (quadrant.equals("A")||quadrant.equals("D")) {
					digit4=2;
				}
				else digit4=Integer.parseInt(dig2);
			}
			else {
				if (quadrant.equals("G")||quadrant.equals("D")) {
					digit4=4;
				}
				else digit4=9;
			}
		}
		String encrypt = dig1+dig2+dig3+digit4;
		
		System.out.println("Final Entry");
		System.out.println("Warp: "+warp);
		System.out.println("Planet: "+planetnames[quadrantint][planetavailable]);
		System.out.println("Officer: Whoever you sent.");
		System.out.println("Encryption Code: "+encrypt);
	}
	public void TheTimeKeeper() {
		System.out.println("123##N all characters besides ## are first letter of colors. ## is the number");
		String parseme = input.next().toLowerCase();
		if (portPlates==-1) {
			System.out.print("Num of port plates: ");
			portPlates = input.nextInt();
		}
		System.out.print("Num of modules: ");
		int modules = input.nextInt();
		String led1 = parseme.substring(0, 1).toLowerCase();
		String led2 = parseme.substring(1,2).toLowerCase();
		String led3 = parseme.substring(2,3).toLowerCase();
		int numDisplayed = Integer.parseInt(parseme.substring(3,5));
		String numColor = parseme.substring(5,6).toLowerCase();
		int ledToPress = 0;
		int calc = numDisplayed;
		boolean rule9 = false;
		for (int i : serialConvertToNums()) calc+=i;
		for (int i = 0; i<6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				calc-=(2*x);
			}
			catch(NumberFormatException e) {}
		}
		if (debug)System.out.println("Rule 1 "+calc);
		if (led1.equals("w"))calc+=14;
		if (led1.equals("w")&&debug)System.out.println("Rule 2 "+calc);
		if (led2.equals(numColor))calc+=22;
		else calc+=13;
		if (led2.equals(numColor)&&debug)System.out.println("Rule 3 true "+calc);
		else if (debug)System.out.println("Rule 3 false "+calc);
		calc+=2*portPlates;
		if (ports.contains("dvi"))calc-=9;
		else if (debug)System.out.println("Rule 4 no dvi "+calc);
		if (ports.contains("dvi")&&debug)System.out.println("Rule 4 dvi "+calc);
		if (led1.equals(led2)&&led2.equals(led3))ledToPress=1;
		if (led1.equals(led2)&&led2.equals(led3)&&debug)System.out.println("Rule 5 LED"+ledToPress);
		if ((numColor.equals("r")||numColor.equals("b")||numColor.equals("g"))&&(!led1.equals("y")&&!led2.equals("y")&&!led3.equals("y")))calc+=numDisplayed;
		if (debug&&(numColor.equals("r")||numColor.equals("b")||numColor.equals("g"))&&(!led1.equals("y")&&!led2.equals("y")&&!led3.equals("y")))System.out.println("Rule 6 "+calc);
		if (modules>batteries+holders)calc-=18;
		if (modules>batteries+holders&&debug)System.out.println("Rule 7 "+calc);
		if (calc%2==0&&calc>72)calc/=2;
		if (calc%2==0&&calc>72&&debug)System.out.println("Rule 8 "+calc);
		if (ledToPress==0&&(led2.equals("g")||led2.equals("k"))) {
			ledToPress=2;
			rule9=true;
		}
		if (led2.equals("g")||led2.equals("k")&&debug)System.out.println("Rule 9 true LED"+ledToPress);
		else if (debug)System.out.println("Rule 9 false");
		for (int i = 0; i < 1; i++) {
			if (calc%23 < 2*ports.size()&&debug)System.out.println("Broke on rule 10");
			if (calc%23 < 2*ports.size())break;
			Calendar now = Calendar.getInstance();
			calc+= now.get(Calendar.MONTH)+1;
			if (debug)System.out.println("Rule 11 "+calc);
			
			if (numDisplayed>23)calc+=holders;
			else calc*=holders;
			
			if (numDisplayed>23&&debug)System.out.println("Rule 12 true "+calc);
			else if (debug)System.out.println("Rule 12 false "+calc);
			calc+=numOfLitIndicators()*2;
			calc-=(indicators.size()-numOfLitIndicators())*3;
			if (debug)System.out.println("Rule 13 "+calc);
			if (ledToPress==0&&led3.equals(led1)&&led1.equals(numColor)&&!led3.equals(led2)) {
				ledToPress=3;
				if (debug)System.out.println("Broke on rule 14 LED"+ledToPress);
				break;
			}
			
			if (rule9)calc+=10;
			else calc-=19;
			if (debug&&rule9)System.out.println("Rule 15 true "+calc);
			else if (debug)System.out.println("Rule 15 false "+calc);
			if (calc<0) {
				calc*=-2;
				if (debug)System.out.println("Broke on rule 16 "+calc);
				break;
			}
			calc*=3;
			if (debug)System.out.println("Rule 17 "+calc);		
			ArrayList<String> colors = new ArrayList<String>();
			ArrayList<Integer> numColors = new ArrayList<Integer>();
			colors.add(led1);
			colors.add(led2);
			colors.add(led3);
			colors.add(numColor);
			for (int j = 0; j < 4; j++) {
				switch (colors.get(j)) {
				case "y":
					numColors.add(6);
					break;
				case "r":
					numColors.add(3);
					break;
				case "b":
					numColors.add(4);
					break;
				case "w":
					numColors.add(5);
					break;
				case "k":
					numColors.add(5);
					break;
				case "g":
					numColors.add(5);
					break;
				}
			}
			if (numColors.get(0)+numColors.get(1)+numColors.get(2)>13) {
				calc+=numColors.get(3);
				if (debug)System.out.println("Rule 18 "+calc);
			}
			if (portPlates==0&&debug)System.out.println("Broke on rule 19");
			if (portPlates==0)break;
			if (!indicators.contains("FRK")&&!indicators.contains("FRK*")&&ledToPress==0) {
				if (numColors.get(0)>numColors.get(1)&&numColors.get(0)>numColors.get(2))ledToPress=1;
				if (numColors.get(1)>numColors.get(0)&&numColors.get(1)>numColors.get(2))ledToPress=2;
				if (numColors.get(2)>numColors.get(0)&&numColors.get(2)>numColors.get(1))ledToPress=3;
				if (debug)System.out.println("Rule 20 true LED"+ledToPress);
			}
			
			for (String l : indicators) if (l.length()==3)calc+=((int)l.toLowerCase().charAt(0))-96;
			if (numOfLitIndicators()==indicators.size())calc*=3;
			if (debug)System.out.println("Rule 21 "+calc);
		}
		if (calc<0)calc*=-1;
		if (calc<10)calc+=13;
		if (debug)System.out.println("Final Calc "+calc);
		if (ledToPress==0) {
			if (debug)System.out.println("Manually choosing LED");
			if (calc<100)ledToPress=1;
			else if (numColor.equals("g")&&!led1.equals("g"))ledToPress=3;
			else if (!led1.equals(led2)&&!led2.equals(led3)&&!led3.equals(led1)&&!led1.equals(numColor)&&!led2.equals(numColor)&&!led3.equals(numColor))ledToPress=1;
			else if (ports.contains("parallel"))ledToPress=2;
			else ledToPress=3;
			if (debug)System.out.println("Chosen "+ledToPress);
		}
		System.out.println("Press led "+ledToPress+" at "+calc/60+":"+calc%60);
	}
	public void ColorGenerator() {
		int[] fix = serialConvertToNums();
		for (int i = 0; i < 6; i++) {
			fix[i] = fix[i]%16;
		}
		int red = fix[0]*16+fix[1];
		int green = fix[2]*16+fix[3];
		int blue = fix[4]*16+fix[5];
		int red1 = red%10;
		red/=10;
		int red2 = red%10;
		red/=10;
		int red3 = red%10;
		int green1 = green%10;
		green/=10;
		int green2 = green%10;
		green/=10;
		int green3 = green%10;
		int blue1 = blue%10;
		blue/=10;
		int blue2 = blue%10;
		blue/=10;
		int blue3 = blue%10;
		System.out.println(red1+","+green1+","+blue1);
		System.out.println("Mult");
		System.out.println(red2+","+green2+","+blue2);
		System.out.println("Mult");
		System.out.println(red3+","+green3+","+blue3);
	}
	public void WasteManagement() {
		System.out.println("Ensure that the bomb has more than half time remaining.");
		boolean saveMyWorld = false;
		int consonants = 0;
		for (int i = 0; i<6; i++) {
			try {
				@SuppressWarnings("unused")
				int x = Integer.parseInt(serial.substring(i,i+1));
			}
			catch(NumberFormatException e) {
				String f = serial.substring(i,i+1);
				if (f.equals("s")||f.equals("a")||f.equals("v")||f.equals("e")||f.equals("m")||f.equals("y")||f.equals("w")||f.equals("o")||f.equals("r")||f.equals("l")||f.equals("d"))saveMyWorld=true;
				if (!(f.equals("a")||f.equals("e")||f.equals("i")||f.equals("o")||f.equals("u")))consonants++;
			}
		}
		
		
		int strikes = 0;
		System.out.print("Strikes: ");
		strikes=input.nextInt();
		if (emptyPortPlate==2) {
			System.out.println("Empty port plate? y/n");
			String emptyPortPlate = input.next().toLowerCase().substring(0,1);
			if (emptyPortPlate.equals("y"))this.emptyPortPlate=1;
			else this.emptyPortPlate=0;
		}
		System.out.print("Num of modules: ");
		int modules = input.nextInt();
		System.out.println("FMN y/n?");
		boolean FMN = input.next().toLowerCase().substring(0,1).equals("y");
		double paper = 0;
		double plastic = 0;
		double metal = 0;
		double leftovers = 0;

		if ((indicators.contains("IND")||indicators.contains("IND*"))&&batteries<5)paper+=19;
		if (indicators.contains("SND")||indicators.contains("SND*"))paper+=15;
		if (ports.contains("parallel"))paper-=44;
		if (batteries==0&&indicators.size()<3)paper+=154;
		if (saveMyWorld&&consonants<3)paper+=200;

		if ((indicators.contains("TRN")||indicators.contains("TRN*"))&&strikes!=1)plastic+=91;
		if ((indicators.contains("FRK")||indicators.contains("FRK*"))&&strikes!=2)plastic+=69;
		if (emptyPortPlate==1&&modules%2==0)plastic-=17;
		if ((indicators.contains("FRQ")||indicators.contains("FRQ*"))&&(2*(batteries-holders))>=((batteries-2*(batteries-holders))))plastic+=153;
		
		if (indicators.contains("BOB")||indicators.contains("BOB*"))metal+=199;
		if (indicators.contains("MSA")||indicators.contains("MSA*"))metal+=92;
		if ((indicators.contains("CAR")||indicators.contains("CAR*"))&&!ports.contains("rj45"))metal-=200;
		if (duplicatePorts()&&!ports.contains("dvi"))metal+=153;
		if (indicators.contains("SIG")||indicators.contains("SIG*"))metal+=99;
		if (FMN) {
			if (indicators.contains("BOB*")&&ports.size()>5)metal+=99;
			else metal-=84;
		}
		
		if (plastic<0)plastic*=-1;
		if (paper<0)paper*=-1;
		if (metal<0)metal*=-1;
		String paperInstruc = "No paper.";
		String plasticInstruc = "No plastic.";
		String metalInstruc = "No metal.";
		String leftoverInstruc = "No leftovers.";
		boolean done = false;
		boolean do3 = true;
		boolean do5 = true;
		if (debug) {
			System.out.println("Paper: "+paper);
			System.out.println("Plastic: "+plastic);
			System.out.println("Metal: "+metal);
		}
		if (paper+plastic+metal>695) {
			paperInstruc="Recycle "+paper+" paper.";
			plasticInstruc="Recycle "+plastic+" plastic.";
			metalInstruc="Recycle "+metal+" metal.";
			paper=0;
			plastic=0;
			metal=0;
			done=true;
		}
		if (metal>200&&!done) {
			metalInstruc="Recycle "+(0.75*metal)+" and waste "+(0.25*metal);
			do3 = false;
			metal=0;
		}
		if (do3&&metal<paper&&!done) {
			done=true;
			paperInstruc="Recycle "+paper+" paper.";
			paper=0;
			metalInstruc="Waste "+(0.25*metal)+" metal.";
			metal-=(0.25*metal);
			
			leftovers+=metal;
			leftovers+=paper;
			leftovers+=plastic;
			
			leftoverInstruc="Recycle "+(0.5*leftovers)+" leftovers.";
		}
		if (plastic<300&&plastic>100&&!done) {
			plasticInstruc="Recycle "+(0.5*plastic)+" plastic.";
			plastic-=(0.5*plastic);
			do5=false;
		}
		if (plastic<100&&plastic>10&&!done&&do5) {
			plasticInstruc = "Waste "+plastic+" plastic.";
			plastic=0;
		}
		if (paper<65&&!done) {
			if (do5) {
				paperInstruc="Waste "+(1.0/3.0 * paper)+" paper.";
				paper-=(1.0/3.0 * paper);
			}
			else {
				paperInstruc="Recycle "+paper+" paper.";
				paper=0;
			}
		}
		if (!done) {
			leftovers+=metal;
			leftovers+=paper;
			leftovers+=plastic;
			if (leftovers>100&&leftovers<300) {
				leftoverInstruc="Recycle "+leftovers+" leftovers.";
			}
			else {
				leftoverInstruc="Waste "+leftovers+" leftovers.";
			}
		}
		
		System.out.println(paperInstruc+"\n"+plasticInstruc+"\n"+metalInstruc+"\n"+leftoverInstruc);
	}
	public void TheMoon () {
		ArrayList<Integer> moving = new ArrayList<Integer>();
		String clockwise = "013578ACEGHJLMOQSTVXY".toLowerCase();
		int consonants = 0;
		int digits=0;
		int total = sumOfSerialDigits();
		for (int i = 0; i<6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				digits++;
				if (clockwise.contains(x+""))moving.add(2);
				else moving.add(-2);
			}
			catch(NumberFormatException e) {
				String f = serial.substring(i,i+1);
				if (!(f.equals("a")||f.equals("e")||f.equals("i")||f.equals("o")||f.equals("u")))consonants++;
				if (clockwise.contains(f))moving.add(2);
				else moving.add(-2);
			}
		}
		moving.add(2);
		if (debug)System.out.println(moving);
		System.out.println("Where is your first lit set?");
		String starting = input.next().toUpperCase();
		System.out.print("Num of modules: ");
		int modules = input.nextInt();
		if (portPlates==-1) {
		System.out.print("Num of port plates: ");
		portPlates=input.nextInt();
		}
		int led = -1;
		switch(starting) {
		case "N":
			led = 0;
			break;
		case "NE":
			led = 1;
			break;
		case "E":
			led = 2;
			break;
		case "SE":
			led = 3;
			break;
		case "S":
			led = 4;
			break;
		case "SW":
			led = 5;
			break;
		case "W":
			led = 6;
			break;
		case "NW":
			led = 7;
			break;
		}
		
		
		//From 1st lit going clockwise
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add((batteries-2*(batteries-holders)));
		values.add(2*(batteries-holders)%7);
		values.add(digits%7);
		values.add(portPlates%7);
		values.add(modules%7);
		values.add(consonants%7);
		values.add(indicators.size()%7);
		values.add(ports.size()%7);
		
		int position = antiModulo(total,4)+3;
		
		for (int i = 0; i < 8; i++) {
			
			while (position>7)position-=8;
			while (position<0)position+=8;
			
			while(values.get(position)==-1) {
				if (moving.get(i-1)<0)position-=1;
				if (moving.get(i-1)>0)position+=1;
				while (position>7)position-=8;
				while (position<0)position+=8;
			}
			if (debug)System.out.println(position+" position");
			String button = "";
			if (values.get(position)>2&&values.get(position)<5)button = "Inner ";
			if (values.get(position)<3)button="Outer ";
			if (values.get(position)>4)button = "Center";
			String helper = "";
			switch ((position+led)%8) {
			case 0:
				helper = "North";
				break;
			case 1:
				helper = "Northeast";
				break;
			case 2:
				helper = "East";
				break;
			case 3:
				helper = "Southeast";
				break;
			case 4:
				helper = "South";
				break;
			case 5:
				helper = "Southwest";
				break;
			case 6:
				helper = "West";
				break;
			case 7:
				helper = "Northwest";
				break;
			}
			System.out.println(button+helper);
			values.set(position, -1);
			if (i!=7)position+=moving.get(i);
			if (debug)System.out.println("Round "+i+", next position "+position);
		}
	}
	public void Bases() {
		ArrayList<Integer>firstNumber = new ArrayList<Integer>();
		ArrayList<Integer>secondNumber = new ArrayList<Integer>();
		ArrayList<Integer>answer = new ArrayList<Integer>();
		
		System.out.print("First number: ");
		String num1 = input.next();
		System.out.print("Operand: ");
		String operand = input.next();
		System.out.print("Second number: ");
		String num2 = input.next();
		if (portPlates==-1) {
			System.out.print("Num of port plates: ");
			portPlates = input.nextInt();
		}
		
		int base1 = numOfLitIndicators()+lastDigit;
		if (ports.contains("dvi"))base1+=3;
		int base2 = indicators.size()-numOfLitIndicators()+firstDigit();
		if (ports.contains("serial"))base2+=6;
		int base3 = portPlates;
		if (indicators.contains("IND")||indicators.contains("IND*"))base3+=2;
		
		if (base1!=10)base1=base1%7 + 2;
		if (base2!=10)base2=base2%7+2;
		if (base3!=10)base3=base3%7+2;
		
		for (int i = 0; i < num1.length(); i++) {
			firstNumber.add(Integer.parseInt(num1.substring(i, i+1)));
		}
		for (int i = 0; i < num2.length(); i++) {
			secondNumber.add(Integer.parseInt(num2.substring(i, i+1)));
		}
		int num1InBase10 = 0;
		for (int i = firstNumber.size()-1; i > -1; i--) {
			num1InBase10 += firstNumber.get(i)*Math.pow(base1, firstNumber.size()-i-1);
		}
		int num2InBase10 = 0;
		for (int i = secondNumber.size()-1; i > -1; i--) {
			num2InBase10 += secondNumber.get(i)*Math.pow(base2, secondNumber.size()-i-1);
		}
		
		int num3InBase10 =Math.abs((int) doMath(num1InBase10,num2InBase10,operand));
		int biggestPower = 0;
		if (debug)System.out.println("Num 1: "+num1InBase10);
		if (debug)System.out.println("Num 2: "+num2InBase10);
		if (debug)System.out.println(num3InBase10+"Final num");
		while (true) {
			if (num3InBase10-Math.pow(base3, biggestPower)<0)break;
			biggestPower++;
		}
		biggestPower--;
		
		while (biggestPower>-1) {
			if (debug)System.out.println(num3InBase10);
			int howMany = 0;
			while (num3InBase10>=0&&howMany<base3) {
				num3InBase10 -= Math.pow(base3, biggestPower);
				howMany++;
			}
			num3InBase10+=Math.pow(base3, biggestPower);
			howMany--;
			answer.add(howMany);
			biggestPower--;
		}
		if (debug)System.out.println(base1+", "+base2+", "+base3);
		for (int i : answer)System.out.print(i);
	}
	public void PlayfairCipher() {
		int total = sumOfSerialDigits();
		
		String[][] part2 = {{"SAFE","EFAS","MESSAGE","GROOVE"},{"CODE", "EDOC", "QUIET", "ETIUQ"},{"GROOVE", "EVOORG","TEIUQ","QUITE"},{"MESSAGE","EGASSEM","SAFE","EDOC"}};
		String[][] answerTable = {{"ABCD", "CDAB", "BADC", "DABC"},
								  {"BCDA", "DACB", "ADCB", "ABCD"},
								  {"CDAB", "ACBD", "DCBA", "BCDA"},
								  {"DABC", "CBDA", "CBAD", "CDAB"},
								  {"ABDC", "BDAC", "BACD", "DACB"},
								  {"BDCA", "DBCA", "ACDB", "ACBD"},
								  {"CABD", "BCAD", "CDBA", "CBDA"},
								  {"DCAB", "CADB", "DBAC", "BDAC"}};
		
		
		System.out.println("Color of text: \n1. Magenta\n2. Blue\n3. Orange\n4. Yellow");
		int color = input.nextInt();
		System.out.print("Message: ");
		String message = input.next();
		System.out.print("Strikes: ");
		int strikes = input.nextInt();
		//FIGURE OUT DAYS AND BOB RULE
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		int day = now.get(Calendar.DAY_OF_WEEK);
		if (indicators.contains("BOB*")) {
			if (day%2==0)day++;
		}
		String keypart1 = "";
		switch (day) {
		case 1:
			keypart1="BECOZY";
			break;
		case 2:
			keypart1="PLAY";
			break;
		case 3:
			keypart1="HIDDEN";
			break;
		case 4:
			keypart1="SECRET";
			break;
		case 5:
			keypart1="CIPHER";
			break;
		case 6:
			keypart1="FAIL";
			break;
		case 7:
			keypart1="PARTYHARD";
			break;
		}
		int rowkey2 = -1;
		if (ports.contains("serial")&&ports.contains("parallel")) {
			rowkey2 = 0;
		}
		else if (total>10) {
			rowkey2=1;
		}
		else if ((batteries-2*(batteries-holders))>2*((batteries-holders))){
			rowkey2=2;
		}
		else {
			rowkey2=3;
		}
		String keypart2 = part2[rowkey2][color-1];
		String finalkey = "";
		finalkey = keypart1+keypart2;
		if (!vowel) {
			finalkey = keypart2+keypart1;
		}
		if (strikes>0) {
			String keypart3 = "";
			if (strikes==1)keypart3 = "ONE";
			if (strikes==2)keypart3 = "TWO";
			if (strikes>2)keypart3="MANY";
			finalkey=finalkey+keypart3;
		}
		if (isPrime(total)) {
			String kl = finalkey;
			finalkey="";
			for (int i = 0; i < kl.length(); i++) {
				finalkey=kl.substring(i,i+1)+finalkey;
			}
		}
		String solved = PlayfairSolver(finalkey,message);
		int finalrow = -1;
		switch (solved) {
		case "strike":
			finalrow=0;
			break;
		case "strikx":
			finalrow=1;
			break;
		case "stryke":
			finalrow=2;
			break;
		case "strykx":
			finalrow=3;
			break;
		case "ztrike":
			finalrow=4;
			break;
		case "ztrikx":
			finalrow=5;
			break;
		case "ztryke":
			finalrow=6;
			break;
		case "ztrykx":
			finalrow=7;
			break;
		}
		try {
		System.out.println(answerTable[finalrow][color-1]);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Incorrect message.");
		}
	}
	public void UnfairCipher() {
		//https://ktane.timwi.de/HTML/Unfair%20Cipher.html
		if (portPlates==-1) {
			System.out.print("Num of port plates: ");
			portPlates = input.nextInt();
		}
		System.out.print("Modules: ");
		int modules = input.nextInt();
		System.out.print("Module ID: ");
		int moduleID = input.nextInt();
		System.out.println("Message: ");
		String moduleMessage = input.next().toLowerCase();
		String keyA= "";
		int[] serialnums = serialConvertToNums();
		String decimalStringSerial = "";
		for (int i = 0 ; i <6; i++) {
			if(serialnums[0]>19&&i==0) {}
			else {
				decimalStringSerial+=serialnums[i];
			}
		}
		int decimalSerial = Integer.parseInt(decimalStringSerial);
		if (serial.substring(3,4).equals("a")||serial.substring(3,4).equals("e")||serial.substring(3,4).equals("i")||serial.substring(3,4).equals("o")||serial.substring(3,4).equals("u")||serial.substring(4,5).equals("a")||serial.substring(4,5).equals("e")||serial.substring(4,5).equals("i")||serial.substring(4,5).equals("o")||serial.substring(4,5).equals("u")) {
			decimalSerial/=10;
		}
		if (debug)System.out.println("Decimal serial "+decimalSerial);
		String decimalHexReverse = "";
		while (decimalSerial>0) {
			int remainder = decimalSerial%16;
			decimalSerial/=16;
			String remainderToAdd = "";
			if (remainder<10) {
				remainderToAdd=remainder+"";
			}
			else {
				remainderToAdd=(char)(remainder+55)+"";
			}
			decimalHexReverse+=remainderToAdd;
		}
		String decimalHex = "";
		for (int i = 0; i < decimalHexReverse.length(); i++) {
			decimalHex=decimalHexReverse.substring(i,i+1)+decimalHex;
		}
		if (decimalHex.substring(0,1).equals("0"))decimalHex=decimalHex.substring(1);
		if (debug)System.out.println(decimalHex+"-decimalHex");
		boolean flag1 = false;
		int here = 0;
		for (int i = 0 ; i < decimalHex.length(); i++) {
			flag1 = false;
			if (i<decimalHex.length()-1) {
				try {
					here = Integer.parseInt(decimalHex.substring(i,i+1));
					flag1 = true;
					int next = Integer.parseInt(decimalHex.substring(i+1,i+2));
					int combined = Integer.parseInt(here+""+next);
					if (combined>9&&combined<27) {
						keyA+=(char)(combined+64)+"";
						i++;
					}
					else {
						if (here!=0)keyA+=(char)(here+64)+"";
					}
				}
				catch(NumberFormatException e) {
					if (flag1) {
						if (here!=0)keyA+=(char)(here+64)+"";
					}
					else {
						keyA+=decimalHex.substring(i,i+1);
					}
				}
			}
			else {
				try {
					here = Integer.parseInt(decimalHex.substring(i,i+1));
					if (here!=0)keyA+=(char)(here+64)+"";
				}
				catch (NumberFormatException e) {
					keyA+=decimalHex.substring(i,i+1);
				}
			}
		}
		keyA+=(char)(moduleID+64)+"";
		if (portPlates!=0)keyA+=(char)(portPlates+64)+"";
		if (holders!=0)keyA+=(char)(holders+64)+"";
		if (debug)System.out.println(keyA+"-KeyA");
		
		
		//KEY B
		String[][] keyBTable = {{"ABDG", "FEBH", "DBHI", "BLA", "DBIB", "AFEC", "AFCD", "CQE", "DEAF", "FET", "EFBA", "DEDB"},
				{"ABDA", "FEV", "DBHC", "BLD", "DBIE", "AFEF", "AFCG", "CQH", "DEAI", "FEAA", "EFAB", "DECC"},
				{"ABDB", "FEW", "DBHD", "BLE", "DBIF", "AFEG", "AFCH", "CQI", "DEAA", "FEAB", "EFAC", "DECD"},
				{"ABDC", "FEX", "DBHE", "BLF", "DBIG", "AFEH", "AFCI", "CQA", "DEAB", "FEAC", "EFAD", "DECE"},
				{"ABDD", "FEY", "DBHF", "BLG", "DBIH", "AFEI", "AFCA", "CQB", "DEAC", "FEAD", "EFAE", "DECF"},
				{"ABDE", "FEZ", "DBHG", "BLH", "DBII", "AFEA", "AFCB", "CQC", "DEAD", "FEAE", "EFAF", "DED"},
				{"ABDF", "FEBG", "DBHH", "BLI", "DBIA", "AFEB", "AFCC", "CQD", "DEAE", "FEAF", "EFB", "DEDA"}};
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		int day = now.get(Calendar.DAY_OF_WEEK);
		int month = now.get(Calendar.MONTH);
		String keyB = keyBTable[day-1][month];
		if (debug)System.out.println(keyB+"-keyB");
		
		//KEY C
		String keyC = PlayfairEncrypter(keyB,keyA);
		if (debug)System.out.println(keyC+"-keyC");
		//CAESAR
		int consonantsInSerial = 0;
		int vowelsInSerial = 0;
		for (int i = 0; i<6; i++) {
			try {
				@SuppressWarnings("unused")
				int x = Integer.parseInt(serial.substring(i,i+1));
			}
			catch(NumberFormatException e) {
				if (!(serial.substring(i,i+1).equalsIgnoreCase("A")||
						serial.substring(i,i+1).equalsIgnoreCase("E")||
						serial.substring(i,i+1).equalsIgnoreCase("I")||
						serial.substring(i,i+1).equalsIgnoreCase("O")||
						serial.substring(i,i+1).equalsIgnoreCase("U"))) {consonantsInSerial+=1;}
						else {
							vowelsInSerial++;
						}
			}
		}
		
		
		
		int caesarOffset = 0;
		
		if (ports.contains("parallel"))caesarOffset-=2;
		if (ports.contains("dvi"))caesarOffset-=2;
		if (ports.contains("serial"))caesarOffset-=2;
		if (ports.contains("rj45"))caesarOffset-=2;
		if (ports.contains("ps2"))caesarOffset-=2;
		if (ports.contains("rca"))caesarOffset-=2;
		caesarOffset+=portPlates;
		caesarOffset+=consonantsInSerial;
		caesarOffset-=vowelsInSerial*2;
		caesarOffset+=2*numOfLitIndicators();
		caesarOffset-=2*(indicators.size()-numOfLitIndicators());
		caesarOffset-=batteries;
		if (batteries==0)caesarOffset+=10;
		if (ports.size()==0)caesarOffset*=2;
		if (modules>30)caesarOffset/=2;
		if (debug)System.out.println(caesarOffset);
		String messageAfterCaesar = "";
		for (int i = 0; i < moduleMessage.length(); i++) {
			int newNum = (int) (moduleMessage.charAt(i)-caesarOffset);
			while (newNum<97)newNum+=26;
			while (newNum>122)newNum-=26;
			messageAfterCaesar+=(char)newNum+"";
		}
		if (debug)System.out.println(messageAfterCaesar+" message after caesar");
		String afterKeyC = PlayfairSolver(keyC, messageAfterCaesar);
		if (debug)System.out.println(afterKeyC+" after key C");
		String original = PlayfairSolver(keyA, afterKeyC);
		if (debug)System.out.println(original+" original");
		ArrayList<String> Instructions = new ArrayList<String>();
		for (int i = 0 ; i < original.length(); i=i+3) {
			Instructions.add(original.substring(i, i+3));
		}
		System.out.println(Instructions);
		String userAnswer = "";
		int colored = 0;
		for (int i = 0; i <Instructions.size(); i++) {
			String instruc = Instructions.get(i).toUpperCase();
			if (instruc.equals("REP")||instruc.equals("EAT")||instruc.equals("XEP")||instruc.equals("RXP")||instruc.equals("REX")||instruc.equals("XAT")||instruc.equals("EXT")||instruc.equals("EAX")) {
				if (i==0)userAnswer+="Inner\n";
				else {
					Instructions.set(i, Instructions.get(i-1));
					i--;
				}
			}
			if (instruc.equals("PCR")||instruc.equals("XCR")||instruc.equals("PXR")) {
				userAnswer+="RED\n";
				colored++;
			}
			if (instruc.equals("PCG")||instruc.equals("XCG")||instruc.equals("PXG")) {
				userAnswer+="GREEN\n";
				colored++;
			}
			if (instruc.equals("PCB")||instruc.equals("XCB")||instruc.equals("PXB")) {
				userAnswer+="BLUE\n";
				colored++;
			}
			if (instruc.equals("SUB")||instruc.equals("XUB")||instruc.equals("SXB")||instruc.equals("SUX"))userAnswer+="Outer when seconds match on timer\n";
			if (instruc.equals("MIT")||instruc.equals("XIT")||instruc.equals("MXT")||instruc.equals("MIX"))userAnswer+="Inner on "+(moduleID+colored+(i+1))%10+" last digit in timer.\n";
			if (instruc.equals("PRN")||instruc.equals("XRN")||instruc.equals("PXN")||instruc.equals("PRX")) {
				if (isPrime(moduleID%20))userAnswer+="Inner\n";
				else userAnswer+="Outer\n";
			}
			if (instruc.equals("CHK")||instruc.equals("XHK")||instruc.equals("CXK")||instruc.equals("CHX")) {
				if (!isPrime(moduleID%20))userAnswer+="Inner\n";
				else userAnswer+="Outer\n";
			}
			if (instruc.equals("BOB")||instruc.equals("XOB")||instruc.equals("BXB")||instruc.equals("BOX"))userAnswer+="Inner\n";
			if (instruc.equals("STR")||instruc.equals("IKE")||instruc.equals("XKE")||instruc.equals("IXE")||instruc.equals("IKX")) {
				userAnswer+="Colored buttons clockwise from Red (0) according to strikes\n";
				colored++;
			}
		}
		System.out.println(userAnswer);
	}
	public void TBCC() {
		int total = sumOfSerialDigits();
		ArrayList<Integer> buttons = new ArrayList<Integer>();
		System.out.println("Your color code (one letter): ");
		String codeString = input.next().toLowerCase();
		for (int i = 0 ; i < 10; i++) {
			if (codeString.substring(i,i+1).equals("r")) {
				buttons.add(0);
			}
			if (codeString.substring(i,i+1).equals("g")) {
				buttons.add(1);
			}
			if (codeString.substring(i,i+1).equals("b")) {
				buttons.add(2);
			}
		}
		ArrayList<Integer> stage1Stages = new ArrayList<Integer>();
		for (int i = 0; i < 11; i++) {
			stage1Stages.add((i+lastDigit)%10);
		}
		ArrayList<Integer> stage2Stages = new ArrayList<Integer>();
		for (int i = 10; i > -1; i--) {
			stage2Stages.add((i+total)%10);
		}
		for (int i = 0 ; i < 11; i ++) {
			switch (stage1Stages.get(i)) {
			case 0:
				for (int j = 0; j < 10; j++) {
					buttons.set(j, (buttons.get(j)+4)%3);
				}
				int temp = lastDigit;
				if (temp==0)temp=10;
				buttons.set(temp-1, (buttons.get(temp-1)+2)%3);
				break;
			case 1:
				boolean flag = true;
				for (int j = 0; j<4; j++) {
					if (buttons.get(j)==buttons.get(j+1)&&flag) {
						flag=false;
						if (TBColor(buttons.get(j+1)).equals("r")) {
							buttons.set(j+1, 1);
						}
						else {
							buttons.set(j+1, 0);
						}
					}
				}
				flag = true;
				for (int j = 5; j<9; j++) {
					if (buttons.get(j)==buttons.get(j+1)&&flag) {
						flag = false;
						if (TBColor(buttons.get(j+1)).equals("r")) {
							buttons.set(j+1, 1);
						}
						else {
							buttons.set(j+1, 0);
						}
					}
				}
				break;
			case 2:
				String indices0 = "";
				String indices1 = "";
				String indices2 = "";
				for (int j = 0 ; j < 5; j ++) {
					if (buttons.get(j)==0) {

						indices0+=j;
					}
					if (buttons.get(j)==1) {

						indices1+=j;
					}
					if (buttons.get(j)==2) {

						indices2+=j;
					}
				}
				if (indices0.length()>2||indices1.length()>2||indices2.length()>2) {
					if (indices0.length()>2) {
						buttons.set(Integer.parseInt(indices0.substring(0, 1)), (buttons.get(Integer.parseInt(indices0.substring(0, 1)))+1)%3);
						buttons.set(Integer.parseInt(indices0.substring(1, 2)), (buttons.get(Integer.parseInt(indices0.substring(1, 2)))+2)%3);
					}
					if (indices1.length()>2) {
						buttons.set(Integer.parseInt(indices1.substring(0, 1)), (buttons.get(Integer.parseInt(indices1.substring(0, 1)))+1)%3);
						buttons.set(Integer.parseInt(indices1.substring(1, 2)), (buttons.get(Integer.parseInt(indices1.substring(1, 2)))+2)%3);
					}
					if (indices2.length()>2) {
						buttons.set(Integer.parseInt(indices2.substring(0, 1)), (buttons.get(Integer.parseInt(indices2.substring(0, 1)))+1)%3);
						buttons.set(Integer.parseInt(indices2.substring(1, 2)), (buttons.get(Integer.parseInt(indices2.substring(1, 2)))+2)%3);
					}
				}
				
				break;
			case 3:
				int place1 = buttons.get(0);
				int place2 = buttons.get(5);
				buttons.set(0, buttons.get(3));
				buttons.set(5, buttons.get(8));
				buttons.set(3, place1);
				buttons.set(8, place2);
				place1 = buttons.get(2);
				place2 = buttons.get(7);
				buttons.set(2, buttons.get(4));
				buttons.set(7, buttons.get(9));
				buttons.set(4, place1);
				buttons.set(9, place2);
				place1 = buttons.get(1);
				buttons.set(1,buttons.get(6));
				buttons.set(6, place1);
				break;
			case 4:
				if (buttons.get(0)==buttons.get(1)&&buttons.get(1)==buttons.get(2)&&buttons.get(2)==buttons.get(3)&&buttons.get(3)==buttons.get(4)){
					buttons.set(0, (buttons.get(0)+1)%3);
					buttons.set(2, (buttons.get(2)+1)%3);
					buttons.set(4, (buttons.get(4)+1)%3);
				}
				if (buttons.get(5)==buttons.get(6)&&buttons.get(6)==buttons.get(7)&&buttons.get(7)==buttons.get(8)&&buttons.get(8)==buttons.get(9)){
					buttons.set(5, (buttons.get(5)+1)%3);
					buttons.set(7, (buttons.get(7)+1)%3);
					buttons.set(9, (buttons.get(9)+1)%3);
				}
				break;
			case 5:
				for (int j = 0; j<5; j++) {
					if (buttons.get(j)==buttons.get(j+5)) {
						buttons.set(j, 2);
						if (lastDigit%2==0) {
							buttons.set(j+5, 1);
						}
						else {
							buttons.set(j+5, 0);
						}
					}
				}
				break;
			case 6:
				if (!buttons.contains(0)) {
					buttons.set(1, 0);
					buttons.set(5, 0);
					buttons.set(8, 0);
				}
				break;
			case 7:
				String greenIndexes = "";
				for (int j = 0; j<10; j++) {
					if (buttons.get(j)==1)greenIndexes+=j;
				}
				if (greenIndexes.length()>5) {
					buttons.set(Integer.parseInt(greenIndexes.substring(0, 1)), 0);
					buttons.set(Integer.parseInt(greenIndexes.substring(2, 3)), 2);
					buttons.set(Integer.parseInt(greenIndexes.substring(3, 4)), 0);
					if (greenIndexes.length()>7) {
						buttons.set(Integer.parseInt(greenIndexes.substring(7, 8)), 2);
					}
				}
				break;
			case 8:
				boolean flag11 = true;
				for (int j = 0 ; j < 3; j ++) {
					if (buttons.get(j)==buttons.get(j+1)&&buttons.get(j+1)==buttons.get(j+2)&&flag11) {
						flag11 = false;
						buttons.set(j+1, (buttons.get(j+1)+1)%3);
					}
				}
				flag11 = true;
				for (int j = 5; j < 8; j ++) {
					if (buttons.get(j)==buttons.get(j+1)&&buttons.get(j+1)==buttons.get(j+2)&&flag11) {
						flag11 = false;
						buttons.set(j+1, (buttons.get(j+1)+1)%3);
					}
				}
				break;
			case 9:
				for (int j = 0; j<4; j++) {
					if (buttons.get(j)==buttons.get(j+1)&&buttons.get(j)==buttons.get(j+5)&&buttons.get(j)==buttons.get(j+6)) {
						buttons.set(j, (buttons.get(j)+2)%3);
						buttons.set(j+6, (buttons.get(j+6)+2)%3);
						break;
					}
				}
				break;
			}//TBCC switch
			if (debug)System.out.println("Round "+(i+lastDigit)%10);
			if (debug)for (int l : buttons)System.out.print(TBColor(l));
			if (debug)System.out.println();
		}
		System.out.println("STAGE 1: ");
		for (int l =0; l<10; l++) {
			System.out.print(TBColor(buttons.get(l)));
			if (l==4)System.out.println();
		}
		System.out.println();
		buttons.clear();
		System.out.println("Your color code STAGE 2 (one letter): ");
		codeString = input.next().toLowerCase();
		for (int i = 0 ; i < 10; i++) {
			if (codeString.substring(i,i+1).equals("r")) {
				buttons.add(0);
			}
			if (codeString.substring(i,i+1).equals("g")) {
				buttons.add(1);
			}
			if (codeString.substring(i,i+1).equals("b")) {
				buttons.add(2);
			}
		}
		
		
		
		for (int i = 0 ; i < 11; i ++) {
			switch (stage2Stages.get(i)) {
			case 0:
				for (int j = 0; j < 10; j++) {
					buttons.set(j, (buttons.get(j)+4)%3);
				}
				int temp = total%10;
				if (temp==0)temp=10;
				buttons.set(temp-1, (buttons.get(temp-1)+2)%3);
				break;
			case 1:
				boolean flag = true;
				for (int j = 0; j<4; j++) {
					if (buttons.get(j)==buttons.get(j+1)&&flag) {
						flag=false;
						if (TBColor(buttons.get(j+1)).equals("r")) {
							buttons.set(j+1, 1);
						}
						else {
							buttons.set(j+1, 0);
						}
					}
				}
				flag = true;
				for (int j = 5; j<9; j++) {
					if (buttons.get(j)==buttons.get(j+1)&&flag) {
						flag = false;
						if (TBColor(buttons.get(j+1)).equals("r")) {
							buttons.set(j+1, 1);
						}
						else {
							buttons.set(j+1, 0);
						}
					}
				}
				break;
			case 2:
				String indices0 = "";
				String indices1 = "";
				String indices2 = "";
				for (int j = 0 ; j < 5; j ++) {
					if (buttons.get(j)==0) {

						indices0+=j;
					}
					if (buttons.get(j)==1) {

						indices1+=j;
					}
					if (buttons.get(j)==2) {

						indices2+=j;
					}
				}
				if (indices0.length()>2||indices1.length()>2||indices2.length()>2) {
					if (indices0.length()>2) {
						buttons.set(Integer.parseInt(indices0.substring(0, 1)), (buttons.get(Integer.parseInt(indices0.substring(0, 1)))+1)%3);
						buttons.set(Integer.parseInt(indices0.substring(1, 2)), (buttons.get(Integer.parseInt(indices0.substring(1, 2)))+2)%3);
					}
					if (indices1.length()>2) {
						buttons.set(Integer.parseInt(indices1.substring(0, 1)), (buttons.get(Integer.parseInt(indices1.substring(0, 1)))+1)%3);
						buttons.set(Integer.parseInt(indices1.substring(1, 2)), (buttons.get(Integer.parseInt(indices1.substring(1, 2)))+2)%3);
					}
					if (indices2.length()>2) {
						buttons.set(Integer.parseInt(indices2.substring(0, 1)), (buttons.get(Integer.parseInt(indices2.substring(0, 1)))+1)%3);
						buttons.set(Integer.parseInt(indices2.substring(1, 2)), (buttons.get(Integer.parseInt(indices2.substring(1, 2)))+2)%3);
					}
				}
				break;
			case 3:
				int place1 = buttons.get(0);
				int place2 = buttons.get(5);
				buttons.set(0, buttons.get(3));
				buttons.set(5, buttons.get(8));
				buttons.set(3, place1);
				buttons.set(8, place2);
				place1 = buttons.get(2);
				place2 = buttons.get(7);
				buttons.set(2, buttons.get(4));
				buttons.set(7, buttons.get(9));
				buttons.set(4, place1);
				buttons.set(9, place2);
				place1 = buttons.get(1);
				buttons.set(1,buttons.get(6));
				buttons.set(6, place1);
				break;
			case 4:
				if (buttons.get(0)==buttons.get(1)&&buttons.get(1)==buttons.get(2)&&buttons.get(2)==buttons.get(3)&&buttons.get(3)==buttons.get(4)){
					buttons.set(0, (buttons.get(0)+1)%3);
					buttons.set(2, (buttons.get(2)+1)%3);
					buttons.set(4, (buttons.get(4)+1)%3);
				}
				if (buttons.get(5)==buttons.get(6)&&buttons.get(6)==buttons.get(7)&&buttons.get(7)==buttons.get(8)&&buttons.get(8)==buttons.get(9)){
					buttons.set(5, (buttons.get(5)+1)%3);
					buttons.set(7, (buttons.get(7)+1)%3);
					buttons.set(9, (buttons.get(9)+1)%3);
				}
				break;
			case 5:
				for (int j = 0; j<5; j++) {
					if (buttons.get(j)==buttons.get(j+5)) {
						buttons.set(j, 2);
						if (lastDigit%2==0) {
							buttons.set(j+5, 1);
						}
						else {
							buttons.set(j+5, 0);
						}
					}
				}
				break;
			case 6:
				if (!buttons.contains(0)) {
					buttons.set(1, 0);
					buttons.set(5, 0);
					buttons.set(8, 0);
				}
				break;
			case 7:
				String greenIndexes = "";
				for (int j = 0; j<10; j++) {
					if (buttons.get(j)==1)greenIndexes+=j;
				}
				if (greenIndexes.length()>5) {
					buttons.set(Integer.parseInt(greenIndexes.substring(0, 1)), 0);
					buttons.set(Integer.parseInt(greenIndexes.substring(2, 3)), 2);
					buttons.set(Integer.parseInt(greenIndexes.substring(3, 4)), 0);
					if (greenIndexes.length()>7) {
						buttons.set(Integer.parseInt(greenIndexes.substring(7, 8)), 2);
					}
				}
				break;
			case 8:
				boolean flag11 = true;
				for (int j = 0 ; j < 3; j ++) {
					if (buttons.get(j)==buttons.get(j+1)&&buttons.get(j+1)==buttons.get(j+2)&&flag11) {
						flag11 = false;
						buttons.set(j+1, (buttons.get(j+1)+1)%3);
					}
				}
				flag11 = true;
				for (int j = 5; j < 8; j ++) {
					if (buttons.get(j)==buttons.get(j+1)&&buttons.get(j+1)==buttons.get(j+2)&&flag11) {
						flag11 = false;
						buttons.set(j+1, (buttons.get(j+1)+1)%3);
					}
				}
				break;
			case 9:
				for (int j = 0; j<4; j++) {
					if (buttons.get(j)==buttons.get(j+1)&&buttons.get(j)==buttons.get(j+5)&&buttons.get(j)==buttons.get(j+6)) {
						buttons.set(j, (buttons.get(j)+2)%3);
						buttons.set(j+6, (buttons.get(j+6)+2)%3);
						break;
					}
				}
				break;
			}//TBCC switch
			if (debug)System.out.println("Round "+(total-i+10)%10);
			if (debug)for (int l : buttons)System.out.print(TBColor(l));
			if (debug)System.out.println();
		}
		System.out.println("Stage 2: ");
		for (int l =0; l<10; l++) {
			System.out.print(TBColor(buttons.get(l)));
			if (l==4)System.out.println();
		}
	}
	public void BlackHole() {
		System.out.println("How many black holes?: ");
		int countOfBlackHoles = input.nextInt();
		ArrayList<Integer> numsToEnter = new ArrayList<Integer>();
		int[][] grid = {{3, 4, 1, 0, 2, 3, 1, 2, 0, 4},
					    {1, 3, 0, 2, 4, 1, 2, 3, 4, 0},
					    {3, 2, 4, 2, 1, 3, 0, 0, 1, 4},
					    {4, 0, 0, 1, 3, 4, 2, 2, 1, 3},
					    {1, 2, 1, 3, 0, 0, 4, 3, 4, 2},
					    {4, 0, 2, 3, 4, 1, 3, 0, 2, 1},
					    {2, 1, 3, 1, 3, 0, 4, 4, 0, 2},
					    {2, 4, 4, 0, 0, 2, 1, 1, 3, 3},
					    {0, 1, 3, 4, 2, 2, 0, 4, 3, 1},
					    {0, 3, 2, 4, 1, 4, 3, 1, 2, 0}};
		int xCoord = Integer.parseInt(serial.substring(2,3));
		int yCoord = lastDigit;
		int xMod = 0;
		int yMod = 0;
		for (int i = 0; i < countOfBlackHoles*7; i++) {
			int sumOfNums = 0;
			switch ((i+ports.size())%8) {
			case 0:
				xMod = 0;
				yMod = -1;
				break;
			case 1:
				xMod = 1;
				yMod = -1;
				break;
			case 2:
				xMod = 1;
				yMod = 0;
				break;
			case 3:
				xMod = 1;
				yMod = 1;
				break;
			case 4:
				xMod = 0;
				yMod = 1;
				break;
			case 5:
				xMod = -1;
				yMod = 1;
				break;
			case 6:
				xMod = -1;
				yMod = 0;
				break;
			case 7:
				xMod = -1;
				yMod = -1;
				break;
			}
			if (debug)System.out.println("Round "+i+" start. Current pos ("+xCoord+", "+yCoord+")");
			if (debug)System.out.println("Modifiers: "+xMod+", "+yMod);
			sumOfNums+=grid[yCoord][xCoord];
			if (debug)System.out.println("Added "+grid[yCoord][xCoord]);
			for (int j = 0; j < i; j++) {
				xCoord=(xCoord+xMod+10)%10;
				yCoord=(yCoord+yMod+10)%10;
				sumOfNums+=grid[yCoord][xCoord];
				if (debug)System.out.println("Added "+grid[yCoord][xCoord]);
			}
			xCoord=(xCoord+xMod+10)%10;
			yCoord=(yCoord+yMod+10)%10;
			numsToEnter.add(sumOfNums%5);
		}//BH For Loop
		System.out.println(numsToEnter);
		for (int i : numsToEnter) {
			switch (i) {
			case 0:
				System.out.println("hold, tick, release");
				break;
			case 1:
				System.out.println("tap, tick, tap");
				break;
			case 2:
				System.out.println("tap, tick, hold, tick, release");
				break;
			case 3:
				System.out.println("hold, tick, release, hold, tick, release");
				break;
			case 4:
				System.out.println("hold, tick, tick, release");
				break;
			}
		}
	}
	public void LightCycle() {
		String[][] table = {{"5/B", "B/R", "M/G", "Y/5", "4/1", "R/W", "6/4", "1/6", "2/3", "3/M", "G/Y", "W/2"},
				 {"2/R", "6/M", "4/3", "5/B", "R/5", "Y/2", "1/G", "M/Y", "W/6", "3/4", "B/W", "G/1"},
				 {"M/Y", "2/4", "Y/R", "3/5", "W/2", "G/B", "1/W", "R/3", "5/G", "4/6", "B/M", "6/1"},
				 {"5/6", "6/3", "1/4", "M/2", "R/Y", "2/M", "W/R", "B/G", "Y/W", "3/B", "G/1", "4/5"},
				 {"B/R", "W/2", "2/3", "1/4", "M/B", "5/6", "Y/W", "R/M", "G/Y", "6/G", "3/5", "4/1"},
				 {"R/Y", "2/G", "1/M", "Y/5", "5/R", "W/B", "6/3", "B/1", "M/4", "G/6", "3/2", "4/W"},
				 {"Y/1", "5/4", "2/W", "R/Y", "1/R", "B/3", "6/G", "G/6", "M/B", "W/5", "4/2", "3/M"},
				 {"3/5", "W/Y", "G/2", "2/B", "5/G", "M/R", "B/3", "1/4", "4/6", "Y/M", "6/W", "R/1"},
				 {"R/M", "4/5", "5/W", "B/1", "M/6", "3/2", "W/B", "G/Y", "Y/R", "1/4", "6/G", "2/3"},
				 {"W/B", "R/6", "5/Y", "4/1", "2/5", "Y/3", "M/W", "3/2", "B/G", "G/M", "1/R", "6/4"},
				 {"6/4", "B/2", "W/G", "R/5", "G/1", "2/Y", "Y/R", "M/B", "1/6", "3/W", "5/3", "4/M"},
				 {"6/4", "B/5", "W/6", "1/G", "R/2", "4/R", "G/W", "3/M", "2/B", "Y/3", "5/Y", "M/1"},
				 {"W/3", "3/G", "2/4", "Y/M", "M/2", "R/5", "6/R", "B/6", "G/Y", "5/B", "1/W", "4/1"},
				 {"1/Y", "6/M", "2/1", "G/R", "3/G", "5/B", "R/4", "4/3", "W/2", "Y/W", "B/5", "M/6"},
				 {"R/5", "3/G", "2/3", "W/4", "B/2", "1/M", "5/6", "M/1", "4/Y", "G/B", "6/R", "Y/W"},
				 {"1/4", "4/B", "6/2", "3/W", "M/R", "Y/6", "B/Y", "2/G", "5/M", "G/5", "R/3", "W/1"},
				 {"5/G", "M/B", "4/W", "Y/2", "R/M", "W/4", "6/1", "3/6", "B/Y", "1/5", "G/R", "2/3"},
				 {"M/G", "5/6", "G/M", "W/5", "Y/2", "R/4", "B/1", "1/B", "2/R", "4/3", "6/W", "3/Y"},
				 {"R/Y", "6/5", "5/G", "G/B", "W/M", "4/3", "1/W", "B/1", "3/6", "2/4", "Y/2", "M/R"},
				 {"G/3", "B/2", "6/W", "M/B", "1/5", "Y/4", "5/M", "W/R", "4/6", "3/Y", "2/G", "R/1"},
				 {"5/1", "W/3", "4/5", "3/4", "Y/W", "1/Y", "B/G", "6/2", "M/6", "G/R", "2/M", "R/B"},
				 {"M/6", "6/B", "1/G", "3/5", "W/R", "B/4", "G/M", "R/1", "2/W", "5/2", "4/Y", "Y/3"},
				 {"Y/M", "B/1", "5/3", "2/G", "3/2", "R/5", "1/4", "W/6", "4/W", "G/R", "M/Y", "6/B"},
				 {"4/2", "R/B", "W/5", "Y/M", "2/Y", "5/1", "B/R", "G/3", "M/G", "3/6", "6/W", "1/4"},
				 {"G/Y", "1/R", "5/4", "4/G", "3/B", "M/6", "2/5", "Y/2", "R/1", "W/3", "B/W", "6/M"},
				 {"G/B", "B/G", "1/5", "M/1", "3/M", "R/3", "Y/W", "6/Y", "5/2", "4/6", "W/R", "2/4"},
				 {"2/R", "R/B", "5/G", "W/2", "Y/1", "4/Y", "3/5", "1/M", "B/W", "G/6", "6/4", "M/3"},
				 {"R/4", "W/6", "3/2", "2/W", "4/Y", "6/5", "B/R", "5/G", "Y/B", "G/M", "M/1", "1/3"},
				 {"4/B", "B/3", "6/4", "W/1", "M/Y", "R/6", "G/5", "Y/W", "5/2", "2/R", "3/G", "1/M"},
				 {"B/6", "M/3", "4/B", "1/4", "2/5", "Y/1", "G/Y", "R/W", "W/G", "5/2", "6/M", "3/R"},
				 {"M/R", "2/B", "W/5", "6/Y", "B/3", "4/2", "G/1", "Y/6", "5/G", "3/M", "R/W", "1/4"},
				 {"Y/1", "5/6", "1/W", "W/4", "B/G", "G/5", "4/M", "2/B", "3/R", "6/3", "M/2", "R/Y"},
				 {"3/4", "W/B", "Y/G", "5/M", "R/1", "G/W", "1/2", "6/Y", "B/R", "M/6", "4/3", "2/5"},
				 {"4/G", "6/5", "Y/4", "G/B", "3/1", "M/Y", "5/3", "1/M", "2/R", "R/2", "B/W", "W/6"},
				 {"Y/B", "R/2", "W/R", "5/3", "1/W", "3/5", "B/M", "G/4", "6/Y", "4/G", "2/1", "M/6"},
				 {"G/Y", "3/1", "5/M", "R/2", "6/W", "M/B", "Y/6", "2/4", "4/G", "B/5", "1/R", "W/3"}};
		ArrayList<String> changes = new ArrayList<String>();
		int[] serialNums = new int [6];
		for (int i = 0; i <6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				serialNums[i]=x+26;
			}
			catch(NumberFormatException e) {
				serialNums[i]=((int)serial.charAt(i))-97;
			}
		}
		for (int i = 0; i<6; i++) {
			changes.add(table[serialNums[i]][(serialNums[5-i])/3]);
		}
		System.out.print("Sequence: ");
		String sequence = input.next().toUpperCase();
		for (String j : changes) {
			int indexOne = -8;
			int indexTwo = -50;
			String newSequence = "";
			try {
				indexOne = Integer.parseInt(j.substring(0,1))-1;
			}
			catch (NumberFormatException e) {
				indexOne = sequence.indexOf(j.substring(0,1));
			}
			try {
				indexTwo = Integer.parseInt(j.substring(2,3))-1;
			}
			catch (NumberFormatException e) {
				indexTwo = sequence.indexOf(j.substring(2,3));
			}
			for (int i = 0; i <6; i++) {
				if (i!=indexOne&&i!=indexTwo) {
					newSequence+=sequence.substring(i,i+1);
				}
				else {
					if (i==indexOne)newSequence+=sequence.substring(indexTwo,indexTwo+1);
					else newSequence+=sequence.substring(indexOne,indexOne+1);
				}
			}
			sequence=newSequence;
		}
		System.out.println(sequence);
	}
	public void SafetySafe() {
		int portTypes = 0;
		if (ports.contains("rj45"))portTypes++;
		if (ports.contains("dvi"))portTypes++;
		if (ports.contains("rca"))portTypes++;
		if (ports.contains("ps2"))portTypes++;
		if (ports.contains("serial"))portTypes++;
		if (ports.contains("parallel"))portTypes++;
		int indsMatchSerial = 0;
		if (debug)System.out.println("global from ports "+portTypes*7);
		for (String j : indicators) {
			boolean flagLit = false;
			boolean flagUnlit = false;
			if (j.length()>3) {
				for (int i = 0; i < j.length(); i++)if (serial.contains(j.substring(i, i+1).toLowerCase()))flagLit=true;
			}
			else {
				for (int i = 0; i < j.length(); i++)if (serial.contains(j.substring(i, i+1).toLowerCase()))flagUnlit=true;
			}
			if (flagLit)indsMatchSerial+=5;
			if (flagUnlit)indsMatchSerial++;
		}
		int globalOffset = portTypes*7+indsMatchSerial;
		if (debug)System.out.println(globalOffset+" global offset");
		int table[][] = {{8,3,4,8,9,0},
				 {10,1,3,7,3,8},
				 {2,1,1,5,3,6},
				 {11,6,11,11,7,7},
				 {0,5,5,8,2,1},
				 {4,2,7,7,1,5},
				 {7,4,4,2,10,5},
				 {8,3,6,6,6,5},
				 {0,11,0,0,9,10},
				 {2,11,8,0,5,6},
				 {5,2,5,1,0,4},
				 {1,9,8,11,11,11},
				 {1,7,9,5,6,2},
				 {9,5,1,4,4,9},
				 {5,9,8,10,2,8},
				 {3,10,9,1,9,7},
				 {4,10,6,1,4,8},
				 {8,0,4,0,6,11},
				 {9,4,0,6,3,10},
				 {7,6,7,11,5,3},
				 {11,9,6,3,11,1},
				 {11,11,2,8,1,0},
				 {6,0,11,6,11,2},
				 {4,2,7,2,8,10},
				 {10,7,10,10,8,9},
				 {3,7,1,10,0,4},
				 {7,0,3,5,8,6},
				 {9,10,10,9,1,2},
				 {2,5,11,7,7,3},
				 {10,8,10,4,10,4},
				 {6,8,0,3,5,0},
				 {6,3,3,3,0,11},
				 {1,1,5,2,7,3},
				 {0,6,2,4,2,1},
				 {5,4,9,9,10,7},
				 {3,8,2,9,4,9}};
		int[] dials = new int[6];
		int[] serialNums = new int [6];
		for (int i = 0; i <6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				serialNums[i]=x+26;
			}
			catch(NumberFormatException e) {
				serialNums[i]=((int)serial.charAt(i))-97;
			}
		}
		for (int i = 0; i < 5; i++) {
			dials[i]=(table[serialNums[i]][i]+globalOffset)%12;
			dials[5]+=table[serialNums[i]][5];
		}
		dials[5]=(dials[5]+(table[serialNums[5]][5]+globalOffset))%12;
		printArray(dials);
	}
	public void Morsematics() {
		String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "..-", ".--", "-..-", "-.--", "--.."};
		System.out.print("Enter first letter: ");
		String letterOne = input.next();
		int one = indexOf(morse,letterOne)+1;
		System.out.print("Enter second letter: ");
		String letterTwo = input.next();
		int two = indexOf(morse,letterTwo)+1;
		System.out.print("Enter third letter: ");
		String letterThree = input.next();
		int three = indexOf(morse,letterThree)+1;
		int charPair1 = ((int)serial.charAt(3))-96;
		int charPair2 = ((int)serial.charAt(4))-96;
		for (String ind : indicators) {
			if (ind.toLowerCase().contains((char)(one+96)+"")){
				if (ind.length()>3)charPair1++;
				else charPair2++;
			}
			if (ind.toLowerCase().contains((char)(two+96)+"")){
				if (ind.length()>3)charPair1++;
				else charPair2++;
			}
			if (ind.toLowerCase().contains((char)(three+96)+"")){
				if (ind.length()>3)charPair1++;
				else charPair2++;
			}
		}
		if (debug)System.out.println("Entered letters: "+one+", "+two+", "+three);
		charPair1 = antiModulo(charPair1,26);
		charPair2 = antiModulo(charPair2,26);
		if (debug)System.out.println("Char pair after inds: "+charPair1+", "+charPair2);
		int sum = antiModulo(charPair1+charPair2,26);
		if (sum==4||sum==9||sum==16||sum==25||sum==36||sum==49||sum==64) charPair1+=4;
		else charPair2-=4;
		if (debug)System.out.println("Char pair after squares: "+charPair1+", "+charPair2);
		if (one>two&&one>three)charPair1+=one;
		else if (two>one&&two>three)charPair1+=two;
		else if (three>two&&three>one)charPair1+=three;
		else if (one>two)charPair1+=one;
		else if (two>one)charPair1+=two;
		else charPair1+=three;
		charPair1 = antiModulo(charPair1,26);
		charPair2 = antiModulo(charPair2,26);
		if (debug)System.out.println("Char pair after greater thans: "+charPair1+", "+charPair2);
		if (isPrime(one))charPair1-=one;
		if (isPrime(two))charPair1-=two;
		if (isPrime(three))charPair1-=three;
		if (debug)System.out.println("Char pair after primes: "+charPair1+", "+charPair2);
		if (Math.pow((int)Math.sqrt(one),2)==one)charPair2-=one;
		if (Math.pow((int)Math.sqrt(two),2)==two)charPair2-=two;
		if (Math.pow((int)Math.sqrt(three),2)==three) {
			charPair2-=three;
		}
		charPair1 = antiModulo(charPair1,26);
		charPair2 = antiModulo(charPair2,26);
		if (debug)System.out.println("Char pair after squares: "+charPair1+", "+charPair2);
		if (batteries!=0) {
			if (one%batteries==0) {
				charPair1-=one;
				charPair2-=one;
			}
			if (two%batteries==0) {
				charPair1-=two;
				charPair2-=two;
			}
			if (three%batteries==0) {
				charPair1-=three;
				charPair2-=three;
			}
		}
		charPair1 = antiModulo(charPair1,26);
		charPair2 = antiModulo(charPair2,26);
		if (charPair1==charPair2)System.out.println(morse[charPair1-1]);
		else if (charPair1>charPair2)System.out.println(morse[antiModulo(charPair1-charPair2,26)-1]);
		else System.out.println(morse[antiModulo(charPair1+charPair2,26)-1]);
	}
	public void FizzBuzz() {
		int[][] table = {{7, 3, 2, 4, 5},
						 {3, 4, 9, 2, 8},
						 {4, 5, 8, 8, 2},
						 {2, 3, 7, 9, 1},
						 {6, 6, 1, 2, 8},
						 {1, 2, 2, 5, 3},
						 {3, 1, 8, 3, 4}};
		int serialLetters = 0;
		for (int i = 0; i<6; i++)if ((int)serial.toLowerCase().charAt(i)>96)serialLetters++;
		System.out.print("Colors (3 letters total): ");
		String colors = input.next().toLowerCase();
		System.out.print("Num 1: ");
		int one = input.nextInt();
		System.out.print("Num 2: ");
		int two = input.nextInt();
		System.out.print("Num 3: ");
		int three = input.nextInt();
		System.out.println("Strikes: ");
		int strikes = input.nextInt();
		ArrayList<Integer> nums = new ArrayList<Integer>();
		nums.add(one);
		nums.add(two);
		nums.add(three);
		ArrayList<Integer> adds = new ArrayList<Integer>();
		ArrayList<Integer> trueRows = new ArrayList<Integer>();
		if (holders>2)trueRows.add(0);
		if (ports.contains("serial")&&ports.contains("parallel"))trueRows.add(1);
		if (serialLetters==3)trueRows.add(2);
		if (ports.contains("rca")&&ports.contains("dvi"))trueRows.add(3);
		if (strikes>1)trueRows.add(4);
		if (batteries>4)trueRows.add(5);
		if (trueRows.size()==0)trueRows.add(6);
		
		for (int i = 0; i<3; i++) {
			int sum = 0;
			int col = 0;
			if (colors.substring(i,i+1).equals("r")) col = 0;
			if (colors.substring(i,i+1).equals("g")) col = 1;
			if (colors.substring(i,i+1).equals("b")) col = 2;
			if (colors.substring(i,i+1).equals("y")) col = 3;
			if (colors.substring(i,i+1).equals("w")) col = 4;
			for (int j : trueRows)sum+=table[j][col];
			adds.add(sum%10);
		}
		if (debug) System.out.println("adds: "+adds);
		for (int i = 0; i < 3; i++) {
			int newnum = 0;
			for (int j = 0; j<7; j++) {
				newnum+=Math.pow(10, j)*((nums.get(i)%10+adds.get(i))%10);
				nums.set(i, nums.get(i)/10);
			}
			nums.set(i,newnum);
		}
		for (int i = 0; i <3; i++) {
			if (nums.get(i)%5==0&&nums.get(i)%3==0)System.out.println("Fizzbuzz");
			else if (nums.get(i)%5==0)System.out.println("Buzz");
			else if (nums.get(i)%3==0)System.out.println("Fizz");
			else System.out.println(nums.get(i));
		}
	}
	public void LogicalButtons() {
		for (int j = 0; j < 3; j++) {
			System.out.print("Button colors (3 letters total, top down, left to right): ");
			String colorString = input.next().toLowerCase();
			System.out.print("Label 1: ");
			String lab1 = input.next().toLowerCase();
			System.out.print("Label 2: ");
			String lab2 = input.next().toLowerCase();
			System.out.print("Label 3: ");
			String lab3 = input.next().toLowerCase();
			System.out.print("Operator: ");
			String operator = input.next().toLowerCase();
			boolean[] validColor = new boolean[3];
			boolean[] validLabel = new boolean[3];
			String[] colors = {colorString.substring(0,1),colorString.substring(1, 2),colorString.substring(2,3)};
			String[] labels = {lab1,lab2,lab3};
			boolean primaries = false;
			String dupeLabel = "";
			if (lab1.equals(lab2)||lab1.equals(lab3))dupeLabel=lab1;
			if (lab2.equals(lab3))dupeLabel=lab2;
			for (int i = 0; i < 3; i++) {
				if (colors[i].equals("r")||colors[i].equals("b")||colors[i].equals("y"))primaries=true;
			}
			for (int i = 0; i < 3; i++) {
				if (colors[i].equals("r"))if (indexOf(colors,"b")==-1)validColor[i]=true;
				if (colors[i].equals("b")) {if (indexOf(colors,"b")!=-1&&indexOf(colors,"b")!=i) {validColor[i]=true;validColor[indexOf(colors,"b")]=true;}}
				if (colors[i].equals("g"))if (colors[modulo(i-1,3)].equals("p")||colors[modulo(i-1,3)].equals("w"))validColor[i]=true;
				if (colors[i].equals("y"))if (!labels[i].equals("wrong")&&!labels[i].equals("logic"))validColor[i]=true;
				if (colors[i].equals("p"))if (!primaries)validColor[i]=true;
				if (colors[i].equals("w"))if (primaries)validColor[i]=true;
				if (colors[i].equals("o"))if (!colors[i].equals("o"))validColor[i]=true;
				if (colors[i].equals("c"))if (labels[i].length()==5)validColor[i]=true;
				if (colors[i].equals("a"))if (labels[i].equals(dupeLabel))validColor[i]=true;
				if (labels[i].equals("logic"))if (indexOf(colors,"a")==-1)validLabel[i]=true;
				if (labels[i].equals("color"))if (!colors[i].equals("g")&&!colors[i].equals("y")&&!colors[i].equals("o"))validLabel[i]=true;
				if (labels[i].equals("label"))if (labels[0].length()!=5)validLabel[i]=true;
				if (labels[i].equals("button"))if (!labels[modulo(i-1,3)].equals("hmmm")&&!labels[modulo(i-1,3)].equals("no"))validLabel[i]=true;
				if (labels[i].equals("wrong"))if (colors[(i+1)%3].equals(colors[i]))validLabel[i]=true;
				if (labels[i].equals("boom"))if (colors[(i+1)%3].equals(colors[(i+2)%3]))validLabel[i]=true;
				if (labels[i].equals("no"))if (!validColor[i])validLabel[i]=true;
				if (labels[i].equals("wait"))if (j==2)validLabel[i]=true;
				if (labels[i].equals("hmmm"))if (validColor[1])validLabel[i]=true;
			}
			ArrayList<Integer> validButtons = new ArrayList<Integer>();
			boolean notValidOperator = true;
			while (notValidOperator) {
				for (int i = 0; i < 3; i++) {
					if (operator.equals("and")&&validColor[i]&&validLabel[i])validButtons.add(i+1);
					if (operator.equals("or")&&(validColor[i]||validLabel[i]))validButtons.add(i+1);
					if (operator.equals("xor")&&(validColor[i]^validLabel[i]))validButtons.add(i+1);
					if (operator.equals("nand")&&!(validColor[i]&&validLabel[i]))validButtons.add(i+1);
					if (operator.equals("nor")&&!(validColor[i]||validLabel[i]))validButtons.add(i+1);
					if (operator.equals("xnor")&&(validColor[i]==validLabel[i]))validButtons.add(i+1);
				}
				if (!validButtons.isEmpty())notValidOperator=false;
				if (notValidOperator) {
					System.out.println("Hit operator. New operator: ");
					operator=input.next();
				}
			}
			int group = 0;
			if (operator.contains("n"))group=1;
			String table[][] = {{"123","213","321"},{"312","231","132"}};
			for (int i = 0; i < 3; i++)if(validButtons.contains(Integer.parseInt(table[group][j].substring(i, i+1))))System.out.println(table[group][j].substring(i, i+1));
		}
	}
	public void SplittingTheLoot() {
		System.out.println("Please enter your 3 jewels.");
		String[] jewelString = new String[3];
		int[] jewels = new int[3];
		int[] nums = new int[7];
		int[] requirednums;
		int[][] table = { {20, 19, 13, 26, 23, 34, 12, 14, 35, 16},
						  {10, 21, 13, 25, 24, 11, 11, 30, 19, 39},
						  {39, 38, 25, 30, 24, 23, 28, 34, 15, 36},
						  {14, 18, 33, 22, 31, 32, 22, 37, 36, 31},
						  {40, 20, 26, 12, 32, 33, 28, 15, 38, 17},
						  {19, 29, 18, 16, 17, 21, 35, 27, 27, 37}};
		for (int i = 0; i < 3; i++) {
			String jewel = input.next().toLowerCase();
			jewelString[i]=jewel;
			int jewelrow=Integer.parseInt(jewel.substring(1))-1;
			int jewelcol = (int)jewel.charAt(0)-97;
			jewels[i]=table[jewelrow][jewelcol];
			nums[i+4]=jewels[i];
		}
		System.out.println("Enter the other numbers.");
		for (int i = 0; i<4; i++) {
			int j = input.nextInt();
			nums[i]=j;
		}
		System.out.println("Enter your colored bag (even if it's a jewel).");
		String require = input.next();
		try {
			int newbag = Integer.parseInt(require);
			requirednums=new int[4];
			for (int i = 0; i < 3; i++)requirednums[i]=jewels[i];
			requirednums[3]=newbag;
		}
		catch(NumberFormatException e) {
			requirednums=new int[3];
			for (int i = 0; i < 3; i++)requirednums[i]=jewels[i];
		}
				
		if (splitHelper(0,nums,new int[nums.length],new int[nums.length])) {
			if (debug)System.out.println("solutions found");
			if (debug)System.out.println(lists);
			for (int i = 0 ; i < lists.size(); i++) {
				boolean bad = false;
				ArrayList<ArrayList<Integer>> groups = lists.get(i);
				for (int j : requirednums) {
					if (!groups.get(0).contains(j)&&!groups.get(1).contains(j))bad=true;
				}
				if (bad) {
					lists.remove(i);
					i--;
				}
			}
			
			printArray(jewelString);
			System.out.println();
			printArray(jewels);
			System.out.println();
			System.out.println(lists);
		}
		
		else System.out.println("no solutions found");
	}
	public void TheCube() {
		ArrayList<Object> stuffDump = CubeGUI.getStuff();
		System.out.println(stuffDump);
		ArrayList<String> rotations = (ArrayList<String>)stuffDump.get(0);
		ArrayList<Integer> faces = (ArrayList<Integer>)stuffDump.get(1);
		ArrayList<String> colors = (ArrayList<String>)stuffDump.get(2);
		ArrayList<String> symbols = (ArrayList<String>)stuffDump.get(3);
		ArrayList<Integer> symbolInts = (ArrayList<Integer>)stuffDump.get(4);
		
		
		ArrayList<Integer> code = new ArrayList<Integer>();
		ArrayList<Integer> rcode = new ArrayList<Integer>();
		ArrayList<Integer>wireInts = new ArrayList<Integer>();
		ArrayList<Integer> symbolCode1 = new ArrayList<Integer>();
		ArrayList<Integer> symbolCode2 = new ArrayList<Integer>();
		ArrayList<Integer> finalcode = new ArrayList<Integer>();
		String submitColor = colors.get(12);
		colors.remove(12);
		String submitSymbol = symbols.get(12);
		symbols.remove(12);
		for (int i = 0 ; i < 4; i++)symbols.remove(0);
		ArrayList<String> squareColors = new ArrayList<String>();
		for (int i = 4; i <12;i++)squareColors.add(colors.get(i));
		for (int i = 0; i <8; i++)symbolCode1.add(symbolInts.get(i));
		for (int i = 8; i <16; i++)symbolCode2.add(symbolInts.get(i));
		int modules = 0;
		
		
		
		for (int i = 0; i < 6; i++) {
			switch(rotations.get(i).toLowerCase()) {
			case "up":
				rcode.add(lastDigit);
				break;
			case "down":
				rcode.add(firstDigit());
				break;
			case "clock":
				rcode.add(4);
				break;
			case "counter":
				rcode.add(7);
				break;
			case "left":
				rcode.add(countOf(squareColors, colors.get(2)));
				break;
			case "right":
				rcode.add(countOf(squareColors,colors.get(0)));
				break;
			}
		}
		if (debug)System.out.println(rcode+" ROTATION CODES");
		for (int i = 0; i < 4; i++) {
			switch(colors.get(i).toLowerCase()) {
			case "red":
				if (modules==0) {
					System.out.println("num of modules: ");
					modules =input.nextInt();
				}
				wireInts.add((modules+7)%10);
				break;
			case "orange":
				wireInts.add((countOf(squareColors,"green")+3)%10);
				break;
			case "green":
				wireInts.add((countOf(squareColors,"blue")+7)%10);
				break;
			case "purple":
				int sum = 0;
				for (int j = 0 ; j <6; j++) sum+=faces.get(j);
				wireInts.add(sum%10);
				break;
			case "blue":
				wireInts.add((i+6)%10);
				break;
			case "white":
				wireInts.add(6);
				break;
			}
		}
		if (debug)System.out.println(faces+" CUBE FACES");
		if (debug)System.out.println(wireInts+" WIRE COLORS");
		code.add((rcode.get(0)+faces.get(5)+wireInts.get(2))%10);
		code.add((rcode.get(1)+faces.get(4)+wireInts.get(3))%10);
		code.add((rcode.get(2)+faces.get(3)+wireInts.get(0))%10);
		code.add((rcode.get(3)+faces.get(2)+wireInts.get(1))%10);
		code.add((rcode.get(4)+faces.get(1))%8);
		code.add((rcode.get(5)+faces.get(0))%9);
		code.add(0);
		code.add(0);
		if (debug)System.out.println(code+" CIPHER 1");
		if (debug)System.out.println(symbolCode1+" LEFT SYMBOLS");
		if (debug)System.out.println(symbolCode2+" BOTTOM SYMBOLS");
		for (int i = 0; i <8;i++) {
			finalcode.add((code.get(i)+symbolCode1.get(i)+symbolCode2.get(i))%10);
		}
		if (debug)System.out.println(finalcode+" FINAL CODE");
		
		
		//Stages
		
		for (int i = 0; i <8; i++) {
			ArrayList<Integer> presses = new ArrayList<Integer>();
			ArrayList<String> validSymbols = new ArrayList<String>();
			ArrayList<String> validColors = new ArrayList<String>();
			switch(finalcode.get(i)) {
			case 0:
				validSymbols.add("tunnel");
				validSymbols.add("pepsi");
				validSymbols.add("charlie");
				validSymbols.add("vortex");
				break;
			case 1:
				validSymbols.add("russiandoll");
				validSymbols.add("flag");
				validSymbols.add("stonehenge");
				validSymbols.add("checkmark");
				break;
			case 2:
				validSymbols.add("squeeze");
				validSymbols.add("meteor");
				validSymbols.add("globe");
				break;
			case 3:
				validSymbols.add("pluto");
				validSymbols.add("eclipse");
				validSymbols.add("coffee");
				break;
			case 4:
				validSymbols.add("fireball");
				validSymbols.add("oscar");
				validSymbols.add("ribbon");
				break;
			case 5:
				validSymbols.add("flag");
				validSymbols.add("oscar");
				validSymbols.add("globe");
				break;
			case 6:
				validSymbols.add("pepsi");
				validSymbols.add("vortex");
				validSymbols.add("coffee");
				break;
			case 7:
				validSymbols.add("tunnel");
				validSymbols.add("stonehenge");
				validSymbols.add("ribbon");
				break;
			case 8:
				validSymbols.add("pluto");
				validSymbols.add("eclipse");
				validSymbols.add("fireball");
				validSymbols.add("checkmark");
				break;
			case 9:
				validSymbols.add("russiandoll");
				validSymbols.add("squeeze");
				validSymbols.add("charlie");
				validSymbols.add("meteor");
				break;
			}
			if (i==1)validSymbols.add(submitSymbol);
			if (i==3)validColors.add(submitColor);
			if (i==5)validColors.add(colors.get(0));
			if (i==6)validColors.add(colors.get(2));
			for (int j = 0 ; j < 8; j++) {
				if (validColors.contains(squareColors.get(j))||validSymbols.contains(symbols.get(j)))presses.add(j);
			}
			if (i==7) {
				int k = presses.size();
				for (int j = 0; j<8;j++) {
					if (!presses.contains(j))presses.add(j);
				}
				for (int l = 0; l < k; l++)presses.remove(0);
			}
			System.out.print("Press ");
			for (int j : presses)System.out.print((j+1)+", ");
			System.out.println();
		}
	}
	public void SimonSends() {
		 String[] paragraphs = {"This is the “first” word for purposes of counting words and paragraphs in this text. The flavor text and appendix are excluded.","Hyphenated words equate to just one word. Punctuation marks do not count as letters.","A Simon Sends puzzle is equipped with colorized lights which flash unique letters in Morse code simultaneously, and a dial for adjusting the frequency of flashing.","Owing to their proximity, the lights (red, green and blue) mix by way of additive color mixing. Work out the individual colors.","Convert each recognized letter into a number using its alphabetic position. Call your thusly acquired numbers R, G and B. Derive new letters as follows:","Count R letters from the start of the G’th word from the start of the B’th paragraph in this manual and make it your new red letter.","Count G letters from the start of the B’th word from the start of the R’th paragraph in this manual and make it your new green letter.","Count B letters from the start of the R’th word from the start of the G’th paragraph in this manual and make it your new blue letter.","Realize a new color sequence by juxtaposing again using known additive color mixing one copy of each new letter’s Morse code.","Acknowledge a dot and a dash in Morse code have sizes of one and three units, respectively. Gaps between them also have a size of just one unit.","Input your acquired color sequence using each qualifying color button.","A mistake is rejected with a strike. On such an occasion, adjust and finish your answer. Look at the display to judge your input thus far.","Jump back to the “first” word if, while counting, you advance beyond the “last” word, which is this."};


		    System.out.println("Please enter at least 20 colors.");
		    String userColors = input.next().toLowerCase();
		    String red = "";
		    String green = "";
		    String blue = "";
		    String redmorse = "";
		    String greenmorse = "";
		    String bluemorse = "";
		    for (int i = 0; i < userColors.length(); i++){
		      String userLetter = userColors.substring(i,i+1);
		      switch(userLetter){
		        case "r":
		          red+="r";
		          green+="_";
		          blue+="_";
		          break;
		        case "g":
		          red+="_";
		          green+="g";
		          blue+="_";
		          break;
		        case "b":
		          red+="_";
		          green+="_";
		          blue+="b";
		          break;
		        case "y":
		          red+="r";
		          green+="g";
		          blue+="_";
		          break;
		        case "c":
		          red+="_";
		          green+="g";
		          blue+="b";
		          break;
		        case "m":
		          red+="r";
		          green+="_";
		          blue+="b";
		          break;
		        case "w":
		          red+="r";
		          green+="g";
		          blue+="b";
		          break;
		        case "k":
		          red+="_";
		          green+="_";
		          blue+="_";
		          break;
		      }
		    }
		    for (int i = 0; i < red.length()-1; i++){
		      int add = 0;
		      if (red.substring(i,i+1).equals("r")&&!red.substring(i+1,i+2).equals("r")){
		        redmorse+=".";
		      }
		      if (red.substring(i,i+1).equals("_")&&red.substring(i+1,i+2).equals("_")){
		        redmorse+="E";
		        add=2;
		      }
		      if (red.substring(i,i+1).equals("r")&&red.substring(i+1,i+2).equals("r")){
		        redmorse+="-";
		        add=2;
		      }
		      i+=add;
		    }
		    for (int i = 0; i < green.length()-1; i++){
		      int add = 0;
		      if (green.substring(i,i+1).equals("g")&&!green.substring(i+1,i+2).equals("g")){
		        greenmorse+=".";
		      }
		      if (green.substring(i,i+1).equals("_")&&green.substring(i+1,i+2).equals("_")&&i!=0){
		        greenmorse+="E";
		        add=2;
		      }
		      if (green.substring(i,i+1).equals("g")&&green.substring(i+1,i+2).equals("g")){
		        greenmorse+="-";
		        add=2;
		      }
		      i+=add;
		    }
		    for (int i = 0; i < blue.length()-1; i++){
		      int add = 0;
		      if (blue.substring(i,i+1).equals("b")&&!blue.substring(i+1,i+2).equals("b")){
		        bluemorse+=".";
		      }
		      if (blue.substring(i,i+1).equals("_")&&blue.substring(i+1,i+2).equals("_")&&i!=0){
		        bluemorse+="E";
		        add=2;
		      }
		      if (blue.substring(i,i+1).equals("b")&&blue.substring(i+1,i+2).equals("b")){
		        bluemorse+="-";
		        add=2;
		      }
		      i+=add;
		    }
		    for (int i = 0 ; i < redmorse.length()-1; i++)if (redmorse.substring(i,i+1).equals("E")&&redmorse.substring(i+1,i+2).equals("E")) {
		    	redmorse=redmorse.substring(0,i)+redmorse.substring(i+1);
		    	i--;
		    }
		    for (int i = 0 ; i < bluemorse.length()-1; i++)if (bluemorse.substring(i,i+1).equals("E")&&bluemorse.substring(i+1,i+2).equals("E")) {
		    	bluemorse=bluemorse.substring(0,i)+bluemorse.substring(i+1);
		    	i--;
		    }
		    for (int i = 0 ; i < greenmorse.length()-1; i++)if (greenmorse.substring(i,i+1).equals("E")&&greenmorse.substring(i+1,i+2).equals("E")) {
		    	greenmorse=greenmorse.substring(0,i)+greenmorse.substring(i+1);
		    	i--;
		    }
		    System.out.println(redmorse);
		    System.out.println(greenmorse);
		    System.out.println(bluemorse);
		    int redStart = redmorse.indexOf("E");
		    int redEnd = redmorse.substring(redStart+1).indexOf("E");
		    if (redEnd==-1) {
		    	System.out.println("REENTER SOLUTION! BAD SEQUENCE");
		    	redEnd=redmorse.substring(redStart+1).length();
		    }
		    int greenStart = greenmorse.indexOf("E");
		    int greenEnd = greenmorse.substring(greenStart+1).indexOf("E");
		    if (greenEnd==-1) {
		    	System.out.println("REENTER SOLUTION! BAD SEQUENCE");
		    	greenEnd=greenmorse.substring(greenStart+1).length();
		    }
		    int blueStart = bluemorse.indexOf("E");
		    int blueEnd = bluemorse.substring(blueStart+1).indexOf("E");
		    if (blueEnd==-1) {
		    	System.out.println("REENTER SOLUTION! BAD SEQUENCE");
		    	blueEnd=bluemorse.substring(blueStart+1).length();
		    }
		    redEnd+=redStart+1;
		    greenEnd+=greenStart+1;
		    blueEnd+=blueStart+1;

		    String redLetter = morseConverter(redmorse.substring(redStart+1,redEnd));
		    String greenLetter = morseConverter(greenmorse.substring(greenStart+1,greenEnd));
		    String blueLetter = morseConverter(bluemorse.substring(blueStart+1,blueEnd));

		    int redNum = ((int)redLetter.charAt(0))-96;
		    int greenNum = ((int)greenLetter.charAt(0))-96;
		    int blueNum = ((int)blueLetter.charAt(0))-96;


		    System.out.println("NEW RED NUMBER: "+redNum);
		    System.out.println("NEW GREEN NUMBER: "+greenNum);
		    System.out.println("NEW BLUE NUMBER: "+blueNum);

		    redNum--;
		    greenNum--;
		    blueNum--;

		    String newRedLetter = "";
		    String newGreenLetter = "";
		    String newBlueLetter = "";

		    int redParagraph = (blueNum)%13;
		    String redPar = paragraphs[redParagraph].toLowerCase();
		    

		    int word =0;
		    ArrayList<String> redParWords = paragraphToWords(redPar);
		    for (int i = 0; i < greenNum; i ++){
		      word++;
		      if (word>=redParWords.size()){
		        redParagraph=(redParagraph+1)%13;
		        redPar = paragraphs[redParagraph].toLowerCase();
		        redParWords = paragraphToWords(redPar);
		        word=0;
		      }
		      
		    }

		    int letter = 0;
		    ArrayList<String> redParLetters = wordToLetters(redParWords.get(word));
		    for (int i = 0; i < redNum; i++){
		      letter++;
		      if (letter>=redParLetters.size()){
		        word++;
		        if (word>=redParWords.size()){
		          redParagraph=(redParagraph+1)%13;
		          redPar = paragraphs[redParagraph].toLowerCase();
		          redParWords = paragraphToWords(redPar);
		          word=0;
		        }
		        letter = 0;
		        redParLetters = wordToLetters(redParWords.get(word));
		      }
		      
		    }
		    if (debug) {
		    	System.out.println("Final paragraph "+redParagraph);
		    	System.out.println("Final word "+word);
		    	System.out.println("Final letter "+letter);
		    }
		    newRedLetter = redParLetters.get(letter);









		    int greenParagraph = (redNum)%13;
		    String greenPar = paragraphs[greenParagraph].toLowerCase();
		    

		    word =0;
		    ArrayList<String> greenParWords = paragraphToWords(greenPar);
		    for (int i = 0; i < blueNum; i ++){
		      word++;
		      if (word>=greenParWords.size()){
		        greenParagraph=(greenParagraph+1)%13;
		        greenPar = paragraphs[greenParagraph].toLowerCase();
		        greenParWords = paragraphToWords(greenPar);
		        word=0;
		      }
		      
		    }

		    letter = 0;
		    ArrayList<String> greenParLetters = wordToLetters(greenParWords.get(word));
		    for (int i = 0; i < greenNum; i++){
		      letter++;
		      if (letter>=greenParLetters.size()){
		        word++;
		        if (word>=greenParWords.size()){
		          greenParagraph=(greenParagraph+1)%13;
		          greenPar = paragraphs[greenParagraph].toLowerCase();
		          greenParWords = paragraphToWords(greenPar);
		          word=0;
		        }
		        letter = 0;
		        greenParLetters = wordToLetters(greenParWords.get(word));
		      }
		      
		    }

		    newGreenLetter = greenParLetters.get(letter);
		    if (debug) {
		    	System.out.println("Final paragraph "+greenParagraph);
		    	System.out.println("Final word "+word);
		    	System.out.println("Final letter "+letter);
		    }








		    int blueParagraph = (greenNum)%13;
		    String bluePar = paragraphs[blueParagraph].toLowerCase();
		    

		    word =0;
		    ArrayList<String> blueParWords = paragraphToWords(bluePar);
		    for (int i = 0; i < redNum; i ++){
		      word++;
		      if (word>=blueParWords.size()){
		        blueParagraph=(blueParagraph+1)%13;
		        bluePar = paragraphs[blueParagraph].toLowerCase();
		        blueParWords = paragraphToWords(bluePar);
		        word=0;
		      }
		     
		    }
		    letter = 0;
		    ArrayList<String> blueParLetters = wordToLetters(blueParWords.get(word));
		    for (int i = 0; i < blueNum; i++){
		      letter++;
		      if (letter>=blueParLetters.size()){
		        word++;
		        if (word>=blueParWords.size()){
		          blueParagraph=(blueParagraph+1)%13;
		          bluePar = paragraphs[blueParagraph].toLowerCase();
		          blueParWords = paragraphToWords(bluePar);
		          word=0;
		        }
		        letter = 0;
		        blueParLetters = wordToLetters(blueParWords.get(word));
		      }
		      
		    }
		    newBlueLetter = blueParLetters.get(letter);
		    if (debug) {
		    	System.out.println("Final paragraph "+blueParagraph);
		    	System.out.println("Final word "+word);
		    	System.out.println("Final letter "+letter);
		    	
		    }
		    
		    System.out.println("NEW RED LETTER: "+newRedLetter);
	    	System.out.println("NEW GREEN LETTER: "+newGreenLetter);
	    	System.out.println("NEW BLUE LETTER: "+newBlueLetter);
	    	
		    String newRedMorse = englishConverter(newRedLetter);
		    String newGreenMorse = englishConverter(newGreenLetter);
		    String newBlueMorse = englishConverter(newBlueLetter);

		    String elongatedRedMorse = "";
		    String elongatedGreenMorse = "";
		    String elongatedBlueMorse = "";
		    
		   for (int i = 0; i < newRedMorse.length(); i++){
		     if (newRedMorse.substring(i,i+1).equals("."))elongatedRedMorse+=".-";
		     if (newRedMorse.substring(i,i+1).equals("-"))elongatedRedMorse+="...-";
		   }

		   for (int i = 0; i < newGreenMorse.length(); i++){
		     if (newGreenMorse.substring(i,i+1).equals("."))elongatedGreenMorse+=".-";
		     if (newGreenMorse.substring(i,i+1).equals("-"))elongatedGreenMorse+="...-";
		   }

		   for (int i = 0; i < newBlueMorse.length(); i++){
		     if (newBlueMorse.substring(i,i+1).equals("."))elongatedBlueMorse+=".-";
		     if (newBlueMorse.substring(i,i+1).equals("-"))elongatedBlueMorse+="...-";
		   }

		   while (elongatedRedMorse.length()<20)elongatedRedMorse+="-";
		   while (elongatedGreenMorse.length()<20)elongatedGreenMorse+="-";
		   while (elongatedBlueMorse.length()<20)elongatedBlueMorse+="-";

		   String userAnswer = "";
		   for (int i = 0; i < 20; i++){
		     boolean redc = elongatedRedMorse.substring(i,i+1).equals(".");
		     boolean greenc = elongatedGreenMorse.substring(i,i+1).equals(".");
		     boolean bluec = elongatedBlueMorse.substring(i,i+1).equals(".");

		    if (redc&&!greenc&&!bluec)userAnswer+="r";
		    if (!redc&&greenc&&!bluec)userAnswer+="g";
		    if (!redc&&!greenc&&bluec)userAnswer+="b";

		    if (redc&&greenc&&!bluec)userAnswer+="y";
		    if (!redc&&greenc&&bluec)userAnswer+="c";
		    if (redc&&!greenc&&bluec)userAnswer+="m";

		    if (!redc&&!greenc&&!bluec)userAnswer+="k";
		    if (redc&&greenc&&bluec)userAnswer+="w";
		    
		   }
		   while(userAnswer.endsWith("k"))userAnswer=userAnswer.substring(0,userAnswer.length()-1);
		   System.out.println(userAnswer);
	}
	public void SimonStores() {
		String[] table = {"RGBCMY".toLowerCase(),"YBGMCR".toLowerCase(),"BMRYGC".toLowerCase()};
		String[] colorTable = {"r","g","b","c","m","y"};
		int dummy1 = 0;
		int dummy2 = 0;
		int d = 0;
		int serialBase36[] = new int[6];
		int stage1Nums[] = new int[5];
		int stage2Nums[] = new int[5];
		int stage3Nums[] = new int[6];
		stage1Nums[4]=0;
		if (letterPos(serial.substring(2,3))>0) dummy1 = 9+letterPos(serial.substring(2,3));
		else dummy1 = Integer.parseInt(serial.substring(2,3));
		if (letterPos(serial.substring(3,4))>0) dummy2 = 9+letterPos(serial.substring(3,4));
		else dummy2 = Integer.parseInt(serial.substring(3,4));
		int a0 = dummy1*36+dummy2;
		while (a0>364)a0-=365;
		while (a0<-364)a0+=365;
		stage1Nums[0] = a0;
		d+=dummy1+dummy2;
		
		if (letterPos(serial.substring(4,5))>0) dummy1 = 9+letterPos(serial.substring(4,5));
		else dummy1 = Integer.parseInt(serial.substring(4,5));
		if (letterPos(serial.substring(5,6))>0) dummy2 = 9+letterPos(serial.substring(5,6));
		else dummy2 = Integer.parseInt(serial.substring(5,6));
		int b0 = dummy1*36+dummy2;
		while (b0>364)b0-=365;
		while (b0<-364)b0+=365;
		stage2Nums[0] = b0;
		d+=dummy1+dummy2;
		
		if (letterPos(serial.substring(0,1))>0) dummy1 = 9+letterPos(serial.substring(0,1));
		else dummy1 = Integer.parseInt(serial.substring(0,1));
		if (letterPos(serial.substring(1,2))>0) dummy2 = 9+letterPos(serial.substring(1,2));
		else dummy2 = Integer.parseInt(serial.substring(1,2));
		int c0 = dummy1*36+dummy2;
		while (c0>364)c0-=365;
		while (c0<-364)c0+=365;
		stage3Nums[0] = c0;
		d+=dummy1+dummy2;
				
		System.out.println("Colors Clockwise From W: ");
		String colors = input.next().toLowerCase();
		if (!colors.substring(4,5).equals("k")||!colors.substring(0,1).equals("w")) {
			System.out.println("Colors entered wrong. Start with W, 4th char should be K.");
			return;
		}
		System.out.println("Enter your flashing colors, with a space in between each flash. Colors flashing simultaneously should be next to each other.");
		String nulll = input.nextLine();
		String[] s1Colors = input.nextLine().toLowerCase().split(" ");
		for (int i = 1; i < 4; i++) {
			String color = s1Colors[i-1];
			int x = stage1Nums[i-1];
			if (color.length()==1) {
				switch(color) {
				case "r":
					stage1Nums[i]=x+d;
					break;
				case "g":
					stage1Nums[i]=x-d;
					break;
				case "b":
					stage1Nums[i]=x*2-d;
					break;
				case "c":
					stage1Nums[i]=d-x-8*i;
					break;
				case "m":
					stage1Nums[i]=3*i*i*i-2*x;
					break;
				case "y":
					stage1Nums[i]=x+d-6*i;
					break;
				}
			}
			if (color.length()==2) {
				int prims = 0;
				for (int j = 0; j<2; j++)if (color.substring(j, j+1).equals("r")||color.substring(j, j+1).equals("g")||color.substring(j, j+1).equals("b"))prims++;
				int dum[] = new int[2];
				for (int j = 0; j<2; j++) {
					switch(color.substring(j,j+1)) {
					case "r":
						dum[j]=x+d;
						break;
					case "g":
						dum[j]=x-d;
						break;
					case "b":
						dum[j]=x*2-d;
						break;
					case "c":
						dum[j]=d-x-8*i;
						break;
					case "m":
						dum[j]=3*i*i*i-2*x;
						break;
					case "y":
						dum[j]=x+d-6*i;
						break;
					}
				}
				for (int k = 0; k < 2; k++) {
					while (dum[k]>364)dum[k]-=365;
					while (dum[k]<-364)dum[k]+=365;
				}
				if (prims==2)stage1Nums[i]=Math.max(dum[0], dum[1]);
				if (prims==1)stage1Nums[i]=dum[0]+dum[1]-2*d;
				if (prims==0)stage1Nums[i]=Math.min(dum[0], dum[1]);
			}
			if (color.length()==3) {
				int prims = 0;
				for (int j = 0; j<3; j++)if (color.substring(j, j+1).equals("r")||color.substring(j, j+1).equals("g")||color.substring(j, j+1).equals("b"))prims++;
				int dum[] = new int[3];
				for (int j = 0; j<3; j++) {
					switch(color.substring(j,j+1)) {
					case "r":
						dum[j]=x+d;
						break;
					case "g":
						dum[j]=x-d;
						break;
					case "b":
						dum[j]=x*2-d;
						break;
					case "c":
						dum[j]=d-x-8*i;
						break;
					case "m":
						dum[j]=3*i*i*i-2*x;
						break;
					case "y":
						dum[j]=x+d-6*i;
						break;
					}
				}
				for (int k = 0; k < 3; k++) {
					while (dum[k]>364)dum[k]-=365;
					while (dum[k]<-364)dum[k]+=365;
				}
				if (prims==3)stage1Nums[i]=x+stage1Nums[0];
				if (prims==2)stage1Nums[i]=Math.max(dum[0], Math.max(dum[1], dum[2]));
				if (prims==1)stage1Nums[i]=Math.min(dum[0], Math.min(dum[1], dum[2]));
				if (prims==0)stage1Nums[i]=x-stage1Nums[0];
			}
			while (stage1Nums[i]>364)stage1Nums[i]-=365;
			while (stage1Nums[i]<-364)stage1Nums[i]+=365;
		}
		int ternery[] = new int[6];
		for (int j = 0; j<6; j++)ternery[j]=0;
		int[] sequence = new int[6];
		for (int j = 0; j<6; j++)sequence[j]=0;
		int stage1 = stage1Nums[3];
		if (stage1Nums[3]<0) { stage1*=-1;}
		for (int j = 0; j < 6; j++) {
			while (Math.pow(3, 5-j)<=stage1) {
				stage1-=Math.pow(3, 5-j);
				ternery[j]+=1;
			}
		}
		for (int j = 5; j >= 0; j--) {
			if (ternery[j]>=2) {
				ternery[j]-=3;
				ternery[j-1]++;
			}
		}
			
		for (int j = 0; j<6; j++) {
			sequence[j]=ternery[5-j];
		}
		if (stage1Nums[3]<0) {
			for (int j = 0; j<6; j++) {
				sequence[j]=sequence[j]*-1;
			}
		}
		if (debug)System.out.println("Number received, "+stage1Nums[3]);
		String entry = table[0];
		
		//Below is color swapping
		if (colors.substring(1,2).equals("y")){
			String dum = entry.substring(5, 6);
			entry = dum+entry.substring(0,5);
		}
		if (Math.abs(colors.indexOf("r")-colors.indexOf("c"))==4) {
			int[] swap = {entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("y"), entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("b")}; 
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (Math.abs(colors.indexOf("g")-colors.indexOf("w"))==1||Math.abs(colors.indexOf("g")-colors.indexOf("w"))==7) {
			int[] swap = {entry.indexOf("b"), entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("y")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (Math.abs(colors.indexOf("m")-4)==1) {
			int[] swap = {entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("b"), entry.indexOf("y"), entry.indexOf("c"), entry.indexOf("m")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if ((colors.indexOf("b")>4&&colors.indexOf("y")>4)||(colors.indexOf("b")<4&&colors.indexOf("y")<4)){
			int oppBlu = (5-entry.indexOf("b"));
			String newString = "";
			if (entry.indexOf("b")<oppBlu) {
				newString = entry.substring(0,entry.indexOf("b"))+entry.substring(oppBlu,oppBlu+1)+entry.substring(entry.indexOf("b")+1,oppBlu)+"b"+entry.substring(oppBlu+1);
			}
			else {
				newString = entry.substring(0,oppBlu)+"b"+entry.substring(oppBlu+1,entry.indexOf("b"))+entry.substring(oppBlu,oppBlu+1)+entry.substring(entry.indexOf("b")+1);
			}
			entry = newString;
		}
		if (colors.indexOf("r")<4) {
			int[] swap = {entry.indexOf("y"), entry.indexOf("g"), entry.indexOf("b"), entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("r")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (colors.indexOf("b")>4) {
			int[] swap = {entry.indexOf("r"), entry.indexOf("c"), entry.indexOf("b"), entry.indexOf("g"), entry.indexOf("m"), entry.indexOf("y")}; 
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		//Done modifying sequence
		if (debug) System.out.println("Color sequence: "+entry);
		for (int j = 0; j<6; j++) {
			if (sequence[j]!=0)System.out.print(sequence[j]+" "+entry.substring(j,j+1));
			System.out.println();
		}
		
		
		//STAGE 2
		
		System.out.println("Enter the newest flashing color(s).");
		String s2color = input.next().toLowerCase();
		String[] s2Colors = new String [4];
		for (int i = 0; i < s1Colors.length; i++) {
			s2Colors[i]=s1Colors[i];
		}
		s2Colors[3]=s2color;
		
		for (int i = 1; i < 5; i++) {
			String color = s2Colors[i-1];
			int x = stage2Nums[i-1];
			if (color.length()==1) {
				switch (color) {
				case "r":
					stage2Nums[i] = x + stage1Nums[i - 1] + i * i;
					break;
				case "g":
					stage2Nums[i] = 2 * x - stage1Nums[i - 1];
					break;
				case "b":
					stage2Nums[i] = 2 * x - stage1Nums[0] - 4 * i * i;
					break;
				case "c":
					stage2Nums[i] = x + stage1Nums[1];
					break;
				case "m":
					stage2Nums[i] = x + stage1Nums[2] - d;
					break;
				case "y":
					stage2Nums[i] = x + stage1Nums[3] - stage1Nums[i - 1];
					break;
				}
			}
			if (color.length()==2) {
				ArrayList<String> primaries = new ArrayList<String>();
				primaries.add("r");
				primaries.add("b");
				primaries.add("g");
				ArrayList<String> secondaries = new ArrayList<String>();
				secondaries.add("c");
				secondaries.add("m");
				secondaries.add("y");
				String colorOrder = "";
				int[] values = new int[2];
				for (int j = 2; j >= 0; j--) {
					if (color.contains(primaries.get(j))) {
						colorOrder+=primaries.get(j);
						primaries.remove(j);
					}
				}
				for (int j = 2; j >= 0; j--) {
					if (color.contains(secondaries.get(j))) {
						colorOrder+=secondaries.get(j);
						secondaries.remove(j);
					}
				}
				int r = x + stage1Nums[i - 1] + i * i;
				int g = 2 * x - stage1Nums[i - 1];
				int b = 2 * x - stage1Nums[0] - 4 * i * i;
				int c = x + stage1Nums[1];
				int m= x + stage1Nums[2] - d;
				int y = x + stage1Nums[3] - stage1Nums[i - 1];
				
				int[] colorValues = {r,g,b,c,m,y};
				for (int k = 0; k < colorValues.length; k++) {
					while (colorValues[k]>364)colorValues[k]-=365;
					while (colorValues[k]<-364)colorValues[k]+=365;
				}
				if (primaries.size()==1) {
					stage2Nums[i]=Math.abs(colorValues[indexOf(colorTable,colorOrder.substring(0,1))]-colorValues[indexOf(colorTable,colorOrder.substring(1,2))]);
				}
				if (primaries.size()==2) {
					stage2Nums[i]=4*d-Math.abs(colorValues[indexOf(colorTable,colorOrder.substring(0,1))]-colorValues[indexOf(colorTable,colorOrder.substring(1,2))]);
				}
				if (primaries.size()==3) {
					x=stage1Nums[i-1];
					c = x + stage1Nums[1];
					m= x + stage1Nums[2] - d;
					y = x + stage1Nums[3] - stage1Nums[i - 1];
					while (c>364)c-=365;
					while (c<-364)c+=365;
					while (m>364)m-=365;
					while (m<-364)m+=365;
					while (y>364)y-=365;
					while (y<-364)y+=365;
					int recalc = 0;
					if (secondaries.contains("c"))recalc=c;
					if (secondaries.contains("m"))recalc=m;
					if (secondaries.contains("y"))recalc=y;
					stage2Nums[i]=Math.max(colorValues[indexOf(colorTable,secondaries.get(0))], recalc);
				}
			}
			if (color.length()==3) {
				ArrayList<String> primaries = new ArrayList<String>();
				primaries.add("r");
				primaries.add("b");
				primaries.add("g");
				ArrayList<String> secondaries = new ArrayList<String>();
				secondaries.add("c");
				secondaries.add("m");
				secondaries.add("y");
				String colorOrder = "";
				int[] values = new int[3];
				for (int j = 2; j >= 0; j--) {
					if (color.contains(primaries.get(j))) {
						colorOrder+=primaries.get(j);
						primaries.remove(j);
					}
				}
				for (int j = 2; j >= 0; j--) {
					if (color.contains(secondaries.get(j))) {
						colorOrder+=secondaries.get(j);
						secondaries.remove(j);
					}
				}
				int r = x + stage1Nums[i - 1] + i * i;
				int g = 2 * x - stage1Nums[i - 1];
				int b = 2 * x - stage1Nums[0] - 4 * i * i;
				int c = x + stage1Nums[1];
				int m= x + stage1Nums[2] - d;
				int y = x + stage1Nums[3] - stage1Nums[i - 1];
				
				int[] colorValues = {r,g,b,c,m,y};
				for (int k = 0; k < colorValues.length; k++) {
					while (colorValues[k]>364)colorValues[k]-=365;
					while (colorValues[k]<-364)colorValues[k]+=365;
				}
				if (primaries.size()==0) {
					stage2Nums[i]=stage2Nums[i-1]+(stage2Nums[i-1]%4)*stage2Nums[0]-stage1Nums[3];
				}
				if (primaries.size()==1) {
					x = stage1Nums[i-1];
					int special = 0;
					if (!secondaries.contains("c"))special=x + stage1Nums[1];
					if (!secondaries.contains("m"))special=x + stage1Nums[2] - d;
					if (!secondaries.contains("y"))special=x + stage1Nums[3] - stage1Nums[i - 1];
					stage2Nums[i]=stage2Nums[i-1]+colorValues[indexOf(colorTable,colorOrder.substring(0,1))]+colorValues[indexOf(colorTable,colorOrder.substring(1,2))]-special;
				}
				if (primaries.size()==2) {
					x = stage1Nums[i-1];
					int special = 0;
					int ca = x+stage1Nums[1];
					int ma = x+stage1Nums[2]-d;
					int ya = stage1Nums[3];
					if (secondaries.contains("c"))special=ma+ya;
					if (secondaries.contains("m"))special=ca+ya;
					if (secondaries.contains("y"))special=ca+ma;					
					stage2Nums[i]=stage2Nums[i-1]+special-colorValues[indexOf(colorTable,colorOrder.substring(0,1))];
				}
				if (primaries.size()==3) {
					stage2Nums[i]=stage2Nums[i-1]+(stage2Nums[0]%4)*stage2Nums[i-1]-stage1Nums[3];
				}
			}
			while (stage2Nums[i]>364)stage2Nums[i]-=365;
			while (stage2Nums[i]<-364)stage2Nums[i]+=365;
		}
		
		
		for (int j = 0; j<6; j++)ternery[j]=0;
		
		int stage2 = stage2Nums[4];
		if (stage2Nums[4]<0) { stage2*=-1;}
		for (int j = 0; j < 6; j++) {
			while (Math.pow(3, 5-j)<=stage2) {
				stage2-=Math.pow(3, 5-j);
				ternery[j]+=1;
			}
		}
		for (int j = 5; j >= 0; j--) {
			if (ternery[j]>=2) {
				ternery[j]-=3;
				ternery[j-1]++;
			}
		}
		for (int j = 0; j<6; j++) {
			sequence[j]=ternery[5-j];
		}
		if (stage2Nums[4]<0) {
			for (int j = 0; j<6; j++) {
				sequence[j]=sequence[j]*-1;
			}
		}
		if (debug)System.out.println("Number received, "+stage2Nums[4]);
		entry = table[1];
		
		//Below is color swapping, round the second.
		if (colors.substring(1,2).equals("y")){
			String dum = entry.substring(5, 6);
			entry = dum+entry.substring(0,5);
		}
		if (Math.abs(colors.indexOf("r")-colors.indexOf("c"))==4) {
			int[] swap = {entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("y"), entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("b")}; 
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (Math.abs(colors.indexOf("g")-colors.indexOf("w"))==1||Math.abs(colors.indexOf("g")-colors.indexOf("w"))==7) {
			int[] swap = {entry.indexOf("b"), entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("y")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (Math.abs(colors.indexOf("m")-4)==1) {
			int[] swap = {entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("b"), entry.indexOf("y"), entry.indexOf("c"), entry.indexOf("m")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if ((colors.indexOf("b")>4&&colors.indexOf("y")>4)||(colors.indexOf("b")<4&&colors.indexOf("y")<4)){
			int oppBlu = (5-entry.indexOf("b"));
			String newString = "";
			if (entry.indexOf("b")<oppBlu) {
				newString = entry.substring(0,entry.indexOf("b"))+entry.substring(oppBlu,oppBlu+1)+entry.substring(entry.indexOf("b")+1,oppBlu)+"b"+entry.substring(oppBlu+1);
			}
			else {
				newString = entry.substring(0,oppBlu)+"b"+entry.substring(oppBlu+1,entry.indexOf("b"))+entry.substring(oppBlu,oppBlu+1)+entry.substring(entry.indexOf("b")+1);
			}
			entry = newString;
		}
		if (colors.indexOf("r")<4) {
			int[] swap = {entry.indexOf("y"), entry.indexOf("g"), entry.indexOf("b"), entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("r")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (colors.indexOf("b")>4) {
			int[] swap = {entry.indexOf("r"), entry.indexOf("c"), entry.indexOf("b"), entry.indexOf("g"), entry.indexOf("m"), entry.indexOf("y")}; 
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		//Done modifying sequence
		if (debug) System.out.println("Color sequence: "+entry);
		for (int j = 0; j<6; j++) {
			if (sequence[j]!=0)System.out.print(sequence[j]+" "+entry.substring(j,j+1));
			System.out.println();
		}
		
		//Stage 3
		
		System.out.println("Enter the newest flashing color(s).");
		String s3color = input.next().toLowerCase();
		String[] s3Colors = new String [5];
		for (int i = 0; i < s2Colors.length; i++) {
			s3Colors[i]=s2Colors[i];
		}
		s3Colors[4]=s3color;
		
		for (int i = 1; i < 6; i++) {
			String color = s3Colors[i-1];
			int x = stage3Nums[i-1];
			if (color.length()==1) {
				switch (color) {
				case "r":
					stage3Nums[i]=x+stage2Nums[i-1]-stage1Nums[i-1];
					break;
				case "g":
					stage3Nums[i]=x-2*stage2Nums[i-1];
					break;
				case "b":
					stage3Nums[i]=x+stage2Nums[0]-stage1Nums[3];
					break;
				case "c":
					stage3Nums[i]=x-stage2Nums[i-1]+stage1Nums[i-1];
					break;
				case "m":
					stage3Nums[i]=x-2*stage1Nums[i-1];
					break;
				case "y":
					stage3Nums[i]=x+stage2Nums[4]-stage1Nums[0];
					break;
				}
			}
			if (color.length()==2) {
				ArrayList<String> primaries = new ArrayList<String>();
				primaries.add("r");
				primaries.add("b");
				primaries.add("g");
				ArrayList<String> secondaries = new ArrayList<String>();
				secondaries.add("c");
				secondaries.add("m");
				secondaries.add("y");
				String colorOrder = "";
				int[] values = new int[2];
				for (int j = 2; j >= 0; j--) {
					if (color.contains(primaries.get(j))) {
						colorOrder+=primaries.get(j);
						primaries.remove(j);
					}
				}
				for (int j = 2; j >= 0; j--) {
					if (color.contains(secondaries.get(j))) {
						colorOrder+=secondaries.get(j);
						secondaries.remove(j);
					}
				}
				int r = x+stage2Nums[i-1]-stage1Nums[i-1];
				int g = x-2*stage2Nums[i-1];
				int b = x+stage2Nums[0]+stage1Nums[3];
				int c = x-stage2Nums[i-1]+stage1Nums[i-1];
				int m = x-2*stage1Nums[i-1];
				int y = x+stage2Nums[4]-stage1Nums[0];
				
				int[] colorValues = {r,g,b,c,m,y};
				for (int k = 0; k < colorValues.length; k++) {
					while (colorValues[k]>364)colorValues[k]-=365;
					while (colorValues[k]<-364)colorValues[k]+=365;
				}
				if (primaries.size()==1) {
					x = stage2Nums[i-1];
					int rb = x+stage2Nums[i-1]-stage1Nums[i-1];
					int gb = x-2*stage2Nums[i-1];
					int bb = x+stage2Nums[0]+stage1Nums[3];
					x = stage1Nums[i-1];
					int ra = x+stage2Nums[i-1]-stage1Nums[i-1];
					int ga = x-2*stage2Nums[i-1];
					int ba = x+stage2Nums[0]+stage1Nums[3];
					
					if (primaries.contains("r"))stage3Nums[i]=r+rb+ra;
					if (primaries.contains("g"))stage3Nums[i]=g+gb+ga;
					if (primaries.contains("b"))stage3Nums[i]=b+bb+ba;
				}
				if (primaries.size()==2) {
					stage3Nums[i]=Math.min(colorValues[indexOf(colorTable,colorOrder.substring(0,1))], Math.min(colorValues[indexOf(colorTable,colorOrder.substring(1,2))], -1*Math.abs(colorValues[indexOf(colorTable,colorOrder.substring(0,1))]-colorValues[indexOf(colorTable,colorOrder.substring(1,2))])));
				}
				if (primaries.size()==3) {
					stage3Nums[i]=colorValues[indexOf(colorTable,secondaries.get(0))]-colorValues[indexOf(colorTable,colorOrder.substring(0,1))]-colorValues[indexOf(colorTable,colorOrder.substring(1,2))];
				}
			}
			
			if (color.length()==3) {
				ArrayList<String> primaries = new ArrayList<String>();
				primaries.add("r");
				primaries.add("b");
				primaries.add("g");
				ArrayList<String> secondaries = new ArrayList<String>();
				secondaries.add("c");
				secondaries.add("m");
				secondaries.add("y");
				String colorOrder = "";
				int[] values = new int[2];
				for (int j = 2; j >= 0; j--) {
					if (color.contains(primaries.get(j))) {
						colorOrder+=primaries.get(j);
						primaries.remove(j);
					}
				}
				for (int j = 2; j >= 0; j--) {
					if (color.contains(secondaries.get(j))) {
						colorOrder+=secondaries.get(j);
						secondaries.remove(j);
					}
				}
				int r = x+stage2Nums[i-1]-stage1Nums[i-1];
				int g = x-2*stage2Nums[i-1];
				int b = x+stage2Nums[0]+stage1Nums[3];
				int c = x-stage2Nums[i-1]+stage1Nums[i-1];
				int m = x-2*stage1Nums[i-1];
				int y = x+stage2Nums[4]-stage1Nums[0];
				
				int[] colorValues = {r,g,b,c,m,y};
				for (int k = 0; k < colorValues.length; k++) {
					while (colorValues[k]>364)colorValues[k]-=365;
					while (colorValues[k]<-364)colorValues[k]+=365;
				}
				if (primaries.size()==0) {
					stage3Nums[i]=stage3Nums[i-1]+stage3Nums[0]*(stage3Nums[i-1]%3)-stage2Nums[0]*(stage2Nums[i-1]%3)+stage1Nums[0]*(stage1Nums[i-1]%3);
				}
				if (primaries.size()==1) {
					int base = colorValues[indexOf(colorTable,colorOrder.substring(0,1))]+colorValues[indexOf(colorTable,colorOrder.substring(1,2))];
					x = stage2Nums[i-1];
					int cb = x-stage2Nums[i-1]+stage1Nums[i-1];
					int mb = x-2*stage1Nums[i-1];
					int yb = x+stage2Nums[4]-stage1Nums[0];
					x = stage1Nums[i-1];
					int ca = x-stage2Nums[i-1]+stage1Nums[i-1];
					int ma = x-2*stage1Nums[i-1];
					int ya = x+stage2Nums[4]-stage1Nums[0];
					
					if (!secondaries.contains("c"))stage3Nums[i]=base-cb-ca;
					if (!secondaries.contains("m"))stage3Nums[i]=base-mb-ma;
					if (!secondaries.contains("y"))stage3Nums[i]=base-yb-ya;
				}
				if (primaries.size()==2) {
					int base = colorValues[indexOf(colorTable,colorOrder.substring(1,2))]+colorValues[indexOf(colorTable,colorOrder.substring(2,3))];
					x = stage2Nums[i-1];
					int rb = x+stage2Nums[i-1]-stage1Nums[i-1];
					int gb = x-2*stage2Nums[i-1];
					int bb = x+stage2Nums[0]+stage1Nums[3];
					x = stage1Nums[i-1];
					int ra = x+stage2Nums[i-1]-stage1Nums[i-1];
					int ga = x-2*stage2Nums[i-1];
					int ba = x+stage2Nums[0]+stage1Nums[3];
					
					if (!primaries.contains("r"))stage3Nums[i]=base-rb-ra;
					if (!primaries.contains("g"))stage3Nums[i]=base-gb-ga;
					if (!primaries.contains("b"))stage3Nums[i]=base-bb-ba;
				}
				if (primaries.size()==3) {
					stage3Nums[i]=stage3Nums[i-1]+stage3Nums[i-1]*(stage3Nums[0]%3)-stage2Nums[i-1]*(stage2Nums[0]%3)+stage1Nums[i-1]*(stage1Nums[0]%3);
				}
			}
			while (stage3Nums[i]>364)stage3Nums[i]-=365;
			while (stage3Nums[i]<-364)stage3Nums[i]+=365;
		}
		
		for (int j = 0; j<6; j++)ternery[j]=0;
		
		int stage3 = stage3Nums[5];
		if (stage3Nums[5]<0) { stage3*=-1;}
		for (int j = 0; j < 6; j++) {
			while (Math.pow(3, 5-j)<=stage3) {
				stage3-=Math.pow(3, 5-j);
				ternery[j]+=1;
			}
		}
		for (int j = 5; j >= 0; j--) {
			if (ternery[j]>=2) {
				ternery[j]-=3;
				ternery[j-1]++;
			}
		}
		for (int j = 0; j<6; j++) {
			sequence[j]=ternery[5-j];
		}
		if (stage3Nums[5]<0) {
			for (int j = 0; j<6; j++) {
				sequence[j]=sequence[j]*-1;
			}
		}
		if (debug)System.out.println("Number received, "+stage3Nums[5]);
		entry = table[2];
		
		//Below is color swapping, round the second.
		if (colors.substring(1,2).equals("y")){
			String dum = entry.substring(5, 6);
			entry = dum+entry.substring(0,5);
		}
		if (Math.abs(colors.indexOf("r")-colors.indexOf("c"))==4) {
			int[] swap = {entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("y"), entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("b")}; 
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (Math.abs(colors.indexOf("g")-colors.indexOf("w"))==1||Math.abs(colors.indexOf("g")-colors.indexOf("w"))==7) {
			int[] swap = {entry.indexOf("b"), entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("y")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (Math.abs(colors.indexOf("m")-4)==1) {
			int[] swap = {entry.indexOf("r"), entry.indexOf("g"), entry.indexOf("b"), entry.indexOf("y"), entry.indexOf("c"), entry.indexOf("m")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if ((colors.indexOf("b")>4&&colors.indexOf("y")>4)||(colors.indexOf("b")<4&&colors.indexOf("y")<4)){
			int oppBlu = (5-entry.indexOf("b"));
			String newString = "";
			if (entry.indexOf("b")<oppBlu) {
				newString = entry.substring(0,entry.indexOf("b"))+entry.substring(oppBlu,oppBlu+1)+entry.substring(entry.indexOf("b")+1,oppBlu)+"b"+entry.substring(oppBlu+1);
			}
			else {
				newString = entry.substring(0,oppBlu)+"b"+entry.substring(oppBlu+1,entry.indexOf("b"))+entry.substring(oppBlu,oppBlu+1)+entry.substring(entry.indexOf("b")+1);
			}
			entry = newString;
		}
		if (colors.indexOf("r")<4) {
			int[] swap = {entry.indexOf("y"), entry.indexOf("g"), entry.indexOf("b"), entry.indexOf("c"), entry.indexOf("m"), entry.indexOf("r")};
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		if (colors.indexOf("b")>4) {
			int[] swap = {entry.indexOf("r"), entry.indexOf("c"), entry.indexOf("b"), entry.indexOf("g"), entry.indexOf("m"), entry.indexOf("y")}; 
			String[] l = new String[6];
			for (int j = 0; j<6; j++) {
				l[swap[j]] = colorTable[j];
			}
			String newString = "";
			for (int j = 0; j<6; j++)newString+=l[j];
			entry = newString;
		}
		//Done modifying sequence
		if (debug) System.out.println("Color sequence: "+entry);
		for (int j = 0; j<6; j++) {
			if (sequence[j]!=0)System.out.print(sequence[j]+" "+entry.substring(j,j+1));
			System.out.println();
		}
	}
	
	//helper
	
	//Gamepad,Equations,Maintenence,Mortal Kombat,Neutralization,European Travel
	//Helper methods
	
	public static int letterPos(String j) {
		return ((int)j.charAt(0))-96;
	}
	public static ArrayList<String> paragraphToWords(String garbage){
		    ArrayList<String> words = new ArrayList<String>();
		    String word = "";
		    for (int i = 0; i < garbage.length(); i++){
		      int ascii = (int)garbage.charAt(i);
		      if (ascii==32){
		        words.add(word);
		        word="";
		      }
		      else if (ascii>96&&ascii<123){
		        word+=((char)ascii);
		      }
		    }
		    words.add(word);
		    return words;
		  }
	public static ArrayList<String> wordToLetters(String word){
		    ArrayList<String> letters = new ArrayList<String>();
		    for (int i = 0; i < word.length(); i++){
		      letters.add(word.substring(i,i+1));
		    }
		    return letters;
		  }
	public static String morseConverter(String morseString){
		    String[] english = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
		                  "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", 
		                  "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

		    String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", 
		                ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.",
		                "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
		                "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
		                "-----"};
		    for (int i = 0; i < morse.length; i++){
		      if (morse[i].equals(morseString))return english[i];
		    }
		    return null;
		  }
	public static String englishConverter(String englishString){
		    String[] english = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
		                  "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", 
		                  "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

		    String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", 
		                ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
		                "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----",
		                "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.",
		                "-----"};
		    for (int i = 0; i < morse.length; i++){
		      if (english[i].equals(englishString))return morse[i];
		    }
		    return null;
		  }
	public static boolean mazeHelper(int y, int x, String[][]maze, ArrayList<String> currentMoves, int desty, int destx, int movesLeft){
		if (y==desty&&x==destx){
			moves.add(currentMoves);
			return true;
		}
		if (movesLeft==0)return false;
		if (maze[y][x].toLowerCase().contains("u")){
			if (currentMoves.size()>0){
				if (!currentMoves.get(currentMoves.size()-1).equals("d")){
					ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
					newMoves.add("u");
					if (mazeHelper(y-1,x,maze,newMoves,desty,destx,movesLeft-1))return true;
				}
			}
			else{
				ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
				newMoves.add("u");
				if (mazeHelper(y-1,x,maze,newMoves,desty,destx,movesLeft-1))return true;
			}
		}
		if (maze[y][x].toLowerCase().contains("d")){
			if (currentMoves.size()>0){
				if (!currentMoves.get(currentMoves.size()-1).equals("u")){
					ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
					newMoves.add("d");
					if (mazeHelper(y+1,x,maze,newMoves,desty,destx,movesLeft-1))return true;
				}
			}
			else{
				ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
				newMoves.add("d");
				if (mazeHelper(y+1,x,maze,newMoves,desty,destx,movesLeft-1))return true;
			}
		}
		if (maze[y][x].toLowerCase().contains("l")){
			if (currentMoves.size()>0){
				if (!currentMoves.get(currentMoves.size()-1).equals("r")){
					ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
					newMoves.add("l");
					if (mazeHelper(y,x-1,maze,newMoves,desty,destx,movesLeft-1))return true;
				}
			}
			else{
				ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
				newMoves.add("l");
				if (mazeHelper(y,x-1,maze,newMoves,desty,destx,movesLeft-1))return true;
			}
		}
		if (maze[y][x].toLowerCase().contains("r")){
			if (currentMoves.size()>0){
				if (!currentMoves.get(currentMoves.size()-1).equals("l")){
					ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
					newMoves.add("r");
					if (mazeHelper(y,x+1,maze,newMoves,desty,destx,movesLeft-1))return true;
				}
			}
			else{
				ArrayList<String> newMoves = (ArrayList<String>) currentMoves.clone();
				newMoves.add("r");
				if (mazeHelper(y,x+1,maze,newMoves,desty,destx,movesLeft-1))return true;
			}
		}
		return false;
	}
	public static boolean splitHelper(int pos, int[] nums, int[] g1, int[] g2) {
		boolean found = false;
		if (pos >= nums.length) {
			ArrayList<ArrayList<Integer>> j = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> group1 = toArrayList(g1);
			ArrayList<Integer> group2 = toArrayList(g2);
			j.add(group1);
			j.add(group2);
				if (sumArray(g1)==sumArray(g2)&&sumArray(g1)!=0) {
					boolean dupe = false;
					for (ArrayList<ArrayList<Integer>> k : lists) {
						if ((k.get(0).equals(group1)&&k.get(1).equals(group2))||(k.get(1).equals(group1)&&k.get(0).equals(group2)))dupe=true;
					}
					if (!dupe)lists.add(j);
					return true;
				}
			return false;
		}
		if (splitHelper(pos+1, nums, g1, g2))found=true;
		g1[pos] = nums[pos];
		int[] g1copy = g1.clone();
		if (splitHelper(pos+1, nums, g1copy, g2))found = true;
		g1[pos] = 0;
		g2[pos] = nums[pos];
		int[] g2copy = g2.clone();
		if (splitHelper(pos+1, nums, g1, g2copy))found = true;	
		g2[pos] = 0;
		return found;
	}
	public static ArrayList<Integer> toArrayList(int[] me){
		ArrayList<Integer> thing = new ArrayList<Integer>();
		for (int i : me)if (i!=0)thing.add(i);
		return thing;
	}
	public static int sumArray(int[] k) {
		int sum = 0;
		for (int i : k)sum+=i;
		return sum;
	}
	public static String PlayfairEncrypter(String keyword, String message) {
		String answer = "";
		keyword=keyword.toLowerCase();
		message=message.toLowerCase();
		for (int i = 0; i<message.length(); i++) {
			if (message.substring(i,i+1).equals("j"))message=message.substring(0,i)+"i"+message.substring(i+1);
			if (i>0) {
				if (message.substring(i,i+1).equals(message.substring(i-1,i))&&i%2==1)message=message.substring(0,i)+"x"+message.substring(i+1);
			}
		}
		if (message.length()%2==1)message+="x";
		//Eliminate Duplicates In Keyword
		String keywordNoDupes = "";
		for (int i = 0; i<keyword.length(); i++) {
			if (keyword.substring(i,i+1).equals("j"))keyword=keyword.substring(0,i)+"i"+keyword.substring(i+1);
			if (!keywordNoDupes.contains(keyword.substring(i, i+1)))keywordNoDupes=keywordNoDupes+keyword.substring(i,i+1);
		}
		for (int i = 0; i<26; i++) {
			String letter = (char)(i+97)+"";
			if (!letter.equals("j")) {
				if (!keywordNoDupes.contains(letter))keywordNoDupes=keywordNoDupes+letter;
			}
		}
		
		ArrayList<String> table = new ArrayList<String>();
		table.add(keywordNoDupes.substring(0,5));
		table.add(keywordNoDupes.substring(5,10));
		table.add(keywordNoDupes.substring(10,15));
		table.add(keywordNoDupes.substring(15,20));
		table.add(keywordNoDupes.substring(20,25));
		
		if (debug)for (String kl:table)System.out.println(kl);
		for (int i = 0; i < message.length(); i=i+2) {
			String letter1 = message.substring(i, i+1);
			String letter2 = message.substring(i+1,i+2);
			int row1 = -1;
			int row2 = -1;
			int col1 = -1;
			int col2 = -1;
			String answerLetter1 = "";
			String answerLetter2 = "";
			if (debug) System.out.println("Letters: "+letter1+" "+letter2);
			for (int j = 0; j <5; j++) {
				if (table.get(j).contains(letter1)) {
					row1=j;
				}
				if (table.get(j).contains(letter2)) {
					row2=j;
				}
			}
			col1 = table.get(row1).indexOf(letter1);
			col2 = table.get(row2).indexOf(letter2);
			
			if (row1==row2) {
				int newcol1 = col1+1;
				int newcol2 = col2+1;
				if (newcol1>4)newcol1-=5;
				if (newcol2>4)newcol2-=5;
				answerLetter1 = table.get(row1).substring(newcol1,newcol1+1);
				answerLetter2 = table.get(row2).substring(newcol2,newcol2+1);
			}
			else if (col1==col2) {
				int newrow1 = row1+1;
				int newrow2 = row2+1;
				if (newrow1>4)newrow1-=5;
				if (newrow2>4)newrow2-=5;
				answerLetter1 = table.get(newrow1).substring(col1, col1+1);
				answerLetter2 = table.get(newrow2).substring(col2, col2+1);
			}
			else {
				answerLetter1 = table.get(row1).substring(col2, col2+1);
				answerLetter2 = table.get(row2).substring(col1, col1+1);
			}
			answer=answer+answerLetter1+answerLetter2;
			if (debug) System.out.println("Answer: "+answerLetter1+" "+answerLetter2);
		}
		return answer;
	}
	public static String PlayfairSolver (String keyword,String message) {
		String answer = "";
		keyword=keyword.toLowerCase();
		message=message.toLowerCase();
		for (int i = 0; i<message.length(); i++) {
			if (message.substring(i,i+1).equals("j"))message=message.substring(0,i)+"i"+message.substring(i+1);
		}
		//Eliminate Duplicates
		String keywordNoDupes = "";
		for (int i = 0; i<keyword.length(); i++) {
			if (keyword.substring(i,i+1).equals("j"))keyword=keyword.substring(0,i)+"i"+keyword.substring(i+1);
			if (!keywordNoDupes.contains(keyword.substring(i, i+1)))keywordNoDupes=keywordNoDupes+keyword.substring(i,i+1);
		}
		for (int i = 0; i<26; i++) {
			String letter = (char)(i+97)+"";
			if (!letter.equals("j")) {
				if (!keywordNoDupes.contains(letter))keywordNoDupes=keywordNoDupes+letter;
			}
		}
		
		ArrayList<String> table = new ArrayList<String>();
		table.add(keywordNoDupes.substring(0,5));
		table.add(keywordNoDupes.substring(5,10));
		table.add(keywordNoDupes.substring(10,15));
		table.add(keywordNoDupes.substring(15,20));
		table.add(keywordNoDupes.substring(20,25));
		
		if (debug)for (String kl:table)System.out.println(kl);
		for (int i = 0; i < message.length(); i=i+2) {
			String letter1 = message.substring(i, i+1);
			String letter2 = message.substring(i+1,i+2);
			int row1 = -1;
			int row2 = -1;
			int col1 = -1;
			int col2 = -1;
			String answerLetter1 = "";
			String answerLetter2 = "";
			if (debug) System.out.println("Letters: "+letter1+" "+letter2);
			for (int j = 0; j <5; j++) {
				if (table.get(j).contains(letter1)) {
					row1=j;
				}
				if (table.get(j).contains(letter2)) {
					row2=j;
				}
			}
			col1 = table.get(row1).indexOf(letter1);
			col2 = table.get(row2).indexOf(letter2);
			
			if (row1==row2) {
				int newcol1 = col1-1;
				int newcol2 = col2-1;
				if (newcol1<0)newcol1+=5;
				if (newcol2<0)newcol2+=5;
				answerLetter1 = table.get(row1).substring(newcol1,newcol1+1);
				answerLetter2 = table.get(row2).substring(newcol2,newcol2+1);
			}
			else if (col1==col2) {
				int newrow1 = row1-1;
				int newrow2 = row2-1;
				if (newrow1<0)newrow1+=5;
				if (newrow2<0)newrow2+=5;
				answerLetter1 = table.get(newrow1).substring(col1, col1+1);
				answerLetter2 = table.get(newrow2).substring(col2, col2+1);
			}
			else {
				answerLetter1 = table.get(row1).substring(col2, col2+1);
				answerLetter2 = table.get(row2).substring(col1, col1+1);
			}
			answer=answer+answerLetter1+answerLetter2;
			if (debug) System.out.println("Answer: "+answerLetter1+" "+answerLetter2);
		}
		return answer;
	}
 	public static ArrayList<Integer> shiftLeft(ArrayList<Integer> shiftMe){
		ArrayList<Integer> j = new ArrayList<Integer>(shiftMe);
		for (int i = 0; i < shiftMe.size(); i++) {
			if (i == 0) {
				j.set(shiftMe.size()-1, shiftMe.get(0));
			}
			else {
				j.set(i-1, shiftMe.get(i));
			}
		}
		return j;
	}
	public boolean duplicatePorts() {
		ArrayList<String> newPorts = new ArrayList<String>();
		for (int i = 0; i < ports.size(); i++) {
			if (newPorts.contains(ports.get(i)))return true;
			newPorts.add(ports.get(i));
		}
		return false;
	}	
	public static int indexOf(int[] l, int k) {
		for (int i = 0; i < l.length; i++) {
			if (l[i]==k)return i;
		}
		return -1;
	}
	public static int indexOf(String[] l, String k) {
		for (int i = 0; i < l.length; i++) {
			if (l[i].equals(k))return i;
		}
		return -1;
	}
	public static void printArray(int[] j) {for (int l: j)System.out.print(l+" ");}
	public static void printArray(String[] j) {for (String l: j)System.out.print(l+" ");}
	public static int countOf(ArrayList<String> swires, String theObject) {
		int count = 0;
		for (int i = 0; i < swires.size(); i++) {
			if (swires.get(i).equals(theObject))count++;
		}
		return count;
	}
	public static int countOf(String word, String object) {
		int count = 0;
		for (int i = 0; i < word.length(); i++) if(word.substring(i, i+1).equals(object))count++;
		return count;
	}
	public boolean noLitIndicators() {
		for (String j : indicators)if(j.length()>3)return false;
		return true;
	}
	public int numOfLitIndicators() {
		int j = 0;
		for (String i : indicators)if (i.length()>3)j++;
		return j;
	}
	public int[] serialConvertToNums() {
		int[] serialNums = new int [6];
		for (int i = 0; i <6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				serialNums[i]=x;
			}
			catch(NumberFormatException e) {
				serialNums[i]=((int)serial.charAt(i))-96;
			}
		}
		return serialNums;
	}
	public static boolean isPrime(int num) {
		boolean flag = false;
		if (num<=1)flag=true;
		else {for(int i = 2; i <= num/2; ++i) {if(num % i == 0)flag = true;}}
		
		return !flag;
	}
	public int firstDigit() {
		int firstDigit = -1;
		for (int i = 0; i<6; i++) {
			
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				if (firstDigit==-1)firstDigit = x;
			}
			catch(NumberFormatException e) {}
		}
		return firstDigit;
	}
	public static double digitalRoot(double j) {
		int k = (int)((j*100)+0.1);
		while (k>9) {
			String newJ = k+"";
			int total = 0;
			for (int i = 0; i < newJ.length(); i++) {
				total+=Integer.parseInt(newJ.substring(i, i+1));
			}
			k = total;
		}
		return k;
	}
	public static int antiModulo(int num, int modulo) {
		while (num<1)num+=modulo;
		int j = num%modulo;
		if (j==0)j+=modulo;
		return j;
	}
	public static double doMath(double num1,double num2, String operator) {
		if (operator.equals("+")) {
			return num1+num2;
		}
		else if (operator.equals("-")) {
			return num1-num2;
		}
		else if (operator.equals("*")) {
			return num1*num2;
		}
		else if (operator.equals("/")) {
			return num1/num2;
		}
		else return 100000000;
	}
	public String FEHelper(int replace, String sequence, int newDigit, String color) {
		String before = "";
		if (replace!=0)before = sequence.substring(0,replace);
		String digit = sequence.substring(replace,replace+1);
		String after = "";
		if (replace!=9)after = sequence.substring(replace+1);
		int previous = Integer.parseInt(digit);
		int answer=-9999;
		if (color.equals("r")) {
			answer = newDigit+previous;
		}
		else if (color.equals("y")) {
			answer = previous-newDigit;
		}
		else if (color.equals("g")) {
			answer = previous+newDigit+5;
		}
		else if (color.equals("b")) {
			answer = newDigit-previous;
		}
		else {
			System.out.println("WRONG COLOR FEHelper!");
		}
		while (answer<0)answer+=10;
		while (answer>9)answer-=10;
		return before+answer+after;
	}
	public String TBColor (int button) {
		if ((button)%3==0)return "r";
		if ((button)%3==1)return "g";
		if ((button)%3==2)return "b";
		return "error, helper method";
	}
	public int modulo(int j, int mod) {
		while (j<mod)j+=mod;
		return j%mod;
	}
	public int sumOfSerialDigits() {
		int total = 0;
		for (int i = 0; i<6; i++) {
			try {
				int x = Integer.parseInt(serial.substring(i,i+1));
				total+=x;
			}
			catch(NumberFormatException e) {
			}
		}
		return total;
	}
}