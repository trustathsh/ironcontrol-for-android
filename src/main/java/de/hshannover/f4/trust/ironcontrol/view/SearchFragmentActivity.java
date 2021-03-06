/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of ironcontrol for android, version 1.0.2, implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2013 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.ironcontrol.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TabHost;
import de.hshannover.f4.trust.ironcontrol.R;
import de.hshannover.f4.trust.ironcontrol.database.DBContentProvider;
import de.hshannover.f4.trust.ironcontrol.database.entities.Connections;
import de.hshannover.f4.trust.ironcontrol.database.entities.Requests;
import de.hshannover.f4.trust.ironcontrol.logger.Logger;
import de.hshannover.f4.trust.ironcontrol.logger.LoggerFactory;

public class SearchFragmentActivity extends FragmentActivity  {

	private static final Logger logger = LoggerFactory.getLogger(SearchFragmentActivity.class);

	public static final String MESSAGESEARCH = "Search...";

	private Spinner sStartIdentifier;
	private SeekBar sbMaxDepth;
	private EditText etStartIdentifier, etName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_activity_search);

		Cursor connection_cursor = getContentResolver().query(DBContentProvider.CONNECTIONS_URI, new String[]{Connections.COLUMN_NAME}, null, null, null);
		if(connection_cursor.getCount() == 0){
			createConnectionSettingsDialog().show();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		readResources();

		// Start Activity with data
		Bundle bundle = getIntent().getExtras();
		if(bundle != null && bundle.getString("listItemId") != null){
			fillActivityViews(bundle.getString("listItemId"));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void search(View view){
		FragmentManager fm = getSupportFragmentManager();
		Fragment tabsFragment = fm.findFragmentById(R.id.tabs_fragment);
		TabHost tabHost = (TabHost)tabsFragment.getView().findViewById(android.R.id.tabhost);

		if(tabHost.getCurrentView().getId() == R.id.tab1){
			//Simple Search
			SimpleRequestFragment fSimple = (SimpleRequestFragment) fm.findFragmentByTag(TabFragment.TAB_SIMPLE);
			fSimple.search(view);

		}else if(tabHost.getCurrentView().getId() == R.id.tab2){
			//Advanced Search
			AdvancedRequestFragment fAdvanced = (AdvancedRequestFragment) fm.findFragmentByTag(TabFragment.TAB_ADVANCED);
			fAdvanced.search(view);
		}
	}

	public void saveSearch(View view){
		FragmentManager fm = getSupportFragmentManager();
		Fragment tabsFragment = fm.findFragmentById(R.id.tabs_fragment);
		TabHost tabHost = (TabHost)tabsFragment.getView().findViewById(android.R.id.tabhost);

		if(tabHost.getCurrentView().getId() == R.id.tab1){
			//Simple Search
			SimpleRequestFragment fSimple = (SimpleRequestFragment) fm.findFragmentByTag(TabFragment.TAB_SIMPLE);
			fSimple.createSearchSaveDialog().show();

		}else if(tabHost.getCurrentView().getId() == R.id.tab2){
			//Advanced Search
			AdvancedRequestFragment fAdvanced = (AdvancedRequestFragment) fm.findFragmentByTag(TabFragment.TAB_ADVANCED);
			fAdvanced.createSearchSaveDialog().show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(getBaseContext(), SettingsActivity.class));
			return true;
		case R.id.menu_exit:
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.addCategory(Intent.CATEGORY_HOME);
			home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(home);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void readResources(){
		sStartIdentifier = (Spinner)findViewById(R.id.sIdentifier1);
		etStartIdentifier = (EditText)findViewById(R.id.etIdentifier1);
		sbMaxDepth = (SeekBar)findViewById(R.id.seekBarMaxDepth);
		etName = (EditText)findViewById(R.id.etName);
	}

	private void fillActivityViews(String itemId) {
		Uri publish_uri = Uri.parse(DBContentProvider.SEARCH_URI + "/"+ itemId);
		String[] search_projection = new String[]{Requests.COLUMN_ID, Requests.COLUMN_IDENTIFIER1, Requests.COLUMN_IDENTIFIER1_Value, Requests.COLUMN_NAME, Requests.COLUMN_MATCH_LINKS, Requests.COLUMN_MAX_DEPTH, Requests.COLUMN_MAX_SITZ, Requests.COLUMN_RESULT_FILTER, Requests.COLUMN_TERMINAL_IDENTIFIER_TYPES};

		Cursor publish_cursor = getContentResolver().query(publish_uri, search_projection, null, null, null);

		publish_cursor.moveToNext();
		System.out.println(publish_cursor.getCount());
		String name = publish_cursor.getString(publish_cursor.getColumnIndexOrThrow(Requests.COLUMN_NAME));
		String[] identifiers = {publish_cursor.getString(publish_cursor.getColumnIndexOrThrow(Requests.COLUMN_IDENTIFIER1))};
		String[] values = {publish_cursor.getString(publish_cursor.getColumnIndexOrThrow(Requests.COLUMN_IDENTIFIER1_Value))};
		int maxDepth = publish_cursor.getInt(publish_cursor.getColumnIndexOrThrow(Requests.COLUMN_MAX_DEPTH));

		publish_cursor.close();

		etName.setText(name);
		sStartIdentifier.setSelection(getSpinnerIndex(sStartIdentifier, identifiers[0]));
		etStartIdentifier.setText(values[0]);
		sbMaxDepth.setProgress(maxDepth);

	}

	private int getSpinnerIndex(Spinner spinner, String s){
		int index = 0;
		for (int i=0;i<spinner.getCount();i++){
			if (spinner.getItemAtPosition(i).equals(s)){
				index = i;
			}
		}
		return index;
	}

	private Dialog createConnectionSettingsDialog(){
		AlertDialog.Builder connectionSettingsDialog = createDialog(R.string.no_connection_settings, R.string.no_connection_settings_message);

		connectionSettingsDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(getBaseContext(), ConnectionFragmentActivity.class);
				startActivity(intent);
			}
		});

		connectionSettingsDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled
			}
		});

		return connectionSettingsDialog.create();
	}

	private AlertDialog.Builder createDialog(int titleId, int messageId){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(titleId);
		alert.setMessage(messageId);
		return alert;
	}
}
