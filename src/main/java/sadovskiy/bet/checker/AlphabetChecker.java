package sadovskiy.bet.checker;

import java.util.regex.Pattern;

public class AlphabetChecker {
    public static boolean isCyrillic(String text) {
        Pattern pattern = Pattern.compile("\\p{InCyrillic}+");
        return pattern.matcher(text).find();
    }

    public static boolean isLatin(String text) {
        Pattern pattern = Pattern.compile("\\p{InBasicLatin}+");
        return pattern.matcher(text).find();
    }

    public static String trimStringToLength(String text, int length) {
        if (text != null && text.length() > length) {
            int endIndex = text.substring(0, length).lastIndexOf('.');
            if (endIndex > -1) {
                return text.substring(0, endIndex + 1);
            } else {
                return text.substring(0, length);
            }
        }
        return text;
    }
}
