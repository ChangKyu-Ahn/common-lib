package co.kr.common.security.jwt;

import co.kr.common.security.jwt.dto.JwtUserDetailDto;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
  private JwtUserDetailDto jwtUserDetailDto;

  @Override
  public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
    final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + jwtUserDetailDto.getType()));

    return new User(jwtUserDetailDto.getUserId(), "N/A", grantedAuthorities);
  }

  public UserDetails loadUserByUsername(JwtUserDetailDto jwtUserDetailDto) throws UsernameNotFoundException {
    setJwtUserDetailDto(jwtUserDetailDto);

    return loadUserByUsername(jwtUserDetailDto.getUserId());
  }

  private void setJwtUserDetailDto(JwtUserDetailDto jwtUserDetailDto) {
    this.jwtUserDetailDto = jwtUserDetailDto;
  }
}
