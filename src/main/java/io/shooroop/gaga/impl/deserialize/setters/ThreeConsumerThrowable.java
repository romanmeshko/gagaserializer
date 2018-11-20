package io.shooroop.gaga.impl.deserialize.setters;

@FunctionalInterface
interface ThreeConsumerThrowable<F, U, V> {

    void accept(F f, U u, V v) throws Exception;

}
