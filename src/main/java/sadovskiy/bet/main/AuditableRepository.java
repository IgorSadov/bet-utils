package sadovskiy.bet.main;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import sadovskiy.bet.data.PageRequestDTO;
import sadovskiy.bet.data.TableResponse;

import java.util.ArrayList;
import java.util.List;

@NoRepositoryBean
public interface AuditableRepository <E extends AuditableEntity> extends JpaRepository<E, Long>, JpaSpecificationExecutor<E> {

    default Specification<E> buildSpecification(PageRequestDTO request) {
        Specification<E> spec = Specification.where(null);

        if (request.getFilterCriteria() != null && !request.getFilterCriteria().isEmpty()) {
            for (PageRequestDTO.FilterCriteria criteria : request.getFilterCriteria()) {
                spec = spec.and(buildCriteria(criteria));
            }
        }

        return spec;
    }


    @Transactional
    default Page<E> findSomeEntities(PageRequestDTO request) {
        Specification<E> spec = buildSpecification(request);
        Pageable pageable = createPageable(request);
        return findAll(spec, pageable);
    }

    @Transactional
    default <D> TableResponse<D> tableResponse(PageRequestDTO request, ModelMapper modelMapper, Class<D> dtoClass) {

        Specification<E> spec = buildSpecification(request);
        Pageable pageable = createPageable(request);

        Page<E> entityPage = findAll(spec, pageable);

        List<D> dtoList = new ArrayList<>();
        for (E entity : entityPage.getContent()) {
            D dto = modelMapper.map(entity, dtoClass);
            dtoList.add(dto);
        }

        Page<D> dtoPage = new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());

        return new TableResponse<>(dtoPage);
    }



    @Transactional
    default TableResponse<E> tableResponse(PageRequestDTO request) {
        Specification<E> spec = buildSpecification(request);
        Pageable pageable = createPageable(request);
        Page<E> pageResult = findAll(spec, pageable);
        return new TableResponse<>(pageResult);
    }

    default Pageable createPageable(PageRequestDTO request) {
        List<Sort.Order> orders = new ArrayList<>();

        if (request.getSortCriteria() == null || request.getSortCriteria().isEmpty()) {
            orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        } else {
            for (PageRequestDTO.SortCriteria sortCriteria : request.getSortCriteria()) {
                Sort.Direction direction = Sort.Direction.fromString(sortCriteria.getDirection());
                orders.add(new Sort.Order(direction, sortCriteria.getField()));
            }
        }

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        return PageRequest.of(page, size, Sort.by(orders));
    }


    private Specification<E> buildCriteria(PageRequestDTO.FilterCriteria criteria) {
        return (root, query, cb) -> {
            switch (criteria.getCondition().toLowerCase()) {
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
                    break;
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
            return null;
        };
    }
}
