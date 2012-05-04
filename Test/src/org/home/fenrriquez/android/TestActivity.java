package org.home.fenrriquez.android;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
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