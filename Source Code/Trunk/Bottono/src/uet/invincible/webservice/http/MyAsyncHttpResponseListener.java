/*
 * Name: $RCSfile: MyAsyncHttpResponseListener.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 4:24:06 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package uet.invincible.webservice.http;

/**
 * Predefine some http listener methods
 * 
 */
public interface MyAsyncHttpResponseListener {
	/**
	 * Before get http response
	 */
	public void before();

	/**
	 * After get http response
	 * 
	 * @param statusCode
	 * @param response
	 */
	public void after(int statusCode, String response);
}
