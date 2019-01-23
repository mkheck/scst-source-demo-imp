package com.thehecklers.sourcedemoimp;

import lombok.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@EnableBinding(Source.class)
@EnableScheduling
@SpringBootApplication
public class SourceDemoImpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceDemoImpApplication.class, args);
    }

}

@Component
class PingSource {
    private final Source source;
    private Long prefix = 0L;

    PingSource(Source source) {
        this.source = source;
    }

    @Scheduled(fixedRate = 1000)
    private void ping() {
        Ping ping = new Ping(prefix++ % 2 == 0 ? "A" : "B", UUID.randomUUID().toString(), Instant.now().toString());
        System.out.println(ping.toString());

        source.output().send(MessageBuilder.withPayload(ping).build());
    }
}

@Value
class Ping {
	private final String group, id, message;
}