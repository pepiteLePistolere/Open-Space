package org.home.fenrriquez.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity {
    /** Called when the activity is first created. */
	
	Button myBtn;
	EditText myTxt;
	Button Ct;
	Button addC;
    final int PICK_CONTACT = 1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {  	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myBtn = (Button)findViewById(R.id.button1);
        myBtn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				EditText myEdt = (EditText)findViewById(R.id.editText1);
				Toast.makeText( v.getContext(), myEdt.getText(),Toast.LENGTH_LONG).show();
			} 
		} );
        
        Ct = (Button)findViewById( R.id.button2);
        final Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);

        Ct.setOnClickListener( new OnClickListener(){
        	public void onClick( View v){
        		startActivityForResult(intent, PICK_CONTACT);
        		
        	}
        });
        
        addC = (Button)findViewById( R.id.AddContact);
        addC.setOnClickListener( new OnClickListener() {
        	public void onClick(View v) {
        		addGhostContact( v );
			}
		});
    }
    
    public void addGhostContact( View v){
    	
    	EditText tmp = (EditText)findViewById( R.id.CompleteName);
    	String name = tmp.getText().toString();
    	tmp = (EditText)findViewById( R.id.PhoneNumber);
    	String phone = tmp.getText().toString();
    	
    	ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
    	int rawContactInsertIndex = 0;

    	ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
    	   .withValue(RawContacts.ACCOUNT_TYPE, null)
    	   .withValue(RawContacts.ACCOUNT_NAME,null )
    	   .build());
    	ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
    	   .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
    	   .withValue(Data.MIMETYPE,Phone.CONTENT_ITEM_TYPE)
    	   .withValue(Phone.NUMBER, phone)
    	   .build());
    	ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
    	   .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
    	   .withValue(Data.MIMETYPE,StructuredName.CONTENT_ITEM_TYPE)
    	   .withValue(StructuredName.DISPLAY_NAME, name)
    	   .build());  
    	try {
			ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Toast.makeText( v.getContext() , "Contact "+ name +" Added with Phone: "+phone, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
    super.onActivityResult(reqCode, resultCode, data);

    switch (reqCode) {
    case (PICK_CONTACT) :
      if (resultCode == Activity.RESULT_OK) {
        Uri contactData = data.getData();
        Cursor c =  managedQuery(contactData,null, null, null, null);
        String name;
        String cid;
        if (c.moveToFirst()) {
        	cid = c.getString(c.getColumnIndexOrThrow(Contacts._ID));
         	name = c.getString(c.getColumnIndexOrThrow(Contacts.DISPLAY_NAME));
        }else{
        	name = "No Name";
        	cid = "0";
        }
        Cursor p = managedQuery( Phone.CONTENT_URI,null, Phone.CONTACT_ID+"="+cid,null,null);
     	String phone;
     	if( p.moveToFirst()){
     		phone = p.getString(p.getColumnIndexOrThrow(Phone.NUMBER));
     	}else{
     		phone = "No Phone";
     	}
        TextView n = (TextView)findViewById( R.id.textView1);
        TextView ph = (TextView)findViewById( R.id.textView2);
        n.setText( "Name: "+name );
        ph.setText( "Phone: "+phone );
      }
      break;
    }
    }
}