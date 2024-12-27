package ru.kozelsk.alliance.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PartitionOfList {

    public static <T> List<List<T>> partition(List<T> list, int size) {
        if(list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        for(int i = 0; i < list.size(); i+=size) {
            result.add(list.subList(i, Math.min(i+size, list.size())));
        }
        return result;
    }

}
