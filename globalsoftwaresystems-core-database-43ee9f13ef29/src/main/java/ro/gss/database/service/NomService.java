package ro.gss.database.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

import lombok.extern.slf4j.Slf4j;
import ro.gss.database.dto.GenericDTO;
import ro.gss.database.dto.GridRequestDTO;
import ro.gss.database.dto.NomItemDTO;
import ro.gss.database.dto.NomenclatureDTO;
import ro.gss.database.dto.QGenericDTO;
import ro.gss.database.dto.QNomItemDTO;
import ro.gss.database.dto.QNomenclatureDTO;
import ro.gss.database.entity.NomItem;
import ro.gss.database.entity.NomType;
import ro.gss.database.entity.QNomItem;
import ro.gss.database.entity.QNomType;
import ro.gss.database.repo.NomItemsRepository;
import ro.gss.database.repo.NomTypesRepository;
import ro.gss.database.utils.RepositoryHelper;

@Service
@Transactional
@Slf4j
public class NomService {

	private @PersistenceContext EntityManager entityManager;
	private @Autowired NomTypesRepository nomTypesRepository;
	private @Autowired NomItemsRepository nomItemsRepository;

	private final QNomType qNomType = QNomType.nomType;
	private final QNomItem qNomItem = QNomItem.nomItem;

	public List<NomItemDTO> getAllNomTypesNames() {
		return new JPAQuery(entityManager).from(qNomType)
				.list(new QNomItemDTO(qNomType.id, qNomType.code, qNomType.name));
	}

	public List<NomItemDTO> getNomenclatureItemsByType(final String typeCode) {
		return new JPAQuery(entityManager).from(qNomItem)
				.where(qNomItem.nomType().code.eq(typeCode).and(qNomItem.startDate.loe(new Date()))
						.and(qNomItem.endDate.isNull().or(qNomItem.endDate.goe(new Date()))))
				.orderBy(qNomItem.id.asc()).list(new QNomItemDTO(qNomItem.id, qNomItem.code, qNomItem.value));
	}

	public List<GenericDTO> getAllNomenclatureTypes() {

		List<NomType> items = nomTypesRepository.findAll();

		List<GenericDTO> results = getNomTypesToGenericDTOs(items);

		return results;
	}

	private List<GenericDTO> getNomTypesToGenericDTOs(List<NomType> items) {
		List<GenericDTO> results = new ArrayList<>();

		items.forEach(e -> {
			results.add(new GenericDTO(e.getId(), e.getCode(), e.getName()));
		});
		return results;
	}

	public NomType getNomenclatureTypeByName(String name) {
		NomType nomType = nomTypesRepository.findNomTypeByName(name);
		return nomType;
	}

	public List<GenericDTO> getAllNomenclatureItems() {

		QNomItem qNomItem = QNomItem.nomItem;
		List<GenericDTO> results = new JPAQuery(entityManager).from(qNomItem)
				.list(new QGenericDTO(qNomItem.id, qNomItem.code, qNomItem.value));

		return results;
	}

	public List<GenericDTO> getNomenclatureItemsByTypeCode(String typeCode) {

		QNomItem qNomItem = QNomItem.nomItem;
		JPAQuery query = new JPAQuery(entityManager).from(qNomItem)
				.where(qNomItem.nomType().code.eq(typeCode).and(qNomItem.startDate.loe(new Date()))
						.and(qNomItem.endDate.isNull().or(qNomItem.endDate.goe(new Date()))))
				.orderBy(qNomItem.id.asc());

		List<GenericDTO> results = query.list(new QGenericDTO(qNomItem.id, qNomItem.code, qNomItem.value));
		return results;
	}

	
	public List<String> getNomenclatureCodesByTypeCode(String typeCode) {
		return new JPAQuery(entityManager).from(qNomItem).listResults(qNomItem.value).getResults();
	}
	
	
	public void addNomenclature(NomenclatureDTO dto) {
		log.info(dto.toString());
		NomItem nomItem = null;
		if (dto.getId() != null) {
			// we have a modify nomenclature event
			nomItem = nomItemsRepository.findNomItemByCode(dto.getCode());
			log.info("Retrieved existing nomenclature item: " + nomItem);
		} else {
			// we have an add new nomenclature event
			nomItem = new NomItem();
			log.info("Creating new nomenclature item");
			nomItem.setCode(StringUtils.stripAccents(
					(dto.getCode() != null ? dto.getCode() : dto.getValue()).replace(' ', '_').toUpperCase()));
		}

		nomItem.setNomType(nomTypesRepository.findNomTypeByCode(dto.getTypeCode()));
		nomItem.setStartDate(new Date());

		if (dto.getEndDate() != null) {
			nomItem.setEndDate(dto.getEndDate());
		}

		nomItem.setValue(dto.getValue());

		log.info("Nomenclature item to be saved:" + nomItem);
		nomItemsRepository.save(nomItem);
	}

