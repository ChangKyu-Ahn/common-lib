package co.kr.common.specification;

import co.kr.common.specification.shared.Specification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BooleanCheckSpec extends Specification<Boolean> {
  private final String errorMessage;

  @Override
  protected boolean check(Boolean aBoolean) {
    return Optional.ofNullable(aBoolean).orElse(false);
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }
}
