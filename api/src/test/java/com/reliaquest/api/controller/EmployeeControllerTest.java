package com.reliaquest.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void testGetAllEmployees() throws Exception {
        Employee expectedEmployee1 = new Employee(UUID.randomUUID(),
                "Joe Smith",
                100000,
                25,
                "Job Title 1",
                "joesmith@example.com");
        Employee expectedEmployee2 = new Employee(UUID.randomUUID(),
                "Jane Doe",
                100000,
                26,
                "Job Title 2",
                "janedoe@example.com");
        Employee expectedEmployee3 = new Employee(UUID.randomUUID(),
                "Joe Adams",
                100000,
                27,
                "Job Title 3",
                "joeadams@example.com");
        List<Employee> expectedEmployees = List.of(expectedEmployee1, expectedEmployee2, expectedEmployee3);
        when(employeeService.getAllEmployees()).thenReturn(ResponseEntity.ok(expectedEmployees));
        String response = mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Employee> actualEmployees = objectMapper.readValue(response, new TypeReference<>(){});
        assertEquals(3, actualEmployees.size());
        assertEquals(expectedEmployees, actualEmployees);
    }

    @Test
    void testGetEmployeesByNameSearch() throws Exception {
        Employee expectedEmployee1 = new Employee(UUID.randomUUID(),
                "Joe Smith",
                100000,
                25,
                "Job Title 1",
                "joesmith@example.com");
        Employee expectedEmployee2 = new Employee(UUID.randomUUID(),
                "Joe Adams",
                100000,
                25,
                "Job Title 1",
                "joeadams@example.com");
        List<Employee> expectedEmployees = List.of(expectedEmployee1, expectedEmployee2);
        when(employeeService.getEmployeesByNameSearch("Joe"))
                .thenReturn(ResponseEntity.ok(expectedEmployees));
        String response = mockMvc.perform(get("/api/v1/employees/search/Joe"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Employee> actualEmployees = objectMapper.readValue(response, new TypeReference<>(){});
        assertEquals(2, actualEmployees.size());
        assertEquals(expectedEmployees, actualEmployees);
    }

    @Test
    void testGetEmployeeById() throws Exception {
        Employee expectedEmployee = new Employee(UUID.randomUUID(),
                "Joe Smith",
                120000,
                27,
                "Job Title",
                "joe.smith@example.com");
        when(employeeService.getEmployeeById(expectedEmployee.id().toString()))
                .thenReturn(ResponseEntity.ok(expectedEmployee));
        String response = mockMvc.perform(get("/api/v1/employees/" + expectedEmployee.id().toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Employee actualEmployee = objectMapper.readValue(response, Employee.class);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    void testGetHighestSalaryOfEmployees() throws Exception {
        Integer expected = 123456789;
        when(employeeService.getHighestSalaryOfEmployees())
                .thenReturn(ResponseEntity.ok(expected));
        String response = mockMvc.perform(get("/api/v1/employees/highestSalary"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Integer actual = objectMapper.readValue(response, Integer.class);
        assertEquals(expected, actual);
    }

    @Test
    void testTopTenHighestEarningEmployeeNames() throws Exception {
        when(employeeService.getTopTenHighestEarningEmployeeNames())
                .thenReturn(ResponseEntity.ok(List.of("Anna", "Julia", "Sam")));
        mockMvc.perform(get("/api/v1/employees/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Anna"))
                .andExpect(jsonPath("$[1]").value("Julia"))
                .andExpect(jsonPath("$[2]").value("Sam"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("Jane Doe",
                100000,
                25,
                "Job Title");
        Employee expectedEmployee = new Employee(UUID.randomUUID(),
                createEmployeeRequest.name(),
                createEmployeeRequest.salary(),
                createEmployeeRequest.age(),
                createEmployeeRequest.title(),
                "janedoe@example.com");
        when(employeeService.createEmployee(createEmployeeRequest)).thenReturn(ResponseEntity.ok(expectedEmployee));
        String response = mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Employee actualEmployee = objectMapper.readValue(response, Employee.class);
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    void deleteEmployeeById() throws Exception {
        UUID employeeId = UUID.randomUUID();
        when(employeeService.deleteEmployeeById(employeeId.toString())).thenReturn(ResponseEntity.ok("John Doe"));
        String response = mockMvc.perform(delete("/api/v1/employees/" + employeeId.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals("John Doe", response);
    }
}

