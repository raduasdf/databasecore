package ro.gss.database.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.gss.database.entity.User;

public interface UsersRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByLogin(String login);

}
