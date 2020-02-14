package de.irgendwr.telegramAlert.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import com.floreysoft.jmte.renderer.RawRenderer;

import java.util.Locale;
import java.util.Map;

public class RawBracketsRenderer implements NamedRenderer, RawRenderer {

    @Override
    public String render(Object obj, String format, Locale locale, Map<String, Object> model) {
        String string = obj.toString();
        StringBuilder sb = new StringBuilder(string.length());
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            switch (c) {
                case '[':
                    // replace with "FULLWIDTH LEFT SQUARE BRACKET" (U+FF3B, Ps): ［
                    sb.append("［");
                    break;
                case ']':
                    // replace with "FULLWIDTH RIGHT SQUARE BRACKET" (U+FF3D, Pe)
                    sb.append("］");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unused") // graylog 2.0
    public String render(Object obj, String format, Locale locale) {
        return render(obj, format, locale, null);
    }

    @Override
    public String getName() {
        return "brackets";
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