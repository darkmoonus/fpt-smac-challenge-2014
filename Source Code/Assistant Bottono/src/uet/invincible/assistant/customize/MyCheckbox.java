package uet.invincible.assistant.customize;

import uet.invincible.assistant.configure.AppConfigure;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class MyCheckbox extends CheckBox {

	public MyCheckbox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyCheckbox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyCheckbox(Context context) {
		super(context);
		init();
	}

	private void init() {
		if (!isInEditMode()) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/" + AppConfigure.DEFAULT_FONT + ".ttf");
			setTypeface(tf);
		}
	}

}