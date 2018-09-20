package ro.gss.database.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nom_types")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NomType implements Serializable {
	private static final long serialVersionUID = 5625351743593367803L;

	@Id
	@SequenceGenerator(name = "nom_types_id_seq", sequenceName = "nom_types_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nom_types_id_seq")
	private Long id;

	@Column(unique = true)
	private String code;

	@Column(name = "start_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "end_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	private String name;

	private String description;

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nomType")
	@JsonIgnore
	private Set<NomItem> nomItems;
	
	@Override
	public String toString() {
		return "NomItem [id=" + id + ", code=" + code + ", name=" + name  + ", description=" + description + "]";
	}
}
