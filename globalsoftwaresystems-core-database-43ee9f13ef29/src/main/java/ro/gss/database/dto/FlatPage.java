package ro.gss.database.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class FlatPage<T> {
	private int totalPages;

	private long totalElements;

	private int number;

	private int size;

	private int numberOfElements;

	private List<T> content;

	private Sort sort;

	private boolean isFirst;

	private boolean isLast;

	public FlatPage() {
	}

	public FlatPage(Page<T> page) {
		totalPages = page.getTotalPages();
		totalElements = page.getTotalElements();
		number = page.getNumber();
		size = page.getSize();
		sort = page.getSort();
		isFirst = page.isFirst();
		isLast = page.isLast();
		content = page.getContent();
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(int numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

}
