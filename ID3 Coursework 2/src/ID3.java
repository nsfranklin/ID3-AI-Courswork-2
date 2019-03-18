// ECS629/759 Assignment 2 - ID3 Skeleton Code
// Author: Simon Dixon

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

class ID3 {

	/** Each node of the tree contains either the attribute number (for non-leaf
	 *  nodes) or class number (for leaf nodes) in <b>value</b>, and an array of
	 *  tree nodes in <b>children</b> containing each of the children of the
	 *  node (for non-leaf nodes).
	 *  The attribute number corresponds to the column number in the training
	 *  and test files. The children are ordered in the same order as the
	 *  Strings in strings[][]. E.g., if value == 3, then the array of
	 *  children correspond to the branches for attribute 3 (named data[0][3]):
	 *      children[0] is the branch for attribute 3 == strings[3][0]
	 *      children[1] is the branch for attribute 3 == strings[3][1]
	 *      children[2] is the branch for attribute 3 == strings[3][2]
	 *      etc.
	 *  The class number (leaf nodes) also corresponds to the order of classes
	 *  in strings[][]. For example, a leaf with value == 3 corresponds
	 *  to the class label strings[attributes-1][3].
	 **/
	class TreeNode {

		TreeNode[] children;
		int value;

		public TreeNode(TreeNode[] ch, int val) {
			value = val;
			children = ch;
		} // constructor

		public String toString() {
			return toString("");
		} // toString()
		
		String toString(String indent) {
			if (children != null) {
				String s = "";
				for (int i = 0; i < children.length; i++)
					s += indent + data[0][value] + "=" +
							strings[value][i] + "\n" +
							children[i].toString(indent + '\t');
				return s;
			} else
				return indent + "Class: " + strings[attributes-1][value] + "\n";
		} // toString(String)

	} // inner class TreeNode

	private int attributes; 	// Number of attributes (including the class)
	private int examples;		// Number of training examples
	private TreeNode decisionTree;	// Tree learnt in training, used for classifying
	private String[][] data;	// Training data indexed by example, attribute
	private String[][] strings; // Unique strings for each attribute
	private int[] stringCount;  // Number of unique strings for each attribute

	public ID3() {
		attributes = 0;
		examples = 0;
		decisionTree = null;
		data = null;
		strings = null;
		stringCount = null;
	} // constructor
	
	public void printTree() {
		if (decisionTree == null)
			error("Attempted to print null Tree");
		else
			System.out.println(decisionTree);
	} // printTree()

	/** Print error message and exit. **/
	static void error(String msg) {
		System.err.println("Error: " + msg);
		System.exit(1);
	} // error()

	static final double LOG2 = Math.log(2.0);
	
	static double xlogx(double x) {
		return x == 0? 0: x * Math.log(x) / LOG2;
	} // xlogx()

	/** Execute the decision tree on the given examples in testData, and print
	 *  the resulting class names, one to a line, for each example in testData.
	 **/

	public void classify(String[][] testData) {
		if (decisionTree == null)
			error("Please run training phase before classification");


	} // classify()

	public void train(String[][] trainingData) {
		indexStrings(trainingData);
		int activeColumn = 0;
		String[][] dataSansHeader = removeHeader(trainingData);
		double totalEntropy = calcTotalEntropy(trainingData);
		double[] branchEntropy = calcBranchEntropy(trainingData);

		branchifyData(trainingData, activeColumn);

	}

	public <E> double[] calcBranchEntropy(E[][] trainingData){
		int numberOfAttributes = trainingData.length - 1;
		double[] results = new double[numberOfAttributes];
		for(int i = 0 ; i < numberOfAttributes ; i++){
			results[i] = calcAttributeEntropy(trainingData, i);
		}
		return results;
	}

	public <E> double calcAttributeEntropy(E[][] data, int parameterColumn){ //
		double result = 0;
		ClassIncludingCountingListing<E> classIncCountList = new ClassIncludingCountingListing<>();
		int numberOfColumns = data[0].length;
		for(int i = 0; i < data.length ; i++){
			classIncCountList.add(data[i][parameterColumn], data[i][numberOfColumns-1]);
		}
		for(int j = 0 ; j < classIncCountList.length ; j++){
			result = result + calcEntropyWithAdjustment(classIncCountList.getClassCount((E)classIncCountList.getObj(parameterColumn), j) , classIncCountList.getClassTotal((E)classIncCountList.getObj(parameterColumn)));
			System.out.println(result + " j");
		}
		return result;
	}

	public <E> ArrayList<E[][]> branchifyData(E[][] data, int column){
		int count = 0;




		E[][] result = (E[][])new Object[count][data[0].length-1];

		for (int i = 0; i < data.length; i++) {
			//if (containsAttribute(data[i])) {
			//	for (int j = 0 ; j < data[0].length ; j++){
			//	}
			//}
		}
		return new ArrayList<E[][]>();
	}

	public <E> boolean containsAttribute(E[][] data){
		return true;
	}

	public double calcTotalEntropy(String[][] data){
		double result = 0;
		CountingList<String> countingList = new CountingList<>();
		int numberOfColumns = data[0].length;
		for(int i = 0 ; i < data.length ; i++){
			countingList.add(data[i][numberOfColumns-1]);
		}
		for(int j = 0 ; j < countingList.length ; j++ ){
			result = result + calcEntropyWithAdjustment(countingList.get(j),countingList.total());
			System.out.println(result);
		}
		return result;
	}
	public <E> E[][] removeHeader(E[][] trainingData){
		E[][] temp = (E[][])new Object[trainingData.length-1][trainingData[0].length];
		for(int i = 1; i < trainingData.length ; i++){
			for(int j = 0 ; j < trainingData[0].length ; j++){
				temp[i-1][j] = trainingData[i][j];
			}
		}
		return temp;
	}
	public <E> double calcEntropyOfCountingList(CountingList<E> list){
		double result = -1;
		for(int i = 0 ; i < list.length ; i++){


		}

		return result;
	}
	public double calcEntropyWithAdjustment(int count, int total){
		if(count > total){
			System.out.println("Count larger than total. What are you doing mug.");
		}
		double x = (double)count/(double)total;
		return xlogx(x)*-1;
	}

