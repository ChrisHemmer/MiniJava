package tester;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/* Automated regression tester for Checkpoint 2 tests
 * Created by Max Beckman-Harned and jfp
 * Put your tests in "tests/pa2_tests" folder in your Eclipse workspace directory
 * If you preface your error messages / exceptions with ERROR or *** they will be 
 * displayed if they appear during processing
 */

public class ChrisCheckpoint {
	
	private static class ReturnInfo {
		int returnCode;
		String ast;
		public ReturnInfo(int _returnCode, String _ast) {
			returnCode = _returnCode;
			ast = _ast;
		}
	}
	
	private static String projDir;
	private static File classPath;
	private static File testDir;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		// project directory for miniJava and tester
		projDir = System.getProperty("user.dir");
		System.out.println("Run chris_tests on miniJava compiler in " + projDir);
		
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
			System.out.println("pa2_tests directory not found - exiting!");
			return;
		}

		
		System.out.println("Running tests from directory " + testDir);

		int failures = 0;
		File[] fileList = testDir.listFiles();
		Arrays.sort(fileList);
		for (File x : fileList) {
			if (x.getName().endsWith(".txt")) {
				continue;
			}
			if (x.getName().endsWith("out") || x.getName().startsWith("."))
				continue;
			ReturnInfo info = runTest(x); 
			try {
				String ast = info.ast;
			} catch (Exception e) {
				System.err.println("### miniJava Compiler crashed while retrieving ast on test " + x.getName());
				failures++;
				continue;
			}
			int returnCode = info.returnCode;
                        if (returnCode == 1) {
				System.err.println("### miniJava Compiler failed while processing test " + x.getName());
				failures++;
				continue;
			}
			if (returnCode == 130) {
				System.err.println("### miniJava Compiler hangs on test " + x.getName());
				failures++;
				continue;
			}
			
			
			if (x.getName().indexOf("pass") != -1) {
				// Test is supposed to succeed
				if (returnCode == 0) {
					// Success
					System.out.println(x.getName() + " parsed succesfully");
				}
				else {
					// Fail
					failures++;
					System.err.println(x.getName()
							+ " failed to be parsed!");
				}
			} else {
				// Test is supposed to fail
				if (returnCode == 4)
					System.out.println(x.getName() + " failed successfully!");
				else {
					System.err.println(x.getName() + " did not fail properly!");
					failures++;
				}
			}
		}
		System.out.println(failures + " failures in all.");	
	}
	
	private static ReturnInfo runTest(File x) throws IOException, InterruptedException {

		String testPath = x.getPath();
		ProcessBuilder pb = new ProcessBuilder("java", "miniJava.Compiler", testPath);
		pb.directory(classPath);
		pb.redirectErrorStream(true);
		Process p = pb.start();

		//String ast = getAST(p.getInputStream());
		int exitValue;
		if (!p.waitFor(4, TimeUnit.SECONDS)) {
			// hung test
			p.destroy();
			exitValue = 130;  // interrupted
		}
		else {
			exitValue = p.exitValue();
		}
		return new ReturnInfo(exitValue, "");
	}

}
