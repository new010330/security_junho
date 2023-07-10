package com.study.security_junho.service.auth;

import com.study.security_junho.web.dto.auth.SignupReqDto;
import com.study.security_junho.web.dto.auth.UsernameCheckReqDto;

public interface AuthService {
	public boolean checkUsername(UsernameCheckReqDto usernameCheckReqDto) throws Exception;
	public boolean signup() throws Exception;
}
