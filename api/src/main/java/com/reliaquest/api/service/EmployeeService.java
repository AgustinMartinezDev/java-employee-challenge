package com.reliaquest.api.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeMockService {
    private final RestTemplate mockServerClient;

    private final static String API_BASE_URL = "/api/v1/employee";

    @Autowired
    public EmployeeMockService(RestTemplate mockServerClient) {
        this.mockServerClient = mockServerClient;
    }

    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(Arrays.asList(fetchAllEmployees().data().toArray(new Employee[0])));
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = fetchAllEmployees().data().stream()
                .filter(employee -> employee.name().contains(searchString))
                .toList();
        if (CollectionUtils.isEmpty(employees)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employees);
    }

    private EmployeeRequestDTO fetchAllEmployees() {
        return mockServerClient.getForObject(API_BASE_URL, EmployeeRequestDTO.class);
    }
}
