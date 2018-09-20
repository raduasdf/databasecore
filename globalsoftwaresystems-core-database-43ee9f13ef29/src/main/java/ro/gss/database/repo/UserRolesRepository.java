package ro.gss.database.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ro.gss.database.entity.UserRole;

public interface UserRolesRepository extends JpaRepository<UserRole, Long> {

	@Query("SELECT ur.role.code FROM UserRole ur WHERE ur.user.id=:userId")
	public List<String> getAllRolesByUserId(@Param("userId") Long userId);

	@Query("SELECT ur FROM UserRole ur WHERE ur.user.login=:login")
	public List<UserRole> getAllUserRolesByUserId(@Param("login") String login);

	
	 @Query("SELECT s.id FROM UserRole s WHERE s.role.id = :rolesId AND s.user.id = :usersId")
	 public Long getUserRoleIdOfRoles(@Param("rolesId") Long
	  rolesId, @Param("usersId") Long usersId);
	 

	@Query("SELECT ss.id FROM UserRole ss WHERE ss.user.id = :userId")
	public List<Long> findAllUsersRolesByUser(@Param("userId") Long userId);
}
