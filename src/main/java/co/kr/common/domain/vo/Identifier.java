package co.kr.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor @NoArgsConstructor
public class Identifier<ID> {
	private ID id;
}
