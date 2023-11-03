package daring.web.service;

import daring.web.config.auth.PrincipalDetails;
import daring.web.domain.User;
import daring.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("testtttttttttttttttttttt");
        System.out.println("username: "+username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    System.out.println("testt333333333333");
                    return new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
                });
        System.out.println("testttttttt22222222222");
        return new PrincipalDetails(user);
    }
}
