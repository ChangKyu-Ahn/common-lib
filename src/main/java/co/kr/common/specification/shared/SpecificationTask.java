package co.kr.common.specification.shared;

import co.kr.common.exception.SpecificationException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SpecificationTask<T> {
	private Specification<T> specification;
	private T target;


	public void validate() throws SpecificationException {
		specification.validateException(target);
	}
}
