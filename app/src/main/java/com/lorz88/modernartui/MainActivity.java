package com.lorz88.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

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

    private SeekBar mSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        public static MoreInfoDialog newInstance() {
            return new MoreInfoDialog();
        }

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
}
