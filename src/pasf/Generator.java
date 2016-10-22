package pasf;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Generator {
	
	public void run(Properties config) throws IOException
	{
		FreeMarkerHelper.Init(config.getProperty("templatedir"));
		String outdir = config.getProperty("outputdir", ".");
		Map<String, Object> map = this.getMap(config);
		Map<String, String> files = this.getFilenames(config, map);
		for (String tname : files.keySet())
		{
			Path ename = FileSystems.getDefault().getPath(outdir, files.get(tname));
			System.out.println("Begin generate file " + ename.toString() + " from template " + tname);
			PrintWriter writer = new PrintWriter(ename.toFile());
			FreeMarkerHelper.templateToWriter(tname, map, writer);
		}
	}
	
	private Map<String, Object> getMap(Properties config)
	{
		System.out.println("Generate map object for freemarker");
		Map<String, Object> map = new HashMap<String, Object>();
		for (Object key : config.keySet())
		{
			String skey = (String) key;
			map.put(skey, config.getProperty(skey));
			if (skey.equals("tablename"))
			{
				String value = config.getProperty(skey);
				map.put("tablenamePascal", value);
				map.put("tablenameLower", value.toLowerCase());
				map.put("tablenameUpper", value.toUpperCase());
				map.put("tablenameCamel", pascalToCamel(value));
				map.put("module", value.toLowerCase());
			}
		}
		return map;
	}
	
	private String pascalToCamel(String value)
	{
		return value.substring(0, 1).toLowerCase() + value.substring(1);
	}
	
	private Map<String, String> getFilenames(Properties config, Map<String, Object> mapObj)
	{
		System.out.println("Get template filenames and export filenames");
		Map<String, String> map = new HashMap<String, String>();
		String[] tempNames = config.getProperty("templatefile").split(";");
		String[] expNames = config.getProperty("exportfile").split(";");
		for (int i = 0; i < tempNames.length; i++)
		{
			String tname = tempNames[i];
			String ename = expNames[i];
			for (String key : mapObj.keySet())
			{
				ename = ename.replaceAll("\\$\\{" + key + "\\}", mapObj.get(key).toString());
			}
			System.out.println("Get template name " + tname + " and export name " + ename);
			map.put(tname, ename);
		}
		return map;
	}
}
