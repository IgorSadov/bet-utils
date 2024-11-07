package sadovskiy.bet.actual;

import java.time.Year;

public class ActualDataUtils {

    public static int getCurrentYear() {
        return Year.now().getValue();
    }

    public static int getPreviousYear() {
        return Year.now().minusYears(1).getValue();
    }

}