	/** Given a 2-dimensional array containing the training data, numbers each
	 *  unique value that each attribute has, and stores these Strings in
	 *  instance variables; for example, for attribute 2, its first value
	 *  would be stored in strings[2][0], its second value in strings[2][1],
	 *  and so on; and the number of different values in stringCount[2].
	 **/
	void indexStrings(String[][] inputData) {
		data = inputData;
		examples = data.length;
		attributes = data[0].length;
		stringCount = new int[attributes];
		strings = new String[attributes][examples];// might not need all columns
		int index = 0;
		for (int attr = 0; attr < attributes; attr++) {
			stringCount[attr] = 0;
			for (int ex = 1; ex < examples; ex++) {
				for (index = 0; index < stringCount[attr]; index++)
					if (data[ex][attr].equals(strings[attr][index]))
						break;	// we've seen this String before
				if (index == stringCount[attr])		// if new String found
					strings[attr][stringCount[attr]++] = data[ex][attr];
			} // for each example
		} // for each attribute
	} // indexStrings()

	/** For debugging: prints the list of attribute values for each attribute
	 *  and their index values.
	 **/
	void printStrings() {
		for (int attr = 0; attr < attributes; attr++)
			for (int index = 0; index < stringCount[attr]; index++)
				System.out.println(data[0][attr] + " value " + index +
									" = " + strings[attr][index]);
	} // printStrings()
		
	/** Reads a text file containing a fixed number of comma-separated values
	 *  on each line, and returns a two dimensional array of these values,
	 *  indexed by line number and position in line.
	 **/
	static String[][] parseCSV(String fileName)
								throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String s = br.readLine();
		int fields = 1;
		int index = 0;
		while ((index = s.indexOf(',', index) + 1) > 0)
			fields++;
		int lines = 1;
		while (br.readLine() != null)
			lines++;
		br.close();
		String[][] data = new String[lines][fields];
		Scanner sc = new Scanner(new File(fileName));
		sc.useDelimiter("[,\n]");
		for (int l = 0; l < lines; l++)
			for (int f = 0; f < fields; f++)
				if (sc.hasNext())
					data[l][f] = sc.next();
				else
					error("Scan error in " + fileName + " at " + l + ":" + f);
		sc.close();
		return data;
	} // parseCSV()

	public static void main(String[] args) throws FileNotFoundException,
												  IOException {
		if (args.length != 2)
			error("Expected 2 arguments: file names of training and test data");
		String[][] trainingData = parseCSV(args[0]);
		String[][] testData = parseCSV(args[1]);
		ID3 classifier = new ID3();
		classifier.train(trainingData);
		classifier.printTree();
		classifier.classify(testData);
	} // main()

} // class ID3

class CountingList<E>{

	protected ArrayList<E> listOfContent;
	protected ArrayList<Integer> countOfContent;
	public int length;

	CountingList(){
		listOfContent = new ArrayList<E>();
		countOfContent = new ArrayList<Integer>();
		length = 0;
	}

	public void add(E ob){
		int indexInContent;
		if(listOfContent.contains(ob)){
			indexInContent = listOfContent.indexOf(ob);
			countOfContent.set(indexInContent, countOfContent.get(indexInContent) + 1);
		}else{
			listOfContent.add(ob);
			countOfContent.add(1);
			this.length++;
		}
	}

	public void remove(){}

	public int getByOb(E ob){
		int indexInContent = listOfContent.indexOf(ob);
		if(indexInContent > 0){
			return countOfContent.get(indexInContent);
		}
		return indexInContent;
	}

	public int get(int i){
		return countOfContent.get(i);
	}

	public E getObj(int i){
		return listOfContent.get(i);
	}

	public int total() {
		int total = 0;
		for (int i = 0 ; i < length ; i++) {
			total = total + countOfContent.get(i);
		}
		return total;
	}

}
class ClassIncludingCountingListing<E> extends CountingList{

	private ArrayList<CountingList> classOfCounts;

	ClassIncludingCountingListing(){
		super();
		classOfCounts = new ArrayList<>();
	}

	public void add(E ob, E classFound){
		int indexInContent;
		int tempsize;
		if(listOfContent.contains(ob)){
			indexInContent = listOfContent.indexOf(ob);
			countOfContent.set(indexInContent, (Integer)countOfContent.get(indexInContent) + 1);
			classOfCounts.get(indexInContent).add(classFound);

		}else{
			listOfContent.add(ob);
			countOfContent.add(1);
			classOfCounts.add(new CountingList());
			tempsize = classOfCounts.size();
			classOfCounts.get(tempsize-1).add(classFound);
			this.length++;
		}
	}

	public int getClassTotal(E ob){
		int indexInContent = listOfContent.indexOf(ob);
		if(indexInContent == -1){
			return -1;
		}else{
			return classOfCounts.get(indexInContent).total();
		}
	}

	public int getClassCount(E ob, int i){
		int indexInContent = listOfContent.indexOf(ob);
		if(indexInContent == -1){
			return -1;
		}else{
			return classOfCounts.get(indexInContent).get(i);
		}
	}


}
































