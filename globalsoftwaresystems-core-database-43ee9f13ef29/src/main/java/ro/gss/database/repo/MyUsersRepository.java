package ro.gss.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.gss.database.entity.MyUser;
import java.lang.Long;
import java.util.Optional;
import java.lang.String;

public interface MyUsersRepository extends JpaRepository<MyUser, Long> {

	Optional<MyUser> findById(Long id);
	
	Optional<MyUser> findByUserName(String username);
}
