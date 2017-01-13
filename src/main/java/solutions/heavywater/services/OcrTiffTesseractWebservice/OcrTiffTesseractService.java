package solutions.heavywater.services.OcrTiffTesseractWebservice;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

@Path("/")
public interface OcrTiffTesseractService {

	@POST
	@Path("/ocrtifftesseract")
	@Consumes({ MediaType.APPLICATION_OCTET_STREAM })
	@Produces("text/html")

	public Response OcrTiffTesseractServiceMethod(InputStream inputStream);

}
