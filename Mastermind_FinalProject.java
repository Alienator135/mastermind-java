package Mastermind_FinalProject;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
public class Mastermind_FinalProject {
/* Name: Hubert Chan
 * Date: Wednesday, January 12, 2022
 * Game rules: The computer will always be the Codemaker. 
 * The code can be made up of any combination of the colored pegs but can not use two or more pegs of the same color. 
 * There will be 6 colors. The user/decoder gets 10 guesses, and if they do not get it by the tenth try, they lose. 
 * For every peg that the player gets the correct color and position, they are given a red peg (without specifying which is 
 * correct). For every peg that the player gets the correct color but not position, they will be given a white peg (without 
 * specifying which is correct).
 * Anything that is not mentioned here should be mentioned in the instructions below in the print statements
 */

	public static void main(String[] args) throws Exception{
		//define and initialize variables for later use
		int correct = 0;
		int numTurns = 0;
		int numGames = 0;
		boolean shouldContinue = true;
		String gameResult="Lose";
		String [] secretCode = new String [4]; //array for storing secret code
		String [] guessResult = new String [4]; //array for storing result of user's guess
		String [] guessArray = new String [4]; //array for storing user's guess
		File namefile = new File("user_name.txt"); //file for storing user name
		File historyfile = new File("game_history.txt"); //file for storing game history
		PrintWriter nameoutput= new PrintWriter(namefile);
		PrintWriter gameoutput= new PrintWriter(historyfile);
		Scanner myscanner = new Scanner(System.in); //scanner for user input
		
		//instructions for user in case they do not know how to play Mastermind
		System.out.println("Welcome to the game of Mastermind!");
		System.out.println("How to play Mastermind: ");
		System.out.println("The computer will generate a secret code, that you're going to have to guess!");
		System.out.println("The code will be 4 different colours, no repeats");
		System.out.println("You will then enter a color for each of the guesses, and the computer will tell you 4 results for whether you are correct");
		System.out.println("If you guess a colour that is in the code, but not in the correct order, you will get a white pin/result");
		System.out.println("If you guess a colour that is in the code and in the correct order/location, you will get a red pin/result");
		System.out.println("The possible colours to guess are: ");
		System.out.println("R - Red");
		System.out.println("Y - Yellow");
		System.out.println("B - Blue");
		System.out.println("G - Green");
		System.out.println("P - Purple");
		System.out.println("O - Orange");
		System.out.println("You only get 10 tries, so make them count!");
		System.out.println("When you guess, remember to capitalize and only enter the first letter of the colour! The computer will only accept valid guesses");
		
		//ask user for username, store on separate file for later use
		System.out.println("Enter a username: ");
		String username = myscanner.nextLine();
		nameoutput.println(username);
		nameoutput.close();
		System.out.println("Welcome "+username+"!"); //user friendly greeting
		
		String response="";
		while (!response.equals("yes")) { //the game will not start until the user is ready and enters 'yes'
			System.out.println("Enter 'yes' when you want to start the game: ");
			response = myscanner.nextLine();
		}
		
		generateSecretCode(secretCode); //generates secret code of colours
		
		
		//actual game code starts here
		do {
			if (numTurns==0) {
				System.out.println("Let's start the game!");
			}
			for (int i=0;i<4;i++) { //asks user for colour 4 times, one for each in the code
				boolean validAns = false;
				while(validAns!=true) { //loops question until user gives a valid answer
					System.out.println("Enter color "+(i+1)+":"); 
					String guess = myscanner.nextLine();
					if(validateInput(guess)) { //uses a method to identify if the guess is a valid input
						guessArray[i]=guess;
					}else {
						continue;
					}
					validAns=true;
				}
			}
			correct = 0; 
			for (int i=0;i<4;i++) {
				guessResult[i]=checkGuess(guessArray[i],i, secretCode); //using the method checkGuess, checks each of 4 guess for if they are red, white, or nothing
				if (guessResult[i].equals("red")) {
					correct++; //stores number of correct(red) guesses
				}
			}
			numTurns++; //stores total number of turns
			
			Scanner input = new Scanner(namefile);
			String name = input.next(); //read user's name from namefile
			input.close();
			
			if(correct<4) { //if there are less than 4 correct(red) guesses
				int redCounter=0; //stores number of red results
				int whiteCounter=0; //stores number of white results
				for(int i=0;i<4;i++) {
					if(guessResult[i]=="red") {
						redCounter++; //tally total number of red in the guessResult array
					}else if(guessResult[i]=="white") {
						whiteCounter++; //tally total number of white in the guessResult array
					}
				}
				System.out.println("Turn "+numTurns+":" ); //gives user all necessary information from the turn so they can make a better guess next round
				System.out.print("Your guess: "); //prints out user's guess in more organized manner
				for(int i=0;i<3;i++) {
					System.out.print(guessArray[i]+", ");
				}
				System.out.println(guessArray[3]);
				System.out.println(redCounter+" red and "+whiteCounter+" white"); //prints number of red and white results
				if (numTurns==10&&correct!=4) { //if they run of tries, they get a lose message and the computer reveals the answer
					System.out.println("Sorry "+name+", you ran out of tries. Nice try, I bet you'll win next time!");
					System.out.println("The secret code was: "+guessArray[0]+", "+guessArray[1]+", "+guessArray[2]+", "+guessArray[3]+"!");
					gameResult="Lose";
				}
			}
			
			if (correct==4) { //if the user gets all 4 colours correct in the right order, they get a win message
				System.out.println("Congratulations, "+name+"! You beat the computer and won Mastermind!");
				System.out.println("The secret code was: "+guessArray[0]+", "+guessArray[1]+", "+guessArray[2]+", "+guessArray[3]+"!");
				gameResult="Win";
			}
				
			if (correct!=4&&numTurns!=10) { //if the game is not done yet, ask if they want to continue
				System.out.println("Continue to next round? (yes/no) ");
				if(myscanner.nextLine().equals("no")) { //if they don't want to continue, ask if they want to exit game (asks twice in case the user accidentally entered 'no' and then loses the game)
					System.out.println("Exit game? (yes/no)");
					if (myscanner.nextLine().equals("yes")) {
						shouldContinue=false; //if they answer yes to exit game, stops the game and game result becomes 'lose'
						gameResult="Lose";
					}
				}
			}
				
			if (correct==4||numTurns==10||shouldContinue==false) { //if the game has ended in any way (win,lose,exit game)
				numGames++; //stores total amount of games played and finished by user so far
				gameoutput.println("Game "+numGames+":"); //store/print the game's info onto another file for game history
				gameoutput.println("Turns: "+numTurns+"  Result: "+gameResult);
				
				System.out.println("Play again? (yes/no)"); //asks if user wants to play again
				if (myscanner.nextLine().equals("yes")){ //if answer is yes, start another game. if it is any other answer it will exit loop
					numTurns=0; //if yes, reset all variables
					correct=0;
					shouldContinue=true;
					for (int i=0;i<4;i++) {
						guessArray[i]="";
						guessResult[i]="";
					}
					generateSecretCode(secretCode); //generate new secret code
				}else {	
					break;
				}
			}
		}while(correct!=4&&numTurns!=10); //keep looping while user does not have 4 correct or runs out of turns. will not stop looping until user chooses to stop playing at any point
		
		//close scanner and printwriter
		myscanner.close();
		gameoutput.close();
		
		//print out game history for user
		System.out.println("Game history: ");
		Scanner gameinput = new Scanner(historyfile);
		while(gameinput.hasNext()){
			System.out.println(gameinput.nextLine());
		}
		System.out.println("Thanks for playing!"); //final exit message for user
		
		gameinput.close();
		
	}
	
