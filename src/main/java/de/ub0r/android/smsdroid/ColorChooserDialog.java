package de.ub0r.android.smsdroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ColorChooserDialog extends DialogFragment implements View.OnClickListener {

    private ColorCallback mCallback;
    private int[] mColors;
    private GridView mGrid;
    private Toast mToast;
    private String TAG = "ColorChooser";
    Boolean dark;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (ColorCallback) activity;
        dark = ThemeUtils.isDarkMode(activity);

    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            final int index = (Integer) v.getTag();
            getArguments().putInt("preselect", mColors[index]);
            invalidateGrid();
        }
    }

    public static interface ColorCallback {
        void onColorSelection(int title, int color);
    }

    public ColorChooserDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(getArguments().getInt("title", 0))
                .autoDismiss(false)
                .customView(R.layout.dialog_color_chooser, false)
                .neutralText(R.string.defaultStr)
                .theme(dark ? Theme.DARK : Theme.LIGHT)
                .positiveText(R.string.done)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        final int title = getArguments().getInt("title", 0);
                        final int preselect = getArguments().getInt("preselect", -1);
                        mCallback.onColorSelection(title, preselect);
                        dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        if (getArguments().getInt("title", 0) == R.string.primary_color) {
                            getArguments().putInt("preselect", getResources().getColor(R.color.cabinet_color));
                        }
                        invalidateGrid();
                    }
                })
                .build();

        final boolean primary = getArguments().getInt("title", 0) == R.string.primary_color ||
                getArguments().getInt("title", 0) == R.string.thumbnail_color;
        final TypedArray ta = getActivity().getResources().obtainTypedArray(
                primary ? R.array.colors_primary : R.array.colors_accent);
        mColors = new int[ta.length()];

        for (int i = 0; i < ta.length(); i++)
            mColors[i] = ta.getColor(i, 0);
        ta.recycle();
        mGrid = (GridView) dialog.getCustomView();
        invalidateGrid();
        return dialog;
    }

    private void invalidateGrid() {
        if (mGrid.getAdapter() == null) {
            mGrid.setAdapter(new ColorGridAdapter());
            mGrid.setSelector(getResources().getDrawable(R.drawable.md_transparent));
        } else ((BaseAdapter) mGrid.getAdapter()).notifyDataSetChanged();
    }

    private class ColorGridAdapter extends BaseAdapter implements View.OnClickListener {

        public ColorGridAdapter() {
        }

        @Override
        public int getCount() {
            return mColors.length;
        }

        @Override
        public Object getItem(int position) {
            return mColors[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.griditem_color_chooser, null);
            final boolean dark = ThemeUtils.isDarkMode(getActivity());
            CircleView child = (CircleView) convertView;
            child.setActivated(getArguments().getInt("preselect") == mColors[position]);
            child.setBackgroundColor(mColors[position]);
            child.setBorderColor(dark ? Color.WHITE : Color.BLACK);
            child.setTag(position);
            child.setOnClickListener(this);

            Drawable selector = createSelector(mColors[position]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int[][] states = new int[][]{
                        new int[]{android.R.attr.state_pressed}
                };
                int[] colors = new int[]{
                        shiftColorDown(mColors[position])
                };
                ColorStateList rippleColors = new ColorStateList(states, colors);
                child.setForeground(new RippleDrawable(rippleColors, selector, null));
            } else {
                child.setForeground(selector);
            }
            return convertView;
        }

        @Override
        public void onClick(View v) {
            final int index = (Integer) v.getTag();
            getArguments().putInt("preselect", mColors[index]);
             invalidateGrid();
        }
    }

    public static int shiftColorDown(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f; // value component
        return Color.HSVToColor(hsv);
    }

    public static int shiftColorUp(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.1f; // value component
        return Color.HSVToColor(hsv);
    }

    public static int translucentColor(int color) {
        final float factor = 0.7f;
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    static Drawable createSelector(int color) {
        ShapeDrawable darkerCircle = new ShapeDrawable(new OvalShape());
        darkerCircle.getPaint().setColor(translucentColor(shiftColorDown(color)));
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, darkerCircle);
        return stateListDrawable;
    }

    public void show(Activity context, int title, int preselect) {
        Bundle args = new Bundle();
        try {

         //   final boolean dark =ThemeUtils.isDarkMode(this);
         //   setTheme(dark ? R.style.appCompatDark:R.style.appCompatLight);

        args.putInt("preselect", preselect);
        args.putInt("title", title);
        setArguments(args);
        show(context.getFragmentManager(), "COLOR_SELECTOR");

    } catch (Exception e) {
        Log.e("Preferences", e.toString());
    }
    }
}
