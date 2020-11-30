

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ZFunction {
	private int x;
	private int y;
	public ZFunction(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		if(getY() >= 0) {
			return getX()+"x + "+getY()+"y";
		}
		else {
			return getX()+"x - "+(getY()*-1)+"y";
		}
	}
}

class Equations {
	private int x;
	private int y;
	private int c;
	private String equality;
	public Equations(int x, int y, int c, String equality) {
		this.x = x;
		this.y = y;
		this.c = c;
		this.equality = equality;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getC() {
		return c;
	}
	public String getEquality() {
		return equality;
	}
	@Override
	public String toString() {
		if(getY() >= 0) {
			return getX()+"x + "+getY()+"y "+getEquality()+" "+getC();
		}
		else {
			return getX()+"x - "+(getY()*-1)+"y "+getEquality()+" "+getC();
		}
	}
}

class Constraints {
	private String x;
	private String equality;
	private int c;
	public Constraints(String x, String equality, int c) {
		this.x = x;
		this.equality = equality;
		this.c = c;
	}
	public String getX() {
		return x;
	}
	public String getEquality() {
		return equality;
	}
	public int getC() {
		return c;
	}
	@Override
	public String toString() {
		return getX()+" "+getEquality()+" "+getC();
	}
}

class StandardZ {
	private int X;
	private int Y;
	private int slack[];
	public StandardZ(int x, int y, int size) {
		X = x;
		Y = y;
		slack = new int[size];
		for(int i=0; i<slack.length; i++) {
			slack[i] = 0;
		}
	}
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	public int[] getSlack() {
		return slack;
	}
	public String printSlack(int slack[]) {
		String slackString = "";
		for(int i=0; i<slack.length; i++) {
			if(i == (slack.length-1)) {
				slackString += slack[i]+"S"+(i+1);
			}
			else {
				slackString += slack[i]+"S"+(i+1)+" + ";
			}
		}
		return slackString;
	}
	@Override
	public String toString() {
		if(getY() >= 0) {
			return "Z = "+getX()+"x + "+getY()+"y + "+printSlack(getSlack());
		}
		return "Z = "+getX()+"x - "+(getY()*-1)+"y + "+printSlack(getSlack());
	}
}

class StandardEquations {
	private int coef_X;
	private int coef_Y;
	private int slack[];
	private int c;
	public StandardEquations(int coef_X, int coef_Y, int c, int slack, int size) {
		this.coef_X = coef_X;
		this.coef_Y = coef_Y;
		this.c = c;
		this.slack = new int[size];
		for(int i=0; i<size; i++) {
			if(i == slack) {
				this.slack[i] = 1;
			}
			else {
				this.slack[i] = 0;
			}
		}
	}
	public int getCoef_X() {
		return coef_X;
	}
	public int getCoef_Y() {
		return coef_Y;
	}
	public int[] getSlack() {
		return slack;
	}
	public int getC() {
		return c;
	}
	public String printSlack(int slack[]) {
		String slackString = "";
		for(int i=0; i<slack.length; i++) {
			if(i == slack.length-1) {
				slackString += slack[i]+"S"+(i+1);
			}
			else {
				slackString += slack[i]+"S"+(i+1)+" + ";
			}
		}
		return slackString;
	}
	@Override
	public String toString() {
		if(getCoef_Y() >= 0) {
			return getCoef_X()+"x + "+getCoef_Y()+"y + "+printSlack(getSlack())+" = "+getC();
		}
		return getCoef_X()+"x - "+(getCoef_Y()*-1)+"y + "+printSlack(getSlack())+" = "+getC();
	}	
}

public class Simplex {
	
