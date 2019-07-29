package de.sandstorm_projects.telegramAlert.template;


import com.floreysoft.jmte.encoder.Encoder;

/**
 * Encodes the HTML special characters that Telegram supports, as described in
 * <a href="https://core.telegram.org/bots/api#html-style">https://core.telegram.org/bots/api#html-style</a>.
 */
public class TelegramHTMLEncoder implements Encoder {

    @Override
    public String encode(String string) {
        StringBuilder sb = new StringBuilder((int) (string.length() * 1.2));
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}