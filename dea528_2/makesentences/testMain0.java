package makesentences;
import java.io.*;
import java.util.*;

public class testMain0 
{
	public static TreeSet<String> ts = new TreeSet<String>(); // holds non-duplicate dictionary words
	public static ArrayList<String> outputSentences = new ArrayList<String>(); // holds valid final output sentence matchings
	public static ArrayList<String> Dictionary = new ArrayList<String>(); // holds non-duplicate dictionary entries that are contained in the input sentence
	public static Stack<String> stack = new Stack<String>(); // holds partially completed sentence at all times until remainingString = ""
	
	public static void main(String[] args) 
	{
		// Parse Input
		File file = new File(args[0]);
		
		// Access file for reading using the Scanner Class
		Scanner scanner = null;
		try {scanner = new Scanner(file);} 
		catch (FileNotFoundException e) {System.out.println("Input File Not Found");}
		
		// Add dictionary words to a list w/o duplicate words & removing unnecessary whitespace
		int DictionarySize = Integer.parseInt(scanner.nextLine().replaceAll("\\s+", ""));
		for(int i = 0; i < DictionarySize; i++)
		{
			String inString = scanner.nextLine();
			ts.add(inString);
		}
		
		// Get the sentence we are trying to match our dictionary words to
		String sentence = scanner.nextLine();
		Iterator<String> it = ts.iterator();
		while(it.hasNext()) 
		{   // only add words to the dictionary if the sentence contains those dictionary words
			Object inSentence = it.next();
			if(sentence.contains((String)inSentence))
			{
				Dictionary.add((String)inSentence);
			}
		}
		
//		System.out.println("----------------------------------");
		DynProg3(sentence); // Do the recursive function
		System.out.println(outputSentences.size()); // output number of sentences matched
		for(int kk = 0; kk < outputSentences.size(); kk++) 
		{	// output the valid sentences that could be constructed from using the provided dictionary
//			System.out.print(kk+1 + ": ");
			System.out.println(outputSentences.get(kk));
		}
	}

	/**
	 * Builds reconstructs the possible sentences using 
	 * given words from the dictionary
	 * @param remainingString the string that has had 
	 * matched dictionary words removed and is still left
	 * to be matched to potentially any word in the dictionary
	 * 
	 * @param stack holds all the words that have been parsed
	 * off the front of the sentence and has appropriate spaces 
	 * delimiting the words. At any point in time it holds the 
	 * valid sentence that is being constructed.
	 * 
	 * when terminating a sentence it pops the last value off stack
	 * and telescopes backwards looking for any other potential matches
	 * When a valid sentence is found it is first reversed and then added
	 * in the ArrayList<String> outputSentence
	 */
	@SuppressWarnings("unchecked")
	public static void DynProg3(String remainingString)
	{
		StringBuilder sb = new StringBuilder(remainingString);
		String removedPrefix;
		
		if(remainingString.length() == 0)
		{	// A valid sentence has been constructed
			Stack<String> tempStack = new Stack<String>();
			tempStack = (Stack<String>) stack.clone(); // copy stack so I don't iterate over a stack of changing size when I pop off elements
			//ArrayList<String> reversedSentence = new ArrayList<String>();
			
			// Builds the outputString from the back to the front by always
			// inserting the last String added to the stack at the beginning
			// of the StringBuilder sb
			for(int b = 0; b < stack.size(); b++)
			{	// insert elements at front of string
				sb.insert(0, tempStack.pop());
			}
			outputSentences.add(sb.toString()); // adds the final sentence to valid output sentences
			// System.out.println("");
			// System.out.print("----------------------------------");
			return;
		}
		
		// If there are still words to match in the remainingString
		// iterate over the entire dictionary looking for matches
		for(int i = 0; i < Dictionary.size(); i++)
		{
			if(remainingString.startsWith(Dictionary.get(i)))
			{   // If a prefix match is found strip off the matching prefix
				// add that prefix to the stack which holds the current valid
				// sentence (adding whitespace after the word). Then look for
				// remaining matches by calling DynProg3 again. To unmark my 
				// changes I pop off the stack and look for other possible 
				// matches so that stack always holds the current valid 
				// sentence at all times
				removedPrefix = removeFront(Dictionary.get(i), remainingString);
				stack.push(Dictionary.get(i) + " ");
				DynProg3(removedPrefix);
				stack.pop();
			}
		}
	}
	
	/**
	 * Removes the Prefix from the remaining senten String and
	 * returns the senten with the prefix removed
	 * @param pattern is the matching prefix of the senten string
	 * @param senten is the string with unmatched dictionary words that
	 * 		  still needs the whitespace to be added to delimit the words
	 * @return senten with prefix removed
	 */
	public static String removeFront(String pattern, String senten)
	{
		return senten.substring(pattern.length());
	}
/*
	public static ArrayList<String> DynProg2(int i, String s)
	{
		StringBuilder sb = new StringBuilder(s);
		ArrayList<String> p = new ArrayList<String>();
		
		int j;
		for(j = 0; j < s.length(); j++)
		{
			if(Dictionary.get(i).equals(sb.substring(0, j))) // my sb.substring == my dictionary word
			{
				if(sb.substring(0,j).length() == s.length())
				{
					p.add(s);
					return p;
				}
				break;
			}
		}
		
		p.addAll(DynProg2(i, s.substring(0,j)));
		p.addAll(DynProg2(i, s.substring(j,s.length())));
		return p;
	}
	*/
}
