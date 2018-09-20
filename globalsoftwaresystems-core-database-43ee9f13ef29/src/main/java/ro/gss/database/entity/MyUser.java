package ro.gss.database.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "my_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyUser implements Serializable {
	private static final long serialVersionUID = 5924170343176642528L;
	
	@Id
	@SequenceGenerator(name="my_users_id_seq", sequenceName="my_users_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="my_users_id_seq")
	private Long id;
		
	@Column(unique=true)
	@NotNull
	private String userName;
	
	private String password;
	
	private String email;
	
	private String address;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nom_item_id")
	private NomItem nomItem;
}
