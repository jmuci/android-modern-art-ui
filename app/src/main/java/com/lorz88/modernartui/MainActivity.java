package com.lorz88.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * This application's user interface is composed of geometric shapes arranged in a particular order.
 * This application's user interface has one area containing multiple colored rectangles and another
 * containing a SeekBar (sometimes called a Slider). When the user drags the SeekBar, all non-white /
 * non- grey rectangles gradually change their color.
 *
 * This specific implementation was inspired on the works Sadie Benning, here is a link to the artist's
 * work : http://www.moma.org/collection/browse_results.php?object_id=187158
 *
 * App developed for learning purposes for the Coursera Android Development Course.
 *
 * @author Jose M Mucientes
 */

public class MainActivity extends ActionBarActivity {

    private static final int MENU_MORE_INFO = Menu.FIRST;
    private static final String TAG = "ModernArtUI";
    public static final String ARTIST_URL = "http://www.moma.org/collection/browse_results.php?criteria=O%3ADE%3AI%3A5%7CG%3AHI%3AE%3A1&page_number=1&template_id=1&sort_order=2";
    private TextView[][] mTVArray = new TextView[5][5];
    private int [][] mOriginalColors = new int[5][5];
    private SeekBar mSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_linear_layout);

        mSeekbar = (SeekBar) findViewById(R.id.seekbar);

        // First we iterate through the Layout Views
        //NOTE -1 is substracted as we don't want to include the seek bar.
        for (int i = 0; i < ((ViewGroup)mainLayout).getChildCount()-1; i++) {
            Log.d(TAG, "Iterating now through Layouts i: "+i);
            //Iterate through the TextViews
            View childLinLayout = ((ViewGroup)mainLayout).getChildAt(i);
            for (int t = 0; t < ((ViewGroup)childLinLayout).getChildCount(); t++) {
                Log.d(TAG, ">> Iterating now through Layouts t: "+t+"ArrayPos: "+((int)i+(int)t));
                TextView textView = (TextView) ((ViewGroup) childLinLayout).getChildAt(t);
                mTVArray[i][t] = textView;

                int color = ((ColorDrawable) textView.getBackground()).getColor();
                mOriginalColors[i][t] = color;
            }
        }

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                for (int i= 0; i < mTVArray.length; i++) {
                    for (int j=0; j <mTVArray[i].length; j++) {
                        int [] transition_index  = {0,0};
                        int color;
                        // If tile in the right edge and not in bottom right corner
                        if (j == 4 && i != 4) {
                            transition_index[0] = i+1;
                            transition_index[1] = 2;
                        } else if (i == 4 && j == 4) {
                            transition_index[0] = 0;
                            transition_index[1] = 1;
                            Log.d(TAG, "Bottom right");
                        } else {
                            transition_index[0] = i;
                            transition_index[1] = j+1;
                        }
                        Log.d(TAG, "Current Index: x "+ i + " y "+ j+ " Change to: x "+transition_index[0]+" y "+transition_index[1]);

                        color = transitionColor(mOriginalColors[i][j],
                                                mOriginalColors[transition_index[0]][transition_index[1]],
                                                (float) progress / 30);
                        mTVArray[i][j].setBackgroundColor(color);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(Menu.NONE, MENU_MORE_INFO, Menu.NONE, "More information");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == MENU_MORE_INFO) {
                createDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MoreInfoDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.opts_dialog_message)
                    .setPositiveButton(R.string.opts_dialog_visit_moma, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "On Click for Create Dialog");
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ARTIST_URL));
                            startActivity(browserIntent);
                        }
                    })
                    .setNegativeButton(R.string.opts_dialog_not_visit, null);
            return builder.create();
        }

    }

    private void createDialog() {
        DialogFragment dialogFragment = new MoreInfoDialog();
        dialogFragment.show(getFragmentManager(), "More Info");

    }

    private static int transitionColor(int startColor, int finalColor, float percentage) {
        int transitionRed = (int) Color.red(startColor) + Math.round((Color.red(finalColor)-Color.red(startColor)) * (float) percentage);
        int transitionGreen = (int) Color.green(startColor) + Math.round((Color.green(finalColor)-Color.green(startColor)) * (float) percentage);
        int transitionBlue = (int) Color.blue(startColor) + Math.round((Color.blue(finalColor)-Color.blue(startColor)) * (float) percentage);
        return Color.argb(Color.alpha(startColor), transitionRed, transitionGreen, transitionBlue);
    }
}
