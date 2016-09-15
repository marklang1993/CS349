
import java.lang.System;
import java.io.PrintWriter;

class Check {

	public static void main(String[] args) {
		try {
			PrintWriter writer = new PrintWriter("results.txt", "UTF-8");
			writer.println("Java: " + System.getProperty("java.runtime.name"));
			writer.println("Java Version: " + System.getProperty("java.version"));
			writer.println("OS Name: " + System.getProperty("os.name"));
			writer.println("OS Version: " + System.getProperty("os.version"));
			writer.println("OS Architecture: " + System.getProperty("os.arch"));
			writer.println("Total Memory (MB): " + Runtime.getRuntime().totalMemory() / (1024*1024));
			writer.println("Max Memory (MB): " + Runtime.getRuntime().maxMemory() / (1024*1024)); 
			writer.close();			
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

}
