package com.jgaap.experiments;
/**
 * Given a file path to a directory, loops through all files and converts
 * all valid JGAAP docs into an ArrayList of LogData objects stored along
 * with the name of the grouping of logs, either the dir name or given name.
 *
 * @author Derek S. Prijatelj
 *
 * TODO The Logs are NOT in order as expected. they go by 0-9 of first, but then
 * if it has any other numbers following it, they are processed first. So 10
 * comes right after 1, and before the teens and all that before 2. This is due
 * to how the files are read in by the machine.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.IndexOutOfBoundsException;
import java.text.Collator;

class MultiLog {
	public String name;
	public ArrayList<LogData> logs = new ArrayList<>();

	public MultiLog() {
	}

	public MultiLog(String pathToDir) {
		this(pathToDir, "");
	}

	public MultiLog(String pathToDir, String name) {
		//this(pathToDir, name, false);
	    try{
	        File mlDir = new File(pathToDir);
	        if (!mlDir.isDirectory()) {
	            throw new NotADirectory();
	        }
	        if (name.isEmpty()) {
	            this.name = mlDir.getName().trim();
	        } else {
	            this.name = name.trim();
	        }
	        
	        ArrayList <File> files = new ArrayList<>();
	        getFilesRecursive(mlDir, files);
	        
	        for (File file:files){
	            logs.add(new LogData(file));
	        }
	        
	        //Collections.sort(logs);
        } catch(NotADirectory | InvalidLogFileType | InvalidLogStructure
                | ResultContainsNaN e){
            e.printStackTrace();
        }
	}

	public void print() {
		System.out.println("\nMultiLog: " + name);
		for (int i = 0; i < logs.size(); i++) {
			logs.get(i).print();
			System.out.println();
		}
	}

   /**                                                                         
     * Exports a csv table of MLog's methods individual binary success/failure
     * authorship attribution for each test document.
     * 
     * TODO Need to ensure the data represented on in the CSV is correct, as in
     * the data 
     */
    public void exportCSV() {
        try {
            File csvFile;
            if (name.isEmpty()){
                csvFile = new File("UnnamedMultiLog_binVotes.csv");
            } else {
                csvFile = new File(name + "_binVotes.csv");
            }
            PrintWriter pw = new PrintWriter(csvFile);

            int testsSize = logs.get(0).tests.size(); // #methods
            int resultsSize = logs.get(0).tests.get(0).results.size(); // #testdocs
            int methodCount = 0;
            String logName;
            HashSet<String> logSuffix = new HashSet<>();
            logSuffix.add(logs.get(0).tests.get(0).questionedDoc);
            
            
            // print header row (Methods) based on first log's methods
            pw.print(name + ",");
            for (int i = 0; i < logs.size(); i++) {
                logName = logs.get(i).tests.get(0).questionedDoc;
                
                if (methodCount == 0 && logSuffix.add(logName)){
                    
                    System.out.println("logSuffix " + logSuffix.toString());
                    
                    methodCount = i;
                    break;
                }
                
                pw.print(logs.get(i).printMethod());
                if (i < logs.size() - 1)
                    pw.print(",");
            }
            pw.println();
            
            System.out.println("logs = " + logs.size());
            System.out.println("methodCount = " + methodCount);
            System.out.println("tests = " + testsSize);
            System.out.println("results = " + resultsSize);
            
            String qDoci, qDocCurrent, methodi,methodCurrent;
            
            // Print by row.
            // TODO Confirm this step size by methodCount is correct
            for (int currentLog = 0; currentLog < logs.size();
                    currentLog += methodCount){
                logName = logs.get(currentLog).name;
               
                for (int j = 0; j < testsSize; j++) { // rows
                    
                    // print row j's column header:
                    try {
                        pw.print(logs.get(currentLog).tests.get(j).questionedDoc + ",");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Warning: Current dir "
                                + logName + " has " + logs.get(currentLog).tests.size()
                                + " files (methods) and is not equal to first dir "
                                + logs.get(0).name + " which has " + testsSize + "!");
                        continue;
                    }
                    
                    qDocCurrent = logs.get(currentLog).tests.get(j).questionedDoc;
                    methodCurrent = logs.get(currentLog).printMethod();
                    
                    // Print row's (testDoc's) binary results across methods
                    for (int i = currentLog; i < currentLog + methodCount && i < logs.size(); i++) {
                        qDoci = logs.get(i).tests.get(j).questionedDoc;
                        methodi = logs.get(i).printMethod();
                        
                        // Error check for reliable data representation in table
                        if (!qDocCurrent.equals(qDoci)){
                            System.err.println("Error: questionedDocs do not "
                                    + "match!\n"
                                    + qDocCurrent + " != "
                                    + qDoci
                                    );
                        }
                        if (!methodCurrent.equals(methodi)){
                            System.err.println("Error: methods of docs do not "
                                    + "match!\n"
                                    + methodCurrent + "\n!=\n"
                                    + methodi + "\n"
                                    );
                        }
                        
                        pw.print(isCorrect(i, j)? '1':'0');
                        if (i < currentLog + methodCount - 1)
                            pw.print(",");
                        else {
                            System.out.println();
                        }
                    }
                    pw.println();
                }
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Checks if correct author is first in results of test in specific log
     * 
     * @param i log number
     * @param t test number
     * @return true if the correct author to questioned doc is ranked first
     */
    private boolean isCorrect(int i, int t){
        String s1[] = logs.get(i).tests.get(t).author.trim().split(" ");
        String s2 = logs.get(i).tests.get(t).results.get(0).author;
        if (s1.length <= 1) {
            //if (!defectFiles.contains(ml.logs.get(i).name))
            //   defectFiles.add(ml.logs.get(i).name);
            return false;
        }

        if (s2.contains(" ")) {
            s2 = (s2.split(" "))[0];
        }

        return s1[1].equals(s2) && logs.get(i).tests.get(t).results.get(0).rank
                != logs.get(i).tests.get(t).results.get(1).rank;
    }
    
    private static void getFilesRecursive(File pFile, ArrayList<File> list) {
        //System.out.println(pFile.getName()+pFile.listFiles().length);
        for (File files : pFile.listFiles()) {
            if (files.isDirectory()) {
                getFilesRecursive(files, list);
            } else if (files.getName().substring(
                    files.getName().lastIndexOf('.') ).equals(".txt")){
                list.add(files);
            }
        }
    }

    public static void main(String args[]){
        MultiLog ml = new MultiLog("../../SciFi", "Sci");

        ml.exportCSV();
        
        ml = new MultiLog("../../Mystery", "Mys");

        ml.exportCSV();
    }
}
