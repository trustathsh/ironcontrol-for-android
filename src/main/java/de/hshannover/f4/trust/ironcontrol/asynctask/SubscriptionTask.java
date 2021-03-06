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
package de.hshannover.f4.trust.ironcontrol.asynctask;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ironcontrol.R;
import de.hshannover.f4.trust.ironcontrol.database.DBContentProvider;
import de.hshannover.f4.trust.ironcontrol.database.entities.Requests;
import de.hshannover.f4.trust.ironcontrol.logger.Level;
import de.hshannover.f4.trust.ironcontrol.logger.Logger;
import de.hshannover.f4.trust.ironcontrol.logger.LoggerFactory;
import de.hshannover.f4.trust.ironcontrol.logic.RequestsController;
import de.hshannover.f4.trust.ironcontrol.logic.data.Operation;
import de.hshannover.f4.trust.ironcontrol.logic.data.SubscribeRequestData;

/**
 * AsyncTask to subscribe in background and inform the user.
 * @author Marcel Reichenbach
 * @version 1.0
 */

public class SubscriptionTask extends AsyncTask<Void, Void, Void> {

	private static final Logger logger = LoggerFactory.getLogger(SubscriptionTask.class);

	private ProgressDialog pd;
	private Context context;
	private String name, startIdent, identValue, matchLinks, resultFilter, terminalIdentifierTypes, subscribeId;
	private int maxDepth, maxSitze;
	private Operation type;

	private String error;

	public SubscriptionTask(Context context, String name, String startIdent, String identValue, int maxDepth, String subscribeId, Operation type) {
		this(context, name, startIdent, identValue, maxDepth, 0, null, null, null, subscribeId, type);
	}

	public SubscriptionTask(Context context, String name, String startIdent, String identValue, int maxDepth, int maxSitze, String terminalIdentifierTypes, String resultFilter, String matchLinks, String subscribeId, Operation type) {
		logger.log(Level.DEBUG, "NEW...");
		this.context = context;
		this.name = name;
		this.startIdent = startIdent;
		this.identValue = identValue;
		this.maxDepth = maxDepth;
		this.matchLinks = matchLinks;
		this.resultFilter = resultFilter;
		this.maxSitze = maxSitze;
		this.terminalIdentifierTypes = terminalIdentifierTypes;
		this.subscribeId = subscribeId;
		this.type = type;
		pd= new ProgressDialog(this.context);
		pd.setMessage("Subscription ...");
		logger.log(Level.DEBUG, "...NEW");
	}

	@Override
	protected void onPreExecute() {
		logger.log(Level.DEBUG, "onPreExecute()...");
		super.onPreExecute();
		pd.show();
		logger.log(Level.DEBUG, "...onPreExecute()");
	}

	@Override
	protected Void doInBackground(Void... params) {
		Thread.currentThread().setName(SubscriptionTask.class.getSimpleName());
		logger.log(Level.DEBUG, "doInBackground()...");
		try {

			RequestsController.createSubscription(buildSubscribeData());

			if(subscribeId != null){

				// set subscription active
				ContentValues value = new ContentValues();
				switch (type) {
				case UPDATE : value.put(Requests.COLUMN_ACTIVE, 1);
				break;
				case DELETE : value.put(Requests.COLUMN_ACTIVE, 0);
				break;
				default : logger.log(Level.FATAL, "Wrong Operation type for subscription");
				break;
				}
				context.getContentResolver().update(Uri.parse(DBContentProvider.SUBSCRIPTION_URI + "/" +subscribeId), value, null, null);

			}

		} catch (IfmapErrorResult e) {
			error = e.getErrorCode().toString();
		} catch (IfmapException e) {
			error = e.getDescription();
		} catch (Exception e) {
			error = e.getMessage();
		}

		logger.log(Level.DEBUG, "...doInBackground()");
		return null;
	}

	private SubscribeRequestData buildSubscribeData(){
		logger.log(Level.DEBUG, "buildSubscribeData()...");
		SubscribeRequestData data = new SubscribeRequestData(type);
		data.setName(name);
		data.setIdentifier1(startIdent);
		data.setIdentifier1Value(identValue);
		data.setMaxDepth(maxDepth);
		data.setMatchLinks(matchLinks);
		data.setResultFilter(resultFilter);
		data.setTerminalIdentifierTypes(terminalIdentifierTypes);
		data.setMaxSize(maxSitze);
		logger.log(Level.DEBUG, "...buildSubscribeData()");
		return data;
	}

	@Override
	protected void onPostExecute(Void result) {
		logger.log(Level.DEBUG, "onPostExecute()...");
		pd.dismiss();

		if(error == null){
			Toast.makeText(context, R.string.publishReceived, Toast.LENGTH_SHORT).show();
			logger.log(Level.INFO, context.getResources().getString(R.string.publishReceived));
		}else {
			Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
		}

		logger.log(Level.DEBUG, "...onPostExecute()");
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartIdent(String startIdent) {
		this.startIdent = startIdent;
	}

	public void setIdentValue(String identValue) {
		this.identValue = identValue;
	}

	public void setMatchLinks(String matchLinks) {
		this.matchLinks = matchLinks;
	}

	public void setResultFilter(String resultFilter) {
		this.resultFilter = resultFilter;
	}

	public void setTerminalIdentifierTypes(String terminalIdentifierTypes) {
		this.terminalIdentifierTypes = terminalIdentifierTypes;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public void setMaxSitze(int maxSitze) {
		this.maxSitze = maxSitze;
	};
}
