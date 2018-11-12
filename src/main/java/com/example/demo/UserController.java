package com.example.demo;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.atlassian.connect.spring.AtlassianHostRestClients;
import com.atlassian.connect.spring.AtlassianHostUser;

@Controller
@EnableJpaRepositories
public class UserController {

	@Autowired
	AtlassianHostRestClients restClients;

	@RequestMapping(value = "/cidiControl", method = RequestMethod.GET, produces = "application/json")
	public String cidiControl(@AuthenticationPrincipal AtlassianHostUser hostUser)
			throws IOException, JSONException, ParseException, InterruptedException, ExecutionException {
		try {
			// the code below is the API call using restClients - Error
			String response1A = restClients.authenticatedAs(hostUser).getForObject("/rest/api/space", String.class);
			System.out.println(response1A);

			// the code below is the API call using basic Authentication - Working!
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getInterceptors()
					.add(new BasicAuthorizationInterceptor("somexyz@senecaglobal.com", "somexyz"));
			String response2 = restTemplate
					.getForObject("https://seneca-global.atlassian.net/wiki/rest/api/latest/space/", String.class);
			System.out.println(response2);
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
