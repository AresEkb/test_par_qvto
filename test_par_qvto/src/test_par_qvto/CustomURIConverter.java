package test_par_qvto;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

public class CustomURIConverter extends ExtensibleURIConverterImpl implements
        URIConverter {
    @Override
    public InputStream createInputStream(URI uri, Map<?, ?> options)
            throws IOException {
        System.out.println("  Loading " + uri);
        return super.createInputStream(uri, options);
    }
}
