package edu.jhu.coe.util;

import edu.jhu.coe.io.PennTreebankReader;
import edu.jhu.coe.PCFGLA.Binarization;
import edu.jhu.coe.PCFGLA.Corpus;
import edu.jhu.coe.PCFGLA.Corpus.TreeBankType;
import edu.jhu.coe.PCFGLA.Option;
import edu.jhu.coe.PCFGLA.OptionParser;
import edu.jhu.coe.syntax.StateSet;
import edu.jhu.coe.syntax.Tree;
import edu.jhu.coe.syntax.Trees;
import edu.jhu.coe.util.Numberer;
import edu.jhu.coe.util.CommandLineUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Reads in a corpus and prints out (STDOUT) a binarized version of it.
 *
 * @author Frank Ferraro
 */
public class BinarizeCorpus {
    public static int HORIZONTAL_MARKOVIZATION = 1; 
    public static int VERTICAL_MARKOVIZATION = 2;
    public static Random RANDOM = new Random(0);
  
    public static class Options {
	@Option(name = "-path", usage = "Path to Corpus (Default: null)")
	    public String path = null;
	@Option(name = "-in", usage = "Single training file (Default: null)")
	    public String trainList = null;
	@Option(name = "-out", usage = "Output file (Default: null) :: required")
	    public String out = null;
	@Option(name = "-treebank", usage = "Language:  WSJ, CHNINESE, GERMAN, CONLL, SINGLEFILE (Default: ENGLISH)")
	    public TreeBankType treebank = TreeBankType.WSJ;
	@Option(name = "-b", usage = "LEFT/RIGHT Binarization (Default: RIGHT)")
	    public Binarization binarization = Binarization.RIGHT;
	@Option(name = "-hor", usage = "Horizontal Markovization (Default: 0)")
	    public int horizontalMarkovization = 0;
	@Option(name = "-ver", usage = "Vertical Markovization (Default: 1)")
	    public int verticalMarkovization = 1;
	@Option(name = "-lowercase", usage = "Lowercase all words in the treebank")
	    public boolean lowercase = false;
	@Option(name = "-r", usage = "Level of Randomness at init (Default: 1)")
	    public double randomization = 1.0;
    }


  
    public static void main(String[] args) {
	OptionParser optParser = new OptionParser(Options.class);
	Options opts = (Options) optParser.parse(args, true);
	// provide feedback on command-line arguments
	System.err.println("Calling with " + optParser.getPassedInOptions());

	if(opts.path!=null && (opts.trainList !=null )){
	    System.err.println("Please specify either a single path or a single file");
	    System.exit(2);
	}

	if(opts.out==null){
	    System.err.println("Please specify an output file");
	    System.exit(2);
	}
	HORIZONTAL_MARKOVIZATION = opts.horizontalMarkovization;
	VERTICAL_MARKOVIZATION = opts.verticalMarkovization;    
	int maxSentenceLength = 10000;
	Binarization binarization = opts.binarization; 
	double randomness = opts.randomization;    
	RANDOM = new Random(8);
	boolean VERBOSE = false;

	boolean manualAnnotation = false;

	Double trainingFractionToKeep = 1.0;

	String path;
        String trainList;
        String validationList;
	int skipSection = -1;

	Corpus corpus;
	if(opts.path!=null){
	    path = opts.path;
	    System.out.println("Loading trees from "+path+" and using language "+opts.treebank);

	    corpus = new Corpus(path,opts.treebank,trainingFractionToKeep,false, skipSection);
	} else{        
	    trainList = opts.trainList;
	    System.err.println("Loading trees from " + trainList);

	    corpus = new Corpus(trainList, null);
	}

	System.err.println("Using horizontal="+HORIZONTAL_MARKOVIZATION+" and vertical="+VERTICAL_MARKOVIZATION+" markovization.");
	System.err.println("Using "+ binarization.name() + " binarization.");// and "+annotateString+".");    


	List<Tree<String>> trainTrees = Corpus.binarizeAndFilterTrees(corpus.getTrainTrees(), 
								      VERTICAL_MARKOVIZATION,
								      HORIZONTAL_MARKOVIZATION, 
								      maxSentenceLength, 
								      binarization, 
								      manualAnnotation,
								      VERBOSE);
	if(opts.path ==null){
	    try{
		// Create file 
		FileWriter fstream = new FileWriter(opts.out);
		BufferedWriter out = new BufferedWriter(fstream);
		for(Tree<String> t : trainTrees){
		    out.write(t.toString()+"\n");
		}
		out.close();
	    }catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());
	    }
	} else {
	    try{
		// Create file 
		FileWriter fstream = new FileWriter(opts.out+".0200-2199");
		BufferedWriter out = new BufferedWriter(fstream);
		for(Tree<String> t : trainTrees){
		    out.write(t.toString()+"\n");
		}
		out.close();

		List<Tree<String>> valTrees = Corpus.binarizeAndFilterTrees(corpus.getValidationTrees(),VERTICAL_MARKOVIZATION,HORIZONTAL_MARKOVIZATION,maxSentenceLength, binarization,manualAnnotation,VERBOSE);
		fstream = new FileWriter(opts.out+".2100-2199");
		out = new BufferedWriter(fstream);
		for(Tree<String> t : valTrees){
		    out.write(t.toString()+"\n");
		}
		out.close();
		valTrees = null;

		List<Tree<String>> devTestTrees = Corpus.binarizeAndFilterTrees(corpus.getDevTestingTrees(),VERTICAL_MARKOVIZATION,HORIZONTAL_MARKOVIZATION,maxSentenceLength, binarization,manualAnnotation,VERBOSE);
		fstream = new FileWriter(opts.out+".2200-2299");
		out = new BufferedWriter(fstream);
		for(Tree<String> t : devTestTrees){
		    out.write(t.toString()+"\n");
		}
		out.close();
		devTestTrees = null;

		List<Tree<String>> finalTestTrees = Corpus.binarizeAndFilterTrees(corpus.getFinalTestingTrees(),VERTICAL_MARKOVIZATION,HORIZONTAL_MARKOVIZATION,maxSentenceLength, binarization,manualAnnotation,VERBOSE);
		fstream = new FileWriter(opts.out+".2300-2399");
		out = new BufferedWriter(fstream);
		for(Tree<String> t : finalTestTrees){
		    out.write(t.toString()+"\n");
		}
		out.close();
	    }catch (Exception e){//Catch exception if any
		System.err.println("Error: " + e.getMessage());
	    }

	}
    }
}