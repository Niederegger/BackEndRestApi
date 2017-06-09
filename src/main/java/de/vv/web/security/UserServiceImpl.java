package de.vv.web.security;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import de.vv.web.model.Account;
import de.vv.web.model.UserRoles;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void save(Account user) {
		user.password(bCryptPasswordEncoder.encode(user.password));
		user.role(new HashSet<>(UserRoles.roles.keySet()));
		userRepository.save(user);
	}

	@Override
	public Account findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}