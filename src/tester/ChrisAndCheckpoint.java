package tester;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

public class ChrisAndCheckpoint {
        
	private static String projDir;
	private static File classPath;
	
	private static File prinsDir;
	private static File chrisDir;
        
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
		prinsDir = (new File(projDir + "/../tests/pa3_tests").getCanonicalFile());
		chrisDir = (new File(projDir + "/../tests/chris_tests").getCanonicalFile());
		if (! (prinsDir.isDirectory() && chrisDir.isDirectory()  )) {
			System.out.println("pa3_tests or chris_tests directory not found - exiting!");
			return;
		}

		//System.out.println("Running tests from directory " + testDir);
        int failures = 0;
        
        File[] prinsFileList = prinsDir.listFiles();
        File[] chrisFileList = chrisDir.listFiles();
        Arrays.sort(prinsFileList);
        Arrays.sort(chrisFileList);
        
        File[] fileList = new File[prinsFileList.length + chrisFileList.length];
        int counter = 0;
        for (File x : prinsFileList) {
        	fileList[counter] = x;
        	counter++;
        }
        
        for (File x: chrisFileList) {
        	fileList[counter] = x;
        	counter++;
        }
        
        
		
		
        for (File x : fileList) {
            if (x.getName().endsWith("out") || x.getName().startsWith(".") 
                || x.getName().endsWith("mJAM") || x.getName().endsWith("asm"))
                   continue;
            int returnCode = runTest(x); 
            if (returnCode == 1) {
				System.err.println("### miniJava Compiler fails while processing test " + x.getName());
				failures++;
				continue;
			}
			if (returnCode == 130) {
				System.err.println("### miniJava Compiler hangs on test " + x.getName());
				failures++;
				continue;
			}
            if (x.getName().indexOf("pass") != -1) {
                if (returnCode == 0) {
                    System.out.println(x.getName() + " passed successfully!");
                }
                else {
                    failures++;
                    System.err.println(x.getName()  + " did not pass!");
                }
            } else {
                if (returnCode == 4)
                    System.out.println(x.getName() + " failed successfully!");
                else {
                    System.err.println(x.getName() + " failed to detect the error!");
                    failures++;
                }
            }
            System.out.println("=========================================================");
        }
        System.out.println(failures + " incorrect results in all.");     
    }
        
    private static int runTest(File x) throws IOException, InterruptedException {

        String testPath = x.getPath();
        ProcessBuilder pb = new ProcessBuilder("java", "miniJava.Compiler", testPath);
        pb.directory(classPath);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        processStream(p.getInputStream());
        if (!p.waitFor(5, TimeUnit.SECONDS)) {
			// hung test
			p.destroy();
			return 130;  // interrupted
		}
        return p.exitValue();
    }
        
        
    public static void processStream(InputStream stream) {
        Scanner scan = new Scanner(stream);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.startsWith("*** "))
                System.out.println(line);
            if (line.startsWith("ERROR")) {
                System.out.println(line);
            }
        }
        scan.close();
    }
}