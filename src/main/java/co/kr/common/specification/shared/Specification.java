package co.kr.common.specification.shared;

import co.kr.common.exception.SpecificationException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Getter
public abstract class Specification<T> {
	protected final List<String> customErrorMessageList = new ArrayList<>();

	private boolean isSatisfiedBy(T t) {
		this.customErrorMessageList.clear();
		return check(t);
	}

	public void validateException(T t) throws SpecificationException {
		if (!isSatisfiedBy(t)) {
			throw new SpecificationException(getErrorMessage());
		}
	}

	protected abstract boolean check(T t);

	protected void addErrorMessage(String errorMessage) {
		if (StringUtils.isNotBlank(errorMessage)) {
			customErrorMessageList.add(errorMessage);
		}
	}

	protected void addErrorMessageList(List<String> errorMessageList) {
		if (CollectionUtils.isNotEmpty(errorMessageList)) {
			customErrorMessageList.addAll(errorMessageList);
		}
	}

	protected String getErrorMessage() {
		String customErrorMessage = String.join(",", customErrorMessageList);

		return StringUtils.defaultIfBlank(customErrorMessage, getDefaultErrorMessage());
	}

	private String getDefaultErrorMessage() {
		return "Specification is not satisfied.";
	}
}
