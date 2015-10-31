/**
 * Copyright (C) 2015 RandomByte apps.randombyte@gmail.com
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not,
 * see <http://www.gnu.org/licenses/>
 */
package de.randombyte.lmgtfy_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.search_text) SearchBox searchBox;
    @InjectView(R.id.link_preview) TextView previewTextView;
    @InjectView(R.id.action_buttons_bar) ViewGroup actionButtonBar;
    @InjectView(R.id.copy_link_to_clipboard) Button copyLinkButton;
    @InjectView(R.id.share_link) Button shareLinkButton;
    @InjectView(R.id.open_link) Button openPreviewButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        searchBox.enableVoiceRecognition(this);
        searchBox.setLogoText("");
        searchBox.setMenuVisibility(View.INVISIBLE);
        searchBox.setSearchListener(new SearchBox.SearchListener() {
            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {

            }

            @Override
            public void onSearchTermChanged(String searchTerm) {
                searchTerm = searchTerm.trim();
                boolean emptySearchTerm = searchTerm.isEmpty();
                enableRecursive(actionButtonBar, !emptySearchTerm);
                previewTextView.setText(emptySearchTerm ? "" : Lmgtfy.createLink(searchTerm));
            }

            @Override
            public void onSearch(String s) {

            }

            @Override
            public void onResultClick(SearchResult searchResult) {

            }
        });

        copyLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE))
                        .setPrimaryClip(ClipData.newPlainText("Lmgtfy-Link",
                                Lmgtfy.createLink(searchBox.getSearchText())));
                Toast.makeText(MainActivity.this, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });

        shareLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Lmgtfy.createLink(searchBox.getSearchText()));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_with)));
            }
        });

        openPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Lmgtfy.createLink(searchBox.getSearchText())));
                startActivity(intent);
            }
        });

        // at start searchBox is empty, placed here because setOnClickListener makes them visible again
        enableRecursive(actionButtonBar, false);
    }

    /**
     * Enables/Disables and sets alpha to every child of the given ViewGroup
     * @param viewGroup ViewGroup whose children will be enabled/disabled
     * @param enabled True to enable, false to disable children
     */
    private static void enableRecursive(ViewGroup viewGroup, boolean enabled) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            view.setAlpha(enabled ? .87f : .26f);
            view.setClickable(enabled);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.about_text)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0) {
                searchBox.populateEditText(matches.get(0));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