	public void deleteNomenclature(Long id) {
		NomItem nomItem = nomItemsRepository.getOne(id);
		nomItem.setEndDate(new Date());
		nomItemsRepository.save(nomItem);
	}

	public Page<NomenclatureDTO> getNomenclaturesWithPagination(GridRequestDTO gridRequest, String typeCode) {
		QNomItem qNomItem = QNomItem.nomItem;
		PathBuilder<NomItem> entityPath = new PathBuilder<>(qNomItem.getType(), qNomItem.getMetadata());
		Predicate pathPredicate = RepositoryHelper.getPredicate(gridRequest, entityPath);
		OrderSpecifier<String> sorterPath = RepositoryHelper.getSorter(gridRequest, entityPath);

		JPAQuery query = new JPAQuery(entityManager).from(qNomItem)
				.where(qNomItem.nomType().code.eq(typeCode).and(pathPredicate).and(qNomItem.startDate.loe(new Date()))
						.and(qNomItem.endDate.isNull().or(qNomItem.endDate.goe(new Date()))));
		if (sorterPath != null) {
			query.orderBy(sorterPath);
		}
		RepositoryHelper.setPaginationOnQuery(gridRequest, query);

		List<NomenclatureDTO> list = query.list(new QNomenclatureDTO(qNomItem.id, qNomItem.nomType().id,
				qNomItem.nomType().code, qNomItem.code, qNomItem.value, qNomItem.startDate, qNomItem.endDate));

		return new PageImpl<NomenclatureDTO>(list, RepositoryHelper.getPageable(gridRequest), query.count());
	}

	public Page<NomenclatureDTO> getPaginatedNomenclatures(GridRequestDTO gridRequest) {
		QNomItem qNomItem = QNomItem.nomItem;
		PathBuilder<NomItem> entityPath = new PathBuilder<>(qNomItem.getType(), qNomItem.getMetadata());
		Predicate pathPredicate = RepositoryHelper.getPredicate(gridRequest, entityPath);
		OrderSpecifier<String> sorterPath = RepositoryHelper.getSorter(gridRequest, entityPath);

		JPAQuery query = new JPAQuery(entityManager).from(qNomItem).where(pathPredicate).where(
				qNomItem.startDate.loe(new Date()).and(qNomItem.endDate.isNull().or(qNomItem.endDate.goe(new Date()))));
		if (sorterPath != null) {
			query.orderBy(sorterPath);
		}
		RepositoryHelper.setPaginationOnQuery(gridRequest, query);

		List<NomenclatureDTO> list = query.list(new QNomenclatureDTO(qNomItem.id, qNomItem.nomType().id,
				qNomItem.nomType().code, qNomItem.code, qNomItem.value, qNomItem.startDate, qNomItem.endDate));

		return new PageImpl<NomenclatureDTO>(list, RepositoryHelper.getPageable(gridRequest), query.count());
	}

	public List<NomenclatureDTO> getNomenclatures(GridRequestDTO gridRequest, String typeCode) {
		QNomItem qNomItem = QNomItem.nomItem;
		PathBuilder<NomItem> entityPath = new PathBuilder<>(qNomItem.getType(), qNomItem.getMetadata());
		Predicate pathPredicate = RepositoryHelper.getPredicate(gridRequest, entityPath);
		OrderSpecifier<String> sorterPath = RepositoryHelper.getSorter(gridRequest, entityPath);

		JPAQuery query = new JPAQuery(entityManager).from(qNomItem)
				.where(qNomItem.nomType().code.eq(typeCode).and(pathPredicate).and(qNomItem.startDate.loe(new Date()))
						.and(qNomItem.endDate.isNull().or(qNomItem.endDate.goe(new Date()))));
		if (sorterPath != null) {
			query.orderBy(sorterPath);
		}

		List<NomenclatureDTO> list = query.list(new QNomenclatureDTO(qNomItem.id, qNomItem.nomType().id,
				qNomItem.nomType().code, qNomItem.code, qNomItem.value, qNomItem.startDate, qNomItem.endDate));

		return list;
	}

}
