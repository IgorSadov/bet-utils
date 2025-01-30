package sadovskiy.bet.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class PageRequestDTO {
    private Integer page;
    private Integer size;
    private List<SortCriteria> sortCriteria;
    private List<FilterCriteria> filterCriteria;


    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class SortCriteria {
        private String field;
        private String direction;
    }



    /*FilterCriteria criteria = new FilterCriteria();
criteria.setField("status");
criteria.setValues(Arrays.asList("active", "pending", "completed"));
criteria.setCondition("in");*/

    /*FilterCriteria criteria = new FilterCriteria();
criteria.setField("createdAt");
criteria.setValues(Arrays.asList(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31)));
criteria.setCondition("between");*/


    @Setter
    @Getter
    @RequiredArgsConstructor
    public static class FilterCriteria {
        private String field;
        private Object value;
        private List<Object> values;
        private String condition;
    }


}

