package soap;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Document;

import catalogue.IDcfCatalogue;
import catalogue.IDcfCataloguesList;
import config.Environment;
import response_parser.GetCataloguesListParser;
import response_parser.IDcfList;
import user.IDcfUser;

/**
 * Get the entire catalogues list which are present in DCF
 * @author avonva
 *
 */
public class GetCataloguesList<T extends IDcfCatalogue> extends GetList<T> {

	private static final String URL = "https://dcf-cms.efsa.europa.eu/catalogues";
	private static final String NAMESPACE = "http://ws.catalog.dc.efsa.europa.eu/";
	private static final String TEST_URL = "https://dcf-01.efsa.test/dc-catalog-public-ws/catalogues/?wsdl";
	
	private IDcfCataloguesList<T> output;
	
	public GetCataloguesList(IDcfUser user, Environment env, IDcfCataloguesList<T> output) {
		super(user, env, URL, TEST_URL, NAMESPACE);
		this.output = output;
	}
	
	@Override
	public IDcfList<T> getList() throws DetailedSOAPException {
		
		SOAPConsole.log("GetCataloguesList", getUser());

		IDcfList<T> response = super.getList();
		
		SOAPConsole.log("GetCataloguesList:", response);
		
		return response;
	}
	
	@Override
	public IDcfCataloguesList<T> getList(Document cdata) {
		GetCataloguesListParser<T> parser = new GetCataloguesListParser<>(output);
		return parser.parse(cdata);
	}

	@Override
	public SOAPMessage createRequest(SOAPConnection con) throws SOAPException {

		// create the standard structure and get the message
		SOAPMessage soapMsg = createTemplateSOAPMessage ("ws");

		// get the body of the message
		SOAPBody soapBody = soapMsg.getSOAPPart().getEnvelope().getBody();

		// create the xml message structure to get the dataset list
		SOAPElement soapElem = soapBody.addChildElement("getCatalogueList", "ws");

		// set that we want an XML file as output
		SOAPElement arg = soapElem.addChildElement("arg2");
		arg.setTextContent("XML");

		// save the changes in the message and return it
		soapMsg.saveChanges();

		return soapMsg;
	}
}
