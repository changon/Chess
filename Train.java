import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.jblas.FloatMatrix;
//read in current params
//read in a game, update theta parameters based on game moves
//return params to out file. Training session end

/***********************************************
NOTE:
	This training of the NN is based off of the fact that (ideally pro players) or games as given by the expansive training set makes the optimal moves!
TODO:
easy file selection, contol file (?) rather than hardcoded in

TRAINING Policy:
Let's say that for a given board position B, 
if its child positions are c_1, c_2, c_3, ..., and c_1 is the move that the 'pro' played, 
you want f(c_1) >= f(c_2), f(c_1) >= f(c_3), ..., where f() is your evaluation function, or our NN. 
Now let's let M = argmax f(c_i) [for i >= 2].
Let the hinge loss function be defined by
max(\gamma - (M - f(c_1)), 0). 

f(c_1) = y; 

Intuitively we are saying that we want the move the 'pro' played to be scored higher 
than all other moves by at least a margin of \gamma.

If M + \gamma > f(c_1), then we got this position "wrong" and need the NN 
weights must be adjusted. 
If f(c_1) > M + \gamma, then the position is 'right', and the 
weights do not need to be updated, as indicated by the loss being 0.
 
Evalutate game tree minimax, alpha/beta 


************************************************/
/************************
Order of programs.
readin()::reads in the pgn data, taking only the game move sets
getMoves()::makes an array of the movesets [black,white] (in state variable moves) 
			UPDATE getMoves() with each iteration

************************/
public class Train extends Application
{
	public static List <String> trainingSet = new ArrayList <String>();
	public static List <String> moves = new ArrayList <String>();
	public static List <Integer> allIter = new ArrayList <Integer>();
	public static List <Float> allJ = new ArrayList <Float>();
	public static File inp = new File ("dataTrunc.pgn");
	public float J; public static int iters;
	
