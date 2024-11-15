package co.kr.common.specification;

import co.kr.common.exception.SpecificationException;
import co.kr.common.specification.shared.Specification;
import co.kr.common.specification.shared.SpecificationTask;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
@NoArgsConstructor
public class SpecificationExecutor {
	private final List<SpecificationTask<?>> specificationTasks = new ArrayList<>();

	public <T> void addSpecification(Specification<T> specification, T target) {
		specificationTasks.add(new SpecificationTask<>(specification, target));
	}

	public void validateAll() throws SpecificationException {
		List<String> errorMessageList = new ArrayList<>();

		specificationTasks.forEach(task -> {
			try {
				task.validate();
			} catch (SpecificationException e) {
				log.error("#### Specification Error : {}", e.getErrorMessages(), e);
				errorMessageList.add(e.getMessage());
			}
		});

		if (CollectionUtils.isNotEmpty(errorMessageList)) {
			throw new SpecificationException(String.join("\n", errorMessageList));
		}
	}

	public void validateEach() throws SpecificationException {
		specificationTasks.forEach(SpecificationTask::validate);
	}


}
