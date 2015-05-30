package uet.invincible.utilities;

import android.util.Log;

public class EmailSender {
	public static void sendEmail(final String userName, final String password, 
			final String name, final String[] receiveList, final String subject, 
			final String content, final String[] attachLinks) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Mail m = new Mail(userName, password);
				m.setTo(receiveList);
				m.setFrom(name);
				m.setSubject(subject);
				m.setBody(content);
				try {
				 if(attachLinks != null) {
					  for(int i=0; i<attachLinks.length; i++) {
						  m.addAttachment(attachLinks[i]);
					  }
				  }
				  if(m.send()) {
				    Log.e("Viadroid Emailsender", "success");
				  } else {
					  Log.e("Viadroid Emailsender", "failed");
				  }
				} catch(Exception e) {
				  Log.e("Viadroid Emailsender", "Could not send email", e);
				}
			}
		}).start();
	}
}
