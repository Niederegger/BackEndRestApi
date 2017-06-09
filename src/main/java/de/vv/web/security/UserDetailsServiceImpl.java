package de.vv.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.vv.web.db.DBCon;
import de.vv.web.model.Account;
import de.vv.web.model.UserRoles;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account user = DBCon.getAccountByEmail(email);//userRepository.findByUsername(email);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (String role : UserRoles.roles.keySet()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(user.username, user.password, grantedAuthorities);
    }

}