package ro.gss.database.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.StringPath;

import ro.gss.database.dto.FilterDTO;
import ro.gss.database.dto.GridRequestDTO;
import ro.gss.database.dto.SorterDTO;

public class RepositoryHelper {

	public static Long getDefaultCount(GridRequestDTO gridRequest, int size) {
		if (size >= gridRequest.getNrElements())
			return new Long((gridRequest.getPage() + 2) * gridRequest.getNrElements());
		else
			return new Long((gridRequest.getPage() + 1) * gridRequest.getNrElements());
	}

	public static Pageable getPageable(GridRequestDTO gridRequest) {
		final List<Sort.Order> orders = new ArrayList<Sort.Order>();
		Pageable pageable = null;
		List<SorterDTO> sorters = gridRequest.getSorters();
		if (sorters != null) {
			sorters.stream().filter(sorter -> !sorter.getKey().isEmpty()).forEach(sorter -> {
				Sort.Direction direction = sorter.getDirection().equals("ASC") ? Sort.Direction.ASC
						: Sort.Direction.DESC;
				Sort.Order order = new Sort.Order(direction, sorter.getKey());
				orders.add(order);
			});
		}
		if (orders.isEmpty()) {
			pageable = PageRequest.of(gridRequest.getPage(), gridRequest.getNrElements());
		} else {
			pageable = PageRequest.of(gridRequest.getPage(), gridRequest.getNrElements(), Sort.by(orders));
		}
		return pageable;

	}

	public static <T> Predicate getPredicate(GridRequestDTO gridRequest, PathBuilder<T> pathBuilder) {
		List<FilterDTO> filters = gridRequest.getFilters();
		BooleanExpression rootPredicate = null;
		if (filters != null) {
			for (FilterDTO filter : filters) {

				if (!filter.getValue().isEmpty()) {
					if (rootPredicate == null) {
						rootPredicate = buildPredicate(filter, pathBuilder);
					} else {
						BooleanExpression predicate = buildPredicate(filter, pathBuilder);
						if (predicate != null) {
							rootPredicate = rootPredicate.and(predicate);
						}
					}
				}
			}
		}

		return rootPredicate;
	}

	public static <T> BooleanExpression buildPredicate(FilterDTO filter, PathBuilder<T> pathBuilder) {
		SearchTypeEnum type = SearchTypeEnum.getByName(filter.getType());
		if (type == SearchTypeEnum.DATE || type == SearchTypeEnum.DATE_TIME) {
			return buildDatePredicate(filter, pathBuilder);
		} else if (type == SearchTypeEnum.NUMBER) {
			return buildNumericPredicate(filter, pathBuilder);
		} else if (type == SearchTypeEnum.STRING) {
			return buildStringPredicate(filter, pathBuilder);
		} else if (type == SearchTypeEnum.CLOB) {
			return buildClobPredicate(filter, pathBuilder);
		} else if (type == SearchTypeEnum.LIST) {
			return buildListPredicate(filter, pathBuilder);
		} else {
			return null;
		}
	}

	private static <T> BooleanExpression buildDatePredicate(FilterDTO filter, PathBuilder<T> pathBuilder) {
		Date attributeValue = new Date(Long.parseLong(filter.getValue()));
		DatePath<Date> datePath = pathBuilder.getDate(filter.getKey(), Date.class);
		if (filter.getOp().equals("=")) {
			return datePath.eq(attributeValue);
		} else if (filter.getOp().equals(">")) {
			return datePath.after(attributeValue);
		} else if (filter.getOp().equals("<")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(attributeValue);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			return datePath.before(calendar.getTime());
		} else if (filter.getOp().equals(">=")) {
			return datePath.goe(attributeValue);
		} else if (filter.getOp().equals("<=")) {
			return datePath.loe(attributeValue);
		} else if (filter.getOp().equals(">=N")) {
			return datePath.goe(attributeValue).or(datePath.isNull());
		} else if (filter.getOp().equals("<=N")) {
			return datePath.loe(attributeValue).or(datePath.isNull());
		} else if (filter.getOp().equals("<>")) {
			return datePath.ne(attributeValue);
		} else if (filter.getOp().equals("=T")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(attributeValue);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			return datePath.between(attributeValue, calendar.getTime());
		} else {
			throw new IllegalArgumentException("No such operation [" + filter.getKey() + "]");
		}
	}

	private static <T> BooleanExpression buildNumericPredicate(FilterDTO filter, PathBuilder<T> pathBuilder) {
		BigDecimal attributeValue = new BigDecimal(filter.getValue());
		NumberPath<BigDecimal> numberPath = pathBuilder.getNumber(filter.getKey(), BigDecimal.class);
		if (filter.getOp().equals("=")) {
			return numberPath.eq(attributeValue);
		} else if (filter.getOp().equals(">")) {
			return numberPath.gt(attributeValue);
		} else if (filter.getOp().equals("<")) {
			return numberPath.lt(attributeValue);
		} else if (filter.getOp().equals(">=")) {
			return numberPath.goe(attributeValue);
		} else if (filter.getOp().equals("<=")) {
			return numberPath.loe(attributeValue);
		} else if (filter.getOp().equals("<>")) {
			return numberPath.ne(attributeValue);
		} else if (filter.getOp().equals("=N")) {
			return numberPath.isNull();
		} else if (filter.getOp().equals("!N")) {
			return numberPath.isNotNull();
		} else {
			throw new IllegalArgumentException("No such operation [" + filter.getKey() + "]");
		}
	}

