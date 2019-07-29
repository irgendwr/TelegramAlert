package de.sandstorm_projects.telegramAlert.template;

import com.floreysoft.jmte.NamedRenderer;
import com.floreysoft.jmte.RenderFormatInfo;
import com.floreysoft.jmte.renderer.RawRenderer;

import java.util.Locale;

public final class RawNoopRenderer implements NamedRenderer, RawRenderer {

    @Override
    public String render(Object obj, String format, Locale locale) {
        return obj.toString();
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