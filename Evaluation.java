import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.jblas.FloatMatrix;
import org.jblas.MatrixFunctions;

//NOTE: Bugchecking watch out for rmBias() fx rm the correct bias
public class Evaluation
{
	//cur options for optimization: simulated annealing, bfgs, gradient descent, genetic, simplex [linear programming]
	//total of 832 + 12 = 844 
	public int iters;
	public FloatMatrix X = new FloatMatrix((64*13) + 8 + 4);//our input, 64 spots on the board, 13 potential states on each spot, along with 8 potential en passant movements
	public FloatMatrix des = new FloatMatrix(844); //respresents the desired state
	public float J; 
	public float lambda = (float)1;
	public FloatMatrix allTheta= new FloatMatrix(1);
	private FloatMatrix trivialTheta;//for linear evaluation function
	private FloatMatrix theta1 = new FloatMatrix(3000, 845);
	private FloatMatrix theta2 = new FloatMatrix(3000, 3001);
	private FloatMatrix theta3 = new FloatMatrix(1, 3001);//844
	private FloatMatrix theta1Grad = new FloatMatrix(3000, 845);
	private FloatMatrix theta2Grad = new FloatMatrix(3000, 3001);
	private FloatMatrix theta3Grad = new FloatMatrix(1, 3001);//844
	private FloatMatrix z1, z2, z3, delta3, delta2, delta1, a1, a2;
	private float gamma = (float)1;
	//private FloatMatrix a1b, a2b, z1b, z2b, delta3b, delta2b;
	//being pedantic, a and z definitions should be switched
	//constructor to initialize weights on our network
	public Evaluation()
	{
		getWeights(1);
		a1= new FloatMatrix(1, 3000);
		z1= new FloatMatrix(1, 3000);
		a2 = new FloatMatrix(1, 3000);
		z2 = new FloatMatrix(1, 3000);
		FloatMatrix h, z3 = new FloatMatrix(1);//h is a3
		delta1 = new FloatMatrix(3000);
		delta2 = new FloatMatrix(3000, 845);
		delta3 = new FloatMatrix(1);
	}
	public Evaluation(float scale)
	{
		getWeights(scale);
		a1= new FloatMatrix(1, 3000);
		z1= new FloatMatrix(1, 3000);
		a2 = new FloatMatrix(1, 3000);
		z2 = new FloatMatrix(1, 3000);
		FloatMatrix h, z3 = new FloatMatrix(1);//h is a3
		delta1 = new FloatMatrix(3000);
		delta2 = new FloatMatrix(3000, 845);
		delta3 = new FloatMatrix(1);
	}
	/**
	Get random weights in a matrix of dimensions (outsize, inpsize+1) or outsize rows and inpsize+1 columns; the weights are normalized
	*/
	public FloatMatrix randInitWeights(int inpsize, int outsize, float scale)
	{
		scale = (float)Math.sqrt(3)/(float)Math.sqrt(inpsize+outsize);//normalized weights
		System.out.println((scale * Math.random() * ((float)1- (float)0) + (float) 0));
		FloatMatrix ret = new FloatMatrix(outsize, inpsize+1);
		for (int i =0; i < outsize; i ++)
			for (int j =0; j < inpsize+1; j++)
				ret.put(i, j,(float)(scale * Math.random() * ((float)1- (float)0) + (float) 0));//if this is max - min; 1 - 0, then FloatMatrix.rand(m,n) is the same
		return ret;
	}
	/**
	OBTAIN params from prior training session if present
	*/
	public void getWeights(float scale)
	{
		File inpparams = new File("parameters.in");
		if (inpparams.exists())//continue training
		{
			try (BufferedReader br = new BufferedReader(new FileReader( new File("parameters.in" )) ) )
			{
				//String it = br.readLine();	
				//System.out.printf("first str is %s", it);
				iters = Integer.parseInt(br.readLine());
				StringTokenizer split = new StringTokenizer(br.readLine());
				for (int i =0; i < 3000; i++)
					for (int j =0; j < 845; j ++)
						theta1.put(i, j, Float.parseFloat(split.nextToken()));
				split = new StringTokenizer(br.readLine());
				for (int i =0; i < 3001; i++)
					for (int j=0; j < 3000; j ++)
						theta2.put(i, j, Float.parseFloat(split.nextToken()));//err here
				split = new StringTokenizer(br.readLine());
				for (int i =0; i < 3000; i++)//3001??
					theta3.put(0, i, Float.parseFloat(split.nextToken()));
			}
			catch(FileNotFoundException ex){System.err.printf("Error File %s", "parameters.in"); }
			catch(IOException e){System.err.printf("IO error %s", "parameters.in"); }
		}
		else //generate parameters for first training session, .rand(), or .ones()
		{
			iters = 0;
			theta1 = randInitWeights(844, 3000, scale);//inp maps onto hidden
			theta2 = randInitWeights(3000, 3000, scale);//hidden onto other hidden
			theta3 = randInitWeights(3000, 1, scale);//hidden to out
			
			//call past data in network
		}
	}
	/**
	a trivial heuristic
	@param int[][]pieces two d array of current board state
	@param boolean black, if current player is black or not
	*/
	public int hypothesis(int [][] pieces, boolean black)
	{
		//here the piece values are valued over centrality
		//the core evaluation function here is based on the immediate piece values, and centrality; this will be a linear fx
		int total = 0;
		int centrality = 0;
		
		int king = 100;
		int queen = 18;
		int rook= 10;
		int knight = 6;
		int bishop = 6;
		int pawn = 2;
		int [] nums = {0,0,0,0,0,0,0,0,0,0,0,0,0};
		int wk = 0;
		int bk =0;
		int wq = 0;
		int bq = 0;
		int wr = 0;
		int br =0;
		int wn = 0;
		int bn = 0;
		int wb = 0;
		int bb = 0;
		int wp = 0;
		int bp = 0;

		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j ++)
			{
				if (pieces[i][j] == 12){bk ++;}
				if (pieces[i][j] == 11){bq ++;}
				if (pieces[i][j] == 10){br ++;}
				if (pieces[i][j] == 9){bb ++;}
				if (pieces[i][j] == 8){bn ++;}
				if (pieces[i][j] == 7){bp ++;}
				
				if (pieces[i][j] == 6){wk ++;}
				if (pieces[i][j] == 5){wq ++;}
				if (pieces[i][j] == 4){wr ++;}
				if (pieces[i][j] == 3){wb ++;}
				if (pieces[i][j] == 2){wn ++;}
				if (pieces[i][j] == 1){wp ++;}
				//centrality += getCentralityReward(i, j,pieces[i][j],black);
			}
		}
			total += (
				king*(bk-wk)
				+ queen *(bq-wq)
				+ rook *(br-wr)
				+ bishop *(bb-wr)
				+ knight *(bn-wn)
				+ pawn *(bp-wp)
			);
		return total;
	}
	/**
	Gets the centrality reward based on the given info, subject to many changes to accurately reflect a competent engine
	@param num number of board position (aligning with algebraic notation)
	@param letter number of board position (aligning with algebraic notation)
	@param piece the current piece at the spot
	@param black if the player is black or not, not est necesse as of now
	@return the centrality reward ('-' or '+') for the given piece at the given position
	*/
	public int getCentralityReward(int num, int letter, int piece, boolean black)
	{
		int totalw = 0;
		int totalb = 0;
		if (piece ==0)
		{ if(central(num, letter)){if (black == false){totalb += 2; totalw += -2; } }
			else{if (black == true){totalb += -2; totalw += 2; }  }
		}
		//if (piece ==1){} //pawns dev, discount factro with promotion? 
		if (piece == 2){ if(central(num, letter)){totalw += 3;} }
		if (piece == 3){ if(central(num, letter)){totalw += 3;} }
		if (piece == 4){ if(central(num, letter)){totalw += 5;} }
		if (piece == 5){ if(central(num, letter)){totalw += 9;} }//promote cnetrality of queen?
		//if (piece == 7){} //pawns dev, discount factro with promotion? 
		if (piece == 8){ if(central(num, letter)){totalb += 3;} }
		if (piece == 9){ if(central(num, letter)){totalb += 3;} }
		if (piece == 10){ if(central(num, letter)){totalb += 5;} }
		if (piece == 11){ if(central(num, letter)){totalb += 9;} }//promote cnetrality of queen?
		if (black == true){return (totalb- totalw);}
		else if (black == false){return (totalw - totalb);}
		return 0;
	}
	/**
	Loosely determines if a piece is central or not, subject to many changes
	@param num number of board position (aligning with algebraic notation)
	@param letter number of board position (aligning with algebraic notation)
	*/
	boolean central(int num, int letter)
	{
		if ( ((num >6) || (num < 3)) || ((letter < 2) || (letter > 5)))
		//evaluation here based from rectangle enclosed by the four corners: b6, g6, g3, b3 (double check)
		{return false;}
		else{return true;}
	}
	/**
	assigns the pieces[][] to an input vector; modifies X float
	*/
	public void setX(int [][] pieces, int[]en, int[]castle)
	{
		int cc = 0;
		for (int i = 0 ; i < 8; i ++)
		{
			for (int j = 0; j < 8; j ++)
			{
				for (int k = 0; k < 13; k ++)
				{
					if (pieces[i][j] == k)
					{
						X.put(cc, (float)1);
					}
					else
					{
						X.put(cc, (float)0);
					}
					cc ++;
				}
			}
		}
		//arr = passant()
		for (int i =0; i < 8; i ++){X.put(cc, (float)en[i]); cc++;}
		for (int i=0; i<4; i++){X.put(cc, (float)castle[i]); cc++;}
		//System.out.println("the first position");
		//for (int i=0; i<15; i++) System.out.printf(" %f ",X.get(i));
	}
	/**
	Assigns pieces[][] to the desired state of the board; modifies des float
	*/
	public void setStrive(int [][] pieces, int[]en, int[]castle)
	{
		int cc = 0;
		for (int i = 0 ; i < 8; i ++)
		{
			for (int j = 0; j < 8; j ++)
			{
				for (int k = 0; k < 13; k ++)
				{
					if (pieces[i][j] == k)
					{
						des.put(cc, (float)1);
					}
					else
					{
						des.put(cc, (float)0);
					}
					cc ++;
				}
			}
		}
		//arr = passant()
		for (int i =0; i < 8; i ++){des.put(cc, (float)en[i]); cc++;}
		for (int i=0; i<4; i++){des.put(cc, (float)castle[i]); cc++;}
		//for (int i=831; i< 844; i++) System.out.printf(" %f ",X.get(i));
	}
	/**
	Sets an arbitrary state to a floatmatrix representation for the neural net input
	*/
	public FloatMatrix setBoardRepViaFloat(int [][] pieces, int []en, int[]castle)
	{
		FloatMatrix ret = new FloatMatrix(844);
		int cc = 0;
		for (int i = 0 ; i < 8; i ++)
		{
			for (int j = 0; j < 8; j ++)
			{
				for (int k = 0; k < 13; k ++)
				{
					if (pieces[i][j] == k)
					{
						ret.put(cc, (float)1);
					}
					else
					{
						ret.put(cc, (float)0);
					}
					cc ++;
				}
			}
		}
		//arr = passant()
		for (int i =0; i < 8; i ++){ret.put(cc, (float)en[i]); cc++;}
		for (int i=0; i<4; i++){ret.put(cc, (float)castle[i]); cc++;}
		return ret;
	}
	/* **********************************************
	Here starts the NN or evaluation function methods
	************************************************

	The current Activation fx. Given by the sigmoid:
		 1
	------------
	(1 + e ^(-z))
	
	UPDATE: tanh(), bend id fx
	
	The net will have two hidden layers.
	A 64x13 + 8 + 4 input vector.
	The input vector consists of (64 x 13) + 8 + (centrality):
		8 is the en passant
		4 is for castling moves
		superficial piece count 
		13 for each piece 

   844    3000x1'    3000x1'   844
	x ----> a_1 ----> a_2 ----> 
	
	x ----> a_1 ----> a_2 ---->  output
	
	x ----> a_1 ----> a_2 ---->
	
	Computes the activation function with a given matrix input; here the activation 
	function is the the parametric rectified linear unit
	@param inp Floatmatrix X
	@return activation of the input FloatMatrix X
	*/	
	public FloatMatrix activation(FloatMatrix inp)
	{
		FloatMatrix top = MatrixFunctions.sqrt(MatrixFunctions.pow(inp, (float) 2).add((float)1)).sub((float)1).div((float)2);
		return top.add(inp);
		//bent id
		/*
		FloatMatrix denom = inp;
		FloatMatrix one = FloatMatrix.ones(inp.rows, inp.columns);
		one.muli(-1);//muli matrix mult.; mul element wise multiply
		MatrixFunctions.expi(denom); //take the exponent
		result.addi(1); //add 1, denominator calculated
		return one.divi(inp);//now divide one by denom 
		*/
	}
	/**
	Computes the gradient of the activation function; here the gradient is of the 
	bent identity fx
	@param inp input
	@return gradient of the FloatMatrix inp
	*/
	public FloatMatrix activationGrad(FloatMatrix inp)
	{
		FloatMatrix ret = inp.div(MatrixFunctions.sqrt(MatrixFunctions.pow(inp ,(float)2).add((float)1)).mul((float)2) );
		return ret.add((float)1);
		/*FloatMatrix ones = FloatMatrix.ones(inp.rows, inp.columns);
		return ones.sub(MatrixFunctions.pow((float)2, activation(inp)) );*/
		/*
		FloatMatrix sig = activation(inp);
		FloatMatrix ones = FloatMatrix.ones(inp.rows,inp.columns);
		return sig.mul(ones.sub(sig));
		*/
	}
	/**
	Calculates the regularization term for the cost function given by sum(theta[sub{l}]^2)
	@param inp floatmatrix with which to sum
	@param indic integer- 1 indicates gradient regularization, 2 indicates cost function regularization
	*/
	public float sumTheta(FloatMatrix inp, int indic)
	{
		float ret = 0;
		for (int i = 0; i < inp.rows; i++)
			for (int j =0; j < inp.columns; j++)
				if (indic ==2) ret += inp.get(i,j)*inp.get(i,j);
				else if (indic ==1) ret += inp.get(i,j);
				else{System.err.println("Invalid indic integer");}
		return ret;
	}
	/**
	performs forward propagation on the current neural network, updating state variables as well, raw output
	*/
	public FloatMatrix forwardProp(FloatMatrix in)
	{
		FloatMatrix one = FloatMatrix.ones(in.columns);//bias unit; 844 -> 845 
		in = FloatMatrix.concatVertically(one, in);
		
		//theta1 holds 845x3001 mult. by 1x845
		z1 = in.transpose().mmul(theta1.transpose());
//apply activation
		a1 =activation(z1).transpose();//1x3001

		a1 = FloatMatrix.concatVertically(FloatMatrix.ones(a1.columns), a1);

		//theta2 is 3000x3001 mult by 1x3001
		z2 = a1.transpose().mmul(theta2.transpose());
//apply activation
		a2 = activation(z2).transpose(); //3000x1
			//a2 = z2;
		FloatMatrix ones = FloatMatrix.ones(a2.columns);//bias unit again
		a2 = FloatMatrix.concatVertically(ones, a2);
		//3001x1

		//theta3 is 1x3001 mult. by 3001x1
		z3 = a2.transpose().mmul(theta3.transpose()); 
//apply activation
		FloatMatrix h = activation(z3);//a3 is h
		
		float regularization = 0;
		regularization += sumTheta(theta1, 2);
		regularization += sumTheta(theta2, 2);
		regularization += sumTheta(theta3, 2);
//apply regularization
//		h.add(regularization*lambda/2);
		/*		
		System.out.println(String.format("For in: rows are %d, columns are %d", in.rows, in.columns));
		System.out.println(String.format("For one: rows are %d, columns are %d", one.rows, one.columns));
		System.out.println(String.format("For in: rows are %d, columns are %d", in.rows, in.columns));
		System.out.println(String.format("For theta1: rows are %d, columns are %d", theta1.rows, theta1.columns));	
		System.out.println(String.format("For a1: rows are %d, columns are %d", a1.rows, a1.columns));		
		System.out.println(String.format("For a1: rows are %d, columns are %d", a1.rows, a1.columns));	
		System.out.println(String.format("For theta2: rows are %d, columns are %d", theta2.rows, theta2.columns));
		System.out.println(String.format("For a2: rows are %d, columns are %d", a2.rows, a2.columns));	
		System.out.println(String.format("For ones: rows are %d, columns are %d", ones.rows, ones.columns));
		System.out.println(String.format("For theta3: rows are %d, columns are %d", theta3.rows, theta3.columns));		
		System.out.println(String.format("For biased a2: rows are %d, columns are %d", a2.rows, a2.columns));
		System.out.println(String.format("For h: rows are %d, columns are %d", h.rows, h.columns));
		System.out.printf("%f is 0\n ", h.get(0));
*/	
		return h;
	}
	/**
	Computes the cost function as given by the SS 
	0.5*sum(h-y)^2
	*/
	public void setJ()
	{
		FloatMatrix h = forwardProp(X);
		FloatMatrix y = forwardProp(des);
		J = ((h.get(0)-y.get(0))*(h.get(0)-y.get(0)))/2;
	}
	/**
	remove the bias unit in a floatmatrix by rm the column. This is under the assumption that the bias is the first row placed in FloatMatrix inp
	*/
	public FloatMatrix rmBias(FloatMatrix inp)
	{
		//System.out.printf("%d followed by %d \n", inp.rows, inp.columns-1);
		FloatMatrix ret = new FloatMatrix(inp.rows, inp.columns-1);
		for (int i =0; i < inp.rows; i ++)
			for (int j =1; j < inp.columns-1; j++){
				ret.put(i, j, inp.get(i,j));
			}
		return ret;
	}
	/** 
	performs backpropagation on the current neural network
	Sets the Theta1Grad, theta2Grad, theta3Grad; note the other state variable matrices are not modified
	*/
	public void backProp()
	{			
		//perform forward prop
		FloatMatrix y = forwardProp(des);
		FloatMatrix h = forwardProp(X);//ths the state vars are reflective of X, or the state of the board before the pro came into play
		
		//back-prop time
		delta3 = y.sub(h);

		delta2 = theta3.transpose().mmul(delta3).transpose();
		delta2 = rmBias(delta2);
		delta2 = delta2.mul(activationGrad(z2));

		delta1 = theta2.transpose().mmul(delta2.transpose()).transpose(); //1x3001 w/transpose

		delta1 = rmBias(delta1);//rm bias
		delta1 = delta1.mul(activationGrad(z1));

		theta3Grad.add(delta3.mul(a2.transpose()));//1x3001

		theta2Grad.add(delta2.transpose().mmul(a1.transpose()));//1x3000 * 1x3001 = 3000x3001

		FloatMatrix tmpX = X;
		FloatMatrix one = FloatMatrix.ones(tmpX.columns);//bias unit; 844 -> 845 
		tmpX = FloatMatrix.concatVertically(one, tmpX);
		theta1Grad.add(delta1.transpose().mmul(tmpX.transpose()).transpose()); 	

		gradientCheck();//perform grad. check after optimization
		//add regularization
		theta1Grad.add (lambda*sumTheta(theta1, 1));
		theta2Grad.add (lambda*sumTheta(theta2, 1));
		theta3Grad.add (lambda*sumTheta(theta3, 1));
		/*
		
		System.out.println(String.format("For delta3: rows are %d, columns are %d", delta3.rows, delta3.columns));		
		System.out.println(String.format("For delta1: rows are %d, columns are %d", delta1.rows, delta1.columns));		
		System.out.println(String.format("For delta2: rows are %d, columns are %d", delta2.rows, delta2.columns));		

		System.out.println(String.format("For a2: rows are %d, columns are %d", a2.rows, a2.columns));		
		System.out.println(String.format("For a1: rows are %d, columns are %d", a1.rows, a1.columns));	

		System.out.println(String.format("For t1grad: rows are %d, columns are %d", theta1Grad.rows, theta1Grad.columns));		
		System.out.println(String.format("For t2grad: rows are %d, columns are %d", theta2Grad.rows, theta2Grad.columns));		
		System.out.println(String.format("For t3grad: rows are %d, columns are %d", theta3Grad.rows, theta3Grad.columns));		
		System.out.println(String.format("For x: rows are %d, columns are %d", X.rows, X.columns));				
*/
	}
	/**
	Based on the hinge loss function, the weights of the NN will be updated via backprop.
	*/
	public void updateWeights(FloatMatrix maxPos, FloatMatrix proPos)
	{
		float max = forwardProp(maxPos).get(0); //this is the maximum valued position by the board at that moment	
		float pro = forwardProp(proPos).get(0);//this is the pro player move evaluated by forward prop
		float loss = gamma + max; //max()
		if (loss>pro) optimize();//then we got this position "wrong" and need the NN weights must be adjusted; here optimize calls backprop in order to get the partials
	}
	/**
	Ensure the gradient calculation is correct; each iteration of this O(n^2) loop is essentially a gradient check
	*/
	public FloatMatrix gradientCheck()
	{
		System.out.println("Gradient Check start");
		FloatMatrix checkone = new FloatMatrix(3000, 845);
		FloatMatrix checktwo = new FloatMatrix(3000, 3001);
		FloatMatrix checkthree = new FloatMatrix(1, 3001);//844
		List <Float> gradResiduals = new ArrayList <Float>();
		float epsilon = (float)1e-4;
		//theta1
		theta1.put(1,0, epsilon);
		for (int i =0; i < 3000; i ++)
			for (int j =0; j < 845; j++){
				float sub = theta1.get(i,j) + epsilon;//j(theta+eps)
				theta1.put(i, j, sub);
				setJ();
				float right = J;
				
				theta1.put(i, j, (float)theta1.get(i,j) - (2* epsilon));//j(theta - eps)
				setJ();
				float left = J;
				
				theta1.put(i, j, (float)theta1.get(i,j) + epsilon); //set back to j(theta)
				float grad = (right - left)/ (2*epsilon);//calculate approximate grad
				checkone.put(i,j, (float)grad); 
				float resid = theta1Grad.get(i,j) - checkone.get(i,j);
				gradResiduals.add(resid);
			}
		for (int i =0; i < 3000; i ++)
			for (int j =0; j < 3001; j++){
				theta2.put(i, j, theta2.get(i,j) + epsilon);//get righthand
				setJ();
				float right = J;				
				
				theta2.put(i, j, theta2.get(i,j) - (2* epsilon));//get lefthand
				setJ();
				float left = J;				
				
				theta2.put(i, j, theta2.get(i,j) + epsilon);//set back to normal
				float grad =(right - left)/ (2*epsilon);
				checktwo.put(i,j, (float)grad); 
				float resid = theta2Grad.get(i,j) - checktwo.get(i,j);
				gradResiduals.add(resid);
			}	
		for (int i =0; i < 1; i ++)
			for (int j =0; j < 3001; j++){
				theta3.put(i, j, theta3.get(i,j) + epsilon);//get righthand
				setJ();
				float right = J;
								
				theta3.put(i, j, theta3.get(i,j) - (2* epsilon));//get lefthand
				setJ();
				float left= J;
				
				theta3.put(i, j, theta3.get(i,j) + epsilon);//set back to normal
				float grad =(right - left)/(2*epsilon);
				checkthree.put(i,j, (float)grad); 
				float resid = theta3Grad.get(i,j) - checkthree.get(i,j);
				gradResiduals.add(resid);
			}			
		System.out.println("Gradient residuals start");
		for (int i = 0; i < gradResiduals.size(); i ++)
			System.out.println(gradResiduals.get(i));
		System.out.println("Gradient residuals end");
		System.out.println("Gradient Check End");
		return FloatMatrix.ones(5);
	}
	/**
	Set allTheta
	*/
	public void setTheta()
	{
		int pointer = 0;
		for (int i = 0; i < 3001; i ++)
			{allTheta.put(pointer, theta1.get(i)); pointer++; }
		for (int i = 0; i < 3001; i ++)
			{allTheta.put(pointer, theta2.get(i)); pointer ++;}
		for (int i = 0; i < 846; i ++)
			{allTheta.put(pointer, theta3.get(i)); pointer ++;}
	}
	/**
	Optimization of parameters, or an online training session
	*/
	public void optimize()
	{
		System.out.println("\nOptimization process started\n");
		gradDescent(0.01, 1500);
		System.out.println("Finished optimization process");
	}
	/**
	Perform gradient descent, with the partial deriv's given by backprop.
	This is however completed not with batch descent but essentially basically online learning
	*/
	public void gradDescent(double alpha, int iters)
	{
		//float red = getReduction();
		//int i =0;
		//while(/*(red > 1e-4) &&*/ (i < iters)))//until convergence
		//{
			backProp();		
			theta1 = theta1.sub(theta1Grad.mul((float)alpha));
			theta2 = theta2.sub(theta2Grad.mul((float)alpha));
			theta3 = theta3.sub(theta3Grad.mul((float)alpha));
			//red = getReduction();
		//	i++;
		//}
	}
	/**
	Gets the average value on the gradients of theta to determine convergence
	*/
	public float getReduction()
	{//253500 + 9003000 + 3001
		float tot = 0;
		for (int i =0; i < 3000; i ++)
			for (int j =0; j < 845; j++)
				tot += theta1Grad.get(i,j);
		for (int i =0; i < 3000; i ++)
			for (int j =0; j < 3001; j++)
				tot += theta2Grad.get(i,j);
		for (int i =0; i < 1; i ++)
			for (int j =0; j < 3001; j++)
				tot += theta3Grad.get(i,j);
		return tot/9259501;
	}
	public void test()
	{
		FloatMatrix tt= new FloatMatrix(1);
		tt.put(0, (float)-2);
		//System.out.println(activation(tt).get(0));
	}
	/**
	Cleanup function that outputs current parameters to an outfile
	*/
	public void fin()
	{
  		try{
			PrintWriter writer = new PrintWriter("parameters.in");
			writer.println(iters);
			for (int i =0; i < 3000; i ++)
				for (int j =0; j < 845; j++)
					writer.printf("%f ", theta1.get(i, j));
			writer.println();
			for (int i =0; i < 3000; i++)
				for (int j =0; j < 3001; j ++)
					writer.printf("%f ", theta2.get(i, j));
			writer.println();
			for (int i =0; i < 3000; i ++)
				writer.printf("%f ", theta3.get(0, i));
			writer.close();
  		} catch(FileNotFoundException ex){System.err.println("File not found");}
	}
}