	public static void main (String [] args)
	{
		launch(args);
	}
	//@Override
	public void start(Stage stage)
	{
		Evaluation eval = new Evaluation();
		train(1);
  		/*try{
				PrintWriter writer = new PrintWriter("scaling.out");
				writer.printf("here is the allJ");
				for (int g =0; g < allJ.size(); g ++)//500
				{
					writer.printf(" %f:\n", allJ.get(g));
					//for (int k =0; k < allH.get(g).size(); k++)//30
					//{	writer.printf(" %f ", allH.get(g).get(k));}
				}
				writer.close();
  			} catch(FileNotFoundException ex){System.err.println("File not found");}*/
		stage.setTitle("J vs. iters");
		final NumberAxis xaxis = new NumberAxis();
		final NumberAxis yaxis = new NumberAxis();
		xaxis.setLabel("iters");
		final LineChart <Number, Number> linechart= new LineChart<Number,Number>(xaxis, yaxis);
		linechart.setTitle("Cost Function J over training iterations");
		XYChart.Series series = new XYChart.Series();
		series.setName("J");
		for (int i = 0; i < allIter.size(); i ++)
		{	
			System.out.println(allJ.get(i));
			series.getData().add(new XYChart.Data(allIter.get(i), allJ.get(i)));
		}
		Scene scene = new Scene(linechart, 400, 600);
		linechart.getData().add(series);	
		stage.setScene(scene);
		stage.show();
	}		
	public static void train(float scale)
	{
  		//redirect System err to a text file
  		File file = new File("TrainError.txt");
  		try{
   			FileOutputStream fos = new FileOutputStream(file);
   			PrintStream ps = new PrintStream (fos);
  			System.setErr(ps);
  		} catch(FileNotFoundException ex){System.err.println("File not found");}
		
		readin();//read in all the training data
		Board board = new Board();
		Evaluation evaluation = new Evaluation(scale);
		//board.printBoard(3);
		for (int i = 0; i < 1//trainingSet.size()
			; i ++)//essentially each iteration represents a training episode
		{
			getMoves( trainingSet.get(i) );//get the movesets array for a training game
			int who =1;
			boolean black = false;
			evaluation.setX(board.pieces, board.returnEN, board.returnCastle);
			for (int j =0; j < moves.size()-50; j ++)
			{
				if (j%2 ==1) {who = 2; black = true;}
				if (j%2 ==0) {who = 1; black = false;}
					String cur = moves.get(j);
					System.out.println("Board rn at X set");

					board.updateBoard(moves.get(j), who);//updates the board state to pro moves
					evaluation.setStrive(board.pieces, board.returnEN, board.returnCastle);//set the desired state which is what the pro did
					//System.out.println(String.format("move is %s",cur));
					if(j%2==0){//update weights under the conditions specified in updateWeights() along with if the player is white
						Board maxBoard = board;
						System.out.println("before max move made");
						maxBoard.printBoard(3);
						maxBoard.getMaxMove(black);//update the board based on the highest value soln
						System.out.println("after max move made");
						maxBoard.printBoard(3);
						evaluation.updateWeights(evaluation.setBoardRepViaFloat(maxBoard.pieces, maxBoard.returnEN, maxBoard.returnCastle) , evaluation.setBoardRepViaFloat(board.pieces,board.returnEN, board.returnCastle));
					}
					System.out.println("Board rn after updateWeights sess, at strive set");
					board.printBoard(3);
					evaluation.setJ();//set Err
					allJ.add(evaluation.J);//to monitor Err fx
					System.out.println(evaluation.J);
					iters ++;
					allIter.add(iters);
					
					evaluation.setX(board.pieces, board.returnEN, board.returnCastle);//set to cur state now, what we want to do is the pro move update, which will be updated during the next iter
					//update board, network
			}//move on to next move
		}
		//outfile the updated params now to use in an eval fx
		evaluation.fin();
	}
	/**
	Get a string of moves to iterate through and update the nn with according to the training policy
	*/
	public static void getMoves(String moveset)//get all the moves made from pgn format!
	{
		moves.clear();//clear prior game moves considered
		String [] tmpMoves = moveset.split("\\s+");//split the str on whitespace
													//results in [move1, move2, .]
		int Nmoves = 0;
		//now we add on all the moves from the moveset
		for (String aMove: tmpMoves)
		{
			if (aMove.contains(".")){Nmoves ++;}//if it has a . that indicates the turn moves, giving us how many exchanges were made
		}
		int cnt =0;
		for (int i =1; i < 3*(Nmoves) + 1; i ++)//guranteed to have size of at least 3Nmoves(-1[?])
		{
			//adds a move if it does not have the following based on pgn formatting
			if ( (!(tmpMoves[i].contains("."))) && (!(tmpMoves[i].contains("{")))  /*&& (!(tmpMoves[i].contains("-")))*/ || (tmpMoves[i].contains("O")) )
			{//cnt++;			System.out.println(String.format("move #%d is %s", cnt,tmpMoves[i]));
				moves.add(tmpMoves[i]);
			}
		}
	}
	/**
	Reads in .pgn format games
	*/
	public static void readin()
	{
	//http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
		try (BufferedReader br = new BufferedReader(new FileReader( inp )))
		{
			String tmp;
			int i = 0; // i cannot equal 0 else this mod will be equivalent to zero
			int games = 0;
			while ( (tmp = br.readLine()) != null) 
			{
				i++;
				int dd = ((i - (20*games)) );//% 19);
				//19,39,59...
				if 	( ( ((i - (20*games)) % 19) == 0) && (dd != 0) )//to read pgn format for line with all the info. 
				{
					games ++;
					trainingSet.add(tmp);
				}				
			}
		}
		catch(FileNotFoundException ex){System.err.printf("Error File %s", inp); }
		catch(IOException e){System.err.printf("IO error %s", inp);}
	}
}