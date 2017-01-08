package com.musicboxsystem.server;

import com.musicboxsystem.server.domain.Albums;
import com.musicboxsystem.server.domain.Bands;
import com.musicboxsystem.server.service.AlbumsService;
import com.musicboxsystem.server.service.BandsService;
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

	public static void main(String[] args) {
		SpringApplication.run(MusicboxSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Bands obj = new Bands("AC/DC", "desc","08.01.2017", "leader1", "active");
		bandsService.create(obj);

		Albums albumObj = new Albums("TNT","albumdesc","08.01.2017", "651364");
		albumsService.create(albumObj);



	}
}