	private static <T> BooleanExpression buildStringPredicate(FilterDTO filter, PathBuilder<T> pathBuilder) {
		String[] filterParts = filter.getKey().split("-");
		BooleanExpression be = null;
		for (String part : filterParts) {
			StringPath stringPath = pathBuilder.getString(part);

			if (be == null) {
				be = stringPath.upper().like("%" + filter.getValue().toUpperCase() + "%");
			} else {
				be = be.or(stringPath.upper().like("%" + filter.getValue().toUpperCase() + "%"));
			}

			if (filter.getOp().equals("=")) {
				if (be == null) {
					be = stringPath.equalsIgnoreCase(filter.getValue());
				} else {
					be = be.or(stringPath.equalsIgnoreCase(filter.getValue()));
				}
			}

			else if (filter.getOp().equals("<>")) {
				if (be == null) {
					be = stringPath.notEqualsIgnoreCase(filter.getValue());
				} else {
					be = be.or(stringPath.notEqualsIgnoreCase(filter.getValue()));
				}
			}

			else if (filter.getOp().equals(":")) {
				if (be == null) {
					be = stringPath.upper().like("%" + filter.getValue().toUpperCase() + "%");
				} else {
					be = be.or(stringPath.upper().like("%" + filter.getValue().toUpperCase() + "%"));
				}
			}

			else if (filter.getOp().equals(":>")) {
				if (be == null) {
					be = stringPath.upper().like(filter.getValue().toUpperCase() + "%");
				} else {
					be = be.or(stringPath.upper().like(filter.getValue().toUpperCase() + "%"));
				}
			}

			else if (filter.getOp().equals("<:")) {
				if (be == null) {
					be = stringPath.upper().like("%" + filter.getValue().toUpperCase());
				} else {
					be = be.or(stringPath.upper().like("%" + filter.getValue().toUpperCase()));
				}
			}

			else if (filter.getOp().equals("<=")) {
				if (be == null) {
					be = stringPath.upper().loe(filter.getValue().toUpperCase());
				} else {
					be = be.or(stringPath.upper().loe(filter.getValue().toUpperCase()));
				}
			}

			else if (filter.getOp().equals(">=")) {
				if (be == null) {
					be = stringPath.upper().goe(filter.getValue().toUpperCase());
				} else {
					be = be.or(stringPath.upper().goe(filter.getValue().toUpperCase()));
				}
			} else {
				throw new IllegalArgumentException("No such operation [" + filter.getOp() + "]");
			}
		}
		return be;
	}

	private static <T> BooleanExpression buildListPredicate(FilterDTO filter, PathBuilder<T> pathBuilder) {

		NumberPath<Long> numberPath = pathBuilder.getNumber(filter.getKey(), Long.class);
		if (filter.getOp().equals("=IN")) {
			String[] values = filter.getValue().split(",");
			List<Long> longValues = new ArrayList<>();
			for (String val : values) {
				longValues.add(new Long(val));
			}
			return numberPath.in(longValues);
		}

		else {
			throw new IllegalArgumentException("No such operation [" + filter.getOp() + "]");
		}
	}

	private static <T> BooleanExpression buildClobPredicate(FilterDTO filter, PathBuilder<T> pathBuilder) {

		StringPath stringPath = pathBuilder.getString(filter.getKey());
		if (filter.getOp().equals("instr")) {
			return stringPath.contains(filter.getValue());
		} else {
			throw new IllegalArgumentException("No such operation [" + filter.getOp() + "]");
		}
	}

	public static void setPaginationOnQuery(GridRequestDTO gridRequest, JPAQuery query) {
		if (gridRequest.getPage() != null && gridRequest.getPage() >= 0 && gridRequest.getNrElements() != null
				&& gridRequest.getNrElements() > 0) {
			Long offset = (long) (gridRequest.getPage() * gridRequest.getNrElements());
			Long limit = (long) gridRequest.getNrElements();

			query.limit(limit).offset(offset);

		}
	}

	public static <T> OrderSpecifier<String> getSorter(GridRequestDTO gridRequest, PathBuilder<T> entityPath) {
		OrderSpecifier<String> sorterPath = null;
		if (gridRequest.getSorters() != null && !gridRequest.getSorters().isEmpty()) {
			List<SorterDTO> sorters = gridRequest.getSorters();
			for (SorterDTO s : sorters) {
				String[] stringToSplit = s.getKey().split("-");
				for (String part : stringToSplit) {
					sorterPath = s.getDirection().equals("ASC") ? entityPath.getString(part).asc()
							: entityPath.getString(part).desc();
				}
			}
		}
		return sorterPath;
	}
}
