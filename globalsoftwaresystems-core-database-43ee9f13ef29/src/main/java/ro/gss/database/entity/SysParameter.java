package ro.gss.database.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sys_parameters")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysParameter implements Serializable {
	private static final long serialVersionUID = -6770695936999950589L;
	
	@Id
	@SequenceGenerator(name="sys_parameters_id_seq", sequenceName="sys_parameters_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sys_parameters_id_seq")
	private Long id;
	
	@Column(unique=true)
	private String code;
	
	@Column(name="string_value")
	private String stringValue;

	@Column(name="number_value")
	private Long numberValue;

	@Column(name="float_value")
	private Double floatValue;
	
}
