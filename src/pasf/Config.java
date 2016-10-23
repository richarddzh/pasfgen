package pasf;

import java.util.Properties;

public class Config {
	
	public Properties properties;
	
	public Config(Properties p)
	{
		this.properties = p;
	}
	
	public String getEncoding()
	{
		return this.properties.getProperty("encoding", "UTF-8");
	}
}
