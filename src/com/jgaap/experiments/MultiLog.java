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
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.IndexOutOfBoundsException;

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
	            this.name = mlDir.getName();
	        } else {
	            this.name = name;
	        }
	        
	        ArrayList <File> files = new ArrayList<>();
	        getFilesRecursive(mlDir, files);
	        
	        for (File file:files){
	            logs.add(new LogData(file));
	        }
        } catch(NotADirectory | InvalidLogFileType | InvalidLogStructure
                | ResultContainsNaN e){
            e.printStackTrace();
        }
	}

	/**
	 * Constructor creates a MultiLog from a directory of log files with each
	 * log a different method, or from a directory of directories of log files,
	 * where each directoy signifies a new method.
	 *
	 * @param pathToDir String representation of file path to Multi Log Dir
	 * @param name String name of MultiLog, default name = Multi Log Dir
	 * @param byDir Boolean switch: True: Method change by directories
     *
     * TODO May want to edit LogData to have a constructor that
     * accepts File
	 */
	public MultiLog(String pathToDir, String name, boolean byDir) {
		try {
			File mlDir = new File(pathToDir);
			if (!mlDir.isDirectory()) {
				throw new NotADirectory();
			}
			if (name.isEmpty()) {
				this.name = mlDir.getName();
			} else {
				this.name = name;
			}

			if (byDir) {
				for (final File file : mlDir.listFiles()) {
					try {
						// TODO Need to ensure I do not add nulls at all to logs
						logs.add(new LogData(file.getPath(), true));
					} catch (InvalidLogFileType | InvalidLogStructure | NotADirectory e) {
						continue;
					} catch (ResultContainsNaN e) {
						e.printStackTrace();
						continue;
						/*
						 * TODO by catching this error, do I immediately stop
						 * the creation of that log? If so good, otherwise, need
						 * to delete or overwrite that log form logs.
						 */
					}
				}
			} else {
				// System.out.println("Number of Files:" +
				// mlDir.listFiles().length);
				for (final File file : mlDir.listFiles()) {
					try {
						// TODO Need to ensure I do not add nulls at all to logs
						logs.add(new LogData(file.getPath()));
					} catch (InvalidLogFileType | InvalidLogStructure | NotADirectory e) {
						continue;
					} catch (ResultContainsNaN e) {
						e.printStackTrace();
						continue;
						/*
						 * TODO by catching this error, do I immediately stop
						 * the creation of that log? If so good, otherwise, need
						 * to delete or overwrite that log form logs.
						 */
					}
				}
			}
		}
		/*
		 * catch (IOException e) { e.printStackTrace(); }
		 */
		catch (NotADirectory e) {
			System.err.println("Error: " + pathToDir + " is not a valid " + "directory");
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
     * Exports a csv table of MLog's methods individual binary success/failure authorship
     * attribution for each test document.
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
            
            
            // print header row (Methods)
            pw.print(name + ",");
            for (int i = 0; i < logs.size(); i++) {
                logName = logs.get(i).tests.get(0).questionedDoc;
                
                if (methodCount == 0 && logSuffix.add(logName)){
                    methodCount = i;
                    break;
                }
                
                pw.print(logs.get(i).printMethod());
                if (i < logs.size() - 1)
                    pw.print(",");
            }
            pw.println();

            // print by row (test docs).
            //int currentLog = 0; // increment after j > tests.size()
            
            System.out.println("logs = " + logs.size());
            System.out.println("methodCount = " + methodCount);
            System.out.println("tests = " + testsSize);
            System.out.println("results = " + resultsSize);
            
            for (int currentLog = 0; currentLog < logs.size();
                    currentLog += methodCount){
                logName = logs.get(currentLog).name;
               
                for (int j = 0; j < testsSize; j++) { // rows

                    try {
                        pw.print(logs.get(currentLog).tests.get(j).questionedDoc + ",");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Warning: Current dir "
                                + logName + " has " + logs.get(currentLog).tests.size()
                                + " files (methods) and is not equal to first dir "
                                + logs.get(0).name + " which has " + testsSize + "!");
                        continue;
                    }

                    for (int i = 0; i < methodCount; i++) {
                        pw.print(isCorrect(i, j)? '1':'0');
                        if (i < methodCount - 1)
                            pw.print(",");
                    }
                    pw.println();
                }
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
