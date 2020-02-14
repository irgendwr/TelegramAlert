package de.irgendwr.telegramAlert.template;


import com.floreysoft.jmte.encoder.Encoder;

/**
 * Encodes the Markdown special characters that Telegram supports, as described in
 * <a href="https://core.telegram.org/bots/api#markdown-style">https://core.telegram.org/bots/api#markdown-style</a>.
 */
public class TelegramMarkdownEncoder implements Encoder {

    @Override
    public String encode(String string) {
        StringBuilder sb = new StringBuilder((int) (string.length() * 1.2));
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            switch (c) {
                case '_':
                    sb.append("\\_");
                    break;
                case '*':
                    sb.append("\\*");
                    break;
                case '[':
                    sb.append("\\[");
                    break;
                case '`':
                    sb.append("\\`");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}