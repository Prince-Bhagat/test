package com.test.test.Controller;


import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@RestController
public class TestController {
	private WebClient webClient;
	
	TestController(){
		webClient = WebClient.builder()
				.baseUrl("http://localhost:6001")
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
				.build();
	}
	
	@GetMapping(path ="/get",produces = APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Mono<DataBuffer>> get() throws IOException {
		System.out.println("get called ");
		Flux<DataBuffer> dataBufferFlux = webClient
				.get()
				.uri("/document?userId=user70&artifactId=Form70&productId=Form69Product&artifactType=Member-Enrolment-Form&artifactGroup=Group69")
				.retrieve()
				.bodyToFlux(DataBuffer.class);
				
		Mono<DataBuffer> array = DataBufferUtils.join(dataBufferFlux);
		return ResponseEntity
				.status(HttpStatus.OK)
				.header("Content-Disposition",String.format("attachment; filename=\"%s\"", "Form70" + ".pdf"))
				.contentType(MediaType.APPLICATION_PDF)
				.body(array);
	}
	
}
