package com.villanuevand.lector.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.villanuevand.handler.ServiceHandler;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class MainActivity extends ActionBarActivity {
	
	private static final String DEGUB_TAG = "Villanuevand";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends ListFragment implements OnItemClickListener{

		private ProgressDialog progressDialog;		
		//Url del Servicio 
		private static String URL = "http://api.androidhive.info/contacts/";
		//Nombres de Nodos JSON
		private static final String TAG_CONTACTS = "contacts";
	    private static final String TAG_ID = "id";
	    private static final String TAG_NAME = "name";
	    private static final String TAG_EMAIL = "email";
	    private static final String TAG_ADDRESS = "address";
	    private static final String TAG_GENDER = "gender";
	    private static final String TAG_PHONE = "phone";
	    private static final String TAG_PHONE_MOBILE = "mobile";
	    private static final String TAG_PHONE_HOME = "home";
	    private static final String TAG_PHONE_OFFICE = "office";
	    
	    JSONArray contacts = null;
	    ArrayList<HashMap<String, String>> contacList; 
	    ListView listView;
	    
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,false);			
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			contacList = new ArrayList<HashMap<String, String>>();
			ListView lvListView = getListView();
			new GetContacts().execute();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			
			
		}
		
		
		private class GetContacts extends AsyncTask<Void, Void, Void>
		{
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				//ProgressDialog 
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setMessage("Descargando...");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		

			@Override
			protected Void doInBackground(Void... params) {
				//Instanciando el Manejador Servicio
				ServiceHandler sHandler = new ServiceHandler();
				String jsonStr = sHandler.makeServiceCall(URL, ServiceHandler._GET);
				//Agregando una Entrada al LOGCAT
				Log.d(DEGUB_TAG, " > "+jsonStr);
				if(jsonStr != null)
				{
					try {
						JSONObject jsonObject = new JSONObject(jsonStr);
						contacts = jsonObject.getJSONArray(TAG_CONTACTS);
						
						//Iterando en todos los contactos
						for (int i = 0; i < contacts.length(); i++) {
							/*
							 * Recordatorio:
							 * JSONArray = []
							 * JSONObject = {}
							 * */
							JSONObject c = contacts.getJSONObject(i);
							String id = c.getString(TAG_ID);
	                        String name = c.getString(TAG_NAME);
	                        String email = c.getString(TAG_EMAIL);
	                        String address = c.getString(TAG_ADDRESS);
	                        String gender = c.getString(TAG_GENDER);
	                        
	                        // Phone es un JSONObject { }
	                        JSONObject phone = c.getJSONObject(TAG_PHONE);
	                        String mobile = phone.getString(TAG_PHONE_MOBILE);
	                        String home = phone.getString(TAG_PHONE_HOME);
	                        String office = phone.getString(TAG_PHONE_OFFICE);
	                        
	                        // HashMap temporal para contacto individual.
	                        HashMap<String,	String> contact = new HashMap<String,	String>();
	                        
	                        // Agregando cada nodo hijo al HashMap Llave => Valor
	                        contact.put(TAG_ID, id);
	                        contact.put(TAG_NAME, name);
	                        contact.put(TAG_EMAIL, email);
	                        contact.put(TAG_PHONE_MOBILE, mobile);
	                        
	                        //Agregando el contacto a la Lista de Contactos
	                        contacList.add(contact);	                        
	 
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else 
				{
					Log.e(DEGUB_TAG, "No se puede obtener data desde la URL");
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(progressDialog.isShowing())
					progressDialog.dismiss();
				/*
				 * Actualizando data parseada en el ListView
				 * */
				ListAdapter adapter = new SimpleAdapter(getActivity(),contacList, R.layout.list_item,new String[]{TAG_NAME,TAG_EMAIL,TAG_PHONE_MOBILE} ,new int[] {R.id.list_item_name,R.id.list_item_mail,R.id.list_item_phone_mobile});
				setListAdapter(adapter);
			}
			
		}
	}

}
