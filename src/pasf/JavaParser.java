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
	
	private List<Object> keys;
	private List<Object> primaryKeys;
	private Config config;
	private Pattern pattern;
	private String[] primaryKeyNames;
	
	public JavaParser(Config config)
	{
		this.config = config;
		this.keys = new ArrayList<Object>();
		this.primaryKeys = new ArrayList<Object>();
		this.pattern = Pattern.compile(config.properties.getProperty("fieldregex"));
		this.primaryKeyNames = config.properties.getProperty("primarykeys").split(";");
		String dir = config.properties.getProperty("mybatisdir");
		String tablename = config.properties.getProperty("tablename");
		this.parseFile(FileSystems.getDefault().getPath(dir, tablename + ".java").toFile());
		this.parseFile(FileSystems.getDefault().getPath(dir, tablename + "Key.java").toFile());
	}
	
	public void extendDataMap(Map<String, Object> map)
	{
		System.out.println("Add " + keys.size() + " items to data map");
		map.put("items", keys);
		System.out.println("Add " + primaryKeys.size() + " primary keys to data map");
		map.put("primarykeys", primaryKeys);
		map.put("primarykeyCount", primaryKeys.size());
		map.put("primarykey", primaryKeys.get(0));
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
						this.keys.add(item);
						for (int i = 0; i < this.primaryKeyNames.length; i++)
						{
							if (name.equalsIgnoreCase(this.primaryKeyNames[i]))
							{
								this.primaryKeys.add(item);
							}
						}
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
