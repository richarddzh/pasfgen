package pasf;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Program {
	
	private Properties properties;
	private Config config;
	
	public void loadConfig(String path) throws IOException
	{
		System.out.println("Loading config properties from file: " + path);
		properties = new Properties();
		properties.load(new FileReader(path));
		for (Object key : properties.keySet()) {
			String skey = (String) key;
			System.out.println(skey + "=" + properties.getProperty(skey));
		}
		this.config = new Config(properties);
		System.out.println("Finish loading config");
	}
	
	public void run(String configPath) throws IOException
	{
		this.loadConfig(configPath);
		new Generator().run(config);
	}
	
	// To run this:
	// java -jar pasfgen.jar config.txt
	public static void main(String[] args) throws IOException {
		Program program = new Program();
		program.run(args[0]);
	}

}
