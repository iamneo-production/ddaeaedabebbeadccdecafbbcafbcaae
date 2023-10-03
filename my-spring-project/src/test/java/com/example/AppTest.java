package com.example;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ContextConfiguration(classes = { TestConfig.class })
public class AppTest {

    private MockMvc mockMvc;

    @Mock
    private EmpDao empDao;

    @InjectMocks
    private EmpController empController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(empController).build();
    }

    @Test
    public void testShowIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testGetEmployees() {
        List<Emp> expectedEmpList = new ArrayList<>();

        Emp emp1 = new Emp();
        emp1.setId(1);
        emp1.setName("John");
        emp1.setSalary(50000.0);
        emp1.setDesignation("Manager");

        Emp emp2 = new Emp();
        emp2.setId(2);
        emp2.setName("Alice");
        emp2.setSalary(60000.0);
        emp2.setDesignation("Developer");

        expectedEmpList.add(emp1);
        expectedEmpList.add(emp2);

        // Mock the behavior of empDao.getEmployees method
        when(empDao.getEmployees()).thenReturn(expectedEmpList);

        List<Emp> actualEmpList = empDao.getEmployees();

        assertEquals(expectedEmpList, actualEmpList);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Emp empToCreate = new Emp();
        empToCreate.setName("John");
        empToCreate.setSalary(50000.0);
        empToCreate.setDesignation("Manager");

        // Mock the behavior of empDao.save method
        when(empDao.save(any(Emp.class))).thenReturn(1);

        mockMvc.perform(post("/save")
                .flashAttr("emp", empToCreate))
                .andExpect(redirectedUrl("/viewwemp"));
    }

    @Test
    public void testReadEmployees() throws Exception {
        // Create expected employee objects
        Emp emp1 = new Emp();
        emp1.setId(1);
        emp1.setName("John");
        emp1.setSalary(50000.0);
        emp1.setDesignation("Manager");

        Emp emp2 = new Emp();
        emp2.setId(2);
        emp2.setName("Alice");
        emp2.setSalary(60000.0);
        emp2.setDesignation("Developer");

        List<Emp> expectedEmpList = new ArrayList<>();
        expectedEmpList.add(emp1);
        expectedEmpList.add(emp2);

        // Mock the behavior of empDao.getEmployees method
        when(empDao.getEmployees()).thenReturn(expectedEmpList);

        mockMvc.perform(get("/viewwemp"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewemp"))
                .andExpect(model().attribute("list", expectedEmpList));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Emp empToUpdate = new Emp();
        empToUpdate.setId(1);
        empToUpdate.setName("John");
        empToUpdate.setSalary(60000.0);
        empToUpdate.setDesignation("Senior Manager");

        when(empDao.update(any(Emp.class))).thenReturn(1);

        mockMvc.perform(post("/editsave")
                .flashAttr("emp", empToUpdate))
                .andExpect(redirectedUrl("/viewwemp"));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        int empIdToDelete = 1;

        when(empDao.delete(eq(empIdToDelete))).thenReturn(1);

        mockMvc.perform(get("/deleteemp/{id}", empIdToDelete))
                .andExpect(redirectedUrl("/viewwemp"));
    }

}
