package com.csbaic.edatope.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionStreamUtils {

    /**
     * 将list转为map
     * @param collection
     * @param key
     * @param <T>
     * @return
     */
    public static  <K, V> Map<K, V> toMap(Collection<V> collection, Function<V, K> key ){
        return collection.stream().collect(Collectors.toMap(key, Function.identity()));
    }

    /**
     * map操作
     * @param collection
     * @param fun
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> mapList(Collection<T> collection, Function<T, R> fun){
        return collection.stream().map(fun).collect(Collectors.toList());
    }

    /**
     * map操作
     * @param collection
     * @param fun
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Set<R> mapSet(Collection<T> collection, Function<T, R> fun){
        return collection.stream().map(fun).collect(Collectors.toSet());
    }

}
