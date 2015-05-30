/*
 * Name: $RCSfile: HttpContextFactory.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Jun 6, 2012 9:57:43 AM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */
package uet.invincible.webservice.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * HttpContextFactory supports get http context
 * 
 */
public class HttpContextFactory {

	private static HttpContextFactory instance;
	private Map<String, HttpContext> map;

	private HttpContextFactory() {
		map = new HashMap<String, HttpContext>();
	}

	public static HttpContextFactory newInstance() {
		if (instance == null) {
			instance = new HttpContextFactory();
		}
		return instance;
	}

	/**
	 * Get HTTP context for a specified domain
	 * 
	 * @param domain
	 * @return
	 */
	public HttpContext getHttpContext(String domain) {
		HttpContext httpContext = map.get(domain);
		if (httpContext == null) {
			CookieStore cookieStore = new BasicCookieStore();
			httpContext = new BasicHttpContext();
			httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			map.put(domain, httpContext);
		}
		return httpContext;
	}

}
