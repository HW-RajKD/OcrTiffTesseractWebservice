package solutions.heavywater.test.steps;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.Output;
import com.amazonaws.services.cloudformation.model.Stack;
import com.amazonaws.services.cloudformation.model.StackStatus;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CaseSteps {
	static AmazonCloudFormation stackbuilder;
	static Logger logger = LoggerFactory.getLogger(CaseSteps.class);
	static String OcrTiffTesseractWSURL;

	@When("^OcrTiffTesseractWebservice CFT is launched$")
	public void namedEntityExtraction_CFT_is_launched() throws Throwable {
		System.out.println("Checking if OcrTiffTesseractWebservice is launched");
		stackbuilder = new AmazonCloudFormationClient(new InstanceProfileCredentialsProvider());
		DescribeStacksRequest describeRequest = new DescribeStacksRequest();
		describeRequest.setStackName("heavywater-OcrTiffTesseractWebservice");
		String stackStatus = "Unknown";
		List<Stack> stacks = stackbuilder.describeStacks(describeRequest).getStacks();
		if (stacks.isEmpty()) {
			stackStatus = "NO_SUCH_STACK";
			assertTrue(false);
		} else {
			for (Stack stack : stacks) {
				if (stack.getStackStatus().equals(StackStatus.CREATE_FAILED.toString())
						|| stack.getStackStatus().equals(StackStatus.ROLLBACK_FAILED.toString())
						|| stack.getStackStatus().equals(StackStatus.DELETE_FAILED.toString())) {
					stackStatus = stack.getStackStatus();
					assertTrue(false);
				} else if (stack.getStackStatus().equals(StackStatus.CREATE_COMPLETE.toString())) {
					stackStatus = stack.getStackStatus();
					assertTrue(true);
				} else {
					stackStatus = stack.getStackStatus();
					assertTrue(false);
				}
			}
		}
		Thread.sleep(60000);
		System.out.println(stackStatus);
	}

	@Then("^I get CFT output$")
	public void i_get_CFT_output() throws Throwable {
		System.out.println("Checking for OcrTiffTesseractWebservice CFT output");
		DescribeStacksRequest describeRequest = new DescribeStacksRequest();
		describeRequest.setStackName("heavywater-OcrTiffTesseractWebservice");
		List<Stack> stacks = stackbuilder.describeStacks(describeRequest).getStacks();
		for (Stack stack : stacks) {
			for (Output out : stack.getOutputs()) {
				if (out.getOutputKey().equals("Url")) {
					OcrTiffTesseractWSURL = out.getOutputValue();
				}
			}
		}
		
	}

	@Then("^I execute my RestFul service$")
	public void i_execute_my_RestFul_service() throws Throwable {
		Set<Future<String>> set = new HashSet<Future<String>>();
		try {
			ExecutorService executor = Executors.newFixedThreadPool(40);
			int transactionsCount = 40;
			System.out.println("Total Transactions:"+ transactionsCount);
			for(int i =0; i<transactionsCount; i++)
			{
				String localFilePath = "RENEW WIND 39220_058.tif";
				File tifFile = new File(localFilePath);
				InputStream stream=new FileInputStream(tifFile);
				MyCallable myCallable = new MyCallable(stream);
				Future<String> future = executor.submit(myCallable);
				set.add(future);
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int errorCount = 0;
			int responses =0;
			for(Future<String> f: set){
				System.out.println(f.get());
				System.out.println();
				System.out.println();
				if(f.get().compareTo(new String("error"))==0){
					errorCount++;
				}
				else{
					responses++;
				}
			}
			System.out.println("Errors: "+errorCount);
			
			assertTrue(errorCount==0&&responses!=0);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