	public static String checkGuess(String userguess, int guessnum, String secretCode[]) { //method for checking user guess for if it is red or white
		for(int i=0;i<4;i++) {
			if (secretCode[i].equals(userguess)){ //if the user guess is a colour in secret code
				if (i==guessnum) { //check if in correct location. if correct, return "red" result
					return "red";
				}else { //if incorrect location but correct colour, return "white" result
					return "white";
				}
			}
		}
		return ""; //if colour is not in code, return nothing
	}
	

	public static void generateSecretCode(String secretCode[]) { //method for creating a secret code with no repeated colours
		boolean newcolorfound= false; //define and initialize variables
		String newColor="";
		
		for(int i=0; i<4; i++) { //loops 4 times, once for each new colour
			newcolorfound=false;
			
			while (newcolorfound==false) { //while the code doesn't find a new colour
				
				int randNum = (int)(Math.random() * 5)+0; //random number generator to find colours
				
				if (randNum==0) { //depending on the number, a colour is assigned to create secret code of colours
					newColor="R";
				}else if (randNum==1) {
					newColor="Y";
				}else if (randNum==2) {
					newColor="B";
				}else if (randNum==3) {
					newColor="G";
				}else if (randNum==4) {
					newColor="P";
				}else if (randNum==5) {
					newColor="O";
				}
				
				if (newColor.equals(secretCode[0])||newColor.equals(secretCode[1])||newColor.equals(secretCode[2])||newColor.equals(secretCode[3])){
					continue; //if the new number/colour that has just been generated is already in the secret code, find a new number/colour
				}else {
					secretCode[i]=newColor; //store the new colour into secret code array
					newcolorfound=true; //stop while loop
					
				}
			}
		}
	}		
	
	
	public static boolean validateInput(String usercolor) { //method for validating user input
		for(int i=0;i<6;i++) { //checks if the user guess is any of 6 possible answers
			if(usercolor.equals("R")) { //if the user guess is any of 6 possible answers, return true
				return true;
			}else if(usercolor.equals("Y")) {
				return true;
			}else if(usercolor.equals("B")) {
				return true;
			}else if(usercolor.equals("G")) {
				return true;
			}else if(usercolor.equals("P")) {
				return true;
			}else if(usercolor.equals("O")) {
				return true;
			}
			}
		return false; //if the user guess is not a valid answer, returns false
		}
	}
			