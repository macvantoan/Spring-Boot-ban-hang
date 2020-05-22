package com.toan.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import com.toan.spring.dao.AccountDAO;
import com.toan.spring.entity.Account;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountDAO accountDAO;

	@Override	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountDAO.findAccount(username);
		
		if (account == null) {
			throw new UsernameNotFoundException("User" + username + "khong ton tai");
		}
		String role = account.getUserRole();
		List<GrantedAuthority> grandtList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority(role);
		grandtList.add(authority);

		boolean enabled = account.isActive();
		boolean accountNonExpored = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		UserDetails userDetails = (UserDetails) new User(account.getUsername(), account.getEncrytedPassword(), enabled,
				accountNonExpored, credentialsNonExpired, accountNonLocked, grandtList);
		return userDetails;
	}

}
