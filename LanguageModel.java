import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		String window = "";
        char c;
        In in = new In(fileName);

         // Reads just enough characters to form the first window
         for (int i = 0; i < this.windowLength; i++) {
            window += in.readChar();
         }
         
         while (!in.isEmpty())
         {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1, windowLength);
            window += c;
         }
         for(List probs : CharDataMap.values())
         {
            calculateProbabilities(probs);
         }

	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {
        int charNumber = 0;			
		for (int i = 0; i < probs.getSize(); i++) {
           charNumber += probs.listIterator(i).current.cp.count;
        }
        double prevCp =0;
        for (int i = 0; i < probs.getSize(); i++) {
            CharData currentChar = probs.listIterator(i).current.cp;
            currentChar.p = ((double)(currentChar.count)/charNumber);
            currentChar.cp = prevCp + currentChar.p;
            prevCp = currentChar.cp;
        }
        
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        calculateProbabilities(probs);
        double rnd = this.randomGenerator.nextDouble();
        for (int i = 0; i < probs.getSize(); i++) {
            if (probs.listIterator(i).current.cp.cp > rnd) {
                return probs.listIterator(i).current.cp.chr; 
            }
        }
        return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		if (windowLength > initialText.length()) {
            return initialText;
        }
        String window = initialText.substring(initialText.length() - windowLength);
        String generatedTxt = window;
        int counter = textLength + windowLength;
        //continue untill the generated text at the desired length
        while (generatedTxt.length() < counter) {
            List currentL = CharDataMap.get(window);
            //if the current window doesnt exists, we return what we have generated so far
            if (currentL == null) {
                break;
            }
            generatedTxt += getRandomChar(currentL);
            window = generatedTxt.substring(generatedTxt.length() - windowLength);
        }
        return generatedTxt;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

}
