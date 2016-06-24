package com.laioffer.laiofferproject;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class YelpApi {

    private static final String API_HOST = "api.yelp.com";
    private static final String SEARCH_PATH = "/v2/search";

    private static final String CONSUMER_KEY = "_OCgRiBEiGZ6s_TWfWxPOw";//"YOUR_CONSUMER_KEY";
    private static final String CONSUMER_SECRET = "KgkUf9Zz7j7YG0fovpv7cVgWvgw";//"YOUR_CONSUMER_SECRET";
    private static final String TOKEN = "3zNkfG9ehlVy_LUTKobXhxuB4T2HIdPB";	//"YOUR_TOKEN";
    private static final String TOKEN_SECRET = "NkpPLDyIQpwNtXmva2W_TiPnxfw";//"YOUR_TOKEN_SECRET";

    private OAuthService service;
    private Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     */
    public YelpApi() {
        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET).build();
        this.accessToken = new Token(TOKEN, TOKEN_SECRET);
    }

    /**
     * Fire a search request to Yelp API.
     */
    public String searchForBusinessesByLocation(String term, double lat, double lng, int searchLimit) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", lat + "," + lng);
        request.addQuerystringParameter("limit", String.valueOf(searchLimit));
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

}
