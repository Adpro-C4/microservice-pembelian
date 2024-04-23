package com.adpro.pembelian.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
    UserDetails getUserDetails(Long userId);
}
