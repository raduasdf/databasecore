package ro.gss.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.gss.database.entity.NomType;

public interface NomTypesRepository extends JpaRepository<NomType, Long> {
	public NomType findNomTypeByName(String name);
	
	public NomType findNomTypeByCode(String code);
}
