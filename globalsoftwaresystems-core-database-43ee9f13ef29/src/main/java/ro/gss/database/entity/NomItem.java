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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nom_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NomItem implements Serializable {
	private static final long serialVersionUID = 4263599705412912880L;

	@Id
	@SequenceGenerator(name = "nom_items_id_seq", sequenceName = "nom_items_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nom_items_id_seq")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private NomType nomType;

	@NotNull
	private String value;

	@Override
	public String toString() {
		return "NomItem [id=" + id + ", code=" + code + ", value=" + value + "]";
	}

}