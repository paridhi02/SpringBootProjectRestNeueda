# Prompt: Writing Spring Boot Controller Tests for the Employee REST API

## Project Context

This is a **Spring Boot REST API** project (`com.neueda.rest`) that manages `Employee` records via a standard CRUD controller.

### Key Classes

| Class | Role |
|---|---|
| `Employee` | Entity with fields `id` (int), `name` (String), `department` (String), `salary` (int) |
| `EmpController` | `@RestController` mapped to `/employees`; delegates to `EmpService` |
| `EmpService` | `@Service` layer; delegates to `EmpRepository` |
| `EmpRepository` | Data-access layer using Spring JDBC |
| `GlobalExceptionHandler` | Handles `EmployeeNotFoundException` globally |

### `EmpController` Response Shape

Every endpoint wraps its result in a `Map<String, Object>` and returns a `ResponseEntity`. The **exact map keys** used in the controller are:

```
GET  /employees        → key "Messagge : "   (message)   + key "Employess: "  (list)
POST /employees        → key "Message:"      (message)   + key "Employee:"    (object)
GET  /employees/{id}   → key "Message:"      (message)   + key "Employee"     (object)
PUT  /employees/{id}   → key "Message: "     (message)   + key "Employee : "  (object)
DELETE /employees/{id} → key "Message :"     (message)   + key "Message:"     (object)
```

> ⚠️ Note: the map keys contain typos and irregular spacing — match them exactly in `jsonPath` assertions.

---

## Test Setup Rules

### Correct Import for `@WebMvcTest`
```java
// ✅ Correct
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

// ❌ Wrong (does not exist)
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
```

### Mock the Service Layer
```java
@MockitoBean          // Spring Boot 3.4+
EmpService empService;
```

### `Employee` Constructor
```java
// Available constructor (no id — id is auto-assigned by the repository)
new Employee(String name, String department, int salary)
```

---

## Correct Test Template

```java
@WebMvcTest(EmpController.class)
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmpService empService;

    @Test
    void shouldReturnAllEmployees() throws Exception {

        List<Employee> list = List.of(
                new Employee("John", "IT", 50000)
        );

        when(empService.getAllEmployees()).thenReturn(list);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                // Use the EXACT key from the controller, including the trailing space
                .andExpect(jsonPath("$['Employess: '][0].name").value("John"))
                .andExpect(jsonPath("$['Employess: '][0].department").value("IT"))
                .andExpect(jsonPath("$['Employess: '][0].salary").value(50000));
    }

    @Test
    void shouldAddEmployee() throws Exception {
        String requestBody = """
                {"name":"Jane","department":"HR","salary":60000}
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['Message:']").value("Employee addded successfully"));
    }

    @Test
    void shouldGetEmployeeById() throws Exception {
        Employee emp = new Employee("John", "IT", 50000);
        emp.setId(1);

        when(empService.getEmployeeById(1)).thenReturn(emp);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Employee.name").value("John"));
    }
}
```

---

## Common Mistakes to Avoid

| Mistake | Fix |
|---|---|
| `jsonPath("$.data[0].name()")` | `jsonPath("$['Employess: '][0].name")` — key has typo + trailing space; `.name` not `.name()` |
| `jsonPath("$.name()")` | `jsonPath("$.name")` — JSONPath does not use method-call syntax |
| Wrong `@WebMvcTest` import | Use `org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest` |
| Asserting key `"data"` | The controller never uses `"data"` as a key |
| Asserting key `"message"` | Actual keys include typos like `"Messagge : "` — copy exactly from the controller |

---

## Instructions for Copilot

When generating or fixing tests for this project:

1. Always use the exact response map keys from `EmpController` (copy-paste them, do not normalize).
2. Use `jsonPath("$['key with spaces'][…].field")` bracket-notation when the key contains spaces or special characters.
3. Never use `name()` or method-call syntax inside a JSONPath expression.
4. Mock only `EmpService` — never mock `EmpRepository` directly in controller tests.
5. Use `@MockitoBean` (Spring Boot 3.4+), not `@MockBean` (deprecated).
6. The `Employee` class does **not** use Lombok — getters/setters are hand-written; fields are package-private.

