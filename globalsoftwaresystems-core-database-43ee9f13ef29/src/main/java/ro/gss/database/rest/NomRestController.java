package ro.gss.database.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.gss.database.dto.FlatPage;
import ro.gss.database.dto.GenericDTO;
import ro.gss.database.dto.GridConfigDTO;
import ro.gss.database.dto.GridRequestDTO;
import ro.gss.database.dto.NomItemDTO;
import ro.gss.database.dto.NomenclatureDTO;
import ro.gss.database.service.NomService;

@RestController
@RequestMapping("/nom")
@AllArgsConstructor
@Slf4j
public class NomRestController {

	private final @Autowired NomService nomService;

	@GetMapping("/nomTypes")
	public List<NomItemDTO> findAll() {
		return nomService.getAllNomTypesNames();
	}

	@GetMapping("/nomItems/{typeCode}")
	public List<NomItemDTO> getAllNomValuesByTypeCode(@PathVariable(value = "typeCode") String typeCode) {
		return nomService.getNomenclatureItemsByType(typeCode);
	}
	
	@RequestMapping(value = "/nomenclatureList", method = RequestMethod.GET)
	public List<GenericDTO> getAllNomItems() {
		return nomService.getAllNomenclatureItems();
	}
	
	@RequestMapping(value = "/nomTypesList", method = RequestMethod.GET)
	public List<GenericDTO> getAllNomTypes() {
		return nomService.getAllNomenclatureTypes();
	}
	
	@RequestMapping(value = "/nomListByTypeCode/{typeCode}", method = RequestMethod.GET)
	public List<GenericDTO> getAllNomItemsbytTypeCode(@PathVariable(value = "typeCode") String typeCode) {
		return nomService.getNomenclatureItemsByTypeCode(typeCode);
	}
	
	@RequestMapping(value = "/nomListByCode/{typeCode}", method = RequestMethod.GET)
	public List<String> getAllNomCodesbytTypeCode(@PathVariable(value = "typeCode") String typeCode) {
		return nomService.getNomenclatureCodesByTypeCode(typeCode);
	}
	
	@RequestMapping(value = "/management/addNomenclature", method = RequestMethod.POST)
	public void addNomenclaure(@RequestBody NomenclatureDTO nomenclatureDTO) {
		nomService.addNomenclature(nomenclatureDTO);
	}
	
	@RequestMapping(value = "/management/removeNomenclature/{id}", method = RequestMethod.DELETE)
	public void removeNomenclature(@PathVariable(value = "id") Long id) {
		nomService.deleteNomenclature(id);
	}
	
	@RequestMapping(value = "/management/nomenclatures/columns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GridConfigDTO> getNomenclaturesColumns() {
		log.info("Getting columns for the noemclatures table.");
		List<GridConfigDTO> ret = new ArrayList<>();
		ret.add(new GridConfigDTO("id", false, "NUMBER", "=", true, "id"));
		ret.add(new GridConfigDTO("code", true, "STRING", ":", false, "code"));
		ret.add(new GridConfigDTO("value", true, "STRING", ":", false, "value"));
		ret.add(new GridConfigDTO("startDate", true, "DATE_TIME", ">=", false, "startDate"));
		ret.add(new GridConfigDTO("endDate", true, "DATE_TIME", "<=", false, "endDate"));
		return ret;
	}
	
	@RequestMapping(value = "/management/nomenclatures/list/{typeCode}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public FlatPage<NomenclatureDTO> getNomenclaturesList(@PathVariable(value = "typeCode") String typeCode,
			@RequestBody GridRequestDTO gridRequest) {
		log.info("Getting list of nomenclatures.");
		FlatPage<NomenclatureDTO> flatPage = new FlatPage<>(
				nomService.getNomenclaturesWithPagination(gridRequest, typeCode));
		return flatPage;
	}
	
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FlatPage<NomenclatureDTO>> getNomList(@RequestBody GridRequestDTO gridRequest, @RequestParam(value = "typeCode", required=false) String typeCode) {
		if(typeCode.isEmpty() || typeCode == null) {
			return ResponseEntity.ok().body(new FlatPage<>(nomService.getPaginatedNomenclatures(gridRequest)));
		}
		return ResponseEntity.ok().body(new FlatPage<>(nomService.getNomenclaturesWithPagination(gridRequest, typeCode))); 
		
	}
}
