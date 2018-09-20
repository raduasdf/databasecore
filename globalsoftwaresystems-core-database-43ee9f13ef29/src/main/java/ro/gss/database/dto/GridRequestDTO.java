package ro.gss.database.dto;

import java.util.Arrays;
import java.util.List;

public class GridRequestDTO {
    private Integer page;
    private Integer nrElements;
    private List<SorterDTO> sorters;
    private List<FilterDTO> filters;

    public List<FilterDTO> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDTO> filters) {
        this.filters = filters;
    }

    public List<SorterDTO> getSorters() {

        return sorters;
    }

    public void setSorters(List<SorterDTO> sorters) {
        this.sorters = sorters;
    }

    public Integer getNrElements() {

        return nrElements;
    }

    public void setNrElements(Integer nrElements) {
        this.nrElements = nrElements;
    }

    public Integer getPage() {

        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "page:"+page+"nrElements:"+nrElements+ Arrays.toString(filters.toArray());
    }
}