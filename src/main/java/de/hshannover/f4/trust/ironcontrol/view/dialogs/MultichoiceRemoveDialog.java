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
package de.hshannover.f4.trust.ironcontrol.view.dialogs;

import android.content.Context;
import android.net.Uri;
import de.hshannover.f4.trust.ironcontrol.R;

public class MultichoiceRemoveDialog extends MultichoiceListDialog {

	public MultichoiceRemoveDialog(Context context, Uri uri, int resIdTitle, int resIdButton) {
		super(context, uri, resIdTitle, resIdButton);
	}

	//	@Override
	//	protected void callBack(int clicked) {
	//		if (context instanceof MultichoiceSearchEvent) {
	//			MultichoiceRemoveEvent event = (MultichoiceRemoveEvent) context;
	//
	//			event.remove(getSelectedRowIds());
	//		}
	//	}

	@Override
	protected String getPositiveButtonLabel() {
		return context.getResources().getString(R.string.remove);
	}

	@Override
	protected void setNeutralButton() {}	// nothing to do

}
