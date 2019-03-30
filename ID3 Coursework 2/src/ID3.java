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
		String[] TEST = {"No", "No", "Yes", "Yes", "Yes", "No", "Yes","No", "Yes", "Yes", "Yes", "Yes", "Yes","No"};


		
	} // classify()

	public void train(String[][] trainingData) {
		indexStrings(trainingData);
		String[][] dataSansHeader = removeHeader(trainingData);
		String[][] alteredData = classNumberfier(dataSansHeader);
		Integer[] columnsRemoved = new Integer[trainingData[0].length];
		for(int i = 0 ; i < trainingData[0].length ; i++){
			columnsRemoved[i] = -1;
		}
		decisionTree = recursiveTrainingmethod(alteredData, columnsRemoved);
	}

	public int recursiveClassify(TreeNode tree, String[] rowToClassify, String[] header){

	}

	public String[][] classNumberfier(String[][] data){
		String[][] results = new String[data.length][data[0].length];
		CountingList<String> classes = new CountingList<>();
		for(int i = 0 ; i < data.length ; i++){
			for(int j = 0 ; j < data[i].length ; j++){
				if(j == data[i].length -1){
					classes.add(data[i][j]);
					results[i][j] = Integer.toString(classes.find(data[i][j]));
					//System.out.print(Integer.toString(classes.find(data[i][j])) + " | ");
				}else{
					results[i][j] = data[i][j];
				}
			}
		}
		return results;
	}

	public int findString(String test, int column){
		for(int i =  0; i < strings[column].length; i++){
			if(test.equals(strings[column][i])){
				return i;
			}
		}
		return -1;
	}

	public TreeNode recursiveTrainingmethod(String[][] data, Integer[] columnsRemoved){
		int activeColumn = 0;
		double totalEntropy = calcTotalEntropy(data);
		//System.out.println("Total Level Entropy: " + totalEntropy + " | Total Values " + data.length);
		ArrayList<TreeNode> returnedTreeNode = new ArrayList<>();
		double[] branchEntropy = calcBranchEntropy(data);
		activeColumn = biggestGain(branchEntropy, totalEntropy);
		if(totalEntropy == 0){
			return new TreeNode(null, Integer.parseInt(data[0][data[0].length-1]));
		}
		//System.out.println("Active Column: " + activeColumn);
		ArrayList<String[][]> branchifiedData = new ArrayList<>();
		branchifiedData = branchifyData(data, activeColumn);
		//System.out.println(data[0].length + " " + branchifiedData.size());
		Integer[] columnsRemoveNow = calcRemoved(columnsRemoved, activeColumn);
		for(int k = 0 ; k < columnsRemoveNow.length ; k++){
			//System.out.print(columnsRemoveNow[k] + " ");
		}
		//System.out.println();
		if(data[0].length > 1) {
			for (int i = 0; i < branchifiedData.size(); i++) {
				returnedTreeNode.add(recursiveTrainingmethod(branchifiedData.get(i), columnsRemoveNow));
			}
		}
		TreeNode[] childAtLevel = new TreeNode[returnedTreeNode.size()];
		for(int i = 0 ; i < returnedTreeNode.size() ; i++){
			childAtLevel[i] = returnedTreeNode.get(i);
		}
		return new TreeNode(childAtLevel, compareIntArrays(columnsRemoved, columnsRemoveNow));
	}

	public int compareIntArrays(Integer[] oldColumn, Integer[] newColumn){
		for(int i = 0 ; i < oldColumn.length ; i++){
			//System.out.println(oldColumn[i] + " " + newColumn[i]);
			if(oldColumn[i] != newColumn[i]){
				return i;
			}
		}
		return -100;
	}

	public Integer[] calcRemoved(Integer[] data, int newInt){
		int count = 0;
		Integer[] results = new Integer[data.length];
		for(int j = 0 ; j < data.length ; j++){
			results[j] = data[j];
			//System.out.println(results[j]);
		}
		for(int i = 0 ; i < data.length ; i++){
			if(results[i] == -1){
				if(count == newInt){
					results[i] = newInt;
					return results;
				}
				count++;
			}
		}
		return results;
	}

	public int biggestGain(double[] branchEntropy, double totalEntropy){
		int biggestGainCurrently = 0;
		double gain = -1;
		for(int i = 0 ; i < branchEntropy.length ; i++){
			if(totalEntropy - branchEntropy[i] > gain){
				gain = totalEntropy - branchEntropy[i];
				//System.out.println(gain);
				biggestGainCurrently = i;
			}
		}
		return biggestGainCurrently;
	}

	public  double[] calcBranchEntropy(String[][] trainingData){
		int numberOfAttributes = trainingData[0].length - 1;
		double[] results = new double[numberOfAttributes];
		for(int i = 0 ; i < numberOfAttributes ; i++){
			results[i] = calcAttributeEntropy(trainingData, i);
		}
		for(int j = 0 ; j < results.length ; j++){
			//System.out.println("Attribute " + j + ": " + results[j]);
		}
		return results;
	}

	public  double calcAttributeEntropy(String[][] data, int parameterColumn){ //
		double result = 0;
		int numberOfColumns = data[0].length;
		CountingList<String> AttributeList = new CountingList<String>();
		ArrayList<CountingList<String>> classList = new ArrayList<CountingList<String>>();
		int attributePosition = 0;
		double probabilityOfCurrent = 0;
		for(int i = 0; i < data.length ; i++){
			AttributeList.add(data[i][parameterColumn]);
			attributePosition = AttributeList.find(data[i][parameterColumn]);
			if(AttributeList.size() == classList.size()) {
				//System.out.println(data[i][parameterColumn] + " at " + AttributeList.find(data[i][parameterColumn]));
				classList.get(AttributeList.find(data[i][parameterColumn])).add(data[i][numberOfColumns-1]);
			}else{
				//System.out.println(data[i][parameterColumn] + " at " + AttributeList.find(data[i][parameterColumn]));
				classList.add(new CountingList<String>());
				classList.get(AttributeList.find(data[i][parameterColumn])).add(data[i][numberOfColumns-1]);
			}
		}
		for(int j = 0 ; j < AttributeList.size() ; j++){
			for(int k = 0 ; k < classList.get(j).size(); k++){
				probabilityOfCurrent = (double)classList.get(j).get(k)/(double)AttributeList.total();
				//System.out.println();
				result = result + probabilityOfCurrent*calcEntropyWithAdjustment(classList.get(j).get(k) , classList.get(j).total());
				//System.out.println("prob " + probabilityOfCurrent + " | result " + result + " | Count " + classList.get(j).get(k) + " | " + AttributeList.total());
			}
			//System.out.println("break");
		}
		return result;
	}

	public ArrayList<ArrayList<String[]>> splitifyData(String[][] data, int column){
		ArrayList<ArrayList<String[]>> results = new ArrayList<>();
		ArrayList<String> positionOfElements = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			if(positionOfElements.contains(data[i][column])){
				results.get(positionOfElements.indexOf(data[i][column])).add(removeColumn(data[i], column));
			}else{
				positionOfElements.add(data[i][column]);
				results.add(new ArrayList<String[]>());
				results.get(positionOfElements.indexOf(data[i][column])).add(removeColumn(data[i], column));
			}
		}
		return results;
	}

	public String[] removeColumn(String[] data, int column){
		String[] results = new String[data.length-1];
		for (int i = 0; i < data.length; i++) {
			if(i < column) {
				results[i] = data[i];
			}
			if(i > column){
				results[i-1] = data[i];
			}
		}
		return results;
	}

	public  ArrayList<String[][]> branchifyData(String[][] data, int column){
		int count = 0;
		ArrayList<String[][]> results = new ArrayList<>();
		ArrayList<ArrayList<String[]>> splitData = splitifyData(data, column);
		String[][] resultsTemp = new String[0][0];
		for(int i = 0 ; i < splitData.size() ; i++){
			resultsTemp = new String[splitData.get(i).size()][splitData.get(i).get(0).length];
			for(int j = 0 ; j < splitData.get(i).size() ; j++){
				resultsTemp[j] = splitData.get(i).get(j);
			}
			results.add(resultsTemp);
		}
		return results;
	}

	public boolean containsAttribute(String[][] data){
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
			//System.out.println(result);
		}
		return result;
	}
	public  String[][] removeHeader(String[][] trainingData){
		String[][] temp = new String[trainingData.length-1][trainingData[0].length];
		for(int i = 1; i < trainingData.length ; i++){
			for(int j = 0 ; j < trainingData[0].length ; j++){
				temp[i-1][j] = trainingData[i][j];
			}
		}
		return temp;
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
		listOfContent = new ArrayList();
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
		if(indexInContent >= 0){
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

	public int find(E ob){
		return listOfContent.indexOf(ob);
	}

	public int total() {
		int total = 0;
		for (int i = 0 ; i < length ; i++) {
			total = total + countOfContent.get(i);
		}
		return total;
	}

	public int size(){
		return listOfContent.size();
	}

	public void print(){
		for(int i = 0 ; i < listOfContent.size() ; i++){
			System.out.println(listOfContent.get(i));
		}
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
	public int getClassLength(E ob){
		int indexInContent = listOfContent.indexOf(ob);
		if(indexInContent == -1){
			return -1;
		}else {
			return classOfCounts.get(indexInContent).length;
		}
	}
}
































