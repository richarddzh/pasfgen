package pasf;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public abstract class FreeMarkerHelper {

	private static final String ENCODING = "UTF-8";
	private static Configuration CONFIG;

	public static void Init(String templateDir) throws IOException {
		System.out.println("Init freemarker to load template from dir: " + templateDir);
		Configuration config = new Configuration(Configuration.VERSION_2_3_22);
		config.setDirectoryForTemplateLoading(new File(templateDir));
		config.setDefaultEncoding(ENCODING);
		config.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
		config.setLogTemplateExceptions(false);
		CONFIG = config;
		System.out.println("Finish init freemarker");
	}
	
	public static String textFromTemplate(String templateName, Object data) {
		try {
			Template template = CONFIG.getTemplate(templateName);
			StringWriter out = new StringWriter();
			template.process(data, out);
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void templateToWriter(String templateName, Object data, Writer out) {
		try {
			Template template = CONFIG.getTemplate(templateName);
			template.process(data, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
