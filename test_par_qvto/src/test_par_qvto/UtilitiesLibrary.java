package test_par_qvto;

import org.eclipse.m2m.qvt.oml.blackbox.java.Module;
import org.eclipse.ocl.pivot.utilities.OCL;

@Module(packageURIs={
        "http://www.eclipse.org/emf/2002/Ecore",
        "http://www.eclipse.org/emf/2003/XMLType",
        "http://www.eclipse.org/ocl/2015/Pivot",
        "http://www.example.org/sample"})
public class UtilitiesLibrary {

    public UtilitiesLibrary() {
        super();
    }

    final static OCL ocl = OCL.newInstance();

    final static String DOCUMENT_ROOT = "DocumentRoot";
    
    private static int seq = 0;

    public static int getSeqNumber()
    {
        return ++seq;
    }

}
