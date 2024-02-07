package io.github.susimsek.springkafkasamples.util;

import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SupplierUtil {

    @FunctionalInterface
    @SuppressWarnings("java:S112")
    public interface SupplierWithException<T> {
        T get() throws Throwable;
    }

    public static <T> Supplier<T> rethrowSupplier(SupplierWithException<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable exception) {
                throwAsUnchecked(exception);
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Throwable exception) throws E {
        throw (E) exception;
    }

}