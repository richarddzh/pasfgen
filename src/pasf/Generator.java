package pasf;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Generator {
	
	public void run(Config config) throws IOException
	{
		Properties properties = config.properties;
		FreeMarkerHelper freemarker = new FreeMarkerHelper();
		freemarker.init(properties.getProperty("templatedir"), config.getEncoding());
		String outdir = properties.getProperty("outputdir", ".");
		Map<String, Object> data = this.getDataMap(config);
		Map<String, String> files = this.getFilenames(properties, data);
		for (String tname : files.keySet())
		{
			Path ename = FileSystems.getDefault().getPath(outdir, files.get(tname));
			System.out.println("Begin generate file " + ename.toString() + " from template " + tname);
			PrintWriter writer = new PrintWriter(ename.toFile());
			freemarker.templateToWriter(tname, data, writer);
		}
	}
	
	private Map<String, Object> getDataMap(Config config)
	{
		Properties properties = config.properties;
		System.out.println("Generate map object for freemarker");
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object key : properties.keySet())
		{
			String skey = (String) key;
			map.put(skey, properties.getProperty(skey));
		}
		JavaParser jparser = new JavaParser(config);
		jparser.extendDataMap(map);
		return map;
	}
	
	private Map<String, String> getFilenames(Properties properties, Map<String, Object> data)
	{
		System.out.println("Get template filenames and export filenames");
		Map<String, String> map = new HashMap<String, String>();
		String[] tempNames = properties.getProperty("templatefile").split(";");
		String[] expNames = FreeMarkerHelper.textFromString(properties.getProperty("exportfile"), data).split(";");
		for (int i = 0; i < tempNames.length; i++)
		{
			String tname = tempNames[i];
			String ename = expNames[i];
			System.out.println("Get template name " + tname + " and export name " + ename);
			map.put(tname, ename);
		}
		return map;
	}
}
