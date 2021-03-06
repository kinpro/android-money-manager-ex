package com.money.manager.ex.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.money.manager.ex.Constants;
import com.money.manager.ex.R;
import com.money.manager.ex.utils.MmxDatabaseUtils;
import com.nononsenseapps.filepicker.FilePickerActivity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Various methods that assist with the UI Android requirements.
 */
public class UIHelper {

    public static void pickFileDialog(Activity activity, String location, int requestCode) {
// This always works
        Intent i = new Intent(activity, FilePickerActivity.class);
        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, location);
        // Environment.getExternalStorageDirectory().getPath()

        activity.startActivityForResult(i, requestCode);
    }

    /**
     * Extracts the path to the selected database file.
     * @param data Intent
     * @return Path to the selected file.
     */
    public static String getSelectedFile(Intent data) {
        if (data == null) return null;

        String filePath = data.getData().getPath();

        // check if the db file is valid
        if (!MmxDatabaseUtils.isValidDbFile(filePath)) return null;

        return filePath;
    }

    public static Observable<Boolean> binaryDialog(final Context context, final int title, final int message) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                final MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title(title)
                        .content(message)
                        .positiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                subscriber.onNext(true);
                                subscriber.onCompleted();
                            }
                        })
                        .negativeText(android.R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                subscriber.onNext(false);
                                subscriber.onCompleted();
                            }
                        })
                        .build();

                // cleaning up
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        dialog.dismiss();
                    }
                }));

                // show the dialog
                dialog.show();
            }
        });
    }

    /*
        Instance
     */

    public UIHelper(Context context) {
        this.context = context;
    }

    private Context context;

    public Context getContext() {
        return this.context;
    }

    /**
     * Finds the theme color from an attribute.
     * @param attrId    Id of the attribute to parse. i.e. R.attr.some_color
     */
    public int getColor(int attrId) {
        TypedValue typedValue = new TypedValue();
        getContext().getTheme()
            .resolveAttribute(attrId, typedValue, true);
        return typedValue.data;
    }

    public int getDimenInDp(int dimenId) {
        int sizeInDp = (int) (getContext().getResources().getDimension(dimenId)
            / getContext().getResources().getDisplayMetrics().density);
        return sizeInDp;
    }

    public IconicsDrawable getIcon(IIcon icon) {
        Context context = getContext();

        UIHelper uiHelper = new UIHelper(context);

        return new IconicsDrawable(context)
                .icon(icon)
                .color(uiHelper.getPrimaryColor())
                .sizeDp(uiHelper.getToolbarIconSize());
    }

    public int getPrimaryColor() {
        return getColor(R.attr.toolbarItemColor);
    }

    public int getToolbarIconSize() {
        return getDimenInDp(R.dimen.mmx_toolbar_icon_size);
    }

    public boolean isDarkTheme() {
        Core core = new Core(getContext());
        return core.getThemeId() == R.style.Theme_Money_Manager_Dark;
    }

    /**
     * Resolve the id attribute into int value
     * @param attr id attribute
     * @return resource id
     */
    public int resolveIdAttribute(int attr) {
        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(attr, tv, true))
            return tv.resourceId;
        else
            return Constants.NOT_SET;
    }

//    public int dpToPx(int dp) {
//        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
//        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//        return px;
//    }
//
//    public int pxToDp(int px) {
//        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
//        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//        return dp;
//    }

//    /**
//     * Pick a database file using built-in file picker.
//     * @param locationPath ?
//     */
//    public void pickFileDialog(String locationPath, int requestCode) {
//        // root path should be the internal storage?
//        String root = Environment.getExternalStorageDirectory().getPath();
//
//        new MaterialFilePicker()
//                .withActivity((Activity) getContext())
//                .withRequestCode(requestCode)
//                .withRootPath(root)
//                .withPath(locationPath)
//                .withFilter(Pattern.compile(".*\\.mmb$"))
//                //.withFilterDirectories()
//                .withHiddenFiles(true)
//                .start();
//
//        // continues in onActivityResult in the parent activity
//    }

    public void showToast(int messageId) {
        showToast(messageId, Toast.LENGTH_SHORT);
    }

    public void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public void showToast(final int message, final int length) {
        Context context = getContext();
        if (!(context instanceof Activity)) return;

        final Activity parent = (Activity) context;

        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, length).show();
            }
        });
    }

    public void showToast(final String message, final int length) {
        Context context = getContext();
        if (!(context instanceof Activity)) return;

        final Activity parent = (Activity) context;

        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), message, length).show();
            }
        });
    }
}
