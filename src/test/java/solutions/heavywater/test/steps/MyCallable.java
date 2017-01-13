package solutions.heavywater.test.steps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;

public class MyCallable implements Callable<String>{
	
	InputStream stream;
	String url = "https://mgnrl9129c.execute-api.us-east-1.amazonaws.com/acceptance/ocrtifftesseract";
	
	MyCallable(InputStream is)
	{
		this.stream = is;
	}
	
	
	public String call(){
		String output=null;
		try{
			HttpClient httpClient = new HttpClient();
			PostMethod mPost = new PostMethod(url);
			Header methodHeader = new Header();
			int statusCode = 0;
			RequestEntity entity = new InputStreamRequestEntity(stream,"application/octet-stream");
			methodHeader.setName("Content-Type");
	        methodHeader.setValue("application/octet-stream");
	        methodHeader.setName("Accept");
	        methodHeader.setValue("text/html");
	        mPost.addRequestHeader(methodHeader);
	        mPost.setRequestEntity(entity);
	        long currentTime = System.currentTimeMillis();
	        statusCode = httpClient.executeMethod(mPost);;
	        long endTime = System.currentTimeMillis();
	        System.out.println("Response Time:"+(endTime-currentTime)/1000);
		System.out.println("status code is " + statusCode);
		if (statusCode == 200) {
			InputStream is = mPost.getResponseBodyAsStream();
			BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder sb1 = new StringBuilder();
			while ((line = br1.readLine()) != null) {
				sb1.append(line);
			}
			output = sb1.toString();

		} else if (statusCode == 403) {
			output="error";
		} else if (statusCode == 504) {
			System.out.println("504: Timed out");
			output="error";
		}else {
			//logger.info("status other that 403 " + mPost.getResponseBodyAsString());
			output="error";
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	} 
		return output;
	}

}