	public static Object[][] initialTable(StandardZ z, List<StandardEquations> stEquations) {
									//6 X 8
		Object table[][] = new Object[4+stEquations.size()][6+stEquations.size()];
		table[0][0] = "CBi";
		table[0][1] = "Cj";
		table[1][0] = "  ";
		table[1][1] = "BV";
		
		table[0][2] = z.getX();
		table[0][3] = z.getY();
		table[1][2] = "x";
		table[1][3] = "y";
		
		table[0][stEquations.size()+4] = "Solution";
		table[0][stEquations.size()+5] = "Ratio";
		
		int slack[] = z.getSlack();
		int sumX=0, sumY=0, sumC=0;
		int sumSlack[] = new int[stEquations.size()];
		for(int i=0; i<stEquations.size(); i++) {
			table[0][i+4] = table[i+2][0] = slack[i];
			table[1][i+4] = table[i+2][1] = "S"+(i+1);
			
			StandardEquations steq = stEquations.get(i);
			table[i+2][2] = steq.getCoef_X();
			table[i+2][3] = steq.getCoef_Y();
			int slack2[] = steq.getSlack();
			for(int j=4; j<4+stEquations.size(); j++) {
				table[i+2][j] = slack2[j-4];
				sumSlack[j-4] += ((int)table[i+2][0]) * slack2[j-4];
			}
			table[i+2][4+stEquations.size()] = steq.getC();
			
			sumX += ((int)table[i+2][0]) * ((int)table[i+2][2]);
			sumY += ((int)table[i+2][0]) * ((int)table[i+2][3]);
			sumC += ((int)table[i+2][0]) * ((int)table[i+2][4+stEquations.size()]);
		}
		table[stEquations.size()+2][0] = "  ";
		table[stEquations.size()+3][0] = "  ";
		table[stEquations.size()+2][1] = "Zj";
		table[stEquations.size()+3][1] = "Cj-Zj";
		
		for(int j=2; j<4+stEquations.size(); j++) {
			if(j == 2) {
				table[stEquations.size()+2][j] = sumX;
				table[stEquations.size()+3][j] = (int)table[0][j] - sumX;
			}
			else if(j == 3) {
				table[stEquations.size()+2][j] = sumY;
				table[stEquations.size()+3][j] = (int)table[0][j] - sumY;
			}
			else if(j <= 3+stEquations.size()){
				table[stEquations.size()+2][j] = sumSlack[j-4];
				table[stEquations.size()+3][j] = (int)table[0][j] - sumSlack[j-4];
				table[stEquations.size()+2][j+1] = sumC;
			}
		}
		return table;
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		//Operation Min/Max
		String lppOperation = "";
		System.out.println("Minimize(m) or Maximize(M) ?");
		String op = scanner.next();
		if(op.equals("m")) {
			lppOperation = "Minimize";
		}
		else if(op.equals("M")) {
			lppOperation = "Maximize";
		}
		
		//Function Z
		System.out.println("Enter the Z function (x, y) :- ");
		ZFunction z = new ZFunction(scanner.nextInt(), scanner.nextInt());
		
		//Subject to functions 
		System.out.println("Enter no. of Subject to functions : ");
		int noOfEq = scanner.nextInt();
		List<Equations> equationsList = new ArrayList<Equations>();
		for(int i=0; i<noOfEq; i++) {
			System.out.println("Enter (X, Y, C, Equality) : ");
			Equations equation = new Equations(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.next());
			equationsList.add(equation);
		}
		
		//Constraints for the variables
		System.out.println("Enter no. of Constraints :- ");
		int noOfConstraints = scanner.nextInt();
		List<Constraints> constraintsList = new ArrayList<Constraints>();
		for(int i=0; i<noOfConstraints; i++) {
			System.out.println("Enter constraints as (X (Str), Equality, C) :- ");
			Constraints constraint = new Constraints(scanner.next(), scanner.next(), scanner.nextInt());
			constraintsList.add(constraint);
		}
		
		//Standard Form of equations
		StandardZ standardZ = new StandardZ(z.getX(), z.getY(), equationsList.size());
		List<StandardEquations> standardEquations = new ArrayList<StandardEquations>();
		for(int i=0; i<equationsList.size(); i++) {
			Equations equation = equationsList.get(i);
			StandardEquations stdEquations = new StandardEquations(equation.getX(), equation.getY(), equation.getC(), i, equationsList.size());
			standardEquations.add(stdEquations);
		}
		
		//Printing
		System.out.println("-------------------------------------------------");
		System.out.println(lppOperation+" Z = "+z);
		System.out.println("Subject to ");
		for(Equations eq : equationsList) {
			System.out.println(eq);
		}
		System.out.println("Constraint to ");
		for(Constraints constraint : constraintsList) {
			System.out.println(constraint);
		}
		System.out.println("Standard Equations :");
		System.out.println("\t"+lppOperation+"\t"+standardZ);
		System.out.println("\tSubject to");
		for(StandardEquations sEq : standardEquations) {
			System.out.println("\t"+sEq);
		}
		for(int i=0; i<equationsList.size(); i++) {
			Constraints constraint = new Constraints("S"+(i+1), ">=", 0);
			constraintsList.add(constraint);
		}
		System.out.println("\tConstraints");
		for(Constraints constraint : constraintsList) {
			System.out.print("\t"+constraint+" ");
		}
		
		//Print Initial Feasible Table
		System.out.println("\n\nInitial Feasible Table : ");
		Object table[][] = initialTable(standardZ, standardEquations);
		printTable(table);


		
		int max=0;
		int index_of_max=0;
		char choice_of_variable = '0';
		for(int j=2; j<4+equationsList.size(); j++) {
			int temp = (int)table[equationsList.size()+3][j];	
			if(temp>0){
				// max = (max < temp) ? temp : max;
				if(temp>max){
					max = temp;
					index_of_max = j;
					
				}
			}			
		}
		if(max <= 0){
			System.out.print(" END");
		}
		else{
			System.out.print("Index Of Key Column J: " + index_of_max);
			System.out.print("\n Max : " + max);
		}
		float min=999999;
		int index_of_min = 0;
		for(int j=0; j<equationsList.size(); j++) {
			
			float ratio_value = (float)(int)table[equationsList.size()+j][equationsList.size()+4]/(float)(int)table[equationsList.size()+j][index_of_max];
			table[equationsList.size()+j][equationsList.size()+5] = ratio_value; //HardCode
			if(min>ratio_value){
				min=ratio_value;
				index_of_min=j;
			}
			// System.out.println("\n ratio value "+ratio_value);
		}
		index_of_min+=equationsList.size();
		System.out.println("\n Key Row : "+index_of_min);
		System.out.print(" Min : " + min+"\n");
		System.out.print("\nKey Element : " + (int)table[index_of_min][index_of_max]+"\n");

		float key_element = (int)table[index_of_min][index_of_max];

		//
		//Identify X Or Y variable To Interchange With S(N)
		table[index_of_min][0] = table[0][index_of_max];//Coeff Of X Or Coeff Of Y
		table[index_of_min][1] = table[1][index_of_max];//X Or Y
		//Upto Solution Of Key Row Change The Values :)
		 for(int i=2;i <= equationsList.size()+4;i++ ){
			
			if ((int)table[index_of_min][i]>0){                                  //Avoiding ZERO DIV Error, This Condition Was mandatory :(
				float output =  (int)table[index_of_min][i]/key_element;
				System.out.println(table[index_of_min][i]);
				table[index_of_min][i] =  output ;
			}

		 }

		
			//RePrinting Table...
		printTable(table);
				
		scanner.close();
	}

	public static void printTable(Object[][] table) {
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[i].length; j++) {
				if(table[i][j] == null) {
					System.out.print("");
				}
				else {
					System.out.print(table[i][j]+"\t");
				}
			}
			System.out.println();
		}
	}
}