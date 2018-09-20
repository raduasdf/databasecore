package ro.gss.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.gss.database.entity.NomItem;

public interface NomItemsRepository extends JpaRepository<NomItem, Long> {
	
	public NomItem findNomItemByCode(String code);
}
