package sadovskiy.bet.data;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class GenericSpecifications {

    public static <T> Specification<T> buildSpecification(PageRequestDTO.FilterCriteria criteria) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            String condition = criteria.getCondition().toLowerCase();
            switch (condition) {
                case "equals":
                    return cb.equal(root.get(criteria.getField()), criteria.getValue());
                case "notequals":
                    return cb.notEqual(root.get(criteria.getField()), criteria.getValue());
                case "more":
                    return cb.greaterThan(root.get(criteria.getField()), (Comparable) criteria.getValue());
                case "less":
                    return cb.lessThan(root.get(criteria.getField()), (Comparable) criteria.getValue());
                case "between":
                    if (criteria.getValues() != null && criteria.getValues().size() == 2) {
                        return cb.between(root.get(criteria.getField()),
                                (Comparable) criteria.getValues().get(0),
                                (Comparable) criteria.getValues().get(1));
                    }
                    return null;
                case "in":
                    return root.get(criteria.getField()).in(criteria.getValues());
                case "notin":
                    return cb.not(root.get(criteria.getField()).in(criteria.getValues()));
                case "isnull":
                    return cb.isNull(root.get(criteria.getField()));
                case "isnotnull":
                    return cb.isNotNull(root.get(criteria.getField()));
                case "like":
                    return cb.like(root.get(criteria.getField()), "%" + criteria.getValue() + "%");
                default:
                    return null;
            }
        };
    }


}
