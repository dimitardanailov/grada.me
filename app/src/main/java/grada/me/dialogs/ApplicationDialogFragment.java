package grada.me.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import grada.me.R;
import grada.me.dialogs.enums.DialogType;

/**
 * A fragment that displays a dialog window, floating on top of its activity's window.
 * * This fragment contains a Dialog object, which it displays as appropriate based on the fragment's state.
 * * Control of the dialog (deciding when to show, hide, dismiss it) should be done through the API here, not with direct calls on the dialog.
 */
public class ApplicationDialogFragment extends DialogFragment {
    
    public Activity activity;
    public int title;
    public DialogType type;

    /**
     * Create ApplicationDialogFragment, providing "title" and type of dialog
     * @param activity
     * @param title
     * @param dialogType
     */
    public ApplicationDialogFragment(Activity activity, int title, DialogType dialogType) {
        // set Activity
        this.activity = activity;
        
        this.title = title;
        
        // set dialog type
        this.type = dialogType;
    }

    public Dialog onCrete(Bundle savedInstanceState) {
        return this.createDefaultApplicationError();
    }

    /**
     * Method will return Alert Dialog to display something is wrong with our application
     * @return Dialog
     */
    public Dialog createDefaultApplicationError() {
        int title = R.string.default_alert_dialog_title;

        Resources resources = getResources();
        String description = resources.getString(R.string.default_alert_dialog_description);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setIcon(R.drawable.alert_dialog_icon);
        alertDialog.setTitle(title);
        alertDialog.setMessage(description);
        
        // Set Positive Button
        alertDialog.setPositiveButton(R.string.default_alert_dialog_positive_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        
                    }
                }
        );

        return alertDialog.create();
    }
}
