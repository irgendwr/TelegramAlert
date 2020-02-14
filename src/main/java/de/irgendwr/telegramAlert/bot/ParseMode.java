package de.irgendwr.telegramAlert.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ParseMode {
    // values
    public static final String TEXT = "Text";
    public static final String MARKDOWN = "Markdown";
    public static final String HTML = "HTML";

    // number of values
    private static final int SIZE = 3;

    // list of values
    public static final List<String> VALUES = new ArrayList<>(SIZE);
    static {
        VALUES.add(TEXT);
        VALUES.add(MARKDOWN);
        VALUES.add(HTML);
    }
    // options for config
    public static final Map<String, String> OPTIONS = new HashMap<>(SIZE);
    static {
        OPTIONS.put("text", TEXT);
        OPTIONS.put("Markdown", MARKDOWN);
        OPTIONS.put("HTML", HTML);
    }

    // value
    private final String mode;

    /**
     * Selects a parse mode.
     * @param str value
     * @require ParseMode.VALUES.contains(str)
     */
    private ParseMode(String str) {
        assert VALUES.contains(str);
        this.mode = str;
    }

    /**
     * @return Text parse mode.
     */
    public static ParseMode text() {
        return new ParseMode(TEXT);
    }

    /**
     * See https://core.telegram.org/bots/api#markdown-style
     * @return Markdown parse mode.
     */
    public static ParseMode markdown() {
        return new ParseMode(MARKDOWN);
    }

    /**
     * See https://core.telegram.org/bots/api#html-style
     * @return HTML parse mode.
     */
    public static ParseMode html() {
        return new ParseMode(HTML);
    }

    public static ParseMode fromString(String str) {
        for (String mode : VALUES) {
            if (mode.toLowerCase().equals(str.toLowerCase())) {
                return new ParseMode(mode);
            }
        }
        return text();
    }

    public String value() {
        return mode;
    }

    @Override
    public String toString() {
        return value();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ParseMode) {
            return equals((ParseMode) obj);
        } else {
            return false;
        }
    }

    public boolean equals(ParseMode pm) {
        return pm.mode.equals(mode);
    }

    @Override
    public int hashCode() {
        return VALUES.indexOf(mode);
    }
}
