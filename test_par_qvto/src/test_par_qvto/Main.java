package test_par_qvto;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.util.WriterLog;
import org.eclipse.ocl.pivot.uml.UMLStandaloneSetup;
import org.eclipse.ocl.xtext.essentialocl.EssentialOCLStandaloneSetup;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

import sample.SamplePackage;

public class Main {

	public static void main(String[] args) {
//		final String input = "model/sample.xmi";
//		final String transform = "transforms/test.qvto";

		final String input = "model/my.uml";
		final String transform = "transforms/test2.qvto";

		ResourceSet rs = new ResourceSetImpl();
		init(rs);

		try {
			System.out.println("Loading model " + input);
			Resource resource = rs.getResource(createFileURI(input), true);

			System.out.println("Input objects found: " + resource.getContents().size());
			System.out.println(">>>");
			for (EObject obj : resource.getContents()) {
				System.out.println(obj);
			}
			System.out.println("<<<");

			EObject model = resource.getContents().get(0);

			// Uncomment the following line and everything will work 
			//transformModel(rs, transform, model, 0);
			
			IntStream.rangeClosed(0, 10)
				.boxed()
				.collect(Collectors.toList())
				.parallelStream()
				.forEach(i -> {
					transformModel(rs, transform, model, i);
				});

			System.out.println("Done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void init(ResourceSet rs) {
		System.out.println("Initialization");
		rs.setURIConverter(new CustomURIConverter());

		System.out.println("  UML");
        UMLResourcesUtil.init(rs);
        
		System.out.println("  OCL");
		EssentialOCLStandaloneSetup.doSetup();
		UMLStandaloneSetup.init();

		System.out.println("  Ecore packages");
		SamplePackage.eINSTANCE.getEFactoryInstance();

		System.out.println("  Blackbox units");
		TransformationExecutor.BlackboxRegistry.INSTANCE.registerModules(UtilitiesLibrary.class);
	}

	private static URI createFileURI(String relativePath) {
		return URI.createFileURI(new File(relativePath).getAbsolutePath());
	}

	private static void transformModel(ResourceSet rs, String transform, EObject model, int i) {
		try {
			List<EObject> result = transformModel(rs, createFileURI(transform), model);

			System.out.println(String.format("(%d) Output objects created: %d", i, result.size()));
			System.out.println(">>>");
			for (EObject obj : result) {
				System.out.println(obj);
			}
			System.out.println("<<<");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<EObject> transformModel(ResourceSet rs, URI transformation, EObject model) throws Exception {
		TransformationExecutor executor = new TransformationExecutor(transformation);
		ExecutionContextImpl context = new ExecutionContextImpl();
		context.setConfigProperty("keepModeling", true);
		context.setLog(new WriterLog(new OutputStreamWriter(System.out)));
		EList<EObject> modelList = new BasicEList<EObject>();
		modelList.add(model);
		ModelExtent input = new BasicModelExtent(modelList);
		ModelExtent output = new BasicModelExtent();
		ExecutionDiagnostic result = executor.execute(context, input, output);
		if (result.getSeverity() == Diagnostic.OK) {
			return output.getContents();
		} else {
			IStatus status = BasicDiagnostic.toIStatus(result);
			for (IStatus error : status.getChildren()) {
				System.out.println("  " + error);
			}
			throw new Exception(status.getMessage());
		}
	}
}
