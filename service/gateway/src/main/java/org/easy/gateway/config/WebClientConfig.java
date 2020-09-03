/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.easy.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Rob Winch
 * @since 5.1
 */
@Configuration
public class WebClientConfig {

	@Autowired
	private LoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction;

//	@Bean
//	@LoadBalanced
//	WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepository,
//			ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
//		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
//				clientRegistrationRepository, authorizedClientRepository);
//		oauth.setDefaultOAuth2AuthorizedClient(true);
//		return WebClient.builder().filter(oauth).filter(loadBalancerExchangeFilterFunction).build();
////        return WebClient.builder().filter(oauth).build();
//	}

	@Bean
	public WebClient webClient(){
		return WebClient.builder()
				.filter(loadBalancerExchangeFilterFunction)
				.build();
	}
}
