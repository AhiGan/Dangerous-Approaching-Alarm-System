package com.server.test;

import java.io.IOException;
import java.io.Serializable;

import com.server.service.Server;

public class ServerTest implements Serializable {
	public static void main(String[] args) throws IOException
	{
		Server server = new Server();
	}
}
