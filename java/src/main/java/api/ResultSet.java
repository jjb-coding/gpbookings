package api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Intended to be applied to any parameter in an APIOutput subclass record that represents an
 * ArrayList<T extends OutputResult> i.e. that should be filled by the ORM from a result set.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT})
public @interface ResultSet {}