package pcv.ifc;

import java.io.PrintWriter;

import jsdai.lang.SdaiRepository;
import jsdai.lang.SdaiSession;
import jsdai.lang.SdaiTransaction;

public class IFCImporter {

	public IFCImporter() {
		// TODO Auto-generated constructor stub
	}

	public static final void main(String argv[]) throws jsdai.lang.SdaiException {
//		if (argv.length != 2) { 
//			System.out.println("USAGE: java importExport imput_file output_file");
//			return;
//		}
		
		System.setProperty("jsdai.properties", "C:/Program Files (x86)/jsdai-core");
		//System.out.println("---- " + System.getProperty("TEMP"));
		
		
		String av[] = {"C:/Program Files (x86)/ifcOpensHell/IfcOpenShell-0.4.0/test/input/revit2011_wall1.ifc",
				"C:/Program Files (x86)/ifcOpensHell/IfcOpenShell-0.4.0/test/input/revit2011_wall1.txt"};
		
		SdaiSession.setLogWriter(new PrintWriter(System.out, true));
		SdaiSession session = SdaiSession.openSession();
		SdaiTransaction trans = session.startTransactionReadWriteAccess();
		
		SdaiRepository repo = session.importClearTextEncoding(null, av[0], null);
		
		//  repo.openRepository() -- already open after import
		
		repo.exportClearTextEncoding(av[1]);
		trans.endTransactionAccessAbort();
		repo.closeRepository();
		repo.deleteRepository();
		session.closeSession();
	}

}
