package de.vv.web.security;

import de.vv.web.model.Account;

public interface UserService {
	void save(Account user);

	Account findByUsername(String username);
}