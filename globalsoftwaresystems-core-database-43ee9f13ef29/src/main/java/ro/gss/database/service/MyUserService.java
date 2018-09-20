package ro.gss.database.service;

 
import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;


import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

import ro.gss.database.dto.GridRequestDTO;
import ro.gss.database.dto.MyUserDTO;
import ro.gss.database.dto.QMyUserDTO;

import ro.gss.database.entity.MyUser;
import ro.gss.database.entity.QMyUser;

import ro.gss.database.repo.MyUsersRepository;
import ro.gss.database.repo.NomItemsRepository;
import ro.gss.database.utils.RepositoryHelper;

@Service
@Transactional
public class MyUserService {
	
	@PersistenceContext
	private EntityManager entityManager;

	private final MyUsersRepository myUserRepository;
	private @Autowired NomItemsRepository nomItemsRepository;
	
	private final QMyUser qUser = QMyUser.myUser;
	
	public MyUserService(MyUsersRepository myUserRepository, NomItemsRepository nomItemsRepository) {
		this.myUserRepository = myUserRepository;
		this.nomItemsRepository = nomItemsRepository;
		// TODO Auto-generated constructor stub
	}
	
	public Collection<MyUserDTO> getAllUsers(){
		Collection<MyUser> c =  myUserRepository.findAll();
		return c.stream().map(user -> new MyUserDTO(user)).collect(Collectors.toList());

	}
	
	public PageImpl<MyUserDTO> getUsersWithPagination(final GridRequestDTO gridRequest) {

		final PathBuilder<MyUser> entityPath = new PathBuilder<>(qUser.getType(), qUser.getMetadata());
		final Predicate pathPredicate = RepositoryHelper.getPredicate(gridRequest, entityPath);
		final OrderSpecifier<String> sorterPath = RepositoryHelper.getSorter(gridRequest, entityPath);

		final JPAQuery query = new JPAQuery(entityManager).from(qUser).where(pathPredicate);
		if (sorterPath != null) {
			query.orderBy(sorterPath);
		}
		RepositoryHelper.setPaginationOnQuery(gridRequest, query);
		System.out.println(gridRequest.getFilters());
		return new PageImpl<MyUserDTO>(
				query.list(new QMyUserDTO(qUser.id, qUser.userName, qUser.password, qUser.email, qUser.address, qUser.nomItem().value)).stream()
				.collect(Collectors.toList()), RepositoryHelper.getPageable(gridRequest), query.count());
	}
	
	//add for the controller to use
	public Long addUser(MyUserDTO user) {
		MyUser newUser = new MyUser();
		newUser.setUserName(user.getUserName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setAddress(user.getAddress());
		newUser.setNomItem(
				nomItemsRepository.findNomItemByCode(user.getGender().toUpperCase()));
		return myUserRepository.save(newUser).getId();
	}


	public void deleteUser(Long id) {
		myUserRepository.deleteById(id);
		
	}
	
	public void updateUser(MyUserDTO newUser) {
		MyUser user = myUserRepository.getOne(newUser.getId());
		user.setUserName(newUser.getUserName());
		user.setEmail(newUser.getEmail());
		user.setPassword(newUser.getPassword());
		user.setAddress(newUser.getAddress());
		user.setNomItem(
		nomItemsRepository.findNomItemByCode(newUser.getGender().toUpperCase()));
		
		
	}

	
}
