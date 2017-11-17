package io.github.squat_team.performance.peropteryx.environment.uriconverter;

import org.eclipse.emf.common.util.URI;

/**
 * Created by Fabian on 07.06.2016.
 */
public interface URIConverterInterceptor {
    /**
     * Determines whether this URI converter can convert the given URI.
     * <p>
     * Returning true results in the {@link URIConverterInterceptor#convert(URI)} method to be called.
     */
    boolean canConvert(URI uri);

    /**
     * Converts the given URI.
     */
    URI convert(URI uri);
}
