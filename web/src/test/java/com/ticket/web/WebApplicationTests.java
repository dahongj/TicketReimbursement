package com.ticket.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.ticket.web.controller.ReimburseController;
import com.ticket.web.exception.TicketNotFoundException;
import com.ticket.web.models.Ticket;
import com.ticket.web.models.Users;
import com.ticket.web.repository.TicketRepository;
import com.ticket.web.repository.UsersRepository;
import com.ticket.web.service.TicketService;
import com.ticket.web.service.UserService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@SpringBootTest
class WebApplicationTests {
	ApplicationContext applicationContext;

    @BeforeEach
    public void setUp(){
        String[] args = new String[] {};
        applicationContext = SpringApplication.run(WebApplication.class, args);
    }

    @AfterEach
    public void tearDown(){
        SpringApplication.exit(applicationContext);
    }

	@Test
    public void getReimburseControllerBean(){
        ReimburseController bean = applicationContext.getBean(ReimburseController.class);
        Assertions.assertNotNull(bean);
    }

    @Test
    public void getTicketServiceBean(){
        TicketService bean = applicationContext.getBean(TicketService.class);
        Assertions.assertNotNull(bean);
    }

    @Test
    public void getUserServiceBean(){
        UserService bean = applicationContext.getBean(UserService.class);
        Assertions.assertNotNull(bean);
    }

    @Test
    public void getTicketRepositoryBean(){
        TicketRepository bean = applicationContext.getBean(TicketRepository.class);
        Assertions.assertNotNull(bean);
    }

    @Test
    public void getUserRepositoryBean(){
        UsersRepository bean = applicationContext.getBean(UsersRepository.class);
        Assertions.assertNotNull(bean);
    }

	@Test
	public void ticketRepositoryIsRepositoryTest() throws ReflectiveOperationException {
        TicketRepository repository = applicationContext.getBean(TicketRepository.class);
        Method[] repositoryMethods = repository.getClass().getMethods();
        Method saveMethod = null;
        Method findAllMethod = null;
        Integer expectedAmount = 123;
        String expectedDescription = "This is my description.";
		Integer expectedUser = 552;

        Ticket testTicket = new Ticket(expectedAmount, expectedDescription,expectedUser);
        for(Method m : repositoryMethods){
            System.out.println(m.getName());
            if(m.getName().equals("save") && m.getParameterCount() == 1){
                saveMethod = m;
            }else if(m.getName().equals("findAll") && m.getParameterCount() == 0){
                findAllMethod = m;
            }
        }
        if(saveMethod == null || findAllMethod == null){
            Assertions.fail("The save / findAll methods were not found. Ensure that TicketRepository properly " +
                    "extends JPARepository.");
        }
        List<Ticket> TicketList1 = (List<Ticket>) findAllMethod.invoke(repository, new Object[]{});
        System.out.println(TicketList1);
        // Assertions.assertTrue(TicketList1.size() == 0, "There should be no accounts in the " +
        //         "JPARepository on startup.");
        // Ticket actualTicket = (Ticket) saveMethod.invoke(repository, testTicket);
        // Assertions.assertEquals(actualTicket.getAmount(), expectedAmount);
        // List<Ticket> ticketList2 = (List<Ticket>) findAllMethod.invoke(repository, new Object[]{});
        // Assertions.assertTrue(ticketList2.size() > 0, "The ticket should be addable to the " +
        //         "JPARepository.");
    }

	@Test
    public void UsersRepositoryIsRepositoryTest() throws ReflectiveOperationException{
        UsersRepository repository = applicationContext.getBean(UsersRepository.class);
        Method[] repositoryMethods = repository.getClass().getMethods();
        Method saveMethod = null;
        Method findAllMethod = null;
        String username = "ted test 1";
		String password = "123";

        Users testUsers = new Users(username,password);
        for(Method m : repositoryMethods){
            System.out.println(m.getName());
            if(m.getName().equals("save") && m.getParameterCount() == 1){
                saveMethod = m;
            }else if(m.getName().equals("findAll") && m.getParameterCount() == 0){
                findAllMethod = m;
            }
        }
        if(saveMethod == null || findAllMethod == null){
            Assertions.fail("The save / findAll methods were not found. Ensure that UsersRepository properly " +
                    "extends JPARepository.");
        }
        List<Users> usersList1 = (List<Users>) findAllMethod.invoke(repository, new Object[]{});
        System.out.println(usersList1);
        // Assertions.assertTrue(usersList1.size() == 0, "There should be no users in the " +
        //         "JPARepository on startup.");
        // Users actualUsers = (Users) saveMethod.invoke(repository, testUsers);
        // Assertions.assertEquals(actualUsers.getPassword(), password);
        // List<Users> userList2 = (List<Users>) findAllMethod.invoke(repository, new Object[]{});
        // Assertions.assertTrue(userList2.size() > 0, "The users should be addable to the " +
        //         "JPARepository.");
    }

	@Test
    public void default404Test() throws IOException, InterruptedException {
        HttpClient webClient = HttpClient.newHttpClient();
        int random = (int) (Math.random()*100000);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/arbitrary"+random))
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assertions.assertEquals(404, status);
        String body = response.body().toString();
        Assertions.assertTrue(body.contains("timestamp"));
        Assertions.assertTrue(body.contains("status"));
        Assertions.assertTrue(body.contains("error"));
        Assertions.assertTrue(body.contains("path"));
    }

	@Test
	void contextLoads() {
	}

}
