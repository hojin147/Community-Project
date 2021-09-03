package com.bit.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
		
	private String userid;
	private String pw;
	private String name;
	private String email;
	private boolean mailcheck;
	private String phone;
	private String address;
	private String type;
}