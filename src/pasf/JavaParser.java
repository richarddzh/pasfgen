package pasf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaParser {
	
	private Map<String, Object> keys;
	private Config config;
	private Pattern pattern;
	private String[] extendSelections = new String[] { "primarykeys", "queryitems", "insertitems", "updateitems" };
	
	public JavaParser(Config config)
	{
		this.config = config;
		this.keys = new HashMap<String, Object>();
		this.pattern = Pattern.compile(config.properties.getProperty("fieldregex"));
		
		String dir = config.properties.getProperty("mybatisdir");
		String tablename = config.properties.getProperty("tablename");
		this.parseFile(FileSystems.getDefault().getPath(dir, tablename + ".java").toFile());
		this.parseFile(FileSystems.getDefault().getPath(dir, tablename + "Key.java").toFile());
	}
	
	public void extendDataMap(Map<String, Object> map)
	{
		System.out.println("Add " + keys.size() + " items to data map");
		List<Object> allkeys = new ArrayList<Object>(keys.values());
		map.put("items", allkeys);
		for (String skey : extendSelections)
		{
			System.out.println("Generate partial key collection " + skey);
			String names = config.properties.getProperty(skey);
			List<Object> selectKeys = new ArrayList<Object>();
			if (names == null || names.isEmpty())
			{
				System.out.println("Use all keys to set " + skey);
				selectKeys = allkeys;
			}
			else
			{
				for (String name : names.split(";"))
				{
					selectKeys.add(keys.get(name));
				}
			}
			map.put(skey, selectKeys);
			map.put(skey.substring(0, skey.length() - 1), selectKeys.get(0));
			map.put(skey.substring(0, skey.length() - 1) + "Count", selectKeys.size());
			System.out.println("Add " + selectKeys.size() + " items to data map as " + skey);
		}
	}
	
	@SuppressWarnings("resource")
	private void parseFile(File file)
	{
		if (file.exists() && !file.isDirectory())
		{
			System.out.println("Parsing file " + file.getAbsolutePath());
			try {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName(config.getEncoding()));
				BufferedReader br = new BufferedReader(isr);
				String line;
				while ((line = br.readLine()) != null)
				{
					Matcher m = pattern.matcher(line);
					if (m.matches())
					{
						Map<String, Object> item = new HashMap<String, Object>();
						String name = m.group("name");
						item.put("name", name);
						item.put("type", m.group("type"));
						this.keys.put(name, item);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Skip not existing file " + file.getAbsolutePath());
		}
	}
}
