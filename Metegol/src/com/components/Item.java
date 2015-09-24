package com.components;

import android.content.Context;
import android.view.View;

/**
 * Interface that is used to return a view within AdapterGeneric class.
 *
 * @author Carlos - InfinixSoft
 */

public interface Item {

    public Object getData();

    /**
     * Set object with the information necessary to display the view.
     */

    public void setData(Object data);

    /**
     * Returns a new View or reuse some already created.
     *
     * @param view
     * @param context
     * @return
     */

    public View getView(View view, Context context);

}
