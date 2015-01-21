package grada.me.googlemaps;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Class will add two listeners, when user want to draw something on our map and when user stop this process.
 */
public class MapWrapperLayout extends FrameLayout {

    /**
     * View.OnDragListener: 
     *  Interface definition for a callback to be invoked when a drag is being dispatched to this view.
     *  The callback will be invoked before the hosting view's own onDrag(event) method.
     *  If the listener wants to fall back to the hosting view's onDrag(event) behavior,
     *  it should return 'false' from this callback.
     */
    private OnDragListener mOnDragListener;
    
    public MapWrapperLayout(Context context) {
        super(context);
    }

    /**
     * Setter mOnDragListener
     * @param mOnDragListener
     */
    public void setOnDragListener(OnDragListener mOnDragListener) {
        this.mOnDragListener = mOnDragListener;
    }

    public interface OnDragListener {
        public void onDrag(MotionEvent motionEvent);
        public void onDragStop(MotionEvent motionEvent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mOnDragListener != null) {
            mOnDragListener.onDrag(event);

            mOnDragListener.onDragStop(event);
        }
        
        // Pass the touch screen motion event down to the target view, or this view if it is the target.
        boolean viewIsTarget = super.dispatchTouchEvent(event);
        
        return viewIsTarget;
    }
}
