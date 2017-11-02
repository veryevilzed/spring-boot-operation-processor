# Operation Processor

## Configuration

```yaml

- name: process-name # Process Name
  FilterA:           # class FilterA extend Filter<TransactionObject>
    order: 2         # order
    field1: value1   # custom field1
    field2: value2   # custom field2
  FilterB:
    order: 1
    field1: value1
    field2: value2
    field3: value3
  FilterC:
    order: 0
    field1: value1
  ModificatorA:      # class ModificatorA extend Modificator<TransactionObject>
    field1: value1
  ModificatorB:
    field1: value1
  ActionA:           # class ActionA extend Action<TransactionObject>
    field1: value1
  ActionB:
    field1: value1

```

