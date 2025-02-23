package sadovskiy.bet.translit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class Transliteration {

    public enum TransliterationFormat {
        RU_TO_ENG,
        ENG_TO_RU
    }

    final static int UPPER = 1;
    final static int LOWER = 2;
    private static final Pattern CLEAR_PATTERN = Pattern.compile("[\\s]+");
    final static Map<String, String> mapEngToRus = mapEngToRus();
    final static Map<String, String> mapRusToEng = mapRusToEng();

    private static Map<String, String> mapEngToRus() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "а");
        map.put("b", "б");
        map.put("v", "в");
        map.put("g", "г");
        map.put("d", "д");
        map.put("e", "е");
        map.put("yo", "ё");
        map.put("zh", "ж");
        map.put("z", "з");
        map.put("i", "и");
        map.put("j", "й");
        map.put("k", "к");
        map.put("l", "л");
        map.put("m", "м");
        map.put("n", "н");
        map.put("o", "о");
        map.put("p", "п");
        map.put("r", "р");
        map.put("s", "с");
        map.put("t", "т");
        map.put("u", "у");
        map.put("f", "ф");
        map.put("h", "х");
        map.put("ts", "ц");
        map.put("ch", "ч");
        map.put("sh", "ш");
        map.put("`", "ъ");
        map.put("y", "у");
        map.put("'", "ь");
        map.put("yu", "ю");
        map.put("ya", "я");
        map.put("x", "кс");
        map.put("w", "в");
        map.put("q", "к");
        map.put("iy", "ий");
        return map;
    }

    private static Map<String, String> mapRusToEng() {
        Map<String, String> map = new HashMap<>();
        map.put("а", "a");
        map.put("ә", "a");
        map.put("б", "b");
        map.put("в", "v");
        map.put("г", "g");
        map.put("ғ", "g");
        map.put("д", "d");
        map.put("е", "e");
        map.put("ё", "yo");
        map.put("ж", "zh");
        map.put("з", "z");
        map.put("и", "y");
        map.put("і", "i");
        map.put("й", "j");
        map.put("к", "k");
        map.put("қ", "q");
        map.put("л", "l");
        map.put("м", "m");
        map.put("н", "n");
        map.put("о", "o");
        map.put("ө", "o");
        map.put("п", "p");
        map.put("р", "r");
        map.put("с", "s");
        map.put("т", "t");
        map.put("у", "u");
        map.put("ұ", "u");
        map.put("ү", "u");
        map.put("ф", "f");
        map.put("х", "h");
        map.put("һ", "h");
        map.put("ц", "ts");
        map.put("ч", "ch");
        map.put("ш", "sh");
        map.put("щ", "sh");
        map.put("ң", "n");
        map.put("ы", "y");
        map.put("э", "e");
        map.put("ю", "yu");
        map.put("я", "ya");
        map.put("ий", "iy");

        return map;
    }

    public static Map<String, String> mapKazToRus() {
        Map<String, String> map = new HashMap<>();
        map.put("ә", "а");
        map.put("ғ", "г");
        map.put("қ", "к");
        map.put("ө", "о");
        map.put("ұ", "у");
        map.put("ү", "у");
        map.put("һ", "х");
        map.put("ң", "н");
        map.put("і", "и");

        return map;
    }

    private static int charClass(char c) {
        return Character.isUpperCase(c) ? UPPER : LOWER;
    }

    private static String get(String s, TransliterationFormat format) {
        int charClass = charClass(s.charAt(0));

        String result = null;

        switch (format) {
            case ENG_TO_RU:
                result = mapEngToRus.get(s.toLowerCase());
                break;
            case RU_TO_ENG:
                result = mapRusToEng.get(s.toLowerCase());
                break;
        }
        return result == null ? "" : (charClass == UPPER ? (result.charAt(0) + "").toUpperCase() +
                (result.length() > 1 ? result.substring(1) : "") : result);
    }

    public static String translit(String text, TransliterationFormat format) {
        int len = text.length();
        if (len == 0) {
            return text;
        }
        if (len == 1) {
            return get(text, format);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ) {
            String toTranslate = text.substring(i, i <= len - 2 ? i + 2 : i + 1);
            String translated = get(toTranslate, format);
            if (TextUtils.isEmpty(translated)) {
                translated = get(toTranslate.charAt(0) + "", format);
                sb.append(TextUtils.isEmpty(translated) ? toTranslate.charAt(0) : translated);
                i++;
            } else {
                sb.append(TextUtils.isEmpty(translated) ? toTranslate : translated);
                i += 2;
            }
        }
        try {
            if (sb.length()!=0){
                return normalize(sb.toString().trim(), format);
            }
        } catch (Exception e) {
            log.error("Error process translit and normalize text [{}], message {}", text, e.getMessage());
        }
        return null;
    }

    public static String normalize(String message, TransliterationFormat format) {
        if (format.equals(TransliterationFormat.RU_TO_ENG)) {
            message = message.replaceAll("ь", "");
            message = message.replaceAll("ъ", "");
        }

        String[] words = message.split("\\s+");
        int uppercaseCount = 0;
        int lowercaseCount = 0;

        for (String word : words) {
            if (!word.isEmpty() && Character.isUpperCase(word.charAt(0))) {
                uppercaseCount++;
            } else {
                lowercaseCount++;
            }
        }

        String normalizedMessage;
        if (uppercaseCount > lowercaseCount) {
            normalizedMessage = message.toUpperCase();
        } else {
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                if (Character.isUpperCase(word.charAt(0))) {
                    sb.append(word).append(" ");
                } else {
                    sb.append(word.toLowerCase()).append(" ");
                }
            }
            normalizedMessage = sb.toString().trim();
        }

        return CLEAR_PATTERN.matcher(normalizedMessage).replaceAll(" ").trim();
    }


}
