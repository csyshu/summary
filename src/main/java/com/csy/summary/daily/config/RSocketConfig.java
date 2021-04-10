package com.csy.summary.daily.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeTypeUtils;

/**
 * @author csy
 * description
 * @date create in 17:48 2019/10/29
 */
@Configuration
public class RSocketConfig {
    // @Bean
    // public RSocket rSocket() {
    //     return RSocketFactory
    //             .connect()
    //             .mimeType(MimeTypeUtils.APPLICATION_JSON_VALUE, MimeTypeUtils.APPLICATION_JSON_VALUE)
    //             .frameDecoder(PayloadDecoder.ZERO_COPY)
    //             .transport(TcpClientTransport.create(7000))
    //             .start()
    //             .block();
    // }
    //
    // @Bean
    // RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
    //     return RSocketRequester.wrap(rSocket(), MimeTypeUtils.APPLICATION_JSON, MimeTypeUtils.APPLICATION_JSON, rSocketStrategies);
    // }

}
