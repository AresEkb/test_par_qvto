package test_par_qvto;

import org.eclipse.m2m.qvt.oml.blackbox.java.Module;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation;
import org.eclipse.ocl.pivot.ExpressionInOCL;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.pivot.utilities.ParserException;
import org.eclipse.uml2.uml.Constraint;

@Module(packageURIs = {
		"http://www.eclipse.org/emf/2002/Ecore",
		"http://www.eclipse.org/emf/2003/XMLType",
		"http://www.eclipse.org/ocl/2015/Pivot",
		"http://www.example.org/sample" })
public class UtilitiesLibrary {

	public UtilitiesLibrary() {
		super();
	}

	final static OCL ocl = OCL.newInstance();

	private static int seq = 0;

	public static int getSeqNumber() {
		return ++seq;
	}

	@Operation(contextual = true)
	public static ExpressionInOCL toExpressionInOCL(Constraint constraint) {
		ExpressionInOCL expr = null;
		try {
			org.eclipse.ocl.pivot.Constraint asConstraint = ocl.getMetamodelManager()
					.getASOf(org.eclipse.ocl.pivot.Constraint.class, constraint);
			expr = ocl.getSpecification(asConstraint);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return expr;
	}

}
