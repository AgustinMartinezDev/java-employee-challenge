package com.reliaquest.api.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.DeleteEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {
    private final RestTemplate mockServerClient;

    private final EmployeeSalaryComparator employeeSalaryComparator = new EmployeeSalaryComparator();

    private final static String API_BASE_URL = "/api/v1/employee";

    @Autowired
    public EmployeeService(RestTemplate mockServerClient) {
        this.mockServerClient = mockServerClient;
    }

    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(Arrays.asList(fetchAllEmployees().data().toArray(new Employee[0])));
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(final String searchString) {
        List<Employee> employees = fetchAllEmployees().data().stream()
                .filter(employee -> employee.name().contains(searchString))
                .toList();
        if (CollectionUtils.isEmpty(employees)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<Employee> getEmployeeById(final String id) {
        return ResponseEntity.ok(fetchEmployeeById(id));
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        List<Employee> employees = fetchAllEmployees().data();
        employees.sort(employeeSalaryComparator);
        return ResponseEntity.ok(employees.stream().findFirst().map(Employee::salary).get());
    }

    private EmployeeServerResponse<List<Employee>> fetchAllEmployees() {
        return mockServerClient.exchange(API_BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<EmployeeServerResponse<List<Employee>>>(){}).getBody();
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = fetchAllEmployees().data();
        employees.sort(employeeSalaryComparator);
        return ResponseEntity.ok(employees.stream().limit(10).map(Employee::name).toList());
    }

    public ResponseEntity<Employee> createEmployee(final CreateEmployeeRequest createEmployeeRequest) {
        EmployeeServerResponse<Employee> employee = mockServerClient.exchange(API_BASE_URL,
                HttpMethod.POST,
                new HttpEntity<>(createEmployeeRequest),
                new ParameterizedTypeReference<EmployeeServerResponse<Employee>>(){}).getBody();
        return ResponseEntity.ok(employee.data());
    }

    public ResponseEntity<String> deleteEmployeeById(final String id) {
        String employeeToDeleteByName = fetchEmployeeById(id).name();
        EmployeeServerResponse<Boolean> deletedEmployee = mockServerClient.exchange(API_BASE_URL,
                HttpMethod.DELETE,
                new HttpEntity<>(new DeleteEmployeeRequest(employeeToDeleteByName)),
                new ParameterizedTypeReference<EmployeeServerResponse<Boolean>>(){}).getBody();
        Boolean employeeWasDeleted = deletedEmployee.data();
        if (employeeWasDeleted) {
            return ResponseEntity.ok(employeeToDeleteByName);
        }
        return ResponseEntity.notFound().build();
    }

    private Employee fetchEmployeeById(String id) {
        String endpoint = API_BASE_URL + "/" + id;
        ResponseEntity<EmployeeServerResponse<Employee>> employeeServerResponse =
                mockServerClient.exchange(endpoint, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return employeeServerResponse.getBody().data();
    }

    private static class EmployeeSalaryComparator implements Comparator<Employee> {
        @Override
        public int compare(Employee e1, Employee e2) {
            return Integer.compare(e2.salary(), e1.salary());
        }
    }
}
