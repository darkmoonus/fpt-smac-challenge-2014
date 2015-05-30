/*
 * Name: $RCSfile: MyAsyncHttpResponseListener.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 4:24:06 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package uet.invincible.listeners;

public interface MyAsyncHttpResponseListener {
	public void before();

	public void after(int statusCode, String response);
}
