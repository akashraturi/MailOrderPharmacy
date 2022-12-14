package com.cts.refill.model.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.cts.refill.model.TokenValid;



class TokenValidTest {

	TokenValid token1 = new TokenValid();
	TokenValid token2 = new TokenValid("Uid","Name",true);
	
	@Test
	void testUid() {
		token1.setUid("Uid");
		assertEquals( "Uid", token1.getUid());
	}

	@Test
	void testName() {
		token1.setName("Name");
		assertEquals( "Name", token1.getName());
	}

	@Test
	void testIsValid() {
		token1.setValid(true);
		assertEquals( true, token1.isValid());
	}
	
	@Test
	void testToString() {
		String str = token1.toString();
		assertEquals(token1.toString(), str);
	}

}
