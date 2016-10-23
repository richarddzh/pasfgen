package pasf;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerHelper {

	private Configuration freemarkerConfig;

	public void init(String templateDir, String encoding) throws IOException {
		System.out.println("Init freemarker to load template from dir: " + templateDir);
		Configuration config = new Configuration(Configuration.VERSION_2_3_22);
		config.setDirectoryForTemplateLoading(new File(templateDir));
		config.setDefaultEncoding(encoding);
		config.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		config.setLogTemplateExceptions(false);
		freemarkerConfig = config;
		System.out.println("Finish init freemarker");
	}
	
	public String textFromTemplate(String templateName, Object data) {
		try {
			Template template = freemarkerConfig.getTemplate(templateName);
			StringWriter out = new StringWriter();
			template.process(data, out);
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void templateToWriter(String templateName, Object data, Writer out) {
		try {
			Template template = freemarkerConfig.getTemplate(templateName);
			template.process(data, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String textFromString(String templateStr, Object data)
	{
		try {
			Template template = new Template("name", new StringReader(templateStr), new Configuration(Configuration.VERSION_2_3_22));
			StringWriter out = new StringWriter();
			template.process(data, out);
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return templateStr;
	}
}
