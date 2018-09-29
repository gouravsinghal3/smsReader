package com.vrs.smsapp;

/***
 *    Application Name : MessageBox 
 *    Author : Vimal Rughani
 *    Website : http://pulse7.net
 *    For more details visit http://pulse7.net/android/read-sms-message-inbox-sent-draft-android/
 */
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vrs.smsapp.adapter.MyCursorAdapter;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MessageBox extends Activity {

	// GUI Widget
	Button btnSent, btnInbox, btnDraft;
	TextView lblMsg, lblNo;
	ListView lvMsg;
	StickyListHeadersListView mListview;
	boolean permGranted = false;

	// Cursor Adapter
	SimpleCursorAdapter adapter;
	MyCursorAdapter myCursorAdapter;
	int  chkid =0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagebox);

		lvMsg = (ListView) findViewById(R.id.lvMsg);
        mListview = (StickyListHeadersListView) findViewById(R.id.rc_confirmed);

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("id"))
		{
			  chkid = getIntent().getExtras().getInt("id");
		}

		Dexter.withActivity(this)
				.withPermissions(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
				.withListener(new MultiplePermissionsListener() {
					@Override
					public void onPermissionsChecked(MultiplePermissionsReport report) {

						if (report.areAllPermissionsGranted()) {
							permGranted = true;
							getSmsList();
						}
					}

					@Override
					public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

						Toast.makeText(MessageBox.this, "Permission is mandatory to app work.", Toast.LENGTH_LONG).show();

					}


				}).check();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		chkid = intent.getExtras().getInt("id");
		getSmsList();
	}

	private void getSmsList()
	{
		Uri inboxURI = Uri.parse("content://sms/inbox");

		// List required columns
		String[] reqCols = new String[]{"_id", "address", "body", Telephony.Sms.Inbox.DEFAULT_SORT_ORDER, Telephony.Sms.DATE};

		// Get Content Resolver object, which will deal with Conte// Provider
		ContentResolver cr = getContentResolver();

		// Fetch Inbox SMS Message from Built-in Content Provider
		Cursor c = cr.query(inboxURI, reqCols, null, null, null);

		// Attached Cursor with adapter and display in listview
		adapter = new SimpleCursorAdapter(this, R.layout.row, c,
				new String[]{"body", "address"}, new int[]{
				R.id.lblMsg, R.id.lblNumber});

		myCursorAdapter = new MyCursorAdapter(this,c, 0, chkid);
		mListview.setAdapter(myCursorAdapter);
	}
}
