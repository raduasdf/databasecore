package ro.gss.database.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

import ro.gss.database.dto.GridRequestDTO;
import ro.gss.database.dto.QUserDTO;
import ro.gss.database.dto.UserDTO;
import ro.gss.database.entity.QRole;
import ro.gss.database.entity.QUser;
import ro.gss.database.entity.QUserRole;
import ro.gss.database.entity.User;
import ro.gss.database.entity.UserRole;
import ro.gss.database.repo.RolesRepository;
import ro.gss.database.repo.UserRolesRepository;
import ro.gss.database.repo.UsersRepository;
import ro.gss.database.utils.RepositoryHelper;

@Service
@Transactional
public class UserService {

	@PersistenceContext
	private EntityManager entityManager;

	private final UsersRepository userRepository;
	private final UserRolesRepository userRolesRepository;
	private final RolesRepository rolesRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	

	public UserService(UsersRepository userRepository, UserRolesRepository userRolesRepository,
			RolesRepository rolesRepository) {
		this.userRepository = userRepository;
		this.userRolesRepository = userRolesRepository;
		this.rolesRepository = rolesRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	private final QUser qUser = QUser.user;
	private final QUserRole qUserRole = QUserRole.userRole;
	private final QRole qRole = QRole.role;

	public PageImpl<UserDTO> getUsersWithPagination(final GridRequestDTO gridRequest) {

		final PathBuilder<User> entityPath = new PathBuilder<>(qUser.getType(), qUser.getMetadata());
		final Predicate pathPredicate = RepositoryHelper.getPredicate(gridRequest, entityPath);
		final OrderSpecifier<String> sorterPath = RepositoryHelper.getSorter(gridRequest, entityPath);

		final JPAQuery query = new JPAQuery(entityManager).from(qUser).where(pathPredicate);
		if (sorterPath != null) {
			query.orderBy(sorterPath);
		}
		System.out.println(gridRequest.getFilters());
		RepositoryHelper.setPaginationOnQuery(gridRequest, query);

		return new PageImpl<UserDTO>(
				query.list(new QUserDTO(qUser.id, qUser.startDate, qUser.endDate, qUser.login)).stream().map(user -> {
					user.setRoles(getUserRoles(user.getLogin()));
					return user;
				}).collect(Collectors.toList()), RepositoryHelper.getPageable(gridRequest), query.count());
	}

	private List<String> getUserRoles(String login) {
		return new JPAQuery(entityManager).from(qUserRole).where(qUserRole.user().login.eq(login))
				.orderBy(qUserRole.role().code.asc()).listResults(qUserRole.role().code).getResults();
	}

	public UserDTO getUserByLogin(final String login) {
		final Optional<User> findOneByLogin = userRepository.findOneByLogin(login);
		if (!findOneByLogin.isPresent()) {
			throw new RuntimeException("User " + login + " not found!");
		}
		final User user = findOneByLogin.get();
		return new UserDTO(user.getId(), user.getStartDate(), user.getEndDate(), login, getUserRoles(login),
				user.getPassword());
	}

	public List<String> getAllRoleCodes() {
		return new JPAQuery(entityManager).from(qRole).listResults(qRole.code).getResults();
	}

	public void deleteUserById(final Long id) {
		new JPADeleteClause(entityManager, qUserRole).where(qUserRole.user().id.eq(id)).execute();
		new JPADeleteClause(entityManager, qUser).where(qUser.id.eq(id)).execute();
	}

	public void activateUserById(final Long id) {
		new JPAUpdateClause(entityManager, qUser).where(qUser.id.eq(id)).setNull(qUser.endDate).execute();
	}

	public void deactivateUserById(final Long id) {
		new JPAUpdateClause(entityManager, qUser).where(qUser.id.eq(id)).set(qUser.endDate, new Date()).execute();
	}

	public void saveUser(final UserDTO userModel) {

		if (!StringUtils.isEmpty(userModel.getNewPassword())
				&& !userModel.getNewPassword().equals(userModel.getConfirmPassword())) {
			throw new RuntimeException("New password dose not match with the confirmed one.");
		}

		User user;
		if (userModel.getId() != null) {
			user = userRepository.findById(userModel.getId()).get();
		} else {
			user = new User();
			user.setStartDate(new Date());
		}
		user.setLogin(userModel.getLogin());
		if (userModel.getNewPassword() != null) {
			user.setPassword(passwordEncoder.encode(userModel.getNewPassword()));
		}

		user = userRepository.save(user);
		final Long userId = user.getId();
		userModel.getRoles().forEach(role -> {
			long count = new JPAQuery(entityManager).from(qUserRole).where(qUserRole.user().id.eq(userId))
					.where(qUserRole.role().code.eq(role)).count();
			if (count == 0l) {
				userRolesRepository.save(UserRole.builder().startDate(new Date()).user(userRepository.findById(userId).get())
						.role(rolesRepository.findOneByCode(role)).build());
			}
		});

	}

}
