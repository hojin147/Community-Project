package com.bit.vo;

import com.bit.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GallerysVO {

	private String galname;
	private String category;
	private Member master;
}