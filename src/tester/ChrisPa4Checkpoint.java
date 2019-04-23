package tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/* Automated regression tester for Checkpoint 3 tests
 * Created by Max Beckman-Harned
 * modified by jfp to improve robustness and accommodate different project organizations
 * Put your tests in "tests/pa3_tests" folder in your Eclipse workspace directory
 * If you preface your error messages / exceptions with *** then they will 
 * be displayed in the regression tester output when they appear during processing
 */

public class ChrisPa4Checkpoint {
        
	private static String projDir;
	private static File classPath;
	private static File testDir;
	
	private static String currentError;
	
        
    public static void main(String[] args) throws IOException, InterruptedException {

		// project directory for miniJava and tester
		projDir = System.getProperty("user.dir");
		System.out.println("Run pa3_tests on miniJava compiler in " + projDir);
		
		// compensate for project organization 
		classPath = new File(projDir + "/bin");
		if (!classPath.isDirectory()) {
			// no bin directory in project, assume projDir is root for class files
			classPath = new File(projDir);
		}

		// miniJava compiler mainclass present ?
		if (! new File(classPath + "/miniJava/Compiler.class").exists()) {
			System.out.println("No miniJava Compiler.class found (has it been compiled?) - exiting");
			return;
		}

		// test directory present ?
		testDir = (new File(projDir + "/../tests/chris_tests").getCanonicalFile());
		if (! testDir.isDirectory()) {
			System.out.println("pa3_tests directory not found - exiting!");
			return;
		}

		System.out.println("Running tests from directory " + testDir);
        int failures = 0;
        
        File[] fileList = testDir.listFiles();
		Arrays.sort(fileList);
		
        for (File x : fileList) {
        	if (x.getName().startsWith("TestIndex")) {
        		continue;
        	}
        	

        	
            if (x.getName().endsWith("out") || x.getName().startsWith(".") 
                || x.getName().endsWith("mJAM") || x.getName().endsWith("asm"))
                   continue;
            int returnCode = runTest(x);
            
            if (x.getName().startsWith("pass")) {
            	if (returnCode == 0) {
            		// correct
            	} else {
            		// incorrect
            		System.err.println(x.getName() + " failed, expected return code of 0, got " + returnCode);
            		failures += 1;
            	}
            } else if (x.getName().startsWith("fail")) {
            	if (returnCode == 0) {
            		// incorrect
            		System.err.println(x.getName() + " failed, expected return code of 0, got " + returnCode);
            		failures += 1;
            	} else {
            		// correct
            	}
            }
            
        }
        System.out.println("============================================");
        System.out.println(failures + " incorrect results in all.");     
    }
        
    private static int runTest(File x) throws IOException, InterruptedException {
    	System.out.println("============================================");
    	System.out.println("Output of test: " + x.getName());
        String testPath = x.getPath();
        
        
        ProcessBuilder pb = new ProcessBuilder("java", "miniJava.Compiler", testPath);
        pb.directory(classPath);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        
        processOurStream(p.getInputStream());
        if (!p.waitFor(5, TimeUnit.SECONDS)) {
			 //hung test
			p.destroy();
			return 130;  // interrupted
		}
        return p.exitValue();
        //return 0;
    }
        
        
    public static void processOurStream(InputStream stream) {
        Scanner scan = new Scanner(stream);
        StringBuilder str = new StringBuilder();
        
        boolean changedError = false;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.length() > 0 && line.charAt(0) == '>') {
            	line = line.substring(4);
            	
            }
            //System.out.println(line);
            str.append(line + "\n");
        }
        scan.close();
        
        System.out.println(str.toString());
    }
    
    public static void processStream(InputStream stream) {
    	Scanner scan = new Scanner(stream);
        //StringBuilder str = new StringBuilder();
        
        boolean changedError = false;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            System.out.println(line);
        }
        scan.close();
        
        //System.out.println(str.toString());
    }
    
    private static void print(String status,InputStream input) throws IOException{
    	BufferedReader in = new BufferedReader(new InputStreamReader(input));
    	System.out.println("************* "+status+"***********************");
    	String line = null;
    	while((line = in.readLine()) != null ){
    	System.out.println(line);
    	}
    	in.close();
    	}
    
}