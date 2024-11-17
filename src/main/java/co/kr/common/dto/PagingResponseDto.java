package co.kr.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagingResponseDto {

	private int code;
	private String message;
	private String error;
	private long totalCount;
	private int totalPage;
	private List<?> response;

	public PagingResponseDto(int code, Page<?> page) {
		this(code, null, null, page.getTotalElements(), page.getTotalPages(), page.getContent());
	}
}