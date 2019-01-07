import java.util.*;
import java.io.*;

public class ac_test {

	/**
	* Reads from dictionary.txt and inserts the words one at a time
	* into the DLB.
	* 
	* @return dlb with dictionary values
	*/
	public static DLB writeDLB(){
		//System.out.println("Beginning to write dictionary...");

		DLB temp = new DLB();

		try {
			
			FileReader f_reader = new FileReader("dictionary.txt");
			BufferedReader b_reader = new BufferedReader(f_reader);

			String word;

			while((word = b_reader.readLine()) != null){
				temp.insertWord(word);
			}

			b_reader.close();

		} catch (IOException ex){
			System.out.println("something went wrong");
		}

		//System.out.println("Finished writing dictionary.");

		return temp;
	}

	/**
	* Write to text file from the hashmap of words that the user has previously types
	* Writes to user-history.txt and creates it if it doesn't exist
	*
	* @param list HashMap with K=word and V=frequency
	*/
	public static void writeHistory(HashMap<String, Integer> list){
			
		try {
			
			File file = new File("user_history.txt");
			file.createNewFile();

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			for(Map.Entry<String, Integer> current : list.entrySet()){
				writer.write(current.getKey() + ":" + current.getValue());
				writer.newLine();
			}

			writer.close();
			
		} catch (IOException ex){
			System.out.println("something went wrong");
		}
	}

	/**
	* Read from user-history.txt and save it in a hash map
	* K/V pairs are separated by ':'
	* 
	* @return hashmap with contents from history
	*/
	public static HashMap<String, Integer> readHistory(){
		
		HashMap<String, Integer> temp = new HashMap<String, Integer>();

		try {
			
			BufferedReader b_reader = new BufferedReader(new FileReader("user_history.txt"));

			String word;
			while((word = b_reader.readLine()) != null){
				String[] line = word.split(":", 2);

				String key = line[0];
				int value = Integer.parseInt(line[1]);

				//System.out.println("adding: " + key);
				temp.put(key, value);
			}

			b_reader.close();

		} catch (IOException ex){
			//System.out.println("something went wrong");
		}

		return temp;
	}

	/**
	* The core of the program
	* Requests the user for input, analyzes the input, and requests predictions
	* Pushes the compelted words to the history map
	* Returns the the words the user completed as well as the existing history
	*
	* @param d DLB made from dictionary.txt
	* @return history map including new words
	*/
	public static HashMap<String, Integer> promptUser(DLB d, HashMap<String, Integer> history){
		Scanner kbd = new Scanner(System.in);
		char input;
		String str = "";
		long time = 0;
		int count = 0;
		String predictions[] = new String[5];
		
		while(true){
			System.out.print("Enter a character: ");
			input = kbd.next().charAt(0);

			if(input == '!'){
				if(count > 0){
					double average = time/count;
					System.out.printf("\nAverage Time: %.6f s\n", (double)average/1000000000.0);		
				}
				System.out.println("Bye!");
				break;
			} else if (input == '$') {
				System.out.println("WORD COMPLETED: " + str + "\n");
				

				if(history.containsKey(str)){
					int val = history.get(str);
					val++;
					history.put(str, val);
				} else {
					history.put(str, 1);
				}
				

				str = "";
			} else {
				if (Character.isDigit(input)){
					int choice = Character.getNumericValue(input) - 1;
					if(choice < 5 && predictions[choice] != null){
						System.out.println("WORD COMPLETED: " + predictions[choice] + "\n");
						

						if(history.containsKey(predictions[choice])){
							int val = history.get(predictions[choice]);
							val++;
							history.put(predictions[choice], val);
						} else {
							history.put(predictions[choice], 1);
						}
						

						str = "";
					} else {
						System.out.println("Not an option.");
					}
				} else {
					str += input;
					
					long t0 = System.nanoTime();
					predictions = predict(str, d, history);
					long t1 = System.nanoTime();
					long t2 = t1-t0;
					time += t2;
					count++;
					System.out.printf("\n(%.6f s)\n", (double)t2/1000000000.0);

					if(predictions[0] == null){
						System.out.println("No predictions found. Enter '$' to finish string.\n");
					} else {
						System.out.println("Predictions: ");
						for(int i = 0; i < predictions.length; i++){
							if(predictions[i] == null){
								break;
							}
							System.out.print("(" + (i+1) + ") " + predictions[i] + "  ");
						}
						System.out.println("\n");
					}
				}
			}
		}

		return history;
	}

	/**
	* Called by predict()
	* Traverses the DLB up until the end of the given prefix
	* Takes the head of the DLB and the string of characters that the user has inputted thusfar
	*
	* @param n Head node of DLB
	* @param s String of characters that the user has inputted
	* @return Node at the end of the prefix
	*/
	public static Node findNode(Node n, String s){
		int ndx = 0;
		Node node = n;
		while(ndx < s.length()){
			node = node.getChild();
			
			while(node.getChar() != s.charAt(ndx)){
				if(node.hasNext()){
					node = node.getNext();
				} else {
					return null;
				}
			}
			ndx++;
		}
		return node;
	}

	/**
	* Sorts the keys from the hashmap in decresing order
	* Puts the keys in a linked list
	*
	* @param map Hashmap of history values to be sorted
	* @return Linked List of strings sorted by frequency of occurance, greatest first
	*/
	public static LinkedList<String> sortMap(HashMap<String, Integer> map){
		LinkedList<Map.Entry<String, Integer>> temp = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
		
		temp.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
		
		LinkedList<String> list = new LinkedList<String>();

		for(Map.Entry<String, Integer> ndx : temp){
			list.add(ndx.getKey());
		}

		return list;
	}

	/**
	* Gives words to predict based on the current string and history values
	* Creates array of at most 5 values
	* If less than 5 values, the rest of the array is null
	* Predicts based off of history first, and then the dictionary DLB
	*
	* @param s Current string of characters that the user is working on
	* @param d DLB of values from dictionary
	* @param hist Map of words the user has already created
	* @return String array of 5 or less predictions
	*/
	public static String[] predict(String s, DLB d, HashMap<String, Integer> hist){
		String[] pred = new String[5];
		Stack<Node> nodeStack= new Stack<Node>();
		String slice = "";
		Node node = findNode(d.getHead(), s);
		LinkedList<String> history = sortMap(hist);
		
		if(node == null){
			return pred;
		} else {
			node = node.getChild();
		}

		//System.out.println("predicting using: "+ history);
		int i = 0;
		for(String temp : history){
			if(temp.indexOf(s) == 0){
				pred[i] = temp;
				i++;
			}
		}

		while(i < 5){
			Node temp = node;
			while(true){
				if(temp.hasNext()){
					temp.setSlice(slice);
					nodeStack.push(temp);
				}

				if(temp.getChar() != '^'){
					slice += temp.getChar();
					temp = temp.getChild();
				} else {
					break;
				}
				
			}
			
			String tempWord = s + slice;
			if(!(history.contains(tempWord))){
				pred[i] = tempWord;
			} else {
				i--;
			}

			if(nodeStack.empty()){
				break;
			}
			node = nodeStack.pop();
			slice = node.getSlice();
			node = node.getNext();
			i++;
		}
		
		return pred;
	}

	public static void main(String[] args){

		DLB dlb = writeDLB();
		HashMap<String, Integer> history = readHistory();
		HashMap<String, Integer> newHistory = promptUser(dlb, history);
		writeHistory(newHistory);
	}
}