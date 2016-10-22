package pasf;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Program {
	
	private Properties config;
	
	public void loadConfig(String path) throws IOException
	{
		System.out.println("Loading config properties from file: " + path);
		config = new Properties();
		config.load(new FileReader(path));
		for (Object key : config.keySet()) {
			String skey = (String) key;
			System.out.println(skey + "=" + config.getProperty(skey));
		}
		System.out.println("Finish loading config");
	}
	
	// To run this:
	// java -jar pasfgen.jar config.txt
	public static void main(String[] args) throws IOException {
		Program program = new Program();
		program.loadConfig(args[0]);
		Generator gen = new Generator();
		gen.run(program.config);
	}

}
