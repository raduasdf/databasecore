package ro.gss.database.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.gss.database.entity.SysParameter;


public interface SystemParametersRepository extends JpaRepository<SysParameter, Long> {
	
	@Query("SELECT sysp FROM SysParameter sysp WHERE sysp.code LIKE UPPER(CONCAT('%',:code,'%'))")
	public List<SysParameter> findAllLikeCode(@Param("code") String code, Pageable page);

}
