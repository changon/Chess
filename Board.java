import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Scanner;
import java.util.Random;
import org.jblas.FloatMatrix;
import org.jblas.MatrixFunctions;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.IndexOutOfBoundsException;
import java.lang.ArrayIndexOutOfBoundsException;

/* ***********************************************
TODO board:
Quiescence search
Regulating move search depth during game
************************************************/
public class Board
{
 //public FloatMatrix X;
 public String [] letters;
 public String [] nums;
 public static String [][] board;//represent board moves
 public int [][] pieces;//represent piece presecens
 public int [] returnEN = new int [16];
 public int [] returnCastle= new int [4];
 private boolean wa1W = false;
 private int wa2 = 0;
 private boolean wa2W = false;
 private int wb2 = 0;
 private boolean wa3W = false;
 private int wc2 = 0;
 private boolean wa4W = false;
 private int wd2 = 0;
 private boolean wa5W = false;
 private int we2 = 0;
 private boolean wa6W = false;
 private int wf2 = 0;
 private boolean wa7W = false;
 private int wg2 = 0;
 private boolean wa8W = false;
 private int wh2 = 0;
 private boolean ba1B = false;
 private int ba6 = 0;
 private boolean ba2B = false;
 private int bb6 = 0;
 private boolean ba3B = false;
 private int bc6 = 0;
 private boolean ba4B = false;
 private int bd6 = 0;
 private boolean ba5B = false;
 private int be6 = 0;
 private boolean ba6B = false;
 private int bf6 = 0;
 private boolean ba7B = false;
 private int bg6 = 0;
 private boolean ba8B = false;
 private int bh6 = 0;
 /**The following is the code for pieces
 white:
  king  = 6;
  queen = 5;
  rook = 4;
  bishop = 3;
  kinght = 2; 
  pawn = 1;
 
 Black:
  king  = 12;
  queen = 11;
  rook = 10;
  bishop = 9;
  kinght = 8; 
  pawn = 7;
 *///Code for pieces 0-12
//board created as the following from method setup()
/* 
a8 b8 c8 d8 e8 f8 g8 h8  
a7 b7 c7 d7 e7 f7 g7 h7  
a6 b6 c6 d6 e6 f6 g6 h6  
a5 b5 c5 d5 e5 f5 g5 h5  
a4 b4 c4 d4 e4 f4 g4 h4  
a3 b3 c3 d3 e3 f3 g3 h3  
a2 b2 c2 d2 e2 f2 g2 h2  
a1 b1 c1 d1 e1 f1 g1 h1

10 8 9 11 12 9 8 10 
7 7 7 7 7 7 7 7      BLACK SIDE
0 0 0 0 0 0 0 0 
0 0 0 0 0 0 0 0 
0 0 0 0 0 0 0 0 
0 0 0 0 0 0 0 0 
1 1 1 1 1 1 1 1      WHITE SIDE
4 2 3 5 6 3 2 4 
*/
 /**
 Sets up initial board state (or pieces[][] array)
 */
 public void setup()//board, into initial state
 {
  for (int i =0; i < 16; i++) returnEN[i] = 0;
  String[] ltmp = {"a","b","c","d","e","f","g","h"};//represents a,b,c,d...
     String[] ntmp= {"1","2","3","4","5","6","7","8"}; //nums 1,2,3,4
     //set letters and nums
     letters = new String[8];
     nums = new String[8];
//completely non est necesse, since it literally reassigns on array to another...
     for (int i = 0; i < 8; i++) { letters[i] = ltmp[i]; nums[i] = ntmp[i]; }
 
  board = new String[8][8];
  pieces = new int [8][8];
  for (int i = 0; i < 8; i++) //creates by going A8,B8,C8...A1, B1..
  {
   for (int j = 0; j < 8; j++)
   {
    String rep = letters[j] + nums[i];
    board[7-i][j] = rep;//to go from 7
         //down to 0   
   }
  }
  for (int i = 0; i < 8; i++)//sets the initial board state
  {
   for (int j = 0; j < 8; j++)
   {
    String cmp = board[i][j];

    /* ROOK SETUP */
    if ( (cmp.equals("a8")) || (cmp.equals("a1")) || (cmp.equals("h8")) || (cmp.equals("h1")) )
    {
     //black side
     if (cmp.contains("8")) {  pieces[i][j] = 10; }
     //white side
     else if (cmp.contains("1")) {  pieces[i][j] = 4;  }
    }
    
    /* KNIGHT SETUP */
    else if ( (cmp.equals("b8")) || (cmp.equals("b1")) || (cmp.equals("g8")) || (cmp.equals("g1")) )
    {
     //black side
     if (cmp.contains("8")) {  pieces[i][j] = 8; }
     //white side
     else if (cmp.contains("1")) {  pieces[i][j] = 2;  }
    }
    /* BISHOP SETUP */
    else if ( (cmp.equals("c8")) || (cmp.equals("c1")) || (cmp.equals("f8")) || (cmp.equals("f1")) )
    {
     //black side
     if (cmp.contains("8")) {  pieces[i][j] = 9; }
     //white side
     else if (cmp.contains("1")) {  pieces[i][j] = 3;  }
    }
    /* QUEEN SETUP */
    else if ( (cmp.equals("d8")) || (cmp.equals("d1")) )
    {
     //black side
     if (cmp.contains("8")) {  pieces[i][j] = 11; }
     //white side
     else if (cmp.contains("1")) {  pieces[i][j] = 5;  }
    }
    /* KING SETUP */
    else if ( (cmp.equals("e8")) || (cmp.equals("e1")) )
    {
     //black side
     if (cmp.contains("8")) {  pieces[i][j] = 12; }
     //white side
     else if (cmp.contains("1")) {  pieces[i][j] = 6;  }
    }
    /* PAWN SETUP */
    //black
    else if ( cmp.contains("7") ) { pieces[i][j] = 7; }
    //white
    else if ( cmp.contains("2") ) { pieces[i][j] = 1; }
    else { pieces[i][j] = 0; } 
   }
  }
 }//end setup
 /**
 Constructor for Board class
 @param k
  if 1, one player game begins
  if 2, two player game begins
 */
 public Board(int k)//int k) //if k = 1 => 1player play, else if k =2 => 2player play
 {
  setup();
  if (k ==1) {onePlayerGame();}
  else if (k==2) {twoPlayerGame(); }
 }
 /**
 Additional constructor for setup
 */
 public Board()
 {setup();}
 /**
  *  Currently incomplete, but will check the board state and generate all moves for the opposing side to move, if in range of the curent sides king position, check is declared, in addition if kings positions all surrounded, checkmate declared
 @param isBlack, if current player is black or not
 @return false if game can continue
 */
 boolean checkCheckMate(boolean isBlack)
 {
  return false;
 }
 /**
 begins a two player game of alternating humanMoves
 */
 public void twoPlayerGame()
 {
  System.out.println("Two player game!");
  Evaluation evaluator = new Evaluation();
  int ticks = 1;
  boolean black = false;
  while( (ticks < 101) || (checkCheckMate(black) == false))//50 move limits
  { 
   printBoard(3);
   if (ticks % 2 == 0) { black = true;} else { black = false;} 
   humanMove(black);
   ticks++;
  }
 }
 /**
 begins a one player game, alternating between a humanmove and alpha/beta modified minimax search for AI
 */
 public void onePlayerGame()
 {
  //redirect System err to a text file
  File file = new File("Error.txt");
  try{
   FileOutputStream fos = new FileOutputStream(file);
   PrintStream ps = new PrintStream (fos);
   System.setErr(ps);
  } catch(FileNotFoundException ex){System.err.println("File not found");}

  //Start the game
  Evaluation evaluator = new Evaluation();
  int ticks = 1;
  boolean black = false;
  int d = 4;// 1, 3, 5, no search at minimizing node
  //depth of 1 is fine, 2 makes sense, 3 is defunct
  while( (ticks < 101) || (checkCheckMate(black) == false))
  {
   //printBoard(1);
   System.out.println();printBoard(3);
   if (ticks % 2 == 0) { black = true; 
   List <Integer> allLeaf = new ArrayList <Integer> (); //represents the leaf utility values, assigned with Minimax, based on our heuristic NN fx
   
   List <String> allLeafStart = new ArrayList <String> ();//start moves, weights
   List <String> allChildEnd = new ArrayList <String> (); //String positions of potential moves to make
   List <String> alive = new ArrayList <String> (); //represents the piece moves to move
  
   allLeafStart = getMovesToMinimax(black, 2);//start moves
   allChildEnd = getMovesToMinimax(black, 1);//endmoves
   alive = getMovesToMinimax(black, 3);//piece indicators
 
   //store movestominimax leaves and leavesPos
   //getMovesToMinimax for all three lists above, and iterate through updating board
   int bestIndex = 0;
   int bestUtility = -1000;
   //int bestEndPiece = 0;
	//allLeafStart = organizeLeaves(allLeafStart, allChildEnd, alive, black, 1); //for more effective alpha/beta prune
	//allChildEnd = organizeLeaves(allLeafStart, allChildEnd, alive, black, 2); //for more effective alpha/beta prune
	//alive = organizeLeaves(allLeafStart, allChildEnd, alive, black, 3); //for more effective alpha/beta prune

   //System.out.println(String.format("i start at here size %d", allLeafStart.size()));
   boolean mix = false;
   for (int i = 0;i < allLeafStart.size(); i ++)
   { 
    //if (allLeafStart.get(i).equals("d7"))System.out.println(String.format("Start move is %s, end move is %s, piece move is %s", allLeafStart.get(i), allChildEnd.get(i), alive.get(i)));
    String substitute = allChildEnd.get(i); if(substitute.contains("EP")){
		substitute=substitute.substring(2, substitute.length());
    }   	
    int endPiece = pieces[getPosition(substitute, 0)][getPosition(substitute, 1)];
    quickUpdateBoard(Integer.parseInt(alive.get(i)), allLeafStart.get(i), allChildEnd.get(i));  
	System.out.println(String.format("Start move is %s, end move is %s, piece move is %s", allLeafStart.get(i), allChildEnd.get(i), alive.get(i)));
    printBoard(3);
    int utility = AlphaBeta(d-1, -1000000, 1000000, black);
    //int utility = negamax(d-1, black);
    System.out.printf("utility is %d\n", utility);
    quickUpdateBoard(Integer.parseInt(alive.get(i)),  allChildEnd.get(i), allLeafStart.get(i));//return back to original state
    quickUpdate(endPiece, allChildEnd.get(i));
    allLeaf.add(new Integer (utility));
   if (utility > bestUtility){bestUtility = utility;  bestIndex = i; }//bestEndPiece = endPiece;} 
     if (utility == bestUtility){mix = true;}//get rand move of all equally valued child nodes
   }
   if (mix == true)
   {
   	List <Integer> indicesToMix = new ArrayList <Integer>();//array lists of indices with the same utility
   	for (int i =0; i < allLeaf.size(); i ++)
   	{
   		if (bestUtility == allLeaf.get(i))
   		{indicesToMix.add(i);}
   	}
   	Random r = new Random();
   	int tmpIndex = r.nextInt(indicesToMix.size());
   	bestIndex = indicesToMix.get(tmpIndex);
   }
   System.out.println("Here are our moves");
   for (int i = 0;i < allLeafStart.size(); i ++)
   {
    System.out.println( String.format("%s is the start position, %s is the end position, utility is %d", allLeafStart.get(i), allChildEnd.get(i),allLeaf.get(i) ) );
   }
   System.out.println("here are end moves");
   
   //update board based on greatest leaf node eval.
   //iterate trhough the allLeaf and update pieces to reflect teh highest minimax evaluation
   //account for checkmate moves, obvious recaptures
System.out.println(String.format("CHOSEN Start move is %s, end move is %s, piece move is %s", allLeafStart.get(bestIndex), allChildEnd.get(bestIndex), alive.get(bestIndex)));
    printBoard(3);
   quickUpdateBoard(Integer.parseInt(alive.get(bestIndex)), allLeafStart.get(bestIndex), allChildEnd.get(bestIndex));
    printBoard(3);
   System.out.println(String.format("move chosen is the piece %d, start pos %s, and end pos %s, utility is %d", Integer.parseInt(alive.get(bestIndex)), allLeafStart.get(bestIndex), allChildEnd.get(bestIndex), bestUtility));
   } else if (ticks % 2 ==1) { black = false; humanMove(black); }
   ticks++;
  }//end while loop for ticks
 }
 /**
 Get max Move; this function is used during the training session in the hinge loss function; we only base this on a depth of 1
 */
 public void getMaxMove(boolean black)
 {
  //redirect System err to a text file
  File file = new File("ErrorMaxMove.txt");
  try{
   FileOutputStream fos = new FileOutputStream(file);
   PrintStream ps = new PrintStream (fos);
   System.setErr(ps);
  } catch(FileNotFoundException ex){System.err.println("File not found");}

  //Start the game
  Evaluation evaluator = new Evaluation();
  int d = 1;// 1, 3 , 5, no search at minimizing node

   //printBoard(1);
   List <Integer> allLeaf = new ArrayList <Integer> (); //represents the leaf utility values, assigned with Minimax, based on our heuristic NN fx
   
   List <String> allLeafStart = new ArrayList <String> ();//start moves, weights
   List <String> allChildEnd = new ArrayList <String> (); //String positions of potential moves to make
   List <String> alive = new ArrayList <String> (); //represents the piece moves to move
  
   allLeafStart = getMovesToMinimax(black, 2);//start moves
   allChildEnd = getMovesToMinimax(black, 1);//endmoves
   alive = getMovesToMinimax(black, 3);//piece indicators
 
   //store movestominimax leaves and leavesPos
   //getMovesToMinimax for all three lists above, and iterate through updating board
   int bestIndex = 0;
   int bestUtility = -1000;
   //int bestEndPiece = 0;
   //organizeLeaves(allLeafStart, allChildEnd, alive, black); //for more effective alpha/beta prune
   //System.out.println(String.format("i start at here size %d", allLeafStart.size()));
   boolean mix = false;
   for (int i = 0;i < allLeafStart.size(); i ++)
   { 
    String substitute = allChildEnd.get(i); if(substitute.contains("EP")){
		substitute=substitute.substring(2, substitute.length());
    }   	
    int endPiece = pieces[getPosition(substitute, 0)][getPosition(substitute, 1)];
    quickUpdateBoard(Integer.parseInt(alive.get(i)), allLeafStart.get(i), allChildEnd.get(i));  
    int utility = AlphaBeta(d-1, -1000000, 1000000, black);
    quickUpdateBoard(Integer.parseInt(alive.get(i)),  allChildEnd.get(i), allLeafStart.get(i));//return back to original state
    quickUpdate(endPiece, allChildEnd.get(i));
    allLeaf.add(new Integer (utility));
    if (utility > bestUtility){bestUtility = utility;  bestIndex = i; }//bestEndPiece = endPiece;} 
    if (utility == bestUtility){mix = true;}//get rand move of all equally valued child nodes
   }
   if (mix == true)
   {
   	List <Integer> indicesToMix = new ArrayList <Integer>();//array lists of indices with the same utility
   	for (int i =0; i < allLeaf.size(); i ++)
   	{
   		if (bestUtility == allLeaf.get(i))
   		{indicesToMix.add(i);}
   	}
   	Random r = new Random();
   	int tmpIndex = r.nextInt(indicesToMix.size());
   	bestIndex = indicesToMix.get(tmpIndex);
   }
   
   //update board based on greatest leaf node eval.
   //iterate trhough the allLeaf and update pieces to reflect teh highest minimax evaluation
   //account for checkmate moves, obvious recaptures
    quickUpdateBoard(Integer.parseInt(alive.get(bestIndex)), allLeafStart.get(bestIndex), allChildEnd.get(bestIndex));
 }
 /**
 * Organizes (lists of) leaves based on immediate utility values, for effective alpha/beta pruning
 * @param allLeaves List represents the current starting positions of leaves being considered
 * @param allCEnd List represents all ending positions
 * @param alivehere List represents strings of numerical piece values in chess
 * @param black if current player is black or not...possibly non est necesse
 * @param indic indicates which list to return, 1 - leaves or start moves; 2 - endmoves; 3 - numeric pieces representing the piece of start moves
 */
 public List <String> organizeLeaves(List <String> allLeaves, List <String> allCEnd, List <String> alivehere, boolean black, int indic)
 {
  List <Integer> utilities = new ArrayList <Integer> ();//of immediate utilities
  List <Integer> ut = new ArrayList <Integer> ();//of immediate utilities

  Evaluation evaluatornator = new Evaluation();
   int c= -1;
   if (black == true){ c = 1;}
  for (int i = 0; i < allLeaves.size(); i ++)
  {
   String tmpChildEnd = allCEnd.get(i);
   if (allCEnd.get(i).contains("EP")) tmpChildEnd=tmpChildEnd.substring(2,4);
   int endPiece = pieces[getPosition(tmpChildEnd, 0)][getPosition(tmpChildEnd, 1)];
   quickUpdateBoard(Integer.parseInt(alivehere.get(i)), allLeaves.get(i), allCEnd.get(i));
   int curUtil = c*evaluatornator.hypothesis(pieces, black);
   quickUpdateBoard(Integer.parseInt(alivehere.get(i)),  allCEnd.get(i), allLeaves.get(i));//return back to original state
   quickUpdate(endPiece, allCEnd.get(i));
   //System.out.printf("curUtil is %d\n", curUtil);
   utilities.add(curUtil);
  }
  List <Integer> utilCpy = utilities;
  List <String> tmpL = new ArrayList <String>();
  List <String> tmpE = new ArrayList <String>();
  List <String> tmpA = new ArrayList <String>();
  int large = 122222222;//arbitrary
  for (int i =0; i < allLeaves.size(); i++)
  {
  int max = -10; int maxIndex= 0;
   for (int j = 0; j < allLeaves.size(); j ++)
   {
     int valetta = utilCpy.get(j);
     if (valetta > max)
     {
     	max = valetta;
     	maxIndex = j;
     }
   }
   tmpL.add(allLeaves.get(maxIndex));
   tmpE.add(allCEnd.get(maxIndex));
   tmpA.add(alivehere.get(maxIndex));
   ut.add(max);
   utilCpy.set(maxIndex, utilCpy.get(maxIndex)-large);//to ensure that it is not the min again
 }
 //for (int i = 0; i < ut.size(); i ++)
 	//{System.out.printf("%d is the ut\n", ut.get(i)); }
  if (indic == 1) return tmpL;
  else if (indic ==2) return tmpE;
  return tmpA;
 }
 /**
 human move turn, type in one string of starting position in algebraic notation, hit ENTER, then type in end move in algebraic notation as they spoke in Harry Potter
 @param black if current player is black or white
 */
 public void humanMove(boolean black)
 {
  String b1t = "Black"; if (black == false) b1t = "White";
  System.out.println(String.format("\n%s's move, type the current starting position and the ending position", b1t));
  Scanner scan = new Scanner(System.in);
  String start = (scan.next());
  String end = scan.next();
  quickUpdateBoard(getPiece(start), start, end );
 }
 /**
 get moves to perform minimax
 @param black if current player is black or white
 @param indicate 
   1 is for endmoves
   2 is for startmoves
   3 is for piece indicators
 @return indicative of the potential moves to update the current state, as indicated by int indicate
 */
 public List <String> getMovesToMinimax(boolean black, int indicate)
 {
  List <String> endMoves = new ArrayList <String> ();//end board moves
  List <String> startMoves = new ArrayList <String> ();//start board moves
  List <String> pieceMoves = new ArrayList <String> ();//integer values, must be converted from a string
  
  List <String> alive = new ArrayList <String> (); //represents the potential moves to move
  List <String> pieceVals = new ArrayList <String>();
  alive = getAlivePieces(black, 1);
  pieceVals = getAlivePieces(black, 2);
  for (int i = 0; i < alive.size(); i++)
  {
   String cur = alive.get(i);//possible piece to move   
   int curI = Integer.parseInt(pieceVals.get(i));
   int num = getPosition(cur, 0);
   int letter = getPosition(cur, 1);
   //generate all possible moves from this possible piece to move, or starting position
   List <String> potential = new ArrayList <String> ();
   if ( (curI == 6) || (curI == 12) ){potential = getKingMove(num, letter, black); }//get all possible KING moves from the starting position
   if ( (curI == 5) || (curI == 11) ){potential = getQueenMove(num, letter, black); }//get all possible QUEEN moves from the starting position
   if ( (curI == 4) || (curI == 10) ){potential = getBRMove(num, letter, 1, black); }//get all possible ROOK moves from the starting position
   if ( (curI == 3) || (curI == 9) ){potential = getBRMove(num, letter, 0, black); }//get all possible BISHOP moves from the starting position
   if ( (curI == 2) || (curI == 8) ){potential = getKnightMove(num, letter, black); }//get all possible KNIGHT moves from the starting position
   if ( (curI == 1) || (curI == 7) ){potential = getPawnMove(num, letter, black); }//get all possible PAWN moves from the starting position
   
   //System.out.println("movesToMake");
   //go through the potential moves, add to end,start, and piece indicators
   for (int j = 0; j < potential.size(); j ++)
   {
    endMoves.add(potential.get(j));
    startMoves.add(cur);//imoves
    pieceMoves.add(Integer.toString(curI));  
   }
  }
  if (indicate == 1){return endMoves;}else if(indicate ==2){return startMoves;}else if(indicate ==3){return pieceMoves;}
  System.err.println("Enter valid indicator. 1 is for the end moves, 2 is for the start moves, 3 is for the integer piece numbers"); return null;
 }
 /**
 perform alpha beta pruning beginning at a given root move
 alpha
 beta 
 @param d depth to search
 @param alpha value begin at -INF; maximum score maximizing player is guranteed
 @param beta value begin at INF; minimum score minimizing player is guranteed
 @param black if current player is black or white
 @return utility of a leaf node
 */
 int AlphaBeta(int d, int alpha, int beta, boolean black)
 {
  Evaluation evaluator = new Evaluation();
  if (d <= 0)//if someone accidentally sets d to 0
  {
   int c= -1;
   if (black == true){ c = 1;}
   return c*evaluator.hypothesis(pieces, black);
  }
  List <Integer> allLeaf = new ArrayList <Integer> (); //represents the potnetial leaf utility values, assigned with Minimax, based on our heuristic NN fx
  List <String> allLeafStart = new ArrayList <String> ();//start moves
  List <String> allChildEnd = new ArrayList <String> (); //String positions of potential moves to make
  List <String> alive = new ArrayList <String> (); //represents the pieces to move
  
  allLeafStart = getMovesToMinimax(black, 2);//start moves
  allChildEnd = getMovesToMinimax(black, 1);//endmoves
  alive = getMovesToMinimax(black, 3);//piece indicators
  //store movestominimax leaves and leavesPos
  //getMovesToMinimax for all three lists above, and iterate through updating board
  //allLeafStart = organizeLeaves(allLeafStart, allChildEnd, alive, black, 1); //for more effective alpha/beta prune
  //allChildEnd = organizeLeaves(allLeafStart, allChildEnd, alive, black, 2); //for more effective alpha/beta prune
  //alive = organizeLeaves(allLeafStart, allChildEnd, alive, black, 3); //for more effective alpha/beta prune
  for (int i = 0; i < allLeafStart.size(); i ++)
  { 
   //System.out.println(String.format("Start move is %s, end move is %s, piece move is %s", allLeafStart.get(i), allChildEnd.get(i), alive.get(i)));
   String substitute = allChildEnd.get(i); 
   if(substitute.contains("EP")){
		substitute=substitute.substring(2, substitute.length());
   }
   int endPiece = pieces[getPosition(substitute, 0)][getPosition(substitute, 1)];
   quickUpdateBoard(Integer.parseInt(alive.get(i)), allLeafStart.get(i), allChildEnd.get(i));
   int utility = -AlphaBeta(d-1, -beta, -alpha, (!black)); //return negamax value
   quickUpdateBoard(Integer.parseInt(alive.get(i)),  allChildEnd.get(i), allLeafStart.get(i));//return back to original state
   quickUpdate(endPiece, allChildEnd.get(i));//return back to original state
   
   if (utility >= beta) return beta; //prune off here, when minimizing score is less than maximizing score 
   if (utility > alpha) alpha = utility; 
  }
  //return alpha*(d*5);
  return alpha;
 }
 /**
 perform negamax on a given leaf node indicated by tmpPieces for the depth 'd'; max(a,b) = -min(-a,-b);
 @param d represents the depth to search
 @param black represents if player is black or white
 @return integer representing the utility on the initially considered leaf node as given by the updated stated of pieces[][]
 */
 int negamax(int d, boolean black)
 { 
  int best = -1000000000;
  Evaluation evaluator = new Evaluation();
  if (d <= 0)//if someone accidentally sets d to 0
  {
   int c= -1;
   if (black == true){ c = 1;}
   return c*evaluator.hypothesis(pieces, black);
  }
  //List <Integer> allLeaf = new ArrayList <Integer> (); //represents the potnetial leaf utility values, assigned with Minimax, based on our heuristic NN fx
  List <String> allLeafStart = new ArrayList <String> ();//start moves
  List <String> allChildEnd = new ArrayList <String> (); //String positions of potential moves to make
  List <String> alive = new ArrayList <String> (); //represents the pieces to move
  //getMovesToMinimax for all three lists above, and iterate through updating board
  allLeafStart = getMovesToMinimax(black, 2);//start moves
  allChildEnd = getMovesToMinimax(black, 1);//endmoves
  alive = getMovesToMinimax(black, 3);//piece indicators
  /*if (d <= 1) {
  	allLeafStart = organizeLeaves(allLeafStart, allChildEnd, alive, black, 1); //for more effective alpha/beta prune
  	allChildEnd = organizeLeaves(allLeafStart, allChildEnd, alive, black, 2); //for more effective alpha/beta prune
  	alive = organizeLeaves(allLeafStart, allChildEnd, alive, black, 3); //for more effective alpha/beta prune
  }*/
  //System.out.println(String.format("%d is start size, end isze is %d, piece size is %d", d, allLeafStart.size(), allChildEnd.size(), alive.size()));
  for (int i = 0; i < allLeafStart.size(); i ++)
  { 
   //printBoard(3);
   //if(pieces[2][3]==1 || pieces[2][2]==1)System.out.println(String.format("Start move is %s, end move is %s, piece move is %s", allLeafStart.get(i), allChildEnd.get(i), alive.get(i)));
   String substitute = allChildEnd.get(i); 
   if(substitute.contains("EP")){
		substitute=substitute.substring(2, substitute.length());
   }
   int endPiece = pieces[getPosition(substitute, 0)][getPosition(substitute, 1)];
   quickUpdateBoard(Integer.parseInt(alive.get(i)), allLeafStart.get(i), allChildEnd.get(i));
   int utility = -negamax(d-1, (!black)); //return negamax value
   quickUpdateBoard(Integer.parseInt(alive.get(i)),  allChildEnd.get(i), allLeafStart.get(i));//return back to original state
   quickUpdate(endPiece, allChildEnd.get(i));//return back to original state
   if (utility > best) {best = utility;} 
  }
  return best*(d*5);//discount factor for the more immediate rewards are reaped
 }
 /**
 updates the pieces array to help 
 @param farewell to set the pieces array position to
 @param result to indicate the position of the pieces array to change based on farewell
 */
 public void quickUpdate(int farewell, String result)
 {
  String tmpresult = result;
  if (result.contains("EP")) //tmpresult=result.substring(2, result.length()); //
  	return;//ignore if EP move since it is taken care of in the quickUpdateBoard
  pieces[getPosition(tmpresult,0)][getPosition(tmpresult,1)]= farewell;
 }
 /**
 update the board with all the given info::the pieces to update, the current piece to consider, the start position and the end position, and pece to replace
 @param curI represents the piece to be present at the ending position
 @param start represents the starting position of the piece, which is now vacated and zero
 @param end represents the ending position of the curI piece to be placed
 */
 public void quickUpdateBoard(int curI, String start, String end)
 {
//get start and end string for EP!!! this is causing -1 array thrown index bounds
//however multiple EP's may exist, thus we can either
//	have an additional check for the proper ep num,letter 
//	alternatively we may just take out the ep, for it will be undone anyways
	String tmpstart = start; 
	String tmpend = end;
	if (start.contains("EP")){
		tmpstart= start.substring(2, 4);
	}
	if (end.contains("EP")){
		tmpend= end.substring(2,4);
	}
 	//identify the scenarios
 	//a2
 	if (pieces[6][0] == 1 && pieces[4][1] == 7 && pieces[5][0] == 0 && pieces[4][0]==0 && returnEN[0] == 0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][0] ==1) && (pieces[6][0]==0)) {
			returnEN[0] ++;//a2 
    	}
		return;
	}
	//b2
 	if (pieces[6][1] == 1 && (pieces[4][0] == 7 || pieces[4][2] ==7) && pieces[5][1] == 0 && pieces[4][1]==0 && returnEN[1] ==0 ) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][1] ==1) && (pieces[6][1]==0)) {
    		returnEN[1] ++;//b2
    	}
		return;
	}
	//c2
	if (pieces[6][2] == 1 && (pieces[4][1] == 7 || pieces[4][3] ==7) && pieces[5][2] == 0 && pieces[4][2]==0 && returnEN[2] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][2] ==1) && (pieces[6][2]==0)) {
    		returnEN[2] ++;//c2
    	}
		return;
	}
	//d2
 	if (pieces[6][3] == 1 && (pieces[4][2] == 7 || pieces[4][4] ==7) && pieces[5][3] == 0 && pieces[4][3]==0 && returnEN[3]==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][3] ==1) && (pieces[6][3]==0)) {
    		returnEN[3] ++;//d2
    	}
		return;
	}
	//e2
 	if (pieces[6][4] == 1 && (pieces[4][3] == 7 || pieces[4][5] ==7) && pieces[5][4] == 0 && pieces[4][4]==0 && returnEN[4]==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][4] ==1) && (pieces[6][4]==0)) {
    		returnEN[4] ++;//e2
    	}
		return;
	}
	//f2
 	if (pieces[6][5] == 1 && (pieces[4][4] == 7 || pieces[4][6] ==7) && pieces[5][5] == 0 && pieces[4][5]==0 && returnEN[5] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][5] ==1) && (pieces[6][5]==0)) {
    		returnEN[5] ++;//f2
    		System.out.println(returnEN[5]);
    		//printBoard(3);
    	}
		return;
	}
	//g2
 	if (pieces[6][6] == 1 && (pieces[4][5] == 7 || pieces[4][7] ==7) && pieces[5][6] == 0 && pieces[4][6]==0 && returnEN[6] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][6] ==1) && (pieces[6][6]==0)) {
    		returnEN[6] ++;//g2
    		System.out.println(returnEN[5]);
    		//printBoard(3);
    	}
		return;
	}
	//h2
 	if (pieces[6][7] == 1 && pieces[4][6] == 7 && pieces[5][7] == 0 && pieces[4][7]==0 && returnEN[7] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[4][7] ==1) && (pieces[6][7]==0)) {
			returnEN[7] ++;//h2    		
			//printBoard(3);
    	}
		return;
	}
	//other side
	//a6
 	if (pieces[1][0] == 7 && pieces[3][1] == 1 && pieces[2][0] == 0 && pieces[3][0]==0 && returnEN[8] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][0] == 0) && (pieces[3][0]==7)) {
			returnEN[8] ++;//a6    		
			//printBoard(3);
    	}
		return;
	}
	//b6
 	if (pieces[1][1] == 7 && (pieces[3][0] == 1 || pieces[3][2]==1) && pieces[2][1] == 0 && pieces[3][1]==0 && returnEN[9] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][1] == 0) && (pieces[3][1]==7)) {
			returnEN[9] ++;//b6    		
			///printBoard(3);
    	}
		return;
	}
	//c6
 	if (pieces[1][2] == 7 && (pieces[3][1] == 1 || pieces[3][3]==1) && pieces[2][2] == 0 && pieces[3][2]==0 && returnEN[10] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][2] == 0) && (pieces[3][2]==7)) {
			returnEN[10] ++;//c6    		
			//printBoard(3);
    	}
		return;
	}
	//d6
 	if (pieces[1][3] == 7 && (pieces[3][2] == 1 || pieces[3][4]==1) && pieces[2][3] == 0 && pieces[3][3]==0 && returnEN[11] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][3] == 0) && (pieces[3][3]==7)) {
			returnEN[11] ++;//d6    		
			//printBoard(3);
    	}
		return;
	}
	//e6
 	if (pieces[1][4] == 7 && (pieces[3][3] == 1 || pieces[3][5]==1) && pieces[2][4] == 0 && pieces[3][4]==0 && returnEN[12] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][4] == 0) && (pieces[3][4]==7)) {
			returnEN[12] ++;//e6    		
			//printBoard(3);
    	}
		return;
	}
	//f6
 	if (pieces[1][5] == 7 && (pieces[3][4] == 1 || pieces[3][6]==1) && pieces[2][5] == 0 && pieces[3][5]==0 && returnEN[13] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][5] == 0) && (pieces[3][5]==7)) {
			returnEN[13] ++;//f6    		
			//printBoard(3);
    	}
		return;
	}
	///g6
 	if (pieces[1][6] == 7 && (pieces[3][5] == 1 || pieces[3][7]==1) && pieces[2][6] == 0 && pieces[3][6]==0 && returnEN[4] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][6] == 0) && (pieces[3][6]==7)) {
			returnEN[14] ++;//g6    		
			//printBoard(3);
    	}
		return;
	}
	//h6
 	if (pieces[1][7] == 7 && (pieces[3][6] == 1 || pieces[2][7] == 0) && pieces[3][7]==0 && returnEN[15] ==0) {
 		pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    	pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
    	if ((pieces[1][7] ==0) && (pieces[3][7]==7)) {
			returnEN[15] ++;//h6    		
			//printBoard(3);
    	}
		return;
	}
	//now interpretation of EP moves
	if(EnPassantEndCheck(curI, start, end)) return;
	if(EnPassantStartCheck(curI, start, end)) return;
	//interpret start moves with
 	pieces[getPosition(tmpstart, 0)][getPosition(tmpstart, 1)]=0;
    pieces[getPosition(tmpend, 0)][getPosition(tmpend, 1)]=curI;
 }
 /**
 interprets En passant startmoves for restoring the board
 */
 public boolean EnPassantStartCheck(int currentI, String begin, String last)
 {
  	List <Integer> vals = new ArrayList <Integer>(); 
 	if (begin.contains("EP")){
 		int number = getPosition(begin.substring(2,begin.length()) ,0); int let = getPosition(begin.substring(2,begin.length()),1);
 		
  		if (wa2 == 1 && pieces[4][0]==0 && pieces[4][1]==0 && pieces[5][0]==7 && number ==4 && let ==1) {
 			pieces[4][0] = 1; pieces[4][1] = 7; pieces[5][0] =0; wa2 =0; returnEN[0] = 1;  
 			return true;
 		}
		else if (wb2 ==1 && pieces[4][1]==0 && pieces[5][1]==7 && (pieces[5][0] == 0 || pieces[5][2]==0) && number == 4 && (let ==0 || let==2)){
			if(begin.contains("a")){
 				pieces[4][1] = 1; pieces[5][0] = 7; pieces[5][1] =0; 
			}		
			if(begin.contains("c")){
 				pieces[4][1] = 1; pieces[5][2] = 7; pieces[5][1] =0; 
			}			
 			wa2 =0; returnEN[1] = 1; //printBoard(3);
			 return true;
		}
		else if (wc2 ==1 && pieces[4][2]==0 && pieces[5][2]==7 && (pieces[5][1] == 0 || pieces[5][3]==0) && number == 4 && (let ==1 || let==3)){
			if(begin.contains("b")){
 				pieces[4][2] = 1; pieces[5][1] = 7; pieces[5][2] =0; 
			}		
			if(begin.contains("d")){
 				pieces[4][2] = 1; pieces[5][3] = 7; pieces[5][2] =0; 
			}		
			wc2 =0; returnEN[2] = 1; return true;
		}
		else if (wd2 ==1 && pieces[4][3]==0 && pieces[5][3]==7 && (pieces[5][2] == 0 || pieces[5][4]==0) && number == 4 && (let ==2 || let==4)){
			if(begin.contains("c")){
 				pieces[4][3] = 1; pieces[5][2] = 7; pieces[5][3] =0; 
			}		
			if(begin.contains("e")){
 				pieces[4][3] = 1; pieces[5][4] = 7; pieces[5][3] =0; 
			}		
			wd2 =0; returnEN[3] = 1; return true;
		}
		else if (we2 ==1 && pieces[4][4]==0 && pieces[5][4]==7 && (pieces[5][3] == 0 || pieces[5][5]==0) && number == 4 && (let ==3 || let==5)){
			if(begin.contains("d")){
 				pieces[4][4] = 1; pieces[5][3] = 7; pieces[5][4] =0; 
			}		
			if(begin.contains("f")){
 				pieces[4][4] = 1; pieces[5][5] = 7; pieces[5][4] =0; 
			}		
			we2 =0; returnEN[4] = 1; return true;
		}
		else if (wf2 ==1 && pieces[4][5]==0 && pieces[5][5]==7 && (pieces[5][4] == 0 || pieces[5][6]==0) && number == 4 && (let ==4 || let==6)){
			if(begin.contains("e")){
 				pieces[4][5] = 1; pieces[5][4] = 7; pieces[5][5] =0; 
			}		
			if(begin.contains("g")){
 				pieces[4][5] = 1; pieces[5][6] = 7; pieces[5][5] =0; 
			}		
			wf2 =0; returnEN[5] = 1; return true;
		}
		else if (wg2 ==1 && pieces[4][6]==0 && pieces[5][6]==7 && (pieces[5][5] == 0 || pieces[5][7]==0) && number == 4 && (let ==5 || let==7)){
			if(begin.contains("f")){
 				pieces[4][6] = 1; pieces[5][5] = 7; pieces[5][6] =0; 
			}		
			if(begin.contains("h")){
 				pieces[4][6] = 1; pieces[5][7] = 7; pieces[5][6] =0; 
			}		
			wg2 =0; returnEN[6] = 1; return true;
		}
 		else if (wh2 ==1 && pieces[4][7]==0 && pieces[4][6]==0 && pieces[5][7]==7 && number ==4 && let ==6) {
 			pieces[4][7] = 1; pieces[4][6] = 7; pieces[5][7] =0; wh2 =0; returnEN[7] = 1; return true;
 		}
 	
  		else if (ba6 ==1 && pieces[3][0]==0 && pieces[2][0]==1 && pieces[3][1]==0 && number == 3 && let ==1) {
 			pieces[3][1] = 1; pieces[3][0] = 7; pieces[2][0] =0; ba6 =0; returnEN[8] = 1; return true;
 		}
		else if (bb6 ==1 && pieces[3][1]==0 && pieces[2][1]==1 && (pieces[3][0]==0 || pieces[3][2]==0) && number == 3 && (let ==0 || let ==2)){
			if(begin.contains("a")){
 				pieces[3][0] = 1; pieces[3][1] = 7; pieces[2][1] =0; 
			}		
			if(begin.contains("c")){
 				pieces[3][2] = 1; pieces[3][1] = 7; pieces[2][1] =0; 
			}		
 			bb6 =0; returnEN[9] = 1; return true;		
		}
		else if (bc6 ==1 && pieces[3][2]==0 && pieces[2][2]==1 && (pieces[3][1]==0 || pieces[3][3]==0) && number == 3 && (let ==1 || let ==3)){
			if(begin.contains("b")){
 				pieces[3][1] = 1; pieces[3][2] = 7; pieces[2][2] =0; 
			}		
			if(begin.contains("d")){
 				pieces[3][3] = 1; pieces[3][2] = 7; pieces[2][2] =0; 
			}		
 			bc6 =0; returnEN[10] = 1; return true;		
		}
		else if (bd6 ==1 && pieces[3][3]==0 && pieces[2][3]==1 && (pieces[3][2]==0 || pieces[3][4]==0) && number == 3 && (let ==2 || let ==4)){
			if(begin.contains("c")){
 				pieces[3][2] = 1; pieces[3][3] = 7; pieces[2][3] =0; 
			}		
			if(begin.contains("e")){
 				pieces[3][4] = 1; pieces[3][3] = 7; pieces[2][3] =0; 
			}		
 			bd6 =0; returnEN[11] = 1; return true;		
		}
		else if (be6 ==1 && pieces[3][4]==0 && pieces[2][4]==1 && (pieces[3][3]==0 || pieces[3][5]==0) && number == 3 && (let ==3 || let ==5)){
			if(begin.contains("d")){
 				pieces[3][3] = 1; pieces[3][4] = 7; pieces[2][4] =0; 
			}		
			if(begin.contains("f")){
 				pieces[3][5] = 1; pieces[3][4] = 7; pieces[2][4] =0; 
			}		
 			be6 =0; returnEN[12] = 1; return true;		
		}
		else if (bf6 ==1 && pieces[3][5]==0 && pieces[2][5]==1 && (pieces[3][4]==0 || pieces[3][6]==0) && number == 3 && (let ==4 || let ==6)){
			if(begin.contains("e")){
 				pieces[3][4] = 1; pieces[3][3] = 7; pieces[2][4] =0; 
			}		
			if(begin.contains("g")){
 				pieces[3][6] = 1; pieces[3][3] = 7; pieces[2][4] =0; 
			}		
 			bf6 =0; returnEN[13] = 1; return true;		
		}
		else if (bg6 ==1 && pieces[3][6]==0 && pieces[2][6]==1 && (pieces[3][5]==0 || pieces[3][7]==0) && number == 3 && (let ==5 || let ==7)){
			if(begin.contains("f")){
 				pieces[3][5] = 1; pieces[3][3] = 7; pieces[2][4] =0; 
			}		
			if(begin.contains("h")){
 				pieces[3][7] = 1; pieces[3][3] = 7; pieces[2][4] =0; 
			}		
 			bg6 =0; returnEN[14] = 1; return true;		
		}
   		else if (bh6 ==1 && pieces[3][7]==0 && pieces[2][7]==1 && pieces[3][6]==0 && number == 3 && let ==6) {
 			pieces[3][6] = 1; pieces[3][7] = 7; pieces[2][7] =0; bh6 =0; returnEN[15] = 1; return true;
 		}
 	}
 	return false;
 }
 /**
 interprets En passant endmoves, or the kill
 */
 public boolean EnPassantEndCheck(int currentI, String begin, String last)
 {
 	if (last.contains("EP")){
 		int number = getPosition(begin, 0); int let = getPosition(begin, 1);
 		String rep = last.substring(2, last.length());
 		//a2
 		if (returnEN[0] ==1 && pieces[4][0] == 1 && pieces[5][0] == 0 && pieces[4][1]==7 && number ==4 && let ==1) {
 			pieces[4][0] = 0; //pawn that forwarded captured
 			pieces[5][0] = 7; //piece moves diagonally as usual
 			pieces[4][1] =0;
 			wa2++; returnEN[0] = 50; return true;}
		//b2
 		else if (returnEN[1]==1 && pieces[4][1]==1 && pieces[5][1]==0 && (pieces[4][2]==7 || pieces[4][0]==7) && number == 4 && (let==0 || let==2)) {
			//nested here for both possible attacking 4,0;4,2
			//a-case
			printBoard(3);
			if (begin.contains("a")){
				pieces[4][0] = 0; 
				pieces[5][1]= 7;
				pieces[4][1]=0;
			}
			//c-case, to be explicit
			else if (begin.contains("c")){
				pieces[4][2] = 0; 
				pieces[5][1]= 7;
				pieces[4][1]=0;
			}
			printBoard(3);
			wb2++; returnEN[1]=50; 
			return true;
		}
		//c2
		else if (returnEN[2]==1 && pieces[4][2]==1 && pieces[5][2]==0 && (pieces[4][3]==7 || pieces[4][1]==7) && number == 4 && (let==1 || let==3)) {
			//nested here for both possible attacking 4,0;4,2
			//b-case
			if (begin.contains("b")){
				pieces[4][1] = 0; 
				pieces[5][2] = 7;
				pieces[4][2] = 0;
			}
			//d-case, to be explicit
			else if (begin.contains("d")){
				pieces[4][3] = 0; 
				pieces[5][2]= 7;
				pieces[4][2]=0;
			}
			wc2++; returnEN[2]=50;
			return true;
		}
		//d2
		else if (returnEN[3]==1 && pieces[4][3]==1 && pieces[5][3]==0 && (pieces[4][4]==7 || pieces[4][2]==7) && number == 4 && (let==2 || let==4)) {
			//nested here for both possible attacking 4,0;4,2
			//c-case
			if (begin.contains("c")){
				pieces[4][2] = 0;//attacking pawn start 
				pieces[5][3] = 7;//pawn attacking
				pieces[4][3] = 0;//pawn captured
			}
			//e-case, to be explicit
			else if (begin.contains("e")){
				pieces[4][4] = 0; //attacking pawn start
				pieces[5][3]= 7;//pawn attacking
				pieces[4][3]=0;//pawn captured
			}
			wd2++; returnEN[3]=50;
			return true;
		}		
		//e2
		else if (returnEN[4]==1 && pieces[4][4]==1 && pieces[5][4]==0 && (pieces[4][5]==7 || pieces[4][3]==7) && number == 4 && (let==3 || let==5)) {
			//nested here for both possible attacking 4,0;4,2
			//c-case
			if (begin.contains("d")){
				pieces[4][3] = 0;//attacking pawn start 
				pieces[5][4] = 7;//pawn attacking end
				pieces[4][4] = 0;//pawn captured
			}
			//e-case, to be explicit
			else if (begin.contains("f")){
				pieces[4][5] = 0; //attacking pawn start
				pieces[5][4]= 7;//pawn attacking end
				pieces[4][4]=0;//pawn captured
			}
			we2++; returnEN[4]=50;
			return true;
		}		
		//f2
		else if (returnEN[5]==1 && pieces[4][5]==1 && pieces[5][5]==0 && (pieces[4][6]==7 || pieces[4][4]==7) && number == 4 && (let==4 || let==6)) {
			//e-case
			if (begin.contains("e")){
				pieces[4][4] = 0;//attacking pawn start 
				pieces[5][5] = 7;//pawn attacking end
				pieces[4][5] = 0;//pawn captured
			}
			//g-case, to be explicit
			else if (begin.contains("g")){
				pieces[4][6] = 0; //attacking pawn start
				pieces[5][5]= 7;//pawn attacking end
				pieces[4][5]=0;//pawn captured
			}
			wf2++; returnEN[5]=50;
			return true;
		}				
		//g2
		else if (returnEN[6]==1 && pieces[4][6]==1 && pieces[5][6]==0 && (pieces[4][7]==7 || pieces[4][5]==7) && number == 4 && (let==5 || let==7)) {
			//e-case
			if (begin.contains("f")){
				pieces[4][5] = 0;//attacking pawn start 
				pieces[5][6] = 7;//pawn attacking end
				pieces[4][6] = 0;//pawn captured
			}
			//g-case, to be explicit
			else if (begin.contains("h")){
				pieces[4][7] = 0; //attacking pawn start
				pieces[5][6]= 7;//pawn attacking end
				pieces[4][6]=0;//pawn captured
			}
			wg2++; returnEN[6]=50;
			return true;
		}		
		//h2
 		else if (returnEN[7] ==1 && pieces[4][7]==1 && pieces[5][7]==0 && pieces[4][6]==7 && number == 4 && let==6) {
 			pieces[4][6] = 0; //pawn that forwarded captured
 			pieces[5][7] = 7; //piece moves diagonally as usual
 			pieces[4][7] =0;
 			wh2++; returnEN[7] = 50; return true;}
		//other side
		//a6
 		else if (returnEN[8] ==1 && pieces[3][0]==7 && pieces[2][0]==0 && pieces[3][1]==1 && number == 3 && let ==1) {
 			pieces[3][0] = 0; //pawn capture
 			pieces[2][0] = 1; //piece moves diagonally as usual
 			pieces[3][1] =0;//attacking piece prior spot
 			ba6++; returnEN[8] = 50; return true;
 		}
		//b6
 		else if (returnEN[9] ==1 && pieces[3][1]==7 && pieces[2][1]==7 && (pieces[3][0]==1 || pieces[3][2]==1) && number ==3 && (let==0 || let ==2)) {	
 			if(begin.contains("a")){
 				pieces[3][1] = 0; //pawn capture
 				pieces[2][1] = 1; //piece moves diagonally as usual
 				pieces[3][0] =0;//attacking piece prior spot
 			}
  			if(begin.contains("c")){
 				pieces[3][1] = 0; //pawn capture
 				pieces[2][1] = 1; //piece moves diagonally as usual
 				pieces[3][2] =0;//attacking piece prior spot
 			}
 			bb6++; returnEN[9] = 50; return true;
 		}
		//c6
 		else if (returnEN[10] ==1 && pieces[3][2]==7 && pieces[2][2]==7 && (pieces[3][1]==1 || pieces[3][3]==1) && number ==3 && (let==1 || let ==3)) {	
 			if(begin.contains("b")){
 				pieces[3][2] = 0; //pawn capture
 				pieces[2][2] = 1; //piece moves diagonally as usual
 				pieces[3][1] =0;//attacking piece prior spot
 			}
  			if(begin.contains("d")){
 				pieces[3][2] = 0; //pawn capture
 				pieces[2][2] = 1; //piece moves diagonally as usual
 				pieces[3][3] =0;//attacking piece prior spot
 			}
 			bc6++; returnEN[10] = 50; return true;
 		}
 		//d6
 		else if (returnEN[11] ==1 && pieces[3][3]==7 && pieces[2][3]==7 && (pieces[3][2]==1 || pieces[3][4]==1) && number ==3 && (let==2 || let ==4)) {	
 			if(begin.contains("c")){
 				pieces[3][3] = 0; //pawn capture
 				pieces[2][3] = 1; //piece moves diagonally as usual
 				pieces[3][2] =0;//attacking piece prior spot
 			}
  			if(begin.contains("e")){
 				pieces[3][3] = 0; //pawn capture
 				pieces[2][3] = 1; //piece moves diagonally as usual
 				pieces[3][4] =0;//attacking piece prior spot
 			}
 			bd6++; returnEN[11] = 50; return true;
 		}
  		//e6
 		else if (returnEN[12] ==1 && pieces[3][4]==7 && pieces[2][4]==7 && (pieces[3][3]==1 || pieces[3][5]==1) && number ==3 && (let==3 || let ==5)) {	
 			if(begin.contains("d")){
 				pieces[3][4] = 0; //pawn capture
 				pieces[2][4] = 1; //piece moves diagonally as usual
 				pieces[3][3] =0;//attacking piece prior spot
 			}
  			if(begin.contains("f")){
 				pieces[3][4] = 0; //pawn capture
 				pieces[2][4] = 1; //piece moves diagonally as usual
 				pieces[3][5] =0;//attacking piece prior spot
 			}
 			be6++; returnEN[12] = 50; return true;
 		}
  		//f6
 		else if (returnEN[13] ==1 && pieces[3][5]==7 && pieces[2][5]==7 && (pieces[3][4]==1 || pieces[3][6]==1) && number ==3 && (let==4 || let ==6)) {	
 			if(begin.contains("e")){
 				pieces[3][5] = 0; //pawn capture
 				pieces[2][5] = 1; //piece moves diagonally as usual
 				pieces[3][4] =0;//attacking piece prior spot
 			}
  			if(begin.contains("g")){
 				pieces[3][5] = 0; //pawn capture
 				pieces[2][5] = 1; //piece moves diagonally as usual
 				pieces[3][6] =0;//attacking piece prior spot
 			}
 			bf6++; returnEN[13] = 50; return true;
 		}
  		//g6
 		else if (returnEN[14] ==1 && pieces[3][6]==7 && pieces[2][6]==7 && (pieces[3][5]==1 || pieces[3][7]==1) && number ==3 && (let==5 || let ==7)) {	
 			if(begin.contains("f")){
 				pieces[3][6] = 0; //pawn capture
 				pieces[2][6] = 1; //piece moves diagonally as usual
 				pieces[3][5] =0;//attacking piece prior spot
 			}
  			if(begin.contains("h")){
 				pieces[3][6] = 0; //pawn capture
 				pieces[2][6] = 1; //piece moves diagonally as usual
 				pieces[3][7] =0;//attacking piece prior spot
 			}
 			bg6++; returnEN[14] = 50; return true;
 		}
		//h6
 		else if (returnEN[15] ==1 && pieces[3][7]==7 && pieces[2][7]==0 && pieces[3][6]==1 && number == 3 && let ==6) {
 			pieces[3][7] = 0; //pawn capture
 			pieces[2][7] = 7; //piece moves diagonally as usual
 			pieces[3][6] =0;//attacking piece prior spot
 			bh6++; returnEN[15] = 50; return true;
 		}	 
 	}	
 	return false;
 }
 /**
 returns all of the starting positions of living pieces for the respective player side (w/black)
 1 returns a list of all the string potential moves, 2 returns all of the piece values; each correspond in order
 @param black if current player is black or not
 @param indicate 1- potential moves to make, 2 - piece values
 @return representative of the integer indicate
 */
 public Vector <String> getAlivePieces(boolean black, int indicate)
 {
  Vector <String> all = new Vector <String>();
  Vector <String> allTo = new Vector <String>();
  all.clear(); allTo.clear();
  if (black == true)
  {
   for (int i = 7; i < 13; i ++)
   {
    //possible starting string positions of the black pieces
    List <String> possible = getStrIndices(i);
    for (int j = 0; j < possible.size(); j++)
    {
     all.add(possible.get(j));
     allTo.add(Integer.toString(i));
    }
   }
  }
  else if (black == false)
  {
   for (int i = 1; i < 7; i ++)
   {
    List <String> possible = getStrIndices(i);
    for (int j = 0; j < possible.size(); j++)
    {
     all.add(possible.get(j));
     allTo.add(Integer.toString(i));
    }
   }
  }
  if (indicate ==1 ){return all; }else if (indicate == 2){ return allTo; }else if((indicate < 1)||(indicate>2)){System.out.println("Enter a valid indicator");}
  return null;
 }
 /**
 gets the piece integer value from a given board position
 @param position of piece as given by algebraic notation, exampla gratia: "a2"
 @return integer 1-12 of the piece at the position given by String position
 */
 public int getPiece (String position)
 {
  for (int i = 0; i < 8; i++)
  {
   for (int j = 0; j < 8; j ++)
   {
    if (board[i][j].equals(position)){
         return pieces[i][j];
    }
   }
  }
  return -1; //if unable to locate...Inicates invalidity of position cur
 }
 /**
 Converts board position to integer positions
 @param cur is the string position
 @param indicator indicates whether or not to return
  the number - 0, or letter - 1 index
 @return integer of the position on pieces[][] as represented by the number and letter corresponding to algebraic notation, based on indicator num or letter is given; 0:0 is a8, 1:0 is a7, 0:1 is b8
 */
 public int getPosition(String cur, int indicator)
 {
  for (int i = 0; i < 8; i++)
  {
   for (int j = 0; j < 8; j ++)
   {
    if (board[i][j].equals(cur)){
         if (indicator == 0){ return i;}
         else if(indicator == 1){ return j;}
    }
   }
  }
  return -1; //if unable to locate...Inicates invalidity of position cur
 }
 /**
  returns the letter,num of all possible pieces to be considered
  @param x 0-12 representing the pieces
  @return letter and num to be parsed
 */
 public List <String> getStrIndices(int x)
 {
  List <String> plausible = new ArrayList <String> ();
  for (int i = 0; i < 8; i ++)
  {
   for (int j = 0; j < 8; j++)
   {
    if (pieces[i][j] == x){
     plausible.add(board[i][j]);
    }
   }
  }
  return plausible;
 }
 /**
 Update for ONE move
 allmoves generated, so in the 2p game, this can be used as well
 order of process
 Find potential pieces (via positions) that could have done it
 now with each starting position of these pieces, generate lal moves available,
  if move is equal ot the endmove provided, the start positin is updated to the indices 
  previously obtained
 @param cur is the endmove
 @param b_w indicates black/white move based on; b_w: 0 black 1 white
 */
 public void updateBoard(String cur, int b_w)//updates the pieces array based on pgn format
 //players moves
 {//based on pgn 
  List <String> plausible = new ArrayList <String> ();//represents the pieces that could have moved
  List <String> potential = new ArrayList <String> ();//potential endmoves available, refreshed with each iter
  List <String> starting = new ArrayList <String> ();//starting positions of pieces
  String [] rep = {"K", "Q", "R", "B", "N"};
  //We narrow down which pieces could have moved
  boolean kill = false;//nicer word capture
//System.out.println(String.format("cur is %s", cur));
 
  if (cur.contains("x")){ kill = true; }
  boolean isBlack= false; if (b_w == 2){isBlack = true;}
  boolean pawnMove = true;//represents pawn movement//
 //pawn promotion
 if (cur.contains("="))//e8=Q or exe8=Q
 {
  if (isBlack == true){plausible = getStrIndices(7);} else if(isBlack == false) {plausible=getStrIndices(1);}
  //defaulted to -1 to throw exception
  int promotion = -1;
  int num = -1; 
  int letter = -1;
  int forNum = -1;//represent forward movement promotion
  if (isBlack == false){ forNum = 1;}

  //get promotion
  for (int i = 1; i < 5; i ++)
  {
   if (cur.contains(rep[i]))
   {
    if (i == 1){  if(isBlack == true){ promotion = 11; } else if (isBlack == false){ promotion = 5; } }//get all possible QUEEN moves from the starting position
    if (i == 2){  if(isBlack == true){ promotion = 10; } else if (isBlack == false){ promotion = 4; } }//get all possible ROOK moves from the starting position
    if (i == 3){  if(isBlack == true){ promotion = 9; } else if (isBlack == false){ promotion = 3; } }//get all possible BISHOP moves from the starting position
    if (i == 4){  if(isBlack == true){ promotion = 8; } else if (isBlack == false){ promotion = 2; } }
   }
  }
  
  if (cur.length() == 6)//exe8=Q
  {
   num =getPosition(cur.substring(2,4), 0);
   letter = getPosition(cur.substring(2,4), 1);
   for (int i = 0;i < plausible.size(); i ++)//go through potential starting points
   {
    String cmp = plausible.get(i);
    //if it is a potential AND it's first letter is equivalent to the letter given from which the pawn promoted and captured
    if ( (cmp.equals(board[num+forNum][letter+1])) && (cmp.substring(0,1).equals(cur.substring(0,1))) )
    {
     pieces [num+forNum][letter+1] = 0;
    }
    if ( (cmp.equals(board[num+forNum][letter-1])) && (cmp.substring(0,1).equals(cur.substring(0,1))) )
    {
     pieces[num+forNum][letter-1] = 0;
    }
   }
  }
  else if (cur.length() ==5)//xe8=Q
  {
   num =getPosition(cur.substring(1,3), 0);
   letter = getPosition(cur.substring(1,3), 1);
   
   for (int i = 0;i < plausible.size(); i ++)//go through potential starting points
   {
    String cmp = plausible.get(i);
    if (cmp.equals(board[num+forNum][letter+1]))
    {
     pieces [num+forNum][letter+1] = 0;
    }
    if (cmp.equals(board[num+forNum][letter-1]))
    {
     pieces[num+forNum][letter-1] = 0;
    }
   }
  }
  else if (cur.length() == 4)//e8=Q
  {
   num =getPosition(cur.substring(0,2), 0);
   letter = getPosition(cur.substring(0,2), 1);
   //here is the losing
   pieces[num+forNum][letter]=0;
  }
  //here is the promotion
  pieces[num][letter]=promotion;
  return;
 }
 //EP scenario
 if (cur.contains("e.p.")){//dxe5
 	int vvv = 1; int moving = 1; //moving is used to get the piece captured via en passant
 	if (isBlack){ vvv = 7; moving = -1;}
 	String endPos = cur.substring(2,4);
 	String character = cur.substring(0,1) + Integer.toString(Integer.parseInt(endPos.substring(1,2))-moving);
 	//System.out.println(String.format("char is %s and endpos is %s", character, endPos));
 	//System.out.println("integer");
 	//System.out.println(Integer.parseInt(endPos.substring(1,2)));
 	pieces[getPosition(endPos, 0)][getPosition(endPos, 1)] = vvv;//set piece 
 	pieces[getPosition(endPos, 0) + moving][getPosition(endPos,1)] = 0;//rm opposing two skip piece
 	pieces[getPosition(character, 0)][getPosition(character, 1)]= 0;//now remove the prior position of the attacking piece
 	if (endPos.substring(0,1).equals("a")){
 		if (isBlack){
 			returnEN[8] = 10;
 		}
 		else {
 			returnEN[0] = 10;
 		}
 	}
  	if (endPos.substring(0,1).equals("b")){
 		if (isBlack){
 			returnEN[9] = 10;
 		}
 		else {
 			returnEN[1] = 10;
 		}
 	}
  	if (endPos.substring(0,1).equals("c")){
 		if (isBlack){
 			returnEN[10] = 10;
 		}
 		else {
 			returnEN[2] = 10;
 		}
 	}
  	if (endPos.substring(0,1).equals("d")){
 		if (isBlack){
 			returnEN[11] = 10;
 		}
 		else {
 			returnEN[3] = 10;
 		}
 	}
  	if (endPos.substring(0,1).equals("e")){
 		if (isBlack){
 			returnEN[12] = 10;
 		}
 		else {
 			returnEN[4] = 10;
 		}
 	}
  	if (endPos.substring(0,1).equals("f")){
 		if (isBlack){
 			returnEN[13] = 10;
 		}
 		else {
 			returnEN[5] = 10;
 		}
 	}
   	if (endPos.substring(0,1).equals("g")){
 		if (isBlack){
 			returnEN[14] = 10;
 		}
 		else {
 			returnEN[6] = 10;
 		}
 	}
   	if (endPos.substring(0,1).equals("h")){
 		if (isBlack){
 			returnEN[15] = 10;
 		}
 		else {
 			returnEN[7] = 10;
 		}
 	}
 	return;
 }
 //Castling scenario; assumes that castling preconditions are valid
 if (cur.equals("O-O"))
 {
  if (isBlack == true)
  {
   //move the king
   pieces[0][6] = 12;
   pieces[0][4] = 0;
   //move the rook
   pieces[0][7] = 0;
   pieces[0][5]=10;
   returnCastle[1]=1;
  }
  else
  {
   //move the king
   pieces[7][6] = 6;
   pieces[7][4] = 0;
   //move the rook
   pieces[7][7] = 0;
   pieces[7][5]=4;   
   returnCastle[0]=1;
  }
  return;
 }
 //Queenside castling
 if (cur.equals("O-O-O"))
 {
  if (isBlack == true)
  {
   //move the king
   pieces[0][2] = 12;
   pieces[0][4] = 0;
   //move the rook
   pieces[0][0] = 0;
   pieces[0][3]=10;
   returnCastle[3]=1;
  }
  else
  {
   //move the king
   pieces[7][2] = 6;
   pieces[7][4] = 0;
   //move the rook
   pieces[7][0] = 0;
   pieces[7][3]=4;   
   returnCastle[2]=1;
  } 
  return;
 }
 for (int x =0; x < 5; x ++)//check for special piece movement
 {
  if(cur.contains(rep[x]))
  {  
    pawnMove = false;
    if (x == 0){  if(isBlack == true){ plausible = getStrIndices(12); } else if (isBlack == false){ plausible = getStrIndices(6); } }//get all possible KING moves from the starting position
    if (x == 1){  if(isBlack == true){ plausible = getStrIndices(11); } else if (isBlack == false){ plausible = getStrIndices(5); } }//get all possible QUEEN moves from the starting position
    if (x == 2){  if(isBlack == true){ plausible = getStrIndices(10); } else if (isBlack == false){ plausible = getStrIndices(4); } }//get all possible ROOK moves from the starting position
    if (x == 3){  if(isBlack == true){ plausible = getStrIndices(9); } else if (isBlack == false){ plausible = getStrIndices(3); } }//get all possible BISHOP moves from the starting position
    if (x == 4){  if(isBlack == true){ plausible = getStrIndices(8); } else if (isBlack == false){ plausible = getStrIndices(2); } }//get all possible KNIGHT moves from the starting position
    //we now have each position of the pieces that could have made this move
    //cur can be length four 
    int letter, num;
    String start;
    String ending;
    outerloop:
    for (int i = 0; i < plausible.size(); i ++)
    {
     //System.out.println();
     //System.out.println(String.format("%s is the plasuible start, integer x is %d", plausible.get(i), x));
     potential.clear();
     start = plausible.get(i);
     num = getPosition(start, 0);//letter,num represents the starting position
     letter = getPosition(start, 1);//letter,num represents the starting position
     //System.out.println(String.format("Num is %d, letter is %d, board is here %s", num, letter, board[num][letter]));

     if (x == 0){potential = getKingMove(num, letter, isBlack); }//get all possible KING moves from the starting position
     if (x == 1){potential = getQueenMove(num, letter, isBlack); }//get all possible QUEEN moves from the starting position
     if (x == 2){potential = getBRMove(num, letter, 1, isBlack); }//get all possible ROOK moves from the starting position
     if (x == 3){potential = getBRMove(num, letter, 0, isBlack); }//get all possible BISHOP moves from the starting position
     if (x == 4){potential = getKnightMove(num, letter, isBlack); }//get all possible KNIGHT moves from the starting position
     
     String curEval = cur;//Qe4     
     //essentiallly we decide to ignore the +, # indicators
     if (cur.contains("+") || cur.contains("#")){
     	curEval = cur.substring(0, cur.length()-1);
     }
     ending = curEval;
     if (curEval.length() == 5){ending = curEval.substring(3, 5);}//eQxd4
     else if (curEval.length() == 4) {ending = curEval.substring(2, 4);}//Qxe5
     else if (curEval.length() == 3) {ending = curEval.substring(1,3);}//Qa3
     //System.out.println(String.format("ending is %s", ending));
     for (int j=0; j < potential.size(); j++)//now we test against the potential move sthat could have made it
     {

      //System.out.println(String.format(" %s is the potential, %s is the cur",potential.get(j), ending));

      if (potential.get(j).equals(ending))//that means that the plausible(i) committed this move
      {
      //System.out.println("potential move found");
     
      //that is if the move is a kill move, and there is more than one possibillity for who committed
       if (curEval.length() == 5) {
        if ( start.substring(0,1).equals(curEval.substring(0,1)) ) 
        {    
         int buf = pieces[num][letter];
         pieces[num][letter] = 0;//refreshed since piece moved out of this postiion
         //if (kill == true){System.out.println(String.format("Capture on piece %d, to be changed into a string", pieces[getPosition(ending, 0)][getPosition(ending, 1)]));}
         pieces[getPosition(ending, 0)][getPosition(ending, 1)] = buf;
         }
       }//updated} }
       //cmp initial letter start pos with beginning letter that indicates where it started
       else{
        int buf = pieces[num][letter];
        pieces[num][letter] = 0;//refreshed since piece moved out of this postiion
        pieces[getPosition(ending, 0)][getPosition(ending, 1)] = buf;  //updated }//there is only one possible piece that could have made the move
       }
      }//end if potential and ending cmp
     }//end for potential
    }//end for plausible
  }//end if (cur.contains(rep[x]))
 }//end x loop  for special piece check
 if (pawnMove == true)
 {
   if (isBlack == true){plausible = getStrIndices(7);} else if(isBlack == false) {plausible=getStrIndices(1);}
   //we now have each position of the pieces that could have made this move
   //cur can be length four 
   int letter, num;
   String start;
   String ending;
   outerloop:
   for (int i = 0; i < plausible.size(); i ++)
   {
    //System.out.println();
    //System.out.println(String.format("%s is the plasuible start", plausible.get(i)));
    potential.clear();
    start = plausible.get(i);
    num = getPosition(start, 0);//letter,num represents the starting position
    letter = getPosition(start, 1);//letter,num represents the starting position
    //System.out.println(String.format("Num is %d, letter is %d, board is here %s", num, letter, board[num][letter]));
	
    potential = getPawnMove(num, letter, isBlack);//get all possible pawn moves from the starting position
    
    for (int j=0; j < potential.size(); j++)//now we test against the potential move sthat could have made it
    {
     String curEval = cur;//e4
     if (cur.length() == 4){curEval = cur.substring(2,4);}//exd4
     else if (cur.length() == 3) {curEval = cur.substring(1,3);}//xe5
     ending = curEval;
	 //System.out.println(String.format("%s is the start move %s is the potential move to make from the plausible start, %s is the cur", start ,potential.get(j), ending));

     if (potential.get(j).equals(ending))//that means that the plausible(i) committed this move
     {
     //System.out.println("potential move found");
     
      //that is if the move is a kill move, and there is more than one possibillity for who committed
      if (cur.length() == 4){
       if ( start.substring(0,1).equals(cur.substring(0,1)) ) 
       {    
        int buf = pieces[num][letter];
        pieces[num][letter] = 0;//refreshed since piece moved out of this postiion
        //if (kill == true){System.out.println(String.format("Capture on piece %d, to be changed into a string", pieces[getPosition(ending, 0)][getPosition(ending, 1)]));}
        pieces[getPosition(ending, 0)][getPosition(ending, 1)] = buf;
        }
      }//updated} }
      //cmp initial letter start pos with beginning letter that indicates where it started
      else{
       int buf = pieces[num][letter];
       pieces[num][letter] = 0;//refreshed since piece moved out of this postiion
       pieces[getPosition(ending, 0)][getPosition(ending, 1)] = buf;  //updated }//there is only one possible piece that could have made the move
      }
     }//end if potential and ending cmp
    }//end for potential
   }//end for plausible
   
  }//if pawnmove check
  //Movelists generated
 }
 /**
  returns all moves possible for a king
  @param int letter 
  @param num represents the board states 0-7x0-7 array position; 
  @param boolean isBlack denotes whether the consdiered piece is black or white
  @return list of all valid king moves as given by the current state of pieces[][]
 */
 private List <String> getKingMove(int num, int letter, boolean isBlack)
 {
  List <String> possible = new ArrayList <String>();
  checkPieceValidity(num, letter+1, isBlack, possible);
  checkPieceValidity(num+1, letter, isBlack, possible);
  checkPieceValidity(num+1, letter+1, isBlack, possible);
  checkPieceValidity(num, letter-1, isBlack, possible); 
  checkPieceValidity(num-1, letter, isBlack, possible);
  checkPieceValidity(num-1, letter-1, isBlack, possible);
  checkPieceValidity(num-1, letter+1, isBlack, possible);
  checkPieceValidity(num+1, letter-1, isBlack, possible);
  return possible;
 }
 /**
  Returns all moves possible for the queen
  based off of rook and queen fx's
  @param int letter 
  @param num represent the board states 0-7x0-7 array position; 
  @param boolean isBlack denotes whether the consdiered piece is black or white
  @return list of all valid queen moves as given by the current state of pieces[][]
 */
 private List <String> getQueenMove(int num, int letter, boolean isBlack)
 {
  List <String> possible = new ArrayList <String>();
  List <String> pp= getBRMove(num, letter, 0,isBlack);//bishop
  for (int i =0; i < pp.size(); i ++){possible.add(pp.get(i));}
  List <String> pp2 = getBRMove(num, letter,1, isBlack);//rook
  for (int i =0;i < pp2.size(); i ++){possible.add(pp2.get(i));}
  return possible;
 }
 /**
  sub-task for rook move retrieval
  @param int letter 
  @param num represent the board states 0-7x0-7 array position; 
  @param boolean isBlack denotes whether the consdiered piece is black or white
  @param i indicative of magnitude for movement to scope all Von Neumann neighbor directions
  @param j indicates the direction of rook movement, in either for Von Neumann neighbor directions
  @return list of all valid rook moves as given by the current state of pieces[][]
 */
 private List <String> getTmpR(int num, int letter, boolean isBlack, int i, int j)
 { 
  boolean iUp =false;if (i > 0){iUp = true;}
  //boolean jUp = false;if(j > 0){jUp = true;} 
  List <String> possible = new ArrayList <String>();
  boolean obstruction = false;
  int tmpN = num; int tmpL = letter;
  int numSeen = 0;
  while (obstruction == false)
  { 
    if(j ==1){tmpN += i; tmpL = letter;} 
    if(j==-1){tmpN = num; tmpL += i;}
    try{String potential = board[tmpN][tmpL]; //System.out.printf("%s %d %d %d %d", potential, letter, num, tmpN, tmpL);
   checkPieceValidity(tmpN, tmpL, isBlack, possible);
     if (isBlack == true) { if ((pieces[tmpN][tmpL] > 6)) {obstruction = true; } 
           if ((pieces[tmpN][tmpL] != 0) && (pieces[tmpN][tmpL] < 7)){numSeen ++; }//System.out.println(String.format(" num seen found %s",board[tmpN][tmpL]));} 
           if(numSeen >0) {obstruction = true;}
     }
     else if (isBlack == false) { if ((pieces[tmpN][tmpL] < 7) && (pieces[tmpN][tmpL] != 0)) {obstruction = true; } 
            if ((pieces[tmpN][tmpL] > 6)) {numSeen ++;}
            if(numSeen >0) {obstruction = true;}
           }
    }catch(IndexOutOfBoundsException ex){System.err.println("Board out of bound exception");obstruction = true;}
  }
  return possible;
 } 
 /**
  return all possible moves from a bishop or rook
  @param int letter 
  @param num represent the board states 0-7x0-7 array position;
  @param int k denotes which piece is obtained. 0 is bishop, 1 is rook
  @return List <String> of all rook/bishop moves as indicated by int k
 */
 private List <String> getBRMove(int num, int letter, int k, boolean isBlack)
 {
  List <String> possible = new ArrayList <String>();
  //boolean isBlack = false;if ((int)pieces[num][letter] > 6){ isBlack = true;} //DOES NOT ACCOUNT FOR ZERO
  int [] vals = {1,1,1,-1,-1, 1, -1,-1}; int [] vv= {1, 1, 1, -1, -1, 1, -1,-1};//poorly planned
  for (int i =0;i < 8; i+=2)
  {
   List <String> tmp = new ArrayList<String>();
   if (k==0){tmp = getTmpB(num, letter, isBlack, vals[i], vals[i+1]);}
   if (k==1){tmp = getTmpR(num, letter, isBlack, vv[i], vv[i+1]);}
   for (int j = 0; j < tmp.size(); j ++)
   {
    possible.add(tmp.get(j));
   }
  }
  return possible;
 }
 /**
  @param int letter 
  @param num represent the board states 0-7x0-7 array position; 
  @param boolean isBlack denotes whether the consdiered piece is black or white
  @int i indicative of magnitude for movement to scope all four diagonal directions
  @int j indicates the direction of bishop movement, in surrounding four diagonal directions
  @return list of all valid bishop moves as given by the current state of pieces[][]
 */
 private List <String> getTmpB(int num, int letter,boolean isBlack, int i, int j)
 { 
  boolean iUp =false;if (i > 0){iUp = true;}
  boolean jUp = false;if(j > 0){jUp = true;}
  i = 0; j = 0; 
  int numSeen = 0; //cannot get more than one white piece, which serves as an obstruction
  List <String> possible = new ArrayList <String>();
  boolean obstruction = false;
  int tmpN = num; int tmpL = letter;
  while (obstruction == false)
  { 
   if (iUp == false){i-=1;}else if(iUp == true){i+=1;}
   if (jUp == false){j-=1;}else if(jUp == true){j+=1;}
    tmpN = num +i; tmpL = letter+j;
    try{String potential = board[tmpN][tmpL];
     if (isBlack == true) {   
           if ((pieces[tmpN][tmpL] > 6)) {obstruction = true; }
             if ((pieces[tmpN][tmpL] != 0) && (pieces[tmpN][tmpL] < 7)){numSeen ++; }// System.out.println(String.format(" num seen found %s",board[tmpN][tmpL]));} 
             if (numSeen > 0){obstruction = true; } 
           }
     else if (isBlack == false) { 
            if ((pieces[tmpN][tmpL] < 7) && (pieces[tmpN][tmpL] != 0)) {obstruction = true; } 
             if ((pieces[tmpN][tmpL] > 6)) {numSeen ++;}
             if (numSeen >0){obstruction = true; }
           }
          checkPieceValidity(tmpN, tmpL, isBlack, possible);
    }catch(IndexOutOfBoundsException ex){System.err.println("Board out of bound exception");obstruction = true;}
  }
  return possible;
 }
 /**
  return ALL possible moves from a given knight position
  @param int letter 
  @param int num represent the board states 0-7x0-7 array position;
  @param boolean isBlack represents whether the considered piece is black or white
  @return list of all valid knight moves as given by the current state of pieces[][]
 */
 private List <String> getKnightMove(int num, int letter, boolean isBlack)
 {
  List <String> possible = new ArrayList <String>();
  /**
  Assuming free space, get all 8 position movements of a knight
  */
  checkPieceValidity(num+2, letter-1, isBlack, possible);
  checkPieceValidity(num+1, letter-2, isBlack, possible);
  checkPieceValidity(num-2, letter-1, isBlack, possible);
  checkPieceValidity(num-1, letter-2, isBlack, possible); 
  checkPieceValidity(num+2, letter+1, isBlack, possible);
  checkPieceValidity(num-2, letter+1, isBlack, possible);
  checkPieceValidity(num+1, letter+2, isBlack, possible);
  checkPieceValidity(num-1, letter+2, isBlack, possible);
  return possible;
 }
 /**
  adds possible moves onto possible moves, if out of bounds, exception caught, checks for friendlyfire
  @param num 
  @param letter represents the considered move-to position, adds values onto possible if possible
  @param isBlack represents whether the considered piece is black or white
  @param possible to add onto if friendly fire conditions check out
 */
 public void checkPieceValidity(int num, int letter, boolean isBlack, List <String> possible)
 {
  //boolean isBlack = false;if ((int)pieces[num][letter] > 6){ isBlack = true;} //DOES NOT ACCOUNT FOR ZERO
  try{
   if((isBlack == false))
   { 
    if ((pieces[num][letter] == 0) || (pieces[num][letter] > 6))
    {  
     possible.add(board[num][letter]);
    } //if
   }//if isBlack statement
   else{//if the piece is black
     if ((pieces[num][letter] < 7))
     {
      possible.add(board[num][letter]);     
     }
   }//else
  }//try
  catch(IndexOutOfBoundsException ex){System.err.println("Index out of bounds exception::Check Piece Validity");}
 }
 /**
  same as checkPieceValidity, but specific for pawndiagonal attacks
  @param num 
  @param letter represents the considered move-to position, adds values onto possible if possible
  @param isBlack represents whether the considered piece is black or white
  @param possible, to add onto if friendly fire conditions check out
 */
 public void checkPawnFriendlyFire(int num, int letter, boolean isBlack, List <String> possible)
 {
 	//System.out.println(String.format("the check pawn friendly fire num is %d, letter is %d", num , letter));
  if (isBlack == true){
   try{
    if( (pieces[num][letter] != 0) && (pieces[num][letter] < 7) ){possible.add(board[num][letter]);}
   }catch(IndexOutOfBoundsException ex){System.err.println("Board out of bound exception::checkFriendlyFire");}
  }
  else if (isBlack == false)
  {
   try{
    if( (pieces[num][letter] != 0) && (pieces[num][letter] > 6) ){possible.add(board[num][letter]);}
   }catch(IndexOutOfBoundsException ex){System.err.println("Board out of bound exception::checkFriendlyFire");}
  }
 }
 /**
  returns ALL possible moves of a selected piece in String array
  @param int letter 
  @num represent the board states 0-7x0-7 array position;
  @boolean kill represents whether a pawn killed or not
  @return List <String> of all valid pawn moves
 */
 private List <String> getPawnMove(int num, int letter, boolean isBlack)//inp is the starting position of piece
 {//letter corresponds to 0-7 abcdefgh, of course num is 0-7; 1-8
  //System.out.println(String.format("here is getpawnmove for %s; num is %d, letter is %d", board[num][letter], num, letter));
  List <String> possible = new ArrayList <String> ();
  //forward movement
  int tmpN = num+2;
  int tempN = num + 1; if(!isBlack) {tempN = num-1; tmpN= num-2;}
  if ((isBlack == true) && (pieces[tempN][letter] == 0))  checkPieceValidity(tempN, letter, isBlack, possible);
  else if ((isBlack == false) && (pieces[num-1][letter] == 0)) checkPieceValidity(tempN, letter, isBlack, possible);

  //if it is a starting position of pawn
  try{
   if (((num == 1) || (num==6)) && (pieces[tmpN][letter] == 0))//1 actually corresponds to 7th row, and 6 actually corresponds to 2nd row
   { 
   if ((isBlack ==true) && (pieces[tmpN-1][letter] == 0)){checkPieceValidity(tmpN, letter, isBlack , possible); }
   if ((isBlack ==false) && (pieces[tmpN+1][letter] == 0)){checkPieceValidity(tmpN, letter, isBlack , possible); }
   } 
  }catch(ArrayIndexOutOfBoundsException ex){System.err.println("arrayIndexoutofbounds exception. See getPawnMove");}
  //corresponds to 0-A,7-G or boardedge, diagonal attack available
  checkPawnFriendlyFire(tempN, letter+1, isBlack, possible);
  checkPawnFriendlyFire(tempN, letter-1, isBlack, possible);
  //En Passant checks
 	//a2 	//if a2 moved to a4, and the black is in b4...
 	if ((returnEN[0]==1) && num == 4 && letter == 1 && pieces[6][0] == 0 && pieces[4][1] == 7 && pieces[5][0] == 0 && pieces[4][0]==1) possible.add("EP"+board[5][0]);
 	//b2
 	if (returnEN[1] == 1 && num == 4 && letter == 0 && pieces[6][1] == 0 && pieces[4][1] == 1 && pieces[5][1] == 0 && pieces[4][0]==7) {possible.add("EP"+board[5][1]); }//System.out.println(String.format("ep, returnen[1] is %d", returnEN[1]));}
  	if (returnEN[1] == 1 && num == 4 && letter == 2 && pieces[6][1] == 0 && pieces[4][1] == 1 && pieces[5][1] == 0 && pieces[4][2]==7) {possible.add("EP"+board[5][1]); }//System.out.println(String.format("ep, returnen[1] is %d", returnEN[1]));}
 	//c2
  	if ((returnEN[2] == 1) && num== 4 && letter == 1 && pieces[6][2] == 0 && pieces[4][2] == 1 && pieces[5][2] == 0 && pieces[4][1]==7) possible.add("EP"+board[5][2]);
  	if ((returnEN[2] == 1) && num== 4 && letter == 3 && pieces[6][2] == 0 && pieces[4][2] == 1 && pieces[5][2] == 0 && pieces[4][3]==7) possible.add("EP"+board[5][2]);
   	//d2
  	if ((returnEN[3] == 1) && num== 4 && letter == 2 && pieces[6][3] == 0 && pieces[4][3] == 1 && pieces[5][3] == 0 && pieces[4][2]==7) possible.add("EP"+board[5][3]);
  	if ((returnEN[3] == 1) && num== 4 && letter == 4 && pieces[6][3] == 0 && pieces[4][3] == 1 && pieces[5][3] == 0 && pieces[4][4]==7) possible.add("EP"+board[5][3]);
   	//e2
  	if ((returnEN[4] == 1) && num== 4 && letter == 3 && pieces[6][4] == 0 && pieces[4][4] == 1 && pieces[5][4] == 0 && pieces[4][3]==7) possible.add("EP"+board[5][4]);
  	if ((returnEN[4] == 1) && num== 4 && letter == 5 && pieces[6][4] == 0 && pieces[4][4] == 1 && pieces[5][4] == 0 && pieces[4][5]==7) possible.add("EP"+board[5][4]);
   	//f2
  	if ((returnEN[5] == 1) && num== 4 && letter == 4 && pieces[6][5] == 5 && pieces[4][5] == 1 && pieces[5][5] == 0 && pieces[4][4]==7) possible.add("EP"+board[5][5]);
  	if ((returnEN[5] == 1) && num== 4 && letter == 6 && pieces[6][5] == 0 && pieces[4][5] == 1 && pieces[5][5] == 0 && pieces[4][6]==7) possible.add("EP"+board[5][5]);
   	//g2
  	if ((returnEN[6] == 1) && num== 4 && letter == 5 && pieces[6][6] == 0 && pieces[4][6] == 1 && pieces[5][6] == 0 && pieces[4][5]==7) possible.add("EP"+board[5][6]);
  	if ((returnEN[6] == 1) && num== 4 && letter == 7 && pieces[6][6] == 0 && pieces[4][6] == 1 && pieces[5][6] == 0 && pieces[4][7]==7) possible.add("EP"+board[5][6]);
  	//h2
 	if ((returnEN[7] == 1) && num == 4 && letter == 6 && pieces[6][7] == 0 && pieces[4][7] == 1 && pieces[5][7] == 0 && pieces[4][6]==7) possible.add("EP"+board[5][7]);
 	//other side
 	//a6
 	if ((returnEN[8]==1) && num == 3 && letter == 1) possible.add("EP"+board[2][0]);
 	//b6
 	if ((returnEN[9] == 1) && num== 3 && letter == 0){ possible.add("EP"+board[2][1]);}//System.out.println(String.format("ep, returnen[9] is %d", returnEN[9]));}
  	if ((returnEN[9] == 1) && num== 3 && letter == 2){ possible.add("EP"+board[2][1]);}//System.out.println(String.format("ep, returnen[9] is %d", returnEN[9]));}
 	//c6
  	if ((returnEN[10] == 1) && num== 3 && letter == 1) possible.add("EP"+board[2][2]);
  	if ((returnEN[10] == 1) && num== 3 && letter == 3) possible.add("EP"+board[2][2]);
   	//d6
  	if ((returnEN[11] == 1) && num== 3 && letter == 2) possible.add("EP"+board[2][3]);
  	if ((returnEN[11] == 1) && num== 3 && letter == 4) possible.add("EP"+board[2][3]);
   	//e6
  	if ((returnEN[12] == 1) && num== 3 && letter == 3) possible.add("EP"+board[2][4]);
  	if ((returnEN[12] == 1) && num== 3 && letter == 5) possible.add("EP"+board[2][4]);
   	//f6
  	if ((returnEN[13] == 1) && num== 3 && letter == 4) possible.add("EP"+board[2][5]);
  	if ((returnEN[13] == 1) && num== 3 && letter == 6) possible.add("EP"+board[2][5]);
   	//g6
  	if ((returnEN[14] == 1) && num== 3 && letter == 5) possible.add("EP"+board[2][6]);
  	if ((returnEN[14] == 1) && num== 3 && letter == 7) possible.add("EP"+board[2][6]);
  	//h6
 	if ((returnEN[15]==1) && num == 3 && letter == 6) possible.add("EP"+board[2][7]);
  return possible;
 }
 /**
  print current board state
  the domain is an integer, if 1 prints the pieces layout, if 2 prints the Algebraicnotation, 3 prints the
  @param val int::1 prints the pieces layout, 2 prints the Algebraicnotation, 3 prints the unicode chess board
 */
 public void printBoard(int val)//optional to havem orei ntuitivae input  
 {
  if (val == 1)
  {
   for (int i = 0; i < 8; i++)
   {
    System.out.println("");
    for (int j = 0;j < 8; j++)
    {
     System.out.printf("%d ", pieces[i][j]);
    }
   }
  }
  else if(val==2)
  {
   for (int i = 0; i < 8; i++)
   {
   System.out.println("");
    for (int j = 0;j < 8; j++)
    {
     System.out.printf("%s ", board[i][j]);
    }
   }
  }
  else if (val==3)
  {
   int tmpCount = 7;
   for (int i = 0; i < 8; i++)
   {
    System.out.println();
    System.out.printf("%s", nums[tmpCount]); tmpCount --; 
    for (int j = 0;j < 8; j++)
    {
     if(pieces[i][j]==0)
     {System.out.printf("  ");}
     if(pieces[i][j]==1)
     {System.out.printf("\u2659 ");}
     if(pieces[i][j]==2)
     {System.out.printf("\u2658 ");}
     if(pieces[i][j]==3)
     {System.out.printf("\u2657 ");}
     if(pieces[i][j]==4)
     {System.out.printf("\u2656 ");}
     if(pieces[i][j]==5)
     {System.out.printf("\u2655 ");}
     if(pieces[i][j]==6)
     {System.out.printf("\u2654 ");}
     if(pieces[i][j]==7)
     {System.out.printf("\u265F ");}
     if(pieces[i][j]==8)
     {System.out.printf("\u265E ");}
     if(pieces[i][j]==9)
     {System.out.printf("\u265D ");}
     if(pieces[i][j]==10)
     {System.out.printf("\u265C ");}
     if(pieces[i][j]==11)
     {System.out.printf("\u265B ");}
     if(pieces[i][j]==12)
     {System.out.printf("\u265A ");}
    }
   }
   System.out.println();System.out.printf(" ");
   for (int i = 0 ; i< 8; i ++)
   {
    System.out.printf("%s ", letters[i]);
   }
  }

  //add more indicators here
  else{ System.out.println("INVALID NUM"); }
 } 
}