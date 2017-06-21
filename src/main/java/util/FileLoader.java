package util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by Christian on 21-06-2017.
 */
public class FileLoader {

    public static String loadFile(String filePath) throws IOException {
        InputStream resourceAsStream = FileLoader.class.getResourceAsStream(filePath);
        if (resourceAsStream==null) throw new FileNotFoundException();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
            while ((length = resourceAsStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
    }

    public static String loadMustache(String templatePath, Map<String,Object> content) throws IOException {
        StringWriter writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        InputStreamReader isr = new InputStreamReader(FileLoader.class.getResourceAsStream(templatePath));

        Mustache mustache = mf.compile(isr, "test");
        Writer execute = mustache.execute(writer, content);
        execute.flush();
        return execute.toString();

    }


}
