package com.musicboxsystem.server;

import com.musicboxsystem.server.domain.Albums;
import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.domain.Members;
import com.musicboxsystem.server.domain.Users;
import com.musicboxsystem.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
//@SpringBootApplication
@Configuration
@ComponentScan
public class MusicboxSystemApplication implements CommandLineRunner{


	@Autowired
	public BandsService bandsService;

	@Autowired
	public AlbumsService albumsService;

	@Autowired
	public UsersService usersService;

	@Autowired
	public MembersService membersService;


	public static void main(String[] args) {
		SpringApplication.run(MusicboxSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
