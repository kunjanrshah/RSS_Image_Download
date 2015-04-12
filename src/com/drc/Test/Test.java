package com.drc.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.sax.Element;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Test extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Bitmap bitmap = DownloadImage("http://1.bp.blogspot.com/_HVS0Jlt2VgY/TLwIPM9UQYI/AAAAAAAAClo/NNPdzjML2Ak/s1600/sonia.jpg");
		ImageView img = (ImageView) findViewById(R.id.img);
		img.setImageBitmap(bitmap);

		String str = DownloadText("http://www.appleinsider.com/appleinsider.rss");
		TextView txt = (TextView) findViewById(R.id.text);
		txt.setText(Html.fromHtml(str));

		// DownloadRSS("http://www.appleinsider.com/appleinsider.rss");

	}

	private InputStream OpenHttpConnection(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception ex) {
			throw new IOException("Error connecting");
		}
		return in;
	}

	private Bitmap DownloadImage(String URL) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bitmap;
	}

	private String DownloadText(String URL) {
		int BUFFER_SIZE = 2000;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}

		InputStreamReader isr = new InputStreamReader(in);
		int charRead;
		String str = "";
		char[] inputBuffer = new char[BUFFER_SIZE];
		try {
			while ((charRead = isr.read(inputBuffer)) > 0) {
				// ---convert the chars to a String---
				String readString = String.copyValueOf(inputBuffer, 0, charRead);
				str += readString;
				inputBuffer = new char[BUFFER_SIZE];
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return str;
	}
	/*
	 * private void DownloadRSS(String URL) { InputStream in = null; try { in =
	 * OpenHttpConnection(URL); Document doc = null; DocumentBuilderFactory dbf
	 * = DocumentBuilderFactory.newInstance(); DocumentBuilder db;
	 * 
	 * try { db = dbf.newDocumentBuilder(); doc = db.parse(in); } catch
	 * (ParserConfigurationException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (SAXException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * doc.getDocumentElement().normalize();
	 * 
	 * //---retrieve all the <item> nodes--- NodeList itemNodes =
	 * doc.getElementsByTagName("item");
	 * 
	 * String strTitle = ""; for (int i = 0; i < itemNodes.getLength(); i++) {
	 * Node itemNode = itemNodes.item(i); if (itemNode.getNodeType() ==
	 * Node.ELEMENT_NODE) { //---convert the Node into an Element--- Element
	 * itemElement = (Element) itemNode;
	 * 
	 * //---get all the <title> element under the <item> // element--- NodeList
	 * titleNodes = ((Document) itemElement).getElementsByTagName("title");
	 * 
	 * //---convert a Node into an Element--- Element titleElement = (Element)
	 * titleNodes.item(0);
	 * 
	 * //---get all the child nodes under the <title> element--- NodeList
	 * textNodes = ((Node) titleElement).getChildNodes();
	 * 
	 * //---retrieve the text of the <title> element--- strTitle = ((Node)
	 * textNodes.item(0)).getNodeValue();
	 * 
	 * //---display the title--- Toast.makeText(getBaseContext(),strTitle,
	 * Toast.LENGTH_SHORT).show(); } } } catch (IOException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); } }
	 */

}