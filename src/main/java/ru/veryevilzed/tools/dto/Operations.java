package ru.veryevilzed.tools.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Operations<T> {

    List<Operation> operations;

    ObjectMapper mapper = new ObjectMapper();

    @Getter
    String defaultNamespace = "ru.veryevilzed.tools";

    private List<Operation> filtering(final T obj) {
        return operations.stream().filter(o -> o.filter(obj)).collect(Collectors.toList());
    }

    private void execution(final T obj, List<Operation> operations) {
        for(Operation o : operations) {
            T item = obj;
            item = o.modification(item);
            o.execution(item);
        }
    }

    /**
     * Выполнить действия связанные с операцией
     * @param item
     */
    public void exec(T item) {
         execution(item, filtering(item));
    }

    @SuppressWarnings("unchecked")
    private Operation getOperation(Map<String, Object> data, Class<T> clazz) {

        Operation res = new Operation();
        for(Map.Entry<String, Object> obj : data.entrySet()) {
            if (obj.getKey().equals("name")){
                res.setName((String)obj.getValue());
                continue;
            }

            Class clz = null;
            try {
                clz = Class.forName(obj.getKey());
            }catch (ClassNotFoundException ignored){ }

            if (clz == null) {
                try {
                    clz = Class.forName(String.format("%s.%s", defaultNamespace, obj.getKey()));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            ParameterizedType superClass = (ParameterizedType)clz.getGenericSuperclass();
            if (superClass.getActualTypeArguments()[0] != clazz){
                log.warn("Rules {} skipped. Transaction Class is not {}", obj.getKey(), clazz.getName());
                continue;
            }

            if (obj.getValue().getClass() == Map.class){
                log.warn("Rules {} skipped. Value is not Map.class", obj.getKey(), obj.getValue().getClass().getName());
                continue;
            }

            if (clz.getSuperclass() == Filter.class)
                res.getFilters().add((Filter<T>)mapper.convertValue(obj.getValue(), clz));

            if (clz.getSuperclass() == Action.class)
                res.getActions().add((Action<T>)mapper.convertValue(obj.getValue(), clz));

            if (clz.getSuperclass() == Modificator.class)
                res.getModificators().add((Modificator<T>)mapper.convertValue(obj.getValue(), clz));

        }
        return res;
    }

    public Operations(File file, Class<T> clazz) throws IOException {
        this(file, clazz, "ru.veryevilzed.tools");
    }

    public Operations(File file, Class<T> clazz, String defaultNamespace) throws IOException {
        this.defaultNamespace = defaultNamespace;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        operations = new ArrayList<>();
        List<Map<String, Object>> values = (List<Map<String, Object>>)mapper.readValue(file, List.class);
        operations = values.stream().map(o -> getOperation(o, clazz)).collect(Collectors.toList());
    }


    @Data
    private class Operation {



        String name;
        List<Filter<T>> filters = new ArrayList<>();
        List<Modificator<T>> modificators = new ArrayList<>();
        List<Action<T>> actions = new ArrayList<>();

        boolean filter(T item) {
            return filters.stream().allMatch(f -> f.filter(item));
        }

        T modification(T item) {
            for(Modificator<T> mod : modificators)
                item = mod.modificate(item);
            return item;
        }

        void execution(final T item) {
            for(Action<T> act : actions)
                act.action(item);
        }

    }

}
