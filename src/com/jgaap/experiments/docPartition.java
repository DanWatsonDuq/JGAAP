package com.jgaap.experiments;

import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;


/**
 * Separates the given document into specified smaller parts, making multiple
 * smaller documents from the original. Assumes .txt
 * 
 * @author Derek S. Prijatelj
 */
public class docPartition{
    
    /**
     * Strict partition of documents. Every partition has exactly the words
     * specified, otherwise is discarded from partition set.
     *
     * @param wordCount number of words to be in each partition
     * @param dirOfDocs path to directory of documents to be partitioned
     * @param exportPath relative path to export documents
     */
    protected static void strict(int wordCount, String dirOfDocs,
            String exportPath){
        partition(wordCount, dirOfDocs, exportPath, true);
    }

    /**
     * Strict partition of documents. Every partition has exactly the words
     * specified, otherwise is discarded from partition set.
     *
     * @param wordCount number of words to be in each partition
     * @param dirOfDocs path to directory of documents to be partitioned
     * @param exportPath relative path to export documents
     */
    protected static void lenient(int wordCount, String dirOfDocs,
            String exportPath){
        partition(wordCount, dirOfDocs, exportPath, false);
    }

    /**
     * Find and access directory of documents
     * 
     * @param dirOfDocs relative path to directory of documents for partitioning
     * @return File object of directory of documents, null if path is not to a
     * directory.
     */
    private static File accessDir(String dirOfDocs){
        try{
            File dir = new File(dirOfDocs);
            
            if (!dir.isDirectory()){
                throw new NotADirectory(
                    "The submitted path is not a directory. Must be given a "
                    + "directory containing the plain text documents to be "
                    + "partitioned."
                    );
            } else {
                return dir;
            }
        } catch (NotADirectory e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Partitions the documents in the provided relative file path based on the
     * indicated word count per partiton and stores the resulting partitions
     * into the desired relative file export path. Assumes all separate files to
     * be partitioned are in their own directories by author.
     * 
     * @param wordCount number of words to be in each partition
     * @param dirOfDocs path to directory of documents to be partitioned
     * @param exportPath path to export documents
     * @param strict if true, strict partitioning occurs where every partition
     * has exactly the words specified and all partitions that have less words
     * are discarded. If false, documents are partitioned the same, but if a
     * partition has less words than indicated it will still be saved.
     */
    private static void partition(int wordCount, String dirOfDocs,
            String exportPath, boolean strict){
        File dir = accessDir(dirOfDocs);
        int parts;
        String fileName, fName;
        HashMap<String, Integer> numOfParts = new HashMap<>();
        File load = null;

        // loop through all files in dir, and execute partitionFile() if .txt
        for (final File file : dir.listFiles()){
            fileName = file.getName();

            if (fileName.equals("load.csv")){
                load = new File(file.getPath());
            } else if (file.isDirectory()){
                System.out.println("Working in dir: " + file.getName());
                for (final File f: file.listFiles()){
                    fName = f.getName();

                    System.out.println("  on file: " + fName);
                    
                    if (!f.isFile() || !fName.endsWith(".txt")){
                        continue;
                    } else {
                        parts = partitionFile(wordCount, f, exportPath, strict);
                        numOfParts.put(fName, parts);
                    }
                }
            }
        }
        
        System.out.println("Now createLoadCSV() . . .");
        createLoadCSV(load, numOfParts);
        System.out.println("newLoad.csv created!");
    }

    /**
     * partitions the given plain text file into multipl plain text files with
     * at maximum as many words in the file as specified
     *
     * @param wordCount max number of words in plain text file
     * @param file the file being partitioned
     * @param exportPath relative path to export the partitioned files
     * @param strict if strict, only files with exactly the specified amount of
     * words will be created.
     *
     * @return returns the number of partitions from file
     */
    private static int partitionFile(int wordCount, File file,
            String exportPath, boolean strict){

        File subDir = createSubDir(file, exportPath);
        int numWords = 0, subDocNum = 1;
        File subDoc = new File(subDir.getPath() + "_" + subDocNum + ".txt");
        boolean makingWord = false;
        int cint;
        char c;

        try {
            FileReader input = new FileReader(file);
            BufferedReader buffRead = new BufferedReader(input);
            PrintWriter writer = new PrintWriter(subDoc, "UTF-8");

            while((cint = buffRead.read()) != -1){
                c = (char)cint;
                writer.write(c);


                if (isAlphaNum(c) && !makingWord){
                    makingWord = true;
                } else if (!isAlphaNum(c) && makingWord){
                    numWords++;
                    makingWord = false;
                }

                if (numWords >= wordCount){
                    writer.close();
                    subDocNum++;
                    subDoc = new File(subDir.getPath() + "_" + subDocNum
                        + ".txt");
                    writer = new PrintWriter(subDoc, "UTF-8");
                    numWords = 0;
                }
            }
            
            writer.close();
            buffRead.close();
            input.close();

            if (numWords != 0 && strict){
                subDoc.delete();
                subDocNum--;
            } 
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("    File partitioned into " + subDocNum + " parts");

        return subDocNum;
    }

    // What about hyphenated words? and other special cases?
    private static boolean isAlphaNum(char c){
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
            ||c >= '0' && c <= '9';
    }

    /**
     * Creates the sub directory and it's parent directory, if they do not
     * already exist.
     */
    private static File createSubDir(File file, String exportPath){
        File export = new File(exportPath);

        if (!export.isDirectory()){
            export.mkdirs();
        }
        
        String name = file.getName();
        File subDir = new File(export, name.substring(0, name.length()-4));

        if (!subDir.isDirectory()){
            file.mkdirs();
        }

        return subDir;
    }

    /**
     * Creates newLoad.csv file for JGAAP to use for accessing all files in
     * appropriate directories. May be only our situation specific, and not
     * general due to how it parses the orignal load.csv
     *
     * @param load file object of original load.csv
     * @param map of partitioned parent file with its number of partitions
     */
    private static void createLoadCSV(File load,
            HashMap<String, Integer> numOfParts){
        if (load == null){
            // ignore or create load.csv from scratch
            return;
        } 

        String name = load.getPath();
        File tmp = new File(
            name.substring(0, name.lastIndexOf("\\")+1) + "newLoad.csv"
            );

        try{
            FileReader in = new FileReader(load);
            BufferedReader bfr = new BufferedReader(in);

            PrintWriter out = new PrintWriter(tmp, "UTF-8");
            String line, path;
            String[] str;
            Integer parts;

            while((line = bfr.readLine()) != null){
                str = line.split(",");
                
                parts = numOfParts.get(str[1]);
                
                if (parts != null){
                    path = str[1].substring(0, str[1].length()-4);
                    
                    for (int i = 1; i < parts+1; i++){
                        str[1] = path + "\\" + path + "_" + i + ".txt";
                        
                        out.print(str[0] + "," + str[1] + "," + str[2]);

                        if (str[2].equals("")){
                            out.println(",");
                        } else {
                            out.println("");
                        }
                    }
                } else {
                    out.println(line);
                }
            }

            out.close();
            bfr.close();
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Command Line Interface
     */
    protected static void cli(String[] args){
        if (args.length == 4){
            if (args[0].equals("strict"))
                strict(Integer.parseInt(args[1]), args[2], args[3]);
            else
                lenient(Integer.parseInt(args[1]), args[2], args[3]);
        } else {
            System.out.println("Help: [partition_type] [number of words per partition] [directory of docs] [export directory]");
        }

    }

    public static void main(String[] args){
       cli(args); 
    }
}
