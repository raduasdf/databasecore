package ro.gss.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.gss.database.entity.Role;

public interface RolesRepository extends JpaRepository<Role, Long> {
	public Role findOneByCode(String code);
}
