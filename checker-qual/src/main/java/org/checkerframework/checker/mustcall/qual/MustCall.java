package org.checkerframework.checker.mustcall.qual;

import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TypeUseLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An expression of type {@code @MustCall({"m1", "m2"})} may be obligated to call {@code m1()}
 * and/or {@code m2()} before it is deallocated, but it is not obligated to call any other methods.
 *
 * <p>This annotation can be enforced by running the Resource Leak Checker. It enforces that the
 * methods {@code m1()} and {@code m2()} are called on the annotated expression before it is
 * deallocated.
 *
 * <p>The subtyping relationship is:
 *
 * <pre>{@code @MustCall({"m1"}) <: @MustCall({"m1", "m2"})}</pre>
 *
 * @checker_framework.manual #must-call-checker Must Call Checker
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@SubtypeOf({MustCallUnknown.class})
@DefaultQualifierInHierarchy
@DefaultFor({TypeUseLocation.EXCEPTION_PARAMETER, TypeUseLocation.UPPER_BOUND})
public @interface MustCall {
    /**
     * Methods that might need to be called on the expression whose type is annotated.
     *
     * @return methods that might need to be called
     */
    public String[] value() default {};
}
