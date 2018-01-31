package ru.veryevilzed.tools.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Operations<T> {

    private static final String DEFAULT_NAMESPACE = "ru.veryevilzed.tools";

    private static final String NAME = "name";
    private static final String EMPTY = "";

    private List<Operation> operations = new LinkedList<>();

    private ObjectMapper mapper = new ObjectMapper();

    private String namespace;

    public Operations(File file, Class<T> clazz) throws IOException {
        this(file, clazz, DEFAULT_NAMESPACE);
    }

    @SuppressWarnings("unchecked")
    public Operations(File file, Class<T> clazz, String namespace) throws IOException {
        this.namespace = namespace;
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        List<Map<String, Object>> values = (List<Map<String, Object>>) yamlMapper.readValue(file, List.class);
        operations = values.stream().map(o -> loadOperation(o, clazz)).collect(Collectors.toList());
    }

    /**
     * Выполнить действия связанные с операцией
     */
    public void exec(T item) {
        executeAll(item, filterByAll(item));
    }

    private List<Operation> filterByAll(T obj) {
        return operations.stream().filter(o -> o.filter(obj)).collect(Collectors.toList());
    }

    private void executeAll(T obj, List<Operation> operationsToExecute) {
        for (Operation o : operationsToExecute) {
            T item = obj;
            item = o.modify(item);
            o.execute(item);
        }
    }

    @SuppressWarnings("unchecked")
    private Operation loadOperation(Map<String, Object> data, Class<T> operationPartParameter) {

        Operation operation = new Operation();

        for (Map.Entry<String, Object> entry : data.entrySet()) {

            if (entry.getKey().equals(NAME)) {
                operation.setName((String) entry.getValue());
                continue;
            }

            String key = entry.getKey();
            String suffix = "";
            if (key.contains("_")) {
                String[] parts = entry.getKey().split(Pattern.quote("_"), 2);
                key = parts[0];
                suffix = parts[1];
            }

            // Load operation part class
            Class operationPartClass = loadOperationPartImplementingClass(key);

            // Check parameter type for operation and it's superclass (they should match)
            ParameterizedType operationPartSuperclass = (ParameterizedType) operationPartClass.getGenericSuperclass();
            Type superclassParameter = operationPartSuperclass.getActualTypeArguments()[0];
            if (!superclassParameter.equals(operationPartParameter)) {
                log.warn(
                    "Rules for {} are skipped. Wrong parameterized type found {}<{}>, but {}<{}> required!",
                    key, operationPartClass, operationPartParameter, operationPartClass, superclassParameter
                );
                continue;
            }

            // Check value implementing class (should be java.util.Map instance)
            Class<?> valueClass = entry.getValue().getClass();
            if (!Map.class.isAssignableFrom(valueClass)) {
                log.warn(
                    "Rules for {} are skipped. Should have been mapped as instance of Map, but {} was found",
                    key, valueClass
                );
                continue;
            }

            // Fill the operation part data for specific entry
            if (Filter.class.isAssignableFrom(operationPartClass)) {
                Filter<T> o = (Filter<T>) mapper.convertValue(entry.getValue(), operationPartClass);
                o.setName((String) data.getOrDefault(NAME, EMPTY));
                o.setSuffix(suffix);
                operation.getFilters().add(o);
            } else if (Action.class.isAssignableFrom(operationPartClass)) {
                Action<T> o = (Action<T>) mapper.convertValue(entry.getValue(), operationPartClass);
                o.setName((String) data.getOrDefault(NAME, EMPTY));
                o.setSuffix(suffix);
                operation.getActions().add(o);
            } else if (Modifier.class.isAssignableFrom(operationPartClass)) {
                Modifier<T> o = (Modifier<T>) mapper.convertValue(entry.getValue(), operationPartClass);
                o.setName((String) data.getOrDefault(NAME, EMPTY));
                o.setSuffix(suffix);
                operation.getModifiers().add(o);
            } else {
                log.warn("Unsupported operation part type: {}", operationPartClass);
            }
        }
        return operation;
    }

    private Class loadOperationPartImplementingClass(String key) {

        // Try to load class with the specified name (probably with namespace/package)
        try {
            return Class.forName(key);
        } catch (ClassNotFoundException ignore) { /**/ }

        // In case of failure try to load with the specified namespace/package
        try {
            return Class.forName(namespace + "." + key);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    private class Operation {

        private String name;
        private List<Filter<T>> filters = new LinkedList<>();
        private List<Modifier<T>> modifiers = new LinkedList<>();
        private List<Action<T>> actions = new LinkedList<>();

        boolean filter(T item) {
            return filters.stream().allMatch(f -> f.filter(item));
        }

        T modify(T item) {
            for (Modifier<T> mod : modifiers) {
                item = mod.modify(item);
            }
            return item;
        }

        void execute(T item) {
            for (Action<T> act : actions) {
                act.action(item);
            }
        }
    }
}
