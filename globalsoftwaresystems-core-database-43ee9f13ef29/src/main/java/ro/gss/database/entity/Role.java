package ro.gss.database.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
	private static final long serialVersionUID = 8659475553763718904L;
	
	@Id
	@SequenceGenerator(name="roles_id_seq", sequenceName="roles_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="roles_id_seq")
	private Long id;
	
	@Column(unique=true)
	private String code;
	
	@Column(name = "start_date", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	@Column(name = "end_date", columnDefinition= "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
}
