package com.miniProject.AeroScale.Redis;

import com.miniProject.AeroScale.SellerModule.DTO.Response.AnalyticsResponse;
import com.miniProject.AeroScale.order.dto.response.OrdersResponseOfBuyer;
import com.miniProject.AeroScale.product.cache.ProductWindow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.*;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisSerializer<AnalyticsResponse> analyticsResponseRedisSerializer = new JacksonJsonRedisSerializer<>(AnalyticsResponse.class);
        RedisSerializer<ProductWindow> productResponseRedisSerializer = new JacksonJsonRedisSerializer<>(ProductWindow.class);

        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        RedisCacheConfiguration sellerAnalytics = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(analyticsResponseRedisSerializer)
                );

        configurationMap.put("Seller:Analytics", sellerAnalytics);


        RedisCacheConfiguration sellerProducts = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(2))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(productResponseRedisSerializer)
                );
        configurationMap.put("Seller:Products", sellerProducts);
        

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(configurationMap)
                .build();

    }
}