package solutions.heavywater.test.steps;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.util.json.JSONException;

public class CallWS {

	private static Logger logger = LoggerFactory.getLogger(CallWS.class);

	static String msg = "";

	static String restURL = "http://localhost:8080/OcrTiffTesseractWebservice/rest/ocrtifftesseract";

	public static void main(String args[]) throws IOException {

		CallWS checkNotification = new CallWS();

		try {
			checkNotification.callWebService(restURL);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void callWebService(String restURL) throws JSONException, IOException {
		System.out.println("Checking the response of TextBlobWebservice");
		System.out.println(restURL);

		String localFilePath = "C:/Users/USER/Downloads/39653_001_Bill.tif";

		HttpClient httpClient = new HttpClient();
		PostMethod mPost = new PostMethod(restURL);
		Header methodHeader = new Header();

		try {

			File tifFile = new File(localFilePath);
			InputStream stream = new FileInputStream(tifFile);
			String fileSize = String.valueOf(tifFile.length());
			// System.out.println("file size is "+ fileSize);
			RequestEntity entity = new InputStreamRequestEntity(stream);
			methodHeader.setName("content-type");
			methodHeader.setValue("application/octet-stream");
			methodHeader.setName("accept");
			methodHeader.setValue("text/html");
			methodHeader.setName("content-length");
			methodHeader.setValue(fileSize);
			mPost.addRequestHeader(methodHeader);
			mPost.setRequestEntity(entity);
			int statusCode = httpClient.executeMethod(mPost);

			System.out.println("status code is " + statusCode);
			logger.info("the status code returned is" + statusCode);
			if (statusCode == 200) {

				System.out.println("web service call Successfull " + mPost.getResponseBodyAsString());

				assertTrue(true);

			} else if (statusCode == 403) {
				logger.info("Invalid input");
				assertTrue(false);
			} else {
				logger.info("status other that 403 " + mPost.getResponseBodyAsString());
				assertTrue(false);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (mPost != null) {
				mPost.releaseConnection();
			}
		}

	}

}
