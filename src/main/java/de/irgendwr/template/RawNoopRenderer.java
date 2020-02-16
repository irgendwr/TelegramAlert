package de.irgendwr.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import com.floreysoft.jmte.renderer.RawRenderer;

import java.util.Locale;
import java.util.Map;

public class RawNoopRenderer implements NamedRenderer, RawRenderer {

    @Override
    public String render(Object obj, String format, Locale locale, Map<String, Object> model) {
        return obj.toString();
    }

    @SuppressWarnings("unused") // graylog 2.0
    public String render(Object obj, String format, Locale locale) {
        return render(obj, format, locale, null);
    }

    @Override
    public String getName() {
        return "raw";
    }

    @Override
    public RenderFormatInfo getFormatInfo() {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return new Class<?>[]{ String.class };
    }
}