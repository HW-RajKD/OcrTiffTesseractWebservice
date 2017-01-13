import boto3
import json
import sys

client = boto3.setup_default_session(region_name='us-east-1')
ApiId = sys.argv[1]
RootResourceId = sys.argv[2]
stage = sys.argv[3]
elb_dns = sys.argv[4]

#ApiId = 'mgnrl9129c'
#RootResourceId = 'lblhuge6x0'
#stage = 'acceptance'
#elb_dns = 'heavywate-ElasticL-11VUOZUIRCGOA-565173634'

resourceName = 'ocrtifftesseract'

client = boto3.client('apigateway')

response = client.get_resources(
    restApiId=ApiId,
    limit=500
)
flag = 0
resources = response['items']
for resource in resources:
        if resourceName == resource.get('pathPart'):
                resourceID = resource.get('id')
                flag = 1
                #If there is need to update the resource or methods, make necessary changes 
                #comment the above line and uncomment the below 2 lines
                #flag = 0
                #client.client.delete_resource(restApiId=ApiId,resourceId=resourceID)
                
if flag == 0:
        try:
                response = client.create_resource(restApiId=ApiId, parentId=RootResourceId, pathPart=resourceName)
        except Exception:
                print 'Resource Exists'
        else:
                resourceID = response.get('id')
                
                methodRequest = client.put_method(
                                    restApiId=ApiId,
                                    resourceId=resourceID,
                                    httpMethod='POST',
                                    authorizationType='NONE',
                                    requestParameters={
                                        'method.request.header.Accept' : True,
                                        'method.request.header.Content-Type' : True
                                    }
                                )
                integrationRequest = client.put_integration(
                                        restApiId=ApiId,
                                        resourceId=resourceID,
                                        httpMethod='POST',
                                        type='HTTP',
                                        passthroughBehavior='WHEN_NO_TEMPLATES',
                                        integrationHttpMethod='POST',
                                        uri='http://${stageVariables.OcrTiffTesseractWebserviceUrl}',
                                        requestParameters={
                                            'integration.request.header.Accept' : 'method.request.header.Accept',
      		                                'integration.request.header.Content-Type' : 'method.request.header.Content-Type'
                                        }                     
                )
                methodResponse = client.put_method_response(
                                    restApiId=ApiId,
                                    resourceId=resourceID,
                                    httpMethod='POST',
                                    statusCode='200'
                                )
                integrationResponse = client.put_integration_response(
                                        restApiId=ApiId,
                                        resourceId=resourceID,
                                        httpMethod='POST',
                                        statusCode='200',
                                        responseParameters={
                                        }
                                        )
                
method1 = '/OcrTiffTesseractWebservice/rest/ocrtifftesseract'
client.update_stage( 
    restApiId=ApiId,
    stageName=stage,
    patchOperations=[
        {
            'op':'replace',
            'path':'/variables/OcrTiffTesseractWebserviceUrl',
            'value':elb_dns + method1
        }
    ]
)

client.update_stage( 
    restApiId=ApiId,
    stageName='mock',
    patchOperations=[
        {
            'op':'replace',
            'path':'/variables/OcrTiffTesseractWebserviceUrl',
            'value':'private-15d3d9-getfileslistapi1.apiary-mock.com/'
        }
    ]
)

deploymentResponse = None
while deploymentResponse is None:
    try:
        deploymentResponse=client.create_deployment(
                                restApiId=ApiId,
                                stageName=stage
        )
    except:
        pass
        


