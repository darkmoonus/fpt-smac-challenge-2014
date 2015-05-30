package uet.invincible.assistant.bases;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Base linear layout
 * 
 * @author khanhnv
 */
public class BaseLinearLayout extends LinearLayout
{
    /**
     * Constructor
     * 
     * @param context
     */
    public BaseLinearLayout(Context context)
    {
        super(context);
    }

    /**
     * Constructor
     * 
     * @param context
     * @param attrs
     */
    public BaseLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    /**
     * Constructor
     * 
     * @param context
     * @param resId
     */
    public void initLayout(Context context, int resId)
    {
        LayoutInflater layoutInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflate.inflate(resId, this, true);
    }
}