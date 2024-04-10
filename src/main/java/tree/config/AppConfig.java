package tree.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import tree.converter.NodeSerializer;
import tree.model.Node;

/**
 * Configuration for Jackson mappers.
 * Mappers for the following types:
 *  - Node
 */
@Configuration
public class AppConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializerByType(Node.class, new NodeSerializer());
        return builder;
    }
}