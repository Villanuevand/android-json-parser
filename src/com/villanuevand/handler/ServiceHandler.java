package com.villanuevand.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class ServiceHandler {

	static String response = null;
	public static final int _GET = 1;
	public static final int _POST = 2;

	
	public ServiceHandler() {
		// TODO Auto-generated constructor stub
	}	
	
	/**
	 * Haciendo la llamada al servicio 
	 * @param url - Url del servicio
	 * @param metodo - Metodo HTTP (GET,POST)
	 * @return
	 */
	public String makeServiceCall(String url, int metodo) {		
        return this.makeServiceCall(url, metodo,null);
    }
	
	public String makeServiceCall(String url, int metodo, List<NameValuePair> params){
		
		try {
			//Cliente HTTP
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			// Detectando el tipo de metodo de la peticion
			if(metodo == _POST){
				HttpPost httpPost = new HttpPost(url);
				//Agregando parametros POST
				if(params != null){
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}
				httpResponse = httpClient.execute(httpPost);
			}else if(metodo ==  _GET){
				//añadiendo parametros a la url
				if(params != null){
					String paramString = URLEncodedUtils.format(params,"utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);				
			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
			
		}  catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return response;
		
	}
	
	

}
