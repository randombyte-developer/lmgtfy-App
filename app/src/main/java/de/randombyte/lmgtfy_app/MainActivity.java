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

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActionBarActivity {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.search_text) EditText searchEditText;
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

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String trimmedText = editable.toString().trim();
                boolean emptySearchTerm = trimmedText.isEmpty();
                enableRecursive(actionButtonBar, !emptySearchTerm);
                previewTextView.setText(emptySearchTerm ? "" : Lmgtfy.createLink(trimmedText));
            }
        });

        copyLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Lmgtfy-Link", Lmgtfy.createLink(searchEditText.getText().toString())));
                Toast.makeText(MainActivity.this, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });

        shareLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, Lmgtfy.createLink(searchEditText.getText().toString()));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_with)));
            }
        });

        openPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Lmgtfy.createLink(searchEditText.getText().toString())));
                startActivity(intent);
            }
        });

        enableRecursive(actionButtonBar, false); // at start searchEditText is empty
    }

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
                new AlertDialog.Builder(this).setMessage(R.string.about_text)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
