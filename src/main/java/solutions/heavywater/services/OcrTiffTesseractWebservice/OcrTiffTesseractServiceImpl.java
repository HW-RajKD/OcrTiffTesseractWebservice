package solutions.heavywater.services.OcrTiffTesseractWebservice;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

public class OcrTiffTesseractServiceImpl implements OcrTiffTesseractService {

	@Override
	public Response OcrTiffTesseractServiceMethod(InputStream inputStream) {
		ExecutorService service = Executors.newFixedThreadPool(1);
		Future<Response>  task = null;
		Response output = null;
		try {
			Callable<Response> object = new OcrTiffTesseractWebserviceMain(inputStream);
			task = service.submit(object);
		}catch(final Exception ex) {
            ex.printStackTrace();
        } 
		service.shutdown();
		while (!service.isTerminated()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			output = task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
		
	}
}
