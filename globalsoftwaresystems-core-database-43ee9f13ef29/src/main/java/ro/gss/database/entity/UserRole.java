package ro.gss.database.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements Serializable {
	private static final long serialVersionUID = 2608384881782066329L;
	
	@Id
	@SequenceGenerator(name="user_roles_id_seq", sequenceName="user_roles_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_roles_id_seq")
	private Long id;
	
	@Column(name = "start_date", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Column(name = "end_date", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="role_id")
	private Role role;	
}
