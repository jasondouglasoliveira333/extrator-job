//package br.com.lkm.extrator.sap.connector;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.sap.conn.jco.ext.DestinationDataEventListener;
//import com.sap.conn.jco.ext.DestinationDataProvider;
//
//import br.com.lkm.extrator.repository.SapConfigurationRepository;
//
//@Component
//public class MultiDestinationDataProvider implements DestinationDataProvider {
//	
//	@Autowired
//	private SapConfigurationRepository sapConfigurationRepository;
//	
//	private Map<String, Properties> propertiesCache = new HashMap<>();  
//	
//	@Override
//	public Properties getDestinationProperties(String destinationName) {
//		System.out.println("In MultiDestinationDataProvider.getDestinationProperties");
//		Properties p = propertiesCache.get(destinationName);
//		if (p == null) {
//			p = loadProperties(destinationName);
//			propertiesCache.put(destinationName, p);
//		}
//		return p;
//	}
//
////	p.put("jco.client.passwd", "123456");
////	p.put("jco.client.user", "integracao");
////	p.put("jco.client.ashost", "192.168.197.24");
//
//	private Properties loadProperties(String destinationName) {
//		Properties p = new Properties();
////		List<SapConfiguration> sapConfigurations = sapConfigurationRepository.findByDestinationName(destinationName);
////		if (sapConfigurations.size() > 0) {
//			p.put("Type", "RFC");
//			p.put("jco.client.passwd", "Lkm@2021");
//			p.put("jco.client.user", "msafdfe");
////			p.put("jco.client.ashost", "192.168.197.36");
//			p.put("jco.client.ashost", "201.68.37.204");
//			p.put("jco.client.client", "100");
//			p.put("jco.client.sysnr", "00");
//			p.put("jco.destination.proxy_type", "OnPremise");
////		}else {
////			System.out.println("Nao existe destination cadastrado com esse nome:" + destinationName);
////		}
//		return p;
//	}
//
//	@Override
//	public boolean supportsEvents() {
//		return false;
//	}
//
//	@Override
//	public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
//	}
//}