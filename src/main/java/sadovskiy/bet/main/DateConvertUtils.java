package sadovskiy.bet.main;



import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class DateConvertUtils {
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public static  String[] patterns = {
            "yyyy-MM-dd",
            "MM/dd/yyyy",
            "dd/MM/yyyy",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "EEE, d MMM yyyy HH:mm:ss Z",
    };

    public static LocalDate asLocalDate(Date date) {
        return asLocalDate(date, ZoneId.systemDefault());
    }

    public static String asStringFrontEnd(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formatDate = dateFormat.format(date);

        LocalDate localDate = asLocalDate(date, ZoneId.systemDefault());
        String s = formatDate.toString();
        return s.replaceAll("-", ".");
    }

    public static LocalDate asLocalDate(Date date, ZoneId zone) {
        if (date == null)
            return null;

        if (date instanceof java.sql.Date)
            return ((java.sql.Date) date).toLocalDate();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
    }



    public static LocalDateTime asLocalDateTime(String date) {
        if (date == null)
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return asLocalDateTime(date, ZoneId.systemDefault());
    }

    public static LocalDateTime asLocalDateTime(Date date, ZoneId zone) {
        if (date == null)
            return null;

        if (date instanceof java.sql.Timestamp)
            return ((java.sql.Timestamp) date).toLocalDateTime();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime();
    }

    public static Date getTomorrowDate() {
        try {
            Date today = formatter.parse(formatter.format(new Date()));
            return new Date(today.getTime() + (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            System.out.println("Error due parse putTomorrowDate, message: " + e.getMessage());
        }
        return null;
    }

    public static LocalDate getTomorrowLocalDate() {
        try {
            Date today = formatter.parse(formatter.format(new Date()));
            LocalDate local = asLocalDate(today);
            return local.plusDays(1);
        } catch (Exception e) {
            System.out.println("Error due parse putTomorrowDate, message: " + e.getMessage());
        }
        return null;
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }

    public static String convertStringLocalDateToFormatter(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return localDate.format(formatter);
    }

    public static Date dateToFormat(Date date) throws ParseException {
        Date dateWithoutTime = removeTime(date);
        return formatter.parse(String.valueOf(dateWithoutTime));
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDateFromStringGuessDateFormat(String dateStr) {
        Objects.requireNonNull(dateStr, "input string must not be null");
        if (!dateStr.isEmpty()){
            for (String pattern : patterns) {
                try {
                    return DateUtils.parseDate(dateStr, pattern);
                } catch (ParseException e) {
                    log.debug("try parse string {} to date", dateStr);
                }
            }
        }
        return null;
    }

}
