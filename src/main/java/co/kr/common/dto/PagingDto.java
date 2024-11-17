package co.kr.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PagingDto {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer pageNo;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer pageSize;

	public Pageable pageable() {
		return PageRequest.of(
			ObjectUtils.defaultIfNull(pageNo, 1) - 1,
			ObjectUtils.defaultIfNull(pageSize, 10)
		);
	}
}
