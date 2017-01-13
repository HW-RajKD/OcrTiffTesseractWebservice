package solutions.heavywater.services.OcrTiffTesseractWebservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.Callable;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.helpers.IOUtils;

public class OcrTiffTesseractWebserviceMain implements Callable<Response> {
	InputStream inputStream;
	public OcrTiffTesseractWebserviceMain()
	{
	}
	
	public OcrTiffTesseractWebserviceMain(InputStream is)
	{
		this.inputStream = is;
	}
	public Response call() throws Exception {

		String downloadPath = "";
		String fileName = "";
		String sendResponse = "";
		System.out.println("Inside OcrTiffTesseractServiceImpl ");

		String folderName = UUID.randomUUID().toString();
		String folderPath = "/tmp/" + folderName;
		File serverFolders = new File(folderPath);
		serverFolders.mkdirs();

		FileOutputStream outputStream = null;
		downloadPath = folderPath + "/inputfile.tif";
		System.out.println("download Path is " + downloadPath);
		try {
			outputStream = new FileOutputStream(downloadPath);
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("Downloaded file successfully");
		File tiffFile = new File(downloadPath);

		fileName = tiffFile.getName();
		System.out.println("fileName is " + fileName);
		// convert the file
		String srcFile = downloadPath;

		//String targetfname = fileName.substring(0, fileName.length() - 4);
		
		String targetFile = folderPath + "/outputfile" ;
		System.out.println("targetFile " + targetFile);

		OcrTiffTesseractWebserviceMain convertFile = new OcrTiffTesseractWebserviceMain();
		// convertFile.convert(srcFile, targetFile);
		String command = "tesseract " + srcFile + " " + targetFile + " hocr";
		System.out.println("command is " + command);
		String pathToExecute = targetFile + ".hocr";
		String Finaloutput = convertFile.executeCommand(command, pathToExecute);

		System.out.println("Finaloutput is " + Finaloutput);
		System.out.println();
		sendResponse = Finaloutput;
		
		ResponseBuilder builder = Response.ok(sendResponse);
		System.out.println("Deleting the folder "+folderPath);
		String[] files = serverFolders.list();
		for(String s: files){
		    File currentFile = new File(serverFolders.getPath(),s);
		    currentFile.delete();
		}
		serverFolders.delete();
		
		return builder.build();

	}

	public String executeCommand(String command, String pathToExecute) {
		System.out.println("Inside executeCommand method and pathToExecute " + pathToExecute);

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
				System.out.println("line " + line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// System.out.println(" execute command " + output.toString());

		File file = new File(pathToExecute);
		String str = "";
		try {
			str = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("str is " + str);
		return str.toString();

	}

}
